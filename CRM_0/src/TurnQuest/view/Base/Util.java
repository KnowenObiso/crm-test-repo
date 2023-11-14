package TurnQuest.view.Base;


import TurnQuest.view.Connect.DBConnector;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;

import org.apache.commons.dbutils.DbUtils;


public class Util {

    public String getDefaultSite() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "ID_NUMBER_REQUIRED");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //  System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getRegNumber() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "REG_NUMBER_REQ");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //   System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getRenTimes() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "RENEWAL_MESSAGES_TIMES");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            // System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getPinValue() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "PIN_NUMBER_VALUE");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //   System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getRelationShipMand() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "RELATIONSHIP_OFFICER_MANDATORY");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getClientAgencyTying() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "ENABLE_TYING_CLIENTS_AGENTS");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }
  public String getClientTying() {
      DBConnector myConn = new DBConnector();
      Connection conn = myConn.getDatabaseConnection();
      String delink = "NONE";
      CallableStatement cst = null;
      try {
          cst =
  conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
          cst.registerOutParameter(1, OracleTypes.VARCHAR);
          cst.setString(2, "ENABLE_TYING_CLIENTS_AGENTS");
          cst.execute();
          delink = cst.getString(1);

      } catch (SQLException e) {
          //System.out.println("Error.." + e.getMessage());
      } finally {
          DbUtils.closeQuietly(conn, cst, null);
      }

      return delink;
  }

    public String getBirthDay() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "SEND_BIRTHDAY_MESSAGES");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getIdPassPortMand() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "ID_OR_PASSPORT_MAND");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getInHouseMand() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "INHOUSE_AGENTS_ID_PIN_BRN");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getDriverExpNumber() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "DRIVER_EXP_NUMBER");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getAgencyPinMand() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "AGENCY_PIN_MAND");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getRegCodeMand() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "REG_CODE_MANDATORY");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String defaultUserBranch() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "DEFAULT_USER_BRANCH");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getBankBranchAccountMand() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "BANK_BRANCH_ACCOUNT_MAND");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getShowRegion() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "LOCATION_APPLICABLE");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getStateMand() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "STATE_MANDATORY");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getTownMand() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "TOWN_MANDATORY");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getInvoiceMand() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "INVOICE_NUMBER_MANDATORY");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getPinNumberValidation() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "PIN_NUMBER_VALIDATION");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getSingleSignOn() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "SINGLESIGNON");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getAgencyEmailMand() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "AGENCY_EMAIL_MANDATORY");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getCountryMand() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "AGENCY_COUNTRY_MANDATORY");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getCountyMand() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "AGENCY_COUNTY_MANDATORY");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getAgencyTownMand() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "AGENCY_TOWN_MANDATORY");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getClientCountryMand() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "CLIENT_COUNTRY_MANDATORY");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getEntityApplicable() {
        DBConnector myConn = new DBConnector();
        OracleConnection conn = null;
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            conn = myConn.getDatabaseConnection();
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "ENTITY_APPLICABLE");
            cst.execute();
            delink = cst.getString(1);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn,e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getDivisionApplicable() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "DIVISIONS_APPLIC");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getSaccoDetails() {
        DBConnector myConn = new DBConnector();
        OracleConnection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "SACCO_VISIBLE");
            cst.execute();
            delink = cst.getString(1);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getActiveSystems() {
        DBConnector myConn = new DBConnector();
        OracleConnection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_ACTIVE_SYSTEMS(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setObject(2, 27);
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING( conn, e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getChecked() {
        DBConnector myConn = new DBConnector();
        OracleConnection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "CHECKED_VISIBLE");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING( conn, e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getParameterValue(String parameter) {
        DBConnector myConn = new DBConnector();
        OracleConnection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, parameter);
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING( conn, e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getActiveSystems(int syscode) {
        DBConnector myConn = new DBConnector();
        OracleConnection conn =null;
        String delink = "NONE";
        CallableStatement cst = null;
        try {
             conn = myConn.getDatabaseConnection();
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_ACTIVE_SYSTEMS(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setObject(2, syscode);
            cst.execute();
            delink = cst.getString(1);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING( conn, e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getIdRegNumber() {
        DBConnector myConn = new DBConnector();
        OracleConnection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "ID_NO_LABEL");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING( conn, e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getIdNumberFormat() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "ID_NUMBER_FORMATS");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public String getDlMand() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "DL_MANDATORY");
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }
    public static HashMap<String,String> strToMap(String s)
            {
                    HashMap<String,String> map=null;
                    try{ 
                            if(s!=null && !"".equals(s))
                            {
                                    if(!s.trim().isEmpty())
                                    {
                                            map=new HashMap<String,String>();
                                            String[] tokens=s.trim().split(",");
                                            for(String t: tokens)
                                            {
                                                    if(!t.isEmpty())
                                                    {
                                                            String[] v=t.split("=>");
                                                            map.put(v[0], v[1]);
                                                    }
                                            }
                                    }
                            }
                    }catch(Exception e)
                    {
                            e.printStackTrace();
                    }
                    return map;
            }
    public static boolean validatePassport(String passport, String type) {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
            conn.prepareCall("begin ? := tqc_clients_pkg.validate_passport_no(?,?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, passport);
            cst.setString(3, type);
            cst.execute();
            delink = cst.getString(1);

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return "Y".equalsIgnoreCase(delink);
    }

}
