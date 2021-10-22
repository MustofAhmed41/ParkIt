package com.example.isdmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.isdmap.Adapter.ownerSlotAdapter;
import com.example.isdmap.Fragments.fragmentBooked;
import com.example.isdmap.Fragments.fragmentEmpty;
import com.example.isdmap.Models.SlotOwner;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class parkingSlotActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<SlotOwner> exampleList = new ArrayList<SlotOwner>();
    private ownerSlotAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_slot);
        BottomNavigationView bottomNav = findViewById(R.id.bottom);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        mRecyclerView = findViewById(R.id.recyclerHome);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ownerSlotAdapter(exampleList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String u= FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("owner").child(u).child("slotList");
        exampleList = new ArrayList<SlotOwner>();


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String value = ds.getValue(String.class);
                    String key =ds.getKey() ;
                    int i=Integer.parseInt(key);
                    i++;
                    String slot=i+"";

                    exampleList.add(new SlotOwner(slot,value));
                }
                mAdapter = new ownerSlotAdapter(exampleList);
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "database Error", Toast.LENGTH_SHORT).show();
            }
        });



    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_reserve:
                            selectedFragment = new fragmentBooked();
                            break;
                        case R.id.nav_empty:
                            selectedFragment = new fragmentEmpty();
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };





}

