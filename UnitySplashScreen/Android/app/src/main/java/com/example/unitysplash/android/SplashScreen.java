package com.example.unitysplash.android;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashScreen extends Activity {
    private String mActivityToStart = "com.unity3d.player.UnityPlayerActivity";
    private int mSplashImage = 0;
    private int mDuration = 1000; // One second
    private int SplashTimeoutMessage = 100;

    private Handler mSplashTimeoutHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SplashTimeoutMessage) {
                try {
                    Intent i = new Intent(SplashScreen.this, Class.forName(mActivityToStart));
                    startActivity(i);
                    SplashScreen.this.finish();
                } catch (ClassNotFoundException e) {
                    Log.d(SplashScreen.class.getName(), e.getMessage());
                }
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            ActivityInfo ai = getPackageManager().getActivityInfo(this.getComponentName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;

            mActivityToStart = bundle.getString("activityToStart", mActivityToStart);
            mSplashImage = bundle.getInt("splashImage", mSplashImage);
            mDuration = bundle.getInt("duration", mDuration);

        } catch (PackageManager.NameNotFoundException e) {
            Log.d(SplashScreen.class.getName(), e.getMessage());
        }

        if (mSplashImage != 0) {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);

            ImageView splashImage = new ImageView(this);
            splashImage.setImageResource(mSplashImage);
            layout.addView(splashImage, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            setContentView(layout, new  ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSplashTimeoutHandler.sendMessageDelayed(mSplashTimeoutHandler.obtainMessage(SplashTimeoutMessage), mDuration);
    }

    @Override
    public void onBackPressed() {
        mSplashTimeoutHandler.removeMessages(SplashTimeoutMessage);
        super.onBackPressed();
    }
}
