package com.hitsme.locker.app.data.Clases;

import com.hitsme.locker.app.core.clases.FileType;

/**
 * Created by 10093 on 2017/4/5.
 */

public class VaultContent {
    private String id;
    private FileType type;
    private String fullPath;
    private String size;
    private boolean extrayendo;
    private boolean esVideo;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isExtrayendo() {
        return extrayendo;
    }

    public void setExtrayendo(boolean extrayendo) {
        this.extrayendo = extrayendo;
    }

    public boolean isEsVideo() {
        return esVideo;
    }

    public void setEsVideo(boolean esVideo) {
        this.esVideo = esVideo;
    }
}
