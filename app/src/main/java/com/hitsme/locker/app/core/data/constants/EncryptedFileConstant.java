package com.hitsme.locker.app.core.data.constants;

/**
 * Created by 10093 on 2017/5/27.
 */

public class EncryptedFileConstant {
    public static final int HEADER_VERSION_SIZE = 1;
    public static final String HEADER_VERSION = "00000001";
    public static final byte HEADER_VERSION_IN_BYTE = 1;
    public static final long START_OF_VERSION = 0L;
    public static final long START_OF_KEY = 1L;
    public static final long START_OF_INDEX_STRUCTURE = 81L;
    public static final long START_OF_FILES = 89L;
    public static Integer LONG_IN_BYTE = Integer.valueOf(8);

    public EncryptedFileConstant() {
    }
}