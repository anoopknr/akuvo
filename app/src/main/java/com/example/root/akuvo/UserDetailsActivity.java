package com.example.root.akuvo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.Calendar;

public class UserDetailsActivity extends AppCompatActivity {

    EditText name,email;
    Button b_save;
    TextView bloodgrp,dob;
    EditText mDateEditText;
    Calendar mCurrentDate;
    Spinner spinner;
    String DateOfBirth;

    ArrayAdapter<CharSequence> adapter;
    String fileName = "UserDetails.dat";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        name = (EditText) findViewById(R.id.Name);
        email = (EditText) findViewById(R.id.Email);
        bloodgrp = (TextView) findViewById(R.id.BloodGrp);
        dob = (TextView) findViewById(R.id.DateOfBirth);
        spinner = (Spinner) findViewById(R.id.spinner);

        b_save = (Button) findViewById(R.id.b_save);

        adapter = ArrayAdapter.createFromResource(this,R.array.blood_groups,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        mDateEditText = (EditText) findViewById(R.id.calender);
        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentDate = Calendar.getInstance();
                int year = mCurrentDate.get(Calendar.YEAR);
                int month = mCurrentDate.get(Calendar.MONTH);
                int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(UserDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        mDateEditText.setText(selectedDay+"-"+selectedMonth+"-"+selectedYear);

                        mCurrentDate.set(selectedYear,selectedMonth,selectedDay);
                    }
                }, year, month, day);
                mDatePicker.show();
                DateOfBirth=day+"-"+month+"-"+year;
            }
        });

        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveFile(fileName, name.getText().toString()+"\n"+DateOfBirth+"\n"+spinner.getSelectedItem().toString()+"\n"+email.getText().toString());
                startActivity(new Intent(UserDetailsActivity.this,MainActivity.class));
            }
        });
    }
    public  void  saveFile(String file,String text)
    {
        try{
            FileOutputStream fos=openFileOutput(file, Context.MODE_PRIVATE);
            fos.write(text.getBytes());
            fos.close();
            Toast.makeText(UserDetailsActivity.this, "saved!", Toast.LENGTH_SHORT).show();


        }catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(UserDetailsActivity.this, "Error saving file!",Toast.LENGTH_SHORT).show();
        }
    }
}
