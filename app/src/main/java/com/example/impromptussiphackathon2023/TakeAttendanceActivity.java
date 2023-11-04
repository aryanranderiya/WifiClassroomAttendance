package com.example.impromptussiphackathon2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class TakeAttendanceActivity extends AppCompatActivity {
    Spinner branchSpinner,groupSpinner,divisionSpinner,subjectSpinner;
    Button btn_startAttendanceSession;
    String[] branchArray,divisionArray, groupArray, subjectArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        Init();
        Buttons();
    }

    private void Buttons() {
        btn_startAttendanceSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelectionValid()) {

                    String selectedBranch = branchSpinner.getSelectedItem().toString();
                    String selectedDivision = divisionSpinner.getSelectedItem().toString();
                    String selectedGroup = groupSpinner.getSelectedItem().toString();
                    String selectedSubject = subjectSpinner.getSelectedItem().toString();

                    Intent intent = new Intent(getApplicationContext(), activity_session.class);

                    intent.putExtra("branch", selectedBranch);
                    intent.putExtra("division", selectedDivision);
                    intent.putExtra("group", selectedGroup);
                    intent.putExtra("subject", selectedSubject);

                    startActivity(intent);
                } else {
                    Toast.makeText(TakeAttendanceActivity.this, "Please select all three options", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isSelectionValid() {
        return (branchSpinner.getSelectedItemPosition() > 0) &&
                (subjectSpinner.getSelectedItemPosition() > 0) &&
                (groupSpinner.getSelectedItemPosition() > 0) &&
                (divisionSpinner.getSelectedItemPosition() > 0);
    }

    private void Init() {
        branchSpinner = findViewById(R.id.branchSpinner);
        groupSpinner = findViewById(R.id.groupSpinner);
        divisionSpinner = findViewById(R.id.divisionSpinner);
        subjectSpinner = findViewById(R.id.subjectSpinner);

        btn_startAttendanceSession = findViewById(R.id.btn_startAttendanceSession);
        
        branchArray = getResources().getStringArray(R.array.branch_array);
        ArrayAdapter<String> branchArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_list, branchArray);
        branchArrayAdapter.setDropDownViewResource(R.layout.spinner_list);
        branchSpinner.setAdapter(branchArrayAdapter);
        branchSpinner.setSelection(0, false);

        divisionArray = getResources().getStringArray(R.array.division_array);
        ArrayAdapter<String> divisionArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_list, divisionArray);
        divisionArrayAdapter.setDropDownViewResource(R.layout.spinner_list);
        divisionSpinner.setAdapter(divisionArrayAdapter);
        divisionSpinner.setSelection(0, false);

        groupArray = getResources().getStringArray(R.array.group_array);
        ArrayAdapter<String> groupArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_list, groupArray);
        groupArrayAdapter.setDropDownViewResource(R.layout.spinner_list);
        groupSpinner.setAdapter(groupArrayAdapter);
        groupSpinner.setSelection(0, false);

        subjectArray = getResources().getStringArray(R.array.subject_array);
        ArrayAdapter<String> subjectArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_list, subjectArray);
        subjectArrayAdapter.setDropDownViewResource(R.layout.spinner_list);
        subjectSpinner.setAdapter(subjectArrayAdapter);
        subjectSpinner.setSelection(0, false);
    }

}