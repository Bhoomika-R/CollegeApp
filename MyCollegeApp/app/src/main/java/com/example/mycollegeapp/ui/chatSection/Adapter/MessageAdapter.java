package com.example.mycollegeapp.ui.chatSection.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mycollegeapp.R;
import com.example.mycollegeapp.ui.chatSection.Model.Chat;
import com.example.mycollegeapp.ui.profile.ProfileSharedPreferences;

import java.util.HashMap;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;


    private Context mContext;
    private List<Chat> mChat;
    private String imageUrl;

    private ProfileSharedPreferences spProfile;
    private HashMap<String, String> profileData;


    public MessageAdapter(Context mContext, List<Chat> mChat, String imageUrl) {
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageUrl = imageUrl;
        spProfile = new ProfileSharedPreferences(mContext);
        profileData=spProfile.getUserProfile();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);

        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);

        }

        return new ViewHolder(view, viewType);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = mChat.get(position);

        holder.date_and_time.setText(chat.getDateAndTime());

        if (chat.getPhotoUrl() == null) {
            holder.show_message.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            holder.show_message.setText(chat.getMessage());
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);

            String url = chat.getPhotoUrl();

            if (url.contains(".pdf")) {

                holder.photo_image.setVisibility(View.GONE);
                holder.show_message.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_picture_as_pdf_24, 0, 0, 0);
//               Log.v("chatApp", "outside click listener");
                holder.show_message.setText("pdf file");
                holder.show_message.setElevation(10);
                holder.show_message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                       Toast.makeText(view.getContext(), url.toString(), Toast.LENGTH_SHORT).show();
//                       Log.e("chatApp", "Inside click listener");

                        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pdfOpenintent.setDataAndType(Uri.parse(url), "application/pdf");

                        try {
                            mContext.startActivity(pdfOpenintent);

                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(mContext, "Pdf error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            } else {
                holder.show_message.setVisibility(View.GONE);
                holder.photo_image.setVisibility(View.VISIBLE);
                Glide.with(holder.photo_image.getContext())
                        .load(chat.getPhotoUrl())
                        .into(holder.photo_image);
            }


        }


        if (imageUrl.contains("default")) {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(imageUrl).into(holder.profile_image);
        }


        if (position == mChat.size() - 1) {
            if (chat.getIsSeen() == 1) {
                holder.txt_seen.setText("Seen");
            } else {
                holder.txt_seen.setText("Sent");
            }

        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView show_message;
        final ImageView profile_image;
        final ImageView photo_image;
        final TextView date_and_time;
        final RelativeLayout relativeLayout;

        public TextView txt_seen;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            if (viewType == MSG_TYPE_LEFT) {
                relativeLayout = itemView.findViewById(R.id.relative_layout_left);
                date_and_time = itemView.findViewById(R.id.date_and_time_chat_item_left);
                photo_image = itemView.findViewById(R.id.photo_image_view_chat_item_left);
                show_message = itemView.findViewById(R.id.show_message_chat_item_left);
                profile_image = itemView.findViewById(R.id.profile_image_chat_item_left);
                txt_seen = itemView.findViewById(R.id.txt_seen_chat_item_left);
            } else {
                relativeLayout = itemView.findViewById(R.id.relative_layout_right);
                date_and_time = itemView.findViewById(R.id.date_and_time_chat_item_right);
                photo_image = itemView.findViewById(R.id.photo_image_view_chat_item_right);
                show_message = itemView.findViewById(R.id.show_message_chat_item_right);
                profile_image = itemView.findViewById(R.id.profile_image_chat_item_right);
                txt_seen = itemView.findViewById(R.id.txt_seen_chat_item_right);
            }


        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mChat.get(position).getSender().equals(profileData.get(ProfileSharedPreferences.KEY_REGNO))) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

}

