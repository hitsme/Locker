package com.hitsme.locker.app.data.converters;

import com.hitsme.locker.app.data.Clases.Vault;
import com.hitsme.locker.app.data.Clases.VaultContent;
import com.hitsme.locker.app.utils.MediaUtils;
import com.hitsme.locker.app.core.clases.LockFile;
import com.hitsme.locker.app.core.utils.FileUtils;

import java.io.File;

/**
 * Created by 10093 on 2017/4/11.
 */

public class Converter {

    public static Vault convertVault(File f){
        Vault v=new Vault();
        v.setFullPath(f.getAbsolutePath());
        v.setName(FileUtils.removeExtensionFile(f.getName()));
        v.setSize(FileUtils.sizeToString(FileUtils.getSize(f)));
        return v;
    }


    public static VaultContent convertVaultContent(LockFile lockFile){
        VaultContent content = new VaultContent();
        content.setFullPath(lockFile.getFullPath());
        content.setExtrayendo(false);
        content.setId(lockFile.getId());
        content.setSize(FileUtils.sizeToString(lockFile.getSize()));
        content.setType(lockFile.getType());
        String extension = FileUtils.getExtensionFile(lockFile.getFullPath());
        content.setEsVideo(MediaUtils.isExtensionVideo(extension));
        return content;
    }
}
