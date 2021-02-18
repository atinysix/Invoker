package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 2020/12/3 10:53
 */
public class FilePart {

    private String mName;
    private String mFileName;
    private String mFilePath;

    public FilePart(String name, String fileName, String filePath) {
        mName = name;
        mFileName = fileName;
        mFilePath = filePath;
    }

    public String getName() {
        return mName == null ? "" : mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getFileName() {
        return mFileName == null ? "" : mFileName;
    }

    public void setFileName(String fileName) {
        mFileName = fileName;
    }

    public String getFilePath() {
        return mFilePath == null ? "" : mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }
}
