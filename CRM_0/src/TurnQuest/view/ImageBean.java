package TurnQuest.view;


import java.io.File;
import java.io.FileOutputStream;

import javax.faces.context.FacesContext;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.ServletContext;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;

import weblogic.jdbc.common.internal.RmiDataSource;


public class ImageBean {
    public static File file;

    public ImageBean() {
        super();

    }


    public void setFile(File file) {
        ImageBean.file = file;
    }

    public File getFile() {
        OracleConnection conn = null;
        File myfile = null;
        try {


            String strBarCodeImage = "/images/compLogo.gif";
            FacesContext context = FacesContext.getCurrentInstance();

            ServletContext sc =
                (ServletContext)context.getExternalContext().getContext();
            strBarCodeImage = sc.getRealPath(strBarCodeImage);
            myfile = new File(strBarCodeImage);
            System.out.println(myfile.length());
            if (myfile.length() == 0) {

                String connectionString = null;
                Context initCtx = new InitialContext();
                Context envCtx = (Context)initCtx.lookup("java:comp/env");
                connectionString = (String)envCtx.lookup("conn");
                RmiDataSource ds =
                    (RmiDataSource)envCtx.lookup(connectionString);

                conn = (OracleConnection)ds.getConnection();
                OracleCallableStatement callStmt = null;

                String hrQuery = null;

                hrQuery =
                        "SELECT ORG_GRP_LOGO FROM TQC_ORGANIZATIONS WHERE ORG_CODE= 2";
                callStmt = (OracleCallableStatement)conn.prepareCall(hrQuery);
                //bind the variables

                OracleResultSet rs = (OracleResultSet)callStmt.executeQuery();

                while (rs.next()) {
                    byte barray[] = rs.getBytes(1);
                    if (barray != null) {
                        FileOutputStream out =
                            new FileOutputStream(strBarCodeImage);
                        out.write(barray);
                        out.close();
                    }
                }
                rs.close();

                callStmt.close();
                conn.close();
            }


        } catch (Exception e) {
e.printStackTrace();
        }

        this.file = myfile;
        return file;
    }


}
