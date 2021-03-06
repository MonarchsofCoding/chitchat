package com.moc.chitchat;

import android.app.Application;

import com.moc.chitchat.application.ChitChatMessagesConfiguration;
import com.moc.chitchat.application.CurrentChatConfiguration;
import com.moc.chitchat.application.SessionConfiguration;
import com.moc.chitchat.crypto.CryptoBox;
import com.moc.chitchat.resolver.ErrorResponseResolver;
import com.moc.chitchat.resolver.UserResolver;
import com.moc.chitchat.validator.UserValidator;

import dagger.Module;
import dagger.Provides;

import java.security.NoSuchAlgorithmException;

import javax.inject.Singleton;


/**
 * ApplicationModule provides the objects that can be injected via Dagger2 DI.
 * NOTE: Add any leaf objects that will need be injected here.
 */
@Module
public class ApplicationModule {

    private Application m7Application;

    public ApplicationModule(Application application) {
        this.m7Application = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return this.m7Application;
    }

    @Provides
    @Singleton
    UserResolver provideUserResolver() {
        return new UserResolver();
    }

    @Provides
    @Singleton
    UserValidator provideUserValidator() {
        return new UserValidator();
    }

    @Provides
    @Singleton
    ErrorResponseResolver provideErrorResponseResolver() {
        return new ErrorResponseResolver();
    }

    @Provides
    @Singleton
    SessionConfiguration provideSessionConfiguration() {
        return new SessionConfiguration();
    }

    @Provides
    @Singleton
    CurrentChatConfiguration provideCurrentChatConfiguration() {
        return new CurrentChatConfiguration();
    }

    @Provides
    @Singleton
    ChitChatMessagesConfiguration provideChitChatMessagesConfiguration() {
        return new ChitChatMessagesConfiguration();
    }

    @Provides
    @Singleton
    CryptoBox provideCryptoBox() {
        try {
            return new CryptoBox();
        } catch (NoSuchAlgorithmException algorithmException) {
            algorithmException.printStackTrace();
        }
        return null;
    }
}
