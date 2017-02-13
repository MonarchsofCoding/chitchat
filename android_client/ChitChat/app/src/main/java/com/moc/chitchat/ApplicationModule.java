package com.moc.chitchat;

import android.app.Application;

import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.resolver.ErrorResponseResolver;
import com.moc.chitchat.resolver.UserResolver;
import com.moc.chitchat.validator.UserValidator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * ApplicationModule provides the objects that can be injected via Dagger2 DI.
 * NOTE: Add any leaf objects that will need be injected here.
 */
@Module
public class ApplicationModule {

    private Application mApplication;

    public ApplicationModule(Application application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return this.mApplication;
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
    HttpClient provideHttpClient() {
        return new HttpClient();
    }

    @Provides
    @Singleton
    ErrorResponseResolver provideErrorResponseResolver() {
        return new ErrorResponseResolver();
    }
}
