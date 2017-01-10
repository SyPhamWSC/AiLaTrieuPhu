package com.example.leojr.ailatrieuphu.play;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.leojr.ailatrieuphu.R;
import com.example.leojr.ailatrieuphu.dialog.ExitDialog;

public class GuideActivity extends AppCompatActivity {

    private Button btnStart;
    private Button btnInfo;
    private Button btnExit;
    private MediaPlayer mediaPlayer;
    private MediaPlayer touchSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        //Ánh xạ
        btnStart = (Button) findViewById(R.id.btn_start_game);
        btnInfo = (Button) findViewById(R.id.btn_info);
        btnExit = (Button) findViewById(R.id.btn_exit);
        mediaPlayer = MediaPlayer.create(this, R.raw.bgmusic);
        mediaPlayer.start();

        //Bắt sự kiện
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(touchSound != null){
                    touchSound.release();
                    touchSound = MediaPlayer.create(GuideActivity.this, R.raw.touch_sound);
                }else {
                    touchSound = MediaPlayer.create(GuideActivity.this, R.raw.touch_sound);
                }
                touchSound.start();
                Intent mIntent = new Intent(GuideActivity.this, ReadyActivity.class);
                startActivity(mIntent);
                GuideActivity.this.finish();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitDialog exitDialog = new ExitDialog(GuideActivity.this);
                exitDialog.setCanceledOnTouchOutside(true);
                exitDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                exitDialog.show();
            }
        });


    }

    @Override
    public void finish() {
        super.finish();
        mediaPlayer.release();
    }

}
