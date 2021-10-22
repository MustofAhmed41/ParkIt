package com.example.isdmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDataDialog {

    private Activity activity;
    private AlertDialog dialog;

    LoadingDataDialog(Activity activity){
        this.activity = activity;
    }

    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_custom_loading, null));
        builder.setCancelable(true);
        //builder.setCancelable(false); // for not dismissable
        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog(){
        dialog.dismiss();
    }

}
