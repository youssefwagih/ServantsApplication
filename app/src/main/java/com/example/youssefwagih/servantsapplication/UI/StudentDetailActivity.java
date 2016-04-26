package com.example.youssefwagih.servantsapplication.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.youssefwagih.servantsapplication.Business.Student;
import com.example.youssefwagih.servantsapplication.R;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

/**
 * Created by youssef wagih on 1/20/2016.
 */
public class StudentDetailActivity extends Activity {
    private Button displayLocationButton;
    private TextView nameTextView;
    private TextView addressTextView;
    private TextView emailTextView;
    private TextView phoneNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);
        displayLocationButton = (Button) findViewById(R.id.displayLocationButton);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        //addressTextView = (TextView) findViewById(R.id.AddressTextView);
        phoneNumberTextView = (TextView) findViewById(R.id.phoneNumberTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);

        // Get Student object from Intent Extras
        final Student student = new Student();
        Intent intent = getIntent();
        student.setID(intent.getIntExtra("ID", 0));
        student.setName(intent.getStringExtra("Name"));
        student.setAddress(intent.getStringExtra("Address"));
        student.setPhoneNumber(intent.getStringExtra("PhoneNumber"));
        student.setEmail(intent.getStringExtra("Email"));
        student.setLocation(new LatLng(intent.getDoubleExtra("Location_Latitude", 0), intent.getDoubleExtra("Location_Longitude", 0)));

        // Display Student
        nameTextView.setText(student.getName());
        emailTextView.setText(student.getEmail());
        phoneNumberTextView.setText(student.getPhoneNumber());
        //addressTextView.setText(student.getAddress());

        displayLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentDetailActivity.this, StudentLocationMapActivity.class);
                intent.putExtra("locationLat", student.getLocation().latitude);
                intent.putExtra("locationLng", student.getLocation().longitude );
                startActivity(intent);
            }
        });

    }
}
