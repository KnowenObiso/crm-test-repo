package TurnQuest.view.Loading;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import com.Ostermiller.util.CSVParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.sql.CallableStatement;
import java.sql.Connection;

import java.util.Date;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputFile;

import oracle.jdbc.OracleCallableStatement;

import org.apache.commons.dbutils.DbUtils;
import org.apache.myfaces.trinidad.model.UploadedFile;


public class BankAccountsLoad {
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private RichInputFile upFile;
    private UploadedFile uploadedFile;
    private String filename;
    private long filesize;
    private String filetype;
    private RichTable bankAccountsTbl; 


    public BankAccountsLoad() {
    }

    public void setUpFile(RichInputFile upFile) {
        this.upFile = upFile;
    }

    public RichInputFile getUpFile() {
        return upFile;
    }

    public static java.sql.Date extractDate(RichInputDate component) {
        java.sql.Date val = null;
        try {
            val = new java.sql.Date(((Date)component.getValue()).getTime());
        } catch (Exception ex) {
            val = null;
        }
        return val;
    }

    public void fileChangeListenerBankAccounts(ValueChangeEvent valueChangeEvent) {

        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            UploadedFile _file = (UploadedFile)valueChangeEvent.getNewValue();
            this.uploadedFile = _file;
            this.filename = _file.getFilename();
            this.filesize = _file.getLength();
            this.filetype = _file.getContentType();
            try {
                processLoadingBankAccounts(uploadedFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void processLoadingBankAccounts(InputStream csvFile) {
        String[][] csvvalues = null;
        int count = 0;
        java.util.Date dateofLead;
        java.util.Date dateodClose;
        String leadDate = null;
        String closeDate = null;
        DBConnector datahandler = new DBConnector();
        Connection conn;
        conn = datahandler.getDatabaseConnection();
        CallableStatement cst = null;
        try {
            csvvalues = CSVParser.parse(new InputStreamReader(csvFile));
            
            String query =
              "begin  tqc_setups_pkg.bank_details_prc(" +
                  "?,?,?,?,?,?,?,?,?,?," +
                  "?,?,?" + 
              "); end;";

            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);

            for (int i = 1; i < csvvalues.length; i++) {

                if (i == 0) {
                } else {
 
                    callStmt.setString(1, null);
                    callStmt.setString(2, null);
                    callStmt.setObject(3, null);

                    callStmt.setObject(4, csvvalues[i][0]);
                    callStmt.setObject(5, csvvalues[i][1]);
                    callStmt.setObject(6, csvvalues[i][2]);
                    callStmt.setObject(7, csvvalues[i][3]);
                    callStmt.setString(8, csvvalues[i][4]);
                    callStmt.setString(9, csvvalues[i][5]);
                    callStmt.setString(10, csvvalues[i][6]);
                    callStmt.setString(11, csvvalues[i][7]);
                    callStmt.setString(12, csvvalues[i][8]);
                    callStmt.setString(13, csvvalues[i][9]);
                 
                    callStmt.setObject(14, GlobalCC.checkNullValues(session.getAttribute("Username")));
                    callStmt.addBatch();
                }
            }
            callStmt.executeBatch();
            conn.commit();
            callStmt.close();
            conn.close();


            ADFUtils.findIterator("fetchLoadedBankAccountsIterator").executeQuery();
            GlobalCC.refreshUI(bankAccountsTbl);

            GlobalCC.INFORMATIONREPORTING("Data Successfully Saved...");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public UploadedFile getUploadedFile() {
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

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public HttpSession getSession() {
        return session;
    }


    public String exportBankAccounts() {
        // Add event code here...
        return null;
    }

    public void setBankAccountsTbl(RichTable bankAccountsTbl) {
        this.bankAccountsTbl = bankAccountsTbl;
    }

    public RichTable getBankAccountsTbl() {
        return bankAccountsTbl;
    }
}
