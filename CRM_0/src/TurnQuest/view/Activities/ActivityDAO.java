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


public class ActivityDAO {
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);


    public List<Activity> findActivities() {
        BigDecimal accountCode = null;
        List<Activity> activityList = new ArrayList<Activity>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin  TQC_ACTIVITIES_CURSOR.get_activities(?,?);end;";
        CallableStatement statement = null;
        Connection connection = null;
        if (session.getAttribute("actyCode") != null) {
            accountCode = (BigDecimal)session.getAttribute("actyCode");
        }
        try {

            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.setBigDecimal(1, accountCode);
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
                activity.setMsgTemplateCode(rs.getString(20));
                activity.setMsgTemplateDesc(rs.getString(21));
                activityList.add(activity);
           }

            rs.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return activityList;
    }

    public List<Note> findActivityNotes() {
        List<Note> noteList = new ArrayList<Note>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin  TQC_ACTIVITIES_CURSOR.get_activity_notes(?,?);end;";
        CallableStatement statement = null;
        Connection connection = null;

        try {

            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.setObject(1, session.getAttribute("ACTIVITY_CODE"));
            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(2);
            while (rs.next()) {
                Note note = new Note();

                note.setNoteCode(rs.getBigDecimal(1));
                note.setNoteAcccountCode(rs.getBigDecimal(2));
                note.setRelatedAccount(rs.getString(3));
                note.setNoteContactCode(rs.getBigDecimal(4));
                note.setNoteSubject(rs.getString(5));
                note.setNoteDescription(rs.getString(6));
                note.setNoteActivityCode(rs.getBigDecimal(7));
                note.setNoteAttachmentType(rs.getString(8));
                note.setNoteAttachmentName(rs.getString(9));
                note.setNoteAttachmentFile(rs.getBinaryStream(10));
                noteList.add(note);

            }

            rs.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);

        }

        return noteList;
    }

    public List<ActivityParticipant> findActivityParticipants() {
        List<ActivityParticipant> participantList =
            new ArrayList<ActivityParticipant>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin  TQC_ACTIVITIES_CURSOR.get_activity_participants(?,?);end;";
        CallableStatement statement = null;
        Connection connection = null;

        try {

            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.setObject(1, session.getAttribute("ACTIVITY_CODE"));
            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(2);
            while (rs.next()) {

                ActivityParticipant activityParticipant =
                    new ActivityParticipant();
                activityParticipant.setParticipantId(rs.getBigDecimal(1));
                activityParticipant.setParticipantActCode(rs.getBigDecimal(2));
                activityParticipant.setParticipantAccountCode(rs.getBigDecimal(3));
                activityParticipant.setAccountName(rs.getString(4));
                activityParticipant.setAccountEmail(rs.getString(5));
                activityParticipant.setSelected(false);

                participantList.add(activityParticipant);


            }

            rs.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return participantList;
    }

    public List<PriorityLevel> findPriorityLevels() {
        List<PriorityLevel> priorityLevelsList =
            new ArrayList<PriorityLevel>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin  ?:=TQC_ACTIVITIES_CURSOR.get_priority_levels();end;";
        CallableStatement statement = null;
        Connection connection = null;

        try {

            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {

                PriorityLevel priorityLevel = new PriorityLevel();
                priorityLevel.setPlCode(rs.getBigDecimal(1));
                priorityLevel.setPlDescription(rs.getString(2));
                priorityLevel.setPrShortDescription(rs.getString(3));
                priorityLevelsList.add(priorityLevel);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return priorityLevelsList;
    }

    public List<Status> findStatuses() {
        List<Status> statusList = new ArrayList<Status>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin ?:= TQC_ACTIVITIES_CURSOR.get_statuses();end;";
        CallableStatement statement = null;
        Connection connection = null;

        try {

            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {
                Status status = new Status();
                status.setStatusId(rs.getBigDecimal(1));
                status.setStatusCode(rs.getString(2));
                status.setStatusDecription(rs.getString(3));
                statusList.add(status);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);

        }

        return statusList;
    }

    public List<ActivityTask> findActivityTasks() {
        List<ActivityTask> activityTasksList = new ArrayList<ActivityTask>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin  TQC_ACTIVITIES_CURSOR.get_activity_tasks(?,?);end;";
        CallableStatement statement = null;
        Connection connection = null;

        try {


            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.setObject(1, session.getAttribute("ACTIVITY_CODE"));
            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(2);
            while (rs.next()) {
                ActivityTask activityTask = new ActivityTask();
                activityTask.setTastCode(rs.getBigDecimal(1));
                activityTask.setActivityCode(rs.getBigDecimal(2));
                activityTask.setTaskFrom(rs.getDate(3));
                activityTask.setTaskTo(rs.getDate(4));
                activityTask.setTaskSubject(rs.getString(5));
                activityTask.setTaskStatusCode(rs.getBigDecimal(6));
                activityTask.setStatus(rs.getString(7));
                activityTask.setTaskPriorityCode(rs.getBigDecimal(8));
                activityTask.setPriority(rs.getString(9));
                activityTask.setTaskAccountCode(rs.getBigDecimal(10));
                activityTask.setRelatedAccount(rs.getString(11));
                activityTasksList.add(activityTask);

            }

            rs.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return activityTasksList;
    }

    public List<Status> findSecurityQuestions() {
        List<Status> statusList = new ArrayList<Status>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ?:= TQC_ROLES_CURSOR.getAllsecurityQuestions();end;";
        CallableStatement statement = null;
        Connection connection = null;

        try {

            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {
                Status status = new Status();
                status.setStatusId(rs.getBigDecimal(1));
                status.setStatusCode(rs.getString(2));
                status.setStatusDecription(rs.getString(3));
                statusList.add(status);
            }
            rs.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);

        }

        return statusList;
    }

}
