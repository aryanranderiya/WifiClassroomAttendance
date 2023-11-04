package com.example.impromptussiphackathon2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class activity_session extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        ImageView gifImageView = findViewById(R.id.loading);

        Button btn = findViewById(R.id.end_session);

// Load the GIF using Glide
        Glide.with(this).asGif().load(R.drawable.loading).into(gifImageView);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(getApplicationContext(),AttendanceReportActivity.class);
                    startActivity(intent);
            }
    });
        }
}