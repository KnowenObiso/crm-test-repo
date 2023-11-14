package TurnQuest.view.dao;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Clients.client;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.ClientAttribute;
import TurnQuest.view.models.Product;
import TurnQuest.view.models.ProductClientAttribute;
import TurnQuest.view.product.Products;

import java.math.BigDecimal;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;


public class ProductDAO {
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public ProductDAO() {
        super();
    }

    public List<Product> findProductAttribute() {
        List<Product> prodlist = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin  TQC_PRODUCT_PKG.get_ProductAttributes(?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;


        prodlist = new ArrayList<Product>();

        try {
            conn = OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.registerOutParameter(1, OracleTypes.CURSOR);

            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                Product prod = new Product();
                prod.setTPA_CODE(rst.getBigDecimal(1));
                prod.setTPA_SYSTEM(rst.getBigDecimal(2));
                prod.setTPA_PROD_CODE(rst.getBigDecimal(3));
                prod.setTPA_PROD_SHTDESC(rst.getString(4));
                prod.setTPA_PROD_DESC(rst.getString(5));
                prod.setTPA_PROD_NARRATION(rst.getString(6));
                prod.setSYSNAME(rst.getString(7));

                prodlist.add(prod);

            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }

        return prodlist;
    }

    public List<Product> findSystemProducts() {
        List<Product> prodlist = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin  TQC_PRODUCT_PKG.get_SystemProducts(?,?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;


        prodlist = new ArrayList<Product>();
        if (session.getAttribute("sysCode") != null) {
            try {
                conn =
(OracleConnection)OracleConnection.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query);

                stmt.registerOutParameter(2, OracleTypes.CURSOR);
                stmt.setBigDecimal(1,
                                   new BigDecimal(session.getAttribute("sysCode").toString()));
                stmt.execute();
                OracleResultSet rst = (OracleResultSet)stmt.getObject(2);
                while (rst.next()) {
                    Product prod = new Product();
                    prod.setTPA_PROD_CODE(rst.getBigDecimal(1));
                    prod.setTPA_PROD_SHTDESC(rst.getString(2));
                    prod.setTPA_PROD_DESC(rst.getString(3));
                    prodlist.add(prod);

                }
                rst.close();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);

            }
        }

        return prodlist;
    }

    public List<ClientAttribute> findClientAttributes() {
        List<ClientAttribute> attrList = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin  TQC_PRODUCT_PKG.get_ClientAttributes(?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;


        attrList = new ArrayList<ClientAttribute>();

        try {
            conn = OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.registerOutParameter(1, OracleTypes.CURSOR);

            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                ClientAttribute attr = new ClientAttribute();
                attr.setTCA_CODE(rst.getBigDecimal(1));
                attr.setTCA_ATTRIBUTE_NAME(rst.getString(2));
                attr.setTCA_ATT_DESC(rst.getString(3));
                attr.setTCA_PROMPT(rst.getString(4));
                attr.setTCA_ATT_RANGE(rst.getString(5));
                attr.setTCA_ATT_TYPE_INPUT(rst.getString(6));
                attr.setTableName(rst.getString(7));
                attr.setColName(rst.getString(8));
                attr.setColDescription(rst.getString(9));

                attrList.add(attr);

            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }


        return attrList;
    }

    public List<ClientAttribute> findUndefinedClientAttributes() {
        List<ClientAttribute> attrList = null;
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin  TQC_PRODUCT_PKG.get_UndefinedClientAttributes(?,?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;


        attrList = new ArrayList<ClientAttribute>();

        try {
            conn = OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.setObject(1, session.getAttribute("PRODUCTCODE"));
            stmt.registerOutParameter(2, OracleTypes.CURSOR);

            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(2);
            while (rst.next()) {
                ClientAttribute attr = new ClientAttribute();
                attr.setTCA_CODE(rst.getBigDecimal(1));
                attr.setTCA_ATTRIBUTE_NAME(rst.getString(2));
                attr.setTCA_ATT_DESC(rst.getString(3));
                attr.setTCA_PROMPT(rst.getString(4));
                attr.setTCA_ATT_RANGE(rst.getString(5));
                attr.setTCA_ATT_TYPE_INPUT(rst.getString(6));
                attr.setTableName(rst.getString(7));
                attr.setColName(rst.getString(8));
                attrList.add(attr);

            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }


        return attrList;
    }

    public List<ProductClientAttribute> findProductClientAttribute() {
        List<ProductClientAttribute> attrList = null;
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin  TQC_PRODUCT_PKG.get_prod_client_attributes(?,?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;


        attrList = new ArrayList<ProductClientAttribute>();

        try {
            conn = OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.setObject(1, session.getAttribute("PRODUCTCODE"));

            stmt.registerOutParameter(2, OracleTypes.CURSOR);

            stmt.execute();
            OracleResultSet rs = (OracleResultSet)stmt.getObject(2);
            while (rs.next()) {
                ProductClientAttribute attr = new ProductClientAttribute();
                attr.setProdClientAttrCode(rs.getBigDecimal(1));
                attr.setProdCode(rs.getBigDecimal(2));
                attr.setClientAttrCode(rs.getBigDecimal(3));
                attr.setClientAttributeName(rs.getString(4));
                attr.setAttributeRange(rs.getString(5));
                attr.setProdMinValue(rs.getString(6));
                attr.setProdMaxValue(rs.getString(7));
                attr.setProdFixedValue(rs.getString(8));
                attr.setColName(rs.getString(9));
                attr.setPrompt(rs.getString(10));
                attrList.add(attr);

            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }


        return attrList;
    }

    public List<client> findClientsPerProduct() {

        String query = null;
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<client> clientList = new ArrayList<client>();
        String whereClause = null;
        try {
            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();

            query = "begin ?:= TQC_PRODUCT_PKG.getProductColumns(?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setObject(2, session.getAttribute("PRODUCT_ATTRIBUTE_CODE"));
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            whereClause = "WHERE ";
            String subQuery = null;
            int k = 0;
            while (rs.next()) {
                //whereClause = " "+rs.getString(1);
                if (k > 0) {
                    whereClause = whereClause + " AND ";
                }
                if (rs.getString(2).equalsIgnoreCase("N")) {
                    subQuery =
                            rs.getString(1) + " LIKE '" + rs.getString(5) + "' ";
                } else {
                    subQuery =
                            rs.getString(1) + " >= " + rs.getString(3) + " AND " +
                            rs.getString(1) + " <= " + rs.getString(4) + " ";
                }
                whereClause = whereClause + subQuery;
                k++;
            }
            if (k == 0) {
                rs.close();
                cst.close();
                conn.close();

                return clientList;
            }
            query =
                    "begin ? := TQC_PRODUCT_PKG.get_clients_per_product(?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setString(2, whereClause);

            cst.execute();

            rs = (OracleResultSet)cst.getObject(1);


            while (rs.next()) {
                client client1 = new client();
                client1.setClientCode(rs.getBigDecimal(1));
                client1.setPINNumber(rs.getString(2));
                client1.setPostalAddress(rs.getString(3));
                client1.setTelphoneOne(rs.getString(4));
                client1.setOthernames(rs.getString(5));
                client1.setFullname(rs.getString(6));
                client1.setIdRegNumber(rs.getString(7));
                client1.setShortDescription(rs.getString(8));
                client1.setZIPCode(rs.getString(9));
                clientList.add(client1);
            }
            rs.close();
            cst.close();
            conn.close();

        } catch (Exception e) {

            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return clientList;
    }

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
