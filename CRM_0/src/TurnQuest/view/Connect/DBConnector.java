package TurnQuest.view.Connect;


import TurnQuest.view.Base.GlobalCC;

import javax.faces.context.FacesContext;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.http.HttpSession;

import javax.sql.DataSource;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import weblogic.jdbc.common.internal.RmiDataSource;


public class DBConnector {
    private static DBConnector instance = null;
    public DBConnector() {
    }

    String jdblUrl; //= "jdbc:oracle:thin:@devlaptop:1521:tqequity";
    String userid; //= "tq_gis";
    String password; //= "tq_gis";

    public OracleConnection getDatabaseConnection() {
        OracleConnection conn = null;
        try {
            String connectionString = null;
            Context initCtx = new InitialContext();
            Context envCtx = (Context)initCtx.lookup("java:comp/env");
            connectionString = (String)envCtx.lookup("conn");
            DataSource ds = (DataSource)envCtx.lookup(connectionString);
            conn = (OracleConnection)ds.getConnection();
            conn.setStatementCacheSize(10);
            conn.setImplicitCachingEnabled(true);
            userVaraibleInitialization(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public OracleConnection getLogOutDatabaseConnection() {
        OracleConnection conn = null;
        try {
            String connectionString = null;
            Context initCtx = new InitialContext();
            Context envCtx = (Context)initCtx.lookup("java:comp/env");
            connectionString = (String)envCtx.lookup("conn");
            DataSource ds = (DataSource)envCtx.lookup(connectionString);
            conn = (OracleConnection)ds.getConnection();
            conn.setStatementCacheSize(10);
            conn.setImplicitCachingEnabled(true);

        } catch (Exception e) {
            //  GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return conn;
    }

    public OracleConnection getLoginConnection() {
        OracleConnection conn = null;
        try {

            String connectionString = null;
            Context initCtx = new InitialContext();
            Context envCtx = (Context)initCtx.lookup("java:comp/env");
            connectionString = (String)envCtx.lookup("conn");
            RmiDataSource datasource =
                (RmiDataSource)envCtx.lookup(connectionString);
            conn = (OracleConnection)datasource.getConnection();
            conn.setStatementCacheSize(10);
            conn.setImplicitCachingEnabled(true);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return conn;
    }

    public DataSource getDatabaseSource() {
        OracleConnection conn = null;

        try {
            String connectionString = null;
            Context initCtx = new InitialContext();
            Context envCtx = (Context)initCtx.lookup("java:comp/env");
            connectionString = (String)envCtx.lookup("conn");
            DataSource ds = (DataSource)envCtx.lookup(connectionString);

            return ds;

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;

    }

    public void userVaraibleInitialization(OracleConnection conn) {

         FacesContext ctx=FacesContext.getCurrentInstance();
       
        OracleCallableStatement callStmt=null;
        if(ctx!=null) {
            HttpSession session =
                (HttpSession)ctx.getExternalContext().getSession(false);
            if(ctx.getExternalContext()!=null) {
                if(session!=null) {
                     try {
                        callStmt =
                                (OracleCallableStatement)conn.prepareCall("begin pkg_global_vars.set_pvar(?,?); end;");
                
                        callStmt.setString(1, "pkg_global_vars.pvg_username");
                        callStmt.setString(2, (String)session.getAttribute("Username"));
                        callStmt.execute();
                        callStmt.close();
                
                    } catch (Exception e) {
                        GlobalCC.EXCEPTIONREPORTING(conn, e);
                    }   
                }
            }
        }
    }
    

    public static void closeConnection(OracleConnection conn) {

        try {
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        } finally {
            try {
                conn.close();
            } catch (Exception e) {

                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }

    }


    public static DBConnector getInstance() {
        return  new DBConnector();
    }
}
