package TurnQuest.view.schedule;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import java.util.ArrayList;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectItem;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.nav.RichCommandLink;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;


public class ScreenBack {
    private RichInputText levelName;
    private RichInputText scheduleTableName;
    private RichInputNumberSpinbox levelNo;
    private RichInputText tablePrefix;
    private RichInputText tableSequence;
    private HtmlPanelGrid levelButtons;
    private RichPanelBox tableStructureTab;
    private HtmlPanelGrid scheduleTableDetails;
    private ArrayList<String> columnName = new ArrayList<String>();
    private ArrayList<String> columnDataType = new ArrayList<String>();
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private RichTable banktbl;
    private RichInputText txtBankname;
    private HtmlPanelGrid dynamicContainer;

    public ScreenBack() {
    }

    public void setLevelName(RichInputText levelName) {
        this.levelName = levelName;
    }

    public RichInputText getLevelName() {
        return levelName;
    }

    public void setScheduleTableName(RichInputText scheduleTableName) {
        this.scheduleTableName = scheduleTableName;
    }

    public RichInputText getScheduleTableName() {
        return scheduleTableName;
    }

    public void setLevelNo(RichInputNumberSpinbox levelNo) {
        this.levelNo = levelNo;
    }

    public RichInputNumberSpinbox getLevelNo() {
        return levelNo;
    }

    public void setTablePrefix(RichInputText tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public RichInputText getTablePrefix() {
        return tablePrefix;
    }

    public void setTableSequence(RichInputText tableSequence) {
        this.tableSequence = tableSequence;
    }

    public RichInputText getTableSequence() {
        return tableSequence;
    }

    public void setLevelButtons(HtmlPanelGrid levelButtons) {
        this.levelButtons = levelButtons;
    }

    public HtmlPanelGrid getLevelButtons() {
        return levelButtons;
    }

    public void setTableStructureTab(RichPanelBox tableStructureTab) {
        this.tableStructureTab = tableStructureTab;
    }

    public RichPanelBox getTableStructureTab() {
        return tableStructureTab;
    }

    public void setScheduleTableDetails(HtmlPanelGrid scheduleTableDetails) {
        this.scheduleTableDetails = scheduleTableDetails;
    }

    public HtmlPanelGrid getScheduleTableDetails() {
        return scheduleTableDetails;
    }

    public void setBanktbl(RichTable banktbl) {
        this.banktbl = banktbl;
    }

    public RichTable getBanktbl() {
        return banktbl;
    }


    public void setTxtBankname(RichInputText txtBankname) {
        this.txtBankname = txtBankname;
    }

    public RichInputText getTxtBankname() {
        return txtBankname;
    }

    public String NextTransition() {
        Connection conn = null;
        try {
            if (txtBankname.getValue() == null) {
                GlobalCC.errorValueNotEntered("Select A Bank...");
                return null;
            }

            if (scheduleTableName.getValue() == null) {
                GlobalCC.errorValueNotEntered("Enter A Table Name");
                return null;
            }

            // Validate Table Provided.
            Integer TotalColumns =
                ValidateTableName(scheduleTableName.getValue().toString());
            if (TotalColumns == 0) {
                GlobalCC.errorValueNotEntered("The Table is Invalid");
                return null;
            }

            //Save Table Details...
            SaveProductTable();

            //  tableDescription1.setValue(scheduleTableName.getValue().toString());

            //Construct Dynamic Objects...
            ConstructTemplateStruct();
            //  tableStructureTab.setVisible(arg0);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public Integer ValidateTableName(String TableName) {
        Connection conn = null;
        Integer TotalColumns = 0;
        try {
            conn = new DBConnector().getDatabaseConnection();
            String CountQuery =
                "SELECT COUNT(*) FROM TQC_USER_TAB_COLUMNS WHERE TABLE_NAME='" +
                TableName + "'";
            CallableStatement cst1 = null;
            cst1 = conn.prepareCall(CountQuery);
            ResultSet rs1 = cst1.executeQuery();
            while (rs1.next()) {
                TotalColumns = rs1.getInt(1);
            }
            cst1.close();
            rs1.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return TotalColumns;
    }

    public String SaveProductTable() {
        Connection conn = null;
        try {
            conn = new DBConnector().getDatabaseConnection();
            String CountQuery =
                "begin TQC_SETUPS_PKG.save_bank_import_tmplt(?,?); end;";
            CallableStatement cst1 = null;
            cst1 = conn.prepareCall(CountQuery);
            cst1.setBigDecimal(1,
                               new BigDecimal(session.getAttribute("bankCode").toString()));
            cst1.setString(2, scheduleTableName.getValue().toString());
            cst1.execute();
            cst1.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String ConstructTemplateStruct() {
        Connection conn = null;
        try {
            //CLEAR CONTAINER..
            dynamicContainer.getChildren().clear();

            //COUNT THE COLUMNS ON THE TEMPLATE TABLE..
            Integer TotalColumns = ValidateTableName("TQC_BANK_IMP_TMPLT");
            System.out.println("Total Columns " + TotalColumns);

            //GET THE COLUMNS ON THE TEMPLATE TABLE..
            String Query =
                "SELECT * FROM TQC_BANK_IMP_TMPLT WHERE LPIT_BNK_CODE = ?";
            CallableStatement cst = null;
            conn = new DBConnector().getDatabaseConnection();
            cst = conn.prepareCall(Query);
            cst.setObject(1,
                          new BigDecimal(session.getAttribute("bankCode").toString()));
            ResultSet rs = cst.executeQuery();
            int iCount = 4;
            int IDCount = 1;
            while (rs.next()) {
                while (iCount <= TotalColumns) {
                    //CONSTRUCT THE COMPONENTS. FIRST COLUMN NAME.
                    RichOutputLabel LabelName = new RichOutputLabel();
                    LabelName.setId("DynLabel_" + IDCount);
                    LabelName.setValue("Column Name");
                    LabelName.setInlineStyle("font-weight:bold;");
                    dynamicContainer.getChildren().add(LabelName);
                    RichInputText TextName = new RichInputText();
                    TextName.setId("DynText_" + IDCount);
                    TextName.setValue(rs.getString(iCount));
                    dynamicContainer.getChildren().add(TextName);
                    //CONSTRUCT THE COMPONENTS. SECOND THE POSITION.
                    RichOutputLabel LabelName2 = new RichOutputLabel();
                    LabelName2.setId("DynLabel2_" + IDCount);
                    LabelName2.setValue("Position");
                    LabelName2.setInlineStyle("font-weight:bold;");
                    dynamicContainer.getChildren().add(LabelName2);
                    RichInputNumberSpinbox SpinBoxName =
                        new RichInputNumberSpinbox();
                    SpinBoxName.setId("DynSpin_" + IDCount);
                    SpinBoxName.setValue(rs.getString(iCount + 1));
                    dynamicContainer.getChildren().add(SpinBoxName);

                    //CONSTRUCT THE COMPONENTS. THIRD THE MAPPING..
                    RichOutputLabel LabelName3 = new RichOutputLabel();
                    LabelName3.setId("DynLabel3_" + IDCount);
                    LabelName3.setValue("Maps Into");
                    LabelName3.setInlineStyle("font-weight:bold;");
                    dynamicContainer.getChildren().add(LabelName3);

                    RichSelectOneChoice ChoiceName =
                        UserTableColumns(scheduleTableName.getValue().toString(),
                                         IDCount, rs.getString(iCount + 2));
                    //ChoiceName.setId("DynText_"+iCount+2);
                    //ChoiceName.setValue(rs.getString(iCount+2));
                    //ChoiceName.getChildren().add(ItemNames);
                    dynamicContainer.getChildren().add(ChoiceName);

                    RichCommandLink clearLink = getClearLink(IDCount);
                    //  clear.setId("DynLink_"+IDCount);
                    // clear.setText("Clear fields");
                    dynamicContainer.getChildren().add(clearLink);

                    iCount = iCount + 3;
                    IDCount++;
                }
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
            e.printStackTrace();
        }
        return null;
    }

    public void setDynamicContainer(HtmlPanelGrid dynamicContainer) {
        this.dynamicContainer = dynamicContainer;
    }

    public HtmlPanelGrid getDynamicContainer() {
        return dynamicContainer;
    }

    public String selectBankDetails() {
        Object key = banktbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("bankCode", r.getAttribute("bankCode"));
        txtBankname.setValue(r.getAttribute("bankName"));
        GlobalCC.refreshUI(txtBankname);
        return null;
    }

    public RichCommandLink getClearLink(int idCount) {
        RichCommandLink clear = new RichCommandLink();
        clear.setId("DynLink_" + idCount);
        clear.setText("Clear fields");
        FacesContext facesCtx = FacesContext.getCurrentInstance();
        Application application = facesCtx.getApplication();
        ExpressionFactory elFactory = application.getExpressionFactory();
        ELContext elContext = facesCtx.getELContext();
        MethodExpression methodExpression = null;
        methodExpression =
                elFactory.createMethodExpression(elContext, "#{ScreenBack.handleClearEvent}",
                                                 Object.class,
                                                 new Class[] { ActionEvent.class });
        MethodExpressionActionListener al = null;
        al = new MethodExpressionActionListener(methodExpression);
        clear.addActionListener(al);

        return clear;
    }

    public RichSelectOneChoice UserTableColumns(String TableName, Integer ID,
                                                String Value) {

        RichSelectOneChoice MyChoice = new RichSelectOneChoice();

        Connection conn = null;
        try {
            MyChoice.setId("DynChoice_" + ID);
            MyChoice.setValue(Value);

            conn = new DBConnector().getDatabaseConnection();
            String CountQuery =
                "SELECT COLUMN_NAME FROM TQC_USER_TAB_COLUMNS WHERE TABLE_NAME='" +
                TableName + "'";
            CallableStatement cst1 = null;
            cst1 = conn.prepareCall(CountQuery);
            ResultSet rs1 = cst1.executeQuery();
            int k = 0;
            while (rs1.next()) {
                RichSelectItem ItemNames = new RichSelectItem();
                ItemNames.setId("SD_" + k);
                ItemNames.setLabel(rs1.getString(1));
                ItemNames.setValue(rs1.getString(1));
                MyChoice.getChildren().add(ItemNames);
                k++;

            }
            cst1.close();
            rs1.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return MyChoice;
    }

    public String SaveProductImportTemplateData() {
        Connection conn = null;
        String Parameters = null;
        String[][] ColumnValues = null;
        try {
            int comp = 0;
            int myCount = 0;
            Integer ColumnCount = 0;
            Integer TotalColumns = ValidateTableName("TQC_BANK_IMP_TMPLT");
            Integer ArraySize = TotalColumns; /*/ 3;*/
            float xFloat;
            xFloat = ArraySize;
            ColumnValues = new String[Math.round(xFloat)][5];

            while (comp < dynamicContainer.getChildCount()) {
                UIComponent mine = dynamicContainer.getChildren().get(comp);
                String ComponentID = mine.getId();
                if (ComponentID == null) {

                } else {
                    //LOOP THRU THE COMPONENTS SAVING THERE DATA.
                    if (ComponentID.contains("DynLabel_")) {
                        //do nothing...

                    } else if (ComponentID.contains("DynText_")) {
                        //Text Box.
                        RichInputText Textvalue = (RichInputText)mine;
                        if (Textvalue.getValue() == null) {

                        } else {
                            //GET ID TO DETERMINE COLUMN NAME.
                            String TextValID = Textvalue.getId();
                            String ColumnID =
                                TextValID.replace("DynText_", "");
                            String ColumnName =
                                "LPIT_COL" + ColumnID + "_NAME";
                            Parameters = ColumnName + ",";

                            ColumnValues[ColumnCount][0] =
                                    ColumnName; //Parameter
                            ColumnValues[ColumnCount][1] =
                                    Textvalue.getValue().toString(); //Column Mapping.
                            ColumnCount++;
                        }

                    } else if (ComponentID.contains("DynSpin_")) {
                        //Spin Box.
                        RichInputNumberSpinbox Spinvalue =
                            (RichInputNumberSpinbox)mine;
                        if (Spinvalue.getValue() == null) {

                        } else {
                            //GET ID TO DETERMINE COLUMN NAME.
                            String TextValID = Spinvalue.getId();
                            String ColumnID =
                                TextValID.replace("DynSpin_", "");
                            String ColumnName =
                                "LPIT_COL" + ColumnID + "_POSITION";
                            Parameters = ColumnName + ",";

                            ColumnValues[ColumnCount][0] =
                                    ColumnName; //Parameter
                            ColumnValues[ColumnCount][1] =
                                    Spinvalue.getValue().toString(); //Column Mapping.
                            ColumnCount++;
                        }

                    } else if (ComponentID.contains("DynChoice_")) {
                        //Select One Choice.
                        RichSelectOneChoice Choicevalue =
                            (RichSelectOneChoice)mine;
                        if (Choicevalue.getValue() == null) {

                        } else {
                            //GET ID TO DETERMINE COLUMN NAME.
                            String TextValID = Choicevalue.getId();
                            String ColumnID =
                                TextValID.replace("DynChoice_", "");
                            String ColumnName =
                                "LPIT_COL" + ColumnID + "_MAPPING";
                            Parameters = ColumnName + ",";

                            ColumnValues[ColumnCount][0] =
                                    ColumnName; //Parameter
                            ColumnValues[ColumnCount][1] =
                                    Choicevalue.getValue().toString(); //Column Mapping.
                            ColumnCount++;
                        }

                    }
                }
                comp++;
            }
            String Params = null;
            int count = 0;
            while (myCount < ColumnValues.length) {
                String Value = ColumnValues[myCount][0];
                String saveParameters = null;
                //System.out.println("Value "+Value);
                if (Value == null) {
                    //do nothing..
                } else {
                    String array[] = ColumnValues[myCount][0].split("_");
                    saveParameters = array[1];
                    // System.out.println("Column "+saveParameters);
                    if (Params == null) {
                        Params =
                                ColumnValues[myCount][0] + " = " + "'" + ColumnValues[myCount][1] +
                                "'" + ",";
                    } else {
                        Params =
                                Params + ColumnValues[myCount][0] + " = " + "'" +
                                ColumnValues[myCount][1] + "'" + ",";
                    }
                }
                myCount++;
            }
            
            String FullParameters=null;
            if(Params.substring(0, Params.length() - 1)!=null){
                   FullParameters = Params.substring(0, Params.length() - 1);
                }
            
            System.out.println("Parameters trunc " + FullParameters);
            String Query =
                "UPDATE TQC_BANK_IMP_TMPLT SET " + FullParameters + " WHERE LPIT_BNK_CODE = ?";
            CallableStatement cst = null;
            conn = new DBConnector().getDatabaseConnection();
            cst = conn.prepareCall(Query);
            cst.setObject(1,
                          new BigDecimal(session.getAttribute("bankCode").toString()));
            cst.execute();
            cst.close();
            conn.close();

            GlobalCC.INFORMATIONREPORTING("Template Saved Successfully");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public void handleClearEvent(ActionEvent event) {
        Connection conn = null;
        int compCount = 0;
        String id = null;
        String query = null;
        RichInputText Textvalue = null;
        RichInputNumberSpinbox Spinvalue = null;
        RichSelectOneChoice Choicevalue = null;

        UIComponent comp = event.getComponent();
        id = comp.getId().substring(comp.getId().indexOf("_") + 1);
        query =
                "UPDATE  TQC_BANK_IMP_TMPLT SET LPIT_COL" + id + "_NAME=null,LPIT_COL" +
                id + "_POSITION=null,LPIT_COL" + id +
                "_MAPPING=null WHERE LPIT_BNK_CODE=?";
        try {
            conn = new DBConnector().getDatabaseConnection();
            CallableStatement cst1 = null;
            cst1 = conn.prepareCall(query);
            cst1.setObject(1, session.getAttribute("bankCode"));
            cst1.execute();
            cst1.close();
            conn.close();

            while (compCount < dynamicContainer.getChildCount()) {
                UIComponent mine =
                    dynamicContainer.getChildren().get(compCount);
                String ComponentID = mine.getId();
                if (ComponentID == null) {

                } else {
                    //LOOP THRU THE COMPONENTS SAVING THERE DATA.
                    if (ComponentID.equals("DynText_" + id)) {
                        //Text Box.
                        Textvalue = (RichInputText)mine;
                        if (Textvalue.getValue() == null) {

                        } else {
                            //GET ID TO DETERMINE COLUMN NAME.
                        }

                    } else if (ComponentID.equals("DynSpin_" + id)) {
                        //Spin Box.
                        Spinvalue = (RichInputNumberSpinbox)mine;
                        if (Spinvalue.getValue() == null) {

                        } else {

                        }

                    } else if (ComponentID.equals("DynChoice_" + id)) {
                        //Select One Choice.
                        Choicevalue = (RichSelectOneChoice)mine;
                        if (Choicevalue.getValue() == null) {

                        } else {

                        }

                    }
                }
                compCount++;
            }
            Textvalue.setValue(null);
            Spinvalue.setValue(null);
            Choicevalue.setValue(null);
            GlobalCC.refreshUI(Textvalue);
            GlobalCC.refreshUI(Spinvalue);
            GlobalCC.refreshUI(Choicevalue);


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

    }
}
