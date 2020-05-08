package com.example.sampleinappmessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        MyClickListener listener = new MyClickListener(new Callback() {
//            @Override
//            public void onCallback(final String url) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        showToast(url);
//                    }
//                });
//            }
//        });
        FirebaseInAppMessaging.getInstance().setAutomaticDataCollectionEnabled(true);
//        FirebaseInAppMessaging.getInstance().addClickListener(listener);

        Intent intent = getIntent();
        if(intent != null && intent.getAction() != null && intent.getData() != null){
            String action = intent.getAction();
            Uri data = intent.getData();
            sendData(data.toString());
            //showToast(data.toString());
        }
    }

    private void sendData(String message) {
        startActivity(new Intent(MainActivity.this, SecondActivity.class).putExtra("data", message));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent != null && intent.getAction() != null && intent.getData() != null){
            String action = intent.getAction();
            Uri data = intent.getData();
            setIntent(intent);
//            sendData(data.toString());
        }
    }

    public void showToast(String message){
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseInAppMessaging.getInstance().triggerEvent("main_activity_resumed");
    }
}
