package com.alok.postapp.data;

import java.util.Map;

public class ConstraintMessageMapper {
    public static final Map<String, String> CONSTRAINT_MESSAGES = Map.of(
            "unique_user_email", "Email already exists"
    );
}
