package com.example.root.akuvo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;

public class UserDetailsActivity extends AppCompatActivity {

    EditText editText,editText3;
    Button b_save;
    TextView textView,textView2,textView3,textView4,textView5;
    Spinner spinner,spinner2,spinner3,spinner4;

    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter <CharSequence> adapter1;
    ArrayAdapter <CharSequence> adapter2;
    ArrayAdapter <CharSequence> adapter3;
    String fileName = "UserDetails.dat";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        editText = (EditText) findViewById(R.id.editText);
        editText3 = (EditText) findViewById(R.id.editText3);
        textView = (TextView) findViewById(R.id.textView);
        textView5 = (TextView) findViewById(R.id.textView5);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner4 = (Spinner) findViewById(R.id.spinner4);

        b_save = (Button) findViewById(R.id.b_save);

        adapter = ArrayAdapter.createFromResource(this,R.array.blood_groups,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        adapter1 = ArrayAdapter.createFromResource(this,R.array.date,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter1);


        adapter2 = ArrayAdapter.createFromResource(this,R.array.month,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter2);


        adapter3 = ArrayAdapter.createFromResource(this,R.array.year,android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter3);

        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFile(fileName, editText.getText().toString()+"\n"+spinner2.getSelectedItem().toString()+"/"+spinner3.getSelectedItem().toString()+"/"+spinner4.getSelectedItem().toString()+"\n"+spinner.getSelectedItem().toString()+"\n"+editText3.getText().toString());
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
