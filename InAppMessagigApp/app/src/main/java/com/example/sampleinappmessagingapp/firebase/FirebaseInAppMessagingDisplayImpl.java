package com.example.sampleinappmessagingapp.firebase;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplay;
import com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplayCallbacks;
import com.google.firebase.inappmessaging.model.InAppMessage;

public class FirebaseInAppMessagingDisplayImpl implements FirebaseInAppMessagingDisplay, Application.ActivityLifecycleCallbacks {

    public FirebaseInAppMessagingDisplayImpl(){

    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        Log.d("Created Activity: ", activity.getClass().getName());
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        Log.d("Started Activity: ", activity.getClass().getName());
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        Log.d("Resumed Activity: ", activity.getClass().getName());
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        Log.d("Paused Activity: ", activity.getClass().getName());
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        Log.d("Stopped Activity: ", activity.getClass().getName());
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        Log.d("SavedInstanceActivity: ", activity.getClass().getName());
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        Log.d("Destroyed Activity: ", activity.getClass().getName());
    }

    @Override
    public void displayMessage(@NonNull InAppMessage inAppMessage, @NonNull FirebaseInAppMessagingDisplayCallbacks firebaseInAppMessagingDisplayCallbacks) {

    }
}
