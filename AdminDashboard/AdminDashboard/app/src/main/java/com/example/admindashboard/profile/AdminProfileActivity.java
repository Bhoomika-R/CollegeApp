package com.example.admindashboard.profile;

import static com.example.admindashboard.profile.ProfileSharedPreferences.KEY_IMAGE_URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admindashboard.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminProfileActivity extends AppCompatActivity {
    private TextView tvName,tvRegNo,tvBranch,tvPhone,tvEmail,tvGender,tvDOB,tvImage,tvRole;
    private LinearLayout linearLayout;
    private CircleImageView image_profile;

    private ProfileSharedPreferences profile;
    private SharedPreferences.OnSharedPreferenceChangeListener spListener;
    private HashMap<String,String> profileData;
    private SharedPreferences sharedPreferences;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        profile=new ProfileSharedPreferences(this);

        storageReference = FirebaseStorage.getInstance().getReference("profile pictures");
        sharedPreferences= getSharedPreferences("userLoginSession",MODE_PRIVATE);
        spListener=new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(key!=null){
                    if(key.equals(KEY_IMAGE_URL)){
                        tvImage.setVisibility(View.GONE);
                        Log.v("hello profile","entered changing");
                        Glide.with(AdminProfileActivity.this).load(sharedPreferences.getString(KEY_IMAGE_URL,null)).into(image_profile);
                    }
                }
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(spListener);

        tvName=findViewById(R.id.tvName);
        image_profile=findViewById(R.id.profileImageView);
        tvRegNo=findViewById(R.id.tvUSN);
        tvBranch=findViewById(R.id.tvDept);
        tvPhone=findViewById(R.id.tvPhone);
        tvEmail=findViewById(R.id.tvEmail);
        tvGender=findViewById(R.id.tvGender);
        tvDOB=findViewById(R.id.tvDOB);
        tvImage=findViewById(R.id.tvImage);
        tvRole=findViewById(R.id.tvRole);
        linearLayout=findViewById(R.id.linearLayout5);

        profileData=profile.getUserProfile();

        if (profileData.get(KEY_IMAGE_URL).equals("default")) {
            tvImage.setText(profileData.get(ProfileSharedPreferences.KEY_NAME_ABBR));
        } else {
            tvImage.setVisibility(View.GONE);
            Glide.with(this).load(profileData.get(KEY_IMAGE_URL)).into(image_profile);
        }

        tvName.setText(profileData.get(ProfileSharedPreferences.KEY_NAME));
        tvRegNo.setText(profileData.get(ProfileSharedPreferences.KEY_REGNO));
        tvBranch.setText(profileData.get(ProfileSharedPreferences.KEY_BRANCH));
        tvPhone.setText(profileData.get(ProfileSharedPreferences.KEY_PHONE));
        tvEmail.setText(profileData.get(ProfileSharedPreferences.KEY_EMAIL));
        tvGender.setText(profileData.get(ProfileSharedPreferences.KEY_GENDER));
        tvRole.setText(profileData.get(ProfileSharedPreferences.KEY_ROLE));
        tvDOB.setText(profileData.get(ProfileSharedPreferences.KEY_DOB));

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();


        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask((Continuation<UploadTask.TaskSnapshot, Task<Uri>>) task -> {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        return fileReference.getDownloadUrl();
                    }


            ).addOnCompleteListener(new OnCompleteListener<Uri>() {

                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Uri dowloadUri = (Uri) task.getResult();
                        String mUri = dowloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("admins").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child(profileData.get(ProfileSharedPreferences.KEY_REGNO));
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageUrl", mUri);
                        reference.updateChildren(map);

                        Query updateProfile = FirebaseDatabase.getInstance().getReference("admins").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).orderByChild("regNo").equalTo(profileData.get(ProfileSharedPreferences.KEY_REGNO));
                        updateProfile.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String name,email,dob,gender,phone,role,branch,imageUrl,regNo;

                                    name = snapshot.child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child("name").getValue(String.class);
                                    regNo = snapshot.child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child("regNo").getValue(String.class);
                                    email = snapshot.child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child("email").getValue(String.class);
                                    dob = snapshot.child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child("dob").getValue(String.class);
                                    gender = snapshot.child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child("gender").getValue(String.class);
                                    phone = snapshot.child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child("phone").getValue(String.class);
                                    role = snapshot.child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child("role").getValue(String.class);
                                    branch = snapshot.child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child("branch").getValue(String.class);
                                    imageUrl = snapshot.child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child("imageUrl").getValue(String.class);
                                    ProfileSharedPreferences profileNew = new ProfileSharedPreferences(AdminProfileActivity.this);
                                    profileNew.createLoginSession(name, email, phone, gender, regNo, dob, branch,role, getNameAbbr(name),imageUrl);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        pd.dismiss();
                    } else {
                        Toast.makeText(AdminProfileActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
                    Toast.makeText(AdminProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });

        } else {
            Toast.makeText(AdminProfileActivity.this, "No image Selected", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(AdminProfileActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }
    private String getNameAbbr(String name) {
        StringBuilder initials = new StringBuilder();
        for (String s : name.split(" ")) {
            initials.append(s.charAt(0));
        }
        String temp = initials.toString().toUpperCase(Locale.ROOT).trim();
        StringBuilder imageText = new StringBuilder();
        if (temp.length() == 1)
            imageText.append(temp.charAt(0));
        else {
            imageText.append(temp.charAt(0));
            imageText.append(temp.charAt(1));
        }

        return imageText.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(spListener);
    }
}