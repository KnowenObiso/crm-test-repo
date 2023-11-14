package TurnQuest.view.Alerts;


import TurnQuest.view.Base.GlobalCC;

import java.io.File;

import java.math.BigDecimal;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;


//import TurnQuest.view.Documents.AlfrescoHelper;


public class ReportAlert {

    public ReportAlert() {
        super();
    }


    public File SaveReportOne(BigDecimal rptCode, String realPath) {
        AlertXmlPub xmlPublisher = new AlertXmlPub();

        String templateFile = null;
        String styleFile = null;
        String reportName = null;
        String dataFile = null;
        byte[] bytes = null;
        File file = null;
        String reportFormat = null;
        reportFormat = "pdf";
        SystemAlerts dbConnector = new SystemAlerts();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;

        String jobquery = "begin ? := tqc_web_cursor.getRptDetails(?); end;";

        try {
            conn = dbConnector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(jobquery);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2, rptCode);
            cst.executeQuery();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            System.out.println(" JOSE " + rptCode);
            while (rs.next()) {
                templateFile = rs.getString(5);
                styleFile = rs.getString(7);
                reportName = rs.getString(2);
                dataFile = rs.getString(3);
            }
            xmlPublisher.RealDataEngine(reportName, dataFile, conn, realPath);

            file =
xmlPublisher.RealFileProcessorEngine(reportFormat, templateFile, styleFile,
                                     reportName, realPath);

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }

        return file;
    }


}
