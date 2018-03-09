package com.example.root.akuvo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioRecord;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecordedAudioListActivity extends AppCompatActivity {

    private ListView RecordList;
    private static final String AUDIO_SAVED_FOLDER = "Akuvo";
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
                if(FileContent=="") {
                    FileContent = TempString;
                }
                else{
                    FileContent=FileContent+","+TempString;
                }
            }
            br.close();
            RecordListFile.close();
            RecordListContent= FileContent.split(",");
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.audio_listview_adapter,R.id.ResourceName ,RecordListContent);
        RecordList.setAdapter(adapter);
        RecordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String SelectedIteam=((TextView) view).getText().toString();

                // get delete_audio_dialog.xml view

                LayoutInflater layoutInflater = LayoutInflater.from(RecordedAudioListActivity.this);
                View promptView = layoutInflater.inflate(R.layout.delete_audio_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RecordedAudioListActivity.this);
                alertDialogBuilder.setView(promptView);

                final TextView DeleteTextView = (TextView) promptView.findViewById(R.id.deletefilename);
                DeleteTextView.setText("Do you Want To Delete "+SelectedIteam);

                // setup a dialog window
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String filepath = Environment.getExternalStorageDirectory().getPath();
                                File TempFile = new File(filepath,AUDIO_SAVED_FOLDER+ "/" +SelectedIteam);
                                if(TempFile.delete()){
                                    try {
                                        FileOutputStream AudioRecordFile=openFileOutput("RecordList.txt",MODE_PRIVATE);
                                        OutputStreamWriter RecordWriter = new OutputStreamWriter(AudioRecordFile);
                                        int count=0;
                                        while (count<RecordListContent.length)
                                        {
                                            if(!RecordListContent[count].equals(SelectedIteam)){
                                                    RecordWriter.write(RecordListContent[count]+"\n");
                                                    Log.i("Writting To File "," Content : "+RecordListContent[count]+" Index : "+count);
                                                }
                                            count++;
                                        }
                                        RecordWriter.flush();
                                        RecordWriter.close();
                                        AudioRecordFile.flush();
                                        AudioRecordFile.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.i("File Error","Failed to Write To File");
                                    }
                                    Toast.makeText(RecordedAudioListActivity.this,SelectedIteam+" Deleted Sucesssfully",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(RecordedAudioListActivity.this,SelectedIteam+" Delete Failed",Toast.LENGTH_SHORT).show();
                                }
                                startActivity(new Intent(RecordedAudioListActivity.this,RecordedAudioListActivity.class));
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create an alert dialog
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RecordedAudioListActivity.this,MainActivity.class));
    }
}