package com.warehouse.auth.infrastructure.adapter.primary.validator;

import com.warehouse.auth.domain.helper.Result;
import com.warehouse.auth.domain.model.User;

import java.util.ArrayList;
import java.util.List;

public class RoleValidator {

    public Result<Void, List<String>> validatePermission(final String permission) {
        final List<String> errors = new ArrayList<>();
        if (permission == null || permission.isBlank()) {
            errors.add("Permission name cannot be null or empty");
        }

        try {
            User.Permission.valueOf(permission);
        } catch (final IllegalArgumentException e) {
            errors.add("Unknown permission: " + permission);
        }

        return errors.isEmpty() ? Result.success() : Result.failure(errors);
    }
}
