package com.example.root.akuvo;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public class RecordedAudioListActivity extends AppCompatActivity {

    private ListView RecordList;
    String[] RecordListContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorded_audio_list);

        RecordList = (ListView) findViewById(R.id.record_list_view);
        FileInputStream RecordListFile = null;
        try {
            RecordListFile = openFileInput("RecordList.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(RecordListFile));
            String FileContent="",TempString;
            while ((TempString= br.readLine())!=null){
                FileContent=FileContent+","+TempString;
            }
            RecordListContent= FileContent.split(",");
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.audio_listview_adapter,R.id.ResourceName ,RecordListContent);
        RecordList.setAdapter(adapter);
        RecordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String SelectedIteam=((TextView) view).getText().toString();
                Toast.makeText(RecordedAudioListActivity.this,SelectedIteam+" Recorded SucesssFully.",Toast.LENGTH_LONG).show();
            }
        });
    }
}