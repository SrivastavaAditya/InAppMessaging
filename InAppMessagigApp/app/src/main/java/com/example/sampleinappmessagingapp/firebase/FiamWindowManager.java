package com.example.sampleinappmessagingapp.firebase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.example.sampleinappmessagingapp.firebase.wrappers.BindingWrapper;
import com.google.firebase.inappmessaging.display.internal.InAppMessageLayoutConfig;
import com.google.firebase.inappmessaging.display.internal.Logging;
import com.google.firebase.inappmessaging.display.internal.SwipeDismissTouchListener;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FiamWindowManager {
    static final int DEFAULT_TYPE = 1003;
    private BindingWrapper bindingWrapper;

    @Inject
    FiamWindowManager() {
    }

    public void show(@NonNull BindingWrapper bindingWrapper, @NonNull Activity activity) {
        if (this.isFiamDisplayed()) {
            Logging.loge("Fiam already active. Cannot show new Fiam.");
        } else {
            InAppMessageLayoutConfig config = bindingWrapper.getConfig();
            WindowManager.LayoutParams layoutParams = this.getLayoutParams(config, activity);
            WindowManager windowManager = this.getWindowManager(activity);
            View rootView = bindingWrapper.getRootView();
            windowManager.addView(rootView, layoutParams);
            Rect insetDimensions = this.getInsetDimensions(activity);
            Logging.logdPair("Inset (top, bottom)", (float)insetDimensions.top, (float)insetDimensions.bottom);
            Logging.logdPair("Inset (left, right)", (float)insetDimensions.left, (float)insetDimensions.right);
            if (bindingWrapper.canSwipeToDismiss()) {
                SwipeDismissTouchListener listener = this.getSwipeListener(config, bindingWrapper, windowManager, layoutParams);
                bindingWrapper.getDialogView().setOnTouchListener(listener);
            }

            this.bindingWrapper = bindingWrapper;
        }
    }

    public boolean isFiamDisplayed() {
        return this.bindingWrapper == null ? false : this.bindingWrapper.getRootView().isShown();
    }

    public void destroy(@NonNull Activity activity) {
        if (this.isFiamDisplayed()) {
            this.getWindowManager(activity).removeViewImmediate(this.bindingWrapper.getRootView());
            this.bindingWrapper = null;
        }

    }

    private WindowManager.LayoutParams getLayoutParams(@NonNull InAppMessageLayoutConfig layoutConfig, @NonNull Activity activity) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(layoutConfig.windowWidth(), layoutConfig.windowHeight(), 1003, layoutConfig.windowFlag(), -3);
        Rect insetDimensions = this.getInsetDimensions(activity);
        if ((layoutConfig.viewWindowGravity() & 48) == 48) {
            layoutParams.y = insetDimensions.top;
        }

        layoutParams.dimAmount = 0.3F;
        layoutParams.gravity = layoutConfig.viewWindowGravity();
        layoutParams.windowAnimations = 0;
        return layoutParams;
    }

    @SuppressLint("WrongConstant")
    private WindowManager getWindowManager(@NonNull Activity activity) {
        return (WindowManager)activity.getSystemService("window");
    }

    private Point getDisplaySize(@NonNull Activity activity) {
        Point size = new Point();
        Display display = this.getWindowManager(activity).getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else {
            display.getSize(size);
        }

        return size;
    }

    private Rect getInsetDimensions(@NonNull Activity activity) {
        Rect padding = new Rect();
        Rect visibleFrame = this.getVisibleFrame(activity);
        Point size = this.getDisplaySize(activity);
        padding.top = visibleFrame.top;
        padding.left = visibleFrame.left;
        padding.right = size.x - visibleFrame.right;
        padding.bottom = size.y - visibleFrame.bottom;
        return padding;
    }

    private Rect getVisibleFrame(@NonNull Activity activity) {
        Rect visibleFrame = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(visibleFrame);
        return visibleFrame;
    }

    private SwipeDismissTouchListener getSwipeListener(InAppMessageLayoutConfig layoutConfig, final BindingWrapper bindingWrapper, final WindowManager windowManager, final WindowManager.LayoutParams layoutParams) {
        SwipeDismissTouchListener.DismissCallbacks callbacks = new SwipeDismissTouchListener.DismissCallbacks() {
            public boolean canDismiss(Object token) {
                return true;
            }

            public void onDismiss(View view, Object token) {
                if (bindingWrapper.getDismissListener() != null) {
                    bindingWrapper.getDismissListener().onClick(view);
                }

            }
        };
        return layoutConfig.windowWidth() == -1 ? new SwipeDismissTouchListener(bindingWrapper.getDialogView(), (Object)null, callbacks) : new SwipeDismissTouchListener(bindingWrapper.getDialogView(), (Object)null, callbacks) {
            protected float getTranslationX() {
                return (float)layoutParams.x;
            }

            protected void setTranslationX(float translationX) {
                layoutParams.x = (int)translationX;
                windowManager.updateViewLayout(bindingWrapper.getRootView(), layoutParams);
            }
        };
    }
}
