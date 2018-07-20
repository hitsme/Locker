package com.hitsme.locker.app.core.clases;

/**
 * Created by 10093 on 2017/5/27.
 */

public class LockFile {
    private String id;
    private FileType type;
    private String fullPath;
    private Long start;
    private Long size;
    private String previewId;

    public LockFile() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FileType getType() {
        return this.type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public String getFullPath() {
        return this.fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public Long getStart() {
        return this.start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getSize() {
        return this.size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getPreviewId() {
        return this.previewId;
    }

    public void setPreviewId(String previewId) {
        this.previewId = previewId;
    }
}
