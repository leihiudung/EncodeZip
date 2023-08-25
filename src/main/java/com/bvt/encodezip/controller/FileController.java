package com.bvt.encodezip.controller;


import com.bvt.encodezip.service.FileService;
import com.bvt.encodezip.vo.Result;
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
import java.nio.channels.FileChannel;

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
        fileService.receiveFileComplete(fileName, fileSuffix, teleporter);
        return Result.ok("work");
    }

    @GetMapping("/all_file")
    public Result findFile() {

        return Result.ok("找到了");
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

        for(int i=0;i<files.length;i++){
            String fileName = files[i].getOriginalFilename();  // 文件名
            File dest = new File(uploadFilePath +'/'+ fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                files[i].transferTo(dest);
            } catch (Exception e) {
                return Result.error("解析出错");
            }
            Boolean encodeFlag = fileService.encodeFile(new File(uploadFilePath), dest);
            if (!encodeFlag) {
                return Result.error("加密出错");
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

    @GetMapping("{filename}")
    public void download(@PathVariable("filename") String filename, HttpServletResponse response) throws IOException {
        // 设置响应头，指定文件名
        response.setHeader("Content-Disposition", "attachment; filename="+filename);

        // 获取文件输入流
        InputStream inputStream = new FileInputStream("D:\\Code\\FILE\\" + filename);

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
}
