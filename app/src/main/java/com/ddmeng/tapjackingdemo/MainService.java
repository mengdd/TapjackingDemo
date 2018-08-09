package com.ddmeng.tapjackingdemo;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class MainService extends Service {

    private WindowManager windowManager;
    private View windowView;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        addOverlayView();
        return super.onStartCommand(intent, flags, startId);
    }

    private void addOverlayView() {

        final WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        0,
                        PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER | Gravity.START;
        params.x = 0;
        params.y = 0;

        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        FrameLayout interceptorLayout = new FrameLayout(this) {

            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    Log.v("back", "BACK Button Pressed");
                    return true;
                }
                return super.dispatchKeyEvent(event);
            }
        };

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        windowView = layoutInflater.inflate(R.layout.floating_view, interceptorLayout);
        windowManager.addView(windowView, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        dismissWindow();
    }

    private void dismissWindow() {
        if (windowView != null) {
            windowManager.removeView(windowView);
            windowView = null;
        }
    }

}