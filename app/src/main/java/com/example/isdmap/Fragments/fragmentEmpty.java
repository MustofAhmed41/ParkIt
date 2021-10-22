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
import com.example.isdmap.Models.SlotOwner;
import com.example.isdmap.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class fragmentEmpty extends Fragment {
    private RecyclerView mRecyclerView;
    private ArrayList<SlotOwner> exampleList = new ArrayList<SlotOwner>();
    private ownerSlotAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    DatabaseReference reference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v= inflater.inflate(R.layout.fragment_empty, container, false);
        mRecyclerView = v.findViewById(R.id.recyclerEmpty);
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(v.getContext());
        //mAdapter = new exampleAdapter(exampleList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        exampleList = new ArrayList<SlotOwner>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        String u= FirebaseAuth.getInstance().getCurrentUser().getUid();
        exampleList = new ArrayList<SlotOwner>();
        reference = FirebaseDatabase.getInstance().getReference("owner").child(u).child("slotList");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {

                    String value = ds.getValue(String.class);
                    String key =ds.getKey() ;
                    if(value.contains("empty"))
                    {
                        int i= Integer.parseInt(key);
                        i++;
                        String slot=i+"";
                        exampleList.add(new SlotOwner(slot,value));

                    }

                }
                mAdapter = new ownerSlotAdapter(exampleList);
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


       return v;
    }
}
