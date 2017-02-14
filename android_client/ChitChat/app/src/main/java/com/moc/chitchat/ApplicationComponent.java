package com.moc.chitchat;


import com.moc.chitchat.activity.LoginActivity;
import com.moc.chitchat.activity.RegistrationActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * ApplicationComponent provides an interface for the injectable classes.
 * NOTE: Add any classes that will use field injection here (Activities).
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(RegistrationActivity activity);
    void inject(LoginActivity activity);

}
