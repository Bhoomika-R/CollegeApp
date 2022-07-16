package com.example.mycollegeapp.ui.navigationDrawer.collegeMessages;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mycollegeapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class CollegeMessageAdapter extends ArrayAdapter<CollegeMessage> {
    private Context context;

    public CollegeMessageAdapter(Context context, int resource, List<CollegeMessage> objects) {
        super(context, resource, objects);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_college_message, parent, false);
        }

        TextView messageTextView =  convertView.findViewById(R.id.messageTextView);
        TextView authorTextView =  convertView.findViewById(R.id.nameTextView);
        TextView dateAndTimeTextView =  convertView.findViewById(R.id.dateAndTimeTextView);
        ImageView imageViewPhoto =  convertView.findViewById(R.id.imageViewPhoto);

        CollegeMessage message = getItem(position);
        if(message.getPhotoUrl().equals("null")){
           messageTextView.setText(message.getText());
        }
        else{
            StorageReference reference= FirebaseStorage.getInstance().getReferenceFromUrl(message.getPhotoUrl());
            reference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                @Override
                public void onSuccess(StorageMetadata storageMetadata) {
                   Log.v("Testing",storageMetadata.getContentType());
                   if (storageMetadata.getContentType().equals("application/pdf")) {
                        Log.v("Hello","Inside Pdf");
                        messageTextView.setVisibility(View.VISIBLE);
                        imageViewPhoto.setVisibility(View.GONE);
                        messageTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_picture_as_pdf_24, 0, 0, 0);
                        messageTextView.setText(storageMetadata.getName());
                        messageTextView.setElevation(10);
                        messageTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                pdfOpenintent.setDataAndType(Uri.parse(message.getPhotoUrl()), "application/pdf");

                                try {
                                    context.startActivity(pdfOpenintent);

                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(context, "Pdf error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    } else {
                        Log.v("Hello","Outside Pdf");
                        messageTextView.setVisibility(View.GONE);
                        imageViewPhoto.setVisibility(View.VISIBLE);
                        Glide.with(context)
                                .load(message.getPhotoUrl())
                                .into(imageViewPhoto);
                    }
                }
            });


        }
        authorTextView.setText(message.getName());
        dateAndTimeTextView.setText(message.getDateAndTime());


        return convertView;
    }
}
