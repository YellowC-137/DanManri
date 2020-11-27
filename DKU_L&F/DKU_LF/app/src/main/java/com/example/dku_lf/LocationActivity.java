package com.example.dku_lf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dku_lf.database.FirebaseID;
import com.example.dku_lf.database.UserAppliaction;
import com.example.dku_lf.ui.home.found.FoundPostActivity;
import com.example.dku_lf.ui.home.found.FoundWritingActivity;
import com.example.dku_lf.ui.models.LocationModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "LocationActivity";
    private GoogleMap googleMap; //구분해야하는지?
    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private String postid, postname,posttype,posttitle;
    private LocationModel locationModel;
    private FusedLocationProviderClient mFLProvider; //현재위치 이용
    private static int REQUEST_CODE_PERMISSION = 2020;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        fragmentManager = getFragmentManager();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.location_frag);
        mapFragment.getMapAsync(this);

        postid = getIntent().getStringExtra("postuid");
        posttitle = getIntent().getStringExtra("posttitle");
        posttype = getIntent().getStringExtra("posttype");
        postname = getIntent().getStringExtra("postname");
        Log.d(TAG,"아디 " + postid + " 이름 "+postname + " 타입 "+ posttype);

        Button myloc = (Button) findViewById(R.id.mapmyloc);

        mFLProvider = LocationServices.getFusedLocationProviderClient(this);


        myloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                        .PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(LocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSION);
                }
                //함수 순서 수정필요힘!
                mFLProvider.getLastLocation().addOnSuccessListener(LocationActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng mylocation = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 17.5f));

                        }
                    }

                });
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                    .PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(LocationActivity.this, "권한 거부", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(final GoogleMap googlemap) {
        LatLng DKU = new LatLng(37.32187140504299, 127.12675029768137);
        googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(DKU, 17.0f));

        if (ContextCompat.checkSelfPermission(LocationActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            googlemap.setMyLocationEnabled(true);
        }
        else {
            Toast.makeText(LocationActivity.this,"권한 거부됨",Toast.LENGTH_LONG).show();
        }



        googlemap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();
                Map<String,String> LocMap = new HashMap<>();
                LocationModel.locatonInfo locatonInfo = new LocationModel.locatonInfo();

                // 마커 타이틀

                mOptions.snippet(postname);

                Double latitude = point.latitude; // 위도
                Double longitude = point.longitude; // 경도
                // 마커의 스니펫(간단한 텍스트) 설정
                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude, longitude));
                // 마커(핀) 추가
                googlemap.addMarker(mOptions);

                locatonInfo.latitude = latitude;
                locatonInfo.longitude = longitude;
                locatonInfo.posttype = posttype;
                locatonInfo.postUid=postid;

                mStore.collection("Post_locations").document(postid).set(locatonInfo, SetOptions.merge());
                UserAppliaction.user_name=postname;

                if (posttype=="lost")
                {

                }
                if (posttype=="found")
                {

                }
                finish();
            }
        });

    }
}