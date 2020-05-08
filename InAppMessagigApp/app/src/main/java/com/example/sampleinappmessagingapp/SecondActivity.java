package com.example.sampleinappmessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        tvMessage = findViewById(R.id.tv_message);

        if(getIntent() != null && getIntent().getStringExtra("data")!= null){
            tvMessage.setText(getIntent().getStringExtra("data"));
        }
    }
}
