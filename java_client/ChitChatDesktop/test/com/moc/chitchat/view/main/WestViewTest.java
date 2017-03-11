package com.moc.chitchat.view.main;

import org.testfx.framework.junit.ApplicationTest;

/**
 * WestViewTest provides tests for the left section of the MainView.
 */
public class WestViewTest {

    public static final String togglePaneBtn = "#west-toggle-btn";

    public static void enterSearchView(ApplicationTest testContext) {

        testContext.clickOn(togglePaneBtn);
    }
}
