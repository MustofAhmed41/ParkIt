package com.example.isdmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isdmap.Models.Owner;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;


public class RegisterOwner extends AppCompatActivity {
    TextView ownerregister;
    TextInputEditText inputOwnerName,inputOwnerEmail,inputOwnerPhone,inputOwnerPassword,inputConfirmPass, parkingName;
    TextInputEditText inputParkingAddress ,inputNumberOfSlots,inputParkingName,inputCostPerHour,latitude,longitude,ownerSlots;
    Button register;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    private ArrayList<String> slotList = new ArrayList<String>();
    private ArrayList<String> ownerBookingList = new ArrayList<String>();
    private LoadingDataDialog mLoadingDialog;
    boolean isPermissionGranted;
    private double mLatitude,mLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_owner);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mLatitude = extras.getDouble("lat");
            mLongitude = extras.getDouble("long");

        }

        initializeViews();
        checkMyPermission();
        getSupportActionBar().setTitle("PARK IT");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perforAuth();
            }

        });

        ownerregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterOwner.this,LogInOwner.class);
                startActivity(intent);
            }
        });
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



    private void initializeViews() {

        mLoadingDialog = new LoadingDataDialog(RegisterOwner.this);
        mLoadingDialog.startLoadingDialog();
        inputOwnerName= findViewById(R.id.ownerName);
        inputOwnerEmail = findViewById(R.id.ownerEmail);
        inputOwnerPhone = findViewById(R.id.ownerPhone);
        inputOwnerPassword = findViewById(R.id.ownerpas);
        inputConfirmPass=findViewById(R.id.ownerconfirmpas);
        inputParkingAddress  = findViewById(R.id.register_owner_address);
        inputNumberOfSlots = findViewById(R.id.slotNo);
        inputCostPerHour=findViewById(R.id.cost);
        register=findViewById(R.id.ownerReg);
        parkingName=findViewById(R.id.register_owner_parking_name);
        progressDialog= new ProgressDialog(this);
        auth =FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        ownerregister = findViewById(R.id.gotoownerlogin);
        reference = FirebaseDatabase.getInstance().getReference("owner");
        mLoadingDialog.dismissDialog();
    }

    private void perforAuth() {
        String ownerName = inputOwnerName.getText().toString();
        String ownerEmail = inputOwnerEmail.getText().toString();
        String ownerPhone = inputOwnerPhone.getText().toString();
        String password= inputOwnerPassword.getText().toString();
        String confirmPass = inputConfirmPass.getText().toString();
        String parkingAddress = inputParkingAddress .getText().toString();
        String numberOfSlots =  inputNumberOfSlots.getText().toString();
        String costPerHour =  inputCostPerHour.getText().toString();
        String parking =  parkingName.getText().toString();


        if(!ownerEmail.matches(emailPattern))
        {
            inputOwnerEmail.setError("Enter correct Email");
        }
        else if(TextUtils.isEmpty(ownerEmail))
        {
            inputOwnerEmail.setError("Email is Required.");

        }
        else if(TextUtils.isEmpty(password))
        {
            inputOwnerPassword.setError("Password is Required.");

        }
        else if(TextUtils.isEmpty(ownerPhone))
        {
            inputOwnerPhone.setError("Phone is Required.");

        }
        else if(TextUtils.isEmpty(parkingAddress))
        {
            inputParkingAddress.setError("Parking Address is Required.");

        }
        else if(TextUtils.isEmpty(numberOfSlots))
        {
            inputNumberOfSlots.setError("number Of Slots is Required.");

        }
        else if(TextUtils.isEmpty(costPerHour))
        {
            inputCostPerHour.setError("cost Per Hour is Required.");

        }
        else if(password.isEmpty() || password.length()<6)
        {
            inputOwnerPassword.setError("Enter Proper Password");
        }
        else if(!password.equals(confirmPass))
        {
            inputConfirmPass.setError("Password Not match Both feild");
        }
        else
        {
            progressDialog.setMessage("Please wait while Registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            auth.createUserWithEmailAndPassword(ownerEmail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        String key = task.getResult().getUser().getUid();
                        for(int i=0;i<Integer.parseInt(numberOfSlots);i++)
                        {
                            slotList.add("empty");
                        }

                        Owner obj = new Owner(ownerName,ownerEmail,ownerPhone,password,
                                numberOfSlots,parking,parkingAddress,costPerHour,
                                mLatitude+"",mLongitude+"",slotList);
                        reference.child(key).setValue(obj);

                        ownerBookingList = new ArrayList<>();
                        for(int i=0;i<Integer.parseInt(numberOfSlots);i++)
                        {
                            ownerBookingList.add("none");
                        }
                        
                        FirebaseDatabase.getInstance().getReference().child("owner-booking").
                                child(key).setValue(ownerBookingList);

                        sendUserToNextActivity();
                        Toast.makeText(RegisterOwner.this,"Registration SuccessFull",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterOwner.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(RegisterOwner.this, LogInOwner.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}