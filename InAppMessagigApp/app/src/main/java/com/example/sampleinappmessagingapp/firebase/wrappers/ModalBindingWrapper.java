package com.example.sampleinappmessagingapp.firebase.wrappers;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.VisibleForTesting;

import com.example.sampleinappmessagingapp.R;
import com.google.firebase.inappmessaging.display.internal.InAppMessageLayoutConfig;
import com.google.firebase.inappmessaging.display.internal.injection.scopes.InAppMessageScope;
import com.google.firebase.inappmessaging.display.internal.layout.FiamRelativeLayout;
import com.google.firebase.inappmessaging.model.Action;
import com.google.firebase.inappmessaging.model.InAppMessage;
import com.google.firebase.inappmessaging.model.MessageType;
import com.google.firebase.inappmessaging.model.ModalMessage;

import java.util.Map;

import javax.inject.Inject;

@InAppMessageScope
public class ModalBindingWrapper extends BindingWrapper {
    private FiamRelativeLayout modalRoot;
    private ViewGroup modalContentRoot;
    private ScrollView bodyScroll;
    private Button button;
    private View collapseImage;
    private ImageView imageView;
    private TextView messageBody;
    private TextView messageTitle;
    private ModalMessage modalMessage;
    private ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ModalBindingWrapper.ScrollViewAdjustableListener();

    @Inject
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public ModalBindingWrapper(InAppMessageLayoutConfig config, LayoutInflater inflater, InAppMessage message) {
        super(config, inflater, message);
    }

    @NonNull
    public ViewTreeObserver.OnGlobalLayoutListener inflate(Map<Action, View.OnClickListener> actionListeners, View.OnClickListener dismissOnClickListener) {
        View root = this.inflater.inflate(R.layout.modal, (ViewGroup)null);
        this.bodyScroll = (ScrollView)root.findViewById(R.id.body_scroll);
        this.button = (Button)root.findViewById(R.id.button);
        this.collapseImage = root.findViewById(R.id.collapse_button);
        this.imageView = (ImageView)root.findViewById(R.id.image_view);
        this.messageBody = (TextView)root.findViewById(R.id.message_body);
        this.messageTitle = (TextView)root.findViewById(R.id.message_title);
        this.modalRoot = (FiamRelativeLayout)root.findViewById(R.id.modal_root);
        this.modalContentRoot = (ViewGroup)root.findViewById(R.id.modal_content_root);
        if (this.message.getMessageType().equals(MessageType.MODAL)) {
            this.modalMessage = (ModalMessage)this.message;
            this.setMessage(this.modalMessage);
            this.setButton(actionListeners);
            this.setLayoutConfig(this.config);
            this.setDismissListener(dismissOnClickListener);
            this.setViewBgColorFromHex(this.modalContentRoot, this.modalMessage.getBackgroundHexColor());
        }

        return this.layoutListener;
    }

    @NonNull
    public ImageView getImageView() {
        return this.imageView;
    }

    @NonNull
    public ViewGroup getRootView() {
        return this.modalRoot;
    }

    @NonNull
    public View getDialogView() {
        return this.modalContentRoot;
    }

    @NonNull
    public View getScrollView() {
        return this.bodyScroll;
    }

    @NonNull
    public View getTitleView() {
        return this.messageTitle;
    }

    @NonNull
    public InAppMessageLayoutConfig getConfig() {
        return this.config;
    }

    @NonNull
    public Button getActionButton() {
        return this.button;
    }

    @NonNull
    public View getCollapseButton() {
        return this.collapseImage;
    }

    @SuppressLint("WrongConstant")
    private void setMessage(ModalMessage message) {
        if (message.getImageData() != null && !TextUtils.isEmpty(message.getImageData().getImageUrl())) {
            this.imageView.setVisibility(0);
        } else {
            this.imageView.setVisibility(8);
        }

        if (message.getTitle() != null) {
            if (!TextUtils.isEmpty(message.getTitle().getText())) {
                this.messageTitle.setVisibility(0);
                this.messageTitle.setText(message.getTitle().getText());
            } else {
                this.messageTitle.setVisibility(8);
            }

            if (!TextUtils.isEmpty(message.getTitle().getHexColor())) {
                this.messageTitle.setTextColor(Color.parseColor(message.getTitle().getHexColor()));
            }
        }

        if (message.getBody() != null && !TextUtils.isEmpty(message.getBody().getText())) {
            this.bodyScroll.setVisibility(0);
            this.messageBody.setVisibility(0);
            this.messageBody.setTextColor(Color.parseColor(message.getBody().getHexColor()));
            this.messageBody.setText(message.getBody().getText());
        } else {
            this.bodyScroll.setVisibility(8);
            this.messageBody.setVisibility(8);
        }

    }

    @SuppressLint("WrongConstant")
    private void setButton(Map<Action, View.OnClickListener> actionListeners) {
        Action modalAction = this.modalMessage.getAction();
        if (modalAction != null && modalAction.getButton() != null && !TextUtils.isEmpty(modalAction.getButton().getText().getText())) {
            setupViewButtonFromModel(this.button, modalAction.getButton());
            this.setButtonActionListener(this.button, (View.OnClickListener)actionListeners.get(this.modalMessage.getAction()));
            this.button.setVisibility(0);
        } else {
            this.button.setVisibility(8);
        }

    }

    private void setLayoutConfig(InAppMessageLayoutConfig config) {
        this.imageView.setMaxHeight(config.getMaxImageHeight());
        this.imageView.setMaxWidth(config.getMaxImageWidth());
    }

    private void setDismissListener(View.OnClickListener dismissListener) {
        this.collapseImage.setOnClickListener(dismissListener);
        this.modalRoot.setDismissListener(dismissListener);
    }

    @VisibleForTesting
    public void setLayoutListener(ViewTreeObserver.OnGlobalLayoutListener listener) {
        this.layoutListener = listener;
    }

    public class ScrollViewAdjustableListener implements ViewTreeObserver.OnGlobalLayoutListener {
        public ScrollViewAdjustableListener() {
        }

        public void onGlobalLayout() {
            ModalBindingWrapper.this.imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    }
}
