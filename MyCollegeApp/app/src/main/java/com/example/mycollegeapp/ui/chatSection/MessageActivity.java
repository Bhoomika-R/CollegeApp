package com.example.mycollegeapp.ui.chatSection;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mycollegeapp.R;
import com.example.mycollegeapp.ui.chatSection.Adapter.MessageAdapter;
import com.example.mycollegeapp.ui.chatSection.Fragments.APIService;
import com.example.mycollegeapp.ui.chatSection.Model.Chat;
import com.example.mycollegeapp.ui.chatSection.Notifications.Client;
import com.example.mycollegeapp.ui.chatSection.Notifications.Data;
import com.example.mycollegeapp.ui.chatSection.Notifications.MyResponse;
import com.example.mycollegeapp.ui.chatSection.Notifications.Sender;
import com.example.mycollegeapp.ui.chatSection.Notifications.Token;
import com.example.mycollegeapp.ui.networks.NetworkChangeListener;
import com.example.mycollegeapp.ui.profile.ProfileClass;
import com.example.mycollegeapp.ui.profile.ProfileSharedPreferences;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    private static final int RC_PHOTO_PICKER = 2;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;

    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String dateAndTime;

    private CircleImageView profile_image;
    private TextView username;

    private DatabaseReference reference;

    private ImageButton btn_send;
    private EditText text_send;
    private ImageButton image_picker;

    private String userid,userRole;

    private APIService apiService;

    private boolean notify = false;

    private MessageAdapter messageAdapter;
    private List<Chat> mChat;

    private RecyclerView recyclerView;

    private Intent intent;

    private ValueEventListener seenListener;


    private ProfileSharedPreferences spProfile;
    private HashMap<String,String> profileData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        spProfile = new ProfileSharedPreferences(MessageActivity.this);
        profileData=spProfile.getUserProfile();

        Toolbar toolbar = findViewById(R.id.toolbarMessageActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MessageActivity.this, ChatMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy \nHH:mm");

        mFirebaseStorage = FirebaseStorage.getInstance();
        mChatPhotosStorageReference = mFirebaseStorage.getReference("chat section").child("chat photos").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH));

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        recyclerView = findViewById(R.id.recycler_view_message_activity);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        profile_image = findViewById(R.id.profile_image_message_activity);
        username = findViewById(R.id.usernameMessageActivity);
        btn_send = findViewById(R.id.btn_send_message_activity);
        text_send = findViewById(R.id.text_send_message_activity);
        image_picker = findViewById(R.id.imagePickerButton);


        intent = getIntent();
        userid = intent.getStringExtra("userid");
        userRole = intent.getStringExtra("userRole");

        image_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Fire an intent to show an image picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*/*");
                String[] mimeTypes = {"image/*", "application/pdf"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String msg = text_send.getText().toString();
                if (!msg.equals("")) {
                    dateAndTime = simpleDateFormat.format(calendar.getTime());
                    sendMessage(profileData.get(ProfileSharedPreferences.KEY_REGNO), userid, msg, null, dateAndTime);
                } else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("root").child("profiles").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child(userRole.toLowerCase(Locale.ROOT)).child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProfileClass user = snapshot.getValue(ProfileClass.class);
                username.setText(user.getName());

                if (user.getImageUrl().contains("default")) {
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageUrl()).into(profile_image);
                }

                readMessages(profileData.get(ProfileSharedPreferences.KEY_REGNO), userid, user.getImageUrl());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        seenMessage(userid);

    }

    private void seenMessage(String userid) {
        reference = FirebaseDatabase.getInstance().getReference("chat section").child("chats").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH));
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(profileData.get(ProfileSharedPreferences.KEY_REGNO)) && chat.getSender().equals(userid)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isSeen", 1);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void sendMessage(String sender, String receiver, String message, String imageUrl, String dateAndTime) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chat section");


        HashMap<String, Object> hashMap = new HashMap<>();

        if (imageUrl == null) {
            hashMap.put("sender", sender);
            hashMap.put("receiver", receiver);
            hashMap.put("message", message);
            hashMap.put("photoUrl", null);
            hashMap.put("dateAndTime", dateAndTime);
            hashMap.put("isSeen", 0);
        } else {
            hashMap.put("sender", sender);
            hashMap.put("receiver", receiver);
            hashMap.put("message", null);
            hashMap.put("photoUrl", imageUrl);
            hashMap.put("dateAndTime", dateAndTime);
            hashMap.put("isSeen", 0);
        }


        reference.child("chats").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).push().setValue(hashMap);

        // add user to chat fragment
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chat section").child("chatlist").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH))
                .child(profileData.get(ProfileSharedPreferences.KEY_REGNO))
                .child(userid);


        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef.child("id").setValue(userid);
                    Query query = FirebaseDatabase.getInstance().getReference("chat section").child("chatlist").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child(userid).child(profileData.get(ProfileSharedPreferences.KEY_REGNO));

                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                DatabaseReference rcvRef = FirebaseDatabase.getInstance().getReference("chat section").child("chatlist").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH))
                                        .child(userid)
                                        .child(profileData.get(ProfileSharedPreferences.KEY_REGNO));
                                rcvRef.child("id").setValue(profileData.get(ProfileSharedPreferences.KEY_REGNO));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        final String msg = message;

        reference = FirebaseDatabase.getInstance().getReference("root").child("profiles").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child(userRole.toLowerCase(Locale.ROOT)).child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProfileClass user = dataSnapshot.getValue(ProfileClass.class);
                if (notify) {
                    sendNotification(receiver, user.getName(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void sendNotification(String receiver, String username, String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("chat section").child("tokens").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH));
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(profileData.get(ProfileSharedPreferences.KEY_REGNO), R.mipmap.ic_launcher, username + ": " + message, "New Message",
                            userid);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 300) {
                                        if (response.body().success != 1) {
                                            Toast.makeText(MessageActivity.this, "Failed", Toast.LENGTH_SHORT);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void readMessages(String myid, String userid, String imageurl) {
        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("chat section").child("chats").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        mChat.add(chat);
                    }


                    messageAdapter = new MessageAdapter(MessageActivity.this, mChat, imageurl);
                    recyclerView.setAdapter(messageAdapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void status(String status) {
       DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("root").child("profiles").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child(profileData.get(ProfileSharedPreferences.KEY_ROLE).toLowerCase(Locale.ROOT)).child(profileData.get(ProfileSharedPreferences.KEY_REGNO));

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference1.updateChildren(hashMap);


    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case RC_PHOTO_PICKER:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Uploading", Toast.LENGTH_SHORT).show();
                    Uri selectedImageUri = data.getData();

                    // Get a reference to store file at chat_photos/<FILENAME>
                    final StorageReference photoRef = mChatPhotosStorageReference.child(String.valueOf(selectedImageUri));

                    // Upload file to Firebase Storage
                    photoRef.putFile(selectedImageUri)
                            .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }

                                    // Continue with the task to get the download URL
                                    return photoRef.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                dateAndTime = simpleDateFormat.format(calendar.getTime());

                                sendMessage(profileData.get(ProfileSharedPreferences.KEY_REGNO), userid, null, downloadUri.toString(), dateAndTime);

                            } else {
                                // Handle failures
                                // ...
                            }
                        }
                    });
                }
                break;

            default:
                Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onStart() {
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}
