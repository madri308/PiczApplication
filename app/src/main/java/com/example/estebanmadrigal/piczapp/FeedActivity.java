package com.example.estebanmadrigal.piczapp;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;




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

        if(!(DataBaseInitializer.getAllPost(PiczDataBase.getPiczDataBase(this)) == null)){
            int i = 0;
            for(Post post:DataBaseInitializer.getAllPost(PiczDataBase.getPiczDataBase(this))) {
                LinearLayout sort = new LinearLayout(this);
                sort.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                sort.setOrientation(LinearLayout.VERTICAL);
                sort.setGravity(Gravity.CENTER);

                TextView date = new TextView(this);
                date.setGravity(Gravity.CENTER);
                date.setText(post.getDate());
                date.setTextColor(Color.rgb(255,255,255));
                date.setBackgroundColor(Color.rgb(181,97,41));
                sort.addView(date,0);
                Drawable l = new BitmapDrawable(getResources(), post.toBitmap(post.getImagen()));
                Button thisPost = new Button(this);
                thisPost.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                thisPost.setBackground(l);
                final int finalI = i;
                thisPost.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getApplicationContext(), ThisPostActivity.class);
                        intent.putExtra("index", finalI);
                        startActivity(intent);

                    }
                });

                sort.addView(thisPost,1);

                TextView comment = new TextView(this);
                comment.setText(post.getComment());
                sort.addView(comment,2);


                posts.addView(sort,0);
                i++;
            }
        }else{
            Toast.makeText(this,"No hay contenido que mostrar",Toast.LENGTH_LONG);
        }

    }
    public void openCam(View view) {
        try{
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            outputFileUri = getOutputUri();
            if (outputFileUri != null) {
                camera.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(camera, SDCARD_REQUEST_CODE);
            } else{
                startActivityForResult(camera, CAMERA_REQUEST_CODE);
            }
        }catch(Exception e){
            Toast.makeText(this,"Active access to camera please",Toast.LENGTH_LONG);
        }

    }
    @Nullable
    private Uri getOutputUri() {
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
            return FileProvider.getUriForFile(this, "com.example.estebanmadrigal.provider", imageFile );
        }else{
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