package com.example.isdmap.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.isdmap.Models.Booking;
import com.example.isdmap.R;

import java.util.ArrayList;


public class slotBookedAdapter  extends RecyclerView.Adapter{
    private ArrayList<Booking> slotList;
    private static final int booked_item=1;
    private static final int arrived_item=2;
    private static final int empty_item=0;
    private OnItemClickListener mListener;

    public  slotBookedAdapter (ArrayList<Booking> slotList)
    {
        this.slotList=slotList;
    }

    public void setOnItemClickListener(ownerSlotAdapter.OnItemClickListener aNull) {
    }

    public interface OnItemClickListener{
        void onItemSelect(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        int n=0;
        if(slotList.get(position).getBookingStatus().contains("completed"))
            n=  0;
        else if(slotList.get(position).getBookingStatus().contains("booked"))
            n= 1;
        else if(slotList.get(position).getBookingStatus().contains("arrived"))
            n= 2;

        return n;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if(viewType==0)
        {
            // if the user is the current user
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_item1, parent, false);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);  // sometimes
            //a single item takes out whole screen so including this lines prevents it from doing that
            view.setLayoutParams(lp);
            return new exampleViewHolderEmpty(view,mListener); // view holder


        }
        if(viewType==1)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_item2, parent, false);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);  // sometimes

            return  new exampleViewHolderbooked(view,mListener);
        }
        view = layoutInflater.inflate(R.layout.slot_item3,parent,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);  // sometimes
        return  new slotViewHolderArrived(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(slotList.get(position).getBookingStatus().contains("completed"))
        {
            exampleViewHolderEmpty viewHolderEmpty = (exampleViewHolderEmpty) holder;
            viewHolderEmpty.emptySlotNo.setText(slotList.get(position).getSlotNumber());
            viewHolderEmpty.emptySlotStatus.setText(slotList.get(position).getBookingStatus());



        }
        if(slotList.get(position).getBookingStatus().contains("booked"))
        {
            exampleViewHolderbooked viewHolderbooked = (exampleViewHolderbooked) holder;
            viewHolderbooked.bookedSlotNo.setText(slotList.get(position).getSlotNumber());
            viewHolderbooked.bookedSlotStatus.setText(slotList.get(position).getBookingStatus());

        }
        if(slotList.get(position).getBookingStatus().contains("arrived"))
        {
            slotViewHolderArrived slotViewHolderArrived= ( slotViewHolderArrived) holder;
            slotViewHolderArrived.arrivedSlotNo.setText(slotList.get(position).getSlotNumber());
            slotViewHolderArrived.arrivedSlotStatus.setText(slotList.get(position).getBookingStatus());

        }

    }

    @Override
    public int getItemCount() {
        return slotList.size();
    }

    class exampleViewHolderEmpty extends RecyclerView.ViewHolder{
        public TextView emptySlotNo,emptySlotStatus;
        public exampleViewHolderEmpty(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            emptySlotNo=itemView.findViewById(R.id.slot1_key);
            emptySlotStatus=itemView.findViewById(R.id.slot1_value);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            System.out.println(position);
                            listener.onItemSelect(position);
                        }
                    }
                }
            });
        }
    }

    class exampleViewHolderbooked extends RecyclerView.ViewHolder {
        public TextView bookedSlotNo, bookedSlotStatus;

        public exampleViewHolderbooked(@NonNull View itemView , OnItemClickListener listener) {
            super(itemView);
            bookedSlotNo =itemView.findViewById(R.id.slot2_key);
            bookedSlotStatus =itemView.findViewById(R.id.slot2_value);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            System.out.println(position);
                            listener.onItemSelect(position);

                        }
                    }
                }
            });


        }
    }

    class slotViewHolderArrived extends RecyclerView.ViewHolder{
        public TextView arrivedSlotNo,arrivedSlotStatus;

        public slotViewHolderArrived(@NonNull View itemView , OnItemClickListener listener) {
            super(itemView);
            arrivedSlotNo =itemView.findViewById(R.id.slot3_key);
            arrivedSlotStatus =itemView.findViewById(R.id.slot3_value);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            System.out.println(position);
                            listener.onItemSelect(position);
                        }
                    }
                }
            });
        }
    }
}
