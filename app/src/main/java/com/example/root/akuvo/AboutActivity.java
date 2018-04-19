package com.example.root.akuvo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    private Button okButton;
    private TextView aboutUs,versionAndDate,sourceCode;
    private String aboutUsText,versionAndDateText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        okButton=(Button) findViewById(R.id.okButton);
        aboutUs=(TextView)findViewById(R.id.aboutUs);
        versionAndDate=(TextView)findViewById(R.id.versionAndDate);
        sourceCode=(TextView)findViewById(R.id.source_code);

        /*
                Edit aboutUs And versionAndDate Here
         */
        aboutUsText="Developed By \n\n Anoop Varghese \n Anto Bose \n Aswin B Nair \n Athul Jacob Mathew\n\n\n Uses Librarys \n\n Music CG \n Google Cloud Speech API \n IBM Watson Conversation";
        versionAndDateText="2018\n Version : 1.0";

        aboutUs.setText(aboutUsText);
        versionAndDate.setText(versionAndDateText);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutActivity.this,MainActivity.class));
            }
        });

        // Go to SourceCode website
        sourceCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://github.com/anoopknr/akuvo"));
                startActivity(intent);
            }
        });
    }
}
