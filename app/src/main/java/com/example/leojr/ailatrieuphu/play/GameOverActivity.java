package com.example.leojr.ailatrieuphu.play;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.leojr.ailatrieuphu.R;
public class GameOverActivity extends Activity implements View.OnClickListener {

    private static final String SCORE = "your_score";
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_layout);
        Intent intent = getIntent();
        int score = intent.getIntExtra(SCORE,0);
        TextView tvMess = (TextView) findViewById(R.id.tv_mess_game_over);
        tvMess.setText("You have got " + score + "VND");

        if(mediaPlayer!= null){
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(this, R.raw.lose);
        }else {
            mediaPlayer = MediaPlayer.create(this, R.raw.lose);
        }
        mediaPlayer.start();
        Button btnQuit = (Button) findViewById(R.id.btn_quit_game);
        Button btnRestart = (Button) findViewById(R.id.btn_restart);
        btnRestart.setOnClickListener(this);
        btnQuit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_quit_game:
                System.exit(0);
                break;
            case R.id.btn_restart:
                Intent intent = new Intent(GameOverActivity.this, GuideActivity.class);
                startActivity(intent);
                GameOverActivity.this.finish();
                mediaPlayer.release();
                break;
        }
    }
}
