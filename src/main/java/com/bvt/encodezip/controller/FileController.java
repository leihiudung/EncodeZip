package com.bvt.encodezip.controller;


import com.bvt.encodezip.service.FileService;
import com.bvt.encodezip.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
}
