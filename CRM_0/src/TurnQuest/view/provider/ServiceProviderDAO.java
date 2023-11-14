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

package TurnQuest.view.provider;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.setups.ServiceProviderType;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;


/**
 * This class serves as the main DAO class for retriving the necessary objects
 * from the database relating to Service Providers.
 *
 * @author Frankline Ogongi
 */
public class ServiceProviderDAO {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    /**
     * Default Class Constructor
     */
    public ServiceProviderDAO() {
        super();
    }

    /**
     * Fetches all <code>ServiceProvider</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>ServiceProvider</code> objects/records
     * fetched from the database.
     */
    public List<ServiceProvider> fetchAllServiceProviders() {

        List<ServiceProvider> providersList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_service_providers(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            providersList = new ArrayList<ServiceProvider>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            String serviceProviderCode =GlobalCC.checkNullValues(session.getAttribute("serviceProviderTypeCode"));
            if (serviceProviderCode == null || serviceProviderCode == "") {
                return null;
            } else if (serviceProviderCode != null ||
                       serviceProviderCode != "") {

                statement.setString(1, serviceProviderCode);
                statement.registerOutParameter(2, OracleTypes.CURSOR);
                statement.execute();
                ResultSet resultSet = (ResultSet)statement.getObject(2);

                while (resultSet.next()) {
                    // For every row, create a ServiceProvider object with the
                    // values and add it to the list
                    ServiceProvider provider = new ServiceProvider();

                    provider.setCode(resultSet.getString(1));
                    provider.setShortDesc(resultSet.getString(2));
                    provider.setName(resultSet.getString(3));
                    provider.setPhysicalAddress(resultSet.getString(4));
                    provider.setPostalAddress(resultSet.getString(5));
                    provider.setTownCode(resultSet.getString(6));
                    provider.setCountryCode(resultSet.getString(7));
                    provider.setProviderTypeCode(resultSet.getString(8));
                    provider.setPhone(resultSet.getString(9));
                    provider.setFax(resultSet.getString(10));
                    provider.setEmail(resultSet.getString(11));
                    provider.setTitle(resultSet.getString(12));
                    //provider.setZip(resultSet.getString(13));
                    provider.setZip(resultSet.getString(47));
                    provider.setWef(resultSet.getDate(14));
                    provider.setWet(resultSet.getDate(15));
                    provider.setContact(resultSet.getString(16));
                    provider.setAimsCode(resultSet.getString(17));
                    provider.setBankBranchCode(resultSet.getString(18));
                    provider.setBankAccNo(resultSet.getString(19));
                    provider.setCreatedBy(resultSet.getString(20));
                    provider.setDateCreated(resultSet.getDate(21));
                    provider.setStatusRemarks(resultSet.getString(22));
                    provider.setStatus(resultSet.getString(23));
                    provider.setPINNumber(resultSet.getString(24));
                    provider.setTrsOccupation(resultSet.getString(25));
                    provider.setProffBody(resultSet.getString(26));
                    provider.setPIN(resultSet.getString(27));
                    provider.setDocPhone(resultSet.getString(28));
                    provider.setDocEmail(resultSet.getString(29));
                    provider.setCountryName(resultSet.getString(30));
                    provider.setTownName(resultSet.getString(31));
                    provider.setBankCode(resultSet.getBigDecimal(32));
                    provider.setBankName(resultSet.getString(33));
                    provider.setBankBranchName(resultSet.getString(34));
                    provider.setInhouse(resultSet.getString(35));
                    provider.setSmsNumber(resultSet.getString(36));
                    provider.setInvoiceNumber(resultSet.getString(37));
                    provider.setClientCode(resultSet.getBigDecimal(38));
                    provider.setClientName(resultSet.getString(39));
                    provider.setContactPhone(resultSet.getString(42));
                    provider.setContactName(resultSet.getString(43));
                    provider.setContactEmail(resultSet.getString(44));
                    provider.setContactTel(resultSet.getString(45));
                    provider.setSprBpnCode(resultSet.getBigDecimal(40));
                    provider.setBpnName(resultSet.getString(41));
                    provider.setTelPay(resultSet.getString(46));
                    provider.setRegNo(resultSet.getString("spr_reg_no"));
                    provider.setPostalcode(resultSet.getString("spr_postal_code"));
                    provider.setIdType(resultSet.getString("spr_id_type"));
                    provider.setIdNo(resultSet.getString("spr_id_no"));
                    providersList.add(provider);
                }

                resultSet.close();
                statement.close();
                connection.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return providersList;
    }

    /**
     * Fetches all <code>ServiceProviderType</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>ServiceProviderType</code> objects/records
     * fetched from the database.
     */
    public List<ServiceProviderType> fetchAllServiceProviderTypes() {

        List<ServiceProviderType> providersList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_service_provider_types(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            providersList = new ArrayList<ServiceProviderType>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a ServiceProviderType object with the
                // values and add it to the list
                ServiceProviderType provider = new ServiceProviderType();
                provider.setSptCode(resultSet.getBigDecimal(1));
                provider.setCode(GlobalCC.checkNullValues(resultSet.getBigDecimal(1)));
                provider.setShortDesc(resultSet.getString(2));
                provider.setName(resultSet.getString(3));
                provider.setStatus(resultSet.getString(4));
                provider.setWhtxRate(resultSet.getString(5));
                provider.setVatRate(resultSet.getString(6));
                providersList.add(provider);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return providersList;
    }


    /**
     * Fetches all <code>ServiceProviderActivity</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>ServiceProviderActivity</code> objects/records
     * fetched from the database.
     */
    public List<ServiceProviderActivity> fetchAllServiceProviderActivities() {

        List<ServiceProviderActivity> providerActivityList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_service_prov_activities(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            providerActivityList = new ArrayList<ServiceProviderActivity>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a ServiceProviderActivity object with the
                // values and add it to the list
                ServiceProviderActivity activity =
                    new ServiceProviderActivity();

                activity.setCode(resultSet.getString(1));
                activity.setProviderTypeCode(resultSet.getString(2));
                activity.setProviderTypeShortDesc(resultSet.getString(3));
                activity.setServiceProviderCode(resultSet.getString(4));
                activity.setServiceProviderShortDesc(resultSet.getString(5));
                activity.setMainAct(resultSet.getString(6));
                activity.setDescription(resultSet.getString(7));

                providerActivityList.add(activity);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return providerActivityList;
    }

    /**
     * Fetches all <code>ServiceProviderActivity</code> objects/records from the
     * database and returns them in a list based on a given provider.
     *
     * @return A list of <code>ServiceProviderActivity</code> objects/records
     * fetched from the database.
     */
    
    
    public List<ServiceProviderActivity> fetchActivitiesByProvider() {

        List<ServiceProviderActivity> providerActivityList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_activities_by_provider(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            providerActivityList = new ArrayList<ServiceProviderActivity>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("serviceProviderCode") != null) {
                statement.setString(1,
                                    (String)session.getAttribute("serviceProviderCode"));
            } else {
                statement.setString(1, "");
            }

            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(2);

            while (resultSet.next()) {
                // For every row, create a ServiceProviderActivity object with the
                // values and add it to the list
                ServiceProviderActivity activity =
                    new ServiceProviderActivity();

                activity.setCode(resultSet.getString(1));
                activity.setProviderTypeCode(resultSet.getString(2));
                activity.setProviderTypeShortDesc(resultSet.getString(3));
                activity.setServiceProviderCode(resultSet.getString(4));
                activity.setServiceProviderShortDesc(resultSet.getString(5));
                activity.setMainAct(resultSet.getString(6));
                activity.setDescription(resultSet.getString(7));
                activity.setProviderTypeName(resultSet.getString(8));
                activity.setProviderName(resultSet.getString(9));

                providerActivityList.add(activity);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return providerActivityList;
    }



    public List<ServiceProvider> fetchProviderContacts() {

            List<ServiceProvider> providerContactList = null;
            DBConnector dbConnector = new DBConnector();
            String query =
                "begin TQC_SETUPS_CURSOR.provider_contacts_prc(?,?);end;";
            CallableStatement statement = null;
            OracleConnection connection = null;

            try {
                providerContactList = new ArrayList<ServiceProvider>();
                connection = dbConnector.getDatabaseConnection();
                statement = connection.prepareCall(query);

                if (session.getAttribute("serviceProviderCode") != null) {
                    statement.setString(1,
                                        (String)session.getAttribute("serviceProviderCode"));
                } else {
                    statement.setString(1, "");
                }

                statement.registerOutParameter(2, OracleTypes.CURSOR);
                statement.execute();
                ResultSet resultSet = (ResultSet)statement.getObject(2);

                while (resultSet.next()) {
                    // For every row, create a ServiceProvider object with the
                    // values and add it to the list
                    ServiceProvider provider =
                        new ServiceProvider();

                    provider.setProvCode(resultSet.getString(1));
                    provider.setProvContacCode(resultSet.getString(2));
                    provider.setProvName(resultSet.getString(3));
                    provider.setProvTitle(resultSet.getString(4));
                    provider.setProvOfficeTelNo(resultSet.getString(5));
                    provider.setProvMobileNo(resultSet.getString(6));
                    provider.setProvEmail(resultSet.getString(7));
                    
                    
                  

                    providerContactList.add(provider);
                }

                resultSet.close();
                statement.close();
                connection.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }

            return providerContactList;
        }



    /**
     * Fetches all <code>System</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>System</code> objects/records
     * fetched from the database.
     */
    public List<System> fetchAllSystems() {

        List<System> systemsList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_systems(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            systemsList = new ArrayList<System>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a System object with the
                // values and add it to the list
                System system = new System();

                system.setCode(resultSet.getString(1));
                system.setShortDesc(resultSet.getString(2));
                system.setName(resultSet.getString(3));
                system.setMainFormName(resultSet.getString(4));
                system.setActive(resultSet.getString(5));
                system.setDbUsername(resultSet.getString(6));
                system.setDbPassword(resultSet.getString(7));
                system.setDbString(resultSet.getString(8));
                system.setPath(resultSet.getString(9));
                system.setOrgCode(resultSet.getString(10));
                system.setAgentMainFormName(resultSet.getString(11));
                system.setKbaCode(resultSet.getString(12));
                system.setSignaturePath(resultSet.getString(13));

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

    public List<System> fetchallBranches() {

        List<System> systemsList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.all_branches(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            systemsList = new ArrayList<System>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a System object with the
                // values and add it to the list
                System system = new System();

                system.setBrn_code(resultSet.getString(1));
                system.setBrn_sht_desc(resultSet.getString(2));
                system.setBrn_reg_code(resultSet.getString(3));
                system.setBrn_name(resultSet.getString(4));
                system.setBrn_phy_addrs(resultSet.getString(5));
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

    public List<System> fetchbankBranches() {

        List<System> systemsList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.allbankbranches(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            systemsList = new ArrayList<System>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a System object with the
                // values and add it to the list
                System system = new System();

                system.setV_bbr_code(resultSet.getString(1));
                system.setV_bbr_branch_name(resultSet.getString(4));
                system.setV_bbr_ref_code(resultSet.getString(6));
                system.setV_bbr_sht_desc(resultSet.getString(5));
                system.setV_bbr_bnk_code(resultSet.getString(2));
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

    public List<System> fetchProviderAssignedSystems() {

        List<System> systemsList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_prov_assigned_systems(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            systemsList = new ArrayList<System>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            String providerCode =
                (String)session.getAttribute("serviceProviderCode");
            if (providerCode == null) {
                return null;
            } else {
                statement.setBigDecimal(1, new BigDecimal(providerCode));
                statement.registerOutParameter(2, OracleTypes.CURSOR);
                statement.execute();
                ResultSet resultSet = (ResultSet)statement.getObject(2);

                while (resultSet.next()) {
                    // For every row, create a System object with the
                    // values and add it to the list
                    System system = new System();

                    system.setCode(resultSet.getString(1));
                    system.setShortDesc(resultSet.getString(2));
                    system.setName(resultSet.getString(3));
                    system.setMainFormName(resultSet.getString(4));
                    system.setActive(resultSet.getString(5));
                    system.setDbUsername(resultSet.getString(6));
                    system.setDbPassword(resultSet.getString(7));
                    system.setDbString(resultSet.getString(8));
                    system.setPath(resultSet.getString(9));
                    system.setOrgCode(resultSet.getString(10));
                    system.setAgentMainFormName(resultSet.getString(11));
                    system.setKbaCode(resultSet.getString(12));
                    system.setSignaturePath(resultSet.getString(13));

                    systemsList.add(system);
                }

                resultSet.close();
                statement.close();
                connection.close();
            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return systemsList;
    }

    public List<System> fetchProviderUnassignedSystems() {

        List<System> systemsList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_prov_unassigned_systems(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            systemsList = new ArrayList<System>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            String providerCode =
                (String)session.getAttribute("serviceProviderCode");
            if (providerCode == null) {
                return null;
            } else {
                statement.setBigDecimal(1, new BigDecimal(providerCode));
                statement.registerOutParameter(2, OracleTypes.CURSOR);
                statement.execute();
                ResultSet resultSet = (ResultSet)statement.getObject(2);

                while (resultSet.next()) {
                    // For every row, create a System object with the
                    // values and add it to the list
                    System system = new System();

                    system.setCode(resultSet.getString(1));
                    system.setShortDesc(resultSet.getString(2));
                    system.setName(resultSet.getString(3));
                    system.setMainFormName(resultSet.getString(4));
                    system.setActive(resultSet.getString(5));
                    system.setDbUsername(resultSet.getString(6));
                    system.setDbPassword(resultSet.getString(7));
                    system.setDbString(resultSet.getString(8));
                    system.setPath(resultSet.getString(9));
                    system.setOrgCode(resultSet.getString(10));
                    system.setAgentMainFormName(resultSet.getString(11));
                    system.setKbaCode(resultSet.getString(12));
                    system.setSignaturePath(resultSet.getString(13));

                    systemsList.add(system);
                }

                resultSet.close();
                statement.close();
                connection.close();
            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return systemsList;
    }

    /**
     * Fetches all <code>ServiceProviderSystem</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>ServiceProviderSystem</code> objects/records
     * fetched from the database.
     */
    public List<ServiceProviderSystem> fetchAllServiceProviderSystems() {

        List<ServiceProviderSystem> provSystemsList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_service_prov_systems(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            provSystemsList = new ArrayList<ServiceProviderSystem>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a ServiceProviderSystem object with the
                // values and add it to the list
                ServiceProviderSystem system = new ServiceProviderSystem();

                system.setCode(resultSet.getString(1));
                system.setServiceProviderCode(resultSet.getString(2));
                system.setSystemCode(resultSet.getString(3));
                system.setWef(resultSet.getDate(4));
                system.setWet(resultSet.getDate(5));
                system.setCreatedBy(resultSet.getString(6));

                provSystemsList.add(system);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return provSystemsList;
    }

    public List<ServiceProvider> fetchAlLMobileTypePrefix() {
        List<ServiceProvider> prefixList = new ArrayList<ServiceProvider>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_SETUPS_CURSOR.getMobPrefixes(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            BigDecimal countryCode=GlobalCC.checkBDNullValues(session.getAttribute("GsmCountryCode"));
            java.lang.System.out.println("GsmCountryCode: "+countryCode);
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query); 
            statement.registerOutParameter(1, OracleTypes.CURSOR); 
            statement.setObject(2,countryCode);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);
            List prefixes = new ArrayList();
            while (resultSet.next()) {
                ServiceProvider serviceProv = new ServiceProvider();
                serviceProv.setMptpCode(resultSet.getBigDecimal(1));
                serviceProv.setPrefix(resultSet.getString(2));
                serviceProv.setMptCode(resultSet.getBigDecimal(3));
                prefixes.add(resultSet.getString(2));
                prefixList.add(serviceProv);

            }
            session.setAttribute("mobilePrefix", prefixes);
            prefixes = null;
            statement.close();
            resultSet.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(connection, e);
            return null;
        }


        return prefixList;
    }

    public List<ServiceProvider> fetchAlLMobileTypePrefix2() {
        List<ServiceProvider> prefixList = new ArrayList<ServiceProvider>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_SETUPS_CURSOR.getMobPrefixes(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            if (session.getAttribute("countryCode") == null) {
                statement.setObject(2, session.getAttribute("COUNTRY_CODE"));
            } else {
                statement.setObject(2, session.getAttribute("countryCode"));
            }

            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);
            List prefixes = new ArrayList();
            while (resultSet.next()) {
                ServiceProvider serviceProv = new ServiceProvider();
                serviceProv.setMptpCode(resultSet.getBigDecimal(1));
                serviceProv.setPrefix(resultSet.getString(2));
                serviceProv.setMptCode(resultSet.getBigDecimal(3));
                prefixes.add(resultSet.getString(2));
                prefixList.add(serviceProv);

            }
            session.setAttribute("mobilePrefix", prefixes);
            prefixes = null;
            statement.close();
            resultSet.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(connection, e);
            return null;
        }


        return prefixList;
    }
}
