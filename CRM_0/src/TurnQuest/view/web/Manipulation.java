package TurnQuest.view.web;


//import TurnQuest.view.commons.GlobalCC;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanRadio;
import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;
import oracle.adf.view.rich.component.rich.input.RichTextEditor;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;

import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;


public class Manipulation {
    private RichSelectBooleanRadio rdCategory;
    private RichSelectBooleanRadio rdProducts;
    private RichPanelGroupLayout gpDtls;
    private RichPanelBox pbTrans;
    private RichPanelBox pbCategories;
    private RichPanelBox pbProducts;
    private RichInputNumberSpinbox txtCatCode;
    private RichInputText txtCatName;
    private RichTextEditor txtCatDesc;
    private RichTable categoryTab;
    private RichTextEditor txtPreview;
    private RichTree trCategories;
    private RichTable tbProduct;
    private RichTable tbOptions;
    private RichInputNumberSpinbox txtProdCode;
    private RichInputText txtProdName;
    private RichTextEditor txtProdDesc;
    private RichTable products;
    private RichInputNumberSpinbox txtPopCode;
    private RichInputText txtPopName;
    private RichTextEditor txtPopDesc;
    private RichTable options;
    private RichInputText txtSystem;
    private RichTable systemTbl;
    private RichSelectBooleanRadio contractPolicy;
    private RichSelectBooleanRadio openMarket;
    private RichSelectOneRadio contractOpenRadio;
    private RichInputText txtProductName;
    private RichTable productsTable;
    private RichInputText txtAgencyAccount;
    private RichTable selectAgencyAccount;

    public Manipulation() {
        super();
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);


    public void setRdCategory(RichSelectBooleanRadio rdCategory) {
        this.rdCategory = rdCategory;
    }

    public RichSelectBooleanRadio getRdCategory() {
        return rdCategory;
    }

    public void setRdProducts(RichSelectBooleanRadio rdProducts) {
        this.rdProducts = rdProducts;
    }

    public RichSelectBooleanRadio getRdProducts() {
        return rdProducts;
    }

    public void setGpDtls(RichPanelGroupLayout gpDtls) {
        this.gpDtls = gpDtls;
    }

    public RichPanelGroupLayout getGpDtls() {
        return gpDtls;
    }

    public void setPbTrans(RichPanelBox pbTrans) {
        this.pbTrans = pbTrans;
    }

    public RichPanelBox getPbTrans() {
        return pbTrans;
    }

    public String transSelected() {
        if (rdCategory.isSelected()) {
            pbTrans.setRendered(false);
            pbCategories.setRendered(true);
            pbProducts.setRendered(false);
        } else if (rdProducts.isSelected()) {
            pbTrans.setRendered(false);
            pbCategories.setRendered(false);
            pbProducts.setRendered(true);
        }
        GlobalCC.refreshUI(gpDtls);
        return null;
    }

    public void setPbCategories(RichPanelBox pbCategories) {
        this.pbCategories = pbCategories;
    }

    public RichPanelBox getPbCategories() {
        return pbCategories;
    }

    public void setPbProducts(RichPanelBox pbProducts) {
        this.pbProducts = pbProducts;
    }

    public RichPanelBox getPbProducts() {
        return pbProducts;
    }

    public String addCategory() {
        txtCatCode.setValue(null);
        txtCatName.setValue(null);
        txtCatDesc.setValue(null);
        GlobalCC.showPopup("demoTemplate:cat");
        return null;
    }

    public String editCategory() {
        Object obj = categoryTab.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)obj;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        txtCatCode.setValue(r.getAttribute("twpCode"));
        txtCatName.setValue(r.getAttribute("twpcName"));
        txtCatDesc.setValue(r.getAttribute("twpcDesc"));
        GlobalCC.showPopup("demoTemplate:cat");
        return null;
    }

    public String deleteCategory() {
        Object obj = categoryTab.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)obj;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        try {

            String query =
                "begin TQC_PORTAL_PKG.categories_proc(?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setObject(1, "D");
            cst.setObject(2, null);
            cst.setObject(3, r.getAttribute("twpCode"));
            cst.setObject(4, null);
            cst.setObject(5, null);
            cst.execute();
            cst.close();
            conn.commit();
            conn.close();

            GlobalCC.sysInformation("Record Deleted Successfully");
            ADFUtils.findIterator("findProductCategoriesIterator").executeQuery();
            ADFUtils.findIterator("findprodCategoriesIterator").executeQuery();
            GlobalCC.refreshUI(categoryTab);
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public String previewCategory() {
        Object obj = categoryTab.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)obj;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        txtPreview.setValue(r.getAttribute("twpcDesc"));
        GlobalCC.showPopup("demoTemplate:preview");
        return null;
    }

    public void setTxtCatCode(RichInputNumberSpinbox txtCatCode) {
        this.txtCatCode = txtCatCode;
    }

    public RichInputNumberSpinbox getTxtCatCode() {
        return txtCatCode;
    }

    public void setTxtCatName(RichInputText txtCatName) {
        this.txtCatName = txtCatName;
    }

    public RichInputText getTxtCatName() {
        return txtCatName;
    }

    public void setTxtCatDesc(RichTextEditor txtCatDesc) {
        this.txtCatDesc = txtCatDesc;
    }

    public RichTextEditor getTxtCatDesc() {
        return txtCatDesc;
    }

    public String saveCategory() {
        String codeVal = null;
        String nameVal = null;
        String descVal = null;
        codeVal = GlobalCC.checkNullValues(txtCatCode.getValue());
        nameVal = GlobalCC.checkNullValues(txtCatName.getValue());
        descVal = GlobalCC.checkNullValues(txtCatDesc.getValue());
        if (nameVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing : Name");
            return null;
        }
        if (descVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing : Description");
            return null;
        }

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        try {

            String query =
                "begin TQC_PORTAL_PKG.categories_proc(?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            if (codeVal == null) {
                cst.setObject(1, "A");
            } else {
                cst.setObject(1, "E");
            }

            cst.setObject(2, session.getAttribute("systemCode"));
            cst.setObject(3, codeVal);
            cst.setObject(4, nameVal);
            cst.setObject(5, descVal);
            cst.execute();
            cst.close();
            conn.commit();
            conn.close();
            GlobalCC.hidePopup("demoTemplate:cat");
            GlobalCC.sysInformation("Record Saved Successfully");
            ADFUtils.findIterator("findprodCategoriesIterator").executeQuery();
            GlobalCC.refreshUI(categoryTab);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public String cancelCategory() {
        GlobalCC.hidePopup("demoTemplate:cat");
        return null;
    }

    public void setCategoryTab(RichTable categoryTab) {
        this.categoryTab = categoryTab;
    }

    public RichTable getCategoryTab() {
        return categoryTab;
    }

    public void setTxtPreview(RichTextEditor txtPreview) {
        this.txtPreview = txtPreview;
    }

    public RichTextEditor getTxtPreview() {
        return txtPreview;
    }

    public void setTrCategories(RichTree trCategories) {
        this.trCategories = trCategories;
    }

    public RichTree getTrCategories() {
        return trCategories;
    }

    public void setTbProduct(RichTable tbProduct) {
        this.tbProduct = tbProduct;
    }

    public RichTable getTbProduct() {
        return tbProduct;
    }

    public void setTbOptions(RichTable tbOptions) {
        this.tbOptions = tbOptions;
    }

    public RichTable getTbOptions() {
        return tbOptions;
    }


    public void trCategorySelected(SelectionEvent selectionEvent) {

        if (selectionEvent.getAddedSet() != null &&
            selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();
            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    trCategories.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nodeBinding =
                        (JUCtrlHierNodeBinding)trCategories.getRowData();

                    String nodeType =
                        (String)nodeBinding.getRow().getAttribute("nodeType");
                    if (nodeType.equals("P")) {
                        session.removeAttribute("twpcCode");
                        session.setAttribute("systemCode",
                                             nodeBinding.getRow().getAttribute("systemCode"));
                    } else if (nodeType.equals("S")) {

                        session.setAttribute("systemCode",
                                             nodeBinding.getParent().getRow().getAttribute("systemCode"));
                        session.setAttribute("twpcCode",
                                             nodeBinding.getRow().getAttribute("twpCode"));

                    }
                }
            }
        }
        ADFUtils.findIterator("findCatProductsIterator").executeQuery();
        ADFUtils.findIterator("findProductOptionsIterator").executeQuery();
        ADFUtils.findIterator("findLifeProductsIterator").executeQuery();
        GlobalCC.refreshUI(tbProduct);
        GlobalCC.refreshUI(tbOptions);
        GlobalCC.refreshUI(products);
    }

    public void tbProductSelected(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet rowKeySet = selectionEvent.getAddedSet();

            Object key2 = rowKeySet.iterator().next();
            tbProduct.setRowKey(key2);
            JUCtrlValueBinding r = (JUCtrlValueBinding)tbProduct.getRowData();
            session.setAttribute("prodCode", r.getAttribute("prodCode"));
            session.setAttribute("bindCode", r.getAttribute("bindCode"));
            session.setAttribute("twpCode", r.getAttribute("twpCode"));
            ADFUtils.findIterator("findProductOptionsIterator").executeQuery();
            ADFUtils.findIterator("findLifeOptionsIterator").executeQuery();
            GlobalCC.refreshUI(tbOptions);
            GlobalCC.refreshUI(options);
        }
    }

    public void setTxtProdCode(RichInputNumberSpinbox txtProdCode) {
        this.txtProdCode = txtProdCode;
    }

    public RichInputNumberSpinbox getTxtProdCode() {
        return txtProdCode;
    }

    public void setTxtProdName(RichInputText txtProdName) {
        this.txtProdName = txtProdName;
    }

    public RichInputText getTxtProdName() {
        return txtProdName;
    }

    public void setTxtProdDesc(RichTextEditor txtProdDesc) {
        this.txtProdDesc = txtProdDesc;
    }

    public RichTextEditor getTxtProdDesc() {
        return txtProdDesc;
    }

    public void setProducts(RichTable products) {
        this.products = products;
    }

    public RichTable getProducts() {
        return products;
    }

    public String newProduct() {
        txtProdCode.setValue(null);
        txtProdName.setValue(null);
        txtProdName.setLabel(null);
        txtProdDesc.setValue(null);
        txtAgencyAccount.setValue(null);
        GlobalCC.showPopup("demoTemplate:prodPop");
        return null;
    }

    public String editProduct() {
        Object obj = tbProduct.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)obj;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        txtProdCode.setValue(r.getAttribute("twpCode"));
        txtProdName.setValue(r.getAttribute("prodDesc"));
        txtProductName.setValue(r.getAttribute("prodDesc"));
        txtProdName.setLabel(r.getAttribute("prodCode").toString());
        txtProdDesc.setValue(r.getAttribute("twpProdDesc"));
        txtAgencyAccount.setValue(r.getAttribute("agaShtDesc"));
        session.setAttribute("agaCode", r.getAttribute("agaCode"));
        session.setAttribute("agntCode", r.getAttribute("agencyCode"));
        session.setAttribute("binderCode", r.getAttribute("bindCode"));
        ADFUtils.findIterator("findAgencyAccountIterator").executeQuery();
        GlobalCC.showPopup("demoTemplate:prodPop");

        return null;
    }

    public String deleteProduct() {
        Object obj = tbProduct.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)obj;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        try {

            String query =
                "begin TQC_PORTAL_PKG.products_proc(?,?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setObject(1, "D");
            cst.setObject(2, r.getAttribute("twpCode"));
            cst.setObject(3, null);
            cst.setObject(4, null);
            cst.setObject(5, null);
            cst.setObject(6, null);
            cst.setObject(7, null);
            cst.execute();
            cst.close();
            conn.commit();
            conn.close();
            GlobalCC.sysInformation("Record Deleted Successfully");
            ADFUtils.findIterator("findCatProductsIterator").executeQuery();
            GlobalCC.refreshUI(tbProduct);

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public String previewProduct() {
        Object obj = tbProduct.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)obj;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        txtPreview.setValue(r.getAttribute("twpProdDesc"));
        GlobalCC.showPopup("demoTemplate:preview");
        return null;
    }

    public String launchProduct() {
        GlobalCC.showPopup("demoTemplate:products");
        return null;
    }

    public String productSelected() {
        Object obj = products.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)obj;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        txtProdName.setValue(r.getAttribute("prodDesc"));
        txtProdName.setLabel(r.getAttribute("prodCode").toString());
        GlobalCC.refreshUI(txtProdName);
        GlobalCC.hidePopup("demoTemplate:products");
        return null;
    }

    public String cancelProducts() {
        GlobalCC.hidePopup("demoTemplate:products");
        return null;
    }

    public String saveProduct() {
        String codeVal = null;
        String nameVal = null;
        String descVal = null;
        String bindType = "M";
        BigDecimal bindCode;
        BigDecimal twpcCode;
        BigDecimal agencyAccount;
        BigDecimal agencyCode;
        codeVal = GlobalCC.checkNullValues(txtProdCode.getValue());
        nameVal = GlobalCC.checkNullValues(txtProdName.getValue());
        descVal = GlobalCC.checkNullValues(txtProdDesc.getValue());
        if (contractOpenRadio.getValue().equals("B")) {
            session.setAttribute("binderType", "B");
        } else {
            session.setAttribute("binderType", "M");
        }
        if (nameVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing : Product");
            return null;
        }
        nameVal = txtProdName.getLabel();
        if (descVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing : Description");
            return null;
        }

        if (session.getAttribute("binderType").equals("B")) {
            bindType = "Y";
        } else {
            bindType = "N";
        }


        if (session.getAttribute("binderCode") != null) {
            bindCode =
                    new BigDecimal(session.getAttribute("binderCode").toString());
        } else {
            bindCode = null;
        }
        if (session.getAttribute("twpcCode") != null) {
            twpcCode =
                    new BigDecimal(session.getAttribute("twpcCode").toString());
        } else {
            twpcCode = null;
        }
        if (session.getAttribute("agaCode") != null) {
            agencyAccount =
                    new BigDecimal(session.getAttribute("agaCode").toString());
        } else {
            agencyAccount = null;
        }
        if (session.getAttribute("agntCode") != null) {
            agencyCode =
                    new BigDecimal(session.getAttribute("agntCode").toString());
        } else {
            agencyCode = null;
        }
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        try {

            String query =
                "begin TQC_PORTAL_PKG.products_proc(?,?,?,?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            if (codeVal == null) {
                cst.setObject(1, "A");
            } else {
                cst.setObject(1, "E");
            }

            cst.setObject(2, codeVal);
            System.out.println("This is twpcCode" +
                               session.getAttribute("twpcCode"));
            cst.setObject(3, twpcCode);
            cst.setObject(4, nameVal);
            cst.setObject(5, descVal);
            cst.setObject(6, bindType);
            cst.setObject(7, bindCode);
            cst.setObject(8, agencyAccount);
            cst.setObject(9, agencyCode);
            cst.execute();
            cst.close();
            conn.commit();
            conn.close();
            GlobalCC.hidePopup("demoTemplate:prodPop");
            GlobalCC.sysInformation("Record Saved Successfully");
            ADFUtils.findIterator("findCatProductsIterator").executeQuery();
            GlobalCC.refreshUI(tbProduct);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
        }

        return null;
    }

    public String cancelProdDtls() {
        GlobalCC.hidePopup("demoTemplate:prodPop");
        return null;
    }

    public void setTxtPopCode(RichInputNumberSpinbox txtPopCode) {
        this.txtPopCode = txtPopCode;
    }

    public RichInputNumberSpinbox getTxtPopCode() {
        return txtPopCode;
    }

    public void setTxtPopName(RichInputText txtPopName) {
        this.txtPopName = txtPopName;
    }

    public RichInputText getTxtPopName() {
        return txtPopName;
    }

    public void setTxtPopDesc(RichTextEditor txtPopDesc) {
        this.txtPopDesc = txtPopDesc;
    }

    public RichTextEditor getTxtPopDesc() {
        return txtPopDesc;
    }

    public void setOptions(RichTable options) {
        this.options = options;
    }

    public RichTable getOptions() {
        return options;
    }

    public String newOption() {
        Object obj = tbProduct.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)obj;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("Please select A product");
            return null;
        }
        txtPopCode.setValue(null);
        txtPopName.setValue(null);
        txtPopName.setLabel(null);
        txtPopDesc.setValue(null);
        GlobalCC.showPopup("demoTemplate:optPop");
        return null;
    }

    public String editOption() {
        Object obj = tbOptions.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)obj;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        txtPopCode.setValue(r.getAttribute("twpoCode"));
        txtPopName.setValue(r.getAttribute("popDesc"));
        txtPopName.setLabel(r.getAttribute("popCode").toString());
        txtPopDesc.setValue(r.getAttribute("twpoDesc"));
        GlobalCC.showPopup("demoTemplate:optPop");
        return null;
    }

    public String deleteOption() {
        Object obj = tbOptions.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)obj;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        try {

            String query =
                "begin TQC_PORTAL_PKG.options_proc(?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setObject(1, "D");
            cst.setObject(2, r.getAttribute("twpoCode"));
            cst.setObject(3, null);
            cst.setObject(4, null);
            cst.setObject(5, null);
            cst.setObject(6, null);
            cst.execute();
            cst.close();
            conn.commit();
            conn.close();
            GlobalCC.sysInformation("Record Deleted Successfully");
            ADFUtils.findIterator("findProductOptionsIterator").executeQuery();
            GlobalCC.refreshUI(tbOptions);

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String previewOption() {
        Object obj = tbOptions.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)obj;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        txtPreview.setValue(r.getAttribute("twpoDesc"));
        GlobalCC.showPopup("demoTemplate:preview");
        return null;
    }

    public String launchOptions() {
        GlobalCC.showPopup("demoTemplate:pop");
        return null;
    }

    public String optionSelected() {
        Object obj = options.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)obj;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        txtPopName.setValue(r.getAttribute("popDesc"));
        txtPopName.setLabel(r.getAttribute("popCode").toString());
        GlobalCC.hidePopup("demoTemplate:pop");
        GlobalCC.refreshUI(txtPopName);

        return null;
    }

    public String cancelOptions() {
        GlobalCC.hidePopup("demoTemplate:pop");
        return null;
    }

    public String saveOptions() {
        String codeVal = null;
        String nameVal = null;
        String descVal = null;
        BigDecimal bindCode;
        BigDecimal twpCode;

        codeVal = GlobalCC.checkNullValues(txtPopCode.getValue());
        nameVal = GlobalCC.checkNullValues(txtPopName.getValue());
        descVal = GlobalCC.checkNullValues(txtPopDesc.getValue());
        if (nameVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing : Product Option");
            return null;
        }
        nameVal = txtPopName.getLabel();
        if (descVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing : Description");
            return null;
        }
        if (session.getAttribute("bindCode") != null) {
            bindCode =
                    new BigDecimal(session.getAttribute("bindCode").toString());
        } else {
            bindCode = null;
        }
        if (session.getAttribute("twpCode") != null) {
            twpCode =
                    new BigDecimal(session.getAttribute("twpCode").toString());
        } else {
            twpCode = null;
        }
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        try {

            String query =
                "begin TQC_PORTAL_PKG.options_proc(?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            if (codeVal == null) {
                cst.setObject(1, "A");
            } else {
                cst.setObject(1, "E");
            }

            cst.setObject(2, codeVal);
            cst.setObject(3, nameVal);
            cst.setObject(4, descVal);
            cst.setObject(5, bindCode);
            cst.setObject(6, twpCode);
            cst.execute();
            cst.close();
            conn.commit();
            conn.close();
            GlobalCC.hidePopup("demoTemplate:optPop");
            GlobalCC.sysInformation("Record Saved Successfully");
            ADFUtils.findIterator("findProductOptionsIterator").executeQuery();
            GlobalCC.refreshUI(tbOptions);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String cancelOptionDtls() {
        GlobalCC.hidePopup("demoTemplate:optPop");
        return null;
    }

    public void setTxtSystem(RichInputText txtSystem) {
        this.txtSystem = txtSystem;
    }

    public RichInputText getTxtSystem() {
        return txtSystem;
    }

    public void setSystemTbl(RichTable systemTbl) {
        this.systemTbl = systemTbl;
    }

    public RichTable getSystemTbl() {
        return systemTbl;
    }

    public void systemSelected(SelectionEvent selectionEvent) {
        Object obj = systemTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)obj;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return;
        }
        session.setAttribute("systemCode", r.getAttribute("systemCode"));
        ADFUtils.findIterator("findprodCategoriesIterator").executeQuery();
        GlobalCC.refreshUI(categoryTab);
    }

    public void OpenMarketListener(ValueChangeEvent valueChangeEvent) {

        if (openMarket.isSelected()) {
            session.setAttribute("binderType", "M");
            /* session.setAttribute("productCode", null);
      session.setAttribute("productDesc", null);
      session.setAttribute("BindCode", null);
      session.setAttribute("ProductShtCode", null);

      productDescription.setValue(null);*/
            ADFUtils.findIterator("findTransProductsIterator").executeQuery();
            GlobalCC.refreshUI(productsTable);
        }
    }

    public void ContractBinderListener(ValueChangeEvent valueChangeEvent) {

        if (contractPolicy.isSelected()) {
            session.setAttribute("binderType", "B");
            /* session.setAttribute("productCode", null);
          session.setAttribute("productDesc", null);
          session.setAttribute("BindCode", null);
          session.setAttribute("ProductShtCode", null);

          productDescription.setValue(null);*/
            ADFUtils.findIterator("findTransProductsIterator").executeQuery();
            GlobalCC.refreshUI(productsTable);
        }


    }

    public void setContractPolicy(RichSelectBooleanRadio contractPolicy) {
        this.contractPolicy = contractPolicy;
    }

    public RichSelectBooleanRadio getContractPolicy() {
        return contractPolicy;
    }

    public void setOpenMarket(RichSelectBooleanRadio openMarket) {
        this.openMarket = openMarket;
    }

    public RichSelectBooleanRadio getOpenMarket() {
        return openMarket;
    }

    public void setContractOpenRadio(RichSelectOneRadio contractOpenRadio) {
        this.contractOpenRadio = contractOpenRadio;
    }

    public RichSelectOneRadio getContractOpenRadio() {
        return contractOpenRadio;
    }

    public void setTxtProductName(RichInputText txtProductName) {
        this.txtProductName = txtProductName;
    }

    public RichInputText getTxtProductName() {
        return txtProductName;
    }

    public String launchBrokerOptions() {
        GlobalCC.showPopup("demoTemplate:BrokerProduct");
        return null;
    }

    public void setProductsTable(RichTable productsTable) {
        this.productsTable = productsTable;
    }

    public RichTable getProductsTable() {
        return productsTable;
    }

    public String selectProduct() {
        Object key = productsTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        txtProductName.setValue(r.getAttribute("productDesc"));
        txtProdCode.setValue(r.getAttribute("productCode"));
        txtProdName.setValue(r.getAttribute("productDesc"));
        txtProdName.setLabel(r.getAttribute("productCode").toString());
        session.setAttribute("productCode", r.getAttribute("productCode"));
        session.setAttribute("binderCode", r.getAttribute("binderCode"));
        session.setAttribute("agntCode", r.getAttribute("agntCode"));
        GlobalCC.refreshUI(txtProductName);
        GlobalCC.hidePopup("demoTemplate:BrokerProduct");
        return null;
    }

    public void selectContractBinder(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getOldValue() != valueChangeEvent.getNewValue()) {
            if (contractOpenRadio.getValue().equals("B")) {
                session.setAttribute("binderType", "B");
            } else {
                session.setAttribute("binderType", "M");
            }
        } else {
            session.setAttribute("binderType", "B");
        }

    }

    public void setTxtAgencyAccount(RichInputText txtAgencyAccount) {
        this.txtAgencyAccount = txtAgencyAccount;
    }

    public RichInputText getTxtAgencyAccount() {
        return txtAgencyAccount;
    }

    public String launchAgencyLox() {
        GlobalCC.showPopup("demoTemplate:agencyAccount");
        return null;
    }

    public String selectAgencyAccount() {
        Object key = selectAgencyAccount.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        txtAgencyAccount.setValue(r.getAttribute("agaName"));
        session.setAttribute("agaCode", r.getAttribute("agaCode"));
        GlobalCC.refreshUI(txtAgencyAccount);
        GlobalCC.hidePopup("demoTemplate:agencyAccount");
        return null;
    }

    public String cancelAgencyOptions() {
        // Add event code here...
        return null;
    }

    public void setSelectAgencyAccount(RichTable selectAgencyAccount) {
        this.selectAgencyAccount = selectAgencyAccount;
    }

    public RichTable getSelectAgencyAccount() {
        return selectAgencyAccount;
    }
}
