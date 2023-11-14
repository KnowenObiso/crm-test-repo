package TurnQuest.view.dao;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.Branch;
import TurnQuest.view.models.Sequence;
import TurnQuest.view.provider.System;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;


public class SequenceDAO {
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public SequenceDAO() {
        super();
    }


    public List<Sequence> findSequences() {
        List<Sequence> seqlist = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin  TQC_SEQUENCES_PKG.get_sequences(?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;


        seqlist = new ArrayList<Sequence>();

        try {
            conn = OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.registerOutParameter(1, OracleTypes.CURSOR);

            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                Sequence seq = new Sequence();
                seq.setTseq_code(rst.getBigDecimal(1));
                seq.setTseq_type(rst.getString(2));
                seq.setTseq_no_type(rst.getString(3));
                seq.setTseq_brn(rst.getBigDecimal(4));
                seq.setTseq_uwyr(rst.getBigDecimal(5));
                seq.setTseq_next_no(rst.getBigDecimal(6));
                seq.setBranch_name(rst.getString(7));
                seq.setOrg_code(rst.getBigDecimal(8));
                seq.setClientType(rst.getString(9));

                seqlist.add(seq);

            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }

        return seqlist;
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
                system.setV_bbr_branch_name(resultSet.getString(3));
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

    public List<Branch> findAllBranches() {
        List<Branch> brnlist = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin  TQC_SETUPS_CURSOR.get_AllBranches(?,?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;

        brnlist = new ArrayList<Branch>();
        if (session.getAttribute("MY_ORG_CODE") != null) {
            try {
                conn =
(OracleConnection)OracleConnection.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query);

                stmt.registerOutParameter(1, OracleTypes.CURSOR);
                stmt.setBigDecimal(2,
                                   new BigDecimal(session.getAttribute("MY_ORG_CODE").toString()));
                stmt.execute();
                OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
                while (rst.next()) {
                    Branch seq = new Branch();
                    seq.setCode(rst.getBigDecimal(1));
                    seq.setName(rst.getString(2));
                    brnlist.add(seq);
                }
                rst.close();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);

            }
        }
        return brnlist;
    }


}
