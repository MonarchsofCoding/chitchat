package com.moc.chitchat.application;

import com.moc.chitchat.model.UserModel;
import java.util.Observable;
import org.springframework.stereotype.Component;


/**
 * Configuration provides global configuration variables for the application.
 */
@Component
public class Configuration extends Observable {

    private static final String PROD_BACKEND_ADDRESS = "https://chitchat.monarchsofcoding.com";
    private static final String BETA_BACKEND_ADDRESS = "https://beta.chitchat.monarchsofcoding.com";
    private static final String TEST_BACKEND_ADDRESS = "http://chitchat:4000";
    private static final String DEV_BACKEND_ADDRESS = "http://localhost:4000";

    private String backendAddress;

    private UserModel currentLoggedInUser;

    public Configuration() {
        super();
        this.backendAddress = PROD_BACKEND_ADDRESS;
    }

    /**
     * getBackendAddress provides the backend address for the API.
     * @return String the backend address for the API.
     */
    public String getBackendAddress() {
        return this.backendAddress;
    }

    /**
     * setDevelopmentMode applies changes to the configuration for use when developing.
     */
    public void setDevelopmentMode() {
        System.out.println("\nEntering Development Mode!\n"); // TODO: Use Apache Commons logger!

        this.backendAddress = DEV_BACKEND_ADDRESS;
    }

    /**
     * setBetaMode applies changes to the configuration for use when testing.
     */
    public void setBetaMode() {
        System.out.println("\nEntering Beta Mode!\n"); // TODO: Use Apache Commons logger!

        this.backendAddress = BETA_BACKEND_ADDRESS;
    }

    /**
     * setTestingMode applies changes to the configuration for use when testing.
     */
    public void setTestingMode() {
        System.out.println("\nEntering Test Mode!\n"); // TODO: Use Apache Commons logger!

        this.backendAddress = TEST_BACKEND_ADDRESS;
    }

    /**
     * This method sets the name of the Log in user.
     * @param user the parameter of function.
     */
    public void setLoggedInUser(UserModel user) {
        this.currentLoggedInUser = user;
    }

    /**
     * this method returns the logged in User.
     * @return The current logged user.
     */
    public UserModel getLoggedInUser() {
        return this.currentLoggedInUser;
    }

    /**
     * This function deletes the user for the logged in status.
     */
    public void logout() {
        this.setChanged();
        this.notifyObservers();
        this.currentLoggedInUser = null;
    }
}
