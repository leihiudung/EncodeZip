package com.bvt.encodezip.service;

import com.bvt.encodezip.entity.File;
import com.bvt.encodezip.vo.FileVO;

import java.util.List;

public interface FileService {

    void receiveFile(String fileName);

    boolean receiveFileComplete(String fileName, String fileSuffix, String filePath, String teleporter);

    List<FileVO> getAllFile();

    File findFileByFileName(String fileName);

    Boolean encodeFile(java.io.File destinationFile, java.io.File file);

    Boolean decodeFile(java.io.File destionaryFile, String key);
}
