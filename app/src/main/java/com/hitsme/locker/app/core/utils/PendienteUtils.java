package com.hitsme.locker.app.core.utils;

import com.hitsme.locker.app.core.datatype.INewFile;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class PendienteUtils {
    public PendienteUtils() {
    }

    public static void addNamesDataFile(List<String> pendintes, List<INewFile> files) {
        Iterator var2 = files.iterator();

        while(var2.hasNext()) {
            INewFile file = (INewFile)var2.next();
            pendintes.add(file.getName());
        }

    }

    public static void addNamesFile(List<String> pendintes, List<File> files) {
        Iterator var2 = files.iterator();

        while(var2.hasNext()) {
            File file = (File)var2.next();
            pendintes.add(file.getName());
        }

    }

    public static void removeNamesDataFile(List<String> pendintes, List<INewFile> files) {
        Iterator var2 = files.iterator();

        while(var2.hasNext()) {
            INewFile file = (INewFile)var2.next();
            pendintes.remove(file.getName());
        }

    }

    public static void removeNamesFile(List<String> pendintes, List<File> files) {
        Iterator var2 = files.iterator();

        while(var2.hasNext()) {
            File file = (File)var2.next();
            pendintes.remove(file.getName());
        }

    }
}
