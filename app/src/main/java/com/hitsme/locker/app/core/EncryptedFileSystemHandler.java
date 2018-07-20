package com.hitsme.locker.app.core;

import com.hitsme.locker.app.core.exceptions.LockException;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class EncryptedFileSystemHandler {
    private static final Map<String, WeakReference<EncryptedFileSystem>> _map = new HashMap();

    public EncryptedFileSystemHandler() {
    }

    public static synchronized EncryptedFileSystem createEncryptedFile(String filePath, String password) {
        EncryptedFileSystem fs = EncryptedFileSystem.createEncryptedFile(filePath, password);
        _map.put(filePath, new WeakReference(fs));
        return fs;
    }

    public static synchronized EncryptedFileSystem openEncryptedFile(String filePath, String password) {
        WeakReference ref = (WeakReference)_map.get(filePath);
        EncryptedFileSystem fs = ref != null?(EncryptedFileSystem)ref.get():null;
        if(fs == null) {
            fs = EncryptedFileSystem.openEncryptedFile(filePath, password);
            _map.put(filePath, new WeakReference(fs));
        } else if(!fs.equalPassword(password)) {
            throw new LockException("err_unable_to_open_file_Verify_the_password");
        }

        return fs;
    }

    public static synchronized void removeFromUso(String path) {
        _map.remove(path);
    }
}
