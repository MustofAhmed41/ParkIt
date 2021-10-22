package com.example.isdmap.Dialogs;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.isdmap.Models.Owner;
import com.example.isdmap.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DialogBottomSheetBooking extends BottomSheetDialogFragment {
    private Owner mOwner;
    private String mOwnerId;
    private int freeSlotNumber;

    public DialogBottomSheetBooking(Owner owner, String ownerId, int mFreeSlotNumber) {
        this.mOwner = owner;
        this.mOwnerId = ownerId;
        this.freeSlotNumber = mFreeSlotNumber;
    }

    private BottomSheetBookingInfo mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_bottomsheet_booking, container, false);
        initializeViews(v);
        return v;
    }

    private void initializeViews(View v) {

        EditText startTime = v.findViewById(R.id.dialog_bottomsheet_start_time);
        EditText endTime = v.findViewById(R.id.dialog_bottomsheet_end_time);
        Button bookBtn = v.findViewById(R.id.dialog_bottomsheet_book_btn);

        FloatingActionButton floatingActionButtonStartTime = v.findViewById(R.id.dialog_booking_time_start_time_fab);
        FloatingActionButton floatingActionButtonEndTime = v.findViewById(R.id.dialog_booking_time_end_time_fab);

        floatingActionButtonStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeFromTimePicker(getContext(),startTime);
            }
        });
        floatingActionButtonEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeFromTimePicker(getContext(),endTime);
            }
        });


        bookBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(startTime.getText().toString().equals("") ||
                        endTime.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String time = calculateTimeDifference(startTime.getText().toString(),
                       endTime.getText().toString());

                String[] timeArray = time.split(":");
                int hours = Integer.parseInt(timeArray[0]);
                int minutes = Integer.parseInt(timeArray[1]);

                int check = checkUserInput(startTime.getText().toString());
                if(check==0) {
                    return;
                }else {
                    double totalCost = hours * Double.parseDouble(mOwner.getCostPerHour()) +
                            ((minutes * Double.parseDouble(mOwner.getCostPerHour())) / 60);
                    dismiss();
                    mListener.onBottomSheetBookBtnClicked(mOwner, mOwnerId,
                            startTime.getText().toString(), endTime.getText().toString(), time,
                            freeSlotNumber, totalCost);
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private int checkUserInput(String starTime) {
        String currenTime = getCurrentTime();
        String time = calculateTimeDifference(currenTime,starTime);
        String[] timeArray = time.split(":");
        int hours = Integer.parseInt(timeArray[0]);
        int minutes = Integer.parseInt(timeArray[1]);
        if(hours==0){
            return 1;
        }
        Toast.makeText(getContext(), "Cannot Book at that time so early", Toast.LENGTH_SHORT).show();
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getCurrentTime() {
        Calendar c = Calendar.getInstance();;
        android.icu.text.SimpleDateFormat df = new android.icu.text.SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        String convertedTime = "";
        try {
            android.icu.text.SimpleDateFormat _24HourSDF = new android.icu.text.SimpleDateFormat("HH:mm");
            android.icu.text.SimpleDateFormat _12HourSDF = new android.icu.text.SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(formattedDate);
            convertedTime = _12HourSDF.format(_24HourDt).substring(0,5);
            convertedTime += ":00 ";
            convertedTime +=  _12HourSDF.format(_24HourDt).substring(6,8).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertedTime;
    }


    public static void setTimeFromTimePicker(Context context, EditText editText) {

        int hourOfDay, minute;

        final Calendar c = Calendar.getInstance();
        hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog dpd = new TimePickerDialog(context, (timePicker, hourOfDay1, minute1) -> {

            Time time = new Time(hourOfDay1, minute1, 0);

            SimpleDateFormat simpleDateFormat = new
                    SimpleDateFormat("hh:mm aa", Locale.getDefault());
            String s = simpleDateFormat.format(time);
            s = convertTime(s);
            editText.setText(s);
        }, hourOfDay, minute, false);

        dpd.show();
    }

    private static String convertTime(String s) {
        String part1, part2;
        part1 = s.substring(0,5);
        part2 = s.substring(5,8);
        part1 += ":00";
        System.out.println(part1+part2.toLowerCase());
        return part1+part2.toLowerCase();
    }

    private String calculateTimeDifference(String s1, String s2) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss aa");
        Date date1 = null;
        try {
            date1 = format.parse(s2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = format.parse(s1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long millis = date1.getTime() - date2.getTime();
        int hours = (int) (millis / (1000 * 60 * 60));
        int mins = (int) ((millis / (1000 * 60)) % 60);

        String diff = hours + ":" + mins;
        //Toast.makeText(this, diff, Toast.LENGTH_SHORT).show();
        return diff;
    }


    public interface BottomSheetBookingInfo {
        void onBottomSheetBookBtnClicked(Owner owner, String ownerId, String startTime,
                String endTime, String duration, int freeSlotNumber,double cost);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetBookingInfo) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }

}
