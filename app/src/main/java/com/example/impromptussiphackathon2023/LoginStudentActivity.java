package com.example.impromptussiphackathon2023;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginStudentActivity extends AppCompatActivity implements TextWatcher {

    private TextInputLayout emailInputLayout, passwordInputLayout;
    private String loginEmail;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    private DatabaseReference studentsRef;
    private String enrollmentNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_student);

        init();
    }


    private void init() {
        Button loginButton = findViewById(R.id.btn_login);
        emailInputLayout = findViewById(R.id.edt_loginEmail);
        passwordInputLayout = findViewById(R.id.edt_loginPassword);

        emailInputLayout.getEditText().addTextChangedListener(this);
        passwordInputLayout.getEditText().addTextChangedListener(this);

        mAuth = FirebaseAuth.getInstance();
        studentsRef = FirebaseDatabase.getInstance().getReference("Students");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        loginEmail = emailInputLayout.getEditText().getText().toString();
        String loginPassword = passwordInputLayout.getEditText().getText().toString();

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
                            Log.d("aryanranderiya","login email "+loginEmail);
                            searchEnrollment();
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            closeProgressDialog();
                        }
                    }
                });
    }


    private void searchEnrollment() {
        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    String studentEmail = studentSnapshot.child("student_email").getValue(String.class);
                    if (studentEmail != null && studentEmail.equals(loginEmail)) {
                        enrollmentNo = studentSnapshot.getKey();
                        String hardwareAddress = fetchUniqueHardwareID();
                        storeMacAddressInFirebase(enrollmentNo, hardwareAddress);
                        Log.d("aryanranderiya","uniqueID: "+hardwareAddress);
                        return;
                    }
                }
                Toast.makeText(getApplicationContext(), "Email not found.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                closeProgressDialog();
            }
        });
    }

    private String fetchUniqueHardwareID() {
//        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//        if (manager != null) {
//            WifiInfo info = manager.getConnectionInfo();
//            return info.getMacAddress();
//        }
//        return null;
        return Secure.getString(getContentResolver(), Secure.ANDROID_ID);
    }

    private void storeMacAddressInFirebase(String enrollmentNo, String macAddress) {
        DatabaseReference enrollmentRef = studentsRef.child(enrollmentNo);

        enrollmentRef.child("hardware_id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String existingHardwareId = dataSnapshot.getValue(String.class);
                if (existingHardwareId == null) {
                    enrollmentRef.child("hardware_id").setValue(macAddress);
                    startActivity(new Intent(getApplicationContext(), SubmitAttendanceActivity.class));
                    Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                    closeProgressDialog();
                }
                else{
                    if(macAddress!=existingHardwareId){
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(LoginStudentActivity.this, "Cannot login on another device!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private void showProgressDialog(String text) {
        progressDialog = new ProgressDialog(LoginStudentActivity.this);
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
