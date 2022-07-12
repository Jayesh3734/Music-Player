package com.example.jkmusicplayer

import android.content.Intent;
import android.content.pm.InstrumentationInfo;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.util.TimeUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;


public class PlayMusicActivity extends AppCompatActivity {
    int Current;
    public float x1,x2;
    final int minDistance = 150;
    public static MediaPlayer mp;
    int flagforsamesong;
    int flag=1;
    int position;
    int islooping=0;
    ImageView ivPhoto;
    SeekBar seekBar;
    TextView tvCurrentTime,tvDuration;
    ImageButton btnseekback,btnpause,btnseekfwd,btnplay;
    ImageButton btnLoopOne,btnLoopNone,btnnextsong,btnprevsong;
    TextView tvTitle;
    Bitmap bitmap;
    Handler seek_Handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_play_music);

        Bundle data = getIntent().getBundleExtra("data");
        flagforsamesong = data.getInt("flag");
        position = data.getInt("position");
        ivPhoto = (ImageView) findViewById(R.id.iv_FullSize);
        btnLoopNone = (ImageButton) findViewById(R.id.btn_loopnone);
        btnLoopOne = (ImageButton) findViewById(R.id.btn_loopone);
        tvCurrentTime = (TextView) findViewById(R.id.tv_CurrentTime);
        tvDuration = (TextView) findViewById(R.id.tv_Duration);
        seekBar = (SeekBar) findViewById(R.id.sb_SeekBar);
        tvTitle = (TextView) findViewById(R.id.tv_Titile);
        tvTitle.setText(MainActivity.songsLists.get(position).getTitle());

        btnplay  = (ImageButton) findViewById(R.id.btn_Play);

        btnpause = (ImageButton) findViewById(R.id.btn_Pause);


        btnnextsong = (ImageButton) findViewById(R.id.btn_nextsong);
        btnprevsong = (ImageButton) findViewById(R.id.btn_prevsong);
        btnseekback = (ImageButton) findViewById(R.id.btn_SeekBack);

        btnseekfwd = (ImageButton) findViewById(R.id.btn_SeekFrwd);

        Log.d("1234567", "onCreate: "+ MainActivity.songsLists.get(position).getSongPath());
        bitmap = MainActivity.songsLists.get(position).getBitmap();

        if(flagforsamesong==1){
            // that means the same song is already playing
        }

        else{
            mp = new MediaPlayer();
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
