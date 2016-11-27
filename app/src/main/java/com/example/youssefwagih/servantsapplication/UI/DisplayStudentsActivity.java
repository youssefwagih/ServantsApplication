package com.example.youssefwagih.servantsapplication.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.youssefwagih.servantsapplication.Business.Student;
import com.example.youssefwagih.servantsapplication.Business.SundaySchool;
import com.example.youssefwagih.servantsapplication.Data.ServiceDatabase;
import com.example.youssefwagih.servantsapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DisplayStudentsActivity extends AppCompatActivity {
    ListView studentsListView;
    Button addStudentButton;
    ArrayList<Student> studentsList;
    private String TAG = DisplayStudentsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_students);

        studentsListView = (ListView) findViewById(R.id.studentsListView);
        addStudentButton = (Button) findViewById(R.id.addStudentButton);

        final SundaySchool sundaySchool = new SundaySchool(this);
        Log.i("DisplayStudentsActivity", "opening database");
        List<Student> studentList = sundaySchool.getAllStudents();
        Log.i("DisplayStudentsActivity", "got studentlist");

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
                Intent intent = new Intent(DisplayStudentsActivity.this, StudentDetailsActivity.class);
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
