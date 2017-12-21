package com.monetware.demo.xml;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.ArrayList;

/**
 * The main class creating xml files
 */
public class WriteXML {
    private static ArrayList<ArrayList<String>> data = new ArrayList<>();
    private static int height = 0;
    private static int length = 0;

    public WriteXML(){
        super();
    }

    public OutputStream getOutputStream(File file, String title, String agency, String auth, String bib, String filename, String fileType) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s = null;
        while((s = br.readLine()) != null){
            data.add(new ArrayList<String>());
            String[] temp = s.split("\t");
            int i = 0;
            for(String j : temp){
                data.get(height).add(j);
                i++;
            }
            length = i;
            height++;
        }
        System.out.println(length + " " + height);
        dataCheck();

        /**
         * The main logic creating xml files
         * we use the outputstream to store xml context
         */
        OutputStream os = new ByteArrayOutputStream();
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        /**
         * XMLStreamWrter is used to manipulate input of os
         */
        XMLStreamWriter xmls = null;
        try{
            /**
             * connect xlms with os
             */
            xmls = xmlOutputFactory.createXMLStreamWriter(os);
            /**
             * start writing
             */
            xmls.writeStartDocument();
            xmls.writeStartElement("codeBook");
            writeAttribute(xmls, "xmlns", "http://www.icpsr.umich.edu/DDI");
            writeAttribute(xmls, "version", "2.0");

            /**
             * write stdDscr part
             */
            xmls.writeStartElement("stdDscr");
            xmls.writeStartElement("titlStmt");
            xmls.writeStartElement("titl");
            xmls.writeCharacters(title);
            xmls.writeEndElement();
            xmls.writeStartElement("IDNo");
            writeAttribute(xmls, "agency", "doi");
            xmls.writeCharacters(agency);
            xmls.writeEndElement();
            xmls.writeStartElement("rspStmt");
            xmls.writeStartElement("AuthEnty");
            xmls.writeCharacters(auth);
            xmls.writeEndElement();
            xmls.writeEndElement();
            xmls.writeStartElement("biblCit");
            xmls.writeCharacters(bib);
            xmls.writeEndElement();
            xmls.writeEndElement();
            xmls.writeEndElement();

            /**
             * write fileDscr part
             */
            xmls.writeStartElement("fileDscr");
            xmls.writeStartElement("fileTxt");
            xmls.writeStartElement("fileName");
            xmls.writeCharacters(filename);
            xmls.writeEndElement();
            xmls.writeStartElement("dimensns");
            xmls.writeStartElement("caseQnty");
            xmls.writeCharacters(Integer.toString(height));
            xmls.writeEndElement();
            xmls.writeStartElement("varQnty");
            xmls.writeCharacters(Integer.toString(length));
            xmls.writeEndElement();
            xmls.writeEndElement();
            xmls.writeStartElement("fileType");
            xmls.writeCharacters(fileType);
            xmls.writeEndElement();
            xmls.writeEndElement();
            xmls.writeEndElement();

            xmls.writeStartElement("dataDscr");
            for(int i = 0; i < length; i++){
                /**
                 * here document 8 characteristic statistic value of a line of data of same type
                 * each part start with element '<var>' and end up with '</var>'
                 */
                xmls.writeStartElement("var");
                writeAttribute(xmls, "ID", "v" + i);
                writeAttribute(xmls, "name", data.get(0).get(i));

                //location
                xmls.writeStartElement("location");
                writeAttribute(xmls, "fileid", "f");
                xmls.writeEndElement();

                //labl
                xmls.writeStartElement("labl");
                writeAttribute(xmls, "level", "variable");
                xmls.writeCharacters(data.get(0).get(i));
                xmls.writeEndElement();

                //if this line is of String type, then skip this step
                String type = "character";
                try{
                    Double.parseDouble(data.get(1).get(i));
                    double[] statisticdata = SummaryStatCalculator.calculateSummaryStatistics(i, height, data);

                    /**
                     * write min element
                     */
                    xmls.writeStartElement("sumStat");
                    writeAttribute(xmls, "type", "min");
                    xmls.writeCharacters(Double.toString(statisticdata[5]));
                    xmls.writeEndElement();

                    /**
                     * write invalid element
                     */
                    xmls.writeStartElement("sumStat");
                    writeAttribute(xmls, "type", "invd");
                    xmls.writeCharacters(Double.toString(statisticdata[4]));
                    xmls.writeEndElement();

                    /**
                     * write number type element
                     */
                    xmls.writeStartElement("sumStat");
                    writeAttribute(xmls, "type", "mode");
                    xmls.writeCharacters(Double.toString(statisticdata[2]));
                    xmls.writeEndElement();

                    /**
                     * write medium number element
                     */
                    xmls.writeStartElement("sumStat");
                    writeAttribute(xmls, "type", "medn");
                    xmls.writeCharacters(Double.toString(statisticdata[1]));
                    xmls.writeEndElement();

                    /**
                     * write mean number in this element
                     */
                    xmls.writeStartElement("sumStat");
                    writeAttribute(xmls, "type", "mean");
                    xmls.writeCharacters(Double.toString(statisticdata[0]));
                    xmls.writeEndElement();

                    /**
                     * write standard deviation in this element
                     */
                    xmls.writeStartElement("sumStat");
                    writeAttribute(xmls, "type", "stdev");
                    xmls.writeCharacters(Double.toString(statisticdata[7]));
                    xmls.writeEndElement();

                    /**
                     * write maximum number in this element
                     */
                    xmls.writeStartElement("sumStat");
                    writeAttribute(xmls, "type", "max");
                    xmls.writeCharacters(Double.toString(statisticdata[6]));
                    xmls.writeEndElement();

                    /**
                     * write valid number in this element
                     */
                    xmls.writeStartElement("sumStat");
                    writeAttribute(xmls, "type", "vald");
                    xmls.writeCharacters(Double.toString(statisticdata[3]));
                    xmls.writeEndElement();

                    type = "numeric";
                }catch (Exception e){
                    System.out.println("a character serial");
                }finally {
                    xmls.writeStartElement("varFormat");
                    writeAttribute(xmls, "type", type);
                    xmls.writeEndElement();

                    xmls.writeEndElement();
                }
            }

            //end dataDscr
            xmls.writeEndElement();

            //rnf dataBook
            xmls.writeEndElement();
            //end writing
            xmls.writeEndDocument();
        } catch (XMLStreamException e){
            System.out.println("error!");
        }finally {
            try{
                if(xmls != null)
                    xmls.close();
            }catch (XMLStreamException e){

            }
        }
        return os;
    }

    /**
     * write xml attributes
     * @param xmlw
     * @param name
     * @param value
     * @throws XMLStreamException
     */
    private void writeAttribute(XMLStreamWriter xmlw, String name, String value) throws XMLStreamException {
        if (!value.isEmpty()) {
            xmlw.writeAttribute(name, value);
        }

    }

    /**
     * deal with data set of wrong line number
     */
    private static void dataCheck(){
        for(int i = 0; i < height; i++){
            for(int j = 0; j < data.get(i).size(); j++){
                /**
                 * check all the elements of data，transfer the ' ' into '_' in the data
                 */
                char[] test = data.get(i).get(j).toCharArray();
                for(int k = 0; k < test.length; k++){
                    if(test[k] == '\"'){
                        k++;
                        while(test[k] != '\"') {
                            /**
                             * transfer ' ' into '_'
                             */
                            if (test[k] == ' ')
                                test[k] = '_';
                            k++;
                        }
                        k++;
                    }
                }
                String temp = String.valueOf(test);

                /**
                 * according to the regular expression " +" splitting data
                 */
                String[] t = temp.split(" +");
                data.get(i).remove(j);
                for(int k = t.length - 1; k >= 0; k--){
                    //t[k].replace("_", " ");
                    data.get(i).add(j, t[k]);
                }
            }
        }
    }
}