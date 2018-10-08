package com.jay.translator;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class DialogLanguageNotSupported extends AlertDialog implements View.OnClickListener {

    private AnimationDrawable toolBarAnimation;


    public DialogLanguageNotSupported(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog_language_not_supported);

        FrameLayout dialogBackground = findViewById(R.id.dialog_language_not_supported_background);

        ImageView cancel = findViewById(R.id.alert_dialog_cancel_open_language_settings);
        if (cancel != null) {
            cancel.setOnClickListener(this);
        }

        ImageView openInternetSettings = findViewById(R.id.alert_dialog_open_language_settings);
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
        toolBarAnimation.stop();
        super.onDetachedFromWindow();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.alert_dialog_cancel_open_language_settings:
                dismiss();
                break;

            case R.id.alert_dialog_open_language_settings:

                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                getContext().startActivity(installIntent);

                dismiss();
                break;
        }
    }
}
