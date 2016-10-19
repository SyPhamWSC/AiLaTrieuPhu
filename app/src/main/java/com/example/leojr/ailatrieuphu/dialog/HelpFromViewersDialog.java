package com.example.leojr.ailatrieuphu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.example.leojr.ailatrieuphu.R;

/**
 * Created by Leo Jr on 19/10/2016.
 */

public class HelpFromViewersDialog extends Dialog implements View.OnClickListener {

    AudienceFeedbackDialog mAudience;
    int trueCase;
    Context context;

    public HelpFromViewersDialog(Context context, int trueCase) {
        super(context);
        this.context = context;
        this.trueCase = trueCase;
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.help_audience_dialog);
        findViewById(R.id.btn_ok_help_from_audience).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok_help_from_audience:
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mAudience = new AudienceFeedbackDialog(context,trueCase);
                mAudience.setCanceledOnTouchOutside(false);
                mAudience.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mAudience.show();
                HelpFromViewersDialog.this.dismiss();
                break;
        }
    }
}
