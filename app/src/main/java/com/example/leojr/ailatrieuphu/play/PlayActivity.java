package com.example.leojr.ailatrieuphu.play;

import android.app.Activity;
import android.app.VoiceInteractor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leojr.ailatrieuphu.R;
import com.example.leojr.ailatrieuphu.database.DatabaseManager;
import com.example.leojr.ailatrieuphu.database.Question;

import java.io.IOException;
import java.util.List;
import java.util.logging.Handler;

/**
 * Created by Leo Jr on 12/10/2016.
 */

public class PlayActivity extends Activity implements View.OnClickListener{

    TextView tvLevel;
    TextView tvQuestion;
    TextView tvCaseA;
    TextView tvCaseB;
    TextView tvCaseC;
    TextView tvcaseD;
    private int trueCase;
    private int level = 0;
    private boolean isPaused= false;
    private boolean correct = false;
    private int yourChoice;


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


    }
    public void initView(){
        tvLevel = (TextView) findViewById(R.id.tv_level);
        tvQuestion = (TextView) findViewById(R.id.tv_question);
        tvCaseA = (TextView) findViewById(R.id.tv_caseA);
        tvCaseB = (TextView) findViewById(R.id.tv_caseB);
        tvCaseC = (TextView) findViewById(R.id.tv_caseC);
        tvcaseD = (TextView) findViewById(R.id.tv_caseD);
        DatabaseManager db = new DatabaseManager(this);
        try{
            db.createDatabase();
        }catch (IOException e){
            e.printStackTrace();
            //Toast.makeText(PlayActivity.this,"Create database error!", Toast.LENGTH_SHORT).show();
        }

        questionList = db.get15Question();



    }
//    public void clickListener(){
//        tvCaseA.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tvCaseA.setBackgroundResource(R.drawable.your_answer);
//                tvCaseC.setClickable(false);
//                tvCaseB.setClickable(false);
//                tvcaseD.setClickable(false);
//            }
//        });
//        tvCaseB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tvCaseB.setBackgroundResource(R.drawable.your_answer);
//                tvCaseC.setClickable(false);
//                tvCaseA.setClickable(false);
//                tvcaseD.setClickable(false);
//            }
//        });
//        tvCaseC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tvCaseC.setBackgroundResource(R.drawable.your_answer);
//                tvCaseA.setClickable(false);
//                tvCaseB.setClickable(false);
//                tvcaseD.setClickable(false);
//            }
//        });
//        tvcaseD.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tvcaseD.setBackgroundResource(R.drawable.your_answer);
//                tvCaseC.setClickable(false);
//                tvCaseB.setClickable(false);
//                tvCaseA.setClickable(false);
//            }
//        });
//    }

    public void setNewQuestion(int level){
        int levels = level +1;
        String levelsContent = Integer.toString(levels);

        tvLevel.setText("Câu "+ levelsContent);
        tvQuestion.setText(questionList.get(level).getContent());
        tvCaseA.setText(questionList.get(level).getCaseA());
        tvCaseB.setText(questionList.get(level).getCaseB());
        tvCaseC.setText(questionList.get(level).getCaseC());
        tvcaseD.setText(questionList.get(level).getCaseD());
        trueCase = questionList.get(level).getTrueCase();
        tvCaseA.setBackgroundResource(R.drawable.answer);
        tvCaseB.setBackgroundResource(R.drawable.answer);
        tvCaseC.setBackgroundResource(R.drawable.answer);
        tvcaseD.setBackgroundResource(R.drawable.answer);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_caseA:
                tvCaseA.setBackgroundResource(R.drawable.your_answer);
                yourChoice = 1;
                processAnswer(yourChoice,trueCase);
                break;
            case R.id.tv_caseB:
                tvCaseB.setBackgroundResource(R.drawable.your_answer);
                yourChoice = 2;
                processAnswer(yourChoice,trueCase);
                break;
            case R.id.tv_caseC:
                tvCaseC.setBackgroundResource(R.drawable.your_answer);
                processAnswer(yourChoice,trueCase);
                yourChoice = 3;
                break;
            case R.id.tv_caseD:
                tvcaseD.setBackgroundResource(R.drawable.your_answer);
                yourChoice = 4;
                processAnswer(yourChoice,trueCase);
                break;
        }


    }
    private boolean checkAnswer(int yourChoice, int trueCase){
        if(yourChoice == trueCase){
            return true;
        }
        return false;
    }
    private void processAnswer(final int yourChoice, final int trueCase){
        MyAsyncTask m = new MyAsyncTask();
        m.execute();
    }

   class MyAsyncTask extends AsyncTask<Integer, Integer, Void>{


       @Override
       protected Void doInBackground(Integer... params) {
           level++;
           publishProgress(level);
           return null;
       }

       @Override
       protected void onProgressUpdate(Integer... values) {
           super.onProgressUpdate(values);
           int levels = values[0];
           if(checkAnswer(yourChoice,trueCase)){
               try {
                   Thread.sleep(3000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               setNewQuestion(levels);
           }
           else {

               Toast.makeText(PlayActivity.this, "Sai rồi, Ngu vãi", Toast.LENGTH_LONG).show();
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               System.exit(0);
           }

       }

       @Override
       protected void onPostExecute(Void aVoid) {
           super.onPostExecute(aVoid);
           Toast.makeText(PlayActivity.this, "Correct", Toast.LENGTH_LONG).show();
       }
   }
}
