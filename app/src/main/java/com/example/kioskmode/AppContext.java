package com.example.kioskmode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AppContext extends Application implements Application.ActivityLifecycleCallbacks {
    private AppContext instance;
    private PowerManager.WakeLock wakeLock;
    private OnScreenOffReceiver onScreenOffReceiver;
    private static boolean isShown = false;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        registerKioskModeScreenOffReceiver();
        startKioskService();

    }

    private void registerKioskModeScreenOffReceiver() {
        // register screen off receiver
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        onScreenOffReceiver = new OnScreenOffReceiver();
        //registerReceiver(onScreenOffReceiver, filter);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(final Activity activity) {
        isShown = true;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
     isShown=false;
    }

    @Override
    public void onActivityStopped(final Activity activity) {
        isShown = false;
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    @SuppressLint("InvalidWakeLockTag")
    public PowerManager.WakeLock getWakeLock() {
        if(wakeLock == null) {
         //    lazy loading: first call, create wakeLock via PowerManager.
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = ((PowerManager) pm).newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "wakeup");
        }
        return wakeLock;
    }

    private void startKioskService() { // ... and this method
        startService(new Intent(this, KioskService.class));
    }
}
