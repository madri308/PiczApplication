package com.example.estebanmadrigal.piczapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EditActivity extends AppCompatActivity {

    Bitmap bitmap;
    String comment = "";
    EditText textview;
    ImageView imageview;
    private static final int PERMISSION_REQUEST_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        bitmap = FeedActivity.photo;
        imageview = findViewById(R.id.imageView);
        imageview.setImageBitmap(bitmap);
        textview = findViewById(R.id.comment);
    }
    private boolean checkPermission() {

        return ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ;
    }
    public void downloadImage(View view){
        if (!checkPermission()) {
            download();
        } else {
            if (checkPermission()) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }else {
                download();
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {

                boolean flag = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }
                if (flag) {
                    download();
                } else {
                    finish();
                }

            } else {
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public void download() {
        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, bitmap.toString() , bitmap.toString());
        Toast.makeText(this, "Image saved", Toast.LENGTH_LONG).show();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void up(View view){
        comment = textview.getText().toString();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        String date = (dtf.format(localDate));
        Integer postId = bitmap.getGenerationId();
        Post post = new Post(postId,bitmap,comment,date);

        DataBaseInitializer.insert(PiczDataBase.getPiczDataBase(this),post);

        Intent intent = new Intent(this, FeedActivity.class);
        startActivity(intent);
    }
    public void blackAndWhite(View view) {
        BlackAndWhiteFilter blackAndWhiteFilter = new BlackAndWhiteFilter();
        bitmap = blackAndWhiteFilter.applyFilter(bitmap);
        imageview.setImageBitmap(bitmap);
    }

    public void blur (View view){
        BlurFilter blurFilter = new BlurFilter();
        bitmap = blurFilter.applyFilter(bitmap);
        imageview.setImageBitmap(bitmap);
    }

    public void onSharpMasking(View view){


    }
    public void original(View view){
        bitmap = FeedActivity.photo;
        imageview.setImageBitmap(bitmap);
    }
    public void invented(View view){

    }
}


