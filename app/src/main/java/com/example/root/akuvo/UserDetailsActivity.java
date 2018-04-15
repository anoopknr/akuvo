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
    EditText dob;
    Calendar mCurrentDate;
    Spinner bloodGroupSelector,Preferred_Language;
    String DateOfBirth;

    ArrayAdapter<CharSequence> adapter,langList;
    String fileName = "UserDetails.dat";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        name = (EditText) findViewById(R.id.Name);
        email = (EditText) findViewById(R.id.Email);
        dob = (EditText) findViewById(R.id.calender);
        bloodGroupSelector = (Spinner) findViewById(R.id.bloodGroup);
        Preferred_Language = (Spinner) findViewById(R.id.language);

        /* Blood Group*/
        adapter = ArrayAdapter.createFromResource(this,R.array.blood_groups,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSelector.setAdapter(adapter);

        /* Language*/
        langList = ArrayAdapter.createFromResource(this,R.array.preferred_language,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Preferred_Language.setAdapter(langList);

        // save Button
        b_save = (Button) findViewById(R.id.b_save);

        /* Check for Passed Value From main Activity settings option*/
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            name.setText(extras.getString("currentUserName"));
            email.setText(extras.getString("currentUserEmailId"));
            dob.setText(extras.getString("currentUserDOB"));
            // setting date to save default
            DateOfBirth=extras.getString("currentUserDOB");

            ArrayAdapter bloodGroupArrayAdapter = (ArrayAdapter) bloodGroupSelector.getAdapter(); //cast to an ArrayAdapter
            int spinnerPosition = bloodGroupArrayAdapter.getPosition(extras.getString("currentUserBloodGroup"));
            bloodGroupSelector.setSelection(spinnerPosition);

            ArrayAdapter preferredLanguageArrayAdapter = (ArrayAdapter) Preferred_Language.getAdapter(); //cast to an ArrayAdapter
            spinnerPosition =  preferredLanguageArrayAdapter.getPosition(extras.getString("currentUserPreferredLanguage"));
            Preferred_Language.setSelection(spinnerPosition);

        }
        
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentDate = Calendar.getInstance();
                int year = mCurrentDate.get(Calendar.YEAR);
                int month = mCurrentDate.get(Calendar.MONTH);
                int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(UserDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        dob.setText(selectedDay+"-"+selectedMonth+"-"+selectedYear);

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

                saveFile(fileName, name.getText().toString()+"\n"+DateOfBirth+"\n"+bloodGroupSelector.getSelectedItem().toString()+"\n"+email.getText().toString()+"\n"+Preferred_Language.getSelectedItem().toString());
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
            Toast.makeText(UserDetailsActivity.this, "Settings Saved", Toast.LENGTH_SHORT).show();


        }catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(UserDetailsActivity.this, "Error saving file!",Toast.LENGTH_SHORT).show();
        }
    }
}
