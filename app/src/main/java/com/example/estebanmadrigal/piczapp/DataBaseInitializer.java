package com.example.estebanmadrigal.piczapp;
import java.util.List;

public class DataBaseInitializer {
    private static final String TAG = DataBaseInitializer.class.getName();

    public static void insert (final PiczDataBase db, Post post){
        db.postsDao().insert(post);
    }
    public static List<Post> getAllPost(final PiczDataBase db){
        List<Post> postList = db.postsDao().getAll();
        return postList;
    }
}
