package TurnQuest.view.reports;


//import TurnQuest.view.Documents.AlfrescoHelper;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.sql.ResultSet;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.input.RichSelectBooleanRadio;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.nav.RichCommandLink;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.apache.commons.dbutils.DbUtils;


public class ReportEngine {
    private RichCommandLink rptlink;


    public ReportEngine() {
        super();
    }

    /* HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    */
    /* public String QuotationReport() {
        JasperReport jasperReport = null;
        DBConnector datahandler = null; datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        HashMap parameters = new HashMap();
        parameters = null;
        parameters = new HashMap();

        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();

        String path = sc.getRealPath("/Reports/Quotnrpt.jrxml");
        String path1 = sc.getRealPath("Reports/TurnQuest_ReportHeader.jrxml");
        String path2 = sc.getRealPath("Reports/Quotnrpt_1.jrxml");
        String path3 = sc.getRealPath("Reports/Quotnrpt_3.jrxml");
          try {
             jasperReport =
                    JasperCompileManager.compileReport(path);
            JasperReport header = JasperCompileManager.compileReport(path1);
            JasperReport subReport1 = JasperCompileManager.compileReport(path2);
            JasperReport subReport2 = JasperCompileManager.compileReport(path3);

            parameters.put("quocode",session.getAttribute("quoteCode"));
            parameters.put("header",header);
            parameters.put("subReport1",subReport1);
            parameters.put("subReport2",subReport2);
            HttpServletResponse response =
                (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
            ServletOutputStream servletOutputStream;
            response.reset();
            response.resetBuffer();
            servletOutputStream = response.getOutputStream();
            byte[] bytes = null;

            bytes =
                    JasperRunManager.runReportToPdf(jasperReport, parameters, conn);
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition",
                               "attachment; filename=\"QuotationReport.pdf\"");
            response.setContentLength(bytes.length);

            servletOutputStream.write(bytes, 0, bytes.length);
            servletOutputStream.flush();
            servletOutputStream.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn,e);
        }

        return null;
    }

    public String ArchiveQuotationReport() {
        JasperReport jasperReport = null;
        DBConnector datahandler = null; datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        HashMap parameters = new HashMap();
        parameters = null;
        parameters = new HashMap();

        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();

        String path = sc.getRealPath("/Reports/Quotnrpt.jrxml");
        String path1 = sc.getRealPath("Reports/TurnQuest_ReportHeader.jrxml");
        String path2 = sc.getRealPath("Reports/Quotnrpt_1.jrxml");
        String path3 = sc.getRealPath("Reports/Quotnrpt_3.jrxml");
          try {
             jasperReport =
                    JasperCompileManager.compileReport(path);
            JasperReport header = JasperCompileManager.compileReport(path1);
            JasperReport subReport1 = JasperCompileManager.compileReport(path2);
            JasperReport subReport2 = JasperCompileManager.compileReport(path3);

            parameters.put("quocode",session.getAttribute("quoteCode"));
            parameters.put("header",header);
            parameters.put("subReport1",subReport1);
            parameters.put("subReport2",subReport2);

            byte[] bytes = null;

            bytes =
                    JasperRunManager.runReportToPdf(jasperReport, parameters, conn);
            AlfrescoHelper alfrescoHelper = new AlfrescoHelper();
              String name = null;
            name = "Quotation-"+new Date().toString()+".pdf";
            AuthenticationServiceSoapBindingStub authenticationService = (AuthenticationServiceSoapBindingStub) new AuthenticationServiceLocator().getAuthenticationService();

            // Start the session
            AuthenticationResult result = authenticationService.startSession("admin", "admin");
            String ticket2 = result.getTicket();
            String url = "http://localhost:8080/alfresco/upload/"+name+"?ticket="+ticket2;
                      URL server = new URL(url);
                      HttpURLConnection connection = (HttpURLConnection)server.openConnection();
                      connection.setRequestMethod("PUT");
                      connection.setDoOutput(true);
                      connection.setDoInput(true);
                      connection.connect();

                      OutputStream cosa = connection.getOutputStream();
                      cosa.write(bytes);
                      cosa.flush();
                      //System.out.println("code: "+connection.getResponseCode());
                      InputStream is = connection.getInputStream();
                      int c;
                      while ((c=is.read())!=-1){
                         System.err.print((char)c);
                      }
            alfrescoHelper.uploadFile("Sampleclient", name, bytes);
            ///alfresco/upload/workspace/SpacesStore/0000-0000-0000-0000/myfile.pdf

            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn,e);
        }

        return null;
    }*/
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);

    private RichSelectOneRadio repFormat;
    private RichCommandButton rptBtn;
    private RichCommandLink rpt1;
    private RichSelectBooleanRadio radioBtn;
    private RichSelectOneChoice sendTo;
    public static String checkReportDetails(BigDecimal rptCode)
     {
             DBConnector datahandler = new DBConnector();
         OracleConnection conn=null;
         OracleCallableStatement callStmt = null;
         
         String status=null;
         ResultSet rs=null;
        try{
             conn = (OracleConnection) datahandler.getDatabaseConnection();
            String query =" BEGIN ? := tqc_web_cursor.check_rpt_details(?) ; END;";
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.setBigDecimal(2,rptCode);
            callStmt.registerOutParameter(1, OracleTypes.VARCHAR);
            callStmt.execute();
            status=callStmt.getString(1);
            callStmt.close();
            conn.close();   
        }catch(Exception e)
        {
            GlobalCC.EXCEPTIONREPORTING(conn,e);
            status=e.getMessage();
        }finally{
             DbUtils.closeQuietly(conn, callStmt, rs);
        }
        return status;
     }
    public String reportOne(BigDecimal rptCode) {
        XMLPublisher xmlPublisher = new XMLPublisher();
        String templateFile = null;
        String styleFile = null;
        String reportName = null;
        String dataFile = null;
        byte[] bytes = null;
        String reportFormat = null;
        if (repFormat != null) {
            reportFormat = GlobalCC.checkNullValues(repFormat.getValue());
        } else if (session.getAttribute("outputFormat") != null) {
            reportFormat = (String)session.getAttribute("outputFormat");
        } else {
            reportFormat = "pdf";
        }
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;
        if (rptCode.toString().equalsIgnoreCase("0")) {
            GlobalCC.errorValueNotEntered("Report Not Attached");
            return null;
        }
        String status=ReportEngine.checkReportDetails(rptCode);
        if(!"OK".equalsIgnoreCase(status)) 
        {
            GlobalCC.errorValueNotEntered("Error:  "+status);
            return null;
        }
        String jobquery = "begin ? := tqc_web_cursor.getRptDetails(?); end;";

        try {
            conn = dbConnector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(jobquery);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2, rptCode);
            cst.executeQuery();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            while (rs.next()) {
                templateFile = rs.getString(5);
                styleFile = rs.getString(7);
                reportName = rs.getString(2);
                dataFile = rs.getString(3);
                
                session.setAttribute("title", reportName);
                System.out.println("title: "+reportName);
                System.out.println("styleFile: "+styleFile);
                System.out.println("dataFile: "+dataFile);
                System.out.println("templateFile: "+templateFile);
                
                //styleFile = styleFile.toUpperCase();
                //styleFile = styleFile.replace(".XSL", ".xsl");
                //dataFile = dataFile.toUpperCase();
                //dataFile = dataFile.replace(".XML", ".xml");
                //templateFile = templateFile.toUpperCase();
                //templateFile = templateFile.replace(".RTF", ".rtf");
                //reportName = reportName.toUpperCase();
                System.out.println(reportName);
            }
            if(templateFile==null) {
                GlobalCC.errorValueNotEntered("Error Template File For Report "+rptCode+" not defined!");
                return null;
            }
            if(dataFile==null) {
                GlobalCC.errorValueNotEntered("Error DataFile File For Report "+rptCode+" not defined!");
                return null;
            }
            xmlPublisher.dataEngine(reportName, dataFile, conn);
           
           
            bytes = xmlPublisher.processorEngine(reportFormat, templateFile, styleFile,
                                                 reportName);

            if (bytes == null) {
                return null;
            }
            HttpServletResponse response =
                (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
            ServletOutputStream servletOutputStream;
            response.reset();
            response.resetBuffer();
            servletOutputStream = response.getOutputStream();


            String filename = null;
            filename = reportName;


            String output = null;
            if (reportFormat == null) {

                output = filename + ".pdf";
            } else {
                output = filename + "." + reportFormat;
            }


            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition",  "attachment; filename=" + output + "");


            response.setContentLength(bytes.length);

            servletOutputStream.write(bytes, 0, bytes.length);
            servletOutputStream.flush();
            servletOutputStream.close();
            if (rptBtn != null) {
                GlobalCC.refreshUI(rptBtn);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public String paramReport() {
        reportOne((BigDecimal)session.getAttribute("rptCode"));

        return null;
    }

    public void coreReport(ActionEvent actionEvent) {
        String reportId = actionEvent.getComponent().getId();
        reportId = reportId.replace("rpt", "");
        if(reportId.equalsIgnoreCase("0")) {
            if (session.getAttribute("rptCode") == null) {
                GlobalCC.errorValueNotEntered("No Report Selected");
            } else {
                reportId = (String)session.getAttribute("rptCode");
                reportId = reportId.replace("rpt", "");
                reportOne(new BigDecimal(reportId));
            }
        } else {
            reportId = reportId.replace("rpt", "");
            reportOne(new BigDecimal(reportId));
        }
    }

    public void outputFormat(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (valueChangeEvent.getNewValue() == null) {
                session.setAttribute("outputFormat", null);
            } else {
                session.setAttribute("outputFormat",
                                     valueChangeEvent.getNewValue().toString());

            }
        }
    }

    public void printToprinter(ActionEvent actionEvent) {
        String reportId = actionEvent.getComponent().getId();
        reportId = reportId.replace("rpt", "");
        if (reportId.equalsIgnoreCase("0")) {
            if (session.getAttribute("rptCode") == null) {
                GlobalCC.errorValueNotEntered("No Report Selected");
            } else {
                reportId = (String)session.getAttribute("rptCode");
                reportId = reportId.replace("rpt", "");
                reportOne(new BigDecimal(reportId));
            }
        } else {
            reportId = reportId.replace("rpt", "");
            reportOne(new BigDecimal(reportId));
        }


    }

    public void accountLvl(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (valueChangeEvent.getNewValue() == null) {
                session.setAttribute("accLvl", null);
            } else {
                session.setAttribute("accLvl",
                                     new BigDecimal(valueChangeEvent.getNewValue().toString()));
            }
        }
        // Add event code here...
    }

    public void consBrh(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (valueChangeEvent.getNewValue() == null) {
                session.setAttribute("consBrh", "N");
            } else {
                if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("true")) {
                    session.setAttribute("consBrh", "Y");
                } else {
                    session.setAttribute("consBrh", "N");
                }
            }
        }
    }

    public void dateTo(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (valueChangeEvent.getNewValue() == null) {
                session.setAttribute("dateTo", null);
            } else {
                session.setAttribute("dateTo",
                                     GlobalCC.parseDate(valueChangeEvent.getNewValue().toString()));

            }
        }
    }

    public void chequeTo(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (valueChangeEvent.getNewValue() == null) {
                session.setAttribute("valTo", null);
            } else {
                session.setAttribute("valTo", valueChangeEvent.getNewValue());

            }
        }
    }

    public void chequeFrom(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (valueChangeEvent.getNewValue() == null) {
                session.setAttribute("valFrom", null);
            } else {
                session.setAttribute("valFrom",
                                     valueChangeEvent.getNewValue());

            }
        }
    }

    public void dateFrom(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (valueChangeEvent.getNewValue() == null) {
                session.setAttribute("dateFrom", null);
            } else {
                session.setAttribute("dateFrom",
                                     GlobalCC.parseDate(valueChangeEvent.getNewValue().toString()));

            }
        }
    }

    public void weekEnding(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (valueChangeEvent.getNewValue() == null) {
                session.setAttribute("weekending", null);
            } else {
                session.setAttribute("weekending",
                                     GlobalCC.parseDate(valueChangeEvent.getNewValue().toString()));


            }
        }
    }


    public void serialNo(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (valueChangeEvent.getNewValue() == null) {
                session.setAttribute("serialNo", null);
            } else {
                session.setAttribute("serialNo",
                                     valueChangeEvent.getNewValue().toString());

            }
        }
    }

    public void rptType(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (valueChangeEvent.getNewValue() == null) {
                session.setAttribute("rptCode", null);
            } else {
                session.setAttribute("rptCode",
                                     valueChangeEvent.getNewValue().toString());

            }
            //System.out.println("Report Value");
            //System.out.println(valueChangeEvent.getNewValue());

        }
    }

    public void finRptVal(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (valueChangeEvent.getNewValue() == null) {
                session.setAttribute("rptCode", null);
            } else {
                RichSelectBooleanRadio myRadio =
                    (RichSelectBooleanRadio)valueChangeEvent.getComponent();
                if (myRadio.isSelected()) {
                    //System.out.println(valueChangeEvent.getComponent().getId());
                    session.setAttribute("rptCode",
                                         valueChangeEvent.getComponent().getId());
                }

            }
        }
    }

    public void CRMReport(ActionEvent actionEvent) {

        String reportId = actionEvent.getComponent().getId();
        reportId = reportId.replaceAll("rpt", "");
        if (reportId.equalsIgnoreCase("0")) {
            if (session.getAttribute("rptCode") == null) {
                GlobalCC.errorValueNotEntered("No Report Selected");
            } else {
                reportId = (String)session.getAttribute("rptCode");
                reportId = reportId.replaceAll("rpt", "");
                reportOne(new BigDecimal(reportId));
            }
        } else {
            reportId = reportId.replace("rpt", "");
            reportOne(new BigDecimal(reportId));
        }


    }

    public void accAllSelec(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (valueChangeEvent.getNewValue() == null) {
                session.setAttribute("allSelAcc", null);
            } else {
                session.setAttribute("allSelAcc",
                                     valueChangeEvent.getNewValue().toString());

            }
        }
    }

    public void consDivs(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue() &&
            valueChangeEvent.getNewValue() != null) {
            //System.out.println("Value");
            if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("false")) {

                session.setAttribute("consDiv", false);
            } else {
                session.setAttribute("consDiv", true);

            }
            //System.out.println( session.getAttribute("consDiv"));
        }
    }

    public void consBranch(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue() &&
            valueChangeEvent.getNewValue() != null) {

            if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("false")) {
                session.setAttribute("consBrh", false);
            } else {
                session.setAttribute("consBrh", true);

            }
        }
    }

    public void consOrg(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue() &&
            valueChangeEvent.getNewValue() != null) {

            if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("false")) {
                session.setAttribute("consOrg", false);
            } else {
                session.setAttribute("consOrg", true);

            }
        }
    }

    public void paramReport(ActionEvent actionEvent) {
        String reportId = actionEvent.getComponent().getId();
        reportId = reportId.replace("rpt", "");
        if (reportId.equalsIgnoreCase("0")) {
            if (session.getAttribute("rptCode") == null) {
                GlobalCC.errorValueNotEntered("No Report Selected");
            } else {
                reportId = (String)session.getAttribute("rptCode");
                reportId = reportId.replace("rpt", "");
                session.setAttribute("rptCode", reportId);
                GlobalCC.showPopUp("crm", "paramReport");
            }
        } else {
            reportId = reportId.replace("rpt", "");
            session.setAttribute("rptCode", reportId);
            GlobalCC.showPopUp("crm", "paramReport");
        }

    }

    public String genRpt() {
        String reportId = (String)session.getAttribute("rptCode");
        reportOne(new BigDecimal(reportId));
        return null;
    }

    public String launchParamRpt() {
        String reportId = rptlink.getId();
        reportId = reportId.replace("rpt", "");
        if (reportId.equalsIgnoreCase("0")) {
            if (session.getAttribute("rptCode") == null) {
                GlobalCC.errorValueNotEntered("No Report Selected");
            } else {
                reportId = (String)session.getAttribute("rptCode");
                reportId = reportId.replace("rpt", "");
                session.setAttribute("rptCode", reportId);
                GlobalCC.showPopUp("crm", "paramReport");

            }
        } else {
            reportId = reportId.replace("rpt", "");
            session.setAttribute("rptCode", reportId);
            GlobalCC.showPopUp("crm", "paramReport");

        }
        return null;
    }


    public void verificationReport(ActionEvent actionEvent) {
        BigDecimal polCode =
            (BigDecimal)session.getAttribute("policyProposalCode");
        if (polCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Policy Code");
        } else {
            String reportId = actionEvent.getComponent().getId();
            reportId = reportId.replace("rpt", "");
            if (reportId.equalsIgnoreCase("0")) {
                if (session.getAttribute("rptCode") == null) {
                    GlobalCC.errorValueNotEntered("No Report Selected");
                } else {
                    reportId = (String)session.getAttribute("rptCode");
                    reportId = reportId.replace("rpt", "");
                    reportOne(new BigDecimal(reportId));
                }
            } else {
                reportId = reportId.replace("rpt", "");
                reportOne(new BigDecimal(reportId));
            }
        }

    }

    public void directPolicyReport(ActionEvent actionEvent) {
        BigDecimal polCode =
            (BigDecimal)session.getAttribute("policyProposalCode");
        if (polCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Policy Code");
        } else {
            String reportId = actionEvent.getComponent().getId();
            reportId = reportId.replace("rpt", "");
            if (reportId.equalsIgnoreCase("0")) {
                if (session.getAttribute("rptCode") == null) {
                    GlobalCC.errorValueNotEntered("No Report Selected");
                } else {
                    reportId = (String)session.getAttribute("rptCode");
                    reportId = reportId.replace("rpt", "");
                    reportOne(new BigDecimal(reportId));
                }
            } else {
                reportId = reportId.replace("rpt", "");
                reportOne(new BigDecimal(reportId));
            }
        }

    }

    public void directEndorsementReport(ActionEvent actionEvent) {
        BigDecimal endorseCode =
            (BigDecimal)session.getAttribute("endorsementCode");
        if (endorseCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Endorsement Code");
        } else {
            String reportId = actionEvent.getComponent().getId();
            reportId = reportId.replace("rpt", "");
            if (reportId.equalsIgnoreCase("0")) {
                if (session.getAttribute("rptCode") == null) {
                    GlobalCC.errorValueNotEntered("No Report Selected");
                } else {
                    reportId = (String)session.getAttribute("rptCode");
                    reportId = reportId.replace("rpt", "");
                    reportOne(new BigDecimal(reportId));
                }
            } else {
                reportId = reportId.replace("rpt", "");
                reportOne(new BigDecimal(reportId));
            }
        }

    }


    public void setRepFormat(RichSelectOneRadio repFormat) {
        this.repFormat = repFormat;
    }

    public RichSelectOneRadio getRepFormat() {
        return repFormat;
    }

    public void setRptBtn(RichCommandButton rptBtn) {
        this.rptBtn = rptBtn;
    }

    public RichCommandButton getRptBtn() {
        return rptBtn;
    }

    public void setRpt1(RichCommandLink rpt1) {
        this.rpt1 = rpt1;
    }

    public RichCommandLink getRpt1() {
        return rpt1;
    }

    public void setRadioBtn(RichSelectBooleanRadio radioBtn) {
        this.radioBtn = radioBtn;
    }

    public RichSelectBooleanRadio getRadioBtn() {
        return radioBtn;
    }

    public void setSendTo(RichSelectOneChoice sendTo) {
        this.sendTo = sendTo;
    }

    public RichSelectOneChoice getSendTo() {
        return sendTo;
    }


    public void setRptlink(RichCommandLink rptlink) {
        this.rptlink = rptlink;
    }

    public RichCommandLink getRptlink() {
        return rptlink;
    }

    //    public String directDebitReports() {
    //       try {
    //              String Id = null;
    //              String ReportID = null;
    //
    //              Id = txtDirectDebit.getId();
    //              ReportID = Id.substring(3);
    //           BigDecimal ReportCode = new BigDecimal(ReportID);
    //            reportOne(ReportCode);
    //          } catch (Exception e) {
    //              GlobalCC.EXCEPTIONREPORTING(e);
    //          }
    //          return null;
    //
    //    }


}
