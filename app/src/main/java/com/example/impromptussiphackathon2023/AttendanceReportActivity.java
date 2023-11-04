package com.example.impromptussiphackathon2023;

import static org.apache.poi.ss.usermodel.TableStyleType.headerRow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

public class AttendanceReportActivity extends AppCompatActivity{

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST = 1;
    private ProgressDialog progressDialog;
    private SimpleDateFormat sdf;
    private String formattedDateTime;
    private Button myButton;
    private FirebaseDatabase firebaseDatabase;
    private static Sheet sheet;
    static private int rowNum;
    private TextView text_absent_number, text_present_number,text_students_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report);

        Init();

        myButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(AttendanceReportActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(AttendanceReportActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AttendanceReportActivity.this,
                            new String[]{
                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                            },
                            WRITE_EXTERNAL_STORAGE_REQUEST);
                } else {
                    formattedDateTime = sdf.format(new Date());
                    progressDialog.setMessage("Attendance Report\n"+ formattedDateTime + "\nExcel File will be Downloaded to Downloads directory!");

                    progressDialog.show();
                    exportExcelFile();
                }
            }
        });
    }

    private void Init() {
        myButton = findViewById(R.id.download);
        text_absent_number = findViewById(R.id.text_absent_number);
        text_present_number = findViewById(R.id.text_present_number);
        text_students_number = findViewById(R.id.text_students_number);

        progressDialog = new ProgressDialog(AttendanceReportActivity.this);
        progressDialog.setTitle("Creating Excel File...");
        progressDialog.setCancelable(false);
        sdf = new SimpleDateFormat("dd MM yyyy HH:mm", Locale.US);
        firebaseDatabase = FirebaseDatabase.getInstance();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportExcelFile();
            } else {
                Toast.makeText(this, "Permission denied. Cannot create Excel file.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void exportExcelFile() {
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                        sdf = new SimpleDateFormat("dd-MM-yyyy_HH-mm", Locale.US);
                        formattedDateTime = sdf.format(new Date());

                        File file = new File(downloadDir, "AttendanceReport_" + formattedDateTime + ".xlsx");
                        Workbook workbook = new XSSFWorkbook();
                        sheet = workbook.createSheet("Data");
                        sheet.setDefaultColumnWidth(24);

                        Intent intent = getIntent();
                        String sessionId = intent.getStringExtra("sessionId");

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.child("AttendanceReport").child(sessionId).getChildren()) {
                            Row subjectInfoRow = sheet.createRow(rowNum++);
                            String key = dataSnapshot1.getKey();
                            String value = dataSnapshot1.getValue().toString();

                            subjectInfoRow.createCell(0).setCellValue(key);
                            subjectInfoRow.createCell(1).setCellValue(value);
                        }

                        // Add a blank row
                        rowNum++;

                        // Add header row for students
                        Row studentHeaderRow = sheet.createRow(rowNum++);
                        studentHeaderRow.createCell(0).setCellValue("Student Enrollment");
                        studentHeaderRow.createCell(1).setCellValue("Student Name");
                        studentHeaderRow.createCell(2).setCellValue("Student Email");
                        studentHeaderRow.createCell(3).setCellValue("Division");
                        studentHeaderRow.createCell(4).setCellValue("Group");
                        studentHeaderRow.createCell(5).setCellValue("Attendance");

                        Row studentRow;
                        for (DataSnapshot enrollmentSnapshot : dataSnapshot.child("Students").getChildren()) {
                            studentRow = sheet.createRow(rowNum++);
                            String enrollmentNo = enrollmentSnapshot.getKey().toString();
                            studentRow.createCell(0).setCellValue(enrollmentNo);
                            studentRow.createCell(1).setCellValue(dataSnapshot.child("Students").child(enrollmentNo).child("student_name").getValue(String.class));
                            studentRow.createCell(2).setCellValue(dataSnapshot.child("Students").child(enrollmentNo).child("student_email").getValue(String.class));
                            studentRow.createCell(3).setCellValue(dataSnapshot.child("Students").child(enrollmentNo).child("Division").getValue().toString());
                            studentRow.createCell(4).setCellValue(dataSnapshot.child("Students").child(enrollmentNo).child("Group").getValue().toString());

                            String attendanceStatus = dataSnapshot.child("Students").child(enrollmentNo)
                                    .child("Attendance")
                                    .child(sessionId)
                                    .getValue(String.class);

                            if(attendanceStatus == null || attendanceStatus.equals("")){
                                attendanceStatus = "A";
                                setAbsent(enrollmentNo,sessionId);
                            }
                            studentRow.createCell(5).setCellValue(attendanceStatus);
                        }

                        FileOutputStream fileOut = new FileOutputStream(file);
                        workbook.write(fileOut);
                        fileOut.close();
                        workbook.close();

                        Toast.makeText(getApplicationContext(), "Excel file created", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    } catch (IOException e) {
                        progressDialog.dismiss();
                        Log.d("aryanranderiya", e.toString());
                        Toast.makeText(getApplicationContext(), "Error creating Excel file", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Firebase Data retrieval error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setAbsent(String enrollmentNo, String sessionId) {
        DatabaseReference studentsReference = firebaseDatabase.getReference().child("Students")
                .child(enrollmentNo)
                .child("Attendance")
                .child(sessionId);

        studentsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    studentsReference.setValue("A")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        System.out.println("Successfully set record to Absent");
                                    } else {
                                        System.out.println("Could not set record to Absent");
                                    }
                                }
                            });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
