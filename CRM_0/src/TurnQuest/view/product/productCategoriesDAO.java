package TurnQuest.view.product;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;


public class productCategoriesDAO {
    public productCategoriesDAO() {
        super();
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);

    public List<Products> findWebProductsPromotion() {


        String query = "begin ? := gis_web_pkg.web_products_promotion(); end;";

        //Connection con = null;
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<Products> promotionProducts = null;
        try {

            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();
            //con = ConnectionUtil.getConnection();


            cst = (OracleCallableStatement)conn.prepareCall(query);


            //register out
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code


            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);


            promotionProducts = new ArrayList<Products>();
            while (rs.next()) {
                Products product = new Products();
                product.setProductCode(rs.getBigDecimal(1));
                product.setProductDetails(rs.getString(2));
                product.setProductPromoName(rs.getString(4));
                product.setProductDesc(rs.getString(5));
                product.setProductShtDesc(rs.getString(6));
                promotionProducts.add(product);

            }
            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return promotionProducts;

    }

    public List<Products> findLifeProductCategories() {

        String query =
            "begin ? := tqc_portal_cursor.getProductCategories(?); end;";

        //Connection con = null;
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<Products> categories = null;
        try {

            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code

            cst.setInt(2, 26);
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            categories = new ArrayList<Products>();
            while (rs.next()) {
                Products product = new Products();
                product.setTwpcCode(rs.getBigDecimal(1));
                product.setTwpcName(rs.getString(3));
                product.setTwpcDesc(rs.getString(4));
                categories.add(product);

            }
            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return categories;

    }

    public List<Products> findCategoryProducts() {

        String query = "begin ? := tqc_portal_cursor.getProducts(?); end;";

        //Connection con = null;
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<Products> categories = null;
        try {

            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setObject(2, session.getAttribute("twpcCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            categories = new ArrayList<Products>();
            while (rs.next()) {
                Products product = new Products();
                product.setTwpCode(rs.getBigDecimal(1));
                product.setTwpcCode(rs.getBigDecimal(2));
                product.setProdCode(rs.getBigDecimal(3));
                product.setProdName(rs.getString(4));
                product.setProdDesc(rs.getString(5));
                categories.add(product);

            }
            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return categories;

    }

    public List<Products> findProductOptions() {

        String query =
            "begin ? := tqc_portal_cursor.getProductOption(?); end;";

        //Connection con = null;
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<Products> categories = null;
        try {

            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setObject(2, session.getAttribute("prodCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            categories = new ArrayList<Products>();
            while (rs.next()) {
                Products product = new Products();
                product.setTwpoCode(rs.getBigDecimal(1));
                product.setPopCode(rs.getBigDecimal(2));
                product.setTwpoDesc(rs.getString(3));
                categories.add(product);

            }
            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return categories;

    }
}
