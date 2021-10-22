package com.example.isdmap;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogInOwner extends AppCompatActivity {
    TextView ownerlogin,ownerForgotPass;
    TextInputEditText loginOwnerEmail,loginOwnerPass;
    Button button;
    ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_owner);
        ownerlogin = findViewById(R.id.gotoownerregister);
        loginOwnerEmail = findViewById(R.id.emailOwner);
        loginOwnerPass= findViewById(R.id.loginOwnerPass);
        button= findViewById(R.id.loginOwner);
        auth = FirebaseAuth.getInstance();
        ownerForgotPass = findViewById(R.id.OwnerforgotPassword);

        getSupportActionBar().setTitle("PARK IT");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ownerForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        auth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LogInOwner.this,"Reset Link Sent To Your Email.",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LogInOwner.this,"Error ! Reset Link is Not Sent "+e.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();


            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ownerEmail = loginOwnerEmail.getText().toString();
                String ownerPassword = loginOwnerPass.getText().toString();

                if(TextUtils.isEmpty(ownerEmail))
                {
                    loginOwnerEmail.setError("Email is Required.");
                    return;
                }
                if(TextUtils.isEmpty(ownerPassword))
                {
                    loginOwnerPass.setError("Password is Required.");
                    return;
                }
                if(ownerPassword.length()<6)
                {
                    loginOwnerPass.setError("Password must be more than 6 characters.");
                    return;
                }
                auth.signInWithEmailAndPassword(ownerEmail,ownerPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("owner").child(uid);
                            Toast.makeText(LogInOwner.this,"Login successfully",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LogInOwner.this, DashboardOwnerActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                        else
                        {
                            Toast.makeText(LogInOwner.this,"Try Again",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

        ownerlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInOwner.this,OwnerRegistrationMap.class);
                startActivity(intent);

            }
        });
    }


}