package com.example.impromptussiphackathon2023;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFacultyActivity extends AppCompatActivity implements TextWatcher {

    private Button loginButton;
    private TextInputLayout emailInputLayout, passwordInputLayout;
    private String loginPassword, loginEmail;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_faculty);

        initializeUI();
    }

    private void initializeUI() {
        loginButton = findViewById(R.id.btn_login);
        emailInputLayout = findViewById(R.id.edt_loginEmail);
        passwordInputLayout = findViewById(R.id.edt_loginPassword);

        emailInputLayout.getEditText().addTextChangedListener(this);
        passwordInputLayout.getEditText().addTextChangedListener(this);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        loginEmail = emailInputLayout.getEditText().getText().toString();
        loginPassword = passwordInputLayout.getEditText().getText().toString();

        if (loginEmail.isEmpty()) {
            emailInputLayout.setError("Email Cannot be Empty!");
            return;
        } else if (loginPassword.isEmpty()) {
            passwordInputLayout.setError("Password Cannot be Empty!");
            return;
        } else if (!isValidEmail(loginEmail)) {
            emailInputLayout.setError("Invalid Email or not from 'sot.pdpu.ac.in' domain");
            return;
        }

        showProgressDialog("Logging in...");

        mAuth.signInWithEmailAndPassword(loginEmail, loginPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getApplicationContext(), TakeAttendanceActivity.class));
                            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                            closeProgressDialog();
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            closeProgressDialog();
                        }
                    }
                });
    }

    private void showProgressDialog(String text) {
        progressDialog = new ProgressDialog(LoginFacultyActivity.this);
        progressDialog.setMessage(text);
        progressDialog.show();
    }

    private void closeProgressDialog() {
        progressDialog.dismiss();
    }
    private boolean isValidEmail(String email) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            String domain = email.substring(email.indexOf('@') + 1);
            return domain.equals("sot.pdpu.ac.in");
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        clearErrors();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        clearErrors();
    }

    @Override
    public void afterTextChanged(Editable editable) {
        clearErrors();
    }

    private void clearErrors() {
        emailInputLayout.setError(null);
        passwordInputLayout.setError(null);
    }
}
