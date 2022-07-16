package com.example.admindashboard.addDeleteEdit;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.admindashboard.R;
import com.example.admindashboard.profile.ProfileClassUser;
import com.example.admindashboard.profile.UserProfileActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private Set<ProfileClassUser> mUsersSet;
    private List<ProfileClassUser> mUsers;

    private HashMap<String, String> profileData;
    private String pageHeading,userType;

    public UserAdapter(Context mContext, HashMap<String, String> profileData, Set<ProfileClassUser> mUsersSet,String pageHeading,String userType) {
        this.mUsersSet = mUsersSet;
        this.mContext = mContext;
        this.profileData = profileData;
        this.pageHeading = pageHeading;
        this.userType = userType;
        mUsers = new ArrayList<>(mUsersSet);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProfileClassUser user = mUsers.get(position);
        holder.userName.setText(user.getName());
        holder.tvRegNo.setText(user.getRegNo());

        if (user.getImageUrl() != null) {
            if (user.getImageUrl().contains("default")) {
                holder.profile_image.setImageResource(R.drawable.ic_baseline_person_24);
            } else {
                Glide.with(mContext).load(user.getImageUrl()).into(holder.profile_image);
            }
        } else {
            holder.profile_image.setImageResource(R.drawable.ic_baseline_person_24);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConnected((AddDeleteUserActivity) mContext)){
                    Toast.makeText(mContext,"No internet connection",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(mContext, UserProfileActivity.class);
                intent.putExtra("regNo", user.getRegNo());
                intent.putExtra("role", user.getRole().toLowerCase(Locale.ROOT));
                intent.putExtra("branch", user.getBranch());
                intent.putExtra("pageHeading", pageHeading);
                intent.putExtra("userType", userType);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userName;
        public TextView tvRegNo;
        public ImageView profile_image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_item_user_name);
            profile_image = itemView.findViewById(R.id.user_item_profile_image);
            tvRegNo = itemView.findViewById(R.id.tvRegNo);

        }
    }

    private boolean isConnected(AddDeleteUserActivity activity) {
        ConnectivityManager manager= (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mob=manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo wifi=manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi!=null&&wifi.isConnected())||(mob!=null&&mob.isConnected())){
            return true;
        }
        else{
            return false;
        }
    }

}


