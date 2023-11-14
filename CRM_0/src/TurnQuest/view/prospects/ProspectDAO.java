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

package TurnQuest.view.prospects;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.setups.Nation;
import TurnQuest.view.setups.Town;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;


public class ProspectDAO {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public ProspectDAO() {
        super();
    }

    public List<Prospect> fetchAllProspects() {
        List<Prospect> prospectsList = new ArrayList<Prospect>();
        DBConnector dbConnector = new DBConnector();
        String query2 = "begin ? := TQC_SETUPS_CURSOR.getProspects();end;";
        OracleCallableStatement statement2 = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement2 =
                    (OracleCallableStatement)connection.prepareCall(query2);

            statement2.registerOutParameter(1, OracleTypes.CURSOR);
            statement2.execute();
            OracleResultSet resultSet2 =
                (OracleResultSet)statement2.getObject(1);

            while (resultSet2.next()) {
                Prospect prospect = new Prospect();
                prospect.setPrsCode(resultSet2.getBigDecimal(1));
                prospect.setPrsSurname(resultSet2.getString(2));
                prospect.setPrsPhysicalAddress(resultSet2.getString(3));
                prospect.setPrsPostalAddress(resultSet2.getString(4));
                prospect.setPrsOtherNames(resultSet2.getString(5));
                prospect.setPrsTelNo(resultSet2.getString(6));
                prospect.setPrsMobileNo(resultSet2.getString(7));
                prospect.setPrsEmailAddress(resultSet2.getString(8));
                prospect.setPrsIdRegNo(resultSet2.getString(9));
                prospect.setPrsDob(resultSet2.getDate(10));
                prospect.setPrsPin(resultSet2.getString(11));
                prospect.setPrsTownCode(resultSet2.getBigDecimal(12));
                prospect.setPrsCountryCode(resultSet2.getBigDecimal(13));
                prospect.setPrsPostalCode(resultSet2.getBigDecimal(14));
                prospect.setCountryName(resultSet2.getString(15));
                prospect.setTownName(resultSet2.getString(16));
                prospect.setProDesc(resultSet2.getString(17));
                prospect.setType(resultSet2.getString(18));
                prospect.setPrsContact(resultSet2.getString(19));
                prospect.setPrsContactTel(resultSet2.getString(20));

                prospectsList.add(prospect);
            }
            statement2.close();
            resultSet2.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return prospectsList;
    }

    public List<Nation> fetchAllCountries() {
        List<Nation> countryList = new ArrayList<Nation>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_SETUPS_CURSOR.getAllCountries();end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                Nation country = new Nation();
                country.setCode(resultSet.getString(1));
                country.setShortDesc(resultSet.getString(2));
                country.setName(resultSet.getString(3));

                countryList.add(country);
            }
            statement.close();
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return countryList;
    }

    public List<Town> fetchAllTownsByCountry() {
        List<Town> townList = new ArrayList<Town>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getTownsByCountry(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
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
                    Town town = new Town();
                    town.setCode(resultSet.getString(1));
                    town.setCountryCode(resultSet.getString(2));
                    town.setShortDesc(resultSet.getString(3));
                    town.setName(resultSet.getString(4));


                    townList.add(town);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return townList;
    }
}
