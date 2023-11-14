package TurnQuest.view.servlets;


import TurnQuest.view.Base.GlobalCC;

import java.io.IOException;

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


public class OrgImageServlet extends HttpServlet {

    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException,
                                                           IOException {

        OracleConnection conn = null;
        try {
            response.setContentType(CONTENT_TYPE);
            String imageId = request.getParameter("id");
            System.out.println("ImageId is " + imageId);

            Context initCtx = new InitialContext();
            Context envCtx = (Context)initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource)envCtx.lookup("jdbc/CRMDS");

            conn = (OracleConnection)ds.getConnection();

            OraclePreparedStatement statement = null;
            String hrQuery = null;

            hrQuery =
                    "SELECT ORG_RPT_LOGO FROM TQC_ORGANIZATIONS WHERE ORG_CODE = ?";

            statement =
                    (OraclePreparedStatement)conn.prepareStatement(hrQuery);
            statement.setInt(1, new Integer(imageId));

            OracleResultSet rs = (OracleResultSet)statement.executeQuery();

            while (rs.next()) {
                byte barray[] = rs.getBytes(1);
                response.setContentType("image/jpg");
                response.setContentLength(barray.length);
                response.getOutputStream().write(barray);
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

    /*public void doGet(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException,
                                                         IOException {
      response.setContentType(CONTENT_TYPE);
      String imageId = request.getParameter("id");
      OutputStream os = response.getOutputStream();
      OracleConnection conn = null;
      try {
          Context ctx = new InitialContext();
          //Datasource as defined in <res-ref-name> element of weblogic.xml
          DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/CRMDS");
          conn = ds.getConnection();
          OraclePreparedStatement  statement = conn.prepareStatement("SELECT ORG_RPT_LOGO FROM TQC_ORGANIZATIONS WHERE ORG_CODE = ?");
          statement.setInt(1, new Integer(imageId));

          OracleResultSet  rs = statement.executeQuery();
          if (rs.next()) {
              Blob blob = rs.getBlob("ORG_RPT_LOGO");
              BufferedInputStream in = new BufferedInputStream(blob.getBinaryStream());
              int b;
              byte[] buffer = new byte[10240];
              while ((b = in.read(buffer, 0, 10240)) != -1) {
                  os.write(buffer, 0, b);
              }
              os.close();
          }
      } catch (Exception e) {
          System.out.println(e);
      } finally {
          try {
              if (conn != null) {
                  conn.close();
              }
          } catch (SQLException sqle) {
              System.out.println("SQLException error");
          }
      }
  }*/

    /*public void doGet( HttpServletRequest request,
					   HttpServletResponse response ) throws ServletException,
															 IOException {

	  String appModuleName = this.getServletConfig().getInitParameter("ApplicationModuleName");
    String appModuleConfig = this.getServletConfig().getInitParameter("ApplicationModuleConfig");
    String voQuery = this.getServletConfig().getInitParameter("ImageViewObjectQuery");
    String mimeType = this.getServletConfig().getInitParameter("MimeType");

    //TODO: throw exception if mandatory parameter not set


    ApplicationModule am = Configuration.createRootApplicationModule(appModuleName, appModuleConfig);
    ViewObject vo =  am.createViewObjectFromQueryStmt("TempView", voQuery);

    Map paramMap = request.getParameterMap();
    Iterator paramValues = paramMap.values().iterator();

    int i=0;

    while (paramValues.hasNext()) {
        // Only one value for a parameter is expected.

        // TODO: If more then 1 parameter is supplied make sure the value is bound to the right bind

        // variable in the query! Maybe use named variables instead.

        String[] paramValue = (String[])paramValues.next();
        vo.setWhereClauseParam(i, paramValue[0]);
        i++;
    }

    // Run the query
    vo.executeQuery();

    // Get the result (only the first row is taken into account
    Row product = vo.first();

    BlobDomain image = null;

    // Check if a row has been found
    if (product != null) {
      // We assume the Blob to be the first a field
      image = (BlobDomain) product.getAttribute(0);

      // Check if there are more fields returned. If so, the second one

      // is considered to hold the mime type
      if ( product.getAttributeCount()> 1 ) {
          mimeType = (String)product.getAttribute(1);
      }
    } else {
        System.out.println("No row found to get image from !!!");
        return;
    }

    // Set the content-type. Only images are taken into account
    response.setContentType("image/"+ mimeType+ "; charset=windows-1252");
    OutputStream os = response.getOutputStream();
    InputStream is = image.getInputStream();

    // copy blob to output
    byte[] buffer = new byte[4096];
    int nread;

    while ((nread = is.read(buffer)) != -1) {
        os.write(buffer, 0, nread);
    }

    os.close();

    // Remove the temporary viewobject
    vo.remove();

    // Release the appModule
    Configuration.releaseRootApplicationModule(am, false);
	}*/
}
