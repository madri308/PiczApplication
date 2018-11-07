package com.example.estebanmadrigal.piczapp;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class FeedActivity extends AppCompatActivity {
    static Bitmap photo;
    private static final int CAMERA_REQUEST = 1888;
    LinearLayout posts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        posts = findViewById(R.id.posts);
        if(this.getIntent().getParcelableExtra("post") != null){
            Post content = this.getIntent().getParcelableExtra("post");


            ConstraintLayout post = new ConstraintLayout(this);
            post.setMaxWidth(MATCH_PARENT);
            post.setMaxHeight(MATCH_PARENT);

            ImageView image = new ImageView(this);
            image.setImageBitmap(content.imagen);
            image.setMinimumWidth(MATCH_PARENT);
            image.setMinimumHeight(WRAP_CONTENT);
            post.addView(image);

            TextView date = new TextView(this);
            date.setText(content.date);
            date.setMinimumWidth(MATCH_PARENT);
            date.setMinimumHeight(WRAP_CONTENT);
            post.addView(date);

            posts.addView(post);

        }

    }
    public void openCam(View view) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            photo = (Bitmap) extras.get("data");
            Intent intent = new Intent(this, EditActivity.class);
            startActivity(intent);
        }
        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                Intent intent = new Intent(this, EditActivity.class);
                startActivity(intent);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openGallery(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
    }

}