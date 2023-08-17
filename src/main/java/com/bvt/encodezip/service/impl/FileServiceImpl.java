package com.bvt.encodezip.service.impl;

import com.bvt.encodezip.entity.File;
import com.bvt.encodezip.mapper.FileMapper;
import com.bvt.encodezip.service.FileService;
import com.bvt.encodezip.util.ZipInputStreamTool;
import com.bvt.encodezip.util.ZipOutputStreamTool;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    FileMapper fileMapper;

    @Override
    public void receiveFile(String fileName) {

    }

    @Override
    public void receiveFileComplete(String fileName, String fileSuffix, String teleporter) {

    }

    @Override
    public File findFileByFileName(String fileName) {
        File file = fileMapper.findFileByFileName(fileName);
        return file;
    }

    @Override
    public Boolean encodeFile(java.io.File destinationDir, java.io.File file) {

        List<java.io.File> needZipFile = new ArrayList<>();
        long currentTime=System.currentTimeMillis();
        String destionaryFileName = String.format("%d.zip", currentTime);
        java.io.File targetFile = combinePath(destinationDir, destionaryFileName);

        if (file.isFile()) {
            needZipFile.add(file);
        }
        if (file.isDirectory()) {
        }

        String password = new String("abcgo");


        ZipOutputStreamTool outputStream = new ZipOutputStreamTool();
        try {
            outputStream.zipOutputStreamExample(targetFile, needZipFile, password.toCharArray(), CompressionMethod.DEFLATE, true, EncryptionMethod.ZIP_STANDARD, AesKeyStrength.KEY_STRENGTH_256);
        } catch (IOException e) {
//            throw new RuntimeException(e);
            return false;
        }

        if (targetFile.exists()) {
            String encodeFileName = String.format("%d.abc", currentTime);
            java.io.File newFile = combinePath(destinationDir, encodeFileName);
            targetFile.renameTo(newFile);
        }
        return true;
    }

    @Override
    public Boolean decodeFile(java.io.File destionaryFile, String key) {

        try {
            ZipInputStreamTool.deCompress(destionaryFile, key, new String("abcgo"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static java.io.File combinePath(java.io.File path1, String path2)

    {

        java.io.File tagetFile = new java.io.File(path1, path2);

        return tagetFile;

    }
}
