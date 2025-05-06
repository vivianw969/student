package com.example.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.server.HttpServerResponse;
import com.example.common.Result;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

@RestController

@RequestMapping("/files")
public class FileController {

    //以下两个变量是从配置（application.yml）中获取的 用于拼接url
    @Value("${ip}")
    private String ip;

    @Value("${server.port}")
    private String port;

    private static final String ROOT_PATH = System.getProperty("user.dir") + "/files"; //根路径

    @PostMapping("/upload")
    //upload file
    public Result upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename(); //getOriginalFilename
        long flag = System.currentTimeMillis(); //用时间戳作为唯一标识
        String fileName = flag + "_" + originalFilename;
        File targetFile = new File(ROOT_PATH + "/" + fileName);//最终存到磁盘中的文件对象
        if (!targetFile.getParentFile().exists()) { //如果父级目录不存在，就需要创建
            targetFile.getParentFile().mkdirs();
        }

        file.transferTo(targetFile);
        //返回文件的URL (给前端用)
        String url = "http://" + ip + ":" + port + "/files/download?fileName=" + fileName;
        return Result.success(url);
    }


    //download file
    @GetMapping("/download")
    public void download(String fileName, HttpServletResponse response) throws IOException {
        File file = new File(ROOT_PATH + "/" + fileName); //磁盘中存储的文件对象
        ServletOutputStream os = response.getOutputStream(); //写出路径  ServletOutputStream 是一个用于发送二进制数据到客户端的输出流。
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("application/octet-stream");
        FileUtil.writeToStream(file, os); //也可以写成        os.write(FileUtil.readBytes(file));
        os.flush();
        os.close();
    }
}
