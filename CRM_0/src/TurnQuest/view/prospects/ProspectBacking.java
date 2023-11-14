package TurnQuest.view.prospects;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.navigation.Links;

import java.math.BigDecimal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class ProspectBacking {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private RichTable tblProspects;
    private RichInputText txtPrsCode;
    private RichInputText txtPrsFullNames;
    private RichInputText txtPrsPhysicalAddress;
    private RichInputText txtPrsPostalAddress;
    private RichCommandButton btnSaveUpdateProspect;
    private RichInputText txtPrsSurname;
    private RichInputText txtPrsOtherNames;
    private RichInputText txtPrsTelNo;
    private RichInputText txtPrsMobileNo;
    private RichInputText txtPrsEmail;
    private RichInputText txtPrsIdRegNo;
    private RichInputDate txtPrsDob;
    private RichInputText txtPrsPin;
    private RichInputText txtPrsCouCode;
    private RichInputText txtPrsCouName;
    private RichInputText txtPrsTwnCode;
    private RichInputText txtPrsTwnName;
    private RichInputNumberSpinbox txtPrsPostalCode;
    private RichTable tblCountriesPop;
    private RichPanelBox panelProspectsDetails;
    private RichTable tblTownsPop;
    private RichInputText txtPostalCode;
    private RichTable tblPostalBacking;
    private RichSelectOneChoice txtType;
    private RichInputText txtContactTel;
    private RichInputText txtContact;
    private RichPanelGroupLayout pnlDtls;
    private RichCommandButton btnBackProspect;

    public ProspectBacking() {
    }

    public void setTblProspects(RichTable tblProspects) {
        this.tblProspects = tblProspects;
    }

    public RichTable getTblProspects() {
        return tblProspects;
    }

    public void setTxtPrsCode(RichInputText txtPrsCode) {
        this.txtPrsCode = txtPrsCode;
    }

    public RichInputText getTxtPrsCode() {
        return txtPrsCode;
    }

    public void setTxtPrsFullNames(RichInputText txtPrsFullNames) {
        this.txtPrsFullNames = txtPrsFullNames;
    }

    public RichInputText getTxtPrsFullNames() {
        return txtPrsFullNames;
    }

    public void setTxtPrsPhysicalAddress(RichInputText txtPrsPhysicalAddress) {
        this.txtPrsPhysicalAddress = txtPrsPhysicalAddress;
    }

    public RichInputText getTxtPrsPhysicalAddress() {
        return txtPrsPhysicalAddress;
    }

    public void setTxtPrsPostalAddress(RichInputText txtPrsPostalAddress) {
        this.txtPrsPostalAddress = txtPrsPostalAddress;
    }

    public RichInputText getTxtPrsPostalAddress() {
        return txtPrsPostalAddress;
    }

    public void setBtnSaveUpdateProspect(RichCommandButton btnSaveUpdateProspect) {
        this.btnSaveUpdateProspect = btnSaveUpdateProspect;
    }

    public RichCommandButton getBtnSaveUpdateProspect() {
        return btnSaveUpdateProspect;
    }

    public void setTxtPrsSurname(RichInputText txtPrsSurname) {
        this.txtPrsSurname = txtPrsSurname;
    }

    public RichInputText getTxtPrsSurname() {
        return txtPrsSurname;
    }

    public void setTxtPrsOtherNames(RichInputText txtPrsOtherNames) {
        this.txtPrsOtherNames = txtPrsOtherNames;
    }

    public RichInputText getTxtPrsOtherNames() {
        return txtPrsOtherNames;
    }

    public void setTxtPrsTelNo(RichInputText txtPrsTelNo) {
        this.txtPrsTelNo = txtPrsTelNo;
    }

    public RichInputText getTxtPrsTelNo() {
        return txtPrsTelNo;
    }

    public void setTxtPrsMobileNo(RichInputText txtPrsMobileNo) {
        this.txtPrsMobileNo = txtPrsMobileNo;
    }

    public RichInputText getTxtPrsMobileNo() {
        return txtPrsMobileNo;
    }

    public void setTxtPrsEmail(RichInputText txtPrsEmail) {
        this.txtPrsEmail = txtPrsEmail;
    }

    public RichInputText getTxtPrsEmail() {
        return txtPrsEmail;
    }

    public void setTxtPrsIdRegNo(RichInputText txtPrsIdRegNo) {
        this.txtPrsIdRegNo = txtPrsIdRegNo;
    }

    public RichInputText getTxtPrsIdRegNo() {
        return txtPrsIdRegNo;
    }

    public void setTxtPrsDob(RichInputDate txtPrsDob) {
        this.txtPrsDob = txtPrsDob;
    }

    public RichInputDate getTxtPrsDob() {
        return txtPrsDob;
    }

    public void setTxtPrsPin(RichInputText txtPrsPin) {
        this.txtPrsPin = txtPrsPin;
    }

    public RichInputText getTxtPrsPin() {
        return txtPrsPin;
    }

    public void setTxtPrsCouCode(RichInputText txtPrsCouCode) {
        this.txtPrsCouCode = txtPrsCouCode;
    }

    public RichInputText getTxtPrsCouCode() {
        return txtPrsCouCode;
    }

    public void setTxtPrsCouName(RichInputText txtPrsCouName) {
        this.txtPrsCouName = txtPrsCouName;
    }

    public RichInputText getTxtPrsCouName() {
        return txtPrsCouName;
    }

    public void setTxtPrsTwnCode(RichInputText txtPrsTwnCode) {
        this.txtPrsTwnCode = txtPrsTwnCode;
    }

    public RichInputText getTxtPrsTwnCode() {
        return txtPrsTwnCode;
    }

    public void setTxtPrsTwnName(RichInputText txtPrsTwnName) {
        this.txtPrsTwnName = txtPrsTwnName;
    }

    public RichInputText getTxtPrsTwnName() {
        return txtPrsTwnName;
    }

    public void setTxtPrsPostalCode(RichInputNumberSpinbox txtPrsPostalCode) {
        this.txtPrsPostalCode = txtPrsPostalCode;
    }

    public RichInputNumberSpinbox getTxtPrsPostalCode() {
        return txtPrsPostalCode;
    }

    public void setPanelProspectsDetails(RichPanelBox panelProspectsDetails) {
        this.panelProspectsDetails = panelProspectsDetails;
    }

    public RichPanelBox getPanelProspectsDetails() {
        return panelProspectsDetails;
    }

    public void setTblTownsPop(RichTable tblTownsPop) {
        this.tblTownsPop = tblTownsPop;
    }

    public RichTable getTblTownsPop() {
        return tblTownsPop;
    }

    public void setTblCountriesPop(RichTable tblCountriesPop) {
        this.tblCountriesPop = tblCountriesPop;
    }

    public RichTable getTblCountriesPop() {
        return tblCountriesPop;
    }

    public void refreshProspectFields() {
        txtPrsCode.setValue(null);
        txtPrsSurname.setValue(null);
        txtPrsPhysicalAddress.setValue(null);
        txtPrsPostalAddress.setValue(null);
        txtPrsOtherNames.setValue(null);
        txtPrsTelNo.setValue(null);
        txtPrsMobileNo.setValue(null);
        txtPrsEmail.setValue(null);
        txtPrsIdRegNo.setValue(null);
        txtPrsDob.setValue(null);
        txtPrsPin.setValue(null);
        txtPrsCouCode.setValue(null);
        txtPrsCouName.setValue(null);
        txtPrsTwnCode.setValue(null);
        txtPrsTwnName.setValue(null);
        txtPostalCode.setValue(null);
        txtPrsSurname.setLabel("Surname : ");
        txtContact.setVisible(false);
        txtContactTel.setVisible(false);
        btnSaveUpdateProspect.setText("Save");
    }

    public String actionNewProspect() {
        refreshProspectFields();
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:prospectPop" + "').show(hints);");
        return null;
    }

    public String actionEditProspect() {
        Object key2 = tblProspects.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtPrsCode.setValue(nodeBinding.getAttribute("prsCode"));
            txtPrsSurname.setValue(nodeBinding.getAttribute("prsSurname"));
            txtPrsPhysicalAddress.setValue(nodeBinding.getAttribute("prsPhysicalAddress"));
            txtPrsPostalAddress.setValue(nodeBinding.getAttribute("prsPostalAddress"));
            txtPrsOtherNames.setValue(nodeBinding.getAttribute("prsOtherNames"));
            txtPrsTelNo.setValue(nodeBinding.getAttribute("prsTelNo"));
            txtPrsMobileNo.setValue(nodeBinding.getAttribute("prsMobileNo"));
            txtPrsEmail.setValue(nodeBinding.getAttribute("prsEmailAddress"));
            txtPrsIdRegNo.setValue(nodeBinding.getAttribute("prsIdRegNo"));
            txtPrsDob.setValue(nodeBinding.getAttribute("prsDob"));
            txtPrsPin.setValue(nodeBinding.getAttribute("prsPin"));
            txtPrsCouCode.setValue(nodeBinding.getAttribute("prsCountryCode"));
            txtPrsCouName.setValue(nodeBinding.getAttribute("countryName"));
            txtPrsTwnCode.setValue(nodeBinding.getAttribute("prsTownCode"));
            txtPrsTwnName.setValue(nodeBinding.getAttribute("townName"));
            txtPostalCode.setValue(nodeBinding.getAttribute("prsPostalCode"));
            String type = nodeBinding.getAttribute("type").toString();
            if (type.equalsIgnoreCase("I")) {
                txtPrsSurname.setLabel("Surname : ");
                txtContact.setVisible(false);
                txtContactTel.setVisible(false);
            } else {
                txtPrsSurname.setLabel("Company Name : ");
                txtContact.setVisible(true);
                txtContactTel.setVisible(true);
                txtContact.setValue(nodeBinding.getAttribute("prsContact"));
                txtContactTel.setValue(nodeBinding.getAttribute("prsContactTel"));
            }

            btnSaveUpdateProspect.setText("Edit");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:prospectPop" + "').show(hints);");
        } else {
            GlobalCC.errorValueNotEntered("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String actionDeleteProspect() {
        Object key2 = tblProspects.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String code = nodeBinding.getAttribute("prsCode").toString();
            String surname = nodeBinding.getAttribute("prsSurname").toString();
            String physicalAddress = null;
            String postalAddress = null;

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = dbConnector.getDatabaseConnection();
                String query = "begin TQC_SETUPS_PKG.prospects_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_PROSPECTS_TAB",
                                                     conn);
                ArrayList prospectList = new ArrayList();
                Prospect prospect = new Prospect();
                prospect.setSQLTypeName("TQC_PROSPECTS_OBJ");

                prospect.setPrsCode(code == null ? null :
                                    new BigDecimal(code));
                prospect.setPrsSurname(surname);
                prospect.setPrsPhysicalAddress(physicalAddress);
                prospect.setPrsPostalAddress(postalAddress);

                prospectList.add(prospect);
                ARRAY array =
                    new ARRAY(descriptor, conn, prospectList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchAllProspectsIterator").executeQuery();
                GlobalCC.refreshUI(tblProspects);

                refreshProspectFields();

                String message = "Record DELETED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.errorValueNotEntered("You need to select a Record first.");
            return null;
        }
        return null;
    }
  public String actionBackProspect() {
      try{
          String back_url=GlobalCC.checkNullValues(session.getAttribute("back_url"));
          if( back_url!=null ) 
          {
              System.out.println("back_url: "+back_url);
              Links.navigate(back_url);
          }
      }catch(Exception e) {
          GlobalCC.EXCEPTIONREPORTING(e);
      }
      return null;
  }
    public String actionSaveUpdateProspect() {
        if (btnSaveUpdateProspect.getText().equals("Edit")) {
            actionUpdateProspect();
        } else {
            String code = GlobalCC.checkNullValues(txtPrsCode.getValue());
            String surname =
                GlobalCC.checkNullValues(txtPrsSurname.getValue());
            String physicalAddress =
                GlobalCC.checkNullValues(txtPrsPhysicalAddress.getValue());
            String postalAddress =
                GlobalCC.checkNullValues(txtPrsPostalAddress.getValue());
            String otherNames =
                GlobalCC.checkNullValues(txtPrsOtherNames.getValue());
            String telNo = GlobalCC.checkNullValues(txtPrsTelNo.getValue());
            String mobileNo =
                GlobalCC.checkNullValues(txtPrsMobileNo.getValue());
            String email = GlobalCC.checkNullValues(txtPrsEmail.getValue());
            String idRegNo =
                GlobalCC.checkNullValues(txtPrsIdRegNo.getValue());
            String dob = GlobalCC.checkNullValues(txtPrsDob.getValue());
            String pin = GlobalCC.checkNullValues(txtPrsPin.getValue());
            String country =
                GlobalCC.checkNullValues(txtPrsCouCode.getValue());
            String town = GlobalCC.checkNullValues(txtPrsTwnCode.getValue());
            String type = GlobalCC.checkNullValues(txtType.getValue());
            String contact = GlobalCC.checkNullValues(txtContact.getValue());
            String contactTel =
                GlobalCC.checkNullValues(txtContactTel.getValue());
            if (surname == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Surname is Empty");
                return null;

            } else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;
                try {
                    conn = dbConnector.getDatabaseConnection();
                    String query =
                        "begin TQC_SETUPS_PKG.prospects_prc(?,?); end;";

                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_PROSPECTS_TAB",
                                                         conn);
                    ArrayList prospectList = new ArrayList();
                    Prospect prospect = new Prospect();
                    prospect.setSQLTypeName("TQC_PROSPECTS_OBJ");

                    // Take care of all the date fields on the form.
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                    Date tmpDobDate = null;
                    if (txtPrsDob.getValue() != null &&
                        !(txtPrsDob.getValue().equals(""))) {
                        String date1 = df.format(txtPrsDob.getValue());
                        tmpDobDate = df.parse(date1);
                    }

                    prospect.setPrsCode(code == null ? null :
                                        new BigDecimal(code));
                    prospect.setPrsSurname(surname);
                    prospect.setPrsPhysicalAddress(physicalAddress);
                    prospect.setPrsPostalAddress(postalAddress);
                    prospect.setPrsOtherNames(otherNames);
                    prospect.setPrsTelNo(telNo);
                    prospect.setPrsMobileNo(mobileNo);
                    prospect.setPrsEmailAddress(email);
                    prospect.setPrsIdRegNo(idRegNo);
                    prospect.setPrsDob(tmpDobDate == null ? null :
                                       new java.sql.Date(tmpDobDate.getTime()));
                    prospect.setPrsPin(pin);
                    prospect.setPrsCountryCode(country == null ? null :
                                               new BigDecimal(country));
                    prospect.setPrsTownCode(town == null ? null :
                                            new BigDecimal(town));
                    prospect.setPrsPostalCode(session.getAttribute("pstCode") ==
                                              null ? null :
                                              new BigDecimal(session.getAttribute("pstCode").toString()));
                    prospect.setType(type);
                    prospect.setPrsContact(contact);
                    prospect.setPrsContactTel(contactTel);
                    prospectList.add(prospect);
                    ARRAY array =
                        new ARRAY(descriptor, conn, prospectList.toArray());

                    statement =
                            (OracleCallableStatement)conn.prepareCall(query);
                    statement.setString(1, "A");
                    statement.setArray(2, array);
                    statement.execute();

                    statement.close();
                    conn.commit();
                    conn.close();

                    ADFUtils.findIterator("fetchAllProspectsIterator").executeQuery();
                    GlobalCC.refreshUI(tblProspects);

                    refreshProspectFields();
                    GlobalCC.dismissPopUp("crm", "prospectPop");
                    String message = "New Record ADDED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionUpdateProspect() {
        String code = GlobalCC.checkNullValues(txtPrsCode.getValue());
        String surname = GlobalCC.checkNullValues(txtPrsSurname.getValue());
        String physicalAddress =
            GlobalCC.checkNullValues(txtPrsPhysicalAddress.getValue());
        String postalAddress =
            GlobalCC.checkNullValues(txtPrsPostalAddress.getValue());
        String otherNames =
            GlobalCC.checkNullValues(txtPrsOtherNames.getValue());
        String telNo = GlobalCC.checkNullValues(txtPrsTelNo.getValue());
        String mobileNo = GlobalCC.checkNullValues(txtPrsMobileNo.getValue());
        String email = GlobalCC.checkNullValues(txtPrsEmail.getValue());
        String idRegNo = GlobalCC.checkNullValues(txtPrsIdRegNo.getValue());
        String dob = GlobalCC.checkNullValues(txtPrsDob.getValue());
        String pin = GlobalCC.checkNullValues(txtPrsPin.getValue());
        String country = GlobalCC.checkNullValues(txtPrsCouCode.getValue());
        String town = GlobalCC.checkNullValues(txtPrsTwnCode.getValue());
        String postalCode = GlobalCC.checkNullValues(txtPostalCode.getValue());
        String type = GlobalCC.checkNullValues(txtType.getValue());
        String contact = GlobalCC.checkNullValues(txtContact.getValue());
        String contactTel = GlobalCC.checkNullValues(txtContactTel.getValue());

        if (surname == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Surname is Empty");
            return null;

        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = dbConnector.getDatabaseConnection();
                String query = "begin TQC_SETUPS_PKG.prospects_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_PROSPECTS_TAB",
                                                     conn);
                ArrayList prospectList = new ArrayList();
                Prospect prospect = new Prospect();
                prospect.setSQLTypeName("TQC_PROSPECTS_OBJ");

                // Take care of all the date fields on the form.
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                Date tmpDobDate = null;
                if (txtPrsDob.getValue() != null &&
                    !(txtPrsDob.getValue().equals(""))) {
                    String date1 = df.format(txtPrsDob.getValue());
                    tmpDobDate = df.parse(date1);
                }

                prospect.setPrsCode(code == null ? null :
                                    new BigDecimal(code));
                prospect.setPrsSurname(surname);
                prospect.setPrsPhysicalAddress(physicalAddress);
                prospect.setPrsPostalAddress(postalAddress);
                prospect.setPrsOtherNames(otherNames);
                prospect.setPrsTelNo(telNo);
                prospect.setPrsMobileNo(mobileNo);
                prospect.setPrsEmailAddress(email);
                prospect.setPrsIdRegNo(idRegNo);
                prospect.setPrsDob(tmpDobDate == null ? null :
                                   new java.sql.Date(tmpDobDate.getTime()));
                prospect.setPrsPin(pin);
                prospect.setPrsCountryCode(country == null ? null :
                                           new BigDecimal(country));
                prospect.setPrsTownCode(town == null ? null :
                                        new BigDecimal(town));
                prospect.setPrsPostalCode(postalCode == null ? null :
                                          new BigDecimal(postalCode));
                prospect.setType(type);
                prospect.setPrsContact(contact);
                prospect.setPrsContactTel(contactTel);

                prospectList.add(prospect);
                ARRAY array =
                    new ARRAY(descriptor, conn, prospectList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "E");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchAllProspectsIterator").executeQuery();
                GlobalCC.refreshUI(tblProspects);

                refreshProspectFields();
                GlobalCC.dismissPopUp("crm", "prospectPop");
                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public String actionShowCountriesPop() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:countriesPop" + "').show(hints);");
        return null;
    }

    public String actionAcceptCountry() {
        Object key2 = tblCountriesPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtPrsCouCode.setValue(nodeBinding.getAttribute("code"));
            txtPrsCouName.setValue(nodeBinding.getAttribute("name"));

            session.setAttribute("couCode", nodeBinding.getAttribute("code"));
        }

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:countriesPop" + "').hide(hints);");

        GlobalCC.refreshUI(panelProspectsDetails);
        return null;
    }

    public String actionShowTownsPop() {
        if (session.getAttribute("couCode") != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:townsPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select Country to proceed.");
            return null;
        }
        return null;
    }

    public String actionAcceptTown() {
        Object key2 = tblTownsPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtPrsTwnCode.setValue(nodeBinding.getAttribute("code"));
            session.setAttribute("townCode", nodeBinding.getAttribute("code"));
            txtPrsTwnName.setValue(nodeBinding.getAttribute("name"));
        }

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:townsPop" +
                             "').hide(hints);");

        GlobalCC.refreshUI(panelProspectsDetails);
        return null;
    }

    public String actionCancelCountry() {
        GlobalCC.dismissPopUp("crm", "countriesPop");
        return null;
    }

    public String actionCancelTown() {
        GlobalCC.dismissPopUp("crm", "townsPop");

        return null;
    }

    public void setTxtPostalCode(RichInputText txtPostalCode) {
        this.txtPostalCode = txtPostalCode;
    }

    public RichInputText getTxtPostalCode() {
        return txtPostalCode;
    }

    public String showPostal() {
        if (txtPrsTwnName.getValue() != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:postalPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select Town to proceed.");
            return null;
        }
        return null;
    }

    public String actionAcceptCode() {
        Object key2 = tblPostalBacking.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Postal Code Selected");
            return null;
        }
        txtPostalCode.setValue(r.getAttribute("pstDesc").toString());
        session.setAttribute("pstCode", r.getAttribute("pstCode"));
        GlobalCC.refreshUI(txtPostalCode);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:postalPop" +
                             "').hide(hints);");
        return null;
    }

    public void setTblPostalBacking(RichTable tblPostalBacking) {
        this.tblPostalBacking = tblPostalBacking;
    }

    public RichTable getTblPostalBacking() {
        return tblPostalBacking;
    }

    public void setTxtType(RichSelectOneChoice txtType) {
        this.txtType = txtType;
    }

    public RichSelectOneChoice getTxtType() {
        return txtType;
    }

    public void typeChange(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue() &&
            valueChangeEvent.getNewValue() != null) {
            String type = valueChangeEvent.getNewValue().toString();
            if (type.equalsIgnoreCase("I")) {
                txtPrsSurname.setLabel("Surname : ");
                txtContact.setVisible(false);
                txtContactTel.setVisible(false);
                txtPrsDob.setLabel("D.O.B");
                GlobalCC.refreshUI(txtPrsDob);
            } else {
                txtPrsSurname.setLabel("Company Name : ");
                txtContact.setVisible(true);
                txtContactTel.setVisible(true);
                txtPrsDob.setLabel("Y.O.R");
                GlobalCC.refreshUI(txtPrsDob);
            }
            GlobalCC.refreshUI(pnlDtls);
        }
    }

    public void setTxtContactTel(RichInputText txtContactTel) {
        this.txtContactTel = txtContactTel;
    }

    public RichInputText getTxtContactTel() {
        return txtContactTel;
    }

    public void setTxtContact(RichInputText txtContact) {
        this.txtContact = txtContact;
    }

    public RichInputText getTxtContact() {
        return txtContact;
    }

    public void setPnlDtls(RichPanelGroupLayout pnlDtls) {
        this.pnlDtls = pnlDtls;
    }

    public RichPanelGroupLayout getPnlDtls() {
        return pnlDtls;
    }

  public void setBtnBackProspect(RichCommandButton btnBackProspect)
  {
    this.btnBackProspect = btnBackProspect;
  }

  public RichCommandButton getBtnBackProspect()
  {
    return btnBackProspect;
  }
}
