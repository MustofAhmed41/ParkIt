package com.example.isdmap.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.isdmap.Models.Booking;
import com.example.isdmap.Models.User;
import com.example.isdmap.R;

public class DialogCarArrivalDetails extends AppCompatDialogFragment {

    private TextView mCarNumber;
    private TextView mCarBrand;
    private TextView mCarColor;
    private TextView mPhoneNumber;
    private TextView mExpectedArrivalTime;
    private Button mDismissBtn;
    private Button mConfirmBtn;

    private String mBookingKey;
    private Booking mBooking;
    private User user;

    private DialogCarArrivalDetailsBtn mListener;

    public DialogCarArrivalDetails(String bookingKey, Booking booking, User user) {
        this.mBookingKey = bookingKey;
        this.mBooking = booking;
        this.user = user;
    }


    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_car_arrival_details, null);
        builder.setView(view);
        builder.setCancelable(false);

        initializeViews(view, mBooking, user);

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

    private void initializeViews(View view, Booking booking, User user) {

        mCarNumber = view.findViewById(R.id.arrival_details_car_number);
        mCarBrand = view.findViewById(R.id.arrival_details_car_brand);
        mCarColor = view.findViewById(R.id.arrival_details_car_color);
        mPhoneNumber = view.findViewById(R.id.arrival_details_phone_number);
        mExpectedArrivalTime = view.findViewById(R.id.arrival_details_expected_arrival_time);
        mDismissBtn = view.findViewById(R.id.arrival_details_dismiss_btn);
        mConfirmBtn = view.findViewById(R.id.arrival_details_confirm_btn);

        mCarNumber.setText(user.getCarNumber());
        mCarBrand.setText(user.getCarBrand());
        mCarColor.setText(user.getCarColor());
        mPhoneNumber.setText(user.getPhone());
        mExpectedArrivalTime.setText(booking.getStartTime());

        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onCarArrivalButtonClicked(booking,user);
                Toast.makeText(getContext(), "Arrived", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        mDismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onCarDismissButtonClicked(booking,user);
                Toast.makeText(getContext(), "Clear", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

    }

    public interface DialogCarArrivalDetailsBtn {
        void onCarArrivalButtonClicked(Booking booking, User user);
        void onCarDismissButtonClicked(Booking booking, User user);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (DialogCarArrivalDetailsBtn) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }




}
