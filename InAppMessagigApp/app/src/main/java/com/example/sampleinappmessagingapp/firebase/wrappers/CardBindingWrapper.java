package com.example.sampleinappmessagingapp.firebase.wrappers;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import com.google.firebase.inappmessaging.display.internal.layout.BaseModalLayout;
import com.google.firebase.inappmessaging.display.internal.layout.FiamCardView;
import com.google.firebase.inappmessaging.model.Action;
import com.google.firebase.inappmessaging.model.CardMessage;
import com.google.firebase.inappmessaging.model.InAppMessage;
import com.google.firebase.inappmessaging.model.MessageType;

import java.util.Map;

import javax.inject.Inject;

@InAppMessageScope
public class CardBindingWrapper extends BindingWrapper {
    private FiamCardView cardRoot;
    private BaseModalLayout cardContentRoot;
    private ScrollView bodyScroll;
    private Button primaryButton;
    private Button secondaryButton;
    private ImageView imageView;
    private TextView messageBody;
    private TextView messageTitle;
    private CardMessage cardMessage;
    private View.OnClickListener dismissListener;
    private ViewTreeObserver.OnGlobalLayoutListener layoutListener = new CardBindingWrapper.ScrollViewAdjustableListener();

    @Inject
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public CardBindingWrapper(InAppMessageLayoutConfig config, LayoutInflater inflater, InAppMessage message) {
        super(config, inflater, message);
    }

    @NonNull
    public ViewTreeObserver.OnGlobalLayoutListener inflate(Map<Action, View.OnClickListener> actionListeners, View.OnClickListener dismissOnClickListener) {
        View root = this.inflater.inflate(R.layout.card, (ViewGroup)null);
        this.bodyScroll = (ScrollView)root.findViewById(R.id.body_scroll);
        this.primaryButton = (Button)root.findViewById(R.id.primary_button);
        this.secondaryButton = (Button)root.findViewById(R.id.secondary_button);
        this.imageView = (ImageView)root.findViewById(R.id.image_view);
        this.messageBody = (TextView)root.findViewById(R.id.message_body);
        this.messageTitle = (TextView)root.findViewById(R.id.message_title);
        this.cardRoot = (FiamCardView)root.findViewById(R.id.card_root);
        this.cardContentRoot = (BaseModalLayout)root.findViewById(R.id.card_content_root);
        if (this.message.getMessageType().equals(MessageType.CARD)) {
            this.cardMessage = (CardMessage)this.message;
            this.setMessage(this.cardMessage);
            this.setImage(this.cardMessage);
            this.setButtons(actionListeners);
            this.setLayoutConfig(this.config);
            this.setDismissListener(dismissOnClickListener);
            this.setViewBgColorFromHex(this.cardContentRoot, this.cardMessage.getBackgroundHexColor());
        }

        return this.layoutListener;
    }

    @NonNull
    public ImageView getImageView() {
        return this.imageView;
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
    public ViewGroup getRootView() {
        return this.cardRoot;
    }

    @NonNull
    public View getDialogView() {
        return this.cardContentRoot;
    }

    @NonNull
    public InAppMessageLayoutConfig getConfig() {
        return this.config;
    }

    @NonNull
    public View.OnClickListener getDismissListener() {
        return this.dismissListener;
    }

    @NonNull
    public Button getPrimaryButton() {
        return this.primaryButton;
    }

    @NonNull
    public Button getSecondaryButton() {
        return this.secondaryButton;
    }

    @SuppressLint("WrongConstant")
    private void setMessage(CardMessage message) {
        this.messageTitle.setText(message.getTitle().getText());
        this.messageTitle.setTextColor(Color.parseColor(message.getTitle().getHexColor()));
        if (message.getBody() != null && message.getBody().getText() != null) {
            this.bodyScroll.setVisibility(0);
            this.messageBody.setVisibility(0);
            this.messageBody.setText(message.getBody().getText());
            this.messageBody.setTextColor(Color.parseColor(message.getBody().getHexColor()));
        } else {
            this.bodyScroll.setVisibility(8);
            this.messageBody.setVisibility(8);
        }

    }

    @SuppressLint("WrongConstant")
    private void setButtons(Map<Action, View.OnClickListener> actionListeners) {
        Action primaryAction = this.cardMessage.getPrimaryAction();
        Action secondaryAction = this.cardMessage.getSecondaryAction();
        setupViewButtonFromModel(this.primaryButton, primaryAction.getButton());
        this.setButtonActionListener(this.primaryButton, (View.OnClickListener)actionListeners.get(primaryAction));
        this.primaryButton.setVisibility(0);
        if (secondaryAction != null && secondaryAction.getButton() != null) {
            setupViewButtonFromModel(this.secondaryButton, secondaryAction.getButton());
            this.setButtonActionListener(this.secondaryButton, (View.OnClickListener)actionListeners.get(secondaryAction));
            this.secondaryButton.setVisibility(0);
        } else {
            this.secondaryButton.setVisibility(8);
        }

    }

    @SuppressLint("WrongConstant")
    private void setImage(CardMessage message) {
        if (message.getPortraitImageData() == null && message.getLandscapeImageData() == null) {
            this.imageView.setVisibility(8);
        } else {
            this.imageView.setVisibility(0);
        }

    }

    private void setLayoutConfig(InAppMessageLayoutConfig config) {
        this.imageView.setMaxHeight(config.getMaxImageHeight());
        this.imageView.setMaxWidth(config.getMaxImageWidth());
    }

    private void setDismissListener(View.OnClickListener dismissListener) {
        this.dismissListener = dismissListener;
        this.cardRoot.setDismissListener(dismissListener);
    }

    @VisibleForTesting
    public void setLayoutListener(ViewTreeObserver.OnGlobalLayoutListener listener) {
        this.layoutListener = listener;
    }

    public class ScrollViewAdjustableListener implements ViewTreeObserver.OnGlobalLayoutListener {
        public ScrollViewAdjustableListener() {
        }

        public void onGlobalLayout() {
            CardBindingWrapper.this.imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    }
}
