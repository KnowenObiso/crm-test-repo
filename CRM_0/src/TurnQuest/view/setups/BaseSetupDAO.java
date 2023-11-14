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

package TurnQuest.view.setups;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.provider.System;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;


/**
 * This class serves as the main DAO class for retriving the necessary objects
 * from the database in the setups section/menu.
 *
 * @author Frankline Ogongi
 */
public class BaseSetupDAO {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    /**
     * Default Constructor
     */
    public BaseSetupDAO() {
        super();
    }

    /**
     * Fetch from the database a list of <code>AgencyHoldingCompany</code>
     * records.
     *
     * @return A list of all <code>AgencyHoldingCompany</code> objects/records
     */
    public List<AgencyHoldingCompany> fetchAllAgencyHoldingCompanies() {

        List<AgencyHoldingCompany> holdingCompaniesList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_agency_holding_company(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            holdingCompaniesList = new ArrayList<AgencyHoldingCompany>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a AgencyHoldingCompany object with
                // the values and add it to the list
                AgencyHoldingCompany company = new AgencyHoldingCompany();
                company.setCode(resultSet.getString(1));
                company.setName(resultSet.getString(2));
                company.setPostAdd(resultSet.getString(3));
                company.setPhyAdd(resultSet.getString(4));
                company.setTelNumber(resultSet.getString(5));
                company.setMobNumber(resultSet.getString(6));
                company.setContactPerson(resultSet.getString(7));
                holdingCompaniesList.add(company);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return holdingCompaniesList;
    }

    /**
     * Fetch a list of all the <code>Bank</code> objects/records
     *
     * @return A list of all <code>Bank</code> objects/records
     */
    public List<Bank> fetchAllBanks() {

        List<Bank> banksList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_banks(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            banksList = new ArrayList<Bank>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a Bank object with the values and
                // add it to the list
                Bank bank = new Bank();
                bank.setBankCode(resultSet.getString(1));
                bank.setBankName(resultSet.getString(2));
                bank.setRemarks(resultSet.getString(3));
                bank.setShortDesc(resultSet.getString(4));
                bank.setDDRCode(resultSet.getString(5));
                bank.setDDFormatDesc(resultSet.getString(6));
                bank.setForwardingBankCode(resultSet.getString(7));
                bank.setKBACode(resultSet.getString(8));
                banksList.add(bank);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return banksList;
    }

    /**
     * Fetch a list of all the <code>BankBranch</code> objects/records with a
     * given bank code.
     *
     * @return A list of all <code>BankBranch</code> objects/records
     */
    public List<BankBranch> fetchBankBranchByBankCode() {

        List<BankBranch> bankBranchesList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_bank_branches_by_bank_code(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            bankBranchesList = new ArrayList<BankBranch>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("bankCode") != null) {
                statement.setString(1,
                                    (String)session.getAttribute("bankCode"));
            } else {
                statement.setString(1, "");
            }

            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(2);

            while (resultSet.next()) {
                // For every row, create a BankBranch object with the values
                // and add it to the list
                BankBranch bankBranch = new BankBranch();
                bankBranch.setBranchCode(resultSet.getString(1));
                bankBranch.setBranchBankCode(resultSet.getString(2));
                bankBranch.setBranchName(resultSet.getString(3));
                bankBranch.setRemarks(resultSet.getString(4));
                bankBranch.setShortDesc(resultSet.getString(5));
                bankBranch.setRefCode(resultSet.getString(6));
                bankBranch.setEFTSupported(resultSet.getString(7));
                bankBranch.setDDSupported(resultSet.getString(8));
                bankBranch.setDateCreated(resultSet.getDate(9));
                bankBranch.setCreatedBy(resultSet.getString(10));
                bankBranch.setPhysicalAddress(resultSet.getString(11));
                bankBranch.setPostalAddress(resultSet.getString(12));
                bankBranch.setKBACode(resultSet.getString(13));
                bankBranchesList.add(bankBranch);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return bankBranchesList;
    }

    /**
     * Fetches all <code>AgencyClass</code> objects/records from the database
     * and returns them in a list.
     *
     * @return A list of <code>AgencyClass</code> objects/records fetched from
     * the database
     */
    public List<AgencyClass> fetchAllAgencyClasses() {

        List<AgencyClass> agencyClassesList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_agency_classes(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            agencyClassesList = new ArrayList<AgencyClass>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a AgencyClass object with the values
                // and add it to the list
                AgencyClass agencyClass = new AgencyClass();
                agencyClass.setCode(resultSet.getString(1));
                agencyClass.setDescription(resultSet.getString(2));
                agencyClassesList.add(agencyClass);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return agencyClassesList;
    }

    /**
     * Fetches all <code>Currency</code> objects/records from the database
     * and returns them in a list.
     *
     * @return A list of <code>Currency</code> objects/records fetched from
     * the database.
     */
    public List<Currency> fetchAllCurrencies() {

        List<Currency> currenciesList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.currencies(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            currenciesList = new ArrayList<Currency>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a Currency object with the values
                // and add it to the list
                Currency currency = new Currency();
                currency.setCode(resultSet.getString(1));
                currency.setSymbol(resultSet.getString(2));
                currency.setDescription(resultSet.getString(3));
                currency.setRound(resultSet.getString(4));
                currency.setNumWords(resultSet.getString(5));
                currency.setDecimalWord(resultSet.getString(6));
                currenciesList.add(currency);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return currenciesList;
    }

    /**
     * Fetches all <code>Currency</code> objects/records from the database
     * and returns them in a list.
     *
     * @return A list of <code>Currency</code> objects/records fetched from
     * the database.
     */
    public List<Currency> fetchCurrenciesExcludeCurrency() {


        List<Currency> currenciesList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_currencies_exclude_curr(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        if (session.getAttribute("currencyCode") != null) {
            try {
                currenciesList = new ArrayList<Currency>();
                connection = dbConnector.getDatabaseConnection();
                statement = connection.prepareCall(query);

                statement.setBigDecimal(1,
                                        new BigDecimal(session.getAttribute("currencyCode").toString()));
                statement.registerOutParameter(2, OracleTypes.CURSOR);
                statement.execute();
                ResultSet resultSet = (ResultSet)statement.getObject(2);

                while (resultSet.next()) {
                    // For every row, create a Currency object with the values
                    // and add it to the list
                    Currency currency = new Currency();
                    currency.setCode(resultSet.getString(1));
                    currency.setSymbol(resultSet.getString(2));
                    currency.setDescription(resultSet.getString(3));
                    currency.setRound(resultSet.getString(4));
                    currenciesList.add(currency);
                }

                resultSet.close();
                statement.close();
                connection.close();

            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
        }

        return currenciesList;
    }

    /**
     * Get the Currency name corresponding to the given currency code.
     *
     * @param currencyCode The Currency code to be used to match the currency
     * name
     * @return the currency name
     */
    public String getCurrencyName(String currencyCode) {
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.fetch_currency_name(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        String currencyName = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (currencyCode != null) {
                statement.registerOutParameter(1, OracleTypes.VARCHAR);
                statement.setBigDecimal(2, new BigDecimal(currencyCode));
                statement.execute();

                currencyName = statement.getString(1);

                statement.close();
                connection.close();
            } else {
                return null;
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return currencyName;
    }

    /**
     * Fetches all <code>CurrencyDenomination</code> objects/records that
     * belong to a given currency from the database and returns them in a list.
     *
     * @return A list of <code>CurrencyDenomination</code> objects/records
     * fetched from the database.
     */
    public List<CurrencyDenomination> fetchCurrencyDenominationsByCurrency() {

        List<CurrencyDenomination> denominationsList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_curr_denominations_by_code(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            denominationsList = new ArrayList<CurrencyDenomination>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("currencyCode") != null) {
                statement.setString(1,
                                    (String)session.getAttribute("currencyCode"));
            } else {
                statement.setString(1, "");
            }

            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(2);

            while (resultSet.next()) {
                // For every row, create a CurrencyDenomination object with
                // the values and add it to the list
                CurrencyDenomination currDenomination =
                    new CurrencyDenomination();
                currDenomination.setCode(resultSet.getString(1));
                currDenomination.setCurrencyCode(resultSet.getString(2));
                currDenomination.setValue(resultSet.getString(3));
                currDenomination.setName(resultSet.getString(4));
                currDenomination.setWef(resultSet.getDate(5));
                denominationsList.add(currDenomination);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return denominationsList;
    }

    /**
     * Fetches all <code>CurrencyRate</code> objects/records that
     * belong to a given currency from the database and returns them in a list.
     *
     * @return A list of <code>CurrencyRate</code> objects/records
     * fetched from the database.
     */
    public List<CurrencyRate> fetchCurrencyRatesByCurrency() {

        List<CurrencyRate> ratesList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_currency_rates_by_currency(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            ratesList = new ArrayList<CurrencyRate>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("currencyCode") != null) {
                statement.setString(1,
                                    (String)session.getAttribute("currencyCode"));
            } else {
                statement.setString(1, "");
            }

            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(2);

            while (resultSet.next()) {
                // For every row, create a CurrencyRate object with
                // the values and add it to the list
                CurrencyRate currRate = new CurrencyRate();
                currRate.setCode(resultSet.getString(1));
                currRate.setCurrencyCode(resultSet.getString(2));
                currRate.setCurrRate(resultSet.getString(3));
                currRate.setCurrDate(resultSet.getString(4));
                currRate.setBaseCurrencyCode(resultSet.getString(5));
                currRate.setCurrencyDesc(resultSet.getString(6));
                currRate.setBaseCurrencyName(getCurrencyName(resultSet.getString(2).toString()));
                ratesList.add(currRate);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return ratesList;
    }

    /**
     * Fetches all <code>Sector</code> objects/records from the database
     * and returns them in a list.
     *
     * @return A list of <code>Sector</code> objects/records fetched from
     * the database.
     */
    public List<Sector> fetchAllSectors() {

        List<Sector> sectorsList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_sectors(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            sectorsList = new ArrayList<Sector>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a Sector object with the values
                // and add it to the list
                Sector sector = new Sector();
                sector.setCode(resultSet.getString(1));
                sector.setShortDesc(resultSet.getString(2));
                sector.setName(resultSet.getString(3));
                sectorsList.add(sector);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return sectorsList;
    }


    /**
     * Fetches all <code>Sector</code> objects/records from the database
     * and returns them in a list.
     *
     * @return A list of <code>Sector</code> objects/records fetched from
     * the database.
     */
    public List<Sector> fetchAllSectorsOccups() {

        List<Sector> sectorsList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "SELECT sec_code, sec_sht_desc, sec_name, null occ_name,\n" + 
        "                         null occ_code\n" + 
        "                     FROM tqc_sectors ORDER BY sec_name";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            sectorsList = new ArrayList<Sector>();
            connection = dbConnector.getDatabaseConnection();
            //java.lang.System.out.println("query: ok: "+query);
            statement = connection.prepareCall(query);  
            ResultSet resultSet = (ResultSet)statement.executeQuery();

            while (resultSet.next()) {
                // For every row, create a Sector object with the values
                // and add it to the list
                Sector sector = new Sector();
                sector.setCode(resultSet.getString(1));
                sector.setShortDesc(resultSet.getString(2));
                sector.setName(resultSet.getString(3));
                sector.setAgentName(resultSet.getString(4));
                sector.setOccupationCode(resultSet.getBigDecimal(5));
                sectorsList.add(sector);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return sectorsList;
    }


    public List<Sector> fetchAllMarketers() {

        List<Sector> sectorsList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_SETUPS_CURSOR.getMarketers ;end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            sectorsList = new ArrayList<Sector>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                Sector sector = new Sector();
                sector.setAgentCode(resultSet.getBigDecimal(1));
                sector.setAgentAccountCode(resultSet.getBigDecimal(2));
                sector.setAgentShtDesc(resultSet.getString(3));
                sector.setAgentName(resultSet.getString(4));
                sectorsList.add(sector);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return sectorsList;
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

            statement.registerOutParameter(1,OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a ServiceProviderType object with the
                // values and add it to the list
                ServiceProviderType provider = new ServiceProviderType();
                provider.setSptCode(resultSet.getBigDecimal(1));
                provider.setShortDesc(resultSet.getString(2));
                provider.setName(resultSet.getString(3));
                provider.setStatus(resultSet.getString(4));
                provider.setWhtxRate(resultSet.getString(5));
                provider.setVatRate(resultSet.getString(6));
                provider.setSuffix(resultSet.getInt(7));
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


    public List<ServiceProviderType> fetchAllServiceProviderTypesActivities() {

        List<ServiceProviderType> providersList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_service_provider_types_act(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            BigDecimal sptCode=GlobalCC.checkBDNullValues(session.getAttribute("vSptCode"));
            providersList = new ArrayList<ServiceProviderType>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.setBigDecimal(1,sptCode);
            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(2);
            while (resultSet.next()) {
                ServiceProviderType provider = new ServiceProviderType();
                provider.setCode(resultSet.getString(1));
                provider.setShortDesc(resultSet.getString(3));
                provider.setName(resultSet.getString(4));
                provider.setSmsmessage(resultSet.getString(5));
                provider.setEmailmessage(resultSet.getString(6));
                provider.setSmsDefault(resultSet.getString(7));

                provider.setEmailDefault(resultSet.getString(8));
                provider.setSmsCode(resultSet.getBigDecimal(9));
                provider.setEmailCode(resultSet.getBigDecimal(10));
                provider.setReportDays(resultSet.getBigDecimal(11));
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


    public List<ServiceProviderType> findServActivities() {

        List<ServiceProviderType> providersList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_service_provider_types_act(?);end;";
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
                ServiceProviderType provider = new ServiceProviderType();
                provider.setCode(resultSet.getString(1));
                provider.setShortDesc(resultSet.getString(3));
                provider.setName(resultSet.getString(4));
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
     * Fetches all <code>Country</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>Country</code> objects/records
     * fetched from the database.
     */
    public List<Country> fetchAllCountries() {

        List<Country> countriesList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_countries_and_currency(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            countriesList = new ArrayList<Country>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a Country object with the
                // values and add it to the list
                Country country = new Country();
                country.setCode(resultSet.getString(1));
                country.setCouCode(resultSet.getBigDecimal(1));
                country.setShortDesc(resultSet.getString(2));
                country.setCouShortDesc(resultSet.getString(2));                
                country.setName(resultSet.getString(3));
                country.setCouName(resultSet.getString(3));
                country.setBaseCurrency(resultSet.getString(4));
                country.setNationality(resultSet.getString(5));
                country.setZipCode(resultSet.getString(6));
                country.setCurrencySymbol(resultSet.getString(7));
                country.setCurrencyDesc(resultSet.getString(8));
                country.setAdministrativeType(resultSet.getString(9));
                country.setAdminTypeMandatory(resultSet.getString(10));

                countriesList.add(country);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return countriesList;
    }
    /**
     * Fetches all <code>Country</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>Country</code> objects/records
     * fetched from the database.
     */
    public static List<Country> fetchCountries(HttpSession session) {

        List<Country> countriesList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "SELECT   cou_code, cou_sht_desc, cou_name, cou_base_curr,\n" + 
        "                  cur_desc cou_base_curr_desc, cou_nationality, cou_zip_code,\n" + 
        "                  cou_admin_reg_type, cou_admin_reg_mandatory, cou_schegen,\n" + 
        "                  cou_emb_code, cou_curr_serial, cou_mobile_prefix,\n" + 
        "                  cou_client_number\n" + 
        "             FROM tqc_countries, tqc_currencies\n" + 
        "            WHERE cou_base_curr = cur_code(+)\n" + 
        "             AND UPPER(cou_name) LIKE '%'||UPPER(NVL(:v_search,cou_name))||'%'\n" + 
        "             AND cou_schegen = NVL (:v_schengen, cou_schegen)\n" + 
        "         ORDER BY cou_name ASC";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            countriesList = new ArrayList<Country>();
            connection = dbConnector.getDatabaseConnection(); 
            query=query.replaceAll(":v_schengen", "null");
            String vSearch=GlobalCC.checkNullValues(session.getAttribute("txtSearchCountry"));
            query=query.replaceAll(":v_search", "'"+(vSearch!=null?vSearch:"")+"'"); 
            statement = connection.prepareCall(query); 
           
            ResultSet resultSet = (ResultSet) statement.executeQuery();

            while (resultSet.next()) {
                // For every row, create a Country object with the
                // values and add it to the list
                Country country = new Country();
                country.setCode(resultSet.getString(1));
                country.setShortDesc(resultSet.getString(2));
                country.setName(resultSet.getString(3));
                country.setBaseCurrency(resultSet.getString(4));
                country.setCurrencyDesc(resultSet.getString(5));
                country.setNationality(resultSet.getString(6));
                country.setZipCode(resultSet.getString(7));
                country.setAdministrativeType(resultSet.getString(8));
                country.setAdminTypeMandatory(resultSet.getString(9));
                country.setCouShengen(resultSet.getString(10));
                country.setCouEmbCode(resultSet.getString(11));
                country.setCouCurrSerial(resultSet.getString(12));
                countriesList.add(country);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
            String restore="";
            GlobalCC.executeSql(restore);
        }

        return countriesList;
    }
    public static List<Parameter> fetchTaxAuthorities() {

        List<Parameter> taxAuthorities = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.getTaxAuthorities(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            taxAuthorities = new ArrayList<Parameter>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a Parameter object with the
                // values and add it to the list
                Parameter parameter = new Parameter();
                parameter.setTaxAuthCode(resultSet.getString(1));
                parameter.setTaxAuthDesc(resultSet.getString(2));
                parameter.setTaxAuthName(resultSet.getString(3));
                taxAuthorities.add(parameter);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return taxAuthorities;
    }
    /**
     * Fetches all <code>Country</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>Country</code> objects/records
     * fetched from the database.
     */
    public List<Country> fetchAllCountriesInfo() {
        return BaseSetupDAO.fetchCountries(session);
    }
    
    public List<Country> fetchAllCountriesInfoGsm() {
        return BaseSetupDAO.fetchCountries(session);
    }
    
    public List<Country> fetchAllCountriesInfoForeign() {
        return BaseSetupDAO.fetchCountries(session);
    }
  
    /**
     * Fetches all <code>State</code> objects/records that
     * belong to a given country from the database and returns them in a list.
     *
     * @return A list of <code>State</code> objects/records
     * fetched from the database.
     */
    public List<State> fetchStatesByCountry() {
        List<State> statesList = new ArrayList<State>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.getstates(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("countryCode") != null) {
                BigDecimal countryCode =
                    new BigDecimal(session.getAttribute("countryCode").toString());

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, countryCode);
                statement.execute();
                ResultSet resultSet = (ResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    State state = new State();
                    state.setStateCode(resultSet.getBigDecimal(1));
                    state.setStateCountryCode(resultSet.getBigDecimal(2));
                    state.setStateShortDesc(resultSet.getString(3));
                    state.setStateName(resultSet.getString(4));
                    statesList.add(state);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return statesList;
    }
    
    
    public List<State> fetchStatesByCountrySign() {
        List<State> statesList = new ArrayList<State>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.getstates(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            BigDecimal countryCode =null;
            if (session.getAttribute("countryCode1") != null) {
                 countryCode =
                    new BigDecimal(session.getAttribute("countryCode1").toString());
            }

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, countryCode);
                statement.execute();
                ResultSet resultSet = (ResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    State state = new State();
                    state.setStateCode(resultSet.getBigDecimal(1));
                    state.setStateCountryCode(resultSet.getBigDecimal(2));
                    state.setStateShortDesc(resultSet.getString(3));
                    state.setStateName(resultSet.getString(4));
                    statesList.add(state);
                }
                statement.close();
                resultSet.close();
                connection.close();
            
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return statesList;
    }
    
    
    public List<State> fetchStatesByCountryDir() {
        List<State> statesList = new ArrayList<State>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.getstates(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            BigDecimal countryCode =null;
            if (session.getAttribute("countryCodeDir") != null) {
                 countryCode =
                    new BigDecimal(session.getAttribute("countryCodeDir").toString());
            }

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, countryCode);
                statement.execute();
                ResultSet resultSet = (ResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    State state = new State();
                    state.setStateCode(resultSet.getBigDecimal(1));
                    state.setStateCountryCode(resultSet.getBigDecimal(2));
                    state.setStateShortDesc(resultSet.getString(3));
                    state.setStateName(resultSet.getString(4));
                    statesList.add(state);
                }
                statement.close();
                resultSet.close();
                connection.close();
            
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return statesList;
    }
    
    
    public List<State> fetchStatesByCountryClnt() {
        List<State> statesList = new ArrayList<State>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.getstates(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            BigDecimal countryCode =null;
            if (session.getAttribute("countryCodeClnt") != null) {
                 countryCode =
                    new BigDecimal(session.getAttribute("countryCodeClnt").toString());
          
            
                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, countryCode);
                statement.execute();
                ResultSet resultSet = (ResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    State state = new State();
                    state.setStateCode(resultSet.getBigDecimal(1));
                    state.setStateCountryCode(resultSet.getBigDecimal(2));
                    state.setStateShortDesc(resultSet.getString(3));
                    state.setStateName(resultSet.getString(4));
                    statesList.add(state);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
            
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return statesList;
    }


    public List<AdministrativeRegion> fetchAdminRegionsByCountry() {
        List<AdministrativeRegion> adminRegionsList =
            new ArrayList<AdministrativeRegion>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_admin_regions(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("countryCode") != null) {
                BigDecimal countryCode =
                    new BigDecimal(session.getAttribute("countryCode").toString());

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, countryCode);
                statement.execute();
                ResultSet rs = (ResultSet)statement.getObject(1);

                while (rs.next()) {
                    AdministrativeRegion administrativeRegion =
                        new AdministrativeRegion();
                    administrativeRegion.setRegCode(rs.getBigDecimal(1));
                    administrativeRegion.setRegShortDesc(rs.getString(2));
                    administrativeRegion.setRegName(rs.getString(3));
                    administrativeRegion.setRegCouCode(rs.getBigDecimal(4));
                    adminRegionsList.add(administrativeRegion);

                }
                statement.close();
                rs.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return adminRegionsList;
    }

    /**
     * Fetches all <code>Town</code> objects/records that
     * belong to a given country from the database and returns them in a list.
     *
     * @return A list of <code>Town</code> objects/records
     * fetched from the database.
     */
    public List<Town> fetchTownsByCountry() {

        List<Town> townsList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin ?:=TQC_SETUPS_CURSOR.getTownsByCountry(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            townsList = new ArrayList<Town>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);

            if (session.getAttribute("countryCode") != null) {
                statement.setString(2,
                                    (String)session.getAttribute("countryCode"));
            } else {
                statement.setString(2, "");
            }

            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a Town object with
                // the values and add it to the list
                Town town = new Town();
                town.setCode(resultSet.getString(1));
                town.setCountryCode(resultSet.getString(2));
                town.setShortDesc(resultSet.getString(3));
                town.setName(resultSet.getString(4));                
                town.setPostalZipCode(resultSet.getString(7));
                townsList.add(town);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return townsList;
    }

    /**
     * Fetches all <code>Location</code> objects/records that
     * belong to a given currency from the database and returns them in a list.
     *
     * @return A list of <code>Location</code> objects/records
     * fetched from the database.
     */
    public List<Location> fetchLocationsByTown() {

        List<Location> locationsList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_locations_by_town(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            locationsList = new ArrayList<Location>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("townCode") != null) {
                statement.setString(1,
                                    (String)session.getAttribute("townCode"));
            } else {
                statement.setString(1, "");
            }

            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(2);

            while (resultSet.next()) {
                Location location = new Location();
                location.setCode(resultSet.getString(1));
                location.setTownCode(resultSet.getString(2));
                location.setShortDesc(resultSet.getString(3));
                location.setName(resultSet.getString(4));
                location.setLandmark(resultSet.getString(5));
                locationsList.add(location);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return locationsList;
    }

    /**
     * Fetches all <code>Parameter</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>Parameter</code> objects/records
     * fetched from the database.
     */
    public List<Parameter> fetchAllParameters() {

        List<Parameter> parametersList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_parameters(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            parametersList = new ArrayList<Parameter>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a Parameter object with the
                // values and add it to the list
                Parameter parameter = new Parameter();
                parameter.setCode(resultSet.getString(1));
                parameter.setName(resultSet.getString(2));
                parameter.setValue(resultSet.getString(3));
                parameter.setStatus(resultSet.getString(4));
                parameter.setDescription(resultSet.getString(5));
                parametersList.add(parameter);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return parametersList;
    }

    public List<Parameter> fetchAllLabels() {

        List<Parameter> parametersList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_labels(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            parametersList = new ArrayList<Parameter>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a Parameter object with the
                // values and add it to the list
                Parameter parameter = new Parameter();
                parameter.setCode(resultSet.getString(1));
                parameter.setName(resultSet.getString(2));
                parameter.setValue(resultSet.getString(3));
                parameter.setStatus(resultSet.getString(4));
                parameter.setDescription(resultSet.getString(5));
                parametersList.add(parameter);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return parametersList;
    }


    public List<PaymentMode> fetchAllPaymentModes() {

        List<PaymentMode> paymentModesList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_payment_modes(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            paymentModesList = new ArrayList<PaymentMode>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a Payment Mode object with the
                // values and add it to the list
                PaymentMode mode = new PaymentMode();
                mode.setCode(resultSet.getString(1));
                mode.setShortDesc(resultSet.getString(2));
                mode.setDescription(resultSet.getString(3));
                mode.setNaration(resultSet.getString(4));
                mode.setDefaultMode(resultSet.getString(5).toString());
                paymentModesList.add(mode);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return paymentModesList;
    }

    public List<SysApplicableArea> fetchSystemApplicableAreas() {

        List<SysApplicableArea> sysApplicableAreaList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.getSystemAppArea(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            sysApplicableAreaList = new ArrayList<SysApplicableArea>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.setObject(1, session.getAttribute("sysCode"));
            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(2);

            while (rs.next()) {

                SysApplicableArea sysApplicableArea = new SysApplicableArea();
                sysApplicableArea.setSysAppAreaCode(rs.getString(1));
                sysApplicableArea.setSysCode(rs.getString(2));
                sysApplicableArea.setSysName(rs.getString(3));
                sysApplicableArea.setSysAppAreaDesc(rs.getString(4));

                sysApplicableAreaList.add(sysApplicableArea);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return sysApplicableAreaList;
    }


    public List<PrintServer> fetchAllPrintServers() {

        List<PrintServer> printServerList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin ?:=TQC_SETUPS_CURSOR.get_print_servers();end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            printServerList = new ArrayList<PrintServer>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {
                // For every row, create a Payment Mode object with the
                // values and add it to the list
                PrintServer printserver = new PrintServer();
                printserver.setCode(rs.getBigDecimal(1));
                printserver.setName(rs.getString(2));
                printserver.setFilter(rs.getString(3));
                printserver.setUri(rs.getString(4));
                printserver.setFilter_command(rs.getString(5));
                printserver.setSec_username(rs.getString(6));
                printserver.setSec_password(rs.getString(7));
                printserver.setSec_auth_type(rs.getString(8));
                printserver.setSec_encrpt_type(rs.getString(9));
                printserver.setProxy_host(rs.getString(10));
                printserver.setProxy_port(rs.getString(11));
                printserver.setProxy_username(rs.getString(12));
                printserver.setProxy_pasword(rs.getString(13));
                printserver.setProxy_authen_type(rs.getString(14));
                printServerList.add(printserver);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return printServerList;
    }

    public List<EcmBean> findEcmSetups() {

        List<EcmBean> ecmsetups = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin  ?:= TQC_SETUPS_CURSOR.get_ecm_setups();end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        ResultSet rs = null;
        try {
            ecmsetups = new ArrayList<EcmBean>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {

                EcmBean ecmbean = new EcmBean();
                ecmbean.setEstcode(rs.getBigDecimal(1));
                ecmbean.setEstecmurl(rs.getString(2));
                ecmbean.setEstecmusername(rs.getString(3));
                ecmbean.setEstecmpassword(rs.getString(4));
                ecmbean.setEstsyscode(rs.getBigDecimal(5));
                ecmbean.setEstrootfolder(rs.getString(6));
                ecmbean.setSysname(rs.getString(7));
                ecmsetups.add(ecmbean);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        } finally {
            try {
                rs.close();
                statement.close();
                connection.close();
            } catch (Exception ex) {
            }
        }

        return ecmsetups;
    }


    public List<EcmBean> findEcmSystems() {

        List<EcmBean> ecmsetups = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin  ?:= TQC_SETUPS_CURSOR.get_ecm_systems();end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        ResultSet rs = null;
        try {
            ecmsetups = new ArrayList<EcmBean>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {

                EcmBean ecmbean = new EcmBean();
                ecmbean.setSyscode(rs.getBigDecimal(1));
                ecmbean.setSysShtDesc(rs.getString(2));
                ecmbean.setSysname(rs.getString(3));
                ecmsetups.add(ecmbean);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        } finally {
            try {
                rs.close();
                statement.close();
                connection.close();
            } catch (Exception ex) {
            }
        }

        return ecmsetups;
    }

    public List<EcmBean> findEcmProcesses() {

        List<EcmBean> ecmsetups = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin  ?:= TQC_SETUPS_CURSOR.get_ecm_processes(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        ResultSet rs = null;
        try {
            ecmsetups = new ArrayList<EcmBean>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setBigDecimal(2,
                                    (BigDecimal)session.getAttribute("EcmsysCode"));
            statement.execute();
            rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {
                EcmBean ecmbean = new EcmBean();
                ecmbean.setProcessCode(rs.getBigDecimal(1));
                ecmbean.setProcessName(rs.getString(2));
                ecmsetups.add(ecmbean);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        } finally {
            try {
                rs.close();
                statement.close();
                connection.close();
            } catch (Exception ex) {
            }
        }

        return ecmsetups;
    }


    public List<EcmBean> findEcmsystemReports() {

        List<EcmBean> ecmsetups = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin  ?:= TQC_SETUPS_CURSOR.get_system_reports(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        ResultSet rs = null;
        try {
            ecmsetups = new ArrayList<EcmBean>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setBigDecimal(2,
                                    (BigDecimal)session.getAttribute("EcmsysCode"));
            statement.execute();
            rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {
                EcmBean ecmbean = new EcmBean();
                ecmbean.setRptcode(rs.getBigDecimal(1));
                ecmbean.setRptname(rs.getString(2));
                ecmbean.setRptdesc(rs.getString(3));
                ecmsetups.add(ecmbean);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        } finally {
            try {
                rs.close();
                statement.close();
                connection.close();
            } catch (Exception ex) {
            }
        }

        return ecmsetups;
    }


    public List<EcmBean> findEcmProcessReports() {

        List<EcmBean> ecmsetups = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin  ?:= TQC_SETUPS_CURSOR.get_process_reports(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        ResultSet rs = null;
        try {
            ecmsetups = new ArrayList<EcmBean>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setBigDecimal(2,
                                    (BigDecimal)session.getAttribute("EcmPrcCode"));
            statement.execute();
            rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {
                EcmBean ecmbean = new EcmBean();
                ecmbean.setSprrCode(rs.getBigDecimal(1));
                ecmbean.setRptcode(rs.getBigDecimal(2));
                ecmbean.setSprrDesc(rs.getString(4));
                ecmbean.setRptname(rs.getString(5));
                ecmbean.setRptdesc(rs.getString(6));
                ecmbean.setType(rs.getString(7));
                ecmbean.setContDesc(rs.getString(8));
                ecmsetups.add(ecmbean);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        } finally {
            try {
                rs.close();
                statement.close();
                connection.close();
            } catch (Exception ex) {
            }
        }

        return ecmsetups;
    }


    public List<EcmBean> findEcmProcessDocTypes() {

        List<EcmBean> ecmsetups = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin  ?:= TQC_SETUPS_CURSOR.get_ecm_doc_types();end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        ResultSet rs = null;
        try {
            ecmsetups = new ArrayList<EcmBean>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {
                EcmBean ecmbean = new EcmBean();
                ecmbean.setSdt_code(rs.getBigDecimal(1));
                ecmbean.setContent_name(rs.getString(2));
                ecmbean.setContent_desc(rs.getString(3));
                ecmbean.setContentType(rs.getString(4));
                ecmsetups.add(ecmbean);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        } finally {
            try {
                rs.close();
                statement.close();
                connection.close();
            } catch (Exception ex) {
            }
        }

        return ecmsetups;
    }

    public List<EcmBean> findEcmProcessContentTypes() {

        List<EcmBean> ecmsetups = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin  ?:= TQC_SETUPS_CURSOR.get_content_metadata(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        ResultSet rs = null;
        try {
            ecmsetups = new ArrayList<EcmBean>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setBigDecimal(2,
                                    (BigDecimal)session.getAttribute("EcmSdtCode"));
            statement.execute();
            rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {
                EcmBean ecmbean = new EcmBean();
                ecmbean.setSdt_code(rs.getBigDecimal(1));
                ecmbean.setContent_name(rs.getString(3));
                ecmbean.setContent_desc(rs.getString(4));
                ecmsetups.add(ecmbean);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        } finally {
            try {
                rs.close();
                statement.close();
                connection.close();
            } catch (Exception ex) {
            }
        }

        return ecmsetups;
    }

    public List<SysApplicableArea> findSmsServiceProviders() {
        List<SysApplicableArea> sysApplicableAreaList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin  ?:= TQC_SETUPS_CURSOR.get_sms_template () ;end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            sysApplicableAreaList = new ArrayList<SysApplicableArea>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {

                SysApplicableArea servProv = new SysApplicableArea();
                servProv.setMsgCode(rs.getBigDecimal(1));
                servProv.setMsgShtDesc(rs.getString(2));
                servProv.setMessage(rs.getString(3));
                servProv.setSystemCode(rs.getBigDecimal(4));
                servProv.setSysModule(rs.getString(5));
                servProv.setMesageType(rs.getString(6));
                sysApplicableAreaList.add(servProv);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return sysApplicableAreaList;
    }

    public List<SysApplicableArea> findEmailServiceProviders() {
        List<SysApplicableArea> sysApplicableAreaList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin  ?:= TQC_SETUPS_CURSOR.get_email_template () ;end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            sysApplicableAreaList = new ArrayList<SysApplicableArea>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {

                SysApplicableArea servProv = new SysApplicableArea();
                servProv.setMsgCode(rs.getBigDecimal(1));
                servProv.setMsgShtDesc(rs.getString(2));
                servProv.setMessage(rs.getString(3));
                servProv.setSystemCode(rs.getBigDecimal(4));
                servProv.setSysModule(rs.getString(5));
                servProv.setMesageType(rs.getString(6));
                sysApplicableAreaList.add(servProv);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return sysApplicableAreaList;
    }

    public List<SysApplicableArea> findSystems() {
        List<SysApplicableArea> sysApplicableAreaList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin  TQC_SETUPS_CURSOR.get_systems (?) ;end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        try {
            sysApplicableAreaList = new ArrayList<SysApplicableArea>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {

                SysApplicableArea servProv = new SysApplicableArea();
                servProv.setSystemCode(rs.getBigDecimal(1));
                servProv.setSysShtDesc(rs.getString(2));
                servProv.setSysName(rs.getString(3));
                sysApplicableAreaList.add(servProv);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return sysApplicableAreaList;
    }

    public List<SysApplicableArea> findRequiredDocuments() {
        BigDecimal SysCode;
        if (session.getAttribute("SystemCode") != null) {
            SysCode =
                    new BigDecimal(session.getAttribute("SystemCode").toString());
        } else {

            SysCode = null;
        }
        List<SysApplicableArea> sysApplicableAreaList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getSystemReqDocs (?) ;end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        try {
            sysApplicableAreaList = new ArrayList<SysApplicableArea>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setBigDecimal(2, SysCode);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {

                SysApplicableArea servProv = new SysApplicableArea();
                servProv.setDocId(rs.getBigDecimal(1));
                servProv.setDocSht(rs.getString(2));
                servProv.setDocDesc(rs.getString(3));
                servProv.setMandatory(rs.getString(4));
                servProv.setNdDoc(rs.getString(5));
                servProv.setEnDoc(rs.getString(6));
                servProv.setRnDoc(rs.getString(7));
                servProv.setCertDoc(rs.getString(8));
                servProv.setLopDoc(rs.getString(9));
                servProv.setClmPayDoc(rs.getString(10));
                servProv.setValidPrd(rs.getBigDecimal(11));
                servProv.setRqcCode(rs.getBigDecimal(12));
                if (rs.getBigDecimal(12) == null) {
                    servProv.setChecked(false);
                } else {
                    servProv.setChecked(true);
                }
                sysApplicableAreaList.add(servProv);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return sysApplicableAreaList;
    }

    public List<System> findSmsDetails() {
        List<System> systemsList = new ArrayList<System>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getallsmsmessages(?,?,?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        
        try {
            String message = session.getAttribute("msg").toString();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            if (session.getAttribute("sysCode") != null) {

                BigDecimal sysCode =
                    new BigDecimal(session.getAttribute("sysCode").toString());
                String smsWEFVal = "";
                String smsWETVal = "";
                String smsTransTypeVal = "";

                if (session.getAttribute("smsWEF") != null) {
                    smsWEFVal = session.getAttribute("smsWEF").toString();
                }

                if (session.getAttribute("smsWET") != null) {
                    smsWETVal = session.getAttribute("smsWET").toString();
                }

                if (session.getAttribute("smsTransType") != null) {
                    smsTransTypeVal =
                            session.getAttribute("smsTransType").toString();
                }

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, sysCode);
                statement.setString(3, smsWEFVal == "" ? null : smsWEFVal);
                statement.setString(4, smsWETVal == "" ? null : smsWETVal);
                statement.setString(5,
                                    smsTransTypeVal == "" ? null : smsTransTypeVal);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    System system = new System();
                    system.setSms_code(resultSet.getString(1));
                    system.setSms_sys_code(resultSet.getString(2));
                    system.setSms_sys_module(resultSet.getString(3));
                    system.setSms_clnt_code(resultSet.getString(4));
                    system.setSms_agn_code(resultSet.getString(5));
                    system.setSms_pol_no(resultSet.getString(6));
                    system.setSms_pol_code(resultSet.getString(7));
                    system.setSms_clm_no(resultSet.getString(8));
                    system.setSms_tel_no(resultSet.getString(9));
                    system.setSms_msg(resultSet.getString(10));
                    if (resultSet.getString(10) != null) {
                        /* if (resultSet.getString(10).toLowerCase().contains("has been authori")) {
                            system.setTransLevel("Policy Authorization");
                        } else if (resultSet.getString(10).toLowerCase().contains("has been cancelled")) {
                            system.setTransLevel("Policy Cancellation");
                        } else if (resultSet.getString(10).toLowerCase().contains("has been received")) {
                            system.setTransLevel("Policy Conversion");
                        } else if (resultSet.getString(10).toLowerCase().contains("laps")) {
                            system.setTransLevel("Policy Lapsation");
                        } else if (resultSet.getString(10).toLowerCase().contains("renewed")) {
                            system.setTransLevel("Policy Renewal");
                        } else {
                            system.setTransLevel("Others");
                        }*/
                        
                        if (resultSet.getString(10).toLowerCase().contains("loan") ||
                            resultSet.getString(10).toLowerCase().contains("maturity") ||
                            resultSet.getString(10).toLowerCase().contains("refund") ||
                            resultSet.getString(10).toLowerCase().contains("surrender") ||
                            resultSet.getString(10).toLowerCase().contains("claim") ||
                            resultSet.getString(10).toLowerCase().contains("withdrawal") 
                                    ) {
                            system.setTransLevel("Policy Benefits");
                        }
                        else if (resultSet.getString(10).toLowerCase().contains("not taken")) {
                            system.setTransLevel("Policy Not Taken Up");
                        }
                        else if (resultSet.getString(10).toLowerCase().contains("authoriz")) {
                            system.setTransLevel("Policy Authorization");
                        } else if (resultSet.getString(10).toLowerCase().contains("cancel")) {
                            system.setTransLevel("Policy Cancellation");
                        } else if (resultSet.getString(10).toLowerCase().contains("laps")) {
                            system.setTransLevel("Policy Lapsation");
                        } else if (resultSet.getString(10).toLowerCase().contains("accept")) {
                            system.setTransLevel("Policy Conversion");
                        } else if (resultSet.getString(10).toLowerCase().contains("renewed")) {
                            system.setTransLevel("Policy Renewal");
                        } else if (resultSet.getString(10).toLowerCase().contains("first premium")) {
                            system.setTransLevel("Policy Inception");
                        } else if (resultSet.getString(10).toLowerCase().contains("accept")) {
                            system.setTransLevel("Policy Accepted");
                        } else if (resultSet.getString(10).toLowerCase().contains("deducted and allocated")) {
                            system.setTransLevel("Premim allocation");
                        } else if (resultSet.getString(10).toLowerCase().contains("outstanding")) {
                            system.setTransLevel("Deduction Ceased");
                        }else if (resultSet.getString(10).toLowerCase().contains("birthday")) {
                            system.setTransLevel("Birthday Message");
                        } else {
                            system.setTransLevel("Others");
                        }
                    }
                    system.setSms_status(resultSet.getString(11));
                    system.setSms_prepared_by(resultSet.getString(12));
                    system.setSms_send_date(resultSet.getString(13));
                    system.setPol_current_status(resultSet.getString(14));
                    system.setClientName(resultSet.getString(15));
                    system.setCouCode(resultSet.getString(16));
                    system.setCouZipCode(resultSet.getString(17));
                    String phoneNumber = resultSet.getString(9);
                    
                    String no_format="[\\+]{0,1}[0-9\\-]{10,12}";//kenya format
                    boolean correct_no=false;
                    if(!GlobalCC.isEmptyStr(phoneNumber)) 
                    {
                        if (phoneNumber.matches(no_format) ) 
                        {
                            correct_no=true;
                        }  
                    }


                    if (message.equalsIgnoreCase("all")) {
                        systemsList.add(system);
                    } else if (message.equalsIgnoreCase("invalid")) {
                        if (resultSet.getString(10) == null || !correct_no ||
                            phoneNumber == null ||
                            resultSet.getString(15) == null) {
                            systemsList.add(system);
                        }
                    } else if (message.equalsIgnoreCase("valid")) {
                        if (resultSet.getString(10) != null && correct_no &&
                            phoneNumber != null &&
                            resultSet.getString(15) != null) {
                            systemsList.add(system);
                        }
                    } else {
                        systemsList.add(system);
                    }
                    phoneNumber = null;
                    no_format = null;
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return systemsList;
    }

    public List<SysApplicableArea> findEmailDetails() {
        BigDecimal SysCode;
        String emailWEFVal = "";
        String emailWETVal = "";
        String emailTransTypeVal = "";

        if (session.getAttribute("sysCode") != null) {
            SysCode =
                    new BigDecimal(session.getAttribute("sysCode").toString());
        } else {
            SysCode = null;
        }

        if (session.getAttribute("emailWEF") != null) {
            emailWEFVal = session.getAttribute("emailWEF").toString();
        }

        if (session.getAttribute("emailWET") != null) {
            emailWETVal = session.getAttribute("emailWET").toString();
        }

        if (session.getAttribute("emailTransType") != null) {
            emailTransTypeVal =
                    session.getAttribute("emailTransType").toString();
        }

        List<SysApplicableArea> sysApplicableAreaList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getAllEmailMessages (?,?,?,?,?) ;end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        try {
            String message = null;
            if (session.getAttribute("EmailsentStatus") == null) {
                message = "all";
            } else {
                message = session.getAttribute("EmailsentStatus").toString();
            }
            sysApplicableAreaList = new ArrayList<SysApplicableArea>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setBigDecimal(2, SysCode);
            if (message.equalsIgnoreCase("valid") ||
                message.equalsIgnoreCase("invalid")) {
                statement.setObject(3, null);
            } else {
                statement.setObject(3,
                                    session.getAttribute("EmailsentStatus"));
            }

            statement.setString(4, emailWEFVal == "" ? null : emailWEFVal);
            statement.setString(5, emailWETVal == "" ? null : emailWETVal);
            statement.setString(6,
                                emailTransTypeVal == "" ? null : emailTransTypeVal);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {

                SysApplicableArea servProv = new SysApplicableArea();
                servProv.setCode(rs.getBigDecimal(1));
                servProv.setSysCode(rs.getString(2));
                servProv.setSysModule1(rs.getString(3));
                servProv.setClntCode(rs.getBigDecimal(4));
                servProv.setAgnCode(rs.getBigDecimal(5));
                servProv.setPolicyCode(rs.getString(6));
                servProv.setPolicyNumber(rs.getString(7));
                servProv.setClmNumber(rs.getString(8));
                servProv.setSmsTelNumber(rs.getString(9));
                servProv.setMessages(rs.getString(10));
                servProv.setStatus(rs.getString(11));
                servProv.setPreparedBy(rs.getString(12));
                servProv.setPreparedDate(rs.getString(13));
                servProv.setSendDate(rs.getString(14));
                servProv.setQoutCode(rs.getString(15));
                servProv.setQuotationNumber(rs.getString(16));
                servProv.setUserCode(rs.getBigDecimal(17));
                servProv.setSentResponse(rs.getString(18));
                servProv.setClientName(rs.getString(19));
                servProv.setAgentName(rs.getString(20));
                servProv.setUserName(rs.getString(21));
                servProv.setEmailAddress(rs.getString(22));
                servProv.setEmailSubject(rs.getString(23));
                servProv.setChecked(false);
                String email = rs.getString(22);
                if (message.equalsIgnoreCase("valid")) {
                    if (email != null) {
                        InternetAddress addr;
                        try {
                            addr = new InternetAddress(email);
                            addr.validate();
                            java.lang.System.out.println("Valid");
                            sysApplicableAreaList.add(servProv);
                        } catch (AddressException e) {

                        }
                    }
                } else if (message.equalsIgnoreCase("invalid")) {
                    if (email != null) {
                        InternetAddress addr;
                        try {
                            addr = new InternetAddress(email);
                            addr.validate();

                        } catch (AddressException e) {
                            java.lang.System.out.println("inValid");
                            sysApplicableAreaList.add(servProv);
                        }
                    } else {
                        sysApplicableAreaList.add(servProv);
                    }
                } else {
                    sysApplicableAreaList.add(servProv);
                }

            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return sysApplicableAreaList;
    }

    public List<SysApplicableArea> findLocationDtls() {
        List<SysApplicableArea> sysApplicableAreaList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? :=  TQC_SETUPS_CURSOR.getLocationDetails (?) ;end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        try {
            sysApplicableAreaList = new ArrayList<SysApplicableArea>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setObject(2, session.getAttribute("townCode"));
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {

                SysApplicableArea servProv = new SysApplicableArea();
                servProv.setLocCode(rs.getBigDecimal(1));
                servProv.setLocTwnCode(rs.getBigDecimal(2));
                servProv.setLocShtDesc(rs.getString(3));
                servProv.setLocName(rs.getString(4));
                sysApplicableAreaList.add(servProv);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return sysApplicableAreaList;
    }

    public List<SysApplicableArea> findBussinessPerson() {
        List<SysApplicableArea> sysApplicableAreaList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? :=  TQC_SETUPS_CURSOR.get_bussiness_person(?,?) ;end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        try {
            sysApplicableAreaList = new ArrayList<SysApplicableArea>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setObject(2, session.getAttribute("PayeeType"));
            statement.setObject(3, session.getAttribute("ClientCode"));
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {

                SysApplicableArea servProv = new SysApplicableArea();
                servProv.setBpnCode(rs.getBigDecimal(1));
                servProv.setBpnIdNumber(rs.getString(2));
                servProv.setBpnAddress(rs.getString(3));
                servProv.setBpnTel(rs.getString(4));

                servProv.setBpnMobile(rs.getString(5));
                servProv.setBpnEmail(rs.getString(6));
                servProv.setBpnType(rs.getString(7));
                servProv.setBpnZip(rs.getString(8));

                servProv.setBpnTown(rs.getString(9));
                servProv.setBpnCountryCode(rs.getBigDecimal(10));
                servProv.setBpnName(rs.getString(11));
                servProv.setBpnPin(rs.getString(12));

                servProv.setBpnBbrCode(rs.getBigDecimal(13));
                servProv.setBpnBankAccountNumber(rs.getString(14));
                servProv.setBpnBbSwift(rs.getBigDecimal(15));
                servProv.setBpnRegClmtCode(rs.getBigDecimal(16));

                servProv.setBpnbranch(rs.getString(17));
                servProv.setBpnCountry(rs.getString(18));
                servProv.setClaimantCde(rs.getString(19));

                sysApplicableAreaList.add(servProv);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return sysApplicableAreaList;
    }

    public List<SysApplicableArea> findBankBranches() {
        List<SysApplicableArea> sysApplicableAreaList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? :=  TQC_SETUPS_CURSOR.get_bank_branches_val ;end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        try {
            sysApplicableAreaList = new ArrayList<SysApplicableArea>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {

                SysApplicableArea servProv = new SysApplicableArea();
                servProv.setBbrCode(rs.getBigDecimal(1));
                servProv.setBbrBnkCode(rs.getBigDecimal(2));
                servProv.setBbrBranchName(rs.getString(3));
                servProv.setBbrRemarks(rs.getString(4));
                servProv.setBbrShtDesc(rs.getString(5));
                sysApplicableAreaList.add(servProv);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return sysApplicableAreaList;
    }

    public List<SysApplicableArea> findRegisteredClaimants() {
        List<SysApplicableArea> sysApplicableAreaList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? :=  TQC_SETUPS_CURSOR.get_registered_claimants ;end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        try {
            sysApplicableAreaList = new ArrayList<SysApplicableArea>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {

                SysApplicableArea servProv = new SysApplicableArea();
                servProv.setRegClaimantCode(rs.getBigDecimal(1));
                servProv.setCldCode(rs.getBigDecimal(2));
                servProv.setCldIdNumber(rs.getString(3));
                servProv.setName(rs.getString(4));
                sysApplicableAreaList.add(servProv);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return sysApplicableAreaList;
    }
}
