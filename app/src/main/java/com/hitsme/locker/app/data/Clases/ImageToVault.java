package com.hitsme.locker.app.data.Clases;

import android.graphics.Bitmap;

import com.hitsme.locker.app.utils.MediaUtils;
import com.hitsme.locker.app.core.datatype.DataNewFile;

import java.io.File;
import java.io.InputStream;

/**
 * Created by 10093 on 2017/4/5.
 */

public class ImageToVault extends DataNewFile {

    public ImageToVault(File in) {
        super(in);
    }




    @Override
    public InputStream getPreview() {
        Bitmap tumb =MediaUtils.getImagePreview(in.getAbsolutePath());
        return MediaUtils.bitMapToInputStream(tumb);
    }


}
