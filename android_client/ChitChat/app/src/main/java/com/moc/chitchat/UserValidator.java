package com.moc.chitchat;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;

public class UserValidator extends ValidatorHandler<UserModel> implements Validator<UserModel>{

    @Override
    public boolean validate(ValidatorContext context, UserModel user) {
        boolean validatorResult = true;
        if (user.getUsername().isEmpty()) {
            context.addError(ValidationError.create("Username cannot be empty"));
            validatorResult = false;
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            context.addError(ValidationError.create("Password cannot be empty"));
            validatorResult = false;
        }

        if (user.getRePassword() == null || !user.getRePassword().equals(user.getPassword())) {
            context.addError(ValidationError.create("The two password inputs should match"));
            validatorResult = false;
        }
        return validatorResult;
    }
};

