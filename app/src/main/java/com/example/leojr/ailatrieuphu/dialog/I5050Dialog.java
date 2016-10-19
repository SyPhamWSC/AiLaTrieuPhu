package com.example.leojr.ailatrieuphu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.leojr.ailatrieuphu.R;

/**
 * Created by Leo Jr on 19/10/2016.
 */

public class I5050Dialog extends Dialog implements View.OnClickListener{
    private Button btnOK;
    public I5050Dialog(Context context) {
        super(context);
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_50_50);
        findViewById(R.id.btn_ok_50_50).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok_50_50:
                dismiss();
                break;
        }
    }
}
