package com.example.isdmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isdmap.Dialogs.DialogBookingDetails;
import com.example.isdmap.Dialogs.DialogCarArrivalDetails;
import com.example.isdmap.Dialogs.DialogOwnerDetails;
import com.example.isdmap.Models.Booking;
import com.example.isdmap.Models.Owner;
import com.example.isdmap.Models.SlotOwner;
import com.example.isdmap.Models.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardOwnerActivity extends AppCompatActivity implements DialogCarArrivalDetails.DialogCarArrivalDetailsBtn {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private boolean mReadFirstTime;
    private ArrayList<SlotOwner> slotOwnerArrayList;
    private LoadingDataDialog mLoadingDialog;
    DatabaseReference ownerReference;
    String owner;
    View view;
    TextView email,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_owner);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        navigationView = findViewById(R.id.navigation);
        drawer = findViewById(R.id.drawer);

        //icon color change
        navigationView.setItemIconTintList(null);


        view = navigationView.getHeaderView(0);
        email = view.findViewById(R.id.email);
        name = view.findViewById(R.id.name);

        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation);
        drawer = findViewById(R.id.drawer);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        //icon color change
        navigationView.setItemIconTintList(null);

        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer.closeDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.Aboutus:
                        Intent intent0 = new Intent(DashboardOwnerActivity.this,AboutUsActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.Sendabug:
                        Intent intent1 = new Intent(DashboardOwnerActivity.this,ReportBugActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.settings:
                        Intent intent2 = new Intent(DashboardOwnerActivity.this,UpdateOwner.class);
                        startActivity(intent2);
                        break;

                    case R.id.LogOut:
                        auth.signOut();
                        Intent myIntent= new Intent(DashboardOwnerActivity.this, OpenActivity.class);
                        startActivity(myIntent);
                        break;
                }
                return true;
            }
        });
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        ownerReference = FirebaseDatabase.getInstance().getReference("owner").child(uid);

        ownerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Owner parina= snapshot.getValue(Owner.class);
                owner = parina.getOwnerName();
                System.out.println(owner);

                name.setText(owner);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        String UserEmail = user.getEmail();
        email.setText(UserEmail);


        attachBookingListener();
    }

    private void attachBookingListener() {

        mReadFirstTime = true;
        FirebaseDatabase.getInstance().getReference("owner").child(user.getUid()).child("slotList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()) {
                    return;
                }
                if (mReadFirstTime) {
                    slotOwnerArrayList = new ArrayList<>();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        SlotOwner slotOwner = new SlotOwner(dataSnapshot.getKey(),
                                dataSnapshot.getValue(String.class));
                        slotOwnerArrayList.add(slotOwner);

                    }
                    //reading first time
                    mReadFirstTime = false;

                } else {
                    int counter = 0;
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                        if(slotOwnerArrayList.get(counter).getText2().equals("empty") && dataSnapshot.getValue(String.class).equals("booked")){
                            slotOwnerArrayList.get(counter).setText2(dataSnapshot.getValue(String.class));

                            FirebaseDatabase.getInstance().getReference().child("owner-booking")
                                    .child(user.getUid()).child(counter+"").
                                    addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            if(!snapshot.exists()){
                                                return;
                                            }

                                            String key = snapshot.getValue(String.class);
                                            openCarArrivalDialog(key);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }else if( slotOwnerArrayList.get(counter).getText2().equals("arrived") && dataSnapshot.getValue(String.class).equals("empty") ){

                            slotOwnerArrayList.get(counter).setText2(dataSnapshot.getValue(String.class));
                            showCheckOutDialog(counter);
                        }
                        else{
                            slotOwnerArrayList.get(counter).setText2(dataSnapshot.getValue(String.class));
                        }
                        counter++;
                    }
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void showCheckOutDialog(int counter) {
        mLoadingDialog.startLoadingDialog();
        FirebaseDatabase.getInstance().getReference().child("owner-booking")
                .child(user.getUid()).child(counter+"").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(!snapshot.exists()){
                            mLoadingDialog.dismissDialog();
                            return;
                        }

                        String key = snapshot.getValue(String.class);
                        FirebaseDatabase.getInstance().getReference().child("booking").child(key)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(!snapshot.exists()){
                                            mLoadingDialog.dismissDialog();
                                            return;
                                        }

                                        Booking booking = snapshot.getValue(Booking.class);
                                        FirebaseDatabase.getInstance().getReference().child("user").
                                                child(booking.getUserId()).
                                                addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(!snapshot.exists()){
                                                            mLoadingDialog.dismissDialog();
                                                            return;
                                                        }
                                                        mLoadingDialog.dismissDialog();
                                                        User user = snapshot.getValue(User.class);
                                                        DialogBookingDetails dialog = new DialogBookingDetails(booking,user);
                                                        dialog.show(getSupportFragmentManager(),"NULL");

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });



                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void openCarArrivalDialog(String bookingKey){

        mLoadingDialog = new LoadingDataDialog(DashboardOwnerActivity.this);
        mLoadingDialog.startLoadingDialog();
        FirebaseDatabase.getInstance().getReference().child("booking").child(bookingKey)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists()){
                            mLoadingDialog.dismissDialog();
                            return;
                        }

                        Booking booking = snapshot.getValue(Booking.class);
                        FirebaseDatabase.getInstance().getReference().child("user").
                                child(booking.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(!snapshot.exists()){
                                    mLoadingDialog.dismissDialog();
                                    return;
                                }
                                User user = snapshot.getValue(User.class);

                                DialogCarArrivalDetails dialog = new DialogCarArrivalDetails(bookingKey, booking, user);
                                dialog.show(getSupportFragmentManager(), "MapActivity");
                                mLoadingDialog.dismissDialog();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void gotoParkingSlot(View v) {
        Intent myIntent= new Intent(DashboardOwnerActivity.this, parkingSlotActivity.class);
        startActivity(myIntent);
    }

    public void gotoOwnerHistory(View v) {
        Intent myIntent= new Intent(DashboardOwnerActivity.this, historyActivity.class);
        startActivity(myIntent);
    }

    public void gotoBugReport(View view) {
        Intent myIntent= new Intent(DashboardOwnerActivity.this, ReportBugActivity.class);
        startActivity(myIntent);
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public void gotoUpdateprofile(View view) {
        Intent myIntent= new Intent(DashboardOwnerActivity.this, UpdateOwner.class);
        startActivity(myIntent);
    }
    @Override
    public void onCarArrivalButtonClicked(Booking booking, User user) {
        mLoadingDialog.startLoadingDialog();
        int slotNumber = Integer.parseInt(booking.getSlotNumber());
        slotNumber--;
        slotOwnerArrayList.get(slotNumber).setText2("arrived");
        FirebaseDatabase.getInstance().getReference().child("owner").child(booking.getOwnerId())
                .child("slotList").child(slotNumber+"").setValue("arrived");

        //not sure if i should change this or not
/*        FirebaseDatabase.getInstance().getReference().child("booking").child(booking.getBookingId())
                .child("bookingStatus").setValue("arrived");*/
        mLoadingDialog.dismissDialog();
    }

    @Override
    public void onCarDismissButtonClicked(Booking booking, User user) {
        mLoadingDialog.startLoadingDialog();
        int slotNumber = Integer.parseInt(booking.getSlotNumber());
        slotNumber--;
        slotOwnerArrayList.get(slotNumber).setText2("empty");
        FirebaseDatabase.getInstance().getReference().child("owner").child(booking.getOwnerId())
                .child("slotList").child(slotNumber+"").setValue("empty");
        FirebaseDatabase.getInstance().getReference().child("owner-booking")
                .child(booking.getOwnerId()).child(slotNumber+"").setValue("none");
        FirebaseDatabase.getInstance().getReference().child("user")
                .child(booking.getUserId()).child("bookingStatus").setValue("nothing");
        FirebaseDatabase.getInstance().getReference().child("booking")
                .child(booking.getBookingId()).child("bookingStatus").setValue("Incomplete");
        mLoadingDialog.dismissDialog();
    }


}