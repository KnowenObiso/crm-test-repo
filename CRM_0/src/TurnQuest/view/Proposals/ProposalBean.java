package TurnQuest.view.Proposals;


//import TurnQuest.view.Base.DBConnector;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanRadio;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.nav.RichCommandLink;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;
import oracle.adf.view.rich.component.rich.output.RichSeparator;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;

import org.apache.myfaces.trinidad.convert.DateTimeConverter;


public class ProposalBean {


    public ProposalBean() {
        super();
    }

    private HtmlPanelGrid mainParent;
    private RichPanelGroupLayout dtls;
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private RichCommandLink rptlink;
    private RichInputDate myDate;


    public String createComponents() {


        mainParent.getChildren().clear();
        mainParent.setColumns(1);


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;
        OracleCallableStatement cstDtls = null;
        OracleCallableStatement cstOptDtls = null;
        OracleCallableStatement cstChldDtls = null;
        OracleCallableStatement cstOptCnt = null;
        String sysSubunits =
            "SELECT TSMS_DESC,TSMS_CODE FROM TQC_SYS_MOD_SUBUNITS,TQC_SYSTEM_MODULES WHERE TSMS_TSM_CODE = TSM_CODE AND TSM_SHT_DESC = 'PROPOSAL' ORDER BY TSMS_ORDER";
        String sysSubunitsDtls =
            "SELECT TSMSD_CODE,TSMSD_ORDER,TSMSD_PROMPT,TSMSD_TYPE,TSMSD_MORE_DTLS_APPL,TSMSD_MORE_DTLS,TSMSD_ROOT,TSMSD_MORE_DTLS_REQUIRED FROM TQC_SYS_MOD_SUBUNITS_DETAILS WHERE TSMSD_TSMS_CODE = ? AND TSMSD_PARENT IS NULL ORDER BY TSMSD_ORDER";

        String sysSubunitsDtlsChild =
            "SELECT TSMSD_CODE,TSMSD_ORDER,TSMSD_PROMPT,TSMSD_TYPE,TSMSD_MORE_DTLS_APPL,TSMSD_MORE_DTLS,TSMSD_ROOT FROM TQC_SYS_MOD_SUBUNITS_DETAILS WHERE TSMSD_PARENT = ? ORDER BY TSMSD_ORDER";


        String sysSubunitsOptions =
            "SELECT TSSO_CODE, TSSO_OPTION_NAME, TSSO_OPTION_DESC FROM TQC_SYS_SUBUNITS_OPTIONS WHERE TSSO_TSMSD_CODE = ? ORDER BY TSSO_ORDER";

        String sysAppOptions =
            "SELECT COUNT(TSSO_CODE), TSSO_OPTION_NAME FROM TQC_SYS_SUBUNITS_OPTIONS WHERE TSSO_TSMSD_CODE = ? GROUP BY TSSO_OPTION_NAME";

        String sysOptCount =
            "SELECT COUNT(TSSO_CODE), TSSO_OPTION_NAME FROM TQC_SYS_SUBUNITS_OPTIONS GROUP BY TSSO_OPTION_NAME";

        try {
            conn = dbConnector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(sysSubunits);

            OracleResultSet rs = (OracleResultSet)cst.executeQuery();
            mainParent.getChildren().clear();
            int k = 1;

            while (rs.next()) {
                HtmlPanelGrid headerPnl = new HtmlPanelGrid();
                headerPnl.setColumns(2);
                headerPnl.setStyleClass("text-align:left;");
                RichOutputLabel headerNo = new RichOutputLabel();
                headerNo.setValue(k + ".");
                headerNo.setInlineStyle("font-weight:bold;");
                headerPnl.getChildren().add(headerNo);
                RichOutputLabel subunitHeader = new RichOutputLabel();
                subunitHeader.setValue(rs.getString(1));
                subunitHeader.setInlineStyle("font-weight:bold;");
                headerPnl.getChildren().add(subunitHeader);
                mainParent.getChildren().add(headerPnl);
                RichSeparator separator = new RichSeparator();
                mainParent.getChildren().add(separator);
                cstDtls =
                        (OracleCallableStatement)conn.prepareCall(sysSubunitsDtls);
                cstDtls.setBigDecimal(1, rs.getBigDecimal(2));
                OracleResultSet rsDtls =
                    (OracleResultSet)cstDtls.executeQuery();
                HtmlPanelGrid unitPnl = new HtmlPanelGrid();
                unitPnl.setColumns(3);
                while (rsDtls.next()) {

                    HtmlPanelGrid lblPnl = new HtmlPanelGrid();
                    lblPnl.setColumns(1);
                    lblPnl.setStyle("width:250px");
                    if (rsDtls.getString(5).equalsIgnoreCase("Y")) {
                        headerNo = new RichOutputLabel();
                        headerNo.setValue(rsDtls.getString(2));
                        unitPnl.getChildren().add(headerNo);
                        subunitHeader = new RichOutputLabel();
                        subunitHeader.setValue(rsDtls.getString(3));
                        lblPnl.getChildren().add(subunitHeader);
                        subunitHeader = new RichOutputLabel();
                        subunitHeader.setValue(rsDtls.getString(6));
                        lblPnl.getChildren().add(subunitHeader);
                        unitPnl.getChildren().add(lblPnl);
                    } else {
                        headerNo = new RichOutputLabel();
                        headerNo.setValue(rsDtls.getString(2));
                        unitPnl.getChildren().add(headerNo);
                        subunitHeader = new RichOutputLabel();
                        subunitHeader.setValue(rsDtls.getString(3));
                        lblPnl.getChildren().add(subunitHeader);
                        unitPnl.getChildren().add(lblPnl);
                    }

                    if (rsDtls.getString(4).equalsIgnoreCase("INPUT_TEXT")) {
                        HtmlPanelGrid pnlDtls = new HtmlPanelGrid();
                        RichInputText inputTxt = new RichInputText();
                        inputTxt.setId("txt" + rsDtls.getString(1));
                        inputTxt.setSimple(true);
                        pnlDtls.getChildren().add(inputTxt);
                        unitPnl.getChildren().add(pnlDtls);
                    } else if (rsDtls.getString(4).equalsIgnoreCase("NUMBER")) {
                        HtmlPanelGrid pnlDtls = new HtmlPanelGrid();
                        RichInputNumberSpinbox inputTxt =
                            new RichInputNumberSpinbox();
                        inputTxt.setId("txt" + rsDtls.getString(1));
                        inputTxt.setSimple(true);
                        pnlDtls.getChildren().add(inputTxt);
                        unitPnl.getChildren().add(pnlDtls);
                    } else if (rsDtls.getString(4).equalsIgnoreCase("TEXT")) {
                        HtmlPanelGrid pnlDtls = new HtmlPanelGrid();
                        RichInputText inputTxt = new RichInputText();
                        inputTxt.setId("txt" + rsDtls.getString(1));
                        inputTxt.setColumns(50);
                        inputTxt.setRows(4);
                        inputTxt.setSimple(true);
                        pnlDtls.getChildren().add(inputTxt);
                        unitPnl.getChildren().add(pnlDtls);
                    } else if (rsDtls.getString(4).equalsIgnoreCase("DATE")) {
                        HtmlPanelGrid pnlDtls = new HtmlPanelGrid();
                        RichInputDate dateComp = new RichInputDate();
                        DateTimeConverter convt =
                            (DateTimeConverter)myDate.getConverter();
                        dateComp.setConverter(convt);
                        dateComp.setId("txt" + rsDtls.getString(1));
                        dateComp.setSimple(true);
                        pnlDtls.getChildren().add(dateComp);
                        unitPnl.getChildren().add(pnlDtls);
                    } else if (rsDtls.getString(4).equalsIgnoreCase("OPTION")) {
                        cstOptCnt =
                                (OracleCallableStatement)conn.prepareCall(sysOptCount);
                        //cstDtls.setBigDecimal(1, rs.getBigDecimal(2));

                        OracleResultSet cntRs =
                            (OracleResultSet)cstOptCnt.executeQuery();
                        int no = 0;
                        while (cntRs.next()) {
                            no = cntRs.getInt(1);
                        }
                        cstOptCnt.close();
                        cntRs.close();

                        HtmlPanelGrid pnlDtls = new HtmlPanelGrid();
                        pnlDtls.setColumns(no);
                        cstOptDtls =
                                (OracleCallableStatement)conn.prepareCall(sysSubunitsOptions);
                        cstOptDtls.setBigDecimal(1, rsDtls.getBigDecimal(1));
                        OracleResultSet rsOptDtls =
                            (OracleResultSet)cstOptDtls.executeQuery();
                        while (rsOptDtls.next()) {

                            RichSelectBooleanRadio chkBox =
                                new RichSelectBooleanRadio();
                            chkBox.setId("txt" + rsOptDtls.getString(1));
                            chkBox.setSimple(true);
                            chkBox.setGroup(rsOptDtls.getString(2));
                            chkBox.setText(rsOptDtls.getString(3));
                            pnlDtls.getChildren().add(chkBox);
                        }
                        rsOptDtls.close();
                        cstOptDtls.close();


                        unitPnl.getChildren().add(pnlDtls);
                    } else if (rsDtls.getString(4).equalsIgnoreCase("CHECK")) {
                        cstOptCnt =
                                (OracleCallableStatement)conn.prepareCall(sysOptCount);
                        //cstDtls.setBigDecimal(1, rs.getBigDecimal(2));

                        OracleResultSet cntRs =
                            (OracleResultSet)cstOptCnt.executeQuery();
                        int no = 0;
                        while (cntRs.next()) {
                            no = cntRs.getInt(1);
                        }
                        cstOptCnt.close();
                        cntRs.close();

                        HtmlPanelGrid pnlDtls = new HtmlPanelGrid();
                        pnlDtls.setColumns(no);

                        cstOptDtls =
                                (OracleCallableStatement)conn.prepareCall(sysSubunitsOptions);
                        cstOptDtls.setBigDecimal(1, rsDtls.getBigDecimal(1));
                        OracleResultSet rsOptDtls =
                            (OracleResultSet)cstOptDtls.executeQuery();
                        while (rsOptDtls.next()) {

                            RichSelectBooleanCheckbox chkBox =
                                new RichSelectBooleanCheckbox();
                            chkBox.setId("txt" + rsOptDtls.getString(1));
                            chkBox.setSimple(true);
                            chkBox.setText(rsOptDtls.getString(3));
                            pnlDtls.getChildren().add(chkBox);
                        }
                        rsOptDtls.close();
                        cstOptDtls.close();


                        unitPnl.getChildren().add(pnlDtls);
                    }

                    System.out.println("More Details");
                    System.out.println(rsDtls.getString(8));
                    // HtmlPanelGrid pnlDtlsMore = new HtmlPanelGrid();
                    // pnlDtlsMore.setColumns(3);
                    if (rsDtls.getString(8).equalsIgnoreCase("Y")) {

                        RichInputText inputTxt = new RichInputText();
                        headerNo = new RichOutputLabel();
                        headerNo.setValue(null);
                        unitPnl.getChildren().add(headerNo);
                        subunitHeader = new RichOutputLabel();
                        subunitHeader.setValue("More Details");
                        unitPnl.getChildren().add(subunitHeader);
                        inputTxt.setId("txt" + rsDtls.getString(1));
                        inputTxt.setColumns(50);
                        inputTxt.setRows(2);
                        inputTxt.setSimple(true);
                        unitPnl.getChildren().add(inputTxt);
                        //unitPnl.getChildren().add(pnlDtls);
                    }
                    mainParent.getChildren().add(unitPnl);
                    // mainParent.getChildren().add(pnlDtlsMore);
                    //headerPnl.getChildren().add(unitPnl);


                }
                rsDtls.close();
                cstDtls.close();

                k++;
            }
            rs.close();
            cst.close();

            GlobalCC.refreshUI(dtls);

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public void setMainParent(HtmlPanelGrid mainParent) {
        this.mainParent = mainParent;
    }

    public HtmlPanelGrid getMainParent() {
        return mainParent;
    }

    public void setDtls(RichPanelGroupLayout dtls) {
        this.dtls = dtls;
    }

    public RichPanelGroupLayout getDtls() {
        return dtls;
    }

    public void setRptlink(RichCommandLink rptlink) {
        this.rptlink = rptlink;
    }

    public RichCommandLink getRptlink() {
        return rptlink;
    }

    public void setMyDate(RichInputDate myDate) {
        this.myDate = myDate;
    }

    public RichInputDate getMyDate() {
        return myDate;
    }


}
