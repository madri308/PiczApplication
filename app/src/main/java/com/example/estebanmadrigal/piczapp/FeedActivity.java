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

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class FeedActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    LinearLayout posts;
    Uri selectedImageUri;
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


    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
    public void openCam(View view) {

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
            Intent data = getIntent();
            setResult(RESULT_OK, data);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra("Bitmap",image);
            startActivity(intent);
        }if(requestCode == 5000 && resultCode == RESULT_OK){
            super.onActivityResult(requestCode, resultCode, data);
            try {
                if (requestCode == 500) {
                    // Get the url from data
                    selectedImageUri = data.getParcelableExtra("file");
                } else {
                    final Uri uri_data = data.getData();
                    // Get the path from the Uri
                    final String path = getPathFromURI(uri_data);
                    if (path != null) {
                        File f = new File(path);
                        selectedImageUri = Uri.fromFile(f);
                    }
                    // Set the image in
                    ImageView m = findViewById(R.id.imgView);
                    m.setImageURI(selectedImageUri);
                    }

            } catch (Exception e) {
                Log.e("FileSelectorActivity", "File select error", e);
            }
        }
        }

    public void openGallery(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),5000);
    }

}