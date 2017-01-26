package com.moc.chitchat.validator;

import com.moc.chitchat.application.ApplicationLoader;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import org.junit.Test;
import org.springframework.validation.MapBindingResult;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by spiros on 25/01/17.
 */
public class ValidatorTest {


    //Test #1 check username empty password empty passwordretype empty
    @Test
    public void testusercredentials() throws ValidationException {
       UserModel usermodel =  new UserModel("");
        usermodel.setPassword("");
        usermodel.setPasswordCheck("");
        MapBindingResult errors = new MapBindingResult(new HashMap<String,String>(), UserModel.class.getName());
        UserValidator userValidator = new UserValidator();
        userValidator.validate(usermodel,errors);
        assertEquals(errors.hasFieldErrors("username"),true);
        assertEquals(errors.hasFieldErrors("password"),true);
        assertEquals(errors.hasFieldErrors("passwordCheck"),false);
        assertEquals(errors.hasFieldErrors("passwordType"),true);

    }
    // Test #2 NO NUMBERS NO UPER CASE LETTERS NO SUMBOLS
    @Test
    public void testusercredentials2() throws ValidationException {
        UserModel usermodel =  new UserModel("James");
        usermodel.setPassword("koaos");
        usermodel.setPasswordCheck("koaos");
        MapBindingResult errors = new MapBindingResult(new HashMap<String,String>(), UserModel.class.getName());
        UserValidator userValidator = new UserValidator();
        userValidator.validate(usermodel,errors);
        assertEquals(errors.hasFieldErrors("username"),false);
        assertEquals(errors.hasFieldErrors("password"),false);
        assertEquals(errors.hasFieldErrors("passwordCheck"),false);
        assertEquals(errors.hasFieldErrors("passwordType"),true);

    }

    // Test #3 MISMATCH OF THE TWO PASSWORDS
    @Test
    public void testusercredentials3() throws ValidationException {
        UserModel usermodel =  new UserModel("James");
        usermodel.setPassword("koaos123");
        usermodel.setPasswordCheck("koaos");
        MapBindingResult errors = new MapBindingResult(new HashMap<String,String>(), UserModel.class.getName());
        UserValidator userValidator = new UserValidator();
        userValidator.validate(usermodel,errors);
        assertEquals(errors.hasFieldErrors("username"),false);
        assertEquals(errors.hasFieldErrors("password"),false);
        assertEquals(errors.hasFieldErrors("passwordCheck"),true);
        assertEquals(errors.hasFieldErrors("passwordType"),true);

    }
    // Test #4 IT MISSES THE SPECIAL CHARACTERS
    @Test
    public void testusercredentials4() throws ValidationException {
        UserModel usermodel =  new UserModel("James");
        usermodel.setPassword("koaos123");
        usermodel.setPasswordCheck("koaos123");
        MapBindingResult errors = new MapBindingResult(new HashMap<String,String>(), UserModel.class.getName());
        UserValidator userValidator = new UserValidator();
        userValidator.validate(usermodel,errors);
        assertEquals(errors.hasFieldErrors("username"),false);
        assertEquals(errors.hasFieldErrors("password"),false);
        assertEquals(errors.hasFieldErrors("passwordCheck"),false);
        assertEquals(errors.hasFieldErrors("passwordType"),true);

    }
    // Test #5 length less than 8 characters
    @Test
    public void testusercredentials5() throws ValidationException {
        UserModel usermodel =  new UserModel("James");
        usermodel.setPassword("Kaos12&");
        usermodel.setPasswordCheck("Kaos12&");
        MapBindingResult errors = new MapBindingResult(new HashMap<String,String>(), UserModel.class.getName());
        UserValidator userValidator = new UserValidator();
        userValidator.validate(usermodel,errors);
        assertEquals(errors.hasFieldErrors("username"),false);
        assertEquals(errors.hasFieldErrors("password"),false);
        assertEquals(errors.hasFieldErrors("passwordCheck"),false);
        assertEquals(errors.hasFieldErrors("passwordType"),true);

    }
    // Test #6 length less than 8 characters
    @Test
    public void testusercredentials6() throws ValidationException {
        UserModel usermodel =  new UserModel("James");
        usermodel.setPassword("Kaos12ar&");
        usermodel.setPasswordCheck("Kaos12ar&");
        MapBindingResult errors = new MapBindingResult(new HashMap<String,String>(), UserModel.class.getName());
        UserValidator userValidator = new UserValidator();
        userValidator.validate(usermodel,errors);
        assertEquals(errors.hasFieldErrors("username"),false);
        assertEquals(errors.hasFieldErrors("password"),false);
        assertEquals(errors.hasFieldErrors("passwordCheck"),false);
        assertEquals(errors.hasFieldErrors("passwordType"),false);

    }
    // Test #7 it misses the low case letters
    @Test
    public void testusercredentials7() throws ValidationException {
        UserModel usermodel =  new UserModel("James");
        usermodel.setPassword("KAS12AR&");
        usermodel.setPasswordCheck("KAS12AR&");
        MapBindingResult errors = new MapBindingResult(new HashMap<String,String>(), UserModel.class.getName());
        UserValidator userValidator = new UserValidator();
        userValidator.validate(usermodel,errors);
        assertEquals(errors.hasFieldErrors("username"),false);
        assertEquals(errors.hasFieldErrors("password"),false);
        assertEquals(errors.hasFieldErrors("passwordCheck"),false);
        assertEquals(errors.hasFieldErrors("passwordType"),true);

    }
    // Test #8 it misses the uper case letters
    @Test
    public void testusercredentials8() throws ValidationException {
        UserModel usermodel =  new UserModel("James");
        usermodel.setPassword("kas12ar&");
        usermodel.setPasswordCheck("kas12ar&");
        MapBindingResult errors = new MapBindingResult(new HashMap<String,String>(), UserModel.class.getName());
        UserValidator userValidator = new UserValidator();
        userValidator.validate(usermodel,errors);
        assertEquals(errors.hasFieldErrors("username"),false);
        assertEquals(errors.hasFieldErrors("password"),false);
        assertEquals(errors.hasFieldErrors("passwordCheck"),false);
        assertEquals(errors.hasFieldErrors("passwordType"),true);

    }
    // Test #9 it misses the NUMBERS
    @Test
    public void testusercredentials9() throws ValidationException {
        UserModel usermodel =  new UserModel("James");
        usermodel.setPassword("kasASar&");
        usermodel.setPasswordCheck("kasASar&");
        MapBindingResult errors = new MapBindingResult(new HashMap<String,String>(), UserModel.class.getName());
        UserValidator userValidator = new UserValidator();
        userValidator.validate(usermodel,errors);
        assertEquals(errors.hasFieldErrors("username"),false);
        assertEquals(errors.hasFieldErrors("password"),false);
        assertEquals(errors.hasFieldErrors("passwordCheck"),false);
        assertEquals(errors.hasFieldErrors("passwordType"),true);

    }
    // Test #9 NO letters(lower&upper ) included to the password
    @Test
    public void testusercredentials10() throws ValidationException {
        UserModel usermodel =  new UserModel("James");
        usermodel.setPassword("1235^&%&");
        usermodel.setPasswordCheck("1235^&%&");
        MapBindingResult errors = new MapBindingResult(new HashMap<String,String>(), UserModel.class.getName());
        UserValidator userValidator = new UserValidator();
        userValidator.validate(usermodel,errors);
        assertEquals(errors.hasFieldErrors("username"),false);
        assertEquals(errors.hasFieldErrors("password"),false);
        assertEquals(errors.hasFieldErrors("passwordCheck"),false);
        assertEquals(errors.hasFieldErrors("passwordType"),true);

    }
}
