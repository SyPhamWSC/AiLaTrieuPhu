package com.example.leojr.ailatrieuphu.play;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.leojr.ailatrieuphu.R;
/**
 * Created by Leo Jr on 16/10/2016.
 */

public class GameOverActivity extends Activity implements View.OnClickListener{

    private TextView tvMess;
    private Button btnRestart;
    private Button btnQuit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_layout);
        Intent intent = getIntent();
        String score = intent.getStringExtra("score");
        tvMess = (TextView) findViewById(R.id.tv_mess_game_over);
        tvMess.setText("You have got "+ score+ "VND");
        btnQuit = (Button) findViewById(R.id.btn_quit_game);
        btnRestart = (Button) findViewById(R.id.btn_restart);
        btnRestart.setOnClickListener(this);
        btnQuit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_quit_game:
                System.exit(0);
                break;
            case R.id.btn_restart:
                Intent intent = new Intent(GameOverActivity.this, GuideActivity.class);
                startActivity(intent);
                GameOverActivity.this.finish();
                break;
        }
    }
}
