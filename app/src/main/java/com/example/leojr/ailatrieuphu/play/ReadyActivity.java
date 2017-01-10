package com.example.leojr.ailatrieuphu.play;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.leojr.ailatrieuphu.R;
import com.example.leojr.ailatrieuphu.dialog.IReadyDialog;
import com.example.leojr.ailatrieuphu.dialog.ReadyDialog;

import java.util.Random;

public class ReadyActivity extends Activity implements IReadyDialog {

    private static final int UPDATE_BACKGROUND = 0;
    private static final int SHOW_DIALOG = 1;

    private TextView tv5;
    private TextView tv10;
    private long startTime;
    private boolean isSkip = false;

    private TextView tv15;
    private ReadyDialog readyDialog;
    private MediaPlayer mediaPlayer;
    private LinearLayout readyLayout;
    private Animation animation;
    private Thread thread;
    private Handler handler;
    private Random random;
    private MediaPlayer touchSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ready_layout);
        init();
        initComponents();
        readyDialog = new ReadyDialog(this);
        readyDialog.setiReadyDialog(this);
        readyDialog.setCanceledOnTouchOutside(false);

    }

    private void initComponents() {
        mediaPlayer.start();
        thread.start();
        handler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case UPDATE_BACKGROUND:
                        int arg = msg.arg1;
                        switch (arg){
                            case 5:
                                tv5.setBackgroundResource(R.drawable.money_2);
                                break;
                            case 10:
                                tv10.setBackgroundResource(R.drawable.money_2);
                                tv5.setBackgroundResource(R.drawable.money_3);
                                break;
                            case 15:
                                tv15.setBackgroundResource(R.drawable.money_2);
                                tv10.setBackgroundResource(R.drawable.money_3);
                                tv5.setBackgroundResource(R.drawable.money_3);
                                break;
                        }
                        break;
                    case SHOW_DIALOG:
                        readyDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                        readyDialog.show();
                        random = new Random();
                        int audio = random.nextInt(2);
                        mediaPlayer.release();
                        if (audio % 2 == 0) {
                            mediaPlayer = MediaPlayer.create(ReadyActivity.this, R.raw.ready_b);
                        } else {
                            mediaPlayer = MediaPlayer.create(ReadyActivity.this, R.raw.ready_c);
                        }
                        mediaPlayer.start();

                        break;
                }
            }
        };
    }

    public void init() {
        tv5 = (TextView) findViewById(R.id.tv_money05);
        tv10 = (TextView) findViewById(R.id.tv_money10);
        tv15 = (TextView) findViewById(R.id.tv_money15);

        startTime = System.currentTimeMillis();
        readyLayout = (LinearLayout) findViewById(R.id.ready_layout);
        animation = AnimationUtils.loadAnimation(this, R.anim.translate_to_right_bg);
        animation.start();
        readyLayout.setAnimation(animation);
        mediaPlayer = MediaPlayer.create(this, R.raw.luatchoi_c);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(System.currentTimeMillis() - startTime <= 6000&& isSkip == false){
                    if(System.currentTimeMillis() - startTime == 4500){
                        Message message = new Message();
                        message.what = UPDATE_BACKGROUND;
                        message.arg1 = 5;
                        message.setTarget(handler);
                        message.sendToTarget();
                    }
                    if(System.currentTimeMillis() - startTime == 4800){
                        Message message = new Message();
                        message.what = UPDATE_BACKGROUND;
                        message.arg1 = 10;
                        message.setTarget(handler);
                        message.sendToTarget();
                    }
                    if(System.currentTimeMillis() - startTime == 5200){
                        Message message = new Message();
                        message.what = UPDATE_BACKGROUND;
                        message.arg1 = 15;
                        message.setTarget(handler);
                        message.sendToTarget();
                    }
                }
                Message message = new Message();
                message.what = SHOW_DIALOG;
                message.setTarget(handler);
                message.sendToTarget();
            }
        });

    }

    @Override
    public void setClickBtnNo() {
        mediaPlayer.release();
        Intent mIntent = new Intent(this, GuideActivity.class);
        startActivity(mIntent);
        this.finish();
    }

    @Override
    public void setClickBtnYes() {
        if(touchSound != null){
            touchSound.release();
            touchSound = MediaPlayer.create(ReadyActivity.this, R.raw.touch_sound);
        }else {
            touchSound = MediaPlayer.create(ReadyActivity.this, R.raw.touch_sound);
        }
        touchSound.start();
        mediaPlayer.release();
        //Intent mIntent = new Intent(this, PlayActivity.class);
        Intent mIntent = new Intent(this, LoadingActivity.class);
        startActivity(mIntent);
        this.finish();
    }
}
