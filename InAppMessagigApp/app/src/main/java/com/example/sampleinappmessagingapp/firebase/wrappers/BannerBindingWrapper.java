package com.example.sampleinappmessagingapp.firebase.wrappers;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import com.example.sampleinappmessagingapp.R;
import com.google.firebase.inappmessaging.display.internal.InAppMessageLayoutConfig;
import com.google.firebase.inappmessaging.display.internal.ResizableImageView;
import com.google.firebase.inappmessaging.display.internal.injection.scopes.InAppMessageScope;
import com.google.firebase.inappmessaging.display.internal.layout.FiamFrameLayout;
import com.google.firebase.inappmessaging.model.Action;
import com.google.firebase.inappmessaging.model.BannerMessage;
import com.google.firebase.inappmessaging.model.InAppMessage;
import com.google.firebase.inappmessaging.model.MessageType;

import java.util.Map;

import javax.inject.Inject;

@InAppMessageScope
public class BannerBindingWrapper extends BindingWrapper {
    private FiamFrameLayout bannerRoot;
    private ViewGroup bannerContentRoot;
    private TextView bannerBody;
    private ResizableImageView bannerImage;
    private TextView bannerTitle;
    private View.OnClickListener mDismissListener;

    @Inject
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public BannerBindingWrapper(InAppMessageLayoutConfig config, LayoutInflater inflater, InAppMessage message) {
        super(config, inflater, message);
    }

    @Nullable
    public ViewTreeObserver.OnGlobalLayoutListener inflate(Map<Action, View.OnClickListener> actionListeners, View.OnClickListener dismissOnClickListener) {
        View root = this.inflater.inflate(R.layout.banner, (ViewGroup)null);
        this.bannerRoot = (FiamFrameLayout)root.findViewById(R.id.banner_root);
        this.bannerContentRoot = (ViewGroup)root.findViewById(R.id.banner_content_root);
        this.bannerBody = (TextView)root.findViewById(R.id.banner_body);
        this.bannerImage = (ResizableImageView)root.findViewById(R.id.banner_image);
        this.bannerTitle = (TextView)root.findViewById(R.id.banner_title);
        if (this.message.getMessageType().equals(MessageType.BANNER)) {
            BannerMessage bannerMessage = (BannerMessage)this.message;
            this.setMessage(bannerMessage);
            this.setLayoutConfig(this.config);
            this.setSwipeDismissListener(dismissOnClickListener);
            this.setActionListener((View.OnClickListener)actionListeners.get(bannerMessage.getAction()));
        }

        return null;
    }

    @SuppressLint("WrongConstant")
    private void setMessage(@NonNull BannerMessage message) {
        if (!TextUtils.isEmpty(message.getBackgroundHexColor())) {
            this.setViewBgColorFromHex(this.bannerContentRoot, message.getBackgroundHexColor());
        }

        this.bannerImage.setVisibility(message.getImageData() != null && !TextUtils.isEmpty(message.getImageData().getImageUrl()) ? 0 : 8);
        if (message.getTitle() != null) {
            if (!TextUtils.isEmpty(message.getTitle().getText())) {
                this.bannerTitle.setText(message.getTitle().getText());
            }

            if (!TextUtils.isEmpty(message.getTitle().getHexColor())) {
                this.bannerTitle.setTextColor(Color.parseColor(message.getTitle().getHexColor()));
            }
        }

        if (message.getBody() != null) {
            if (!TextUtils.isEmpty(message.getBody().getText())) {
                this.bannerBody.setText(message.getBody().getText());
            }

            if (!TextUtils.isEmpty(message.getBody().getHexColor())) {
                this.bannerBody.setTextColor(Color.parseColor(message.getBody().getHexColor()));
            }
        }

    }

    private void setLayoutConfig(InAppMessageLayoutConfig layoutConfig) {
        int bannerWidth = Math.min(layoutConfig.maxDialogWidthPx(), layoutConfig.maxDialogHeightPx());
        ViewGroup.LayoutParams params = this.bannerRoot.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(-1, -2);
        }

        params.width = bannerWidth;
        this.bannerRoot.setLayoutParams(params);
        this.bannerImage.setMaxHeight(layoutConfig.getMaxImageHeight());
        this.bannerImage.setMaxWidth(layoutConfig.getMaxImageWidth());
    }

    private void setSwipeDismissListener(View.OnClickListener dismissListener) {
        this.mDismissListener = dismissListener;
        this.bannerRoot.setDismissListener(this.mDismissListener);
    }

    private void setActionListener(View.OnClickListener actionListener) {
        this.bannerContentRoot.setOnClickListener(actionListener);
    }

    @NonNull
    public InAppMessageLayoutConfig getConfig() {
        return this.config;
    }

    @NonNull
    public ImageView getImageView() {
        return this.bannerImage;
    }

    @NonNull
    public ViewGroup getRootView() {
        return this.bannerRoot;
    }

    @NonNull
    public View getDialogView() {
        return this.bannerContentRoot;
    }

    @Nullable
    public View.OnClickListener getDismissListener() {
        return this.mDismissListener;
    }

    public boolean canSwipeToDismiss() {
        return true;
    }
}

