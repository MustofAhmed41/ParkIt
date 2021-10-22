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


public class userHistoryAdapter extends RecyclerView.Adapter <userHistoryAdapter.NewAdapterviewholder>{

    private static ArrayList<Booking> book;
    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemSelect(int position);
    }
    public void setOnItemClickListener(userHistoryAdapter.OnItemClickListener listener){
        mListener = listener;
    }
    public userHistoryAdapter(ArrayList<Booking> bookingArrayList) {
        this.book=bookingArrayList;
    }

    @NonNull
    @Override
    public NewAdapterviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_history_item,parent,false);
        return new NewAdapterviewholder(view,mListener) {
        };
    }




    class NewAdapterviewholder extends RecyclerView.ViewHolder{
        TextView expire;
        TextView time;
        TextView duration;
        TextView slot;
        TextView vType;
        TextView cost;

        public NewAdapterviewholder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            cost= itemView.findViewById(R.id.bill);
            expire = itemView.findViewById(R.id.expire);
            time = itemView.findViewById(R.id.booktime);
            vType = itemView.findViewById(R.id.vType);
            duration = itemView.findViewById(R.id.duration);
            slot = itemView.findViewById(R.id.slot);
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


    public void onBindViewHolder(@NonNull NewAdapterviewholder holder, int position) {
        Booking b = book.get(position);
        holder.cost.setText(b.getCost());
        holder.expire.setText(b.getBookingStatus());
        holder.time.setText(b.getStartTime());
        holder.vType.setText(b.getEndTime());
        holder.duration.setText(b.getDuration());
        holder.slot.setText(b.getSlotNumber());

    }


    @Override
    public int getItemCount() {
        return book.size();
    }


}
