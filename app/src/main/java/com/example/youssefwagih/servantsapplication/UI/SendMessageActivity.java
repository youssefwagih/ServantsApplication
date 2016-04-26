package com.example.youssefwagih.servantsapplication.UI;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.youssefwagih.servantsapplication.Business.Student;
import com.example.youssefwagih.servantsapplication.Data.ServiceDatabase;
import com.example.youssefwagih.servantsapplication.R;

import java.util.ArrayList;

public class SendMessageActivity extends Activity {
    private Button sendMessageButton;
    private EditText contentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        contentEditText = (EditText) findViewById(R.id.contextEditText);
        sendMessageButton = (Button) findViewById(R.id.sendMessageButton);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceDatabase serviceDatabase = new ServiceDatabase(getApplicationContext());
                ArrayList<Student> studentsList = serviceDatabase.getStudents();
                if (!studentsList.isEmpty()) {
                    for (Student student : studentsList) {
                        try {
                            SmsManager.getDefault().sendTextMessage(student.getPhoneNumber(), null, contentEditText.getText().toString(), null, null);
                        } catch (Exception e) {
                            Log.i("Sending message", "Sending Message failed");
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Make sure there that you have students", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
