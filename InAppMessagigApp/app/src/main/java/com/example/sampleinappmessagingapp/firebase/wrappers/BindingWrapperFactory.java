package com.example.sampleinappmessagingapp.firebase.wrappers;

import android.app.Application;

import com.google.firebase.inappmessaging.display.internal.InAppMessageLayoutConfig;
import com.google.firebase.inappmessaging.display.internal.injection.components.DaggerInAppMessageComponent;
import com.google.firebase.inappmessaging.display.internal.injection.modules.InflaterModule;
import com.google.firebase.inappmessaging.model.InAppMessage;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BindingWrapperFactory {
    private final Application application;

    @Inject
    BindingWrapperFactory(Application application) {
        this.application = application;
    }

    public BindingWrapper createImageBindingWrapper(InAppMessageLayoutConfig config, InAppMessage inAppMessage) {
        InAppMessageComponent inAppMessageComponent = (InAppMessageComponent) DaggerInAppMessageComponent.builder().inflaterModule(new InflaterModule(inAppMessage, config, this.application)).build();
        return inAppMessageComponent.imageBindingWrapper();
    }

    public BindingWrapper createModalBindingWrapper(InAppMessageLayoutConfig config, InAppMessage inAppMessage) {
        InAppMessageComponent inAppMessageComponent = (InAppMessageComponent) DaggerInAppMessageComponent.builder().inflaterModule(new InflaterModule(inAppMessage, config, this.application)).build();
        return inAppMessageComponent.modalBindingWrapper();
    }

    public BindingWrapper createBannerBindingWrapper(InAppMessageLayoutConfig config, InAppMessage inAppMessage) {
        InAppMessageComponent inAppMessageComponent = (InAppMessageComponent) DaggerInAppMessageComponent.builder().inflaterModule(new InflaterModule(inAppMessage, config, this.application)).build();
        return inAppMessageComponent.bannerBindingWrapper();
    }

    public BindingWrapper createCardBindingWrapper(InAppMessageLayoutConfig config, InAppMessage inAppMessage) {
        InAppMessageComponent inAppMessageComponent = (InAppMessageComponent) DaggerInAppMessageComponent.builder().inflaterModule(new InflaterModule(inAppMessage, config, this.application)).build();
        return inAppMessageComponent.cardBindingWrapper();
    }
}
