package com.example.youssefwagih.servantsapplication.Business;

import android.content.Context;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.List;

/**
 * Created by Youssef Wagih on 11/12/2016.
 */

public class SundaySchool {
    Firebase ref;
    public SundaySchool(Context context){
        Firebase.setAndroidContext(context);
        this.ref = new Firebase("https://usuffirebasetest.firebaseio.com/");
    }

    public List<Student> getAllStudents(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Student student = postSnapshot.getValue(Student.class);
                    Log.e("Get Data", student.getName());
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
        return null;
    }

    public void addStudent(Student student){
        String userId = ref.push().getKey();
        ref.child("students").child(userId).setValue(student);

    }

    public void deleteStudent(long id){
    }

    public void editStudent(long id){
    }
}
