package com.example.mycollegeapp.ui.navigationDrawer.topperslist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mycollegeapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ToppersAdapter extends RecyclerView.Adapter<ToppersAdapter.MyViewHolder> {

    private ArrayList<Topper> list;
    private Context context;


    public ToppersAdapter(Context ct, ArrayList<Topper> list) {
        context = ct;
        this.list=list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.layout_recycler_view_toppers_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Topper details= list.get(position);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("root").child("profiles").child(details.getBranch()).child("student").child(details.getRegNo());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    holder.topper_name.setText(details.getName());
                    holder.topperRegNo.setText(details.getRegNo());
                    holder.cgpa.setText(details.getCgpa());
                    if(!snapshot.child("imageUrl").getValue(String.class).equals("default"))
                        Glide.with(context).load(snapshot.child("imageUrl").getValue(String.class)).into(holder.imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView topper_name, topperRegNo,cgpa;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            topper_name = itemView.findViewById(R.id.topper_name);
            topperRegNo = itemView.findViewById(R.id.topper_regNo);
            imageView = itemView.findViewById(R.id.imageView);
            cgpa = itemView.findViewById(R.id.cgpa);
        }
    }
}
