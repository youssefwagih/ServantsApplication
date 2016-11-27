package com.example.youssefwagih.servantsapplication.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.youssefwagih.servantsapplication.Business.Student;
import com.example.youssefwagih.servantsapplication.Business.SundaySchool;
import com.example.youssefwagih.servantsapplication.R;
import com.google.android.gms.maps.model.LatLng;

public class AddStudentActivity extends Activity {
    private Button selectLocationButton;
    private Button addStudentButton;
    private EditText emailEditText;
    private EditText nameEditText;
    private EditText phoneNumberEditText;
    private  EditText homeAddressEditText;
    private LatLng selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        selectLocationButton = (Button) findViewById(R.id.selectLocationButton);
        addStudentButton = (Button) findViewById(R.id.addStudentButton);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        homeAddressEditText = (EditText) findViewById(R.id.addressEditText);

        addStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add student
                // Student student = new Student(nameEditText.getText().toString(),homeAddressEditText.getText().toString(), phoneNumberEditText.getText().toString(), selectedLocation,  emailEditText.getText().toString());
                Student student = new Student(nameEditText.getText().toString(),homeAddressEditText.getText().toString(), phoneNumberEditText.getText().toString(), selectedLocation,  emailEditText.getText().toString());
                SundaySchool ss = new SundaySchool(getApplicationContext());
                ss.addStudent(student);
                // return to Display Activity
                Intent intent = new Intent(AddStudentActivity.this, DisplayStudentsActivity.class);
                startActivity(intent);
            }
        });

        selectLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStudentActivity.this, EditStudentLocationMapActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // your code here version 2
                String s = "";
                selectedLocation = new LatLng(data.getExtras().getDouble("locationLat"), data.getExtras().getDouble("locationLng"));
                Log.i("AddStudentActivity", "Entering onActivityResult");
            }
        }
    }
}
