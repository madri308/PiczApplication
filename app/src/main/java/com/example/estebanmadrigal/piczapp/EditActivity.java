package com.example.estebanmadrigal.piczapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
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
import java.time.format.DateTimeFormatter;

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

    public void blackAndWhite(View view){
        BlackAndWhite currentBitMap = new BlackAndWhite();
        Bitmap bmout = currentBitMap.filter(bitmap);
        bitmap = bmout;
        imageview.setImageBitmap(bmout);
    }

    public void blur(View view){
        //GaussianBlur currentBitMap= new GaussianBlur();
        //float[][] result = currentBitMap.generateKernel((float)0.76);
        float[][] GaussianBlurConfig = new float[][] {
                { 1, 2, 1 },
                { 2, 4, 2 },
                { 1, 2, 1 }
        };

        MatrixConvolution convMatrix = new MatrixConvolution(3);
        convMatrix.applyConfig(GaussianBlurConfig);
        convMatrix.Factor = 16;
        convMatrix.Offset = 0;
        bitmap=convMatrix.computeConvolution(bitmap,convMatrix);
        imageview.setImageBitmap(bitmap);
    }

    public void onSharpMasking(View view){
        //UnSharpMaskFilter currentBitMap = new UnSharpMaskFilter();
        //float[][] result= currentBitMap.generateKernel((float)0.51);
        float[][] SharpConfig = new float[][] {
                { 0 , -1 , 0  },
                { -1, 5, -1 },
                { 0 , -1, 0  }
        };
        MatrixConvolution convMatrix = new MatrixConvolution(5);
        convMatrix.applyConfig(SharpConfig);
        bitmap=convMatrix.computeConvolution(bitmap,convMatrix);
        imageview.setImageBitmap(bitmap);
    }

    public void original(View view){
        bitmap = FeedActivity.photo;
        imageview.setImageBitmap(bitmap);
    }

    public void invented(View view){
        BlackAndWhite currentBitMap = new BlackAndWhite();
        Bitmap bmout = currentBitMap.filter(bitmap);
        bitmap = bmout;
        float[][] gradient = {
                { 1, 2, 1},
                { 0, 0, 0},
                { -1, -2, -1} };
        MatrixConvolution convMatrix= new MatrixConvolution(3);
        convMatrix.applyConfig(gradient);
        bitmap=convMatrix.computeConvolution(bitmap,convMatrix);
        imageview.setImageBitmap(bitmap);
    }
}


