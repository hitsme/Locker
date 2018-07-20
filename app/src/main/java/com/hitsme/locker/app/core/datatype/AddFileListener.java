package com.hitsme.locker.app.core.datatype;

public class AddFileListener {
    private int adeedFiles = 0;

    public AddFileListener() {
    }

    public void fileAdded() {
        ++this.adeedFiles;
    }

    public int getAddedFiles() {
        return this.adeedFiles;
    }
}
