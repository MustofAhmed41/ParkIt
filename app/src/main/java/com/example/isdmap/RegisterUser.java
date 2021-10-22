package com.example.isdmap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isdmap.Models.Owner;
import com.example.isdmap.Models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

public class RegisterUser extends AppCompatActivity {
    private AutoCompleteTextView autoCompleteTextView;
    private TextView userregister;
    private TextInputEditText inputName,inputEmail,inputPhone,inputCarBrand,inputCarNumber,inputCarColor,inputPassword,inputConfirmPass;
    private Button register;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String carType;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        inputName = findViewById(R.id.name);
        inputEmail = findViewById(R.id.email);
        inputPhone = findViewById(R.id.phone);
        inputCarBrand = findViewById(R.id.model);
        inputCarNumber = findViewById(R.id.number);
        inputCarColor = findViewById(R.id.color);
        inputPassword = findViewById(R.id.password);
        inputConfirmPass=findViewById(R.id.confirmPass);
        register=findViewById(R.id.btn);
        progressDialog= new ProgressDialog(this);
        auth =FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        getSupportActionBar().setTitle("PARK IT");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        reference = FirebaseDatabase.getInstance().getReference("user");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perforAuth();
            }

        });
        autoCompleteTextView = findViewById(R.id.usercartype);

        String []option = {"Car" , "Bike" , "Microbus" , "Pickup"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this , R.layout.option_item , option);
        autoCompleteTextView.setText(arrayAdapter.getItem(0).toString() , false);
        autoCompleteTextView.setAdapter(arrayAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                carType =(String)arrayAdapter.getItem(position);
                //carType.setText((Integer) arrayAdapter.getItem(position));
            }
        });


        userregister = findViewById(R.id.gotouserlogin);
        userregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterUser.this,LogInUser.class);
                startActivity(intent);


            }
        });
    }


    private void perforAuth() {
        String name = inputName.getText().toString();
        String email = inputEmail.getText().toString();
        String phone = inputPhone.getText().toString();
        String carBrand= inputCarBrand.getText().toString();
        String carColor = inputCarColor.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmPass = inputConfirmPass.getText().toString();
        String carNumber = inputCarNumber.getText().toString();

        if(!email.matches(emailPattern))
        {
            inputEmail.setError("Enter correct Email");
        }
        else if(password.isEmpty() || password.length()<6)
        {
            inputPassword.setError("Enter Proper Password");
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
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        String bookingStatus = "nothing";
                        String key = task.getResult().getUser().getUid();
                        User obj = new User(name,email,phone,carBrand,carNumber,carColor,password,carType,bookingStatus);
                        reference.child(key).setValue(obj);
                        sendUserToNextActivity();
                        Toast.makeText(RegisterUser.this,"Registration SuccessFull",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterUser.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(RegisterUser.this, DashboardUserActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}