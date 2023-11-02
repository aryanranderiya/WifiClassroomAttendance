package com.example.impromptussiphackathon2023;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AttendanceReportActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report);
        Button myButton = findViewById(R.id.download);
        myButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("session_id_0");

                databaseReference.addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String json = dataSnapshot.getValue().toString();

                            // Create an Excel file
                            try {
                                File file = new File(Environment.getExternalStorageDirectory(), "firebase_data.xlsx");
                                Workbook workbook = new XSSFWorkbook();
                                Sheet sheet = workbook.createSheet("Firebase Data");

                                // Assuming the JSON is in a format that can be directly converted to Excel
                                Row headerRow = sheet.createRow(0);
                                headerRow.createCell(0).setCellValue("division");
                                headerRow.createCell(1).setCellValue("group");
                                headerRow.createCell(2).setCellValue("period_date");
                                headerRow.createCell(3).setCellValue("period_end_time");
                                headerRow.createCell(4).setCellValue("period_start_time");
                                headerRow.createCell(5).setCellValue("subject_faculty");
                                headerRow.createCell(6).setCellValue("subject_name");
                                // Add more cells for other keys as needed

                                // Parse and fill the Excel sheet
                                int rowNum = 1;
                                // You would parse your JSON and fill the Excel sheet here
                                // Example:
                                // for (JSONObject obj : yourParsedJsonArray) {
                                //    Row row = sheet.createRow(rowNum++);
                                //    row.createCell(0).setCellValue(obj.getString("key1"));
                                //    row.createCell(1).setCellValue(obj.getString("key2"));
                                //    // Add more cells for other keys as needed
                                // }

                                // Write the Excel file
                                FileOutputStream fileOut = new FileOutputStream(file);
                                workbook.write(fileOut);
                                fileOut.close();
                                workbook.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle errors if data retrieval fails
                    }
                });
            }
        });

    }
}
