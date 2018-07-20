package com.hitsme.locker.app.core.data;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.hitsme.locker.app.core.clases.FileSystemStructure;
import com.hitsme.locker.app.core.clases.FileType;
import com.hitsme.locker.app.core.clases.LockFile;
import com.hitsme.locker.app.core.data.constants.EncryptedFileConstant;
import com.hitsme.locker.app.core.data.crypto.AES.CoreCrypto;
import com.hitsme.locker.app.core.data.crypto.AES.CoreCrypto.AES;
import com.hitsme.locker.app.core.exceptions.LockException;
import com.hitsme.locker.app.core.utils.FileUtils;
import com.hitsme.locker.app.core.utils.LockFileInpuStream;
import com.hitsme.locker.app.core.utils.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

public  class EncryptedFileSystemDAO {
    private AES cipher;
    private Gson converter;
    private String filePath;
    private FileSystemStructure structure;
    private long posOfStructure;
    private String tempFilePath;
    private FileSystemStructure tempStructure;
    private long tempPosOfStructure;

    public EncryptedFileSystemDAO(String filePath) {
        try {
            this.cipher = CoreCrypto.getCipher();
            this.converter = new Gson();
            this.filePath = filePath;
        } catch (LockException var3) {
            throw var3;
        } catch (Exception var4) {
            throw new LockException("err_error_general", var4);
        }
    }

    public void writeVersion() {
        try {
            File e = new File(this.filePath);
            FileOutputStream out = new FileOutputStream(e);
            byte[] version = new byte[1];
            Arrays.fill(version, Byte.parseByte("00000001", 2));
            out.write(version);
            out.close();
        } catch (LockException var4) {
            throw var4;
        } catch (Exception var5) {
            throw new LockException("err_cant_create", var5);
        }
    }

    public void createKey(String password) {
        try {
            RandomAccessFile e = new RandomAccessFile(this.filePath, "rw");
            e.seek(1L);
            OutputStream os = Channels.newOutputStream(e.getChannel());
            this.cipher.makeKey();
            os = this.cipher.saveKey(password, os);
            os.close();
            e.close();
        } catch (LockException var4) {
            throw var4;
        } catch (Exception var5) {
            throw new LockException("err_cant_create", var5);
        }
    }

    public void initStructure() {
        try {
            this.posOfStructure = 89L;
            this.structure = new FileSystemStructure();
            this.structure.setFiles(new LinkedHashMap());
            this.structure.setIdSequence(0);
            this.writeStructureInFile();
        } catch (LockException var2) {
            throw var2;
        } catch (Exception var3) {
            throw new LockException("err_cant_create", var3);
        }
    }

    public void checkVersion() {
        try {
            File e = new File(this.filePath);
            if(!e.exists()) {
                throw new LockException("err_not_found");
            } else {
                FileInputStream in = new FileInputStream(e);
                byte[] version = new byte[1];
                in.read(version);
                if(version[0]!=1) {
                    throw new LockException("error_version");
                } else {
                    in.close();
                }
            }
        } catch (LockException var4) {
            throw var4;
        } catch (Exception var5) {
            throw new LockException("err_cant_create", var5);
        }
    }

    public void loadKey(String password) {
        try {
            RandomAccessFile e = new RandomAccessFile(this.filePath, "rw");
            e.seek(1L);
            InputStream in = Channels.newInputStream(e.getChannel());
            this.cipher.loadKey(in, password);
            in.close();
            e.close();
        } catch (LockException var4) {
            throw var4;
        } catch (Exception var5) {
            throw new LockException("err_unable_to_open_file_Verify_the_password");
        }
    }

    public void loadStructure() {
        try {
            RandomAccessFile e = new RandomAccessFile(this.filePath, "r");
            e.seek(81L);
            InputStream in = Channels.newInputStream(e.getChannel());
            byte[] buff = new byte[EncryptedFileConstant.LONG_IN_BYTE.intValue()];
            in.read(buff);
            this.posOfStructure = FileUtils.bytesToLong(buff);
            e.seek(this.posOfStructure);
            CipherInputStream in1 = new CipherInputStream(in, this.cipher.getCiphertoDec(in));
            byte[] structureInByte = FileUtils.toByteArray(in1);
            this.structure = (FileSystemStructure)this.converter.fromJson(new String(structureInByte), FileSystemStructure.class);
            in1.close();
            e.close();
        } catch (LockException var5) {
            throw var5;
        } catch (Exception var6) {
            throw new LockException("err_unable_to_open_file_Verify_the_password");
        }
    }

    public LockFile addEncryptedFileFromFileToTemp(InputStream iput, String fullPath) {
        try {
            String e = FileUtils.getExtensionFile(fullPath);
            LockFile encryptedFile = new LockFile();
            encryptedFile.setId(this.nextIdTemp(e));
            encryptedFile.setType(FileType.FILE);
            encryptedFile.setStart(Long.valueOf(this.tempPosOfStructure));
            encryptedFile.setFullPath(fullPath);
            this.tempStructure.getFiles().put(encryptedFile.getId(), encryptedFile);
            this.addFileStreamToEndOfTemp(encryptedFile, iput);
            return encryptedFile;
        } catch (LockException var5) {
            throw var5;
        } catch (Exception var6) {
            throw new LockException("err_failed_to_add_the_file");
        }
    }

    public LockFile addPreviewToTemp(LockFile father, InputStream preview) {
        LockFile previewFile = this.addEncryptedFileFromFileToTemp(preview, father.getFullPath());
        previewFile.setType(FileType.PREVIEW);
        father.setPreviewId(previewFile.getId());
        return previewFile;
    }

    public LockFile addFolderToTemp(String fullPath) {
        LockFile encryptedFile = new LockFile();
        encryptedFile.setId(this.nextIdTemp(""));
        encryptedFile.setType(FileType.FOLDER);
        encryptedFile.setFullPath(fullPath);
        this.tempStructure.getFiles().put(encryptedFile.getId(), encryptedFile);
        return encryptedFile;
    }

    public void extractFile(String id, OutputStream out) {
        try {
            LockFile e = (LockFile)this.structure.getFiles().get(id);
            if(e != null) {
                FileInputStream file = new FileInputStream(new File(this.filePath));
                LockFileInpuStream input = new LockFileInpuStream(file, e.getStart().longValue(), e.getSize().longValue());
                CipherInputStream cipherInputStream = new CipherInputStream(input, this.cipher.getCiphertoDec(input));
                BufferedInputStream cipherInputStream1 = new BufferedInputStream(cipherInputStream);
                BufferedOutputStream out1 = new BufferedOutputStream(out);
                FileUtils.copy(cipherInputStream1, out1);
                out1.close();
                input.close();
            }

        } catch (LockException var7) {
            throw var7;
        } catch (Exception var8) {
            var8.printStackTrace();
            throw new LockException("err_could_not_open_the_file");
        }
    }

    private void addFileStreamToEndOfTemp(LockFile fileDescriptor, InputStream iput) throws Exception {
        FileChannel file = (new FileOutputStream(this.tempFilePath, true)).getChannel();
        file.truncate(this.tempPosOfStructure);
        fileDescriptor.setStart(Long.valueOf(this.tempPosOfStructure));
        OutputStream fileOut = Channels.newOutputStream(file);
        CipherOutputStream cipherOut = new CipherOutputStream(fileOut, this.cipher.getCiphertoEnc(fileOut));
        BufferedOutputStream out = new BufferedOutputStream(cipherOut);
        BufferedInputStream sourceStream = new BufferedInputStream(iput, 8192);
        FileUtils.copy(sourceStream, out);
        sourceStream.close();
        out.close();
        file.close();
        this.tempPosOfStructure = (new File(this.tempFilePath)).length();
        fileDescriptor.setSize(Long.valueOf(this.tempPosOfStructure - fileDescriptor.getStart().longValue()));
    }

    public void delteFilesInTemp(Set<String> todelete) throws IOException {
        FileInputStream originalToRead = new FileInputStream(this.filePath);
        FileOutputStream tempToSave = new FileOutputStream(this.tempFilePath);

        try {
            FileUtils.copy(originalToRead, tempToSave, 89L);
            ArrayList e = new ArrayList(this.tempStructure.getFiles().values());
            Collections.sort(e, new Comparator() {
                public int compare(Object o1, Object o2) {
                    LockFile l1=(LockFile)o1;
                    LockFile l2=(LockFile)o2;
                    return l1.getStart().compareTo(l2.getStart());
                }
            });
            long posActual = 89L;

            for(int iter = 0; iter < e.size(); ++iter) {
                LockFile actual = (LockFile)e.get(iter);
                if(todelete.contains(actual.getId())) {
                    originalToRead.skip(actual.getSize().longValue());
                    this.tempStructure.getFiles().remove(actual.getId());
                } else {
                    actual.setStart(Long.valueOf(posActual));
                    FileUtils.copy(originalToRead, tempToSave, actual.getSize().longValue());
                    posActual += actual.getSize().longValue();
                }
            }

            this.tempPosOfStructure = posActual;
        } catch (LockException var17) {
            throw var17;
        } catch (Exception var18) {
            throw new LockException("err_could_not_delete_the_file");
        } finally {
            tempToSave.flush();

            try {
                tempToSave.getFD().sync();
            } catch (IOException var16) {
                ;
            }

            tempToSave.close();
            originalToRead.close();
        }

    }

    public void delteFoldersInTempStructure(Set<String> listfolderID) throws Exception {
        Iterator var2 = listfolderID.iterator();

        while(var2.hasNext()) {
            String folderId = (String)var2.next();
            this.tempStructure.getFiles().remove(folderId);
        }

    }

    public void writeStructureInFile() throws Exception {
        this.writeStructureInPath(this.filePath, this.posOfStructure, this.structure);
    }

    public void writeStructureInTempFile() throws Exception {
        this.writeStructureInPath(this.tempFilePath, this.tempPosOfStructure, this.tempStructure);
    }

    private void writeStructureInPath(String path, long posOfStructure, FileSystemStructure structure) throws Exception {
        RandomAccessFile file = new RandomAccessFile(path, "rw");
        file.seek(81L);
        file.write(FileUtils.longToBytes(posOfStructure));
        file.seek(posOfStructure);
        file.setLength(file.getFilePointer());
        OutputStream out = Channels.newOutputStream(file.getChannel());
        CipherOutputStream out1 = new CipherOutputStream(out, this.cipher.getCiphertoEnc(out));
        BufferedOutputStream out2 = new BufferedOutputStream(out1);
        out2.write(this.converter.toJson(structure).getBytes());
        out2.close();
        file.close();
    }

    private int nextId() {
        this.structure.setIdSequence(this.structure.getIdSequence() + 1);
        return this.structure.getIdSequence();
    }

    private String nextIdTemp(String extension) {
        this.tempStructure.setIdSequence(this.tempStructure.getIdSequence() + 1);
        return this.tempStructure.getIdSequence() + extension;
    }

    public FileSystemStructure getStructure() {
        return this.structure;
    }

    public FileSystemStructure getTempStructure() {
        return this.tempStructure;
    }

    public void copyActualContentToTempFile() throws Exception {
        FileInputStream actual = new FileInputStream(new File(this.filePath));
        FileOutputStream temp = new FileOutputStream(new File(this.tempFilePath));

        try {
            FileUtils.copy(actual, temp);
        } finally {
            temp.flush();

            try {
                temp.getFD().sync();
            } catch (IOException var8) {
                ;
            }

            temp.close();
            actual.close();
        }

    }

    public void copyActualStructureToTempStructure() {
        this.tempPosOfStructure = this.posOfStructure;
        this.tempStructure = new FileSystemStructure();
        this.tempStructure.setIdSequence(this.structure.getIdSequence());
        this.tempStructure.setFiles(new LinkedHashMap());
        Iterator iterator = this.structure.getFiles().values().iterator();

        while(iterator.hasNext()) {
            LockFile f = (LockFile)iterator.next();
            LockFile nuevo = new LockFile();
            nuevo.setFullPath(f.getFullPath());
            nuevo.setId(f.getId());
            nuevo.setPreviewId(f.getPreviewId());
            nuevo.setSize(f.getSize());
            nuevo.setStart(f.getStart());
            nuevo.setType(f.getType());
            this.tempStructure.getFiles().put(nuevo.getId(), nuevo);
        }

    }

    public void convertTempStructureInOriginal() {
        this.structure = this.tempStructure;
        this.posOfStructure = this.tempPosOfStructure;
    }

    public void convertTempFileInOriginalFile() {
        File original = new File(this.filePath);
        original.delete();
        File nuevo = new File(this.tempFilePath);
        nuevo.renameTo(new File(this.filePath));
    }

    public void initTempFilePath() {
        File file = new File(this.filePath);
        this.tempFilePath = FileUtils.getTempPathFileForFile(file);
    }

    public void deleteTempFile() throws IOException {
        if(!TextUtils.isEmpty(this.tempFilePath)) {
            File file = new File(this.tempFilePath);
            if(file.exists()) {
                FileUtils.delete(file);
            }
        }

    }
}
