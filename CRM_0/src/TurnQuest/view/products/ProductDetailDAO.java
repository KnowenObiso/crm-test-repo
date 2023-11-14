package TurnQuest.view.products;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.sql.CallableStatement;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;


public class ProductDetailDAO {
    public ProductDetailDAO() {
        super();
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public List<Product> findProducts() {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<Product> products = new ArrayList<Product>();
        try {
            String productsQuery = "begin LMS_WEB_CURSOR.Products(?); end;";

            cst = (OracleCallableStatement)conn.prepareCall(productsQuery);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                Product product = new Product();
                product.setProductCode(rs.getBigDecimal(1));
                product.setProductDesc(rs.getString(2));
                product.setProductShtDesc(rs.getString(3));
                product.setProductType(rs.getString(4));
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


    public List<Product> findProductOptions() {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<Product> products = new ArrayList<Product>();
        try {

            String productsQuery =
                "begin LMS_WEB_CURSOR.ProdOption(?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(productsQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("productCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                Product product = new Product();
                product.setPopCode(rs.getBigDecimal(1));
                product.setPopShtDesc(rs.getString(2));
                product.setPopDesc(rs.getString(3));
                products.add(product);
            }
            rs.close();
            conn.commit();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return products;

    }

    public List<Product> findProductMasks() {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<Product> products = new ArrayList<Product>();
        try {

            String productsQuery = "begin LMS_WEB_CURSOR.Mask(?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(productsQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("productCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                Product product = new Product();
                product.setPMasCode(rs.getBigDecimal(1));
                product.setPMasShtDesc(rs.getString(2));
                product.setPMasDesc(rs.getString(3));
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

    public List<Product> findProductsByAgentCode() {


        String query =
            "begin ? := gis_web_pkg.agent_web_products(?,?,?); end;";

        //OracleConnection con = null;
        CallableStatement cst = null;
        List<Product> agencyProducts = null;
        try {

            DBConnector datahandler = new DBConnector();
            OracleConnection conn;
            conn = datahandler.getDatabaseConnection();
            //con = ConnectionUtil.getConnection();


            cst = conn.prepareCall(query);


            //register out
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("AgentCode"));
            cst.setString(3, (String)session.getAttribute("prodType"));
            if (session.getAttribute("webQuotationBatchNumber") == null) {
                cst.setInt(4, -2000);
            } else
                cst.setInt(4,
                           (Integer)session.getAttribute("webQuotationBatchNumber"));
            //input params
            //cst.setBigDecimal(2, agentCode);

            //PRO_CODE, PRO_DESC, PRO_WEB_DETAILS,CLP_SCL_CODE

            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);


            agencyProducts = new ArrayList<Product>();
            int k = 0;
            BigDecimal ProductCode;
            BigDecimal ProductSubClassCode;
            String ProductDesc;
            String ProductDetails;
            while (rs.next()) {
                Product product = new Product();
                ProductCode = rs.getBigDecimal(1);
                ProductDesc = rs.getString(2);
                ProductDetails = rs.getString(3);
                ProductSubClassCode = rs.getBigDecimal(4);

                product.setProductCode(ProductCode);
                product.setProductDesc(ProductDesc);
                product.setProductDetails(ProductDetails);
                product.setProductSubClassCode(ProductSubClassCode);
                product.setProductShtDesc(rs.getString(5));
                product.setProductExpiryPeriod(rs.getString(7));
                agencyProducts.add(product);

                k++;
            }
            rs.close();
            conn.close();


        } catch (Exception e) {
            String message = e.getMessage();
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          message,
                                                                          message));
        }

        return agencyProducts;

    }

    public List<Product> findAllProducts() {


        String query = "begin ? := gis_web_pkg.get_products(?); end;";

        //OracleConnection con = null;
        CallableStatement cst = null;
        List<Product> agencyProducts = null;
        try {

            DBConnector datahandler = new DBConnector();
            OracleConnection conn;
            conn = datahandler.getDatabaseConnection();
            //con = ConnectionUtil.getConnection();


            cst = conn.prepareCall(query);


            //register out
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setString(2, null);

            //input params
            //cst.setBigDecimal(2, agentCode);

            //PRO_CODE, PRO_DESC, PRO_WEB_DETAILS,CLP_SCL_CODE

            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);


            agencyProducts = new ArrayList<Product>();
            int k = 0;
            BigDecimal ProductCode;
            BigDecimal ProductSubClassCode;
            String ProductDesc;
            String ProductDetails;
            while (rs.next()) {
                Product product = new Product();
                ProductCode = rs.getBigDecimal(1);
                ProductDesc = rs.getString(2);
                ProductDetails = rs.getString(3);
                ProductSubClassCode = rs.getBigDecimal(4);

                product.setProductCode(ProductCode);
                product.setProductDesc(ProductDesc);
                product.setProductDetails(ProductDetails);
                product.setProductSubClassCode(ProductSubClassCode);
                product.setProductShtDesc(rs.getString(5));
                product.setProductExpiryPeriod(rs.getString(7));
                agencyProducts.add(product);

            }
            rs.close();
            conn.close();


        } catch (Exception e) {
            String message = e.getMessage();
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          message,
                                                                          message));
        }

        return agencyProducts;

    }


    public List<Product> findProductSubClasses() {
        String subClassesQuery =
            "begin ? := gis_web_pkg.web_prod_scl(?); end;";

        CallableStatement cst = null;
        List<Product> ProductSubclasses = new ArrayList<Product>();
        try {
            DBConnector datahandler = new DBConnector();
            OracleConnection conn;
            conn = datahandler.getDatabaseConnection();

            cst = conn.prepareCall(subClassesQuery);
            //register out
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code

            //input params
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("productCode"));

            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            while (rs.next()) {
                Product subclass = new Product();
                subclass.setSubClassCode(rs.getBigDecimal(1));
                subclass.setSubClassDesc(rs.getString(2));
                ProductSubclasses.add(subclass);
            }

            conn.close();
            rs.close();


        } catch (Exception e) {
            String message = e.getMessage();
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          message,
                                                                          message));
        }
        return ProductSubclasses;

    }

    public List<Product> findProductSCoverTypes() {
        String coverTypesQuery =
            "begin ? := gis_web_pkg.web_prod_covertypes(?); end;";

        CallableStatement cst = null;
        List<Product> SubClassCoverTypes = new ArrayList<Product>();
        try {
            DBConnector datahandler = new DBConnector();
            OracleConnection conn;
            conn = datahandler.getDatabaseConnection();

            cst = conn.prepareCall(coverTypesQuery);
            //register out
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code

            //input params
            cst.setInt(2, 701);

            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);


            int coverTypeCounter;
            coverTypeCounter = 0;

            while (rs.next()) {
                Product coverType = new Product();
                coverType.setCoverTypeCode(rs.getBigDecimal(1)); //SCLCOVT_COVT_CODE,
                coverType.setDescription(rs.getString(2));
                coverType.setCoverTypeDesc(rs.getString(3)); //COVT_DESC,
                coverType.setCoverTypeDetails(rs.getString(4));
                SubClassCoverTypes.add(coverType);
                coverTypeCounter++;
            }

            conn.close();
            rs.close();

        } catch (Exception e) {
            String Message = e.getMessage();
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          Message,
                                                                          Message));
        }
        return SubClassCoverTypes;
    }

    public List<Product> findProductCoverTypes() {
        String coverTypesQuery =
            "begin ? := gis_web_pkg.web_prod_covertypes(?); end;";

        CallableStatement cst = null;
        List<Product> SubClassCoverTypes = new ArrayList<Product>();
        try {
            DBConnector datahandler = new DBConnector();
            OracleConnection conn;
            conn = datahandler.getDatabaseConnection();

            cst = conn.prepareCall(coverTypesQuery);
            //register out
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code

            //input params
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("subClassCode"));

            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            while (rs.next()) {
                Product coverType = new Product();
                coverType.setCoverTypeCode(rs.getBigDecimal(1)); //SCLCOVT_COVT_CODE,
                coverType.setDescription(rs.getString(2));
                coverType.setCoverTypeDesc(rs.getString(3)); //COVT_DESC,
                coverType.setCoverTypeDetails(rs.getString(4));
                SubClassCoverTypes.add(coverType);

            }

            conn.close();
            rs.close();


        } catch (Exception e) {
            String message = e.getMessage();
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          message,
                                                                          message));
        }
        return SubClassCoverTypes;
    }

    public List<Product> findProductBinders() {
        String bindersQuery =
            "begin ? := gis_web_pkg. get_prod_binders(?,?,?); end;";

        CallableStatement cst = null;
        List<Product> proBinders = new ArrayList<Product>();
        try {
            DBConnector datahandler = new DBConnector();
            OracleConnection conn;
            conn = datahandler.getDatabaseConnection();

            cst = conn.prepareCall(bindersQuery);
            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("AgentCode"));
            cst.setBigDecimal(3,
                              (BigDecimal)session.getAttribute("productCode"));
            cst.setString(4, "B");
            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            while (rs.next()) {
                Product bind = new Product();
                bind.setBindCode(rs.getBigDecimal(1));
                bind.setBindName(rs.getString(2));
                proBinders.add(bind);

            }

            conn.close();
            rs.close();


        } catch (Exception e) {
            String message = e.getMessage();
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          message,
                                                                          message));
        }
        return proBinders;
    }


}
