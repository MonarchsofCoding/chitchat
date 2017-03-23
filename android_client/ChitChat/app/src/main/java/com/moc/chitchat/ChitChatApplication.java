package com.moc.chitchat;

import android.app.Application;

/**
 * ChitChatApplication provides the application to be initialised by Dagger2.
 */
public class ChitChatApplication extends Application {

    private ApplicationComponent myComponent8;

    @Override
    public void onCreate() {
        super.onCreate();

        myComponent8 = DaggerApplicationComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .build()
        ;
    }

    public ApplicationComponent getComponent() {
        return this.myComponent8;
    }
}
