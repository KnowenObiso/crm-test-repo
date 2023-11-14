package TurnQuest.view.backing;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.Product1;

import java.math.BigDecimal;

import java.sql.SQLException;

import java.util.ArrayList;

import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class ProductBean {
    private RichCommandButton btnNewProduct;
    private RichCommandButton actionEditProduct;
    private RichTable tblSytems;
    private RichInputText txtTpaCode;
    private RichInputText txtTpaSysCode;
    private RichInputText txtTpaSysName;
    private RichInputText txtTpaProductCode;
    private RichInputText txtTpaProductName;
    private RichInputText txtTpaShtDesc;
    private RichInputText txtTpaDescription;
    private RichInputText txtTpaNarration;
    private RichCommandButton btnCreateUpdateProduct;
    private RichTable tblProSystems;
    private RichTable tblProducts;
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private RichTable tblSystemProduct;
    private RichTable tblProductClientAttributes;
    private RichInputNumberSpinbox noMinValue;
    private RichInputNumberSpinbox noMaxValue;
    private RichInputText txtFixedValue;
    private RichInputText txtProdClientCode;
    private RichInputText txtClientAttributeCode;
    private RichTable tblClientAttributes;
    private RichInputText txtClientAttrName;
    private RichOutputLabel lblMaxValue;
    private RichOutputLabel lblMinValue;
    private RichOutputLabel lblPrompt;
    private HtmlPanelGroup pnGroupValues;
    private HtmlPanelGrid gridClientType;
    private RichInputText txtAttrValue;
    private HtmlPanelGrid gridPrompt;
    private HtmlPanelGrid gridValue;
    private HtmlPanelGrid gridSex;
    private RichPanelGroupLayout pgGroupClientAttributes;
    private HtmlPanelGrid gridSector;
    private RichTable tblSectors;
    private RichInputText txtValue;
    private RichInputText txtSector;
    private HtmlPanelGrid gridMinValLabel;
    private HtmlPanelGrid gridMinValIn;
    private HtmlPanelGrid gridMaxLabel;
    private HtmlPanelGrid gridMaxValIn;
    private RichSelectOneChoice choiceSex;
    private RichSelectOneChoice choiceCtype;
    private RichInputDate dtMinValue;
    private RichInputDate dtMaxValue;
    private HtmlPanelGrid gridMinDate;
    private HtmlPanelGrid gridMaxDate;
    private HtmlPanelGrid grdValueLabel;


    public ProductBean() {
    }


    public String actionEditProduct() {
        resetProducts();
        btnCreateUpdateProduct.setText("Update");
        Object key2 = tblProducts.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtTpaCode.setValue(nodeBinding.getAttribute("TPA_CODE"));
            txtTpaSysCode.setValue(nodeBinding.getAttribute("TPA_SYSTEM"));
            txtTpaSysName.setValue(nodeBinding.getAttribute("SYSNAME"));
            txtTpaProductCode.setValue(nodeBinding.getAttribute("TPA_PROD_CODE"));
            txtTpaProductName.setValue(nodeBinding.getAttribute("TPA_PROD_SHTDESC"));
            txtTpaShtDesc.setValue(nodeBinding.getAttribute("TPA_PROD_SHTDESC"));
            txtTpaDescription.setValue(nodeBinding.getAttribute("TPA_PROD_DESC"));
            txtTpaNarration.setValue(nodeBinding.getAttribute("TPA_PROD_NARRATION"));
            session.setAttribute("sysCode",
                                 nodeBinding.getAttribute("TPA_SYSTEM"));
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:productPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record selected:");
        }
        return null;
    }

    public String actionAcceptProduct() {
        Object key2 = tblSystemProduct.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {


            txtTpaProductCode.setValue(nodeBinding.getAttribute("TPA_PROD_CODE"));
            txtTpaProductName.setValue(nodeBinding.getAttribute("TPA_PROD_DESC"));
            txtTpaShtDesc.setValue(nodeBinding.getAttribute("TPA_PROD_SHTDESC"));
            txtTpaDescription.setValue(nodeBinding.getAttribute("TPA_PROD_DESC"));

            ADFUtils.findIterator("findSystemProductsIterator").executeQuery();

            GlobalCC.refreshUI(txtTpaProductCode);
            GlobalCC.refreshUI(txtTpaProductName);
            GlobalCC.refreshUI(txtTpaShtDesc);
            GlobalCC.refreshUI(txtTpaDescription);


        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:showProductsPop" + "').hide(hints);");
        return null;


    }

    public String actionAcceptSystems() {
        Object key2 = tblSytems.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("sysCode", nodeBinding.getAttribute("code"));
            txtTpaSysCode.setValue(nodeBinding.getAttribute("code"));
            txtTpaSysName.setValue(nodeBinding.getAttribute("name"));
            ADFUtils.findIterator("findSystemProductsIterator").executeQuery();

            GlobalCC.refreshUI(txtTpaSysCode);
            GlobalCC.refreshUI(txtTpaSysName);


        } else {
            GlobalCC.INFORMATIONREPORTING("First select System ::");
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:systemsPop" + "').hide(hints);");
        return null;


    }

    public String actionShowSystems() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:systemsPop" + "').show(hints);");
        return null;
    }

    public String actionNewProducts() {


        resetProducts();
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:productPop" + "').show(hints);");
        return null;
    }

    public String actionShowDeleteProducts() {
        Object key2 = tblProducts.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:confirmDeleteProduct" +
                                 "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record selected");
        }
        return null;
    }

    public void actionConfirmedDeleteProduct(DialogEvent dialogEvent) {
        OracleConnection conn = null;
        if (dialogEvent.getOutcome().equals((DialogEvent.Outcome.no))) {

        } else if (dialogEvent.getOutcome().equals((DialogEvent.Outcome.yes))) {


            Object key2 = tblProducts.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
            resetProducts();
            if (nodeBinding != null) {
                String code = null;
                code =
GlobalCC.checkNullValues(nodeBinding.getAttribute("TPA_CODE"));
                if (code != null && code.trim().length() > 0) {
                    actionDeleteProducts(code);
                }
            } else {
                GlobalCC.INFORMATIONREPORTING("No Record Selected::");
            }
        }
    }


    public String actionCreateUpdateProduct() {
        String code = GlobalCC.checkNullValues(txtTpaCode.getValue());
        String sysCode = GlobalCC.checkNullValues(txtTpaSysCode.getValue());
        String prodCode =
            GlobalCC.checkNullValues(txtTpaProductCode.getValue());
        String prodShtDesc =
            GlobalCC.checkNullValues(txtTpaShtDesc.getValue());
        String proDesc =
            GlobalCC.checkNullValues(txtTpaDescription.getValue());
        String proNarration =
            GlobalCC.checkNullValues(txtTpaNarration.getValue());
        if (sysCode == null || sysCode.trim().length() < 1) {
            GlobalCC.errorValueNotEntered("SYSTEM REQUIRED");
            return null;
        }
        if (prodCode == null || prodCode.trim().length() < 1) {
            GlobalCC.errorValueNotEntered("PRODUCT REQUIRED");
            return null;
        }
        if (prodShtDesc == null || prodShtDesc.trim().length() < 1) {
            GlobalCC.errorValueNotEntered("SHORT DESC REQUIRED");
            return null;
        }
        if (proDesc == null || proDesc.trim().length() < 1) {
            GlobalCC.errorValueNotEntered("DESCRIPTION REQUIRED");
            return null;
        }
        if (proNarration == null || proNarration.trim().length() < 1) {
            GlobalCC.errorValueNotEntered("NARRATION  REQUIRED");
            return null;
        }


        OracleConnection conn = null;
        String Query =
            "BEGIN Tqc_Product_Pkg.productAttribute_prc(?,?,?);END;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        String option = null;

        if (btnCreateUpdateProduct.getText().equalsIgnoreCase("Update")) {
            option = "E";
            if (code == null) {
                GlobalCC.errorValueNotEntered("ERROR: CODE REQUIRED::");
            }

        } else {
            option = "A";
        }
        try {


            conn = connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            ArrayDescriptor descriptor =
                ArrayDescriptor.createDescriptor("TQC_PRODUCT_ATTRIBUTES_TAB",
                                                 conn);
            ArrayList templateList = new ArrayList();
            Product1 product = new Product1();
            product.setSQLTypeName("TQC_PRODUCT_ATTRIBUTES_OBJ");

            product.setTPA_CODE(code == null ? null : new BigDecimal(code));
            product.setTPA_SYSTEM(sysCode == null ? null :
                                  new BigDecimal(sysCode));
            product.setTPA_PROD_CODE(prodCode == null ? null :
                                     new BigDecimal(prodCode));
            product.setTPA_PROD_SHTDESC(prodShtDesc);
            product.setTPA_PROD_DESC(proDesc);
            product.setTPA_PROD_NARRATION(proNarration);


            templateList.add(product);
            ARRAY array = new ARRAY(descriptor, conn, templateList.toArray());

            // Add or Edit
            cst.setString(1, option);
            cst.setArray(2, array);
            cst.registerOutParameter(3, OracleTypes.VARCHAR);
            cst.execute();
            String err = cst.getString(3);
            if (err != null) {
                GlobalCC.INFORMATIONREPORTING(err);

            } else {
                if (option.equalsIgnoreCase("E")) {

                    ADFUtils.findIterator("findProductAttributeIterator").executeQuery();
                    GlobalCC.refreshUI(tblProducts);
                    GlobalCC.INFORMATIONREPORTING("Record Updated Successfully:");
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "crm:productPop" + "').hide(hints);");
                } else {
                    GlobalCC.INFORMATIONREPORTING("Record Created Successfully:");
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "crm:productPop" + "').hide(hints);");
                    ADFUtils.findIterator("findProductAttributeIterator").executeQuery();
                    GlobalCC.refreshUI(tblProducts);
                }

            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public String actionShowProducts() {
        String code = GlobalCC.checkNullValues(txtTpaSysCode.getValue());
        if (code != null) {

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:showProductsPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("First: select the System");
        }
        return null;
    }

    public String resetProducts() {
        txtTpaCode.setValue(null);
        txtTpaSysCode.setValue(null);
        txtTpaSysName.setValue(null);
        txtTpaProductCode.setValue(null);
        txtTpaProductName.setValue(null);
        txtTpaShtDesc.setValue(null);
        txtTpaDescription.setValue(null);
        txtTpaNarration.setValue(null);
        session.setAttribute("sysCode", null);
        btnCreateUpdateProduct.setText("Save");
        return null;
    }

    public String actionDeleteProducts(String code) {


        OracleConnection conn = null;
        String Query =
            "BEGIN Tqc_Product_Pkg.productAttribute_prc(?,?,?);END;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        String option = "D";
        try {


            conn = connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            ArrayDescriptor descriptor =
                ArrayDescriptor.createDescriptor("TQC_PRODUCT_ATTRIBUTES_TAB",
                                                 conn);
            ArrayList templateList = new ArrayList();
            Product1 product = new Product1();
            product.setSQLTypeName("TQC_PRODUCT_ATTRIBUTES_OBJ");
            product.setTPA_CODE(code == null ? null : new BigDecimal(code));
            product.setTPA_SYSTEM(null);
            product.setTPA_PROD_CODE(null);
            product.setTPA_PROD_SHTDESC(null);
            product.setTPA_PROD_DESC(null);
            product.setTPA_PROD_NARRATION(null);
            templateList.add(product);
            ARRAY array = new ARRAY(descriptor, conn, templateList.toArray());
            // Add or Edit
            cst.setString(1, option);
            cst.setArray(2, array);
            cst.registerOutParameter(3, OracleTypes.VARCHAR);
            cst.execute();
            String err = cst.getString(3);
            if (err != null) {
                GlobalCC.INFORMATIONREPORTING(err);

            } else {

                ADFUtils.findIterator("findProductAttributeIterator").executeQuery();
                GlobalCC.refreshUI(tblProducts);
                GlobalCC.INFORMATIONREPORTING("Record Deleted Successfully:");
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:productPop" + "').hide(hints);");


            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setTxtTpaCode(RichInputText txtTpaCode) {
        this.txtTpaCode = txtTpaCode;
    }

    public RichInputText getTxtTpaCode() {
        return txtTpaCode;
    }

    public void setTxtTpaSysCode(RichInputText txtTpaSysCode) {
        this.txtTpaSysCode = txtTpaSysCode;
    }

    public RichInputText getTxtTpaSysCode() {
        return txtTpaSysCode;
    }

    public void setTxtTpaSysName(RichInputText txtTpaSysName) {
        this.txtTpaSysName = txtTpaSysName;
    }

    public RichInputText getTxtTpaSysName() {
        return txtTpaSysName;
    }

    public void setTxtTpaProductCode(RichInputText txtTpaProductCode) {
        this.txtTpaProductCode = txtTpaProductCode;
    }

    public RichInputText getTxtTpaProductCode() {
        return txtTpaProductCode;
    }

    public void setTxtTpaProductName(RichInputText txtTpaProductName) {
        this.txtTpaProductName = txtTpaProductName;
    }

    public RichInputText getTxtTpaProductName() {
        return txtTpaProductName;
    }

    public void setTxtTpaShtDesc(RichInputText txtTpaShtDesc) {
        this.txtTpaShtDesc = txtTpaShtDesc;
    }

    public RichInputText getTxtTpaShtDesc() {
        return txtTpaShtDesc;
    }

    public void setTxtTpaDescription(RichInputText txtTpaDescription) {
        this.txtTpaDescription = txtTpaDescription;
    }

    public RichInputText getTxtTpaDescription() {
        return txtTpaDescription;
    }

    public void setTxtTpaNarration(RichInputText txtTpaNarration) {
        this.txtTpaNarration = txtTpaNarration;
    }

    public RichInputText getTxtTpaNarration() {
        return txtTpaNarration;
    }

    public void setBtnCreateUpdateProduct(RichCommandButton btnCreateUpdateProduct) {
        this.btnCreateUpdateProduct = btnCreateUpdateProduct;
    }

    public RichCommandButton getBtnCreateUpdateProduct() {
        return btnCreateUpdateProduct;
    }


    public void setTblProSystems(RichTable tblProSystems) {
        this.tblProSystems = tblProSystems;
    }

    public RichTable getTblProSystems() {
        return tblProSystems;
    }

    public void settblProducts(RichTable tblProducts) {
        this.tblProducts = tblProducts;
    }

    public RichTable gettblProducts() {
        return tblProducts;
    }

    public void setBtnNewProduct(RichCommandButton btnNewProduct) {
        this.btnNewProduct = btnNewProduct;
    }

    public RichCommandButton getBtnNewProduct() {
        return btnNewProduct;
    }


    public void setActionEditProduct(RichCommandButton actionEditProduct) {
        this.actionEditProduct = actionEditProduct;
    }

    public RichCommandButton getActionEditProduct() {
        return actionEditProduct;
    }

    public void setTblSytems(RichTable tblSytems) {
        this.tblSytems = tblSytems;
    }

    public RichTable getTblSytems() {
        return tblSytems;
    }

    public void setTblSystemProduct(RichTable tblSystemProduct) {
        this.tblSystemProduct = tblSystemProduct;
    }

    public RichTable getTblSystemProduct() {
        return tblSystemProduct;
    }

    public void setTblProductClientAttributes(RichTable tblProductClientAttributes) {
        this.tblProductClientAttributes = tblProductClientAttributes;
    }

    public RichTable getTblProductClientAttributes() {
        return tblProductClientAttributes;
    }

    public String actionNewProdClientAttr() {


        if (txtTpaCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Please Select a Product to  proceed"));
            return null;
        }


        resetProdClientFields();

        GlobalCC.showPopUp("crm", "newEditProdClientAttrPOP");
        return null;
    }

    public String actionEditProdClientattr() {
        if (txtTpaCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Please Select a Product to  proceed"));
            return null;
        }
        Object key = tblProductClientAttributes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;

        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Please Select a Product Client attribute to  proceed"));
            return null;
        }
        resetProdClientFields();
        txtProdClientCode.setValue(nodeBinding.getAttribute("prodClientAttrCode"));
        txtClientAttributeCode.setValue(nodeBinding.getAttribute("clientAttrCode"));
        noMinValue.setValue(nodeBinding.getAttribute("prodMinValue"));
        noMaxValue.setValue(nodeBinding.getAttribute("prodMaxValue"));
        Object FixedValue = nodeBinding.getAttribute("prodFixedValue");
        txtClientAttrName.setValue(nodeBinding.getAttribute("clientAttributeName"));
        Object colName = nodeBinding.getAttribute("colName");
        Object isRange = nodeBinding.getAttribute("attributeRange");

        lblPrompt.setValue(nodeBinding.getAttribute("prompt"));
        if (isRange.toString().equalsIgnoreCase("Y") &&
            !colName.toString().equals(" DOB")) {
            gridMinValLabel.setRendered(true);
            gridMinValIn.setRendered(true);
            gridMaxLabel.setRendered(true);
            gridMaxValIn.setRendered(true);

        } else if (isRange.toString().equalsIgnoreCase("Y") &&
                   colName.toString().equalsIgnoreCase("DOB")) {

            gridMinDate.setRendered(true);
            gridMaxDate.setRendered(true);
            gridMaxLabel.setRendered(true);
            gridMinValLabel.setRendered(true);
            dtMinValue.setValue(nodeBinding.getAttribute("prodMinValue"));
            dtMaxValue.setValue(nodeBinding.getAttribute("prodMaxValue"));
        } else {
            gridPrompt.setRendered(true);
            gridValue.setRendered(true);
        }

        if (colName.toString().equalsIgnoreCase("CLNT_GENDER")) {
            gridSex.setRendered(true);
            choiceSex.setValue(FixedValue);
            gridValue.setRendered(false);
            gridPrompt.setRendered(false);
        } else if (colName.toString().equalsIgnoreCase("CLNT_TYPE")) {
            gridClientType.setRendered(true);
            gridValue.setRendered(false);
            gridPrompt.setRendered(false);
            choiceCtype.setValue(FixedValue);
        } else if (colName.toString().equalsIgnoreCase("CLNT_BUSINESS")) {
            gridSector.setRendered(true);
            txtSector.setValue(FixedValue);
            gridValue.setRendered(false);
            gridPrompt.setRendered(false);
        } else {
            txtAttrValue.setValue(FixedValue);

        }

        if (isRange.toString().equalsIgnoreCase("Y") &&
            !colName.toString().equals(" DOB")) {
            gridMinValLabel.setRendered(true);
            gridMinValIn.setRendered(true);
            gridMaxLabel.setRendered(true);
            gridMaxValIn.setRendered(true);

        } else if (isRange.toString().equalsIgnoreCase("Y") &&
                   colName.toString().equalsIgnoreCase("DOB")) {

            gridMinDate.setRendered(true);
            gridMaxDate.setRendered(true);
            gridMaxLabel.setRendered(true);
            gridMinValLabel.setRendered(true);
            dtMinValue.setValue(nodeBinding.getAttribute("prodMinValue"));
            dtMaxValue.setValue(nodeBinding.getAttribute("prodMaxValue"));
        } else {
            gridPrompt.setRendered(true);
            gridValue.setRendered(true);
        }


        GlobalCC.showPopUp("crm", "newEditProdClientAttrPOP");
        return null;
    }

    public String actionDeleteProdClientAttr() {
        if (txtTpaCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Please Select a Product to  proceed"));
            return null;
        }
        Object key = tblProductClientAttributes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;


        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Please Select a Product Client attribute to  proceed"));
            return null;
        }
        Object prodClientAttrCode =
            nodeBinding.getAttribute("prodClientAttrCode");
        OracleConnection conn = null;
        String Query =
            "BEGIN Tqc_Product_Pkg.productClient_prc(?,?,?,?,?,?,?);END;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;

        try {


            conn = connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);


            cst.setString(1, "D");
            cst.setObject(2, prodClientAttrCode);
            cst.setString(3, null);
            cst.setString(4, null);
            cst.setString(5, null);
            cst.setString(6, null);
            cst.setString(7, null);
            cst.execute();
            conn.commit();
            conn.close();
            session.setAttribute("PRODUCTCODE", txtTpaCode.getValue());
            GlobalCC.INFORMATIONREPORTING("Record Deleted successfully");
            ADFUtils.findIterator("findProductClientAttributeIterator").executeQuery();
            GlobalCC.refreshUI(tblProductClientAttributes);

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);


            return null;
        }
        return null;
    }

    public String actionShowClientAttributes() {

        gridClientType.setRendered(false);
        gridPrompt.setRendered(false);
        gridValue.setRendered(false);
        gridSex.setRendered(false);
        gridSector.setRendered(false);
        gridMinValLabel.setRendered(false);
        gridMinValIn.setRendered(false);
        gridMaxLabel.setRendered(false);
        gridMaxValIn.setRendered(false);
        gridMinDate.setRendered(false);
        gridMaxDate.setRendered(false);
        ADFUtils.findIterator("findUndefinedClientAttributesIterator").executeQuery();

        GlobalCC.showPopUp("crm", "ClientsAttributesPOP");
        return null;
    }

    public void setNoMinValue(RichInputNumberSpinbox noMinValue) {
        this.noMinValue = noMinValue;
    }

    public RichInputNumberSpinbox getNoMinValue() {
        return noMinValue;

    }

    public void setNoMaxValue(RichInputNumberSpinbox noMaxValue) {
        this.noMaxValue = noMaxValue;
    }

    public RichInputNumberSpinbox getNoMaxValue() {
        return noMaxValue;
    }


    public void setTxtFixedValue(RichInputText txtFixedValue) {
        this.txtFixedValue = txtFixedValue;
    }

    public RichInputText getTxtFixedValue() {
        return txtFixedValue;
    }

    public void setTxtProdClientCode(RichInputText txtProdClientCode) {
        this.txtProdClientCode = txtProdClientCode;
    }

    public RichInputText getTxtProdClientCode() {
        return txtProdClientCode;
    }

    public void setTxtClientAttributeCode(RichInputText txtClientAttributeCode) {
        this.txtClientAttributeCode = txtClientAttributeCode;
    }

    public RichInputText getTxtClientAttributeCode() {
        return txtClientAttributeCode;
    }

    public void actiontblProductsSelected(SelectionEvent selectionEvent) {
        Object key = tblProducts.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        txtTpaCode.setValue(nodeBinding.getAttribute("TPA_CODE"));
        session.setAttribute("PRODUCTCODE", txtTpaCode.getValue());
        ADFUtils.findIterator("findProductClientAttributeIterator").executeQuery();
        GlobalCC.refreshUI(tblProductClientAttributes);
    }

    public String actionAcceptProductAttribute() {

        Object colName = null;
        Object isRange = null;
        Object key = tblClientAttributes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            colName = nodeBinding.getAttribute("colName");
            if (colName == null) {
                GlobalCC.EXCEPTIONREPORTING("Attribute Matching Column Name not found");
                return null;
            }
            isRange = nodeBinding.getAttribute("TCA_ATT_RANGE");
            System.out.println(nodeBinding.getAttribute("TCA_CODE"));
            txtClientAttributeCode.setValue(nodeBinding.getAttribute("TCA_CODE"));
            txtClientAttrName.setValue(nodeBinding.getAttribute("TCA_ATTRIBUTE_NAME"));
            lblPrompt.setValue(nodeBinding.getAttribute("TCA_PROMPT"));
            if (colName.toString().equalsIgnoreCase("CLNT_GENDER")) {
                session.setAttribute("INPUTTYPE", "GENDER");
                gridSex.setRendered(true);
            } else if (colName.toString().equalsIgnoreCase("CLNT_TYPE")) {
                session.setAttribute("INPUTTYPE", "CLIENTTYPE");
                gridClientType.setRendered(true);
            } else if (colName.toString().equalsIgnoreCase("CLNT_BUSINESS")) {
                session.setAttribute("INPUTTYPE", "CLIENTBUSINESS");
                gridSector.setRendered(true);
            }

            else {
                session.setAttribute("INPUTTYPE", "UNSPECIFIED");
                gridValue.setRendered(true);
                gridPrompt.setRendered(true);
            }


            if (isRange.toString().equalsIgnoreCase("Y") &&
                !colName.toString().equalsIgnoreCase("CLNT_DOB")) {
                gridMinValLabel.setRendered(true);
                gridMinValIn.setRendered(true);
                gridMaxLabel.setRendered(true);
                gridMaxValIn.setRendered(true);
                gridValue.setRendered(false);
                gridPrompt.setRendered(false);
            } else if (colName.toString().equalsIgnoreCase("CLNT_DOB") &&
                       isRange.toString().equalsIgnoreCase("Y")) {
                session.setAttribute("INPUTTYPE", "DOB");
                gridMinDate.setRendered(true);
                gridMaxDate.setRendered(true);
                gridMinValLabel.setRendered(true);
                gridMinValIn.setRendered(false);
                gridMaxLabel.setRendered(true);
                gridMaxValIn.setRendered(false);
                gridPrompt.setRendered(false);
                gridValue.setRendered(false);
            } else {
                gridMinValLabel.setRendered(false);
                gridMinValIn.setRendered(false);
                gridMaxLabel.setRendered(false);
                gridMaxValIn.setRendered(false);
                gridPrompt.setRendered(true);
                gridMinDate.setRendered(false);
                gridMaxDate.setRendered(false);
            }

            if (colName.toString().equalsIgnoreCase("CLNT_GENDER")) {
                session.setAttribute("INPUTTYPE", "GENDER");
                gridSex.setRendered(true);
            } else if (colName.toString().equalsIgnoreCase("CLNT_TYPE")) {
                session.setAttribute("INPUTTYPE", "CLIENTTYPE");
                gridClientType.setRendered(true);
            } else if (colName.toString().equalsIgnoreCase("CLNT_BUSINESS")) {
                session.setAttribute("INPUTTYPE", "CLIENTBUSINESS");
                gridSector.setRendered(true);
            }

            else {
                session.setAttribute("INPUTTYPE", "UNSPECIFIED");
                gridValue.setRendered(true);
                gridPrompt.setRendered(true);
            }

            GlobalCC.refreshUI(pgGroupClientAttributes);
        }
        GlobalCC.dismissPopUp("crm", "ClientsAttributesPOP");
        return null;
    }

    public void setTblClientAttributes(RichTable tblClientAttributes) {
        this.tblClientAttributes = tblClientAttributes;
    }

    public RichTable getTblClientAttributes() {
        return tblClientAttributes;
    }

    public void setTxtClientAttrName(RichInputText txtClientAttrName) {
        this.txtClientAttrName = txtClientAttrName;
    }

    public RichInputText getTxtClientAttrName() {
        return txtClientAttrName;
    }

    public String actionSaveProductClientAttr() {
        String clientAttributeCode =
            GlobalCC.checkNullValues(txtClientAttributeCode.getValue());
        System.out.println(clientAttributeCode);
        String prodClientCode =
            GlobalCC.checkNullValues(txtProdClientCode.getValue());
        Object MinValue = GlobalCC.checkNullValues(noMinValue.getValue());
        Object MaxValue = GlobalCC.checkNullValues(noMaxValue.getValue());
        String prodCode = GlobalCC.checkNullValues(txtTpaCode.getValue());
        String FixedValue = null;
        Object inputType = session.getAttribute("INPUTTYPE");
        if (clientAttributeCode == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("You must Select a Product attribute to proceed"));
            return null;
        }
        if (inputType != null) {
            if (inputType.toString().equalsIgnoreCase("GENDER")) {
                FixedValue = GlobalCC.checkNullValues(choiceSex.getValue());
                if (FixedValue == null) {
                    GlobalCC.EXCEPTIONREPORTING(new Exception("You must Select gender "));
                    return null;
                }
            }

            else if (inputType.toString().equalsIgnoreCase("CLIENTTYPE")) {
                FixedValue = GlobalCC.checkNullValues(choiceCtype.getValue());
                if (FixedValue == null) {
                    GlobalCC.EXCEPTIONREPORTING(new Exception("You must Select a client type  to  proceed"));
                    return null;
                }
            } else if (inputType.toString().equalsIgnoreCase("CLIENTBUSINESS")) {
                FixedValue = GlobalCC.checkNullValues(txtSector.getValue());
                if (FixedValue == null) {
                    GlobalCC.EXCEPTIONREPORTING(new Exception("You must Select a client sector to  proceed"));
                    return null;
                }
            } else if (inputType.toString().equalsIgnoreCase("DOB")) {
                MinValue = GlobalCC.parseDate(dtMinValue.getValue());

                MaxValue = GlobalCC.parseDate(dtMaxValue.getValue());

                if (MinValue == null || MaxValue == null) {
                    GlobalCC.EXCEPTIONREPORTING(new Exception("The two datae Values must be entered"));
                    return null;
                }
            } else if (inputType.toString().equalsIgnoreCase("UNSPECIFIED")) {
                FixedValue = GlobalCC.checkNullValues(txtAttrValue.getValue());
                if (FixedValue == null) {
                    GlobalCC.EXCEPTIONREPORTING(new Exception("You must Key-in Value"));
                    return null;
                }
            }


        }

        OracleConnection conn = null;
        String Query =
            "BEGIN Tqc_Product_Pkg.productClient_prc(?,?,?,?,?,?,?);END;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;

        try {


            conn = connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            if (prodClientCode == null) {
                cst.setString(1, "A");
                cst.setString(2, null);
            } else {
                cst.setString(1, "E");
                cst.setString(2, prodClientCode);
            }
            cst.setString(3, prodCode);
            cst.setString(4, clientAttributeCode);
            cst.setObject(5, MinValue);
            cst.setObject(6, MaxValue);
            cst.setString(7, FixedValue);
            cst.execute();
            conn.commit();
            conn.close();
            GlobalCC.dismissPopUp("crm", "newEditProdClientAttrPOP");
            String message =
                prodClientCode == null ? "Record Successfully Added" :
                "Record Successfully Editted";
            GlobalCC.INFORMATIONREPORTING(message);
            session.setAttribute("PRODUCTCODE", prodCode);
            ADFUtils.findIterator("findProductClientAttributeIterator").executeQuery();
            GlobalCC.refreshUI(tblProductClientAttributes);

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);


            return null;
        }

        return null;
    }

    public String actionHideClientAttrPop() {
        GlobalCC.dismissPopUp("crm", "ClientsAttributesPOP");
        return null;
    }

    public void setLblMaxValue(RichOutputLabel lblMaxValue) {
        this.lblMaxValue = lblMaxValue;
    }

    public RichOutputLabel getLblMaxValue() {
        return lblMaxValue;
    }

    public void setLblMinValue(RichOutputLabel lblMinValue) {
        this.lblMinValue = lblMinValue;
    }

    public RichOutputLabel getLblMinValue() {
        return lblMinValue;
    }

    public void setLblPrompt(RichOutputLabel lblPrompt) {
        this.lblPrompt = lblPrompt;
    }

    public RichOutputLabel getLblPrompt() {
        return lblPrompt;
    }

    public void setPnGroupValues(HtmlPanelGroup pnGroupValues) {
        this.pnGroupValues = pnGroupValues;
    }

    public HtmlPanelGroup getPnGroupValues() {
        return pnGroupValues;
    }

    public void setGridClientType(HtmlPanelGrid gridClientType) {
        this.gridClientType = gridClientType;
    }

    public HtmlPanelGrid getGridClientType() {
        return gridClientType;
    }

    public void setTxtAttrValue(RichInputText txtAttrValue) {
        this.txtAttrValue = txtAttrValue;
    }

    public RichInputText getTxtAttrValue() {
        return txtAttrValue;
    }

    public void setGridPrompt(HtmlPanelGrid gridPrompt) {
        this.gridPrompt = gridPrompt;
    }

    public HtmlPanelGrid getGridPrompt() {
        return gridPrompt;
    }

    public void setGridValue(HtmlPanelGrid gridValue) {
        this.gridValue = gridValue;
    }

    public HtmlPanelGrid getGridValue() {
        return gridValue;
    }

    public void setGridSex(HtmlPanelGrid gridSex) {
        this.gridSex = gridSex;
    }

    public HtmlPanelGrid getGridSex() {
        return gridSex;
    }

    public void setPgGroupClientAttributes(RichPanelGroupLayout pgGroupClientAttributes) {
        this.pgGroupClientAttributes = pgGroupClientAttributes;
    }

    public RichPanelGroupLayout getPgGroupClientAttributes() {
        return pgGroupClientAttributes;
    }


    public void setGridSector(HtmlPanelGrid gridSector) {
        this.gridSector = gridSector;
    }

    public HtmlPanelGrid getGridSector() {
        return gridSector;
    }

    public void setTblSectors(RichTable tblSectors) {
        this.tblSectors = tblSectors;
    }

    public RichTable getTblSectors() {
        return tblSectors;
    }

    public String actionAcceptSector() {
        Object key = tblSectors.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtValue.setValue(nodeBinding.getAttribute("code"));
            txtSector.setValue(nodeBinding.getAttribute("name"));
            GlobalCC.refreshUI(txtSector);
        }
        GlobalCC.dismissPopUp("crm", "SectorsPop");
        return null;
    }


    public void setTxtValue(RichInputText txtValue) {
        this.txtValue = txtValue;
    }

    public RichInputText getTxtValue() {
        return txtValue;
    }

    public void setTxtSector(RichInputText txtSector) {
        this.txtSector = txtSector;
    }

    public RichInputText getTxtSector() {
        return txtSector;
    }

    public void setGridMinValLabel(HtmlPanelGrid gridMinValLabel) {
        this.gridMinValLabel = gridMinValLabel;
    }

    public HtmlPanelGrid getGridMinValLabel() {
        return gridMinValLabel;
    }

    public void setGridMinValIn(HtmlPanelGrid gridMinValIn) {
        this.gridMinValIn = gridMinValIn;
    }

    public HtmlPanelGrid getGridMinValIn() {
        return gridMinValIn;
    }

    public void setGridMaxLabel(HtmlPanelGrid gridMaxLabel) {
        this.gridMaxLabel = gridMaxLabel;
    }

    public HtmlPanelGrid getGridMaxLabel() {
        return gridMaxLabel;
    }

    public void setGridMaxValIn(HtmlPanelGrid gridMaxValIn) {
        this.gridMaxValIn = gridMaxValIn;
    }

    public HtmlPanelGrid getGridMaxValIn() {
        return gridMaxValIn;
    }

    public void setChoiceSex(RichSelectOneChoice choiceSex) {
        this.choiceSex = choiceSex;
    }

    public RichSelectOneChoice getChoiceSex() {
        return choiceSex;
    }

    public void setChoiceCtype(RichSelectOneChoice choiceCtype) {
        this.choiceCtype = choiceCtype;
    }

    public RichSelectOneChoice getChoiceCtype() {
        return choiceCtype;
    }

    public void setDtMinValue(RichInputDate dtMinValue) {
        this.dtMinValue = dtMinValue;
    }

    public RichInputDate getDtMinValue() {
        return dtMinValue;
    }

    public void setDtMaxValue(RichInputDate dtMaxValue) {
        this.dtMaxValue = dtMaxValue;
    }

    public RichInputDate getDtMaxValue() {
        return dtMaxValue;
    }

    public void setGridMinDate(HtmlPanelGrid gridMinDate) {
        this.gridMinDate = gridMinDate;
    }

    public HtmlPanelGrid getGridMinDate() {
        return gridMinDate;
    }

    public void setGridMaxDate(HtmlPanelGrid gridMaxDate) {
        this.gridMaxDate = gridMaxDate;
    }

    public HtmlPanelGrid getGridMaxDate() {
        return gridMaxDate;
    }

    private void resetProdClientFields() {
        txtProdClientCode.setValue(null);
        txtClientAttributeCode.setValue(null);
        txtAttrValue.setValue(null);
        txtClientAttrName.setValue(null);
        gridClientType.setRendered(false);
        gridPrompt.setRendered(false);
        gridValue.setRendered(false);
        gridSex.setRendered(false);
        gridSector.setRendered(false);
        gridMinValLabel.setRendered(false);
        gridMinValIn.setRendered(false);
        gridMaxLabel.setRendered(false);
        gridMaxValIn.setRendered(false);
        gridMinDate.setRendered(false);
        gridMaxDate.setRendered(false);
        dtMinValue.setValue(null);
        dtMaxValue.setValue(null);
        noMinValue.setValue(null);
        noMaxValue.setValue(null);
    }

    public void setGrdValueLabel(HtmlPanelGrid grdValueLabel) {
        this.grdValueLabel = grdValueLabel;
    }

    public HtmlPanelGrid getGrdValueLabel() {
        return grdValueLabel;
    }
}
