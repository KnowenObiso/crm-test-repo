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
import TurnQuest.view.models.SystemPost;
import TurnQuest.view.models.SystemPostLevel;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.internal.OracleTypes;


public class SystemPostDAO {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public SystemPostDAO() {
        super();
    }

    public List<SystemPostLevel> fetchSystemPostLevels() {
        List<SystemPostLevel> postLevelList = new ArrayList<SystemPostLevel>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getSysPostLevelsBySys(?);end;";
        CallableStatement statement = null;
        Connection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("sysCode") != null) {
                BigDecimal sysCode =
                    new BigDecimal(session.getAttribute("sysCode").toString());

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, sysCode);
                statement.execute();
                ResultSet resultSet = (ResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    SystemPostLevel postLevel = new SystemPostLevel();
                    postLevel.setSysplSysCode(resultSet.getBigDecimal(1));
                    postLevel.setSysplCode(resultSet.getBigDecimal(2));
                    postLevel.setSysplShtDesc(resultSet.getString(3));
                    postLevel.setSysplDesc(resultSet.getString(4));
                    postLevel.setSysplRanking(resultSet.getBigDecimal(5));
                    postLevel.setSysplWef(resultSet.getDate(6));

                    postLevelList.add(postLevel);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING((OracleConnection)connection, e);
        }
        return postLevelList;
    }

    public List<SystemPost> fetchSystemPostsBySystem() {
        List<SystemPost> postList = new ArrayList<SystemPost>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getSysPostsBySystem(?);end;";
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
                ResultSet resultSet = (ResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    SystemPost systemPost = new SystemPost();
                    systemPost.setSpostSysCode(resultSet.getBigDecimal(1));
                    systemPost.setSpostSysplCode(resultSet.getBigDecimal(2));
                    systemPost.setSpostParentSpostCode(resultSet.getBigDecimal(3));
                    systemPost.setSpostCode(resultSet.getBigDecimal(4));
                    systemPost.setSpostShtDesc(resultSet.getString(5));
                    systemPost.setSpostDesc(resultSet.getString(6));
                    systemPost.setSpostRemarks(resultSet.getString(7));
                    systemPost.setSpostWef(resultSet.getDate(8));
                    systemPost.setSpostBrnCode(resultSet.getBigDecimal(9));
                    systemPost.setSpostSubdivOsdCode(resultSet.getString(10));
                    systemPost.setSpostUsrCode(resultSet.getBigDecimal(11));

                    systemPost.setSystemPostList(fetchSystemPostsByPost(systemPost.getSpostCode()));

                    postList.add(systemPost);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return postList;
    }

    public List<SystemPost> fetchSystemPostsByLevel() {
        List<SystemPost> postList = new ArrayList<SystemPost>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getSysPostsByLevel(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("sysplCode") != null) {
                BigDecimal sysplCode =
                    new BigDecimal(session.getAttribute("sysplCode").toString());

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, sysplCode);
                statement.execute();
                ResultSet resultSet = (ResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    SystemPost systemPost = new SystemPost();
                    systemPost.setSpostSysCode(resultSet.getBigDecimal(1));
                    systemPost.setSpostSysplCode(resultSet.getBigDecimal(2));
                    systemPost.setSpostParentSpostCode(resultSet.getBigDecimal(3));
                    systemPost.setSpostCode(resultSet.getBigDecimal(4));
                    systemPost.setSpostShtDesc(resultSet.getString(5));
                    systemPost.setSpostDesc(resultSet.getString(6));
                    systemPost.setSpostRemarks(resultSet.getString(7));
                    systemPost.setSpostWef(resultSet.getDate(8));
                    systemPost.setSpostBrnCode(resultSet.getBigDecimal(9));
                    systemPost.setSpostSubdivOsdCode(resultSet.getString(10));
                    systemPost.setSpostUsrCode(resultSet.getBigDecimal(11));

                    systemPost.setSystemPostList(fetchSystemPostsByPost(systemPost.getSpostCode()));

                    postList.add(systemPost);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return postList;
    }

    public List<SystemPost> fetchBankBranches() {
        List<SystemPost> postList = new ArrayList<SystemPost>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.all_bank__branches(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            String brn_name;
            if (session.getAttribute("brn_name") == null ||
                session.getAttribute("brn_name").equals("")) {
                brn_name = null;
            } else {
                brn_name = session.getAttribute("brn_name").toString();
            }
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setString(2, brn_name);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                SystemPost bankbranches = new SystemPost();
                bankbranches.setBBR_CODE(resultSet.getBigDecimal(1));
                bankbranches.setBBR_BNK_CODE(resultSet.getBigDecimal(2));
                bankbranches.setBBR_BRANCH_NAME(resultSet.getString(3));
                bankbranches.setBBR_REMARKS(resultSet.getString(4));
                bankbranches.setBBR_SHT_DESC(resultSet.getString(5));
                //  bankbranches.setBBR_REF_CODE( resultSet.getBigDecimal( 6 ) );
                // bankbranches.setBBR_EFT_SUPPORTED( resultSet.getString( 7 ) );
                //  bankbranches.setBBR_DD_SUPPORTED( resultSet.getString( 8 ) );
                bankbranches.setBBR_DATE_CREATED(resultSet.getDate(9));
                bankbranches.setBBR_CREATED_BY(resultSet.getString(10));
                bankbranches.setBBR_PHYSICAL_ADDRS(resultSet.getString(11));
                if(bankbranches.getSpostCode()!=null)
                bankbranches.setSystemPostList(fetchSystemPostsByPost(bankbranches.getSpostCode()));
                postList.add(bankbranches);
            }
            statement.close();
            resultSet.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return postList;
    }

    public List<SystemPost> fetchBankBranchDetails() {
        List<SystemPost> postList = new ArrayList<SystemPost>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.get_branches_details(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (session.getAttribute("BBR_CODE") != null) {
                BigDecimal brn_Code =
                    new BigDecimal(session.getAttribute("BBR_CODE").toString());
                System.out.println("BBR_CODE" +
                                   session.getAttribute("BBR_CODE"));
                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, brn_Code);
                statement.execute();
                ResultSet resultSet = (ResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    SystemPost bankbranches = new SystemPost();
                    bankbranches.setBBB_CODE(resultSet.getBigDecimal(1));
                    bankbranches.setBBB_BRN_CODE(resultSet.getBigDecimal(2));
                    bankbranches.setBBB_BBR_SHT_DESC(resultSet.getString(3));
                    bankbranches.setBBB_BRN_REG_CODE(resultSet.getBigDecimal(4));
                    bankbranches.setBBB_BRN_NAME(resultSet.getString(5));
                    bankbranches.setBBB_BRN_PHY_ADDRS(resultSet.getString(6));
                    bankbranches.setBBB_BBR_CODE(resultSet.getBigDecimal(7));
                    bankbranches.setBBB_BBR_BNK_CODE(resultSet.getBigDecimal(8));
                    bankbranches.setBBB_BBR_BRANCH_NAME(resultSet.getString(9));
                    bankbranches.setBBB_BBR_SHT_DESC(resultSet.getString(10));
                    bankbranches.setBBB_BRN_PHY_ADDRS(resultSet.getString(11));
                    bankbranches.setSystemPostList(fetchSystemPostsByPost(bankbranches.getSpostCode()));
                    postList.add(bankbranches);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return postList;
    }

    public List<SystemPost> fetchSystemPostsByPost(BigDecimal spostCode) {
        List<SystemPost> postList = new ArrayList<SystemPost>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getSysPostsByPost(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            if (spostCode != null) {
                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, spostCode);
                statement.execute();
                ResultSet resultSet = (ResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    SystemPost systemPost = new SystemPost();
                    systemPost.setSpostSysCode(resultSet.getBigDecimal(1));
                    systemPost.setSpostSysplCode(resultSet.getBigDecimal(2));
                    systemPost.setSpostParentSpostCode(resultSet.getBigDecimal(3));
                    systemPost.setSpostCode(resultSet.getBigDecimal(4));
                    systemPost.setSpostShtDesc(resultSet.getString(5));
                    systemPost.setSpostDesc(resultSet.getString(6));
                    systemPost.setSpostRemarks(resultSet.getString(7));
                    systemPost.setSpostWef(resultSet.getDate(8));
                    systemPost.setSpostBrnCode(resultSet.getBigDecimal(9));
                    systemPost.setSpostSubdivOsdCode(resultSet.getString(10));
                    systemPost.setSpostUsrCode(resultSet.getBigDecimal(11));

                    // Use Recursion to get the child SystemPost by the parent SystemPost
                    systemPost.setSystemPostList(fetchSystemPostsByPost(systemPost.getSpostCode()));

                    postList.add(systemPost);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return postList;
    }

}
