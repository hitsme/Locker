package com.hitsme.locker.app.core.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LockFileInpuStream extends InputStream {
    InputStream file;
    long entryOffset = 0L;
    long entrySize;

    public LockFileInpuStream(FileInputStream file, long posOfStart, long size) throws IOException {
        this.entrySize = size;
        file.skip(posOfStart);
        this.file = file;
    }

    public int read() throws IOException {
        if(this.available() <= 0) {
            return -1;
        } else {
            ++this.entryOffset;
            return this.file.read();
        }
    }

    public int read(byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }

    public int read(byte[] buf, int offset, int numToRead) throws IOException {
        boolean totalRead = false;
        if(this.entryOffset >= this.entrySize) {
            return -1;
        } else {
            numToRead = Math.min(numToRead, this.available());
            int totalRead1 = this.file.read(buf, offset, numToRead);
            if(totalRead1 == -1) {
                if(numToRead > 0) {
                    throw new IOException("Truncated file");
                }

                this.entryOffset = this.entrySize;
            } else {
                this.entryOffset += (long)totalRead1;
            }

            return totalRead1;
        }
    }

    public long skip(long n) throws IOException {
        this.entryOffset += n;
        return this.file.skip(n);
    }

    public int available() throws IOException {
        return this.entrySize - this.entryOffset > 2147483647L?2147483647:(int)(this.entrySize - this.entryOffset);
    }

    public void close() throws IOException {
        this.file.close();
    }

    public boolean markSupported() {
        return false;
    }
}
