package com.example.visak.hackothonproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button cameraIntent;
    Button userInputIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraIntent = (Button)findViewById(R.id.cameraIntent);
        userInputIntent = (Button)findViewById(R.id.userIntent);

        userInputIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,UserInput.class);
                intent.putExtra("flowValue",-1);
                startActivity(intent);
            }
        });
    }
}
