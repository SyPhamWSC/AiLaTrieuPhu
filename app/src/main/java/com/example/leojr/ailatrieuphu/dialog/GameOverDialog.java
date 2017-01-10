package com.example.leojr.ailatrieuphu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.leojr.ailatrieuphu.R;


public class GameOverDialog extends Dialog implements View.OnClickListener{
    private Button btnOk;
    private IGameOverDialog iGameOverDialog;

    public GameOverDialog(Context context) {
        super(context);
        init();
    }


    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game_over_dialog_layout);
        btnOk = (Button) findViewById(R.id.btn_ok_game_over_dialog);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        iGameOverDialog.setFinishGameOver();
    }

    public void setiGameOverDialog(IGameOverDialog iGameOverDialog) {
        this.iGameOverDialog = iGameOverDialog;
    }
}
