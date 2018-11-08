package com.example.estebanmadrigal.piczapp;

import android.app.Application;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;


import java.io.ByteArrayOutputStream;

@Entity(tableName = "Post")
public class Post  {
    @PrimaryKey(autoGenerate = true)
    private Integer postId;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] imagen;

    @ColumnInfo(name = "comment")
    private String comment;

    @ColumnInfo(name = "date")
    private String date;


    public Post(){

    }
    public Post(Integer id, Bitmap image, String cooment, String ndate){
        postId = id;
        imagen = this.toByte(image);
        comment = cooment;
        date = ndate;
    }


    public byte[] toByte(Bitmap imagen){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        imagen.compress(Bitmap.CompressFormat.JPEG,50,bos);
        return bos.toByteArray();
    }
    public Bitmap toBitmap(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    public Integer getPostId() {
        return postId;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
