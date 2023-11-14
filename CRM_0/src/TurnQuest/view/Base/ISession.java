/*
 * @author: Dan Kavagi.
 * */

package TurnQuest.view.Base;


import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.Branch;

import java.io.File;
import java.io.FileOutputStream;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.apache.commons.dbutils.DbUtils;


public class ISession{
    private static final String SESS_DATE_FMT="yyyy-MM-dd'T'HH:mm:ss.SSS" ;
    private static final int COOKIE_MAX_AGE = 1200;//20 min

    public static void doFilter(ServletRequest request, ServletResponse response,FilterChain filterChain) {                                
            HttpServletRequest rq = (HttpServletRequest)request;
            HttpServletResponse httpServletResponse =(HttpServletResponse)response;
            
            OracleConnection conn=null;
            OracleCallableStatement callStmt = null;
              try {
                
                HttpSession session = rq.getSession(false);           
                String url = rq.getPathTranslated();
                //System.out.println("url: ["+url+"]");
                  
                if( rq!=null ) {
                    //System.out.println("crm_state[sn="+rq.getParameter("sn")+"]");
                }
                
                String page = ISession.getRequestedPage((HttpServletRequest)request);
                //System.out.println("Page: "+page);
                String context = rq.getContextPath();
                if((url.endsWith("index.jspx") || url.endsWith("index.html")) && filterChain!=null) {
                    filterChain.doFilter(request, response);
                    return;
                }
                if(rq.getParameter("sn") !=null)//session cloning. [secure]
                {
                        //System.out.println("sn="+rq.getParameter("sn"));
                        HashMap<String,Object> m = ISession.decodeSession(rq.getParameter("sn"));
                        
                        String username=(String)m.get("Username");
                        BigDecimal usercode=(BigDecimal)m.get("UserCode");
                        
                        System.out.println("Username=>"+username+" UserCode=>"+usercode);
                        if(ISession.userExists(username, usercode) )
                        {
                            
                            System.out.println("Initialized Parameters.");
                            if(session == null) {
                               session = rq.getSession(true);
                            }
                            session.setAttribute("Username", username);
                            session.setAttribute("LoginDate", new java.util.Date());
                            session.setAttribute("UserCode", usercode); 
                            
                            ISession.sysParameters(session);
                            ISession.cloneSession(session, m);
      
                            if(page!=null && "".equals(page)!=true) {
                                httpServletResponse.sendRedirect(context + "/faces/"+page);
                                return;
                            }else{
                                httpServletResponse.sendRedirect(context + "/faces/home.jspx");
                                return;
                            }
                        }else{
                             httpServletResponse.sendRedirect(context + "/faces/index.jspx");
                             return;
                        }
                }
                if(rq.getParameter("aid") !=null)//oldscul login and create assesion [insecure]
                {
                        DBConnector datahandler = new DBConnector();
                        conn = datahandler.getDatabaseConnection();
                        if(conn == null) {
                            String message = "Error Connecting to Database. Contact Your System Administrator";
                            GlobalCC.errorValueNotEntered(message);
                        }
                        String query =
                            "begin tqc_web_cursor.getUserName(?,?,?); end;";
                
                        callStmt = (OracleCallableStatement)conn.prepareCall(query);
                        callStmt.setBigDecimal(1,
                                               new BigDecimal(rq.getParameter("aid")));
                        callStmt.registerOutParameter(2, OracleTypes.VARCHAR);
                        callStmt.registerOutParameter(3, OracleTypes.DATE);
                        callStmt.execute();
                        
                        String username=(String)callStmt.getString(2);
                        BigDecimal usercode=new BigDecimal(rq.getParameter("aid"));
                    
                        callStmt.close();
                        conn.commit();
                        conn.close();
                       
                        System.out.println("Initialized Parameters.");
                        if(session == null) {
                           session = rq.getSession(true);
                        }
                        session.setAttribute("Username", username);
                        session.setAttribute("LoginDate", new java.util.Date());
                        session.setAttribute("UserCode", usercode); 
                        ISession.sysParameters(session);
                        
                        if(page!=null && "".equals(page)!=true) {
                            httpServletResponse.sendRedirect(context + "/faces/"+page);
                            return;
                        }else{
                            httpServletResponse.sendRedirect(context + "/faces/home.jspx");
                            return;
                        }
                }
                  
            
              if(session!=null) 
              { 
                   if(session.getAttribute("Username")==null)//not logged in
                    {
                        if ((url.endsWith("index.jspx") || url.endsWith("index.html"))!=true) //not homepage
                        {
                                httpServletResponse.sendRedirect(context + "/faces/index.jspx");
                                return;
                        }
                    }
                  if (session.getAttribute("Username") != null) //logged in update session.
                  {
                    //every time a page refresh trigger form fields refresh from db.
                    ISession.sysParameters(session);
                    Cookie[] cookies =  ((HttpServletRequest)rq).getCookies();
                    for (Cookie cookie : cookies) {
                      if (cookie.getName().equalsIgnoreCase("JSESSIONID")) {
                        ISession.setCookie((HttpServletResponse)response, cookie.getName(), cookie.getValue(), COOKIE_MAX_AGE);
                      }
                    }
                  }
              }else{
                  httpServletResponse.sendRedirect(context + "/faces/index.jspx");
                  return;
              }
              filterChain.doFilter(request, response);
        }catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }finally{
            DbUtils.closeQuietly(conn, callStmt, null);
        }
   }
    
    public static String serializeObj(Object o)
        {
            if(o!=null)
            {
                  if (o instanceof java.lang.String)
                  {
                       String s=GlobalCC.checkNullValues(o);
                       if(s!=null)
                       {
                           if( !(s.toLowerCase().contains("oracle"))
                               && !(s.toLowerCase().contains("apache"))
                               && !(s.toLowerCase().contains("adf"))
                               && !(s.toLowerCase().contains("fx_"))
                               && !(s.toLowerCase().contains("fm_"))
                                   )
                            {
                                if((s.indexOf(",")<0) && (s.indexOf("=>")<0))
                                {
                                         return "S:"+o;
                                }
                            }
                       }
                        
                  }
                if (o.getClass() == BigDecimal.class)
                {
                        return "G:"+o.toString();
                }
                
                if (o.getClass() == Integer.class)
                {
                        return "I:"+o.toString();
                }
                if (o.getClass() == Long.class)
                {
                        return "L:"+o.toString();
                }
                if (o.getClass() == Character.class)
                {
                        return "C:"+o.toString();
                }
                if (o.getClass() == Float.class)
                {
                        return "F:"+o.toString();
                }
                if (o.getClass() == Double.class)
                {
                        return "D:"+o.toString();
                }
                if (o.getClass() == Short.class)
                {
                        return "H:"+o.toString();
                }
                if (o.getClass() == Boolean.class)
                {
                        return "B:"+o.toString();
                }
                if (o.getClass() ==  java.sql.Date.class)
                { 
                         SimpleDateFormat sdf = new SimpleDateFormat(SESS_DATE_FMT);
                         return "Q:"+sdf.format(o);
                }
                if (o.getClass() ==  java.util.Date.class)
                { 
                         SimpleDateFormat sdf = new SimpleDateFormat(SESS_DATE_FMT);
                         return "T:"+sdf.format(o);
                }
                }
                return null; 
        }
            public static Object unSerializeObj(String s)
            {
                    if(s!=null && "".equals(s)!=true)
                    {
                            String t=s.substring(0,1);
                            String v=s.substring(2,s.length());
                        if (t.equals("S")) {
                            return v;
                        }
                        if (t.equals("C")) {
                            return v.charAt(0);
                        }
                        if (t.equals("H")) {
                            return Short.parseShort(v);
                        }
                        if (t.equals("I")) {
                            return Integer.parseInt(v);
                        }
                        if (t.equals("L")) {
                            return Long.parseLong(v);
                        }
                        if (t.equals("F")) {
                            return Float.parseFloat(v);
                        }
                        if (t.equals("D")) {
                            return Double.parseDouble(s);
                        }
                        if (t.equals("B")) {
                            return Boolean.parseBoolean(v);
                        }
                        if (t.equals("G") ) { 
                            return new BigDecimal(v);
                        }
                        if(t.equals("T")) {//UTIL DATE
                            java.util.Date u=null;
                                    SimpleDateFormat sdf = new SimpleDateFormat(SESS_DATE_FMT);
                                    try {
                                            u = sdf.parse(v);
                                    } catch (Exception e) {
                                            e.printStackTrace();
                                            u=null;
                                    }
                                    return u;
                                    
                        }
                        if(t.equals("Q")) {//SQL DATE
                            java.util.Date u=null;
                            
                                    SimpleDateFormat sdf = new SimpleDateFormat(SESS_DATE_FMT);
                                    try {
                                            u = sdf.parse(v);
                                    } catch (Exception e) {
                                            e.printStackTrace();
                                            u=null;
                                    }
                                    if(u!=null)
                                    {
                                            return new java.sql.Date(u.getTime());
                                    }
                                    return null;
                        } 
                    }
                    
                return null;
            }
        
     public static String encodeSession(HttpSession session,HashMap<String,Object> m) {
                    
                    List<String> params=new ArrayList<String>();
                    
                    try{
                            if(session!=null)
                            {
                                    Enumeration keys = session.getAttributeNames();
                                    while (keys.hasMoreElements())
                                    {
                                      String key = (String)keys.nextElement();
                                      Object val = session.getAttribute(key);
                                      //System.out.println(key + ": " + val); 
                                      if(val!=null && key!=null){
                                              
                                              if(    !(key.toLowerCase().contains("oracle"))
                                                  && !(key.toLowerCase().contains("apache"))
                                                  && !(key.toLowerCase().contains("adf"))
                                                  && !(key.toLowerCase().contains("fm_"))
                                                  && !(key.toLowerCase().contains("fx_"))
                                                      )
                                               {
                                                  String s = ISession.serializeObj(val); 
                                                  if(s!=null)
                                                  {
                                                     params.add(key+"=>"+s); 
                                                     //System.out.println(key + "=>" + val); 
                                                  } 
                                               }
                                              
                                      }
                                    }
                            }
                            if(m!=null)
                            {
                                    Iterator it = m.entrySet().iterator();
                                while (it.hasNext()) {
                                    Map.Entry pair = (Map.Entry)it.next();
                                    //System.out.println(pair.getKey() + " = " + pair.getValue());
                                    it.remove(); // avoids a ConcurrentModificationException
                                    String key = (String)pair.getKey();
                                    Object val = pair.getValue();
                                      //System.out.println(key + ": " + val);
                                        if(val!=null && key!=null){
                                                
                                                if(    !(key.toLowerCase().contains("oracle"))
                                                    && !(key.toLowerCase().contains("apache"))
                                                    && !(key.toLowerCase().contains("adf"))
                                                    && !(key.toLowerCase().contains("fm_"))
                                                        )
                                                 {
                                                    String s = ISession.serializeObj(val); 
                                                    if(s!=null)
                                                    {
                                                       params.add(key+"=>"+s); 
                                                      // System.out.println(key + "=>" + val); 
                                                    } 
                                                 }
                                                
                                        }
                                }
                            }
                            if(params.size()>0)
                            {
                                    String sz=GlobalCC.join(params, ",");
                                    //System.out.println("SZ_SESSION: "+sz);
                                    String en=com.Ostermiller.util.Base64.encode(sz);
                                    //System.out.println("EN_SESSION: "+en);
                                    return en;
                            }
                            return null;
                    }catch(Exception e){
                      GlobalCC.EXCEPTIONREPORTING(e);  
                    }
                    return null;
                }
        public static HashMap<String,Object> decodeSession(String params) {
                //System.out.println("Session Decoding....");
                HashMap<String,Object> m=new HashMap();
                try{
                    if(params!=null && "".equals(params)!=true)
                    {       
                            String s=com.Ostermiller.util.Base64.decode(params);
                            //System.out.println("decoded: "+s);
                            if(s!=null && "".equals(s)!=true)
                            {
                                 String[] tokens=s.trim().split(",");
                                 for(String t: tokens)
                                 {
                                         if(!t.isEmpty())
                                         {
                                                 String[] v=t.split("=>");
                                                 if( v.length==2) 
                                                 {
                                                     String k=v[0];
                                                     Object n = ISession.unSerializeObj(v[1]);
                                                     //System.out.println("cloned: "+k+" => "+ n);
                                                     if( k!=null && n!=null) {//not set
                                                         m.put(k, n);
                                                     }
                                                 }
                                                 
                                         }
                                 }        
                            }   
                    }
                }catch(Exception e){
                  GlobalCC.EXCEPTIONREPORTING(e);  
                }
               return m;
            }
        
        public static String cloneSession(HttpSession session,HashMap<String,Object> params) {
            //clone part of the session variables and dont overight.
                try{
                    if(params!=null )
                    {
                        Iterator it = params.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            it.remove(); // avoids a ConcurrentModificationException
                            String k = (String)pair.getKey();
                            Object v = pair.getValue();
                            session.setAttribute(k, v); 
                        }
                        //ISession.dump(session);
                    }
                }catch(Exception e){
                  GlobalCC.EXCEPTIONREPORTING(e);  
                }
               return null;
            }
        
        public static String dump(HttpSession session) {
                try{
                      if(session!=null) {
                         Enumeration keys = session.getAttributeNames();
                        while (keys.hasMoreElements())
                        {
                          String key = (String)keys.nextElement();
                          Object val=session.getAttribute(key);
                          System.out.println("session[" + key + "] => " + val);
                        } 
                      }
                }catch(Exception e){
                  GlobalCC.EXCEPTIONREPORTING(e);  
                }
               return null;
         }
    public static String getRequestedPage(  HttpServletRequest request) {
                
            String url = request.getRequestURI(); 
            //System.out.println("CURRENT URI: "+url);
            if(url!=null && "".equals(url)!=true){
                int firstSlash = url.indexOf("/faces/",1); 
                String requestedPage = null; 
                if (firstSlash != -1) {
                    requestedPage = url.substring(firstSlash + 1, url.length()); 
                    return requestedPage.substring(6);
                }  
                return null; 
            }
            return null;
     }   
    
    public static void setCookie( HttpServletResponse response, String name, String value, int maxAge)
           
    {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/CRM");
        cookie.setSecure(true);
        response.addCookie(cookie);
        
    }
    
    public static boolean userExists(String username,BigDecimal usercode) {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        ResultSet rs = null;
        int found=0;
        if( (username!=null) && (usercode!=null) ) {
            try{
                conn = dbConnector.getDatabaseConnection();
                if(conn == null) {
                    String message = "Error Connecting to Database. Contact Your System Administrator";
                    GlobalCC.errorValueNotEntered(message);
                }
                String query ="SELECT count(1) FROM tqc_users WHERE usr_code = ? AND usr_username = ? "; 

                stmt = (OracleCallableStatement)conn.prepareCall(query);
                stmt.setBigDecimal(1,usercode);
                stmt.setString(2,username);
                rs=stmt.executeQuery();
                
                if(rs.next())
                {
                    found=rs.getInt(1);
                }
                //overright with new values
            }  catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            }finally{
                DbUtils.closeQuietly(conn, stmt, rs);
            }
        }
        return  (found == 1) ; 
    }
    public static String getParameter(String param_name, String sessionName,HttpSession session) {

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        try {
            conn = dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.getParameter(?); end;";
            stmt = (OracleCallableStatement)conn.prepareCall(query1);
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            stmt.setString(2, param_name);

            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);
            while (rs.next()) {
                session.setAttribute(sessionName, rs.getString(1));

            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }
    
     public static String sysParameters(HttpSession session) {
        session.setAttribute("screenWidth", 1000);
        session.setAttribute("screenHeight", 1000);
        session.setAttribute("DOB_REQUIRED", null);
        session.setAttribute("SMS_REQUIRED", null);
        session.setAttribute("EMAIL_REQUIRED", null);
        session.setAttribute("TELEPHONE_REQUIRED", null);
        session.setAttribute("RELATIONSHIP_OFFICER_REQUIRED", null);
 
        ISession.checkDefaultSite(session); // Parameterised the Default Site
        ISession.checkIfDivisionApplicable(session);
        
        ISession.getParameter("STATE_REQUIRED", "STATE_REQUIRED",session); 
        ISession.getParameter("CREDIT_RATING_REQUIRED", "CREDIT_RATING_REQUIRED",session);
        ISession.getParameter("MANAGER_LABEL", "MANAGER",session);
        ISession.getParameter("ID_NO_LABEL", "ID_NO_LABEL",session);
        ISession.getParameter("PAYROLL_NO_LABEL", "PAYROLL_NO_LABEL",session);
        ISession.getParameter("ID_NO_FORMAT", "ID_NO_FORMAT",session);
        ISession.getParameter("CHANNEL_MANAGER", "CHANNEL_MANAGER",session);
        ISession.getParameter("DISTRICT", "DISTRICT",session);
        ISession.getParameter("CLIENT_CONTACT_DEBTOR", "CLIENT_CONTACT_DEBTOR",session);
        ISession.getParameter("PIN_OR_PASS_MAND", "PIN_OR_PASS_MAND",session);
        ISession.getParameter("CLIENT", "CLIENT",session);

        ISession.checkIfClientDOBRequired(session);
        ISession.checkIfRelationshRequired(session);
        ISession.checkIfClientSMSRequired(session);
        ISession.checkIfTelephone(session);
        ISession.checkIfEmailRequired(session);
        ISession.checkIfPhysicalAddrRequired(session);
        ISession.getUserDefaults(session);
        ISession.establishmentLogo(session);
        ISession.setupFormFields(session);
        
        BigDecimal orgCode = GlobalCC.checkBDNullValues(session.getAttribute("orgCode"));
        ISession.getOrgType(orgCode, "ORG_TYPE",session);
        
        ISession.findUserActiveSystems(session); 
        //restartAllStoppedJobs(); 
        return null;
    }
    
    public static List<sys> findUserActiveSystems(HttpSession session) {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn;
        conn = dbConnector.getDatabaseConnection();
        List<sys> userSys = new ArrayList<sys>();

        OracleCallableStatement callStmt = null;
        try {
            String hrQuery =
                "begin ? := TQC_ROLES_CURSOR.Get_User_Active_Systems(?); end;";
            callStmt = (OracleCallableStatement)conn.prepareCall(hrQuery);
            //bind the variables
            callStmt.registerOutParameter(1,
                                          OracleTypes.CURSOR); //Return value
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("UserCode"));
            java.lang.System.out.println("UserCode");
            java.lang.System.out.println(session.getAttribute("UserCode"));
            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);
            
            session.removeAttribute("systemMap");
            session.removeAttribute("systems");
            HashMap map = new HashMap();
            while (rs.next()) {
                sys sys = new sys();
                sys.setUsysCode(rs.getBigDecimal(1));
                map.put(rs.getBigDecimal(2).toString(), rs.getString(7)); 
                sys.setSysCode(rs.getBigDecimal(2));
                sys.setSysShtDesc(rs.getString(3));
                sys.setUsysWef(rs.getDate(4));
                sys.setSysDesc(rs.getString(5));
                sys.setUsysWet(rs.getDate(6));
                sys.setSyspath(rs.getString(7));
                sys.setUsrCode((BigDecimal)session.getAttribute("UserCode"));
                userSys.add(sys);
                
                //java.lang.System.out.println("active-system: "+rs.getBigDecimal(2));
            }
            session.setAttribute("systemMap", map);
            session.setAttribute("systems", map);
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            //   GlobalCC.ExceptionEncountered(e);
        }
        return userSys;
    }
    public static List<sys> findUserSystems(HttpSession session) {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn;
        conn = dbConnector.getDatabaseConnection();
        List<sys> userSys = new ArrayList<sys>();

        OracleCallableStatement callStmt = null;
        try {
            String hrQuery =
                "begin ? := TQC_ROLES_CURSOR.Get_User_Systems(?); end;";
            callStmt = (OracleCallableStatement)conn.prepareCall(hrQuery);
            //bind the variables
            callStmt.registerOutParameter(1,
                                          OracleTypes.CURSOR); //Return value
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("UserCode"));
            java.lang.System.out.println("UserCode");
            java.lang.System.out.println(session.getAttribute("UserCode"));
            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);
            session.removeAttribute("systemMap");
            HashMap map = new HashMap();
            while (rs.next()) {
                sys sys = new sys();
                sys.setUsysCode(rs.getBigDecimal(1));
                map.put(rs.getBigDecimal(2).toString(), rs.getString(7)); 
                sys.setSysCode(rs.getBigDecimal(2));
                sys.setSysShtDesc(rs.getString(3));
                sys.setUsysWef(rs.getDate(4));
                sys.setSysDesc(rs.getString(5));
                sys.setUsysWet(rs.getDate(6));
                sys.setSyspath(rs.getString(7));
                sys.setUsrCode((BigDecimal)session.getAttribute("UserCode"));
                userSys.add(sys);
            }
            session.setAttribute("systemMap", map);
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            //   GlobalCC.ExceptionEncountered(e);
        }
        return userSys;
    }
    public static String getOrgType(BigDecimal orgCode, String sessionName,HttpSession session) {

        System.out.println("erroorrr herer"+orgCode);
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        try {
            connection = dbConnector.getDatabaseConnection();
            String query =
                "begin SELECT org_type \n" + 
                " INTO ? \n" + 
                "     FROM tqc_organizations \n" + 
                "     WHERE org_code=? and rownum=1; end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.VARCHAR);
            stmt.setBigDecimal(2, orgCode);
               stmt.execute(); 
            session.setAttribute(sessionName, GlobalCC.checkNullValues(stmt.getString(1))); 
            stmt.close(); 
            connection.close();
            if( session.getAttribute(sessionName)==null)
            {
              GlobalCC.EXCEPTIONREPORTING("Org_Type for Org_Code "+orgCode+" not defined!");
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e); 
        }
        return null;
    }

    
    
    public static String establishmentLogo(HttpSession session) {
        OracleConnection conn = null;
        File myfile = null;
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            if(context==null) {
                return null;
            }
            ExternalContext ec =
                context.getExternalContext();
            if(ec==null) {
                return null;
            }
            ServletContext sc =(ServletContext)ec.getContext();
            if(sc==null) {
                return null;
            }
            String strBarCodeImage = "/images/" + session.getAttribute("orgCode") + ".gif";
            strBarCodeImage = sc.getRealPath(strBarCodeImage);
            
            DBConnector ds = new DBConnector();
            conn = ds.getDatabaseConnection();
            OracleCallableStatement callStmt = null;
            String hrQuery = null;
            hrQuery =
                    "SELECT ORG_RPT_LOGO FROM TQC_ORGANIZATIONS WHERE ORG_CODE = " +
                    session.getAttribute("orgCode") + " ";
            callStmt = (OracleCallableStatement)conn.prepareCall(hrQuery);
            //bind the variables

            OracleResultSet rs = (OracleResultSet)callStmt.executeQuery();
            myfile = new File(strBarCodeImage);
            System.out.println(myfile.length());
            System.out.println(strBarCodeImage);
            if (myfile.length() == 0) {
                while (rs.next()) {
                    byte barray[] = rs.getBytes(1);
                    FileOutputStream out =
                        new FileOutputStream(strBarCodeImage);
                    out.write(barray);
                    out.close();
                }

                myfile = new File(strBarCodeImage);
                rs.close();
                callStmt.close();
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }
    
    
    public static String getUserDefaults(HttpSession session) {
        String query =
            "begin ? := tqc_roles_cursor.get_user_defaults(?); end;";
        CallableStatement cst = null;
        DBConnector handler = new DBConnector();
        OracleConnection connection = null;
        Branch branch = new Branch();
        try {
            if (session.getAttribute("UserCode") != null) {
                connection = handler.getDatabaseConnection();

                cst = connection.prepareCall(query);
                cst.registerOutParameter(1, OracleTypes.CURSOR);
                cst.setBigDecimal(2,
                                  new BigDecimal(session.getAttribute("UserCode").toString()));
                cst.execute();

                OracleResultSet rs = (OracleResultSet)cst.getObject(1);
                int k = 0;
                while (rs.next()) {

                    branch.setOrgCode(rs.getBigDecimal(1));
                    branch.setOrgName(rs.getString(2));
                    branch.setRegCode(rs.getString(3));
                    branch.setRegionName(rs.getString(4));
                    branch.setCode(rs.getBigDecimal(5));
                    branch.setName(rs.getString(6));
                    // branch.setCurrencyCode(rs.getString(7));
                    // branch.setCurrencyDesc(rs.getString(8));
                    branch.setCouCode(rs.getBigDecimal(7));
                    branch.setCouName(rs.getString(8));
                    branch.setAdminRgnType(rs.getString(9));
                    session.setAttribute("orgCode", branch.getOrgCode());
                    session.setAttribute("orgName", branch.getOrgName());
                    
                    session.setAttribute("DEFAULT_BRANCH_CODE", branch.getCode());
                    session.setAttribute("DEFAULT_BRANCH_NAME", branch.getName());
                    session.setAttribute("userDefaultReg", branch.getRegCode());
                    session.setAttribute("COUNTRY_CODE", branch.getCouCode());
                    session.setAttribute("COUNTRY_NAME", branch.getCouName());
                    session.setAttribute("ADMIN_REG_TYPE",branch.getAdminRgnType());
                    session.setAttribute("zipCode", rs.getString(11));
                    session.setAttribute("CouZipCode", rs.getString(11));
                    k++;
                }

                if (k == 0) {
                   // GlobalCC.INFORMATIONREPORTING("User Default Branch Not Defined.Please set default Branch as soon as you LOGIN  ::<b>Click Ok To continue<b>");

                } else {
                    cst.close();
                    rs.close();
                    connection.close();

                    Date today = new Date();
                    String currentYear =
                        GlobalCC.parseYearDate(today.toString());
                    String currentPeriod =
                        GlobalCC.parseMonth(today.toString()).toUpperCase();
                    session.setAttribute("yerYear", currentYear);
                    session.setAttribute("period", currentPeriod);


                }

            } else {
              //  GlobalCC.INFORMATIONREPORTING("Error Getting  user defaults:: ");
                return null;


            }
        } catch (Exception e) {
           // GlobalCC.EXCEPTIONREPORTING(connection, e);
            return null;
        }

        return null;

    }
    
    
    public static void checkIfPhysicalAddrRequired(HttpSession session) {


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        int status = 0;
        int rankStatus = 0;
        try {

            //System.out.append("PHYSICAL ADDRESS");
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.PHYSICAL_ADDRESS_REQUIRED(); end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);
            while (rs.next()) {
                session.setAttribute("PHYSICAL_ADDRESS_REQUIRED",
                                     rs.getString(1));

            }
            System.out.println(session.getAttribute("PHYSICAL_ADDRESS_REQUIRED"));
            rs.close();

            stmt.close();
            connection.commit();
            connection.close();


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    
    public static void checkIfEmailRequired(HttpSession session) {
        String val=(String) GlobalCC.getSysParamValue("EMAIL_REQUIRED");
        session.setAttribute("EMAIL_REQUIRED",val);
    }
    
    
    public static void checkIfTelephone(HttpSession session) {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        int status = 0;
        int rankStatus = 0;
        try {


            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.checkif_TelephoneRequired(); end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);


            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);
            while (rs.next()) {
                session.setAttribute("TELEPHONE_REQUIRED", rs.getString(1));

            }
            rs.close();

            stmt.close();
            connection.commit();
            connection.close();


        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    
    
    public static String checkIfDivisionApplicable(HttpSession session) {


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        int status = 0;
        int rankStatus = 0;
        try {

            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.checkif_DIVISIONS_APPLIC(); end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);


            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);
            while (rs.next()) {
                session.setAttribute("div_applicable", rs.getString(1));


            }

            rs.close();
            stmt.close();
            connection.commit();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
    
    public static String checkDefaultSite(HttpSession session) {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        int status = 0;
        int rankStatus = 0;
        try {


            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.findDefaultSite(); end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);


            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);
            while (rs.next()) {
                session.setAttribute("DEFAULT_SITE", rs.getString(1));
                System.out.println("DEFAULT_SITE "+rs.getString(1));
            }
            rs.close();

            stmt.close();
            connection.commit();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    
    
    public static void  checkIfClientDOBRequired(HttpSession session) {


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        try {


            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.checkif_dob_required(); end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);


            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);
            while (rs.next()) {
                session.setAttribute("DOB_REQUIRED", rs.getString(1));

            }
            rs.close();
            stmt.close();
            connection.commit();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
    }

    public static void checkIfRelationshRequired(HttpSession session) {
        
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;

        try {


            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? :=TQC_PARAMETERS_PKG.get_param_varchar('RELATIONSHIP_OFFICER_REQUIRED'); end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.registerOutParameter(1, OracleTypes.VARCHAR);
            stmt.execute();
            String value = stmt.getString(1);

            session.setAttribute("RELATIONSHIP_OFFICER_REQUIRED", value);
            stmt.close();
            connection.commit();
            connection.close();


        } catch (Exception e) {
        }
    }

    public static void checkIfClientSMSRequired(HttpSession session) {


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        int status = 0;
        int rankStatus = 0;
        try {


            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.checkif_sms_required(); end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);


            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);
            while (rs.next()) {
                session.setAttribute("SMS_REQUIRED", rs.getString(1));

            }
            rs.close();

            stmt.close();
            connection.commit();
            connection.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
    }

    public static void forceLogOut() {


        OracleConnection conn = null;
        try {

            DBConnector ds = new DBConnector();

            conn = ds.getLogOutDatabaseConnection();
            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)conn.prepareCall("begin tqc_web_pkg.user_log_out(?,?,?); end;");
            callStmt.setObject(1, null);
            callStmt.setObject(2, GlobalCC.sysCode);
            callStmt.setObject(3, "SYSTEM");
            callStmt.executeQuery();
            callStmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            //GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

    }

    private static void setupFormFields(HttpSession session) { 
        OracleConnection conn = null;
        OracleResultSet rs=null;
        OracleCallableStatement callStmt = null;
        DBConnector ds = new DBConnector();  
        try {  
            if( session != null )
            {
                    String sql = "SELECT * FROM tqc_form_fields";  
                   
                    conn = ds.getDatabaseConnection();
                    
                    callStmt =(OracleCallableStatement)conn.prepareCall(sql);
                    rs=(OracleResultSet)callStmt.executeQuery();
                    while(rs.next()){
                        String name=GlobalCC.checkNullValues(rs.getString("FM_FIELD_NAME"));
                        String visible=GlobalCC.checkNullValues(rs.getString("FM_VISIBLE"));
                        String label=GlobalCC.checkNullValues(rs.getString("FM_FIELD_LABEL"));
                        String lmsMand=GlobalCC.checkNullValues(rs.getString("FM_LMS_MANDATORY"));
                        String gisMand=GlobalCC.checkNullValues(rs.getString("FM_GIS_MANDATORY"));
                        String required=GlobalCC.checkNullValues(rs.getString("FM_MANDATORY")); 
                        String disabled=GlobalCC.checkNullValues(rs.getString("FM_DISABLED")); 
                        /*always overight session*/
                        session.setAttribute("FX_"+name+".visible",visible);
                        session.setAttribute("FX_"+name+".required",required);
                        session.setAttribute("FX_"+name+".label",label);
                        session.setAttribute("FX_"+name+".required",required);
                        session.setAttribute("FX_"+name+".gisRequired",gisMand);
                        session.setAttribute("FX_"+name+".lmsRequired",lmsMand); 
                        session.setAttribute("FX_"+name+".disabled",disabled); 
                    } 
                   //ISession.dump(session);
            } 
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING( e);
        }finally{
            DbUtils.closeQuietly(conn,callStmt,rs);
        }
    }
}


