package com.moc.chitchat;

import com.moc.chitchat.activity.ChatListActivity;
import com.moc.chitchat.activity.CurrentChatActivity;
import com.moc.chitchat.activity.LoginActivity;
import com.moc.chitchat.activity.RegistrationActivity;
import com.moc.chitchat.activity.SearchUserActivity;

import dagger.Component;

import javax.inject.Singleton;

/**
 * ApplicationComponent provides an interface for the injectable classes.
 * NOTE: Add any classes that will use field injection here (Activities).
 */

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(RegistrationActivity activity);

    void inject(LoginActivity activity);

    void inject(SearchUserActivity activity);

    void inject(CurrentChatActivity activity);

    void inject(ChatListActivity activity);

}
