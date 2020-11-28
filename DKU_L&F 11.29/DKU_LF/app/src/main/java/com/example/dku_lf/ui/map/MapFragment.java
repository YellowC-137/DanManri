package com.example.dku_lf.ui.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.dku_lf.LocationActivity;
import com.example.dku_lf.R;
import com.example.dku_lf.ui.models.LocationModel;
import com.example.dku_lf.ui.models.MarkerModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.model.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "MapFragment";
    private MapView mapView;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FusedLocationProviderClient mFLProvider;
    private String postid, postname, posttype;
    private LocationModel locationModel;
    private MarkerModel markerModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFLProvider = LocationServices.getFusedLocationProviderClient(getActivity());

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView)layout.findViewById(R.id.Map_view);
        mapView.getMapAsync(this);

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //액티비티가 처음 생성될 때 실행되는 함수

        if(mapView != null)
        {
            mapView.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        final Map<String,LocationModel.locatonInfo> locMap = new HashMap<>();
        LatLng DKU = new LatLng(37.32187140504299, 127.12675029768137);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DKU, 17.0f));
        final MarkerOptions mOptions = new MarkerOptions();
        final CollectionReference Locref= mStore.collection("Post_locations");

        if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){ // 권한 체크
            googleMap.setMyLocationEnabled(true);
        }
        else {
            // Toast.makeText(getActivity(),"GPS 권한이 거부된 상태입니다.",Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.detach(this).attach(this).commit();
        }



        Locref.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> shot = document.getData();
                                String Docuid = document.getId();
                                Double latitude = document.getDouble("latitude");
                                Double longitude = document.getDouble("longitude");
                                String posttype = (String) document.get("posttype");
                                String posttitle = (String) document.get("posttitle");

                                mOptions.title(posttitle);
                                mOptions.position(new LatLng(latitude, longitude));
                                Log.d(TAG, "가져오기 :"+Docuid +" : "+ new LatLng(latitude, longitude) +" : "+posttype +" : "+postname);
                                // 마커(핀) 추가
                                googleMap.addMarker(mOptions);

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });










    }

}
