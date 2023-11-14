package TurnQuest.view.dao;


import TurnQuest.view.Agents.Agent;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;


public class AgentsDAO {
    public AgentsDAO() {
        super();
    }
    public List<Agent> fetchAllAgentTypes() {

        List<Agent> agentTypes = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_SETUPS_CURSOR.get_agent_types(); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            agentTypes = new ArrayList<Agent>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                Agent agent = new Agent();
                agent.setAGNTY_CODE(resultSet.getBigDecimal(1));
                agent.setAGNTY_TYPE_SHT_DESC(resultSet.getString(2));
                agent.setAGNTY_TYPE(resultSet.getString(3));
                agent.setAGNTY_ACT_CODE(resultSet.getBigDecimal(4));
                agent.setACT_ACCOUNT_TYPE(resultSet.getString(5));
                agentTypes.add(agent);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return agentTypes;
    }
    
}
