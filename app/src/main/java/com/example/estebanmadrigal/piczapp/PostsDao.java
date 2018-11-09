package com.example.estebanmadrigal.piczapp;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import java.util.List;

@Dao
public interface PostsDao {

    @Query("SELECT * FROM Post")
    List<Post> getAll();

    @Insert
    void insert(Post post);








}
