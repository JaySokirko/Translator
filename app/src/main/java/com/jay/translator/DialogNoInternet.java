package com.jay.translator;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class DialogNoInternet extends android.support.v7.app.AlertDialog implements View.OnClickListener {


    private AnimationDrawable toolBarAnimation;

    public DialogNoInternet(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog_internet_connection);

        ImageView cancel = findViewById(R.id.alert_dialog_cancel);
        if (cancel != null) {
            cancel.setOnClickListener(this);
        }

        FrameLayout dialogBackground = findViewById(R.id.dialog_background);

        ImageView openInternetSettings = findViewById(R.id.alert_dialog_internet);
        if (openInternetSettings != null) {
            openInternetSettings.setOnClickListener(this);
        }

        if (dialogBackground != null) {
            toolBarAnimation = (AnimationDrawable) dialogBackground.getBackground();
        }
        toolBarAnimation.setExitFadeDuration(4000);
        toolBarAnimation.start();
    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        toolBarAnimation.stop();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.alert_dialog_cancel:
                dismiss();
                break;

            case R.id.alert_dialog_internet:

                getContext().startActivity(new Intent(
                        Settings.ACTION_WIRELESS_SETTINGS));
                dismiss();
                break;
        }
    }
}
