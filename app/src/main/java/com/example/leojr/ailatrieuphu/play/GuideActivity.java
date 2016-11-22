package com.example.leojr.ailatrieuphu.play;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.leojr.ailatrieuphu.R;

public class GuideActivity extends AppCompatActivity {

    private Button btnStart;
    private Button btnHighSoccer;
    private Button btnInfo;
    private Button btnSound;
    private Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        //Ánh xạ
        btnStart = (Button) findViewById(R.id.btn_start_game);
        btnHighSoccer = (Button) findViewById(R.id.btn_high_soccer);
        btnInfo = (Button) findViewById(R.id.btn_info);
        btnSound = (Button) findViewById(R.id.btn_sound);
        btnExit = (Button) findViewById(R.id.btn_exit);

        //Bắt sự kiện
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(GuideActivity.this, ReadyActivity.class);
                startActivity(mIntent);
                GuideActivity.this.finish();
            }
        });
        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSound.setBackgroundResource(R.drawable.music_off);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });


    }
}
