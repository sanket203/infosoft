/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infosoft.ats.service.impl;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.parser.txt.TXTParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.TeeContentHandler;
import org.apache.tika.sax.ToXMLContentHandler;




/**
 * File Extraction is a java Bean Class.In this class we extract the text from any type of file in Hindi & English both 
 * this Class set & get the data of File & other parameters of File  
 * @author Abhilasha 
 * @see  HindiPdfFileExtraction , UrlFetcher
 *
 */
public class FileExtraction
{
      
    // Varible Declartion
   public static String fileName,fileType,fileLanguage,fileContent,content;
   public static String []fileMetaData;
   
   // default constructor 
   /**
    * this is used to create a object of File Extraction class.
    */
   public FileExtraction(){}
    
   // Setter & getter method declaration
   /**
    * This method sets the fileName 
    * @param fileName1 - A string type value 
    * Called By -FileExtraction(File,String)
    * Calls- none
    */
    public static void setFileName(String fileName1){fileName=fileName1;}
    /**
     * this method sets the File Type 
     * @param fileType1  - A String type Value
     * Called By -FileExtraction(File,String)
     * Calls- none
     */
    public static void setFileType(String fileType1){fileType=fileType1;}
    
    /**
     * this method sets the language.
     * in this method to identify the language we use the Pattern.Class 
     * if data is in English then it sets "en" or in Hindi then "lt"
     * @param content  - A string type value of Data 
     * Called By -FileExtraction(File,String),NamedEntityFeature.posTagSentence(String) 
     * ProperNounFeature.totalProperNoun(String)
     * Calls- none
     */
    public static void setFileLanguage(String content)
    {
        // pattern to detect in the string UTF-code in the string to detect hindi & englsih
        Pattern p = Pattern.compile("[\\u0900-\\u097f]",Pattern.UNICODE_CHARACTER_CLASS);
         Matcher mm= p.matcher(content);
             if(mm.find())
                 fileLanguage="lt";
             else
                 fileLanguage="en"; 
    
    }
    /**
     * this method  sets the file Content 
     * @param fileContent1 - A String type value
     * Called By -FileExtraction(File,String)
     * Calls- none
     */
    public static void setFileContent(String fileContent1){fileContent=fileContent1;}
    /**
     * this method  sets File Meta Data
     * @param fileMetadata1 - A String type value
     *  Called By -HTMLFileExtraction(File,String)
     *   Calls- none
     */
    public static void setFileMetaData(String fileMetadata1[]) { fileMetaData=fileMetadata1;}
    
    
    // Getter methods 
    /**
     * this method returns the File Name 
     * @return - A String type value
     * Called By-FileExtractionContent()
     * Calls - none
     */
    public static String getFileName(){return fileName;}
    /**
     * this method returns the File Type
     * @return - A String type value
     * Called By -textExtractionForSummary(File,String)
     * Calls-none
     */
    public static String getFileType(){return fileType;}
    /**
     * this method returns the File Language
     * @return - A String type Value
     * Called By -textExtractionForSummary(File, String)
     * Calls -none
     */
    public static String getFileLanguage(){return fileLanguage;}
    /**
     * this method returns the File Content
     * @return - A String type Value
     * Called By - textExtractionForSummary(File,String), showExtractText.jsp ,fileUpload.jsp
     * NamedEntityFeature.posTagSentence(String),ProperNounFeature.totalProperNoun(String sentence)
     * Calls - none
     */
    public static String getFileContent(){return fileContent;}
    /**
     * this method returns the File Meta Data
     * @return - A String type Value
     * Calls-none
     * Called By -showExtractText.jsp
     */
    public static String[] getFileMetaData(){return fileMetaData;}
    
    // method declaration  
    /**
     * this method use for English text extraction from pdf file and return file content
     * @return - A String type file Content
     * Called By -FileExtraction(File,String)
     * Calls -none
     * @throws Exception 
     */
     
    public static String EnglishPdfFileExtraction() throws Exception
    { 
        // to convert the pdf file into xml & then get the data
        InputStream inputstream=new FileInputStream("G:\\INFOSOFT\\Resumes\\test.pdf"); 
        ToXMLContentHandler hand=new ToXMLContentHandler();
        TeeContentHandler handler=new TeeContentHandler(hand);
        Metadata metadata =new Metadata();

        AutoDetectParser autoParser=new AutoDetectParser();
        ParseContext pcontext=new ParseContext();
        autoParser.parse(inputstream, handler,metadata,pcontext); 
        content=hand.toString();
        
      
            return content;
        
     }
    
    public static void main(String[] args) throws Exception {
		//EnglishPdfFileExtraction();
		HtmlFileExtraction();
	}
    /**
     * this method extract the text from text type of file and return file text in both hindi & english language
     * @param handler - A BodyContentHandler type
     * @param metadata - A MetaData type
     * @param inputstream - A FileInputStream type
     * @param pcontext -A parse Context type
     * @return - A String type File Content 
     * Called By- FileExtractionContent()
     * Calls - none
     * @throws Exception 
     */
    
    public static String TextFileExtraction(BodyContentHandler handler,Metadata metadata, FileInputStream inputstream,ParseContext pcontext) throws Exception
    {
        TXTParser TexTParser = new TXTParser(); 
        TexTParser.parse(inputstream, handler, metadata,pcontext);
        fileContent =handler.toString();
        return fileContent;
    }
   /**
     * this method extract the text from html type of file and return file text in both hindi & english language
     * @param handler - A BodyContentHandler type
     * @param metadata - A MetaData type
     * @param inputstream - A FileInputStream type
     * @param pcontext -A parse Context type
     * @return - A String type File Content 
     * Called By- FileExtractionContent()
     * Calls - none
     * @throws Exception 
     */
    
    public static String HtmlFileExtraction() throws Exception
    {
    	BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream inputstream = new FileInputStream(new File("G:\\INFOSOFT\\Resumes\\test.txt"));
        ParseContext pcontext = new ParseContext();
        
        //Html parser 
        HtmlParser htmlparser = new HtmlParser();
        htmlparser.parse(inputstream, handler, metadata,pcontext);
        System.out.println("Contents of the document:" + handler.toString());
        System.out.println("Metadata of the document:");
        String[] metadataNames = metadata.names();
        return handler.toString();
    }
    
    /**
     * this method extract the text from Micro Soft Word type of file and return file text in both hindi & english language
     * @param handler - A BodyContentHandler type
     * @param metadata - A MetaData type
     * @param inputstream - A FileInputStream type
     * @param pcontext -A parse Context type
     * @return - A String type File Content 
     * Called By- FileExtractionContent()
     * Calls - none
     * @throws Exception 
     */
    
    public static String MSFileExtraction(BodyContentHandler handler,Metadata metadata, FileInputStream inputstream,ParseContext pcontext) throws Exception
    {
        System.out.println("ms file");
        OOXMLParser msofficeparser=new OOXMLParser (); 
        msofficeparser.parse(inputstream, handler, metadata,pcontext);
        fileContent = handler.toString();
        return fileContent;
    }
    
    /**
     * this method is used to extract the text from the any type of file and return the content in both language
     * Hindi & English.
     * Called By-FileExtraction(File,String)
     * Calls -MSFileExtraction(BodyContentHandler,Metadata,FileInputStream,ParseContext),TextFileExtraction(BodyContentHandler,Metadata,FileInputStream,ParseContext)
     * HTMLFileExtraction(BodyContentHandler,Metadata,FileInputStream,ParseContext)
     * @return A String type file Content
     */
    public static String FileExtractionContent()
    {   
            try{
            BodyContentHandler handler = new BodyContentHandler(-1);
            Metadata metadata = new Metadata();
            FileInputStream inputstream=new FileInputStream(new File(getFileName()));
            ParseContext pcontext=new ParseContext();
            
             if(fileType.equalsIgnoreCase("text/plain")) fileContent =FileExtraction.TextFileExtraction(handler,metadata,inputstream,pcontext);
           //  else if(fileType.equalsIgnoreCase("text/html")) fileContent =HtmlFileExtraction(handler,metadata,inputstream,pcontext);
             else if(fileType.contains("application/vnd"))
             { 
                 if(fileType.contains("application/vnd.ms-powerpoint"))fileContent=fileContent;
                 else fileContent=MSFileExtraction(handler,metadata,inputstream,pcontext);
             }
             else{fileContent= fileContent;}
             }catch(Exception e){ fileContent ="null";}
            return fileContent;
    }

    /**
     * This method extracts the text from the File & set the all parameters of the file 
     * @param file - A File type 
     * @param fileName1 - A String type Value of File Name
     * Called By -
     * Calls -setFileType(String),setFileName(String),setFileLanguage(String),EnglishPdfFileExtraction()
     * HindiPdfFileExtraction.HindiPdfFileExtractionContent(String),FileExtractionContent()
     */
    // this method uses all methods for extraction of the data 
    public static void FileExtraction(File file,String fileName1) 
    {
        Tika tika=new Tika();
        tika.setMaxStringLength(1024*1024*1024); 
        fileType=tika.detect(fileName1);
        setFileType(fileType);
        setFileName(fileName1); 
        try
        {   
            fileContent = tika.parseToString(file);
            setFileLanguage(fileContent);
            if(fileType.equalsIgnoreCase("application/pdf"))
            {
                    if(fileLanguage.equalsIgnoreCase("en")) fileContent=EnglishPdfFileExtraction();
                   // else fileContent= HindiPdfFileExtraction.HindiPdfFileExtractionContent(fileName1);
            }
            else fileContent = FileExtractionContent();
            setFileContent(fileContent);
            String s = System.getProperty("user.dir");
        }catch(Exception e){}     
     }
    
    /***
     * This method is used to write the fileContent in the file and returns the true when it is data write in file otherwise false
     * @param filePath - A String type value of File path 
     * @param fileContent - A String type value of File Content
     * @return - A boolean type value 
     * Called By -
     * Calls -none
     * @throws Exception 
     */
    public static boolean CreateFileExtracted(String filePath, String fileContent) throws Exception
    {
       if(new File(filePath).exists()) new File(filePath).delete();
       File createFileExtract = new File(filePath);
       fileContent = fileContent.trim();
       fileContent=fileContent.replaceAll("<br>","\\n");
       try{
            FileUtils.writeStringToFile(createFileExtract, fileContent,"UTF-8");
           }catch(Exception e){return false;}
       return true;
     }
    /**
     * This method is used to write the fileContent in the file and returns the File
     * @param filePath - A String type value of File Path
     * @param fileContent - A String type value of File Content
     * Called By -
     * Calls - none
     * @return A File  
     * @throws Exception 
     */
    
    public static File createTextExtractedFile(String filePath, String fileContent) throws Exception
    {
       if(new File(filePath).exists()) new File(filePath).delete();
       File createFileExtract = new File(filePath);
       fileContent = fileContent.trim();
       FileUtils.writeStringToFile(createFileExtract, fileContent,"UTF-8");
       return createFileExtract;
     }
    /**
     * this method is used to create a file for further pre processing of extracted text 
     * @param fileContent - A String type of File Content
     * @return A String type value of processed file content 
     * Called by -textExtractionForSummary(File, String)
     * Calls - none
     * @throws Exception 
     */
    
     public static String textPreProcessing(String fileContent) throws Exception
    {
     /*  // handle the abbrivations of the string
       fileContent=sentiPreProcessing.removeAbbrevationFromText(fileContent.trim());
       fileContent=fileContent.replaceAll("[‘]","\"");
       fileContent=fileContent.replaceAll("[’]","\"");
       fileContent = fileContent.replaceAll("[\']","\"");
       fileContent = fileContent.replaceAll("[|]",". ");
       System.out.println("in text preprocess");
       
       StringBuffer data = new StringBuffer(fileContent); 
       // pattern to detect the qoutes in between string
       Pattern pp = Pattern.compile("\"(.*?)\"");
       Matcher mm = pp.matcher(data);
       while(mm.find())
       {
           data.replace(mm.start(0),mm.end(0),mm.group(0).toLowerCase());
           data.replace(mm.start(0),mm.end(0),mm.group(0).replace("[।]","@"));
           data.replace(mm.start(0),mm.end(0),mm.group(0).replace("[?]","#"));
           data.replace(mm.start(0),mm.end(0),mm.group(0).replace("[!]","$"));
       }  
       return data.toString();*/
    	 return null;
     }
      
    /**
     * This method is used to extract the  text from file For Text Summarization  and returns the Array of Sentences.
     * @param extractFile - A File of extracted text
     * @param Name- A String type of filename
     * @return- Array of Sentences
     * Called By-
     * Calls -textPreProcessing(String),getFileType(),getFileLanguage(),textsummarization.SentenceDetection.sentenceDetectSummary(String)
     * @throws Exception 
     */
    
    public static String[] textExtractionForSummary(File extractFile, String Name) throws Exception
    {
        
            FileExtraction(extractFile,Name);
            String content=getFileContent();
            
            // to preprocess the data accoring to sentence
            content = textPreProcessing(content);
            String getType = getFileType(); 
            String language = getFileLanguage();
            String finalTextForSummary[];
            ArrayList<String> finalTextExtractedList=new ArrayList<String>();
            if(getType.equalsIgnoreCase("application/pdf")&&language.equals("en"))
            {
                    String[] paragraphContent=content.split("<br>");
                    Pattern p=Pattern.compile("[^A-Za-z0-9., ]");
                    Pattern p1=Pattern.compile("^[0-9.]*$");//pattern to check string contains only numbers
                    Matcher m,m1,m2;
                    for(int i=0;i<paragraphContent.length;i++)
                    {
                     ArrayList<String> paragraphSentences=null;//SentenceDetection.sentenceDetectSummary(paragraphContent[i]);  
                     for(int j=0;j<paragraphSentences.size();j++)
                        {
                            m=p.matcher(paragraphSentences.get(j));
                            m1=p1.matcher(paragraphSentences.get(j));
                            if(paragraphSentences.get(j).startsWith("ABSTRACT") || paragraphSentences.get(j).startsWith("Abstract"))  
                            {
                                finalTextExtractedList=new ArrayList<String>();
                            }
                             if((paragraphSentences.get(j).contains("Table.")) || (paragraphSentences.get(j).contains("fig.")) || (paragraphSentences.get(j).contains("Fig.")))
                            {
                                j=j+1;
                            }
                            else
                            {    
                                if(!m.find() && paragraphSentences.get(j).length()>4 && !m1.find() &&   !finalTextExtractedList.contains(paragraphSentences.get(j)) && !paragraphSentences.get(j).startsWith("."))    
                                    finalTextExtractedList.add(paragraphSentences.get(j));
                       
                            }          
                }      
            }
            }
           /* else
            {
              finalTextExtractedList=SentenceDetection.sentenceDetectSummary(content);          
            }*/
            finalTextForSummary=new String[finalTextExtractedList.size()];
               for(int j=0;j<finalTextExtractedList.size();j++)
                    {
                        finalTextForSummary[j]=finalTextExtractedList.get(j).trim();
                        finalTextForSummary[j]=finalTextForSummary[j].replaceAll("[@]","।");
                        finalTextForSummary[j]=finalTextForSummary[j].replaceAll("[#]","?");
                        finalTextForSummary[j]=finalTextForSummary[j].replaceAll("[$]","!");
                    }
        return finalTextForSummary;    
    }
}



