package com.example.youssefwagih.servantsapplication.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.youssefwagih.servantsapplication.Business.Servant;
import com.example.youssefwagih.servantsapplication.Business.Student;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by youssef wagih on 1/17/2016.
 */
public class ServiceDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ServiceDB.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SERVANTS_CREATION_SQL = "CREATE TABLE \"Servants\" (\"ID\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ,\"PhoneNumber\" TEXT DEFAULT (null) , \"Name\" VARCHAR(50), \"Address\" VARCHAR)";
    private static final String TABLE_STUDENTS_CREATION_SQL = "CREATE TABLE \"Students\" (\"Name\" TEXT,\"Address\" TEXT,\"PhoneNumber\" TEXT DEFAULT (null) , \"Email\" TEXT DEFAULT (null) , \"Location_LNG\" double , \"Location_LAT\" double , \"ID\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE)";
    private static final String TABLE_STUDENT_SERVANT_SQL = "CREATE TABLE \"Student_Servant\" (\"Servant_ID\" INTEGER, \"Student_ID\" INTEGER, \"ID\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE )";

    static class Student_Servant_Table{
        static String ID = "ID";
        static String studentID = "Student_ID";
        static String servantID = "Servant_ID";
    }
    static class StudentTable{
        static String ID = "ID";
        static String name = "Name";
        static String address = "Address";
        static String email = "Email";
        static String phoneNumber = "PhoneNumber";
        static String Location_LAT = "Location_LAT";
        static String Location_LNG = "Location_LNG";
    }
    static class ServantTable{
        static String name = "Name";
        static String address = "Address";
        static String phoneNumber = "PhoneNumber";
    }

    public ServiceDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_SERVANTS_CREATION_SQL);
        db.execSQL(TABLE_STUDENTS_CREATION_SQL);
        db.execSQL(TABLE_STUDENT_SERVANT_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "Servants");
        db.execSQL("DROP TABLE IF EXISTS " + "Students");
        db.execSQL("DROP TABLE IF EXISTS " + "Student_Servant");

        onCreate(db);
    }

    /* Students Queries */
    public ArrayList<Student> getStudents(){
        ArrayList<Student> studentsList = new ArrayList<Student>();
        String query = "SELECT * FROM Students";
        Cursor cursor = this.getWritableDatabase().rawQuery(query, null);
        while (cursor.moveToNext()){
            Student currentStudent = new Student();
            currentStudent.setName(cursor.getString(cursor.getColumnIndex(StudentTable.name)));
            currentStudent.setAddress(cursor.getString(cursor.getColumnIndex(StudentTable.address)));
            currentStudent.setPhoneNumber(cursor.getString(cursor.getColumnIndex(StudentTable.phoneNumber)));
            studentsList.add(currentStudent);
        }

        return studentsList;
    }

    public void addStudent(Student student){
        getWritableDatabase().execSQL("INSERT INTO Students(Name, Address, PhoneNumber) VALUES (" + "'" + student.getName() + "'" + "," + "'" + student.getAddress() + "'" + "," + "'"  + student.getPhoneNumber()+ "'" + ")");
    }

    public ArrayList<Student> getStudentsByServantID(long servantID){
        ArrayList<Student> studentsList = new ArrayList<Student>();
        String query =  "SELECT Students.* FROM Students " +
                        "INNER JOIN Student_Servant on Students.ID = Student_Servant.Student_ID " +
                        "INNER JOIN Servants on Servants.ID = Student_Servant.Servant_ID " +
                        "WHERE Servants.ID = " + String.valueOf(servantID);
        Cursor cursor = this.getWritableDatabase().rawQuery(query, null);
        while (cursor.moveToNext()){
            Student currentStudent = new Student();
            currentStudent.setID(cursor.getLong(cursor.getColumnIndex(StudentTable.ID)));
            currentStudent.setName(cursor.getString(cursor.getColumnIndex(StudentTable.name)));
            currentStudent.setAddress(cursor.getString(cursor.getColumnIndex(StudentTable.address)));
            currentStudent.setPhoneNumber(cursor.getString(cursor.getColumnIndex(StudentTable.phoneNumber)));
            currentStudent.setEmail(cursor.getString(cursor.getColumnIndex(StudentTable.email)));
            currentStudent.setLocation(new LatLng(cursor.getDouble(cursor.getColumnIndex(StudentTable.Location_LAT)), cursor.getDouble(cursor.getColumnIndex(StudentTable.Location_LNG))));
            studentsList.add(currentStudent);
        }

        return studentsList;
    }

    public long addStudentByServantID(Student student, long servantID){
        ContentValues studentContentValues = new ContentValues();
        studentContentValues.put(StudentTable.name, student.getName());
        studentContentValues.put(StudentTable.address, student.getAddress());
        studentContentValues.put(StudentTable.phoneNumber, student.getPhoneNumber());
        studentContentValues.put(StudentTable.Location_LAT, student.getLocation().latitude);
        studentContentValues.put(StudentTable.Location_LNG, student.getLocation().longitude);
        student.setID(getWritableDatabase().insert("Students", null, studentContentValues));

        ContentValues student_servant_ContentValues = new ContentValues();
        student_servant_ContentValues.put(Student_Servant_Table.studentID, student.getID());
        student_servant_ContentValues.put(Student_Servant_Table.servantID, servantID);
        return getWritableDatabase().insert("Student_Servant", null, student_servant_ContentValues);
    }

    /* Servants Queries */
    public ArrayList<Servant> getServants(){
        ArrayList<Servant> servantsList = new ArrayList<Servant>();
        String query = "SELECT * FROM Servants";
        Cursor cursor = this.getWritableDatabase().rawQuery(query, null);
        while (cursor.moveToNext()){
            Servant currentServant = new Servant();
            currentServant.setName(cursor.getString(cursor.getColumnIndex(ServantTable.name)));
            currentServant.setAddress(cursor.getString(cursor.getColumnIndex(ServantTable.address)));
            currentServant.setPhoneNumber(cursor.getString(cursor.getColumnIndex(ServantTable.phoneNumber)));
            servantsList.add(currentServant);
        }

        return servantsList;
    }

    public void addServant(){
        // for testing only
        Servant servant = new Servant(1, "M.Mina", "Abbaasya", "122323");
        ContentValues servantContentValues = new ContentValues();
        servantContentValues.put(ServantTable.name, servant.getName());
        servantContentValues.put(ServantTable.address, servant.getAddress());
        servantContentValues.put(ServantTable.phoneNumber, servant.getPhoneNumber());
        servant.setID(getWritableDatabase().insert("Servants", null, servantContentValues));
    }
}
