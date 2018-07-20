package com.hitsme.locker.app.core.utils;

import com.hitsme.locker.app.core.data.constants.EncryptedFileConstant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class FileUtils {
    public static final String LOCK_EXTENSION = "lock";
    public static final String TEMP_EXTENSION = "temp";
    public static final int BUFFER_SIZE = 8192;

    public FileUtils() {
    }

    public static void getFileAndSubFiles(Collection<File> files, File actual) {
        if(actual.isDirectory()) {
            File[] var2;
            int var3 = (var2 = actual.listFiles()).length;

            for(int var4 = 0; var4 < var3; ++var4) {
                File hijo = var2[var4];
                getFileAndSubFiles(files, hijo);
            }
        } else {
            files.add(actual);
        }

    }

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(EncryptedFileConstant.LONG_IN_BYTE.intValue());
        buffer.putLong(x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(EncryptedFileConstant.LONG_IN_BYTE.intValue());
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static void copy(InputStream from, OutputStream to) throws IOException {
        byte[] data = new byte[8192];

        int count;
        while((count = from.read(data, 0, 8192)) != -1) {
            to.write(data, 0, count);
        }

    }

    public static long copy(InputStream from, OutputStream to, long sizeOfByte) throws IOException {
        long total = 0L;
        byte[] data = new byte[8192];

        do {
            int toRead = (int)(total + 8192L > sizeOfByte?sizeOfByte - total:8192L);
            int r = from.read(data, 0, toRead);
            if(r == -1) {
                break;
            }

            to.write(data, 0, r);
            total += (long)r;
        } while(total != sizeOfByte);

        return total;
    }

    public static String getNameRelativeToBase(File baseF, File node) {
        String fileName = node.getAbsoluteFile().toString();
        int x = baseF.getParent().length() + 1;
        int y = fileName.length();
        return fileName.substring(x, y);
    }

    public static String createNewFileNameInPath(String path, String name, String extension) {
        int i = 0;
        if(!TextUtils.isEmpty(extension)) {
            extension = "." + extension;
        }

        String iter = "";
        (new StringBuilder(String.valueOf(path))).append(name).append(extension).toString();

        while((new File(path + name + iter + extension)).exists()) {
            ++i;
            iter = " (" + i + ")";
        }

        return path + name + iter + extension;
    }

    public static long getSize(List<File> inFileList) {
        long size = 0L;

        File a;
        for(Iterator var3 = inFileList.iterator(); var3.hasNext(); size += getSize(a)) {
            a = (File)var3.next();
        }

        return size;
    }

    public static long getSize(File file) {
        if(!file.isDirectory()) {
            return file.length();
        } else {
            long length = 0L;
            File[] var3;
            int var4 = (var3 = file.listFiles()).length;

            for(int var5 = 0; var5 < var4; ++var5) {
                File iter = var3[var5];
                length += getSize(iter);
            }

            return length;
        }
    }

    public static String sizeToString(long tamF) {
        DecimalFormat formatter = new DecimalFormat("#########.##");
        String fileSize = String.valueOf(tamF) + " B";
        double aux = (double)tamF;
        aux /= 1024.0D;
        if(aux > 1.0D) {
            fileSize = String.valueOf(formatter.format(aux)) + " kB";
        }

        aux /= 1024.0D;
        if(aux > 1.0D) {
            fileSize = String.valueOf(formatter.format(aux)) + " MB";
        }

        aux /= 1024.0D;
        if(aux > 1.0D) {
            fileSize = String.valueOf(formatter.format(aux)) + " GB";
        }

        return fileSize;
    }

    public static String getExtensionFile(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf(46);
        if(i > 0) {
            extension = fileName.substring(i + 1);
        }

        return extension.toLowerCase();
    }

    public static String removeExtensionFile(String fileName) {
        int i = fileName.lastIndexOf(46);
        return i > 0?fileName.substring(0, i):fileName;
    }

    public static String getTempPathFileForFile(File file) {
        String basePath = file.getParent() + File.separator;
        String name = removeExtensionFile(file.getName());
        String extension = getExtensionFile(file.getName());
        if(!TextUtils.isEmpty(extension)) {
            extension = "." + extension;
        }

        extension = extension + "." + "temp";

        Integer digits;
        for(digits = getRandomDigit(); (new File(basePath + name + "_" + digits + extension)).exists(); digits = Integer.valueOf(digits.intValue() + 1)) {
            ;
        }

        return basePath + name + "_" + digits + extension;
    }

    private static Integer getRandomDigit() {
        Random rand = new Random();
        return Integer.valueOf(0 + rand.nextInt(10000000));
    }

    public static int byteArrayToInt(byte[] b) {
        return b[3] & 255 | (b[2] & 255) << 8 | (b[1] & 255) << 16 | (b[0] & 255) << 24;
    }

    public static void delete(File file) throws IOException {
        if(file.isDirectory()) {
            if(file.list().length == 0) {
                file.delete();
            } else {
                String[] files = file.list();
                String[] var2 = files;
                int var3 = files.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    String temp = var2[var4];
                    File fileDelete = new File(file, temp);
                    delete(fileDelete);
                }

                if(file.list().length == 0) {
                    file.delete();
                }
            }
        } else {
            file.delete();
        }

    }

    public static void delete(List<File> files) throws IOException {
        Iterator var1 = files.iterator();

        while(var1.hasNext()) {
            File f = (File)var1.next();
            delete(f);
        }

    }

    public static int countFileNodes(List<File> inFileList) {
        int count = 0;

        File a;
        for(Iterator var2 = inFileList.iterator(); var2.hasNext(); count += countFileNodes(a)) {
            a = (File)var2.next();
        }

        return count;
    }

    public static int countFileNodes(File f) {
        if(f == null) {
            return 0;
        } else {
            int countFiles = 1;
            if(f.isDirectory()) {
                File[] files = f.listFiles();
                if(files != null) {
                    File[] var3 = files;
                    int var4 = files.length;

                    for(int var5 = 0; var5 < var4; ++var5) {
                        File iter = var3[var5];
                        countFiles += countFileNodes(iter);
                    }
                }
            }

            return countFiles;
        }
    }

    public static boolean esBobeda(File file) {
        if(!file.exists()) {
            return false;
        } else if(file.isDirectory()) {
            return false;
        } else {
            String extension = getExtensionFile(file.getName());
            return isLockExtension(extension);
        }
    }

    public static boolean isLockExtension(String extension) {
        return "lock".equals(extension);
    }

    public static String createNameForFile() {
        String name = "lockFile";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hhmm ");
        String actualDate = df.format(c.getTime());
        name = actualDate + name;
        return name;
    }
}
