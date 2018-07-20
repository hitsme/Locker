package com.hitsme.locker.app.data.converters;

import com.hitsme.locker.app.data.Clases.ImageToVault;
import com.hitsme.locker.app.data.Clases.VideoToVault;
import com.hitsme.locker.app.utils.MediaUtils;
import com.hitsme.locker.app.core.datatype.DataNewFile;
import com.hitsme.locker.app.core.datatype.INewFile;
import com.hitsme.locker.app.core.utils.FileUtils;

import java.io.File;

/**
 * Created by 10093 on 2017/4/11.
 */

public class FileConverter {

    public static INewFile getFileToVault(File file){
        INewFile res = null;
        String extendion = FileUtils.getExtensionFile(file.getName());
        if (MediaUtils.isExtensionImage(extendion)){
            res = new ImageToVault(file);
        }else if (MediaUtils.isExtensionVideo(extendion)){
            res = new VideoToVault(file);
        }else{
            res = new DataNewFile(file);
        }
        return res;
    }
}
