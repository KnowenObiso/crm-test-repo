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
import TurnQuest.view.Base.IQuery;
import TurnQuest.view.Base.IQueryAction;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.DirectDebitReport;
import TurnQuest.view.setups.Bank;
import TurnQuest.view.setups.BankBranch;
import TurnQuest.view.setups.BankTerritory;

import java.math.BigDecimal;

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

import org.apache.commons.dbutils.DbUtils;


public class BankDAO {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public BankDAO() {
        super();
    }

    /**
     * Fetch a list of all the <code>Bank</code> objects/records
     *
     * @return A list of all <code>Bank</code> objects/records
     */
    public List<Bank> fetchAllBanks() {

        List<Bank> banksList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_banks(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            banksList = new ArrayList<Bank>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                Bank bank = new Bank();
                bank.setBankCode(resultSet.getString(1));
                bank.setBankName(resultSet.getString(2));
                bank.setRemarks(resultSet.getString(3));
                bank.setShortDesc(resultSet.getString(4));
                bank.setDDRCode(resultSet.getString(5));
                bank.setDDFormatDesc(resultSet.getString(6));
                bank.setForwardingBankCode(resultSet.getString(7));
                bank.setKBACode(resultSet.getString(8));
                bank.setFwdBankName(resultSet.getString(9));
                bank.setDdReportFormat(resultSet.getString(10));
                bank.setEftSupported(resultSet.getString(11));
                bank.setClassType(resultSet.getString(12));
                bank.setCharacterNo(resultSet.getBigDecimal(13));
                bank.setNegotiatedBank(resultSet.getString(14));
                bank.setAdministativeAmnt(resultSet.getBigDecimal(15));
                bank.setLogo(resultSet.getBlob(16));
                bank.setBnkWef(resultSet.getDate(17));
                bank.setBnkWet(resultSet.getDate(18));
                bank.setBnkStatus(resultSet.getString(19));
                bank.setCharacterMaxNo(resultSet.getString(20));
                bank.setCharacterMinNo(resultSet.getString(21));
                bank.setCountryCode(resultSet.getString(22));
                bank.setCountryName(resultSet.getString(23));
                
                //bank.setCharacterMaxNo(resultSet.getString(20));
                //bank.setCharacterMinNo(resultSet.getString(21));
                //bank.setCountryCode(resultSet.getString(22));
                //bank.setCountryName(resultSet.getString(23));
                banksList.add(bank);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return banksList;
    }

    /**
     * Fetch a list of all the <code>BankBranch</code> objects/records with a
     * given bank code.
     *
     * @return A list of all <code>BankBranch</code> objects/records
     */
    public List<BankBranch> fetchBankBranchByBankCode() {
        List<BankBranch> bankBranchesList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_bank_branches_by_bank_code(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            bankBranchesList = new ArrayList<BankBranch>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);
            System.out.println("BankCode");
            System.out.println(session.getAttribute("bankCode"));
            if (session.getAttribute("bankCode") != null) {
                statement.setString(1,
                                    session.getAttribute("bankCode").toString());
                statement.registerOutParameter(2, OracleTypes.CURSOR);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(2);

                while (resultSet.next()) {
                    BankBranch bankBranch = new BankBranch();
                    bankBranch.setBranchCode(resultSet.getString(1));
                    bankBranch.setBranchBankCode(resultSet.getString(2));
                    bankBranch.setBranchName(resultSet.getString(3));
                    bankBranch.setRemarks(resultSet.getString(4));
                    bankBranch.setShortDesc(resultSet.getString(5));
                    bankBranch.setRefCode(resultSet.getString(6));
                    bankBranch.setEFTSupported(resultSet.getString(7));
                    bankBranch.setDDSupported(resultSet.getString(8));
                    bankBranch.setDateCreated(resultSet.getDate(9));
                    bankBranch.setCreatedBy(resultSet.getString(10));
                    bankBranch.setPhysicalAddress(resultSet.getString(11));
                    bankBranch.setPostalAddress(resultSet.getString(12));
                    bankBranch.setKBACode(resultSet.getString(13));
                    bankBranch.setBankTerritoryCode(resultSet.getString(14));
                    bankBranch.setBankTerritoryName(resultSet.getString(15));                  

                    bankBranchesList.add(bankBranch);
                }
                resultSet.close();
                statement.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
            GlobalCC.INFORMATIONREPORTING(e.getMessage());
        }
        return bankBranchesList;
    }


    public List<BankBranch> fetchBankBranchByTerritoryCode() {
            List<BankBranch> bankBranchesList = null;
            DBConnector dbConnector = new DBConnector();   
            OracleCallableStatement statement = null;
            OracleConnection connection = null;
            OracleResultSet resultSet =null;
            try {
                if(!GlobalCC.tableExists("tqc_bank_branches")) 
                {
                    return bankBranchesList;
                }
                bankBranchesList = new ArrayList<BankBranch>();
                //System.out.println("BankDAO.fetchBankBranchByTerritoryCode(): territoryCode:"+session.getAttribute("territoryCode")+
                                  // "session.getAttribute(\"bankCode\")"+session.getAttribute("bankCode"));
                connection = dbConnector.getDatabaseConnection();
                String query =
                    "                    SELECT bbr_code, bbr_bnk_code, bbr_branch_name, bbr_remarks," + 
                    "                        bbr_sht_desc, bbr_ref_code, bbr_eft_supported," + 
                    "                        bbr_dd_supported, bbr_date_created, bbr_created_by," + 
                    "                        bbr_physical_addrs, bbr_postal_addrs, bbr_kba_code," + 
                    "                        bbr_email, bbr_person_name, bbr_person_email," +
                    "                        bbr_person_cou_code, bbr_person_phone"+
                    "                   FROM tqc_bank_branches " + 
                    "                  WHERE ((bbr_bnkt_code = ?) or (bbr_bnkt_code is null)) and" + 
                    "                   bbr_bnk_code = ?";
                statement = (OracleCallableStatement)connection.prepareCall(query);
               
                statement.setBigDecimal(1,GlobalCC.checkBDNullValues(session.getAttribute("territoryCode"))); 
                statement.setString(2,(String)session.getAttribute("bankCode"));     
                resultSet =
                                (OracleResultSet)statement.executeQuery();
                while (resultSet.next()) {
                    BankBranch bankBranch = new BankBranch();
                    bankBranch.setBranchCode(resultSet.getString(1));
                    bankBranch.setBranchBankCode(resultSet.getString(2));
                    bankBranch.setBranchName(resultSet.getString(3));
                    bankBranch.setRemarks(resultSet.getString(4));
                    bankBranch.setShortDesc(resultSet.getString(5));
                    bankBranch.setRefCode(resultSet.getString(6));
                    bankBranch.setEFTSupported(resultSet.getString(7));
                    bankBranch.setDDSupported(resultSet.getString(8));
                    bankBranch.setDateCreated(resultSet.getDate(9));
                    bankBranch.setCreatedBy(resultSet.getString(10));
                    bankBranch.setPhysicalAddress(resultSet.getString(11));
                    bankBranch.setPostalAddress(resultSet.getString(12));
                    bankBranch.setKBACode(resultSet.getString(13));
                    //bankBranch.setBankTownCode(resultSet.getBigDecimal(14));
                    //bankBranch.setBankTownName(resultSet.getString(15));
                    bankBranch.setBranchEmail(resultSet.getString(14));
                    bankBranch.setBranchPersonName(resultSet.getString(15));
                    bankBranch.setBranchPersonEmail(resultSet.getString(16));
                    bankBranch.setCountryCode(resultSet.getString(17));
                    bankBranch.setBranchPersonPhone(resultSet.getString(18));
                    bankBranchesList.add(bankBranch);
                }
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
                GlobalCC.INFORMATIONREPORTING(e.getMessage());
          }finally{
                DbUtils.closeQuietly(connection, statement, resultSet);
           }
            return bankBranchesList;
        }
    
    public List<BankBranch> fetchCountryCode() {
            List<BankBranch> countryCodes = null;
            DBConnector dbConnector = new DBConnector();   
            OracleCallableStatement statement = null;
            OracleConnection connection = null;
            OracleResultSet resultSet =null;
            try {
                if(!GlobalCC.tableExists("tqc_countries")) 
                {
                    return countryCodes;
                }
                countryCodes = new ArrayList<BankBranch>();
                connection = dbConnector.getDatabaseConnection();
                String query ="SELECT cou_zip_code FROM tqc_countries WHERE cou_zip_code IS NOT NULL";
                statement = (OracleCallableStatement)connection.prepareCall(query); 
                resultSet =(OracleResultSet)statement.executeQuery();
                
                while (resultSet.next()) {
                    BankBranch bankBranch = new BankBranch();
                    bankBranch.setCountryCode(resultSet.getString(1));         
                    countryCodes.add(bankBranch);
                }
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
                GlobalCC.INFORMATIONREPORTING(e.getMessage());
          }finally{
                DbUtils.closeQuietly(connection, statement, resultSet);
           }
            return countryCodes;
        }


    /**
         * Fetch a list of all the <code>BankBranch</code> objects/records with a
         * given bank code.
         *
         * @return A list of all <code>BankBranch</code> objects/records
         */
        public List<BankTerritory> fetchBankTerritoryByBankCode() {
            List<BankTerritory>  territories = null;
            DBConnector dbConnector = new DBConnector();
            String query ="SELECT bnkt_code, bnkt_territory_name, bnkt_sht_desc, bnkt_bnk_code FROM tqc_bank_territories WHERE bnkt_bnk_code = ?";
                   
            OracleCallableStatement statement = null;
            OracleConnection connection = null;
            OracleResultSet resultSet =null;
            try {
                        territories = new ArrayList<BankTerritory>();
                        System.out.println("BankDAO.fetchBankTerritoryByBankCode(): BankCode: "+session.getAttribute("bankCode"));
                        if (session.getAttribute("bankCode") != null) {
                            connection = dbConnector.getDatabaseConnection();
                            statement = (OracleCallableStatement)connection.prepareCall(query);
                            statement.setString(1, session.getAttribute("bankCode").toString());
                            resultSet =
                                            (OracleResultSet)statement.executeQuery();
                            while (resultSet.next()) {
                                BankTerritory  territory = new BankTerritory();
                                territory.setBankTerritoryCode(resultSet.getString(1));
                                territory.setBankTerritoryName(resultSet.getString(2));
                                territory.setBankTerritoryShtDesc(resultSet.getString(3));
                                territory.setTerritoryBankCode(resultSet.getString(4));
                                System.out.println("Territory: "+territory.getBankTerritoryName());
                                territories.add( territory);
                            }
                        }
             } catch (Exception e) {
                        GlobalCC.EXCEPTIONREPORTING(connection, e);
                        GlobalCC.INFORMATIONREPORTING(e.getMessage());
              }finally{
                       DbUtils.closeQuietly(connection, statement, resultSet);
             }
            return territories;
        }


    public List<Bank> fetchAllForwadingBanks() {
        List<Bank> fwdBanksList = new ArrayList<Bank>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getDDFwdingBanksLov();end;";
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
                Bank bank = new Bank();
                bank.setBankCode(resultSet.getString(1));
                bank.setDdrReportDesc(resultSet.getString(2));
                bank.setBankName(resultSet.getString(3));

                fwdBanksList.add(bank);
            }
            statement.close();
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return fwdBanksList;
    }

    public List<DirectDebitReport> fetchAllDirectDebitReports() {
        List<DirectDebitReport> ddReportList =
            new ArrayList<DirectDebitReport>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getDirectDebitReports();end;";
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
                DirectDebitReport ddReport = new DirectDebitReport();
                ddReport.setDdrCode(resultSet.getBigDecimal(1));
                ddReport.setDdrReportDesc(resultSet.getString(2));

                ddReportList.add(ddReport);
            }
            statement.close();
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return ddReportList;
    }

    public List<DirectDebitReport> fetchDDFailedRemarks() {
        List<DirectDebitReport> ddReportList =
            new ArrayList<DirectDebitReport>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getDDApplicationRmk();end;";
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
                DirectDebitReport ddReport = new DirectDebitReport();
                ddReport.setDfrCode(resultSet.getBigDecimal(1));
                ddReport.setDfrFailedRemarks(resultSet.getString(2));
                ddReport.setDfrApplicationLevel(resultSet.getString(3));

                ddReportList.add(ddReport);
            }
            statement.close();
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return ddReportList;
    }
    public List<BankAccount> fetchBankAccounts() {
        List<BankAccount> accountList = new ArrayList<BankAccount>();
        
        String query ="SELECT BACT_CODE,\n" + 
        "BACT_NAME,\n" + 
        "BACT_ACCOUNT_NO,\n" + 
        "BACT_BBR_CODE,\n" + 
        "USR_USERNAME BACT_ACCOUNT_OFFICER,\n" +
        "BACT_CELL_NOS,\n" + 
        "BACT_TEL_NOS,\n" + 
        "BACT_ACCOUNT_TYPE,\n" + 
        "BACT_DEFAULT,\n" + 
        "BACT_CUR_CODE,\n" + 
        "BACT_ACC_CODE,\n" + 
        "BBR_BRANCH_NAME BACT_BANK_BRANCH,\n" + 
        "CUR_DESC BACT_CURRENCY,\n" + 
        "USR_CODE BACT_USER_CODE,\n" + 
        "BNK_BANK_NAME BACT_BANK_NAME,\n" + 
        "BACT_IBAN,\n" + 
        "BACT_STATUS\n" + 
        "FROM tqc_bank_accounts,tqc_bank_branches,tqc_currencies,tqc_users,tqc_banks \n" + 
        "WHERE BACT_ACCOUNT_TYPE= :v_account_type " +
        "AND BACT_ACCOUNT_OFFICER= USR_CODE(+) " +
        "AND BBR_BNK_CODE =BNK_CODE(+) " +
        "AND  BACT_BBR_CODE = BBR_CODE(+)"+
        "AND  BACT_CUR_CODE = CUR_CODE(+) " +
        "AND BACT_ACC_CODE = :v_account_code\n"; 
        
        BigDecimal bactAccCode=GlobalCC.checkBDNullValues(session.getAttribute("bactAccCode"));
        String bactAccountType=GlobalCC.checkNullValues(session.getAttribute("bactAccountType"));
        
        query=query.replaceAll(":v_account_type","'"+ (bactAccountType!=null?bactAccountType: "null")+"'");
        query=query.replaceAll(":v_account_code", bactAccCode!=null?bactAccCode.toString(): "null");
        System.out.println("query"+query);
        IQuery.fetchResult(query,accountList,new IQueryAction() {  
                        public void fetch(ResultSet rs,Object o) {
                                        try{ 
                                                while (rs.next()) { 
                                                List<BankAccount> accountList=(List<BankAccount>)o;
                                                      BankAccount account = new BankAccount();
                                                      account.setBACT_CODE(rs.getBigDecimal(1));
                                                      account.setBACT_NAME(rs.getString(2));
                                                      account.setBACT_ACCOUNT_NO(rs.getString(3));
                                                      account.setBACT_BBR_CODE(rs.getBigDecimal(4)); 
                                                      account.setBACT_ACCOUNT_OFFICER(rs.getString(5));
                                                      account.setBACT_CELL_NOS(rs.getString(6));
                                                      account.setBACT_TEL_NOS(rs.getString(7));
                                                      account.setBACT_ACCOUNT_TYPE(rs.getString(8));
                                                      account.setBACT_DEFAULT(rs.getString(9));
                                                      account.setBACT_CUR_CODE(rs.getBigDecimal(10));
                                                      account.setBACT_ACC_CODE(rs.getBigDecimal(11));
                                                      account.setBACT_BANK_BRANCH(rs.getString(12));
                                                      account.setBACT_CURRENCY(rs.getString(13));
                                                      account.setBACT_USER_CODE(rs.getString(14));
                                                      account.setBACT_BANK_NAME(rs.getString(15));
                                                      account.setBACT_IBAN(rs.getString(16));
                                                      account.setBACT_STATUS(rs.getString(17));
						      accountList.add(account);
                                                    System.out.println("Code: "+account.getBACT_CODE()+ " Name: "+account.getBACT_NAME()); 
                                                  }
                                         } catch (Exception e) {
                                         GlobalCC.EXCEPTIONREPORTING( e);
                                     }
                                }
        });
        return accountList;
    }
    public List<BankAccount> fetchLoadedBankAccounts() {
        List<BankAccount> accountList = new ArrayList<BankAccount>();
        
        String query ="SELECT BACT_CODE,\n" + 
        "BACT_NAME,\n" + 
        "BACT_ACCOUNT_NO,\n" + 
        "BACT_BBR_CODE,\n" + 
        "USR_USERNAME BACT_ACCOUNT_OFFICER,\n" +
        "BACT_CELL_NOS,\n" + 
        "BACT_TEL_NOS,\n" + 
        "BACT_ACCOUNT_TYPE,\n" + 
        "BACT_DEFAULT,\n" + 
        "BACT_CUR_CODE,\n" + 
        "BACT_ACC_CODE,\n" + 
        "BBR_BRANCH_NAME BACT_BANK_BRANCH,\n" + 
        "CUR_DESC BACT_CURRENCY,\n" + 
        "USR_CODE BACT_USER_CODE,\n" + 
        "BNK_BANK_NAME BACT_BANK_NAME,\n" + 
        "BACT_IBAN,\n" + 
        "BACT_STATUS\n" + 
        "FROM tqc_bank_accounts,tqc_bank_branches,tqc_currencies,tqc_users,tqc_banks \n" + 
        "WHERE BACT_ACCOUNT_OFFICER= USR_CODE(+) " +
        "AND BBR_BNK_CODE =BNK_CODE(+) " +
        "AND  BACT_BBR_CODE = BBR_CODE(+)"+
        "AND  BACT_CUR_CODE = CUR_CODE(+) " ;  
        
        System.out.println("query: "+query);
        IQuery.fetchResult(query,accountList,new IQueryAction() {  
                        public void fetch(ResultSet rs,Object o) {
                                        try{ 
                                                while (rs.next()) { 
                                                List<BankAccount> accountList=(List<BankAccount>)o;
                                                      BankAccount account = new BankAccount();
                                                      account.setBACT_CODE(rs.getBigDecimal(1));
                                                      account.setBACT_NAME(rs.getString(2));
                                                      account.setBACT_ACCOUNT_NO(rs.getString(3));
                                                      account.setBACT_BBR_CODE(rs.getBigDecimal(4)); 
                                                      account.setBACT_ACCOUNT_OFFICER(rs.getString(5));
                                                      account.setBACT_CELL_NOS(rs.getString(6));
                                                      account.setBACT_TEL_NOS(rs.getString(7));
                                                      account.setBACT_ACCOUNT_TYPE(rs.getString(8));
                                                      account.setBACT_DEFAULT(rs.getString(9));
                                                      account.setBACT_CUR_CODE(rs.getBigDecimal(10));
                                                      account.setBACT_ACC_CODE(rs.getBigDecimal(11));
                                                      account.setBACT_BANK_BRANCH(rs.getString(12));
                                                      account.setBACT_CURRENCY(rs.getString(13));
                                                      account.setBACT_USER_CODE(rs.getString(14));
                                                      account.setBACT_BANK_NAME(rs.getString(15));
                                                      account.setBACT_IBAN(rs.getString(16));
                                                      account.setBACT_STATUS(rs.getString(17));
                                                      accountList.add(account);
                                                      System.out.println("Code: "+account.getBACT_CODE()+ " Name: "+account.getBACT_NAME()); 
                                                  }
                                         } catch (Exception e) {
                                         GlobalCC.EXCEPTIONREPORTING( e);
                                     }
                                }
        });
        return accountList;
    }
    public List<BankBranch> fetchBancassuranceByBranchCode() {
        List<BankBranch> bankBranchesList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_bancassurance_branches(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            bankBranchesList = new ArrayList<BankBranch>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);
            System.out.println(session.getAttribute("branchCode"));
            if (session.getAttribute("branchCode") != null) {
                statement.setString(1,
                                    session.getAttribute("branchCode").toString());
                statement.registerOutParameter(2, OracleTypes.CURSOR);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(2);

                while (resultSet.next()) {
                    BankBranch bankBranch = new BankBranch();
                    bankBranch.setBranchCode(resultSet.getString(1));
                    bankBranch.setBranchBankCode(resultSet.getString(2));
                    bankBranch.setBranchName(resultSet.getString(3));
                    bankBranch.setRemarks(resultSet.getString(4));
                    bankBranch.setShortDesc(resultSet.getString(5));
                    bankBranch.setDateCreated(resultSet.getDate(6));
                    bankBranch.setCreatedBy(resultSet.getString(7));
                    bankBranch.setPhysicalAddress(resultSet.getString(8));
                    bankBranch.setPostalAddress(resultSet.getString(9));
                    bankBranch.setKBACode(resultSet.getString(10));
                    bankBranch.setBankTerritoryCode(resultSet.getString(11));
                    bankBranch.setBankTerritoryName(resultSet.getString(12));   
                    bankBranch.setBranchEmail(resultSet.getString(13));
                    bankBranch.setBranchPersonEmail(resultSet.getString(15));
                    bankBranch.setBranchPersonName(resultSet.getString(14));
                    bankBranch.setBranchPersonPhone(resultSet.getString(17));
                    bankBranch.setCountryCode(resultSet.getString(16));
                    bankBranchesList.add(bankBranch);
                }
                resultSet.close();
                statement.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
            GlobalCC.INFORMATIONREPORTING(e.getMessage());
        }
        return bankBranchesList;
    }

}
