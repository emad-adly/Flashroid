package com.emadadly.flashroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.emadadly.flashroid.CameraFlashController.CameraFlashChangeListener;

public class MainActivity extends Activity implements CameraFlashChangeListener, OnClickListener {
    ImageView flashImageView;
    CameraFlashController cameraController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flashImageView = (ImageView) findViewById(R.id.flashIV);
        cameraController = new CameraFlashController(this);
        flashImageView.setOnClickListener(this);
    }


    @Override
    public void onFlashChange(boolean isFlashOn) {
        if (isFlashOn) {
            flashImageView.setImageResource(R.drawable.flash_on);
        } else {
            flashImageView.setImageResource(R.drawable.flash_off);
        }
    }

    @Override
    public void onClick(View v) {
        if (cameraController.isFlashOn) {
            cameraController.flashOff();
        } else {
            cameraController.flashOn();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraController.freeCamera();
        cameraController = null;
    }
}
