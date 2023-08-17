package com.bvt.encodezip.service;

import com.bvt.encodezip.entity.File;

public interface FileService {

    void receiveFile(String fileName);

    void receiveFileComplete(String fileName, String fileSuffix, String teleporter);

    File findFileByFileName(String fileName);

    Boolean encodeFile(java.io.File destinationFile, java.io.File file);

    Boolean decodeFile(java.io.File destionaryFile, String key);
}
