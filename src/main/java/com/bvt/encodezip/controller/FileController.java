package com.bvt.encodezip.controller;


import com.bvt.encodezip.service.FileService;
import com.bvt.encodezip.util.HashUtils;
import com.bvt.encodezip.util.Office2HtmlUtils;
import com.bvt.encodezip.vo.FileVO;
import com.bvt.encodezip.vo.Result;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.jodconverter.office.OfficeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;



@RestController
@RequestMapping("/employee")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/save_file")
    public Result saveFile(@RequestBody String fileName) {
        fileService.receiveFile(fileName);
        return Result.ok("work");
    }

    @PostMapping("/file_name_suffix_teleporter")
    public Result saveFile(@RequestBody String fileName, @RequestBody String fileSuffix, String teleporter) {
//        fileService.receiveFileComplete(fileName, fileSuffix, teleporter);
        return Result.ok("work");
    }

    @GetMapping("/all_file")
    public Result findFile() {
        List<FileVO> fileAll = fileService.getAllFile();

        return Result.ok("找到了", fileAll);
    }

    @GetMapping("/file_name")
    public Result fileName(@RequestParam(name="fileName") String fileName) {
        fileService.findFileByFileName(fileName);
        return Result.ok("work");
    }

    @Value("${file.upload.url}")
    private String uploadFilePath;

    @RequestMapping("/upload")
    public Result httpUpload(@RequestParam("files") MultipartFile[] files) {
        Logger logger = Logger.getGlobal();
        logger.info("log info UPLOAD");
        for(int i=0;i<files.length;i++){
            String fileFullName = files[i].getOriginalFilename();  // 文件名
            String fileName = fileFullName.substring(0, fileFullName.lastIndexOf("."));
            File dest = new File(uploadFilePath +'/'+ fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }

            try {
                files[i].transferTo(dest);
            } catch (Exception e) {
                return Result.error("解析出错");
            }

            String suffixName = fileFullName.substring(fileFullName.lastIndexOf(".") + 1);  //  获取后缀名


            long currentTime=System.currentTimeMillis();
            String destinationFileName = String.format("%d", currentTime);
            Boolean encodeFlag = fileService.encodeFile(new File(uploadFilePath), dest, destinationFileName);
            if (!encodeFlag) {
                return Result.error("加密/重命名出错");
            }
            dest.delete();

            //  存储传送文件记录到数据库
            boolean saveFlag = fileService.receiveFileComplete(fileName, suffixName, uploadFilePath, destinationFileName, "tom");
            if (!saveFlag) {
                return Result.error("数据库记录出错");
            }
        }

        return Result.ok("上传成功");
    }

    @PostMapping("/decode_file")
    public Result decodeFile(@RequestParam("file_name") String fileName) {
        File destinationFile = new File(uploadFilePath +'/'+ fileName);
        File copyFile = new File(uploadFilePath+"/TEMP");
        try {
            if (!copyFile.exists()) {
                copyFile.createNewFile();
            }
            FileChannel inputChannel;
            FileChannel outputChannel;

            inputChannel = new FileInputStream(destinationFile).getChannel();
            outputChannel = new FileOutputStream(copyFile).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            inputChannel.close();
            outputChannel.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        fileService.decodeFile(copyFile, uploadFilePath+"/decode");
        // dev分支
        return Result.ok("解密成功");
    }
    private final String prefix = "/filedownload";
//    @GetMapping("{filename}")
//    public ResponseEntity<?> download(@PathVariable("filename") String filename) {
//        // 在这之前进行一些必要的处理，比如鉴权，或者其它的处理逻辑。
//        // 通过X-Accel-Redirect返回在nginx中的实际下载地址
//        return ResponseEntity.ok().header("X-Accel-Redirect", prefix + "/" + filename).build();
//    }

    @GetMapping("{filealiasname}")
    public void download(@PathVariable("filealiasname") String filealiasname, HttpServletResponse response) throws IOException {

        FileVO fileVO = fileService.findFileByFileAliasName(filealiasname);
        //  获取文件输入流
        InputStream inputStream;
        Logger log = Logger.getLogger("tesglog");
        String officeType = "docx,doc,xls,xlsx,ppt,pptx";
        if (officeType.contains(fileVO.getFileSuffix())) {
            String path = fileService.fineFilePath(fileVO.getFileAliasName());
            File officeFile = new File(path + File.separator + fileVO.getFileAliasName() + ".abc");

            String decodePath = onServeDecode(officeFile);

//            Office2HtmlUtils utils = new Office2HtmlUtils();
//            Office2HtmlUtils.office2Pdf(decodePath, decodePath);
            Office2HtmlUtils.office2PDF(decodePath, decodePath + ".pdf");

            File needDeleteFile = new File(decodePath);
            needDeleteFile.delete();    //   删除解压后的文件,为了后面逻辑做准备

            File[] files = new File[1];
            files[0] = new File(decodePath + ".pdf");

            String[] pathArr = repackageToEncodePackage(files);

            inputStream = new FileInputStream(pathArr.length > 0 ? pathArr[0] : null);

//            boolean flag = Office2HtmlUtils.office2Pdf(decodePath + File.separator + fileVO.getFileAliasName() + "." + fileVO.getFileSuffix(), fileVO.getFileAliasName() + "." + fileVO.getFileSuffix());
//            inputStream = new FileInputStream(pathArr[0]);
        } else {
            inputStream = new FileInputStream(uploadFilePath + File.separator + fileVO.getFileAliasName() + ".abc");
        }
        // 设置响应头，指定文件名
        response.setHeader("Content-Disposition", "attachment;");
        response.setCharacterEncoding("UTF-8");

        response.addHeader("filename", URLEncoder.encode(fileVO.fileName, "UTF-8"));
        response.addHeader("filealiasname", fileVO.getFileAliasName());
        response.addHeader("filetype",  fileVO.fileSuffix);
        // 获取文件输入流


        // 创建StreamingResponseBody对象，将文件内容写入响应输出流
        StreamingResponseBody responseBody = outputStream -> {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
        };

        // 返回StreamingResponseBody对象
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        responseBody.writeTo(response.getOutputStream());
    }

    private String onServeDecode(File file) throws IOException {
        String destDirPath = file.getParent() + File.separator + "office";
        File copyDestDir = new File(destDirPath + File.separator + file.getName());
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(file);
            output = new FileOutputStream(copyDestDir);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
        File[] decodeFile = fileService.decodeFile(copyDestDir, destDirPath);
        if (decodeFile != null && decodeFile.length > 0) {
         return destDirPath + File.separator + decodeFile[0].getName();
        }
        return null;
    }

    private String[] repackageToEncodePackage(File[] files) {
        String[] pathStr = new String[1];
        for(int i=0;i<files.length;i++){

            int nameIndex = files[i].getName().lastIndexOf('.');
            File withoutSuffixFile = new File(files[i].getParent() + File.separator + files[i].getName().substring(0, nameIndex));
            String fileFullName = files[i].getName();  // 文件名
            files[i].renameTo(withoutSuffixFile);
            long currentTime=System.currentTimeMillis();
            String destinationFileName = String.format("%d", currentTime);
            Boolean encodeFlag = fileService.encodeFile(new File(files[i].getParentFile().getPath()), withoutSuffixFile, destinationFileName);
            if (!encodeFlag) {
                return null;
            }
//            dest.delete();
            pathStr[i] = files[i].getParentFile().getPath() + File.separator + destinationFileName + ".abc";
        }
        return pathStr;
    }



}
