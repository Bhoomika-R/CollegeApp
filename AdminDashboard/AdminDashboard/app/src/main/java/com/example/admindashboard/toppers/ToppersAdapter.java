package com.example.admindashboard.toppers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.admindashboard.R;
import com.example.admindashboard.clubs.ClubActivity;
import com.example.admindashboard.clubs.ClubsDetailActivity;
import com.example.admindashboard.profile.ProfileSharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ToppersAdapter extends RecyclerView.Adapter<ToppersAdapter.MyViewHolder> {

    private ArrayList<Topper> list;
    private Context context;

    private String heading;
    public ToppersAdapter(Context ct, ArrayList<Topper> list,String heading) {
        context = ct;
        this.list=list;
        this.heading=heading;
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
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Context wrapper = new ContextThemeWrapper(context, R.style.PopupMenu);
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(wrapper, v);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu_toppers_list, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.deleteTask) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setIcon(R.drawable.ic_baseline_delete_24);
                            builder.setTitle("Delete");
                            builder.setMessage("Are you sure?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("toppers list").child(details.getBranch()).child(details.getYear()).child(details.getRegNo());
                                    reference.removeValue();
                                    Intent getBack = new Intent(context, TopperListActivity.class);
                                    getBack.putExtra("Year",details.getYear());
                                    getBack.putExtra("TVHeading",heading);
                                    getBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(getBack);
                                }

                            });
                            builder.setNegativeButton("No", null);
                            builder.show();
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu

                return true;
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
