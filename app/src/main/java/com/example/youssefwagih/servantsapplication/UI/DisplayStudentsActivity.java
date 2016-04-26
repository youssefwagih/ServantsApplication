package com.example.youssefwagih.servantsapplication.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.youssefwagih.servantsapplication.Business.Student;
import com.example.youssefwagih.servantsapplication.Data.ServiceDatabase;
import com.example.youssefwagih.servantsapplication.R;

import java.util.ArrayList;

public class DisplayStudentsActivity extends AppCompatActivity {
    ListView studentsListView;
    Button addStudentButton;
    ArrayList<Student> studentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_students);
        studentsListView = (ListView) findViewById(R.id.studentsListView);
        addStudentButton = (Button) findViewById(R.id.addStudentButton);

        final ServiceDatabase serviceDatabase = new ServiceDatabase(this);
        Log.i("DisplayStudentsActivity", "opening database");
        serviceDatabase.addServant();
        studentsList = serviceDatabase.getStudentsByServantID(1);
        Log.i("DisplayStudentsActivity", "got studentlist");
        //Log.i("DisplayStudentsActivity", studentsList.get(0).getName());
        ArrayAdapter<Student> studentsListAdapter = new ArrayAdapter<Student>(getApplicationContext(), R.layout.student_list_item_layout, studentsList);
        studentsListView.setAdapter(studentsListAdapter);

        addStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayStudentsActivity.this, AddStudentActivity.class);
                startActivity(intent);
            }
        });

        studentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student selectedStudent = studentsList.get((int)id);
                Intent intent = new Intent(DisplayStudentsActivity.this, StudentDetailActivity.class);
                intent.putExtra("ID",selectedStudent.getID());
                intent.putExtra("Name", selectedStudent.getName());
                intent.putExtra("Address", selectedStudent.getAddress());
                intent.putExtra("Email", selectedStudent.getEmail());
                intent.putExtra("PhoneNumber", selectedStudent.getPhoneNumber());
                intent.putExtra("Location_Latitude", selectedStudent.getLocation().latitude);
                intent.putExtra("Location_Longitude", selectedStudent.getLocation().longitude);
                startActivity(intent);
            }
        });


    }

}
