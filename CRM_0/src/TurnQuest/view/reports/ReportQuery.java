package TurnQuest.view.reports;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.RichDialog;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectItem;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.nav.RichCommandLink;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;
import oracle.adf.view.rich.event.QueryEvent;
import oracle.adf.view.rich.model.FilterableQueryDescriptor;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.apache.commons.dbutils.DbUtils;
import org.apache.myfaces.trinidad.convert.DateTimeConverter;


//import oracle.adf.view.rich.component.rich.RichPopup;


public class ReportQuery {
    //  public static HtmlPanelGrid mainParent;
    // private RichPanelGroupLayout dtls;
    private RichCommandLink rptlink;
    // private RichInputDate myTest;
    // private static RichPopup paramReport;
    private UIComponent comp;
    private HtmlPanelGrid childGrid;
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private RichTable reportQueryTab;
    private RichInputText lovSelectedValue;
    //    private RichDialog queryLovDialog;
    // private RichSelectOneRadio reportType;
    private RichPanelGroupLayout dtls;
    private HtmlPanelGrid mainParent;
    private RichSelectOneRadio reportType;
    private RichInputDate myTest;
    private HtmlPanelGrid mainHolder;
    private RichPanelGroupLayout details;
    private RichPopup repLov;
    private RichDialog queryLov;
    private RichTable reportQueryTbl;
    // private RichInputDate myTest;


    //    public ReportQuery() {
    //        super();
    //        RichPanelGroupLayout dfd = new RichPanelGroupLayout();
    //        this.dtls = dfd;
    //        HtmlPanelGrid panel = new HtmlPanelGrid();
    //        this.mainParent = panel;
    //
    //
    //    }

    //    public String createComponents() {
    //        mainParent.getChildren().clear();
    //        mainParent.setColumns(2);
    //        if (session.getAttribute("rptCode") == null) {
    //            return null;
    //        }
    //
    //        DBConnector dbConnector = new DBConnector();
    //        OracleConnection conn = null;
    //        OracleCallableStatement cst = null;
    //        String jobquery =
    //            "begin  TQC_WEB_CURSOR.get_report_parameters(?,?,?,?); end;";
    //        /*  String jobquery =
    //            "SELECT RPTP_PARAM_PROMPT, RPTP_PARAM_TYPE, RPTP_PARENT_CODE, RPTP_QUERY,RPTP_PARAM_NAME, " +
    //            " RPTP_PARAM_CLAUSE, RPTP_USER_REQUIRED, RPTP_CODE" +
    //            " FROM TQC_SYS_RPT_PARAMETERS " +
    //            " WHERE RPTP_RPT_CODE = ? ORDER BY RPTP_PARENT_CODE ASC NULLS FIRST,RPTP_CODE"; */
    //
    //        try {
    //            conn = dbConnector.getDatabaseConnection();
    //            cst = (OracleCallableStatement)conn.prepareCall(jobquery);
    //            cst.registerOutParameter(1, OracleTypes.CURSOR);
    //            cst.setString(2, (String)session.getAttribute("rptCode"));
    //            cst.setString(3, null);
    //            cst.setString(4, null);
    //            cst.execute();
    //            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
    //            mainParent.getChildren().clear();
    //            int k = 0;
    //            while (rs.next()) {
    //                RichOutputLabel LabelArr = new RichOutputLabel();
    //                LabelArr.setValue(rs.getString(2));
    //                LabelArr.setId("ol" + rs.getString(1));
    //                //  System.out.println("paramLabel1=" + LabelArr.getId());
    //                mainParent.getChildren().add(LabelArr);
    //                if (rs.getString(3).equalsIgnoreCase("DATE")) {
    //                    RichInputDate dateComp = new RichInputDate();
    //                    DateTimeConverter test =
    //                        (DateTimeConverter)myTest.getConverter();
    //                    dateComp.setConverter(test);
    //                    dateComp.setId(rs.getString(6));
    //                    // dateComp.setLabel(rs.getString(1));
    //                    mainParent.getChildren().add(dateComp);
    //                } else if (rs.getString(3).equalsIgnoreCase("LOV")) {
    //                    OracleCallableStatement cst2 = null;
    //                    String query = rs.getString(5);
    //
    //                    cst2 = (OracleCallableStatement)conn.prepareCall(query);
    //                    OracleResultSet rs2 = null;
    //                    rs2 = (OracleResultSet)cst2.executeQuery();
    //                    RichSelectOneChoice lov = new RichSelectOneChoice();
    //                    while (rs2.next()) {
    //                        RichSelectItem select = new RichSelectItem();
    //                        select.setLabel(rs2.getString(2));
    //                        select.setValue(rs2.getString(1));
    //                        System.out.println(rs2.getString(1));
    //                        lov.getChildren().add(select);
    //                    }
    //                    lov.setId(rs.getString(6));
    //                    lov.setSimple(true);
    //                    lov.setLabel(rs.getString(1));
    //                    lov.setAutoSubmit(false);
    //                    lov.setUnselectedLabel(rs.getString(2));
    //                    /* Class[] parms = new Class[] { ValueChangeEvent.class };
    //                    MethodBinding mb =
    //                        FacesContext.getCurrentInstance().getApplication().createMethodBinding("#{ReportQuery.queryLovChanged}",
    //                                                                                               parms);
    //                    lov.setValueChangeListener(mb);*/
    //                    //   mainParent.getChildren().add(lov);
    //                    if (rs.getString(6).equalsIgnoreCase("V_POL_CODE") ||
    //                        rs.getString(6).equalsIgnoreCase("V_ENDR_CODE") ||
    //                        rs.getString(6).equalsIgnoreCase("V_PRP_CODE") ||
    //                        rs.getString(6).equalsIgnoreCase("V_CLAIM_NO") ||
    //                        rs.getString(6).equalsIgnoreCase("V_CLM_NO") ||
    //                        rs.getString(6).equalsIgnoreCase("V_AGEN_CODE") ||
    //                        rs.getString(6).equalsIgnoreCase("V_AGN_CODE") ||
    //                        rs.getString(6).equalsIgnoreCase("P_CLNT_CODE") ||
    //                        rs.getString(6).equalsIgnoreCase("P_POLICY_NO")) {
    //                        session.removeAttribute("componentId");
    //                        queryLovDialog.setTitle(rs.getString(1));
    //                        Class[] params = new Class[] { ActionEvent.class };
    //                        MethodBinding rptQuerySelect =
    //                            FacesContext.getCurrentInstance().getApplication().createMethodBinding("#{ReportQuery.newSelectRptQuery}",
    //                                                                                                   params);
    //
    //                        HtmlPanelGrid grid = new HtmlPanelGrid();
    //                        grid.setColumns(2);
    //                        RichInputText textComp = new RichInputText();
    //                        textComp.setId(rs.getString(6));
    //                        session.setAttribute("componentId", rs.getString(6));
    //                        textComp.setDisabled(true);
    //                        //textComp.setContext(lovSelectedValue);
    //                        textComp.setValue(null);
    //                        //childGrid.setRendered(true);
    //                        //  lovSelectedValue.setLabel(rs.getString(1));
    //                        //  lovSelectedValue.setRendered(true);
    //                        // lovSelectedValue.setId(rs.getString(5));
    //                        RichCommandButton btn = new RichCommandButton();
    //                        btn.setPartialSubmit(true);
    //                        btn.setId("pt_cmd_1");
    //                        btn.setIcon("/images/dropdown.gif");
    //                        btn.setActionListener(rptQuerySelect);
    //                        grid.getChildren().add(textComp);
    //                        grid.getChildren().add(btn);
    //                        //mainParent.getChildren().add(textComp);
    //                        //  mainParent.getChildren().add(btn);
    //                        mainParent.getChildren().add(grid);
    //                    } else {
    //                        mainParent.getChildren().add(lov);
    //                    }
    //                    rs2.close();
    //                    cst2.close();
    //                } else if (rs.getString(3).equalsIgnoreCase("TEXT")) {
    //                    RichInputText textComp = new RichInputText();
    //                    textComp.setId(rs.getString(6));
    //                    // textComp.setLabel(rs.getString(1));
    //                    mainParent.getChildren().add(textComp);
    //                } else if (rs.getString(3).equalsIgnoreCase("NUMBER")) {
    //                    RichInputNumberSpinbox textComp =
    //                        new RichInputNumberSpinbox();
    //                    textComp.setId(rs.getString(6));
    //                    // textComp.setLabel(rs.getString(1));
    //                    mainParent.getChildren().add(textComp);
    //                }
    //
    //            }
    //            conn.close();
    //            GlobalCC.refreshUI(dtls);
    //
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //            GlobalCC.EXCEPTIONREPORTING(conn, e);
    //        }
    //
    //        return null;
    //    }

    public String createComponents() throws SQLException {
        mainHolder.getChildren().clear();
        mainHolder.setColumns(2);
        if (session.getAttribute("rptCode") == null) {
            return null;
        }

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;
        session.removeAttribute("RptReportQuery");
        String jobquery =
            "SELECT RPTP_CODE,RPTP_PARAM_PROMPT, RPTP_PARAM_TYPE, RPTP_PARENT_CODE, RPTP_QUERY,RPTP_PARAM_NAME, RPTP_PARAM_CLAUSE, \n" +
            "RPTP_USER_REQUIRED, RPTP_CHILD_CODE FROM TQC_SYS_RPT_PARAMETERS \n" +
            "WHERE RPTP_RPT_CODE = ? ORDER BY RPTP_PARENT_CODE ASC NULLS FIRST,RPTP_CODE";
        OracleResultSet rs = null;
        try {
            conn = dbConnector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(jobquery);
            cst.setString(1, (String)session.getAttribute("rptCode"));
            cst.execute();
            rs = (OracleResultSet)cst.executeQuery();
            mainHolder.getChildren().clear();
            int k = 0;

            while (rs.next()) {
                System.out.println("This is executes finds Report parameters");
                RichOutputLabel LabelArr = new RichOutputLabel();
                LabelArr.setValue(rs.getString("RPTP_PARAM_PROMPT"));
                LabelArr.setId("ol" + rs.getString("RPTP_CODE"));
                //  System.out.println("paramLabel1=" + LabelArr.getId());
                mainHolder.getChildren().add(LabelArr);
                if (rs.getString("RPTP_PARAM_TYPE").equalsIgnoreCase("DATE")) {
                    RichInputDate dateComp = new RichInputDate();
                    DateTimeConverter test = (DateTimeConverter)myTest.getConverter();
                    dateComp.setConverter(test);
                    dateComp.setId(rs.getString("RPTP_PARAM_NAME"));
                    dateComp.setImmediate(true);
                    dateComp.setAutoSubmit(true);
                    // dateComp.setLabel(rs.getString(1));
                    mainHolder.getChildren().add(dateComp);
                } else if (rs.getString("RPTP_PARAM_TYPE").equalsIgnoreCase("LOV")) {
                    OracleCallableStatement cst2 = null;
                    String query = rs.getString("RPTP_QUERY");
                    System.out.println(query);
                    cst2 = (OracleCallableStatement)conn.prepareCall(query);
                    OracleResultSet rs2 = null;
                    rs2 = (OracleResultSet)cst2.executeQuery();
                    RichSelectOneChoice lov = new RichSelectOneChoice();
                    while (rs2.next()) {
                        RichSelectItem select = new RichSelectItem();
                        select.setValue(rs2.getString(1));
                        select.setLabel(rs2.getString(2));
                        lov.getChildren().add(select);
                    }

                    if (rs.getString("RPTP_CHILD_CODE") == null) {
                        //No Child Parameter Specified.
                    } else {
                        FacesContext fc = FacesContext.getCurrentInstance();
                        Class[] klass = new Class[] { ValueChangeEvent.class };
                        MethodBinding mb =
                            fc.getApplication().createMethodBinding("#{ReportQuery.DynamicSelectListener}",
                                                                    klass);
                        lov.setValueChangeListener(mb);
                        lov.setShortDesc(rs.getString(9));
                    }

                    lov.setId(rs.getString("RPTP_PARAM_NAME"));
                    lov.setSimple(true);
                    lov.setLabel(rs.getString(1));
                    lov.setAutoSubmit(true);
                    lov.setImmediate(true);
                    lov.setUnselectedLabel(rs.getString(2));
                    Class[] parms = new Class[] { ValueChangeEvent.class };
                    MethodBinding mb =
                        FacesContext.getCurrentInstance().getApplication().createMethodBinding("#{ReportQuery.queryLovChanged}",
                                                                                               parms);
                    lov.setValueChangeListener(mb);
                    mainHolder.getChildren().add(lov);
                    rs2.close();
                    cst2.close();
                } else if (rs.getString("RPTP_PARAM_TYPE").equalsIgnoreCase("PLOV")) {
                    session.setAttribute("RptReportQuery", rs.getString(5));
                    session.removeAttribute("componentId");
                    queryLov.setTitle("Select Item...");
                    Class[] params = new Class[] { ActionEvent.class };

                    MethodBinding rptQuerySelect =
                        FacesContext.getCurrentInstance().getApplication().createMethodBinding("#{ReportQuery.newSelectRptQuery}",
                                                                                               params);
                    //repLov.setId("pt_"+k);
                    HtmlPanelGrid grid = new HtmlPanelGrid();
                    grid.setColumns(2);
                    RichInputText textComp = new RichInputText();
                    textComp.setId(rs.getString(6));
                    session.setAttribute("componentId", rs.getString(6));
                    System.out.println("Query " + rs.getString(6));
                    textComp.setDisabled(true);
                    //textComp.setContext(lovSelectedValue);
                    textComp.setValue(null);
                    //childGrid.setRendered(true);
                    //  lovSelectedValue.setLabel(rs.getString(1));
                    //  lovSelectedValue.setRendered(true);
                    // lovSelectedValue.setId(rs.getString(5));
                    RichCommandButton btn = new RichCommandButton();
                  
                    btn.setId("ptcmd1_" + rs.getString(6));
                    btn.setIcon("/images/dropdown.gif");
                    btn.setActionListener(rptQuerySelect);
                    btn.setPartialSubmit(true);
                    //btn.setImmediate(true);
                    grid.getChildren().add(textComp);
                    grid.getChildren().add(btn);

                    //mainParent.getChildren().add(textComp);
                    //  mainParent.getChildren().add(btn);
                    mainHolder.getChildren().add(grid);


                } else if (rs.getString(3).equalsIgnoreCase("TEXT")) {
                    RichInputText textComp = new RichInputText();
                    textComp.setId(rs.getString(6));
                    textComp.setImmediate(true);
                    // textComp.setLabel(rs.getString(1));
                    mainHolder.getChildren().add(textComp);
                } else if (rs.getString(3).equalsIgnoreCase("NUMBER")) {
                    RichInputNumberSpinbox textComp =
                        new RichInputNumberSpinbox();
                    textComp.setAutoSubmit(true);
                    textComp.setId(rs.getString(6));
                    System.out.println("Id Retrieved " + textComp.getId());
                    mainHolder.getChildren().add(textComp);
                }
                k++;
            }

            GlobalCC.refreshUI(details);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, cst, rs);
        }

        return null;
    }
    //  public void DynamicSelectListener(ValueChangeEvent valueChangeEvent) {
    //        // Add event code here...
    //         if(valueChangeEvent.getOldValue()!=valueChangeEvent.getNewValue()){
    //             DBConnector dbConnector = new DBConnector();
    //             Connection conn = null;
    //             CallableStatement cst = null;
    //             String ChildQuery = null;
    //             String ChildParamName = null;
    //             Integer ComponentPosition = null;
    //             String ChildParamPrompt = null;
    //             String ChildCode = null;
    //             String paramType=null;
    //
    //             RichSelectOneChoice select =(RichSelectOneChoice)valueChangeEvent.getComponent();
    //             String ChildName = select.getShortDesc().toString();
    //             String ParentValue = select.getValue().toString();
    //
    //
    //             String jobquery = "select RPTP_CODE,RPTP_PARAM_PROMPT, RPTP_PARAM_TYPE, RPTP_PARENT_CODE, RPTP_QUERY,RPTP_PARAM_NAME, RPTP_PARAM_CLAUSE, \n" +
    //             "RPTP_USER_REQUIRED, RPTP_CHILD_CODE from TQC_SYS_RPT_PARAMETERS where RPTP_CODE = ?";
    //            try {
    //                conn = dbConnector.getDatabaseConnection();
    //                cst = conn.prepareCall(jobquery);
    //                cst.setString(1, ChildName);
    //                ResultSet rs = cst.executeQuery();
    //                while(rs.next()){
    //                    ChildQuery = rs.getString(5);
    //                    ChildParamName = rs.getString(6);
    //                    ChildParamPrompt = rs.getString(2);
    //                    ChildCode = rs.getString(9);
    //                    paramType=rs.getString(3);
    //                }
    //                int t = 0;
    //                int k = 0;
    //                while(k < mainParent.getChildCount()){
    //                if(t==1){
    //                UIComponent mine  = mainParent.getChildren().get(k);
    //                String label = mine.getId();
    //                if (ChildParamName.equalsIgnoreCase(label)){
    //                    mainParent.getChildren().remove(k);
    //                    ComponentPosition = k;
    //                }
    //                 t--;
    //                 }else{
    //                    t++;
    //                 }
    //                    k++;
    //                }
    //                CallableStatement cst2 = null;
    //                cst2 = conn.prepareCall(ChildQuery);
    //                ResultSet rs2 = null;
    //                cst2.setString(1, ParentValue);
    //                rs2 = cst2.executeQuery();
    //                if (paramType.equalsIgnoreCase("PLOV")) {
    //                    session.setAttribute("RptReportQuery", ChildQuery);
    //                    session.setAttribute("rptParentValue", ParentValue);
    //                    session.removeAttribute("componentId");
    //                    queryLovDialog.setTitle("Select Item...");
    //                    Class[] params = new Class[] { ActionEvent.class };
    //                    MethodBinding rptQuerySelect =
    //                        FacesContext.getCurrentInstance().getApplication().createMethodBinding("#{reportParameterBean.newSelectRptQuery}",
    //                                                                                               params);
    //                    //repLov.setId("pt_"+k);
    //                    HtmlPanelGrid grid = new HtmlPanelGrid();
    //                    grid.setColumns(2);
    //                    RichInputText textComp = new RichInputText();
    //                    textComp.setId(ChildParamName);
    //                    session.setAttribute("componentId", ChildParamName);
    //                    System.out.println("Query " + ChildParamName);
    //                    textComp.setDisabled(true);
    //                    textComp.setValue(null);
    //                    RichCommandButton btn = new RichCommandButton();
    //                    btn.setPartialSubmit(true);
    //                    btn.setId("ptcmd1_" + ChildParamName);
    //                    btn.setIcon("/images/dropdown.gif");
    //                    btn.setActionListener(rptQuerySelect);
    //                    grid.getChildren().add(textComp);
    //                    grid.getChildren().add(btn);
    //                    mainParent.getChildren().add(ComponentPosition,grid);
    //                } else  if (paramType.equalsIgnoreCase("LOV")) {
    //                RichSelectOneChoice lov = new RichSelectOneChoice();
    //                while(rs2.next()){
    //                    RichSelectItem Newselect = new RichSelectItem();
    //                    Newselect.setLabel(rs2.getString(2));
    //                    Newselect.setValue(rs2.getString(1));
    //                    lov.getChildren().add(Newselect);
    //                }
    //
    //                if(ChildCode==null){
    //                    //No Child Parameter Specified.
    //                }else{
    //                    FacesContext fc = FacesContext.getCurrentInstance();
    //                    Class[] klass = new Class[] { ValueChangeEvent.class};
    //                    MethodBinding mb = fc.getApplication().createMethodBinding("#{reportParameterBean.DynamicSelectListener}", klass);
    //                    lov.setValueChangeListener(mb);
    //                    lov.setShortDesc(ChildCode);
    //                }
    //
    //                lov.setId(ChildParamName);
    //                lov.setSimple(true);
    //                lov.setAutoSubmit(true);
    //                lov.setUnselectedLabel(ChildParamPrompt);
    //                lov.setValue(null);
    //                mainParent.getChildren().add(ComponentPosition, lov);
    //                }
    //            GlobalCC.refreshUI(dtls);
    //
    //            } catch (Exception e) {
    //                GlobalCC.EXCEPTIONREPORTING( e);
    //            }
    //
    //
    //         }
    //     }

    public void DynamicSelectListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...

        if (valueChangeEvent.getOldValue() != valueChangeEvent.getNewValue()) {
            DBConnector dbConnector = new DBConnector();
            Connection conn = null;
            CallableStatement cst = null;
            String ChildQuery = null;
            String ChildParamName = null;
            Integer ComponentPosition = null;
            String ChildParamPrompt = null;
            String ChildCode = null;
            String paramType = null;

            RichSelectOneChoice select =
                (RichSelectOneChoice)valueChangeEvent.getComponent();
            String ChildName = select.getShortDesc().toString();
            String ParentValue = select.getValue().toString();


            String jobquery =
                "select RPTP_CODE,RPTP_PARAM_PROMPT, RPTP_PARAM_TYPE, RPTP_PARENT_CODE, RPTP_QUERY,RPTP_PARAM_NAME, RPTP_PARAM_CLAUSE, \n" +
                "RPTP_USER_REQUIRED, RPTP_CHILD_CODE from TQC_SYS_RPT_PARAMETERS where RPTP_CODE = ?";
            try {
                conn = dbConnector.getDatabaseConnection();
                cst = conn.prepareCall(jobquery);
                cst.setString(1, ChildName);
                ResultSet rs = cst.executeQuery();
                while (rs.next()) {
                    ChildQuery = rs.getString(5);
                    ChildParamName = rs.getString(6);
                    ChildParamPrompt = rs.getString(2);
                    ChildCode = rs.getString(9);
                    paramType = rs.getString(3);
                }
                int t = 0;
                int k = 0;
                while (k < mainHolder.getChildCount()) {
                    if (t == 1) {
                        UIComponent mine = mainHolder.getChildren().get(k);
                        String label = mine.getId();
                        if (ChildParamName.equalsIgnoreCase(label)) {
                            mainHolder.getChildren().remove(k);
                            ComponentPosition = k;
                        }
                        t--;
                    } else {
                        t++;
                    }
                    k++;
                }
                CallableStatement cst2 = null;
                cst2 = conn.prepareCall(ChildQuery);
                ResultSet rs2 = null;
                cst2.setString(1, ParentValue);
                rs2 = cst2.executeQuery();
                if (paramType.equalsIgnoreCase("PLOV")) {
                    session.setAttribute("RptReportQuery", ChildQuery);
                    session.setAttribute("rptParentValue", ParentValue);
                    session.removeAttribute("componentId");
                    queryLov.setTitle("Select Item...");
                    Class[] params = new Class[] { ActionEvent.class };
                    MethodBinding rptQuerySelect =
                        FacesContext.getCurrentInstance().getApplication().createMethodBinding("#{ReportQuery.newSelectRptQuery}",
                                                                                               params);
                    //repLov.setId("pt_"+k);
                    HtmlPanelGrid grid = new HtmlPanelGrid();
                    grid.setColumns(2);
                    RichInputText textComp = new RichInputText();
                    textComp.setId(ChildParamName);
                    session.setAttribute("componentId", ChildParamName);
                    System.out.println("Query " + ChildParamName);
                    textComp.setDisabled(true);
                    textComp.setValue(null);
                    RichCommandButton btn = new RichCommandButton();
                    btn.setPartialSubmit(true);
                    btn.setId("ptcmd1_" + ChildParamName);
                    btn.setIcon("/images/dropdown.gif");
                    btn.setActionListener(rptQuerySelect);
                    grid.getChildren().add(textComp);
                    grid.getChildren().add(btn);
                    mainHolder.getChildren().add(ComponentPosition, grid);
                } else if (paramType.equalsIgnoreCase("LOV")) {
                    RichSelectOneChoice lov = new RichSelectOneChoice();
                    while (rs2.next()) {
                        RichSelectItem Newselect = new RichSelectItem();
                        Newselect.setLabel(rs2.getString(2));
                        Newselect.setValue(rs2.getString(1));
                        lov.getChildren().add(Newselect);
                    }

                    if (ChildCode == null) {
                        //No Child Parameter Specified.
                    } else {
                        FacesContext fc = FacesContext.getCurrentInstance();
                        Class[] klass = new Class[] { ValueChangeEvent.class };
                        MethodBinding mb =
                            fc.getApplication().createMethodBinding("#{ReportQuery.DynamicSelectListener}",
                                                                    klass);
                        lov.setValueChangeListener(mb);
                        lov.setShortDesc(ChildCode);
                    }

                    lov.setId(ChildParamName);
                    lov.setSimple(true);
                    lov.setAutoSubmit(true);
                    lov.setUnselectedLabel(ChildParamPrompt);
                    lov.setValue(null);
                    mainHolder.getChildren().add(ComponentPosition, lov);
                }
                GlobalCC.refreshUI(details);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
                e.printStackTrace();
            }


        }
    }

    public String cancelSysReport() {
        mainHolder.getChildren().clear();
        GlobalCC.refreshUI(details);
        return null;
    }


    public String dialogTitle(String param) {
        String title = "Select Record";
        if (param.equalsIgnoreCase("V_POL_CODE") ||
            param.equalsIgnoreCase("V_ENDR_CODE")) {
            title = "Policies";
        } else if (param.equalsIgnoreCase("V_PRP_CODE")) {
            title = "Clients";
        } else if (param.equalsIgnoreCase("V_CLAIM_NO") ||
                   param.equalsIgnoreCase("V_CLM_NO")) {
            title = "Claims";
        }
        return title;
    }

    //    public String reQuery(String parentCode, String replacableValue,
    //                          String replaceValue) {
    //        DBConnector dbConnector = new DBConnector();
    //        OracleConnection conn = null;
    //        OracleCallableStatement cst = null;
    //        String paramType = "LOV";
    //        String paramLabel = null;
    //        String paramName = null;
    //        String paramPrompt = null;
    //        String paramQuery = null;
    //        String lovLabel = null;
    //        try {
    //
    //            conn = dbConnector.getDatabaseConnection();
    //            String rptCode = (String)session.getAttribute("rptCode");
    //            System.out.println("rptCode=>" + rptCode);
    //            String query = null;
    //            // GlobalCC.reportQuery(rptCode, paramType, parentCode);
    //            //System.out.println("query=>" + query);
    //            if (query != null) {
    //                query = query.toUpperCase();
    //                query = query.replace(replacableValue, replaceValue);
    //                cst = (OracleCallableStatement)conn.prepareCall(query);
    //                OracleResultSet rs = null;
    //                rs = (OracleResultSet)cst.executeQuery();
    //                RichOutputLabel prompt = new RichOutputLabel();
    //                RichSelectOneChoice lov = new RichSelectOneChoice();
    //
    //                while (rs.next()) {
    //                    RichSelectItem select = new RichSelectItem();
    //                    select.setLabel(rs.getString(2));
    //                    select.setValue(rs.getString(1));
    //                    lov.getChildren().add(select);
    //                }
    //                rs.close();
    //                cst.close();
    //
    //                paramQuery =
    //                        "begin  TQC_WEB_CURSOR.get_report_parameters(?,?,?,?); end;";
    //                OracleCallableStatement cst2 = null;
    //                cst2 = (OracleCallableStatement)conn.prepareCall(paramQuery);
    //                cst2.registerOutParameter(1, OracleTypes.CURSOR);
    //                cst2.setString(2, rptCode);
    //                cst2.setString(3, paramType);
    //                cst2.setString(4, parentCode);
    //                cst2.execute();
    //                OracleResultSet rs2 = (OracleResultSet)cst2.getObject(1);
    //                while (rs2.next()) {
    //                    lovLabel = rs2.getString(1);
    //                    paramLabel = "ol" + rs2.getString(1);
    //                    paramPrompt = rs2.getString(2);
    //                    paramName = rs2.getString(6);
    //                }
    //                rs2.close();
    //                cst2.close();
    //
    //                UIComponent componentLabel =
    //                    findComponent(mainHolder, paramLabel);
    //                UIComponent component = findComponent(mainHolder, paramName);
    //
    //                prompt.setValue(paramPrompt);
    //                prompt.setId(paramLabel);
    //                //System.out.println("LI:" + prompt.getId());
    //                lov.setId(paramName);
    //                lov.setLabel(lovLabel);
    //                lov.setSimple(true);
    //                lov.setAutoSubmit(true);
    //                lov.setUnselectedLabel(paramPrompt);
    //
    //                if (componentLabel != null) {
    //                    mainHolder.getChildren().remove(componentLabel);
    //                }
    //                if (component != null) {
    //                    mainHolder.getChildren().remove(component);
    //                }
    //                mainHolder.getChildren().add(prompt);
    //                mainHolder.getChildren().add(lov);
    //                rs.close();
    //                cst.close();
    //                conn.commit();
    //                conn.close();
    //            }
    //            GlobalCC.refreshUI(details);
    //        } catch (SQLException e) {
    //            e.printStackTrace();
    //            GlobalCC.EXCEPTIONREPORTING(conn, e);
    //        }
    //        return null;
    //    }

    public String reQuery(String parentCode, String replacableValue,
                          String replaceValue) {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;
        String paramType = "LOV";
        String paramLabel = null;
        String paramName = null;
        String paramPrompt = null;
        String paramQuery = null;
        String lovLabel = null;
        OracleResultSet rs = null;
        try {

            conn = dbConnector.getDatabaseConnection();
            String rptCode = (String)session.getAttribute("rptCode");
            // System.out.println("parentCode=>" + parentCode);
            String query =
                GlobalCC.reportQuery(rptCode, paramType, parentCode);
            //System.out.println("query=>" + query);
            if (query != null) {
                query = query.toUpperCase();
                query = query.replace(replacableValue, replaceValue);
                cst = (OracleCallableStatement)conn.prepareCall(query);

                rs = (OracleResultSet)cst.executeQuery();
                RichOutputLabel prompt = new RichOutputLabel();
                RichSelectOneChoice lov = new RichSelectOneChoice();

                while (rs.next()) {
                    RichSelectItem select = new RichSelectItem();
                    select.setLabel(rs.getString(2));
                    select.setValue(rs.getString(1));
                    lov.getChildren().add(select);
                }
                rs.close();
                cst.close();

                paramQuery =
                        "begin  TQC_WEB_CURSOR.get_report_parameters(?,?,?,?); end;";
                OracleCallableStatement cst2 = null;
                cst2 = (OracleCallableStatement)conn.prepareCall(paramQuery);
                cst2.registerOutParameter(1, OracleTypes.CURSOR);
                cst2.setString(2, rptCode);
                cst2.setString(3, paramType);
                cst2.setString(4, parentCode);
                cst2.execute();
                OracleResultSet rs2 = (OracleResultSet)cst2.getObject(1);
                while (rs2.next()) {
                    lovLabel = rs2.getString(1);
                    paramLabel = "ol" + rs2.getString(1);
                    paramPrompt = rs2.getString(2);
                    paramName = rs2.getString(6);
                }
                rs2.close();
                cst2.close();

                UIComponent componentLabel =
                    findComponent(mainHolder, paramLabel);
                UIComponent component = findComponent(mainHolder, paramName);

                prompt.setValue(paramPrompt);
                prompt.setId(paramLabel);
                //System.out.println("LI:" + prompt.getId());
                lov.setId(paramName);
                lov.setLabel(lovLabel);
                lov.setSimple(true);
                lov.setAutoSubmit(true);
                lov.setUnselectedLabel(paramPrompt);

                if (componentLabel != null) {
                    mainHolder.getChildren().remove(componentLabel);
                }
                if (component != null) {
                    mainHolder.getChildren().remove(component);
                }
                mainHolder.getChildren().add(prompt);
                mainHolder.getChildren().add(lov);
                rs.close();
                cst.close();
                conn.commit();
                conn.close();
            }
            //GlobalCC.refreshUI(dtls);
        } catch (SQLException e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cst, rs);
        }
        return null;
    }
    //    public void queryLovChanged(ValueChangeEvent evt) {
    //        if (evt.getNewValue() != evt.getOldValue()) {
    //            String label = evt.getComponent().getId();
    //            RichSelectOneChoice comm = (RichSelectOneChoice)evt.getComponent();
    //            String parentCode = comm.getLabel();
    //           reQuery(parentCode, label, evt.getNewValue().toString());
    //        }
    //    }

    public void queryLovChanged(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue()) {
            String label = evt.getComponent().getId();
            RichSelectOneChoice comm = (RichSelectOneChoice)evt.getComponent();
            String parentCode = comm.getLabel();
            // System.out.println("label= " + label);
            //if (label.equalsIgnoreCase("V_REG_CODE")) {
            //String regCode = (String)session.getAttribute("regCode");
            if (evt.getNewValue() == null) {

            } else {
                session.setAttribute("rptParentValue", evt.getNewValue());
                reQuery(parentCode, label, evt.getNewValue().toString());
            }
            //
            // }
            // if (label.equalsIgnoreCase("V_BRN_CODE")) {
            //String regCode = (String)session.getAttribute("regCode");
            //  reQuery(parentCode, label, evt.getNewValue().toString());
            // }
        }
    }

    public void newSelectRptQuery(ActionEvent evt) {
        System.out.println("Select Success!");
        try {
            session.setAttribute("rpt_param_name", evt.getComponent().getId().substring(7));
            System.out.println("Param Name: " + session.getAttribute("rpt_param_name"));
            ADFUtils.findIterator("findQueryParametersIterator").executeQuery();
            GlobalCC.refreshUI(reportQueryTbl);
            GlobalCC.refreshUI(repLov);
            GlobalCC.showPop("crm:queryLovPop");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
    }

    public String getQueryViewString() {
        if (session.getAttribute("filter") == null) {
            filterView(reportQueryTbl, "valueDesc");
            session.setAttribute("filter", "Y");
        }
        return "Values";
    }

    public static String filterView(RichTable table, String columnFilter) {
        FilterableQueryDescriptor queryDescriptor =
            (FilterableQueryDescriptor)table.getFilterModel();
        Set featureSet = new HashSet();
        featureSet.add(FilterableQueryDescriptor.FilterFeature.CASE_INSENSITIVE);
        Map filterFeatures = new HashMap();
        ((HashMap)filterFeatures).put(columnFilter, featureSet);
        queryDescriptor.setFilterFeatures(filterFeatures);
        HashMap<String, Object> criteria = new HashMap<String, Object>();
        criteria.put(columnFilter, "%Search%");
        queryDescriptor.setFilterCriteria(criteria);
        table.setFilterVisible(true);
        table.queueEvent(new QueryEvent(table, queryDescriptor));
        //String filter = criteria.get(columnFilter).toString();
        return null;

    }

    public void filterChanged(ValueChangeEvent evt) {
        if (evt.getNewValue() != null) {
            getQueryViewString();
            session.setAttribute("filter", "Y");
            ADFUtils.findIterator("findReportQueryIterator").executeQuery();
            GlobalCC.refreshUI(reportQueryTbl);
        }
    }

    public String selectReportQueryValue() {
        Object key2 = reportQueryTbl.getSelectedRowData();
        if (key2 == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        try {
            String componentId = GlobalCC.checkNullValues(session.getAttribute("rpt_param_name"));
            if ( componentId != null ) {
                RichInputText comp =(RichInputText)findComponent(mainHolder, componentId);
                comp.setValue(r.getAttribute("value"));
                session.setAttribute(componentId, r.getAttribute("code"));
            } 
            GlobalCC.refreshUI(details);
            GlobalCC.hidePopup("crm:queryLovPop");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING( e);
        }
        return null;
    }

/*
 *     public String selectReportQueryValue() {
        Object key2 = reportQueryTbl.getSelectedRowData();
        if (key2 == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        try {
            String componentId = (String)session.getAttribute("componentId");
            RichInputText comp =
                (RichInputText)findComponent(mainHolder, componentId);
            //System.out.println("comp id"+ comp.getId());
            comp.setValue(r.getAttribute("value"));
            String V_ENDR_CODE = null;
            if (componentId.equalsIgnoreCase("V_POL_CODE")) {
                if (r.getAttribute("code") != null) {
                    session.setAttribute("policyProposalCode", new BigDecimal(r.getAttribute("code").toString()));
                    session.setAttribute("policyNo",new BigDecimal(r.getAttribute("code").toString()));
                    session.setAttribute("polCode",new BigDecimal(r.getAttribute("code").toString()));
                }
            } else if (componentId.equalsIgnoreCase("P_POLICY_NO")) {
                session.setAttribute("policyNo", r.getAttribute("code"));
            } else if (componentId.equalsIgnoreCase("P_STATUS")||componentId.equalsIgnoreCase("V_STATUS")) {
                session.setAttribute("status", r.getAttribute("code"));
            } else if (componentId.equalsIgnoreCase("P_CLIENT")||componentId.equalsIgnoreCase("V_CLIENT")) {
                session.setAttribute("client", r.getAttribute("code"));
            } else if (componentId.equalsIgnoreCase("V_ENDR_CODE")) {
                session.setAttribute("endorsementCode",
                                     new BigDecimal(r.getAttribute("code").toString()));
            } else if (componentId.equalsIgnoreCase("V_PRP_CODE") ||
                       componentId.equalsIgnoreCase("P_CLNT_CODE")) {
                session.setAttribute("prpCode",
                                     new BigDecimal(r.getAttribute("code").toString()));
                session.setAttribute("clntCode",
                                     new BigDecimal(r.getAttribute("code").toString()));
            } else if (componentId.equalsIgnoreCase("V_CLAIM_NO") ||
                       componentId.equalsIgnoreCase("V_CLM_NO")) {
                session.setAttribute("claimNumber", r.getAttribute("code"));
            } else if (componentId.equalsIgnoreCase("V_AGEN_CODE") ||
                       componentId.equalsIgnoreCase("V_AGN_CODE")) {
                session.setAttribute("agnCode", new BigDecimal(r.getAttribute("code").toString()));
            }
            GlobalCC.refreshUI(comp);
            GlobalCC.refreshUI(details);
            GlobalCC.hidePopup("crm:queryLovPop");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING( e);
        }
        return null;
    }
 */

    public String cancelSelectRptQuery() {
        GlobalCC.hidePopup("crm:queryLovPop");
        return null;
    }

    public UIComponent getComponentById(List componentList, String id) {
        try {
            for (int i = 0; i < componentList.size(); i++) {
                UIComponent component = (UIComponent)(componentList.get(i));
                //System.out.print("COMP ID: " + component.getId());
                if (component.getId() != null && id!=null && !"".equals(id)) {
                    if(component.getId().equals(id)){
                      return component;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private UIComponent findComponent(UIComponent parent, String id) {
        try {
            List li = parent.getChildren();
            UIComponent component = null;
            UIComponent childComp = null;
            for (int i = 0; i < li.size(); i++) {
                UIComponent uiComponent = (UIComponent)li.get(i);
                if (uiComponent.getChildren() != null) {
                    childComp =
                            getComponentById(uiComponent.getChildren(), id);
                }
                if (childComp != null) {
                    uiComponent = childComp;
                }
                if (uiComponent.getId() != null &&
                    uiComponent.getId().equals(id)) {
                    // System.out.println("Comp Id: " + uiComponent.getId());
                    return uiComponent;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public void accAllSelec(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
    }

    public void paramReport(ActionEvent actionEvent) {
        DBConnector datahandler = new DBConnector();
        Connection conn;
        conn = datahandler.getDatabaseConnection();
        CallableStatement cst = null;
        ResultSet rs = null;
        try {
            String reportId = actionEvent.getComponent().getId();
            reportId = reportId.replace("rpt", "");
            session.setAttribute("rptCode", reportId);
            if (session.getAttribute("rptCode") == null) {
                GlobalCC.errorValueNotEntered("No Report Selected");
            } else {
                reportId = (String)session.getAttribute("rptCode");
                reportId = reportId.replace("rpt", "");

                session.setAttribute("rptCode", reportId);
                String sql =
                    "SELECT NVL(SUBSTR(RPT_NAME,1,INSTR(RPT_NAME,'_RPT')),RPT_NAME) FROM tqc_system_reports where RPT_CODE=?";
                cst = conn.prepareCall(sql);
                cst.setString(1, reportId);
                rs = cst.executeQuery();

                if (rs.next()) {

                    session.setAttribute("ProcessAreaShtDesc",
                                         rs.getString(1));
                    session.setAttribute("ProcessSubAShtDesc",
                                         "R" + rs.getString(1));
                }
            }

            if (reportId.equalsIgnoreCase("0")) {

                GlobalCC.showPop("crm:paramReportPop");

            } else {
                System.out.println("This is executes pop right");
                reportId = reportId.replace("rpt", "");
                session.setAttribute("rptCode", reportId);
                createComponents();
                GlobalCC.refreshUI(mainHolder);
                GlobalCC.showPop("crm:paramReportPop");
            }

        } catch (SQLException ex) {
            GlobalCC.INFORMATIONREPORTING("Error Occured While Rendering Report Parameters");
            ex.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, cst, rs);
        }
    }
    public void paramReportAgent(ActionEvent actionEvent) {
        DBConnector datahandler = new DBConnector();
        Connection conn;
        conn = datahandler.getDatabaseConnection();
        CallableStatement cst = null;
        ResultSet rs = null;
        try {
            String reportId = actionEvent.getComponent().getId();
            reportId = reportId.replace("rpt", "");
            session.setAttribute("rptCode", reportId);
            if (session.getAttribute("rptCode") == null) {
                GlobalCC.errorValueNotEntered("No Report Selected");
            } else {
                reportId = (String)session.getAttribute("rptCode");
                reportId = reportId.replace("rpt", "");

                session.setAttribute("rptCode", reportId);
                String sql =
                    "SELECT NVL(SUBSTR(RPT_NAME,1,INSTR(RPT_NAME,'_RPT')),RPT_NAME) FROM tqc_system_reports where RPT_CODE=?";
                cst = conn.prepareCall(sql);
                cst.setString(1, reportId);
                rs = cst.executeQuery();

                if (rs.next()) {

                    session.setAttribute("ProcessAreaShtDesc", rs.getString(1));
                    session.setAttribute("ProcessSubAShtDesc",  "R" + rs.getString(1));
                }
            }

            if (reportId.equalsIgnoreCase("0")) { 
               GlobalCC.showPopup("pt1:paramReportPop"); 
            } else {
                System.out.println("This is executes pop right");
                reportId = reportId.replace("rpt", "");
                session.setAttribute("rptCode", reportId); 
                GlobalCC.showPopup("pt1:paramReportPop"); 
                createComponents();
                GlobalCC.refreshUI(mainHolder);
            }

        } catch (SQLException ex) {
            GlobalCC.INFORMATIONREPORTING("Error Occured While Rendering Report Parameters");
            ex.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, cst, rs);
        }
    }
    //    public void paramReport(ActionEvent actionEvent) {
    //        String reportId = actionEvent.getComponent().getId();
    //        reportId = reportId.replace("rpt", "");
    //        if (reportId.equalsIgnoreCase("0")) {
    //            if (session.getAttribute("rptCode") == null) {
    //                GlobalCC.errorValueNotEntered("No Report Selected");
    //            } else {
    //                reportId = (String)session.getAttribute("rptCode");
    //                reportId = reportId.replace("rpt", "");
    //                session.setAttribute("rptCode", reportId);
    //                GlobalCC.showPopup("crm:paramReport");
    //
    //            }
    //        } else {
    //            reportId = reportId.replace("rpt", "");
    //            session.setAttribute("rptCode", reportId);
    //            GlobalCC.showPopup("crm:paramReport");
    //        }
    //        try{
    //        createComponents();
    //        }catch(Exception e)
    //        {
    //            e.printStackTrace();
    //            }
    //        GlobalCC.refreshUI(mainParent);
    //    }


    public void noparamReport(ActionEvent actionEvent) {
        String reportId = actionEvent.getComponent().getId();
        reportId = reportId.replace("rpt", "");
        System.out.println("reportId: "+reportId);
        ReportEngine rpt = new ReportEngine();
        rpt.reportOne(new BigDecimal(reportId));

    }

    public void setRptlink(RichCommandLink rptlink) {
        this.rptlink = rptlink;
    }

    public RichCommandLink getRptlink() {
        return rptlink;
    }

    /*public String genRpt() {
        int k = 0;
        int t = 0;
        while (k < mainHolder.getChildCount()) {
            if (t == 1) {
                UIComponent mine = mainHolder.getChildren().get(k);
                String label = mine.getId();
            if (label != null) {
                System.out.println("label=>" + label);
                if (label.equalsIgnoreCase("V_PROD_CODE")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("prodCode", value.getValue());
                }else if(label.equalsIgnoreCase("V_CLNT_CODE")){ 
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("clntCode", value.getValue());
               } else if (label.equalsIgnoreCase("V_CLA_CODE")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("claCode", value.getValue());
                } else if (label.equalsIgnoreCase("V_BRN_CODE") ||
                           label.equalsIgnoreCase("V_BRN_CODE_1") ||
                           label.equalsIgnoreCase("V_BRN") ||
                           label.equalsIgnoreCase("V_CHKPROPBRN") ||
                           label.equalsIgnoreCase("V_ProppremBRN")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("brnCode", value.getValue());
                } else if (label.equalsIgnoreCase("V_AGEN_CODE") ||
                           label.equalsIgnoreCase("V_AGN_CODE")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("agnCode", value.getValue());
                } else if (label.equalsIgnoreCase("V_DATE_1") ||
                           label.equalsIgnoreCase("V_DATE_FROM") ||
                           label.equalsIgnoreCase("FROMDT") ||
                           label.equalsIgnoreCase("DATE_FROM")) {
                    RichInputDate value = (RichInputDate)mine;
                    if (value.getValue() != null) {
                        session.setAttribute("dateFrom",
                                             GlobalCC.parseDate(value.getValue().toString()));
                    }

                } else if (label.equalsIgnoreCase("V_DATE_2") ||
                           label.equalsIgnoreCase("V_DATE_TO") ||
                           label.equalsIgnoreCase("TODT") ||
                           label.equalsIgnoreCase("DATE_TO")) {
                    RichInputDate value = (RichInputDate)mine;
                    if (value.getValue() != null) {
                        session.setAttribute("dateTo",
                                             GlobalCC.parseDate(value.getValue().toString()));
                    }
                } else if (label.equalsIgnoreCase("V_DATE_3")) {
                    RichInputDate value = (RichInputDate)mine;
                    if (value.getValue() != null) {
                        session.setAttribute("dateThree",
                                             GlobalCC.parseDate(value.getValue().toString()));
                    }
                } else if (label.equalsIgnoreCase("V_POL_CODE")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("policyProposalCode",value.getValue());
                    session.setAttribute("polCode", value.getValue());
                } else if (label.equalsIgnoreCase("V_TT_CODE")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("ttCode", value.getValue());
                } else if (label.equalsIgnoreCase("V_RATE_CODE")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("rateCode", value.getValue());
                } else if (label.equalsIgnoreCase("V_AGNT_TYPE")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("actCode", value.getValue());
                } else if (label.equalsIgnoreCase("V_MAT_TYPE") ||
                           label.equalsIgnoreCase("V_PM_MATURITY_TYPE")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("maturityType", value.getValue());
                } else if (label.equalsIgnoreCase("V_DATE")) {
                    RichInputDate value = (RichInputDate)mine;
                    if (value.getValue() != null) {
                        session.setAttribute("date",GlobalCC.parseDate(value.getValue().toString()));
                    }
                } else if (label.equalsIgnoreCase("V_LNA_NO")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("loanNo", value.getValue());
                } else if (label.equalsIgnoreCase("V_P_STATUS")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("policyStatus", value.getValue());
                } else if (label.equalsIgnoreCase("V_AUTHORIZED_BY")) {
                    RichInputText value = (RichInputText)mine;
                    session.setAttribute("authorizedBy", value.getValue());
                } else if (label.equalsIgnoreCase("V_CHECKED_BY")) {
                    RichInputText value = (RichInputText)mine;
                    session.setAttribute("checkedBy", value.getValue());
                } else if (label.equalsIgnoreCase("V_PVSOURCE")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("source", value.getValue());
                } else if (label.equalsIgnoreCase("V_DESCRIPTION")) {
                    RichInputText value = (RichInputText)mine;
                    session.setAttribute("description", value.getValue());
                } else if (label.equalsIgnoreCase("V_PERIOD")) {
                    RichInputNumberSpinbox value =
                        (RichInputNumberSpinbox)mine;
                    session.setAttribute("period", value.getValue());
                } else if (label.equalsIgnoreCase("V_PREPARED_BY")) {
                    RichInputText value = (RichInputText)mine;
                    session.setAttribute("preparedBy", value.getValue());
                } else if (label.equalsIgnoreCase("V_YEAR")) {
                    RichInputNumberSpinbox value =
                        (RichInputNumberSpinbox)mine;
                    session.setAttribute("year", value.getValue());
                }   else if (label.equalsIgnoreCase("P_STATUS")||label.equalsIgnoreCase("V_STATUS")) {
                    if(mine instanceof RichSelectOneChoice)
                    {
                        RichSelectOneChoice value = (RichSelectOneChoice)mine;
                        session.setAttribute("status", value.getValue());
                    }
                } 
                if (label.equalsIgnoreCase("V_REG_CODE")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("regCode", value.getValue());
                }
                if (label.equalsIgnoreCase("V_BRA_CODE")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("braCode", value.getValue());
                }
                if (label.equalsIgnoreCase("V_BRU_CODE")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("bruCode", value.getValue());
                }
                if (label.equalsIgnoreCase("V_MATURITY_DATE")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("maturityDate", value.getValue());
                } else if (label.equalsIgnoreCase("V_CLAIM_NO") ||
                           label.equalsIgnoreCase("V_CLM_NO")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("claimNumber", value.getValue());
                } else if (label.equalsIgnoreCase("V_ENDR_CODE")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("endorsementCode", value.getValue());
                } else if (label.equalsIgnoreCase("V_SIGN_DESIG")) {
                    RichInputText value = (RichInputText)mine;
                    session.setAttribute("designation", value.getValue());
                } else if (label.equalsIgnoreCase("V_SIGN_NAME") ||
                           label.equalsIgnoreCase("V_SIGNATORY")) {
                    RichInputText value = (RichInputText)mine;
                    session.setAttribute("signatoryName", value.getValue());
                } else if (label.equalsIgnoreCase("V_REFERENCE")) {
                    RichInputText value = (RichInputText)mine;
                    session.setAttribute("reference", value.getValue());
                } else if (label.equalsIgnoreCase("V_SIGN_FOR")) {
                    RichInputText value = (RichInputText)mine;
                    session.setAttribute("signFor", value.getValue());
                } else if (label.equalsIgnoreCase("V_PRP_CODE")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("prpCode", value.getValue());
                } else if (label.equalsIgnoreCase("P_BBR")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    System.out.println(value.getValue());
                    if (value.getValue() != null) {
                        if (value.getValue().toString().contains("ALL")) {
                            session.setAttribute("bbrCode", null);
                        } else {
                            session.setAttribute("bbrCode", value.getValue());
                        }
                    } else {
                        session.setAttribute("bbrCode", value.getValue());
                    }
                } else if (label.equalsIgnoreCase("V_CHEQUE_NO")) {
                    RichInputText value = (RichInputText)mine;
                    session.setAttribute("chequeNo", value.getValue());
                } else if (label.equalsIgnoreCase("V_ENCLOSURE")) {
                    RichInputText value = (RichInputText)mine;
                    session.setAttribute("enclosure", value.getValue());
                } else if (label.equalsIgnoreCase("V_DD_REF_NO")) {
                    RichInputText value = (RichInputText)mine;
                    session.setAttribute("ddRefNo", value.getValue());
                } else if (label.equalsIgnoreCase("ASOFDATE")) {
                    RichInputDate value = (RichInputDate)mine;
                    if (value.getValue() != null) {
                        session.setAttribute("asAtDate",
                                             GlobalCC.parseDate(value.getValue().toString()));
                    }
                } else if (label.equalsIgnoreCase("P_CLIENT")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("client", value.getValue());
                } else if (label.equalsIgnoreCase("P_SHOW_CLNT_STATUS")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("clientStatus", value.getValue());
                } else if (label.equalsIgnoreCase("P_CURR")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("currency", value.getValue());
                } else if (label.equalsIgnoreCase("V_POL_CODES")) {
                    RichInputText value = (RichInputText)mine;
                    session.setAttribute("polCode", value.getValue());
                } else if (label.equalsIgnoreCase("V_POLNO")) {
                    RichInputText value = (RichInputText)mine;
                    session.setAttribute("policyNumber", value.getValue());
                }else if (label.equalsIgnoreCase("P_POLICY_NO")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("policyNo", value.getValue());
                } 
                else if (label.equalsIgnoreCase("P_CLNT_STATUS")) {
                    RichInputText value = (RichInputText)mine;
                    session.setAttribute("clientStatus", value.getValue());
                }else if(label.equalsIgnoreCase("V_CLNT_STATUS")){ 
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    session.setAttribute("clntStatus", value.getValue());
                }
                else if (label.equalsIgnoreCase("V_CLNA_STATUS")) {
                    RichSelectOneChoice value = (RichSelectOneChoice)mine;
                    System.out.println(value.getValue());
                    if (value.getValue() != null) {
                        session.setAttribute("clnaStatus", value.getValue());
                    } else {
                        session.setAttribute("clnaStatus", "A");
                    }
                }
            }
                t--;
            } else {
                t++;
            }

            k++;
        }
        String reportId = (String)session.getAttribute("rptCode");
        System.out.println("rptCode: "+reportId);
        ReportEngine rpt = new ReportEngine();
        rpt.reportOne(new BigDecimal(reportId));
        return null;
    }*/

    public String genRpt() {
            int k = 0;
            int t = 0;
            while (k < mainHolder.getChildCount()) {
                if (t == 1) {
                    UIComponent mine = mainHolder.getChildren().get(k);
                    String label = mine.getId();
                    String value=null;
                    if (label != null) 
                    {
                        session.setAttribute(label, null);
                        if (mine.getClass() ==  RichSelectOneChoice.class) {
                            RichSelectOneChoice ui = (RichSelectOneChoice)mine;
                            session.setAttribute(label, ui.getValue());
                        }else if (mine.getClass() ==  RichInputDate.class ) {
                            RichInputDate ui = (RichInputDate)mine;
                            session.setAttribute(label,  GlobalCC.parseDate(ui.getValue()));
                        }else if (mine.getClass() ==  RichInputText.class ) {
                            RichInputText ui = (RichInputText)mine;
                            session.setAttribute(label,  ui.getValue());
                        }else if (mine.getClass() ==  RichInputNumberSpinbox.class ) {
                            RichInputNumberSpinbox ui = (RichInputNumberSpinbox)mine;
                            session.setAttribute(label,  ui.getValue());
                         }
                        System.out.println("label --> " + label+" value --> "+session.getAttribute(label));
                    }
                    t--;
                } else {
                    t++;
                }
                k++;
            }
            String reportId = (String)session.getAttribute("rptCode");
            System.out.println("rptCode: "+reportId);
            ReportEngine rpt = new ReportEngine();
        
            rpt.reportOne(new BigDecimal(reportId));
            return null;
        }
    public void setReportQueryTab(RichTable reportQueryTab) {
        this.reportQueryTab = reportQueryTab;
    }

    public RichTable getReportQueryTab() {
        return reportQueryTab;
    }


    public void setComp(UIComponent comp) {
        this.comp = comp;
    }

    public UIComponent getComp() {
        return comp;
    }

    public void setLovSelectedValue(RichInputText lovSelectedValue) {
        this.lovSelectedValue = lovSelectedValue;
    }

    public RichInputText getLovSelectedValue() {
        return lovSelectedValue;
    }

    public void setChildGrid(HtmlPanelGrid childGrid) {
        this.childGrid = childGrid;
    }

    public HtmlPanelGrid getChildGrid() {
        return childGrid;
    }


    public void changeReportType(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        session.setAttribute("outputFormat", valueChangeEvent.getNewValue().toString());
    }

    public String generateReports() {
        try { 
            genRpt();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public void setDtls(RichPanelGroupLayout dtls) {
        this.dtls = dtls;
    }

    public RichPanelGroupLayout getDtls() {
        return dtls;
    }

    public void setMainParent(HtmlPanelGrid mainParent) {
        this.mainParent = mainParent;
    }

    public HtmlPanelGrid getMainParent() {
        return mainParent;
    }

    public void setReportType(RichSelectOneRadio reportType) {
        this.reportType = reportType;
    }

    public RichSelectOneRadio getReportType() {
        return reportType;
    }


    public void setMyTest(RichInputDate myTest) {
        this.myTest = myTest;
    }

    public RichInputDate getMyTest() {
        return myTest;
    }

    public void setMainHolder(HtmlPanelGrid mainHolder) {
        this.mainHolder = mainHolder;
    }

    public HtmlPanelGrid getMainHolder() {
        return mainHolder;
    }

    public void setDetails(RichPanelGroupLayout details) {
        this.details = details;
    }

    public RichPanelGroupLayout getDetails() {
        return details;
    }

    public void setRepLov(RichPopup repLov) {
        this.repLov = repLov;
    }

    public RichPopup getRepLov() {
        return repLov;
    }

    public void setQueryLov(RichDialog queryLov) {
        this.queryLov = queryLov;
    }

    public RichDialog getQueryLov() {
        return queryLov;
    }

    public void setReportQueryTbl(RichTable reportQueryTbl) {
        this.reportQueryTbl = reportQueryTbl;
    }

    public RichTable getReportQueryTbl() {
        return reportQueryTbl;
    }
}
