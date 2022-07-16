package com.example.mycollegeapp.ui.teachers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycollegeapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class TeachersAdapter extends RecyclerView.Adapter<TeachersAdapter.TeachersAdapterViewHolder>{
    final Context context;
    private Set<ProfileClassFaculty> mUsersSet;

    private List<ProfileClassFaculty> teachers;

    private HashMap<String, String> profileData;

    public TeachersAdapter(Context context,HashMap<String, String> profileData, Set<ProfileClassFaculty> mUsersSet){
        this.mUsersSet = mUsersSet;
        this.context = context;
        this.profileData = profileData;
        this.teachers = new ArrayList<>(mUsersSet);
    }

    @NonNull
    @Override
    public TeachersAdapter.TeachersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.teacher_list_item, viewGroup , false);
        return new TeachersAdapter.TeachersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeachersAdapterViewHolder holder, int position) {
        ProfileClassFaculty user = teachers.get(position);
        holder.userName.setText(user.getName());
        holder.designation.setText(user.getDesignation());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TeachersDetailActivity.class);
                intent.putExtra("regNo", user.getRegNo());
                intent.putExtra("role", user.getRole().toLowerCase(Locale.ROOT));
                intent.putExtra("branch", user.getBranch());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }


    public class TeachersAdapterViewHolder extends RecyclerView.ViewHolder{

        public TextView userName;
        public TextView designation;

        public TeachersAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.teacherOrClubOrSportName);
            designation = itemView.findViewById(R.id.teacherDesignationOrClubGenreOrSportType);
        }

    }
}
