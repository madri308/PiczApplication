package com.example.estebanmadrigal.piczapp;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


public class FeedActivity extends AppCompatActivity {
    private String fileName;
    private final int CAMERA_REQUEST_CODE = 1234;
    private final int SDCARD_REQUEST_CODE = 1235;
    static Bitmap photo;
    LinearLayout posts;
    Uri outputFileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        posts = findViewById(R.id.posts);

/*


            TextView comment = new TextView(this);
            comment.setId(View.generateViewId());
            comment.setText(content.getComment());
            comment.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            post.addView(comment);
            */
        //set.connect(comment.getId(), ConstraintSet.START, date.getId(), ConstraintSet.END);
        //set.connect(image.getId(), ConstraintSet.BOTTOM, comment.getId(), ConstraintSet.TOP);
        //set.applyTo(post);

        if(!(DataBaseInitializer.getAllPost(PiczDataBase.getPiczDataBase(this)) == null)){
            for(Post post:DataBaseInitializer.getAllPost(PiczDataBase.getPiczDataBase(this))){

                ConstraintLayout newPost = new ConstraintLayout(this);
                ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
                newPost.setLayoutParams(p);

                ImageView image = new ImageView(this);
                //image.setImageBitmap(Bitmap.createScaledBitmap(post.toBitmap(post.getImagen()),900, 900, true));
                image.setImageBitmap(post.toBitmap(post.getImagen()));
                image.setId(View.generateViewId());
                newPost.addView(image);

                TextView date = new TextView(this);
                date.setId(View.generateViewId());
                date.setText(post.getDate());
                date.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                newPost.addView(date);

                posts.addView(newPost);
            }
        }else{
            Toast.makeText(this,"No hay contenido que mostrar",Toast.LENGTH_LONG);
        }

    }
    public void openCam(View view) {

        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        outputFileUri = getOutputUri();
        if (outputFileUri != null) {
            camera.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(camera, SDCARD_REQUEST_CODE);
        } else{
            startActivityForResult(camera, CAMERA_REQUEST_CODE);
        }
    }
    @Nullable
    private Uri getOutputUri() {
// If sd card is available
        if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            fileName = "image" + System.currentTimeMillis() + ".jpg";
            path += "/folder_name";
            File file = new File(path);
            if (!file.isDirectory()) {
                file.mkdirs();
            }
            path += "/" + fileName;
            File imageFile = new File(path);
            if (!imageFile.exists()) {
                try {
                    imageFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return Uri.fromFile(imageFile);
        }else{ // If sd card is not available
            return null;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == this.RESULT_OK && requestCode == SDCARD_REQUEST_CODE){
            try {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outputFileUri);
                Intent intent = new Intent(this, EditActivity.class);
                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }
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