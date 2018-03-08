package com.example.root.akuvo;



import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;



public class MainActivity extends AppCompatActivity {

    TextView tv,tv3;
    ImageButton im,im2;
    Button b1,b2,b3,b4;
    SpeechRecognizer sr;
    Intent srint;
    MultiAutoCompleteTextView tv2;
    TextToSpeech tts;
    ConversationService service;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    String workspace_id,conversation_username,conversation_password,str;
    String Uname,Uemail,UBloodGroup,UDate;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv=(TextView) findViewById(R.id.tv);
        tv3=(TextView) findViewById(R.id.tv3);
        im=(ImageButton) findViewById(R.id.im);
        im2=(ImageButton) findViewById(R.id.im2);
        b1=(Button) findViewById(R.id.b1);
        b2=(Button) findViewById(R.id.b2);
        b3=(Button) findViewById(R.id.b3);
        b4=(Button) findViewById(R.id.b4);
        tv2=(MultiAutoCompleteTextView)findViewById(R.id.tv2);

        File file = new File(getApplicationContext().getFilesDir(),"UserDetails.dat");
        if(file.exists()){
            try{
                FileInputStream fis = openFileInput("UserDetails.dat");
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                Uname = br.readLine();
                UDate = br.readLine();
                UBloodGroup=br.readLine();
                Uemail=br.readLine();

            }catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Error Reading file!",Toast.LENGTH_SHORT).show();
            }

        }
        else{
            Toast.makeText(MainActivity.this,"File Not Found",Toast.LENGTH_SHORT).show();

            startActivity(new Intent(MainActivity.this,UserDetailsActivity.class));
        }
        /*
                Connnection to IBM  Watson Conversation Service
         */
        service = new ConversationService(ConversationService.VERSION_DATE_2017_02_03);
        service.setUsernameAndPassword("d0311543-0b7d-47d3-8563-cb5d8c2b28c5", "R71H3Oluaunl");

        /*
                Speech Recognition Class
         */
        sr=SpeechRecognizer.createSpeechRecognizer(this);
        srint = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        srint.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        srint.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        srint.putExtra(RecognizerIntent.EXTRA_PROMPT,"Akuvo");
        srint.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

        sr.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {
                Log.i( "onRmsChanged: ","hello" );

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });


        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.getDefault());
                }
            }
        });
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });
        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toSpeak = tv2.getText().toString();
                tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setText("");
                tv2.setText("");
                tv3.setText("");
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv2.setText(tv3.getText());
                tv3.setText("");
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MainActivity.this,AudioRecoderActivity.class));;
                startActivity(new Intent(MainActivity.this,RecordedAudioListActivity.class));
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AudioScannerActivity.class));
            }
        });

    }
    private void promptSpeechInput() {
        try {
            startActivityForResult(srint, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Not Supportted",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                            String SpeechResult=result.get(0);

                            tv.setText(SpeechResult);

                    try {
                        String WatsonResponse = new FindRespose().execute(SpeechResult).get();
                        tv3.setText(WatsonResponse);
                    }catch (Exception e) {
                    }
                }
                break;
            }

        }
    }
    private class FindRespose extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... request) {
            String ResponseStr="No Automated Respose";
            try {

                Log.i("test",request[0]);

                MessageRequest newMessage = new MessageRequest.Builder().inputText(request[0]).build();

                JSONObject ResposeJSON;

                MessageResponse response = service.message("669cc349-1dba-4aea-8d1b-ca78431f50ed", newMessage).execute();
                Log.i("test",response.toString());
                ResposeJSON = new JSONObject(response.toString());
                JSONObject WatsonOutput=ResposeJSON.getJSONObject("output");
                ResponseStr=WatsonOutput.getString("text");
                ResponseStr=ResponseStr.replaceAll("\"","").replaceAll("\\[", "").replaceAll("\\]","");
                ResponseStr = ResponseStr.replaceAll("UNAME",Uname).replaceAll("UDOB",UDate).replaceAll("UBLOOD",UBloodGroup).replaceAll("UEMAIL",Uemail);
            } catch (Exception e) {
                e.printStackTrace();
                //Toast NetError=Toast.makeText(getApplicationContext(),"Network Error !",Toast.LENGTH_SHORT);
               // NetError.show();
            }

            return ResponseStr;
        }

        @Override
        protected void onPostExecute(String result) {


             // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}
