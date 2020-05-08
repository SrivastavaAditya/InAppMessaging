package com.example.sampleinappmessagingapp.firebase.wrappers;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import com.example.sampleinappmessagingapp.R;
import com.example.sampleinappmessagingapp.firebase.wrappers.BindingWrapper;
import com.google.firebase.inappmessaging.display.internal.InAppMessageLayoutConfig;
import com.google.firebase.inappmessaging.display.internal.injection.scopes.InAppMessageScope;
import com.google.firebase.inappmessaging.display.internal.layout.FiamFrameLayout;
import com.google.firebase.inappmessaging.model.Action;
import com.google.firebase.inappmessaging.model.ImageOnlyMessage;
import com.google.firebase.inappmessaging.model.InAppMessage;
import com.google.firebase.inappmessaging.model.MessageType;

import java.util.Map;

import javax.inject.Inject;

import static com.example.sampleinappmessagingapp.R.id.collapse_button;

@InAppMessageScope
public class ImageBindingWrapper extends BindingWrapper {
    private FiamFrameLayout imageRoot;
    private ViewGroup imageContentRoot;
    private ImageView imageView;
    private Button collapseButton;

    @Inject
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public ImageBindingWrapper(InAppMessageLayoutConfig config, LayoutInflater inflater, InAppMessage message) {
        super(config, inflater, message);
    }

    @SuppressLint("WrongConstant")
    @Nullable
    public ViewTreeObserver.OnGlobalLayoutListener inflate(Map<Action, View.OnClickListener> actionListeners, View.OnClickListener dismissOnClickListener) {
        View v = this.inflater.inflate(R.layout.image, (ViewGroup)null);
        this.imageRoot = (FiamFrameLayout)v.findViewById(R.id.image_root);
        this.imageContentRoot = (ViewGroup)v.findViewById(R.id.image_content_root);
        this.imageView = (ImageView)v.findViewById(R.id.image_view);
        this.collapseButton = (Button)v.findViewById(collapse_button);
        this.imageView.setMaxHeight(this.config.getMaxImageHeight());
        this.imageView.setMaxWidth(this.config.getMaxImageWidth());
        if (this.message.getMessageType().equals(MessageType.IMAGE_ONLY)) {
            ImageOnlyMessage msg = (ImageOnlyMessage)this.message;
            this.imageView.setVisibility(msg.getImageData() != null && !TextUtils.isEmpty(msg.getImageData().getImageUrl()) ? 0 : 8);
            this.imageView.setOnClickListener((View.OnClickListener)actionListeners.get(msg.getAction()));
        }

        this.imageRoot.setDismissListener(dismissOnClickListener);
        this.collapseButton.setOnClickListener(dismissOnClickListener);
        return null;
    }

    @NonNull
    public ImageView getImageView() {
        return this.imageView;
    }

    @NonNull
    public ViewGroup getRootView() {
        return this.imageRoot;
    }

    @NonNull
    public View getDialogView() {
        return this.imageContentRoot;
    }

    @NonNull
    public View getCollapseButton() {
        return this.collapseButton;
    }
}
