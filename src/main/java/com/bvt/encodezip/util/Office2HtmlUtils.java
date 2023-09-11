package com.bvt.encodezip.util;


import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
//import org.objectweb.asm.commons.*;
//import com.artofsolving.jodconverter.DocumentConverter;

/**
 * @projectName: EncodeZip
 * @package: com.bvt.encodezip.util
 * @className: Office2HtmlUtils
 * @author: Tom
 * @description: TODO
 */
public class Office2HtmlUtils {
//    @Autowired
//    private OfficeDocumentConverter  converter;
    public static String office2Pdf(String officeFilePath) throws FileNotFoundException {
//        File fileDir = new File("D:/Code/FILE/office");//转换之后文件生成的地址
//        if (!fileDir.exists()) {
//            fileDir.mkdirs();
//        }
//        //文件转化
//        converter.convert(file).to(new File("D:/obj-pdf/hello.pdf")).execute();
//        //使用response,将pdf文件以流的方式发送的前段
//        ServletOutputStream outputStream = response.getOutputStream();
//        InputStream in = new FileInputStream(new File("D:/obj-pdf/hello.pdf"));// 读取文件
//        // copy文件
//        int i = IOUtils.copy(in, outputStream);
        return "";
    }
}
