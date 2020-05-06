package com.example.sampleinappmessagingapp;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.inappmessaging.FirebaseInAppMessagingClickListener;
import com.google.firebase.inappmessaging.model.Action;
import com.google.firebase.inappmessaging.model.CampaignMetadata;
import com.google.firebase.inappmessaging.model.InAppMessage;

public class MyClickListener implements FirebaseInAppMessagingClickListener {

    @Override
    public void messageClicked(@NonNull InAppMessage inAppMessage, @NonNull Action action) {
        // Determine which URL the user clicked
        String url = action.getActionUrl();

        // Get general information about the campaign
        CampaignMetadata metadata = inAppMessage.getCampaignMetadata();

        Log.d("FIAM-Click-URL", url);
    }
}
