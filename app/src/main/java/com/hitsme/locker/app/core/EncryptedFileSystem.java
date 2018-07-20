package com.hitsme.locker.app.core;

import com.hitsme.locker.app.core.clases.FileSystemStructure;
import com.hitsme.locker.app.core.clases.FileType;
import com.hitsme.locker.app.core.clases.LockFile;
import com.hitsme.locker.app.core.data.EncryptedFileSystemDAO;
import com.hitsme.locker.app.core.datatype.AddFileListener;
import com.hitsme.locker.app.core.datatype.INewFile;
import com.hitsme.locker.app.core.exceptions.LockException;
import com.hitsme.locker.app.core.utils.FileUtils;
import com.hitsme.locker.app.core.utils.PendienteUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EncryptedFileSystem {
    private final ReadWriteLock lockForOrignial = new ReentrantReadWriteLock();
    private final Object lockForTemp = new Object();
    private final EncryptedFileSystemDAO dao;
    private List<String> pendientesDeAgregar = Collections.synchronizedList(new ArrayList());
    private String password;

    private EncryptedFileSystem(EncryptedFileSystemDAO dao, String password) {
        this.dao = dao;
        this.password = password;
    }

    static EncryptedFileSystem createEncryptedFile(String filePath, String password) {
        File file = new File(filePath);
        if(file.exists()) {
            throw new LockException("err_the_file_already_exists");
        } else {
            file.getParentFile().mkdirs();
            EncryptedFileSystemDAO dao = new EncryptedFileSystemDAO(filePath);
            dao.writeVersion();
            dao.createKey(password);
            dao.initStructure();
            EncryptedFileSystem system = new EncryptedFileSystem(dao, password);
            return system;
        }
    }

    static EncryptedFileSystem openEncryptedFile(String filePath, String password) {
        File file = new File(filePath);
        if(!file.exists()) {
            throw new LockException("err_not_found");
        } else {
            EncryptedFileSystemDAO dao = new EncryptedFileSystemDAO(filePath);
            dao.checkVersion();
            dao.loadKey(password);
            dao.loadStructure();
            EncryptedFileSystem system = new EncryptedFileSystem(dao, password);
            return system;
        }
    }

    public void addFilesWithPreview(AddFileListener listener, List<INewFile> files) {
        PendienteUtils.addNamesDataFile(this.pendientesDeAgregar, files);
        Object var3 = this.lockForTemp;
        synchronized(this.lockForTemp) {
            try {
                this.beginTransactionToWriteInTemp();
                Iterator var4 = files.iterator();

                while(var4.hasNext()) {
                    INewFile e = (INewFile)var4.next();
                    LockFile lockFile = this.dao.addEncryptedFileFromFileToTemp(e.getIn(), e.getName());
                    InputStream preview = e.getPreview();
                    if(preview != null) {
                        this.dao.addPreviewToTemp(lockFile, preview);
                    }
                }

                this.commitTransactionInTemp();
            } catch (LockException var8) {
                this.abortTransactionInTemp();
                throw var8;
            } catch (Exception var9) {
                this.abortTransactionInTemp();
                throw new LockException("err_failed_to_add_the_file");
            }
        }

        PendienteUtils.removeNamesDataFile(this.pendientesDeAgregar, files);
    }

    public void addFile(AddFileListener listener, List<File> files) {
        PendienteUtils.addNamesFile(this.pendientesDeAgregar, files);
        Object var3 = this.lockForTemp;
        synchronized(this.lockForTemp) {
            try {
                this.beginTransactionToWriteInTemp();
                Iterator var4 = files.iterator();

                while(var4.hasNext()) {
                    File e = (File)var4.next();
                    if(e.isDirectory()) {
                        this.generateFileListForFolder(e, e, listener);
                    } else {
                        FileInputStream input = new FileInputStream(e);
                        this.dao.addEncryptedFileFromFileToTemp(input, e.getName());
                        listener.fileAdded();
                    }
                }

                this.commitTransactionInTemp();
            } catch (LockException var8) {
                this.abortTransactionInTemp();
                throw var8;
            } catch (Exception var9) {
                this.abortTransactionInTemp();
                throw new LockException("err_failed_to_add_the_file");
            }
        }

        PendienteUtils.removeNamesFile(this.pendientesDeAgregar, files);
    }

    private void generateFileListForFolder(File baseFolder, File node, AddFileListener listener) throws FileNotFoundException {
        if(node.isFile()) {
            FileInputStream subNote = new FileInputStream(node);
            this.dao.addEncryptedFileFromFileToTemp(subNote, FileUtils.getNameRelativeToBase(baseFolder, node));
            listener.fileAdded();
        }

        if(node.isDirectory()) {
            this.dao.addFolderToTemp(FileUtils.getNameRelativeToBase(baseFolder, node));
            listener.fileAdded();
            String[] var9 = node.list();
            String[] var5 = var9;
            int var6 = var9.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String filename = var5[var7];
                this.generateFileListForFolder(baseFolder, new File(node, filename), listener);
            }
        }

    }

    public void deleteFiles(String... ids) throws Exception {
        Object var2 = this.lockForTemp;
        synchronized(this.lockForTemp) {
            try {
                this.beginTransactionToWriteInTempSinCopiar();
                LinkedHashSet e = new LinkedHashSet();
                LinkedHashSet files = new LinkedHashSet();
                FileSystemStructure structure = this.dao.getTempStructure();
                String[] var6 = ids;
                int var7 = ids.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String fileId = var6[var8];
                    if(structure.getFiles().containsKey(fileId)) {
                        LockFile f = (LockFile)structure.getFiles().get(fileId);
                        if(f.getType() == FileType.FOLDER) {
                            e.add(fileId);
                        } else {
                            files.add(fileId);
                            if(f.getPreviewId() != null) {
                                files.add(f.getPreviewId());
                            }
                        }
                    }
                }

                this.dao.delteFoldersInTempStructure(e);
                this.dao.delteFilesInTemp(files);
                this.commitTransactionInTemp();
            } catch (LockException var11) {
                this.abortTransactionInTemp();
                throw var11;
            } catch (Exception var12) {
                this.abortTransactionInTemp();
                throw new LockException("err_could_not_delete_the_file");
            }

        }
    }

    public void extractFile(String id, OutputStream out) {
        this.lockForOrignial.readLock().lock();

        try {
            this.dao.extractFile(id, out);
        } finally {
            this.lockForOrignial.readLock().unlock();
        }

    }

    public void extractAllFilesAndFolders(File baseFolder) throws FileNotFoundException {
        this.lockForOrignial.readLock().lock();

        try {
            ArrayList files = new ArrayList(this.dao.getStructure().getFiles().values());
            Iterator var3 = files.iterator();

            while(var3.hasNext()) {
                LockFile lf = (LockFile)var3.next();
                if(lf.getType() != FileType.PREVIEW) {
                    File file = new File(baseFolder.getAbsoluteFile() + File.separator + lf.getFullPath());
                    if(lf.getType() == FileType.FOLDER) {
                        file.mkdirs();
                    } else {
                        if(file.exists()) {
                            file = new File(FileUtils.createNewFileNameInPath(file.getParent() + File.separator, FileUtils.removeExtensionFile(file.getName()), FileUtils.getExtensionFile(file.getName())));
                        }

                        file.getParentFile().mkdirs();
                        FileOutputStream out = new FileOutputStream(file);
                        this.dao.extractFile(lf.getId(), out);
                    }
                }
            }
        } finally {
            this.lockForOrignial.readLock().unlock();
        }

    }

    public List<LockFile> getFilesAndFolders() {
        this.lockForOrignial.readLock().lock();

        try {
            ArrayList files = new ArrayList(this.dao.getStructure().getFiles().values());
            Iterator iter = files.iterator();

            while(iter.hasNext()) {
                LockFile lf = (LockFile)iter.next();
                if(lf.getType() == FileType.PREVIEW) {
                    iter.remove();
                }
            }

            ArrayList var4 = files;
            return var4;
        } finally {
            this.lockForOrignial.readLock().unlock();
        }
    }

    public LockFile getFile(String id) {
        this.lockForOrignial.readLock().lock();

        LockFile var2;
        try {
            var2 = (LockFile)this.dao.getStructure().getFiles().get(id);
        } finally {
            this.lockForOrignial.readLock().unlock();
        }

        return var2;
    }

    public String getPreviewIdOfFile(String id) {
        this.lockForOrignial.readLock().lock();

        try {
            if(!this.dao.getStructure().getFiles().containsKey(id)) {
                return null;
            }

            LockFile lf = (LockFile)this.dao.getStructure().getFiles().get(id);
            if(this.dao.getStructure().getFiles().containsKey(lf.getPreviewId())) {
                String var3 = lf.getPreviewId();
                return var3;
            }
        } finally {
            this.lockForOrignial.readLock().unlock();
        }

        return null;
    }

    public boolean equalPassword(String pass) {
        return this.password.equals(pass);
    }

    private void beginTransactionToWriteInTemp() throws Exception {
        this.lockForOrignial.readLock().lock();

        try {
            this.dao.initTempFilePath();
            this.dao.copyActualContentToTempFile();
            this.dao.copyActualStructureToTempStructure();
        } finally {
            this.lockForOrignial.readLock().unlock();
        }

    }

    private void beginTransactionToWriteInTempSinCopiar() throws Exception {
        this.lockForOrignial.readLock().lock();

        try {
            this.dao.initTempFilePath();
            this.dao.copyActualStructureToTempStructure();
        } finally {
            this.lockForOrignial.readLock().unlock();
        }

    }

    public void commitTransactionInTemp() throws Exception {
        this.lockForOrignial.writeLock().lock();

        try {
            this.dao.writeStructureInTempFile();
            this.dao.convertTempFileInOriginalFile();
            this.dao.convertTempStructureInOriginal();
        } finally {
            this.lockForOrignial.writeLock().unlock();
        }

    }

    private void abortTransactionInTemp() {
        try {
            this.dao.deleteTempFile();
        } catch (Exception var2) {
            ;
        }

    }
}
