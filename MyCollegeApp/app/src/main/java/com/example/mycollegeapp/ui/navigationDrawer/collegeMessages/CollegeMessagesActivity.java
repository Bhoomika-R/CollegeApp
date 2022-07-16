package com.example.mycollegeapp.ui.navigationDrawer.collegeMessages;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mycollegeapp.R;
import com.example.mycollegeapp.ui.networks.NetworkChangeListener;
import com.example.mycollegeapp.ui.profile.ProfileSharedPreferences;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CollegeMessagesActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    private static final int RC_PHOTO_PICKER = 2;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;

    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String dateAndTime;

    private ListView mMessageListView;
    private CollegeMessageAdapter mMessageAdapter;
    private EditText mMessageEditText;
    private ImageButton mSendButton,image_picker;
    private ProgressBar mProgressBar;
    private RelativeLayout relativeLayout;

    // Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private ProfileSharedPreferences profile;
    private HashMap<String,String> profileData;

    private List<CollegeMessage> collegeMessages;

    private SimpleDateFormat sdDate=new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat sdTime=new SimpleDateFormat("HH:mm");

    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_messages);

        profile=new ProfileSharedPreferences(this);
        profileData=profile.getUserProfile();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference("college messages").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH));
        mFirebaseAuth = FirebaseAuth.getInstance();
        // Initialize references to views
        mMessageListView = (ListView) findViewById(R.id.messageListView);
        mMessageEditText = (EditText) findViewById(R.id.text_send_message_activity);
        mSendButton =  findViewById(R.id.btn_send_message_activity);
        image_picker =  findViewById(R.id.imagePickerButton);
        mProgressBar=findViewById(R.id.progressBar);
        relativeLayout=findViewById(R.id.bottom_message_editor_message_activity);

        mProgressBar.setVisibility(View.GONE);

        // Initialize message ListView and its adapter
        collegeMessages = new ArrayList<>();
        mMessageAdapter = new CollegeMessageAdapter(this, R.layout.item_college_message, collegeMessages);

        mMessageListView.setAdapter(mMessageAdapter);


        mUsername=profileData.get(ProfileSharedPreferences.KEY_NAME);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy \nHH:mm");

        mFirebaseStorage = FirebaseStorage.getInstance();
        mChatPhotosStorageReference = mFirebaseStorage.getReference("college messages").child("college notices");

        if(profileData.get(ProfileSharedPreferences.KEY_ROLE).equals("Student")){
            relativeLayout.setVisibility(View.GONE);
            mMessageEditText.setVisibility(View.GONE);
            mSendButton.setVisibility(View.GONE);
        }

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

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    dateAndTime = simpleDateFormat.format(calendar.getTime());
                    sendMessage( "null", dateAndTime);
                // Clear input box
                mMessageEditText.setText("");
            }
        });


        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    CollegeMessage collegeMessage = new CollegeMessage(snapshot.child("text").getValue(String.class),snapshot.child("name").getValue(String.class),snapshot.child("photoUrl").getValue(String.class),snapshot.child("dateAndTime").getValue(String.class));
                    Collections.sort(collegeMessages, new Comparator<CollegeMessage>() {
                        @Override
                        public int compare(CollegeMessage o1, CollegeMessage o2) {
                            long t1,t2;
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                            Calendar calendar = Calendar.getInstance();
                            try {
                                Date d1=sdf.parse(o1.getDateAndTime()+":00");
                                calendar.setTime(d1);
                                t1=calendar.getTimeInMillis();
                                Date d2=sdf.parse(o2.getDateAndTime()+":00");
                                calendar.setTime(d2);
                                t2=calendar.getTimeInMillis();
                                return t1<t2?1:-1;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                           return 0;
                        }
                    });
                    mMessageAdapter.add(collegeMessage);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    mMessageAdapter.remove(snapshot.getValue(CollegeMessage.class));
                    Collections.sort(collegeMessages, new Comparator<CollegeMessage>() {
                        @Override
                        public int compare(CollegeMessage o1, CollegeMessage o2) {
                            long t1,t2;
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                            Calendar calendar = Calendar.getInstance();
                            try {
                                Date d1=sdf.parse(o1.getDateAndTime()+":00");
                                calendar.setTime(d1);
                                t1=calendar.getTimeInMillis();
                                Date d2=sdf.parse(o2.getDateAndTime()+":00");
                                calendar.setTime(d2);
                                t2=calendar.getTimeInMillis();
                                return t1<t2?1:-1;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return 0;
                        }
                    });
                    mMessageAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);

        }


    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        detachDatabaseReadListener();
        mMessageAdapter.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        attachDatabaseReadListener();
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

                                        sendMessage(  downloadUri.toString(), dateAndTime);

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

    private void sendMessage(String imageUrl, String dateAndTime) {
        Date curDate= Calendar.getInstance().getTime();
        Log.v("Date",sdDate.format(curDate));
        Log.v("Time",sdTime.format(curDate));

        CollegeMessage collegeMessage;

        if (imageUrl.equals("null")) {
             collegeMessage = new CollegeMessage(mMessageEditText.getText().toString(), mUsername,"null",dateAndTime);
        } else {
             collegeMessage = new CollegeMessage("null", mUsername,imageUrl,dateAndTime);
        }
        mMessagesDatabaseReference.push().setValue(collegeMessage);

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