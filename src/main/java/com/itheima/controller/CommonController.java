package com.itheima.controller;

import com.itheima.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author 蒋樟
 * @date 2023/9/11 9:02
*/
@Slf4j
@RestController
@RequestMapping(("/common"))
public class CommonController {
    //图片保存位置
    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //获得原始文件名
       String originalFileName=file.getOriginalFilename();
       String suffix=originalFileName.substring(originalFileName.lastIndexOf("."));
       String fileName= UUID.randomUUID().toString()+suffix;
       //创建一个目录对象
       File dir=new File(basePath);
       if(!dir.exists()){
           //创建目录
            dir.mkdirs();
       }
        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }
    //文件下载
    @GetMapping("/download")
    public void fileDownload( String name,HttpServletResponse response) {
        //把刚刚存的文件读取到内存中，准备回显
        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //输出流，通过输出流文件写回浏览器，在浏览器展示图片
            ServletOutputStream servletOutputStream = response.getOutputStream();
            response.setContentType("image/jpeg");//图片文件
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                //写入响应流，从0开始，写入到数组末尾长度
                servletOutputStream.write(bytes, 0, len);
                //把流里的东西挤出来
                servletOutputStream.flush();
            }
            fileInputStream.close();
            servletOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
