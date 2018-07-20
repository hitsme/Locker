package com.hitsme.locker.app.core.clases;

/**
 * Created by 10093 on 2017/5/27.
 */

import java.util.LinkedHashMap;

public class FileSystemStructure {
    private int idSequence;
    private LinkedHashMap<String,LockFile> files;

    public FileSystemStructure() {
    }

    public int getIdSequence() {
        return this.idSequence;
    }

    public void setIdSequence(int idSequence) {
        this.idSequence = idSequence;
    }

    public LinkedHashMap<String, LockFile> getFiles() {
        return this.files;
    }

    public void setFiles(LinkedHashMap<String, LockFile> files) {
        this.files = files;
    }
}
