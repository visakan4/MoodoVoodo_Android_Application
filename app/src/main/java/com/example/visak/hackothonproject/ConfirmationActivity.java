package com.example.visak.hackothonproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class ConfirmationActivity extends AppCompatActivity {

    public static int userResponse;
    public static TextView tvQuest;
    public static Button btnPositive, btnNegative;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        Intent intent = getIntent();
        tvQuest = (TextView) findViewById(R.id.tvQuestion);
        btnPositive = (Button) findViewById(R.id.btnPositive);
        btnNegative = (Button) findViewById(R.id.btnNegative);
        if (intent.getExtras().getInt("flowValue")==1){
            userResponse = intent.getExtras().getInt("emotion");
            Log.d("EmotionValue",""+userResponse);
        }
        else {
            userResponse = UserInput.userInput;
        }

        if(userResponse == 2){
            //Happy
            tvQuest.setText("Here's some great music to dance to!");
            btnNegative.setVisibility(View.GONE);
            btnPositive.setVisibility(View.GONE);
            //Code to play music
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.happy);
            mediaPlayer.start();
        }

        if ((userResponse == 1)){
            //Sad
            tvQuest.setText("Would you like us to lift your spirits?");
            btnNegative.setText("Nah! I think I'll sulk for a bit.");
            btnPositive.setText("You can try.");

        }

        if ((userResponse == 0)){
            //Angry
            tvQuest.setText("Would you like us to lift your spirits?");
            btnNegative.setText("Nah! I want to punch a hole in the wall!");
            btnPositive.setText("Sure. I could use some help!");

        }
    }

    public void clkPositive(View view) {
          Intent intent = new Intent(ConfirmationActivity.this,PlayerActivity.class);
          startActivity(intent);
//        if (userResponse == 1)
//        {
//            //Write code to play uplifting music
//        }
//        if (userResponse == 0)
//        {
//            //Write code to play soothing music
//        }
    }

    public void clkNegative(View view) {
        Intent intent = new Intent(ConfirmationActivity.this,PlayerActivity.class);
        startActivity(intent);
//        if (userResponse == 1)
//        {
//            //Write code to play sad music
//        }
//        if (userResponse == 0)
//        {
//            //Write code to play heavy, violent music
//        }
    }
}
