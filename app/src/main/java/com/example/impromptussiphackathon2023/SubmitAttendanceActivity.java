package com.example.impromptussiphackathon2023;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
        if (isConnectedToSpecificNetwork("90:4c:81:d9:48:93")) {
            startActivity(new Intent(getApplicationContext(), SelectAttendanceActivity.class));
        } else {
            Toast.makeText(this, "Not connected to the University Network.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isConnectedToSpecificNetwork(String specificBSSID) {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String currentBSSID = wifiInfo.getBSSID();

            Log.d("aryanranderiya","fetched BSSID of phone: "+currentBSSID);

            if (specificBSSID.equals(currentBSSID)) {
                return true;
            }
        }
        return false;
    }
        private void Init () {
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

        public void locationAccess(){
            if (hasLocationPermissions()) {
                checkWifi();
            } else {
                requestLocationPermissions();
            }
        }

    private boolean hasLocationPermissions() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123; // You can use any value

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkWifi();
            } else {
                Toast.makeText(this, "Please enable Location permissions", Toast.LENGTH_SHORT).show();
            }
        }
        }
    }