package com.bvt.encodezip.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

/**
 * @projectName: EncodeZip
 * @package: com.bvt.encodezip.controller
 * @className: UserController
 * @author: Tom
 * @description: TODO
 */
@RestController
public class UserController {
    private final String prefix = "/filedownload";
    @GetMapping("{filename}")
    public ResponseEntity<?> download(@PathVariable("filename") String filename) {
        // 在这之前进行一些必要的处理，比如鉴权，或者其它的处理逻辑。
        // 通过X-Accel-Redirect返回在nginx中的实际下载地址
    try {
        return ResponseEntity.ok().header("X-Accel-Redirect", prefix + "/" + filename).build();

    } catch (RuntimeException e) {
        System.out.println(e.getMessage());
    }
        return null;
    }
}
