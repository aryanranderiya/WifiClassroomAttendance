package com.example.impromptussiphackathon2023;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
        try {
            sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (isConnectedToSpecificNetwork("9c:53:22:3a:46:d9")) {
            startActivity(new Intent(getApplicationContext(), SelectAttendanceActivity.class));
        } else {
            Toast.makeText(this, "Not connected to the University Network.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isConnectedToSpecificNetwork(String specificBSSID) {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//            String currentBSSID = wifiInfo.getBSSID();
            String currentBSSID = specificBSSID; // temporary assign the current to the specific BSSID

            Log.d("aryanranderiya",currentBSSID);

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
    }