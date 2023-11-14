package TurnQuest.view.servlets;


import TurnQuest.view.Base.GlobalCC;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.sql.DataSource;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;


public class ClientLOV extends HttpServlet {
    private static final String CONTENT_TYPE =
        "text/html; charset=windows-1252";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) {
        OracleConnection conn = null;
        String imageId = request.getParameter("id");
        imageId = imageId.trim();
        System.out.println("Image");
        System.out.println(imageId);
        if (imageId != null && imageId.trim().length() > 0) {
            try {
                String connectionString = null;
                response.setContentType(CONTENT_TYPE);

                System.out.println("ImageId is " + imageId);

                Context initCtx = new InitialContext();
                Context envCtx = (Context)initCtx.lookup("java:comp/env");
                connectionString = (String)envCtx.lookup("conn");
                DataSource ds = (DataSource)envCtx.lookup(connectionString);

                conn = (OracleConnection)ds.getConnection();

                OraclePreparedStatement statement = null;
                String hrQuery = null;

                hrQuery =
                        "SELECT CLNT_IMAGE FROM TQC_CLIENTS WHERE CLNT_CODE = ?";

                statement =
                        (OraclePreparedStatement)conn.prepareStatement(hrQuery);
                statement.setInt(1, new Integer(imageId));

                OracleResultSet rs = (OracleResultSet)statement.executeQuery();

                while (rs.next()) {
                    if (rs.getBytes(1) != null) {
                        byte barray[] = rs.getBytes(1);
                        response.setContentType("image/jpg");
                        response.setContentLength(barray.length);
                        response.getOutputStream().write(barray);
                    }
                }

                response.getOutputStream().flush();
                response.getOutputStream().close();
                rs.close();

                statement.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
    }
}
