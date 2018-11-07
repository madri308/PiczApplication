package com.example.estebanmadrigal.piczapp;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Post {
    Bitmap imagen;
    String comment;
    String date;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Post(Bitmap pImagen, String pComment){
        imagen = pImagen;
        comment = pComment;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        System.out.println(); //2016/11/16

    }
}
