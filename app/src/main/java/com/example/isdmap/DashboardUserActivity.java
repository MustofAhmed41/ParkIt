package com.example.isdmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isdmap.Dialogs.DialogBookingInformation;
import com.example.isdmap.Models.Booking;
import com.example.isdmap.Models.Owner;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class DashboardUserActivity extends AppCompatActivity implements DialogBookingInformation.BookingCompletedInfo{

    private CardView carParkCardView, checkOutCardView, profileUpdateView;
    private LoadingDataDialog mLoadingDialog;
    private String mUser;
    private ImageView carParkImage, checkOutImage;

    //for navigation
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        //#007c91
        //getSupportActionBar().setTitle("PANDA");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //for navigation
        auth = FirebaseAuth.getInstance();
        user =auth.getCurrentUser();

        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation);
        drawer = findViewById(R.id.drawer);

        //icon color change
        navigationView.setItemIconTintList(null);

        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer.closeDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.Aboutus:
                        Toast.makeText(DashboardUserActivity.this, "About us", Toast.LENGTH_SHORT).show();
                        Intent intent0 = new Intent(DashboardUserActivity.this,AboutUsActivity.class);
                        startActivity(intent0);
                        break;
                    case R.id.Sendabug:
                        Toast.makeText(DashboardUserActivity.this, "Send A bug", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(DashboardUserActivity.this,ReportBugActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.settings:
                        Toast.makeText(DashboardUserActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(DashboardUserActivity.this,UpdateOwner.class);
                        startActivity(intent2);
                        break;
                    case R.id.LogOut:
                        Toast.makeText(DashboardUserActivity.this, "Loging Out ", Toast.LENGTH_SHORT).show();
                        auth.signOut();
                        Intent myIntent= new Intent(DashboardUserActivity.this, OpenActivity.class);
                        startActivity(myIntent);
                        break;
                }
                return true;
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userName = user.getDisplayName();
        String UserEmail = user.getEmail();
        View view = navigationView.getHeaderView(0);
        TextView email = view.findViewById(R.id.email);
        TextView name = view.findViewById(R.id.name);
        email.setText(UserEmail);
        mUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().
                child("user").child(mUser).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    name.setText(snapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        initiailizeViews();

        mUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(!mUser.equals("")) {
            FirebaseDatabase.getInstance().getReference().
                    child("user").child(mUser).child("bookingStatus").
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String status = snapshot.getValue(String.class);
                                if(status.equals("nothing")) {
                                    enableCarPark();
                                    disableCheckOut();
                                }else{
                                    disableCarPark();
                                    enableCheckOut();
                                }
                                mLoadingDialog.dismissDialog();
                            }else{
                                mLoadingDialog.dismissDialog();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            mLoadingDialog.dismissDialog();
                        }
                    });

        }


    }

    private void initiailizeViews() {
        mLoadingDialog = new LoadingDataDialog(DashboardUserActivity.this);
        carParkCardView = findViewById(R.id.dashboard_user_car_park);
        checkOutCardView = findViewById(R.id.dashboard_user_check_out);
        carParkImage = findViewById(R.id.user_dashboard_car_park_image);
        checkOutImage = findViewById(R.id.user_dashboard_check_out_image);
        profileUpdateView = findViewById(R.id.user_dashboard_profile_update);
        mLoadingDialog.startLoadingDialog();
        profileUpdateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardUserActivity.this, UpdateUser.class);
                startActivity(intent);
            }
        });
    }

    private void enableCheckOut() {
        checkOutImage.setImageResource(R.drawable.exportlog);
        checkOutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOutDialog();
            }
        });
    }

    private void disableCarPark() {
        carParkImage.setImageResource(R.drawable.car_dashboard_icon_disabled);
        carParkCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardUserActivity.this, "You already have a booking", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void disableCheckOut() {
        checkOutImage.setImageResource(R.drawable.exportlog_disabled);
        checkOutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardUserActivity.this, "You do not have any booking", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableCarPark() {
        carParkImage.setImageResource(R.drawable.car_dashboard_icon);
        carParkCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFindParkActivity();
            }
        });
    }

    private void checkOutDialog() {
        mLoadingDialog.startLoadingDialog();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("user").child(mUser).child("bookingStatus").
                addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String bookingKey = snapshot.getValue(String.class);
                    db.child("booking").child(bookingKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Booking booking = snapshot.getValue(Booking.class);
                                String ownerId = booking.getOwnerId();
                                db.child("owner").child(ownerId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            Owner owner = snapshot.getValue(Owner.class);
                                            mLoadingDialog.dismissDialog();
                                            DialogBookingInformation dialog = new DialogBookingInformation(booking.getOwnerId(),booking.getSlotNumber(),mUser,bookingKey,
                                                    booking.getStartTime(), booking.getEndTime(),owner.getOwnerPhone(), owner.getCostPerHour(), booking.getCost());
                                            dialog.show(getSupportFragmentManager(), "MapActivity");

                                        }else{
                                            mLoadingDialog.dismissDialog();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }else{
                                mLoadingDialog.dismissDialog();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{
                    mLoadingDialog.dismissDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void openFindParkActivity() {
        Intent intent = new Intent(this, SearchParkingLotActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBookingDone(String mBookingKey, double cost, String time, String currentTime, String mSlotNumber, String mOwnerId) {
        mLoadingDialog.startLoadingDialog();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("booking").child(mBookingKey).
                child("bookingStatus").setValue("completed");
        myRef.child("booking").child(mBookingKey).
                child("cost").setValue(cost+"");
        myRef.child("booking").child(mBookingKey).
                child("duration").setValue(time);
        myRef.child("booking").child(mBookingKey).
                child("endTime").setValue(currentTime);
        myRef.child("user").child(mUser).child("bookingStatus").setValue("nothing");
        int slot = Integer.parseInt(mSlotNumber);
        slot--;
        myRef.child("owner").child(mOwnerId).child("slotList").child(slot+"").
                setValue("empty");
        mLoadingDialog.dismissDialog();
        recreate();
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void gotoUserHistory(View view) {
        Intent intent = new Intent(this, UserHistory.class);
        startActivity(intent);
    }
}
