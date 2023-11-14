package TurnQuest.view.reports;


import TurnQuest.view.Base.GlobalCC;

import com.sun.java.util.collections.ArrayList;
import com.sun.java.util.collections.Iterator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Array;

import java.sql.DriverManager;

import javax.faces.context.FacesContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import oracle.apps.xdo.common.pdf.util.PDFDocMerger;
import oracle.apps.xdo.dataengine.DataProcessor;
import oracle.apps.xdo.dataengine.Parameter;
import oracle.apps.xdo.template.FOProcessor;
import oracle.apps.xdo.template.RTFProcessor;
import oracle.apps.xdo.template.fo.util.FOUtility;

import oracle.jdbc.OracleConnection;


public class XMLPublisher {
    public XMLPublisher() {
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);

    public void foProcessorEngine() {
        try {
            FOProcessor processor = new FOProcessor();
            processor.setData("catalog.xml");
            processor.setTemplate("catalog.xsl");
            processor.setOutput("catalog.pdf");
            processor.setOutputFormat(FOProcessor.FORMAT_PDF);

            processor.generate();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
    }

    public void foProcessorEngine2() {
        try {
            FOProcessor processor = new FOProcessor();
            processor.setData("catalogData.xml");
            processor.setTemplate("catalogData.xsl");
            processor.setOutput("catalogData.pdf");
            processor.setOutputFormat(FOProcessor.FORMAT_PDF);

            processor.generate();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
    }

    public byte[] processorEngine(String format, String templateFile,
                                  String styleFile, String reportName) {

        byte[] bytes = null;

        try {
            templateFile = "/Reports/" + templateFile;
            styleFile = "/Reports/" + styleFile;
            reportName = "/Reports/" + reportName;

            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext sc =
                (ServletContext)context.getExternalContext().getContext();
            templateFile = sc.getRealPath(templateFile);
            styleFile = sc.getRealPath(styleFile);
            FOProcessor processor = new FOProcessor();
            RTFProcessor rtfProcessor = null; //input template
            rtfProcessor = new RTFProcessor(templateFile);
            rtfProcessor.setOutput(styleFile);

            rtfProcessor.process();

            /*  RTFProcessor rtfProcessor =
                new RTFProcessor(templateFile); //input template
            rtfProcessor.setOutput(styleFile); // output file
            rtfProcessor.process();
*/
            //System.out.println("First");
            String filename = null;
            filename = reportName;

            String data = reportName + ".xml";
            String template = styleFile;
            data = sc.getRealPath(data);
            filename = sc.getRealPath(filename);
            String output = null;
            processor.setData(data);
            processor.setTemplate(template);
            processor.setOutput(output);
            if (format == null) {
                processor.setData(data);
                processor.setTemplate(template);
                processor.setOutput(filename + ".pdf");
                output = filename + ".pdf";
                processor.setOutputFormat(FOProcessor.FORMAT_PDF);
            } else {
                processor.setData(data);
                processor.setTemplate(template);
                processor.setOutput(filename + "." + format);
                output = filename + "." + format;
                if (format.equalsIgnoreCase("pdf")) {
                    processor.setOutput(filename + "." + format);
                    processor.setOutputFormat(FOProcessor.FORMAT_PDF);
                } else if (format.equalsIgnoreCase("rtf")) {
                    processor.setOutput(filename + "." + format);
                    processor.setOutputFormat(FOProcessor.FORMAT_RTF);
                } else if (format.equalsIgnoreCase("html")) {
                    processor.setOutput(filename + "." + format);
                    processor.setOutputFormat(FOProcessor.FORMAT_HTML);
                } else if (format.equalsIgnoreCase("xls")) {
                    processor.setOutput(filename + "." + format);
                    processor.setOutputFormat(FOProcessor.FORMAT_EXCEL);
                }
            }
            System.out.println("before  Generate");
            processor.generate();
            System.out.println("  post Generate");
            File file = new File(output);
            bytes = getBytesFromFile(file);
            //System.out.println("Final");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }

        return bytes;
    }

    public File FileProcessorEngine(String format, String templateFile,
                                    String styleFile, String reportName) {

        byte[] bytes = null;
        File file = null;
        try {
            templateFile = "/Reports/" + templateFile;
            styleFile = "/Reports/" + styleFile;
            reportName = "/Reports/" + reportName;

            FacesContext context = FacesContext.getCurrentInstance();

            ServletContext sc =
                (ServletContext)context.getExternalContext().getContext();
            templateFile = sc.getRealPath(templateFile);
            styleFile = sc.getRealPath(styleFile);
            FOProcessor processor = new FOProcessor();
            RTFProcessor rtfProcessor = null; //input template
            rtfProcessor = new RTFProcessor(templateFile);

            rtfProcessor.setOutput(styleFile);

            rtfProcessor.process();

            /*  RTFProcessor rtfProcessor =
              new RTFProcessor(templateFile); //input template
          rtfProcessor.setOutput(styleFile); // output file
          rtfProcessor.process();
  */
            //System.out.println("First");
            String filename = null;
            filename = reportName;

            String data = reportName + ".xml";
            String template = styleFile;
            data = sc.getRealPath(data);
            filename = sc.getRealPath(filename);
            String output = null;
            processor.setData(data);
            processor.setTemplate(template);
            processor.setOutput(output);
            if (format == null) {
                processor.setData(data);
                processor.setTemplate(template);
                processor.setOutput(filename + ".pdf");
                output = filename + ".pdf";
                processor.setOutputFormat(FOProcessor.FORMAT_PDF);
            } else {
                processor.setData(data);
                processor.setTemplate(template);
                processor.setOutput(filename + "." + format);
                output = filename + "." + format;
                if (format.equalsIgnoreCase("pdf")) {
                    processor.setOutput(filename + "." + format);
                    processor.setOutputFormat(FOProcessor.FORMAT_PDF);
                } else if (format.equalsIgnoreCase("rtf")) {
                    processor.setOutput(filename + "." + format);
                    processor.setOutputFormat(FOProcessor.FORMAT_RTF);
                } else if (format.equalsIgnoreCase("html")) {
                    processor.setOutput(filename + "." + format);
                    processor.setOutputFormat(FOProcessor.FORMAT_HTML);
                } else if (format.equalsIgnoreCase("xls")) {
                    processor.setOutput(filename + "." + format);
                    processor.setOutputFormat(FOProcessor.FORMAT_EXCEL);
                }
            }
            //System.out.println("Generate");
            processor.generate();
            //System.out.println("post Generate");
            file = new File(output);
            //bytes = getBytesFromFile(file);
            //System.out.println("Final");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }

        return file;
    }

    public File RealFileProcessorEngine(String format, String templateFile,
                                        String styleFile, String reportName,
                                        String realPath) {

        byte[] bytes = null;
        File file = null;
        try {
            templateFile = "/Reports/" + templateFile;
            styleFile = "/Reports/" + styleFile;
            reportName = "/Reports/" + reportName;


            templateFile = realPath + templateFile;
            styleFile = realPath + styleFile;
            FOProcessor processor = new FOProcessor();
            RTFProcessor rtfProcessor = null; //input template
            rtfProcessor = new RTFProcessor(templateFile);

            rtfProcessor.setOutput(styleFile);

            rtfProcessor.process();

            /*  RTFProcessor rtfProcessor =
            new RTFProcessor(templateFile); //input template
        rtfProcessor.setOutput(styleFile); // output file
        rtfProcessor.process();
  */
            //System.out.println("First");
            String filename = null;
            filename = reportName;

            String data = reportName + ".xml";
            String template = styleFile;
            data = realPath + data;
            filename = realPath + filename;
            String output = null;
            processor.setData(data);
            processor.setTemplate(template);
            processor.setOutput(output);
            if (format == null) {
                processor.setData(data);
                processor.setTemplate(template);
                processor.setOutput(filename + ".pdf");
                output = filename + ".pdf";
                processor.setOutputFormat(FOProcessor.FORMAT_PDF);
            } else {
                processor.setData(data);
                processor.setTemplate(template);
                processor.setOutput(filename + "." + format);
                output = filename + "." + format;
                if (format.equalsIgnoreCase("pdf")) {
                    processor.setOutput(filename + "." + format);
                    processor.setOutputFormat(FOProcessor.FORMAT_PDF);
                } else if (format.equalsIgnoreCase("rtf")) {
                    processor.setOutput(filename + "." + format);
                    processor.setOutputFormat(FOProcessor.FORMAT_RTF);
                } else if (format.equalsIgnoreCase("html")) {
                    processor.setOutput(filename + "." + format);
                    processor.setOutputFormat(FOProcessor.FORMAT_HTML);
                } else if (format.equalsIgnoreCase("xls")) {
                    processor.setOutput(filename + "." + format);
                    processor.setOutputFormat(FOProcessor.FORMAT_EXCEL);
                }
            }
            //System.out.println("Generate");
            processor.generate();
            //System.out.println("post Generate");
            file = new File(output);
            //bytes = getBytesFromFile(file);
            //System.out.println("Final");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }

        return file;
    }

    public byte[] getBytesFromFile(File file) {
        byte[] bytes = null;
        try {
            InputStream is = new FileInputStream(file);

            // Get the size of the file
            long length = file.length();

            if (length > Integer.MAX_VALUE) {
                // File is too large
            }

            // Create the byte array to hold the data
            bytes = new byte[(int)length];

            // Read in the bytes
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length &&
                   (numRead = is.read(bytes, offset, bytes.length - offset)) >=
                   0) {
                offset += numRead;
            }

            // Ensure all the bytes have been read in
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " +
                                      file.getName());
            }

            // Close the input stream and return bytes
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return bytes;
    }

    public void xslFoUtility() {
        try {
            InputStream[] input = new InputStream[2];
            InputStream firstFOStream =
                FOUtility.createFO("catalog2.xml", "catalog.xsl");
            InputStream secondFOStream =
                FOUtility.createFO("catalog3.xml", "catalog.xsl");
            Array.set(input, 0, firstFOStream);
            Array.set(input, 1, secondFOStream);

            InputStream mergedFOStream = FOUtility.mergeFOs(input, null);

            if (mergedFOStream == null) {
                //System.out.println("Merge failed.");
            }

            FOProcessor processor = new FOProcessor();
            processor.setData(mergedFOStream);
            processor.setTemplate((String)null);
            processor.setOutput("catalog2.pdf");
            processor.setOutputFormat(FOProcessor.FORMAT_PDF);
            processor.generate();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
    }

    public void xslFoUtility2() {
        try {
            InputStream[] input = new InputStream[2];
            InputStream firstFOStream =
                FOUtility.createFO("catalogData.xml", "catalogData.xsl");
            //InputStream secondFOStream = FOUtility.createFO("catalog3.xml",  "catalog.xsl");
            Array.set(input, 0, firstFOStream);
            FOProcessor processor = new FOProcessor();
            processor.setData(firstFOStream);
            processor.setTemplate((String)null);
            processor.setOutput("catalogData.pdf");
            processor.setOutputFormat(FOProcessor.FORMAT_PDF);
            processor.generate();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
    }

    public void pdfDocumentMerger() {
        try {
            FileInputStream[] inputStreams = new FileInputStream[2];
            inputStreams[0] = new FileInputStream("catalog.pdf");
            inputStreams[1] = new FileInputStream("catalog2.pdf");

            FileOutputStream outputStream =
                new FileOutputStream("catalog3.pdf");
            PDFDocMerger pdfMerger =
                new PDFDocMerger(inputStreams, outputStream);

            pdfMerger.setPageNumberCoordinates(300, 20);
            pdfMerger.setPageNumberFontInfo("Courier", 10);
            pdfMerger.setPageNumberValue(1, 1);
            pdfMerger.process();
            pdfMerger = null;
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
            //System.out.println("XDOException" + e.getMessage());
        }
    }

    public void dataEngine2() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");

            String url = "jdbc:oracle:thin:@10.176.18.64:1522:niger";
            OracleConnection jdbcConnection =
                (OracleConnection)DriverManager.getConnection(url, "TQ_FMS",
                                                              "TQ_FMS");

            DataProcessor dataProcessor = new DataProcessor();
            dataProcessor.setDataTemplate("FGLRVOUC.xml");


            //Get Parameters
            ArrayList parameters2 = dataProcessor.getParameters();
            //set Parameter Values as ArrayList of oracle.apps.xdo.dataengine.Parameter

            Iterator it = parameters2.iterator();

            while (it.hasNext()) {
                Parameter p = (Parameter)it.next();
                if (p.getName().equals("quoteCode")) {
                    p.setValue("2010729");
                } else if (p.getName().equals("UP_ORG_CODE")) {
                    p.setValue("2");

                } else if (p.getName().equals("UP_BRH_CODE")) {
                    p.setValue("76");
                } else if (p.getName().equals("UP_YER_YEAR")) {
                    p.setValue("2010");
                } else if (p.getName().equals("UP_PRD_PERIOD")) {
                    p.setValue("AUG");
                }
            }
            dataProcessor.setParameters(parameters2);

            dataProcessor.setParameters(parameters2);
            dataProcessor.setConnection(jdbcConnection);
            dataProcessor.setOutput("MyDataTemplate24.xml");
            dataProcessor.processData();
            dataProcessor.setParameters(parameters2);

            //OR you can set Parameter Values as Hashtable.

            /*  Hashtable parameters = new Hashtable();
             parameters2.put("p_DeptNo","10");
             dataProcessor.setParameters(parameters);

             dataProcessor.setConnection(jdbcConnection);
             dataProcessor.setOutput("/home/EmpDetails.xml")
             dataProcessor.processData();

          Hashtable parameters = new Hashtable();
          parameters.put("actCode", 100);*/
            dataProcessor.setParameters(parameters2);
            dataProcessor.setConnection(jdbcConnection);
            dataProcessor.setOutput("MyDataTemplate24.xml");
            dataProcessor.processData();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
    }

    public void dataEngine(String reportName, String dataFile, OracleConnection conn) {
        try {

            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext sc =
                (ServletContext)context.getExternalContext().getContext();
            dataFile = "/Reports/" + dataFile;
            reportName = "/Reports/" + reportName;
            String data = dataFile;
            data = sc.getRealPath(data);
            String output = reportName + ".xml";
            output = sc.getRealPath(output);
            DataProcessor dataProcessor = new DataProcessor();
            dataProcessor.setDataTemplate(data);

            System.out.println("DATA:" + data);
            //Get Parameters
            ArrayList parameters = dataProcessor.getParameters();
            Iterator it = parameters.iterator();

            while (it.hasNext()) {
                Parameter p = (Parameter)it.next();
                String key=p.getName();
                if (GlobalCC.checkNullValues(key)!=null) {
                    p.setValue(session.getAttribute(key));
                }
                System.out.println("PARAMETER: "+p.getName()+" ---> " +p.getValue());
            }
            dataProcessor.setParameters(parameters);
            dataProcessor.setConnection(conn);
            dataProcessor.setOutput(output);
            dataProcessor.processData();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
    }

    public void RealDataEngine(String reportName, String dataFile,
                               OracleConnection conn, String realPath) {
        try {


            dataFile = "/Reports/" + dataFile;
            reportName = "/Reports/" + reportName;
            String data = dataFile;
            data = realPath + data;
            String output = reportName + ".xml";
            output = realPath + output;
            DataProcessor dataProcessor = new DataProcessor();
            dataProcessor.setDataTemplate(data);

            System.out.println("DATA:" + data);
            //Get Parameters
            ArrayList parameters2 = dataProcessor.getParameters();
            Iterator it = parameters2.iterator();

            while (it.hasNext()) {
                Parameter p = (Parameter)it.next();
                if (p.getName().equalsIgnoreCase("FROMDT")) {
                    p.setValue(session.getAttribute("dateFrom"));
                } else if (p.getName().equalsIgnoreCase("P_CLNT_TYPE")) {
                    p.setValue(session.getAttribute("clientType"));
                } else if (p.getName().equalsIgnoreCase("P_CURRENCY")) {
                    p.setValue(session.getAttribute("currency"));
                } else if (p.getName().equalsIgnoreCase("P_MIN_ENDOS_AMNT")) {
                    p.setValue(session.getAttribute("minEndorseAmnt"));
                } else if (p.getName().equalsIgnoreCase("TODT")) {
                    p.setValue(session.getAttribute("dateTo"));
                } else if (p.getName().equalsIgnoreCase("P_CLNT_STATUS")) {
                    p.setValue(session.getAttribute("clientStatus"));
                }else if (p.getName().equalsIgnoreCase("V_CLNT_CODE")) {
                    p.setValue(session.getAttribute("clntCode"));
                }
                System.out.println(p.getName()+"  ---> " +p.getValue());
            }
            dataProcessor.setParameters(parameters2);
            dataProcessor.setConnection(conn);
            dataProcessor.setOutput(output);
            dataProcessor.processData();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
    }

    public static void main(String[] argv) {
        XMLPublisher xmlPublisher = new XMLPublisher();
        //  xmlPublisher.foProcessorEngine();
        //  xmlPublisher.xslFoUtility();
        //  xmlPublisher.pdfDocumentMerger();
        xmlPublisher.dataEngine2();
        // xmlPublisher.foProcessorEngine2();
        // xmlPublisher.foProcessorEngine3();
    }
}
