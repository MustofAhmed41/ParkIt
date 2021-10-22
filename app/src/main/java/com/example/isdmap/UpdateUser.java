package com.example.isdmap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.isdmap.Models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateUser extends AppCompatActivity  {

    private AutoCompleteTextView carType;
    private TextInputEditText name, phone, carModel, carColor, carNumber;
    private User mUser;
    private LoadingDataDialog mLoadingDialog;
    private Button mUpdateBtn;
    private String mUserId;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        mLoadingDialog = new LoadingDataDialog(UpdateUser.this);
        mLoadingDialog.startLoadingDialog();
        initializeVariables(savedInstanceState);
        loadFirebaseData();



    }



    private void initializeVariables(Bundle savedInstanceState) {

        name = findViewById(R.id.update_user_name);
        phone = findViewById(R.id.update_user_phone);
        carColor = findViewById(R.id.update_user_car_color);
        carModel = findViewById(R.id.update_user_car_model);
        carNumber = findViewById(R.id.update_user_car_number);
        mUpdateBtn = findViewById(R.id.update_user_update_btn);
        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingDialog.startLoadingDialog();
                mUser.setName(name.getText().toString());
                mUser.setPhone(phone.getText().toString());
                mUser.setCarBrand(carModel.getText().toString());
                mUser.setCarColor(carColor.getText().toString());
                mUser.setCarNumber(carNumber.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("user").child(mUserId).
                        setValue(mUser);
                mLoadingDialog.dismissDialog();

                Intent intent = new Intent(UpdateUser.this, DashboardUserActivity.class);
                startActivity(intent);

            }
        });

    }

    private void loadFirebaseData() {

        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("user").child(mUserId).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            mUser = snapshot.getValue(User.class);
                            name.setText(mUser.getName());
                            phone.setText(mUser.getPhone());
                            carModel.setText(mUser.getCarBrand());
                            carColor.setText(mUser.getCarColor());
                            carNumber.setText(mUser.getCarNumber());
                        }
                        mLoadingDialog.dismissDialog();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}