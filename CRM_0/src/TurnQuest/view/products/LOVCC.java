package TurnQuest.view.products;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;

import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class LOVCC {
    private RichInputText objProductDesc;
    private RichInputText productOptionDesc;
    private RichTable popOptions;
    private RichTable products;
    private RichTable productMasks;
    private RichInputText productMaskDesc;
    private RichTable prodCoverTypes;
    private RichPopup coverPopUp;
    private RichInputText coverDesc;
    private RichTable prodProvisions;
    private RichTable moduleProdProvs;

    public LOVCC() {
    }
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public String productSelected() {
        RowKeySet rowKeySet = products.getSelectedRowKeys();
        if (rowKeySet == null) {
            return null;
        }
        if (!rowKeySet.iterator().hasNext()) //no selection
        {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        Object key2 = rowKeySet.iterator().next();
        products.setRowKey(key2);
        JUCtrlValueBinding r = (JUCtrlValueBinding)products.getRowData();
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        BigDecimal prodCode = (BigDecimal)r.getAttribute("productCode");
        session.setAttribute("productCode", prodCode);
        /*String prodGenProp = GlobalCC.findProppropgen(prodCode);
        if (prodGenProp == null) {
            GlobalCC.errorValueNotEntered("Error.....");
            return null;
        }

        session.setAttribute("prodGenProp", prodGenProp);*/
        session.setAttribute("manLcf",
                             GlobalCC.findManLifeCover((BigDecimal)r.getAttribute("productCode")));
        String genTerm =
            GlobalCC.findProdTermAuto((BigDecimal)r.getAttribute("productCode"));
        if (genTerm == null) {
            genTerm = "N";
        }
        if (genTerm.equalsIgnoreCase("Y")) {
            session.setAttribute("genTerm", true);
        } else {
            session.setAttribute("genTerm", false);
        }
        session.setAttribute("lifeRider",
                             GlobalCC.findProdLifeRider(prodCode));
        session.setAttribute("productDesc", r.getAttribute("productDesc"));
        session.setAttribute("productType", r.getAttribute("productType"));
        objProductDesc.setValue(r.getAttribute("productDesc"));

        if (prodProvisions == null) {

        } else {
            ADFUtils.findIterator("findProductProvisionsIterator").executeQuery();
            GlobalCC.refreshUI(prodProvisions);
        }

        if (productMaskDesc == null) {

        } else {
            productMaskDesc.setValue(null);
            session.setAttribute("pMasCode", null);
            session.setAttribute("pMasDesc", null);
            ADFUtils.findIterator("findProductMasksIterator").executeQuery();
            GlobalCC.refreshUI(productMasks);
            GlobalCC.refreshUI(productMaskDesc);
        }
        if (productOptionDesc == null) {

        } else {
            productOptionDesc.setValue(null);
            session.setAttribute("popCode", null);
            session.setAttribute("popDesc", null);
            ADFUtils.findIterator("findProductOptionsIterator").executeQuery();
            GlobalCC.refreshUI(popOptions);
            GlobalCC.refreshUI(productOptionDesc);

        }


        return null;
    }

    public String productMaskSelected() {
        //valueChangeEvent.getNewValue();
        DCIteratorBinding dciter =
            ADFUtils.findIterator("findProductMasksIterator");
        RowKeySet set = productMasks.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();

            try {
                session.setAttribute("pMasCode", r.getAttribute("PMasCode"));
                session.setAttribute("pMasDesc", r.getAttribute("PMasDesc"));
                productMaskDesc.setValue(r.getAttribute("PMasDesc"));


            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(null, e);
            }

        }

        return null;
    }

    public String coverTypeSelected() {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("findProductOptionCoversIterator");
        RowKeySet set = prodCoverTypes.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();

            try {
                session.setAttribute("cvtCode", r.getAttribute("cvtCode"));
                session.setAttribute("cvtShtDesc",
                                     r.getAttribute("cvtShtDesc"));
                session.setAttribute("cvtDesc", r.getAttribute("cvtDesc"));
                session.setAttribute("pctCode", r.getAttribute("pctCode"));
                coverDesc.setValue(r.getAttribute("cvtDesc"));

                System.out.print("cvt Code: " + r.getAttribute("cvtCode"));
                System.out.print("Pct Code: " + r.getAttribute("pctCode"));

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(null, e);
            }

        }
        GlobalCC.refreshUI(coverDesc);
        GlobalCC.refreshUI(coverPopUp);
        return null;
    }

    public String saveModProductProvision() {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("findProductProvisionsIterator");
        RowKeySet set = prodProvisions.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        String provisionsQuery = null;
        if (((String)session.getAttribute("sysModule")).equalsIgnoreCase("Q")) {
            provisionsQuery =
                    "begin LMS_WEB_PKG_MKT.Update_quot_prod_provs(?,?,?,?,?,?,?); end;";
        }
        try {
            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));

                Row r = dciter.getCurrentRow();
                cst =
(OracleCallableStatement)conn.prepareCall(provisionsQuery);

                cst.registerOutParameter(1, OracleTypes.NUMBER);
                cst.setBigDecimal(2,
                                  (BigDecimal)session.getAttribute("quoteCode"));
                cst.setBigDecimal(3,
                                  (BigDecimal)session.getAttribute("productCode"));
                cst.setBigDecimal(4,
                                  (BigDecimal)r.getAttribute("proProvisionCode"));
                cst.setString(5,
                              (String)r.getAttribute("proProvisionShtDesc"));
                cst.setString(6, (String)r.getAttribute("proprovisionDesc"));
                cst.setString(7,
                              (String)r.getAttribute("proProvisionEditable"));

                cst.execute();
                conn.commit();
                conn.close();


            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        ADFUtils.findIterator("findProductModuleProvisionsIterator").executeQuery();
        GlobalCC.refreshUI(moduleProdProvs);
        return null;
    }

    public String productOptionSelected() {
        //valueChangeEvent.getNewValue();
        DCIteratorBinding dciter =
            ADFUtils.findIterator("findProductOptionsIterator");
        RowKeySet set = popOptions.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();

            try {
                session.setAttribute("popCode", r.getAttribute("popCode"));
                session.setAttribute("popDesc", r.getAttribute("popShtDesc"));
                productOptionDesc.setValue(r.getAttribute("popShtDesc"));

                if (prodCoverTypes == null) {

                } else {
                    ADFUtils.findIterator("findProductOptionCoversIterator").executeQuery();
                    GlobalCC.refreshUI(prodCoverTypes);
                }
            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(null, e);
            }

        }

        return null;
    }

    public void setObjProductDesc(RichInputText productDesc) {
        this.objProductDesc = productDesc;
    }

    public RichInputText getObjProductDesc() {
        return objProductDesc;
    }

    public void setProductOptionDesc(RichInputText productOptionDesc) {
        this.productOptionDesc = productOptionDesc;
    }

    public RichInputText getProductOptionDesc() {
        return productOptionDesc;
    }

    public void setPopOptions(RichTable popOptions) {
        this.popOptions = popOptions;
    }

    public RichTable getPopOptions() {
        return popOptions;
    }

    public void setProducts(RichTable products) {
        this.products = products;
    }

    public RichTable getProducts() {
        return products;
    }

    public void setProductMasks(RichTable productMasks) {
        this.productMasks = productMasks;
    }

    public RichTable getProductMasks() {
        return productMasks;
    }

    public void setProductMaskDesc(RichInputText productMaskDesc) {
        this.productMaskDesc = productMaskDesc;
    }

    public RichInputText getProductMaskDesc() {
        return productMaskDesc;
    }

    public void setProdCoverTypes(RichTable prodCoverTypes) {
        this.prodCoverTypes = prodCoverTypes;
    }

    public RichTable getProdCoverTypes() {
        return prodCoverTypes;
    }

    public void setCoverPopUp(RichPopup coverPopUp) {
        this.coverPopUp = coverPopUp;
    }

    public RichPopup getCoverPopUp() {
        return coverPopUp;
    }

    public void setCoverDesc(RichInputText coverDesc) {
        this.coverDesc = coverDesc;
    }

    public RichInputText getCoverDesc() {
        return coverDesc;
    }

    public void setProdProvisions(RichTable prodProvisions) {
        this.prodProvisions = prodProvisions;
    }

    public RichTable getProdProvisions() {
        return prodProvisions;
    }

    public void setModuleProdProvs(RichTable moduleProdProvs) {
        this.moduleProdProvs = moduleProdProvs;
    }

    public RichTable getModuleProdProvs() {
        return moduleProdProvs;
    }

    public String launchOption() {
        ADFUtils.findIterator("findProductOptionsIterator").executeQuery();
        GlobalCC.refreshUI(popOptions);
        GlobalCC.refreshUI(productOptionDesc);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "demoTemplate:prodOption" + "').show(hints);");
        return null;
    }
}
