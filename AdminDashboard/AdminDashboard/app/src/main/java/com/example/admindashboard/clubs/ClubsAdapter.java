package com.example.admindashboard.clubs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admindashboard.R;
import com.example.admindashboard.sports.Sport;
import com.example.admindashboard.sports.SportsAdapter;
import com.example.admindashboard.sports.SportsDetailActivity;

import java.util.ArrayList;

public class ClubsAdapter extends RecyclerView.Adapter<ClubsAdapter.ClubsAdapterViewHolder>{
    final Context context;
    final ArrayList<Club> clubs;

    public ClubsAdapter(Context context, ArrayList<Club> clubs) {
        this.context = context;
        this.clubs = clubs;
    }

    @NonNull
    @Override
    public ClubsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.club_and_sport_list_item, parent , false);
        return new  ClubsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubsAdapterViewHolder holder, int position) {
        Club currentClub =  clubs.get(position);
        holder.clubName.setText(currentClub.getClubName());
        holder.clubType.setText(currentClub.getClubType());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ClubsDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("clubName",currentClub.getClubName());
                intent.putExtra("clubType",currentClub.getClubType());
                intent.putExtra("clubGenre",currentClub.getClubGenre());
                intent.putExtra("clubLeadName",currentClub.getClubLeadName());
                intent.putExtra("clubLeadContactNumber",currentClub.getClubLeadContactNumber());
                intent.putExtra("clubDescription",currentClub.getClubDescription());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }

    public class ClubsAdapterViewHolder extends RecyclerView.ViewHolder{

        final TextView clubName;
        final TextView clubType;

        public ClubsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            clubName = itemView.findViewById(R.id.teacherOrClubOrSportName);
            clubType = itemView.findViewById(R.id.teacherDesignationOrClubGenreOrSportType);
        }

    }
}
