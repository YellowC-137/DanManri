package com.example.dku_lf.ui.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dku_lf.R;
import com.example.dku_lf.database.FirebaseID;
import com.example.dku_lf.ui.home.found.FoundPostActivity;
import com.example.dku_lf.ui.home.lost.LostPostActivity;
import com.example.dku_lf.ui.models.LocationModel;
import com.example.dku_lf.ui.models.MarkerModel;
import com.example.dku_lf.ui.models.Post;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{
    private static final String TAG = "MapFragment";
    private MapView mapView;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FusedLocationProviderClient mFLProvider;
    private String postid, postname, posttype;
    private LocationModel locationModel;
    private MarkerModel markerModel;
    private List<Post> mDatas;
    private CollectionReference foundref = mStore.collection("post_found");
    private CollectionReference lostref = mStore.collection("post");
    private static String type;
    private static String document_id;
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://lostnfound-3024f.appspot.com/");

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

    public void setMapToolbarEnabled (boolean enabled){

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

        Locref.whereEqualTo("posttype", "found").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                markerModel = new MarkerModel(document.getDouble("latitude"), document.getDouble("longitude"), document.getId(), (String) document.get("posttype"));

                                mOptions.position(new LatLng(markerModel.getLat(), markerModel.getLng()))
                                        .title(markerModel.getType()+"@"+markerModel.getTitle()).snippet("클릭시 게시글로 이동")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                                // 마커(핀) 추가
                                googleMap.addMarker(mOptions);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        Locref.whereEqualTo("posttype", "lost").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                markerModel = new MarkerModel(document.getDouble("latitude"), document.getDouble("longitude"), document.getId(), (String) document.get("posttype"));

                                mOptions.position(new LatLng(markerModel.getLat(), markerModel.getLng()))
                                        .title(markerModel.getType()+"@"+markerModel.getTitle()).snippet("클릭시 게시글로 이동")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                                // 마커(핀) 추가
                                googleMap.addMarker(mOptions);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                final View view = getActivity().getLayoutInflater().inflate(R.layout.item_windowinfo, null);
                TextView text = view.findViewById(R.id.texttype);
                TextView info = view.findViewById(R.id.infoText);
                text.setText(type.toUpperCase());
                info.setText(marker.getSnippet());

                return view;
            }
        });

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                if(type.equals("lost")){
                    Intent in = new Intent(getActivity(),LostPostActivity.class);
                    in.putExtra(FirebaseID.documentId,document_id);
                    startActivity(in);
                }
                else{
                    Intent in = new Intent(getActivity(),FoundPostActivity.class);
                    in.putExtra(FirebaseID.documentId,document_id);
                    startActivity(in);
                }

            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                StringTokenizer DocumentInfo = new StringTokenizer(marker.getTitle(), "@");
                type = DocumentInfo.nextToken();
                document_id = DocumentInfo.nextToken();

                return false;
            }
        });


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
