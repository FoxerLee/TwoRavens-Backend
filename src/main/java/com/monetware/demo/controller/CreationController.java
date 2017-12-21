package com.monetware.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.monetware.demo.xml.*;
import java.io.*;

import com.monetware.demo.prep.RemoteDataFrameService;
import com.monetware.demo.variable.root;

@RestController
@RequestMapping("/create")
public class CreationController {

    @GetMapping("/xml")
    public String GenerateXML(@RequestParam(value = "name", defaultValue = "test") String name){
        String response = "success";
        try{
            CreateXML xml = new CreateXML();
            response = xml.CreateFile(name);

        }catch (Exception e){
            response = "No such a file!";
        }
        return response;
    }

    @GetMapping("/prep")
    public String GeneratePREP(@RequestParam(value = "name", defaultValue = "test")String name){
        String response = "success";
        RemoteDataFrameService prep = new RemoteDataFrameService();
        try {

            File prepFile = prep.runDataPreprocessing(root.FileRoot + "tab/" + name + ".tab", name);
            String header = "{\"dataset\": { \"private\": false }, \"variables\":{";

            prep.fileAppender(root.FileRoot + "prep/" + name + ".prep", header);
        }catch (Exception e) {
            response = "Unexpected error";
        }

        return response;
    }
}
