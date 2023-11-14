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

package TurnQuest.view.dao;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.PostalCode;

import java.math.BigDecimal;

import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;


public class CountryDAO {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public CountryDAO() {
        super();
    }

    public List<PostalCode> fetchPostalCodesByTown() {
        List<PostalCode> postalCodesList = new ArrayList<PostalCode>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getPostalCodesByTown(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("townCode") != null) {
                BigDecimal twnCode =
                    new BigDecimal(session.getAttribute("townCode").toString());

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, twnCode);
                statement.execute();
                ResultSet resultSet = (ResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    PostalCode postalCode = new PostalCode();
                    postalCode.setPstCode(resultSet.getBigDecimal(1));
                    postalCode.setPstTownCode(resultSet.getBigDecimal(2));
                    postalCode.setPstDesc(resultSet.getString(3));
                    postalCode.setPstZipCode(resultSet.getString(4));

                    postalCodesList.add(postalCode);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
            e.printStackTrace();
        }
        return postalCodesList;
    }
}
