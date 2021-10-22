package com.example.isdmap.Dialogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.isdmap.Models.Booking;
import com.example.isdmap.R;
import com.example.isdmap.ownerShowUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OwnerBookingInfo extends AppCompatDialogFragment {


    private Booking mUser;
    private String mUserId,mBookId;
    public OwnerBookingInfo() {
    }
    public OwnerBookingInfo(String book,String user) {

        mUserId = user;
        mBookId= book;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_owner_booking_info, null);
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

        Button dismissBtn = view.findViewById(R.id.dialog_owner_dismiss_btn);
        Button seeUser = view.findViewById(R.id.seeUser);
        TextView cost = view.findViewById(R.id.dialog_cost);
        TextView status = view.findViewById(R.id.status);
        TextView start = view.findViewById(R.id.start);
        TextView end = view.findViewById(R.id.end);
        TextView duration =view.findViewById(R.id.duration);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        System.out.println(mBookId);
        database.getReference().child("booking").child(mBookId).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mUser = snapshot.getValue(Booking.class);
                        cost.setText(mUser.getCost() + " Tk");
                        status.setText(mUser.getBookingStatus());
                        start.setText(mUser.getStartTime());
                        end.setText(mUser.getEndTime());
                        duration.setText(mUser.getDuration());


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
        seeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent= new Intent(getActivity(), ownerShowUser.class);
                myIntent.putExtra("key",mUserId);
                startActivity(myIntent);
            }
        });


    }
}