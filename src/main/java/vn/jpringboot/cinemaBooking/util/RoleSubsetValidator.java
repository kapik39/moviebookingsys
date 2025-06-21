package vn.jpringboot.cinemaBooking.util;

import java.util.Arrays;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleSubsetValidator implements ConstraintValidator<RoleSubset, UserRole> {
    private UserRole[] roles;

    @Override
    public void initialize(RoleSubset constraint) {
        this.roles = constraint.anyOf();
    }

    @Override
    public boolean isValid(UserRole value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(roles).contains(value);
    }

}
