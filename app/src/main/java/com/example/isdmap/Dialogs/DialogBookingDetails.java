package com.example.isdmap.Dialogs;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.isdmap.Models.Booking;
import com.example.isdmap.Models.User;
import com.example.isdmap.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DialogBookingDetails extends AppCompatDialogFragment {

    private Booking booking;
    private User user;
    public DialogBookingDetails(Booking booking, User user) {
        this.booking = booking;
        this.user = user;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_booking_details, null);
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

    private void initializeViews(View view) {

        TextView carNumber = view.findViewById(R.id.dialog_booking_details_car_number);
        TextView carBrand = view.findViewById(R.id.dialog_booking_details_car_brand);
        TextView carColor = view.findViewById(R.id.dialog_booking_details_car_color);
        TextView startTime = view.findViewById(R.id.dialog_booking_details_car_start_time);
        TextView endTime = view.findViewById(R.id.dialog_booking_details_car_end_time);
        TextView cost = view.findViewById(R.id.dialog_booking_details_car_cost);
        Button dismissBtn = view.findViewById(R.id.dialog_booking_details_dismiss_btn);

        carNumber.setText(user.getCarNumber());
        carBrand.setText(user.getCarBrand());
        carColor.setText(user.getCarNumber());
        startTime.setText(booking.getStartTime());
        endTime.setText(booking.getEndTime());
        cost.setText(booking.getCost() + " Tk");

        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }
}
