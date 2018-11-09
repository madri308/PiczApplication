package com.example.estebanmadrigal.piczapp;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import java.util.List;

@Database(entities = {Post.class},version = 1)

public abstract class PiczDataBase extends RoomDatabase {

    private static PiczDataBase ourInstance;

    public abstract PostsDao postsDao();

    public static PiczDataBase getPiczDataBase (Context context){
        if( ourInstance == null){
            ourInstance = Room.databaseBuilder(context.getApplicationContext(),PiczDataBase.class,"Post-database").allowMainThreadQueries().build();
        }
        return ourInstance;
    }
    public static void destroyInstance(){
        ourInstance=null;
    }
}
