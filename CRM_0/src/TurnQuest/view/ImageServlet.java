package TurnQuest.view;


import TurnQuest.view.Base.GlobalCC;

import java.io.IOException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

import org.apache.commons.dbutils.DbUtils;


public class ImageServlet extends HttpServlet {
    private static final String CONTENT_TYPE =
        "image/gif; charset=windows-1252";

    private native void disposeNative();

    public void dispose() {
        disposeNative();
    }

    protected void finalize() {
        dispose();
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException,
                                                           IOException {
        String ClientCode = null;
        String TransType = null;
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        try {
            ClientCode = request.getParameter("clientCode");

            if (ClientCode == null) {
                TransType = "R";
            } else {
                TransType = "C";
            }

            response.setContentType(CONTENT_TYPE);

            String connectionString = null;
            Context initCtx = null;
            initCtx = new InitialContext();
            Context envCtx = (Context)initCtx.lookup("java:comp/env");
            connectionString = (String)envCtx.lookup("conn");
            DataSource ds = (DataSource)envCtx.lookup(connectionString);
            conn = ds.getConnection();
            String hrQuery = null;

            hrQuery = "begin ? := TQC_CLIENTS_PKG.get_images(?,?); end;";
            callStmt = conn.prepareCall(hrQuery);
            //bind the variables
            callStmt.registerOutParameter(1,
                                          OracleTypes.CURSOR); //authorization code
            callStmt.setString(2, TransType);
            callStmt.setString(3, ClientCode);

            callStmt.execute();
            rs = (ResultSet)callStmt.getObject(1);

            while (rs.next()) {
                byte barray[] = rs.getBytes(1);
                response.setContentType("image/gif");
                response.setContentLength(barray.length);
                response.getOutputStream().write(barray);

            }
            response.getOutputStream().flush();
            response.getOutputStream().close();

            rs.close();
            callStmt.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, callStmt, rs);
        }
    }


    @Override
    protected void doPost(HttpServletRequest httpServletRequest,
                          HttpServletResponse httpServletResponse) throws ServletException,
                                                                          IOException {
        super.doGet(httpServletRequest, httpServletResponse);
    }
}
