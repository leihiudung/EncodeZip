package com.bvt.encodezip.util;

import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.core.office.OfficeUtils;
import org.jodconverter.local.JodConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.logging.Logger;


/**
 * @projectName: EncodeZip
 * @package: com.bvt.encodezip.util
 * @className: Office2HtmlUtils
 * @author: Tom
 * @description: TODO
 */

public class Office2HtmlUtils {

    @Value("${file.upload.url.office}")
    private static String decodeFilePath;

    @Autowired
    private DocumentConverter converter;

    public boolean office2Pdf(String officeFilePath, String fileName) {
//        File fileDir = new File(decodeFilePath);//转换之后文件生成的地址
//        if (!fileDir.exists()) {
//            fileDir.mkdirs();
//        }

        File officeFile = new File(officeFilePath);
        try {
//            OpenOfficeConnection connection = new SocketOpenOfficeConnection("127.0.0.1", 8100);
//            connection.connect();
//            // 转换
//            DocumentConverter converter = new StreamOpenOfficeDocumentConverter (connection);

            //文件转化
            converter.convert(officeFile).to(new File(decodeFilePath + File.pathSeparator + fileName + ".pdf")).execute();

        } catch (OfficeException e) {
            return false;
        }
                //使用response,将pdf文件以流的方式发送的前段
//        ServletOutputStream outputStream = response.getOutputStream();
//        InputStream in = new FileInputStream(new File("D:/obj-pdf/hello.pdf"));// 读取文件
//        // copy文件
//        int i = IOUtils.copy(in, outputStream);
        return true;
    }

    public static boolean office2PDF(String sourceFile, String destFile) {
        Logger log = Logger.getLogger("tesglog");

        OfficeManager officeManager
                = LocalOfficeManager.builder().install()
                .officeHome("C:\\Program Files (x86)\\OpenOffice 4")
                .build();
        try {
            File inputFile = new File(sourceFile);
            if (!inputFile.exists()) {
                log.info("找不到源文件");
                return false;// 找不到源文件, 则返回-1
            }
            // 如果目标路径不存在, 则新建该路径
            File outputFile = new File(destFile);
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }
            officeManager.start(); // Start the office process
            JodConverter.convert(new File(sourceFile)).to(outputFile).execute();
        } catch (Exception e) {
            log.info(e.getMessage());
        } finally {
            OfficeUtils.stopQuietly(officeManager); // Stop the office process
        }
        return true;
    }
}
