package com.bvt.encodezip.util;


import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.office.OfficeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletOutputStream;
import java.io.*;

//import com.artofsolving.jodconverter.DocumentConverter;



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
    private static DocumentConverter converter;
    public static boolean office2Pdf(String officeFilePath, String fileName) {
//        File fileDir = new File(decodeFilePath);//转换之后文件生成的地址
//        if (!fileDir.exists()) {
//            fileDir.mkdirs();
//        }

        File officeFile = new File(officeFilePath);
        try {
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
}
