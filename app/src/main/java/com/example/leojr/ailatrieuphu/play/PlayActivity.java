package com.example.leojr.ailatrieuphu.play;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leojr.ailatrieuphu.R;
import com.example.leojr.ailatrieuphu.database.DatabaseManager;
import com.example.leojr.ailatrieuphu.database.Question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo Jr on 12/10/2016.
 */

public class PlayActivity extends Activity {

    TextView tvLevel;
    TextView tvQuestion;
    TextView tvCaseA;
    TextView tvCaseB;
    TextView tvCaseC;
    TextView tvcaseD;

    List<Question> questionList;
    Question questionContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_activity_layout);

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
            Toast.makeText(PlayActivity.this,"Create database error!", Toast.LENGTH_SHORT).show();
        }

        List<Question> questionList = db.get15Question();
        Toast.makeText(this, questionList.get(0).getCaseA(),Toast.LENGTH_LONG).show();

    }
}
