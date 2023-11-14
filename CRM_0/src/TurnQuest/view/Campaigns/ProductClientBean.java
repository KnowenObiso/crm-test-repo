package TurnQuest.view.Campaigns;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import org.apache.myfaces.trinidad.event.SelectionEvent;


public class ProductClientBean {
    private RichTable tblProducts;
    private RichTable tblClients;
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public ProductClientBean() {
    }

    public void setTblProducts(RichTable tblProducts) {
        this.tblProducts = tblProducts;
    }

    public RichTable getTblProducts() {
        return tblProducts;
    }

    public void setTblClients(RichTable tblClients) {
        this.tblClients = tblClients;
    }

    public RichTable getTblClients() {
        return tblClients;
    }


    public void actionTblProductsSelected(SelectionEvent selectionEvent) {

        Object key = tblProducts.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {

            session.setAttribute("PRODUCT_ATTRIBUTE_CODE",
                                 nodeBinding.getAttribute("TPA_CODE"));
            ADFUtils.findIterator("findClientsPerProductIterator").executeQuery();
            GlobalCC.refreshUI(tblClients);

        }
    }
}
