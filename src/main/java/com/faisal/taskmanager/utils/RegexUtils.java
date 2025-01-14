package com.faisal.taskmanager.utils;

import java.util.regex.Pattern;

public class RegexUtils {

    public static final String ENGLISH_LITTERS = "A-Za-z";
    public static final String ARABIC_LITTERS = "\\u0621-\\u064A";

    public static final String ENGLISH_NUMBERS = "0-9";
    public static final String ARABIC_NUMBERS = "\\u0660-\\u0669";

    public static final String ENGLISH_COMMA = ",";
    public static final String ARABIC_COMMA = "\\u060C";

    public static final String FULL_STOP = ".";
    public static final String SPACE = "\\s";

    public static final String UUID_PATTERN = "^" +
            "[0-9a-fA-F]{8}-" +
            "[0-9a-fA-F]{4}-" +
            "[0-9a-fA-F]{4}-" +
            "[0-9a-fA-F]{4}-" +
            "[0-9a-fA-F]{12}" +
            "$";

    public static final String ALLOWED_SEARCH_PATTERN = "^[" +
            ARABIC_LITTERS +
            ENGLISH_LITTERS +
            ARABIC_NUMBERS +
            ENGLISH_NUMBERS +
            ARABIC_COMMA +
            ENGLISH_COMMA +
            FULL_STOP +
            SPACE +
            "]+$";

    public static final String EMAIL_PATTERN = "^" +
            "[A-Za-z0-9]" +
            "([_\\-\\.]{0,1}[A-Za-z0-9]+)*" +
            "@" +
            "([A-Za-z0-9]+\\.)+" +
            "[A-Za-z0-9]{2,}" +
            "$";

    public static final String MOBILE_PATTERN = "^" +
            "(\\+\\d{1,3}[\\-\\s]{0,1}){0,1}" +
            "\\d{9,10}" +
            "$";

    public static final String PERSON_NAME_PATTERN = "^" +
            "([" + ENGLISH_LITTERS + "]{1}([\\s]{1}[" + ENGLISH_LITTERS + "])+)" +
            "|" +
            "([" + ARABIC_LITTERS + "]{1}([\\s]{1}[" + ARABIC_LITTERS + "])+)" +
            "$";

    private static final Pattern UUID_REGEX = Pattern.compile(UUID_PATTERN);
    private static final Pattern ALLOWED_SEARCH_REGEX = Pattern.compile(ALLOWED_SEARCH_PATTERN);

    public static boolean isValidUUID(String value) {
        return UUID_REGEX.matcher(value).matches();
    }

    public static boolean isValidSearchText(String value) {
        return ALLOWED_SEARCH_REGEX.matcher(value).matches();
    }
}
