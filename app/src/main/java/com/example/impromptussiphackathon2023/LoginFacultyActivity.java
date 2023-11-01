package com.example.impromptussiphackathon2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class LoginFacultyActivity extends AppCompatActivity {

    Button btn_login;
    TextInputLayout edt_loginPassword, edt_loginEmail;
    String LoginPassword, LoginEmail;
    Boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_faculty);

        Init();
        Buttons();
        Validation();
        Login();
    }

    private void Login() {

    }

    private void Validation() {
        LoginEmail = edt_loginEmail.getEditText().getText().toString();
        LoginPassword = edt_loginPassword.getEditText().getText().toString();

        if (edt_loginEmail.hasFocus()) {
            if (!Patterns.EMAIL_ADDRESS.matcher(LoginEmail).matches() || LoginEmail.isEmpty()) {
                edt_loginEmail.setError("Please Enter Valid E-Mail Address!");
            }else {
                edt_loginEmail.setError(null);

                flag=true;
            }
        }
        else {
            flag = false;
        }
        if (edt_loginPassword.hasFocus()) {
            if (LoginPassword.isEmpty()) {
                edt_loginPassword.setError("Password cannot be empty!");
                flag = false;
            }else {
                edt_loginPassword.setError(null);
                flag=true;
            }
        }else {
            flag = false;
        }
    }


    private void Init() {
        btn_login = findViewById(R.id.btn_login);
        edt_loginEmail = findViewById(R.id.edt_loginEmail);
        edt_loginPassword = findViewById(R.id.edt_loginPassword);
    }

    private void Buttons() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TakeAttendaceActivity.class));
                Toast.makeText(getApplicationContext(),"Welcome Faculty Member!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}