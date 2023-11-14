package TurnQuest.view.Divisions;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.internal.OracleTypes;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class DivisionsManipulation {
    private RichInputText shtDesc;
    private RichInputText divName;
    private RichSelectOneChoice divStatus;
    private RichTree divisionsTree;
    private RichTable subDivisionsLOV;
    private RichTable brnDivisionsLOV;
    private RichInputText subdivshtDesc;
    private RichInputText subDivDesc;
    private RichOutputText branchLabel;
    private RichInputDate wet;
    private RichInputDate wef;
    private RichTable branchesLOV;
    private RichTree treeOrgDivisions;

    public DivisionsManipulation() {
        super();
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public void DivisionsTreeListener(SelectionEvent selectionEvent) {
        // Add event code here...
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();
            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    divisionsTree.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)divisionsTree.getRowData();

                    session.setAttribute("divisionCode",
                                         nd.getRow().getAttribute("DIV_CODE"));
                    session.setAttribute("subdivCode", null);

                    shtDesc.setValue(nd.getRow().getAttribute("DIV_SHT_DESC"));
                    divName.setValue(nd.getRow().getAttribute("DIV_NAME"));
                    divStatus.setValue(nd.getRow().getAttribute("DIV_DIVISION_STATUS"));

                    //shtDesc.setDisabled(true);
                    //divName.setDisabled(true);
                    //divStatus.setDisabled(true);

                    ADFUtils.findIterator("findSubDivisionsIterator").executeQuery();
                    GlobalCC.refreshUI(subDivisionsLOV);

                    ADFUtils.findIterator("findBranchDivisionsIterator").executeQuery();
                    GlobalCC.refreshUI(brnDivisionsLOV);

                }
            }
        }
    }

    public String NewDivision() {

        try {
            session.setAttribute("divisionCode", null);
            session.setAttribute("action", "A");

            shtDesc.setValue(null);
            divName.setValue(null);
            divStatus.setValue("A");

            shtDesc.setDisabled(false);
            divName.setDisabled(false);
            divStatus.setDisabled(false);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String EditDivision() {

        try {
            session.setAttribute("action", "E");

            shtDesc.setDisabled(false);
            divName.setDisabled(false);
            divStatus.setDisabled(false);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String DeleteDivision() {
        try {
            session.setAttribute("action", "D");
            SaveDivision();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String SaveDivision() {

        if (divName.getValue() == null) {
            GlobalCC.errorValueNotEntered("Enter a Division");
            return null;
        }
        BigDecimal Div = (BigDecimal)session.getAttribute("divisionCode");
        if (session.getAttribute("action") == null) {
            GlobalCC.INFORMATIONREPORTING("Please select an action");
            return null;
        }
        String Action = (String)session.getAttribute("action");

        if (Div == null && !Action.equalsIgnoreCase("A")) {
            GlobalCC.errorValueNotEntered("Select A Division");
            return null;
        }

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = (OracleConnection)dbCon.getDatabaseConnection();
        try {
            String query =
                "begin TQC_WEB_ORGANIZATION_PKG.UPDATE_DIVISIONS(?,?,?,?,?,?,?,?,?,?,?); end;";
            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setString(1, (String)session.getAttribute("action"));
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("divisionCode"));


            if (divName.getValue() == null) {
                cst.setString(3, null);
            } else {
                cst.setString(3, divName.getValue().toString());
            }

            if (shtDesc.getValue() == null) {
                cst.setString(4, null);
            } else {
                cst.setString(4, shtDesc.getValue().toString());
            }

            if (divStatus.getValue() == null) {
                cst.setString(5, null);
            } else {
                cst.setString(5, divStatus.getValue().toString());
            }
            
            cst.setBigDecimal(6, new BigDecimal(session.getAttribute("orgCode").toString()));
            cst.setString(7, (String)session.getAttribute("Username"));
            cst.setString(8, null);
            cst.setString(9, null);
            cst.setString(10, null);
            cst.registerOutParameter(11, OracleTypes.VARCHAR);
            cst.execute();

            if (cst.getString(11) != null) {
                GlobalCC.errorValueNotEntered(cst.getString(7));
                return null;
            }

            ADFUtils.findIterator("findDivisionsByOrgIterator").executeQuery();
            GlobalCC.refreshUI(treeOrgDivisions);

            ADFUtils.findIterator("findSubDivisionsIterator").executeQuery();
            GlobalCC.refreshUI(subDivisionsLOV);

            ADFUtils.findIterator("findBranchDivisionsIterator").executeQuery();
            GlobalCC.refreshUI(brnDivisionsLOV);

            shtDesc.setDisabled(true);
            divName.setDisabled(true);
            divStatus.setDisabled(true);
            cst.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
         GlobalCC.INFORMATIONREPORTING("Division Successfully Saved!");
        return null;
    }

    public String AddSubDivision() {
        try {
            session.setAttribute("action", "A");
            session.setAttribute("subdivCode", null);
            subdivshtDesc.setValue(null);
            subDivDesc.setValue(null);

            // Render Popup
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crmTemplate:subDiv" + "').show(hints);");

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String EditSubDivision() {
        try {
            session.setAttribute("action", "E");

            DCIteratorBinding dciter =
                ADFUtils.findIterator("findSubDivisionsIterator");
            RowKeySet set = subDivisionsLOV.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();
                session.setAttribute("subdivCode",
                                     r.getAttribute("SDIV_CODE"));
                subdivshtDesc.setValue(r.getAttribute("SDIV_SHT_DESC"));
                subDivDesc.setValue(r.getAttribute("SDIV_NAME"));

                // Render Popup
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crmTemplate:subDiv" + "').show(hints);");

            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String DeleteSubDivision() {
        try {
            session.setAttribute("action", "D");

            DCIteratorBinding dciter =
                ADFUtils.findIterator("findSubDivisionsIterator");
            RowKeySet set = subDivisionsLOV.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();
                session.setAttribute("subdivCode",
                                     r.getAttribute("SDIV_CODE"));
                SaveSubDivision();

            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String SaveSubDivision() {
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        try {
            String query =
                "begin TQC_WEB_ORGANIZATION_PKG.UPDATE_SUBDIVISIONS(?,?,?,?,?,?); end;";
            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setString(1, (String)session.getAttribute("action"));
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("subdivCode"));
            if (subDivDesc.getValue() == null) {
                cst.setString(3, null);
            } else {
                cst.setString(3, subDivDesc.getValue().toString());
            }
            if (subdivshtDesc.getValue() == null) {
                cst.setString(4, null);
            } else {
                cst.setString(4, subdivshtDesc.getValue().toString());
            }
            cst.setBigDecimal(5,
                              (BigDecimal)session.getAttribute("divisionCode"));
            cst.registerOutParameter(6, OracleTypes.VARCHAR);
            cst.execute();

            if (cst.getString(6) != null) {
                GlobalCC.errorValueNotEntered(cst.getString(6));
                return null;
            }
            session.setAttribute("subdivCode", null);
            ADFUtils.findIterator("findSubDivisionsIterator").executeQuery();
            GlobalCC.refreshUI(subDivisionsLOV);

            cst.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String AddBranchDivision() {
        try {
            session.setAttribute("action", "A");
            session.setAttribute("brnDivCode", null);

            wef.setValue(null);
            wet.setValue(null);
            branchLabel.setValue(null);

            // Render Popup
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crmTemplate:branches" + "').show(hints);");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String BranchSelected() {
        try {
            DCIteratorBinding dciter =
                ADFUtils.findIterator("findBranchesIterator");
            RowKeySet set = branchesLOV.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();
                session.setAttribute("branchCode", r.getAttribute("BRN_CODE"));
                branchLabel.setValue(r.getAttribute("BRN_NAME"));

                // Render Popup
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crmTemplate:brnDivisions" +
                                     "').show(hints);");

            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String EditBranchDivision() {
        try {
            DCIteratorBinding dciter =
                ADFUtils.findIterator("findBranchDivisionsIterator");
            RowKeySet set = brnDivisionsLOV.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();
                session.setAttribute("brnDivCode",
                                     r.getAttribute("BDIV_CODE"));
                branchLabel.setValue(r.getAttribute("BRN_NAME"));
                wef.setValue(r.getAttribute("BDIV_WEF"));
                wet.setValue(r.getAttribute("BDIV_WET"));

                // Render Popup
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crmTemplate:brnDivisions" +
                                     "').show(hints);");
            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String DeleteBranchDivision() {
        try {
            DCIteratorBinding dciter =
                ADFUtils.findIterator("findBranchDivisionsIterator");
            RowKeySet set = brnDivisionsLOV.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();
                session.setAttribute("brnDivCode",
                                     r.getAttribute("BDIV_CODE"));
                SaveBranchDivision();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String SaveBranchDivision() {
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        try {
            String query =
                "begin TQC_WEB_ORGANIZATION_PKG.UPDATE_BRNCH_DIVISIONS(?,?,?,?,?,?,?); end;";
            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setString(1, (String)session.getAttribute("action"));
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("brnDivCode"));
            cst.setBigDecimal(3,
                              (BigDecimal)session.getAttribute("branchCode"));
            cst.setBigDecimal(4,
                              (BigDecimal)session.getAttribute("divisionCode"));
            if (wef.getValue() == null) {
                cst.setString(5, null);
            } else {
                String todayString = null;
                if (wef.getValue().toString().contains(":")) {

                    todayString =
                            GlobalCC.parseDate(wef.getValue().toString());
                } else {
                    todayString =
                            GlobalCC.upDateParseDate(wef.getValue().toString());
                }
                cst.setString(5, todayString);
            }
            if (wet.getValue() == null) {
                cst.setString(6, null);
            } else {
                String todayString = null;
                if (wet.getValue().toString().contains(":")) {
                    todayString =
                            GlobalCC.parseDate(wet.getValue().toString());
                } else {
                    todayString =
                            GlobalCC.upDateParseDate(wet.getValue().toString());
                }
                cst.setString(6, todayString);
            }
            cst.registerOutParameter(7, OracleTypes.VARCHAR);
            cst.execute();

            if (cst.getString(7) != null) {
                GlobalCC.errorValueNotEntered(cst.getString(7));
                return null;
            }
            cst.close();
            conn.close();
            ADFUtils.findIterator("findBranchDivisionsIterator").executeQuery();
            GlobalCC.refreshUI(brnDivisionsLOV);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setShtDesc(RichInputText shtDesc) {
        this.shtDesc = shtDesc;
    }

    public RichInputText getShtDesc() {
        return shtDesc;
    }

    public void setDivName(RichInputText divName) {
        this.divName = divName;
    }

    public RichInputText getDivName() {
        return divName;
    }

    public void setDivStatus(RichSelectOneChoice divStatus) {
        this.divStatus = divStatus;
    }

    public RichSelectOneChoice getDivStatus() {
        return divStatus;
    }

    public void setDivisionsTree(RichTree divisionsTree) {
        this.divisionsTree = divisionsTree;
    }

    public RichTree getDivisionsTree() {
        return divisionsTree;
    }

    public void setSubDivisionsLOV(RichTable subDivisionsLOV) {
        this.subDivisionsLOV = subDivisionsLOV;
    }

    public RichTable getSubDivisionsLOV() {
        return subDivisionsLOV;
    }

    public void setBrnDivisionsLOV(RichTable brnDivisionsLOV) {
        this.brnDivisionsLOV = brnDivisionsLOV;
    }

    public RichTable getBrnDivisionsLOV() {
        return brnDivisionsLOV;
    }

    public void setSubdivshtDesc(RichInputText subdivshtDesc) {
        this.subdivshtDesc = subdivshtDesc;
    }

    public RichInputText getSubdivshtDesc() {
        return subdivshtDesc;
    }

    public void setSubDivDesc(RichInputText subDivDesc) {
        this.subDivDesc = subDivDesc;
    }

    public RichInputText getSubDivDesc() {
        return subDivDesc;
    }

    public void setBranchLabel(RichOutputText branchLabel) {
        this.branchLabel = branchLabel;
    }

    public RichOutputText getBranchLabel() {
        return branchLabel;
    }

    public void setWet(RichInputDate wet) {
        this.wet = wet;
    }

    public RichInputDate getWet() {
        return wet;
    }

    public void setWef(RichInputDate wef) {
        this.wef = wef;
    }

    public RichInputDate getWef() {
        return wef;
    }

    public void setBranchesLOV(RichTable branchesLOV) {
        this.branchesLOV = branchesLOV;
    }

    public RichTable getBranchesLOV() {
        return branchesLOV;
    }

    public void setTreeOrgDivisions(RichTree treeOrgDivisions) {
        this.treeOrgDivisions = treeOrgDivisions;
    }

    public RichTree getTreeOrgDivisions() {
        return treeOrgDivisions;
    }

    public void treeOrgDivSelectionListener(SelectionEvent selectionEvent) {
        
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();
            
            if (keys != null && keys.getSize() > 0) {

                for (Object treeRowKey : keys) {
                    treeOrgDivisions.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)treeOrgDivisions.getRowData();
                            
                    System.out.println("nd.getRow().getAttribute(\"nodeType\")"+nd.getRow().getAttribute("nodeType"));
                    System.out.println(" nd.getRow().getAttribute(\"orgCode\")"+ nd.getRow().getAttribute("orgCode"));
                    if (nd.getRow().getAttribute("nodeType") == "S") {
                        /*String orgCode =
                            (String)nd.getRow().getAttribute("regOrgCode");
                        String orgName =
                            (String)nd.getRow().getAttribute("orgName");
                        String branchCode =
                            (String)nd.getRow().getAttribute("code");
                        String branchName =
                            (String)nd.getRow().getAttribute("name");*/
                        session.setAttribute("orgCode",
                                             nd.getRow().getAttribute("orgCode"));
                        //session.setAttribute("branchCode", branchCode);


                        // Fetch the Transaction Types within the organization branches
                        //ADFUtils.findIterator("fetchAllTransactionTypesIterator").executeQuery();
                        //GlobalCC.refreshUI(tblTransactions);

                        // Show the panel
                        //panelTransList.setVisible(true);
                        //GlobalCC.refreshUI(panelMain);
                        session.setAttribute("divisionCode",
                                             nd.getRow().getAttribute("DIV_CODE"));
                        session.setAttribute("subdivCode", null);

                        shtDesc.setValue(nd.getRow().getAttribute("DIV_SHT_DESC"));
                        divName.setValue(nd.getRow().getAttribute("DIV_NAME"));
                        divStatus.setValue(nd.getRow().getAttribute("DIV_DIVISION_STATUS"));

                        //shtDesc.setDisabled(true);
                        // divName.setDisabled(true);
                        // divStatus.setDisabled(true);

                        GlobalCC.refreshUI(shtDesc);
                        GlobalCC.refreshUI(divName);
                        GlobalCC.refreshUI(divStatus);

                        ADFUtils.findIterator("findSubDivisionsIterator").executeQuery();
                        GlobalCC.refreshUI(subDivisionsLOV);

                        ADFUtils.findIterator("findBranchDivisionsIterator").executeQuery();
                        GlobalCC.refreshUI(brnDivisionsLOV);

                    } else if (nd.getRow().getAttribute("nodeType") == "P") {
                        // Hide the panel
                        //panelTransList.setVisible(false);
                        //GlobalCC.refreshUI(panelMain);
                    }
                }
            }
        }
    }
}
