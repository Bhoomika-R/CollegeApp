package com.example.mycollegeapp.ui.chatSection.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mycollegeapp.R;
import com.example.mycollegeapp.ui.chatSection.MessageActivity;
import com.example.mycollegeapp.ui.chatSection.Model.Chat;
import com.example.mycollegeapp.ui.profile.ProfileClass;
import com.example.mycollegeapp.ui.profile.ProfileSharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private Context mContext;
    private Set<ProfileClass> mUsersSet;
    private List<ProfileClass> mUsers;
    private Boolean ischat;

    private HashMap<String, String> profileData;

    String theLastMessage;

    public UserAdapter(Context mContext ,HashMap<String,String> profileData,Set<ProfileClass> mUsersSet, Boolean ischat){
        this.mUsersSet = mUsersSet;
        this.mContext = mContext;
        this.ischat = ischat;
        this.profileData=profileData;
        mUsers = new ArrayList<ProfileClass>( mUsersSet );
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
          ProfileClass user = mUsers.get(position);
          holder.userName.setText(user.getName());

           if(user.getImageUrl().contains("default")){
                    holder.profile_image.setImageResource(R.drawable.ic_baseline_person_24);
                } else {
                    Glide.with(mContext).load(user.getImageUrl()).into(holder.profile_image);
                }



        if(ischat){
           lastMessage(user.getRegNo(), holder.last_msg, holder.unread_chat_icon);

        } else{
            holder.last_msg.setVisibility(View.GONE);
        }

          if(ischat){
                if(user.getStatus().equals("online")){
                    holder.img_on.setVisibility(View.VISIBLE);
                    holder.img_off.setVisibility(View.GONE);
                } else {
                    holder.img_on.setVisibility(View.GONE);
                    holder.img_off.setVisibility(View.VISIBLE);
                }

          } else{
              holder.img_on.setVisibility(View.GONE);
              holder.img_off.setVisibility(View.GONE);
          }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid",user.getRegNo());
                intent.putExtra("userRole",user.getRole());
                mContext.startActivity(intent);
            }
        });
        if(ischat){
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    Context wrapper = new ContextThemeWrapper(mContext, R.style.PopupMenu);
//                    //Creating the instance of PopupMenu
//                    PopupMenu popup = new PopupMenu(wrapper, v);
//                    //Inflating the Popup using xml file
//                    popup.getMenuInflater().inflate(R.menu.popup_menu_chat_person, popup.getMenu());
//
//                    //registering popup with OnMenuItemClickListener
//                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        public boolean onMenuItemClick(MenuItem item) {
//                            if (item.getItemId() == R.id.deleteTask) {
//                               DatabaseReference reference=FirebaseDatabase.getInstance().getReference("chat section").child("chatlist").child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child(user.getRegNo());
//                               reference.removeValue();
//                            }
//                            return true;
//                        }
//                    });
//
//                    popup.show();//showing popup menu
//                    return true;
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


   public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView userName;
        public ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;
        private TextView last_msg;
        private ImageView unread_chat_icon;

       public ViewHolder(@NonNull View itemView) {
           super(itemView);

           userName = itemView.findViewById(R.id.user_item_user_name);
           profile_image = itemView.findViewById(R.id.user_item_profile_image);
           img_on = itemView.findViewById(R.id.image_on_user_item);
           img_off = itemView.findViewById(R.id.image_off_user_item);
           last_msg = itemView.findViewById(R.id.user_item_last_msg);
           unread_chat_icon = itemView.findViewById(R.id.user_item_unread_chat_icon);
       }
   }

  //check for last message
    private void lastMessage(String userid, TextView last_msg, ImageView unread_chat_icon){
     theLastMessage = "default";
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chat section").child("chats").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                  Chat chat = snapshot.getValue(Chat.class);
                  if(chat.getReceiver().equals(profileData.get(ProfileSharedPreferences.KEY_REGNO)) && chat.getSender().equals(userid) ||
                     chat.getReceiver().equals(userid) && chat.getSender().equals(profileData.get(ProfileSharedPreferences.KEY_REGNO))){

                      if(chat.getReceiver().equals(profileData.get(ProfileSharedPreferences.KEY_REGNO)) && chat.getSender().equals(userid) && chat.getIsSeen()==0){
                          unread_chat_icon.setVisibility(View.VISIBLE);
                      } else{
                          unread_chat_icon.setVisibility(View.GONE);
                      }


                      if(chat.getMessage()== null) {
                          theLastMessage = "file";
                      }else{
                          theLastMessage= chat.getMessage();
                      }
                  }
              }

              switch (theLastMessage){
                  case "default":
                      last_msg.setText("No Message");
                      break;

                  default:
                      last_msg.setText(theLastMessage);
                      break;
              }

              theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
