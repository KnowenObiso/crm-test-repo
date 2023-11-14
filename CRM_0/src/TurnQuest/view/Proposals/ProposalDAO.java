package TurnQuest.view.Proposals;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.sql.Date;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;


public class ProposalDAO {
    public ProposalDAO() {
    }
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public List<Proposal> findProposal() {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<Proposal> proposal = new ArrayList<Proposal>();
        try {

            String proposalQuery =
                "begin LMS_WEB_CURSOR.Mktpropnext(?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(proposalQuery);
            if (session.getAttribute("transCode") == null) {

            }
            //register out
            session.removeAttribute("loanable");
            session.removeAttribute("mortgage");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            if (session.getAttribute("policyProposalCode") == null) {
                Proposal prop = new Proposal();
                prop.setPprPolTerm(null);
                prop.setPremiumAmount(GlobalCC.zeroValue);
                proposal.add(prop);
            } else {
                cst.setBigDecimal(2,
                                  (BigDecimal)session.getAttribute("policyProposalCode"));
                cst.execute();
                OracleResultSet rs = (OracleResultSet)cst.getObject(1);
                while (rs.next()) {
                    session.setAttribute("resetValue", true);
                    Proposal prop = new Proposal();
                    prop.setPprCode(rs.getBigDecimal(1));
                    prop.setTransactionNumber(rs.getBigDecimal(2));
                    prop.setProposalNumber(rs.getString(3));
                    String exists = (rs.getString(3) == null) ? "N" : "Y";
                    session.setAttribute("proposalExists", exists);
                    prop.setClient(rs.getString(4));
                    session.setAttribute("clientName", prop.getClient());
                    session.setAttribute("ClientCode",
                                         rs.getBigDecimal(19));
                    prop.setBranchName(rs.getString(5));
                    session.setAttribute("sysBranchDesc",
                                         prop.getBranchName());
                    session.setAttribute("sysBranchCode",
                                         rs.getBigDecimal(20));
                    prop.setAgentName(rs.getString(6));
                    session.setAttribute("agentName", prop.getAgentName());
                    session.setAttribute("agentCode", rs.getBigDecimal(21));
                    session.setAttribute("agentShtDesc", rs.getString(22));
                    prop.setProductDescription(rs.getString(7));
                    session.setAttribute("productDesc",
                                         prop.getProductDescription());
                    prop.setProductCode(rs.getBigDecimal(8));
                    session.setAttribute("productCode", rs.getBigDecimal(8));
                    session.setAttribute("calcTrmRtAge",
                                         GlobalCC.findCalcTermRetAgeMkt(rs.getBigDecimal(8)));
                    session.setAttribute("manLcf",
                                         GlobalCC.findManLifeCover(rs.getBigDecimal(8)));
                    session.setAttribute("productType",
                                         GlobalCC.findProdType(rs.getBigDecimal(8)));
                    prop.setLifeRider(GlobalCC.findProdLifeRider(rs.getBigDecimal(8)));
                    session.setAttribute("loanable",
                                         GlobalCC.findProductLoanable(rs.getBigDecimal(8),
                                                                      conn));
                    session.setAttribute("mortgage",
                                         GlobalCC.findIfMortgageInterest(rs.getBigDecimal(8),
                                                                         conn));
                    prop.setPprPolTerm(rs.getString(9));


                    String useTerm =
                        GlobalCC.findProplifeCoverFact(rs.getBigDecimal(8));
                    if (useTerm != null) {
                        if (useTerm.equalsIgnoreCase("Y")) {
                            prop.setLifeCoverFactor(rs.getString(9));
                            session.setAttribute("lcf", true);
                        } else {
                            //prop.setLifeCoverFactor("");
                        }
                    } else {
                        // session.setAttribute("useTerm", "");
                    }

                    prop.setPremiumAmount(rs.getBigDecimal(10));
                    //MarketingProposal.premiumAmount = prop.getPremiumAmount();
                    prop.setPprMippContr(rs.getString(11));
                    prop.setPaymentFrequencyCode(rs.getString(12));
                    prop.setPaymentFrequencyDesc(GlobalCC.decodePaymentFrequency(rs.getString(12)));
                    session.setAttribute("payFreqDesc",
                                         prop.getPaymentFrequencyDesc());

                    session.setAttribute("payFreqCode",
                                         prop.getPaymentFrequencyCode());
                    //System.out.println(prop.getPaymentFrequencyCode());
                    prop.setPaymentModeCode(rs.getString(13));
                    prop.setPaymentModeDesc(GlobalCC.decodePaymentMode(rs.getString(13)));
                    session.setAttribute("payModeCode",
                                         prop.getPaymentModeCode());
                    session.setAttribute("payModeDesc",
                                         prop.getPaymentModeDesc());
                    prop.setStatus(rs.getString(14));
                    /*String Status = rs.getString(14);
                    if(session.getAttribute("processInstance")!=null){
                      JBPMEngine bpm = new JBPMEngine();
                      session.setAttribute("decision", "Yes");
                      String processId = null;
                      processId = bpm.findProcessInstanceWhetherStarted(((BigDecimal)session.getAttribute("policyProposalCode")).toString(),"MK");

                      String name = null;
                      name = bpm.findProcessInstanceTaskName(processId);
                      String taskID = null;
                      taskID = bpm.findProcessInstanceTaskId(processId);
                      session.setAttribute("activityName", name);
                      session.setAttribute("ticketID", taskID);
                      session.setAttribute("processInstance", processId);
                      bpm.completeTaskByTaskId(taskID);

                      String active = null;

                      if(Status.equalsIgnoreCase("U")||Status.equalsIgnoreCase("X")||Status.equalsIgnoreCase("C")){
                          active = "N";
                        //System.out.println(name);
                        //System.out.println(taskID);
                          name = bpm.findProcessInstanceTaskName(processId);
                          taskID = bpm.findProcessInstanceTaskId(processId);
                          bpm.saveTicketDetails(processId,((BigDecimal)session.getAttribute("policyProposalCode")).toString(),taskID,(String)session.getAttribute("module"),name,active);
                        //System.out.println(name);
                        //System.out.println(taskID);
                        //System.out.println(processId);
                          session.setAttribute("module", null);
                          session.setAttribute("enProposal", null);
                      }
                    }
                  */
                    prop.setPreparedBy(rs.getString(15));
                    prop.setPprBankAccountNo(rs.getString(16));
                    prop.setBank(rs.getString(17));
                    prop.setEffectiveDate(rs.getDate(18));
                    session.setAttribute("effDate", rs.getDate(18));
                    session.setAttribute("anb",
                                         GlobalCC.findAnb((BigDecimal)session.getAttribute("ClientCode"),
                                                          "N", null, null,
                                                          rs.getString(18)));
                    prop.setPrpInstCode(rs.getBigDecimal(23));
                    session.setAttribute("prpInstCode", rs.getBigDecimal(23));
                    prop.setPrpInstName(rs.getString(24));
                    prop.setDeductMonth(rs.getDate(25));
                    prop.setLaunchDate(rs.getDate(26));
                    prop.setBoDebitDay(rs.getBigDecimal(27));
                    prop.setBoStartDate(rs.getDate(28));
                    prop.setBoDebitDate(rs.getDate(29));
                    prop.setBbrCode(rs.getBigDecimal(30));
                    session.setAttribute("bankBranchCode",
                                         rs.getBigDecimal(30));
                    prop.setPopCode(rs.getBigDecimal(31));
                    session.setAttribute("popCode", rs.getBigDecimal(31));
                    prop.setPopDesc(rs.getString(32));
                    prop.setPayrollNo(rs.getString(33));
                    prop.setPprEdCode(rs.getString(34));
                    prop.setPprItemAccCode(rs.getString(35));
                    prop.setChildAge(rs.getBigDecimal(36));
                    session.setAttribute("childAge", rs.getBigDecimal(36));
                    prop.setPprCurrentSa(rs.getBigDecimal(37));
                    prop.setPprCoinsurance(rs.getString(38));
                    prop.setPprCoinsureLeader(rs.getString(39));
                    prop.setPprProposalSignDate(rs.getDate(40));
                    prop.setPprDate(rs.getDate(41));
                    prop.setPprUwDate(rs.getDate(42));
                    prop.setPprReturnToBrnDate(rs.getDate(43));
                    prop.setPprReturnFromBrnDate(rs.getDate(44));
                    prop.setPprAnnuityAmt(rs.getBigDecimal(45));
                    proposal.add(prop);
                }
                rs.close();

            }
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return proposal;

    }

    public List<Proposal> findLaunchData() {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<Proposal> proposal = new ArrayList<Proposal>();
        try {

            String proposalQuery =
                "begin LMS_WEB_CURSOR.Mktpropnext(?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(proposalQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            if (session.getAttribute("transCode") == null) {
                cst.setBigDecimal(2,
                                  (BigDecimal)session.getAttribute("policyProposalCode"));

                cst.execute();
                OracleResultSet rs = (OracleResultSet)cst.getObject(1);
                while (rs.next()) {

                }
                rs.close();

            }
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return proposal;

    }

    public List<Proposal> findPropCancDtls() {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<Proposal> propCancDtls = new ArrayList<Proposal>();
        try {

            String propCancDtlsQuery =
                "begin LMS_WEB_CURSOR.propCancDtls(?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(propCancDtlsQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("policyProposalCode"));

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                Proposal prop = new Proposal();
                prop.setRcptamt(rs.getBigDecimal(1));
                prop.setMedamt(rs.getBigDecimal(2));
                prop.setTotcommclwbk(rs.getBigDecimal(3));
                prop.setAdmin_chrg(rs.getBigDecimal(4));
                prop.setTotrefundamt(rs.getBigDecimal(5));
                prop.setCancelType(rs.getString(6));
                propCancDtls.add(prop);
            }
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return propCancDtls;
    }

    public List<Proposal> findPropPendingStatus() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<Proposal> propPending = new ArrayList<Proposal>();
        try {

            String propCancDtlsQuery =
                "begin LMS_WEB_CURSOR.PropPendingStatuses(?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(propCancDtlsQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("policyProposalCode"));

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                Proposal prop = new Proposal();
                prop.setPrpsCode(rs.getBigDecimal(1));
                prop.setPrpsPolCode(rs.getBigDecimal(2));
                prop.setPrpsDate(rs.getDate(3));
                prop.setPendingReason(rs.getString(4));
                prop.setPrpsStatus(rs.getString(5));
                prop.setPrpsDateSorted(rs.getDate(6));
                propPending.add(prop);
            }
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return propPending;
    }

    public List<Proposal> findRetireAges() {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<Proposal> propPending = new ArrayList<Proposal>();
        try {

            String propCancDtlsQuery =
                "begin LMS_WEB_PKG_MKT.retire_age_list(?,?,?,?,?  ); end;";
            cst = (OracleCallableStatement)conn.prepareCall(propCancDtlsQuery);

            //register out
            cst.setBigDecimal(1,
                              (BigDecimal)session.getAttribute("productCode"));
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("popCode"));
            cst.setBigDecimal(3,
                              (BigDecimal)session.getAttribute("ClientCode"));
            cst.setDate(4, (Date)session.getAttribute("effDate"));
            cst.registerOutParameter(5, OracleTypes.CURSOR);

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(5);
            while (rs.next()) {
                Proposal prop = new Proposal();
                prop.setRetAgeDesc(rs.getString(1));
                prop.setRetAge(rs.getBigDecimal(1));
                propPending.add(prop);
            }
            rs.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return propPending;
    }

    public List<Proposal> findLifeCoverFactors() {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<Proposal> propPending = new ArrayList<Proposal>();
        try {

            String propCancDtlsQuery =
                "begin LMS_WEB_CURSOR.Sa_Lc_factors(?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(propCancDtlsQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("productCode"));
            cst.setString(3, (String)session.getAttribute("term"));


            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                Proposal prop = new Proposal();
                prop.setLsfRate(rs.getBigDecimal(1));
                prop.setLsfApplyTo(rs.getString(2));
                prop.setLsfCode(rs.getBigDecimal(3));
                propPending.add(prop);
            }
            rs.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return propPending;
    }

    public List<Proposal> findPendingStatus() {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<Proposal> propPending = new ArrayList<Proposal>();
        try {

            String propCancDtlsQuery =
                "begin LMS_WEB_CURSOR.PendingStatuses(?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(propCancDtlsQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("policyProposalCode"));

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                Proposal prop = new Proposal();
                prop.setPgsCode(rs.getBigDecimal(1));
                prop.setPgsDesc(rs.getString(2));
                propPending.add(prop);
            }
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return propPending;
    }

    public List<Proposal> findCachedProposal() {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<Proposal> proposal = new ArrayList<Proposal>();
        try {

            String proposalQuery =
                "begin LMS_WEB_CURSOR.Sessions(?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(proposalQuery);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setString(2, session.getId());
            cst.setBigDecimal(3, (BigDecimal)session.getAttribute("UserCode"));

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                Proposal prop = new Proposal();
                prop.setLssCode(rs.getBigDecimal(1));
                prop.setLssUsrCode(rs.getBigDecimal(2));
                prop.setLssSid(rs.getString(3));
                prop.setLssDate(rs.getDate(1));
                prop.setLssSourceJspx(rs.getString(2));
                prop.setLssTranstype(rs.getString(3));
                prop.setLssInitProp(rs.getString(4));
                prop.setLssProdcode(rs.getBigDecimal(6));
                prop.setLssProddesc(rs.getString(7));
                prop.setLssBrncode(rs.getBigDecimal(8));
                prop.setLssBrndesc(rs.getString(9));
                prop.setLssPprcode(rs.getBigDecimal(10));
                prop.setLssPprProposalNo(rs.getString(11));
                prop.setLssPrpcode(rs.getBigDecimal(12));
                prop.setLssPrpOtherNames(rs.getString(13));
                prop.setLssPrpSurname(rs.getString(14));
                prop.setLssCoCode(rs.getBigDecimal(15));
                prop.setLssPayfreq(rs.getString(16));
                prop.setLssPayfreqdesc(rs.getString(17));
                prop.setLssPaymode(rs.getString(18));
                prop.setLssPaymodedesc(rs.getString(19));
                prop.setLssPremamt(rs.getBigDecimal(20));
                prop.setLssTerm(rs.getBigDecimal(21));
                prop.setLssAgncode(rs.getBigDecimal(22));
                prop.setLssAgnname(rs.getString(23));
                prop.setLssInstcode(rs.getBigDecimal(24));
                prop.setLssInstdesc(rs.getString(25));
                prop.setLssBankaccnt(rs.getString(26));
                prop.setLssBankbbrcode(rs.getBigDecimal(27));
                prop.setLssBankbbrname(rs.getString(28));
                prop.setLssEffectiveDate(rs.getString(29));
                prop.setLssDeductmonth(rs.getString(30));
                prop.setLssDebitday(rs.getString(31));
                prop.setLssStartdate(rs.getDate(32));
                prop.setLssPopCode(rs.getBigDecimal(33));
                prop.setLssPopDesc(rs.getString(34));
                prop.setLssPayrollNo(rs.getString(35));
                prop.setLssChildage(rs.getBigDecimal(36));
                prop.setLssSa(rs.getBigDecimal(37));
                prop.setLssClientPrpCode(rs.getBigDecimal(38));
                prop.setLssClientOtherNames(rs.getString(39));
                prop.setLssClientSurname(rs.getString(40));
                prop.setLssJointPrpCode(rs.getBigDecimal(41));
                prop.setLssJointOtherNames(rs.getString(42));
                prop.setLssJointSurname(rs.getString(43));
                prop.setLssMthlyIncome(rs.getBigDecimal(44));
                prop.setLssMaturityDate(rs.getDate(45));
                prop.setLssProposalDateSigned(rs.getDate(46));
                prop.setLssIntrRate(rs.getBigDecimal(47));
                prop.setLssGrpLifeRider(rs.getString(48));
                prop.setLssLifeCvrFact(rs.getBigDecimal(49));
                prop.setLssBcaCode(rs.getBigDecimal(50));
                prop.setLssBcaDesc(rs.getString(51));

                proposal.add(prop);
            }
            rs.close();


            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return proposal;

    }


    public List<Proposal> findProposalReceipts() {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<Proposal> proposal = new ArrayList<Proposal>();
        try {

            String proposalQuery = "begin LMS_WEB_CURSOR.Proprcpt(?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(proposalQuery);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("policyProposalCode"));

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                Proposal prop = new Proposal();
                prop.setOpprDate(rs.getDate(1));
                prop.setOpprAmt(rs.getBigDecimal(2));
                prop.setOpprReceiptNo(rs.getString(3));
                prop.setOpprReceiptDate(rs.getDate(1));
                prop.setOpprDrCr(rs.getString(2));
                prop.setOpprDoneBy(rs.getString(3));
                proposal.add(prop);
            }
            rs.close();


            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return proposal;

    }

    public List<Proposal> findProposalDefects() {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<Proposal> proposal = new ArrayList<Proposal>();
        try {

            String proposalQuery =
                "begin LMS_WEB_CURSOR.Prpdefects(?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(proposalQuery);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("policyProposalCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                Proposal prop = new Proposal();
                prop.setPrdCode(rs.getBigDecimal(1));
                prop.setPrdDefCode(rs.getBigDecimal(2));
                prop.setPrdPprCode(rs.getBigDecimal(3));
                prop.setPrdSorted(rs.getString(4));
                prop.setPrdSortDate(rs.getDate(5));
                prop.setDefDesc(rs.getString(6));
                proposal.add(prop);
            }
            rs.close();


            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return proposal;

    }

    public List<Proposal> findDefects() {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<Proposal> proposal = new ArrayList<Proposal>();
        try {

            String proposalQuery =
                "begin LMS_WEB_CURSOR.ProposaldefectsLov(?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(proposalQuery);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("policyProposalCode"));

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                Proposal prop = new Proposal();
                prop.setDefCode(rs.getBigDecimal(1));
                prop.setDefDesc(rs.getString(2));
                proposal.add(prop);
            }
            rs.close();
            cst.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return proposal;

    }

    public List<ProposalSchedulesBean> findPropModSystems() {
        String query =
            "begin ? := TQC_SETUPS_CURSOR.get_proposal_mod_systems(); end;";
        OracleCallableStatement cst = null;
        List<ProposalSchedulesBean> props =
            new ArrayList<ProposalSchedulesBean>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        try {
            cst = (OracleCallableStatement)conn.prepareCall(query);
            //register out
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            //input params
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                ProposalSchedulesBean claim = new ProposalSchedulesBean();
                claim.setTSMS_CODE(rs.getBigDecimal(1));
                claim.setTSMS_SHT_DESC(rs.getString(2));
                claim.setTSMS_DESC(rs.getString(3));
                props.add(claim);
            }

            rs.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }

        return props;
    }


    public List<ProposalSchedulesBean> findPropModulesSubUnits() {
        String query =
            "begin ? := TQC_SETUPS_CURSOR.get_modules_subunits(?); end;";
        OracleCallableStatement cst = null;
        List<ProposalSchedulesBean> props =
            new ArrayList<ProposalSchedulesBean>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        try {
            cst = (OracleCallableStatement)conn.prepareCall(query);
            //register out
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            //input params
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("prodCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                ProposalSchedulesBean claim = new ProposalSchedulesBean();
                claim.setTSMS_CODE(rs.getBigDecimal(1));
                claim.setTSMS_TSM_CODE(rs.getBigDecimal(2));
                claim.setTSMS_SHT_DESC(rs.getString(3));
                claim.setTSMS_DESC(rs.getString(4));
                claim.setTSMS_ORDER(rs.getBigDecimal(5));
                claim.setTSMS_SCL_CODE(rs.getBigDecimal(6));
                claim.setSCL_DESC(rs.getString(7));
                claim.setTSM_DESC(rs.getString(8));
                props.add(claim);
            }

            rs.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }

        return props;
    }


    public List<ProposalSchedulesBean> findPropModulesSubUnitsDet() {
        String query =
            "begin ? := TQC_SETUPS_CURSOR.get_mod_subunits_det(?); end;";
        OracleCallableStatement cst = null;
        List<ProposalSchedulesBean> props =
            new ArrayList<ProposalSchedulesBean>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        try {
            cst = (OracleCallableStatement)conn.prepareCall(query);
            //register out
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("SysTsmCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                ProposalSchedulesBean claim = new ProposalSchedulesBean();
                claim.setTSMSD_CODE(rs.getBigDecimal(1));
                claim.setTSMSD_NAME(rs.getString(2));
                claim.setTSMSD_PROMPT(rs.getString(3));
                claim.setTSMSD_TYPE(rs.getString(4));
                claim.setTSMSD_ORDER(rs.getString(5));
                claim.setTSMSD_PARENT(rs.getBigDecimal(6));
                claim.setTSMSD_MORE_DTLS_APPL(rs.getString(7));
                claim.setTSMSD_MORE_DTLS(rs.getString(8));
                claim.setTSMSD_ROOT(rs.getString(9));
                claim.setTSMSD_MORE_DTLS_REQUIRED(rs.getString(10));
                claim.setTmsc_code(rs.getBigDecimal(11));
                claim.setTmsc_desc(rs.getString(12));
                props.add(claim);
            }

            rs.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
            e.printStackTrace();
        }

        return props;
    }

    public List<ProposalSchedulesBean> findPropMappingColumns() {
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getSysMappingColmns(?); end;";
        OracleCallableStatement cst = null;
        List<ProposalSchedulesBean> props =
            new ArrayList<ProposalSchedulesBean>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        try {
            cst = (OracleCallableStatement)conn.prepareCall(query);
            //register out
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setInt(2, 27);
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                ProposalSchedulesBean claim = new ProposalSchedulesBean();
                claim.setTmsc_code(rs.getBigDecimal(1));
                claim.setTmsc_desc(rs.getString(2));
                props.add(claim);
            }

            rs.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
            e.printStackTrace();
        }

        return props;
    }


    public List<ProposalSchedulesBean> findPropModulesSubUnitsDetOptions() {
        String query =
            "begin ? := TQC_SETUPS_CURSOR.get_subunits_options(?); end;";
        OracleCallableStatement cst = null;
        List<ProposalSchedulesBean> props =
            new ArrayList<ProposalSchedulesBean>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        try {
            cst = (OracleCallableStatement)conn.prepareCall(query);
            //register out
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("TsmsdCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                ProposalSchedulesBean claim = new ProposalSchedulesBean();
                claim.setTSSO_CODE(rs.getBigDecimal(1));
                claim.setTSSO_TSMSD_CODE(rs.getBigDecimal(2));
                claim.setTSSO_OPTION_NAME(rs.getString(3));
                claim.setTSSO_OPTION_DESC(rs.getString(4));
                claim.setTSSO_ORDER(rs.getBigDecimal(5));
                claim.setTSSO_TYPE(rs.getString(6));
                props.add(claim);
            }

            rs.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
            e.printStackTrace();
        }

        return props;
    }


}
