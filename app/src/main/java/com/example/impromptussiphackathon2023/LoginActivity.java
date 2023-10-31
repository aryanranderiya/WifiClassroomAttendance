package com.example.impromptussiphackathon2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    Button btn_login_to_faculty, btn_login_to_student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login_to_faculty = findViewById(R.id.btn_login_to_faculty);
        btn_login_to_student = findViewById(R.id.btn_login_to_student);

        btn_login_to_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginStudentActivity.class));
            }
        });

        btn_login_to_faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginFacultyActivity.class));
            }
        });
    }
}