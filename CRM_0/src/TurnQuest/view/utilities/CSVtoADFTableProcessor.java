package TurnQuest.view.utilities;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.dao.EcmHelper;
import TurnQuest.view.dao.EcmProps;
import TurnQuest.view.dao.GeneralDAO;

import com.Ostermiller.util.CSVParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.math.BigDecimal;

import java.nio.charset.Charset;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichColumn;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.commons.dbutils.DbUtils;
import org.apache.myfaces.trinidad.model.UploadedFile;

import org.turnkey.cmis.CmisUtil;


public class CSVtoADFTableProcessor {
    private RichTable table;
    private List rows = new ArrayList();
    private boolean useFirstRowAsHeaders = true;
    private int numberOfColumns;
    private RichCommandButton importTable;
    private RichSelectOneChoice dataType;
    private RichPanelBox ddImportPan;
    private RichPanelBox ddMainPan;
    private RichPanelGroupLayout details;
    private UploadedFile uploadedFile;
    private UploadedFile stmtUploadedFile;
    private String filename;
    private long filesize;
    private String filecontents;
    private String filetype;
    private String folder;
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);


    public CSVtoADFTableProcessor() {
    }


    public String myProcedure() {
        table.setRendered(true);
        GlobalCC.refreshUI(table);
        importTable.setRendered(false);
        return null;
    }

    public String clearTable() {
        this.table.getChildren().clear();
        GlobalCC.refreshUI(this.table);
        return null;
    }

    public String newImportDirectDebit() {
        ddImportPan.setRendered(true);
        ddMainPan.setRendered(false);
        GlobalCC.refreshUI(details);
        return null;
    }

    public String exitImportDirectDebit() {
        if (ddImportPan != null) {
            ddImportPan.setRendered(false);
        }
        if (ddMainPan != null) {
            ddMainPan.setRendered(true);
        }
        clearTable();
        if (details != null) {
            GlobalCC.refreshUI(details);
        }
        // GlobalCC.refreshUI(dtls);
        return null;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
        if (uploadedFile != null) {
            this.filename = uploadedFile.getFilename();
            this.filesize = uploadedFile.getLength();
            this.filetype = uploadedFile.getContentType();
            try {
                String fileName = uploadedFile.getFilename();
                int k = fileName.indexOf(".");
                fileName = fileName.substring(k);
                System.out.println(uploadedFile.getFilename());
                if (!fileName.equalsIgnoreCase(".csv")) {
                    GlobalCC.errorValueNotEntered("Error: Wrong File Type. Only .csv Files can be uploaded");
                } else {
                    table.setRendered(true);
                    processCSV(uploadedFile.getInputStream());
                    GlobalCC.refreshUI(table);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            useFirstRowAsHeaders = true;
            File file = null;
            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext sc =
                (ServletContext)context.getExternalContext().getContext();

            String path = sc.getRealPath("default.csv");
            file = new File(path);
            InputStream is = null;
            try {
                is = new FileInputStream(file);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            table.setRendered(true);
            processCSV(is);
            GlobalCC.refreshUI(table);
        }
    }

    public void setStmtUploadedFile(UploadedFile stmtUploadedFile) {
        this.stmtUploadedFile = stmtUploadedFile;
        this.filename = stmtUploadedFile.getFilename();
        this.filesize = stmtUploadedFile.getLength();
        this.filetype = stmtUploadedFile.getContentType();
        try {

            processCSV(stmtUploadedFile.getInputStream());
            GlobalCC.refreshUI(table);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Integer CheckNullExcelValues(String someValue) {
        Integer Results = 0;
        Results = someValue.trim().length();
        return Results;
    }

    public void processCSV(InputStream csvFile) {
        // Parse the data, using http://ostermiller.org/utils/download.html
        String[][] csvvalues = null;
        try {
            csvvalues = CSVParser.parse(new InputStreamReader(csvFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        rows = new ArrayList();

        numberOfColumns = 0;
        for (int i = 0; i < csvvalues.length; i++) {
            Map tablerow = new HashMap();
            if ((csvvalues[i][0]).equals("") || csvvalues[i][0] == null)
                continue;
            for (int j = 0; j < csvvalues[i].length; j++) {
                if (j > numberOfColumns)

                    numberOfColumns = j;
                tablerow.put("cell" + (j + 1), csvvalues[i][j]);
                //System.out.println("This is row data"+csvvalues[i][0]);

            } // for cells

            rows.add(tablerow);
        } // for rows


        setupTableColumns();

    }

    private void setupTableColumns() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();
        table.getChildren().clear();
        RichColumn col =
            (RichColumn)app.createComponent(RichColumn.COMPONENT_TYPE);
        RichOutputText cell =
            (RichOutputText)app.createComponent(RichOutputText.COMPONENT_TYPE);
        col.setId("rowheader");

        cell.setId("rowcell");
        cell.setValueBinding("value",
                             app.createValueBinding("#{rowStatus.index}"));

        col.getChildren().add(cell);
        col.setHeaderText("^");
        col.setRendered(false);
        table.getChildren().add(col);
        Map columnHeaders = new HashMap();
        columnHeaders = getColumnHeaders();

        for (int i = 0; i < numberOfColumns + 1; i++) {
            col = (RichColumn)app.createComponent(RichColumn.COMPONENT_TYPE);
            col.setId("col" + i);
            cell =
(RichOutputText)app.createComponent(RichOutputText.COMPONENT_TYPE);
            cell.setId("cell" + i);
            cell.setValueBinding("value",
                                 app.createValueBinding("#{row['cell" +
                                                        (i + 1) + "']}"));

            col.getChildren().add(cell);
            col.setSortable(true);
            col.setSortProperty("cell" + (i + 1));
            col.setHeaderText((String)((Map)rows.get(0)).get("cell" +
                                                             (i + 1)));
            table.getChildren().add(col);
        }
    }


    public List getRows() {
        if (rows.size() == 0) {
            useFirstRowAsHeaders = true;
            File file = null;
            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext sc =
                (ServletContext)context.getExternalContext().getContext();

            String path = sc.getRealPath("default.csv");
            file = new File(path);
            InputStream is = null;
            try {
                is = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(null, e);
            }
            processCSV(is);

        }
        return rows.subList(useFirstRowAsHeaders ? 1 : 0, rows.size());

    }


    public Map getColumnHeaders() {
        Map columnHeaders = new HashMap();
        if (useFirstRowAsHeaders) {
            for (int i = 0; i < numberOfColumns + 1; i++) {
                columnHeaders.put(Integer.toString(i),
                                  ((Map)rows.get(0)).get("cell" + (i + 1)));
            }


        } else
            for (int i = 0; i < numberOfColumns + 1; i++)
                columnHeaders.put(Integer.toString(i),
                                  String.valueOf((char)('A' + i)));
        return columnHeaders;
    }


    public String importDirectDebit() {
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        conn = datahandler.getDatabaseConnection();
        String errMessage = null;
        int rowCount = rows.size();
        int k = 1;
        try {
            conn.setAutoCommit(false);
            String query =
                "begin TQC_SETUPS_PKG.failUpdateDDHeader2(?,?,?,?,?,?); end;";
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            String usernameVal = (String)session.getAttribute("Username");
            String ddhCodeVal = null;
            String bookDateVal = null;
            String failRemarks = null;
            String ddhDdCodeVal = null;
            String statusVal = null;
            if (session.getAttribute("ddBookDate") == null) {
                GlobalCC.errorValueNotEntered("Select Receipt Batch to proceed");
                return null;
            }
            bookDateVal = session.getAttribute("ddBookDate").toString();
            if (bookDateVal != null) {
                if (bookDateVal.contains(":")) {
                    bookDateVal = GlobalCC.parseDate(bookDateVal);
                } else {
                    bookDateVal = GlobalCC.upDateParseDate(bookDateVal);
                }

            }
            while (k < rowCount) {
                int i = 0;
                while (i < numberOfColumns + 1) {

                    switch (i) {
                    case 0:
                        if (((Map)rows.get(k)).get("cell" + (i + 1)) != null) {
                            ddhCodeVal =
                                    ((Map)rows.get(k)).get("cell" + (i + 1)).toString();
                        }

                        break;
                    case 1:


                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    case 5:

                        break;
                    case 6:

                    case 7:
                        if (((Map)rows.get(k)).get("cell" + (i + 13)) !=
                            null) {
                            statusVal =
                                    ((Map)rows.get(k)).get("cell" + (i + 13)).toString();
                        }
                    case 8:
                        if (((Map)rows.get(k)).get("cell" + (i + 1)) != null) {
                            failRemarks =
                                    ((Map)rows.get(k)).get("cell" + (i + 1)).toString();
                        }

                    default:
                        break;


                    }
                    i++;
                }

                callStmt.setString(1, usernameVal);
                callStmt.setString(2, ddhCodeVal);
                callStmt.setString(3, bookDateVal);
                callStmt.setString(4, failRemarks);
                callStmt.setString(5, ddhDdCodeVal);
                callStmt.setString(6, statusVal);
                callStmt.execute();
                k++;
            }

            callStmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("fetchDirectDebitHeaders2Iterator").executeQuery();
            ADFUtils.findIterator("fetchDirectDebitAuthorisedIterator").executeQuery();
            ADFUtils.findIterator("fetchDirectDebitDetails2Iterator").executeQuery();
            GlobalCC.sysInformation("Import Successful");
            exitImportDirectDebit();
            clearTable();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }

        return null;
    }

    public String importUpdateFail() {
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        conn = datahandler.getDatabaseConnection();
        int rowCount = rows.size();
        int k = 1;
        try {
            conn.setAutoCommit(false);
            /*
           * DD Code	POLICY NO	ACCOUNT NO	SORT CODE	PAY FREQUENCY	NEXT DUE DATE	AMOUNT	ACCOUNT NAME	NARRATION	COMPANY	FAIL (F/A)
           */
            String launchType = (String)session.getAttribute("launchType");
            String query = "";

            if (launchType.equalsIgnoreCase("ImportRelaunch")) {
                query =
                        "begin TQC_SETUPS_PKG.failRelaunchUpdateHeader(?,?,?,?,?,?,?,?,?); end;";
            } else {
                query =
                        "begin TQC_SETUPS_PKG.failUpdateHeader(?,?,?,?,?,?,?,?,?); end;";
            }

            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            String usernameVal = (String)session.getAttribute("Username");
            String clientVal = null;
            String polVal = null;
            String bnkAmtVal = null;
            String accountVal = null;
            String dddCode = null;
            String bookDateVal = null;
            String failRemarks = null;
            BigDecimal ddhDdCodeVal = GlobalCC.checkBDNullValues(session.getAttribute("ddCode"));
            String statusVal = null;
            if (session.getAttribute("ddBookDate") == null) {
                GlobalCC.errorValueNotEntered("Select Receipt Batch to proceed");
                return null;
            }
            bookDateVal = session.getAttribute("ddBookDate").toString();
            if (bookDateVal != null) {
                if (bookDateVal.contains(":")) {
                    bookDateVal = GlobalCC.parseDate(bookDateVal);
                } else {
                    bookDateVal = GlobalCC.upDateParseDate(bookDateVal);
                } 
            }
            
            System.out.println("numberOfColumns: "+numberOfColumns+" rowCount: "+rowCount);
            while (k < rowCount) {
                int i = 0;
                while (i < numberOfColumns + 1) {

                    switch (i) {
                    case 0:
                        if (((Map)rows.get(k)).get("cell" + (i + 1)) != null) {
                            dddCode =
                                    ((Map)rows.get(k)).get("cell" + (i + 7)).toString();
                            statusVal =
                                    ((Map)rows.get(k)).get("cell" + (i + 12)).toString();
                            failRemarks =
                                    ((Map)rows.get(k)).get("cell" + (i + 13)).toString();
                            bnkAmtVal =
                                    ((Map)rows.get(k)).get("cell" + (i + 3)).toString();
                        }
                        break;
                    case 1:
                        if (((Map)rows.get(k)).get("cell" + (i + 1)) != null) {
                            polVal =
                                    ((Map)rows.get(k)).get("cell" + (i + 1)).toString();
                        }

                        break;
                    case 2:
                        if (((Map)rows.get(k)).get("cell" + (i + 1)) != null) {
                            accountVal =
                                    ((Map)rows.get(k)).get("cell" + (i + 1)).toString();
                        }


                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    case 5:
                        break;


                    case 6:

                        break;
                    case 7:

                        break;
                    case 8:


                        break;
                    case 9:

                        break;
                    case 10:
                        if (((Map)rows.get(k)).get("cell" + (i + 12)) !=
                            null) {
                            statusVal =
                                    ((Map)rows.get(k)).get("cell" + (i + 12)).toString();
                            System.out.println("statusVal: "+statusVal);
                        }
                        break;
                    case 11:
                        if (((Map)rows.get(k)).get("cell" + (i + 13)) !=
                            null) {
                            failRemarks =
                                    ((Map)rows.get(k)).get("cell" + (i + 13)).toString();
                            System.out.println("failRemarks: "+failRemarks);
                        }
                        break;

                    default:
                        break;


                    }
                    i++;
                }

                System.out.println("statusVal" + statusVal);

                if (statusVal == null) {
                    statusVal = "A";
                }
                if (statusVal != null) {
                    //if(statusVal.equalsIgnoreCase("F")){

                    if (launchType.equalsIgnoreCase("ImportRelaunch")) {
                        callStmt.setString(1, dddCode);
                        callStmt.setString(2, usernameVal);
                        callStmt.setString(3, polVal);
                        callStmt.setString(4, accountVal);
                        callStmt.setString(5, bookDateVal);
                        callStmt.setString(6, failRemarks);
                        callStmt.setBigDecimal(7, ddhDdCodeVal);
                        callStmt.setString(8, statusVal);
                        callStmt.setString(9, bnkAmtVal);
                    } else {
                        callStmt.setString(1, dddCode);
                        callStmt.setString(2, usernameVal);
                        callStmt.setString(3, polVal);
                        callStmt.setString(4, accountVal);
                        callStmt.setString(5, bookDateVal);
                        callStmt.setString(6, failRemarks);
                        callStmt.setBigDecimal(7, ddhDdCodeVal);
                        callStmt.setString(8, statusVal);
                        callStmt.setString(9, bnkAmtVal);
                    }

                    System.out.println("dddCode" + dddCode);
                    System.out.println("usernameVal" + usernameVal);
                    System.out.println("accountVal" + accountVal);
                    System.out.println("bookDateVal" + bookDateVal);
                    System.out.println("failRemarks" + failRemarks);
                    System.out.println("ddhDdCodeVal" + ddhDdCodeVal);
                    System.out.println("statusVal" + statusVal);
                    System.out.println("polVal" + polVal);

                    callStmt.execute();
                    //  }
                }
                k++;
            }

            callStmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("fetchDirectDebitHeaders2Iterator").executeQuery();
            ADFUtils.findIterator("fetchDirectDebitAuthorisedIterator").executeQuery();
            ADFUtils.findIterator("fetchDirectDebitDetails2Iterator").executeQuery();
            GlobalCC.sysInformation("Import Successful");
            exitImportDirectDebit();
            clearTable();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }

        return null;
    }


    public void setUseFirstRowAsHeaders(boolean useFirstRowAsHeaders) {
        this.useFirstRowAsHeaders = true;
    }

    public boolean isUseFirstRowAsHeaders() {
        return true;
    }


    public void setImportTable(RichCommandButton importTable) {
        this.importTable = importTable;
    }

    public RichCommandButton getImportTable() {
        return importTable;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public UploadedFile getStmtUploadedFile() {
        return uploadedFile;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilecontents(String filecontents) {
        this.filecontents = filecontents;
    }

    public String getFilecontents() {
        return filecontents;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setTable(RichTable table) {
        this.table = table;
    }

    public RichTable getTable() {
        return table;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }


    public void setDataType(RichSelectOneChoice dataType) {
        this.dataType = dataType;
    }

    public RichSelectOneChoice getDataType() {
        return dataType;
    }


    public void setDdImportPan(RichPanelBox ddImportPan) {
        this.ddImportPan = ddImportPan;
    }

    public RichPanelBox getDdImportPan() {
        return ddImportPan;
    }

    public void setDdMainPan(RichPanelBox ddMainPan) {
        this.ddMainPan = ddMainPan;
    }

    public RichPanelBox getDdMainPan() {
        return ddMainPan;
    }

    public void setDetails(RichPanelGroupLayout details) {
        this.details = details;
    }

    public RichPanelGroupLayout getDetails() {
        return details;
    }


    public void uploadClientDocuments(InputStream inputStream, String filename,
                                      String mimeType) {
        String file = "/Reports/" + filename;


        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext sc =
                (ServletContext)context.getExternalContext().getContext();
            file = sc.getRealPath(file);
            File toUpload = new File(file);
            OutputStream out;

            out = new FileOutputStream(toUpload);
            int read = 0;
            byte[] bytes = new byte[inputStream.available()];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            System.out.println("bytes: "+new String(bytes, Charset.forName("ISO-8859-1")));
            
            inputStream.close();
            out.flush();
            out.close();
            if (toUpload == null) {
                GlobalCC.INFORMATIONREPORTING("No File Uploaded....");
                return;
            }
            
           
            
            //Magic parser = new Magic() ;
            // MagicMatch match = parser.getMagicMatch(getBytesFromFile(new File(file)));
            String mtype = "text/plain";
            HttpSession sessions =
                (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            
            String entityId = GlobalCC.checkNullValues(sessions.getAttribute("ClientCode"));
            String entityName = GlobalCC.checkNullValues(sessions.getAttribute("clientName"));
            
            Session session = getCmisSession();
            if (session == null || getFolder() == null) {
                return;
            }
            String fold = getFolder();
            boolean folderExist =
                CmisUtil.checkIfDirectoryExists(session, "/" + fold);
            if (!folderExist)
                CmisUtil.createFolderInRootFolder(session, fold);
            CmisObject object = session.getObjectByPath("/" + fold);
            Folder gisFolder = (Folder)object;
            boolean uwFolderExist =
                CmisUtil.checkIfDirectoryExists(session, "/" + fold + "/" +
                                                "CLIENTS");
            if (!uwFolderExist)
                CmisUtil.createFolder(session, gisFolder, "CLIENTS");
            Folder uwFolder =
                (Folder)session.getObjectByPath("/" + fold + "/" + "CLIENTS");
            
            entityId = entityId.replace("/", "_");
            boolean entityFolderExist =
                CmisUtil.checkIfDirectoryExists(session,
                                                "/" + fold + "/" + "CLIENTS/" +
                                                entityId);
            Folder entityFolder = null;  
            if (!entityFolderExist)
                entityFolder =
                        CmisUtil.createFolder(session, uwFolder, entityId);
            else
                entityFolder =
                        (Folder)session.getObjectByPath("/" + fold + "/" +
                                                        "CLIENTS/" + entityId);
            String docType = "D:tqgib:CRMClientDocs";
            List<EcmProps> properties = new ArrayList<EcmProps>();
            properties.add(new EcmProps("tqgib:ClientID",
                                        entityId));
            properties.add(new EcmProps("tqgib:clientName",
                                        entityName));
            String apsects = docType + ",P:tqgib:clientsDocs";

            String docId=null; 
            docId =  new EcmHelper().UploadFile(session, entityFolder, filename,
                                       toUpload, mimeType, apsects,
                                       properties);
            
            sessions.setAttribute("clientDocId",docId);
            sessions.setAttribute("clientDocSubmitted","Y");
            
            ADFUtils.findIterator("enquireDocsIterator").executeQuery();
            new GeneralDAO().getEnquireDocs();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
            e.printStackTrace();

        }
    }
    public void uploadAgentDocuments(InputStream inputStream, String filename,
                                      String mimeType) {
        String file = "/Reports/" + filename;


        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext sc =
                (ServletContext)context.getExternalContext().getContext();
            file = sc.getRealPath(file);
            File toUpload = new File(file);
            OutputStream out;

            out = new FileOutputStream(toUpload);
            int read = 0;
            byte[] bytes = new byte[inputStream.available()];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            System.out.println("bytes: "+new String(bytes, Charset.forName("ISO-8859-1")));
            
            inputStream.close();
            out.flush();
            out.close();
            if (toUpload == null) {
                GlobalCC.INFORMATIONREPORTING("No File Uploaded....");
                return;
            }
            //Magic parser = new Magic() ;
            // MagicMatch match = parser.getMagicMatch(getBytesFromFile(new File(file)));
            String mtype = "text/plain";
            HttpSession sessions =
                (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            
            String entityId = GlobalCC.checkNullValues(sessions.getAttribute("agencyCode"));
            String entityName = GlobalCC.checkNullValues(sessions.getAttribute("agencyName"));
            
            
            
            Session session = getCmisSession();
            if (session == null || getFolder() == null) {
                return;
            }
            String fold = getFolder();
            boolean folderExist =
                CmisUtil.checkIfDirectoryExists(session, "/" + fold);
            if (!folderExist)
                CmisUtil.createFolderInRootFolder(session, fold);
            CmisObject object = session.getObjectByPath("/" + fold);
            Folder gisFolder = (Folder)object;
            boolean uwFolderExist =
                CmisUtil.checkIfDirectoryExists(session, "/" + fold + "/" +
                                                "AGENTS");
            if (!uwFolderExist)
                CmisUtil.createFolder(session, gisFolder, "AGENTS");
            Folder uwFolder =
                (Folder)session.getObjectByPath("/" + fold + "/" + "AGENTS");
            Folder entityFolder = null;
            
            entityId = entityId.replace("/", "_");
            boolean entityFolderExist =
                CmisUtil.checkIfDirectoryExists(session,
                                                "/" + fold + "/" + "AGENTS/" +
                                                entityId);

            if (!entityFolderExist)
                entityFolder =
                        CmisUtil.createFolder(session, uwFolder, entityId);
            else
                entityFolder =
                        (Folder)session.getObjectByPath("/" + fold + "/" +
                                                        "AGENTS/" + entityId);
            String docType = "D:tqgib:CRMAgentDocs";
            List<EcmProps> properties = new ArrayList<EcmProps>();
            properties.add(new EcmProps("tqgib:AgentCode",entityId));
            properties.add(new EcmProps("tqgib:AgentName",entityName));
            String apsects = docType + ",P:tqgib:agentsDocs";

            String docId=null; 
            docId = new EcmHelper().UploadFile(session, entityFolder, filename,
                                       toUpload, mimeType, apsects,
                                       properties);
            
            sessions.setAttribute("agentDocId",docId);
            sessions.setAttribute("agentDocSubmitted","Y");
           // ADFUtils.findIterator("enquireDocsIterator").executeQuery();
           new GeneralDAO().getAgentDocs();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
            e.printStackTrace();

        }
    }
    public void uploadSpDocuments(InputStream inputStream, String filename,
                                      String mimeType) {
        String file = "/Reports/" + filename;


        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext sc =
                (ServletContext)context.getExternalContext().getContext();
            file = sc.getRealPath(file);
            File toUpload = new File(file);
            OutputStream out;

            out = new FileOutputStream(toUpload);
            int read = 0;
            byte[] bytes = new byte[inputStream.available()];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            System.out.println("bytes: "+new String(bytes, Charset.forName("ISO-8859-1")));
            
            inputStream.close();
            out.flush();
            out.close();
            if (toUpload == null) {
                GlobalCC.INFORMATIONREPORTING("No File Uploaded....");
                return;
            }
            //Magic parser = new Magic() ;
            // MagicMatch match = parser.getMagicMatch(getBytesFromFile(new File(file)));
            String mtype = "text/plain";
            HttpSession sessions =
                (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            
            String entityId = GlobalCC.checkNullValues(sessions.getAttribute("serviceProviderCode"));
            String entityName = GlobalCC.checkNullValues(sessions.getAttribute("serviceProviderName"));
            
            
            
            Session session = getCmisSession();
            if (session == null || getFolder() == null) {
                return;
            }
            String fold = getFolder();
            boolean folderExist =
                CmisUtil.checkIfDirectoryExists(session, "/" + fold);
            if (!folderExist)
                CmisUtil.createFolderInRootFolder(session, fold);
            CmisObject object = session.getObjectByPath("/" + fold);
            Folder gisFolder = (Folder)object;
            boolean uwFolderExist =
                CmisUtil.checkIfDirectoryExists(session, "/" + fold + "/" +
                                                "SERVICE_PROVIDERS");
            if (!uwFolderExist)
                CmisUtil.createFolder(session, gisFolder, "SERVICE_PROVIDERS");
            Folder uwFolder =
                (Folder)session.getObjectByPath("/" + fold + "/" + "SERVICE_PROVIDERS");
            Folder entityFolder = null;
            
            entityId = entityId.replace("/", "_");
            boolean entityFolderExist =
                CmisUtil.checkIfDirectoryExists(session,
                                                "/" + fold + "/" + "SERVICE_PROVIDERS/" +
                                                entityId);

            if (!entityFolderExist)
                entityFolder =
                        CmisUtil.createFolder(session, uwFolder, entityId);
            else
                entityFolder =
                        (Folder)session.getObjectByPath("/" + fold + "/" +
                                                        "SERVICE_PROVIDERS/" + entityId);
            String docType = "D:tqgib:CRMSPDocs";
            List<EcmProps> properties = new ArrayList<EcmProps>();
            properties.add(new EcmProps("tqgib:SPCode",entityId));
            properties.add(new EcmProps("tqgib:SPName",entityName));
            String apsects = docType + ",P:tqgib:SPsDocs";

            String docId=null; 
            docId = new EcmHelper().UploadFile(session, entityFolder, filename,
                                       toUpload, mimeType, apsects,
                                       properties);
            
            sessions.setAttribute("spDocId",docId);
            sessions.setAttribute("spDocSubmitted","Y");
            //ADFUtils.findIterator("enquireDocsIterator").executeQuery();
            new GeneralDAO().getSPDocs();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
            e.printStackTrace();

        }
    }
    public void uploadServReqDocuments(InputStream inputStream, String filename,
                                      String mimeType) {
        String file = "/Reports/" + filename;


        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext sc =
                (ServletContext)context.getExternalContext().getContext();
            file = sc.getRealPath(file);
            File toUpload = new File(file);
            OutputStream out;

            out = new FileOutputStream(toUpload);
            int read = 0;
            byte[] bytes = new byte[inputStream.available()];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            inputStream.close();
            out.flush();
            out.close();
            if (toUpload == null) {
                GlobalCC.INFORMATIONREPORTING("No File Uploaded....");
                return;
            }
            //Magic parser = new Magic() ;
            // MagicMatch match = parser.getMagicMatch(getBytesFromFile(new File(file)));
            String mtype = "text/plain";
            HttpSession sessions =
                (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            Session session = getCmisSession();
            if (session == null || getFolder() == null) {
                return;
            }
            String fold = getFolder();
            boolean folderExist =
                CmisUtil.checkIfDirectoryExists(session, "/" + fold);
            if (!folderExist)
                CmisUtil.createFolderInRootFolder(session, fold);
            CmisObject object = session.getObjectByPath("/" + fold);
            Folder gisFolder = (Folder)object;
            boolean uwFolderExist =
                CmisUtil.checkIfDirectoryExists(session, "/" + fold + "/" +
                                                "SERV_REQ");
            if (!uwFolderExist)
                CmisUtil.createFolder(session, gisFolder, "SERV_REQ");
            Folder uwFolder =
                (Folder)session.getObjectByPath("/" + fold + "/" + "SERV_REQ");
            Folder policyFolder = null;
            Folder endorsementFolder = null;
            String servReqRef = (String)sessions.getAttribute("reqRefNo");
            servReqRef = servReqRef.replace("/", "_");
            boolean policyFolderExist =
                CmisUtil.checkIfDirectoryExists(session,
                                                "/" + fold + "/" + "SERV_REQ/" +
                                                servReqRef);

            if (!policyFolderExist)
                policyFolder =
                        CmisUtil.createFolder(session, uwFolder, servReqRef);
            else
                policyFolder =
                        (Folder)session.getObjectByPath("/" + fold + "/" +
                                                        "SERV_REQ/" + servReqRef);
            String docType = "D:tqgib:ServiceReqDocs";
            List<EcmProps> properties = new ArrayList<EcmProps>();
            properties.add(new EcmProps("tqgib:serviceRefNo",
                                        sessions.getAttribute("reqRefNo").toString()));
            String apsects = docType + ",P:tqgib:serviceRequestDocs";

            new EcmHelper().UploadFile(session, policyFolder, filename,
                                       toUpload, mimeType, apsects,
                                       properties);
            ADFUtils.findIterator("serviceRequestDocsIterator").executeQuery();
          new GeneralDAO().getServiceRequestDocs();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
            e.printStackTrace();

        }
    }

    public Session getCmisSession() {
        Session session = null;
        Connection conn = null;
        String url = "";
        String user = "";
        String pass = "";
        DBConnector datahandler = new DBConnector();
        conn = datahandler.getDatabaseConnection();
        CallableStatement cstSections = null;
        ResultSet sectionsRS = null;

        try {
            cstSections =
                    conn.prepareCall("begin ? := TQC_SETUPS_CURSOR.get_system_ecm_setups(?); end;");
            cstSections.registerOutParameter(1,
                                             oracle.jdbc.OracleTypes.CURSOR);
            cstSections.setInt(2, 27);
            cstSections.execute();
            sectionsRS = (ResultSet)cstSections.getObject(1);
            if (sectionsRS.next()) {
                url = sectionsRS.getString(2);
                user = sectionsRS.getString(3);
                pass = sectionsRS.getString(4);
                setFolder(sectionsRS.getString(6));
            }
            if (url.endsWith("/")) {
                url = url + "cmisatom";
            } else
                url = url + "/cmisatom";
            DbUtils.closeQuietly(conn, cstSections, sectionsRS);
            System.out.println(url);
            Map<String, String> params = new HashMap<String, String>();
            params.put(SessionParameter.USER, user);
            params.put(SessionParameter.PASSWORD, pass);
            params.put(SessionParameter.ATOMPUB_URL, url);
            params.put(SessionParameter.BINDING_TYPE,
                       BindingType.ATOMPUB.value());
            params.put(SessionParameter.OBJECT_FACTORY_CLASS,
                       "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");
            params.put(SessionParameter.CONNECT_TIMEOUT, "60000000");
            params.put(SessionParameter.READ_TIMEOUT, "60000000");
            // params.put(SessionParameter.REPOSITORY_ID, "51a30c2b-bba0-4f3d-8038-c3899b37439c");
            SessionFactory factory = SessionFactoryImpl.newInstance();
            session = factory.getRepositories(params).get(0).createSession();
            ;
            session.getDefaultContext().setCacheEnabled(false);

        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof SQLException)
                GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cstSections, sectionsRS);
        }
        return session;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }
    public String generateDoc(String id) {

           /*String[] ids = id.split(";");
           String finalId = "";
           if (ids.length > 1) {
               finalId = ids[0];
           } else
               finalId = id;

           if (!new Rendering().isAnnotatedDoc()) {
               try {
                   ServletContext servletContext =
                       (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();

                   HttpServletRequest request =
                       (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
                   String urlss =
                       request.getScheme() + "://" + request.getServerName() +
                       ":" + request.getServerPort() +
                       servletContext.getContextPath();
                   System.out.println("Url " + urlss);
                   String finalUrl = urlss + "/viewer.html?"; 
                   
                   CmisObject object = new CSVtoADFTableProcessor.getCmisSession().getObject(finalId);
                   Document document = (Document)object;
                   String filename = document.getName();
                   InputStream inputStream =
                       document.getContentStream().getStream();
                   String file = "/Reports/" + filename;
                   String filerpt = "/Reports/" + filename;

                   FacesContext context = FacesContext.getCurrentInstance();
                   ServletContext sc =
                       (ServletContext)context.getExternalContext().getContext();
                   file = sc.getRealPath(file);
                   File toUpload = new File(file);
                   OutputStream out;

                   out = new FileOutputStream(toUpload);
                   int read = 0;
                   byte[] bytes = new byte[inputStream.available()];

                   while ((read = inputStream.read(bytes)) != -1) {
                       out.write(bytes, 0, read);
                   }

                   inputStream.close();
                   out.flush();
                   out.close();
                   finalUrl = finalUrl + "file=" + urlss + filerpt;
                   session.setAttribute("toPrint", finalUrl);
                   System.out.println(finalUrl);

                   ExtendedRenderKitService erkService =
                       Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                          ExtendedRenderKitService.class);
                   erkService.addScript(FacesContext.getCurrentInstance(),
                                        "var hints = {autodismissNever:false}; " +
                                        "AdfPage.PAGE.findComponent('" +
                                        "generalTemplate:p201" +
                                        "').show(hints);");

               } catch (Exception e) {

               }

           } else {

               Connection conn = null;
               String url = "";
               String user = "";
               String pass = "";
               DBConnector datahandler = DBConnector.getInstance();
               conn = datahandler.getDatabaseConnection();
               CallableStatement cstSections = null;
               ResultSet sectionsRS = null;
               try {
                   cstSections =
                           conn.prepareCall("begin ? := TQC_SETUPS_CURSOR.get_system_ecm_setups(?); end;");
                   cstSections.registerOutParameter(1,
                                                    oracle.jdbc.OracleTypes.CURSOR);
                   cstSections.setInt(2, 37);
                   cstSections.execute();
                   sectionsRS = (ResultSet)cstSections.getObject(1);
                   if (sectionsRS.next()) {
                       url = sectionsRS.getString(2);
                       user = sectionsRS.getString(3);
                       pass = sectionsRS.getString(4);

                   }
                   String[] urls = url.split("/");
                   String toPrint =
                       urls[0] + "//" + urls[2] + "/" + "/OpenAnnotate/login/unencrypted.htm?username=" +
                       user + "&password=" + pass +
                       "&docbase=Company Home&docId=" + finalId;
                   System.out.println(toPrint);
                   session.setAttribute("toPrint", toPrint);
                   ExtendedRenderKitService erkService =
                       Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                          ExtendedRenderKitService.class);
                   erkService.addScript(FacesContext.getCurrentInstance(),
                                        "var hints = {autodismissNever:false}; " +
                                        "AdfPage.PAGE.findComponent('" +
                                        "generalTemplate:p201" +
                                        "').show(hints);");
               } catch (SQLException e) {
                   e.printStackTrace();
               } finally {
                   DbUtils.closeQuietly(conn, cstSections, sectionsRS);
               }
           }
          */
           return null;
       }
}
