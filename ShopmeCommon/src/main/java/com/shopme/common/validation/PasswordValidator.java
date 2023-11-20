package com.shopme.common.validation;

import com.shopme.common.annotations.PasswordMatching;
import com.shopme.common.entity.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordMatching, Object> {

    @Override
    public void initialize(PasswordMatching matching) {

    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        User user = (User) obj;

        //https://stackoverflow.com/questions/24955817/jsr303-custom-validators-being-called-twice
        if (user.getNewPassword() == null) {
            return true;
        }

        return user.getNewPassword().equals(user.getMatchingPassword());
    }
}
