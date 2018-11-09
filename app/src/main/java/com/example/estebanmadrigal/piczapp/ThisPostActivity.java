package com.example.estebanmadrigal.piczapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ThisPostActivity extends AppCompatActivity {
    LinearLayout content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_this_post);
        content = findViewById(R.id.content);
        int i = getIntent().getExtras().getInt("index");
        Post post = DataBaseInitializer.getAllPost(PiczDataBase.getPiczDataBase(this)).get(i);

        content.setGravity(Gravity.CENTER);

        TextView date = new TextView(this);
        date.setGravity(Gravity.CENTER);
        date.setText(post.getDate());
        date.setTextColor(Color.rgb(255,255,255));
        date.setBackgroundColor(Color.rgb(181,97,41));
        content.addView(date,0);

        ImageView photo = new ImageView(this);
        photo.setImageBitmap(post.toBitmap(post.getImagen()));
        content.addView(photo,1);

        TextView comment = new TextView(this);
        comment.setText(post.getComment());
        content.addView(comment,2);

    }
}
