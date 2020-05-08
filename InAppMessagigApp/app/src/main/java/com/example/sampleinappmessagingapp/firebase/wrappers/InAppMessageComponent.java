package com.example.sampleinappmessagingapp.firebase.wrappers;

import com.google.firebase.inappmessaging.display.internal.injection.modules.InflaterModule;
import com.google.firebase.inappmessaging.display.internal.injection.scopes.InAppMessageScope;

import dagger.Component;

@Component(
        modules = {InflaterModule.class}
)
@InAppMessageScope
public interface InAppMessageComponent {
    @InAppMessageScope
    ImageBindingWrapper imageBindingWrapper();

    @InAppMessageScope
    ModalBindingWrapper modalBindingWrapper();

    @InAppMessageScope
    BannerBindingWrapper bannerBindingWrapper();

    @InAppMessageScope
    CardBindingWrapper cardBindingWrapper();
}
