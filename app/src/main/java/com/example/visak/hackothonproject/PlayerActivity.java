package com.example.visak.hackothonproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.HashMap;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{
    Button b1;
    private YouTubePlayerView ytpv;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    HashMap<Integer,String> playListMap = new HashMap<>();
    public String Playlist_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ytpv = findViewById(R.id.youTubePlayerView);
        ytpv.initialize("AIzaSyAp-124WVjDXlMYBPyUzlM_bLMHx1ev81Q", this);
        playListMap.put(0,"PL0KNrm9eK17hULjzezYzggFGBjtbvIlxC");
        playListMap.put(1,"PLzzwfO_D01M4nNqJKR828zz6r2wGikC5a");
        playListMap.put(2,"PLinS5uF49IBo8HLKBDAjQaeiN4TjHi75Q");

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if(!b){
            Playlist_id = playListMap.get(ConfirmationActivity.userResponse);
            youTubePlayer.cuePlaylist(Playlist_id);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
