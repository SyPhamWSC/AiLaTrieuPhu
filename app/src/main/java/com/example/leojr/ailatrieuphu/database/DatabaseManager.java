package com.example.leojr.ailatrieuphu.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Leo Jr on 12/10/2016.
 */

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseManager";
    private static String DB_PATH = "/data/data/com.example.leojr.ailatrieuphu/databases/";
    private static String DB_NAME = "ailatrieuphuData.sqlite";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "Question";
    private static final String QUESTION_COLUMN = "question";
    private static final String LEVEL_COLUMN = "level";
    private static final String CASE_A_COLUMN = "casea";
    private static final String CASE_B_COLUMN = "caseb";
    private static final String CASE_C_COLUMN = "casec";
    private static final String CASE_D_COLUMN = "cased";
    private static final String TRUE_CASE_COLUMN = "truecase";

    private SQLiteDatabase db;
    private final Context context;

    public DatabaseManager(Context context) {
        super(context, DB_NAME,null, DB_VERSION);
        this.context = context;
    }

    public void openDatabase() throws SQLException{

        String myPath = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(myPath,null, SQLiteDatabase.OPEN_READONLY);
    }

    //Check xem database có null hay không
    public boolean checkDatabase(){

        SQLiteDatabase checkDB = null;
        String myPath = DB_PATH + DB_NAME;

        try{
            checkDB = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READONLY);
        }catch (SQLException e){

        }
        if(checkDB!=null){
            checkDB.close();
            return true;
        }

        //trả về true nếu không null, trả về false nếu null
        return false;

    }

    private void copyDatabase() throws IOException{
        InputStream myInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer,0,length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void createDatabase() throws IOException{
        boolean dbExist = checkDatabase();
        if(dbExist) {
            Log.i(TAG,"khong co database");
        }
        else {

            this.getReadableDatabase();
            copyDatabase();
        }
    }

    private Question getQuestion(String level){
        try{
            createDatabase();
        }catch (IOException e){
            e.printStackTrace();
        }
        openDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{QUESTION_COLUMN,CASE_A_COLUMN,CASE_B_COLUMN,CASE_C_COLUMN,CASE_D_COLUMN,TRUE_CASE_COLUMN}
                        , LEVEL_COLUMN + "=?", new String[]{level}, null, null,null,null
                        );
        int n = new Random().nextInt(cursor.getCount());
        if(cursor != null && cursor.moveToFirst()){
            for (int i = 0; i < n; i++) {
                cursor.moveToNext();
            }
        }
        int questionIndex = cursor.getColumnIndexOrThrow(QUESTION_COLUMN);
        int caseAIndex = cursor.getColumnIndexOrThrow(CASE_A_COLUMN);
        int caseBIndex = cursor.getColumnIndexOrThrow(CASE_B_COLUMN);
        int caseCIndex = cursor.getColumnIndexOrThrow(CASE_C_COLUMN);
        int caseDIndex = cursor.getColumnIndexOrThrow(CASE_D_COLUMN);
        int trueCaseIndex = cursor.getColumnIndexOrThrow(TRUE_CASE_COLUMN);

        Question question = new Question(cursor.getString(questionIndex), cursor.getString(caseAIndex),
                cursor.getString(caseBIndex),cursor.getString(caseCIndex),cursor.getString(caseDIndex),cursor.getInt(trueCaseIndex),level
                );
        db.close();
        return question;
    }

    public List<Question> get15Question(){
        List<Question> listQuestion = new ArrayList<Question>();
        for (int i = 1; i <16 ; i++) {
            String content = Integer.toString(i);
            Question questionContent = getQuestion(content);
            listQuestion.add(questionContent);
        }
        return listQuestion;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
