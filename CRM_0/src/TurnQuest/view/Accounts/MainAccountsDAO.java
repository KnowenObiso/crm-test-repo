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

package TurnQuest.view.Accounts;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.HibernateUtil;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.Usr.User;
import TurnQuest.view.client.Client2;
import TurnQuest.view.models.AgencySystem;

import TurnQuest.view.models.Agent;

import java.math.BigDecimal;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;

import org.apache.commons.dbutils.DbUtils;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;


/**
 * This class serves as the main DAO class for retriving the necessary objects
 * from the database relating to Accounts.
 *
 * @author Frankline Ogongi
 */
public class MainAccountsDAO {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private String accountTypeCode;

    /**
     * Default Class Constructor
     */
    public MainAccountsDAO() {
        super();
    }


    /**
     * Fetches all <code>AccountsType</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>AccountsType</code> objects/records
     * fetched from the database.
     */
    public List<AccountsType> fetchAllAccountTypes() {

        List<AccountsType> accountsList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.search_account_types(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            accountsList = new ArrayList<AccountsType>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);


            String accountTypeCode =
                (String)session.getAttribute("accountTypeCode");

            statement.setString(1, accountTypeCode);


            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(2);

            while (resultSet.next()) {
                // For every row, create a AccountsType object with the
                // values and add it to the list
                AccountsType accountType = new AccountsType();

                accountType.setCode(resultSet.getString(1));
                accountType.setAccountType(resultSet.getString(2));
                accountType.setAccountTypeShortDesc(resultSet.getString(3));
                accountType.setWthtxRate(resultSet.getString(4));
                accountType.setTypeId(resultSet.getString(5));
                accountType.setCommReserveRate(resultSet.getString(6));
                accountType.setMaxAdvAmt(resultSet.getString(7));
                accountType.setMaxAdvRepaymentPrd(resultSet.getString(8));
                accountType.setRcptsIncludeComm(resultSet.getString(9));
                accountType.setOverideRate(resultSet.getString(10));
                accountType.setIdSerialFormat(resultSet.getString(11));
                accountType.setVatRate(resultSet.getString(12));

                accountsList.add(accountType);
            }

            resultSet.close();
            statement.close();
            connection.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        session.removeAttribute("accountTypeCode");

        return accountsList;
    }

    public List<AccountsType> fetchAllAgents() {
        BigDecimal accountCode;
        accountCode =
                new BigDecimal(session.getAttribute("accType").toString());
        List<AccountsType> accountsList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.select_Parent_Agency(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            accountsList = new ArrayList<AccountsType>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setBigDecimal(2, accountCode);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a AccountsType object with the
                // values and add it to the list
                AccountsType accountType = new AccountsType();

                accountType.setAgentCode(resultSet.getBigDecimal(1));
                accountType.setAgentname(resultSet.getString(2));
                accountType.setAgentShtDesc(resultSet.getString(3));
                accountsList.add(accountType);

            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        session.removeAttribute("accountTypeCode");

        return accountsList;
    }

    /**
     * Fetches all <code>Agency</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>Agency</code> objects/records
     * fetched from the database.
     */
    public List<Agency> fetchAllAccountAgencies() {

        List<Agency> agenciesList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_agencies(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            agenciesList = new ArrayList<Agency>();
            connection =
                    (OracleConnection)(OracleConnection)dbConnector.getDatabaseConnection();
            statement =
                    (OracleCallableStatement)(OracleCallableStatement)connection.prepareCall(query);


            String accountTypeCode =
                (String)session.getAttribute("accountTypeCode");

            if (accountTypeCode == null || accountTypeCode == "") {
                return null;
            } else if (accountTypeCode != null || accountTypeCode != "") {
                statement.setString(1, accountTypeCode);


                statement.registerOutParameter(2, OracleTypes.CURSOR);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(2);

                while (resultSet.next()) {
                    // For every row, create a AgencyHoldingCompany object with
                    // the values and add it to the list
                    Agency agency = new Agency();

                    agency.setCode(resultSet.getString(1));
                    agency.setAccountCode(resultSet.getString(2));
                    agency.setShortDesc(resultSet.getString(3));
                    agency.setName(resultSet.getString(4));
                    agency.setPhysicalAddress(resultSet.getString(5));
                    agency.setPostalAddress(resultSet.getString(6));
                    agency.setTownCode(resultSet.getString(7));
                    agency.setCountryCode(resultSet.getString(8));
                    agency.setEmailAddress(resultSet.getString(9));
                    agency.setWebAddress(resultSet.getString(10));
                    agency.setZip(resultSet.getString(11));
                    agency.setContactPerson(resultSet.getString(12));
                    agency.setContactTitle(resultSet.getString(13));
                    agency.setTelephone1(resultSet.getString(14));
                    agency.setTelephone2(resultSet.getString(15));
                    agency.setFax(resultSet.getString(16));
                    agency.setAccountNum(resultSet.getString(17));
                    agency.setPIN(resultSet.getString(18));
                    agency.setCommission(resultSet.getString(19));
                    agency.setCreditAllowed(resultSet.getString(20));
                    agency.setWitholdTx(resultSet.getString(21));
                    agency.setPrintDbNote(resultSet.getString(22));
                    agency.setStatus(resultSet.getString(23));
                    agency.setDateCreated(resultSet.getDate(24));
                    agency.setCreatedBy(resultSet.getString(25));
                    agency.setRegistrationCode(resultSet.getString(26));
                    agency.setCommReserverate(resultSet.getString(27));
                    agency.setAnnualBudget(resultSet.getString(28));
                    agency.setStatusEffectiveDate(resultSet.getDate(29));
                    agency.setCreditPeriod(resultSet.getString(30));
                    agency.setCommStatEffectiveDate(resultSet.getDate(31));
                    agency.setCommStatusDate(resultSet.getDate(32));
                    agency.setCommAllowed(resultSet.getString(33));
                    agency.setChecked(resultSet.getString(34));
                    agency.setCheckedBy(resultSet.getString(35));
                    agency.setCheckDate(resultSet.getDate(36));
                    agency.setCompCommArrears(resultSet.getString(37));
                    agency.setReinsurer(resultSet.getString(38));
                    agency.setBranchCode(resultSet.getString(39));
                    agency.setTown(resultSet.getString(40));
                    agency.setCountry(resultSet.getString(41));
                    agency.setStatusDesc(resultSet.getString(42));
                    agency.setIDNum(resultSet.getString(43));
                    agency.setConCode(resultSet.getString(44));
                    agency.setAgentCode(resultSet.getString(45));
                    agency.setSms(resultSet.getString(46));
                    agency.setHoldCompanyCode(resultSet.getString(47));
                    agency.setSectorCode(resultSet.getString(48));
                    agency.setClassCode(resultSet.getString(49));
                    agency.setExpiriyDate(resultSet.getDate(50));
                    agency.setLicenseNum(resultSet.getString(51));
                    agency.setRunOff(resultSet.getString(52));
                    agency.setLicensed(resultSet.getString(53));
                    agency.setLicenseGracePeriod(resultSet.getString(54));
                    agency.setOldAccountNum(resultSet.getString(55));
                    agency.setStatusRemarks(resultSet.getString(56));
                    agency.setBranchName(resultSet.getString(57));
                    agency.setHoldCompanyName(resultSet.getString(58));
                    agency.setSectorName(resultSet.getString(59));
                    agency.setClassName(resultSet.getString(60));


                    agenciesList.add(agency);
                }

                resultSet.close();
                statement.close();
                connection.close();
            }

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return agenciesList;
    }

    /**
     * Fetches all <code>Agency</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>Agency</code> objects/records
     * fetched from the database.
     */
    public List<Agency> fetchAllAccountAgenciesInfoOnly() {
        int count = 0;
        List<Agency> agenciesList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_agency_info(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            agenciesList = new ArrayList<Agency>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);


            if (session.getAttribute("accountTypeCode") != null) {
                String accountTypeCode =
                    session.getAttribute("accountTypeCode").toString();
                statement.setString(1, accountTypeCode);


                statement.registerOutParameter(2, OracleTypes.CURSOR);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(2);

                while (resultSet.next()) {
                    // For every row, create a AgencyHoldingCompany object with
                    // the values and add it to the list
                    Agency agency = new Agency();

                    agency.setCode(resultSet.getString(1));
                    agency.setAccountCode(resultSet.getString(2));
                    agency.setShortDesc(resultSet.getString(3));
                    agency.setName(resultSet.getString(4));
                    agency.setPhysicalAddress(resultSet.getString(5));
                    agency.setPostalAddress(resultSet.getString(6));
                    agency.setTownCode(resultSet.getString(7));
                    agency.setCountryCode(resultSet.getString(8));
                    agency.setEmailAddress(resultSet.getString(9));
                    agency.setWebAddress(resultSet.getString(10));
                    agency.setZip(resultSet.getString(11));
                    agency.setContactPerson(resultSet.getString(12));
                    agency.setContactTitle(resultSet.getString(13));
                    agency.setTelephone1(resultSet.getString(14));
                    agency.setTelephone2(resultSet.getString(15));
                    agency.setFax(resultSet.getString(16));
                    agency.setAccountNum(resultSet.getString(17));
                    agency.setPIN(resultSet.getString(18));
                    agency.setCommission(resultSet.getString(19));
                    agency.setCreditAllowed(resultSet.getString(20));
                    agency.setWitholdTx(resultSet.getString(21));
                    agency.setPrintDbNote(resultSet.getString(22));
                    agency.setStatus(resultSet.getString(23));
                    agency.setDateCreated(resultSet.getDate(24));
                    agency.setCreatedBy(resultSet.getString(25));
                    agency.setRegistrationCode(resultSet.getString(26));
                    agency.setCommReserverate(resultSet.getString(27));
                    agency.setAnnualBudget(resultSet.getString(28));
                    agency.setStatusEffectiveDate(resultSet.getDate(29));
                    agency.setCreditPeriod(resultSet.getString(30));
                    agency.setCommStatEffectiveDate(resultSet.getDate(31));
                    agency.setCommStatusDate(resultSet.getDate(32));
                    agency.setCommAllowed(resultSet.getString(33));
                    agency.setChecked(resultSet.getString(34));
                    agency.setCheckedBy(resultSet.getString(35));
                    agency.setCheckDate(resultSet.getDate(36));
                    agency.setCompCommArrears(resultSet.getString(37));
                    agency.setReinsurer(resultSet.getString(38));
                    agency.setBranchCode(resultSet.getString(39));
                    agency.setTown(resultSet.getString(40));
                    agency.setCountry(resultSet.getString(41));
                    agency.setStatusDesc(resultSet.getString(42));
                    agency.setIDNum(resultSet.getString(43));
                    agency.setConCode(resultSet.getString(44));
                    agency.setAgentCode(resultSet.getString(45));
                    agency.setSms(resultSet.getString(46));
                    agency.setHoldCompanyCode(resultSet.getString(47));
                    agency.setSectorCode(resultSet.getString(48));
                    agency.setClassCode(resultSet.getString(49));
                    agency.setExpiriyDate(resultSet.getDate(50));
                    agency.setLicenseNum(resultSet.getString(51));
                    agency.setRunOff(resultSet.getString(52));
                    agency.setLicensed(resultSet.getString(53));
                    agency.setLicenseGracePeriod(resultSet.getString(54));
                    agency.setOldAccountNum(resultSet.getString(55));
                    agency.setStatusRemarks(resultSet.getString(56));
                    agency.setBankCode(resultSet.getString(57));
                    agency.setBankName(resultSet.getString(58));
                    agency.setBankBranchCode(resultSet.getString(59));
                    agency.setBankBranchName(resultSet.getString(60));
                    agency.setAccountNo(resultSet.getString(61));
                    agency.setAgentPrefix(resultSet.getString(62));
                    agency.setCouAdminType(resultSet.getString(63));
                    agency.setAgentStateCode(resultSet.getString(64));
                    agency.setAgentStateName(resultSet.getString(65));
                    agency.setAgentCrRating(resultSet.getString(66));
                    agency.setClientName(resultSet.getString(67));
                    agency.setAccountManager(resultSet.getString(68));

                    agency.setPromTransDate(resultSet.getDate(69));
                    agency.setTransType(resultSet.getString(70));
                    agency.setPromDemType(resultSet.getString(71));
                    agency.setBrnName(resultSet.getString(72));
                    agency.setBraName(resultSet.getString(73));
                    agency.setBrnCode(resultSet.getBigDecimal(74));

                    agency.setAgencyPrefix(resultSet.getBigDecimal(75));
                    agency.setUnitPrefix(resultSet.getBigDecimal(76));
                    agency.setAgencySeqNumber(resultSet.getBigDecimal(77));
                    agency.setPrecontractCode(resultSet.getBigDecimal(78));
                    agency.setCouZipCode(resultSet.getString(79));
                    agency.setAccountManagerCode(resultSet.getBigDecimal(80));
                    agency.setAgencyLimit(resultSet.getBigDecimal(81));
                    agency.setBruCode(resultSet.getBigDecimal(82));
                    agency.setBruName(resultSet.getString(83));
                    agency.setLocalInternational(resultSet.getString(84));
                    agency.setRegulatorNumber(resultSet.getString(85));
                    agency.setAuthorised(resultSet.getString(86));
                    agency.setRorgCode(resultSet.getBigDecimal(87));
                    agency.setOrsCode(resultSet.getBigDecimal(88));
                    agency.setRorgDesc(resultSet.getString(89));
                    agency.setOrsDesc(resultSet.getString(90));
                    agency.setAllocateCerts(resultSet.getString(91));
                    agency.setHoldingCompany(resultSet.getString(92));
                    agency.setBouncedCheque(resultSet.getString(93));
                    agency.setDefaultCommMode(resultSet.getString(94));
                    agency.setAgnBpnCode(resultSet.getBigDecimal(95));
                    agency.setBpnName(resultSet.getString(96));
                    agency.setAgencyType(resultSet.getString(97));
                    agency.setAgencyGroup(resultSet.getString(98));
                    agency.setMainAgnCode(resultSet.getBigDecimal(99));
                    agency.setMainAgent(resultSet.getString(100));
                    agency.setVatApplicable(resultSet.getString(101));
                    agency.setWhtaxApplicable(resultSet.getString(102));
                    agency.setTelPay(resultSet.getString(103));
                    agency.setPmtFreq(resultSet.getString(104));
                    agency.setCpmMode(resultSet.getBigDecimal(105));
                    agency.setCpmModeDesc(resultSet.getString(106));
                    agency.setPmtValidated(resultSet.getString(107));
                    agency.setAGN_COMM_LEVY_APP(resultSet.getString(108));
                    agency.setAGN_COMM_LEVY_RATE(resultSet.getBigDecimal(109));
                    agency.setRegionCode(resultSet.getString(110));
                    agency.setRegionName(resultSet.getString(111));
                    agency.setTaxAuthorityName(resultSet.getString(112));
                    agency.setAgnUpdatedBy(resultSet.getString(113));
                    agency.setAgnUpdatedOn(resultSet.getString(114));
                    agency.setSelect(false);

                    // agency.setBru_code(resultSet.getBigDecimal(69));
                    agenciesList.add(agency);
                    if (session.getAttribute("agencySearch") != null) {
                        if (resultSet.getString(4) != null &&
                            session.getAttribute("otherNames") != null) {
                            if (session.getAttribute("otherNames").toString().equalsIgnoreCase(resultSet.getString(4))) {
                                count = count + 1;
                            }

                        }
                        if (resultSet.getString(14) != null &&
                            session.getAttribute("telNumber") != null) {
                            if (session.getAttribute("telNumber").toString().equalsIgnoreCase(resultSet.getString(14))) {
                                count = count + 1;
                            }

                        }
                        if (resultSet.getString(9) != null &&
                            session.getAttribute("emailAddress") != null) {
                            if (session.getAttribute("emailAddress").toString().equalsIgnoreCase(resultSet.getString(9))) {
                                count = count + 1;
                            }

                        }
                        if (resultSet.getString(18) != null &&
                            session.getAttribute("pinNumber") != null) {
                            if (session.getAttribute("pinNumber").toString().equalsIgnoreCase(resultSet.getString(18))) {
                                count = count + 1;
                            }

                        }
                        if (resultSet.getString(5) != null &&
                            session.getAttribute("physicalAddress") != null) {
                            if (session.getAttribute("physicalAddress").toString().equalsIgnoreCase(resultSet.getString(5))) {
                                count = count + 1;
                            }

                        }
                    }
                }
                session.setAttribute("countAgents", count);
                resultSet.close();
                statement.close();
                connection.close();
            }

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return agenciesList;
    }

    public List<Agency> fetchAllAccountAgenciesBasedOnNames() {
        int count = 0;
        String physicalAddress;
        String emailAddress;
        String agnPin;
        String agnTel;
        List<Agency> agenciesList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_agency_infoByNames(?,?,?,?,?,?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        if (session.getAttribute("physicalAddress") != null) {
            physicalAddress =
                    session.getAttribute("physicalAddress").toString();
        } else {
            physicalAddress = null;
        }
        if (session.getAttribute("emailAddress") != null) {
            emailAddress = session.getAttribute("emailAddress").toString();
        } else {
            emailAddress = null;

        }
        if (session.getAttribute("pinNumber") != null) {
            agnPin = session.getAttribute("pinNumber").toString();
        } else {
            agnPin = null;
        }
        if (session.getAttribute("telNumber") != null) {
            agnTel = session.getAttribute("telNumber").toString();
        } else {
            agnTel = null;
        }
        if (session.getAttribute("otherNames") != null) {
            String fullName = (String)session.getAttribute("otherNames");
            try {
                agenciesList = new ArrayList<Agency>();
                connection =
                        (OracleConnection)(OracleConnection)dbConnector.getDatabaseConnection();
                statement =
                        (OracleCallableStatement)(OracleCallableStatement)connection.prepareCall(query);


                if (session.getAttribute("accountTypeCode") != null) {
                    String accountTypeCode =
                        session.getAttribute("accountTypeCode").toString();
                    statement.setString(1, accountTypeCode);
                    statement.setString(2, fullName);


                    statement.registerOutParameter(3, OracleTypes.CURSOR);
                    statement.setString(4, physicalAddress);
                    statement.setString(5, emailAddress);
                    statement.setString(6, agnPin);
                    statement.setString(7, agnTel);
                    statement.execute();
                    OracleResultSet resultSet =
                        (OracleResultSet)statement.getObject(3);

                    while (resultSet.next()) {
                        // For every row, create a AgencyHoldingCompany object with
                        // the values and add it to the list

                        count = count + 1;

                        Agency agency = new Agency();

                        agency.setCode(resultSet.getString(1));
                        agency.setAccountCode(resultSet.getString(2));
                        agency.setShortDesc(resultSet.getString(3));
                        agency.setName(resultSet.getString(4));
                        agency.setPhysicalAddress(resultSet.getString(5));
                        agency.setPostalAddress(resultSet.getString(6));
                        agency.setTownCode(resultSet.getString(7));
                        agency.setCountryCode(resultSet.getString(8));
                        agency.setEmailAddress(resultSet.getString(9));
                        agency.setWebAddress(resultSet.getString(10));
                        agency.setZip(resultSet.getString(11));
                        agency.setContactPerson(resultSet.getString(12));
                        agency.setContactTitle(resultSet.getString(13));
                        agency.setTelephone1(resultSet.getString(14));
                        agency.setTelephone2(resultSet.getString(15));
                        agency.setFax(resultSet.getString(16));
                        agency.setAccountNum(resultSet.getString(17));
                        agency.setPIN(resultSet.getString(18));
                        agency.setCommission(resultSet.getString(19));
                        agency.setCreditAllowed(resultSet.getString(20));
                        agency.setWitholdTx(resultSet.getString(21));
                        agency.setPrintDbNote(resultSet.getString(22));
                        agency.setStatus(resultSet.getString(23));
                        agency.setDateCreated(resultSet.getDate(24));
                        agency.setCreatedBy(resultSet.getString(25));
                        agency.setRegistrationCode(resultSet.getString(26));
                        agency.setCommReserverate(resultSet.getString(27));
                        agency.setAnnualBudget(resultSet.getString(28));
                        agency.setStatusEffectiveDate(resultSet.getDate(29));
                        agency.setCreditPeriod(resultSet.getString(30));
                        agency.setCommStatEffectiveDate(resultSet.getDate(31));
                        agency.setCommStatusDate(resultSet.getDate(32));
                        agency.setCommAllowed(resultSet.getString(33));
                        agency.setChecked(resultSet.getString(34));
                        agency.setCheckedBy(resultSet.getString(35));
                        agency.setCheckDate(resultSet.getDate(36));
                        agency.setCompCommArrears(resultSet.getString(37));
                        agency.setReinsurer(resultSet.getString(38));
                        agency.setBranchCode(resultSet.getString(39));
                        agency.setTown(resultSet.getString(40));
                        agency.setCountry(resultSet.getString(41));
                        agency.setStatusDesc(resultSet.getString(42));
                        agency.setIDNum(resultSet.getString(43));
                        agency.setConCode(resultSet.getString(44));
                        agency.setAgentCode(resultSet.getString(45));
                        agency.setSms(resultSet.getString(46));
                        agency.setHoldCompanyCode(resultSet.getString(47));
                        agency.setSectorCode(resultSet.getString(48));
                        agency.setClassCode(resultSet.getString(49));
                        agency.setExpiriyDate(resultSet.getDate(50));
                        agency.setLicenseNum(resultSet.getString(51));
                        agency.setRunOff(resultSet.getString(52));
                        agency.setLicensed(resultSet.getString(53));
                        agency.setLicenseGracePeriod(resultSet.getString(54));
                        agency.setOldAccountNum(resultSet.getString(55));
                        agency.setStatusRemarks(resultSet.getString(56));
                        agency.setBranchName(resultSet.getString(57));
                        agenciesList.add(agency);
                    }

                    session.setAttribute("count", count);

                    resultSet.close();
                    statement.close();
                    connection.close();
                }

            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            }
        }
        return agenciesList;
    }

    /**
     * Fetches all <code>AgencyClass</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>AgencyClass</code> objects/records
     * fetched from the database.
     */
    public List<AgencyClass> fetchAllAgencyClasses() {

        List<AgencyClass> classesList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_agency_classes(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            classesList = new ArrayList<AgencyClass>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a AgencyClass object with the
                // values and add it to the list
                AgencyClass agencyClass = new AgencyClass();

                agencyClass.setCode(resultSet.getString(1));
                agencyClass.setDescription(resultSet.getString(2));

                classesList.add(agencyClass);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return classesList;
    }

    public List<AgencyClass> fetchSubAgentsDetails() {
        BigDecimal agentCode;
        if (session.getAttribute("agencyCode") == null ||
            session.getAttribute("agencyCode").equals("")) {
            agentCode = null;
        } else {
            agentCode =
                    new BigDecimal(session.getAttribute("agencyCode").toString());
        }
        List<AgencyClass> classesList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.select_subAgents_datails(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            classesList = new ArrayList<AgencyClass>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setBigDecimal(2, agentCode);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a AgencyClass object with the
                // values and add it to the list
                AgencyClass agencyClass = new AgencyClass();

                agencyClass.setAgentCode(resultSet.getBigDecimal(1));
                agencyClass.setAccountCode(resultSet.getBigDecimal(2));
                agencyClass.setAgntShtDesc(resultSet.getString(3));
                agencyClass.setAgentName(resultSet.getString(4));
                agencyClass.setPhysicalAddress(resultSet.getString(5));
                agencyClass.setPostalAddress(resultSet.getString(6));
                agencyClass.setEmailAddress(resultSet.getString(7));
                agencyClass.setAgntSubagnt(resultSet.getString(8));
                agencyClass.setMainAgnt_code(resultSet.getBigDecimal(9));
                agencyClass.setAccountTypes(resultSet.getString(10));
                classesList.add(agencyClass);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return classesList;
    }

    /**
     * Fetches all accounts records for a given agency
     *
     * @return A list of all accounts for a given client
     */
    public List<AgencyAccounts> fetchAgencyAccounts() {

        List<AgencyAccounts> accountsList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "SELECT aga_code, aga_sht_desc, aga_name, aga_agn_code,\n" + 
            "                aga_created_by, aga_date_created, aga_status, aga_remarks,\n" + 
            "                aga_wef, aga_wet, div_name, div_code\n" + 
            "           FROM tqc_agency_accounts, tqc_divisions\n" + 
            "          WHERE aga_agn_code = :v_agn_code AND aga_div_code = div_code(+)";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            accountsList = new ArrayList<AgencyAccounts>();
           
           BigDecimal agencyCode=GlobalCC.checkBDNullValues(session.getAttribute("agencyCode")) ;
            if (agencyCode != null) {
                query=query.replaceAll(":v_agn_code",agencyCode.toString()); 
                
                 connection = (OracleConnection)dbConnector.getDatabaseConnection();
                 statement = (OracleCallableStatement)connection.prepareCall(query);
                OracleResultSet resultSet = (OracleResultSet)statement.executeQuery();

                while (resultSet.next()) {
                    AgencyAccounts account = new AgencyAccounts();

                    account.setCode(resultSet.getBigDecimal(1));
                    account.setShortDesc(resultSet.getString(2));
                    account.setName(resultSet.getString(3));
                    account.setAgentCode(resultSet.getBigDecimal(4));
                    account.setCreatedBy(resultSet.getString(5));
                    account.setDateCreated(resultSet.getDate(6));
                    account.setStatus(resultSet.getString(7));
                    account.setRemarks(resultSet.getString(8));
                    account.setWef(resultSet.getDate(9));
                    account.setWet(resultSet.getDate(10));
                    account.setDivName(resultSet.getString(11));
                    account.setDivCode(resultSet.getBigDecimal(12));
                    accountsList.add(account);
                }

                resultSet.close();
                statement.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return accountsList;
    }

    public List<Agency> fetchAllAgencies(String agentName, String agentId) {

        List<Agency> agenciesList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_AGENCIES_CURSORS.getAllAgencies(?,?); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            agenciesList = new ArrayList<Agency>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);


            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setString(2, agentName);
            statement.setString(3, agentId);
            statement.execute();
            OracleResultSet rs = (OracleResultSet)statement.getObject(1);


            while (rs.next()) {
                Agency agency = new Agency();

                agency.setAgentCode(rs.getString(1));
                agency.setAccountCode(rs.getString(2));
                agency.setShortDesc(rs.getString(3));
                agency.setName(rs.getString(4));
                agency.setLicensed(rs.getString(5));
                agenciesList.add(agency);
            }

            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return agenciesList;
    }

    public List<Client2> fetchAgencyClients() {

        List<Client2> clientList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin  ?:=TQC_CLIENTS_PKG.getAgentClients(?); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            clientList = new ArrayList<Client2>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);


            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setObject(2, session.getAttribute("agencyCode"));
            statement.execute();
            OracleResultSet rs = (OracleResultSet)statement.getObject(1);

            while (rs.next()) {
                Client2 client = new Client2();
                client.setClientCode(rs.getString(1));
                client.setShortDesc(rs.getString(2));
                client.setName(rs.getString(3));
                client.setSelected(false);

                clientList.add(client);
            }

            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return clientList;
    }

    public List<Client2> fetchAgencyServiceProviders() {

        List<Client2> clientList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin  ?:=TQC_AGENCIES_CURSORS.getAgentServiceProviders(?); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            clientList = new ArrayList<Client2>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);


            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setObject(2, session.getAttribute("agencyCode"));
            statement.execute();
            OracleResultSet rs = (OracleResultSet)statement.getObject(1);

            while (rs.next()) {
                Client2 client = new Client2();
                client.setSPR_CODE(rs.getBigDecimal(1));
                client.setSPR_NAME(rs.getString(2));
                client.setAGNT_CODE(rs.getBigDecimal(3));
                client.setSPR_SHT_DESC(rs.getString(4));
                client.setSelected(false);
                clientList.add(client);
            }

            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return clientList;
    }

    public List<Client2> fetchAgencyNoneClients() {

        List<Client2> clientList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ?:= TQC_AGENCIES_CURSORS.fetchAgencyNoneClients(?); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            clientList = new ArrayList<Client2>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);


            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setBigDecimal(2,
                                    new BigDecimal((String)session.getAttribute("agencyCode")));
            statement.execute();
            OracleResultSet rs = (OracleResultSet)statement.getObject(1);

            while (rs.next()) {
                Client2 client = new Client2();
                client.setClientCode(rs.getString(1));
                client.setShortDesc(rs.getString(2));
                client.setName(rs.getString(3));
                client.setSelected(false);
                clientList.add(client);
            }

            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return clientList;
    }

    public List<Agency> fetchAgencyProducts() {

        List<Agency> Products = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ?:= TQC_AGENCIES_CURSORS.fetchAgencyProducts(?); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            Products = new ArrayList<Agency>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);


            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setBigDecimal(2,
                                    new BigDecimal((String)session.getAttribute("agencyCode")));
            statement.execute();
            OracleResultSet rs = (OracleResultSet)statement.getObject(1);

            while (rs.next()) {
                Agency prod = new Agency();
                prod.setProdCode(rs.getBigDecimal(1));
                prod.setProdDesc(rs.getString(2));
                Products.add(prod);
            }

            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return Products;
    }

    public List<Agency> fetchNoneAgencyProducts() {

        List<Agency> Products = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ?:= TQC_AGENCIES_CURSORS.fetchNoneAgencyProducts(?); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            Products = new ArrayList<Agency>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);


            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setBigDecimal(2,
                                    new BigDecimal((String)session.getAttribute("agencyCode")));
            statement.execute();
            OracleResultSet rs = (OracleResultSet)statement.getObject(1);

            while (rs.next()) {
                Agency prod = new Agency();
                prod.setProdCode(rs.getBigDecimal(1));
                prod.setProdDesc(rs.getString(2));
                Products.add(prod);
            }

            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return Products;
    }

    /**
     * Fetch all Unassigned systems for the given account
     *
     * @return
     */
    public List<User> fetchUnassignedAccountSystems() {

        String query =
            "begin TQC_AGENCIES_CURSORS.get_account_unassigned_systems(?,?); end;";
        OracleCallableStatement cst = null;
        List<User> unassignedSystems = new ArrayList<User>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;

        if (session.getAttribute("agencyCode") != null) {
            try {
                BigDecimal accountid =  GlobalCC.checkBDNullValues(session.getAttribute("agencyCode"));
                conn = (OracleConnection)datahandler.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.setBigDecimal(1,accountid);
                cst.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
                cst.execute();
                OracleResultSet rs = (OracleResultSet)cst.getObject(2);

                while (rs.next()) {
                    User user = new User();
                    user.setSysCode(rs.getBigDecimal(1));
                    user.setSysShtDesc(rs.getString(2));
                    user.setSysName(rs.getString(3));
                    user.setSysActive(rs.getString(4));
                    unassignedSystems.add(user);
                }

                cst.close();
                rs.close();
                conn.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                e.printStackTrace();
            }
        }

        return unassignedSystems;
    }

    public List<User> fetchAssignedAccountSystems() {

        String query =
            "begin TQC_AGENCIES_CURSORS.get_account_assigned_systems(?,?); end;";
        OracleCallableStatement cst = null;
        List<User> assignedSystems = new ArrayList<User>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;

        if (session.getAttribute("agencyCode") != null) {
            try {
                conn = (OracleConnection)datahandler.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.setBigDecimal(1,
                                  new BigDecimal((String)session.getAttribute("agencyCode")));
                cst.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
                cst.execute();
                OracleResultSet rs = (OracleResultSet)cst.getObject(2);

                while (rs.next()) {
                    User user = new User();
                    user.setSysCode(rs.getBigDecimal(1));
                    user.setSysShtDesc(rs.getString(2));
                    user.setSysName(rs.getString(3));
                    user.setSysActive(rs.getString(4));
                    assignedSystems.add(user);
                }
                cst.close();
                rs.close();
                conn.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                e.printStackTrace();
            }
        }

        return assignedSystems;
    }

    public List<AgencySystem> fetchAllAgencyAssignedSystems() {
        String query =
            "begin ? := TQC_AGENCIES_CURSORS.getAllAgencyAssignedSystems(?); end;";
        OracleCallableStatement cst = null;
        List<AgencySystem> agencySystemsList = new ArrayList<AgencySystem>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;

        if (session.getAttribute("agencyCode") != null) {
            try {
                conn = (OracleConnection)datahandler.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
                cst.setBigDecimal(2,
                                  new BigDecimal(session.getAttribute("agencyCode").toString()));
                cst.execute();
                OracleResultSet rs = (OracleResultSet)cst.getObject(1);

                while (rs.next()) {
                    AgencySystem agencySystem = new AgencySystem();
                    agencySystem.setSysCode(rs.getBigDecimal(1));
                    agencySystem.setSysShtDesc(rs.getString(2));
                    agencySystem.setSysName(rs.getString(3));
                    agencySystem.setSysActive(rs.getString(4));
                    agencySystem.setAsysSysCode(rs.getBigDecimal(5));
                    agencySystem.setAsysAgnCode(rs.getBigDecimal(6));
                    agencySystem.setAsysWef(rs.getDate(7));
                    agencySystem.setAsysWet(rs.getDate(8));
                    agencySystem.setAsysComment(rs.getString(9));
                    agencySystem.setAsysOsdCode(rs.getString(10));
                    agencySystem.setSubdivisionName(rs.getString(11));
                    agencySystem.setAsysOsdId(rs.getBigDecimal(12));
                    agencySystem.setAsysShtDesc(rs.getString(13));
                    agencySystemsList.add(agencySystem);
                }
                cst.close();
                rs.close();
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return agencySystemsList;
    }

    public List<AccountsType> fetchAllGlAccounts() {

        String query = "begin ? := TQC_SETUPS_CURSOR.getAllGlAccounts(); end;";
        OracleCallableStatement cst = null;
        List<AccountsType> glAccounts = new ArrayList<AccountsType>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;


        try {
            conn = (OracleConnection)datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            while (rs.next()) {
                AccountsType glAccount = new AccountsType();
                glAccount.setAccNumber(rs.getString(1));
                glAccount.setAccName(rs.getString(2));
                glAccounts.add(glAccount);
            }

            cst.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
        }

        return glAccounts;
    }

    public List<AccountsType> fetchAllAgentDetails() {

        String query =
            "begin ? := TQC_SETUPS_CURSOR.select_Parent_Agency(); end;";
        OracleCallableStatement cst = null;
        List<AccountsType> glAccounts = new ArrayList<AccountsType>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;


        try {
            conn = (OracleConnection)datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            while (rs.next()) {
                AccountsType accountType = new AccountsType();
                accountType.setAgentCode(rs.getBigDecimal(1));
                accountType.setAgentname(rs.getString(2));
                accountType.setAgentShtDesc(rs.getString(3));
                glAccounts.add(accountType);
            }

            cst.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
        }

        return glAccounts;
    }

    public List<AccountsType> fetchAllSystems() {

        String query = "begin ? := TQC_SETUPS_CURSOR.getagencysystems(); end;";
        OracleCallableStatement cst = null;
        List<AccountsType> glAccounts = new ArrayList<AccountsType>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;


        try {
            conn = (OracleConnection)datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            while (rs.next()) {
                AccountsType accountType = new AccountsType();
                accountType.setSysCode(rs.getBigDecimal(1));
                accountType.setSysName(rs.getString(2));
                accountType.setSysShtDesc(rs.getString(3));
                glAccounts.add(accountType);
            }

            cst.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
        }

        return glAccounts;
    }

    public void setAccountTypeCode(String accountTypeCode) {
        session.setAttribute("accountTypeCode", accountTypeCode);
        this.accountTypeCode = accountTypeCode;
    }

    public String getAccountTypeCode() {
        accountTypeCode = (String)session.getAttribute("accountTypeCode");
        return accountTypeCode;
    }

    public List<AccountsType> fetchExistingServiceProv() {
        int count = 0;
        String query =
            "begin TQC_SETUPS_CURSOR.getServiceProvider(?,?,?); end;";
        OracleCallableStatement cst = null;
        List<AccountsType> glAccounts = new ArrayList<AccountsType>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {
            conn = (OracleConnection)datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
            cst.setObject(1, session.getAttribute("sprName"));
            cst.setObject(3, session.getAttribute("serviceProviderTypeCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(2);

            while (rs.next()) {

                AccountsType accountType = new AccountsType();
                accountType.setSprCode(rs.getBigDecimal(1));
                accountType.setSprShtDesc(rs.getString(2));
                accountType.setSprName(rs.getString(3));
                accountType.setSprPhysicalAddress(rs.getString(4));
                accountType.setSprPostalAddress(rs.getString(5));
                accountType.setSprPhone(rs.getString(6));
                accountType.setSprFax(rs.getString(7));
                accountType.setSprEmail(rs.getString(8));
                accountType.setSprCreatedBy(rs.getString(9));
                accountType.setSprDateCreated(rs.getDate(10));
                accountType.setSprStatusRemarks(rs.getString(11));
                accountType.setSprName(rs.getString(12));
                glAccounts.add(accountType);
                count = count + 1;
                session.setAttribute("countSprVal", count);
            }

            cst.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
        }

        return glAccounts;
    }
    public List<AccountsType> fetchClaimPaymentModes() {
        int count = 0;
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getclaimpaymodes(); end;";
        OracleCallableStatement cst = null;
        List<AccountsType> pymtModes = new ArrayList<AccountsType>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {
            conn = (OracleConnection)datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            while (rs.next()) {

                AccountsType pmtMode = new AccountsType();
                pmtMode.setCpmShtDesc(rs.getString(1));
                pmtMode.setCpmDesc(rs.getString(2));
                pymtModes.add(pmtMode);
                count = count + 1;
                session.setAttribute("payment mode val", count);
            }

            cst.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
        }

        return pymtModes;
    }
    public List<Agency> fetchReplacementMgrs() {

        List<Agency> agenciesList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "SELECT   agn_code, agn_sht_desc, agn_name\n" + 
            "             FROM tqc_agencies, tqc_account_types\n" + 
            "            WHERE \n" + 
            "               agn_act_code =act_code\n" + 
            "              and  act_type_id = :v_act_type \n" + 
            "              AND agn_status = 'ACTIVE'";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleResultSet rs=null;

        try {
            agenciesList = new ArrayList<Agency>();
            String type=(String)session.getAttribute("prom_replace_act_type_id"); 
            
            connection = dbConnector.getDatabaseConnection();
           
            query=query.replaceAll(":v_act_type", "'"+type+"'");
            System.out.println("sql: "+query);
            statement = (OracleCallableStatement)connection.prepareCall(query);  
            
             rs = (OracleResultSet)statement.executeQuery(); 

            while (rs.next()) {
                Agency agency = new Agency(); 
                agency.setAgnCode(rs.getBigDecimal(1));
                agency.setAgnShtDesc(rs.getString(2));
                agency.setAgnName(rs.getString(3));
                agenciesList.add(agency);
            }
         } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }finally{
            DbUtils.closeQuietly(connection,statement, rs);
        }
        return agenciesList;
    }
    //-----------start dao's fetchAgents --------------------//
        public List<Agent> fetchAgents(){
                    List<Agent> agents = null; 
            Session dbSess = HibernateUtil.getSession();
            Transaction tx = null;

             try {
                String query = "FROM Agent S";
                tx = dbSess.beginTransaction(); 
                agents = dbSess.createQuery(query).list(); 
                for (Iterator iterator = agents.iterator(); iterator.hasNext();){
                   Agent item = (Agent) iterator.next(); 
                   System.out.println(item);  
                }
                tx.commit();
             } catch (HibernateException e) {
                if (tx!=null) tx.rollback();
                GlobalCC.EXCEPTIONREPORTING(e);
             } finally {
                dbSess.close(); 
             }
            return agents;
        }
        public String updateAgent(Agent val){ 
               Session dbSess = HibernateUtil.getSession();
               Transaction tx = null; 
               try { 
                     tx = dbSess.beginTransaction(); 
                   
                     Agent item = new Agent();
                   
                     item = (Agent)dbSess.get(Agent.class, val.getCode()); 
                     
                    item.setCode(val.getCode());
                    item.setActCode(val.getActCode());
                    item.setShtDesc(val.getShtDesc());
                    item.setName(val.getName());
                    item.setPhysicalAddress(val.getPhysicalAddress());
                    item.setPostalAddress(val.getPostalAddress());
                    item.setTwnCode(val.getTwnCode());
                    item.setCouCode(val.getCouCode());
                    item.setEmailAddress(val.getEmailAddress());
                    item.setWebAddress(val.getWebAddress());
                    item.setZip(val.getZip());
                    item.setContactPerson(val.getContactPerson());
                    item.setContactTitle(val.getContactTitle());
                    item.setTel1(val.getTel1());
                    item.setTel2(val.getTel2());
                    item.setFax(val.getFax());
                    item.setAccNo(val.getAccNo());
                    item.setPin(val.getPin());
                    item.setAgentCommission(val.getAgentCommission());
                    item.setCreditAllowed(val.getCreditAllowed());
                    item.setAgentWhtTax(val.getAgentWhtTax());
                    item.setPrintDbnote(val.getPrintDbnote());
                    item.setStatus(val.getStatus());
                    item.setDateCreated(val.getDateCreated());
                    item.setCreatedBy(val.getCreatedBy());
                    item.setRegCode(val.getRegCode());
                    item.setCommReserveRate(val.getCommReserveRate());
                    item.setAnnualBudget(val.getAnnualBudget());
                    item.setStatusEffDate(val.getStatusEffDate());
                    item.setCreditPeriod(val.getCreditPeriod());
                    item.setCommStatEffDt(val.getCommStatEffDt());
                    item.setCommStatusDt(val.getCommStatusDt());
                    item.setCommAllowed(val.getCommAllowed());
                    item.setChecked(val.getChecked());
                    item.setCheckedBy(val.getCheckedBy());
                    item.setCheckDate(val.getCheckDate());
                    item.setCompCommArrears(val.getCompCommArrears());
                    item.setReinsurer(val.getReinsurer());
                    item.setBrnCode(val.getBrnCode());
                    item.setTown(val.getTown());
                    item.setCountry(val.getCountry());
                    item.setStatusDesc(val.getStatusDesc());
                    item.setIdNo(val.getIdNo());
                    item.setConCode(val.getConCode());
                    item.setAgnCode(val.getAgnCode());
                    item.setSmsTel(val.getSmsTel());
                    item.setAhcCode(val.getAhcCode());
                    item.setSecCode(val.getSecCode());
                    item.setAgncClassCode(val.getAgncClassCode());
                    item.setExpiryDate(val.getExpiryDate());
                    item.setLicenseNo(val.getLicenseNo());
                    item.setRunoff(val.getRunoff());
                    item.setLicensed(val.getLicensed());
                    item.setLicenseGracePr(val.getLicenseGracePr());
                    item.setOldAccNo(val.getOldAccNo());
                    item.setStatusRemarks(val.getStatusRemarks());
                    item.setOsdCode(val.getOsdCode());
                    item.setBbrAccCode(val.getBbrAccCode());
                    item.setBbrCode(val.getBbrCode());
                    item.setBankAccNo(val.getBankAccNo());
                    item.setUniquePrefix(val.getUniquePrefix());
                    item.setStateCode(val.getStateCode());
                    item.setCrdtRting(val.getCrdtRting());
                    item.setClntCode(val.getClntCode());
                    item.setBirthDate(val.getBirthDate());
                    item.setCreditLimit(val.getCreditLimit());
                    item.setBruCode(val.getBruCode());
                    item.setLocalInternational(val.getLocalInternational());
                    item.setRegulatorNumber(val.getRegulatorNumber());
                    item.setAuthorised(val.getAuthorised());
                    item.setAuthorisedBy(val.getAuthorisedBy());
                    item.setAuthorisedDate(val.getAuthorisedDate());
                    item.setRorgCode(val.getRorgCode());
                    item.setOrsCode(val.getOrsCode());
                    item.setAllocateCert(val.getAllocateCert());
                    item.setBouncedChq(val.getBouncedChq());
                    item.setBpnCode(val.getBpnCode());
                    item.setAgentType(val.getAgentType());
                    item.setGroup(val.getGroup());
                    item.setSubagent(val.getSubagent());
                    item.setMainAgnCode(val.getMainAgnCode());
                    item.setPayee(val.getPayee());
                    item.setEnableWebEdit(val.getEnableWebEdit());
                    item.setAccountManager(val.getAccountManager());
                    item.setDefaultCommMode(val.getDefaultCommMode());
                    item.setVatApplicable(val.getVatApplicable());
                    item.setWhtaxApplicable(val.getWhtaxApplicable());
                    item.setTelPay(val.getTelPay());
                    item.setPaymentFreq(val.getPaymentFreq());
                    item.setDefaultCpmMode(val.getDefaultCpmMode());
                    item.setPymtValidated(val.getPymtValidated());
                    item.setQualification(val.getQualification());
                    item.setMaritalStatus(val.getMaritalStatus());
                    item.setBenefitStartDate(val.getBenefitStartDate());
                    item.setIdNoDocUsed(val.getIdNoDocUsed());
                    item.setAgntyCode(val.getAgntyCode());
                    item.setSbuCode(val.getSbuCode());
                    item.setCommLevyApp(val.getCommLevyApp());
                    item.setCommLevyRate(val.getCommLevyRate());
                    item.setBrrCode(val.getBrrCode());
                    item.setBrrName(val.getBrrName());
                    item.setAuthName(val.getAuthName());
                    item.setUpdatedBy(val.getUpdatedBy());
                    item.setUpdatedOn(val.getUpdatedOn());
                    item.setEntCode(val.getEntCode());
                    item.setGender(val.getGender());
                    item.setIraRegno(val.getIraRegno());
                    item.setPrincipalOfficer(val.getPrincipalOfficer());
                    item.setPassportNo(val.getPassportNo());
                    item.setValidationSource(val.getValidationSource());
                    item.setIprsValidated(val.getIprsValidated());
                    item.setPymtDetail(val.getPymtDetail());
                    item.setCrdtLimit(val.getCrdtLimit());
                    item.setInstCommAllwd(val.getInstCommAllwd());
                    item.setPaydetailValidated(val.getPaydetailValidated());
                    item.setGlRcvblAccNo(val.getGlRcvblAccNo());
                    item.setForgn(val.getForgn());
                    item.setIniquePrefix(val.getIniquePrefix());
                    item.setCpmModeDesc(val.getCpmModeDesc());
                    item.setRelatedParty(val.getRelatedParty());
                   
                     dbSess.update(item);  
                   
                     tx.commit();
                   
                     ADFUtils.findIterator("fetchAgentsIterator").executeQuery();  
                     String message = "Agent Saved Successfully!";
                     GlobalCC.INFORMATIONREPORTING(message); 
               } catch (HibernateException e) {
                  if (tx!=null) tx.rollback();
                  GlobalCC.EXCEPTIONREPORTING(e); 
               } finally {
                  dbSess.close(); 
               }
              return null;
          }
        public String addAgent(Agent val){ 
               Session dbSess = HibernateUtil.getSession();
               Transaction tx = null; 
               try { 
                     tx = dbSess.beginTransaction(); 
                   
                     Agent item = new Agent(); 
                     
                    item.setCode(val.getCode());
                    item.setActCode(val.getActCode());
                    item.setShtDesc(val.getShtDesc());
                    item.setName(val.getName());
                    item.setPhysicalAddress(val.getPhysicalAddress());
                    item.setPostalAddress(val.getPostalAddress());
                    item.setTwnCode(val.getTwnCode());
                    item.setCouCode(val.getCouCode());
                    item.setEmailAddress(val.getEmailAddress());
                    item.setWebAddress(val.getWebAddress());
                    item.setZip(val.getZip());
                    item.setContactPerson(val.getContactPerson());
                    item.setContactTitle(val.getContactTitle());
                    item.setTel1(val.getTel1());
                    item.setTel2(val.getTel2());
                    item.setFax(val.getFax());
                    item.setAccNo(val.getAccNo());
                    item.setPin(val.getPin());
                    item.setAgentCommission(val.getAgentCommission());
                    item.setCreditAllowed(val.getCreditAllowed());
                    item.setAgentWhtTax(val.getAgentWhtTax());
                    item.setPrintDbnote(val.getPrintDbnote());
                    item.setStatus(val.getStatus());
                    item.setDateCreated(val.getDateCreated());
                    item.setCreatedBy(val.getCreatedBy());
                    item.setRegCode(val.getRegCode());
                    item.setCommReserveRate(val.getCommReserveRate());
                    item.setAnnualBudget(val.getAnnualBudget());
                    item.setStatusEffDate(val.getStatusEffDate());
                    item.setCreditPeriod(val.getCreditPeriod());
                    item.setCommStatEffDt(val.getCommStatEffDt());
                    item.setCommStatusDt(val.getCommStatusDt());
                    item.setCommAllowed(val.getCommAllowed());
                    item.setChecked(val.getChecked());
                    item.setCheckedBy(val.getCheckedBy());
                    item.setCheckDate(val.getCheckDate());
                    item.setCompCommArrears(val.getCompCommArrears());
                    item.setReinsurer(val.getReinsurer());
                    item.setBrnCode(val.getBrnCode());
                    item.setTown(val.getTown());
                    item.setCountry(val.getCountry());
                    item.setStatusDesc(val.getStatusDesc());
                    item.setIdNo(val.getIdNo());
                    item.setConCode(val.getConCode());
                    item.setAgnCode(val.getAgnCode());
                    item.setSmsTel(val.getSmsTel());
                    item.setAhcCode(val.getAhcCode());
                    item.setSecCode(val.getSecCode());
                    item.setAgncClassCode(val.getAgncClassCode());
                    item.setExpiryDate(val.getExpiryDate());
                    item.setLicenseNo(val.getLicenseNo());
                    item.setRunoff(val.getRunoff());
                    item.setLicensed(val.getLicensed());
                    item.setLicenseGracePr(val.getLicenseGracePr());
                    item.setOldAccNo(val.getOldAccNo());
                    item.setStatusRemarks(val.getStatusRemarks());
                    item.setOsdCode(val.getOsdCode());
                    item.setBbrAccCode(val.getBbrAccCode());
                    item.setBbrCode(val.getBbrCode());
                    item.setBankAccNo(val.getBankAccNo());
                    item.setUniquePrefix(val.getUniquePrefix());
                    item.setStateCode(val.getStateCode());
                    item.setCrdtRting(val.getCrdtRting());
                    item.setClntCode(val.getClntCode());
                    item.setBirthDate(val.getBirthDate());
                    item.setCreditLimit(val.getCreditLimit());
                    item.setBruCode(val.getBruCode());
                    item.setLocalInternational(val.getLocalInternational());
                    item.setRegulatorNumber(val.getRegulatorNumber());
                    item.setAuthorised(val.getAuthorised());
                    item.setAuthorisedBy(val.getAuthorisedBy());
                    item.setAuthorisedDate(val.getAuthorisedDate());
                    item.setRorgCode(val.getRorgCode());
                    item.setOrsCode(val.getOrsCode());
                    item.setAllocateCert(val.getAllocateCert());
                    item.setBouncedChq(val.getBouncedChq());
                    item.setBpnCode(val.getBpnCode());
                    item.setAgentType(val.getAgentType());
                    item.setGroup(val.getGroup());
                    item.setSubagent(val.getSubagent());
                    item.setMainAgnCode(val.getMainAgnCode());
                    item.setPayee(val.getPayee());
                    item.setEnableWebEdit(val.getEnableWebEdit());
                    item.setAccountManager(val.getAccountManager());
                    item.setDefaultCommMode(val.getDefaultCommMode());
                    item.setVatApplicable(val.getVatApplicable());
                    item.setWhtaxApplicable(val.getWhtaxApplicable());
                    item.setTelPay(val.getTelPay());
                    item.setPaymentFreq(val.getPaymentFreq());
                    item.setDefaultCpmMode(val.getDefaultCpmMode());
                    item.setPymtValidated(val.getPymtValidated());
                    item.setQualification(val.getQualification());
                    item.setMaritalStatus(val.getMaritalStatus());
                    item.setBenefitStartDate(val.getBenefitStartDate());
                    item.setIdNoDocUsed(val.getIdNoDocUsed());
                    item.setAgntyCode(val.getAgntyCode());
                    item.setSbuCode(val.getSbuCode());
                    item.setCommLevyApp(val.getCommLevyApp());
                    item.setCommLevyRate(val.getCommLevyRate());
                    item.setBrrCode(val.getBrrCode());
                    item.setBrrName(val.getBrrName());
                    item.setAuthName(val.getAuthName());
                    item.setUpdatedBy(val.getUpdatedBy());
                    item.setUpdatedOn(val.getUpdatedOn());
                    item.setEntCode(val.getEntCode());
                    item.setGender(val.getGender());
                    item.setIraRegno(val.getIraRegno());
                    item.setPrincipalOfficer(val.getPrincipalOfficer());
                    item.setPassportNo(val.getPassportNo());
                    item.setValidationSource(val.getValidationSource());
                    item.setIprsValidated(val.getIprsValidated());
                    item.setPymtDetail(val.getPymtDetail());
                    item.setCrdtLimit(val.getCrdtLimit());
                    item.setInstCommAllwd(val.getInstCommAllwd());
                    item.setPaydetailValidated(val.getPaydetailValidated());
                    item.setGlRcvblAccNo(val.getGlRcvblAccNo());
                    item.setForgn(val.getForgn());
                    item.setIniquePrefix(val.getIniquePrefix());
                    item.setCpmModeDesc(val.getCpmModeDesc());
                    item.setRelatedParty(val.getRelatedParty());
                   
                     dbSess.persist(item);  
                   
                     tx.commit();
                   
                     ADFUtils.findIterator("fetchAgentsIterator").executeQuery();  
                     String message = "Agent Saved Successfully!";
                     GlobalCC.INFORMATIONREPORTING(message); 
               } catch (HibernateException e) {
                  if (tx!=null) tx.rollback();
                  GlobalCC.EXCEPTIONREPORTING(e); 
               } finally {
                  dbSess.close(); 
               }
              return null;
          } 
         //-----------start dao's fetchAgent --------------------//
         public TurnQuest.view.models.Agent fetchAgentByAgentCode(BigDecimal agentCode){
             TurnQuest.view.models.Agent agent = null; 
             Session dbSess = HibernateUtil.getSession();
             Transaction tx = null; 
              try { 
                 tx = dbSess.beginTransaction(); 
                 agent = (TurnQuest.view.models.Agent)dbSess.get(TurnQuest.view.models.Agent.class, agentCode);
                 tx.commit();
              } catch (HibernateException e) { 
                 GlobalCC.EXCEPTIONREPORTING(e);
              } finally {
                 dbSess.close(); 
              }
             return agent;
         }
                    //-----------end dao's fetchAgent --------------------//
}
