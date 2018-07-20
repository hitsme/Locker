package com.hitsme.locker.app.data.Clases;

import android.graphics.Bitmap;

import com.hitsme.locker.app.utils.MediaUtils;
import com.hitsme.locker.app.core.datatype.DataNewFile;

import java.io.File;
import java.io.InputStream;

/**
 * Created by 10093 on 2017/4/8.
 */

public class VideoToVault extends DataNewFile {

    public VideoToVault(File in) {
        super(in);
    }
    public InputStream getPreview() {
        Bitmap tumb = MediaUtils.getVideoPreview(in.getAbsolutePath());
        return MediaUtils.bitMapToInputStream(tumb);
    }

}
