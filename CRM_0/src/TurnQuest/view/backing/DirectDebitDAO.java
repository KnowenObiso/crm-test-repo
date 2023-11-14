/*
* Copyright (c) 2010 TurnKey Africa Ltd. All Rights Reserved.
*
* This software is the confidential and proprietary information of TurnKey
* Africa Ltd. ("Confidential Information"). You shall not disclose such
* Confidential Information and shall use it only in accordance with the terms
* of the license agreement you entered into with TurnKey Africa Ltd.
*
* TURNKEY AFRICA MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY
* OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
* TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
* PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TURNKEY AFRICA SHALL NOT BE LIABLE
* FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
* DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
*/

package TurnQuest.view.backing;


import TurnQuest.view.Activities.Account;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.DirectDebit;
import TurnQuest.view.models.DirectDebitDetail;
import TurnQuest.view.models.DirectDebitHeader;
import TurnQuest.view.models.Holiday;
import TurnQuest.view.models.HolidayDefinition;
import TurnQuest.view.setups.Bank;

import java.math.BigDecimal;
  
import java.sql.Date;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;


public class DirectDebitDAO {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public DirectDebitDAO() {
        super();
    }

    /**
     * Fetches all <code>Bank</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>Bank</code> objects/records fetched
     * from the database.
     */
    public List<Bank> fetchBankBranches() {
        List<Bank> bankList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getBankBranchesLov();end;";
        if (session.getAttribute("_b_or_bb") != null &&
            session.getAttribute("_b_or_bb").toString().equalsIgnoreCase("B")) {
            query = "begin ? := TQC_SETUPS_CURSOR.getbanksov();end;";
        }
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            bankList = new ArrayList<Bank>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                Bank bank = new Bank();
                bank.setShortDesc(resultSet.getString(1));
                bank.setBankCode(resultSet.getString(2));
                bank.setBankName(resultSet.getString(3));
                bank.setBbrBranchName(resultSet.getString(4));
                bank.setBbrCode(resultSet.getBigDecimal(5));
                bank.setBctNum(resultSet.getString(6));
                bankList.add(bank);
            }
            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return bankList;
    }

    /**
     * Fetches all <code>GeneralBean</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>GeneralBean</code> objects/records fetched
     * from the database.
     */
    public List<GeneralBean> fetchClientRecords() {
        List<GeneralBean> recList = null;
        DBConnector dbConnector = new DBConnector();
        String date_range_query =
            "begin Direct_Debit_Pkg.get_date_range(?,?,?);end;";

        String query =
            "begin Direct_Debit_Pkg.Get_direct_debit_recs(?,?,?,?,?,?,?);end;";

        OracleCallableStatement statement = null;
        OracleCallableStatement statement2 = null;
        OracleConnection connection = null;
        try {
            recList = new ArrayList();
            connection = dbConnector.getDatabaseConnection();
            if ((session.getAttribute("systemCode") != null) &&
                (session.getAttribute("searchDate") != null) &&
                (session.getAttribute("bankCode") != null)) {


                statement2 =
                        (OracleCallableStatement)connection.prepareCall(date_range_query);

                statement2.registerOutParameter(1, -10);
                statement2.setDate(2,
                                   getSqlDate(session.getAttribute("searchDate").toString()));

                statement2.setDate(3,
                                   getSqlDate(session.getAttribute("searchDateTo").toString()));


                statement2.execute();
                OracleResultSet resultSet2 =
                    (OracleResultSet)statement2.getObject(1);

                OracleResultSet resultSet = null;

                while (resultSet2.next()) {
                    statement =
                            (OracleCallableStatement)connection.prepareCall(query);

                    int count = 0;


                    String systemCode =
                        session.getAttribute("systemCode").toString();

                    String bankCode =
                        session.getAttribute("bankCode").toString();


                    statement.registerOutParameter(1, -10);
                    statement.setBigDecimal(2, new BigDecimal(systemCode));
                    statement.setDate(3, resultSet2.getDate(1));
                    statement.setBigDecimal(4, new BigDecimal(bankCode));
                    statement.setObject(5, session.getAttribute("InstallDay"));
                    statement.setObject(6,
                                        getSqlDate(session.getAttribute("searchDateTo").toString()));
                    statement.setBigDecimal(7, (BigDecimal)session.getAttribute("productCode"));


                    statement.execute();
                    resultSet = (OracleResultSet)statement.getObject(1);

                    if (resultSet != null) {
                        while (resultSet.next()) {
                            GeneralBean record = new GeneralBean();
                            record.setSysCode(resultSet.getBigDecimal(1));
                            record.setPolCode(resultSet.getBigDecimal(2));
                            record.setPprCode(resultSet.getBigDecimal(3));
                            record.setPrpCode(resultSet.getBigDecimal(4));
                            record.setClientCode(resultSet.getBigDecimal(5));
                            record.setClientShortDesc(resultSet.getString(6));
                            record.setClientName(resultSet.getString(7));
                            record.setClientBankAccNum(resultSet.getString(8));
                            record.setClientBbrCode(resultSet.getBigDecimal(9));
                            record.setBbrBankCode(resultSet.getBigDecimal(10));
                            record.setBbrBranchName(resultSet.getString(11));
                            record.setBbrShortDesc(resultSet.getString(12));
                            record.setBbrRefCode(resultSet.getString(13));
                            record.setPolType(resultSet.getString(14));
                            record.setProposalNum(resultSet.getString(15));
                            record.setPolicyNum(resultSet.getString(16));
                            record.setPremAmount(resultSet.getBigDecimal(17));
                            record.setRemarks(resultSet.getString(18));
                            record.setPrevDdhCode(resultSet.getBigDecimal(19));
                            record.setBookDate(resultSet.getDate(20));
                            record.setAccountHolder(resultSet.getString(21));
                            record.setPayType(resultSet.getString(22));
                            record.setLoanNo(resultSet.getString(23));
                            recList.add(record);
                            count++;
                        }
                        resultSet.close();
                    }
                } 
                statement.close();
                resultSet2.close();
                statement2.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return recList;
    }

    /**
     * Fetches all <code>Holiday</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>Holiday</code> objects/records fetched
     * from the database.
     */
    public List<Holiday> fetchHolidays() {
        List<Holiday> holidayList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_SETUPS_CURSOR.getHolidays(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            holidayList = new ArrayList<Holiday>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("genYear") != null) {
                BigDecimal genYear =
                    new BigDecimal(session.getAttribute("genYear").toString());
                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, genYear);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    Holiday holiday = new Holiday();
                    holiday.setHldDate(resultSet.getDate(1));
                    holiday.setHldDesc(resultSet.getString(2));
                    holidayList.add(holiday);
                }
                resultSet.close();
                statement.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return holidayList;
    }

    /**
     * Fetches all <code>HolidayDefinition</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>HolidayDefinition</code> objects/records fetched
     * from the database.
     */
    public List<HolidayDefinition> fetchHolidayDefinitionsByCountry() {
        List<HolidayDefinition> holidayList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getHolidayDefinitions(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            holidayList = new ArrayList<HolidayDefinition>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("couCode") != null) {
                BigDecimal couCode =
                    new BigDecimal(session.getAttribute("couCode").toString());
                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, couCode);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    HolidayDefinition holiday = new HolidayDefinition();
                    holiday.setThdDesc(resultSet.getString(1));
                    holiday.setThdDay(resultSet.getBigDecimal(2));
                    holiday.setThdMonth(resultSet.getBigDecimal(3));
                    holiday.setThdStatus(resultSet.getString(4));
                    holiday.setThdCouCode(resultSet.getBigDecimal(5));
                    holiday.setMonthName(getMonthName(holiday.getThdMonth()));
                    holidayList.add(holiday);
                }
                resultSet.close();
                statement.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return holidayList;
    }

    /**
     * Fetches all <code>Holiday</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>Holiday</code> objects/records fetched
     * from the database.
     */
    public List<Holiday> fetchHolidayYearsLov() {
        List<Holiday> holidayList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_SETUPS_CURSOR.getYearsLov();end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            holidayList = new ArrayList<Holiday>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                Holiday holiday = new Holiday();
                holiday.setHYear(resultSet.getBigDecimal(1));
                holidayList.add(holiday);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return holidayList;
    }

    /**
     * Fetches all <code>DirectDebit</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>DirectDebit</code> objects/records fetched
     * from the database.
     */
    public List<DirectDebit> fetchDirectDebitNonReceipt() {
        List<DirectDebit> directDebitList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getDirectDebitNonReceipt();end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            directDebitList = new ArrayList<DirectDebit>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                DirectDebit directDebit = new DirectDebit();
                directDebit.setDdCode(resultSet.getBigDecimal(1));
                directDebit.setDdRefNo(resultSet.getString(2));
                directDebit.setDdSentYn(resultSet.getString(3));
                directDebit.setDdAccountNum(resultSet.getString(4));
                directDebit.setDdBookDate(resultSet.getDate(5));
                directDebit.setDdBbrCode(resultSet.getBigDecimal(6));
                directDebit.setDdStatus(resultSet.getString(7));
                directDebit.setDdReceipted(resultSet.getString(8));
                directDebit.setDdValueDate(resultSet.getDate(9));
                directDebit.setDdRaisedBy(resultSet.getString(10));
                directDebit.setDdDate(resultSet.getDate(11));
                directDebit.setDdBankCode(resultSet.getBigDecimal(12));
                directDebit.setDdAuthBy(resultSet.getString(13));
                directDebit.setDdAuthDate(resultSet.getDate(14));
                directDebit.setDdAuthorized(resultSet.getString(15));
                directDebit.setBankBranch(resultSet.getString(16));
                directDebitList.add(directDebit);
            }
            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return directDebitList;
    }


    public List<DirectDebit> fetchDirectDebitReport() {
        List<DirectDebit> directDebitList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getDirectDebitsReport(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            directDebitList = new ArrayList<DirectDebit>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setObject(2, session.getAttribute("ddCode"));
            statement.setObject(3, session.getAttribute("query"));
            statement.execute();
            OracleResultSet rs = (OracleResultSet)statement.getObject(1);
            String period = null;

            while (rs.next()) {
                DirectDebit directDebit = new DirectDebit();
                directDebit.setDddCode(rs.getBigDecimal(1));
                directDebit.setPolicyNo(rs.getString(2));
                directDebit.setAccountNo(rs.getString(3));
                directDebit.setSortCode(rs.getString(4));
                directDebit.setAmount(rs.getBigDecimal(5));
                directDebit.setAccName(rs.getString(6));
                directDebit.setNarration(rs.getString(7));
                directDebit.setCompany(rs.getString(8));
                directDebit.setBbRefCode(rs.getString(9));
                directDebit.setNextDueDate(rs.getDate(10));
                directDebit.setPayFreq(rs.getString(11));
                directDebit.setDdRefNumber(rs.getString(12));
                directDebit.setDdBankBranch(rs.getString(13));
                directDebit.setDdDebitDay(rs.getString(14));
                period = rs.getString(15);
                directDebitList.add(directDebit);
            }
            String ddFileDesc = null;
            if (session.getAttribute("ddFileDesc") != null) {
                ddFileDesc =
                        session.getAttribute("ddFileDesc").toString() + " - " +
                        period;
                session.setAttribute("ddFileDesc", ddFileDesc);
            }
            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return directDebitList;
    }

    /**
     * Fetches all <code>DirectDebit</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>DirectDebit</code> objects/records fetched
     * from the database.
     */
    public List<DirectDebit> fetchDirectDebitAuthorised() {
        List<DirectDebit> directDebitList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getDirectDebitAuthorised();end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            directDebitList = new ArrayList<DirectDebit>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                DirectDebit directDebit = new DirectDebit();
                directDebit.setDdCode(resultSet.getBigDecimal(1));
                directDebit.setDdRefNo(resultSet.getString(2));
                directDebit.setDdSentYn(resultSet.getString(3));
                directDebit.setDdAccountNum(resultSet.getString(4));
                directDebit.setDdBookDate(resultSet.getDate(5));
                directDebit.setDdBbrCode(resultSet.getBigDecimal(6));
                directDebit.setDdStatus(resultSet.getString(7));
                directDebit.setDdReceipted(resultSet.getString(8));
                directDebit.setDdValueDate(resultSet.getDate(9));
                directDebit.setDdRaisedBy(resultSet.getString(10));
                directDebit.setDdDate(resultSet.getDate(11));
                directDebit.setDdBankCode(resultSet.getBigDecimal(12));
                directDebit.setDdAuthBy(resultSet.getString(13));
                directDebit.setDdAuthDate(resultSet.getDate(14));
                directDebit.setDdAuthorized(resultSet.getString(15));
                directDebit.setBankBranch(resultSet.getString(16));
                directDebit.setDD_RECEIPT_DATE(resultSet.getDate(17));
                directDebit.setDD_RECEIPT_NO(resultSet.getString(18)); 
                directDebit.setDD_ALLOC_STATUS(resultSet.getString(19));
                directDebit.setDD_TOTAL_RECORDS(resultSet.getBigDecimal(20));
                directDebit.setDD_RECORDS_ALLOCATED(resultSet.getBigDecimal(21));
                directDebit.setDD_RECORDS_ALLOC_SUCCESS(resultSet.getBigDecimal(22)); 
                directDebit.setDD_RECORDS_ALLOC_FAIL(resultSet.getBigDecimal(23));
                directDebit.setDD_ALLOC_PERCENT(resultSet.getBigDecimal(24)); 
                directDebit.setDD_ALLOC_SUCCESS_PERC(resultSet.getBigDecimal(25)); 
                directDebit.setDD_EST_ALLOC_COMPL_DATE(resultSet.getDate(26));
                directDebit.setDD_ALLOC_STATUS_CODE(resultSet.getString(27));
                directDebitList.add(directDebit);
            }
            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return directDebitList;
    }

    /**
     * Fetches all <code>DirectDebitHeader</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>DirectDebitHeader</code> objects/records fetched
     * from the database.
     */
    public List<DirectDebitHeader> fetchDirectDebitHeaders() {
        List<DirectDebitHeader> directDebitHeaderList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getDirectDebitHeader(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            directDebitHeaderList = new ArrayList<DirectDebitHeader>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("ddCode") != null) {
                BigDecimal ddCode =
                    new BigDecimal(session.getAttribute("ddCode").toString());
                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, ddCode);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    DirectDebitHeader directDebitHeader =
                        new DirectDebitHeader();
                    directDebitHeader.setDdhCode(resultSet.getBigDecimal(1));
                    directDebitHeader.setDdhDDCode(resultSet.getBigDecimal(2));
                    directDebitHeader.setDdhClientCode(resultSet.getBigDecimal(3));
                    directDebitHeader.setDdhClientBbrCode(resultSet.getBigDecimal(4));
                    directDebitHeader.setDdhBbrBankCode(resultSet.getBigDecimal(5));
                    directDebitHeader.setDdhClientShortDesc(resultSet.getString(6));
                    directDebitHeader.setDdhClientName(resultSet.getString(7));
                    directDebitHeader.setDdhClientBankAccNum(resultSet.getString(8));
                    directDebitHeader.setDdhBbrBranchName(resultSet.getString(9));
                    directDebitHeader.setDdhBbrShortDesc(resultSet.getString(10));
                    directDebitHeader.setDdhBbrRefCode(resultSet.getString(11));
                    directDebitHeader.setDdhTotAmount(resultSet.getBigDecimal(12));
                    directDebitHeader.setDdhStatus(resultSet.getString(13));
                    directDebitHeader.setDdhReceipted(resultSet.getString(14));
                    directDebitHeader.setDdhFailDate(resultSet.getDate(15));
                    directDebitHeader.setDdhFailUpdatedBy(resultSet.getString(16));
                    directDebitHeader.setDdhFailUpdateDate(resultSet.getDate(17));
                    directDebitHeader.setDdhFailRemarks(resultSet.getString(18));
                    directDebitHeader.setDdhRelaunchDate(resultSet.getDate(19));
                    directDebitHeader.setDdhRelaunchStopDate(resultSet.getDate(20));
                    directDebitHeader.setDdhRelaunchedBy(resultSet.getString(21));
                    directDebitHeader.setDdhRelaunchStoppedBy(resultSet.getString(22));
                    directDebitHeader.setDdhInitialBookDate(resultSet.getDate(23));
                    directDebitHeader.setDdhPrevDdhCode(resultSet.getBigDecimal(24));
                    directDebitHeader.setDdhAccHolder(resultSet.getString(25));
                    directDebitHeader.setBankBranch(resultSet.getString(26));

                    directDebitHeaderList.add(directDebitHeader);
                }
                resultSet.close();
                statement.close();
                connection.close();
            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return directDebitHeaderList;
    }

    /**
     * Fetches all <code>DirectDebitHeader</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>DirectDebitHeader</code> objects/records fetched
     * from the database.
     */
    public List<DirectDebitHeader> fetchDirectDebitHeaders2() {
        List<DirectDebitHeader> directDebitHeaderList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getDirectDebitHeader(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            directDebitHeaderList = new ArrayList<DirectDebitHeader>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("ddCode") != null) {
                BigDecimal ddCode =
                    new BigDecimal(session.getAttribute("ddCode").toString());
                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, ddCode);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    DirectDebitHeader directDebitHeader =
                        new DirectDebitHeader();
                    directDebitHeader.setDdhCode(resultSet.getBigDecimal(1));
                    directDebitHeader.setDdhDDCode(resultSet.getBigDecimal(2));
                    directDebitHeader.setDdhClientCode(resultSet.getBigDecimal(3));
                    directDebitHeader.setDdhClientBbrCode(resultSet.getBigDecimal(4));
                    directDebitHeader.setDdhBbrBankCode(resultSet.getBigDecimal(5));
                    directDebitHeader.setDdhClientShortDesc(resultSet.getString(6));
                    directDebitHeader.setDdhClientName(resultSet.getString(7));
                    directDebitHeader.setDdhClientBankAccNum(resultSet.getString(8));
                    directDebitHeader.setDdhBbrBranchName(resultSet.getString(9));
                    directDebitHeader.setDdhBbrShortDesc(resultSet.getString(10));
                    directDebitHeader.setDdhBbrRefCode(resultSet.getString(11));
                    directDebitHeader.setDdhTotAmount(resultSet.getBigDecimal(12));
                    directDebitHeader.setDdhStatus(resultSet.getString(13));
                    directDebitHeader.setDdhReceipted(resultSet.getString(14));
                    directDebitHeader.setDdhFailDate(resultSet.getDate(15));
                    directDebitHeader.setDdhFailUpdatedBy(resultSet.getString(16));
                    directDebitHeader.setDdhFailUpdateDate(resultSet.getDate(17));
                    directDebitHeader.setDdhFailRemarks(resultSet.getString(18));
                    directDebitHeader.setDdhRelaunchDate(resultSet.getDate(19));
                    directDebitHeader.setDdhRelaunchStopDate(resultSet.getDate(20));
                    directDebitHeader.setDdhRelaunchedBy(resultSet.getString(21));
                    directDebitHeader.setDdhRelaunchStoppedBy(resultSet.getString(22));
                    directDebitHeader.setDdhInitialBookDate(resultSet.getDate(23));
                    directDebitHeader.setDdhPrevDdhCode(resultSet.getBigDecimal(24));
                    directDebitHeader.setDdhAccHolder(resultSet.getString(25));
                    directDebitHeader.setBankBranch(resultSet.getString(26));
                    directDebitHeaderList.add(directDebitHeader);
                }
                resultSet.close();
                statement.close();
                connection.close();
            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return directDebitHeaderList;
    }

    /**
     * Fetches all <code>DirectDebitDetail</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>DirectDebitDetail</code> objects/records fetched
     * from the database.
     */
    public List<DirectDebitDetail> fetchDirectDebitDetails() {
        List<DirectDebitDetail> directDebitDetailList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getDirectDebitDetail(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            directDebitDetailList = new ArrayList<DirectDebitDetail>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            System.out.println("session.getAttribute(\"ddhCode\")" +
                               session.getAttribute("ddhCode"));

            if (session.getAttribute("ddhCode") != null) {
                BigDecimal ddhCode =
                    new BigDecimal(session.getAttribute("ddhCode").toString());
                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, ddhCode);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    DirectDebitDetail directDebitDetail =
                        new DirectDebitDetail();
                    directDebitDetail.setDddCode(resultSet.getBigDecimal(1));
                    directDebitDetail.setDddDdhCode(resultSet.getBigDecimal(2));
                    directDebitDetail.setDddDdCode(resultSet.getBigDecimal(3));
                    directDebitDetail.setDddSysCode(resultSet.getBigDecimal(4));
                    directDebitDetail.setDddPolCode(resultSet.getBigDecimal(5));
                    directDebitDetail.setDddPolPrpCode(resultSet.getBigDecimal(6));
                    directDebitDetail.setDddPolProposalNo(resultSet.getString(7));
                    directDebitDetail.setDddPolPolicyNo(resultSet.getString(8));
                    directDebitDetail.setDddOtherIdentifier(resultSet.getString(9));
                    directDebitDetail.setDddAmount(resultSet.getBigDecimal(10));
                    directDebitDetail.setDddRemarks(resultSet.getString(11));
                    directDebitDetail.setDddStartDate(resultSet.getDate(12));
                    directDebitDetail.setDddStopDate(resultSet.getDate(13));
                    directDebitDetail.setDddStatus(resultSet.getString(14));
                    directDebitDetail.setDddFailDate(resultSet.getDate(15));
                    directDebitDetail.setDddReceipted(resultSet.getString(16));
                    directDebitDetail.setDddFailUpdatedBy(resultSet.getString(17));
                    directDebitDetail.setDddFailUpdateDate(resultSet.getDate(18));
                    directDebitDetail.setDddPprCode(resultSet.getBigDecimal(19));
                    directDebitDetail.setDddPolType(resultSet.getString(20));
                    directDebitDetail.setDddReceiptedBy(resultSet.getString(21));
                    directDebitDetail.setDddReceiptNo(resultSet.getString(22));
                    directDebitDetail.setDddReceiptDate(resultSet.getDate(23));
                    directDebitDetail.setDddReceiptedDate(resultSet.getDate(24));
                    directDebitDetail.setDddFailRemarks(resultSet.getString(25));
                    directDebitDetail.setDddRelaunchDate(resultSet.getDate(26));
                    directDebitDetail.setDddRelaunchStopDate(resultSet.getDate(27));
                    directDebitDetail.setDddRelaunchedBy(resultSet.getString(28));
                    directDebitDetail.setDddRelaunchStoppedBy(resultSet.getString(29));
                    directDebitDetailList.add(directDebitDetail);
                }
                resultSet.close();
                statement.close();
                connection.close();
            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return directDebitDetailList;
    }

    /**
     * Fetches all <code>DirectDebitDetail</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>DirectDebitDetail</code> objects/records fetched
     * from the database.
     */
    public List<DirectDebitDetail> fetchDirectDebitDetails2() {
        List<DirectDebitDetail> directDebitDetailList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getDirectDebitDetail(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            directDebitDetailList = new ArrayList<DirectDebitDetail>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("ddhCode") != null) {
                BigDecimal ddhCode =
                    new BigDecimal(session.getAttribute("ddhCode").toString());
                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, ddhCode);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    DirectDebitDetail directDebitDetail =
                        new DirectDebitDetail();
                    directDebitDetail.setDddCode(resultSet.getBigDecimal(1));
                    directDebitDetail.setDddDdhCode(resultSet.getBigDecimal(2));
                    directDebitDetail.setDddDdCode(resultSet.getBigDecimal(3));
                    directDebitDetail.setDddSysCode(resultSet.getBigDecimal(4));
                    directDebitDetail.setDddPolCode(resultSet.getBigDecimal(5));
                    directDebitDetail.setDddPolPrpCode(resultSet.getBigDecimal(6));
                    directDebitDetail.setDddPolProposalNo(resultSet.getString(7));
                    directDebitDetail.setDddPolPolicyNo(resultSet.getString(8));
                    directDebitDetail.setDddOtherIdentifier(resultSet.getString(9));
                    directDebitDetail.setDddAmount(resultSet.getBigDecimal(10));
                    directDebitDetail.setDddRemarks(resultSet.getString(11));
                    directDebitDetail.setDddStartDate(resultSet.getDate(12));
                    directDebitDetail.setDddStopDate(resultSet.getDate(13));
                    directDebitDetail.setDddStatus(resultSet.getString(14));
                    directDebitDetail.setDddFailDate(resultSet.getDate(15));
                    directDebitDetail.setDddReceipted(resultSet.getString(16));
                    directDebitDetail.setDddFailUpdatedBy(resultSet.getString(17));
                    directDebitDetail.setDddFailUpdateDate(resultSet.getDate(18));
                    directDebitDetail.setDddPprCode(resultSet.getBigDecimal(19));
                    directDebitDetail.setDddPolType(resultSet.getString(20));
                    directDebitDetail.setDddReceiptedBy(resultSet.getString(21));
                    directDebitDetail.setDddReceiptNo(resultSet.getString(22));
                    directDebitDetail.setDddReceiptDate(resultSet.getDate(23));
                    directDebitDetail.setDddReceiptedDate(resultSet.getDate(24));
                    directDebitDetail.setDddFailRemarks(resultSet.getString(25));
                    directDebitDetail.setDddRelaunchDate(resultSet.getDate(26));
                    directDebitDetail.setDddRelaunchStopDate(resultSet.getDate(27));
                    directDebitDetail.setDddRelaunchedBy(resultSet.getString(28));
                    directDebitDetail.setDddRelaunchStoppedBy(resultSet.getString(29));
                    directDebitDetail.setDddBankAmount(resultSet.getBigDecimal(30));
                    directDebitDetailList.add(directDebitDetail);
                }
                resultSet.close();
                statement.close();
                connection.close();
            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return directDebitDetailList;
    }

    public java.sql.Date getBusinessDate(java.sql.Date ddBookDate) {
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := Direct_Debit_Pkg.Get_business_date(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        Date failDate = null;

        try {
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (ddBookDate != null) {
                statement.registerOutParameter(1, OracleTypes.DATE);
                statement.setDate(2, ddBookDate);
                statement.setBigDecimal(3,
                                        new BigDecimal("4")); // 4 is hardcoded on the V3 form
                statement.execute();

                failDate = statement.getDate(1);

                statement.close();
                connection.close();
            } else {
                return null;
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return failDate;
    }

    /** Get a String (yyyy/mm/dd) and returns a java.sql.Date.
     * It is useful to get user input and insert it in
     * the database like MySQL.
     *
     * @param strDate a String in the format dd/mm/yyyy
     * @return java.sql.Date
     */
    public static java.sql.Date getSqlDate(String strDate) {
        String[] splitedDate = strDate.split("-");
        int year = Integer.parseInt(splitedDate[0]) - 1900;
        int month = Integer.parseInt(splitedDate[1]) - 1;
        int day = Integer.parseInt(splitedDate[2]);

        return new java.sql.Date(year, month, day);
    }

    public String getMonthName(BigDecimal monthIndex) {
        String months[] = new String[13];
        months[0] = null;
        months[1] = "January";
        months[2] = "February";
        months[3] = "March";
        months[4] = "April";
        months[5] = "May";
        months[6] = "June";
        months[7] = "July";
        months[8] = "August";
        months[9] = "September";
        months[10] = "October";
        months[11] = "November";
        months[12] = "December";

        return months[monthIndex.intValue()];
    }
    
    
    
    public List<DirectDebitDetail> findDDProducts() {
                       
            DBConnector dbConnector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;
            OracleResultSet rst =null;
            conn = (OracleConnection)dbConnector.getDatabaseConnection();
              

            List<DirectDebitDetail> directDebitList = new ArrayList<DirectDebitDetail>();

            try {
                String polQuery =
                    "begin DIRECT_DEBIT_PKG.get_dd_lms_products(?);end;";
                cst = (OracleCallableStatement)conn.prepareCall(polQuery);
                cst.registerOutParameter(1, OracleTypes.CURSOR);
                
                cst.execute();
                rst = (OracleResultSet)cst.getObject(1);
                while (rst.next()) {

                    DirectDebitDetail ddDetail = new DirectDebitDetail();

                    ddDetail.setPROD_CODE(rst.getBigDecimal(1));
                    ddDetail.setPROD_DESC(rst.getString(2));
                    ddDetail.setPROD_UMBRELLA(rst.getString(3));
                    ddDetail.setPROD_CLA_CODE(rst.getBigDecimal(4));
                    ddDetail.setCLA_DESC(rst.getString(5));
                    ddDetail.setPROD_TYPE(rst.getString(6));

                    directDebitList.add(ddDetail);

                }
                rst.close();
                cst.close();
                conn.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                e.printStackTrace();
            } finally {
                GlobalCC.CloseConnections(rst, cst, conn);
            }
            return directDebitList;
        }
}
