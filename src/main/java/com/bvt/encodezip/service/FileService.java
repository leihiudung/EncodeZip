package com.bvt.encodezip.service;

import com.bvt.encodezip.entity.File;
import com.bvt.encodezip.vo.FileVO;

import java.util.List;

public interface FileService {

    String fineFilePath(String fileAliasName);
    void receiveFile(String fileName);

    boolean receiveFileComplete(String fileName, String fileSuffix, String filePath, String aliasName, String teleporter);

    List<FileVO> getAllFile();

    File findFileByFileName(String fileName);
    FileVO findFileByFileAliasName(String fileName);

    Boolean encodeFile(java.io.File destinationFile, java.io.File file, String aliasName);

    java.io.File[] decodeFile(java.io.File destionaryFile, String key);

    String unzipFile(String filePath);
}
