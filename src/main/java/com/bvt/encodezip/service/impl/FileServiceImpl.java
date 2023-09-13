package com.bvt.encodezip.service.impl;

import com.bvt.encodezip.entity.File;
import com.bvt.encodezip.mapper.FileMapper;
import com.bvt.encodezip.service.FileService;
import com.bvt.encodezip.util.ZipInputStreamTool;
import com.bvt.encodezip.util.ZipOutputStreamTool;

import com.bvt.encodezip.vo.FileVO;
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
    public String fineFilePath(String fileAliasName) {
        String filePaht = fileMapper.fineFilePath(fileAliasName);
        return filePaht;
    }

    @Override
    public void receiveFile(String fileName) {

    }

    @Override
    public boolean receiveFileComplete(String fileName, String fileSuffix, String filePath, String aliasName, String teleporter) {
        int savedCount = fileMapper.receiveFileComplete(fileName, fileSuffix, filePath, aliasName, teleporter);
        if (savedCount > 0) {
            return true;
        }
        return false;
    }

    @Override
    public File findFileByFileName(String fileName) {
        File file = fileMapper.findFileByFileName(fileName);
        return file;
    }

    @Override
    public FileVO findFileByFileAliasName(String fileName) {
        FileVO file = fileMapper.findFileByFileAliasName(fileName);
        return file;
    }

    @Override
    public List<FileVO> getAllFile() {
        List<FileVO> fileAll = fileMapper.getAllFile();
        return fileAll;
    }

    @Override
    public Boolean encodeFile(java.io.File destinationDir, java.io.File file, String aliasName) {

        List<java.io.File> needZipFile = new ArrayList<>();
        String destinationFileName = String.format("%s.zip", aliasName);
        java.io.File targetFile = combinePath(destinationDir, destinationFileName);

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
            String encodeFileName = String.format("%s.abc", aliasName);
            java.io.File newFile = combinePath(destinationDir, encodeFileName);
            return targetFile.renameTo(newFile);
        }
        return false;
    }

    /**
     * 解密文件
     *
     * @param destionaryFile 目标文件路径
     * @param key            加密密钥
     * @return
     */
    @Override
    public java.io.File[] decodeFile(java.io.File destionaryFile, String key) {

        java.io.File[] decodeFile;
        try {
            decodeFile = ZipInputStreamTool.deCompress(destionaryFile, key, new String("abcgo"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return decodeFile;
    }

    public static java.io.File combinePath(java.io.File path1, String path2)

    {

        java.io.File tagetFile = new java.io.File(path1, path2);

        return tagetFile;

    }

    @Override
    public String unzipFile(String filePath) {
        return null;
    }
}
