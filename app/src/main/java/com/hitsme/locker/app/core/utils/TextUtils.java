package com.hitsme.locker.app.core.utils;

public class TextUtils {
    public TextUtils() {
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static String createString(String base, String param1) {
        return base.replace("%1$s", param1);
    }
}
