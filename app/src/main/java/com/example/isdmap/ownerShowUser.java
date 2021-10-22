package com.example.isdmap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.isdmap.Adapter.ownerSlotAdapter;
import com.example.isdmap.Models.User;
import com.example.isdmap.Models.SlotOwner;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ownerShowUser extends AppCompatActivity {

    public void ownerShowUser(String userId){

    }
    private RecyclerView mRecyclerView;
    private ArrayList<SlotOwner> exampleList = new ArrayList<SlotOwner>();
    private ownerSlotAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    DatabaseReference reference;
    User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_show_user);
        TextView name = findViewById(R.id.name);
        TextView phoneNumber = findViewById(R.id.phone);
        TextView email = findViewById(R.id.email);
        TextView carBrand = findViewById(R.id.carbrand);
        TextView cartype = findViewById(R.id.carType);
        TextView carNumber =findViewById(R.id.carNumber);
        TextView carColor =findViewById(R.id.carColor);
        Intent intent =getIntent();
        String userId=intent.getStringExtra("key");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("user").child(userId).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mUser = snapshot.getValue(User.class);
                        name.setText(mUser.getName());
                        phoneNumber.setText(mUser.getPhone());
                        email.setText(mUser.getEmail());
                        carBrand.setText(mUser.getCarBrand());
                        cartype.setText(mUser.getCarType());
                        carNumber.setText(mUser.getCarNumber());
                        carColor.setText(mUser.getCarColor());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}