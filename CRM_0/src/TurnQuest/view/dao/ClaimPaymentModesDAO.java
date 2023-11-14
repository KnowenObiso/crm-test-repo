package TurnQuest.view.dao;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.ClaimPaymentModes;

import java.math.BigDecimal;

import java.sql.CallableStatement;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;


public class ClaimPaymentModesDAO {
    public ClaimPaymentModesDAO() {
        super();


    }
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public List<ClaimPaymentModes> fetchAlClaimsModes() {
        List<ClaimPaymentModes> clmPaymodeList =
            new ArrayList<ClaimPaymentModes>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_SETUPS_CURSOR.getClaimPaymodes();end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                ClaimPaymentModes claimPaymentModes = new ClaimPaymentModes();
                claimPaymentModes.setCode(resultSet.getBigDecimal(1));
                claimPaymentModes.setShortdesc(resultSet.getString(2));
                claimPaymentModes.setDescription(resultSet.getString(3));
                claimPaymentModes.setRemarks(resultSet.getString(4));
                claimPaymentModes.setMinAmt(resultSet.getString(5));
                claimPaymentModes.setMaxAmt(resultSet.getString(6));
                claimPaymentModes.setDefault_value(resultSet.getString(7));


                clmPaymodeList.add(claimPaymentModes);

            }
            statement.close();
            resultSet.close();
            connection.close();

            return clmPaymodeList;
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(connection, e);
            return null;
        }

    }


    public List<ClaimPaymentModes> fetchAlLMobileTypes() {
        List<ClaimPaymentModes> clmPaymodeList =
            new ArrayList<ClaimPaymentModes>();
        DBConnector dbConnector = new DBConnector();
        if (session.getAttribute("countryCode") != null) {
            String query =
                "begin ? := TQC_SETUPS_CURSOR.getMobilePaymentTypes(?);end;";
            CallableStatement statement = null;
            OracleConnection connection = null;

            try {
                connection = dbConnector.getDatabaseConnection();
                statement = connection.prepareCall(query);

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2,
                                        new BigDecimal(session.getAttribute("countryCode").toString()));
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    ClaimPaymentModes claimPaymentModes =
                        new ClaimPaymentModes();
                    claimPaymentModes.setCode(resultSet.getBigDecimal(1));
                    claimPaymentModes.setShortdesc(resultSet.getString(2));
                    claimPaymentModes.setDescription(resultSet.getString(3));

                    claimPaymentModes.setMinAmt(resultSet.getString(4));
                    claimPaymentModes.setMaxAmt(resultSet.getString(5));

                    clmPaymodeList.add(claimPaymentModes);

                }
                statement.close();
                resultSet.close();
                connection.close();


                return clmPaymodeList;
            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(connection, e);
                return null;
            }

        }
        return clmPaymodeList;
    }

    public List<ClaimPaymentModes> fetchAlLMobileTypePrefix() {
        List<ClaimPaymentModes> clmPaymodeList =
            new ArrayList<ClaimPaymentModes>();
        DBConnector dbConnector = new DBConnector();
        if (session.getAttribute("mptCode") != null) {
            String query =
                "begin ? := TQC_SETUPS_CURSOR.getMobTypesPrefixes(?);end;";
            CallableStatement statement = null;
            OracleConnection connection = null;

            try {
                connection = dbConnector.getDatabaseConnection();
                statement = connection.prepareCall(query);

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2,
                                        new BigDecimal(session.getAttribute("mptCode").toString()));
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    ClaimPaymentModes claimPaymentModes =
                        new ClaimPaymentModes();
                    claimPaymentModes.setMptpCode(resultSet.getBigDecimal(1));
                    claimPaymentModes.setPrefix(resultSet.getString(2));
                    claimPaymentModes.setMptCode(resultSet.getBigDecimal(3));


                    clmPaymodeList.add(claimPaymentModes);

                }
                statement.close();
                resultSet.close();
                connection.close();


                return clmPaymodeList;
            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(connection, e);
                return null;
            }

        }
        return clmPaymodeList;
    }


}
