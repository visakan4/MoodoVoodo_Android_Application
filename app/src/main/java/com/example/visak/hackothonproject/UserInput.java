package com.example.visak.hackothonproject;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class UserInput extends AppCompatActivity {
    public static int userInput = 0;
    SeekBar terminalSeekBar;
    ConstraintLayout relativeLayout;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);
        Intent intent = getIntent();
        relativeLayout = (ConstraintLayout) findViewById(R.id.relativeLayout);
        tv = (TextView) findViewById(R.id.textView);
        terminalSeekBar = (SeekBar) findViewById(R.id.seekBar);
        relativeLayout.setBackgroundResource(R.color.def);
        terminalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekBarProgress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBarProgress = i;
                userInput = i;
                 Toast.makeText( getApplicationContext(), "" + i, Toast.LENGTH_SHORT).show();

                 if (i == 0) {
                     relativeLayout.setBackgroundResource(R.color.red);
                 }
                 if(i==1){
                     relativeLayout.setBackgroundResource(R.color.blue);
                 }
                 if(i==2){
                     relativeLayout.setBackgroundResource(R.color.yellow);
                 }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                Toast.makeText( getApplicationContext(), "" + i, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                Toast.makeText( getApplicationContext(), "stopTracking", Toast.LENGTH_SHORT).show();

            }
        });



    }

    public void nextActBtnClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ConfirmationActivity.class);
        intent.putExtra("flowValue",0);
        startActivity(intent);
    }
}
