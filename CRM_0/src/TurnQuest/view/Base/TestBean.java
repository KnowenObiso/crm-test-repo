package TurnQuest.view.Base;


import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import oracle.adf.view.rich.component.rich.nav.RichCommandButton;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;


public class TestBean {
    private RichCommandButton btnCreateUpdateAlertType;

    public TestBean() {
        String query = "begin ? := tqc_roles_cursor.Get_Sys_Roles(?); end;";
        CallableStatement cst = null;
        DBConnector datahandler = new DBConnector();
        Connection conn = null;
        try {

            conn = datahandler.getDatabaseConnection();

            cst = conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setInt(2, 27);
            cst.execute();
            ResultSet rs = (ResultSet)cst.getObject(1);

            while (rs.next()) {
                SelectItem selectItem = new SelectItem();
                selectItem.setValue(rs.getBigDecimal(2));
                selectItem.setDescription(rs.getString(3));
                selectItem.setLabel(rs.getString(3));
                selectValues.add(selectItem);

            }

            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING((OracleConnection)conn, e);
        }
        /*

        selectValues.add(new SelectItem("Value1", "Display Text For Value1" ));
        selectValues.add(new SelectItem("Value2", "Display Text For Value2"));
        selectValues.add(new SelectItem("Value3", "Display Text For Value3"));

        displayValue.add("Value");
        displayValue.add("Value1");*/
    }

    private List<SelectItem> selectValues = new ArrayList<SelectItem>();
    private List<String> displayValue = new ArrayList<String>();

    public void setDisplayValue(List<String> displayValue) {
        this.displayValue = displayValue;
    }

    public List<String> getDisplayValue() {
        return displayValue;
    }

    public void setSelectValues(List<SelectItem> selectValues) {
        this.selectValues = selectValues;
    }

    public List<SelectItem> getSelectValues() {
        return selectValues;
    }

    public void userRoles(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            int k = 0;

            List<BigDecimal> newDispVals = new ArrayList<BigDecimal>();
            newDispVals = (List<BigDecimal>)valueChangeEvent.getNewValue();
            int z = 0;
            while (z < newDispVals.size()) {
                if (displayValue.contains(newDispVals.get(z))) {

                } else {
                    displayValue.add(newDispVals.get(z).toString());
                }
                z++;
            }

            while (k < displayValue.size()) {

                k++;
            }
            int t = 0;
            while (t < selectValues.size()) {
                t++;
            }


        }
    }

    public void setBtnCreateUpdateAlertType(RichCommandButton btnCreateUpdateAlertType) {
        this.btnCreateUpdateAlertType = btnCreateUpdateAlertType;
    }

    public RichCommandButton getBtnCreateUpdateAlertType() {
        return btnCreateUpdateAlertType;
    }
}
