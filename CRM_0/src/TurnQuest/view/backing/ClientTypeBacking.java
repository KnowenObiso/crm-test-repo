package TurnQuest.view.backing;

import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;

import TurnQuest.view.Base.HibernateUtil;
import TurnQuest.view.models.ClientType;

import java.math.BigDecimal;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;
import org.apache.myfaces.trinidad.event.SelectionEvent;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ClientTypeBacking {
        HttpSession session =
            (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    public ClientTypeBacking() {
        super();
    }
    //-----------start buttons--------------------//
        private RichCommandButton  btnNewClientType;
        private RichCommandButton  btnEditClientType;
        private RichCommandButton  btnDeleteClientType;
        private RichCommandButton  btnSaveClientType;
    //-----------end buttons--------------------//
    //-----------start __fields--------------------// 
      private RichSelectOneChoice txtClientTypeCategory;
      private RichInputText txtClientTypeType;
      private RichSelectOneChoice txtClientTypePerson;
      private RichInputText txtClientTypeDesc; 
      private RichTable clienttypesTable;
    
     
    //-----------start selectClientType--------------------//
        public void selectClientType(SelectionEvent selectionEvent) {
            Object keys = clienttypesTable.getSelectedRowData();
            JUCtrlValueBinding r = (JUCtrlValueBinding)keys;
            if (r == null) {
                GlobalCC.INFORMATIONREPORTING("Please select a Client Type!");
                return;
            }
            session.setAttribute("clientTypeCode", r.getAttribute("code")); 
        }
    //-----------end selectClientType--------------------//
    //-----------start actionNewClientType--------------------//
        public String actionNewClientType() { 
               session.removeAttribute("clientTypeCode") ;  
               txtClientTypeCategory.setValue(null);
               txtClientTypeType.setValue(null);
               txtClientTypePerson.setValue(null);
               txtClientTypeDesc.setValue(null);
            btnSaveClientType.setText("Save");
            GlobalCC.showPopup("pt1:ClientTypePop");
            return null;
        }
    //-----------end actionNewClientType--------------------//
    //-----------start actionEditClientType--------------------//
        public String actionEditClientType() {
                Object key2 = clienttypesTable.getSelectedRowData();
                JUCtrlValueBinding n = (JUCtrlValueBinding)key2;
     
                if (n != null) {
                    session.setAttribute("clientTypeCode",n.getAttribute("code"));   
                    txtClientTypeCategory.setValue(n.getAttribute("category"));
                    txtClientTypeType.setValue(n.getAttribute("type"));
                    txtClientTypePerson.setValue(n.getAttribute("person"));
                    txtClientTypeDesc.setValue(n.getAttribute("desc"));
                    btnSaveClientType.setText("Edit"); 
                    // Open the popup dialog 
                    GlobalCC.showPopup("pt1:ClientTypePop");
     
                    } else {
                            GlobalCC.INFORMATIONREPORTING("No ClientType Selected:");
                    }
            return null;
        }
    //-----------end actionEditClientType--------------------//
    //-----------start actionDeleteClientType--------------------//
        public String actionDeleteClientType(){ 
            Object key2 = clienttypesTable.getSelectedRowData();
            JUCtrlValueBinding n = (JUCtrlValueBinding)key2; 
            if (n == null){
                GlobalCC.INFORMATIONREPORTING("No Client Type Selected.");
                return null;
            }
            Session dbSess = HibernateUtil.getSession();
            Transaction tx = null;
            
            try {
                    BigDecimal code = GlobalCC.checkBDNullValues(n.getAttribute("code"));
                    tx = dbSess.beginTransaction();
                    ClientType item = (ClientType)dbSess.get(ClientType.class, code); 
                    dbSess.delete(item); 
                    tx.commit();
                    session.removeAttribute("clientTypeCode");
                    ADFUtils.findIterator("fetchClientTypesIterator").executeQuery();
                    GlobalCC.refreshUI(clienttypesTable);
                    
                    String message = "Client Type DELETED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);
            } catch (HibernateException e) {
               if (tx!=null) tx.rollback();
                GlobalCC.EXCEPTIONREPORTING(e); 
            } finally {
               dbSess.close(); 
            } 
            return null;
        }
    //-----------end actionDeleteClientType--------------------//
    //-----------start actionSaveClientType--------------------//
        public String actionSaveClientType(){ 
                     Session dbSess = HibernateUtil.getSession();
                     Transaction tx = null;
                     String action = btnSaveClientType.getText(); 
                     try { 
                            tx = dbSess.beginTransaction();
                            BigDecimal valCode = GlobalCC.checkBDNullValues(session.getAttribute("clientTypeCode"));
                            ClientType item = new ClientType();
                            if("Edit".equals(action)){ //Edit
                                item = (ClientType)dbSess.get(ClientType.class, valCode);
                            } 
                            //-----------retrieve inputs ClientType-----------------// 
                            String valCategory = GlobalCC.checkNullValues(txtClientTypeCategory.getValue());
                            String valType = GlobalCC.checkNullValues(txtClientTypeType.getValue());
                            String valPerson = GlobalCC.checkNullValues(txtClientTypePerson.getValue());
                            String valDesc = GlobalCC.checkNullValues(txtClientTypeDesc.getValue());
                            //-----------set ClientType------------------//
                            item.setCode(valCode); 
                            item.setCategory(valCategory);
                            item.setType(valType);
                            item.setPerson(valPerson);
                            item.setDesc(valDesc);
                            if("Edit".equals(action)){ //Edit
                                dbSess.update(item);  
                            }else{
                                dbSess.save(item);  
                            }
                            tx.commit();
                            ADFUtils.findIterator("fetchClientTypesIterator").executeQuery();
                            GlobalCC.refreshUI(clienttypesTable);
                            
                            String message = "Client Type Saved Successfully!";
                            GlobalCC.INFORMATIONREPORTING(message);
                            GlobalCC.hidePopup("pt1:ClientTypePop");
                     } catch (HibernateException e) {
                        if (tx!=null) tx.rollback();
                        GlobalCC.EXCEPTIONREPORTING(e); 
                     } finally {
                        dbSess.close(); 
                     }
            return null;
        }
    //-----------end actionSaveClientType--------------------//

    public void setBtnNewClientType(RichCommandButton btnNewClientType) {
        this.btnNewClientType = btnNewClientType;
    }

    public RichCommandButton getBtnNewClientType() {
        return btnNewClientType;
    }

    public void setBtnEditClientType(RichCommandButton btnEditClientType) {
        this.btnEditClientType = btnEditClientType;
    }

    public RichCommandButton getBtnEditClientType() {
        return btnEditClientType;
    }

    public void setBtnDeleteClientType(RichCommandButton btnDeleteClientType) {
        this.btnDeleteClientType = btnDeleteClientType;
    }

    public RichCommandButton getBtnDeleteClientType() {
        return btnDeleteClientType;
    }

    public void setBtnSaveClientType(RichCommandButton btnSaveClientType) {
        this.btnSaveClientType = btnSaveClientType;
    }

    public RichCommandButton getBtnSaveClientType() {
        return btnSaveClientType;
    }

    public void setTxtClientTypeCategory(RichSelectOneChoice txtClientTypeCategory) {
        this.txtClientTypeCategory = txtClientTypeCategory;
    }

    public RichSelectOneChoice getTxtClientTypeCategory() {
        return txtClientTypeCategory;
    }

    public void setTxtClientTypeType(RichInputText txtClientTypeType) {
        this.txtClientTypeType = txtClientTypeType;
    }

    public RichInputText getTxtClientTypeType() {
        return txtClientTypeType;
    }

    public void setTxtClientTypePerson(RichSelectOneChoice txtClientTypePerson) {
        this.txtClientTypePerson = txtClientTypePerson;
    }

    public RichSelectOneChoice getTxtClientTypePerson() {
        return txtClientTypePerson;
    }

    public void setTxtClientTypeDesc(RichInputText txtClientTypeDesc) {
        this.txtClientTypeDesc = txtClientTypeDesc;
    }

    public RichInputText getTxtClientTypeDesc() {
        return txtClientTypeDesc;
    }

    public void setClienttypesTable(RichTable clienttypesTable) {
        this.clienttypesTable = clienttypesTable;
    }

    public RichTable getClienttypesTable() {
        return clienttypesTable;
    }
}
