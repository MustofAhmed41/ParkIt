package com.example.isdmap.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.isdmap.Models.Owner;
import com.example.isdmap.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DialogBookingInformation  extends AppCompatDialogFragment {

    private String mStartingTime, mEndingTime, mOwnerPhone, mCostPerHour, mBookingKey,
    mUser, mSlotNumber, mOwnerId, mCost;

    public interface BookingCompletedInfo{
        void onBookingDone(String mBookingKey, double cost, String time, String currentTime, String mSlotNumber, String mOwnerId);
    }

    private BookingCompletedInfo mListener;


    public DialogBookingInformation(String ownerId, String slotNumber, String mUser,
                                    String bookingKey, String startingTime, String endingTime,
                                    String ownerPhone, String costPerHour,
                                    String cost){
        this.mOwnerId = ownerId;
        this.mSlotNumber = slotNumber;
        this.mUser = mUser;
        this.mBookingKey = bookingKey;
        this.mStartingTime = startingTime;
        this.mEndingTime = endingTime;
        this.mOwnerPhone = ownerPhone;
        this.mCostPerHour = costPerHour;
        this.mCost = cost;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (DialogBookingInformation.BookingCompletedInfo) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_booking_information, null);
        builder.setView(view);

        initializeViews(view);

        AlertDialog alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        // makes background transparent
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            // removes title from dialog.
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            WindowManager.LayoutParams windowManager = window.getAttributes();
            // positions the dialog in bottom|center
            windowManager.gravity = Gravity.CENTER;
            // window.getAttributes().windowAnimations = R.style.DialogAnimationSliding;
            window.getAttributes().windowAnimations = R.style.DialogBottomAnimationSliding;

        }

        return alertDialog;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initializeViews(View view) {
        Button dismissBtn = view.findViewById(R.id.dialog_booking_dismiss_btn);
        Button checkoutBtn = view.findViewById(R.id.dialog_booking_checkout_btn);
        TextView startingTime = view.findViewById(R.id.dialog_booking_starting_time);
        TextView expectedEndingTime = view.findViewById(R.id.dialog_booking_expected_ending_time);
        TextView currentCost = view.findViewById(R.id.dialog_booking_current_cost);
        TextView ownerPhone = view.findViewById(R.id.dialog_booking_owner_phone);
        startingTime.setText(mStartingTime);
        expectedEndingTime.setText(mEndingTime);
        ownerPhone.setText(mOwnerPhone);
        currentCost.setText(mCost + " Tk");

        String currentTime = getCurrentTime();

        String time = calculateTimeDifference(mStartingTime, currentTime);
        String[] timeArray = time.split(":");
        int hours = Integer.parseInt(timeArray[0]);
        int minutes = Integer.parseInt(timeArray[1]);
        double totalCost = hours*Double.parseDouble(mCostPerHour) +
                ((minutes*Double.parseDouble(mCostPerHour))/60);
        System.out.println(time);
        System.out.println(totalCost);
        String str = String.format("%1.2f", totalCost);
        totalCost = Double.valueOf(str);
        currentCost.setText(totalCost+"");
        final double cost = totalCost;
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBookingDone(mBookingKey,cost, time,currentTime,mSlotNumber,mOwnerId);
                dismiss();
            }
        });

        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

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

}
