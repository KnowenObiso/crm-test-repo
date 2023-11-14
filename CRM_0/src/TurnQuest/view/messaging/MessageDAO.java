package TurnQuest.view.messaging;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;


public class MessageDAO {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);


    public List<MessageBean> findMessageTemplates() {

        String query =
            "begin ? := TQC_SETUPS_CURSOR.get_em_template_by_type(?,?); end;";
        OracleCallableStatement cst = null;
        List<MessageBean> templates = new ArrayList<MessageBean>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {
            conn = datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setObject(2, session.getAttribute("sysCode"));
            cst.setObject(3, session.getAttribute("messageType"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            templates = new ArrayList<MessageBean>();
            while (rs.next()) {
                MessageBean template = new MessageBean();
                template.setMsgtCode(rs.getBigDecimal(1));
                template.setMsgtShtDesc(rs.getString(2));
                template.setMsgtMsg(rs.getString(3));
                template.setMsgtSysModule(rs.getString(5));
                template.setMsgtType(rs.getString(6));
                templates.add(template);
            }
            rs.close();
            cst.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return templates;
    }

    public List<MessageBean> findCountries() {

        String query = "begin ? := gis_setups_cursor.getCountries; end;";
        OracleCallableStatement cst = null;
        List<MessageBean> templates = new ArrayList<MessageBean>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {
            conn = datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            templates = new ArrayList<MessageBean>();
            while (rs.next()) {
                MessageBean template = new MessageBean();
                template.setCountryCode(rs.getBigDecimal(1));
                template.setCouShtDesc(rs.getString(2));
                template.setCounntryName(rs.getString(3));
                templates.add(template);
            }
            rs.close();
            cst.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return templates;
    }

    public List<MessageBean> findCounties() {

        String query = "begin ? := gis_setups_cursor.getCounties(?); end;";
        OracleCallableStatement cst = null;
        List<MessageBean> templates = new ArrayList<MessageBean>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {
            conn = datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("countryCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            templates = new ArrayList<MessageBean>();
            while (rs.next()) {
                MessageBean template = new MessageBean();
                template.setStateCode(rs.getBigDecimal(1));
                template.setCounCode(rs.getBigDecimal(2));
                template.setStateShtDesc(rs.getString(3));
                template.setStateName(rs.getString(4));
                templates.add(template);
            }
            rs.close();
            cst.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return templates;
    }

}
