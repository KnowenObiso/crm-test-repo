package TurnQuest.view.Memos;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.MessageTemplate;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;


public class MemoDAO {
    public MemoDAO() {
        super();
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public List<Memo> findSystemMemos() {
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();
        List<Memo> orgData = new ArrayList<Memo>();
        String query = "begin TQC_SETUPS_CURSOR.get_systems(?); end;";

        //List<memoValues> MemoValues=null;
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {
                Memo MemoDef = new Memo();
                MemoDef.setSysCode(rs.getBigDecimal(1));
                MemoDef.setSysShortDesc(rs.getString(2));
                MemoDef.setSysName(rs.getString(3));
                MemoDef.setType("S");
                MemoDef.setName(rs.getString(3));

                List<memoValues> values = new ArrayList<memoValues>();
                String sql =
                    "begin ? := TQC_COMMON_CURSORS.getMemoTypeInDtls(?,?); end;";
                OracleCallableStatement stmt2 =
                    (OracleCallableStatement)conn.prepareCall(sql);

                stmt2.registerOutParameter(1, OracleTypes.CURSOR);
                stmt2.setBigDecimal(2, rs.getBigDecimal(1));
                stmt2.setString(3, null);
                stmt2.execute();
                OracleResultSet rst = (OracleResultSet)stmt2.getObject(1);
                while (rst.next()) {
                    memoValues Memotype2 = new memoValues();
                    Memotype2.setMtypCode(rst.getBigDecimal(1));
                    Memotype2.setMtypSysCode(rst.getBigDecimal(2));
                    Memotype2.setMtypMemoType(rst.getString(3));
                    Memotype2.setAppAreaCode(rst.getString(4));
                    Memotype2.setAppAreaDesc(rst.getString(5));
                    Memotype2.setMtypStatus(rst.getString(6));
                    Memotype2.setMtypSubCode(rst.getBigDecimal(7));
                    Memotype2.setSclDesc(rst.getString(8));
                    Memotype2.setApplLvl(rst.getString(9));
                    Memotype2.setType("MT");
                    Memotype2.setName(rst.getString(3));
                    values.add(Memotype2);
                }
                MemoDef.setProduces(values);
                orgData.add(MemoDef);
                rst.close();
                stmt2.close();

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return orgData;
    }

    public List<Memo> findSubClasses() {
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin TQC_COMMON_CURSORS.find_sub_classes(?); end;";

        List<Memo> orgData = new ArrayList<Memo>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);
            while (rs.next()) {
                Memo MemoDef = new Memo();
                MemoDef.setSclCode(rs.getBigDecimal(1));
                MemoDef.setSclDesc(rs.getString(2));
                orgData.add(MemoDef);
            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return orgData;
    }

    public List<Memo> findMemoSubject() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query =
            "begin ? := TQC_COMMON_CURSORS.get_memos_subjects(?); end;";

        List<Memo> orgData = new ArrayList<Memo>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("mtypCode"));
            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);
            while (rs.next()) {
                Memo MemoDef = new Memo();
                MemoDef.setMemoCode(rs.getBigDecimal(1));
                MemoDef.setMemoSubject(rs.getString(2));
                MemoDef.setMemoMtypCode(rs.getBigDecimal(3));
                orgData.add(MemoDef);
            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return orgData;
    }

    public List<Memo> findMemoDetails() {
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin ? := TQC_COMMON_CURSORS.find_memo_dtls(?); end;";

        List<Memo> orgData = new ArrayList<Memo>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("memoCode"));
            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);
            session.setAttribute("memDtlsCode", null);
            while (rs.next()) {
                Memo MemoDef = new Memo();
                MemoDef.setMemdetCode(rs.getBigDecimal(1));
                MemoDef.setMemdetContent(rs.getString(2));
                MemoDef.setMemdetMemoCode(rs.getBigDecimal(3));
                session.setAttribute("memDtlsCode", rs.getBigDecimal(1));
                orgData.add(MemoDef);
            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return orgData;
    }

    public List<Memo> findMemoSubjectDetails() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin ? := TQC_COMMON_CURSORS.getMemoDtls(?); end;";

        List<Memo> orgData = new ArrayList<Memo>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("mtypCode"));
            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);
            while (rs.next()) {
                Memo MemoDef = new Memo();
                MemoDef.setMemoCode(rs.getBigDecimal(1));
                MemoDef.setMemoSubject(rs.getString(2));
                MemoDef.setMemoMtypCode(rs.getBigDecimal(3));
                MemoDef.setMemdetCode(rs.getBigDecimal(4));
                MemoDef.setMemdetContent(rs.getString(5));
                MemoDef.setMemdetMemoCode(rs.getBigDecimal(6));
                MemoDef.setSclCode(rs.getBigDecimal(7));
                MemoDef.setSclDesc(rs.getString(8));

                orgData.add(MemoDef);
            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return orgData;
    }

    public List<MessageTemplate> findMsgTemplate() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query =
            "begin ? := TQC_SETUPS_CURSOR.getMessageTemplates(?); end;";

        List<MessageTemplate> msgData = new ArrayList<MessageTemplate>();
        if (session.getAttribute("sysCode") != null) {
            try {

                OracleCallableStatement callStmt = null;
                callStmt = (OracleCallableStatement)conn.prepareCall(query);
                callStmt.registerOutParameter(1, OracleTypes.CURSOR);
                callStmt.setObject(2, session.getAttribute("sysCode"));
                callStmt.execute();
                OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);
                while (rs.next()) {

                    MessageTemplate msgTemp = new MessageTemplate();
                    msgTemp.setMsgtCode(rs.getBigDecimal(1));
                    msgTemp.setMsgtShtDesc(rs.getString(2));
                    msgTemp.setMsgtMsg(rs.getString(3));
                    msgTemp.setMsgtSysCode(rs.getBigDecimal(4));
                    msgTemp.setMsgtSysModule(rs.getString(5));
                    msgTemp.setMsgtType(rs.getString(6));

                    msgData.add(msgTemp);
                }
                rs.close();
                callStmt.close();
                conn.commit();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return msgData;
    }

    public List<MessageTemplate> findSmsTemplate() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query =
            "begin ? := TQC_SETUPS_CURSOR.get_sms_template(?); end;";

        List<MessageTemplate> msgData = new ArrayList<MessageTemplate>();
        try {

            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setObject(2, session.getAttribute("SystemCode"));
            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);
            while (rs.next()) {

                MessageTemplate msgTemp = new MessageTemplate();
                msgTemp.setMsgtCode(rs.getBigDecimal(1));
                msgTemp.setMsgtShtDesc(rs.getString(2));
                msgTemp.setMsgtMsg(rs.getString(3));
                msgTemp.setMsgtSysCode(rs.getBigDecimal(4));
                msgTemp.setMsgtSysModule(rs.getString(5));
                msgTemp.setMsgtType(rs.getString(6));

                msgData.add(msgTemp);
            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }
        return msgData;
    }

    public List<MessageTemplate> findEmailTemplate() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query =
            "begin ? := TQC_SETUPS_CURSOR.get_email_template(?); end;";

        List<MessageTemplate> msgData = new ArrayList<MessageTemplate>();

        try {

            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setObject(2, session.getAttribute("SystemCode"));
            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);
            while (rs.next()) {

                MessageTemplate msgTemp = new MessageTemplate();
                msgTemp.setMsgtCode(rs.getBigDecimal(1));
                msgTemp.setMsgtShtDesc(rs.getString(2));
                msgTemp.setMsgtMsg(rs.getString(3));
                msgTemp.setMsgtSysCode(rs.getBigDecimal(4));
                msgTemp.setMsgtSysModule(rs.getString(5));
                msgTemp.setMsgtType(rs.getString(6));

                msgData.add(msgTemp);
            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return msgData;
    }


}
