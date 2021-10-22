package com.example.isdmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.isdmap.Adapter.historyAdapter;
import com.example.isdmap.Dialogs.DialogUserDetails;
import com.example.isdmap.Models.Booking;
import com.example.isdmap.Models.history;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class historyActivity extends AppCompatActivity {
    RecyclerView reView;
    private  historyAdapter mAdapter;
    DatabaseReference ownerReference;
    FirebaseUser userOwner;
    FirebaseAuth auth;
    private ArrayList<Booking> bookingArrayList;
    private RecyclerView.LayoutManager mLayoutManager;



    DatabaseReference reference;
    private ArrayList<history> historyList = new ArrayList<history>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        bookingArrayList = new ArrayList<Booking>();

        mLayoutManager = new LinearLayoutManager(this);
        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("booking");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Booking booking;
                if(snapshot.exists()){
                    for (DataSnapshot data : snapshot.getChildren()){
                        booking = data.getValue(Booking.class);

                        if(booking.getBookingStatus().equals("completed") && booking.getOwnerId().equals(userUID) ){
                            bookingArrayList.add(booking);
                        }

                    }

                    reView = findViewById(R.id.rcView);
                    reView.setHasFixedSize(true);
                    mAdapter = new historyAdapter(bookingArrayList);
                    reView.setLayoutManager(mLayoutManager);
                    reView.setAdapter(mAdapter);
                    mAdapter.setOnItemClickListener(new  historyAdapter.OnItemClickListener() {
                        @Override
                        public void onItemSelect(int position) {

                            DialogUserDetails dialog = new DialogUserDetails(bookingArrayList.get(position).getUserId());
                            dialog.show(getSupportFragmentManager(),"NULL");
                            //Toast.makeText(parkingSlotActivity.this, bookingArrayList.get(position).getUserId(), Toast.LENGTH_SHORT).show();
                            // Toast.makeText(parkingSlotActivity.this, "ok", Toast.LENGTH_SHORT).show();
                        }
                    });




                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}