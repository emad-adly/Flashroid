package com.emadadly.flashroid;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.widget.Toast;

/**
 * Created by Emad Adly on 12/24/2015.
 */
public class CameraFlashController {
    CameraFlashChangeListener flashChangeListener;
    Camera camera;
    Camera.Parameters camParams;
    boolean isFlashOn = false;
    Context context;

    CameraFlashController(final Context context) {
        this.context = context;
        flashChangeListener = (CameraFlashChangeListener) context;
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
               try {
                   camera = Camera.open();
                   camParams = camera.getParameters();
                   camera.startPreview();
               } catch (Exception e) {
                   showToast("Camera currently unavalable or used by another app !");
               }
            } else {
                showToast("Your Device does not has camera !");
            }
        } else {
            showToast("Your Device does not has camera !");
        }
    }

    public void flashOn() {
        new ChangeFlashThread(true).start();
    }

    public void flashOff() {
        new ChangeFlashThread(false).start();
    }

    public boolean isFlashOn() {
        return isFlashOn;
    }

    class ChangeFlashThread extends Thread {
        boolean flashOn;
        ChangeFlashThread(boolean flashOn) {
            this.flashOn = flashOn;
        }

        @Override
        public void run() {
            if (flashOn) {
                isFlashOn = true;
                camParams.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            } else {
                isFlashOn = false;
                camParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            }
            camera.setParameters(camParams);
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    flashChangeListener.onFlashChange(flashOn);
                }
            });
        }
    }

    public interface CameraFlashChangeListener {
        void onFlashChange(boolean isFlashOn);
    }

    private void showToast(String message) {
        final String toast = message;
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
            }
        });
    }
}
