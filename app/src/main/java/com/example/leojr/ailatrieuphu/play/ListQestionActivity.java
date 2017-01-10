package com.example.leojr.ailatrieuphu.play;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.example.leojr.ailatrieuphu.R;

import java.util.Random;


public class ListQestionActivity extends Activity {

    private LinearLayout linearLayout;
    private Animation animation;
    private MediaPlayer media;
    private Random random;
    private MediaPlayer backgroundMusic;
    private long startTime;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_question_layout);
        initView();
    }

    private void initView() {
        startTime = System.currentTimeMillis();

    }


    private void mediaOption(int level){
        if (level == 0){
            backgroundMusic = MediaPlayer.create(this, R.raw.background_music);
            backgroundMusic.setLooping(true);
            media =  MediaPlayer.create(this, R.raw.ques1);
            backgroundMusic.start();
            media.start();
        }else {
            backgroundMusic.release();
            media.release();
        }
        if(level < 5){
            backgroundMusic = MediaPlayer.create(this, R.raw.background_music);
            backgroundMusic.setLooping(true);
            backgroundMusic.start();
        }
        random = new Random();
        int randomNumber = random.nextInt(2);
        switch (level+1){
            case 1:
                if(randomNumber%2==0){
                    media = MediaPlayer.create(this, R.raw.ques1);
                }else {
                    media = MediaPlayer.create(this, R.raw.ques1_b);
                }
                break;
            case 2:
                if(randomNumber%2==0){
                    media = MediaPlayer.create(this, R.raw.ques2);
                }else {
                    media = MediaPlayer.create(this, R.raw.ques2_b);
                }
                break;
            case 3:
                if(randomNumber%2==0){
                    media = MediaPlayer.create(this, R.raw.ques3);
                }else {
                    media = MediaPlayer.create(this, R.raw.ques3_b);
                }
                break;
            case 4:
                if(randomNumber%2==0){
                    media = MediaPlayer.create(this, R.raw.ques4);
                }else {
                    media = MediaPlayer.create(this, R.raw.ques4_b);
                }
                break;
            case 5:
                if(randomNumber%2==0){
                    media = MediaPlayer.create(this, R.raw.ques5);
                }else {
                    media = MediaPlayer.create(this, R.raw.ques5_b);
                }
                break;
            case 6:
                if(randomNumber%2==0) {
                    media = MediaPlayer.create(this, R.raw.ques6);
                }
                break;
            case 7:
                if(randomNumber%2==0){
                    media = MediaPlayer.create(this, R.raw.ques7);
                }else {
                    media = MediaPlayer.create(this, R.raw.ques7_b);
                }
                break;
            case 8:
                if(randomNumber%2==0){
                    media = MediaPlayer.create(this, R.raw.ques8);
                }else {
                    media = MediaPlayer.create(this, R.raw.ques8_b);
                }
                break;
            case 9:
                if(randomNumber%2==0){
                    media = MediaPlayer.create(this, R.raw.ques9);
                }else {
                    media = MediaPlayer.create(this, R.raw.ques9_b);
                }
                break;
            case 10:
                media = MediaPlayer.create(this, R.raw.ques10);
                break;
            case 11:
                media = MediaPlayer.create(this, R.raw.ques11);
                break;
            case 12:
                media = MediaPlayer.create(this, R.raw.ques12);
                break;
            case 13:
                media = MediaPlayer.create(this, R.raw.ques13);
                break;
            case 14:
                media = MediaPlayer.create(this, R.raw.ques14);
                break;
            case 15:
                media = MediaPlayer.create(this, R.raw.ques15);
                break;
        }
        media.start();
    }
}
