package com.example.leojr.ailatrieuphu.play;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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
import com.example.leojr.ailatrieuphu.dialog.HelpFromViewersDialog;
import com.example.leojr.ailatrieuphu.dialog.I5050Dialog;
import com.example.leojr.ailatrieuphu.dialog.ITimeOutDialog;
import com.example.leojr.ailatrieuphu.dialog.TimeOutDialog;

import java.io.IOException;
import java.util.List;


public class PlayActivity extends Activity implements View.OnClickListener, ITimeOutDialog {

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

    private int trueCase;
    private int level = 0;
    private int yourChoice;
    private int timePlay;
    private boolean isPause;
    private int score;
    private Score mScore;
    private TimeOutDialog timeOutDialog;

    private Animation blinkAnswer;
    private Animation blinkAnswerFalse;
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

        blinkAnswer = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        blinkAnswerFalse = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink_false);
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
                Intent mIntent = new Intent(PlayActivity.this, GameOverActivity.class);
                mScore = new Score(level,false);
                score = mScore.returnScore();
                mIntent.putExtra(SCORE,score);
                startActivity(mIntent);
                PlayActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        btnPhone.setOnClickListener(this);
        btn50_50.setOnClickListener(this);
        btnPeople.setOnClickListener(this);
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

        isPause = false;
        //Set câu hỏi mới
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
                isPause = true;
                tvCaseA.setBackgroundResource(R.drawable.your_answer);
                yourChoice = 1;
                tvCaseB.setClickable(false);
                tvCaseC.setClickable(false);
                tvcaseD.setClickable(false);
                processAnswer();
                break;
            case R.id.tv_caseB:
                isPause = true;
                tvCaseB.setBackgroundResource(R.drawable.your_answer);
                yourChoice = 2;
                tvCaseA.setClickable(false);
                tvCaseC.setClickable(false);
                tvcaseD.setClickable(false);
                processAnswer();
                break;
            case R.id.tv_caseC:
                isPause = true;
                tvCaseC.setBackgroundResource(R.drawable.your_answer);
                yourChoice = 3;
                tvCaseB.setClickable(false);
                tvCaseA.setClickable(false);
                tvcaseD.setClickable(false);
                processAnswer();
                break;
            case R.id.tv_caseD:
                isPause = true;
                tvcaseD.setBackgroundResource(R.drawable.your_answer);
                yourChoice = 4;
                tvCaseB.setClickable(false);
                tvCaseC.setClickable(false);
                tvCaseA.setClickable(false);
                processAnswer();
                break;
            case R.id.btn_phone:
                CallMeDialog callMe = new CallMeDialog(PlayActivity.this, trueCase);
                callMe.setCanceledOnTouchOutside(false);
                callMe.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                callMe.show();
                btnPhone.setBackgroundResource(R.drawable.icn_telefone_disable);
                btnPhone.setClickable(false);
                break;
            case R.id.btn_50_50:
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
    public void setFinish() {
        Intent mIntent = new Intent(PlayActivity.this, GameOverActivity.class);
        mScore = new Score(level,false);
        score = mScore.returnScore();
        mIntent.putExtra(SCORE, score);
        startActivity(mIntent);
        PlayActivity.this.finish();
    }

//    private void processAnswer(final int yourChoice, final int trueCase){
//        MyAsyncTaskProcessPlay m = new MyAsyncTaskProcessPlay();
//        m.execute();
//    }
    //    class MyAsyncTaskProcessPlay extends AsyncTask<Integer, Integer, Void> {
//
//
//       @Override
//       protected Void doInBackground(Integer... params) {
//           level++;
//           publishProgress(level);
//           return null;
//       }
//
//       @Override
//       protected void onProgressUpdate(Integer... values) {
//           super.onProgressUpdate(values);
//           int levels = values[0];
//           try {
//               Thread.sleep(3000);
//           } catch (InterruptedException e) {
//               e.printStackTrace();
//           }
//
//           try {
//               Thread.sleep(1000);
//           } catch (InterruptedException e) {
//               e.printStackTrace();
//           }
//           if (checkAnswer(yourChoice, trueCase)) {
//               animationTrueCase(trueCase);
//               try {
//                   Thread.sleep(1000);
//               } catch (InterruptedException e) {
//                   e.printStackTrace();
//               }
//               setNewQuestion(levels);
//               Toast.makeText(PlayActivity.this, "You have got " + checkScore(levels), Toast.LENGTH_LONG).show();
//           } else {
//               animationTrueCase(trueCase);
//               try {
//                   Thread.sleep(1000);
//               } catch (InterruptedException e) {
//                   e.printStackTrace();
//               }
//               Toast.makeText(PlayActivity.this, "Sai rồi, Ngu vãi", Toast.LENGTH_LONG).show();
//               try {
//                   Thread.sleep(1000);
//               } catch (InterruptedException e) {
//                   e.printStackTrace();
//               }
//               Intent mIntent = new Intent(PlayActivity.this, GameOverActivity.class);
//               mIntent.putExtra("score", checkScore(levels - 1));
//               startActivity(mIntent);
//               PlayActivity.this.finish();
//           }
//
//       }
//
//       @Override
//       protected void onPostExecute(Void aVoid) {
//           super.onPostExecute(aVoid);
//       }
//
//       private String checkScore(int level) {
//           String score = null;
//           switch (level) {
//               case 0:
//                   score = "0";
//                   break;
//               case 1:
//                   score = "200000";
//                   break;
//               case 2:
//                   score = "400000";
//                   break;
//               case 3:
//                   score = "600000";
//                   break;
//               case 4:
//                   score = "1000000";
//                   break;
//               case 5:
//                   score = "2000000";
//                   break;
//               case 6:
//                   score = "3000000";
//                   break;
//               case 7:
//                   score = "6000000";
//                   break;
//               case 8:
//                   score = "10000000";
//                   break;
//               case 9:
//                   score = "14000000";
//                   break;
//               case 10:
//                   score = "22000000";
//                   break;
//               case 11:
//                   score = "30000000";
//                   break;
//               case 12:
//                   score = "40000000";
//                   break;
//               case 13:
//                   score = "60000000";
//                   break;
//               case 14:
//                   score = "85000000";
//                   break;
//               case 15:
//                   score = "150000000";
//                   break;
//           }
//           return score;
//
}
