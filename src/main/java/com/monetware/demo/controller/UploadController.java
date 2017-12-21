//package com.monetware.demo.controller;
//
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.*;
//
//import com.monetware.demo.variable.root;
//
///**
// * Created by Stephen on 2017/12/4.
// */
//@RestController
//@RequestMapping("/upload")
//public class UploadController {
//
//    @PostMapping("/tab")
//    public void UploadTAB(@RequestParam(value = "name", defaultValue = "test")String name, HttpServletRequest req, MultipartHttpServletRequest multiReq) throws IOException{
//        FileOutputStream fos = new FileOutputStream(new File(root.FileRoot + "tab/" + name + ".tab"));
//        FileInputStream fs = (FileInputStream) multiReq.getFile("file").getInputStream();
//        byte[] buffer = new byte[1024];
//        int len = 0;
//        while((len = fs.read(buffer)) != -1){
//            fos.write(buffer, 0, len);
//        }
//        fos.close();
//        fs.close();
//    }
//}
