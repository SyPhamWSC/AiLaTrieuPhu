package com.example.leojr.ailatrieuphu.play;


import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.leojr.ailatrieuphu.R;

public class LoadingActivity extends Activity{

    private static final int UPDATE_UI = 1;
    private LinearLayout loadingLayout;
    private Animation animation;
    private ImageView iv_rotate_loading;
    private MediaPlayer mediaPlayer;
    private Thread thread;
    private Handler handler;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        initViews();
        initComponents();

    }

    private void initComponents() {
        thread.start();

    }

    private void initViews() {

        loadingLayout = (LinearLayout) findViewById(R.id.loading_layout);
        animation = AnimationUtils.loadAnimation(this, R.anim.translate_to_right_bg);
        loadingLayout.setAnimation(animation);
        animation.start();

        iv_rotate_loading = (ImageView) findViewById(R.id.iv_loading);
        animation = AnimationUtils.loadAnimation(this, R.anim.rotate_loading);
        iv_rotate_loading.setAnimation(animation);
        animation.start();

        mediaPlayer = MediaPlayer.create(this, R.raw.gofind);
        mediaPlayer.start();

        startTime = System.currentTimeMillis();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if(System.currentTimeMillis() - startTime == 5500){
                        Intent mIntent = new Intent(LoadingActivity.this, PlayActivity.class);
                        startActivity(mIntent);
                        LoadingActivity.this.finish();
                    }
                }
            }
        });
    }
}
