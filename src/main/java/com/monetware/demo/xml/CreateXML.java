package com.monetware.demo.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import com.monetware.demo.variable.root;

public class CreateXML {

    public CreateXML(){}

    /**
     * main part
     * the test .tab file is test.tab located under the 'src' folder
     * and you will create a test.xml under the same folder
     * @throws Exception
     */
    public String CreateFile(String fileName) throws Exception{
        File file = new File(root.FileRoot + "tab/" + fileName + ".tab");
        WriteXML c = new WriteXML();

        //write xml context into os
        OutputStream os = c.getOutputStream(file, "1", "2", "3", "4", "5","text/tab-separated-values");

        //define output file
        File fileout = new File(root.FileRoot + "xml/" + fileName + "-ddi.xml");
        if (fileout.exists())
            return "xml file already exist!";
        FileOutputStream fos = new FileOutputStream(fileout);

        //convert os into byte array and store them into test.xml
        String temp = os.toString();
        byte[] flush = temp.getBytes();
        fos.write(flush);
        fos.close();
        os.close();
        return "create file successfully!";
    }
}