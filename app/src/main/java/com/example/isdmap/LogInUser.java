package com.example.isdmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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


public class LogInUser extends AppCompatActivity {

    TextView userlogin,userForgotPassword;
    TextInputEditText loginUserEmail,loginUserPass;
    Button button;
    ProgressBar progressBar;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_user);
        loginUserEmail= findViewById(R.id.userEmail);
        loginUserPass= findViewById(R.id.userPass);
        button= findViewById(R.id.loginUser);
        userForgotPassword = findViewById(R.id.userforgotPass);
        auth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("PARK IT");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userForgotPassword.setOnClickListener(new View.OnClickListener() {
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
                                Toast.makeText(LogInUser.this,"Reset Link Sent To Your Email.",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LogInUser.this,"Error ! Reset Link is Not Sent "+e.getMessage(),Toast.LENGTH_SHORT).show();

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


        userlogin = findViewById(R.id.gotouserregister);
        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInUser.this,RegisterUser.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = loginUserEmail.getText().toString();
                String userPassword = loginUserPass.getText().toString();

                if(TextUtils.isEmpty(userEmail))
                {
                    loginUserEmail.setError("Email is Required.");
                    return;
                }
                if(TextUtils.isEmpty(userPassword))
                {
                    loginUserPass.setError("Password is Required.");
                    return;
                }
                if(userPassword.length()<6)
                {
                    loginUserPass.setError("Password must be more than 6 characters.");
                    return;
                }
                auth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(LogInUser.this,"Login is successfully",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LogInUser.this, DashboardUserActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                        else
                        {
                            Toast.makeText(LogInUser.this,"Try Again",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

    }
}