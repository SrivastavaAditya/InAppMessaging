package com.example.sampleinappmessagingapp.firebase.wrappers;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.firebase.inappmessaging.display.internal.InAppMessageLayoutConfig;
import com.google.firebase.inappmessaging.display.internal.Logging;
import com.google.firebase.inappmessaging.model.Action;
import com.google.firebase.inappmessaging.model.InAppMessage;

import java.util.Map;

public abstract class BindingWrapper {
    protected final InAppMessage message;
    final InAppMessageLayoutConfig config;
    final LayoutInflater inflater;

    protected BindingWrapper(InAppMessageLayoutConfig config, LayoutInflater inflater, InAppMessage message) {
        this.config = config;
        this.inflater = inflater;
        this.message = message;
    }

    @NonNull
    public abstract ImageView getImageView();

    @NonNull
    public abstract ViewGroup getRootView();

    @NonNull
    public abstract View getDialogView();

    @Nullable
    public abstract ViewTreeObserver.OnGlobalLayoutListener inflate(Map<Action, View.OnClickListener> var1, View.OnClickListener var2);

    public boolean canSwipeToDismiss() {
        return false;
    }

    @Nullable
    public View.OnClickListener getDismissListener() {
        return null;
    }

    @NonNull
    public InAppMessageLayoutConfig getConfig() {
        return this.config;
    }

    public void setViewBgColorFromHex(@Nullable View view, @Nullable String hexColor) {
        if (view != null && !TextUtils.isEmpty(hexColor)) {
            try {
                view.setBackgroundColor(Color.parseColor(hexColor));
            } catch (IllegalArgumentException var4) {
                Logging.loge("Error parsing background color: " + var4.toString() + " color: " + hexColor);
            }

        }
    }

    public static void setButtonBgColorFromHex(Button button, String hexColor) {
        try {
            Drawable drawable = button.getBackground();
            Drawable compatDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(compatDrawable, Color.parseColor(hexColor));
            button.setBackground(compatDrawable);
        } catch (IllegalArgumentException var4) {
            Logging.loge("Error parsing background color: " + var4.toString());
        }

    }

    public static void setupViewButtonFromModel(Button viewButton, com.google.firebase.inappmessaging.model.Button modelButton) {
        String buttonTextHexColor = modelButton.getText().getHexColor();
        setButtonBgColorFromHex(viewButton, modelButton.getButtonHexColor());
        viewButton.setText(modelButton.getText().getText());
        viewButton.setTextColor(Color.parseColor(buttonTextHexColor));
    }

    public void setButtonActionListener(@Nullable Button button, View.OnClickListener actionListener) {
        if (button != null) {
            button.setOnClickListener(actionListener);
        }

    }
}

