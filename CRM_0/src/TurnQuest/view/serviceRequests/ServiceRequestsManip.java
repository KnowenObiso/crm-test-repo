package TurnQuest.view.serviceRequests;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanRadio;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelFormLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelLabelAndMessage;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class ServiceRequestsManip {
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private RichTable tblClients;
    private RichSelectOneChoice searchCriteria;
    private RichInputText txtsearchCriteriaValue;
    private RichInputText txtSearchShtDesc;
    private RichInputText txtSearchName;
    private RichInputText txtOtherName;
    private RichInputText txtSearchOtherName;
    private RichInputText txtSearchSector;
    private RichInputText txtSearchShortDesc;
    private RichSelectOneChoice txtSearchStatus;
    private RichPanelLabelAndMessage txtSearchClientType;
    private RichSelectOneChoice txtSearchClntType;
    private RichSelectBooleanRadio rbtnExactNmBig;
    private RichSelectBooleanRadio rbtnPartNmInOrder;
    private RichSelectBooleanRadio rbtnAnyPartOfFirAndOthNm;
    private RichSelectBooleanRadio rbtnBegPartOfAnyNm;
    private RichSelectBooleanRadio rbtnPartOfAnyNameInOrder;
    private RichSelectBooleanRadio rbtnExactName;
    private RichSelectBooleanRadio rbtnPartOfAnyName;
    private RichSelectBooleanRadio rbtnBegPartOfFstNmAndOName;
    private Object sectorName;
    private RichSelectBooleanRadio rbtnShortDesc;
    private RichSelectBooleanRadio rbtnClientType;
    private RichSelectBooleanRadio rbtnStatus;
    private RichSelectBooleanRadio rbtnSector;
    private RichSelectBooleanRadio rbtnPhySicalAddr;
    private RichSelectBooleanRadio rbtnPostalAddr;
    private RichInputText txtSearchPhysical;
    private RichInputText txtSearchPostal;
    private RichSelectBooleanRadio rbtnCustomerId;
    private RichSelectBooleanRadio rbtnIncome;
    private RichInputText txtSearchOldId;
    private RichSelectBooleanRadio rbtnOldNames;
    private RichCommandButton btnSectorLov;
    private RichPanelGroupLayout searchFormHolder;
    private RichInputDate clntDateCreatedFrom;
    private RichInputDate clntDateCreatedTo;
    private RichSelectBooleanRadio rbtnDateCreated;
    private RichInputText txtAccountNo;
    private HtmlPanelGrid gridClientSearchDetails;

    private RichSelectBooleanRadio rbtnSearchAccountNo;
    private RichPanelLabelAndMessage statusHolder;
    private RichInputText txtSrchSectorName;
    private RichPanelLabelAndMessage resetSearchContainer;
    private RichTable tblSectorPop;
    private RichPanelLabelAndMessage searchClientType;
    private RichPanelFormLayout SEARCHHOLDER;
    private RichTable tblBankBranches;
    private RichInputText txtBankBranch;
    private RichInputText txtBankCode;
    private RichInputText txtBankBranchCode;
    private RichTable reservWrds;
    private RichTree systems;
    private RichInputText code;
    private RichSelectOneChoice type;
    private RichInputText word;
    private RichInputText desc;
    private RichInputText category;
    private RichTable categories;

    public ServiceRequestsManip() {
    }

    public void setTblClients(RichTable tblClients) {
        this.tblClients = tblClients;
    }

    public RichTable getTblClients() {
        return tblClients;
    }

    public void enterKeyPressed(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            String action = actionAcceptSearchCriteria();
        }
    }

    public String actionAcceptSector() {
        Object key2 = tblSectorPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {


            session.setAttribute("sectorCode",
                                 nodeBinding.getAttribute("code"));
            session.setAttribute("sectorName",
                                 nodeBinding.getAttribute("name"));
            txtSrchSectorName.setValue(session.getAttribute("sectorName"));
            GlobalCC.refreshUI(txtSrchSectorName);
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:sectorPop" + "').hide(hints);");


        }
        return null;
    }

    public String actionResetSearch() {

        txtSearchName.setValue(null);
        txtSearchOtherName.setValue(null);
        txtSearchSector.setValue(null);
        txtSearchShortDesc.setValue(null);
        txtSearchStatus.setValue(null);
        txtSearchClntType.setValue(null);
        txtSearchPhysical.setValue(null);
        txtSearchPostal.setValue(null);
        session.setAttribute("sectorCode", null);
        clntDateCreatedFrom.setValue(null);
        clntDateCreatedTo.setValue(null);

        session.setAttribute("sectorName", null);
        session.setAttribute("searchClntStatus", null);
        session.setAttribute("searchClntType", null);
        session.setAttribute("sectorCode", null);

        txtSearchName.setDisabled(true);
        txtSearchOtherName.setDisabled(true);
        txtSearchSector.setDisabled(true);
        txtSearchShortDesc.setDisabled(true);
        txtSearchStatus.setDisabled(true);
        txtSearchClntType.setDisabled(true);
        txtSearchPhysical.setDisabled(true);
        txtSearchPostal.setDisabled(true);
        clntDateCreatedFrom.setDisabled(true);
        clntDateCreatedTo.setDisabled(true);
        txtAccountNo.setDisabled(true);

        //refresh radio buttons
        rbtnPartOfAnyName.setSelected(false);
        rbtnExactName.setSelected(false);
        rbtnSector.setSelected(false);
        rbtnStatus.setSelected(false);
        rbtnClientType.setSelected(false);
        rbtnShortDesc.setSelected(false);
        rbtnDateCreated.setSelected(false);
        rbtnPhySicalAddr.setSelected(false);
        rbtnPostalAddr.setSelected(false);
        rbtnOldNames.setSelected(false);
        rbtnCustomerId.setSelected(false);
        rbtnSearchAccountNo.setSelected(false);

        GlobalCC.refreshUI(rbtnSearchAccountNo);
        GlobalCC.refreshUI(rbtnPartOfAnyName);
        GlobalCC.refreshUI(rbtnExactName);
        GlobalCC.refreshUI(rbtnSector);
        GlobalCC.refreshUI(rbtnStatus);
        GlobalCC.refreshUI(rbtnClientType);
        GlobalCC.refreshUI(rbtnShortDesc);
        GlobalCC.refreshUI(rbtnDateCreated);
        GlobalCC.refreshUI(rbtnPhySicalAddr);
        GlobalCC.refreshUI(rbtnPostalAddr);
        GlobalCC.refreshUI(rbtnOldNames);
        GlobalCC.refreshUI(rbtnCustomerId);
        //refesh components
        GlobalCC.refreshUI(txtAccountNo);
        GlobalCC.refreshUI(txtSearchName);
        GlobalCC.refreshUI(txtSearchOtherName);
        GlobalCC.refreshUI(txtSearchSector);
        GlobalCC.refreshUI(txtSearchShortDesc);
        GlobalCC.refreshUI(txtSearchStatus);
        GlobalCC.refreshUI(txtSearchClntType);
        GlobalCC.refreshUI(clntDateCreatedFrom);
        GlobalCC.refreshUI(clntDateCreatedTo);
        GlobalCC.refreshUI(txtSearchPhysical);
        GlobalCC.refreshUI(txtSearchPostal);
        session.setAttribute("searchCriteria", null);
        ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
        GlobalCC.refreshUI(tblClients);

        return null;
    }

    public void cboSearchClntTypelistener(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue() &&
            evt.getNewValue() != null) {


            String type =
                GlobalCC.checkNullValues(txtSearchClntType.getValue());
            session.setAttribute("searchClntType", type);

        }
    }

    public void cboSearchStatusListener(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue() &&
            evt.getNewValue() != null) {
            session.setAttribute("searchClntStatus", null);

            String searchStatus =
                GlobalCC.checkNullValues(txtSearchStatus.getValue());
            session.setAttribute("searchClntStatus", searchStatus);

        }
    }

    public String actionAcceptSearchCriteria() {

        String name = null;
        String oName = null;
        String shtDesc = null;
        String clntType = null;
        String status = null;
        String postalAddr = null;
        String physicalAddr = null;
        String oldId = null;
        String sector =
            GlobalCC.checkNullValues(session.getAttribute("sectorCode"));
        String criteria = null;
        String criteria2 = null;
        String searchName = null;
        String searchOName = null;
        String searchPostalAddr = null;
        String searchPhysicalAddr = null;
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String dFrom = null;
        String dTo = null;
        String fromDate = null;
        String searchOldId = null;
        String searchOldNames = null;

        String searchShtDesc =
            GlobalCC.checkNullValues(txtSearchShortDesc.getValue());
        String searchAccountNo =
            GlobalCC.checkNullValues(txtAccountNo.getValue()); //client account NO.


        String searchClntType =
            GlobalCC.checkNullValues(session.getAttribute("searchClntType"));
        String searchStatus =
            GlobalCC.checkNullValues(session.getAttribute("searchClntStatus"));
        String searchSector =
            GlobalCC.checkNullValues(session.getAttribute("sectorCode"));
        searchPhysicalAddr =
                GlobalCC.checkNullValues(txtSearchPhysical.getValue());
        searchPostalAddr =
                GlobalCC.checkNullValues(txtSearchPostal.getValue());
        if (rbtnCustomerId.isSelected()) {
            searchOldNames =
                    GlobalCC.checkNullValues(txtSearchShortDesc.getValue());
        }


        session.setAttribute("searchCriteria", null);
        session.setAttribute("searchCriteria2", null);


        if (clntDateCreatedFrom.getValue() != null &&
            !(clntDateCreatedFrom.getValue().equals(""))) {
            String date1 = df.format(clntDateCreatedFrom.getValue());

            fromDate =
                    GlobalCC.parseNormalDate(clntDateCreatedFrom.getValue().toString());

        }

        String toDate = null; //new Date();
        if (clntDateCreatedTo.getValue() != null &&
            !(clntDateCreatedTo.getValue().equals(""))) {
            String date2 = df.format(clntDateCreatedTo.getValue());

            toDate =
                    GlobalCC.parseNormalDate(clntDateCreatedTo.getValue().toString());

        }

        searchName = GlobalCC.checkNullValues(txtSearchName.getValue());

        if (searchName == null || searchName == "") {

            searchName = null;

        } else if (searchName.trim().length() < 1) {
            searchName = null;
        }

        searchOName = GlobalCC.checkNullValues(txtSearchOtherName.getValue());
        if (searchOName == null || searchOName == "") {

            searchOName = null;

        } else if (searchOName.trim().length() < 1) {
            searchOName = null;
        }
        if (searchPhysicalAddr == null) {

        } else if (searchPhysicalAddr.trim().length() < 1) {
            searchPhysicalAddr = null;
        }
        if (searchPostalAddr == null) {
            searchPostalAddr = null;
        } else if (searchPostalAddr.trim().length() < 1) {
            searchPostalAddr = null;
        }
        if (searchOldId != null) {
            if (searchOldId.trim().length() < 1) {
                searchOldId = null;
            }
        }

        if (searchName != null) {
            name = "'" + searchName + "'";

        }
        if (searchOName != null) {
            oName = "'" + searchOName + "'";
        }
        if (searchShtDesc != null) {
            shtDesc = "'" + searchShtDesc + "'";
        }
        if (searchClntType != null) {
            clntType = "'" + searchClntType + "'";
        }
        if (searchStatus != null) {
            status = "'" + searchStatus + "'";
        }
        if (fromDate != null) {
            dFrom = "'" + fromDate + "'";
        }
        if (toDate != null) {
            dTo = "'" + toDate + "'";
        }
        if (searchPhysicalAddr != null) {
            physicalAddr = "'" + searchPhysicalAddr + "'";
        }
        if (searchPostalAddr != null) {
            postalAddr = "'" + searchPostalAddr + "'";
        }
        if (searchOldId != null) {
            oldId = "'" + searchOldId + "'";
        }

        if (rbtnSearchAccountNo.isSelected()) {
            if (searchAccountNo == null || searchAccountNo == "") {
                GlobalCC.EXCEPTIONREPORTING("Specify  Clietn Account Number");
                return null;
            }

            criteria =
                    ", TQC_CLIENT_ACCOUNTS " + "      WHERE CLNT_CODE=CLNA_CLNT_CODE" +
                    "      AND CLNA_SHT_DESC= '" + searchAccountNo + "'";
        }


        else if (rbtnPartOfAnyName.isSelected()) {
            // criteria
            if (oName != null && name != null) {
                GlobalCC.INFORMATIONREPORTING("Specify only One name:");
                return null;
            } else if (oName == null && name == null) {
                GlobalCC.INFORMATIONREPORTING("No name specified:");
                return null;
            } else if (status == null && clntType == null && sector == null &&
                       dFrom == null && dTo == null) {
                criteria =
                        "    WHERE ( UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA)'))||'%'\n" +
                        "            OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA'))||'%' OR " +
                        "UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        oName + ",'HAKUNA)'))||'%'" +
                        " OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" + oName +
                        ",'HAKUNA'))||'%')";

            } else if (clntType == null && sector == null) {
                criteria =
                        "    WHERE ( UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA)'))||'%'\n" +
                        "            OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA'))||'%' OR " +
                        "UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        oName + ",'HAKUNA)'))||'%'" +
                        " OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" + oName +
                        ",'HAKUNA'))||'%')" + "            AND (" +
                        " CLNT_STATUS=NVL(" + status + " ,CLNT_STATUS)" +

                        " AND (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                        dFrom +
                        ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" + dTo +
                        ",'01/01/2099'),'dd/mm/yyyy')))";
            } else if (clntType == null && sector != null && status != null) {
                criteria =
                        "    WHERE ( UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA)'))||'%'\n" +
                        "            OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA'))||'%' OR " +
                        "UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        oName + ",'HAKUNA)'))||'%'" +
                        " OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" + oName +
                        ",'HAKUNA'))||'%')" + "            AND (" +
                        "            nvl(CLNT_SEC_CODE,0) = nvl(" + sector +
                        ",0) \n" +

                        "  CLNT_STATUS=NVL(" + status + " ,CLNT_STATUS)" +
                        " AND (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                        dFrom +
                        ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" + dTo +
                        ",'01/01/2099'),'dd/mm/yyyy')))";
            } else {
                criteria =
                        "  WHERE ( UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA)'))||'%'\n" +
                        "            OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA'))||'%' OR " +
                        "UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        oName + ",'HAKUNA)'))||'%'" +
                        " OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" + oName +
                        ",'HAKUNA'))||'%')" +
                        "            AND (nvl(CLNT_SEC_CODE,0) = nvl(" +
                        sector + ",0) \n" +
                        "            and CLNT_TYPE=nvl(" +

                        clntType + ",CLNT_TYPE) \n" +
                        "           AND CLNT_STATUS=NVL(" + status +
                        " ,CLNT_STATUS)" +
                        " AND (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                        dFrom +
                        ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" + dTo +
                        ",'01/01/2099'),'dd/mm/yyyy')))";
            }


        } else if (rbtnExactName.isSelected()) {
            if (oName != null && name != null) {
                GlobalCC.INFORMATIONREPORTING("Specify only One name:");
                return null;
            } else if (oName == null && name == null) {
                GlobalCC.INFORMATIONREPORTING("No name specified:");
                return null;
            } else if (name != null) {
                if (status == null && clntType == null && sector == null &&
                    dFrom == null && dTo == null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_NAME) =UPPER( " + name + ")) ";

                } else if (status == null && clntType == null &&
                           sector == null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_NAME) =UPPER( " + name + ")) " +
                            "AND (  CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                            dFrom +
                            ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" +
                            dTo + ",'01/01/2099'),'dd/mm/yyyy'))";
                } else if (status == null && clntType != null &&
                           sector != null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_NAME) =UPPER( " + name + ")) " +
                            "AND (nvl(CLNT_SEC_CODE,0) = nvl(" + sector +
                            ",0) \n" +
                            "            and CLNT_TYPE=nvl(" + clntType +
                            ",CLNT_TYPE) \n" +
                            "  AND  (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                            dFrom +
                            ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" +
                            dTo + ",'01/01/2099'),'dd/mm/yyyy')))";
                } else if (clntType == null && status != null &&
                           sector != null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_NAME) =UPPER( " + name + ")) " +
                            "AND (nvl(CLNT_SEC_CODE,0) = nvl(" + sector +
                            ",0) \n" +
                            " AND CLNT_STATUS=NVL(" + status +
                            " ,CLNT_STATUS) AND  (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                            dFrom +
                            ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" +
                            dTo + ",'01/01/2099'),'dd/mm/yyyy')))";
                } else if (clntType == null && status != null &&
                           sector == null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_NAME) =UPPER( " + name + ")) " +
                            "AND ( \n" +
                            "  CLNT_STATUS=NVL(" + status +
                            " ,CLNT_STATUS) AND  (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                            dFrom +
                            ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" +
                            dTo + ",'01/01/2099'),'dd/mm/yyyy')))";
                } else {
                    criteria =
                            "WHERE ( UPPER(CLNT_NAME) =UPPER( " + name + ")) " +
                            "AND (nvl(CLNT_SEC_CODE,0) = nvl(" + sector +
                            ",0) \n" +
                            "            and CLNT_TYPE=nvl(" + clntType +
                            ",CLNT_TYPE) \n" +
                            "           AND CLNT_STATUS=NVL(" + status +
                            " ,CLNT_STATUS) AND  (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                            dFrom +
                            ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" +
                            dTo + ",'01/01/2099'),'dd/mm/yyyy')))";
                }

            } else if (oName != null) {
                if (status == null && clntType == null && sector == null &&
                    dFrom == null && dTo == null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_OTHER_NAMES)=UPPER(" + oName +
                            ") )";

                } else if (status == null && clntType == null &&
                           sector == null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_OTHER_NAMES)=UPPER(" + oName +
                            ") )" +
                            "AND (  CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                            dFrom +
                            ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" +
                            dTo + ",'01/01/2099'),'dd/mm/yyyy'))";
                } else if (status == null && clntType != null &&
                           sector != null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_OTHER_NAMES)=UPPER(" + oName +
                            ") )" + "AND (nvl(CLNT_SEC_CODE,0) = nvl(" +
                            sector + ",0) \n" +
                            "            and CLNT_TYPE=nvl(" + clntType +
                            ",CLNT_TYPE) \n" +
                            "  AND  (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                            dFrom +
                            ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" +
                            dTo + ",'01/01/2099'),'dd/mm/yyyy')))";
                } else if (clntType == null && status != null &&
                           sector != null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_OTHER_NAMES)=UPPER(" + oName +
                            ") )" + "AND (nvl(CLNT_SEC_CODE,0) = nvl(" +
                            sector + ",0) \n" +
                            " AND CLNT_STATUS=NVL(" + status +
                            " ,CLNT_STATUS) AND  (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                            dFrom +
                            ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" +
                            dTo + ",'01/01/2099'),'dd/mm/yyyy')))";
                } else if (clntType == null && status != null &&
                           sector == null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_OTHER_NAMES)=UPPER(" + oName +
                            ") )" + "AND ( \n" +
                            "  CLNT_STATUS=NVL(" + status +
                            " ,CLNT_STATUS) AND  (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                            dFrom +
                            ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" +
                            dTo + ",'01/01/2099'),'dd/mm/yyyy')))";
                } else {
                    criteria =
                            "WHERE ( UPPER(CLNT_OTHER_NAMES)=UPPER(" + oName +
                            ") " + ") " + "AND (nvl(CLNT_SEC_CODE,0) = nvl(" +
                            sector + ",0) \n" +
                            "          and CLNT_TYPE=nvl(" + clntType +
                            ",CLNT_TYPE) \n" +
                            "           AND CLNT_STATUS=NVL(" + status +
                            " ,CLNT_STATUS) AND (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                            dFrom +
                            ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" +
                            dTo + ",'01/01/2099'),'dd/mm/yyyy')))";
                }
            }

        }

        //beginning part  of first and other name

        else if (rbtnSector.isSelected()) {
            if (sector == null) {
                GlobalCC.INFORMATIONREPORTING("Sector  Required::");
                return null;
            }
            if (clntType == null && dFrom == null && dTo == null) {
                criteria =
                        "WHERE (nvl(CLNT_SEC_CODE," + 0 + ") = nvl(" + sector +
                        ",0))";
            } else {
                criteria =
                        "WHERE (nvl(CLNT_SEC_CODE," + 0 + ") = nvl(" + sector +
                        ",0))" + "AND (          CLNT_TYPE=nvl(" + clntType +
                        ",CLNT_TYPE) \n" +
                        "           AND CLNT_STATUS=NVL(" + status +
                        " ,CLNT_STATUS) AND (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                        dFrom +
                        ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" + dTo +
                        ",'01/01/2099'),'dd/mm/yyyy')))";
            }
        } else if (rbtnStatus.isSelected()) {
            if (status == null) {
                GlobalCC.INFORMATIONREPORTING("Client Status  Required:");
                return null;
            }
            if (clntType == null && sector == null && dFrom == null &&
                dTo == null) {
                criteria =
                        " WHERE  (  CLNT_STATUS=NVL(" + status + " ,CLNT_STATUS))";
            } else {

                criteria =
                        " WHERE  (  CLNT_STATUS=NVL(" + status + " ,CLNT_STATUS))" +
                        "AND (nvl(CLNT_SEC_CODE,0) = nvl(" + sector +
                        ",0) \n" +
                        "         AND  (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                        dFrom +
                        ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" + dTo +
                        ",'01/01/2099'),'dd/mm/yyyy')))";
            }
        } else if (rbtnClientType.isSelected()) {
            if (clntType == null) {
                GlobalCC.INFORMATIONREPORTING("Client Type  Required");
                return null;
            }
            if (status == null && sector == null && dFrom == null &&
                dTo == null) {
                criteria =
                        " WHERE (  CLNT_TYPE=nvl(" + clntType + ",CLNT_TYPE)) ";
            } else {
                criteria =
                        " WHERE (  CLNT_TYPE=nvl(" + clntType + ",CLNT_TYPE)) " +
                        "AND (nvl(CLNT_SEC_CODE,0) = nvl(" + sector +
                        ",0) \n" +
                        "           AND CLNT_STATUS=NVL(" + status +
                        " ,CLNT_STATUS) AND (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                        dFrom +
                        ",'01/01/1900'),'dd/mm/yyyy') AND to_date(nvl(" + dTo +
                        ",'01/01/2099'),'dd/mm/yyyy')))";
            }
        }

        else if (rbtnShortDesc.isSelected()) {
            if (shtDesc == null) {
                GlobalCC.INFORMATIONREPORTING("Short Desc Required");
                return null;
            }
            if (status == null && clntType == null && sector == null &&
                dFrom == null && dTo == null) {
                criteria =
                        " WHERE ( UPPER( CLNT_SHT_DESC ) LIKE '%'||NVL(UPPER(" +
                        shtDesc + "),'HAKUNA')||'%')";
            } else {
                criteria =
                        " WHERE ( UPPER( CLNT_SHT_DESC ) LIKE '%'||NVL(UPPER(" +
                        shtDesc + "),'HAKUNA')||'%')" +
                        "AND (nvl(CLNT_SEC_CODE,0) = nvl(" + sector +
                        ",0) \n" +
                        "            and CLNT_TYPE=nvl(" + clntType +
                        ",CLNT_TYPE) \n" +
                        "           AND CLNT_STATUS=NVL(" + status +
                        " ,CLNT_STATUS)AND (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                        dFrom +
                        ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" + dTo +
                        ",'01/01/2099'),'dd/mm/yyyy')))";
            }
        } else if (rbtnPhySicalAddr.isSelected()) {

            if (physicalAddr == null) {
                GlobalCC.INFORMATIONREPORTING("Physical Address  Required");
                return null;
            }
            if (status == null && clntType == null && sector == null &&
                dFrom == null && dTo == null) {
                criteria =
                        "where ( UPPER(CLNT_PHYSICAL_ADDRS) like '%'||UPPER(" +
                        physicalAddr + ")||'%')";
            } else if (status == null && clntType == null && sector != null) {
                criteria =
                        "where ( UPPER(CLNT_PHYSICAL_ADDRS) like '%'||UPPER(" +
                        physicalAddr + ")||'%')";
            } else {
                criteria =
                        "where ( UPPER(CLNT_PHYSICAL_ADDRS) like '%'||UPPER(" +
                        physicalAddr + ")||'%')";

            }
        } else if (rbtnPostalAddr.isSelected()) {

            if (postalAddr == null) {
                GlobalCC.INFORMATIONREPORTING("Postal Address Required");
                return null;
            }
            if (status == null && clntType == null && sector == null &&
                dFrom == null && dTo == null) {
                criteria =
                        "where( UPPER(CLNT_POSTAL_ADDRS) like '%'||UPPER(" + postalAddr +
                        ")||'%')";
            } else {
                criteria =
                        "where( UPPER(CLNT_POSTAL_ADDRS) like '%'||UPPER(" + postalAddr +
                        ")||'%')";
            }
        } else if (rbtnCustomerId.isSelected()) {
            if (shtDesc == null) {
                GlobalCC.INFORMATIONREPORTING("Old Customer Id  Required");
                return null;
            }
            criteria = ",tqc_clients_log \n" +
                    "where UPPER(tqc_clients.CLNT_CODE)=UPPER(tqc_clients_log.CLNT_CODE) \n" +
                    "AND  UPPER(CLNT_PREV_SHT_DESC) LIKE '%'||UPPER(" +
                    shtDesc + ")||'%' \n";

        } else if (rbtnOldNames.isSelected()) {
            if (oName != null && name != null) {
                GlobalCC.INFORMATIONREPORTING("Specify only One name:");
                return null;
            } else if (oName == null && name == null) {
                GlobalCC.INFORMATIONREPORTING("No name specified:");
                return null;
            }

            criteria = ",tqc_clients_log \n" +
                    "WHERE( ( UPPER(tqc_clients.CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                    name + ",'HAKUNA)'))||'%' \n" +
                    "OR UPPER(tqc_clients.CLNT_NAME) LIKE '%'|| UPPER(NVL(" +
                    name + ",'HAKUNA'))||'%'\n" +
                    "OR UPPER(tqc_clients.CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                    oName + ",'HAKUNA)'))||'%' \n" +
                    "OR UPPER(tqc_clients.CLNT_NAME) LIKE '%'|| UPPER(NVL(" +
                    oName + ",'HAKUNA'))||'%') ) \n" +
                    "AND ( tqc_clients.CLNT_CODE=tqc_clients_log.CLNT_CODE )\n";

        }

        else {
            GlobalCC.INFORMATIONREPORTING("Choose criteria::");
            return null;
        }

        session.setAttribute("searchCriteria", criteria);
        ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
        GlobalCC.refreshUI(tblClients);

        return null;

    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSearchCriteria(RichSelectOneChoice searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public RichSelectOneChoice getSearchCriteria() {
        return searchCriteria;
    }

    public void setTxtsearchCriteriaValue(RichInputText txtsearchCriteriaValue) {
        this.txtsearchCriteriaValue = txtsearchCriteriaValue;
    }

    public RichInputText getTxtsearchCriteriaValue() {
        return txtsearchCriteriaValue;
    }

    public void setTxtSearchShtDesc(RichInputText txtSearchShtDesc) {
        this.txtSearchShtDesc = txtSearchShtDesc;
    }

    public RichInputText getTxtSearchShtDesc() {
        return txtSearchShtDesc;
    }

    public void setTxtSearchName(RichInputText txtSearchName) {
        this.txtSearchName = txtSearchName;
    }

    public RichInputText getTxtSearchName() {
        return txtSearchName;
    }

    public void setTxtOtherName(RichInputText txtOtherName) {
        this.txtOtherName = txtOtherName;
    }

    public RichInputText getTxtOtherName() {
        return txtOtherName;
    }

    public void setTxtSearchOtherName(RichInputText txtSearchOtherName) {
        this.txtSearchOtherName = txtSearchOtherName;
    }

    public RichInputText getTxtSearchOtherName() {
        return txtSearchOtherName;
    }

    public void setTxtSearchSector(RichInputText txtSearchSector) {
        this.txtSearchSector = txtSearchSector;
    }

    public RichInputText getTxtSearchSector() {
        return txtSearchSector;
    }

    public void setTxtSearchShortDesc(RichInputText txtSearchShortDesc) {
        this.txtSearchShortDesc = txtSearchShortDesc;
    }

    public RichInputText getTxtSearchShortDesc() {
        return txtSearchShortDesc;
    }

    public void setTxtSearchStatus(RichSelectOneChoice txtSearchStatus) {
        this.txtSearchStatus = txtSearchStatus;
    }

    public RichSelectOneChoice getTxtSearchStatus() {
        return txtSearchStatus;
    }

    public void setTxtSearchClientType(RichPanelLabelAndMessage txtSearchClientType) {
        this.txtSearchClientType = txtSearchClientType;
    }

    public RichPanelLabelAndMessage getTxtSearchClientType() {
        return txtSearchClientType;
    }

    public void setTxtSearchClntType(RichSelectOneChoice txtSearchClntType) {
        this.txtSearchClntType = txtSearchClntType;
    }

    public RichSelectOneChoice getTxtSearchClntType() {
        return txtSearchClntType;
    }

    public void setRbtnExactNmBig(RichSelectBooleanRadio rbtnExactNmBig) {
        this.rbtnExactNmBig = rbtnExactNmBig;
    }

    public RichSelectBooleanRadio getRbtnExactNmBig() {
        return rbtnExactNmBig;
    }

    public void setRbtnPartNmInOrder(RichSelectBooleanRadio rbtnPartNmInOrder) {
        this.rbtnPartNmInOrder = rbtnPartNmInOrder;
    }

    public RichSelectBooleanRadio getRbtnPartNmInOrder() {
        return rbtnPartNmInOrder;
    }

    public void setRbtnAnyPartOfFirAndOthNm(RichSelectBooleanRadio rbtnAnyPartOfFirAndOthNm) {
        this.rbtnAnyPartOfFirAndOthNm = rbtnAnyPartOfFirAndOthNm;
    }

    public RichSelectBooleanRadio getRbtnAnyPartOfFirAndOthNm() {
        return rbtnAnyPartOfFirAndOthNm;
    }

    public void setRbtnBegPartOfAnyNm(RichSelectBooleanRadio rbtnBegPartOfAnyNm) {
        this.rbtnBegPartOfAnyNm = rbtnBegPartOfAnyNm;
    }

    public RichSelectBooleanRadio getRbtnBegPartOfAnyNm() {
        return rbtnBegPartOfAnyNm;
    }

    public void setRbtnPartOfAnyNameInOrder(RichSelectBooleanRadio rbtnPartOfAnyNameInOrder) {
        this.rbtnPartOfAnyNameInOrder = rbtnPartOfAnyNameInOrder;
    }

    public RichSelectBooleanRadio getRbtnPartOfAnyNameInOrder() {
        return rbtnPartOfAnyNameInOrder;
    }

    public void setRbtnExactName(RichSelectBooleanRadio rbtnExactName) {
        this.rbtnExactName = rbtnExactName;
    }

    public RichSelectBooleanRadio getRbtnExactName() {
        return rbtnExactName;
    }

    public void setRbtnPartOfAnyName(RichSelectBooleanRadio rbtnPartOfAnyName) {
        this.rbtnPartOfAnyName = rbtnPartOfAnyName;
    }

    public RichSelectBooleanRadio getRbtnPartOfAnyName() {
        return rbtnPartOfAnyName;
    }

    public void setRbtnBegPartOfFstNmAndOName(RichSelectBooleanRadio rbtnBegPartOfFstNmAndOName) {
        this.rbtnBegPartOfFstNmAndOName = rbtnBegPartOfFstNmAndOName;
    }

    public RichSelectBooleanRadio getRbtnBegPartOfFstNmAndOName() {
        return rbtnBegPartOfFstNmAndOName;
    }

    public void setSectorName(Object sectorName) {
        this.sectorName = sectorName;
    }

    public Object getSectorName() {
        return sectorName;
    }

    public void setRbtnShortDesc(RichSelectBooleanRadio rbtnShortDesc) {
        this.rbtnShortDesc = rbtnShortDesc;
    }

    public RichSelectBooleanRadio getRbtnShortDesc() {
        return rbtnShortDesc;
    }

    public void setRbtnClientType(RichSelectBooleanRadio rbtnClientType) {
        this.rbtnClientType = rbtnClientType;
    }

    public RichSelectBooleanRadio getRbtnClientType() {
        return rbtnClientType;
    }

    public void setRbtnStatus(RichSelectBooleanRadio rbtnStatus) {
        this.rbtnStatus = rbtnStatus;
    }

    public RichSelectBooleanRadio getRbtnStatus() {
        return rbtnStatus;
    }

    public void setRbtnSector(RichSelectBooleanRadio rbtnSector) {
        this.rbtnSector = rbtnSector;
    }

    public RichSelectBooleanRadio getRbtnSector() {
        return rbtnSector;
    }

    public void setRbtnPhySicalAddr(RichSelectBooleanRadio rbtnPhySicalAddr) {
        this.rbtnPhySicalAddr = rbtnPhySicalAddr;
    }

    public RichSelectBooleanRadio getRbtnPhySicalAddr() {
        return rbtnPhySicalAddr;
    }

    public void setRbtnPostalAddr(RichSelectBooleanRadio rbtnPostalAddr) {
        this.rbtnPostalAddr = rbtnPostalAddr;
    }

    public RichSelectBooleanRadio getRbtnPostalAddr() {
        return rbtnPostalAddr;
    }

    public void setTxtSearchPhysical(RichInputText txtSearchPhysical) {
        this.txtSearchPhysical = txtSearchPhysical;
    }

    public RichInputText getTxtSearchPhysical() {
        return txtSearchPhysical;
    }

    public void setTxtSearchPostal(RichInputText txtSearchPostal) {
        this.txtSearchPostal = txtSearchPostal;
    }

    public RichInputText getTxtSearchPostal() {
        return txtSearchPostal;
    }

    public void setRbtnCustomerId(RichSelectBooleanRadio rbtnCustomerId) {
        this.rbtnCustomerId = rbtnCustomerId;
    }

    public RichSelectBooleanRadio getRbtnCustomerId() {
        return rbtnCustomerId;
    }

    public void setRbtnIncome(RichSelectBooleanRadio rbtnIncome) {
        this.rbtnIncome = rbtnIncome;
    }

    public RichSelectBooleanRadio getRbtnIncome() {
        return rbtnIncome;
    }

    public void setTxtSearchOldId(RichInputText txtSearchOldId) {
        this.txtSearchOldId = txtSearchOldId;
    }

    public RichInputText getTxtSearchOldId() {
        return txtSearchOldId;
    }

    public void setRbtnOldNames(RichSelectBooleanRadio rbtnOldNames) {
        this.rbtnOldNames = rbtnOldNames;
    }

    public RichSelectBooleanRadio getRbtnOldNames() {
        return rbtnOldNames;
    }

    public void setBtnSectorLov(RichCommandButton btnSectorLov) {
        this.btnSectorLov = btnSectorLov;
    }

    public RichCommandButton getBtnSectorLov() {
        return btnSectorLov;
    }

    public void setSearchFormHolder(RichPanelGroupLayout searchFormHolder) {
        this.searchFormHolder = searchFormHolder;
    }

    public RichPanelGroupLayout getSearchFormHolder() {
        return searchFormHolder;
    }

    public void setClntDateCreatedFrom(RichInputDate clntDateCreatedFrom) {
        this.clntDateCreatedFrom = clntDateCreatedFrom;
    }

    public RichInputDate getClntDateCreatedFrom() {
        return clntDateCreatedFrom;
    }

    public void setClntDateCreatedTo(RichInputDate clntDateCreatedTo) {
        this.clntDateCreatedTo = clntDateCreatedTo;
    }

    public RichInputDate getClntDateCreatedTo() {
        return clntDateCreatedTo;
    }

    public void setRbtnDateCreated(RichSelectBooleanRadio rbtnDateCreated) {
        this.rbtnDateCreated = rbtnDateCreated;
    }

    public RichSelectBooleanRadio getRbtnDateCreated() {
        return rbtnDateCreated;
    }

    public void setTxtAccountNo(RichInputText txtAccountNo) {
        this.txtAccountNo = txtAccountNo;
    }

    public RichInputText getTxtAccountNo() {
        return txtAccountNo;
    }

    public void setRbtnSearchAccountNo(RichSelectBooleanRadio rbtnSearchAccountNo) {
        this.rbtnSearchAccountNo = rbtnSearchAccountNo;
    }

    public RichSelectBooleanRadio getRbtnSearchAccountNo() {
        return rbtnSearchAccountNo;
    }

    public void criteriaValueChangeListener(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != null &&
            valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (rbtnSearchAccountNo.isSelected()) {
                txtAccountNo.setDisabled(false);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                txtSearchPostal.setDisabled(true);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                btnSectorLov.setDisabled(true);
                GlobalCC.refreshUI(btnSectorLov);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                GlobalCC.refreshUI(gridClientSearchDetails);


            }
            if (rbtnCustomerId.isSelected()) {
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                btnSectorLov.setDisabled(true);
                GlobalCC.refreshUI(btnSectorLov);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(false);
                txtSearchStatus.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                GlobalCC.refreshUI(gridClientSearchDetails);

            } else if (rbtnShortDesc.isSelected()) {
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                txtSearchPostal.setDisabled(true);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);

                txtSearchShortDesc.setDisabled(false);


                txtSearchStatus.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                GlobalCC.refreshUI(gridClientSearchDetails);

            } else if (rbtnClientType.isSelected()) {
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchPostal.setDisabled(true);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);

                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);

                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchStatus.setDisabled(true);

                txtSearchClntType.setDisabled(false);


                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnStatus.isSelected()) {

                txtSearchStatus.setDisabled(false);
                txtSearchStatus.setValue(null);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchPostal.setDisabled(true);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);

                session.setAttribute("sectorName", null);

                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(false);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnPhySicalAddr.isSelected()) {
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);

                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                txtSearchPostal.setDisabled(true);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);

                txtSearchPhysical.setDisabled(false);


                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnPostalAddr.isSelected()) {
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setDisabled(true);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);

                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchPhysical.setDisabled(true);

                txtSearchPostal.setDisabled(false);


                txtSearchStatus.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                GlobalCC.refreshUI(gridClientSearchDetails);

            } else if (rbtnSector.isSelected()) {
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                txtSearchPostal.setDisabled(true);

                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);

                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);

                txtSearchSector.setDisabled(false);


                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);

                btnSectorLov.setDisabled(false);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnPartOfAnyName.isSelected()) {
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                txtSearchPostal.setDisabled(true);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);

                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);

                txtSearchName.setDisabled(false);


                txtSearchOtherName.setDisabled(false);

                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);

                btnSectorLov.setDisabled(true);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnOldNames.isSelected()) {
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                btnSectorLov.setDisabled(true);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                txtSearchPostal.setDisabled(true);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);

                txtSearchName.setDisabled(false);


                txtSearchOtherName.setDisabled(false);

                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnExactName.isSelected()) {
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);

                txtSearchName.setDisabled(false);


                txtSearchOtherName.setDisabled(false);

                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnDateCreated.isSelected()) {
                btnSectorLov.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);


                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setDisabled(false);


                clntDateCreatedTo.setDisabled(false);

                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(false);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(false);
                txtSearchStatus.setDisabled(false);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                btnSectorLov.setDisabled(false);
                GlobalCC.refreshUI(gridClientSearchDetails);
            }

        }
    }

    public void setGridClientSearchDetails(HtmlPanelGrid gridClientSearchDetails) {
        this.gridClientSearchDetails = gridClientSearchDetails;
    }

    public HtmlPanelGrid getGridClientSearchDetails() {
        return gridClientSearchDetails;
    }

    public void setStatusHolder(RichPanelLabelAndMessage statusHolder) {
        this.statusHolder = statusHolder;
    }

    public RichPanelLabelAndMessage getStatusHolder() {
        return statusHolder;
    }

    public void setTxtSrchSectorName(RichInputText txtSrchSectorName) {
        this.txtSrchSectorName = txtSrchSectorName;
    }

    public RichInputText getTxtSrchSectorName() {
        return txtSrchSectorName;
    }

    public void setResetSearchContainer(RichPanelLabelAndMessage resetSearchContainer) {
        this.resetSearchContainer = resetSearchContainer;
    }

    public RichPanelLabelAndMessage getResetSearchContainer() {
        return resetSearchContainer;
    }

    public void setTblSectorPop(RichTable tblSectorPop) {
        this.tblSectorPop = tblSectorPop;
    }

    public RichTable getTblSectorPop() {
        return tblSectorPop;
    }

    public void setSearchClientType(RichPanelLabelAndMessage searchClientType) {
        this.searchClientType = searchClientType;
    }

    public RichPanelLabelAndMessage getSearchClientType() {
        return searchClientType;
    }

    public void setSEARCHHOLDER(RichPanelFormLayout SEARCHHOLDER) {
        this.SEARCHHOLDER = SEARCHHOLDER;
    }

    public RichPanelFormLayout getSEARCHHOLDER() {
        return SEARCHHOLDER;
    }

    public void setTblBankBranches(RichTable tblBankBranches) {
        this.tblBankBranches = tblBankBranches;
    }

    public RichTable getTblBankBranches() {
        return tblBankBranches;
    }

    public void setTxtBankBranch(RichInputText txtBankBranch) {
        this.txtBankBranch = txtBankBranch;
    }

    public RichInputText getTxtBankBranch() {
        return txtBankBranch;
    }

    public void setTxtBankCode(RichInputText txtBankCode) {
        this.txtBankCode = txtBankCode;
    }

    public RichInputText getTxtBankCode() {
        return txtBankCode;
    }

    public void setTxtBankBranchCode(RichInputText txtBankBranchCode) {
        this.txtBankBranchCode = txtBankBranchCode;
    }

    public RichInputText getTxtBankBranchCode() {
        return txtBankBranchCode;
    }

    public void setReservWrds(RichTable reservWrds) {
        this.reservWrds = reservWrds;
    }

    public RichTable getReservWrds() {
        return reservWrds;
    }

    public void setSystems(RichTree systems) {
        this.systems = systems;
    }

    public RichTree getSystems() {
        return systems;
    }

    public void systemSelected(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet rowKeySet = selectionEvent.getAddedSet();
            Object key2 = rowKeySet.iterator().next();
            systems.setRowKey(key2);
            JUCtrlValueBinding nodeBinding =
                (JUCtrlValueBinding)systems.getRowData();
            if (nodeBinding != null) {
                session.setAttribute("sysCode",
                                     nodeBinding.getAttribute("code"));
                ADFUtils.findIterator("findReservedWordsIterator").executeQuery();
                GlobalCC.refreshUI(reservWrds);
            }
        }
    }

    public void setCode(RichInputText code) {
        this.code = code;
    }

    public RichInputText getCode() {
        return code;
    }

    public void setType(RichSelectOneChoice type) {
        this.type = type;
    }

    public RichSelectOneChoice getType() {
        return type;
    }

    public void setWord(RichInputText word) {
        this.word = word;
    }

    public RichInputText getWord() {
        return word;
    }

    public void setDesc(RichInputText desc) {
        this.desc = desc;
    }

    public RichInputText getDesc() {
        return desc;
    }

    public void setCategory(RichInputText category) {
        this.category = category;
    }

    public RichInputText getCategory() {
        return category;
    }

    public void setCategories(RichTable categories) {
        this.categories = categories;
    }

    public RichTable getCategories() {
        return categories;
    }

    public String newWord() {
        GlobalCC.showPopup("crm:word");
        code.setValue(null);
        type.setValue(null);
        word.setValue(null);
        desc.setValue(null);
        category.setValue(null);
        category.setLabel(null);
        return null;
    }

    public String editWord() {
        Object key = reservWrds.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        if (nodeBinding.getAttribute("editable").toString().equalsIgnoreCase("N")) {
            GlobalCC.errorValueNotEntered(" The Key Word Selected Cannot be Altered");
            return null;
        }
        GlobalCC.showPopup("crm:word");
        code.setValue(nodeBinding.getAttribute("code"));
        type.setValue(nodeBinding.getAttribute("type"));
        word.setValue(nodeBinding.getAttribute("name"));
        desc.setValue(nodeBinding.getAttribute("desc"));
        category.setValue(nodeBinding.getAttribute("tsrcName"));
        category.setLabel(nodeBinding.getAttribute("tsrcCode").toString());
        GlobalCC.refreshUI(type);
        return null;
    }

    public String deleteWord() {
        Object key = reservWrds.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        if (nodeBinding.getAttribute("editable").toString().equalsIgnoreCase("N")) {
            GlobalCC.errorValueNotEntered(" The Key Word Selected Cannot be Altered");
            return null;
        }
        DBConnector dbConnect = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;
        try {
            conn = dbConnect.getDatabaseConnection();
            String queryString =
                "begin TQC_SETUPS_PKG.reservedWords_proc(?,?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(queryString);
            cst.setObject(1, "D");
            cst.setObject(2, nodeBinding.getAttribute("code"));
            cst.setObject(3, null);
            cst.setObject(4, null);
            cst.setObject(5, null);
            cst.setObject(6, null);
            cst.setObject(7, null);
            cst.execute();
            cst.close();
            conn.close();
            ADFUtils.findIterator("findReservedWordsIterator").executeQuery();
            GlobalCC.refreshUI(reservWrds);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String launchCategory() {
        GlobalCC.showPopup("crm:category");
        return null;
    }

    public String saveWord() {
        String codeVal;
        String typeVal;
        String nameVal;
        String descVal;
        String catVal;
        codeVal = GlobalCC.checkNullValues(code.getValue());
        typeVal = GlobalCC.checkNullValues(type.getValue());
        nameVal = GlobalCC.checkNullValues(word.getValue());
        descVal = GlobalCC.checkNullValues(desc.getValue());
        catVal = GlobalCC.checkNullValues(category.getValue());
        if (typeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Type");
            return null;
        }
        if (nameVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Word");
            return null;
        }
        if (catVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Category");
            return null;
        }

        if (descVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Description");
            return null;
        }
        catVal = category.getLabel();
        DBConnector dbConnect = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;
        try {
            conn = dbConnect.getDatabaseConnection();
            String queryString =
                "begin TQC_SETUPS_PKG.reservedWords_proc(?,?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(queryString);
            if (codeVal == null) {
                cst.setObject(1, "A");
            } else {
                cst.setObject(1, "E");
            }
            cst.setObject(2, codeVal);
            cst.setObject(3, session.getAttribute("sysCode"));
            cst.setObject(4, catVal);
            cst.setObject(5, typeVal);
            cst.setObject(6, nameVal);
            cst.setObject(7, descVal);
            cst.execute();
            cst.close();
            conn.close();
            GlobalCC.hidePopup("crm:word");
            ADFUtils.findIterator("findReservedWordsIterator").executeQuery();
            GlobalCC.refreshUI(reservWrds);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String selectCategory() {
        Object key = categories.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        category.setValue(nodeBinding.getAttribute("tsrcName"));
        category.setLabel(nodeBinding.getAttribute("tsrcCode").toString());
        GlobalCC.refreshUI(category);
        GlobalCC.hidePopup("crm:category");
        return null;
    }
   
}
