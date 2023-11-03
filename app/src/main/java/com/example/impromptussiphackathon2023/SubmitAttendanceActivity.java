package com.example.impromptussiphackathon2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SubmitAttendanceActivity extends AppCompatActivity {
Button btn_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_attendance);

        Init();
        checkWifi();
    }

    private void checkWifi() {
        startActivity(new Intent(getApplicationContext(),));
    }

    private void Init() {
        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
    }

    public void logOut() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        FirebaseAuth.getInstance().signOut();
    }
}