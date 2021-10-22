package com.example.isdmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.isdmap.Fragments.TimePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        calculateTimeDifference();
    }

    private void calculateTimeDifference() {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss aa");
        Date date1 = null;
        try {
            date1 = format.parse("08:00:00 pm");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = format.parse("05:30:00 pm");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long millis = date1.getTime() - date2.getTime();
        int hours = (int) (millis / (1000 * 60 * 60));
        int mins = (int) ((millis / (1000 * 60)) % 60);

        String diff = hours + ":" + mins;
        Toast.makeText(this, diff, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String AM_PM = " am";
        String mm_precede = "";
        if (hourOfDay >= 12) {
            AM_PM = " pm";
            if (hourOfDay >=13 && hourOfDay < 24) {
                hourOfDay -= 12;
            }
            else {
                hourOfDay = 12;
            }
        } else if (hourOfDay == 0) {
            hourOfDay = 12;
        }
        if (minute < 10) {
            mm_precede = "0";
        }

        String time = "";
        if(hourOfDay<=9){
            time += "0";
        }
        time += (hourOfDay + ":");
        if(minute<10){
            time += "0";
        }
        time += (minute + ":");
        time += "00" + AM_PM ;

    }

}