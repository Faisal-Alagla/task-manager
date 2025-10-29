package com.faisal.taskmanager.utils;

import java.util.regex.Pattern;

public class RegexUtils {

    public static boolean isValidUUID(String value) {
        return UUID_REGEX.matcher(value).matches();
    }

    private static final String UUID_PATTERN = "^" +
            "[0-9a-fA-F]{8}-" +
            "[0-9a-fA-F]{4}-" +
            "[0-9a-fA-F]{4}-" +
            "[0-9a-fA-F]{4}-" +
            "[0-9a-fA-F]{12}" +
            "$";

    private static final Pattern UUID_REGEX = Pattern.compile(UUID_PATTERN);

}
