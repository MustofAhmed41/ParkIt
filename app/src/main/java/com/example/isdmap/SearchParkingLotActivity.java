package com.example.isdmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.example.isdmap.Dialogs.DialogBottomSheetBooking;
import com.example.isdmap.Dialogs.DialogOwnerDetails;
import com.example.isdmap.Models.Booking;
import com.example.isdmap.Models.Owner;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.snapshot.Index;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

public class SearchParkingLotActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        DialogBottomSheetBooking.BottomSheetBookingInfo {

    boolean isPermissionGranted;
    ArrayList<Owner> ownerArrayList;
    ArrayList<String> ownerIdList;

    private GoogleMap mGoogleMap;
    private FloatingActionButton fab;
    private FusedLocationProviderClient mLocationClient;
    private LoadingDataDialog mLoadingDialog;

    private LatLng userLatlng;
    private int mFreeSlotNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.fab);
        mLoadingDialog = new LoadingDataDialog(SearchParkingLotActivity.this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });

        checkMyPermission();
        initMap();

        mLocationClient = new FusedLocationProviderClient(this);
    }


    private void initMap() {
        if (isPermissionGranted) {
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
            supportMapFragment.getMapAsync(this);
        }
    }


    private void getCurrentLocation() {
        mLoadingDialog.startLoadingDialog();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mLoadingDialog.dismissDialog();
            return;
        }

        mLocationClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Location location = task.getResult();
                if(location!=null) {
                    float zoom_amount = 13;
                    gotoLocation(location.getLatitude(), location.getLongitude(), zoom_amount);
                    userLatlng = new LatLng(location.getLatitude(), location.getLongitude());

                    mGoogleMap.addMarker(new MarkerOptions().position(
                            new LatLng( userLatlng.latitude, userLatlng.longitude))
                            .title("My Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
                            .showInfoWindow();

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    database.getReference().child("owner").
                            addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ownerArrayList = new ArrayList<>();
                            ownerIdList = new ArrayList<>();
                            for (DataSnapshot data : snapshot.getChildren()){
                                Owner owner = data.getValue(Owner.class);
                                if(owner!=null){
                                    ownerArrayList.add(owner);
                                    ownerIdList.add(data.getKey());
                                }else{
                                    Toast.makeText(SearchParkingLotActivity.this, "Import" +
                                            " failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                            mLoadingDialog.dismissDialog();
                            for(int i = 0 ; i < ownerArrayList.size(); i++){
                                double lat = Double.parseDouble(ownerArrayList.get(i).getLatitude());
                                double lon = Double.parseDouble(ownerArrayList.get(i).getLongitude());

                                if(distance(lat, lon, userLatlng.latitude, userLatlng.longitude,'K')<5){
                                    mGoogleMap.addMarker(new MarkerOptions().position(
                                            new LatLng( lat, lon))
                                            .title(ownerArrayList.get(i).getParkingName())).showInfoWindow();
                                }

                            }
                            mLoadingDialog.dismissDialog();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            mLoadingDialog.dismissDialog();
                        }
                    });

                }
            }else{
                mLoadingDialog.dismissDialog();
            }
        });
    }


    private void gotoLocation(double latitude, double longitude, float zoom) {
        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,zoom);
        mGoogleMap.moveCamera(cameraUpdate);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void checkMyPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPermissionGranted = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(),"");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }

        }).check();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mLoadingDialog.startLoadingDialog();
        mGoogleMap = googleMap;
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if(marker.getTitle().equals("My Location")){
                    mLoadingDialog.dismissDialog();
                    return false;
                }

                Owner owner = null;
                String ownerId = "";
                for( int i = 0; i < ownerArrayList.size(); i++){
                    if(ownerArrayList.get(i).getParkingName().equals(marker.getTitle())){
                        owner = ownerArrayList.get(i);
                        ownerId = ownerIdList.get(i);
                        break;
                    }
                }

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                String finalOwnerId = ownerId;
                database.getReference().child("owner").child(ownerId).
                        addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (!snapshot.exists()){
                                    return;
                                }

                                Owner owner = snapshot.getValue(Owner.class);
                                mFreeSlotNumber = -1;
                                for(int i = 0; i < owner.getSlotList().size() ; i++){
                                    if(owner.getSlotList().get(i).equals("empty")){
                                        mFreeSlotNumber = i+1;
                                        break;
                                    }
                                }

                                if(mFreeSlotNumber==-1){
                                    Toast.makeText(SearchParkingLotActivity.this, "No Empty Slots", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                DialogOwnerDetails dialog = new DialogOwnerDetails(owner, finalOwnerId, mFreeSlotNumber,getSupportFragmentManager());
                                dialog.show(getSupportFragmentManager(), "MapActivity");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                return false;
            }
        });
        mLoadingDialog.dismissDialog();
        getCurrentLocation();

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    @Override
    public void onBottomSheetBookBtnClicked(Owner owner, String ownerId, String startTime,
                                            String endTime, String duration, int freeSlotNumber,
                                            double cost) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        String key = myRef.child("booking").push().getKey();
        String status = "booked";

        Booking booking = new Booking(ownerId, FirebaseAuth.getInstance().getCurrentUser()
                .getUid(), startTime, endTime, duration,
                freeSlotNumber+"", status, cost+"",key);
        myRef.child("booking").child(key).setValue(booking);
        freeSlotNumber--;

        myRef.child("owner").child(ownerId).child("slotList").child(freeSlotNumber+"").
                setValue("booked");

        myRef.child("owner-booking").child(ownerId).child(freeSlotNumber+"").
                setValue(key);

        myRef.child("user").child(FirebaseAuth.getInstance().getUid()).
                child("bookingStatus").setValue(key);

        Intent intent = new Intent(this, DashboardUserActivity.class);
        startActivity(intent);
    }


}