package TurnQuest.view.Activities;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;

import org.apache.commons.dbutils.DbUtils;


public class ServiceReqDAO {
    public ServiceReqDAO() {
        super();
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public List<ServiceReq> findRequestCats() {
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ?:= TQC_SERVICE_REQUESTS.getServReqCategories(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {

            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setObject(2, session.getAttribute("srtCode"));
            statement.execute();
            OracleResultSet rs = (OracleResultSet)statement.getObject(1);
            while (rs.next()) {

                ServiceReq request = new ServiceReq();
                request.setTsrcCode(rs.getBigDecimal(1));
                request.setTsrcName(rs.getString(2));
                request.setTsrcValidity(rs.getBigDecimal(3));
                request.setUsrCode(rs.getBigDecimal(4));
                request.setUsrName(rs.getString(5));
                request.setBrnName(rs.getString(6));
                request.setBrnCode(rs.getBigDecimal(7));
                //                request.setDepCode(rs.getBigDecimal(8));
                //                request.setTsrcType(rs.getString(9));
                request.setTsrcDefault(rs.getString(8));
                //                request.setDepName(rs.getString(11));
                service.add(request);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return service;
    }

    public List<ServiceReq> fetchRequestPolicies() {
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "SELECT POL_POLICY_NO, clnt_name || ' ' || clnt_other_names account_name FROM GIN_POLICIES,TQC_CLIENTS\n" +
            "WHERE POL_PRP_CODE=clnt_code and POL_PRP_CODE = :v_clnt_code\n" +
            "UNION \n" +
            "SELECT POL_POLICY_NO, prp_surname || ' ' || prp_other_names account_name from LMS_POLICIES,lms_proposers\n" +
            "WHERE POL_PRP_CODE=PRP_CODE and PRP_CLNT_CODE = :v_clnt_code ";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleResultSet rs = null;

        try {

            connection = dbConnector.getDatabaseConnection();

            BigDecimal clntCode =
                GlobalCC.checkBDNullValues(session.getAttribute("clntCode"));
            query =
                    query.replaceAll(":v_clnt_code", clntCode != null ? clntCode.toString() :
                                                     "-1");
            System.out.println("query: " + query);
            statement = (OracleCallableStatement)connection.prepareCall(query);

            rs = (OracleResultSet)statement.executeQuery();
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setTSR_POLICY_NO(rs.getString(1));
                request.setTSR_ACCOUNT_NAME(rs.getString(2));
                service.add(request);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        } finally {
            DbUtils.closeQuietly(connection, statement, rs);
        }

        return service;
    }

    public List<ServiceReq> findClientPropPols() {

        session.setAttribute("retrnAftaView", "N");
        session.setAttribute("viewUnderProp", "N");

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String medicalQuery =
                "begin TQC_SERVICE_REQUESTS.OrdClientPropPol(?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            //register out
            cst.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            // cst.setString(2, (String)session.getAttribute("medIDNo"));
            cst.setObject(2, session.getAttribute("clntCode"));

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setProposalNo(rs.getString(1));
                request.setPolicyNumber(rs.getString(2));
                request.setSumAssured(rs.getBigDecimal(3));
                request.setPolCode(rs.getBigDecimal(4));
                request.setUrlString(rs.getString(5));
                request.setEndrCode(rs.getString(9));
                service.add(request);
            }
            rs.close();
            cst.close();
            conn.close();
            //for screen LMS4/faces/clientDetails.jspx
            session.setAttribute("clientCode",
                                 session.getAttribute("clntCode"));
            session.setAttribute("ProcessSubAShtDesc", "ECLNT");
            //AddEdit = "E";
            session.setAttribute("addEdit", "E");
            session.setAttribute("createFromCrm", "N");
            session.setAttribute("enquiryMode", true);
            session.setAttribute("selectMode", true);
            session.setAttribute("sourcePage", "clientDetails");
            fetchClientDetails((BigDecimal)session.getAttribute("clntCode"),
                               session);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }

    public static void fetchClientDetails(BigDecimal clientCode,
                                          HttpSession session) {
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement callStmt = null;
        OracleResultSet rs = null;
        try {
            conn = datahandler.getDatabaseConnection();
            String query =
                "select prp_code, clnt_type,clnt_sht_desc " + " FROM tq_lms.lms_proposers, tq_crm.tqc_clients, tq_crm.tqc_serv_requests " +
                " WHERE clnt_code = tsr_acc_code " +
                "  AND clnt_code = prp_clnt_code " + "  AND clnt_code = ? ";

            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.setBigDecimal(1,
                                   clientCode); //session.getAttribute("clientCode")
            rs = (OracleResultSet)callStmt.executeQuery();
            if (rs.next()) {

                session.setAttribute("prpCode", rs.getBigDecimal("prp_code"));
                session.setAttribute("clientType", rs.getString("clnt_type"));
                session.setAttribute("clientShtDesc",
                                     rs.getString("clnt_sht_desc")); /**/
                System.out.println("prp_code: " +
                                   rs.getBigDecimal("prp_code") +
                                   "clnt_type: " + rs.getString("clnt_type") +
                                   "clnt_sht_desc: " +
                                   rs.getString("clnt_sht_desc"));
            }
            callStmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, callStmt, rs);
        }
    }

    public List<ServiceReq> findGroupPropPols() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String medicalQuery =
                "begin TQC_SERVICE_REQUESTS.GrpClientPropPol(?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            //register out
            cst.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            // cst.setString(2, (String)session.getAttribute("medIDNo"));
            cst.setObject(2, session.getAttribute("clntCode"));

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setProposalNo(rs.getString(1));
                request.setPolicyNumber(rs.getString(2));
                request.setSumAssured(rs.getBigDecimal(3));
                request.setPolCode(rs.getBigDecimal(4));
                request.setUrlString(rs.getString(5));
                service.add(request);
            }
            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }

    public List<ServiceReq> findGeneralPropPols() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String medicalQuery =
                "begin TQC_SERVICE_REQUESTS.GisClientPropPol(?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            //register out
            cst.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            // cst.setString(2, (String)session.getAttribute("medIDNo"));
            cst.setObject(2, session.getAttribute("clntCode"));
            cst.setObject(3, session.getAttribute("accountType"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setProposalNo(rs.getString(1));
                request.setPolicyNumber(rs.getString(2));
                request.setSumAssured(rs.getBigDecimal(3));
                request.setPolCode(rs.getBigDecimal(4));
                request.setUrlString(rs.getString(5));
                request.setType(rs.getString(7));
                service.add(request);
            }
            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }

    public List<Account> findAccounts() {
        List<Account> accountsList = new ArrayList<Account>();

        DBConnector dbConnector = new DBConnector();
        String query =
            "begin  TQC_ACTIVITIES_CURSOR.get_normal_accounts(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {

            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);
            statement.setString(1, (String)session.getAttribute("type"));
            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet rs = (OracleResultSet)statement.getObject(2);
            while (rs.next()) {
                Account account = new Account();
                account.setAccountCode(rs.getBigDecimal(1));
                account.setAccountType(rs.getString(2));
                account.setAccountTypeCode(rs.getBigDecimal(3));
                account.setAccountName(rs.getString(4));
                account.setAccountEmail(rs.getString(5));
                accountsList.add(account);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return accountsList;
    }

    public List<Account> findRequestOwnerAccount() {
        List<Account> accountsList = new ArrayList<Account>();

        DBConnector dbConnector = new DBConnector();
        String query =
            "begin  TQC_ACTIVITIES_CURSOR.get_serviceOwner_accounts(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {

            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);
            statement.setString(1, (String)session.getAttribute("type"));
            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet rs = (OracleResultSet)statement.getObject(2);
            while (rs.next()) {
                Account account = new Account();
                account.setAccountCode(rs.getBigDecimal(1));
                account.setAccountType(rs.getString(2));
                account.setAccountTypeCode(rs.getBigDecimal(3));
                account.setAccountName(rs.getString(4));
                account.setAccountEmail(rs.getString(5));
                accountsList.add(account);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return accountsList;
    }

    public List<ServiceReq> findUserRequests() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String medicalQuery =
                "begin TQC_SERVICE_REQUESTS.GisClientPropPol(?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            //register out
            cst.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            // cst.setString(2, (String)session.getAttribute("medIDNo"));
            cst.setObject(2, session.getAttribute("clntCode"));

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setTsrCode(rs.getBigDecimal(1));
                request.setTsrcCode(rs.getBigDecimal(2));
                request.setTsrType(rs.getString(3));
                request.setAccType(rs.getString(4));
                request.setAccCode(rs.getBigDecimal(5));
                request.setTsrDate(rs.getTimestamp(6));
                request.setAssignee(rs.getBigDecimal(7));
                request.setOwnType(rs.getString(8));
                request.setOwnerCode(rs.getBigDecimal(9));
                request.setStatus(rs.getString(10));
                request.setDueDate(rs.getDate(11));
                request.setResDate(rs.getDate(12));
                request.setSummary(rs.getString(13));
                request.setDesc(rs.getString(14));
                request.setSolution(rs.getString(15));
                request.setTsrcName(rs.getString(16));
                request.setAccTypeDesc(rs.getString(17));
                request.setAssignee(rs.getBigDecimal(18));
                request.setAccountName(rs.getString(19));
                request.setOwnerType(rs.getString(20));
                request.setOwner(rs.getString(21));
                service.add(request);
            }
            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }


    public List<ServiceReq> findClientPendingRequests() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        OracleResultSet rs = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String medicalQuery =
                "SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type,\n" +
                "                tsr_acc_code, tsr_date, tsr_assignee, tsr_owner_type,\n" +
                "                tsr_owner_code, tsr_status, tsr_due_date,\n" +
                "                tsr_resolution_date, tsr_summary, tsr_description,\n" +
                "                tsr_solution, tsrc_name,\n" +
                "                DECODE (tsr_acc_type,\n" +
                "                        'C', 'CLIENT',\n" +
                "                        'A', 'AGENT',\n" +
                "                        'SP', 'SERVICE PROVIDER'\n" +
                "                       ) acc_type,\n" +
                "                ass.usr_username assignee,\n" +
                "                clnt_name || ' ' || clnt_other_names account_name,\n" +
                "                DECODE (tsr_owner_type,\n" +
                "                        'C', 'CLIENT',\n" +
                "                        'A', 'AGENT',\n" +
                "                        'SP', 'SERVICE PROVIDER',\n" +
                "                        'U', 'USER'\n" +
                "                       ) owner_type,\n" +
                "                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,\n" +
                "                tsr_comments, clnt_sht_desc, clnt_physical_addrs,\n" +
                "                clnt_email_addrs, clnt_sms_tel, tsr_srt_code, srt_desc,tsr_policy_no,tsr_receive_date,tsr_reporter,tsr_capturedate,\n" +
                "                tsr_closed_by\n" +
                "           FROM tqc_serv_requests,\n" +
                "                tqc_serv_req_cat,\n" +
                "                tqc_users ass,\n" +
                "                tqc_clients,\n" +
                "                tqc_users uown,\n" +
                "                tqc_serv_req_types\n" +
                "          WHERE tsr_tsrc_code = tsrc_code\n" +
                "            AND tsr_assignee = ass.usr_code\n" +
                "            AND tsr_acc_code = clnt_code\n" +
                "            AND tsr_owner_code = uown.usr_code\n" +
                "            AND tsr_acc_type = 'C'\n" +
                "            AND tsr_acc_code = :v_clientcode\n" +
                "            AND tsr_status IN ( 'Open', 'Pending')\n" +
                "            AND tsr_type != 'Enquiry'\n" +
                "            AND srt_code(+) = tsr_srt_code\n" +
                "            AND tsr_acc_type = :v_tsr_acc_type\n" +
                "         UNION\n" +
                "         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type, tsr_acc_code,\n" +
                "                tsr_date, tsr_assignee, tsr_owner_type, tsr_owner_code,\n" +
                "                tsr_status, tsr_due_date, tsr_resolution_date, tsr_summary,\n" +
                "                tsr_description, tsr_solution, tsrc_name,\n" +
                "                DECODE (tsr_acc_type,\n" +
                "                        'C', 'CLIENT',\n" +
                "                        'A', 'AGENT',\n" +
                "                        'SP', 'SERVICE PROVIDER'\n" +
                "                       ) acc_type,\n" +
                "                ass.usr_username assignee, agn_name account_name,\n" +
                "                DECODE (tsr_owner_type,\n" +
                "                        'C', 'CLIENT',\n" +
                "                        'A', 'AGENT',\n" +
                "                        'SP', 'SERVICE PROVIDER',\n" +
                "                        'U', 'USER'\n" +
                "                       ) owner_type,\n" +
                "                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,\n" +
                "                tsr_comments, agn_sht_desc, agn_physical_address,\n" +
                "                agn_email_address, agn_sms_tel, tsr_srt_code, srt_desc,tsr_policy_no,tsr_receive_date,tsr_reporter,tsr_capturedate,\n" +
                "                tsr_closed_by\n" +
                "           FROM tqc_serv_requests,\n" +
                "                tqc_serv_req_cat,\n" +
                "                tqc_users ass,\n" +
                "                tqc_agencies,\n" +
                "                tqc_users uown,\n" +
                "                tqc_serv_req_types\n" +
                "          WHERE tsr_tsrc_code = tsrc_code\n" +
                "            AND tsr_assignee = ass.usr_code\n" +
                "            AND tsr_acc_code = agn_code\n" +
                "            AND tsr_owner_code = uown.usr_code\n" +
                "            AND tsr_acc_type = 'A'\n" +
                "            AND tsr_acc_code = :v_clientcode\n" +
                "             AND tsr_status IN ( 'Open', 'Pending')\n" +
                "            AND tsr_type != 'Enquiry'\n" +
                "            AND srt_code(+) = tsr_srt_code\n" +
                "            AND tsr_acc_type = :v_tsr_acc_type\n" +
                "         UNION\n" +
                "         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type, tsr_acc_code,\n" +
                "                tsr_date, tsr_assignee, tsr_owner_type, tsr_owner_code,\n" +
                "                tsr_status, tsr_due_date, tsr_resolution_date, tsr_summary,\n" +
                "                tsr_description, tsr_solution, tsrc_name,\n" +
                "                DECODE (tsr_acc_type,\n" +
                "                        'C', 'CLIENT',\n" +
                "                        'A', 'AGENT',\n" +
                "                        'SP', 'SERVICE PROVIDER'\n" +
                "                       ) acc_type,\n" +
                "                ass.usr_username assignee, spr_name account_name,\n" +
                "                DECODE (tsr_owner_type,\n" +
                "                        'C', 'CLIENT',\n" +
                "                        'A', 'AGENT',\n" +
                "                        'SP', 'SERVICE PROVIDER',\n" +
                "                        'U', 'USER'\n" +
                "                       ) owner_type,\n" +
                "                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,\n" +
                "                tsr_comments, spr_sht_desc, spr_physical_address, spr_email,\n" +
                "                spr_sms_number, tsr_srt_code, srt_desc,tsr_policy_no,tsr_receive_date,tsr_reporter,tsr_capturedate,\n" +
                "                tsr_closed_by\n" +
                "           FROM tqc_serv_requests,\n" +
                "                tqc_serv_req_cat,\n" +
                "                tqc_users ass,\n" +
                "                tqc_service_providers,\n" +
                "                tqc_users uown,\n" +
                "                tqc_serv_req_types\n" +
                "          WHERE tsr_tsrc_code = tsrc_code\n" +
                "            AND tsr_assignee = ass.usr_code\n" +
                "            AND tsr_acc_code = spr_code\n" +
                "            AND tsr_owner_code = uown.usr_code\n" +
                "            AND tsr_acc_type = 'SP'\n" +
                "            AND tsr_acc_code = :v_clientcode\n" +
                "             AND tsr_status IN ( 'Open', 'Pending')\n" +
                "            AND tsr_type != 'Enquiry'\n" +
                "            AND tsr_acc_type = :v_tsr_acc_type\n" +
                "            AND srt_code(+) = tsr_srt_code\n" +
                "         UNION\n" +
                "         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type, tsr_acc_code,\n" +
                "                tsr_date, tsr_assignee, tsr_owner_type, tsr_owner_code,\n" +
                "                tsr_status, tsr_due_date, tsr_resolution_date, tsr_summary,\n" +
                "                tsr_description, tsr_solution, tsrc_name,\n" +
                "                DECODE (tsr_acc_type,\n" +
                "                        'C', 'CLIENT',\n" +
                "                        'A', 'AGENT',\n" +
                "                        'SP', 'SERVICE PROVIDER'\n" +
                "                       ) acc_type,\n" +
                "                ass.usr_username assignee, srid_name account_name,\n" +
                "                DECODE (tsr_owner_type,\n" +
                "                        'C', 'CLIENT',\n" +
                "                        'A', 'AGENT',\n" +
                "                        'SP', 'SERVICE PROVIDER',\n" +
                "                        'U', 'USER'\n" +
                "                       ) owner_type,\n" +
                "                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,\n" +
                "                tsr_comments, srid_name, srid_physical_address,\n" +
                "                srid_email_address, srid_telephone, tsr_srt_code, srt_desc,tsr_policy_no,tsr_receive_date,tsr_reporter,tsr_capturedate," +
                "                tsr_closed_by\n" +
                "           FROM tqc_serv_requests,\n" +
                "                tqc_serv_req_cat,\n" +
                "                tqc_users ass,\n" +
                "                tqc_serv_req_ind_dtls,\n" +
                "                tqc_users uown,\n" +
                "                tqc_serv_req_types\n" +
                "          WHERE tsr_tsrc_code = tsrc_code\n" +
                "            AND tsr_assignee = ass.usr_code\n" +
                "            AND tsr_acc_code = srid_code\n" +
                "            AND tsr_owner_code = uown.usr_code\n" +
                "            AND tsr_acc_type = 'O'\n" +
                "            AND tsr_acc_code = :v_clientcode\n" +
                "             AND tsr_status IN ( 'Open', 'Pending')\n" +
                "            AND tsr_type != 'Enquiry'\n" +
                "            AND tsr_acc_type = :v_tsr_acc_type\n" +
                "            AND srt_code(+) = tsr_srt_code\n" +
                "         UNION\n" +
                "         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type, tsr_acc_code,\n" +
                "                tsr_date, tsr_assignee, tsr_owner_type, tsr_owner_code,\n" +
                "                tsr_status, tsr_due_date, tsr_resolution_date, tsr_summary,\n" +
                "                tsr_description, tsr_solution, tsrc_name,\n" +
                "                DECODE (tsr_acc_type,\n" +
                "                        'C', 'CLIENT',\n" +
                "                        'A', 'AGENT',\n" +
                "                        'SP', 'SERVICE PROVIDER'\n" +
                "                       ) acc_type,\n" +
                "                ass.usr_username assignee, dlt_desc account_name,\n" +
                "                DECODE (tsr_owner_type,\n" +
                "                        'C', 'CLIENT',\n" +
                "                        'A', 'AGENT',\n" +
                "                        'SP', 'SERVICE PROVIDER',\n" +
                "                        'U', 'USER'\n" +
                "                       ) owner_type,\n" +
                "                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,\n" +
                "                tsr_comments, org_sht_desc, org_phy_addrs, org_email_addrs,\n" +
                "                org_tel1, tsr_srt_code, srt_desc,tsr_policy_no,tsr_receive_date,tsr_reporter,tsr_capturedate,\n" +
                "                tsr_closed_by\n" +
                "           FROM tqc_serv_requests,\n" +
                "                tqc_serv_req_cat,\n" +
                "                tqc_users ass,\n" +
                "                tqc_org_division_levels_type,\n" +
                "                tqc_users uown,\n" +
                "                tqc_serv_req_types,\n" +
                "                tqc_organizations\n" +
                "          WHERE tsr_tsrc_code = tsrc_code\n" +
                "            AND tsr_assignee = ass.usr_code\n" +
                "            AND tsr_acc_code = dlt_code_val\n" +
                "            AND tsr_owner_code = uown.usr_code\n" +
                "            AND tsr_acc_type = 'IN'\n" +
                "            AND tsr_acc_code = :v_clientcode\n" +
                "            AND tsr_status IN ( 'Open', 'Pending')\n" +
                "            AND org_code = tsr_tsrc_code\n" +
                "            AND tsr_type != 'Enquiry'\n" +
                "            AND tsr_acc_type = :v_tsr_acc_type\n" +
                "            AND srt_code(+) = tsr_srt_code\n";


            BigDecimal clntCode =
                GlobalCC.checkBDNullValues(session.getAttribute("clntCode"));
            String accType =
                GlobalCC.checkNullValues(session.getAttribute("accountType"));

            medicalQuery =
                    medicalQuery.replaceAll(":v_clientcode", clntCode != null ?
                                                             clntCode.toString() :
                                                             "-1");
            medicalQuery =
                    medicalQuery.replaceAll(":v_tsr_acc_type", "'" + accType +
                                            "'");
            System.out.println("medicalQuery: " + medicalQuery);
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            rs = (OracleResultSet)cst.executeQuery();
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setTsrCode(rs.getBigDecimal(1));
                request.setTsrcCode(rs.getBigDecimal(2));
                request.setTsrType(rs.getString(3));
                request.setAccType(rs.getString(4));
                request.setAccCode(rs.getBigDecimal(5));
                request.setTsrDate(rs.getTimestamp(6));
                request.setAssignee(rs.getBigDecimal(7));
                request.setOwnType(rs.getString(8));
                request.setOwnerCode(rs.getBigDecimal(9));
                request.setStatus(rs.getString(10));
                request.setDueDate(rs.getDate(11));
                request.setResDate(rs.getDate(12));
                request.setSummary(rs.getString(13));
                request.setDesc(rs.getString(14));
                request.setSolution(rs.getString(15));
                request.setTsrcName(rs.getString(16));
                request.setAccTypeDesc(rs.getString(17));
                request.setAssigneeDesc(rs.getString(18));
                request.setAccountName(rs.getString(19));
                request.setOwnerType(rs.getString(20));
                request.setOwner(rs.getString(21));
                request.setTsrMode(rs.getString(22));
                request.setRequestRefNumber(rs.getString(23));
                request.setComments(rs.getString(24));
                request.setTsrSrtCode(rs.getBigDecimal(29));
                request.setSrtDesc(rs.getString(30));
                request.setPolicyNumber(rs.getString(31));
                request.setTSR_POLICY_NO(rs.getString(31));
                request.setTSR_RECEIVE_DATE(rs.getDate(32));
                request.setTsr_reporter(rs.getString(33));
                request.setTsrCapturedate(rs.getDate(34));
                request.setClosedBy(rs.getString(35));
                
                if (rs.getString(35) != null) {
                    if (rs.getString(35).equalsIgnoreCase(rs.getString(33))) {
                        request.setClosedByReporter(rs.getString(33));
                    } else {
                        request.setClosedByReporter(null);
                    }
                    if (rs.getString(35).equalsIgnoreCase(rs.getString(18))) {
                        request.setClosedByAssignee(rs.getString(18));
                    } else {
                        request.setClosedByAssignee(null);
                    }
                }
                service.add(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, cst, rs);
        }

        return service;
    }


    public List<ServiceReq> findClientRequests() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String medicalQuery =
                "begin ? := TQC_SERVICE_REQUESTS.getAllClientRequests(?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            BigDecimal clntc = null;
            if (session.getAttribute("clntCode") != null) {
                clntc =
                        new BigDecimal(session.getAttribute("clntCode").toString());
            }

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            // cst.setString(2, (String)session.getAttribute("medIDNo"));
            cst.setBigDecimal(2, clntc);
            cst.setString(3, session.getAttribute("accountType").toString());
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setTsrCode(rs.getBigDecimal(1));
                request.setTsrcCode(rs.getBigDecimal(2));
                request.setTsrType(rs.getString(3));
                request.setAccType(rs.getString(4));
                request.setAccCode(rs.getBigDecimal(5));
                request.setTsrDate(rs.getTimestamp(6));
                //request.setAssignee(rs.getString(7));
                request.setOwnType(rs.getString(8));
                request.setOwnerCode(rs.getBigDecimal(9));
                request.setStatus(rs.getString(10));
                request.setDueDate(rs.getDate(11));
                request.setResDate(rs.getDate(12));
                request.setSummary(rs.getString(13));
                request.setDesc(rs.getString(14));
                request.setSolution(rs.getString(15));
                request.setTsrcName(rs.getString(16));
                request.setAccTypeDesc(rs.getString(17));
                request.setAssigneeDesc(rs.getString(18));
                request.setAccountName(rs.getString(19));
                request.setOwnerType(rs.getString(20));
                request.setOwner(rs.getString(21));
                request.setTsrMode(rs.getString(22));
                request.setComments(rs.getString(24));
                request.setRequestRefNumber(rs.getString(23));
                request.setTsrSrtCode(rs.getBigDecimal(29));
                request.setSrtDesc(rs.getString(30));
                request.setTSR_POLICY_NO((String)session.getAttribute("TSR_POLICY_NO"));
                service.add(request);
            }
            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }


    public List<ServiceReq> findUserOpenRequests() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String medicalQuery =
                "begin ? := TQC_SERVICE_REQUESTS.getUserOpenRequests(?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            // cst.setString(2, (String)session.getAttribute("medIDNo"));
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("UserCode"));

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setTsrCode(rs.getBigDecimal(1));
                request.setTsrcCode(rs.getBigDecimal(2));
                request.setTsrType(rs.getString(3));
                request.setAccType(rs.getString(4));
                request.setAccCode(rs.getBigDecimal(5));
                request.setTsrDate(rs.getTimestamp(6));
                request.setAssignee(rs.getBigDecimal(7));
                request.setOwnType(rs.getString(8));
                request.setOwnerCode(rs.getBigDecimal(9));
                request.setStatus(rs.getString(10));
                request.setDueDate(rs.getDate(11));
                request.setResDate(rs.getDate(12));
                request.setSummary(rs.getString(13));
                request.setDesc(rs.getString(14));
                request.setSolution(rs.getString(15));
                request.setTsrcName(rs.getString(16));
                request.setAccTypeDesc(rs.getString(17));
                request.setAssigneeDesc(rs.getString(18));
                request.setAccountName(rs.getString(19));
                request.setOwnerType(rs.getString(20));
                request.setOwner(rs.getString(21));
                request.setTsrMode(rs.getString(22));
                request.setRequestRefNumber(rs.getString(23));
                service.add(request);
            }
            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }


    public List<ServiceReq> findUserOverdueRequests() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String medicalQuery =
                "begin ? := TQC_SERVICE_REQUESTS.getUserOverdueRequests(?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            // cst.setString(2, (String)session.getAttribute("medIDNo"));
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("UserCode"));

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setTsrCode(rs.getBigDecimal(1));
                request.setTsrcCode(rs.getBigDecimal(2));
                request.setTsrType(rs.getString(3));
                request.setAccType(rs.getString(4));
                request.setAccCode(rs.getBigDecimal(5));
                request.setTsrDate(rs.getTimestamp(6));
                request.setAssignee(rs.getBigDecimal(7));
                request.setOwnType(rs.getString(8));
                request.setOwnerCode(rs.getBigDecimal(9));
                request.setStatus(rs.getString(10));
                request.setDueDate(rs.getDate(11));
                request.setResDate(rs.getDate(12));
                request.setSummary(rs.getString(13));
                request.setDesc(rs.getString(14));
                request.setSolution(rs.getString(15));
                request.setTsrcName(rs.getString(16));
                request.setAccTypeDesc(rs.getString(17));
                request.setAssigneeDesc(rs.getString(18));
                request.setAccountName(rs.getString(19));
                request.setOwnerType(rs.getString(20));
                request.setOwner(rs.getString(21));
                request.setTsrMode(rs.getString(22));
                request.setRequestRefNumber(rs.getString(23));
                service.add(request);
            }
            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }


    public List<ServiceReq> findOverdueRequests() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String medicalQuery =
                "begin ? := TQC_SERVICE_REQUESTS.getOverdueRequests(?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setObject(2, session.getAttribute("requestStatus"));

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setTsrCode(rs.getBigDecimal(1));
                request.setTsrcCode(rs.getBigDecimal(2));
                request.setTsrType(rs.getString(3));
                request.setAccType(rs.getString(4));
                request.setAccCode(rs.getBigDecimal(5));
                request.setTsrDate(rs.getTimestamp(6));
                request.setAssignee(rs.getBigDecimal(7));
                request.setOwnType(rs.getString(8));
                request.setOwnerCode(rs.getBigDecimal(9));
                request.setStatus(rs.getString(10));
                request.setDueDate(rs.getDate(11));
                request.setResDate(rs.getDate(12));
                request.setSummary(rs.getString(13));
                request.setDesc(rs.getString(14));
                request.setSolution(rs.getString(15));
                request.setTsrcName(rs.getString(16));
                request.setAccTypeDesc(rs.getString(17));
                request.setAssigneeDesc(rs.getString(18));
                request.setAccountName(rs.getString(19));
                request.setOwnerType(rs.getString(20));
                request.setOwner(rs.getString(21));
                request.setTsrMode(rs.getString(22));
                request.setRequestRefNumber(rs.getString(23));
                request.setComments(rs.getString(24));
                request.setSprShtDesc(rs.getString(25));
                request.setAgnPhysicalAddress(rs.getString(26));
                request.setAgnEmailAddress(rs.getString(27));
                request.setAgnSmsNumber(rs.getString(28));
                request.setTsrSrtCode(rs.getBigDecimal(29));
                request.setSrtDesc(rs.getString(30));
                
                if (rs.getString(32) != null) {
                    if (rs.getString(32).equalsIgnoreCase(rs.getString(31))) {
                        request.setClosedByReporter(rs.getString(31));
                    } else {
                        request.setClosedByReporter(null);
                    }
                    if (rs.getString(32).equalsIgnoreCase(rs.getString(18))) {
                        request.setClosedByAssignee(rs.getString(18));
                    } else {
                        request.setClosedByAssignee(null);
                    }
                }


                service.add(request);
            }
            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }


    public List<ServiceReq> findPendingRequests() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String query =
                "SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type,\n" +
                "                tsr_acc_code, tsr_date, tsr_assignee, tsr_owner_type,\n" +
                "                tsr_owner_code, tsr_status, tsr_due_date,\n" +
                "                tsr_resolution_date, tsr_summary, tsr_description,\n" +
                "                tsr_solution, tsrc_name,\n" +
                "                DECODE (tsr_acc_type,\n" +
                "                        'C', 'CLIENT',\n" +
                "                        'A', 'AGENT',\n" +
                "                        'SP', 'SERVICE PROVIDER',\n" +
                "                        'IN', 'INTERNAL',\n" +
                "                        'U', 'USER',\n" +
                "                        'O', 'OTHERS'\n" +
                "                       ) acc_type,\n" +
                "                ass.usr_username assignee,\n" +
                "                clnt_name || ' ' || clnt_other_names account_name,\n" +
                "                DECODE (tsr_owner_type,\n" +
                "                        'C', 'CLIENT',\n" +
                "                        'A', 'AGENT',\n" +
                "                        'SP', 'SERVICE PROVIDER',\n" +
                "                        'IN', 'INTERNAL',\n" +
                "                        'U', 'USER',\n" +
                "                        'OTHERS'\n" +
                "                       ) owner_type,\n" +
                "                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,\n" +
                "                tsr_comments, clnt_sht_desc, clnt_physical_addrs,\n" +
                "                clnt_email_addrs, clnt_sms_tel, NULL, NULL\n" +
                "           FROM tqc_serv_requests,\n" +
                "                tqc_serv_req_cat,\n" +
                "                tqc_users ass,\n" +
                "                tqc_clients,\n" +
                "                tqc_users uown\n" +
                "          WHERE tsr_tsrc_code = tsrc_code\n" +
                "            AND tsr_assignee = ass.usr_code\n" +
                "            AND tsr_acc_code = clnt_code\n" +
                "            AND tsr_owner_code = uown.usr_code\n" +
                "            AND tsr_status = 'Open'\n" +
                "            AND tsr_type != 'Enquiry'";
            cst = (OracleCallableStatement)conn.prepareCall(query);

            OracleResultSet rs = (OracleResultSet)cst.executeQuery();
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setTsrCode(rs.getBigDecimal(1));
                request.setTsrcCode(rs.getBigDecimal(2));
                request.setTsrType(rs.getString(3));
                request.setAccType(rs.getString(4));
                request.setAccCode(rs.getBigDecimal(5));
                request.setTsrDate(rs.getTimestamp(6));
                request.setAssignee(rs.getBigDecimal(7));
                request.setOwnType(rs.getString(8));
                request.setOwnerCode(rs.getBigDecimal(9));
                request.setStatus(rs.getString(10));
                request.setDueDate(rs.getDate(11));
                request.setResDate(rs.getDate(12));
                request.setSummary(rs.getString(13));
                request.setDesc(rs.getString(14));
                request.setSolution(rs.getString(15));
                request.setTsrcName(rs.getString(16));
                request.setAccTypeDesc(rs.getString(17));
                request.setAssigneeDesc(rs.getString(18));
                request.setAccountName(rs.getString(19));
                request.setOwnerType(rs.getString(20));
                request.setOwner(rs.getString(21));
                request.setTsrMode(rs.getString(22));
                request.setRequestRefNumber(rs.getString(23));
                service.add(request);
            }
            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }


    public List<ServiceReq> findOverdueRequestsChat() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String medicalQuery =
                "begin ? := TQC_SERVICE_REQUESTS.getOverdueRequestsChat; end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setCount(rs.getBigDecimal(1));
                request.setAssigneeDesc(rs.getString(2));
                service.add(request);
            }
            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }

    public List<ServiceReq> findIncomingMail() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String medicalQuery =
                "begin ? := TQC_SETUPS_CURSOR.getIncomingMailSettings; end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            int k = 0;
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setMailType(rs.getString(1));
                request.setMailServerName(rs.getString(2));
                request.setMailHost(rs.getString(3));
                request.setMailUsername(rs.getString(4));
                request.setMailPass(rs.getString(5));
                request.setMailPort(rs.getString(6));
                request.setMailEmail(rs.getString(7));
                request.setMailInOut(rs.getString(8));
                if (rs.getString(9) == null) {
                    request.setSecure(false);
                } else if (rs.getString(9).toString().equalsIgnoreCase("N")) {
                    request.setSecure(false);
                } else {
                    request.setSecure(true);
                }
                service.add(request);
                k++;
            }
            if (k == 0) {
                ServiceReq request = new ServiceReq();
                service.add(request);
            }
            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }

    public List<ServiceReq> findOutgoingMail() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String medicalQuery =
                "begin ? := TQC_SETUPS_CURSOR.getOutgoingMailSettings; end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            int k = 0;
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setMailType(rs.getString(1));
                request.setMailServerName(rs.getString(2));
                request.setMailHost(rs.getString(3));
                request.setMailUsername(rs.getString(4));
                request.setMailPass(rs.getString(5));
                request.setMailPort(rs.getString(6));
                request.setMailEmail(rs.getString(7));
                request.setMailInOut(rs.getString(8));
                if (rs.getString(9) == null) {
                    request.setSecure(false);
                } else if (rs.getString(9).toString().equalsIgnoreCase("N")) {
                    request.setSecure(false);
                } else {
                    request.setSecure(true);
                }
                service.add(request);
                k++;
            }
            if (k == 0) {
                ServiceReq request = new ServiceReq();
                service.add(request);
            }
            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }

    public List<ServiceReq> findReservedWords() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String medicalQuery =
                "begin ? := TQC_SETUPS_CURSOR.getReservedWords(?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setObject(2, session.getAttribute("sysCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            int k = 0;
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setCode(rs.getBigDecimal(1));
                request.setSysCode(rs.getBigDecimal(2));
                request.setTsrcCode(rs.getBigDecimal(3));
                request.setType(rs.getString(4));
                request.setEditable(rs.getString(5));
                request.setTsrcName(rs.getString(6));
                request.setName(rs.getString(7));
                request.setDesc(rs.getString(8));
                service.add(request);
            }

            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }

    public List<ServiceReq> findOutgoingSMS() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String medicalQuery =
                "begin ? := TQC_SETUPS_CURSOR.getSmsSettings; end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            int k = 0;
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setTssDesc(rs.getString(1));
                request.setTssUrl(rs.getString(2));
                request.setTssUsername(rs.getString(3));
                request.setTssPassword(rs.getString(4));
                request.setTssSource(rs.getString(5));
                request.setTssCode(rs.getBigDecimal(6));
                request.setTssDefault(rs.getString(7));
                service.add(request);
                k++;
            }
            if (k == 0) {
                ServiceReq request = new ServiceReq();
                service.add(request);
            }
            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }

    public List<ServiceReq> findBranches() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String medicalQuery =
                "begin ? := TQC_SERVICE_REQUESTS.getCategoryBranches; end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            int k = 0;
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setBrnCode(rs.getBigDecimal(1));
                request.setBrnShtDesc(rs.getString(2));
                request.setRegCode(rs.getBigDecimal(3));
                request.setBrnName(rs.getString(4));
                service.add(request);
            }

            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }

    public List<ServiceReq> findDepartments() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String medicalQuery =
                "begin ? := TQC_SERVICE_REQUESTS.get_departments; end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            int k = 0;
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setDepCode(rs.getBigDecimal(1));
                request.setDepShtDesc(rs.getString(2));
                request.setDepName(rs.getString(3));
                request.setDepWef(rs.getDate(4));
                request.setDepWet(rs.getDate(5));
                service.add(request);
            }

            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }

    public List<ServiceReq> findRequestTypes() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String medicalQuery =
                "begin ? := TQC_SERVICE_REQUESTS.get_serv_request_types; end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            int k = 0;
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setSrtCode(rs.getBigDecimal(1));
                request.setSrtShtDesc(rs.getString(2));
                request.setSrtDesc(rs.getString(3));
                service.add(request);
            }

            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }

    public List<ServiceReq> findAgencies(String agnShtDesc, String agnname) {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        Connection conn = null;
        conn = datahandler.getDatabaseConnection();
        CallableStatement cst = null;
        List<ServiceReq> agents = new ArrayList<ServiceReq>();

        ResultSet rs = null;
        try {
            conn = datahandler.getDatabaseConnection();
            cst =
conn.prepareCall("begin ? := tqc_setups_pkg.get_intermediaries(?,?,?); end;");
            cst.registerOutParameter(1,
                                     oracle.jdbc.OracleTypes.CURSOR); //authorization code
            cst.setString(2, "AGENTS/BROKERS");
            cst.setString(3, agnShtDesc);
            cst.setString(4, agnname);
            cst.execute();
            rs = (ResultSet)cst.getObject(1);
            agents = new ArrayList<ServiceReq>();
            while (rs.next()) {
                ServiceReq agent = new ServiceReq();

                agent.setAgntShtDesc(rs.getString(1));
                agent.setAgntCode(rs.getBigDecimal(2));
                agent.setAgntName(rs.getString(3));
                //System.out.println("This is the agent name"+rs.getString(3));
                agent.setAgntBrnCode(rs.getBigDecimal(4));
                agent.setAgntBrnName(rs.getString(5));
                agent.setAgntCommAllowed(rs.getString(6));
                agent.setAgnPhysicalAddress(rs.getString(8));
                agent.setAgnEmailAddress(rs.getString(9));
                agent.setAgnSmsNumber(rs.getString(10));
                agent.setAgnDefCommMode(rs.getString(11));
                agents.add(agent);
                if (agents.size() == 100) {
                    session.setAttribute("RecordExist",
                                         "More Record Exists....Please Refine Your Search");
                    return agents;
                }
            }
            DbUtils.closeQuietly(conn, cst, rs);

        } catch (SQLException e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cst, rs);
        }
        session.setAttribute("RecordExist", null);

        return agents;


    }

    public List<ServiceReq> findServiceProviders() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String medicalQuery =
                "begin ? := TQC_SERVICE_REQUESTS.get_serv_providers; end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            int k = 0;
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setSprCode(rs.getBigDecimal(1));
                request.setSprShtDesc(rs.getString(2));
                request.setSprName(rs.getString(3));
                request.setSprPhysicalAddress(rs.getString(4));
                request.setSprPostalAddress(rs.getString(5));
                request.setSprPhone(rs.getString(6));
                request.setSprEmail(rs.getString(7));
                request.setSprPhoneNumber(rs.getString(8));
                request.setSprSmsNumber(rs.getString(9));
                service.add(request);
            }

            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }

    public List<ServiceReq> findAllActiveRequests() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {
            conn = datahandler.getDatabaseConnection();
            String query =
                "SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type,\n" +
                "                tsr_acc_code, tsr_date, tsr_assignee, tsr_owner_type,\n" +
                "                tsr_owner_code, tsr_status, tsr_due_date,\n" +
                "                tsr_resolution_date, tsr_summary, tsr_description,\n" +
                "                tsr_solution, tsrc_name,\n" +
                "                DECODE (tsr_acc_type,\n" +
                "                        'C', 'CLIENT',\n" +
                "                        'A', 'AGENT',\n" +
                "                        'SP', 'SERVICE PROVIDER',\n" +
                "                        'IN', 'INTERNAL',\n" +
                "                        'U', 'USER',\n" +
                "                        'O', 'OTHERS'\n" +
                "                       ) acc_type,\n" +
                "                ass.usr_username assignee,\n" +
                "                clnt_name || ' ' || clnt_other_names account_name,\n" +
                "                DECODE (tsr_owner_type,\n" +
                "                        'C', 'CLIENT',\n" +
                "                        'A', 'AGENT',\n" +
                "                        'SP', 'SERVICE PROVIDER',\n" +
                "                        'U', 'USER'\n" +
                "                       ) owner_type,\n" +
                "                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,\n" +
                "                tsr_comments, clnt_sht_desc, clnt_physical_addrs,\n" +
                "                clnt_email_addrs, clnt_sms_tel, NULL, NULL,tsr_reporter\n" +
                "           FROM tqc_serv_requests,\n" +
                "                tqc_serv_req_cat,\n" +
                "                tqc_users ass,\n" +
                "                tqc_clients,\n" +
                "                tqc_users uown\n" +
                "          WHERE tsr_tsrc_code = tsrc_code\n" +
                "            AND tsr_assignee = ass.usr_code\n" +
                "            AND tsr_acc_code = clnt_code(+)\n" +
                "            AND tsr_owner_code = uown.usr_code\n" +
                "            AND tsr_status = 'Open'\n" +
                "            AND tsr_type != 'Enquiry'\n" +
                "            AND ass.usr_username = ':v_tsr_assignee'";

            query =
                    query.replaceAll(":v_tsr_assignee", GlobalCC.checkNullValues(session.getAttribute("Username")));
            cst = (OracleCallableStatement)conn.prepareCall(query);
            OracleResultSet rs = (OracleResultSet)cst.executeQuery();
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setTsrCode(rs.getBigDecimal(1));
                request.setTsrcCode(rs.getBigDecimal(2));
                request.setTsrType(rs.getString(3));
                request.setAccType(rs.getString(4));
                request.setAccCode(rs.getBigDecimal(5));
                request.setTsrDate(rs.getTimestamp(6));
                request.setAssignee(rs.getBigDecimal(7));
                request.setOwnType(rs.getString(8));
                request.setOwnerCode(rs.getBigDecimal(9));
                request.setStatus(rs.getString(10));
                request.setDueDate(rs.getDate(11));
                request.setResDate(rs.getDate(12));
                request.setSummary(rs.getString(13));
                request.setDesc(rs.getString(14));
                request.setSolution(rs.getString(15));
                request.setTsrcName(rs.getString(16));
                request.setAccTypeDesc(rs.getString(17));
                request.setAssigneeDesc(rs.getString(18));
                request.setAccountName(rs.getString(19));
                request.setOwnerType(rs.getString(20));
                request.setOwner(rs.getString(21));
                request.setTsrMode(rs.getString(22));
                request.setRequestRefNumber(rs.getString(23));
                request.setComments(rs.getString(24));
                request.setSprShtDesc(rs.getString(25));
                request.setAgnPhysicalAddress(rs.getString(26));
                request.setAgnEmailAddress(rs.getString(27));
                request.setAgnSmsNumber(rs.getString(28));
                request.setTsrSrtCode(rs.getBigDecimal(29));
                request.setSrtDesc(rs.getString(30));
                request.setTsr_reporter(rs.getString("tsr_reporter"));
                service.add(request);
            }
            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }

    public List<ServiceReq> findOtherServiceSeekers(String name,
                                                    String idNumber) {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String medicalQuery =
                "begin ? := TQC_SERVICE_REQUESTS.get_other_service_requesters(?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setObject(2, name);
            cst.setObject(3, idNumber);
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            int k = 0;
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setSridCode(rs.getBigDecimal(1));
                request.setSridName(rs.getString(2));
                request.setSridTelephone(rs.getString(3));
                request.setSridEmailAddress(rs.getString(4));
                request.setSridPhysicalAddress(rs.getString(5));
                request.setSrisIdNumber(rs.getString(6));
                service.add(request);
            }

            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }

    public List<ServiceReq> findAgentCommission() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String medicalQuery =
                "begin ? := TQC_SERVICE_REQUESTS.get_Agent_commission(?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setObject(2, session.getAttribute("agntCode"));

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            int k = 0;
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setCopAgnCode(rs.getBigDecimal(1));
                request.setAgnName(rs.getString(2));
                request.setCopDate(rs.getDate(3));
                request.setCopCrRefNumber(rs.getString(4));
                request.setCopdrRefNumber(rs.getString(5));
                request.setCopCommAmt(rs.getBigDecimal(6));
                request.setCopWhdtaxAmt(rs.getBigDecimal(7));
                request.setCopNetComm(rs.getBigDecimal(8));
                request.setCopCurrCode(rs.getBigDecimal(9));
                request.setAuthorised(rs.getString(10));
                request.setAuthorisedBy(rs.getString(11));
                request.setCopPaid(rs.getString(12));
                request.setCopPaidChqDate(rs.getDate(13));
                request.setCopPaidChqNo(rs.getString(14));
                service.add(request);
            }

            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }

    public List<ServiceReq> findProviderFees() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String medicalQuery =
                "begin ? := TQC_SERVICE_REQUESTS.getproviderfees(?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setObject(2, session.getAttribute("sprCode"));

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            int k = 0;
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setClientName(rs.getString(1));
                request.setFeeDesc(rs.getString(2));
                request.setPolicyNumber(rs.getString(3));
                request.setProviderFee(rs.getBigDecimal(4));
                service.add(request);
            }

            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }

    public List<ServiceReq> findServiceReqClientComments() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<ServiceReq> service = new ArrayList<ServiceReq>();
        try {

            String commentsQuery =
                "begin ? := TQC_SERVICE_REQUESTS.getServReqClientComments(?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(commentsQuery);

            //register out
            cst.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            // cst.setString(2, (String)session.getAttribute("medIDNo"));
            cst.setObject(2, session.getAttribute("tsrCode"));

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                ServiceReq request = new ServiceReq();
                request.setSrcCode(rs.getBigDecimal(1));
                request.setSrcClientComment(rs.getString(2));
                request.setSrcSolution(rs.getString(3));
                service.add(request);
            }
            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return service;
    }
}
