package com.hitsme.locker.app.core.datatype;

import java.io.InputStream;

public interface INewFile {
    InputStream getIn();

    String getName();

    InputStream getPreview();
}
