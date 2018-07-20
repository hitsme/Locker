package com.hitsme.locker.app.data.source;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.hitsme.locker.app.Constants;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by 10093 on 2017/4/14.
 */

public class CacheAccessProvider extends ContentProvider {

    public static Uri  getUriForCacheFile(File cacheFile){
        return  Uri.parse("content://"+ Constants.Storage.PACKAGE + cacheFile.getAbsolutePath());
    }


    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        File privateFile = new File(uri.getPath());
        return ParcelFileDescriptor.open(privateFile, ParcelFileDescriptor.MODE_READ_ONLY);
    }

    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        return 0;
    }

    @Override
    public String getType(Uri arg0) {
        return null;
    }

    @Override
    public Uri insert(Uri arg0, ContentValues arg1) {
        return null;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
                        String arg4) {
        return null;
    }

    @Override
    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        return 0;
    }
}