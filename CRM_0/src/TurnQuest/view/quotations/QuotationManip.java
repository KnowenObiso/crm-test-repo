package TurnQuest.view.quotations;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.RichDialog;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanRadio;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.input.RichTextEditor;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.nav.RichCommandImageLink;
import oracle.adf.view.rich.component.rich.nav.RichCommandLink;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.component.rich.output.RichSeparator;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.apache.myfaces.trinidad.component.core.data.CoreTable;
import org.apache.myfaces.trinidad.component.core.data.CoreTree;
import org.apache.myfaces.trinidad.convert.DateTimeConverter;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class QuotationManip {
    private RichTable quotation;
    private RichPanelBox lifeCategories;
    private RichPanelBox categoryProducts;
    private CoreTree catListing;
    private RichPanelGroupLayout dtls;
    private RichTextEditor categoryDtls;
    private CoreTree catDetails;
    private RichTextEditor productDtls;
    private RichPanelBox productTab;
    private CoreTree products;
    private RichTextEditor prodOptDtls;
    private RichPanelBox proposalTab;
    private HtmlPanelGrid mainParent;
    private RichInputDate myDate;
    private RichInputNumberSpinbox txtAge;
    private RichInputNumberSpinbox txtTerm;
    private RichSelectOneChoice txtPayFreq;
    private RichInputNumberSpinbox txtPremium;
    private RichInputNumberSpinbox txtSumAssured;
    private CoreTable productOption;
    private RichPanelBox portfolioTab;
    private RichPopup previewButtonPop;
    private RichDialog prevDialogue;

    public QuotationManip() {
    }
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);


    public String quoteSelected() {
        Object key2 = quotation.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        session.setAttribute("quoteCode",
                             nodeBinding.getAttribute("quotationCode"));
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/viewQuotation.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public void setQuotation(RichTable quotation) {
        this.quotation = quotation;
    }

    public RichTable getQuotation() {
        return quotation;
    }

    public void setLifeCategories(RichPanelBox lifeCategories) {
        this.lifeCategories = lifeCategories;
    }

    public RichPanelBox getLifeCategories() {
        return lifeCategories;
    }

    public void setCategoryProducts(RichPanelBox categoryProducts) {
        this.categoryProducts = categoryProducts;
    }

    public RichPanelBox getCategoryProducts() {
        return categoryProducts;
    }

    public void setCatListing(CoreTree catListing) {
        this.catListing = catListing;
    }

    public CoreTree getCatListing() {
        return catListing;
    }

    public void setDtls(RichPanelGroupLayout dtls) {
        this.dtls = dtls;
    }

    public RichPanelGroupLayout getDtls() {
        return dtls;
    }

    public void categoryDtlsSelectedEvnt(ActionEvent actionEvent) {
        RichCommandImageLink link =
            (RichCommandImageLink)actionEvent.getComponent();
        String twpcCode = link.getShortDesc();
        String twpcDtls = null;
        session.setAttribute("twpcCode", twpcCode);
        System.out.println(session.getAttribute("twpcCode"));
        int k = 0;
        while (catDetails.getRowCount() > k) {
            Object obj = catDetails.getRowData(k);
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)obj;
            if (nodeBinding.getAttribute("twpcCode").toString().equalsIgnoreCase(twpcCode)) {
                twpcDtls = nodeBinding.getAttribute("twpcDesc").toString();
            }
            k++;
        }
        if (portfolioTab != null) {
            portfolioTab.setRendered(false);
        }
        lifeCategories.setRendered(false);
        categoryProducts.setRendered(true);
        productTab.setRendered(false);
        proposalTab.setRendered(false);
        categoryDtls.setValue(twpcDtls);
        ADFUtils.findIterator("findCategoryProductsIterator").executeQuery();
        GlobalCC.refreshUI(dtls);
    }

    public void categorySelected(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet rowKeySet = selectionEvent.getAddedSet();

            Object key2 = rowKeySet.iterator().next();
            catListing.setRowKey(key2);

            JUCtrlValueBinding nodeBinding =
                (JUCtrlValueBinding)catListing.getRowData();

            System.out.println(catListing.getRowData());
            session.setAttribute("twpcCode",
                                 nodeBinding.getAttribute("nodeBinding"));
        }
    }

    public void categorySelectedEvnt(ActionEvent actionEvent) {
        RichCommandLink link = (RichCommandLink)actionEvent.getComponent();
        String twpcCode = link.getShortDesc();
        String twpcDtls = null;
        session.setAttribute("twpcCode", twpcCode);
        System.out.println(session.getAttribute("twpcCode"));
        int k = 0;
        while (catListing.getRowCount() > k) {
            Object obj = catListing.getRowData(k);
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)obj;
            System.out.println(nodeBinding.getAttribute("twpcCode").toString());
            System.out.println(twpcCode);
            if (nodeBinding.getAttribute("twpcCode").toString().equalsIgnoreCase(twpcCode)) {
                twpcDtls = nodeBinding.getAttribute("twpcDesc").toString();
            }
            k++;
        }
        if (portfolioTab != null) {
            portfolioTab.setRendered(false);
        }
        lifeCategories.setRendered(false);
        categoryProducts.setRendered(true);
        productTab.setRendered(false);
        proposalTab.setRendered(false);
        categoryDtls.setValue(twpcDtls);
        System.out.println(twpcDtls);
        ADFUtils.findIterator("findCategoryProductsIterator").executeQuery();
        GlobalCC.refreshUI(dtls);
    }


    public void setCategoryDtls(RichTextEditor categoryDtls) {
        this.categoryDtls = categoryDtls;
    }

    public RichTextEditor getCategoryDtls() {
        return categoryDtls;
    }

    public void setCatDetails(CoreTree catDetails) {
        this.catDetails = catDetails;
    }

    public CoreTree getCatDetails() {
        return catDetails;
    }

    public void setProductDtls(RichTextEditor productDtls) {
        this.productDtls = productDtls;
    }

    public RichTextEditor getProductDtls() {
        return productDtls;
    }

    public void setProductTab(RichPanelBox productTab) {
        this.productTab = productTab;
    }

    public RichPanelBox getProductTab() {
        return productTab;
    }

    public void productSelectedEvnt(ActionEvent actionEvent) {
        RichCommandLink link = (RichCommandLink)actionEvent.getComponent();
        String prodCode = link.getShortDesc();
        String prodDesc = null;
        session.setAttribute("prodCode", prodCode);
        int k = 0;
        while (products.getRowCount() > k) {
            Object obj = products.getRowData(k);
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)obj;
            System.out.println("Product Code");
            System.out.println(nodeBinding.getAttribute("prodCode"));
            System.out.println(prodCode);
            if (nodeBinding.getAttribute("prodCode").toString().equalsIgnoreCase(prodCode)) {
                prodDesc = nodeBinding.getAttribute("prodDesc").toString();
            }
            System.out.println(prodDesc);
            k++;
        }
        if (portfolioTab != null) {
            portfolioTab.setRendered(false);
        }
        lifeCategories.setRendered(false);
        categoryProducts.setRendered(true);
        productTab.setRendered(true);
        proposalTab.setRendered(false);
        prodOptDtls.setValue(prodDesc);
        ADFUtils.findIterator("findProductOptionsIterator").executeQuery();
        GlobalCC.refreshUI(dtls);
    }

    public void setProducts(CoreTree products) {
        this.products = products;
    }

    public CoreTree getProducts() {
        return products;
    }

    public void setProdOptDtls(RichTextEditor prodOptDtls) {
        this.prodOptDtls = prodOptDtls;
    }

    public RichTextEditor getProdOptDtls() {
        return prodOptDtls;
    }

    public void setProposalTab(RichPanelBox proposalTab) {
        this.proposalTab = proposalTab;
    }

    public RichPanelBox getProposalTab() {
        return proposalTab;
    }

    public void setMainParent(HtmlPanelGrid mainParent) {
        this.mainParent = mainParent;
    }

    public HtmlPanelGrid getMainParent() {
        return mainParent;
    }

    public void setMyDate(RichInputDate myDate) {
        this.myDate = myDate;
    }

    public RichInputDate getMyDate() {
        return myDate;
    }

    public String createComponents() {
        if (portfolioTab != null) {
            portfolioTab.setRendered(false);
        }
        lifeCategories.setRendered(false);
        categoryProducts.setRendered(false);
        productTab.setRendered(false);
        proposalTab.setRendered(true);
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
            "SELECT TSMS_DESC,TSMS_CODE,TSMS_ORDER FROM TQC_SYS_MOD_SUBUNITS,TQC_SYSTEM_MODULES WHERE TSMS_TSM_CODE = TSM_CODE AND TSM_SHT_DESC = 'PROPOSAL'  AND TSMS_PROD_CODE = " +
            session.getAttribute("prodCode") + " ORDER BY TSMS_ORDER";
        String sysSubunitsDtls =
            "SELECT TSMSD_CODE,TSMSD_ORDER,TSMSD_PROMPT,TSMSD_TYPE,TSMSD_MORE_DTLS_APPL,TSMSD_MORE_DTLS,TSMSD_ROOT,TSMSD_MORE_DTLS_REQUIRED FROM TQC_SYS_MOD_SUBUNITS_DETAILS WHERE TSMSD_TSMS_CODE = ? AND TSMSD_PARENT IS NULL ORDER BY TSMSD_ORDER";

        String sysSubunitsDtlsChild =
            "SELECT TSMSD_CODE,TSMSD_ORDER,TSMSD_PROMPT,TSMSD_TYPE,TSMSD_MORE_DTLS_APPL,TSMSD_MORE_DTLS,TSMSD_ROOT FROM TQC_SYS_MOD_SUBUNITS_DETAILS WHERE TSMSD_PARENT = ? ORDER BY TSMSD_ORDER";


        String sysSubunitsOptions =
            "SELECT TSSO_CODE, TSSO_OPTION_NAME, TSSO_OPTION_DESC,TSSO_TYPE FROM TQC_SYS_SUBUNITS_OPTIONS WHERE TSSO_TSMSD_CODE = ? ORDER BY TSSO_ORDER";

        String sysAppOptions =
            "SELECT COUNT(TSSO_CODE), TSSO_OPTION_NAME FROM TQC_SYS_SUBUNITS_OPTIONS WHERE TSSO_TSMSD_CODE = ? GROUP BY TSSO_OPTION_NAME";

        String sysOptCount =
            "SELECT COUNT(TSSO_CODE), TSSO_OPTION_NAME FROM TQC_SYS_SUBUNITS_OPTIONS WHERE TSSO_TSMSD_CODE = ?GROUP BY TSSO_OPTION_NAME";
        conn = dbConnector.getDatabaseConnection();
        try {

            cst = (OracleCallableStatement)conn.prepareCall(sysSubunits);

            OracleResultSet rs = (OracleResultSet)cst.executeQuery();
            mainParent.getChildren().clear();
            int k = 1;

            while (rs.next()) {
                if (rs.getInt(3) == 1 &&
                    session.getAttribute("ClientCode") == null) {
                    HtmlPanelGrid headerPnl = new HtmlPanelGrid();
                    headerPnl.setId("pg2");
                    headerPnl.setColumns(2);
                    headerPnl.setStyleClass("text-align:left;");
                    RichOutputLabel headerNo = new RichOutputLabel();
                    headerNo.setValue(k + ".");
                    headerNo.setInlineStyle("font-weight:bold;");
                    headerPnl.getChildren().add(headerNo);
                    RichOutputLabel subunitHeader = new RichOutputLabel();
                    if (rs.getInt(3) == 1 &&
                        session.getAttribute("ClientCode") != null) {
                        subunitHeader.setValue(rs.getString(1) +
                                               " - (Already Provided) ");
                        subunitHeader.setInlineStyle("font-weight:bold; color:Blue;");
                    } else {
                        subunitHeader.setInlineStyle("font-weight:bold;");
                        subunitHeader.setValue(rs.getString(1));
                    }

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
                    unitPnl.setId("pg4");
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
                            // System.out.println("Value1");
                            // System.out.println(rsDtls.getString(2));
                            headerNo.setValue(rsDtls.getString(2));
                            unitPnl.getChildren().add(headerNo);
                            subunitHeader = new RichOutputLabel();
                            //System.out.println("Value2");
                            //System.out.println(rsDtls.getString(3));
                            //System.out.println("Value3");
                            subunitHeader.setId("lbl" + rsDtls.getString(1));
                            subunitHeader.setShortDesc(rsDtls.getString(4));
                            //System.out.println(rsDtls.getString(1));
                            subunitHeader.setValue(rsDtls.getString(3));
                            lblPnl.getChildren().add(subunitHeader);
                            unitPnl.getChildren().add(lblPnl);
                        }

                        if (rsDtls.getString(4).equalsIgnoreCase("INPUT_TEXT")) {
                            HtmlPanelGrid pnlDtls = new HtmlPanelGrid();
                            RichInputText inputTxt = new RichInputText();
                            inputTxt.setId("txt" + rsDtls.getString(1));
                            if (rs.getInt(3) == 1 &&
                                session.getAttribute("ClientCode") != null) {
                                inputTxt.setDisabled(true);
                            } else {
                                inputTxt.setDisabled(false);
                            }
                            inputTxt.setSimple(true);
                            pnlDtls.getChildren().add(inputTxt);
                            unitPnl.getChildren().add(pnlDtls);
                        } else if (rsDtls.getString(4).equalsIgnoreCase("NUMBER")) {
                            HtmlPanelGrid pnlDtls = new HtmlPanelGrid();
                            RichInputNumberSpinbox inputTxt =
                                new RichInputNumberSpinbox();
                            inputTxt.setId("txt" + rsDtls.getString(1));
                            if (rs.getInt(3) == 1 &&
                                session.getAttribute("ClientCode") != null) {
                                inputTxt.setDisabled(true);
                            } else {
                                inputTxt.setDisabled(false);
                            }
                            inputTxt.setSimple(true);
                            pnlDtls.getChildren().add(inputTxt);
                            unitPnl.getChildren().add(pnlDtls);
                        } else if (rsDtls.getString(4).equalsIgnoreCase("TEXT")) {
                            HtmlPanelGrid pnlDtls = new HtmlPanelGrid();
                            RichInputText inputTxt = new RichInputText();
                            inputTxt.setId("txt" + rsDtls.getString(1));
                            inputTxt.setColumns(50);
                            inputTxt.setRows(4);
                            if (rs.getInt(3) == 1 &&
                                session.getAttribute("ClientCode") != null) {
                                inputTxt.setDisabled(true);
                            } else {
                                inputTxt.setDisabled(false);
                            }
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
                            if (rs.getInt(3) == 1 &&
                                session.getAttribute("ClientCode") != null) {
                                dateComp.setDisabled(true);
                            } else {
                                dateComp.setDisabled(false);
                            }
                            dateComp.setSimple(true);
                            pnlDtls.getChildren().add(dateComp);
                            unitPnl.getChildren().add(pnlDtls);
                        } else if (rsDtls.getString(4).equalsIgnoreCase("OPTION")) {
                            cstOptCnt =
                                    (OracleCallableStatement)conn.prepareCall(sysOptCount);
                            cstOptCnt.setBigDecimal(1,
                                                    rsDtls.getBigDecimal(1));

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
                            cstOptDtls.setBigDecimal(1,
                                                     rsDtls.getBigDecimal(1));
                            OracleResultSet rsOptDtls =
                                (OracleResultSet)cstOptDtls.executeQuery();
                            while (rsOptDtls.next()) {

                                RichSelectBooleanRadio chkBox =
                                    new RichSelectBooleanRadio();
                                chkBox.setId("rdo" + rsOptDtls.getString(1));
                                if (rs.getInt(3) == 1 &&
                                    session.getAttribute("ClientCode") !=
                                    null) {
                                    chkBox.setDisabled(true);
                                } else {
                                    chkBox.setDisabled(false);
                                }
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
                            cstOptCnt.setBigDecimal(1,
                                                    rsDtls.getBigDecimal(1));

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
                            cstOptDtls.setBigDecimal(1,
                                                     rsDtls.getBigDecimal(1));
                            OracleResultSet rsOptDtls =
                                (OracleResultSet)cstOptDtls.executeQuery();
                            while (rsOptDtls.next()) {

                                RichSelectBooleanCheckbox chkBox =
                                    new RichSelectBooleanCheckbox();
                                chkBox.setId("chk" + rsOptDtls.getString(1));
                                if (rs.getInt(3) == 1 &&
                                    session.getAttribute("ClientCode") !=
                                    null) {
                                    chkBox.setDisabled(true);
                                } else {
                                    chkBox.setDisabled(false);
                                }
                                chkBox.setSimple(true);
                                chkBox.setText(rsOptDtls.getString(3));
                                pnlDtls.getChildren().add(chkBox);
                            }
                            rsOptDtls.close();
                            cstOptDtls.close();


                            unitPnl.getChildren().add(pnlDtls);
                        } else if (rsDtls.getString(4).equalsIgnoreCase("TABLE")) {

                            FacesContext fc =
                                FacesContext.getCurrentInstance();
                            Application app = fc.getApplication();
                            cstOptCnt =
                                    (OracleCallableStatement)conn.prepareCall(sysOptCount);
                            cstOptCnt.setBigDecimal(1,
                                                    rsDtls.getBigDecimal(1));
                            OracleResultSet cntRs =
                                (OracleResultSet)cstOptCnt.executeQuery();
                            int no = 0;
                            while (cntRs.next()) {
                                no = cntRs.getInt(1);
                            }
                            cstOptCnt.close();
                            cntRs.close();

                            HtmlPanelGrid pnlDtls = new HtmlPanelGrid();
                            pnlDtls.setId("pg5");
                            pnlDtls.setColumns(no);
                            for (int b = 0; b < no; b++) {
                                if (b == 0) {
                                    RichCommandButton btn =
                                        new RichCommandButton();
                                    btn.setPartialSubmit(true);
                                    btn.setId("btn" + rsDtls.getBigDecimal(1));
                                    btn.setText("New Row");
                                    btn.setActionListener(app.createMethodBinding("#{ProposalBean.addTableRow}",
                                                                                  new Class[] { ActionEvent.class }));
                                    pnlDtls.getChildren().add(btn);
                                } else if (b == 1) {
                                    RichCommandButton btn =
                                        new RichCommandButton();
                                    btn.setPartialSubmit(true);
                                    btn.setText("Remove Row");
                                    btn.setActionListener(app.createMethodBinding("#{ProposalBean.removeTableRow}",
                                                                                  new Class[] { ActionEvent.class }));
                                    pnlDtls.getChildren().add(btn);
                                } else {
                                    RichOutputText btn = new RichOutputText();
                                    pnlDtls.getChildren().add(btn);
                                }
                            }
                            //pnlDtls.set
                            pnlDtls.setStyle("width:650px;");

                            cstOptDtls =
                                    (OracleCallableStatement)conn.prepareCall(sysSubunitsOptions);
                            cstOptDtls.setBigDecimal(1,
                                                     rsDtls.getBigDecimal(1));
                            OracleResultSet rsOptDtls =
                                (OracleResultSet)cstOptDtls.executeQuery();
                            int c = 0;
                            int[] cols = new int[no];
                            while (rsOptDtls.next()) {
                                RichOutputText out = new RichOutputText();
                                out.setId("col" + rsOptDtls.getString(1));
                                out.setValue(rsOptDtls.getString(3));
                                pnlDtls.getChildren().add(out);
                                cols[c] = rsOptDtls.getInt(1);
                                c++;
                            }
                            for (int v = 0; v < 2; v++) {

                                int s = 0;
                                while (s < c) {
                                    RichInputText text = new RichInputText();
                                    text.setAutoSubmit(true);
                                    text.setSimple(true);
                                    text.setValueChangeListener(app.createMethodBinding("#{ProposalBean.valueEdited}",
                                                                                        new Class[] { ValueChangeEvent.class }));
                                    int b = cols[s];
                                    text.setLabel("lbl" + b);
                                    text.setId("lbl" + b + "cell" + v + "" +
                                               s);
                                    pnlDtls.getChildren().add(text);
                                    s++;
                                }
                            }


                            rsOptDtls.close();
                            cstOptDtls.close();

                            unitPnl.getChildren().add(pnlDtls);
                        }

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
                }
                k++;
            }
            rs.close();
            cst.close();
            conn.close();
            GlobalCC.refreshUI(dtls);

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }


    public String computeQuote() {

        Object obj = productOption.getSelectedRowData();
        if (obj == null) {
            GlobalCC.errorValueNotEntered(GlobalCC.selectProductOption);
            return null;
        }

        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)obj;
        session.setAttribute("popCode", nodeBinding.getAttribute("popCode"));

        if (GlobalCC.checkNullValues(txtAge.getValue()) == null) {
            GlobalCC.errorValueNotEntered(GlobalCC.enterAgeNextBirthday);
            return null;
        }
        if (GlobalCC.checkNullValues(txtTerm.getValue()) == null) {
            GlobalCC.errorValueNotEntered(GlobalCC.enterTerm);
            return null;
        }
        if (session.getAttribute("prodCode") == null) {
            GlobalCC.errorValueNotEntered(GlobalCC.selectProduct);
            return null;
        }

        if (session.getAttribute("popCode") == null) {
            GlobalCC.errorValueNotEntered(GlobalCC.selectProductOption);
            return null;
        }
        if (GlobalCC.checkNullValues(txtPayFreq.getValue()) == null) {
            GlobalCC.errorValueNotEntered(GlobalCC.selectPayFrequency);
            return null;
        }
        if ((GlobalCC.checkNullValues(txtPremium.getValue()) == null) &&
            (GlobalCC.checkNullValues(txtSumAssured.getValue()) == null)) {
            GlobalCC.errorValueNotEntered(GlobalCC.enterPremiumSumAssured);
            return null;
        }


        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;


        String updateTelQuote =
            "begin LMS_PORTAL_PKG.UpdateTelQuot(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";

        //register out
        try {
            cst = (OracleCallableStatement)conn.prepareCall(updateTelQuote);
            if (session.getAttribute("quoteCode") == null) {
                cst.registerOutParameter(1, OracleTypes.NUMBER);
            } else {
                cst.setBigDecimal(1,
                                  (BigDecimal)session.getAttribute("quoteCode"));
            }
            cst.setString(2, "N");
            cst.setObject(3, null);
            cst.setObject(4, null);
            String anbVal = GlobalCC.checkNullValues(txtAge.getValue());
            String termVal = GlobalCC.checkNullValues(txtTerm.getValue());
            String payFreqVal =
                GlobalCC.checkNullValues(txtPayFreq.getValue());
            String premiumVal =
                GlobalCC.checkNullValues(txtPremium.getValue());
            premiumVal = (premiumVal == null) ? "0" : premiumVal;
            String sumAssuredVal =
                GlobalCC.checkNullValues(txtSumAssured.getValue());
            sumAssuredVal = (sumAssuredVal == null) ? "0" : sumAssuredVal;
            String othernamesVal = (String)session.getAttribute("Username");
            cst.setString(5, anbVal);
            cst.setString(6, payFreqVal);
            cst.setObject(7, session.getAttribute("prodCode"));
            cst.setBigDecimal(8, null);
            cst.setObject(9, session.getAttribute("popCode"));
            cst.setString(10, null);
            cst.setString(11, termVal);
            cst.setString(12, premiumVal);
            cst.setString(13, sumAssuredVal);
            cst.setString(14, null);
            cst.setString(15, othernamesVal);
            cst.setString(16, null);
            cst.setString(17, null);
            cst.setObject(18, session.getAttribute("Username"));
            cst.setBigDecimal(19, null);
            if (session.getAttribute("quoteCode") == null) {
                cst.setString(20, "I");
            } else {
                cst.setString(20, "U");
            }

            cst.setString(21, null);
            cst.setString(22, null);
            cst.setBigDecimal(23, null);
            cst.setBigDecimal(24, null);
            cst.execute();

            if (session.getAttribute("quoteCode") == null) {
                session.setAttribute("quoteCode", cst.getBigDecimal(1));
                populateCoverTypes();
            }


            String computeTelQuotePremium =
                "begin LMS_WEB_PKG_MKT.Compute_tel_quot_prem(?); end;";

            //register out

            cst =
(OracleCallableStatement)conn.prepareCall(computeTelQuotePremium);
            cst.setBigDecimal(1,
                              (BigDecimal)session.getAttribute("quoteCode"));
            cst.execute();
            computeTelQuotePremium =
                    "begin TQC_PORTAL_CURSOR.getLifeQuoteOutput(?,?,?); end;";
            cst =
(OracleCallableStatement)conn.prepareCall(computeTelQuotePremium);
            cst.setObject(1, session.getAttribute("quoteCode"));
            cst.registerOutParameter(2, OracleTypes.NUMBER);
            cst.registerOutParameter(3, OracleTypes.NUMBER);
            cst.execute();
            txtPremium.setValue(cst.getBigDecimal(2));
            txtSumAssured.setValue(cst.getBigDecimal(3));
            cst.close();
            conn.commit();
            conn.close();
            GlobalCC.refreshUI(txtPremium);
            GlobalCC.refreshUI(txtSumAssured);

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public void setTxtAge(RichInputNumberSpinbox txtAge) {
        this.txtAge = txtAge;
    }

    public RichInputNumberSpinbox getTxtAge() {
        return txtAge;
    }

    public void setTxtTerm(RichInputNumberSpinbox txtTerm) {
        this.txtTerm = txtTerm;
    }

    public RichInputNumberSpinbox getTxtTerm() {
        return txtTerm;
    }

    public void setTxtPayFreq(RichSelectOneChoice txtPayFreq) {
        this.txtPayFreq = txtPayFreq;
    }

    public RichSelectOneChoice getTxtPayFreq() {
        return txtPayFreq;
    }

    public void setTxtPremium(RichInputNumberSpinbox txtPremium) {
        this.txtPremium = txtPremium;
    }

    public RichInputNumberSpinbox getTxtPremium() {
        return txtPremium;
    }

    public void setTxtSumAssured(RichInputNumberSpinbox txtSumAssured) {
        this.txtSumAssured = txtSumAssured;
    }

    public RichInputNumberSpinbox getTxtSumAssured() {
        return txtSumAssured;
    }

    public void setProductOption(CoreTable productOption) {
        this.productOption = productOption;
    }

    public CoreTable getProductOption() {
        return productOption;
    }

    public String populateCoverTypes() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;


        String updateTelQuote =
            "begin LMS_WEB_PKG_MKT.Populate_Tel_covers(?,?,?,?); end;";

        //register out
        try {
            cst = (OracleCallableStatement)conn.prepareCall(updateTelQuote);

            cst.setBigDecimal(1,
                              (BigDecimal)session.getAttribute("quoteCode"));
            cst.setObject(2, null);
            cst.setObject(3, session.getAttribute("prodCode"));
            cst.setObject(4, session.getAttribute("popCode"));
            cst.execute();
            conn.commit();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String saveProposalDetails() {
        List<UIComponent> components = new ArrayList<UIComponent>();
        components = mainParent.getChildren();
        List<UIComponent> childComp = new ArrayList<UIComponent>();
        List<UIComponent> childChildComp = new ArrayList<UIComponent>();
        UIComponent cComp = null;
        UIComponent cCComp = null;
        int k = 0;
        int t = 0;
        int v = 0;
        String value = null;
        String type = null;
        String labelVal = null;
        OracleConnection conn = null;
        DBConnector dbConnector = new DBConnector();
        OracleCallableStatement cst = null;
        String seqQuery = "SELECT TQC_TSMSI_SEQ.NEXTVAL FROM DUAL";
        String sequence = "0";
        String insertQuery =
            "INSERT INTO TQC_SYS_MOD_SUBUNITS_INPUTS (TSMSI_CODE, TSMSI_TSMSD_CODE, TSMSI_VALUE, TSMSI_MODE_CODE,TSMSI_ROW, TSMSI_COLUMN) VALUES (?,?,?,?,?,?)";
        conn = dbConnector.getDatabaseConnection();
        try {

            cst = (OracleCallableStatement)conn.prepareCall(seqQuery);

            OracleResultSet rs = (OracleResultSet)cst.executeQuery();
            if (session.getAttribute("sequence") == null) {
                while (rs.next()) {
                    sequence = rs.getString(1);
                    session.setAttribute("sequence", sequence);
                }
                cst.close();
                rs.close();
            } else {
                sequence = (String)session.getAttribute("sequence");
            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }
        while (k < components.size()) {

            UIComponent comp = components.get(k);

            //System.out.println("Parent Family");
            //System.out.println(comp.getFamily());
            //if(comp.getChildCount())
            // System.out.println("Parent Children Count");
            //System.out.println(comp.getChildCount());

            if (comp.getFamily().contains("Panel")) {

                if (comp.getChildCount() > 0) {
                    childComp = new ArrayList<UIComponent>();
                    childComp = comp.getChildren();
                    //cComp = new UIComponent();
                    while (t < childComp.size()) {
                        //comp.getChildren()
                        cComp = childComp.get(t);

                        if (cComp.getFamily().contains("Panel")) {

                            //if(comp.getChildCount()>0){
                            childChildComp = new ArrayList<UIComponent>();
                            childChildComp = cComp.getChildren();
                            //cComp = new UIComponent();
                            while (v < childChildComp.size()) {
                                //comp.getChildren()
                                cCComp = childChildComp.get(v);

                                if (!cCComp.getFamily().contains("Output")) {


                                    //System.out.println("Children Family");
                                    //System.out.println(cCComp.getFamily());
                                    if (type.equalsIgnoreCase("INPUT_TEXT") ||
                                        type.equalsIgnoreCase("TEXT")) {
                                        RichInputText inputTxt =
                                            (RichInputText)cCComp;
                                        value =
                                                GlobalCC.checkNullValues(inputTxt.getValue());
                                        // System.out.println(GlobalCC.checkNullValues(inputTxt.getValue()));
                                    } else if (type.equalsIgnoreCase("DATE")) {
                                        RichInputDate inputTxt =
                                            (RichInputDate)cCComp;
                                        value =
                                                GlobalCC.checkNullValues(inputTxt.getValue());
                                        if (value != null) {
                                            if (value.contains(":")) {
                                                value =
                                                        GlobalCC.parseDate(value);
                                            } else {
                                                value =
                                                        GlobalCC.upDateParseDate(value);
                                            }
                                        }
                                        //System.out.println(GlobalCC.checkNullValues(inputTxt.getValue()));
                                    } else if (type.equalsIgnoreCase("NUMBER")) {
                                        RichInputNumberSpinbox inputTxt =
                                            (RichInputNumberSpinbox)cCComp;
                                        value =
                                                GlobalCC.checkNullValues(inputTxt.getValue());
                                        // System.out.println(GlobalCC.checkNullValues(inputTxt.getValue()));
                                    } else if (type.equalsIgnoreCase("OPTION")) {
                                        RichSelectBooleanRadio inputTxt =
                                            (RichSelectBooleanRadio)cCComp;
                                        if (inputTxt.isSelected()) {
                                            //System.out.println(inputTxt.getText());
                                            //System.out.println(inputTxt.getLabel());
                                            value = inputTxt.getText();
                                        }
                                    } else if (type.equalsIgnoreCase("TABLE")) {

                                        /* RichTable table = (RichTable)cCComp;
                        int s = 0;
                        String columnCount = table.getSummary();
                        columnCount = columnCount.replace("columns", "");
                        System.out.println(columnCount);
                        int cc = new Integer(columnCount);
                        System.out.println(table.getRowCount());
                        System.out.println(table.getChildCount());
                        cc = cc-1;
                         try {
                        while(s<table.getChildCount()){

                                int c = 0;
                          conn = dbConnector.getDatabaseConnection(null);
                          cst = (OracleCallableStatement)conn.prepareCall(insertQuery);
                          RichColumn column = (RichColumn)table.getChildren().get(s);
                                while(c<table.getRowCount()&&c<column.getChildCount()){

                                    System.out.println(labelVal);
                                  System.out.println("Family");
                                  System.out.println(column);
                                    //column.getChildren().get(0);
                                  HtmlInputText input = (HtmlInputText)column.getChildren().get(c);
                                 // System.out.println(s);
                                 // System.out.println(c);
                                  //  System.out.println(GlobalCC.checkNullValues(input.getValue()));

                                    if(input.getValue()!=null){
                                  cst.setString(1, sequence);
                                  cst.setString(2, labelVal);
                                  cst.setString(3, input.getValue().toString());
                                  cst.setString(4, null);
                                 cst.setInt(5, c);
                                 cst.setInt(6, s);
                          cst.execute();
                                    }
                                    c++;
                                }
                              cst.close();
                              conn.close();

                            s++;
                        }
                        } catch (Exception e) {
                        e.printStackTrace();
                        GlobalCC.EXCEPTIONREPORTING(null, e);
                        }*/

                                    } else {

                                        //System.out.println(type);
                                        //System.out.println("Children Family");
                                        //System.out.println(cCComp.getFamily());
                                    }
                                    /* System.out.println("Children ID");
                System.out.println(cCComp.getId());*/
                                    //System.out.println("Children Count");
                                    // System.out.println(cCComp.getChildCount());
                                } else {

                                    if (cCComp.getId() != null) {
                                        if (labelVal != null) {
                                            if (!type.equalsIgnoreCase("TABLE")) {
                                                try {
                                                    conn =
dbConnector.getDatabaseConnection();
                                                    cst =
(OracleCallableStatement)conn.prepareCall(insertQuery);
                                                    cst.setString(1, sequence);
                                                    cst.setString(2, labelVal);
                                                    cst.setString(3, value);
                                                    cst.setString(4, null);
                                                    cst.setString(5, null);
                                                    cst.setString(6, null);
                                                    cst.execute();
                                                    cst.close();
                                                    conn.close();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    GlobalCC.EXCEPTIONREPORTING(null,
                                                                                e);
                                                }
                                            }
                                        }
                                        //System.out.println(cCComp);
                                        if (!cCComp.toString().contains("RichOutputText")) {
                                            RichOutputLabel lbl =
                                                (RichOutputLabel)cCComp;
                                            type = lbl.getShortDesc();
                                            labelVal = cCComp.getId();
                                            labelVal =
                                                    labelVal.replace("lbl", "");
                                        }
                                    }
                                }

                                v++;

                            }
                            v = 0;
                            childChildComp = null;
                            //}
                        }

                        t++;
                    }
                    t = 0;
                    childComp = null;
                }
            }

            k++;
        }
        //if(childChildComp.size()==1){


        if (type != null) {
            if (!type.equalsIgnoreCase("TABLE")) {
                try {
                    conn = dbConnector.getDatabaseConnection();
                    cst =
(OracleCallableStatement)conn.prepareCall(insertQuery);
                    cst.setString(1, sequence);
                    cst.setString(2, labelVal);
                    cst.setString(3, value);
                    cst.setString(4, null);
                    cst.setString(5, null);
                    cst.setString(6, null);
                    cst.execute();
                    cst.close();
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        //}
        viewDetails();
        createClient();
        return null;
    }

    public String viewClientDetails() {

        List<UIComponent> components = new ArrayList<UIComponent>();
        components = mainParent.getChildren();
        List<UIComponent> childComp = new ArrayList<UIComponent>();
        List<UIComponent> childChildComp = new ArrayList<UIComponent>();
        UIComponent cComp = null;
        UIComponent cCComp = null;
        int k = 0;
        int t = 0;
        int v = 0;
        String value = null;
        String type = null;
        String labelVal = null;
        OracleConnection conn = null;
        DBConnector dbConnector = new DBConnector();
        OracleCallableStatement cst = null;
        String seqQuery = "SELECT TQC_TSMSI_SEQ.NEXTVAL FROM DUAL";
        String sequence = "0";
        conn = dbConnector.getDatabaseConnection();
        try {

            cst = (OracleCallableStatement)conn.prepareCall(seqQuery);

            OracleResultSet rs = (OracleResultSet)cst.executeQuery();
            if (session.getAttribute("sequence") == null) {
                while (rs.next()) {
                    sequence = rs.getString(1);
                    session.setAttribute("sequence", sequence);
                }
                cst.close();
                rs.close();
            } else {
                sequence = (String)session.getAttribute("sequence");
            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }
        System.out.print("components.size()" + components.size());
        while (k < components.size()) {
            System.out.print("enter here");
            UIComponent comp = components.get(k);

            //System.out.println("Parent Family");
            //System.out.println(comp.getFamily());
            //if(comp.getChildCount())
            // System.out.println("Parent Children Count");
            //System.out.println(comp.getChildCount());

            if (comp.getFamily().contains("Panel")) {
                System.out.print("enter here TOO");
                if (comp.getChildCount() > 0) {
                    childComp = new ArrayList<UIComponent>();
                    childComp = comp.getChildren();
                    //cComp = new UIComponent();
                    while (t < childComp.size()) {
                        //comp.getChildren()
                        cComp = childComp.get(t);

                        if (cComp.getFamily().contains("Panel")) {
                            System.out.print("enter here Panel");
                            //if(comp.getChildCount()>0){
                            childChildComp = new ArrayList<UIComponent>();
                            childChildComp = cComp.getChildren();
                            //cComp = new UIComponent();
                            while (v < childChildComp.size()) {
                                //comp.getChildren()
                                cCComp = childChildComp.get(v);

                                if (!cCComp.getFamily().contains("Output")) {


                                    System.out.println("Children Family");
                                    System.out.println(cCComp.getFamily());
                                    if (type.equalsIgnoreCase("INPUT_TEXT") ||
                                        type.equalsIgnoreCase("TEXT")) {
                                        RichInputText inputTxt =
                                            (RichInputText)cCComp;
                                        value =
                                                GlobalCC.checkNullValues(inputTxt.getValue());
                                        System.out.println(GlobalCC.checkNullValues(inputTxt.getValue()));
                                    } else if (type.equalsIgnoreCase("DATE")) {
                                        RichInputDate inputTxt =
                                            (RichInputDate)cCComp;
                                        value =
                                                GlobalCC.checkNullValues(inputTxt.getValue());
                                        if (value != null) {
                                            if (value.contains(":")) {
                                                value =
                                                        GlobalCC.parseDate(value);
                                            } else {
                                                value =
                                                        GlobalCC.upDateParseDate(value);
                                            }
                                        }
                                        System.out.println(GlobalCC.checkNullValues(inputTxt.getValue()));
                                    } else if (type.equalsIgnoreCase("NUMBER")) {
                                        RichInputNumberSpinbox inputTxt =
                                            (RichInputNumberSpinbox)cCComp;
                                        value =
                                                GlobalCC.checkNullValues(inputTxt.getValue());
                                        System.out.println(GlobalCC.checkNullValues(inputTxt.getValue()));
                                    } else if (type.equalsIgnoreCase("OPTION")) {
                                        RichSelectBooleanRadio inputTxt =
                                            (RichSelectBooleanRadio)cCComp;
                                        if (inputTxt.isSelected()) {
                                            System.out.println(inputTxt.getText());
                                            System.out.println(inputTxt.getLabel());
                                            value = inputTxt.getText();
                                        }
                                    } else if (type.equalsIgnoreCase("TABLE")) {

                                        /* RichTable table = (RichTable)cCComp;
                      int s = 0;
                      String columnCount = table.getSummary();
                      columnCount = columnCount.replace("columns", "");
                      System.out.println(columnCount);
                      int cc = new Integer(columnCount);
                      System.out.println(table.getRowCount());
                      System.out.println(table.getChildCount());
                      cc = cc-1;
                       try {
                      while(s<table.getChildCount()){

                              int c = 0;
                        conn = dbConnector.getDatabaseConnection(null);
                        cst = (OracleCallableStatement)conn.prepareCall(insertQuery);
                        RichColumn column = (RichColumn)table.getChildren().get(s);
                              while(c<table.getRowCount()&&c<column.getChildCount()){

                                  System.out.println(labelVal);
                                System.out.println("Family");
                                System.out.println(column);
                                  //column.getChildren().get(0);
                                HtmlInputText input = (HtmlInputText)column.getChildren().get(c);
                               // System.out.println(s);
                               // System.out.println(c);
                                //  System.out.println(GlobalCC.checkNullValues(input.getValue()));

                                  if(input.getValue()!=null){
                                cst.setString(1, sequence);
                                cst.setString(2, labelVal);
                                cst.setString(3, input.getValue().toString());
                                cst.setString(4, null);
                               cst.setInt(5, c);
                               cst.setInt(6, s);
                        cst.execute();
                                  }
                                  c++;
                              }
                            cst.close();
                            conn.close();

                          s++;
                      }
                      } catch (Exception e) {
                      e.printStackTrace();
                      GlobalCC.EXCEPTIONREPORTING(null, e);
                      }*/

                                    } else {

                                        //System.out.println(type);
                                        //System.out.println("Children Family");
                                        //System.out.println(cCComp.getFamily());
                                    }
                                    /* System.out.println("Children ID");
              System.out.println(cCComp.getId());*/
                                    //System.out.println("Children Count");
                                    // System.out.println(cCComp.getChildCount());
                                } else {

                                    if (cCComp.getId() != null) {

                                        //System.out.println(cCComp);
                                        if (!cCComp.toString().contains("RichOutputText")) {
                                            RichOutputLabel lbl =
                                                (RichOutputLabel)cCComp;
                                            type = lbl.getShortDesc();
                                            labelVal = cCComp.getId();
                                            labelVal =
                                                    labelVal.replace("lbl", "");
                                        }
                                    }
                                }

                                v++;

                            }
                            v = 0;
                            childChildComp = null;
                            //}
                        }

                        t++;
                    }
                    t = 0;
                    childComp = null;
                }
            }

            k++;
        }
        //if(childChildComp.size()==1){
        //}
        viewClientDtls();
        //  createClient();
        return null;
    }

    public String viewDetails() {

        mainParent.getChildren().clear();
        mainParent.setColumns(1);


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;
        OracleCallableStatement cstDtls = null;
        OracleCallableStatement cstOptDtls = null;
        OracleCallableStatement cstChldDtls = null;
        OracleCallableStatement cstOptCnt = null;
        OracleCallableStatement cstValue = null;
        String cmpValue = null;
        OracleCallableStatement cstValue2 = null;
        String cmpValue2 = null;
        String sysSubunits =
            "SELECT TSMS_DESC,TSMS_CODE,TSMS_ORDER FROM TQC_SYS_MOD_SUBUNITS,TQC_SYSTEM_MODULES WHERE TSMS_TSM_CODE = TSM_CODE AND TSM_SHT_DESC = 'PROPOSAL' AND TSMS_PROD_CODE = " +
            session.getAttribute("prodCode") + " ORDER BY TSMS_ORDER";
        String sysSubunitsDtls =
            "SELECT TSMSD_CODE,TSMSD_ORDER,TSMSD_PROMPT,TSMSD_TYPE,TSMSD_MORE_DTLS_APPL,TSMSD_MORE_DTLS,TSMSD_ROOT,TSMSD_MORE_DTLS_REQUIRED FROM TQC_SYS_MOD_SUBUNITS_DETAILS WHERE TSMSD_TSMS_CODE = ? AND TSMSD_PARENT IS NULL ORDER BY TSMSD_ORDER";

        //String sysSubunitsDtlsChild =
        //    "SELECT TSMSD_CODE,TSMSD_ORDER,TSMSD_PROMPT,TSMSD_TYPE,TSMSD_MORE_DTLS_APPL,TSMSD_MORE_DTLS,TSMSD_ROOT FROM TQC_SYS_MOD_SUBUNITS_DETAILS WHERE TSMSD_PARENT = ? ORDER BY TSMSD_ORDER";


        String sysSubunitsOptions =
            "SELECT TSSO_CODE, TSSO_OPTION_NAME, TSSO_OPTION_DESC FROM TQC_SYS_SUBUNITS_OPTIONS WHERE TSSO_TSMSD_CODE = ? ORDER BY TSSO_ORDER";

        // String sysAppOptions =
        //     "SELECT COUNT(TSSO_CODE), TSSO_OPTION_NAME FROM TQC_SYS_SUBUNITS_OPTIONS WHERE TSSO_TSMSD_CODE = ? GROUP BY TSSO_OPTION_NAME";


        String sysOptCount =
            "SELECT COUNT(TSSO_CODE), TSSO_OPTION_NAME FROM TQC_SYS_SUBUNITS_OPTIONS WHERE TSSO_TSMSD_CODE = ?GROUP BY TSSO_OPTION_NAME";

        String sysOptInpCount =
            "SELECT COUNT(TSMSI_TSMSD_CODE) FROM TQC_SYS_MOD_SUBUNITS_INPUTS WHERE TSMSI_TSMSD_CODE = ? AND TSMSI_CODE = ?";
        conn = dbConnector.getDatabaseConnection();
        try {

            cst = (OracleCallableStatement)conn.prepareCall(sysSubunits);

            OracleResultSet rs = (OracleResultSet)cst.executeQuery();
            mainParent.getChildren().clear();
            int k = 1;

            while (rs.next()) {
                if (rs.getInt(3) == 1 &&
                    session.getAttribute("ClientCode") == null) {
                    HtmlPanelGrid headerPnl = new HtmlPanelGrid();
                    headerPnl.setColumns(2);
                    headerPnl.setStyleClass("text-align:left;");
                    RichOutputLabel headerNo = new RichOutputLabel();
                    headerNo.setValue(k + ".");
                    headerNo.setInlineStyle("font-weight:bold;");
                    headerPnl.getChildren().add(headerNo);
                    RichOutputLabel subunitHeader = new RichOutputLabel();
                    subunitHeader.setValue(rs.getString(1));
                    if (rs.getInt(3) == 1 &&
                        session.getAttribute("ClientCode") != null) {
                        subunitHeader.setValue(rs.getString(1) +
                                               " - (Already Provided) ");
                        subunitHeader.setInlineStyle("font-weight:bold; color:Blue;");
                    } else {
                        subunitHeader.setInlineStyle("font-weight:bold;");
                        subunitHeader.setValue(rs.getString(1));
                    }
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
                        System.out.println("This is sequence" +
                                           session.getAttribute("sequence"));
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
                            // System.out.println("Value1");
                            // System.out.println(rsDtls.getString(2));
                            headerNo.setValue(rsDtls.getString(2));
                            unitPnl.getChildren().add(headerNo);
                            subunitHeader = new RichOutputLabel();
                            //System.out.println("Value2");
                            //System.out.println(rsDtls.getString(3));
                            //System.out.println("Value3");
                            subunitHeader.setId("lbl" + rsDtls.getString(1));
                            subunitHeader.setShortDesc(rsDtls.getString(4));
                            //System.out.println(rsDtls.getString(1));
                            subunitHeader.setValue(rsDtls.getString(3));
                            lblPnl.getChildren().add(subunitHeader);
                            unitPnl.getChildren().add(lblPnl);
                        }
                        String sequence = "0";

                        sequence = (String)session.getAttribute("sequence");
                        System.out.println("This is sequence" +
                                           session.getAttribute("sequence"));
                        cstValue =
                                (OracleCallableStatement)conn.prepareCall("SELECT TSMSI_VALUE FROM TQC_SYS_MOD_SUBUNITS_INPUTS WHERE TSMSI_CODE = " +
                                                                          sequence +
                                                                          " AND TSMSI_TSMSD_CODE = " +
                                                                          rsDtls.getString(1) +
                                                                          " ");

                        OracleResultSet rsVal =
                            (OracleResultSet)cstValue.executeQuery();
                        int v = 0;
                        while (rsVal.next()) {
                            cmpValue = rsVal.getString(1);
                            v++;
                        }
                        if (v == 0) {
                            cmpValue = null;
                        }
                        rsVal.close();
                        cstValue.close();

                        if (rsDtls.getString(4).equalsIgnoreCase("TABLE")) {
                            cstOptCnt =
                                    (OracleCallableStatement)conn.prepareCall(sysOptCount);
                            cstOptCnt.setBigDecimal(1,
                                                    rsDtls.getBigDecimal(1));

                            OracleResultSet cntRs =
                                (OracleResultSet)cstOptCnt.executeQuery();
                            int no = 0;
                            while (cntRs.next()) {
                                no = cntRs.getInt(1);
                            }
                            cstOptCnt.close();
                            cntRs.close();

                            /* Rows */
                            cstOptCnt =
                                    (OracleCallableStatement)conn.prepareCall(sysOptInpCount);
                            cstOptCnt.setBigDecimal(1,
                                                    rsDtls.getBigDecimal(1));
                            cstOptCnt.setString(2, sequence);
                            cntRs = (OracleResultSet)cstOptCnt.executeQuery();
                            int rows = 0;
                            while (cntRs.next()) {
                                rows = cntRs.getInt(1);
                                rows = rows / 2;
                            }
                            cstOptCnt.close();
                            cntRs.close();

                            HtmlPanelGrid pnlDtls = new HtmlPanelGrid();
                            pnlDtls.setId("pg5");
                            pnlDtls.setColumns(no);
                            for (int b = 0; b < no; b++) {
                                if (b == 0) {
                                    RichCommandButton btn =
                                        new RichCommandButton();
                                    btn.setPartialSubmit(true);
                                    btn.setId("btn" + rsDtls.getBigDecimal(1));
                                    btn.setText("New Row");
                                    // btn.setActionListener(app.createMethodBinding("#{ClaimsBean.addTableRow}",
                                    //                                              new Class[] { ActionEvent.class }));
                                    btn.setVisible(false);
                                    pnlDtls.getChildren().add(btn);
                                } else if (b == 1) {
                                    RichCommandButton btn =
                                        new RichCommandButton();
                                    btn.setPartialSubmit(true);
                                    btn.setText("Remove Row");
                                    // btn.setActionListener(app.createMethodBinding("#{ClaimsBean.removeTableRow}",
                                    //                                               new Class[] { ActionEvent.class }));
                                    btn.setVisible(false);
                                    pnlDtls.getChildren().add(btn);
                                } else {
                                    RichOutputText btn = new RichOutputText();
                                    pnlDtls.getChildren().add(btn);
                                }
                            }
                            //pnlDtls.set
                            pnlDtls.setStyle("width:650px;");

                            cstOptDtls =
                                    (OracleCallableStatement)conn.prepareCall(sysSubunitsOptions);
                            cstOptDtls.setBigDecimal(1,
                                                     rsDtls.getBigDecimal(1));
                            OracleResultSet rsOptDtls =
                                (OracleResultSet)cstOptDtls.executeQuery();
                            int c = 0;
                            int[] cols = new int[no];
                            while (rsOptDtls.next()) {
                                RichOutputText out = new RichOutputText();
                                out.setId("col" + rsOptDtls.getString(1));
                                out.setValue(rsOptDtls.getString(3));
                                pnlDtls.getChildren().add(out);
                                cols[c] = rsOptDtls.getInt(1);
                                c++;
                            }
                            for (int h = 0; h < rows; h++) {

                                int s = 0;
                                while (s < c) {
                                    cstValue2 =
                                            (OracleCallableStatement)conn.prepareCall("SELECT TSMSI_VALUE FROM TQC_SYS_MOD_SUBUNITS_INPUTS WHERE TSMSI_CODE = " +
                                                                                      sequence +
                                                                                      " AND TSMSI_TSMSD_CODE = " +
                                                                                      rsDtls.getString(1) +
                                                                                      " AND TSMSI_ROW = " +
                                                                                      h +
                                                                                      " AND TSMSI_COLUMN = " +
                                                                                      s +
                                                                                      " ");

                                    OracleResultSet rsVal2 =
                                        (OracleResultSet)cstValue2.executeQuery();
                                    int v2 = 0;
                                    while (rsVal2.next()) {
                                        cmpValue2 = rsVal2.getString(1);
                                        v2++;
                                    }
                                    if (v2 == 0) {
                                        cmpValue2 = null;
                                    }
                                    rsVal2.close();
                                    cstValue2.close();

                                    RichInputText text = new RichInputText();
                                    text.setAutoSubmit(true);
                                    text.setValue(cmpValue2);
                                    text.setSimple(true);
                                    //text.setValueChangeListener(app.createMethodBinding("#{ClaimsBean.valueEdited}",
                                    //                                                    new Class[] { ValueChangeEvent.class }));
                                    int b = cols[s];
                                    text.setLabel("lbl" + b);
                                    text.setId("lbl" + b + "cell" + h + "" +
                                               s);
                                    text.setReadOnly(true);
                                    pnlDtls.getChildren().add(text);
                                    s++;
                                }
                            }

                            rsOptDtls.close();
                            cstOptDtls.close();
                            unitPnl.getChildren().add(pnlDtls);

                        } else {

                            HtmlPanelGrid pnlDtls = new HtmlPanelGrid();
                            RichOutputLabel outLabel = new RichOutputLabel();
                            outLabel.setValue(cmpValue);
                            pnlDtls.getChildren().add(outLabel);
                            unitPnl.getChildren().add(pnlDtls);
                        }

                        // System.out.println("More Details");
                        //  System.out.println(rsDtls.getString(8));
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


                }
                k++;
            }
            rs.close();
            cst.close();
            conn.close();
            GlobalCC.refreshUI(dtls);

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String viewClientDtls() {

        mainParent.getChildren().clear();
        mainParent.setColumns(1);


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;
        OracleCallableStatement cstDtls = null;
        OracleCallableStatement cstOptDtls = null;
        OracleCallableStatement cstChldDtls = null;
        OracleCallableStatement cstOptCnt = null;
        OracleCallableStatement cstValue = null;
        String cmpValue = null;
        OracleCallableStatement cstValue2 = null;
        String cmpValue2 = null;
        String sysSubunits =
            "SELECT TSMS_DESC,TSMS_CODE,TSMS_ORDER FROM TQC_SYS_MOD_SUBUNITS,TQC_SYSTEM_MODULES WHERE TSMS_TSM_CODE = TSM_CODE AND TSM_SHT_DESC = 'PROPOSAL' AND TSMS_PROD_CODE = " +
            session.getAttribute("prodCode") + " ORDER BY TSMS_ORDER";
        String sysSubunitsDtls =
            "SELECT TSMSD_CODE,TSMSD_ORDER,TSMSD_PROMPT,TSMSD_TYPE,TSMSD_MORE_DTLS_APPL,TSMSD_MORE_DTLS,TSMSD_ROOT,TSMSD_MORE_DTLS_REQUIRED FROM TQC_SYS_MOD_SUBUNITS_DETAILS WHERE TSMSD_TSMS_CODE = ? AND TSMSD_PARENT IS NULL ORDER BY TSMSD_ORDER";

        //String sysSubunitsDtlsChild =
        //    "SELECT TSMSD_CODE,TSMSD_ORDER,TSMSD_PROMPT,TSMSD_TYPE,TSMSD_MORE_DTLS_APPL,TSMSD_MORE_DTLS,TSMSD_ROOT FROM TQC_SYS_MOD_SUBUNITS_DETAILS WHERE TSMSD_PARENT = ? ORDER BY TSMSD_ORDER";


        String sysSubunitsOptions =
            "SELECT TSSO_CODE, TSSO_OPTION_NAME, TSSO_OPTION_DESC FROM TQC_SYS_SUBUNITS_OPTIONS WHERE TSSO_TSMSD_CODE = ? ORDER BY TSSO_ORDER";

        // String sysAppOptions =
        //     "SELECT COUNT(TSSO_CODE), TSSO_OPTION_NAME FROM TQC_SYS_SUBUNITS_OPTIONS WHERE TSSO_TSMSD_CODE = ? GROUP BY TSSO_OPTION_NAME";


        String sysOptCount =
            "SELECT COUNT(TSSO_CODE), TSSO_OPTION_NAME FROM TQC_SYS_SUBUNITS_OPTIONS WHERE TSSO_TSMSD_CODE = ?GROUP BY TSSO_OPTION_NAME";

        String sysOptInpCount =
            "SELECT COUNT(TSMSI_TSMSD_CODE) FROM TQC_SYS_MOD_SUBUNITS_INPUTS WHERE TSMSI_TSMSD_CODE = ? AND TSMSI_CODE = ?";
        conn = dbConnector.getDatabaseConnection();
        try {

            cst = (OracleCallableStatement)conn.prepareCall(sysSubunits);

            OracleResultSet rs = (OracleResultSet)cst.executeQuery();
            mainParent.getChildren().clear();
            int k = 1;

            while (rs.next()) {
                if (rs.getInt(3) == 1 &&
                    session.getAttribute("ClientCode") == null) {
                    HtmlPanelGrid headerPnl = new HtmlPanelGrid();
                    headerPnl.setColumns(2);
                    headerPnl.setStyleClass("text-align:left;");
                    RichOutputLabel headerNo = new RichOutputLabel();
                    headerNo.setValue(k + ".");
                    headerNo.setInlineStyle("font-weight:bold;");
                    headerPnl.getChildren().add(headerNo);
                    RichOutputLabel subunitHeader = new RichOutputLabel();
                    subunitHeader.setValue(rs.getString(1));
                    if (rs.getInt(3) == 1 &&
                        session.getAttribute("ClientCode") != null) {
                        subunitHeader.setValue(rs.getString(1) +
                                               " - (Already Provided) ");
                        subunitHeader.setInlineStyle("font-weight:bold; color:Blue;");
                    } else {
                        subunitHeader.setInlineStyle("font-weight:bold;");
                        subunitHeader.setValue(rs.getString(1));
                    }
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
                        System.out.println("Executed");
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
                            // System.out.println("Value1");
                            // System.out.println(rsDtls.getString(2));
                            headerNo.setValue(rsDtls.getString(2));
                            unitPnl.getChildren().add(headerNo);
                            subunitHeader = new RichOutputLabel();
                            //System.out.println("Value2");
                            //System.out.println(rsDtls.getString(3));
                            //System.out.println("Value3");
                            subunitHeader.setId("lbl" + rsDtls.getString(1));
                            subunitHeader.setShortDesc(rsDtls.getString(4));
                            //System.out.println(rsDtls.getString(1));
                            subunitHeader.setValue(rsDtls.getString(3));
                            lblPnl.getChildren().add(subunitHeader);
                            unitPnl.getChildren().add(lblPnl);
                        }
                        System.out.print("sequence" +
                                         session.getAttribute("sequence"));
                        String sequence = "0";
                        sequence = (String)session.getAttribute("sequence");
                        cstValue =
                                (OracleCallableStatement)conn.prepareCall("SELECT TSMSI_VALUE FROM TQC_SYS_MOD_SUBUNITS_INPUTS WHERE TSMSI_CODE = " +
                                                                          1381 +
                                                                          " AND TSMSI_TSMSD_CODE = " +
                                                                          rsDtls.getString(1) +
                                                                          " ");

                        OracleResultSet rsVal =
                            (OracleResultSet)cstValue.executeQuery();
                        int v = 0;
                        while (rsVal.next()) {
                            cmpValue = rsVal.getString(1);
                            v++;
                        }
                        if (v == 0) {
                            cmpValue = null;
                        }
                        rsVal.close();
                        cstValue.close();
                        if (rsDtls.getString(4).equalsIgnoreCase("TABLE")) {
                            cstOptCnt =
                                    (OracleCallableStatement)conn.prepareCall(sysOptCount);
                            cstOptCnt.setBigDecimal(1,
                                                    rsDtls.getBigDecimal(1));

                            OracleResultSet cntRs =
                                (OracleResultSet)cstOptCnt.executeQuery();
                            int no = 0;
                            while (cntRs.next()) {
                                no = cntRs.getInt(1);
                            }
                            cstOptCnt.close();
                            cntRs.close();

                            /* Rows */
                            cstOptCnt =
                                    (OracleCallableStatement)conn.prepareCall(sysOptInpCount);
                            cstOptCnt.setBigDecimal(1,
                                                    rsDtls.getBigDecimal(1));
                            cstOptCnt.setString(2, sequence);
                            cntRs = (OracleResultSet)cstOptCnt.executeQuery();
                            int rows = 0;
                            while (cntRs.next()) {
                                rows = cntRs.getInt(1);
                                rows = rows / 2;
                            }
                            cstOptCnt.close();
                            cntRs.close();

                            HtmlPanelGrid pnlDtls = new HtmlPanelGrid();
                            pnlDtls.setId("pg5");
                            pnlDtls.setColumns(no);
                            for (int b = 0; b < no; b++) {
                                if (b == 0) {
                                    RichCommandButton btn =
                                        new RichCommandButton();
                                    btn.setPartialSubmit(true);
                                    btn.setId("btn" + rsDtls.getBigDecimal(1));
                                    btn.setText("New Row");
                                    // btn.setActionListener(app.createMethodBinding("#{ClaimsBean.addTableRow}",
                                    //                                              new Class[] { ActionEvent.class }));
                                    btn.setVisible(false);
                                    pnlDtls.getChildren().add(btn);
                                } else if (b == 1) {
                                    RichCommandButton btn =
                                        new RichCommandButton();
                                    btn.setPartialSubmit(true);
                                    btn.setText("Remove Row");
                                    // btn.setActionListener(app.createMethodBinding("#{ClaimsBean.removeTableRow}",
                                    //                                               new Class[] { ActionEvent.class }));
                                    btn.setVisible(false);
                                    pnlDtls.getChildren().add(btn);
                                } else {
                                    RichOutputText btn = new RichOutputText();
                                    pnlDtls.getChildren().add(btn);
                                }
                            }
                            //pnlDtls.set
                            pnlDtls.setStyle("width:650px;");

                            cstOptDtls =
                                    (OracleCallableStatement)conn.prepareCall(sysSubunitsOptions);
                            cstOptDtls.setBigDecimal(1,
                                                     rsDtls.getBigDecimal(1));
                            OracleResultSet rsOptDtls =
                                (OracleResultSet)cstOptDtls.executeQuery();
                            int c = 0;
                            int[] cols = new int[no];
                            while (rsOptDtls.next()) {
                                RichOutputText out = new RichOutputText();
                                out.setId("col" + rsOptDtls.getString(1));
                                out.setValue(rsOptDtls.getString(3));
                                pnlDtls.getChildren().add(out);
                                cols[c] = rsOptDtls.getInt(1);
                                c++;
                            }
                            for (int h = 0; h < rows; h++) {

                                int s = 0;
                                while (s < c) {
                                    cstValue2 =
                                            (OracleCallableStatement)conn.prepareCall("SELECT TSMSI_VALUE FROM TQC_SYS_MOD_SUBUNITS_INPUTS WHERE TSMSI_CODE = " +
                                                                                      sequence +
                                                                                      " AND TSMSI_TSMSD_CODE = " +
                                                                                      rsDtls.getString(1) +
                                                                                      " AND TSMSI_ROW = " +
                                                                                      h +
                                                                                      " AND TSMSI_COLUMN = " +
                                                                                      s +
                                                                                      " ");

                                    OracleResultSet rsVal2 =
                                        (OracleResultSet)cstValue2.executeQuery();
                                    int v2 = 0;
                                    while (rsVal2.next()) {
                                        cmpValue2 = rsVal2.getString(1);
                                        v2++;
                                    }
                                    if (v2 == 0) {
                                        cmpValue2 = null;
                                    }
                                    rsVal2.close();
                                    cstValue2.close();

                                    RichInputText text = new RichInputText();
                                    text.setAutoSubmit(true);
                                    text.setValue(cmpValue2);
                                    text.setSimple(true);
                                    //text.setValueChangeListener(app.createMethodBinding("#{ClaimsBean.valueEdited}",
                                    //                                                    new Class[] { ValueChangeEvent.class }));
                                    int b = cols[s];
                                    text.setLabel("lbl" + b);
                                    text.setId("lbl" + b + "cell" + h + "" +
                                               s);
                                    text.setReadOnly(true);
                                    pnlDtls.getChildren().add(text);
                                    s++;
                                }
                            }

                            rsOptDtls.close();
                            cstOptDtls.close();
                            unitPnl.getChildren().add(pnlDtls);

                        } else {

                            HtmlPanelGrid pnlDtls = new HtmlPanelGrid();
                            RichOutputLabel outLabel = new RichOutputLabel();
                            outLabel.setValue(cmpValue);
                            pnlDtls.getChildren().add(outLabel);
                            unitPnl.getChildren().add(pnlDtls);
                        }

                        // System.out.println("More Details");
                        //  System.out.println(rsDtls.getString(8));
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


                }
                k++;
            }
            rs.close();
            cst.close();
            conn.close();
            GlobalCC.refreshUI(previewButtonPop);
            GlobalCC.refreshUI(prevDialogue);
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "demoTemplate:p53234" + "').show(hints);");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String viewDetailsPrev() {

        mainParent.getChildren().clear();
        mainParent.setColumns(1);


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;
        OracleCallableStatement cstDtls = null;
        OracleCallableStatement cstOptDtls = null;
        OracleCallableStatement cstChldDtls = null;
        OracleCallableStatement cstOptCnt = null;
        OracleCallableStatement cstValue = null;
        String cmpValue = null;
        OracleCallableStatement cstValue2 = null;
        String cmpValue2 = null;
        System.out.println("prodCode" + session.getAttribute("prodCode"));
        String sysSubunits =
            "SELECT TSMS_DESC,TSMS_CODE,TSMS_ORDER FROM TQC_SYS_MOD_SUBUNITS,TQC_SYSTEM_MODULES WHERE TSMS_TSM_CODE = TSM_CODE AND TSM_SHT_DESC = 'PROPOSAL' AND TSMS_PROD_CODE = " +
            session.getAttribute("prodCode") + " ORDER BY TSMS_ORDER";
        String sysSubunitsDtls =
            "SELECT TSMSD_CODE,TSMSD_ORDER,TSMSD_PROMPT,TSMSD_TYPE,TSMSD_MORE_DTLS_APPL,TSMSD_MORE_DTLS,TSMSD_ROOT,TSMSD_MORE_DTLS_REQUIRED FROM TQC_SYS_MOD_SUBUNITS_DETAILS WHERE TSMSD_TSMS_CODE = ? AND TSMSD_PARENT IS NULL ORDER BY TSMSD_ORDER";

        //String sysSubunitsDtlsChild =
        //    "SELECT TSMSD_CODE,TSMSD_ORDER,TSMSD_PROMPT,TSMSD_TYPE,TSMSD_MORE_DTLS_APPL,TSMSD_MORE_DTLS,TSMSD_ROOT FROM TQC_SYS_MOD_SUBUNITS_DETAILS WHERE TSMSD_PARENT = ? ORDER BY TSMSD_ORDER";


        String sysSubunitsOptions =
            "SELECT TSSO_CODE, TSSO_OPTION_NAME, TSSO_OPTION_DESC FROM TQC_SYS_SUBUNITS_OPTIONS WHERE TSSO_TSMSD_CODE = ? ORDER BY TSSO_ORDER";

        // String sysAppOptions =
        //     "SELECT COUNT(TSSO_CODE), TSSO_OPTION_NAME FROM TQC_SYS_SUBUNITS_OPTIONS WHERE TSSO_TSMSD_CODE = ? GROUP BY TSSO_OPTION_NAME";


        String sysOptCount =
            "SELECT COUNT(TSSO_CODE), TSSO_OPTION_NAME FROM TQC_SYS_SUBUNITS_OPTIONS WHERE TSSO_TSMSD_CODE = ?GROUP BY TSSO_OPTION_NAME";

        String sysOptInpCount =
            "SELECT COUNT(TSMSI_TSMSD_CODE) FROM TQC_SYS_MOD_SUBUNITS_INPUTS WHERE TSMSI_TSMSD_CODE = ? AND TSMSI_CODE = ?";
        conn = dbConnector.getDatabaseConnection();
        try {

            cst = (OracleCallableStatement)conn.prepareCall(sysSubunits);

            OracleResultSet rs = (OracleResultSet)cst.executeQuery();
            mainParent.getChildren().clear();
            int k = 1;
            System.out.println("ClientCode" +
                               session.getAttribute("ClientCode"));
            while (rs.next()) {
                if (rs.getInt(3) == 1 &&
                    session.getAttribute("ClientCode") == null) {
                    HtmlPanelGrid headerPnl = new HtmlPanelGrid();
                    headerPnl.setColumns(2);
                    headerPnl.setStyleClass("text-align:left;");
                    RichOutputLabel headerNo = new RichOutputLabel();
                    headerNo.setValue(k + ".");
                    headerNo.setInlineStyle("font-weight:bold;");
                    headerPnl.getChildren().add(headerNo);
                    RichOutputLabel subunitHeader = new RichOutputLabel();
                    subunitHeader.setValue(rs.getString(1));
                    if (rs.getInt(3) == 1 &&
                        session.getAttribute("ClientCode") != null) {
                        subunitHeader.setValue(rs.getString(1) +
                                               " - (Already Provided) ");
                        subunitHeader.setInlineStyle("font-weight:bold; color:Blue;");
                    } else {
                        subunitHeader.setInlineStyle("font-weight:bold;");
                        subunitHeader.setValue(rs.getString(1));
                    }
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
                        System.out.println("rsDtls.getString(5)" +
                                           rsDtls.getString(5));
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
                            // System.out.println("Value1");
                            // System.out.println(rsDtls.getString(2));
                            headerNo.setValue(rsDtls.getString(2));
                            unitPnl.getChildren().add(headerNo);
                            subunitHeader = new RichOutputLabel();
                            //System.out.println("Value2");
                            //System.out.println(rsDtls.getString(3));
                            //System.out.println("Value3");
                            subunitHeader.setId("lbl" + rsDtls.getString(1));
                            subunitHeader.setShortDesc(rsDtls.getString(4));
                            //System.out.println(rsDtls.getString(1));
                            subunitHeader.setValue(rsDtls.getString(3));
                            lblPnl.getChildren().add(subunitHeader);
                            unitPnl.getChildren().add(lblPnl);
                        }
                        String sequence = "0";
                        sequence = (String)session.getAttribute("sequence");
                        cstValue =
                                (OracleCallableStatement)conn.prepareCall("SELECT TSMSI_VALUE FROM TQC_SYS_MOD_SUBUNITS_INPUTS WHERE TSMSI_CODE = " +
                                                                          sequence +
                                                                          " AND TSMSI_TSMSD_CODE = " +
                                                                          rsDtls.getString(1) +
                                                                          " ");

                        OracleResultSet rsVal =
                            (OracleResultSet)cstValue.executeQuery();
                        int v = 0;
                        while (rsVal.next()) {
                            cmpValue = rsVal.getString(1);
                            v++;
                        }
                        if (v == 0) {
                            cmpValue = null;
                        }
                        rsVal.close();
                        cstValue.close();

                        if (rsDtls.getString(4).equalsIgnoreCase("TABLE")) {
                            cstOptCnt =
                                    (OracleCallableStatement)conn.prepareCall(sysOptCount);
                            cstOptCnt.setBigDecimal(1,
                                                    rsDtls.getBigDecimal(1));

                            OracleResultSet cntRs =
                                (OracleResultSet)cstOptCnt.executeQuery();
                            int no = 0;
                            while (cntRs.next()) {
                                no = cntRs.getInt(1);
                            }
                            cstOptCnt.close();
                            cntRs.close();

                            /* Rows */
                            cstOptCnt =
                                    (OracleCallableStatement)conn.prepareCall(sysOptInpCount);
                            cstOptCnt.setBigDecimal(1,
                                                    rsDtls.getBigDecimal(1));
                            cstOptCnt.setString(2, sequence);
                            cntRs = (OracleResultSet)cstOptCnt.executeQuery();
                            int rows = 0;
                            while (cntRs.next()) {
                                rows = cntRs.getInt(1);
                                rows = rows / 2;
                            }
                            cstOptCnt.close();
                            cntRs.close();

                            HtmlPanelGrid pnlDtls = new HtmlPanelGrid();
                            pnlDtls.setId("pg5");
                            pnlDtls.setColumns(no);
                            for (int b = 0; b < no; b++) {
                                if (b == 0) {
                                    RichCommandButton btn =
                                        new RichCommandButton();
                                    btn.setPartialSubmit(true);
                                    btn.setId("btn" + rsDtls.getBigDecimal(1));
                                    btn.setText("New Row");
                                    // btn.setActionListener(app.createMethodBinding("#{ClaimsBean.addTableRow}",
                                    //                                              new Class[] { ActionEvent.class }));
                                    btn.setVisible(false);
                                    pnlDtls.getChildren().add(btn);
                                } else if (b == 1) {
                                    RichCommandButton btn =
                                        new RichCommandButton();
                                    btn.setPartialSubmit(true);
                                    btn.setText("Remove Row");
                                    // btn.setActionListener(app.createMethodBinding("#{ClaimsBean.removeTableRow}",
                                    //                                               new Class[] { ActionEvent.class }));
                                    btn.setVisible(false);
                                    pnlDtls.getChildren().add(btn);
                                } else {
                                    RichOutputText btn = new RichOutputText();
                                    pnlDtls.getChildren().add(btn);
                                }
                            }
                            //pnlDtls.set
                            pnlDtls.setStyle("width:650px;");

                            cstOptDtls =
                                    (OracleCallableStatement)conn.prepareCall(sysSubunitsOptions);
                            cstOptDtls.setBigDecimal(1,
                                                     rsDtls.getBigDecimal(1));
                            OracleResultSet rsOptDtls =
                                (OracleResultSet)cstOptDtls.executeQuery();
                            int c = 0;
                            int[] cols = new int[no];
                            while (rsOptDtls.next()) {
                                RichOutputText out = new RichOutputText();
                                out.setId("col" + rsOptDtls.getString(1));
                                out.setValue(rsOptDtls.getString(3));
                                pnlDtls.getChildren().add(out);
                                cols[c] = rsOptDtls.getInt(1);
                                c++;
                            }
                            for (int h = 0; h < rows; h++) {

                                int s = 0;
                                while (s < c) {
                                    cstValue2 =
                                            (OracleCallableStatement)conn.prepareCall("SELECT TSMSI_VALUE FROM TQC_SYS_MOD_SUBUNITS_INPUTS WHERE TSMSI_CODE = " +
                                                                                      sequence +
                                                                                      " AND TSMSI_TSMSD_CODE = " +
                                                                                      rsDtls.getString(1) +
                                                                                      " AND TSMSI_ROW = " +
                                                                                      h +
                                                                                      " AND TSMSI_COLUMN = " +
                                                                                      s +
                                                                                      " ");

                                    OracleResultSet rsVal2 =
                                        (OracleResultSet)cstValue2.executeQuery();
                                    int v2 = 0;
                                    while (rsVal2.next()) {
                                        cmpValue2 = rsVal2.getString(1);
                                        v2++;
                                    }
                                    if (v2 == 0) {
                                        cmpValue2 = null;
                                    }
                                    rsVal2.close();
                                    cstValue2.close();

                                    RichInputText text = new RichInputText();
                                    text.setAutoSubmit(true);
                                    text.setValue(cmpValue2);
                                    text.setSimple(true);
                                    //text.setValueChangeListener(app.createMethodBinding("#{ClaimsBean.valueEdited}",
                                    //                                                    new Class[] { ValueChangeEvent.class }));
                                    int b = cols[s];
                                    text.setLabel("lbl" + b);
                                    text.setId("lbl" + b + "cell" + h + "" +
                                               s);
                                    text.setReadOnly(true);
                                    pnlDtls.getChildren().add(text);
                                    s++;
                                }
                            }

                            rsOptDtls.close();
                            cstOptDtls.close();
                            unitPnl.getChildren().add(pnlDtls);

                        } else {

                            HtmlPanelGrid pnlDtls = new HtmlPanelGrid();
                            RichOutputLabel outLabel = new RichOutputLabel();
                            outLabel.setValue(cmpValue);
                            pnlDtls.getChildren().add(outLabel);
                            unitPnl.getChildren().add(pnlDtls);
                        }

                        // System.out.println("More Details");
                        //  System.out.println(rsDtls.getString(8));
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


                }
                k++;
            }
            rs.close();
            cst.close();
            conn.close();
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "demoTemplate:p53234" + "').hide(hints);");
            GlobalCC.refreshUI(mainParent);

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String createClient() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;
        String surname = null;
        String firstname = null;
        String middlename = null;
        String setValues = null;
        String columnName = null;
        String value = null;
        OracleResultSet rs = null;

        String query = null;

        try {
            query =
                    " BEGIN ? := TQC_PORTAL_CURSOR.getDynamicValues(?,?); END;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setObject(2, "PROPOSAL");
            cst.setObject(3, session.getAttribute("sequence"));
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.execute();
            rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                columnName = rs.getString(7);
                value = rs.getString(2);
                if (columnName.equalsIgnoreCase("CLNT_SURNAME")) {
                    surname = value;
                }
                if (columnName.equalsIgnoreCase("CLNT_NAME")) {
                    firstname = value;
                }
                if (columnName.equalsIgnoreCase("CLNT_OTHER_NAMES")) {
                    middlename = value;
                }
                if (!columnName.equalsIgnoreCase("CLNT_COU_CODE") &&
                    !columnName.equalsIgnoreCase("CLNT_NAME") && /* &&
                    !columnName.equalsIgnoreCase("CLNT_DOB") */!columnName.equalsIgnoreCase("CLNT_EMAIL_ADDRS")) /*&&
                    !columnName.equalsIgnoreCase("CLNT_GENDER")*/ {
                    if (columnName.equalsIgnoreCase("CLNT_GENDER")) {
                        value = (String)value.subSequence(0, 1);
                    }
                    if (setValues == null) {
                        setValues = columnName + " = '" + value + "'";
                    } else {
                        if (!setValues.contains(columnName)) {
                            setValues =
                                    setValues + " , " + columnName + " = '" +
                                    value + "' ";
                        }
                    }
                }


            }
            rs.close();
            query =
                    " BEGIN TQC_PORTAL_PKG.create_clnt_proc(?,?,?,?,?,?,?); END;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setObject(1, session.getAttribute("userCode"));
            cst.setObject(2, firstname);
            cst.setObject(3, middlename);
            cst.setObject(4, surname);
            cst.setObject(5, 27);
            cst.setObject(6, session.getAttribute("Username"));
            cst.registerOutParameter(7, OracleTypes.NUMBER);
            cst.execute();
            session.setAttribute("ClientCode", cst.getBigDecimal(7));


            query =
                    "UPDATE TQC_CLIENTS SET " + setValues + "  WHERE CLNT_CODE =" +
                    session.getAttribute("ClientCode") + " ";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.executeQuery();
            cst.close();
            conn.commit();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setPortfolioTab(RichPanelBox portfolioTab) {
        this.portfolioTab = portfolioTab;
    }

    public RichPanelBox getPortfolioTab() {
        return portfolioTab;
    }

    public void setPreviewButtonPop(RichPopup previewButtonPop) {
        this.previewButtonPop = previewButtonPop;
    }

    public RichPopup getPreviewButtonPop() {
        return previewButtonPop;
    }

    public void setPrevDialogue(RichDialog prevDialogue) {
        this.prevDialogue = prevDialogue;
    }

    public RichDialog getPrevDialogue() {
        return prevDialogue;
    }
}
