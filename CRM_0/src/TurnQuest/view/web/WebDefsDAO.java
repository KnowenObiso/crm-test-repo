package TurnQuest.view.web;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;


public class WebDefsDAO {
    public WebDefsDAO() {
        super();
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);


    public List<WebDefs> findProductCategories() {

        List<WebDefs> productgroups = null;
        DBConnector connection = new DBConnector();
        String query = "begin ? := TQC_PORTAL_CURSOR.getSystems(); end;";
        CallableStatement stmt = null;
        Connection conn = null;
        try {
            List<WebBean> productbeans = null;
            productgroups = new ArrayList<WebDefs>();
            conn = connection.getDatabaseConnection();
            stmt = conn.prepareCall(query);
            stmt.registerOutParameter(1,
                                      oracle.jdbc.internal.OracleTypes.CURSOR);
            stmt.execute();
            ResultSet rst = (ResultSet)stmt.getObject(1);
            while (rst.next()) {
                productbeans = new ArrayList<WebBean>();
                WebDefs pgroup = new WebDefs();
                pgroup.setSystemCode(rst.getBigDecimal(1));
                pgroup.setSystemName(rst.getString(2));
                pgroup.setNodeType("P");
                CallableStatement stmt2 =
                    conn.prepareCall("begin ? := TQC_PORTAL_CURSOR.getProductCategories(?); end;");
                stmt2.setBigDecimal(2, rst.getBigDecimal(1));
                stmt2.registerOutParameter(1,
                                           oracle.jdbc.internal.OracleTypes.CURSOR);
                stmt2.execute();
                ResultSet rs = (ResultSet)stmt2.getObject(1);
                while (rs.next()) {
                    WebBean def = new WebBean();
                    def.setTwpCode(rs.getBigDecimal(1));
                    def.setTwpcName(rs.getString(3));
                    def.setTwpcDesc(rs.getString(4));
                    def.setNodeType("S");
                    productbeans.add(def);
                }
                pgroup.setProducts(productbeans);
                productgroups.add(pgroup);
                rs.close();
                stmt2.close();
            }
            rst.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return productgroups;
    }

    public List<WebDefs> findCatProducts() {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<WebDefs> products = new ArrayList<WebDefs>();
        try {
            String query =
                "begin ? := TQC_PORTAL_CURSOR.getProducts(?,?); end;";

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setObject(2, session.getAttribute("twpcCode"));
            cst.setObject(3, session.getAttribute("systemCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                WebDefs product = new WebDefs();
                product.setTwpCode(rs.getBigDecimal(1));
                product.setTwpcCode(rs.getBigDecimal(2));
                product.setProdCode(rs.getBigDecimal(3));
                product.setProdDesc(rs.getString(4));
                product.setTwpProdDesc(rs.getString(5));
                product.setBindName(rs.getString(6));
                product.setBinder(rs.getString(7));
                product.setBindCode(rs.getBigDecimal(8));
                product.setAgencyAccounts(rs.getString(9));
                product.setAgencyCode(rs.getBigDecimal(10));
                product.setAgaCode(rs.getBigDecimal(11));
                product.setAgaShtDesc(rs.getString(12));
                products.add(product);
            }
            rs.close();
            conn.commit();
            conn.close();


        } catch (Exception e) {

            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return products;

    }

    public List<WebDefs> findProductOptions() {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<WebDefs> options = new ArrayList<WebDefs>();
        try {
            String query =
                "begin ? := TQC_PORTAL_CURSOR.getProductOption(?,?); end;";

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setObject(2, session.getAttribute("prodCode"));
            cst.setObject(3, session.getAttribute("systemCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                WebDefs opt = new WebDefs();
                opt.setTwpoCode(rs.getBigDecimal(1));
                opt.setPopCode(rs.getBigDecimal(2));
                opt.setTwpoDesc(rs.getString(3));
                opt.setPopDesc(rs.getString(4));
                opt.setBindName(rs.getString(5));
                options.add(opt);
            }
            rs.close();
            conn.commit();
            conn.close();


        } catch (Exception e) {

            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return options;

    }

    public List<WebDefs> findLifeProducts() {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<WebDefs> products = new ArrayList<WebDefs>();
        try {
            String query =
                "begin ? := TQC_PORTAL_CURSOR.getLifeProducts(?); end;";

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setObject(2, session.getAttribute("systemCode"));
            System.out.println(session.getAttribute("systemCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                WebDefs product = new WebDefs();
                product.setProdCode(rs.getBigDecimal(1));
                product.setProdShtDesc(rs.getString(2));
                product.setProdDesc(rs.getString(3));
                products.add(product);
            }
            rs.close();
            conn.commit();
            conn.close();


        } catch (Exception e) {

            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return products;

    }

    public List<WebDefs> findLifeOptions() {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<WebDefs> options = new ArrayList<WebDefs>();
        try {
            String query =
                "begin ? := TQC_PORTAL_CURSOR.getProdOptions(?,?); end;";

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setObject(2, session.getAttribute("prodCode"));
            cst.setObject(3, session.getAttribute("systemCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                WebDefs opt = new WebDefs();
                opt.setPopCode(rs.getBigDecimal(1));
                opt.setPopShtDesc(rs.getString(2));
                opt.setPopDesc(rs.getString(3));
                options.add(opt);
            }
            rs.close();
            conn.commit();
            conn.close();


        } catch (Exception e) {

            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return options;

    }

    public List<WebDefs> findSystems() {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<WebDefs> options = new ArrayList<WebDefs>();
        try {
            String query = "begin ? := TQC_PORTAL_CURSOR.getSystems(); end;";

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                WebDefs opt = new WebDefs();
                opt.setSystemCode(rs.getBigDecimal(1));
                opt.setSystemName(rs.getString(2));
                options.add(opt);
            }
            rs.close();
            conn.commit();
            conn.close();


        } catch (Exception e) {

            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return options;

    }

    public List<WebDefs> findprodCategories() {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<WebDefs> options = new ArrayList<WebDefs>();
        try {
            String query =
                "begin ? := TQC_PORTAL_CURSOR.getProductCategories(?); end;";

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("systemCode"));
            System.out.println(session.getAttribute("systemCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                WebDefs def = new WebDefs();
                def.setTwpCode(rs.getBigDecimal(1));
                def.setTwpcName(rs.getString(3));
                def.setTwpcDesc(rs.getString(4));
                options.add(def);
            }
            rs.close();
            conn.commit();
            conn.close();


        } catch (Exception e) {

            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return options;

    }

    public List<WebDefs> findTransProducts() {


        String query =
            "begin ? := gis_web_pkg.get_new_trans_products(?); end;";

        OracleCallableStatement cst = null;
        List<WebDefs> sysProducts = null;
        try {

            DBConnector datahandler = new DBConnector();
            OracleConnection conn;
            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);

            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setString(2, (String)session.getAttribute("binderType"));

            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);


            sysProducts = new ArrayList<WebDefs>();

            while (rs.next()) {
                WebDefs product = new WebDefs();
                product.setBinderCode(rs.getBigDecimal(1));
                product.setBinderShtDesc(rs.getString(2));
                product.setBinderName(rs.getString(3));
                product.setProductCode(rs.getBigDecimal(4));
                product.setProductShtCode(rs.getString(5));
                product.setProductDesc(rs.getString(6));
                product.setAgntShtDesc(rs.getString(7));
                product.setProductAccomodation(rs.getString(8));
                product.setProPreReqShtDesc(rs.getString(9));
                product.setProductMinimumPremium(rs.getBigDecimal(10));
                product.setProEditPolNo(rs.getString(11));
                product.setAgntCode(rs.getBigDecimal(12));
                product.setAgntName(rs.getString(13));
                product.setAGN_COMM_ALLOWED(rs.getString(14));
                product.setPrgType(rs.getString(15));
                sysProducts.add(product);
            }
            rs.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return sysProducts;

    }

    public List<WebDefs> findAgencyAccount() {
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getAgencyAccounts(?); end;";
        OracleCallableStatement cst = null;
        List<WebDefs> sysProducts = null;
        try {
            DBConnector datahandler = new DBConnector();
            OracleConnection conn;
            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);

            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("agntCode"));
            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);


            sysProducts = new ArrayList<WebDefs>();

            while (rs.next()) {
                WebDefs product = new WebDefs();
                product.setAgaCode(rs.getBigDecimal(1));
                product.setAgaShtDesc(rs.getString(2));
                product.setAgaName(rs.getString(3));
                product.setAgaCreatedBy(rs.getString(4));
                sysProducts.add(product);
            }
            rs.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return sysProducts;

    }

}
