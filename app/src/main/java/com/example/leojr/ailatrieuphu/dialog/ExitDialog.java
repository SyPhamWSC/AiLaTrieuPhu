package com.example.leojr.ailatrieuphu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.provider.Settings;
import android.view.View;
import android.view.Window;

import com.example.leojr.ailatrieuphu.R;


public class ExitDialog extends Dialog implements View.OnClickListener{
    public ExitDialog(Context context) {
        super(context);
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.exit_dialog_layout);
        findViewById(R.id.btn_exit_dialog).setOnClickListener(this);
        findViewById(R.id.btn_no_exit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_exit_dialog:
                System.exit(0);
                break;
            case R.id.btn_no_exit:
                dismiss();
                break;
        }
    }
}
