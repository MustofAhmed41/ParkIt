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
import androidx.fragment.app.FragmentManager;

import com.example.isdmap.Dialogs.DialogBottomSheetBooking;
import com.example.isdmap.Models.Owner;
import com.example.isdmap.R;

public class DialogOwnerDetails extends AppCompatDialogFragment {

    private Owner mOwner;
    private String mOwnerId;
    private FragmentManager supportFragmentManager;
    private int mFreeSlotNumber;

    public DialogOwnerDetails(Owner owner, String ownerId, int freeSlotNumber, FragmentManager supportFragmentManager) {
        this.mOwner = owner;
        this.mOwnerId = ownerId;
        this.supportFragmentManager = supportFragmentManager;
        this.mFreeSlotNumber = freeSlotNumber;
        //getEmptySlotNumber(ownerId);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_owner_details, null);
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
        Button bookingButton = view.findViewById(R.id.dialog_owner_book_btn);
        Button dismissButton = view.findViewById(R.id.dialog_owner_dismiss_btn);
        TextView address = view.findViewById(R.id.dialog_owner_address);
        TextView phone = view.findViewById(R.id.dialog_owner_phone_number);
        TextView cost = view.findViewById(R.id.dialog_owner_cost);
        TextView vanacy = view.findViewById(R.id.dialog_owner_vacancy);
        TextView parkingName = view.findViewById(R.id.dialog_owner_parking_name);
        address.setText(mOwner.getParkingAddress());
        phone.setText(mOwner.getOwnerPhone());
        cost.setText(mOwner.getCostPerHour() + " Tk");
        parkingName.setText(mOwner.getParkingName());
        bookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dismiss();
                DialogBottomSheetBooking bottomSheet = new DialogBottomSheetBooking(mOwner,
                        mOwnerId, mFreeSlotNumber);
                bottomSheet.show(supportFragmentManager, "exampleBottomSheet");
            }
        });

        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dismiss();
            }
        });
    }




}
