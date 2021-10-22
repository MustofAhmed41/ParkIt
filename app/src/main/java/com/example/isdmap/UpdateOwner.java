package com.example.isdmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.isdmap.Models.Owner;
import com.example.isdmap.Models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateOwner extends AppCompatActivity {

    private TextInputEditText mName, mPhone, mParkingName, mAddress, mCostPerHour;
    private Owner mOwner;
    private LoadingDataDialog mLoadingDialog;
    private Button mUpdateBtn;
    private String mUserId;

    private FusedLocationProviderClient fusedLocationClient;
    private String mLatitude,mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_owner);

        mLoadingDialog = new LoadingDataDialog(UpdateOwner.this);
        mLoadingDialog.startLoadingDialog();
        initializeVariables();
        loadFirebaseData();
        updateLocation();
    }


    private void updateLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mLatitude = location.getLatitude() + "";
                            mLongitude = location.getLongitude() + "";
                            System.out.println(mLatitude + "  " + mLongitude);

                        }

                        mLoadingDialog.dismissDialog();
                    }
                });

    }

    private void initializeVariables() {

        mName = findViewById(R.id.update_owner_name);
        mPhone = findViewById(R.id.update_owner_phone);
        mParkingName = findViewById(R.id.update_owner_parking_name);
        mAddress = findViewById(R.id.update_owner_address);
        mCostPerHour = findViewById(R.id.update_owner_cost);
        mUpdateBtn = findViewById(R.id.update_owner_update_btn);
        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingDialog.startLoadingDialog();
                updateLocation();
                mOwner.setOwnerName(mName.getText().toString());
                mOwner.setOwnerPhone(mPhone.getText().toString());
                mOwner.setParkingAddress(mAddress.getText().toString());
                mOwner.setParkingName(mParkingName.getText().toString());
                mOwner.setCostPerHour(mCostPerHour.getText().toString());
                mOwner.setLatitude(mLatitude);
                mOwner.setLongitude(mLongitude);
                FirebaseDatabase.getInstance().getReference().child("owner").child(mUserId).
                        setValue(mOwner);
                mLoadingDialog.dismissDialog();
         //       Toast.makeText(UpdateOwner.this, "Done", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateOwner.this, DashboardOwnerActivity.class);
                startActivity(intent);

            }
        });

    }


    private void loadFirebaseData() {

        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("owner").child(mUserId).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            mOwner = snapshot.getValue(Owner.class);
                            mName.setText(mOwner.getOwnerName());
                            mPhone.setText(mOwner.getOwnerPhone());
                            mAddress.setText(mOwner.getParkingAddress());
                            mParkingName.setText(mOwner.getParkingName());
                            mCostPerHour.setText(mOwner.getCostPerHour());
                        }
                        mLoadingDialog.dismissDialog();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}