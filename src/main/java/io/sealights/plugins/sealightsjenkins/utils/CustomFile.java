package io.sealights.plugins.sealightsjenkins.utils;

import io.sealights.plugins.sealightsjenkins.CleanupManager;

import java.io.IOException;

/**
 * Created by shahar on 5/25/2016.
 */
public class CustomFile {

    private Logger logger;
    private String name;
    private CleanupManager cleanupManager;
    private boolean isFolder;

    public CustomFile(Logger logger, CleanupManager cleanupManager, String name) {
        this(logger, cleanupManager, name, false);
    }

    public CustomFile(Logger logger, CleanupManager cleanupManager, String name, boolean isFolder) {
        this.logger = logger;
        this.cleanupManager = cleanupManager;
        this.name = name;
        this.isFolder = isFolder;
    }

    public void copyToSlave() throws IOException, InterruptedException {
        copyToSlave(name, false, false);
    }

    public void copyToSlave(boolean deleteSourceFile, boolean deleteTargetFile) throws IOException, InterruptedException {
        copyToSlave(name, deleteSourceFile, deleteTargetFile);
    }

    public void copyToSlave(String targetFile) throws IOException, InterruptedException {
        copyToSlave(targetFile, false, true);
    }

    public void copyToSlave(String targetFile, boolean deleteSourceFile, boolean deleteTargetFile) throws IOException, InterruptedException {
        boolean copySuccess = FileUtils.tryCopyFileFromLocalToSlave(logger, name, targetFile);

        if (deleteSourceFile)
            cleanupManager.addFile(name);

        if (deleteTargetFile && !targetFile.equals(name) &&copySuccess)
            cleanupManager.addFile(targetFile);
    }

    public void copyToMaster(String targetFile) throws IOException, InterruptedException {
        if (this.isFolder) {
            FileUtils.tryCopyFolderFromSlaveToLocal(logger, targetFile, name);
        }else{
            FileUtils.tryCopyFileFromSlaveToLocal(logger, targetFile, name);
        }
        cleanupManager.addFile(targetFile);
    }
}