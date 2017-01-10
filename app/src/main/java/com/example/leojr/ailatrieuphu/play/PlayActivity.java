package com.example.leojr.ailatrieuphu.play;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leojr.ailatrieuphu.R;
import com.example.leojr.ailatrieuphu.database.DatabaseManager;
import com.example.leojr.ailatrieuphu.database.Question;
import com.example.leojr.ailatrieuphu.dialog.CallMeDialog;
import com.example.leojr.ailatrieuphu.dialog.GameOverDialog;
import com.example.leojr.ailatrieuphu.dialog.HelpFromViewersDialog;
import com.example.leojr.ailatrieuphu.dialog.I5050Dialog;
import com.example.leojr.ailatrieuphu.dialog.IGameOverDialog;
import com.example.leojr.ailatrieuphu.dialog.ITimeOutDialog;
import com.example.leojr.ailatrieuphu.dialog.TimeOutDialog;

import java.io.IOException;
import java.util.List;
import java.util.Random;


public class PlayActivity extends Activity implements View.OnClickListener, ITimeOutDialog ,IGameOverDialog{

    private static final String TAG = "PlayActivity";
    private static final int ANSWER_TRUE = 1;
    private static final int ANSWER_WRONG = 2;
    private static final int UPDATE_TIME_PLAY = 3;
    private static final int SHOW_DIALOG_TIME_OUT = 4;
    private static final String SCORE = "your_score";

    private TextView tvLevel;
    private TextView tvQuestion;
    private TextView tvCaseA;
    private TextView tvCaseB;
    private TextView tvCaseC;
    private TextView tvcaseD;
    private TextView tvTime;
    private Button btnPhone;
    private Button btn50_50;
    private Button btnPeople;

    private MediaPlayer backgroundMusic;
    private MediaPlayer media;
    private MediaPlayer choiceMedia;
    private MediaPlayer answerMedia;
    private MediaPlayer helpMedia;
    private MediaPlayer outOfTime;

    private int trueCase;
    private int level = 0;
    private int yourChoice;
    private int timePlay;
    private boolean isPause;
    private int score;
    private Score mScore;
    private TimeOutDialog timeOutDialog;
    private GameOverDialog gameOverDialog;
    private Random random;

    private Animation blinkAnswer;
    private Animation blinkAnswerFalse;
    private Animation blinkWaitAnswer;
    private Handler handler;
    List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_activity_layout);

        initView();
        setNewQuestion(level);

        tvCaseA.setOnClickListener(this);
        tvCaseB.setOnClickListener(this);
        tvCaseC.setOnClickListener(this);
        tvcaseD.setOnClickListener(this);
        initComponent();


    }

    //Ánh xạ và tạo cơ sở dữ liệu cho Game
    private void initView() {
        tvLevel = (TextView) findViewById(R.id.tv_level);
        tvQuestion = (TextView) findViewById(R.id.tv_question);
        tvCaseA = (TextView) findViewById(R.id.tv_caseA);
        tvCaseB = (TextView) findViewById(R.id.tv_caseB);
        tvCaseC = (TextView) findViewById(R.id.tv_caseC);
        tvcaseD = (TextView) findViewById(R.id.tv_caseD);
        tvTime = (TextView) findViewById(R.id.tv_clock);
        btnPhone = (Button) findViewById(R.id.btn_phone);
        btn50_50 = (Button) findViewById(R.id.btn_50_50);
        btnPeople = (Button) findViewById(R.id.btn_people);
        timeOutDialog = new TimeOutDialog(this);
        gameOverDialog = new GameOverDialog(this);

        blinkAnswer = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        blinkAnswerFalse = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink_false);
        blinkWaitAnswer = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink_wait_answer);
        blinkAnswer.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                setNewQuestion(level);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        blinkAnswerFalse.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(outOfTime != null){
                    outOfTime.release();
                    outOfTime = MediaPlayer.create(PlayActivity.this, R.raw.out_of_time);
                }else {
                    outOfTime = MediaPlayer.create(PlayActivity.this, R.raw.out_of_time);
                }
                outOfTime.start();
                gameOverDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                gameOverDialog.show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        btnPhone.setOnClickListener(this);
        btn50_50.setOnClickListener(this);
        btnPeople.setOnClickListener(this);

        /*
        Connect to database
         */
        DatabaseManager db = new DatabaseManager(this);
        try {
            db.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        questionList = db.get15Question();
    }

    private void initComponent() {
        timeOutDialog.setCanceledOnTouchOutside(false);
        timeOutDialog.setiTimeOutDialog(this);
        gameOverDialog.setiGameOverDialog(this);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ANSWER_TRUE:
                        level += 1;
                        sleepTime(3000);
                        animationTrueAnswer(trueCase);
                        break;
                    case ANSWER_WRONG:
                        sleepTime(3000);
                        animationFalseAnswer(trueCase, yourChoice);
                        break;
                    case UPDATE_TIME_PLAY:
                        tvTime.setText(msg.arg1 + "");
                        break;
                    case SHOW_DIALOG_TIME_OUT:
                        if(outOfTime != null){
                            outOfTime.release();
                            outOfTime = MediaPlayer.create(PlayActivity.this, R.raw.out_of_time);
                        }else {
                            outOfTime = MediaPlayer.create(PlayActivity.this, R.raw.out_of_time);
                        }
                        outOfTime.start();
                        timeOutDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                        timeOutDialog.show();
                        break;
                }
            }
        };
    }

    public void setNewQuestion(int level) {
        //Level trong khoảng 0-14 nên hiển thị tên câu hỏi phải cộng thêm 1;
        int levels = level + 1;
        //Chuyển đổi sang dạng String để setText cho textView hiển thị câu hỏi
        String levelsContent = Integer.toString(levels);

        mediaOption(level);
        isPause = false;
        tvLevel.setText("Câu " + levelsContent);
        tvTime.setText("30");
        tvQuestion.setText(questionList.get(level).getContent());
        tvCaseA.setText("A. " + questionList.get(level).getCaseA());
        tvCaseB.setText("B. " + questionList.get(level).getCaseB());
        tvCaseC.setText("C. " + questionList.get(level).getCaseC());
        tvcaseD.setText("D. " + questionList.get(level).getCaseD());
        tvTime.setText("30");
        setTimePlay();
        trueCase = questionList.get(level).getTrueCase();

        //Set lại backGround cho các textView hiện thị câu trả lời
        tvCaseA.setBackgroundResource(R.drawable.answer);
        tvCaseB.setBackgroundResource(R.drawable.answer);
        tvCaseC.setBackgroundResource(R.drawable.answer);
        tvcaseD.setBackgroundResource(R.drawable.answer);
        tvCaseA.setClickable(true);
        tvCaseB.setClickable(true);
        tvCaseC.setClickable(true);
        tvcaseD.setClickable(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_caseA:
                if(choiceMedia != null){
                    choiceMedia.release();
                    choiceMedia = MediaPlayer.create(PlayActivity.this, R.raw.ans_a);
                }
                else {
                    choiceMedia = MediaPlayer.create(PlayActivity.this, R.raw.ans_a);
                }
                choiceMedia.start();
                isPause = true;
                tvCaseA.setBackgroundResource(R.drawable.your_answer);
                tvCaseA.setAnimation(blinkWaitAnswer);
                yourChoice = 1;
                tvCaseB.setClickable(false);
                tvCaseC.setClickable(false);
                tvcaseD.setClickable(false);
                processAnswer();
                break;
            case R.id.tv_caseB:
                if(choiceMedia != null){
                    choiceMedia.release();
                    choiceMedia = MediaPlayer.create(PlayActivity.this, R.raw.ans_b);
                }
                else {
                    choiceMedia = MediaPlayer.create(PlayActivity.this, R.raw.ans_b);
                }
                choiceMedia.start();
                isPause = true;
                tvCaseB.setBackgroundResource(R.drawable.your_answer);
                yourChoice = 2;
                tvCaseA.setClickable(false);
                tvCaseC.setClickable(false);
                tvcaseD.setClickable(false);
                processAnswer();
                break;
            case R.id.tv_caseC:
                if(choiceMedia != null){
                    choiceMedia.release();
                    choiceMedia = MediaPlayer.create(PlayActivity.this, R.raw.ans_c);
                }
                else {
                    choiceMedia = MediaPlayer.create(PlayActivity.this, R.raw.ans_c);
                }
                choiceMedia.start();
                isPause = true;
                tvCaseC.setBackgroundResource(R.drawable.your_answer);
                yourChoice = 3;
                tvCaseB.setClickable(false);
                tvCaseA.setClickable(false);
                tvcaseD.setClickable(false);
                processAnswer();
                break;
            case R.id.tv_caseD:
                if(choiceMedia != null){
                    choiceMedia.release();
                    choiceMedia = MediaPlayer.create(PlayActivity.this, R.raw.ans_d);
                }
                else {
                    choiceMedia = MediaPlayer.create(PlayActivity.this, R.raw.ans_d);
                }
                choiceMedia.start();
                isPause = true;
                tvcaseD.setBackgroundResource(R.drawable.your_answer);
                yourChoice = 4;
                tvCaseB.setClickable(false);
                tvCaseC.setClickable(false);
                tvCaseA.setClickable(false);
                tvcaseD.setAnimation(blinkWaitAnswer);
                processAnswer();
                break;
            case R.id.btn_phone:
                if(helpMedia != null){
                    helpMedia.release();
                    helpMedia = MediaPlayer.create(PlayActivity.this, R.raw.help_callb);
                }else {
                    helpMedia = MediaPlayer.create(PlayActivity.this, R.raw.help_callb);
                }
                helpMedia.start();

                CallMeDialog callMe = new CallMeDialog(PlayActivity.this, trueCase);
                callMe.setCanceledOnTouchOutside(false);
                callMe.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                callMe.show();
                btnPhone.setBackgroundResource(R.drawable.icn_telefone_disable);
                btnPhone.setClickable(false);
                break;
            case R.id.btn_50_50:
                if(helpMedia != null){
                    helpMedia.release();
                    helpMedia = MediaPlayer.create(PlayActivity.this, R.raw.sound5050);
                }else {
                    helpMedia = MediaPlayer.create(PlayActivity.this, R.raw.sound5050);
                }
                helpMedia.start();
                I5050Dialog i5050dialog = new I5050Dialog(PlayActivity.this);
                i5050dialog.setCanceledOnTouchOutside(false);
                i5050dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                i5050dialog.show();
                help5050(trueCase);//Hàm xử lý 50/50
                btn50_50.setBackgroundResource(R.drawable.icn_50_50_disable);
                btn50_50.setClickable(false);
                break;
            case R.id.btn_people:
                if(helpMedia != null){
                    helpMedia.release();
                    helpMedia = MediaPlayer.create(PlayActivity.this, R.raw.khan_gia);
                }else {
                    helpMedia = MediaPlayer.create(PlayActivity.this, R.raw.khan_gia);
                }
                helpMedia.start();
                HelpFromViewersDialog helpFromViewersDialog = new HelpFromViewersDialog(PlayActivity.this, trueCase);
                helpFromViewersDialog.setCanceledOnTouchOutside(false);
                helpFromViewersDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                helpFromViewersDialog.show();
                btnPeople.setClickable(false);
                btnPeople.setBackgroundResource(R.drawable.icn_publico_disable);

        }

    }

    private void help5050(int trueCase) {
        switch (trueCase) {
            case 1:
                tvCaseC.setText("");
                tvcaseD.setText("");
                tvCaseC.setClickable(false);
                tvcaseD.setClickable(false);
                break;
            case 2:
                tvCaseA.setText("");
                tvcaseD.setText("");
                tvCaseA.setClickable(false);
                tvcaseD.setClickable(false);
                break;
            case 3:
                tvCaseA.setText("");
                tvcaseD.setText("");
                tvCaseA.setClickable(false);
                tvcaseD.setClickable(false);
                break;
            case 4:
                tvCaseA.setText("");
                tvCaseB.setText("");
                tvCaseA.setClickable(false);
                tvCaseB.setClickable(false);
                break;
        }

    }

    private void animationTrueAnswer(int trueAnswer) {
        switch (trueAnswer) {
            case 1:
                tvCaseA.setBackgroundResource(R.drawable.correct_answer);
                tvCaseA.setAnimation(blinkAnswer);
                tvCaseA.startAnimation(blinkAnswer);
                break;
            case 2:
                tvCaseB.setBackgroundResource(R.drawable.correct_answer);
                tvCaseB.setAnimation(blinkAnswer);
                tvCaseB.startAnimation(blinkAnswer);
                break;
            case 3:
                tvCaseC.setBackgroundResource(R.drawable.correct_answer);
                tvCaseC.setAnimation(blinkAnswer);
                tvCaseC.startAnimation(blinkAnswer);
                break;
            case 4:
                tvcaseD.setBackgroundResource(R.drawable.correct_answer);
                tvcaseD.setAnimation(blinkAnswer);
                tvcaseD.startAnimation(blinkAnswer);
                break;
        }
        if(answerMedia != null){
            answerMedia.release();
            switch (trueAnswer){
                case 1:
                    answerMedia = MediaPlayer.create(PlayActivity.this, R.raw.true_a);
                    break;
                case 2:
                    answerMedia = MediaPlayer.create(PlayActivity.this, R.raw.true_b);
                    break;
                case 3:
                    answerMedia = MediaPlayer.create(PlayActivity.this, R.raw.true_c);
                    break;
                case 4:
                    answerMedia = MediaPlayer.create(PlayActivity.this, R.raw.true_d2);
                    break;
            }
        }
        else {
            switch (trueAnswer){
                case 1:
                    answerMedia = MediaPlayer.create(PlayActivity.this, R.raw.true_a);
                    break;
                case 2:
                    answerMedia = MediaPlayer.create(PlayActivity.this, R.raw.true_b);
                    break;
                case 3:
                    answerMedia = MediaPlayer.create(PlayActivity.this, R.raw.true_c);
                    break;
                case 4:
                    answerMedia = MediaPlayer.create(PlayActivity.this, R.raw.true_d2);
                    break;
            }
        }
        answerMedia.start();
    }

    private void animationFalseAnswer(int trueAnswer, int yourAnswer) {
        switch (trueAnswer) {
            case 1:
                tvCaseA.setBackgroundResource(R.drawable.correct_answer);
                tvCaseA.setAnimation(blinkAnswerFalse);
                tvCaseA.startAnimation(blinkAnswerFalse);
                break;
            case 2:
                tvCaseB.setBackgroundResource(R.drawable.correct_answer);
                tvCaseB.setAnimation(blinkAnswerFalse);
                tvCaseB.startAnimation(blinkAnswerFalse);
                break;
            case 3:
                tvCaseC.setBackgroundResource(R.drawable.correct_answer);
                tvCaseC.setAnimation(blinkAnswerFalse);
                tvCaseC.startAnimation(blinkAnswerFalse);
                break;
            case 4:
                tvcaseD.setBackgroundResource(R.drawable.correct_answer);
                tvcaseD.setAnimation(blinkAnswerFalse);
                tvcaseD.startAnimation(blinkAnswerFalse);
                break;
        }
        switch (yourAnswer) {
            case 1:
                tvCaseA.setBackgroundResource(R.drawable.wrong_answer);
                break;
            case 2:
                tvCaseB.setBackgroundResource(R.drawable.wrong_answer);
                break;
            case 3:
                tvCaseC.setBackgroundResource(R.drawable.wrong_answer);
                break;
            case 4:
                tvcaseD.setBackgroundResource(R.drawable.wrong_answer);
                break;
        }
        if(answerMedia != null){
            answerMedia.release();
            switch (trueAnswer){
                case 1:
                    answerMedia = MediaPlayer.create(PlayActivity.this, R.raw.lose_a2);
                    break;
                case 2:
                    answerMedia = MediaPlayer.create(PlayActivity.this, R.raw.lose_b);
                    break;
                case 3:
                    answerMedia = MediaPlayer.create(PlayActivity.this, R.raw.lose_c2);
                    break;
                case 4:
                    answerMedia = MediaPlayer.create(PlayActivity.this, R.raw.lose_d);
                    break;
            }
        }
        else {
            switch (trueAnswer){
                case 1:
                    answerMedia = MediaPlayer.create(PlayActivity.this, R.raw.lose_a2);
                    break;
                case 2:
                    answerMedia = MediaPlayer.create(PlayActivity.this, R.raw.lose_b);
                    break;
                case 3:
                    answerMedia = MediaPlayer.create(PlayActivity.this, R.raw.lose_c2);
                    break;
                case 4:
                    answerMedia = MediaPlayer.create(PlayActivity.this, R.raw.lose_d);
                    break;
            }
        }
        answerMedia.start();
    }

    private void processAnswer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (yourChoice == trueCase) {
                    Message msg = new Message();
                    msg.what = ANSWER_TRUE;
                    msg.setTarget(handler);
                    msg.sendToTarget();
                } else {
                    Message msg = new Message();
                    msg.what = ANSWER_WRONG;
                    msg.setTarget(handler);
                    msg.sendToTarget();
                }
            }
        }).start();
    }

    private void sleepTime(int mili) {
        SystemClock.sleep(mili);
    }

    private void setTimePlay() {
        timePlay = 30;
        isPause = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (timePlay > 0 && !isPause) {
                    sleepTime(1000);
                    timePlay--;
                    Message msg = new Message();
                    msg.what = UPDATE_TIME_PLAY;
                    msg.arg1 = timePlay;
                    msg.setTarget(handler);
                    msg.sendToTarget();
                }
                if (!isPause) {
                    Log.i(TAG, "Time over");
                    Message msg = new Message();
                    msg.what = SHOW_DIALOG_TIME_OUT;
                    msg.setTarget(handler);
                    msg.sendToTarget();
                }
            }
        }).start();
    }

    @Override
    public void setFinishGameOver() {
        backgroundMusic.release();
        Intent mIntent = new Intent(PlayActivity.this, GameOverActivity.class);
        mScore = new Score(level,false);
        score = mScore.returnScore();
        mIntent.putExtra(SCORE,score);
        startActivity(mIntent);
        PlayActivity.this.finish();
    }

    private void mediaOption(int level){
        if (level == 0){
            if(level == 0){
                backgroundMusic= new MediaPlayer().create(PlayActivity.this, R.raw.background_music );
                backgroundMusic.start();
                backgroundMusic.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        PlayActivity.this.backgroundMusic = new MediaPlayer().create(PlayActivity.this, R.raw.background_music );
                        backgroundMusic.setLooping(true);
                        backgroundMusic.start();
                    }
                });
            }
            media =  MediaPlayer.create(this, R.raw.ques1);
            media.start();
        }else {
            media.release();
            media = null;
        }
        if(level>4){
           backgroundMusic.release();
            backgroundMusic = MediaPlayer.create(PlayActivity.this, R.raw.background_music_b);
            backgroundMusic.start();
            backgroundMusic.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    PlayActivity.this.backgroundMusic = new MediaPlayer().create(PlayActivity.this, R.raw.background_music_b );
                    backgroundMusic.setLooping(true);
                    backgroundMusic.start();
                }
            });
        }
        random = new Random();
        int randomNumber = random.nextInt(2);
        switch (level+1){
            case 2:
                if(randomNumber%2==0){
                    media = MediaPlayer.create(this, R.raw.ques2);
                    media.start();
                }else {
                    media = MediaPlayer.create(this, R.raw.ques2_b);
                    media.start();
                }
                break;
            case 3:
                if(randomNumber%2==0){
                    media = MediaPlayer.create(this, R.raw.ques3);
                    media.start();
                }else {
                    media = MediaPlayer.create(this, R.raw.ques3_b);
                    media.start();
                }
                break;
            case 4:
                if(randomNumber%2==0){
                    media = MediaPlayer.create(this, R.raw.ques4);
                    media.start();
                }else {
                    media = MediaPlayer.create(this, R.raw.ques4_b);
                    media.start();
                }
                break;
            case 5:
                if(randomNumber%2==0){
                    media = MediaPlayer.create(this, R.raw.ques5);
                    media.start();
                }else {
                    media = MediaPlayer.create(this, R.raw.ques5_b);
                    media.start();
                }
                break;
            case 6:
                if(randomNumber%2==0) {
                    media = MediaPlayer.create(this, R.raw.ques6);
                    media.start();
                }
                break;
            case 7:
                if(randomNumber%2==0){
                    media = MediaPlayer.create(this, R.raw.ques7);
                    media.start();
                }else {
                    media = MediaPlayer.create(this, R.raw.ques7_b);
                    media.start();
                }
                break;
            case 8:
                if(randomNumber%2==0){
                    media = MediaPlayer.create(this, R.raw.ques8);
                    media.start();
                }else {
                    media = MediaPlayer.create(this, R.raw.ques8_b);
                    media.start();
                }
                break;
            case 9:
                if(randomNumber%2==0){
                    media = MediaPlayer.create(this, R.raw.ques9);
                    media.start();
                }else {
                    media = MediaPlayer.create(this, R.raw.ques9_b);
                    media.start();
                }
                break;
            case 10:
                media = MediaPlayer.create(this, R.raw.ques10);
                media.start();
                break;
            case 11:
                media = MediaPlayer.create(this, R.raw.ques11);
                media.start();
                break;
            case 12:
                media = MediaPlayer.create(this, R.raw.ques12);
                media.start();
                break;
            case 13:
                media = MediaPlayer.create(this, R.raw.ques13);
                media.start();
                break;
            case 14:
                media = MediaPlayer.create(this, R.raw.ques14);
                media.start();
                break;
            case 15:
                media = MediaPlayer.create(this, R.raw.ques15);
                media.start();
                break;
        }


    }

    @Override
    public void setFinish() {
        Intent mIntent = new Intent(PlayActivity.this, GameOverActivity.class);
        mScore = new Score(level,false);
        score = mScore.returnScore();
        mIntent.putExtra(SCORE, score);
        backgroundMusic.release();
        media.release();
        startActivity(mIntent);
        PlayActivity.this.finish();
    }
}
