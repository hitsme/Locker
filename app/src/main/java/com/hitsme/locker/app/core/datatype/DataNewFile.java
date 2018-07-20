package com.hitsme.locker.app.core.datatype;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DataNewFile implements INewFile {
    protected File in;

    public DataNewFile() {
    }

    public DataNewFile(File in) {
        this.in = in;
    }

    public InputStream getIn() {
        try {
            return new FileInputStream(this.in);
        } catch (FileNotFoundException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public String getName() {
        return this.in.getName();
    }

    public InputStream getPreview() {
        return null;
    }
}
