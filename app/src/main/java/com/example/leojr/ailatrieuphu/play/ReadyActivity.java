package com.example.leojr.ailatrieuphu.play;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.leojr.ailatrieuphu.R;
import com.example.leojr.ailatrieuphu.dialog.IReadyDialog;
import com.example.leojr.ailatrieuphu.dialog.ReadyDialog;

/**
 * Created by Leo Jr on 15/10/2016.
 */

public class ReadyActivity extends Activity implements IReadyDialog, View.OnClickListener{

    private TextView tvMoney1;
    private TextView tvMoney2;
    private TextView tvMoney3;
    private TextView tvMoney4;
    private TextView tvMoney5;
    private TextView tvMoney6;
    private TextView tvMoney7;
    private TextView tvMoney8;
    private TextView tvMoney9;
    private TextView tvMoney10;
    private TextView tvMoney11;
    private TextView tvMoney12;
    private TextView tvMoney13;
    private TextView tvMoney14;
    private TextView tvMoney15;
    private Button btnSkip;
    ReadyDialog readyDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ready_layout);
        init();
        readyDialog = new ReadyDialog(this);
        readyDialog.setiReadyDialog(this);
        readyDialog.setCanceledOnTouchOutside(false);
        btnSkip.setOnClickListener(this);

    }

    public void init(){
        btnSkip = (Button) findViewById(R.id.btn_skip);

    }

    @Override
    public void setClickBtnNo() {
        Intent mIntent = new Intent(this,GuideActivity.class);
        startActivity(mIntent);
        this.finish();
    }

    @Override
    public void setClickBtnYes() {
        Intent mIntent = new Intent(this,PlayActivity.class);
        startActivity(mIntent);
        this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_skip:
                readyDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                readyDialog.show();
                break;
        }
    }
}
