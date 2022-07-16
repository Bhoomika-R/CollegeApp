package com.example.admindashboard.sports;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admindashboard.R;

import java.util.ArrayList;

public class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.SportsAdapterViewHolder> {



    final Context context;
    final ArrayList<Sport> sports;

    public SportsAdapter(Context context, ArrayList<Sport> sports){
        this.context = context;
        this.sports = sports;
    }

    @NonNull
    @Override
    public SportsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.club_and_sport_list_item, viewGroup , false);
        return new  SportsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SportsAdapterViewHolder holder, int position) {
        Sport currentSport =  sports.get(position);
        holder.sportName.setText(currentSport.getSportName());
        holder.sportType.setText(currentSport.getSportType());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SportsDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("sportName",currentSport.getSportName());
                intent.putExtra("sportType",currentSport.getSportType());
                intent.putExtra("sportGender",currentSport.getSportGender());
                intent.putExtra("captain",currentSport.getCaptain());
                intent.putExtra("captainPhone",currentSport.getCaptainContactNumber());
                intent.putExtra("teamLegacy",currentSport.getTeamLegacy());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sports.size();
    }


    public class SportsAdapterViewHolder extends RecyclerView.ViewHolder{

        final TextView sportName;
        final TextView sportType;

        public SportsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            sportName = itemView.findViewById(R.id.teacherOrClubOrSportName);
            sportType = itemView.findViewById(R.id.teacherDesignationOrClubGenreOrSportType);
        }

    }
}
