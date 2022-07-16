package com.example.mycollegeapp.ui.profile;

import static android.content.Context.MODE_PRIVATE;

import static com.example.mycollegeapp.ui.profile.ProfileSharedPreferences.KEY_IMAGE_URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mycollegeapp.R;
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

public class ProfileFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    private TextView tvName,tvRegNo,tvBranch,tvSem,tvPhone,tvEmail,tvGender,tvDOB,tvImage,tvRole;
    private LinearLayout linearLayout;
    private CircleImageView image_profile;
    private ImageView ivDobAndDesignation;

    private ProfileSharedPreferences profile;
    private HashMap<String,String> profileData;
    private SharedPreferences sharedPreferences;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profile=new ProfileSharedPreferences(getContext());

        storageReference = FirebaseStorage.getInstance().getReference("profile pictures");
        sharedPreferences= getContext().getSharedPreferences("userLoginSession",MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        tvName=view.findViewById(R.id.tvName);
        image_profile=view.findViewById(R.id.profileImageView);
        tvRegNo=view.findViewById(R.id.tvUSN);
        tvBranch=view.findViewById(R.id.tvDept);
        tvSem=view.findViewById(R.id.tvSem);
        tvPhone=view.findViewById(R.id.tvPhone);
        tvEmail=view.findViewById(R.id.tvEmail);
        tvGender=view.findViewById(R.id.tvGender);
        tvDOB=view.findViewById(R.id.tvDOB);
        tvImage=view.findViewById(R.id.tvImage);
        tvRole=view.findViewById(R.id.tvRole);
        ivDobAndDesignation=view.findViewById(R.id.ivDobAndDesignation);
        linearLayout=view.findViewById(R.id.linearLayout5);

        profileData=profile.getUserProfile();

        if (profileData.get(KEY_IMAGE_URL).equals("default")) {
            tvImage.setText(profileData.get(ProfileSharedPreferences.KEY_NAME_ABBR));
        } else {
            tvImage.setVisibility(View.GONE);
            Glide.with(getActivity()).load(profileData.get(KEY_IMAGE_URL)).into(image_profile);
        }

        tvName.setText(profileData.get(ProfileSharedPreferences.KEY_NAME));
        tvRegNo.setText(profileData.get(ProfileSharedPreferences.KEY_REGNO));
        tvBranch.setText(profileData.get(ProfileSharedPreferences.KEY_BRANCH));
        tvPhone.setText(profileData.get(ProfileSharedPreferences.KEY_PHONE));
        tvEmail.setText(profileData.get(ProfileSharedPreferences.KEY_EMAIL));
        tvGender.setText(profileData.get(ProfileSharedPreferences.KEY_GENDER));
        tvRole.setText(profileData.get(ProfileSharedPreferences.KEY_ROLE));
        if((profileData.get(ProfileSharedPreferences.KEY_ROLE)).equals("Student")){
            tvSem.setText(profileData.get(ProfileSharedPreferences.KEY_SEM)+" Semester");
            tvDOB.setText(profileData.get(ProfileSharedPreferences.KEY_DOB));
        }
        else{
            tvSem.setText(profileData.get(ProfileSharedPreferences.KEY_QUALIFICATIONS));
            ivDobAndDesignation.setImageResource(R.drawable.ic_baseline_label_important_24);
            tvDOB.setText(profileData.get(ProfileSharedPreferences.KEY_DESIGNATION));
        }

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        return view;
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(getContext());
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

                        reference = FirebaseDatabase.getInstance().getReference("root").child("profiles").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child(profileData.get(ProfileSharedPreferences.KEY_ROLE).toLowerCase(Locale.ROOT)).child(profileData.get(ProfileSharedPreferences.KEY_REGNO));
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageUrl", mUri);
                        reference.updateChildren(map);

                        Query updateProfile = FirebaseDatabase.getInstance().getReference("root").child("profiles").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child(profileData.get(ProfileSharedPreferences.KEY_ROLE).toLowerCase(Locale.ROOT)).orderByChild("regNo").equalTo(profileData.get(ProfileSharedPreferences.KEY_REGNO));
                        updateProfile.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                       String name,email,dob,gender,phone,role,branch,sem,imageUrl,regNo,qualifications,designation;

                                        name = snapshot.child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child("name").getValue(String.class);
                                        regNo = snapshot.child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child("regNo").getValue(String.class);
                                        email = snapshot.child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child("email").getValue(String.class);
                                        dob = snapshot.child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child("dob").getValue(String.class);
                                        gender = snapshot.child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child("gender").getValue(String.class);
                                        phone = snapshot.child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child("phone").getValue(String.class);
                                        role = snapshot.child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child("role").getValue(String.class);
                                        branch = snapshot.child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child("branch").getValue(String.class);
                                        imageUrl = snapshot.child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child("imageUrl").getValue(String.class);
                                        ProfileSharedPreferences profileNew = new ProfileSharedPreferences(getContext());
                                        if(profileData.get(ProfileSharedPreferences.KEY_ROLE).equals("Student")){
                                          sem = snapshot.child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child("sem").getValue(String.class);
                                          profileNew.createLoginSessionForStudent(name, email, phone, gender, role, regNo, dob, branch, sem, getNameAbbr(name),imageUrl);
                                        }
                                        else{
                                            designation = snapshot.child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child("designation").getValue(String.class);
                                            qualifications = snapshot.child(profileData.get(ProfileSharedPreferences.KEY_REGNO)).child("qualifications").getValue(String.class);
                                            profileNew.createLoginSessionForFaculty(name, email, phone, gender, role, regNo, dob, branch, getNameAbbr(name),imageUrl,qualifications,designation);
                                        }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        pd.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });

        } else {
            Toast.makeText(getContext(), "No image Selected", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key!=null){
        if(key.equals(KEY_IMAGE_URL)){
            tvImage.setVisibility(View.GONE);
            Log.v("hello profile","entered changing");
            Glide.with(getActivity()).load(sharedPreferences.getString(KEY_IMAGE_URL,null)).into(image_profile);
        }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}