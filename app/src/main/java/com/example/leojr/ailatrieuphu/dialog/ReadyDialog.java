package com.example.leojr.ailatrieuphu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;

import com.example.leojr.ailatrieuphu.R;
import com.example.leojr.ailatrieuphu.play.PlayActivity;

public class ReadyDialog extends Dialog implements View.OnClickListener {

    private IReadyDialog iReadyDialog;

    public ReadyDialog(Context context) {
        super(context);
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ready_dialog_layout);
        findViewById(R.id.btn_yes_ready).setOnClickListener(this);
        findViewById(R.id.btn_no_ready).setOnClickListener(this);
    }

    @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes_ready:
                iReadyDialog.setClickBtnYes();
                break;
            case R.id.btn_no_ready:
                iReadyDialog.setClickBtnNo();
                break;

        }
        dismiss();
    }

    public void setiReadyDialog(IReadyDialog iReadyDialog) {
        this.iReadyDialog = iReadyDialog;
    }
}
