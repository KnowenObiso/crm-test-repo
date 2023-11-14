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

import oracle.jdbc.internal.OracleTypes;


public class AccountDAO {
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public List<Account> findAllAccounts() {
        List<Account> accountsList = new ArrayList<Account>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin  ?:=TQC_ACTIVITIES_CURSOR.get_all_accounts();end;";
        CallableStatement statement = null;
        Connection connection = null;

        try {

            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(1);
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

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return accountsList;
    }

    public List<Account> findUnInvitedAccounts() {
        List<Account> accountsList = new ArrayList<Account>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin  TQC_ACTIVITIES_CURSOR.get_uninvited_act_accounts(?,?);end;";
        CallableStatement statement = null;
        Connection connection = null;
        BigDecimal ACTIVITYCODE =
            session.getAttribute("ACTIVITY_CODE") != null ?
            (BigDecimal)session.getAttribute("ACTIVITY_CODE") : null;

        try {

            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.setBigDecimal(1, ACTIVITYCODE);
            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(2);
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

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return accountsList;
    }
}
