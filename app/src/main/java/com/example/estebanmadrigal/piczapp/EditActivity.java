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
        Post post = new Post(bitmap,comment);



        Intent intent = new Intent(this, FeedActivity.class);
        startActivity(intent);
    }
    public void blackAndWhite(View view){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, bitmap.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = bitmap.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                int gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);
                // use 128 as threshold, above -> white, below -> black
                if (gray > 128) {
                    gray = 255;
                }
                else{
                    gray = 0;
                }
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, gray, gray, gray));
            }
        }
        imageview.setImageBitmap(bmOut);
        bitmap=bmOut;
        imageview.setImageBitmap(bitmap);
    }
    public void blur(View view){

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


