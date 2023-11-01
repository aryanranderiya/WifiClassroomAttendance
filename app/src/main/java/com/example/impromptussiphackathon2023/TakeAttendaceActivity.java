package com.example.impromptussiphackathon2023;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

public class TakeAttendaceActivity extends AppCompatActivity {
    Spinner branchSpinner,groupSpinner,divisionSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendace);

        branchSpinner = findViewById(R.id.branchSpinner);
        groupSpinner = findViewById(R.id.groupSpinner);
        divisionSpinner = findViewById(R.id.divisionSpinner);

        String[] branchArray = getResources().getStringArray(R.array.branch_array);
        ArrayAdapter<String> branchArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_list, branchArray);
        branchArrayAdapter.setDropDownViewResource(R.layout.spinner_list);
        branchSpinner.setAdapter(branchArrayAdapter);
        branchSpinner.setSelection(0, false);

        String[] groupArray = getResources().getStringArray(R.array.group_array);
        ArrayAdapter<String> groupArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_list, groupArray);
        groupArrayAdapter.setDropDownViewResource(R.layout.spinner_list);
        groupSpinner.setAdapter(groupArrayAdapter);
        groupSpinner.setSelection(0, false);

        String[] divisionArray = getResources().getStringArray(R.array.division_array);
        ArrayAdapter<String> divisionArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_list, divisionArray);
        divisionArrayAdapter.setDropDownViewResource(R.layout.spinner_list);
        divisionSpinner.setAdapter(divisionArrayAdapter);
        divisionSpinner.setSelection(0, false);
    }
}