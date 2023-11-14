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

package TurnQuest.view.client;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.HibernateUtil;
import TurnQuest.view.Branches.Branch;
import TurnQuest.view.Clients1.AppClients;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.Divisions.Division;
import TurnQuest.view.Documents.RequiredDocument;
import TurnQuest.view.Settings1.AppSettings;
import TurnQuest.view.Settings2.SettingsValues;
import TurnQuest.view.Usr.User;

import TurnQuest.view.models.ClientDirector;
import TurnQuest.view.models.ClientSignatory;

import TurnQuest.view.models.ClientType;
import TurnQuest.view.models.IncomeSources;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;
import org.hibernate.HibernateException; 
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.apache.commons.dbutils.DbUtils;


/**
 * This class serves as the main DAO class for retriving the necessary objects
 * from the database relating to Clients.
 *
 * @author Frankline Ogongi
 */
public class MainClientDAO {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    /**
     * Default Class Constructor
     */
    public MainClientDAO() {
        super();
    }

    /**
     * Fetches all <code>Client</code> objects/records from the database and
     * returns them in a list.
     *
     * @return A list of <code>Client</code> objects/records fetched from the
     * database.
     */

    public List<Client2> fetchAllClients() {

        List<Client2> clientsList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_CLIENTS_PKG.get_client_detailsNotInGroup(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        clientsList = new ArrayList<Client2>();
        if (session.getAttribute("grpCode") != null) {
            try {

                connection =
                        (OracleConnection)dbConnector.getDatabaseConnection();
                statement =
                        (OracleCallableStatement)connection.prepareCall(query);

                /*if (session.getAttribute("clientCode") != null) {
                statement.setString(2,
                                    (String)session.getAttribute("clientCode"));
            } else {
                statement.setString(2, "");
            }*/

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setString(2, null);
                statement.setBigDecimal(3,
                                        new BigDecimal(session.getAttribute("grpCode").toString()));

                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    // For every row, create a Client object with the
                    // values and add it to the list
                    Client2 client = new Client2();

                    client.setCode(resultSet.getString(1));
                    client.setShortDesc(resultSet.getString(2));
                    client.setName(resultSet.getString(3));
                    client.setOtherNames(resultSet.getString(4));
                    client.setIdRegNo(resultSet.getString(5));
                    client.setDob(resultSet.getDate(6));
                    client.setPin(resultSet.getString(7));
                    client.setPhysicalAddress(resultSet.getString(8));
                    client.setPostalAddress(resultSet.getString(9));
                    client.setTownCode(resultSet.getString(10));
                    client.setTownName(resultSet.getString(11));
                    client.setCountryCode(resultSet.getString(12));
                    client.setCountryName(resultSet.getString(13));
                    client.setEmail(resultSet.getString(14));
                    client.setPhone1(resultSet.getString(15));
                    client.setPhone2(resultSet.getString(16));
                    client.setStatus(resultSet.getString(17));
                    client.setFax(resultSet.getString(18));
                    client.setRemarks(resultSet.getString(19));
                    client.setSpecialTerms(resultSet.getString(20));
                    client.setDeclinedProp(resultSet.getString(21));
                    client.setIncreasedPremium(resultSet.getString(22));
                    client.setPolicyCancelled(resultSet.getString(23));
                    client.setProposer(resultSet.getString(24));
                    client.setAccountNum(resultSet.getString(25));
                    client.setDomicileCountries(resultSet.getString(26));
                    client.setWef(resultSet.getDate(27));
                    client.setWet(resultSet.getDate(28));
                    client.setWithdrawalReason(resultSet.getString(29));
                    client.setSectorCode(resultSet.getBigDecimal(30));

                    client.setSurname(resultSet.getString(31));
                    client.setType(resultSet.getString(32));

                    client.setTitle(resultSet.getString(33));
                    client.setBusiness(resultSet.getString(34));
                    client.setZipCode(resultSet.getString(35));
                    client.setBankBranchCode(resultSet.getString(36));
                    client.setBankAccountNum(resultSet.getString(37));
                    client.setClientCode(resultSet.getString(38));
                    client.setNonDirect(resultSet.getString(39));
                    client.setCreatedBy(resultSet.getString(40));
                    client.setSms(resultSet.getString(41));
                    client.setAgentStatus(resultSet.getString(42));
                    client.setDateCreated(resultSet.getDate(43));
                    client.setRunOff(resultSet.getString(44));
                    client.setLoadedBy(resultSet.getString(45));
                    client.setDirectClient(resultSet.getString(46));
                    client.setOldAccountNum(resultSet.getString(47));
                    client.setUserCode(resultSet.getString(48));
                    client.setUsername(resultSet.getString(49));
                    client.setContactPhone1(resultSet.getString(50));
                    client.setContactEmail1(resultSet.getString(51));
                    client.setContactPhone2(resultSet.getString(52));
                    client.setContactEmail2(resultSet.getString(53));
                    client.setStsCode(resultSet.getBigDecimal(54));
                    client.setStsName(resultSet.getString(55));
                    client.setPassportNumber(resultSet.getString(56));
                    client.setGender(resultSet.getString(57));
                    client.setContactName1(resultSet.getString(58));
                    client.setContactName2(resultSet.getString(59));
                    client.setSectorName(resultSet.getString(60));

                    client.setWebsite(resultSet.getString(61));
                    client.setAuditors(resultSet.getString(62));
                    client.setParent_company(resultSet.getString(63));
                    client.setCurrent_insurer(resultSet.getString(64));
                    client.setProjected_business(resultSet.getBigDecimal(65));
                    client.setDate_of_empl(resultSet.getDate(66));
                    client.setDriving_licence(resultSet.getString(67));
                    client.setParent_company_name(resultSet.getString(68));

                    client.setBrnCode(resultSet.getBigDecimal(69));
                    client.setBrnName(resultSet.getString(70));

                    clientsList.add(client);
                }

                resultSet.close();
                statement.close();
                connection.close();
                //}

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
        }
        return clientsList;
    }

    public List<Client2> fetchClientsByCriteria() {


        List<Client2> clientsList = null;
        DBConnector dbConnector = new DBConnector();

        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        clientsList = new ArrayList<Client2>();
        String myQuery = null;
        String myQuery2 = null;

        String query = GlobalCC.getSysParamValue("CLIENT_SEARCH_QUERY");
        if (session.getAttribute("searchCriteria") != null) {
            if (session.getAttribute("grpCode") != null) {
                myQuery =
                        session.getAttribute("searchCriteria").toString() + "AND  CLNT_CODE NOT IN  (SELECT GRPD_CLNT_CODE FROM TQC_GROUP_CLNT_DTLS  WHERE GRPD_GRP_CODE=" +
                        session.getAttribute("grpCode").toString() + ")";
                if (session.getAttribute("searchCriteria2") != null) {
                    // myQuery2= session.getAttribute("searchCriteria2").toString()+"AND  CLNT_CODE NOT IN  (SELECT GRPD_CLNT_CODE FROM TQC_GROUP_CLNT_DTLS  WHERE GRPD_GRP_CODE="+session.getAttribute("grpCode").toString()+")" ;
                }
            } else {
                myQuery = session.getAttribute("searchCriteria").toString();
                if (session.getAttribute("searchCriteria2") != null) {
                    //  myQuery2=  session.getAttribute("searchCriteria2").toString();
                }

            }
            OracleResultSet resultSet = null;

            try {
                //System.out.println("query: "+query);
                query = query.replaceAll(":v_col", myQuery);
                connection =
                        (OracleConnection)dbConnector.getDatabaseConnection();
                statement =
                        (OracleCallableStatement)connection.prepareCall(query);
                System.out.println("query: " + query);


                resultSet = (OracleResultSet)statement.executeQuery();
                int k = 0;
                while (resultSet.next()) {
                    // For every row, create a Client object with the
                    // values and add it to the list
                    Client2 client = new Client2();

                    client.setCode(resultSet.getString(1));
                    client.setShortDesc(resultSet.getString(2));
                    client.setName(resultSet.getString(3));
                    client.setOtherNames(resultSet.getString(4));
                    client.setIdRegNo(resultSet.getString(5));
                    client.setDob(resultSet.getDate(6));
                    client.setPin(resultSet.getString(7));
                    client.setPhysicalAddress(resultSet.getString(8));
                    client.setPostalAddress(resultSet.getString(9));
                    client.setTownCode(resultSet.getString(10));
                    client.setTownName(resultSet.getString(11));
                    client.setCountryCode(resultSet.getString(12));
                    client.setCountryName(resultSet.getString(13));
                    client.setEmail(resultSet.getString(14));
                    client.setPhone1(resultSet.getString(15));
                    client.setPhone2(resultSet.getString(16));
                    client.setStatus(resultSet.getString(17));
                    client.setFax(resultSet.getString(18));
                    client.setRemarks(resultSet.getString(19));
                    client.setSpecialTerms(resultSet.getString(20));
                    client.setDeclinedProp(resultSet.getString(21));
                    client.setIncreasedPremium(resultSet.getString(22));
                    client.setPolicyCancelled(resultSet.getString(23));
                    client.setProposer(resultSet.getString(24));
                    client.setAccountNum(resultSet.getString(25));
                    client.setDomicileCountries(resultSet.getString(26));
                    client.setWef(resultSet.getDate(27));
                    client.setWet(resultSet.getDate(28));
                    client.setWithdrawalReason(resultSet.getString(29));
                    client.setSectorCode(resultSet.getBigDecimal(30));

                    client.setSurname(resultSet.getString(31));
                    client.setType(resultSet.getString(32));

                    client.setTitle(resultSet.getString(33));
                    client.setBusiness(resultSet.getString(34));
                    client.setZipCode(resultSet.getString(35));
                    client.setBankBranchCode(resultSet.getString(36));
                    client.setBankAccountNum(resultSet.getString(37));
                    client.setClientCode(resultSet.getString(38));
                    client.setNonDirect(resultSet.getString(39));
                    client.setCreatedBy(resultSet.getString(40));
                    client.setSms(resultSet.getString("clnt_sms_tel"));
                    client.setAgentStatus(resultSet.getString(42));
                    client.setDateCreated(resultSet.getDate(43));
                    client.setRunOff(resultSet.getString(44));
                    client.setLoadedBy(resultSet.getString(45));
                    client.setDirectClient(resultSet.getString(46));
                    client.setOldAccountNum(resultSet.getString(47));
                    client.setUserCode(resultSet.getString(48));
                    client.setUsername(resultSet.getString(49));
                    client.setContactPhone1(resultSet.getString(50));
                    client.setContactEmail1(resultSet.getString(51));
                    client.setContactPhone2(resultSet.getString(52));
                    client.setContactEmail2(resultSet.getString(53));
                    client.setStsCode(resultSet.getBigDecimal(54));
                    client.setStsName(resultSet.getString(55));
                    client.setPassportNumber(resultSet.getString(56));
                    client.setGender(resultSet.getString(57));
                    client.setContactName1(resultSet.getString(58));
                    client.setContactName2(resultSet.getString(59));
                    client.setSectorName(resultSet.getString(60));
                    client.setWebsite(resultSet.getString(61));
                    client.setAuditors(resultSet.getString(62));
                    client.setParent_company(resultSet.getString(63));
                    client.setCurrent_insurer(resultSet.getString(64));
                    client.setProjected_business(resultSet.getBigDecimal(65));
                    client.setDate_of_empl(resultSet.getDate(66));
                    client.setDriving_licence(resultSet.getString(67));
                    client.setParent_company_name(resultSet.getString(68));
                    client.setBrnCode(resultSet.getBigDecimal(69));
                    client.setBankBranch(resultSet.getString(70));

                    client.setBouncedCheque(resultSet.getString(73));
                    client.setMaritalStatus(resultSet.getString(74));

                    //client.setClntBpnCode(resultSet.getBigDecimal(75));
                    //client.setBpnName(resultSet.getString(76));
                    //client.setPayrollNo(resultSet.getBigDecimal(77));
                    //client.setSalMax(resultSet.getBigDecimal(78));
                    //client.setSalMin(resultSet.getBigDecimal(79));

                    client.setBrnCode(resultSet.getBigDecimal(69));
                    client.setBrnName(resultSet.getString(70));
                    client.setAacOfficer(resultSet.getBigDecimal(71));
                    client.setAacOfficerName(resultSet.getString(72));
                    client.setOldShtDesc(resultSet.getString(73));
                    client.setCLTN_CLIENT_TYPES(resultSet.getString(79));
                    client.setClientCell(resultSet.getString(74));
                    client.setBankBranch(resultSet.getString(75));
                    client.setOccupation(resultSet.getString(76));
                    client.setAnniversary(resultSet.getDate(77));
                    client.setCreditRating(resultSet.getString(78));
                    client.setHoldingCompany(resultSet.getString(80));
                    client.setSacco(resultSet.getString(81));
                    client.setCouZipCode(resultSet.getString(82));
                    client.setCreditLimitAllowed(resultSet.getString(83));
                    client.setCreditLimit(resultSet.getBigDecimal(84));
                    client.setLocationName(resultSet.getString(85));
                    client.setBouncedCheque(resultSet.getString(86));
                    client.setMaritalStatus(resultSet.getString(87));
                    client.setDefaultModeOfComm(resultSet.getString(88));
                    //client.setPayrollNo(resultSet.getString(89));
                    client.setSalMin(resultSet.getBigDecimal(90));
                    client.setSalMax(resultSet.getBigDecimal(91));
                    client.setClntBpnCode(resultSet.getBigDecimal(92));
                    client.setBpnName(resultSet.getString(93));
                    client.setTelPay(resultSet.getString("clnt_tel_pay"));
                    client.setClntIntTel(resultSet.getString(95));
                    client.setClntEmail2(resultSet.getString(96));
                    client.setWorkPermit(resultSet.getString(97));
                    if (resultSet.getObject(98) == null) {
                        client.setDlIssueDate(null);
                    } else {
                        client.setDlIssueDate(resultSet.getDate(98));
                    }
                    client.setGsmZipCode(resultSet.getBigDecimal(101));
                    client.setIntZipCode(resultSet.getBigDecimal(102));
                    client.setStaffNo(resultSet.getString(103));
                    client.setClientLevel(resultSet.getString(104));
                    clientsList.add(client);
                    k++;
                }
                System.out.println("Records");
                System.out.println(k);
                resultSet.close();
                statement.close();
                connection.close();
                //}

            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(e);

            }

            finally {
                DbUtils.closeQuietly(connection, statement, resultSet);
            }

        }
        return clientsList;
    }

    public List<Client2> fetchAllClientsByTrigger() {

        List<Client2> clientsList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_CLIENTS_PKG.get_client_details(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        clientsList = new ArrayList<Client2>();
        // myTrigger makes sure that the system doesn't load all clients by default it requires the user to click search button on client screen
        if (session.getAttribute("myTrigger") != null) {
            try {

                connection =
                        (OracleConnection)dbConnector.getDatabaseConnection();
                statement =
                        (OracleCallableStatement)connection.prepareCall(query);

                /*if (session.getAttribute("clientCode") != null) {
              statement.setString(2,
                                  (String)session.getAttribute("clientCode"));
          } else {
              statement.setString(2, "");
          }*/

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setString(2, null);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    // For every row, create a Client object with the
                    // values and add it to the list

                    Client2 client = new Client2();

                    client.setCode(resultSet.getString(1));
                    client.setShortDesc(resultSet.getString(2));
                    client.setName(resultSet.getString(3));
                    client.setOtherNames(resultSet.getString(4));
                    client.setIdRegNo(resultSet.getString(5));
                    client.setDob(resultSet.getDate(6));
                    client.setPin(resultSet.getString(7));
                    client.setPhysicalAddress(resultSet.getString(8));
                    client.setPostalAddress(resultSet.getString(9));
                    client.setTownCode(resultSet.getString(10));
                    client.setTownName(resultSet.getString(11));
                    client.setCountryCode(resultSet.getString(12));
                    client.setCountryName(resultSet.getString(13));
                    client.setEmail(resultSet.getString(14));
                    client.setPhone1(resultSet.getString(15));
                    client.setPhone2(resultSet.getString(16));
                    client.setStatus(resultSet.getString(17));
                    client.setFax(resultSet.getString(18));
                    client.setRemarks(resultSet.getString(19));
                    client.setSpecialTerms(resultSet.getString(20));
                    client.setDeclinedProp(resultSet.getString(21));
                    client.setIncreasedPremium(resultSet.getString(22));
                    client.setPolicyCancelled(resultSet.getString(23));
                    client.setProposer(resultSet.getString(24));
                    client.setAccountNum(resultSet.getString(25));
                    client.setDomicileCountries(resultSet.getString(26));
                    client.setWef(resultSet.getDate(27));
                    client.setWet(resultSet.getDate(28));
                    client.setWithdrawalReason(resultSet.getString(29));
                    client.setSectorCode(resultSet.getBigDecimal(30));

                    client.setSurname(resultSet.getString(31));
                    client.setType(resultSet.getString(32));

                    client.setTitle(resultSet.getString(33));
                    client.setBusiness(resultSet.getString(34));
                    client.setZipCode(resultSet.getString(35));
                    client.setBankBranchCode(resultSet.getString(36));
                    client.setBankAccountNum(resultSet.getString(37));
                    client.setClientCode(resultSet.getString(38));
                    client.setNonDirect(resultSet.getString(39));
                    client.setCreatedBy(resultSet.getString(40));
                    client.setSms(resultSet.getString(41));
                    client.setAgentStatus(resultSet.getString(42));
                    client.setDateCreated(resultSet.getDate(43));
                    client.setRunOff(resultSet.getString(44));
                    client.setLoadedBy(resultSet.getString(45));
                    client.setDirectClient(resultSet.getString(46));
                    client.setOldAccountNum(resultSet.getString(47));
                    client.setUserCode(resultSet.getString(48));
                    client.setUsername(resultSet.getString(49));
                    client.setContactPhone1(resultSet.getString(50));
                    client.setContactEmail1(resultSet.getString(51));
                    client.setContactPhone2(resultSet.getString(52));
                    client.setContactEmail2(resultSet.getString(53));
                    client.setStsCode(resultSet.getBigDecimal(54));
                    client.setStsName(resultSet.getString(55));
                    client.setPassportNumber(resultSet.getString(56));
                    client.setGender(resultSet.getString(57));
                    client.setContactName1(resultSet.getString(58));
                    client.setContactName2(resultSet.getString(59));
                    client.setSectorName(resultSet.getString(60));

                    client.setWebsite(resultSet.getString(61));
                    client.setAuditors(resultSet.getString(62));
                    client.setParent_company(resultSet.getString(63));
                    client.setCurrent_insurer(resultSet.getString(64));
                    client.setProjected_business(resultSet.getBigDecimal(65));
                    client.setDate_of_empl(resultSet.getDate(66));
                    client.setDriving_licence(resultSet.getString(67));
                    client.setParent_company_name(resultSet.getString(68));

                    client.setBrnCode(resultSet.getBigDecimal(69));
                    client.setBrnName(resultSet.getString(70));
                    client.setCreditLimitAllowed(resultSet.getString(71));
                    client.setCreditLimit(resultSet.getBigDecimal(72));


                    clientsList.add(client);
                }

                resultSet.close();
                statement.close();
                connection.close();
                //}

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
        }
        return clientsList;
    }

    public List<Client2> fetchAllClientsByAccountOfficer() {

        List<Client2> clientsList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_CLIENTS_PKG.get_clnt_by_AccOfficer(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        clientsList = new ArrayList<Client2>();
        // myTrigger makes sure that the system doesn't load all clients by default it requires the user to click search button on client screen
        if (session.getAttribute("sysUserCode") != null) {
            try {

                connection =
                        (OracleConnection)dbConnector.getDatabaseConnection();
                statement =
                        (OracleCallableStatement)connection.prepareCall(query);

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2,
                                        new BigDecimal(session.getAttribute("sysUserCode") ==
                                                       null ? null :
                                                       session.getAttribute("sysUserCode").toString()));
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);
                int count = 0;
                while (resultSet.next()) {
                    // For every row, create a Client object with the
                    // values and add it to the list
                    Client2 client = new Client2();

                    client.setCode(resultSet.getString(1));
                    client.setShortDesc(resultSet.getString(2));
                    client.setName(resultSet.getString(3));
                    client.setOtherNames(resultSet.getString(4));
                    client.setIdRegNo(resultSet.getString(5));
                    client.setDob(resultSet.getDate(6));
                    client.setPin(resultSet.getString(7));
                    client.setPhysicalAddress(resultSet.getString(8));
                    client.setPostalAddress(resultSet.getString(9));
                    client.setTownCode(resultSet.getString(10));
                    client.setTownName(resultSet.getString(11));
                    client.setCountryCode(resultSet.getString(12));
                    client.setCountryName(resultSet.getString(13));
                    client.setEmail(resultSet.getString(14));
                    client.setPhone1(resultSet.getString(15));
                    client.setPhone2(resultSet.getString(16));
                    client.setStatus(resultSet.getString(17));
                    client.setFax(resultSet.getString(18));
                    client.setRemarks(resultSet.getString(19));
                    client.setSpecialTerms(resultSet.getString(20));
                    client.setDeclinedProp(resultSet.getString(21));
                    client.setIncreasedPremium(resultSet.getString(22));
                    client.setPolicyCancelled(resultSet.getString(23));
                    client.setProposer(resultSet.getString(24));
                    client.setAccountNum(resultSet.getString(25));
                    client.setDomicileCountries(resultSet.getString(26));
                    client.setWef(resultSet.getDate(27));
                    client.setWet(resultSet.getDate(28));
                    client.setWithdrawalReason(resultSet.getString(29));
                    client.setSectorCode(resultSet.getBigDecimal(30));

                    client.setSurname(resultSet.getString(31));
                    client.setType(resultSet.getString(32));

                    client.setTitle(resultSet.getString(33));
                    client.setBusiness(resultSet.getString(34));
                    client.setZipCode(resultSet.getString(35));
                    client.setBankBranchCode(resultSet.getString(36));
                    client.setBankAccountNum(resultSet.getString(37));
                    client.setClientCode(resultSet.getString(38));
                    client.setNonDirect(resultSet.getString(39));
                    client.setCreatedBy(resultSet.getString(40));
                    client.setSms(resultSet.getString(41));
                    client.setAgentStatus(resultSet.getString(42));
                    client.setDateCreated(resultSet.getDate(43));
                    client.setRunOff(resultSet.getString(44));
                    client.setLoadedBy(resultSet.getString(45));
                    client.setDirectClient(resultSet.getString(46));
                    client.setOldAccountNum(resultSet.getString(47));
                    client.setUserCode(resultSet.getString(48));
                    client.setUsername(resultSet.getString(49));
                    client.setContactPhone1(resultSet.getString(50));
                    client.setContactEmail1(resultSet.getString(51));
                    client.setContactPhone2(resultSet.getString(52));
                    client.setContactEmail2(resultSet.getString(53));
                    client.setStsCode(resultSet.getBigDecimal(54));
                    client.setStsName(resultSet.getString(55));
                    client.setPassportNumber(resultSet.getString(56));
                    client.setGender(resultSet.getString(57));
                    client.setContactName1(resultSet.getString(58));
                    client.setContactName2(resultSet.getString(59));
                    client.setSectorName(resultSet.getString(60));

                    client.setWebsite(resultSet.getString(61));
                    client.setAuditors(resultSet.getString(62));
                    client.setParent_company(resultSet.getString(63));
                    client.setCurrent_insurer(resultSet.getString(64));
                    client.setProjected_business(resultSet.getBigDecimal(65));
                    client.setDate_of_empl(resultSet.getDate(66));
                    client.setDriving_licence(resultSet.getString(67));
                    client.setParent_company_name(resultSet.getString(68));

                    client.setBrnCode(resultSet.getBigDecimal(69));
                    client.setBrnName(resultSet.getString(70));
                    clientsList.add(client);
                    count++;
                }
                session.setAttribute("client_count", count);
                resultSet.close();
                statement.close();
                connection.close();
                //}

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
        }
        return clientsList;
    }

    public List<Client2> fetchAllClientsByNames() {
        int count = 0;
        //counts no of record

        List<Client2> clientsList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "SELECT clnt_code, clnt_sht_desc, clnt_name, clnt_other_names,\n" +
            "                clnt_id_reg_no, clnt_dob, clnt_pin, clnt_physical_addrs,\n" +
            "                clnt_postal_addrs, clnt_twn_code,\n" +
            "                tqc_setups_cursor.town_name (clnt_twn_code) twn_name,\n" +
            "                clnt_cou_code,\n" +
            "                tqc_setups_cursor.country_name (clnt_cou_code) cou_name,\n" +
            "                clnt_email_addrs, clnt_tel, clnt_tel2, clnt_status, clnt_fax,\n" +
            "                clnt_remarks, clnt_spcl_terms, clnt_declined_prop,\n" +
            "                clnt_increased_premium, clnt_policy_cancelled, clnt_proposer,\n" +
            "                clnt_accnt_no, clnt_domicile_countries, clnt_wef, clnt_wet,\n" +
            "                clnt_withdrawal_reason, clnt_sec_code, clnt_surname,\n" +
            "                clnt_type, clnt_title, clnt_business, clnt_zip_code,\n" +
            "                clnt_bbr_code, clnt_bank_acc_no, clnt_clnt_code,\n" +
            "                clnt_non_direct, clnt_created_by, clnt_sms_tel,\n" +
            "                clnt_agnt_status, clnt_date_created, clnt_runoff,\n" +
            "                clnt_loaded_by, clnt_direct_client, clnt_old_accnt_no,\n" +
            "                clnt_usr_code,\n" +
            "                tqc_interfaces_pkg.username (clnt_usr_code) usr_name,\n" +
            "                clnt_cntct_phone_1, clnt_cntct_email_1, clnt_cntct_phone_2,\n" +
            "                clnt_cntct_email_2, clnt_sts_code,\n" +
            "                tqc_setups_cursor.state_name (clnt_sts_code) sts_name,\n" +
            "                clnt_passport_no, clnt_gender, clnt_cntct_name_1,\n" +
            "                clnt_cntct_name_2, NULL, NULL\n" +
            "           FROM tqc_clients\n" +
            "          WHERE (       UPPER (clnt_other_names) LIKE\n" +
            "                            '%' || UPPER (NVL (:v_other_name, 'HAKUNA)'))\n" +
            "                            || '%'\n" +
            "                    AND UPPER (clnt_name) LIKE\n" +
            "                                '%' || UPPER (NVL (:v_surname, 'HAKUNA'))\n" +
            "                                || '%'\n" +
            "                 OR UPPER (clnt_pin) LIKE\n" +
            "                               '%' || UPPER (NVL (:v_clnt_pin, 'HAKUNA'))\n" +
            "                               || '%'\n" +
            "                 OR UPPER (clnt_passport_no) LIKE\n" +
            "                       '%' || UPPER (NVL (:v_clnt_passport_no, 'HAKUNA'))\n" +
            "                       || '%'\n" +
            "                 OR UPPER (clnt_id_reg_no) LIKE\n" +
            "                         '%' || UPPER (NVL (:v_clnt_id_reg_no, 'HAKUNA'))\n" +
            "                         || '%'\n" +
            "                )";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        String otherName = null;
        try {
            clientsList = new ArrayList<Client2>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();

            if (session.getAttribute("surNames") != null) {

                String otherNames =
                    GlobalCC.checkNullValues(session.getAttribute("otherNames"));
                String passNo =
                    GlobalCC.checkNullValues(session.getAttribute("passportNumber"));
                String idRegNo =
                    GlobalCC.checkNullValues(session.getAttribute("idRegNumber"));
                String pinNo =
                    GlobalCC.checkNullValues(session.getAttribute("pinNumber"));
                String surNames =
                    GlobalCC.checkNullValues(session.getAttribute("surNames"));

                query =
                        query.replaceAll(":v_surname", surNames != null ? "'" + surNames +
                                                       "'" : "null");
                query =
                        query.replaceAll(":v_other_name", otherNames != null ? "'" +
                                                          otherNames + "'" :
                                                          "null");
                query =
                        query.replaceAll(":v_clnt_passport_no", passNo != null ?
                                                                "'" + passNo +
                                                                "'" : "null");
                query =
                        query.replaceAll(":v_clnt_id_reg_no", idRegNo != null ? "'" +
                                                              idRegNo + "'" :
                                                              "null");
                query =
                        query.replaceAll(":v_clnt_pin", pinNo != null ? "'" + pinNo +
                                                        "'" : "null");

                System.out.println("query: " + query);
                statement =
                        (OracleCallableStatement)connection.prepareCall(query);

                OracleResultSet resultSet =
                    (OracleResultSet)statement.executeQuery();

                while (resultSet.next()) {
                    // For every row, create a Client object with the
                    // values and add it to the list
                    count = count + 1;
                    Client2 client = new Client2();


                    client.setCode(resultSet.getString(1));
                    client.setShortDesc(resultSet.getString(2));
                    client.setName(resultSet.getString(3));
                    client.setOtherNames(resultSet.getString(4));
                    client.setIdRegNo(resultSet.getString(5));
                    client.setDob(resultSet.getDate(6));
                    client.setPin(resultSet.getString(7));
                    client.setPhysicalAddress(resultSet.getString(8));
                    client.setPostalAddress(resultSet.getString(9));
                    client.setTownCode(resultSet.getString(10));
                    client.setTownName(resultSet.getString(11));
                    client.setCountryCode(resultSet.getString(12));
                    client.setCountryName(resultSet.getString(13));
                    client.setEmail(resultSet.getString(14));
                    client.setPhone1(resultSet.getString(15));
                    client.setPhone2(resultSet.getString(16));
                    client.setStatus(resultSet.getString(17));
                    client.setFax(resultSet.getString(18));
                    client.setRemarks(resultSet.getString(19));
                    client.setSpecialTerms(resultSet.getString(20));
                    client.setDeclinedProp(resultSet.getString(21));
                    client.setIncreasedPremium(resultSet.getString(22));
                    client.setPolicyCancelled(resultSet.getString(23));
                    client.setProposer(resultSet.getString(24));
                    client.setAccountNum(resultSet.getString(25));
                    client.setDomicileCountries(resultSet.getString(26));
                    client.setWef(resultSet.getDate(27));
                    client.setWet(resultSet.getDate(28));
                    client.setWithdrawalReason(resultSet.getString(29));
                    client.setSectorCode(resultSet.getBigDecimal(30));
                    client.setSurname(resultSet.getString(31));
                    client.setType(resultSet.getString(32));
                    client.setTitle(resultSet.getString(33));
                    client.setBusiness(resultSet.getString(34));
                    client.setZipCode(resultSet.getString(35));
                    client.setBankBranchCode(resultSet.getString(36));
                    client.setBankAccountNum(resultSet.getString(37));
                    client.setClientCode(resultSet.getString(38));
                    client.setNonDirect(resultSet.getString(39));
                    client.setCreatedBy(resultSet.getString(40));
                    client.setSms(resultSet.getString(41));
                    client.setAgentStatus(resultSet.getString(42));
                    client.setDateCreated(resultSet.getDate(43));
                    client.setRunOff(resultSet.getString(44));
                    client.setLoadedBy(resultSet.getString(45));
                    client.setDirectClient(resultSet.getString(46));
                    client.setOldAccountNum(resultSet.getString(47));
                    client.setUserCode(resultSet.getString(48));
                    client.setUsername(resultSet.getString(49));
                    client.setContactPhone1(resultSet.getString(50));
                    client.setContactEmail1(resultSet.getString(51));
                    client.setContactPhone2(resultSet.getString(52));
                    client.setContactEmail2(resultSet.getString(53));
                    client.setStsCode(resultSet.getBigDecimal(54));
                    client.setStsName(resultSet.getString(55));
                    client.setPassportNumber(resultSet.getString(56));
                    client.setGender(resultSet.getString(57));
                    client.setContactName1(resultSet.getString(58));
                    client.setContactName2(resultSet.getString(59));

                    clientsList.add(client);
                }
                session.setAttribute("count", count);

                resultSet.close();
                statement.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return clientsList;
    }

    /**
     * Fetches all <code>Client</code> objects/records from the database and
     * returns them in a list.
     *
     * @return A list of <code>Client</code> objects/records fetched from the
     * database.
     */
    public List<Client2> fetchClientByCode() {

        List<Client2> clientsList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_CLIENTS_PKG.get_client_dtls(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            clientsList = new ArrayList<Client2>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("clientCode") != null) {
                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setString(2,
                                    session.getAttribute("clientCode").toString());
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);
                System.out.println("Client Errors");
                while (resultSet.next()) {
                    Client2 client = new Client2();

                    client.setCode(resultSet.getString(1));
                    client.setShortDesc(resultSet.getString(2));
                    client.setName(resultSet.getString(3));
                    client.setOtherNames(resultSet.getString(4));
                    client.setIdRegNo(resultSet.getString(5));
                    client.setDob(resultSet.getDate(6));
                    client.setPin(resultSet.getString(7));
                    client.setPhysicalAddress(resultSet.getString(8));
                    client.setPostalAddress(resultSet.getString(9));
                    client.setTownCode(resultSet.getString(10));
                    client.setTownName(resultSet.getString(11));
                    client.setCountryCode(resultSet.getString(12));
                    client.setCountryName(resultSet.getString(13));
                    client.setEmail(resultSet.getString(14));
                    client.setPhone1(resultSet.getString(15));
                    client.setPhone2(resultSet.getString(16));
                    client.setStatus(resultSet.getString(17));
                    client.setFax(resultSet.getString(18));
                    client.setRemarks(resultSet.getString(19));
                    client.setSpecialTerms(resultSet.getString(20));
                    client.setDeclinedProp(resultSet.getString(21));
                    client.setIncreasedPremium(resultSet.getString(22));
                    client.setPolicyCancelled(resultSet.getString(23));
                    client.setProposer(resultSet.getString(24));
                    client.setAccountNum(resultSet.getString(25));
                    client.setDomicileCountries(resultSet.getString(26));
                    client.setWef(resultSet.getDate(27));
                    client.setWet(resultSet.getDate(28));
                    client.setWithdrawalReason(resultSet.getString(29));
                    client.setSectorCode(resultSet.getBigDecimal(30));
                    client.setSurname(resultSet.getString(31));
                    client.setType(resultSet.getString(32));
                    client.setTitle(resultSet.getString(33));
                    client.setBusiness(resultSet.getString(34));
                    client.setZipCode(resultSet.getString(35));
                    client.setBankBranchCode(resultSet.getString(36));
                    client.setBankAccountNum(resultSet.getString(37));
                    client.setClientCode(resultSet.getString(38));
                    client.setNonDirect(resultSet.getString(39));
                    client.setCreatedBy(resultSet.getString(40));
                    client.setSms(resultSet.getString(41));
                    client.setAgentStatus(resultSet.getString(42));
                    client.setDateCreated(resultSet.getDate(43));
                    client.setRunOff(resultSet.getString(44));
                    client.setLoadedBy(resultSet.getString(45));
                    client.setDirectClient(resultSet.getString(46));
                    client.setOldAccountNum(resultSet.getString(47));
                    client.setUserCode(resultSet.getString(48));
                    client.setUsername(resultSet.getString(49));
                    client.setContactPhone1(resultSet.getString(50));
                    client.setContactEmail1(resultSet.getString(51));
                    client.setContactPhone2(resultSet.getString(52));
                    client.setContactEmail2(resultSet.getString(53));
                    client.setStsCode(resultSet.getBigDecimal(54));
                    client.setStsName(resultSet.getString(55));
                    client.setPassportNumber(resultSet.getString(56));
                    client.setGender(resultSet.getString(57));
                    client.setContactName1(resultSet.getString(58));
                    client.setContactName2(resultSet.getString(59));
                    client.setClientCell(resultSet.getString(60));
                    client.setBankBranch(resultSet.getString(61));
                    clientsList.add(client);
                }

                resultSet.close();
                statement.close();
                connection.close();
            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return clientsList;
    }

    /**
     * Fetches all <code>User</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>User</code> objects/records
     * fetched from the database.
     */
    public List<User> fetchAllAccountManagers() {

        List<User> managersList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := tqc_web_cursor.get_acc_mangrs(); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            managersList = new ArrayList<User>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a User object with the
                // values and add it to the list
                User manager = new User();
                manager.setUsername(resultSet.getString(1));
                manager.setUserFullname(resultSet.getString(2));
                manager.setUserCode(new BigDecimal(resultSet.getString(3)));
                managersList.add(manager);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return managersList;
    }

    /**
     * Fetches all <code>Bank</code> object/records from the database and returns
     * them in a list.
     *
     * @return
     */
    public List<AppSettings> fetchAllBanks() {

        String query = "begin ? := tqc_clients_pkg.get_banks(); end;";
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<AppSettings> banksList = new ArrayList<AppSettings>();
        try {
            DBConnector datahandler = new DBConnector();
            conn = (OracleConnection)datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            while (rs.next()) {
                AppSettings Bank = new AppSettings();

                Bank.setBankName(rs.getString(1));
                Bank.setBranchName(rs.getString(2));
                Bank.setBankBranchCode(rs.getBigDecimal(3));
                Bank.setBankShortDesc(rs.getString(4));
                banksList.add(Bank);

            }
            cst.close();
            rs.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return banksList;
    }


    /**
     * Fetches all <code>Division</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>Division</code> objects/records
     * fetched from the database.
     */
    public List<Division> fetchDivisionByBranch() {

        List<Division> divisionsList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin tqc_clients_pkg.get_divisions(?,?); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            divisionsList = new ArrayList<Division>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);
            
           
          
            if (session.getAttribute("branchCode") != null) {
               // GlobalCC.INFORMATIONREPORTING("codeddd"+ session.getAttribute("branchCode").toString());
                statement.setBigDecimal(1,(BigDecimal)session.getAttribute("branchCode")); //(BigDecimal)session.getAttribute("branchCode"));
            } else {
                statement.setString(1, "");
            }

            statement.registerOutParameter(2,
                                           oracle.jdbc.internal.OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(2);

            while (resultSet.next()) {
                // For every row, create a Division object with the
                // values and add it to the list
                Division division = new Division();

                division.setDIV_NAME(resultSet.getString(1));
                division.setDIV_CODE(new BigDecimal(resultSet.getString(2)));

                divisionsList.add(division);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return divisionsList;
    }

    /**
     * Fetches all web accounts records for a given client
     *
     * @return A list of all web accounts for a given client
     */
    public List<AppClients> fetchClientWebAccounts() {

        List<AppClients> webAccountsList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := tqc_clients_pkg.get_client_web_accounts(?); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            webAccountsList = new ArrayList<AppClients>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("ClientCode") != null) {
                statement.setBigDecimal(2,
                                        new BigDecimal(session.getAttribute("ClientCode").toString()));
            } else {
                statement.setBigDecimal(2, null);
            }

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a web account object with the
                // values and add it to the list
                AppClients webAccount = new AppClients();

                webAccount.setClientAccCode(resultSet.getBigDecimal(1));
                webAccount.setClientAccUserName(resultSet.getString(2));
                webAccount.setClientAccName(resultSet.getString(10));
                webAccount.setClientAccPersonelRank(resultSet.getString(5));
                webAccount.setClientAccCreatedBy(resultSet.getString(9));
                webAccount.setClientAccDateCreated(resultSet.getDate(6));
                webAccount.setClientAccLoginAllowed(resultSet.getString(4));
                webAccount.setClientAccStatus(resultSet.getString(7));
                webAccount.setClientAccEmail(resultSet.getString(11));

                webAccountsList.add(webAccount);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return webAccountsList;
    }

    /**
     * Fetches all accounts records for a given client
     *
     * @return A list of all accounts for a given client
     */
    public List<ClientAccount> fetchClientAccounts() {

        List<ClientAccount> accountsList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := tqc_clients_pkg.get_client_accounts(?); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            accountsList = new ArrayList<ClientAccount>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("ClientCode") != null) {

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2,
                                        new BigDecimal(session.getAttribute("ClientCode").toString()));
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    ClientAccount account = new ClientAccount();

                    account.setCode(resultSet.getBigDecimal(1));
                    account.setShortDesc(resultSet.getString(2));
                    account.setName(resultSet.getString(3));
                    account.setClientCode(resultSet.getBigDecimal(4));
                    account.setCreatedBy(resultSet.getString(5));
                    account.setDateCreated(resultSet.getDate(6));
                    account.setStatus(resultSet.getString(7));
                    account.setRemarks(resultSet.getString(8));
                    account.setWef(resultSet.getDate(9));
                    account.setWet(resultSet.getDate(10));
                    account.setBdivCode(resultSet.getBigDecimal(11));
                    account.setBrnName(resultSet.getString(12));
                    account.setDivName(resultSet.getString(13));
                    account.setContactName(resultSet.getString(14));
                    account.setContactTitle(resultSet.getString(15));
                    account.setContactSmsNo(resultSet.getString(16));
                    account.setContactEmail(resultSet.getString(17));
                    accountsList.add(account);
                }

                resultSet.close();
                statement.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return accountsList;
    }


    /**
     * Fetches all Contact records for a given client
     *
     * @return A list of all contacts for a given client
     */
    public List<ClientAccount> fetchClientContacts() {

        List<ClientAccount> accountsList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := tqc_clients_pkg.getClientContacts(?); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            accountsList = new ArrayList<ClientAccount>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("ClientCode") != null) {

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2,
                                        new BigDecimal(session.getAttribute("ClientCode").toString()));
                statement.execute();
                OracleResultSet rs = (OracleResultSet)statement.getObject(1);

                while (rs.next()) {
                    ClientAccount contact = new ClientAccount();
                    contact.setClcoCode(rs.getBigDecimal(1));
                    contact.setClcoName(rs.getString(3));
                    contact.setClcoPostAdd(rs.getString(4));
                    contact.setClcoPhysAdd(rs.getString(5));
                    contact.setSecCode(rs.getBigDecimal(6));
                    contact.setSecName(rs.getString(7));
                    accountsList.add(contact);
                }

                rs.close();
                statement.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return accountsList;
    }

    /**
     * @return
     */
    public List<User> fetchUnallocatedClientSystems() {

        List<User> systemsList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := tqc_clients_pkg.get_client_unassigned_systems(?); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            systemsList = new ArrayList<User>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("ClientCode") != null) {
                statement.setBigDecimal(2,
                                        new BigDecimal(session.getAttribute("ClientCode").toString()));
            } else {
                statement.setBigDecimal(2, null);
            }

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a system account object with the
                // values and add it to the list
                User system = new User();

                system.setSysCode(resultSet.getBigDecimal(1));
                system.setSysShtDesc(resultSet.getString(2));
                system.setSysName(resultSet.getString(3));
                system.setSysActive(resultSet.getString(4));

                systemsList.add(system);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return systemsList;
    }

    /**
     * @return
     */
    public List<User> fetchAllocatedClientSystems() {

        List<User> systemsList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := tqc_clients_pkg.get_client_assigned_systems(?); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            systemsList = new ArrayList<User>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("ClientCode") != null) {
                statement.setBigDecimal(2,
                                        new BigDecimal(session.getAttribute("ClientCode").toString()));
            } else {
                statement.setBigDecimal(2, null);
            }

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a system account object with the
                // values and add it to the list
                User system = new User();

                system.setSysCode(resultSet.getBigDecimal(1));
                system.setSysShtDesc(resultSet.getString(2));
                system.setSysName(resultSet.getString(3));
                system.setSysActive(resultSet.getString(4));

                systemsList.add(system);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return systemsList;
    }


    /**
     * @return
     */
    public List<SettingsValues> fetchClientRequiredDocs() {

        List<SettingsValues> docsList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := tqc_web_cursor.get_client_docs(?); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            docsList = new ArrayList<SettingsValues>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("ClientCode") != null) {
                statement.setBigDecimal(2,
                                        new BigDecimal(session.getAttribute("ClientCode").toString()));
            } else {
                statement.setBigDecimal(2, null);
            }

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a requierd document object with the
                // values and add it to the list
                SettingsValues doc = new SettingsValues();

                doc.setCDOCR_CODE(resultSet.getBigDecimal(1));
                doc.setCDOCR_RDOC_CODE(resultSet.getBigDecimal(2));
                doc.setCDOCR_CLNT_CODE(resultSet.getBigDecimal(3));
                doc.setCDOCR_SUBMITED(resultSet.getString(4));
                doc.setCDOCR_DATE_S(resultSet.getDate(5));
                doc.setCDOCR_REF_NO(resultSet.getString(6));
                doc.setCDOCR_RMRK(resultSet.getString(7));
                doc.setCDOCR_USER_RECEIVD(resultSet.getString(8));
                doc.setROC_DESC(resultSet.getString(9));
                doc.setCDOCR_DOCID(resultSet.getString(10));
                docsList.add(doc);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return docsList;
    }

    public List<SettingsValues> fetchAgentRequiredDocs() {

        List<SettingsValues> docsList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := tqc_web_cursor.get_agent_docs(?); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            docsList = new ArrayList<SettingsValues>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("ClientCode") != null) {
                statement.setBigDecimal(2,
                                        new BigDecimal(session.getAttribute("ClientCode").toString()));
            } else {
                statement.setBigDecimal(2, null);
            }

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a requierd document object with the
                // values and add it to the list
                SettingsValues doc = new SettingsValues();

                doc.setCDOCR_CODE(resultSet.getBigDecimal(1));
                doc.setCDOCR_RDOC_CODE(resultSet.getBigDecimal(2));
                doc.setCDOCR_CLNT_CODE(resultSet.getBigDecimal(3));
                doc.setCDOCR_SUBMITED(resultSet.getString(4));
                doc.setCDOCR_DATE_S(resultSet.getDate(5));
                doc.setCDOCR_REF_NO(resultSet.getString(6));
                doc.setCDOCR_RMRK(resultSet.getString(7));
                doc.setCDOCR_USER_RECEIVD(resultSet.getString(8));
                doc.setROC_DESC(resultSet.getString(9));
                doc.setCDOCR_DOCID(resultSet.getString(10));
                docsList.add(doc);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return docsList;
    }


    public List<SettingsValues> fetchSpRequiredDocs() {

        List<SettingsValues> docsList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := tqc_web_cursor.get_sp_docs(?); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            docsList = new ArrayList<SettingsValues>();
            BigDecimal spCode =
                GlobalCC.checkBDNullValues(session.getAttribute("serviceProviderCode"));
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("serviceProviderCode") != null) {
                statement.setBigDecimal(2, spCode);
            } else {
                statement.setBigDecimal(2, null);
            }

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a requierd document object with the
                // values and add it to the list
                SettingsValues doc = new SettingsValues();

                doc.setCDOCR_CODE(resultSet.getBigDecimal(1));
                doc.setCDOCR_RDOC_CODE(resultSet.getBigDecimal(2));
                doc.setCDOCR_CLNT_CODE(resultSet.getBigDecimal(3));
                doc.setCDOCR_SUBMITED(resultSet.getString(4));
                doc.setCDOCR_DATE_S(resultSet.getDate(5));
                doc.setCDOCR_REF_NO(resultSet.getString(6));
                doc.setCDOCR_RMRK(resultSet.getString(7));
                doc.setCDOCR_USER_RECEIVD(resultSet.getString(8));
                doc.setROC_DESC(resultSet.getString(9));
                doc.setCDOCR_DOCID(resultSet.getString(10));
                docsList.add(doc);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return docsList;
    }

    public List<ClientTitle> fetchAllClientTitles(String clientShtDesc) {

        List<ClientTitle> titlesList = null;
        DBConnector dbConnector = new DBConnector();

        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        String query = "SELECT clt_code, clt_sht_desc, clnt_desc\n" +
            "           FROM tqc_client_titles\n" +
            "          WHERE UPPER (clt_sht_desc) LIKE '%' || UPPER (:v_clt_sht_desc) || '%'\n" +
            "            AND ROWNUM < 120";
        titlesList = new ArrayList<ClientTitle>();
        try {
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            String vSearch = GlobalCC.checkNullValues(clientShtDesc);
            query =
                    query.replaceAll(":v_clt_sht_desc", "'" + (vSearch != null ?
                                                               vSearch : "") +
                                     "'");
            statement = (OracleCallableStatement)connection.prepareCall(query);
            OracleResultSet resultSet =
                (OracleResultSet)statement.executeQuery();

            while (resultSet.next()) {
                // For every row, create a ClientTitle object with the
                // values and add it to the list
                ClientTitle title = new ClientTitle();
                title.setCode(resultSet.getString(1));
                title.setShortDesc(resultSet.getString(2));
                title.setDescription(resultSet.getString(3));
                titlesList.add(title);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
            System.err.println("Unable to execute: " + query);
        }

        return titlesList;
    }

    public List<ClientTitle> fetchAllClientTitles() {

        List<ClientTitle> titlesList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "SELECT clt_code, clt_sht_desc, clnt_desc\n" +
            "           FROM tqc_client_titles";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            titlesList = new ArrayList<ClientTitle>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);
            OracleResultSet resultSet =
                (OracleResultSet)statement.executeQuery();

            while (resultSet.next()) {
                // For every row, create a ClientTitle object with the
                // values and add it to the list
                ClientTitle title = new ClientTitle();
                title.setCode(resultSet.getString(1));
                title.setShortDesc(resultSet.getString(2));
                title.setDescription(resultSet.getString(3));
                titlesList.add(title);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
            System.err.println("Unable to execute: " + query);
        }

        return titlesList;
    }

    public List<RequiredDocument> fetchAllRequiredDocuments() {

        List<RequiredDocument> docsList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_WEB_CURSOR.getrequireddocs(); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            docsList = new ArrayList<RequiredDocument>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a Required Document object with the
                // values and add it to the list
                RequiredDocument doc = new RequiredDocument();
                doc.setCode(resultSet.getString(1));
                doc.setShortDesc(resultSet.getString(2));
                doc.setDescription(resultSet.getString(3));
                doc.setMandatory(resultSet.getString(4));
                doc.setDateAdded(resultSet.getDate(5));
                doc.setAccount_type(resultSet.getString(6));

                docsList.add(doc);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return docsList;
    }

    public List<RequiredDocument> fetchAllAgentRequiredDocuments() {

        List<RequiredDocument> docsList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_WEB_CURSOR.getagentrequireddocs(?); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            docsList = new ArrayList<RequiredDocument>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            if (session.getAttribute("docAccountType") != null) {
                statement.setString(2,
                                    session.getAttribute("docAccountType").toString());
            } else {
                statement.setString(2, null);

            }
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a Required Document object with the
                // values and add it to the list
                RequiredDocument doc = new RequiredDocument();
                doc.setCode(resultSet.getString(1));
                doc.setShortDesc(resultSet.getString(2));
                doc.setDescription(resultSet.getString(3));
                doc.setMandatory(resultSet.getString(4));
                doc.setDateAdded(resultSet.getDate(5));
                doc.setAccount_type(resultSet.getString(6));

                docsList.add(doc);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return docsList;
    }

    public List<RequiredDocument> fetchAllClientRequiredDocuments() {

        List<RequiredDocument> docsList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_WEB_CURSOR.getclientrequireddocs(?); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            docsList = new ArrayList<RequiredDocument>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setString(2, "C");
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a Required Document object with the
                // values and add it to the list
                RequiredDocument doc = new RequiredDocument();
                doc.setCode(resultSet.getString(1));
                doc.setShortDesc(resultSet.getString(2));
                doc.setDescription(resultSet.getString(3));
                doc.setMandatory(resultSet.getString(4));
                doc.setDateAdded(resultSet.getDate(5));
                doc.setAccount_type(resultSet.getString(6));

                docsList.add(doc);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return docsList;
    }

    public List<Client2> getClientCode() {
        List<Client2> cl = new ArrayList<Client2>();
        Client2 cli = new Client2();
        if (session.getAttribute("ClientCode") != null) {
            cli = null;
            cli.setClnt_code(GlobalCC.checkBDNullValues(session.getAttribute("ClientCode")));
            cl.add(cli);
            return cl;
        } else {
            cli.setClnt_code(new BigDecimal(-2000));
            cl.add(cli);
            return cl;

        }
    }

    public List<Client2> findClientsDirectors() {
        List<Client2> client2List = null;
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin TQC_CLIENTS_PKG.get_tqc_client_directors(?,?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        String clientid = "";
        try {
            if (session.getAttribute("ClientCode") != null)
                clientid = session.getAttribute("ClientCode").toString();
            else
                clientid = "0";
            client2List = new ArrayList<Client2>();
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            if (clientid.equals("0"))
                stmt.setBigDecimal(1, null);
            else
                stmt.setBigDecimal(1, new BigDecimal(clientid));
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(2);
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setClntdir_code(rst.getBigDecimal(1));
                client2.setClntdir_year(rst.getBigDecimal(3));
                client2.setClntdir_name(rst.getString(4));
                client2.setClntdir_qualifications(rst.getString(5));
                client2.setClntdir_pct_holdg(rst.getBigDecimal(6));
                client2.setClntdir_designation(rst.getString(7));
                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return client2List;
    }

    public List<Client2> findClientsAuditors() {
        List<Client2> client2List = null;
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin TQC_CLIENTS_PKG.get_tqc_client_auditors(?,?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        String clientid = "";
        try {
            if (session.getAttribute("ClientCode") != null)
                clientid = session.getAttribute("ClientCode").toString();
            else
                clientid = "0";
            client2List = new ArrayList<Client2>();
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            if (clientid.equals("0"))
                stmt.setBigDecimal(1, null);
            else
                stmt.setBigDecimal(1, new BigDecimal(clientid));
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(2);
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setClntaud_code(rst.getBigDecimal(1));
                client2.setClntaud_year(rst.getString(3));
                client2.setClntaud_name(rst.getString(4));
                client2.setClntaud_qualifications(rst.getString(5));
                client2.setClntaud_telephone(rst.getString(6));
                client2.setClntaud_designation(rst.getString(7));
                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return client2List;
    }

    public List<Branch> findBranches() {
        List<Branch> branchList = null;
        DBConnector OracleConnection = new DBConnector();

        String query = "begin  ? := TQC_SETUPS_CURSOR.findBranches(?,?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;

        if (session.getAttribute("userDefaultReg") != null) {

            BigDecimal regcode =
                GlobalCC.checkBDNullValues(session.getAttribute("userDefaultReg"));
            System.out.println("userDefaultReg" + "   --" + regcode);

            try {
                branchList = new ArrayList<Branch>();
                BigDecimal orgCode =
                    GlobalCC.checkBDNullValues(session.getAttribute("orgCode"));

                if ("GisNewClient".equals(session.getAttribute("source"))) {
                    orgCode = new BigDecimal(2);
                }
                if ("LmsNewClient".equals(session.getAttribute("source"))) {
                    orgCode = new BigDecimal(1);
                }
                conn =
(OracleConnection)OracleConnection.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query);
                stmt.setBigDecimal(2, regcode);
                stmt.setBigDecimal(3, orgCode);
                stmt.registerOutParameter(1, OracleTypes.CURSOR);
                stmt.execute();
                OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
                while (rst.next()) {
                    Branch branch = new Branch();
                    branch.setBranchCode(rst.getBigDecimal(1));
                    branch.setOrgShtDesc(rst.getString(2));
                    branch.setBranchName(rst.getString(3));
                    branchList.add(branch);
                }
                rst.close();
                stmt.close();
                conn.close();
            }

            catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return branchList;
    }


    public List<Client2> findClientGroups() {
        List<Client2> client2List = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin  ? :=TQC_SETUPS_CURSOR.getClientGroups();end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        String clientid = "";
        try {


            client2List = new ArrayList<Client2>();
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);


            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setGrp_Code(rst.getBigDecimal(1));
                client2.setGrp_Name(rst.getString(2));
                client2.setGrp_Minimum(rst.getBigDecimal(3));
                client2.setGrp_Maximum(rst.getBigDecimal(4));

                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return client2List;
    }

    public List<Client2> findHouseHolds() {
        List<Client2> client2List = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin  ? := TQC_SETUPS_CURSOR.getHouseHolds();end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        String clientid = "";
        try {


            client2List = new ArrayList<Client2>();
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);


            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setHhCode(rst.getBigDecimal(1));
                client2.setHhName(rst.getString(2));
                client2.setCreatedBy(rst.getString(3));
                client2.setDateCreated(rst.getDate(4));
                client2.setHhCategory(rst.getString(5));
                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return client2List;
    }

    public List<Client2> findHouseHoldMembers() {
        List<Client2> client2List = null;
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin   ? :=TQC_SETUPS_CURSOR.getHouseHoldMembers(?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        String clientid = "";
        client2List = new ArrayList<Client2>();

        try {
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setObject(2, session.getAttribute("hhId"));
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setHhName(rst.getString(1));
                client2.setHhCode(rst.getBigDecimal(2));
                client2.setClnt_code(rst.getBigDecimal(3));
                client2.setShortDesc(rst.getString(4));
                client2.setName(rst.getString(5));
                client2.setOtherNames(rst.getString(6));
                client2.setSelected(false);
                client2.setHead("N");
                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return client2List;
    }


    public List<Client2> findClientGroupMembers() {
        List<Client2> client2List = null;
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin   ? :=TQC_SETUPS_CURSOR.getClientGroupMembers(?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        String clientid = "";
        client2List = new ArrayList<Client2>();
        if (session.getAttribute("grpCode") != null) {
            try {


                conn =
(OracleConnection)OracleConnection.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query);


                stmt.registerOutParameter(1, OracleTypes.CURSOR);
                stmt.setBigDecimal(2,
                                   new BigDecimal(session.getAttribute("grpCode").toString()));

                stmt.execute();
                OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
                while (rst.next()) {
                    Client2 client2 = new Client2();
                    client2.setGrp_Name(rst.getString(1));
                    client2.setGrpd_Code(rst.getBigDecimal(2));
                    client2.setGrpd_Clnt_Code(rst.getBigDecimal(3));
                    client2.setGrpd_Grp_Code(rst.getBigDecimal(4));
                    client2.setShortDesc(rst.getString(5));
                    client2.setName(rst.getString(6));
                    client2.setOtherNames(rst.getString(7));


                    client2List.add(client2);
                }
                rst.close();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return client2List;
    }

    public List<Client2> findParCompany() {
        List<Client2> client2List = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin  TQC_CLIENTS_PKG.get_parentCompany(?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;

        client2List = new ArrayList<Client2>();

        try {


            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);


            stmt.registerOutParameter(1, OracleTypes.CURSOR);

            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setClientCode(rst.getString(1));
                client2.setShortDesc(rst.getString(2));
                client2.setName(rst.getString(3));
                client2.setOtherNames(rst.getString(4));

                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }
        return client2List;
    }

    public List<Client2> findHoldingCompanies() {
        List<Client2> client2List = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin ? := TQC_CLIENTS_PKG.getHoldingClients(); end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        client2List = new ArrayList<Client2>();

        try {
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setCLNT_CODE(rst.getBigDecimal(1));
                client2.setCLNT_SHT_DESC(rst.getString(2));
                client2.setCLNT_NAME(rst.getString(3));
                client2.setCLNT_CLNT_CODE(rst.getBigDecimal(4));
                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return client2List;
    }

   public List<Client2> findClientTypes() {
        List<Client2> client2List = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin ? := TQC_CLIENTS_PKG.getClientTypes(); end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        client2List = new ArrayList<Client2>();

        try {
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setCLNTY_CODE(rst.getBigDecimal(1));
                client2.setCLNTY_CLNT_TYPE(rst.getString(2));
                client2.setCLNTY_CATEGORY(rst.getString(3));
                client2.setCLNTY_PERSON(rst.getString(4));
                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return client2List;
    }
  
    public List<Client2> findLoadedClients() {
        String options = "L";
        if (session.getAttribute("options") == null ||
            session.getAttribute("options").equals("")) {
            options = null;
        } else {
            options = session.getAttribute("options").toString();
        }
        String coverTypeSectionQuery =
            "begin ? := TQC_UTILITIES_CURSOR.search_loaded_clients(?); end;";

        DBConnector datahandler = new DBConnector();
        Connection conn;
        conn = datahandler.getDatabaseConnection();
        ResultSet sectionsRS = null;
        CallableStatement cstSections = null;
        List<Client2> risks = new ArrayList<Client2>();

        try {
            cstSections = conn.prepareCall(coverTypeSectionQuery);
            cstSections.registerOutParameter(1,
                                             oracle.jdbc.OracleTypes.CURSOR); //authorization code

            cstSections.setString(2, options);
            cstSections.execute();
            sectionsRS = (ResultSet)cstSections.getObject(1);

            while (sectionsRS.next()) {
                Client2 risk = new Client2();
                risk.setCLN_CODE(sectionsRS.getBigDecimal(1));
                risk.setCLN_CLNT_SHT_DESC(sectionsRS.getString(2));
                risk.setTownCode(sectionsRS.getString(3));
                risk.setCLN_CLNT_NAME(sectionsRS.getString(4));
                risk.setCLN_CLNT_OTHER_NAMES(sectionsRS.getString(5));
                risk.setCLN_CLNT_POSTAL_ADDRS(sectionsRS.getString(6));
                risk.setCLN_CLNT_PHYSICAL_ADDRS(sectionsRS.getString(7));
                risk.setCLN_CLNT_TEL(sectionsRS.getString(8));
                risk.setCLN_CLNT_TEL2(sectionsRS.getString(9));
                risk.setCLN_CLNT_FAX(sectionsRS.getString(10));
                risk.setCLN_CLNT_CNTCT_EMAIL_1(sectionsRS.getString(11));
                risk.setCLN_CLNT_ID_REG_NO(sectionsRS.getString(12));
                risk.setCLN_CLNT_DOB(sectionsRS.getDate(13));
                risk.setClnRemarks(sectionsRS.getString(14));
                risks.add(risk);

            }
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cstSections, sectionsRS);
        }

        return risks;
    }

    public List<Agents> findLoadedAgents() {
        String options = "A";
        if (session.getAttribute("options") == null ||
            session.getAttribute("options").equals("")) {
            options = "A";
        } else {
            options = session.getAttribute("options").toString();
        }
        String loadedAgents =
            "begin ? := TQC_UTILITIES_CURSOR.search_loaded_agents(?); end;";

        DBConnector datahandler = new DBConnector();
        Connection conn;
        conn = datahandler.getDatabaseConnection();
        ResultSet agentsRs = null;
        CallableStatement agentsloaded = null;
        List<Agents> agent = new ArrayList<Agents>();

        try {
            agentsloaded = conn.prepareCall(loadedAgents);
            agentsloaded.registerOutParameter(1,
                                              oracle.jdbc.OracleTypes.CURSOR); //authorization code
            agentsloaded.setString(2, options);
            agentsloaded.execute();
            agentsRs = (ResultSet)agentsloaded.getObject(1);

            while (agentsRs.next()) {
                Agents agents = new Agents();
                agents.setAgnlCode(agentsRs.getBigDecimal(1));
                agents.setAgnlAccountCode(agentsRs.getString(2));
                agents.setAgnlaccountType(agentsRs.getString(3));
                agents.setAgnlaccountName(agentsRs.getString(4));
                agents.setAgnlphysicalAddress(agentsRs.getString(5));
                agents.setAgnlpostalAddress(agentsRs.getString(6));
                agents.setAgnlTownName(agentsRs.getString(7));
                agents.setAgnlregCode(agentsRs.getString(8));
                agents.setAgnlcontactperson(agentsRs.getString(9));
                agents.setAgnltelephoneNumber(agentsRs.getString(10));
                agents.setAgnlfaxNumber(agentsRs.getString(11));
                agents.setAgnlemailAddress(agentsRs.getString(12));
                agents.setAgnldateCreated(agentsRs.getDate(13));
                agents.setAgnlcheckDate(agentsRs.getDate(14));
                agents.setBranchName(agentsRs.getString(15));
                agent.add(agents);
            }
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, agentsloaded, agentsRs);
        }

        return agent;
    }

    public List<Agents> selectBranchDetails() {

        String brancheslov =
            "begin ? := TQC_UTILITIES_CURSOR.selectAllBranches(); end;";

        DBConnector datahandler = new DBConnector();
        Connection conn;
        conn = datahandler.getDatabaseConnection();
        ResultSet agentsRs = null;
        CallableStatement agentsloaded = null;
        List<Agents> agent = new ArrayList<Agents>();

        try {
            agentsloaded = conn.prepareCall(brancheslov);
            agentsloaded.registerOutParameter(1,
                                              oracle.jdbc.OracleTypes.CURSOR); //authorization code
            agentsloaded.execute();
            agentsRs = (ResultSet)agentsloaded.getObject(1);

            while (agentsRs.next()) {
                Agents agents = new Agents();
                agents.setBranchCodelov(agentsRs.getBigDecimal(1));
                agents.setBranchShtDesc(agentsRs.getString(2));
                agents.setRegCode(agentsRs.getBigDecimal(3));
                agents.setBranchName(agentsRs.getString(4));
                agent.add(agents);
            }
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, agentsloaded, agentsRs);
        }

        return agent;
    }

    public List<Client2> findWebClientBranches() {
        List<Client2> client2List = null;
        BigDecimal clientCode;
        if (session.getAttribute("ClientCode") != null) {
            clientCode =
                    new BigDecimal(session.getAttribute("ClientCode").toString());
        } else {
            clientCode = null;
        }
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin  ? :=TQC_SETUPS_CURSOR.getwebClientsBranches(?) ;end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        OracleResultSet rst = null;

        client2List = new ArrayList<Client2>();

        try {
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setBigDecimal(2, clientCode);
            stmt.execute();
            rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setClientBankCode(rst.getBigDecimal(1));
                client2.setClnCode(rst.getBigDecimal(2));
                client2.setClientShtDesc(rst.getString(3));
                client2.setClientBankName(rst.getString(4));
                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return client2List;
    }

    public List<Client2> findUnassignedCompanies() {
        List<Client2> client2List = null;
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getUnassignedBranches(?); end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        BigDecimal clientCode;
        if (session.getAttribute("ClientCode") != null) {
            clientCode =
                    new BigDecimal(session.getAttribute("ClientCode").toString());
        } else {
            clientCode = null;
        }
        client2List = new ArrayList<Client2>();

        try {
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setBigDecimal(2, clientCode); //authorization code
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setCLNT_CODE(rst.getBigDecimal(1));
                client2.setClientBranchCode(rst.getBigDecimal(2));
                client2.setClientShtDesc(rst.getString(3));
                client2.setClientBankName(rst.getString(4));
                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return client2List;
    }

    public List<Client2> findassignedCompanies() {
        BigDecimal clientCode;
        List<Client2> client2List = null;
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getassignedBranches(?); end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        client2List = new ArrayList<Client2>();

        if (session.getAttribute("ClientCode") != null) {
            clientCode =
                    new BigDecimal(session.getAttribute("ClientCode").toString());
        } else {
            clientCode = null;
        }
        try {
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setBigDecimal(2, clientCode); //authorization code
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setCLNT_CODE(rst.getBigDecimal(1));
                client2.setClientBranchCode(rst.getBigDecimal(2));
                client2.setClientShtDesc(rst.getString(3));
                client2.setClientBankName(rst.getString(4));

                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return client2List;
    }

    public List<Client2> findDefaultBranchLov() {
        List<Client2> client2List = null;

        DBConnector OracleConnection = new DBConnector();
        String query = "begin  ? :=TQC_SETUPS_CURSOR.getDefaultBranches ;end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        OracleResultSet rst = null;

        client2List = new ArrayList<Client2>();

        try {
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setClientBankCode(rst.getBigDecimal(1));
                client2.setClnCode(rst.getBigDecimal(2));
                client2.setClientShtDesc(rst.getString(3));
                client2.setClientBankName(rst.getString(4));
                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return client2List;
    }

    public List<Client2> findWebProducts() {
        List<Client2> client2List = null;

        DBConnector OracleConnection = new DBConnector();
        String query = "begin  ? :=TQC_SETUPS_CURSOR.getwebProducts ;end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        OracleResultSet rst = null;

        client2List = new ArrayList<Client2>();

        try {
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setWebProductCode(rst.getBigDecimal(1));
                client2.setWebproductDetailsCode(rst.getBigDecimal(2));
                client2.setProductCode(rst.getBigDecimal(3));
                client2.setWebProductDesc(rst.getString(4));
                client2.setProductDesc(rst.getString(5));
                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return client2List;
    }

    public List<Client2> findWebUsers() {
        List<Client2> client2List = null;

        DBConnector OracleConnection = new DBConnector();
        String query = "begin  ? :=TQC_SETUPS_CURSOR.getWebUsers ;end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        OracleResultSet rst = null;

        client2List = new ArrayList<Client2>();

        try {
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setWebUserCode(rst.getBigDecimal(1));
                client2.setUserName(rst.getString(2));
                client2.setUserRealName(rst.getString(3));
                client2.setUserStatus(rst.getString(4));
                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return client2List;
    }

    public List<Client2> findWebProductsDetails() {
        BigDecimal clientCode;
        List<Client2> client2List = null;

        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin  ? :=TQC_SETUPS_CURSOR.getWebProductDetails(?) ;end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        OracleResultSet rst = null;

        client2List = new ArrayList<Client2>();
        if (session.getAttribute("ClientCode") != null) {
            clientCode =
                    new BigDecimal(session.getAttribute("ClientCode").toString());
        } else {
            clientCode = null;
        }

        try {
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setBigDecimal(2, clientCode);
            stmt.execute();
            rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setWebclientCode(rst.getBigDecimal(1));
                client2.setProductCode1(rst.getBigDecimal(2));
                client2.setClientName(rst.getString(3));
                client2.setWebproductDesc(rst.getString(4));
                client2.setUserCode(rst.getString(5));
                client2.setUserName1(rst.getString(6));
                client2.setDrLimitAmount(rst.getBigDecimal(7));
                client2.setCrLimitAmount(rst.getBigDecimal(8));
                client2.setPolUse(rst.getString(9));
                client2.setEndosUse(rst.getString(10));
                client2.setPRO_DESC(rst.getString(11));
                client2.setClnaCode(rst.getBigDecimal(12));
                client2.setClnaShtDesc(rst.getString(13)); 
                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return client2List;
    }

    public List<Client2> findClientAccount() {
        BigDecimal clientCode;
        List<Client2> client2List = null;

        DBConnector OracleConnection = new DBConnector();

        String query =
            "begin  ? :=TQC_SETUPS_CURSOR.getClientAccount(?) ;end;";


        //  String query =
        //    "begin  ? :=TQC_clients_pkg.get_client_accounts(?) ;end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        OracleResultSet rst = null;

        client2List = new ArrayList<Client2>();
        if (session.getAttribute("ClientCode") != null) {
            clientCode =
                    new BigDecimal(session.getAttribute("ClientCode").toString());
        } else {
            clientCode = null;
        }

        try {
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setBigDecimal(2, clientCode);
            stmt.execute();
            rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setClnaCode(rst.getBigDecimal(1));
                client2.setClnaShtDesc(rst.getString(2));
                client2.setClnaName(rst.getString(3));
                client2.setClnaRamarks(rst.getString(5));
                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return client2List;
    }

    public List<Client2> findAgencyAccount() {
        BigDecimal clientCode;
        List<Client2> client2List = null;

        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin  ? :=TQC_SETUPS_CURSOR.getClientAccount(?) ;end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        OracleResultSet rst = null;

        client2List = new ArrayList<Client2>();
        if (session.getAttribute("ClientCode") != null) {
            clientCode =
                    new BigDecimal(session.getAttribute("ClientCode").toString());
        } else {
            clientCode = null;
        }

        try {
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setBigDecimal(2, clientCode);
            stmt.execute();
            rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setClnaCode(rst.getBigDecimal(1));
                client2.setClnaShtDesc(rst.getString(2));
                client2.setClnaName(rst.getString(3));
                client2.setClnaRamarks(rst.getString(5));
                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return client2List;
    }

    public List<Client2> findHoldingCompanySubsidiary() {
        BigDecimal clientCode;
        List<Client2> client2List = null;

        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin  ? :=TQC_SETUPS_CURSOR.getallsubsidiary(?) ;end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        OracleResultSet rst = null;

        client2List = new ArrayList<Client2>();
        if (session.getAttribute("ClientCode") != null) {
            clientCode =
                    new BigDecimal(session.getAttribute("ClientCode").toString());
        } else {
            clientCode = null;
        }

        try {
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setBigDecimal(2, clientCode);
            stmt.execute();
            rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setClientNameEnt(rst.getString(1));
                client2.setClientStatus(rst.getString(2));
                client2.setClntShtDesc(rst.getString(3));
                client2.setClientPhysicalAddress(rst.getString(4));
                client2.setClientPostalAddress(rst.getString(5));
                client2.setEmailAddress(rst.getString(6));
                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return client2List;
    }

    public List<ClientTitle> fetchAllBussinessPeople() {

        List<ClientTitle> titlesList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ?:= TQC_SETUPS_CURSOR.get_bussiness_person(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            titlesList = new ArrayList<ClientTitle>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);
            BigDecimal clientCode =
                GlobalCC.checkBDNullValues(session.getAttribute("ClientCode"));

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setObject(2, session.getAttribute("PayeeType"));
            statement.setObject(3, clientCode);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a ClientTitle object with the
                // values and add it to the list
                ClientTitle title = new ClientTitle();
                title.setBpnCode(resultSet.getBigDecimal(1));
                title.setBpnIdNumber(resultSet.getString(2));
                title.setBpnAddress(resultSet.getString(3));
                title.setBpnTelNumber(resultSet.getString(4));
                title.setBpnMobileNumber(resultSet.getString(5));
                title.setBpnEmail(resultSet.getString(6));
                title.setBpnType(resultSet.getString(7));
                title.setBpnZip(resultSet.getString(8));
                title.setBpnTown(resultSet.getString(9));
                title.setBpnCountryCode(resultSet.getBigDecimal(10));
                title.setBpnName(resultSet.getString(11));
                title.setBpnPin(resultSet.getString(12));
                title.setBpnBbrCode(resultSet.getBigDecimal(13));
                title.setBpnBankAccount(resultSet.getString(14));
                title.setBpnBbrSwiftCode(resultSet.getBigDecimal(15));
                title.setBpnRegClmtCode(resultSet.getBigDecimal(16));
                title.setBpnBranchName(resultSet.getString(17));
                title.setBpnCountryName(resultSet.getString(18));
                title.setBpnRegClaimant(resultSet.getString(19));

                titlesList.add(title);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return titlesList;
    }

    public List<Client2> findOccupation() {
        List<Client2> client2List = null;
        DBConnector OracleConnection = new DBConnector();

        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        OracleResultSet rst = null;

        client2List = new ArrayList<Client2>();
        String query =
            "    SELECT occ_code, occ_sec_code, occ_sht_desc, occ_name\n" +
            "                     FROM  tqc_occupations\n" +
            "                    WHERE OCC_CODE IN (SELECT OCC_CODE FROM tqc_sector_occupations so WHERE so.OCC_SEC_CODE = :v_sect_code)\n" +
            "         ORDER BY occ_name";
        try {
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            BigDecimal sectCode =
                GlobalCC.checkBDNullValues(session.getAttribute("sectorCode"));
            query =
                    query.replaceAll(":v_sect_code", sectCode != null ? sectCode.toString() :
                                                     "null");
            // System.out.println("query: "+query);
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            rst = (OracleResultSet)stmt.executeQuery();
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setOccCode(rst.getBigDecimal(1));
                client2.setOccSecCode(rst.getBigDecimal(2));
                client2.setOccShtDesc(rst.getString(3));
                client2.setOccName(rst.getString(4));
                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            System.err.println("Unable to execute: " + query);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return client2List;
    }
    
    public List<Client2> findSignatoryOccupation() {
        List<Client2> client2List = null;
        DBConnector OracleConnection = new DBConnector();

        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        OracleResultSet rst = null;

        client2List = new ArrayList<Client2>();
        String query =
            "   SELECT occ_code, occ_sec_code, occ_sht_desc, occ_name\n" + 
            "                                 FROM  tqc_occupations\n" + 
            "                            \n" + 
            "                     ORDER BY occ_name";
        try {
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            BigDecimal sectCode =
                GlobalCC.checkBDNullValues(session.getAttribute("sectorCode"));
            query =
                    query.replaceAll(":v_sect_code", sectCode != null ? sectCode.toString() :
                                                     "null");
            // System.out.println("query: "+query);
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            rst = (OracleResultSet)stmt.executeQuery();
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setOccCode(rst.getBigDecimal(1));
                client2.setOccSecCode(rst.getBigDecimal(2));
                client2.setOccShtDesc(rst.getString(3));
                client2.setOccName(rst.getString(4));
                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            System.err.println("Unable to execute: " + query);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return client2List;
    }

    public List<Client2> findSpecializations() {
        List<Client2> client2List = null;
        DBConnector OracleConnection = new DBConnector();
        String query =
            "select spz_code,spz_sht_desc, spz_name ,spz_occ_code from  tqc_specializations" +
            " where spz_occ_code = ?";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        OracleResultSet rst = null;

        client2List = new ArrayList<Client2>();
        try {
            if (!GlobalCC.tableExists("tqc_specializations")) {
                return null;
            }
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.setBigDecimal(1, (BigDecimal)session.getAttribute("occCode"));

            rst = (OracleResultSet)stmt.executeQuery();
            ;
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setSPZ_CODE(rst.getBigDecimal(1));
                client2.setSPZ_SHT_DESC(rst.getString(2));
                client2.setSPZ_NAME(rst.getString(3));
                client2.setSPZ_OCC_CODE(rst.getString(4));
                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return client2List;
    }

    public List<Client2> fetchAllClientsByPin() {
        Integer count = 0;
        //counts no of record

        List<Client2> clientsList = null;
        DBConnector dbConnector = new DBConnector();

        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        String otherName = null;
        try {
            clientsList = new ArrayList<Client2>();
            String pinNo =
                GlobalCC.checkNullValues(session.getAttribute("pinNumber"));
            if (pinNo != null) {
                String query =
                    "SELECT clnt_code,nvl(clnt_name,clnt_surname||' - '||clnt_other_names) clnt_name,\n" +
                    "              clnt_pin \n" +
                    "           FROM tqc_clients\n" +
                    "          WHERE (       UPPER (clnt_pin) LIKE\n" +
                    "                               '%' || UPPER (NVL (:v_clnt_pin, 'HAKUNA'))\n" +
                    "                               || '%'\n" +
                    "                )";
                connection =
                        (OracleConnection)dbConnector.getDatabaseConnection();
                query =
                        query.replaceAll(":v_clnt_pin", pinNo != null ? "'" + pinNo +
                                                        "'" : "null");

                System.out.println("query: " + query);
                statement =
                        (OracleCallableStatement)connection.prepareCall(query);

                OracleResultSet resultSet =
                    (OracleResultSet)statement.executeQuery();
                count = 0;
                String clientCode = null;
                while (resultSet.next()) {
                    // For every row, create a Client object with the
                    // values and add it to the list
                    count = count + 1;
                    Client2 client = new Client2();
                    client.setCode(resultSet.getString(1));
                    client.setName(resultSet.getString(2));
                    client.setPin(resultSet.getString(3));

                    clientCode = GlobalCC.checkNullValues(client.getCode());

                    clientsList.add(client);
                }
                if (count == 1 && clientCode != null) {
                    session.setAttribute("ClientCode", clientCode);
                } 

                resultSet.close();
                statement.close();
                connection.close();
            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        session.setAttribute("count", count);
        return clientsList;
    }


    public List<ClientEmployer> fetchClientEmployers() {
        BigDecimal clientCode =
            GlobalCC.checkBDNullValues(session.getAttribute("ClientCode"));
        List<ClientEmployer> clientEmployers = new ArrayList<ClientEmployer>();
        String query =
            "begin ? := tqc_clients_pkg.fetch_client_employers(?); end;";

        DBConnector dbHandler = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cstmt = null;

        if (clientCode == null) {
            return clientEmployers;
        }

        try {
            conn = dbHandler.getDatabaseConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(query);

            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.setBigDecimal(2, clientCode);
            cstmt.execute();

            OracleResultSet rs = (OracleResultSet)cstmt.getObject(1);
            while (rs.next()) {
                ClientEmployer ce = new ClientEmployer();
                ce.setCode(rs.getBigDecimal("CODE"));
                ce.setClntCode(rs.getBigDecimal("CLNT_CODE"));
                ce.setCountryCode(rs.getBigDecimal("COU_CODE"));
                ce.setCountryName(rs.getString("COU_NAME"));
                ce.setStateCode(rs.getBigDecimal("STATE_CODE"));
                ce.setStateName(rs.getString("STS_NAME"));
                ce.setTownCode(rs.getBigDecimal("TOWN_CODE"));
                ce.setTownName(rs.getString("TWN_NAME"));
                // ce.setPostalCode(rs.getBigDecimal("PST_CODE"));
                ce.setPostalZipCode(rs.getString("PST_ZIP_CODE"));
                ce.setSectorCode(rs.getBigDecimal("SECTOR_CODE"));
                ce.setSectorName(rs.getString("SEC_NAME"));
                ce.setEmployerType(rs.getString("EMPLOYER_TYPE"));
                ce.setName(rs.getString("EMPLOYER_NAME"));
                ce.setPayrollNo(rs.getString("PAYROLL_NO"));
                ce.setMinSalary(rs.getBigDecimal("MIN_SALARY"));
                ce.setMaxSalary(rs.getBigDecimal("MAX_SALARY"));
                ce.setMonthlyIncome(rs.getBigDecimal("MONTHLY_INCOME"));
                ce.setEmploymentDate(rs.getDate("EMPLOYMENT_DATE"));
                ce.setEmployerNos(rs.getString("EMPLOYER_NOS"));
                ce.setEmployerNos(rs.getString("EMPLOYER_CELLS"));
                ce.setFax(rs.getString("EMPLOYER_FAX"));

                clientEmployers.add(ce);
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return clientEmployers;
    }


    public List<ClientEmployerContact> fetchClientEmployerContacts() {
        BigDecimal clientCode =
            GlobalCC.checkBDNullValues(session.getAttribute("ClientCode"));
        BigDecimal employerCode =
            GlobalCC.checkBDNullValues(session.getAttribute("EmployerCode"));
        List<ClientEmployerContact> employerContact =
            new ArrayList<ClientEmployerContact>();
        String query =
            "begin ? := tqc_clients_pkg.fetch_client_employers(?); end;";

        return employerContact;
    }
    public List<ClientSignatory> findClientsSignatories() {
         
        List<ClientSignatory> signatories = new ArrayList<ClientSignatory>(); 
        Session DbSess = HibernateUtil.getSession();
        Transaction tx = null;
             
         try {
            BigDecimal clientCode = GlobalCC.checkBDNullValues(session.getAttribute("ClientCode"));
            if(clientCode==null){
                 return signatories;
            }
            String query = "FROM ClientSignatory S WHERE S.clientCode = "+clientCode;
            tx = DbSess.beginTransaction(); 
            signatories = DbSess.createQuery(query).list(); 
            for (Iterator iterator = signatories.iterator(); iterator.hasNext();){
               ClientSignatory signatory = (ClientSignatory) iterator.next(); 
               System.out.println(signatory);  
            }
            tx.commit();
         } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            GlobalCC.EXCEPTIONREPORTING(e);
         } finally {
            DbSess.close(); 
         }
        return signatories;
    }
    //-----------start dao's findClientDirectors --------------------//
    
    
    //find fetch sources of  Income 
    
    public List<IncomeSources> fetchIncomeSources() {
         
        List<IncomeSources> incomeSources = new ArrayList<IncomeSources>(); 
        Session DbSess = HibernateUtil.getSession();
        Transaction tx = null;
             
         try {
            BigDecimal clientCode = GlobalCC.checkBDNullValues(session.getAttribute("ClientCode"));
            if(clientCode==null){
                 return incomeSources;
            }
            String query = "FROM IncomeSources S WHERE S.clientCode = "+clientCode;
            tx = DbSess.beginTransaction(); 
            incomeSources = DbSess.createQuery(query).list(); 
            for (Iterator iterator = incomeSources.iterator(); iterator.hasNext();){
               IncomeSources incomeSource = (IncomeSources) iterator.next(); 
               System.out.println(incomeSource);  
            }
            tx.commit();
         } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            GlobalCC.EXCEPTIONREPORTING(e);
         } finally {
            DbSess.close(); 
         }
        return incomeSources;
    }
    
    
  public      List<ClientDirector> findClientDirectors(){
            List<ClientDirector> directors =  new ArrayList<ClientDirector>(); 
            Session dbSess = HibernateUtil.getSession();
            Transaction tx = null;

             try {
                 BigDecimal clientCode = GlobalCC.checkBDNullValues(session.getAttribute("ClientCode"));
                 if(clientCode==null){
                      return directors;
                 }
                String query = "FROM ClientDirector S WHERE S.clientCode = "+clientCode;
                tx = dbSess.beginTransaction(); 
                directors = dbSess.createQuery(query).list(); 
                for (Iterator iterator = directors.iterator(); iterator.hasNext();){
                   ClientDirector item = (ClientDirector) iterator.next(); 
                   System.out.println(item);  
                }
                tx.commit();
             } catch (HibernateException e) {
                if (tx!=null) tx.rollback();
                GlobalCC.EXCEPTIONREPORTING(e);
             } finally {
                dbSess.close(); 
             }
            return directors;
        }
    //-----------end dao's findClientDirectors --------------------//
    public Boolean actionSaveClientDirector(String action,BigDecimal clientCode,ClientDirector val,BigDecimal directorCode) {
        Session dbSess = HibernateUtil.getSession();
        Transaction tx = null;  Boolean success=false;
        try {
            tx = dbSess.beginTransaction();
            ClientDirector item = new ClientDirector(); 
 
            if ("Edit".equals(action)) { //Edit
                item = (ClientDirector)dbSess.get(ClientDirector.class, directorCode);
            }
            
            Calendar now = Calendar.getInstance();
            
            final String year = val.getYear() != null ?
                val.getYear() : "" + now.get(Calendar.YEAR);

            //-----------set ClientDirector------------------//  
            item.setCode(directorCode); 
            item.setTitle(val.getTitle());
            item.setName(val.getName());
            item.setSourceOfIncome(val.getSourceOfIncome());
            item.setOccupation(val.getOccupation());
            item.setGender(val.getGender());
            item.setNationality(val.getNationality());
            item.setDateOfBirth(val.getDateOfBirth());
            item.setPlaceOfBirth(val.getPlaceOfBirth());
            item.setPhoneNo(val.getPhoneNo());
            item.setMeansOfId(val.getMeansOfId());
            item.setMeansOfIdVal(val.getMeansOfIdVal());
            item.setTaxNo(val.getTaxNo());
            item.setEmail(val.getEmail());
            item.setAddress(val.getAddress());
            item.setYear(year);
            item.setQualifications(val.getQualifications());
            item.setPctHoldg(val.getPctHoldg());
            item.setDesignation(val.getDesignation());  
            item.setPobCountry(val.getPobCountry());  
            item.setPobState(val.getPobState());  
            item.setPobTown(val.getPobTown());            
            item.setClientCode(val.getClientCode());
            
            if ("Edit".equals(action)) { //Edit
                dbSess.update(item);
            } else {
                dbSess.save(item);
            }
            tx.commit(); 
            success=true;
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            dbSess.close();
        } 
        return success;
    }
       //-----------start dao's findClients --------------------//
        public List<TurnQuest.view.models.Client> findClients(){
                    List<TurnQuest.view.models.Client> clients = null; 
            Session dbSess = HibernateUtil.getSession();
            Transaction tx = null;

             try {
                String query = "FROM Client S";
                tx = dbSess.beginTransaction(); 
                clients = dbSess.createQuery(query).list(); 
                for (Iterator iterator = clients.iterator(); iterator.hasNext();){
                   TurnQuest.view.models.Client item = (TurnQuest.view.models.Client) iterator.next(); 
                   System.out.println(item);  
                }
                tx.commit();
             } catch (HibernateException e) {
                if (tx!=null) tx.rollback();
                GlobalCC.EXCEPTIONREPORTING(e);
             } finally {
                dbSess.close(); 
             }
            return clients;
        }
    //-----------end dao's findClients --------------------//
        
    //-----------start dao's fetchClient --------------------//
     public TurnQuest.view.models.Client fetchClientByClientCode(BigDecimal clientCode){
         TurnQuest.view.models.Client client = null; 
         Session dbSess = HibernateUtil.getSession();
         Transaction tx = null; 
          try { 
             tx = dbSess.beginTransaction(); 
             client = (TurnQuest.view.models.Client)dbSess.get(TurnQuest.view.models.Client.class, clientCode);
             tx.commit();
          } catch (HibernateException e) { 
             GlobalCC.EXCEPTIONREPORTING(e);
          } finally {
             dbSess.close(); 
          }
         return client;
     }
    //-----------end dao's findClients --------------------//
}
