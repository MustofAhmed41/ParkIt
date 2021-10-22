package com.example.isdmap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class ReportBugActivity extends AppCompatActivity {
    TextInputEditText edittext;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_bug);
        edittext = (TextInputEditText) findViewById(R.id.edittext);
        button = (Button)findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subject = edittext.getText().toString().trim();
                String email = "fatemamarjan40@gmail.com";
                if (subject.isEmpty()){
                //    Toast.makeText(com.example.parkitnavi.ReportBugActivity.this, "Please add Subject", Toast.LENGTH_SHORT).show();
                }
                else {
                    String mail = "mailto:" + email +"?&subject="+Uri.encode(subject)+"&body="+Uri.encode(subject);
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse(mail));
                    try{
                        startActivity(Intent.createChooser(intent,"Send Email"));
                    }
                    catch (Exception e){
                       // Toast.makeText(com.example.parkitnavi.ReportBugActivity.this, "Exception:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}