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

import com.example.isdmap.Models.User;
import com.example.isdmap.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DialogUserDetails extends AppCompatDialogFragment {

    private User mUser;
    private String mUserId;
    public DialogUserDetails(String user) {
        mUserId = user;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_user_details, null);
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

        Button dismissBtn = view.findViewById(R.id.dialog_user_dismiss_btn);
        TextView name = view.findViewById(R.id.dialog_user_name);
        TextView phoneNumber = view.findViewById(R.id.dialog_user_phone_number);
        TextView email = view.findViewById(R.id.dialog_user_email);
        TextView carBrand = view.findViewById(R.id.dialog_user_car_brand);
        TextView carNumber = view.findViewById(R.id.dialog_user_car_number);
        TextView carColor = view.findViewById(R.id.dialog_user_car_color);
        TextView carType = view.findViewById(R.id.dialog_user_car_type);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("user").child(mUserId).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mUser = snapshot.getValue(User.class);

                        name.setText(mUser.getName());
                        phoneNumber.setText(mUser.getPhone());
                        email.setText(mUser.getEmail());
                        carBrand.setText(mUser.getCarBrand());
                        carType.setText(mUser.getCarType());
                        carNumber.setText(mUser.getCarNumber());
                        carColor.setText(mUser.getCarColor());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }
}
