package com.example.leojr.ailatrieuphu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.leojr.ailatrieuphu.R;

public class AudienceFeedbackDialog extends Dialog implements View.OnClickListener {
    ImageView img;
    int trueCase;

    public AudienceFeedbackDialog(Context context, int trueCase) {
        super(context);
        this.trueCase = trueCase;
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.adience_feedback_dialog);
        findViewById(R.id.btn_ok_feedback).setOnClickListener(this);
        img = (ImageView) findViewById(R.id.img_feedback);
        switch (trueCase) {
            case 1:
                img.setImageResource(R.drawable.audience_a);
                break;
            case 2:
                img.setImageResource(R.drawable.audience_back_b);
                break;
            case 3:
                img.setImageResource(R.drawable.audience_back_c);
                break;
            case 4:
                img.setImageResource(R.drawable.audience_back_d);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok_feedback:
                dismiss();
                break;
        }
    }

}
