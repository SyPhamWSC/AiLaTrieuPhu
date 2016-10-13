package com.example.leojr.ailatrieuphu.splashscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.example.leojr.ailatrieuphu.play.GuideActivity;
import com.example.leojr.ailatrieuphu.R;

/**
 * Created by Leo Jr on 12/10/2016.
 */

public class SplashScreen extends Activity {
    protected boolean active = true;
    protected int splashTime = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);

        Thread splashThread = new Thread(){
            @Override
            public void run() {
                try{
                    int waited = 0;
                    while(active&&(waited<splashTime)){
                        sleep(100);
                        if(active){
                            waited+=100;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    //finish();
                    Intent mIntent = new Intent(SplashScreen.this, GuideActivity.class);
                    SplashScreen.this.startActivity(mIntent);
                    SplashScreen.this.finish();

                }
            }
        };
        splashThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            active = false;
        }
        return true;
    }
}
