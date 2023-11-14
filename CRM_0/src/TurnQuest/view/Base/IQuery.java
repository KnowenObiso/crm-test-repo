package TurnQuest.view.Base; 
/*author: Dancan Kavagi*/


import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import org.apache.commons.dbutils.DbUtils;


public class IQuery {
	public static int fetchInt(String query){
		DBConnector datahandler = new DBConnector();
		OracleConnection conn=null;
		OracleCallableStatement stmt = null;
		ResultSet rs=null;
		int value=0;
		try{
		    conn = (OracleConnection)datahandler.getDatabaseConnection();  
		    //System.out.println("IQuery.fetchString(): query: "+query);
	            stmt = (OracleCallableStatement)conn.prepareCall(query); 
	            rs=stmt.executeQuery();
	            if(rs.next())
	            {
	            	value=rs.getInt(1); 
	            } 
		 //  System.out.println("IQuery.fetchInt():"+value);
		}catch(Exception e)
		{
		    GlobalCC.EXCEPTIONREPORTING(e);
		}finally{
			DbUtils.closeQuietly(conn, stmt, rs);
		}
		return value; 
	}
	public static BigDecimal fetchBg(String query){
		DBConnector datahandler = new DBConnector();
		OracleConnection conn=null;
		OracleCallableStatement stmt = null;
		ResultSet rs=null;
		BigDecimal value=null;
		try{
		    conn = (OracleConnection)datahandler.getDatabaseConnection(); 
		    System.out.println("IQuery.fetchString(): query: "+query);
	            stmt = (OracleCallableStatement)conn.prepareCall(query); 
	            rs=stmt.executeQuery();
	            if(rs.next())
	            {
	            	value=GlobalCC.checkBDNullValues(rs.getBigDecimal(1));
	            } 
                    System.out.println("IQuery.fetchBg():"+value!=null?value.toString():null);
		}catch(Exception e)
		{
		    GlobalCC.EXCEPTIONREPORTING(e);
		}finally{
			DbUtils.closeQuietly(conn, stmt, rs);
		}
		return value; 
	}
	public static String fetchString(String query){
		DBConnector datahandler = new DBConnector();
		OracleConnection conn=null;
		OracleCallableStatement stmt = null;
		ResultSet rs=null;
		String value=null;
		try{
                   // System.out.println("IQuery.fetchString(): query: "+query);
		    conn = (OracleConnection)datahandler.getDatabaseConnection(); 
	            stmt = (OracleCallableStatement)conn.prepareCall(query); 
	            rs=stmt.executeQuery();
	            if(rs.next())
	            {
	            	value=GlobalCC.checkNullValues(rs.getString(1));
	            } 
		   // System.out.println("IQuery.fetchString(): value: "+value);
		}catch(Exception e)
		{
		      GlobalCC.EXCEPTIONREPORTING(e);
		}finally{
			DbUtils.closeQuietly(conn, stmt, rs);
		}
		return value; 
	}
	public static boolean exec(String  sql)
    {
           DBConnector datahandler = new DBConnector();
           OracleConnection conn=null;
           OracleCallableStatement stmt = null;
           boolean result=false; 
           try{
                   conn = (OracleConnection) datahandler.getDatabaseConnection(); 
                   stmt = (OracleCallableStatement)conn.prepareCall(sql); 
                   result=stmt.execute(); 
                   conn.commit();
           }catch(Exception e)
           {
                   GlobalCC.EXCEPTIONREPORTING(conn,e);
           }finally{
                   DbUtils.closeQuietly(conn, stmt, null);
           }
           return result;
    } 
	public static boolean fetchResult(String sql,Object o,IQueryAction a)
    {
           DBConnector datahandler = new DBConnector();
           OracleConnection conn=null;
           OracleCallableStatement stmt = null;
           ResultSet rs=null; 
           boolean success=false;
           try{
              //System.out.println("query: "+sql);
               conn = (OracleConnection) datahandler.getDatabaseConnection();  
               stmt = (OracleCallableStatement)conn.prepareCall(sql); 
               rs = stmt.executeQuery();
               a.fetch(rs,o);
               success=true;
           }catch(Exception e)
           {
                GlobalCC.EXCEPTIONREPORTING(conn,e);
           }finally{
                DbUtils.closeQuietly(conn, stmt, rs);
           }
           return success;
    }
    public static HashMap<String,String> getAssociativeRow(ResultSet rs)
            {
                    HashMap<String,String> row=new HashMap<String,String>();
                    try{
                            ResultSetMetaData mt=rs.getMetaData();
                            for(int i=1;i<= mt.getColumnCount();i++)
                            {
                                    String key=mt.getColumnName(i);
                                    if(key!=null)
                                    {
                                            row.put(key, rs.getString(i));
                                           // System.out.println(key+"=>"+rs.getString(i));
                                    }
                            }
                    }catch(Exception e)
                    {
                            e.printStackTrace();
                    }
                    return row;
            }
    public static  HashMap<String,String> fetchAssociativeRow(String query){
           
        DBConnector datahandler = new DBConnector();
        OracleConnection conn=null;
        OracleCallableStatement stmt = null;
        ResultSet rs=null; 
        HashMap<String,String> m=null;
        try{
            System.out.println("query: "+query);
            conn = (OracleConnection) datahandler.getDatabaseConnection();  
            stmt = (OracleCallableStatement)conn.prepareCall(query); 
            rs = stmt.executeQuery();
            if (rs.next()) { 
                 m=( HashMap<String,String>)IQuery.getAssociativeRow(rs);
              }
        }catch(Exception e)
        {
             GlobalCC.EXCEPTIONREPORTING(conn,e);
        }finally{
             DbUtils.closeQuietly(conn, stmt, rs);
        }
        return m; 
    }
    public static  List<Object> fetchAssociativeRows(String query){
           
        DBConnector datahandler = new DBConnector();
        OracleConnection conn=null;
        OracleCallableStatement stmt = null;
        ResultSet rs=null;  
        List<Object>items=new ArrayList();
        try{
           //System.out.println("query: "+sql);
            conn = (OracleConnection) datahandler.getDatabaseConnection();  
            stmt = (OracleCallableStatement)conn.prepareCall(query); 
            rs = stmt.executeQuery();
           while(rs.next()) { 
                HashMap<String,String> m=IQuery.getAssociativeRow(rs);
                if(m!=null){
                 items.add(m);
                }
              }
        }catch(Exception e)
        {
             GlobalCC.EXCEPTIONREPORTING(conn,e);
        }finally{
             DbUtils.closeQuietly(conn, stmt, rs);
        }
        return items; 
    }
}
