package TurnQuest.view.Base;


import TurnQuest.view.Connect.DBConnector;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;


public class Label {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public Label() {
        super();
        getLabels("REGION");
        getLabels("UNIT");
        getLabels("TOWN");
    }

    public String getLabels(String param_name) {


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        try { 
            connection = dbConnector.getDatabaseConnection();
            String query = "SELECT label_name, label_value\n" + 
            "           FROM tqc_labels\n" + 
            "          WHERE label_status = 'ACTIVE' AND label_name = ':v_labelname' ";
            query=query.replaceAll(":v_labelname",param_name);
            stmt = (OracleCallableStatement)connection.prepareCall(query); 
            rs = (OracleResultSet)stmt.executeQuery();
            while (rs.next()) {
                session.setAttribute(param_name, rs.getString(2)); 
            } 
            rs.close();

            stmt.close();
            connection.commit();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();

        }
        return null;
    }

    public String getRegion() {
     
        String val =
            (String)GlobalCC.getSysParamValue("TOGGLE_ORG_TAB_NAMES");
        
        if ("Y".equalsIgnoreCase(val)) {
            return "Region";
        } else{
        if (session.getAttribute("REGION") == null) {
            return "Region";
        } else {
            return session.getAttribute("REGION").toString();
        }
        }

    }
    
    
    public String getRegDivName(){
        String val =
            (String)GlobalCC.getSysParamValue("TOGGLE_ORG_TAB_NAMES");
        
        if ("Y".equalsIgnoreCase(val)) {
            return "Zone";
        } else {
            return "Division";
        } 
    }
    
    
        
    public String getRegBraDivName(){
        String val =
            (String)GlobalCC.getSysParamValue("TOGGLE_ORG_TAB_NAMES");
        
        if ("Y".equalsIgnoreCase(val)) {
            return "Branch Zones";
        } else {
            return "Branch Divisions";
        } 
    }
           
           
    public String getRegSubDialogName(){
        String val =
            (String)GlobalCC.getSysParamValue("TOGGLE_ORG_TAB_NAMES");
        
        if ("Y".equalsIgnoreCase(val)) {
            return "Sub Zone";
        } else {
            return "Sub Division";
        } 
    }
    
    public String getRegSubDivName(){
        String val =
            (String)GlobalCC.getSysParamValue("TOGGLE_ORG_TAB_NAMES");
        
        if ("Y".equalsIgnoreCase(val)) {
            return "Zones Subzones";
        } else {
            return "Division Subdivisions";
        } 
    }
    
    public String getSalesOffAgencyName(){
        String val =
            (String)GlobalCC.getSysParamValue("TOGGLE_ORG_TAB_NAMES");
        
        if ("Y".equalsIgnoreCase(val)) {
            return "Sales Office";
        } else {
            return "Agencies";
        }
    }
    
    public String getTabtitleSales(){
        String val =
            (String)GlobalCC.getSysParamValue("TOGGLE_ORG_TAB_NAMES");
        
        if ("Y".equalsIgnoreCase(val)) {
            return "ADD/EDIT BRANCH SALES OFFICE";
        } else {
            return "ADD/EDIT BRANCH AGENCIES";
        }
    }
    public String getClusterTitle(){
        String val =
            (String)GlobalCC.getSysParamValue("TOGGLE_ORG_TAB_NAMES");
        
        if ("Y".equalsIgnoreCase(val)) {
            return "ADD/EDIT BRANCH CLUSTERS";
        } else {
            return "Add/Edit Branch Agency Unit";
        }
    }
    
    
    public String getRegTabTitle(){
        String val =
            (String)GlobalCC.getSysParamValue("TOGGLE_ORG_TAB_NAMES");
        
        if ("Y".equalsIgnoreCase(val)) {
            return "  ADD/EDIT ORGANIZATION ZONE";
        } else {
            return "ADD/EDIT ORGANIZATION REGION";
        }
    }
    
    
    public String getPopUpName(){
        String val =
            (String)GlobalCC.getSysParamValue("TOGGLE_ORG_TAB_NAMES");
        
        if ("Y".equalsIgnoreCase(val)) {
            return "Zone Definition";
        } else {
            return "Division Definition";
        }
    }
    
    
 
    
    public String getLabelOne(){
        String val =
            (String)GlobalCC.getSysParamValue("TOGGLE_ORG_TAB_NAMES");
        
        if ("Y".equalsIgnoreCase(val)) {
            return "Zone Order";
        } else {
            return "Division Order";
        }
    }
    
    
    public String getLabelTwo(){
        String val =
            (String)GlobalCC.getSysParamValue("TOGGLE_ORG_TAB_NAMES");
        
        if ("Y".equalsIgnoreCase(val)) {
            return "Zone Status";
        } else {
            return "Division Status";
        }
    }
    
    
    public String getLabelThree(){
        String val =
            (String)GlobalCC.getSysParamValue("TOGGLE_ORG_TAB_NAMES");
        
        if ("Y".equalsIgnoreCase(val)) {
            return "Zone Director";
        } else {
            return "Division Director";
        }
    }
    
    public String getLabelFour(){
        String val =
            (String)GlobalCC.getSysParamValue("TOGGLE_ORG_TAB_NAMES");
        
        if ("Y".equalsIgnoreCase(val)) {
            return "Ass Director";
        } else {
            return "Div Assist Director";
        }
    }
    
    public String getAgentsTeamName(){
        String val =
            (String)GlobalCC.getSysParamValue("TOGGLE_ORG_TAB_NAMES");
        
        if ("Y".equalsIgnoreCase(val)) {
            return "Teams";
        } else {
            return "Agents";
        }
    }

    public String getUnits() {
        
        String val =
            (String)GlobalCC.getSysParamValue("TOGGLE_ORG_TAB_NAMES");
        
        if ("Y".equalsIgnoreCase(val)) {
            return "Clusters";
        } else{
        if (session.getAttribute("UNIT") == null) {
            return "Unit";
        } else {
            return session.getAttribute("UNIT").toString();
        }
        }
    }

    public String getTowns() {
        if (session.getAttribute("TOWN") == null) {
            return "Town";
        } else {
            return session.getAttribute("TOWN").toString();
        }

    }
}
