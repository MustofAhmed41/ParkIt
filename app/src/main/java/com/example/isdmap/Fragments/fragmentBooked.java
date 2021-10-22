package com.example.isdmap.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.isdmap.Adapter.ownerSlotAdapter;
import com.example.isdmap.Adapter.slotBookedAdapter;
import com.example.isdmap.Dialogs.OwnerBookingInfo;
import com.example.isdmap.Models.Booking;
import com.example.isdmap.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class fragmentBooked extends Fragment {
    private RecyclerView mRecyclerView;
    private ArrayList<Booking> bookingArrayList;
    private slotBookedAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FirebaseAuth auth;
    String userId;



    public fragmentBooked(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_booked,container,false);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(v.getContext());
        //mAdapter = new exampleAdapter(exampleList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        auth = FirebaseAuth.getInstance();
        bookingArrayList= new ArrayList<Booking>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        String u= FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef.child("booking").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Booking booking;
                if(snapshot.exists()){
                    for (DataSnapshot data : snapshot.getChildren()){
                        booking = data.getValue(Booking.class);

                        if(!booking.getBookingStatus().equals("completed")){
                            //Toast.makeText(parkingSlotActivity.this, booking.getUserId()+"  hello khaishta bro", Toast.LENGTH_SHORT).show();
                            if(booking.getOwnerId().equals(u) ){
                                bookingArrayList.add(booking);
                                System.out.println(booking.getBookingId());
                                //Toast.makeText(fragmentBooked., booking.getUserId()+"hello keisha juta", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                    //adapter load
                    mAdapter = new slotBookedAdapter(bookingArrayList);
                    mRecyclerView.setAdapter(mAdapter);


                    mAdapter.setOnItemClickListener(new slotBookedAdapter.OnItemClickListener() {
                        @Override
                        public void onItemSelect(int position) {
                            OwnerBookingInfo dialog = new OwnerBookingInfo (bookingArrayList.get(position).getBookingId(),bookingArrayList.get(position).getUserId());
                            dialog.show(getFragmentManager(),"NULL");

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }




}
