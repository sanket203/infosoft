package com.infosoft.ats.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.TeeContentHandler;
import org.apache.tika.sax.ToXMLContentHandler;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFConvertor {
	
	public static String EnglishPdfFileExtraction() throws Exception
    { 
		String content="";
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
	
	 public static String HtmlFileExtraction() throws Exception
	    {
		 String FILENAME = "G:\\INFOSOFT\\htmlTOtext.txt";
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
	        BufferedWriter bw = null;
			FileWriter fw = null;

			try {

				String content = handler.toString();

				fw = new FileWriter(FILENAME);
				bw = new BufferedWriter(fw);
				bw.write(content);

				System.out.println("Done");

			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {

					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}

			}


	        return handler.toString();
	    }
	    
	 
	 public static boolean convertTextToPDF() throws Exception {
File file = new File("G:\\INFOSOFT\\htmlTOtext.txt");
		    BufferedReader br = null;

		    try {

		        Document pdfDoc = new Document(PageSize.A4);
		        String output_file = "G:\\INFOSOFT\\Resumes\\ouputpdf.pdf";
		        System.out.println("## writing to: " + output_file);
		        PdfWriter.getInstance(pdfDoc, new FileOutputStream(output_file)).setPdfVersion(PdfWriter.VERSION_1_7);;

		        pdfDoc.open();
		        BaseFont courier = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1252, BaseFont.EMBEDDED);
		        Font myfont = new Font(courier);
		        myfont.setStyle(Font.NORMAL);
		        myfont.setSize(11);

		        pdfDoc.add(new Paragraph("\n"));

		        if (file.exists()) {

		            br = new BufferedReader(new FileReader(file));
		            String strLine;

		            while ((strLine = br.readLine()) != null) {
		                Paragraph para = new Paragraph(strLine + "\n", myfont);
		                para.setAlignment(Element.ALIGN_JUSTIFIED);
		                pdfDoc.add(para);
		            }
		        } else {
		            System.out.println("no such file exists!");
		            return false;
		        }
		        pdfDoc.close();
		    }

		    catch (Exception e) {
		        e.printStackTrace();
		    } finally {
		        if (br != null) 
		            br.close();
		    }

		    return true;
		}

	public static void main(String[] args) {
		
try {
	HtmlFileExtraction();
	convertTextToPDF();
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

	}

}
