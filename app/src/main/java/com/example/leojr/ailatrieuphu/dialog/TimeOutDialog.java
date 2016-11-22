package com.example.leojr.ailatrieuphu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.leojr.ailatrieuphu.R;


public class TimeOutDialog extends Dialog implements View.OnClickListener {

    private ITimeOutDialog iTimeOutDialog;

    private Button btnOk;

    public TimeOutDialog(Context context) {
        super(context);

        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.time_out_dialog_layout);
        btnOk = (Button) findViewById(R.id.btn_ok_time_out);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        iTimeOutDialog.setFinish();
    }

    public void setiTimeOutDialog(ITimeOutDialog iTimeOutDialog) {
        this.iTimeOutDialog = iTimeOutDialog;
    }
}
