package com.monetware.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.monetware.demo.variable.root;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by Stephen on 2017/12/4.
 */
@RestController
@RequestMapping("/download")
public class DownLoadController {

    @GetMapping("/tab")
    public void DownloadTAB(@RequestParam(value = "name", defaultValue = "test") String name, HttpServletResponse resp){
        File file = new File(root.FileRoot + "tab/" + name + ".tab");
        resp.setHeader("Content-Disposition", "attachment;filename=" + name + ".tab");
        Read(file, resp);
    }

    @GetMapping("/prep")
    public void DownloadPREP(@RequestParam(value = "name", defaultValue = "test") String name, HttpServletResponse resp){
        File file = new File(root.FileRoot + "prep/" + name + ".prep");
        resp.setHeader("Content-Disposition", "attachment;filename=" + name + ".prep");
        Read(file, resp);
    }

    @GetMapping("/xml")
    public void DownloadXML(@RequestParam(value = "name", defaultValue = "test") String name, HttpServletResponse resp){
        File file = new File(root.FileRoot + "xml/" + name + "-ddi.xml");
        resp.setHeader("Content-Disposition", "attachment;filename=" + name + "-ddi.xml");

        Read(file, resp);
    }

    public void Read(File file, HttpServletResponse resp){
        resp.setHeader("content-type", "application/octet-stream");
        resp.setContentType("application/octet-stream");
        OutputStream os = null;
        try {
            os = resp.getOutputStream();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = "";
            while((s = br.readLine()) != null){
                s = s + "\n";
                byte[] bytes = s.getBytes();
                os.write(bytes);
            }
        } catch (IOException e) {
            System.out.println("No such file!");
        }
    }
}
