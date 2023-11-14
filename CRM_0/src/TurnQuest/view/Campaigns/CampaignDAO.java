package TurnQuest.view.Campaigns;


import TurnQuest.view.Activities.Account;
import TurnQuest.view.Activities.Activity;
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

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.internal.OracleTypes;


public class CampaignDAO {
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public List<Campaign> findCampaigns() {
        List<Campaign> campaignList = new ArrayList<Campaign>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin  TQC_CAMPAIGN_CURSOR.get_cammpaigns(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {

            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.setObject(1, null);
            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(2);
            while (rs.next()) {

                Campaign campaign = new Campaign();
                campaign.setCampCode(rs.getBigDecimal(1));
                campaign.setCampUserCode(rs.getBigDecimal(2));
                campaign.setCampUserName(rs.getString(3));
                campaign.setCampTeamUserCode(rs.getBigDecimal(4));
                campaign.setCampTeamName(rs.getString(5));
                campaign.setCampDate(rs.getDate(6));
                campaign.setCampSponsor(rs.getString(7));
                campaign.setCampOrgCode(rs.getBigDecimal(8));
                campaign.setCampOrgName(rs.getString(9));
                campaign.setCampProdCode(rs.getBigDecimal(10));
                campaign.setCampProdName(rs.getString(11));
                campaign.setCampSysCode(rs.getBigDecimal(12));
                campaign.setCampSysName(rs.getString(13));
                campaign.setCampName(rs.getString(14));
                campaign.setCampType(rs.getString(15));
                campaign.setCampStatus(rs.getString(16));
                campaign.setCampExpCloseDate(rs.getDate(17));
                campaign.setCampTargetAudience(rs.getString(18));
                campaign.setCampTargetSize(rs.getBigDecimal(19));
                campaign.setCampNumSent(rs.getString(20));
                campaign.setCampBudgetedCost(rs.getBigDecimal(21));
                campaign.setCampActualCost(rs.getBigDecimal(22));
                campaign.setCampExpCost(rs.getBigDecimal(23));
                campaign.setCampExpRevenue(rs.getBigDecimal(24));
                campaign.setCampExpSalesCount(rs.getBigDecimal(25));
                campaign.setCampActualSalesCount(rs.getBigDecimal(26));
                campaign.setCampActualResponseCount(rs.getBigDecimal(27));
                campaign.setCampExpResponseCount(rs.getBigDecimal(28));
                campaign.setCampExpROI(rs.getBigDecimal(29));
                campaign.setCampActualROI(rs.getBigDecimal(30));
                campaign.setCampDescription(rs.getString(31));
                campaign.setCurrencyCode(rs.getBigDecimal(32));
                campaign.setCurrencyName(rs.getString(33));
                campaign.setCampObjective(rs.getString(34));
                campaign.setCampImpressionCount(rs.getBigDecimal(35));
                campaign.setEvent(rs.getString(36));
                campaign.setVenue(rs.getString(37));
                campaign.setEventTime(rs.getTimestamp(38));
                campaignList.add(campaign);


            }

            rs.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return campaignList;
    }

    public List<System> findCampaignSystems() {
        List<System> systemList = new ArrayList<System>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin ?:= TQC_CAMPAIGN_CURSOR.get_camp_systems();end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {

            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(1);
            while (rs.next()) {
                System system = new System();
                system.setCode(rs.getString(1));
                system.setShortDesc(rs.getString(2));
                system.setName(rs.getString(3));
                systemList.add(system);
            }
            rs.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return systemList;
    }

    public List<CampaignProduct> findCampaignProducts() {
        List<CampaignProduct> campaignProductList =
            new ArrayList<CampaignProduct>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_CAMPAIGN_CURSOR.get_campaign_products(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {

            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.setObject(1, session.getAttribute("sysCode"));
            statement.registerOutParameter(2, OracleTypes.CURSOR);

            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(2);
            while (rs.next()) {
                CampaignProduct campaignProduct = new CampaignProduct();
                campaignProduct.setProductCode(rs.getString(1));
                campaignProduct.setProductShortDesc(rs.getString(2));
                campaignProduct.setProdDesc(rs.getString(3));
                campaignProduct.setProdSystem(rs.getString(4));
                campaignProductList.add(campaignProduct);
            }
            rs.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return campaignProductList;
    }

    public List<CampaignActivity> findCampaignActivities() {
        List<CampaignActivity> campaignActivityList =
            new ArrayList<CampaignActivity>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_CAMPAIGN_CURSOR.get_camp_activities(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {

            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.setObject(1, session.getAttribute("CAMPAIGN_CODE"));
            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(2);
            while (rs.next()) {
                CampaignActivity campaignActivity = new CampaignActivity();
                campaignActivity.setCampActCode(rs.getBigDecimal(1));
                campaignActivity.setCampCode(rs.getBigDecimal(2));
                campaignActivity.setActCode(rs.getBigDecimal(3));
                campaignActivity.setActSubject(rs.getString(4));
                campaignActivity.setActName(rs.getString(5));
                campaignActivity.setActWef(rs.getDate(6));
                campaignActivity.setActWet(rs.getDate(7));
                campaignActivityList.add(campaignActivity);
            }
            rs.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return campaignActivityList;
    }

    public List<System> findCampaignTargets() {
        List<System> systemList = new ArrayList<System>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_CAMPAIGN_CURSOR.get_camp_targets(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {

            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setBigDecimal(2,
                                    (BigDecimal)session.getAttribute("CAMPAIGN_CODE"));
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(1);
            while (rs.next()) {
                System system = new System();

                system.setCMT_CODE(rs.getBigDecimal(1));
                system.setCMT_ACC_CODE(rs.getBigDecimal(2));
                system.setCMT_DATE(rs.getDate(3));
                system.setRELATED_ACCOUNT(rs.getString(4));
                system.setRELATED_EMAIL(rs.getString(5));

                systemList.add(system);
            }
            rs.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return systemList;
    }

    public List<System> findCampaignMessages() {
        List<System> systemList = new ArrayList<System>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_CAMPAIGN_CURSOR.get_campaign_messages(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {

            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setBigDecimal(2,
                                    (BigDecimal)session.getAttribute("CAMPAIGN_CODE"));
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(1);
            while (rs.next()) {
                System system = new System();


                system.setCMSG_CODE(rs.getBigDecimal(1));
                system.setCMSG_TYPE(rs.getString(2));
                system.setCMSG_TYPE_DESC(rs.getString(3));
                system.setCMSG_SUBJ(rs.getString(4));
                system.setCMSG_BODY(rs.getString(5));
                system.setCMSG_STATUS(rs.getString(6));
                system.setCMSG_STATUS_DESC(rs.getString(7));
                system.setCMSG_DATE(rs.getDate(8));
                system.setCMSG_SEND_ALL(rs.getString(9));
                system.setCMSG_SEND_ALL_DESC(rs.getString(10));

                systemList.add(system);
            }
            rs.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return systemList;
    }

    public List<Account> findUndefinedTargetAccounts() {
        List<Account> accountsList = new ArrayList<Account>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin  TQC_CAMPAIGN_CURSOR.get_undefined_target_accounts(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        BigDecimal CAMPAIGNCODE =
            session.getAttribute("CAMPAIGN_CODE") != null ?
            (BigDecimal)session.getAttribute("CAMPAIGN_CODE") : null;

        try {

            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.setBigDecimal(1, CAMPAIGNCODE);
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
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return accountsList;
    }

    public List<Activity> findUndefinedCampaignActivities() {
        List<Activity> activityList = new ArrayList<Activity>();
        BigDecimal CAMPAIGNCODE =
            session.getAttribute("CAMPAIGN_CODE") != null ?
            (BigDecimal)session.getAttribute("CAMPAIGN_CODE") : null;

        DBConnector dbConnector = new DBConnector();
        String query =
            "begin  TQC_CAMPAIGN_CURSOR.get_undefined_camp_activities(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {

            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.setObject(1, CAMPAIGNCODE);
            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(2);
            while (rs.next()) {

                Activity activity = new Activity();
                activity.setActivityCode(rs.getBigDecimal(1));
                activity.setActivityTypeCode(rs.getBigDecimal(2));
                activity.setActivityType(rs.getString(3));
                activity.setActivityWEF(rs.getDate(4));
                activity.setActivityWET(rs.getDate(5));
                activity.setActivityDuration(rs.getBigDecimal(6));
                activity.setActivitysubject(rs.getString(7));
                activity.setActivityLocation(rs.getString(8));
                activity.setActivityAssignedTo(rs.getBigDecimal(9));
                activity.setActivityAssignedUser(rs.getString(10));
                activity.setActivityRelatedTo(rs.getBigDecimal(11));
                activity.setActivityRelatedAccount(rs.getString(12));
                activity.setActivityStatusId(rs.getBigDecimal(13));
                activity.setActivityStatus(rs.getString(14));
                activity.setActivityDescription(rs.getString(15));
                activity.setActivityReminder(rs.getString(16));
                activity.setActivityTeam(rs.getBigDecimal(17));
                activity.setActivityTeamName(rs.getString(18));
                activity.setActivityReminderTime(rs.getDate(19));
                //                activity.setActWef(rs.getDate(20));
                //                activity.setActWet(rs.getDate(21));
                activityList.add(activity);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return activityList;
    }

    public List<System> findAllProducts() {
        List<System> systemList = new ArrayList<System>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_CAMPAIGN_CURSOR.get_all_products(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {

            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setBigDecimal(2,
                                    (BigDecimal)session.getAttribute("CAMPAIGN_CODE"));
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(1);
            while (rs.next()) {
                System system = new System();

                system.setCproCode(rs.getBigDecimal(1));
                system.setCproProCode(rs.getBigDecimal(2));
                system.setCproShtDesc(rs.getString(3));
                system.setCproCmpCode(rs.getBigDecimal(4));
                system.setCproProShtDesc(rs.getString(5));
                system.setCproProDesc(rs.getString(6));

                systemList.add(system);
            }
            rs.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return systemList;
    }
}
