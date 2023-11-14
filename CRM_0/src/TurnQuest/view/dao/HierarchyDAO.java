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


import TurnQuest.view.Accounts.AccountsType;
import TurnQuest.view.Accounts.Agency;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.Usr.User;
import TurnQuest.view.client.Client2;
import TurnQuest.view.models.ActivityType;
import TurnQuest.view.models.AgencyActivity;
import TurnQuest.view.models.OrgDivisionLevel;
import TurnQuest.view.models.OrgDivisionLevelType;
import TurnQuest.view.models.OrgSubDivPreviousHead;
import TurnQuest.view.models.OrgSubDivision;
import TurnQuest.view.provider.ServiceProvider;
import TurnQuest.view.provider.System;

import java.math.BigDecimal;

import java.sql.CallableStatement;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;


public class HierarchyDAO {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public HierarchyDAO() {
        super();
    }

    public List<System> fetchAllSystems() {
        List<System> systemsList = new ArrayList<System>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_SETUPS_CURSOR.getAllSystems();end;";
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
                system.setSysTemplate(resultSet.getString(14));

                systemsList.add(system);
            }
            statement.close();
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return systemsList;
    }

    public List<OrgDivisionLevelType> fetchOrgDivisionLevelTypes() {
        List<OrgDivisionLevelType> typeList =
            new ArrayList<OrgDivisionLevelType>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getOrgDivLevelsType(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("sysCode") != null) {
                BigDecimal sysCode =
                    new BigDecimal(session.getAttribute("sysCode").toString());

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, sysCode);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    OrgDivisionLevelType levelType =
                        new OrgDivisionLevelType();
                    levelType.setDltCode(resultSet.getString(1));
                    levelType.setDltSysCode(resultSet.getBigDecimal(2));
                    levelType.setDltDesc(resultSet.getString(3));
                    levelType.setDltActCode(resultSet.getBigDecimal(4));
                    levelType.setAccountTypeName(resultSet.getString(5));
                    levelType.setDltHeadActCode(resultSet.getBigDecimal(6));
                    levelType.setHeadAccountTypeName(resultSet.getString(7));
                    levelType.setDltType(resultSet.getString(8));
                    levelType.setDltIntCode(resultSet.getBigDecimal(9));
                    levelType.setAgencyIntermediary(resultSet.getString(10));
                    levelType.setDltPayIntermediary(resultSet.getString(11));
                    typeList.add(levelType);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return typeList;
    }

    public List<OrgDivisionLevel> fetchOrgDivisionLevels() {
        List<OrgDivisionLevel> divLevelList =
            new ArrayList<OrgDivisionLevel>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getOrgDivisionLevels(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("dltCode") != null) {
                String dltCode = session.getAttribute("dltCode").toString();

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setString(2, dltCode);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    OrgDivisionLevel divLevel = new OrgDivisionLevel();
                    divLevel.setOdlcode(resultSet.getString(1));
                    divLevel.setOdlDltCode(resultSet.getString(2));
                    divLevel.setOdlDesc(resultSet.getString(3));
                    divLevel.setOdlRanking(resultSet.getBigDecimal(4));
                    divLevel.setOdlType(resultSet.getString(5));
                    divLevelList.add(divLevel);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return divLevelList;
    }

    public List<OrgDivisionLevel> fetchOrgDivisionLevelsByRanking() {
        List<OrgDivisionLevel> divLevelList =
            new ArrayList<OrgDivisionLevel>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getOrgDivisionLevelsByRanking(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("dltCode") != null &&
                session.getAttribute("odlRanking") != null) {
                String dltCode = session.getAttribute("dltCode").toString();
                String oldRanking =
                    session.getAttribute("odlRanking").toString();

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setString(2, dltCode);
                statement.setBigDecimal(3, new BigDecimal(oldRanking));
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    OrgDivisionLevel divLevel = new OrgDivisionLevel();
                    divLevel.setOdlcode(resultSet.getString(1));
                    divLevel.setOdlDltCode(resultSet.getString(2));
                    divLevel.setOdlDesc(resultSet.getString(3));
                    divLevel.setOdlRanking(resultSet.getBigDecimal(4));
                    divLevel.setOdlType(resultSet.getString(5));
                    divLevelList.add(divLevel);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
            e.printStackTrace();
        }
        return divLevelList;
    }

    public List<OrgSubDivision> fetchOrgSubDivisionsBySystem() {
        List<OrgSubDivision> subDivList = new ArrayList<OrgSubDivision>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getOrgSubDivisionsBySys(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("sysCode") != null) {
                BigDecimal sysCode =
                    new BigDecimal(session.getAttribute("sysCode").toString());

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, sysCode);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    OrgSubDivision orgSubDivision = new OrgSubDivision();
                    orgSubDivision.setOsdCode(resultSet.getString(1));
                    orgSubDivision.setOsdParentOsdCode(resultSet.getString(2));
                    orgSubDivision.setOsdDltCode(resultSet.getString(3));
                    orgSubDivision.setOsdOdlCode(resultSet.getString(4));
                    orgSubDivision.setOsdName(resultSet.getString(5));
                    orgSubDivision.setOsdWef(resultSet.getDate(6));
                    orgSubDivision.setOsdDivHeadAgnCode(resultSet.getBigDecimal(7));
                    orgSubDivision.setOsdSysCode(resultSet.getBigDecimal(8));
                    orgSubDivision.setOsdId(resultSet.getBigDecimal(19));
                    orgSubDivision.setOsdParentOsdId(resultSet.getBigDecimal(20));
                    orgSubDivision.setOsdLocationCode(resultSet.getBigDecimal(21));
                    orgSubDivision.setOsdLocation(resultSet.getString(22));
                    subDivList.add(orgSubDivision);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return subDivList;
    }

    public List<OrgSubDivision> fetchOrgSubDivisionsBySystem(String sysCode) {
        List<OrgSubDivision> subDivList = new ArrayList<OrgSubDivision>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getOrgSubDivisionsBySys(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (sysCode != null) {
                BigDecimal sysCodeVar = new BigDecimal(sysCode);

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, sysCodeVar);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    OrgSubDivision orgSubDivision = new OrgSubDivision();
                    orgSubDivision.setOsdCode(resultSet.getString(1));
                    orgSubDivision.setOsdParentOsdCode(resultSet.getString(2));
                    orgSubDivision.setOsdDltCode(resultSet.getString(3));
                    orgSubDivision.setOsdOdlCode(resultSet.getString(4));
                    orgSubDivision.setOsdName(resultSet.getString(5));
                    orgSubDivision.setOsdWef(resultSet.getDate(6));
                    orgSubDivision.setOsdDivHeadAgnCode(resultSet.getBigDecimal(7));
                    orgSubDivision.setOsdSysCode(resultSet.getBigDecimal(8));
                    orgSubDivision.setDltDesc(resultSet.getString(9));
                    orgSubDivision.setOdlDesc(resultSet.getString(10));
                    orgSubDivision.setAgentName(resultSet.getString(11));
                    orgSubDivision.setOsdId(resultSet.getBigDecimal(19));
                    orgSubDivision.setOsdParentOsdId(resultSet.getBigDecimal(20));
                    orgSubDivision.setOsdLocationCode(resultSet.getBigDecimal(21));
                    orgSubDivision.setOsdLocation(resultSet.getString(22));
                    orgSubDivision.setNodeType("S");

                    orgSubDivision.setOrgSubDivisions(fetchOrgSubDivisionsBySubDiv(orgSubDivision.getOsdCode()));

                    subDivList.add(orgSubDivision);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return subDivList;
    }

    public List<OrgSubDivision> fetchLowestOrgSubDivisionsBySystem() {
        List<OrgSubDivision> subDivList = new ArrayList<OrgSubDivision>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getLowestOrgSubDivsBySys(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("sysCode") != null &&
                (session.getAttribute("accountTypeCode") != null)) {
                BigDecimal sysCode =
                    new BigDecimal(session.getAttribute("sysCode").toString());

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, sysCode);
                statement.setBigDecimal(3,
                                        new BigDecimal(session.getAttribute("accountTypeCode").toString()));
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    OrgSubDivision orgSubDivision = new OrgSubDivision();
                    orgSubDivision.setOsdCode(resultSet.getString(1));
                    orgSubDivision.setOsdParentOsdCode(resultSet.getString(2));
                    orgSubDivision.setOsdDltCode(resultSet.getString(3));
                    orgSubDivision.setOsdOdlCode(resultSet.getString(4));
                    orgSubDivision.setOsdName(resultSet.getString(5));
                    orgSubDivision.setOsdWef(resultSet.getDate(6));
                    orgSubDivision.setOsdDivHeadAgnCode(resultSet.getBigDecimal(7));
                    orgSubDivision.setOsdSysCode(resultSet.getBigDecimal(8));
                    orgSubDivision.setDltDesc(resultSet.getString(9));
                    orgSubDivision.setOdlDesc(resultSet.getString(10));
                    orgSubDivision.setAgentName(resultSet.getString(11));
                    orgSubDivision.setOsdId(resultSet.getBigDecimal(19));
                    orgSubDivision.setOsdParentOsdId(resultSet.getBigDecimal(20));
                    orgSubDivision.setOsdLocationCode(resultSet.getBigDecimal(21));
                    orgSubDivision.setOsdLocation(resultSet.getString(22));
                    subDivList.add(orgSubDivision);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(connection, e);

        }
        return subDivList;
    }

    public List<OrgSubDivision> fetchOSDHeadsHist() {
        List<OrgSubDivision> subDivList = new ArrayList<OrgSubDivision>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.get_subdiv_head_history(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setString(2, (String)session.getAttribute("osdCode"));
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                OrgSubDivision orgSubDivision = new OrgSubDivision();
                orgSubDivision.setOdh_code(resultSet.getBigDecimal(1));
                orgSubDivision.setOdh_osd_code(resultSet.getString(2));
                orgSubDivision.setOdh_osd_div_head_agn_code(resultSet.getBigDecimal(3));
                orgSubDivision.setOdh_wef_date(resultSet.getDate(4));
                orgSubDivision.setOdh_wet_date(resultSet.getDate(5));
                orgSubDivision.setOdh_agn_name(resultSet.getString(6));
                orgSubDivision.setOdh_agn_sht_desc(resultSet.getString(7));
                subDivList.add(orgSubDivision);
            }
            statement.close();
            resultSet.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return subDivList;

    }

    public List<OrgSubDivision> fetchOrgSubDivisionsByDlt() {
        List<OrgSubDivision> subDivList = new ArrayList<OrgSubDivision>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getOrgSubDivisionsByDlt(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("dltCode") != null) {
                String dltCode = session.getAttribute("dltCode").toString();

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setString(2, dltCode);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    OrgSubDivision orgSubDivision = new OrgSubDivision();
                    orgSubDivision.setOsdCode(resultSet.getString(1));
                    orgSubDivision.setOsdParentOsdCode(resultSet.getString(2));
                    orgSubDivision.setOsdDltCode(resultSet.getString(3));
                    orgSubDivision.setOsdOdlCode(resultSet.getString(4));
                    orgSubDivision.setOsdName(resultSet.getString(5));
                    orgSubDivision.setOsdWef(resultSet.getDate(6));
                    orgSubDivision.setOsdDivHeadAgnCode(resultSet.getBigDecimal(7));
                    orgSubDivision.setOsdSysCode(resultSet.getBigDecimal(8));
                    orgSubDivision.setDltDesc(resultSet.getString(9));
                    orgSubDivision.setOdlDesc(resultSet.getString(10));
                    orgSubDivision.setAgentName(resultSet.getString(11));
                    orgSubDivision.setNodeType("S");
                    orgSubDivision.setOdlRanking(resultSet.getBigDecimal(12));
                    orgSubDivision.setOdlType(resultSet.getString(13));
                    orgSubDivision.setBrnCode(resultSet.getBigDecimal(14));
                    orgSubDivision.setRegCode(resultSet.getBigDecimal(15));
                    orgSubDivision.setOsdPostLevel(resultSet.getString(16));
                    orgSubDivision.setOsdManagerAllowed(resultSet.getString(17));
                    orgSubDivision.setOsdOverCommAllowed(resultSet.getString(18));
                    orgSubDivision.setOsdId(resultSet.getBigDecimal(19));
                    orgSubDivision.setOsdParentOsdId(resultSet.getBigDecimal(20));
                    orgSubDivision.setOsdLocationCode(resultSet.getBigDecimal(21));
                    orgSubDivision.setOsdLocation(resultSet.getString(22));
                    orgSubDivision.setOrgSubDivisions(fetchOrgSubDivisionsBySubDiv(orgSubDivision.getOsdCode()));

                    subDivList.add(orgSubDivision);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return subDivList;
    }

    public List<OrgSubDivision> fetchOrgSubDivisionsBySubDiv() {
        List<OrgSubDivision> subDivList = new ArrayList<OrgSubDivision>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getOrgSubDivisionsBySubDiv(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("osdCode") != null) {
                String osdCode = session.getAttribute("osdCode").toString();

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setString(2, osdCode);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    OrgSubDivision orgSubDivision = new OrgSubDivision();
                    orgSubDivision.setOsdCode(resultSet.getString(1));
                    orgSubDivision.setOsdParentOsdCode(resultSet.getString(2));
                    orgSubDivision.setOsdDltCode(resultSet.getString(3));
                    orgSubDivision.setOsdOdlCode(resultSet.getString(4));
                    orgSubDivision.setOsdName(resultSet.getString(5));
                    orgSubDivision.setOsdWef(resultSet.getDate(6));
                    orgSubDivision.setOsdDivHeadAgnCode(resultSet.getBigDecimal(7));
                    orgSubDivision.setOsdSysCode(resultSet.getBigDecimal(8));
                    orgSubDivision.setOdlRanking(resultSet.getBigDecimal(9));
                    orgSubDivision.setOsdId(resultSet.getBigDecimal(19));
                    orgSubDivision.setOsdParentOsdId(resultSet.getBigDecimal(20));
                    orgSubDivision.setOsdLocationCode(resultSet.getBigDecimal(21));
                    orgSubDivision.setOsdLocation(resultSet.getString(22));
                    subDivList.add(orgSubDivision);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return subDivList;
    }

    public List<OrgSubDivision> fetchOrgSubDivisionsBySubDiv(String osdCode) {
        List<OrgSubDivision> subDivList = new ArrayList<OrgSubDivision>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getOrgSubDivisionsBySubDiv(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (osdCode != null) {
                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setString(2, osdCode);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    OrgSubDivision orgSubDivision = new OrgSubDivision();
                    orgSubDivision.setOsdCode(resultSet.getString(1));
                    orgSubDivision.setOsdParentOsdCode(resultSet.getString(2));
                    orgSubDivision.setOsdDltCode(resultSet.getString(3));
                    orgSubDivision.setOsdOdlCode(resultSet.getString(4));
                    orgSubDivision.setOsdName(resultSet.getString(5));
                    orgSubDivision.setOsdWef(resultSet.getDate(6));
                    orgSubDivision.setOsdDivHeadAgnCode(resultSet.getBigDecimal(7));
                    orgSubDivision.setOsdSysCode(resultSet.getBigDecimal(8));
                    orgSubDivision.setDltDesc(resultSet.getString(9));
                    orgSubDivision.setOdlDesc(resultSet.getString(10));
                    orgSubDivision.setAgentName(resultSet.getString(11));
                    orgSubDivision.setNodeType("S");
                    orgSubDivision.setOdlRanking(resultSet.getBigDecimal(12));
                    orgSubDivision.setOdlType(resultSet.getString(13));
                    orgSubDivision.setBrnCode(resultSet.getBigDecimal(14));
                    orgSubDivision.setRegCode(resultSet.getBigDecimal(15));
                    orgSubDivision.setOsdPostLevel(resultSet.getString(16));
                    orgSubDivision.setOsdManagerAllowed(resultSet.getString(17));
                    orgSubDivision.setOsdOverCommAllowed(resultSet.getString(18));
                    orgSubDivision.setOsdId(resultSet.getBigDecimal(19));
                    orgSubDivision.setOsdParentOsdId(resultSet.getBigDecimal(20));
                    orgSubDivision.setOsdLocationCode(resultSet.getBigDecimal(21));
                    orgSubDivision.setOsdLocation(resultSet.getString(22));
                    // Use Recursion to get the child OrgSubDivisions by the parent OrgSubDivision
                    orgSubDivision.setOrgSubDivisions(fetchOrgSubDivisionsBySubDiv(orgSubDivision.getOsdCode()));

                    subDivList.add(orgSubDivision);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return subDivList;
    }

    public List<System> fetchSystemsOrgSubDivisions() {
        List<System> systemsList = new ArrayList<System>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_SETUPS_CURSOR.getAllSystems();end;";
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
                system.setSysTemplate(resultSet.getString(14));
                system.setNodeType("P");

                // Start 2nd level
                //List<OrgSubDivision> subDivList2 = new ArrayList<OrgSubDivision>();
                //String query2 = "begin ? := TQC_SETUPS_CURSOR.getOrgSubDivisionsBySubDiv(?);end;";
                //CallableStatement statement = null;
                // End 2nd Level

                //system.setOrgSubDivisions( subDivList2 );
                system.setOrgSubDivisions(fetchOrgSubDivisionsBySystem(system.getCode()));

                systemsList.add(system);
            }
            statement.close();
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return systemsList;
    }

    public List<Agency> fetchAllAgenciesLov() {
        List<Agency> agenciesList = new ArrayList<Agency>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_SETUPS_CURSOR.getAllAgenciesLov();end;";
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
                Agency agency = new Agency();
                agency.setCode(resultSet.getString(1));
                agency.setShortDesc(resultSet.getString(2));
                agency.setName(resultSet.getString(3));

                agenciesList.add(agency);
            }
            statement.close();
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return agenciesList;
    }

    public List<Agency> fetchAllAgencyMarketersLov() {
        List<Agency> agenciesList = new ArrayList<Agency>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_SETUPS_CURSOR.getAllMarketers(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setObject(2, session.getAttribute("headActCode"));
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                Agency agency = new Agency();
                agency.setCode(resultSet.getString(1));
                agency.setShortDesc(resultSet.getString(2));
                agency.setName(resultSet.getString(3));

                agenciesList.add(agency);
            }
            statement.close();
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return agenciesList;
    }

    public List<Agency> fetchSpecificAgencyMarketersLov() {
        List<Agency> agenciesList = new ArrayList<Agency>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getSpecificMarketers(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            //if ( session.getAttribute( "agnCode" ) != null ) {
            BigDecimal agnCode =
                session.getAttribute("agnCode") == null ? null :
                new BigDecimal(session.getAttribute("agnCode").toString());

            java.lang.System.out.println("AGN CODE 2 is " + agnCode);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setBigDecimal(2, agnCode);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                Agency agency = new Agency();
                agency.setCode(resultSet.getString(1));
                agency.setShortDesc(resultSet.getString(2));
                agency.setName(resultSet.getString(3));

                agenciesList.add(agency);
            }
            statement.close();
            resultSet.close();
            connection.close();
            //}
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return agenciesList;
    }

    public List<Agency> fetchAgencyActivityLov() {
        List<Agency> agenciesList = new ArrayList<Agency>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getAgenciesByAccType(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("accShtDesc") != null) {
                String shtDesc = session.getAttribute("accShtDesc").toString();

                java.lang.System.out.println("AGN CODE 2 is " + shtDesc);

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setString(2, shtDesc);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    Agency agency = new Agency();
                    agency.setCode(resultSet.getString(1));
                    agency.setShortDesc(resultSet.getString(2));
                    agency.setName(resultSet.getString(3));

                    agenciesList.add(agency);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return agenciesList;
    }

    public List<AgencyActivity> fetchAgencyActivities() {
        List<AgencyActivity> activityList = new ArrayList<AgencyActivity>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getAgencyActivitiesDetails(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("agnCode") != null) {
                BigDecimal agnCode =
                    new BigDecimal(session.getAttribute("agnCode").toString());

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, agnCode);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {


                    AgencyActivity agencyActivity = new AgencyActivity();
                    agencyActivity.setAacCode(resultSet.getBigDecimal(1));
                    agencyActivity.setAacActyCode(resultSet.getBigDecimal(2));
                    agencyActivity.setAacWef(resultSet.getDate(3));
                    agencyActivity.setAacEstimateWet(resultSet.getDate(4));
                    agencyActivity.setAacActualWet(resultSet.getDate(5));
                    agencyActivity.setAacRemarks(resultSet.getString(6));
                    agencyActivity.setAacAgnCode(resultSet.getBigDecimal(7));
                    // agencyActivity.setAacClientCode( resultSet.getBigDecimal( 8 ) );
                    //agencyActivity.setAacSprCode( resultSet.getBigDecimal( 9 ) );
                    agencyActivity.setAacSysCode(resultSet.getBigDecimal(8));

                    agencyActivity.setAacType(resultSet.getString(10));
                    agencyActivity.setAacActivityByCode(resultSet.getBigDecimal(9));
                    agencyActivity.setAacType(resultSet.getString(10));

                    agencyActivity.setActivityDesc(resultSet.getString(11));
                    // agencyActivity.setClientName( resultSet.getString( 13 ) );
                    agencyActivity.setAgencyName(resultSet.getString(12));
                    // agencyActivity.setMarketerAgencyName( resultSet.getString( 15 ) );
                    //agencyActivity.setProviderName( resultSet.getString( 16 ) );
                    agencyActivity.setSystemName(resultSet.getString(13));
                    agencyActivity.setAacActivityTypeName(resultSet.getString(14));
                    agencyActivity.setAacReasnsforActivity(resultSet.getString(15));


                    activityList.add(agencyActivity);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return activityList;
    }

    public List<ActivityType> fetchAllActivityTypes() {
        List<ActivityType> activityTypeList = new ArrayList<ActivityType>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getAllActivityTypes();end;";
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
                ActivityType activityType = new ActivityType();
                activityType.setActyCode(resultSet.getBigDecimal(1));
                activityType.setActySysCode(resultSet.getBigDecimal(2));
                activityType.setActyDesc(resultSet.getString(3));

                activityTypeList.add(activityType);
            }
            statement.close();
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return activityTypeList;
    }

    public List<ActivityType> fetchActivityTypeBySystem() {
        List<ActivityType> activityTypeList = new ArrayList<ActivityType>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getActivityTypesBySystem(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("sysCode") != null) {
                BigDecimal sysCode =
                    new BigDecimal(session.getAttribute("sysCode").toString());

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, sysCode);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    ActivityType activityType = new ActivityType();
                    activityType.setActyCode(resultSet.getBigDecimal(1));
                    activityType.setActySysCode(resultSet.getBigDecimal(2));
                    activityType.setActyDesc(resultSet.getString(3));

                    activityTypeList.add(activityType);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return activityTypeList;
    }

    public List<Client2> fetchAllClientsLov() {
        List<Client2> clientsList = new ArrayList<Client2>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_SETUPS_CURSOR.getAllClientsLov();end;";
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
                Client2 client = new Client2();
                client.setCode(resultSet.getString(1));
                client.setShortDesc(resultSet.getString(2));
                client.setName(resultSet.getString(3));

                clientsList.add(client);
            }
            statement.close();
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return clientsList;
    }

    public List<ServiceProvider> fetchAllServiceProvidersLov() {
        List<ServiceProvider> providersList = new ArrayList<ServiceProvider>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getAllServiceProvLov();end;";
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
                ServiceProvider provider = new ServiceProvider();
                provider.setCode(resultSet.getString(1));
                provider.setShortDesc(resultSet.getString(2));
                provider.setName(resultSet.getString(3));

                providersList.add(provider);
            }
            statement.close();
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return providersList;
    }

    public List<AccountsType> fetchAllAccountTypesLov() {
        List<AccountsType> accTypeList = new ArrayList<AccountsType>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getAllAccountTypesLov();end;";
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
                AccountsType accountType = new AccountsType();
                accountType.setCode(resultSet.getString(1));
                accountType.setAccountTypeShortDesc(resultSet.getString(2));
                accountType.setAccountType(resultSet.getString(3));

                accTypeList.add(accountType);
            }
            statement.close();
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return accTypeList;
    }

    public List<AccountsType> fetchHierarchyAccountTypesLov() {
        List<AccountsType> accTypeList = new ArrayList<AccountsType>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getHierarchyAccTypesLov(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("sysCode") != null) {
                BigDecimal sysCode =
                    new BigDecimal(session.getAttribute("sysCode").toString());

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, sysCode);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    AccountsType accountType = new AccountsType();
                    accountType.setCode(resultSet.getString(1));
                    accountType.setAccountTypeShortDesc(resultSet.getString(2));
                    accountType.setAccountType(resultSet.getString(3));

                    accTypeList.add(accountType);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return accTypeList;
    }

    public List<OrgSubDivPreviousHead> fetchOrgSubDivPrevHeadsByOrgSubDivision() {
        List<OrgSubDivPreviousHead> prevHeadList =
            new ArrayList<OrgSubDivPreviousHead>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getOrgSubDivPrevHeads(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("osdCode") != null) {
                String osdCode = session.getAttribute("osdCode").toString();

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setString(2, osdCode);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    OrgSubDivPreviousHead prevHead =
                        new OrgSubDivPreviousHead();
                    prevHead.setOsdphCode(resultSet.getBigDecimal(1));
                    prevHead.setOsdphOsdCode(resultSet.getString(2));
                    prevHead.setOsdphPrevAgnCode(resultSet.getBigDecimal(3));
                    prevHead.setOsdphWet(resultSet.getDate(4));
                    prevHead.setOsdName(resultSet.getString(5));
                    prevHead.setAgencyName(resultSet.getString(6));
                    prevHead.setOsdphOsdId(resultSet.getBigDecimal(7));
                    prevHeadList.add(prevHead);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return prevHeadList;
    }

    public List<AgencyActivity> fetchActivityByParticipants() {

        List<AgencyActivity> activityList = new ArrayList<AgencyActivity>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR. getParticipantsByActivity(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("activity_code") != null) {
                BigDecimal aacCode =
                    new BigDecimal(session.getAttribute("activity_code").toString());

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, aacCode);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    AgencyActivity agencyActivity = new AgencyActivity();
                    agencyActivity.setParticipId(resultSet.getBigDecimal(1));
                    agencyActivity.setParticipCode(resultSet.getString(2));
                    agencyActivity.setAacCode(resultSet.getBigDecimal(3));
                    agencyActivity.setParticipActType(resultSet.getString(4));
                    agencyActivity.setParticipByCode(resultSet.getBigDecimal(5));
                    agencyActivity.setParticipName(resultSet.getString(6));


                    activityList.add(agencyActivity);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return activityList;
    }

    public List<User> findGroupUser() {


        String query = "begin ? := TQC_SETUPS_CURSOR.getGroupUsers(?);end;";
        OracleCallableStatement cst = null;
        List<User> users = new ArrayList<User>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, oracle.jdbc.OracleTypes.

                    CURSOR); //authorization code
            cst.setString(2,
                          session.getAttribute("usrGrpDate") == null ? null :
                          session.getAttribute("usrGrpDate").toString());
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            users = new ArrayList<User>();
            int k = 0;
            while (rs.next()) {
                User user = new User();

                user.setUserCode(rs.getBigDecimal(1));
                user.setUsername(rs.getString(2));
                user.setUserFullname(rs.getString(3));
                user.setUserDateCreated(rs.getDate(4));
                user.setUserCreatedBy(rs.getString(5));
                //user.setUserType(rs.getString(6));
                java.lang.System.out.println(rs.getString(3));
                users.add(user);
                k++;
            }

            cst.close();
            rs.close();
            conn.close();
            // session.setAttribute("usrGrpDate",null);

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return users;
        }
        return users;
    }

    public List<OrgDivisionLevelType> fetchOrgLevelTypes() {
        List<OrgDivisionLevelType> typeList =
            new ArrayList<OrgDivisionLevelType>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_SETUPS_CURSOR.getOrgDivLevelsType;end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            //          if (session.getAttribute("sysCode") != null) {
            //              BigDecimal sysCode =
            //                  new BigDecimal(session.getAttribute("sysCode").toString());

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            //   statement.setBigDecimal(2, sysCode);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                OrgDivisionLevelType levelType = new OrgDivisionLevelType();
                levelType.setDltCode(resultSet.getString(1));
                levelType.setDltSysCode(resultSet.getBigDecimal(2));
                levelType.setDltDesc(resultSet.getString(3));
                levelType.setDltActCode(resultSet.getBigDecimal(4));
                levelType.setAccountTypeName(resultSet.getString(5));
                levelType.setCode(resultSet.getBigDecimal(6));
                typeList.add(levelType);
            }
            statement.close();
            resultSet.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return typeList;
    }


    public List<OrgDivisionLevelType> fetchDivisionLocation() {
        List<OrgDivisionLevelType> typeList =
            new ArrayList<OrgDivisionLevelType>();

        if (session.getAttribute("odlType") != null) {
            DBConnector dbConnector = new DBConnector();
            String query =
                "begin ? := TQC_SETUPS_CURSOR.get_org_division_location(?);end;";
            CallableStatement statement = null;
            OracleConnection connection = null;

            try {
                connection = dbConnector.getDatabaseConnection();
                statement = connection.prepareCall(query);

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setString(2,
                                    session.getAttribute("odlType").toString());
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    OrgDivisionLevelType levelType =
                        new OrgDivisionLevelType();
                    levelType.setDivLocationCode(resultSet.getBigDecimal(1));
                    levelType.setDivLocationType(resultSet.getString(2));
                    levelType.setDivLocationName(resultSet.getString(3));
                    ;
                    typeList.add(levelType);
                }
                statement.close();
                resultSet.close();
                connection.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
        }
        return typeList;
    }
}
