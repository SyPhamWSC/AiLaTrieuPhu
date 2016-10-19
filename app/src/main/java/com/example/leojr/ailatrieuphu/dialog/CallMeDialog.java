package com.example.leojr.ailatrieuphu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.leojr.ailatrieuphu.R;

/**
 * Created by Leo Jr on 19/10/2016.
 */

public class CallMeDialog extends Dialog implements View.OnClickListener{
    int trueCase;
    public CallMeDialog(Context context, int trueCase) {
        super(context);
        this.trueCase = trueCase;
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.call_me_dialog_layout);
        TextView tvHelp = (TextView) findViewById(R.id.tv_answer_call_me);
        switch (trueCase){
            case 1:
                tvHelp.setText("Theo sự nghiên cứu của tui, đáp án đúng là A");
                break;
            case 2:
                tvHelp.setText("Theo sự nghiên cứu của tui, đáp án đúng là B");
                break;
            case 3:
                tvHelp.setText("Theo sự nghiên cứu của tui, đáp án đúng là C");
                break;
            case 4:
                tvHelp.setText("Theo sự nghiên cứu của tui, đáp án đúng là D");
                break;
        }
        findViewById(R.id.btn_ok_call_me).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok_call_me:
                dismiss();
                break;
        }
    }
}
