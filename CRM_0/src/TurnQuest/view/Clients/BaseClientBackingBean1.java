package TurnQuest.view.Clients;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.Rendering;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichColumn;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanRadio;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelLabelAndMessage;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;
import oracle.adf.view.rich.component.rich.output.RichPanelCollection;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.RowKeySetImpl;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class BaseClientBackingBean1 {
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
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

    private Object sectorName;
    private RichSelectBooleanRadio rbtnShortDesc;
    private RichCommandButton btnActionEditGroupClient;
    private RichTable tblClientGroup;
    private RichCommandButton btnActionDeleteGroupClient;
    private RichCommandButton btnRemoveClientFromGroup;
    private RichTable tblClientGrpMembers;
    private RichInputText txtGrpCode;
    private RichInputText txtGrpName;
    private RichInputNumberSpinbox txtGrpMinimum;
    private RichInputNumberSpinbox txtGrpMax;
    private RichCommandButton btnsaveGrp;
    private RichTable tblClientPop;
    private RichSelectBooleanCheckbox columnSelect;
    private RichSelectBooleanCheckbox grpMemberSelect;
    private RichCommandButton btnSelectAllGrpMembers;
    private RichCommandButton resetContainer;
    private RichPanelLabelAndMessage resetSearchContainer;
    private RichInputNumberSpinbox txtBranchMgrSeqNo;
    private RichCommandButton btnAddGrpMember;
    private RichCommandButton btnDeselectAll;
    private RichCommandButton btnSelectAll;
    private RichPanelCollection tblClientHolder;
    private RichPanelGroupLayout searchFormHolder;
    private RichInputDate clntDateCreatedFrom;
    private RichInputDate clntDateCreatedTo;

    private RichColumn checkboxCol;
    private RichColumn editCol;
    private RichSelectBooleanRadio rbtnDateCreated;
    private RichSelectBooleanRadio rbtnClientType;
    private RichSelectBooleanRadio rbtnStatus;
    private RichSelectBooleanRadio rbtnSector;
    private RichSelectBooleanRadio rbtnPhySicalAddr;
    private RichSelectBooleanRadio rbtnPostalAddr;
    private RichSelectBooleanRadio rbtnSearchAccountNo;
    private RichSelectBooleanRadio rbtnExactNmBig;
    private RichSelectBooleanRadio rbtnPartNmInOrder;
    private RichSelectBooleanRadio rbtnAnyPartOfFirAndOthNm;
    private RichSelectBooleanRadio rbtnBegPartOfAnyNm;
    private RichSelectBooleanRadio rbtnPartOfAnyNameInOrder;
    private RichSelectBooleanRadio rbtnExactName;
    private RichSelectBooleanRadio rbtnPartOfAnyName;
    private RichSelectBooleanRadio rbtnBegPartOfFstNmAndOName;
    private RichInputText txtSearchPhysical;
    private RichInputText txtSearchPostal;
    private RichSelectBooleanRadio rbtnCustomerId;
    private RichSelectBooleanRadio rbtnIncome;
    private RichInputText txtSearchOldId;
    private RichSelectBooleanRadio rbtnOldNames;
    private RichCommandButton btnSectorLov;
    private RichTable tblAccountOfficers;
    private RichCommandButton btnEditAccOfficer;
    private RichOutputLabel lbCount;
    private RichTable tblAccClients;
    private RichCommandButton btnChangeAccOfficer;
    private RichTable tblUserList;
    private RichInputText baseClientBackingBean1;
    private RichInputText txtAccountNo;

    private HtmlPanelGrid gridClientSearchDetails;
    private RichSelectBooleanRadio rbtnShortDescLeg;
    private RichSelectBooleanRadio rdbnDateOfBirth;
    private RichInputDate txtDateOfBirth;
    private RichSelectBooleanRadio rbtnIdNumber;
    private RichInputText txtIdNum;
    private RichSelectBooleanRadio txtClientPolNumber;
    private RichSelectBooleanRadio sbrClaimNo;
    private RichInputText clientPolicyNumber;
    private RichInputText txtClaimNo;
    private RichSelectBooleanRadio txtPinNumber;
    private RichInputText pinNumber;
    private RichSelectBooleanRadio txtPassPortNumber;
    private RichInputText txtPassport;
    private RichInputText txtTelNo;
    private RichSelectBooleanRadio txtTelNoRadio;
    private RichSelectBooleanRadio txtVehicleRegNoRadio; 
    private RichInputText txtOldAccountNo;
    private RichInputText txtVehicleRegNo; 
    private RichSelectBooleanRadio rbtnSearchOldAccountNo;


    public BaseClientBackingBean1() {
    }

    Rendering renderer = new Rendering();

    public void actionSearchCriterialistener(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue() &&
            evt.getNewValue() != null) {
            RichSelectOneChoice myComp =
                (RichSelectOneChoice)evt.getComponent();
            session.setAttribute("searchCriteria", myComp.getValue());


        }

    }

    public static java.sql.Date extractDate(RichInputDate component) {
        java.sql.Date val = null;
        try {
            val = new java.sql.Date(((Date)component.getValue()).getTime());
        } catch (Exception ex) {
            val = null;
        }
        return val;
    }

    public String actionAcceptSearchCriteria() {
        java.sql.Date dob = null;
        java.util.Date dateBirth;
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        String name = null;
        String oName = null;
        String shtDesc = null;
        String clntType = null;
        String status = null;
        String postalAddr = null;
        String physicalAddr = null;
        String oldId = null;
        String pin;
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
        String dateOfBirth;
        String idNumber;
        String policyNumber;
        String passport;
        String searchShtDesc =
            GlobalCC.checkNullValues(txtSearchShortDesc.getValue());
        String searchAccountNo =
            GlobalCC.checkNullValues(txtAccountNo.getValue()); //client account NO.

        String searchOldAccountNo =
            GlobalCC.checkNullValues(txtOldAccountNo.getValue()); //client Old account NO.


        String telNo = GlobalCC.checkNullValues(txtTelNo.getValue());
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
        
        String claimNo = GlobalCC.checkNullValues(txtClaimNo.getValue());
        
        String vehicleRegNo = GlobalCC.checkNullValues(txtVehicleRegNo.getValue()); 

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
        if (txtDateOfBirth.getValue() != null) {
            dateOfBirth = GlobalCC.extractDate(txtDateOfBirth).toString();
        } else {
            dateOfBirth = null;
        }
        if (pinNumber.getValue() != null) {
            pin = pinNumber.getValue().toString();
        } else {
            pin = null;
        }
        if (txtPassport.getValue() != null) {
            passport = txtPassport.getValue().toString();
        } else {
            passport = null;
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

        if (txtIdNum.getValue() != null) {
            idNumber = txtIdNum.getValue().toString();
        } else {
            idNumber = null;
        }
        if (clientPolicyNumber.getValue() != null) {
            policyNumber = clientPolicyNumber.getValue().toString();
        } else {
            policyNumber = null;
        }

        if (rbtnSearchAccountNo.isSelected()) {
            if (searchAccountNo == null || searchAccountNo == "") {
                GlobalCC.EXCEPTIONREPORTING("Specify  Client Account Number");
                return null;
            }

            criteria = "WHERE CLNT_ACCNT_NO = '" + searchAccountNo + "'";

        }


        else if (rbtnSearchOldAccountNo.isSelected()) {
            if (searchOldAccountNo == null || searchOldAccountNo == "") {
                GlobalCC.EXCEPTIONREPORTING("Specify  Client Old Account Number");
                return null;
            }

            criteria =
                    "WHERE CLNT_OLD_ACCNT_NO = '" + searchOldAccountNo + "'";

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
                        "  CLNT_STATUS=NVL(" +

                        status + " ,CLNT_STATUS)" +
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
                        "            and CLNT_TYPE=nvl(" + clntType +

                        ",CLNT_TYPE) \n" +
                        "           AND CLNT_STATUS=NVL(" + status +
                        " ,CLNT_STATUS)" +
                        " AND (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                        dFrom +
                        ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" + dTo +
                        ",'01/01/2099'),'dd/mm/yyyy')))";
            }


        }


        else if (rbtnOldNames.isSelected()) {
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
                        "  CLNT_STATUS=NVL(" +

                        status + " ,CLNT_STATUS)" +
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
                        "            and CLNT_TYPE=nvl(" + clntType +

                        ",CLNT_TYPE) \n" +
                        "           AND CLNT_STATUS=NVL(" + status +
                        " ,CLNT_STATUS)" +
                        " AND (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                        dFrom +
                        ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" + dTo +
                        ",'01/01/2099'),'dd/mm/yyyy')))";
            }


        }


        else if (rbtnExactName.isSelected()) {
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
                            "WHERE ( UPPER(CLNT_NAME||' '||CLNT_OTHER_NAMES) LIKE '%'||UPPER(" +
                            name + ")||'%' )";

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
        } else if (rbtnShortDescLeg.isSelected()) {
            if (shtDesc == null) {
                GlobalCC.INFORMATIONREPORTING("Short Desc Required");
                return null;
            }
            if (status == null && clntType == null && sector == null &&
                dFrom == null && dTo == null) {
                criteria =
                        " WHERE ( UPPER( CLNT_OLD_SHT_DESC ) LIKE '%'||NVL(UPPER(" +
                        shtDesc + "),'HAKUNA')||'%')";
            } else {
                criteria =
                        " WHERE ( UPPER( CLNT_OLD_SHT_DESC ) LIKE '%'||NVL(UPPER(" +
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

        } else if (rdbnDateOfBirth.isSelected()) {
            criteria =
                    "WHERE(CLNT_DOB=to_date(NVL(" + "'" + dateOfBirth + "'" +
                    ",'1900-10-10'),'RRRR-MM-DD'))";
        } else if (rbtnIdNumber.isSelected()) {
            criteria =
                    "WHERE(CLNT_ID_REG_NO=NVL(" + "'" + idNumber + "'" + ",'HAKUNA'))";
            //          "where( UPPER(CLNT_POSTAL_ADDRS) like '%'||UPPER(" + txtIdNum +
            //          ")||'%')";
        }else if (txtClientPolNumber.isSelected()) {
            
            criteria = "WHERE (  \n" + 
                        "                clnt_code IN ( \n" + 
                        "                        SELECT gin_policies.pol_prp_code FROM gin_policies WHERE  gin_policies.pol_policy_no = NVL(" + "'" + policyNumber + "'" + ",'HAKUNA')\n" + 
                        "                            UNION \n" + 
                        "                        SELECT lms_policies.pol_prp_code FROM lms_policies WHERE  lms_policies.pol_policy_no = NVL(" + "'" + policyNumber + "'" + ",'HAKUNA')\n" + 
                        "                   ) \n" + 
                        "            )";

        } else if (txtPinNumber.isSelected()) {
            criteria = "WHERE(CLNT_PIN=NVL(" + "'" + pin + "'" + ",'HAKUNA'))";
            //          "where( UPPER(CLNT_POSTAL_ADDRS) like '%'||UPPER(" + txtIdNum +
            //          ")||'%')";
        } else if (txtPassPortNumber.isSelected()) {
            criteria =
                    "WHERE(CLNT_PASSPORT_NO=NVL(" + "'" + passport + "'" + ",'HAKUNA'))";
            //          "where( UPPER(CLNT_POSTAL_ADDRS) like '%'||UPPER(" + txtIdNum +
            //          ")||'%')";
        } else if (txtTelNoRadio.isSelected()) {

            if (telNo != null) {
                if (!telNo.matches("^[\\+]{0,1}[0-9\\-]+$")) {
                    GlobalCC.errorValueNotEntered("Invalid Telephone No!");
                    return null;
                }
            }
            criteria =
                    " WHERE ( UPPER(CLNT_SMS_TEL) LIKE '%'|| UPPER(NVL('" + telNo +
                    "','HAKUNA'))||'%'\n" +
                    "            OR UPPER(CLNT_TEL) LIKE '%'|| UPPER(NVL('" +
                    telNo + "','HAKUNA'))||'%' OR " +
                    "UPPER(CLNT_TEL2) LIKE '%'|| UPPER(NVL('" + telNo +
                    "','HAKUNA'))||'%')";
        } else if (sbrClaimNo.isSelected() && claimNo != null) {

            //criteria = "WHERE (( UPPER(CMB_CLAIM_NO) LIKE '%'||'" + claimNo + "'||'%') OR  ( UPPER(CLM_NO) LIKE '%'||'" + claimNo + "'||'%') )";
            criteria =
                    "WHERE CLNT_CODE=(select CMB_CLIENT_PRP_CODE from gin_claim_master_bookings where CMB_CLAIM_NO ='" +
                    claimNo + "')";
        }

        else if (rbtnDateCreated.isSelected()) {

            criteria =
                    "WHERE (  CLNT_DATE_CREATED BETWEEN to_date(NVL(" + dFrom +
                    ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" + dTo +
                    ",'01/01/2099'),'dd/mm/yyyy'))";


        }else if (txtVehicleRegNoRadio.isSelected() && vehicleRegNo != null) {

            criteria =
                    "WHERE CLNT_CODE IN (SELECT ipu_prp_code from gin_insured_property_unds WHERE ipu_property_id ='" +
                    vehicleRegNo + "')";
        }


        else {

            GlobalCC.INFORMATIONREPORTING("Put Search Criteria parameters::");
            return null;
        }

        System.out.println(criteria);
        session.setAttribute("searchCriteria", criteria);
        ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
        GlobalCC.refreshUI(tblClientPop);

        return null;

    }

    public String actionNewGroup() {
        txtGrpCode.setValue(null);
        txtGrpName.setValue(null);
        txtGrpMinimum.setValue(null);
        txtGrpMax.setValue(null);
        btnsaveGrp.setText("Save");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:addGrpClient" + "').show(hints);");
        return null;
    }

    public String actionEditGroupClient() {

        if (session.getAttribute("grpCode") != null) {

            Object key2 = tblClientGroup.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
            txtGrpCode.setValue(null);
            txtGrpName.setValue(null);
            txtGrpMinimum.setValue(null);
            txtGrpMax.setValue(null);

            if (nodeBinding != null) {
                txtGrpCode.setValue(nodeBinding.getAttribute("grp_Code"));
                txtGrpName.setValue(nodeBinding.getAttribute("grp_Name"));
                txtGrpMinimum.setValue(nodeBinding.getAttribute("grp_Minimum"));
                txtGrpMax.setValue(nodeBinding.getAttribute("grp_Maximum"));
                btnsaveGrp.setText("update");

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "pt1:addGrpClient" + "').show(hints);");

            } else {
                GlobalCC.INFORMATIONREPORTING("No record selected::");
                return null;
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected::");
            return null;
        }
        return null;
    }

    public void actionConfirmDeleteClntGrp(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            actionDeleteGroupClient();
        }
    }

    public String actionConfirmDeleteClntGrpMember(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            return null;
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {

            if (checkIfAnyTableRowselected(tblClientGrpMembers) == true) {

                RowKeySet rks = new RowKeySetImpl();

                int rowcount = tblClientGrpMembers.getRowCount();

                for (int i = 0; i < rowcount; i++) {
                    tblClientGrpMembers.setRowIndex(i);
                    Object key = tblClientGrpMembers.getRowKey();
                    tblClientGrpMembers.setRowKey(key);
                    JUCtrlHierNodeBinding nodeBinding =
                        (JUCtrlHierNodeBinding)tblClientGrpMembers.getRowData();


                    if (nodeBinding.getAttribute("selected").toString().equalsIgnoreCase("true")) {

                        DBConnector dbConnector = new DBConnector();
                        OracleConnection conn = null;
                        OracleCallableStatement statement = null;
                        try {
                            conn = dbConnector.getDatabaseConnection();
                            String query =
                                "begin TQC_SETUPS_PKG.clientGroupMembers_prc(?,?,?,?); end;";
                            statement =
                                    (OracleCallableStatement)conn.prepareCall(query);
                            statement.setString(1, "D");
                            statement.setBigDecimal(2,
                                                    new BigDecimal(nodeBinding.getAttribute("grpd_Code").toString()));
                            statement.setBigDecimal(3, null);
                            statement.setBigDecimal(4, null);
                            statement.execute();

                            statement.close();
                            conn.commit();
                            conn.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                            GlobalCC.EXCEPTIONREPORTING(conn, e);
                            return null;
                        }
                    }
                }
                //btnRemoveClientFromGroup.setDisabled(true);
                GlobalCC.refreshUI(btnRemoveClientFromGroup);
                GlobalCC.INFORMATIONREPORTING("Client(s) Deleted Successfully");
                // ADFUtils.findIterator("fetchAllClientsIterator").executeQuery();
                // GlobalCC.refreshUI(tblClientPop);
                ADFUtils.findIterator("findClientGroupMembersIterator").executeQuery();
                GlobalCC.refreshUI(tblClientGrpMembers);
                return null;


            } else {
                GlobalCC.INFORMATIONREPORTING("No  Record  Selected");
                return null;
            }
        }
        return null;
    }

    public String actionDeleteGroupClient() {
        Object key2 = tblClientGroup.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;


        if (nodeBinding != null) {


            DBConnector dbCon = new DBConnector();
            OracleConnection conn = null;
            try {
                conn = dbCon.getDatabaseConnection();

                String query =
                    "begin TQC_SETUPS_PKG.clientGroup_prc(?,?,?,?,?); end;";
                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  new BigDecimal(session.getAttribute("grpCode").toString()));
                cst.setString(3, null);
                cst.setBigDecimal(4, null);
                cst.setBigDecimal(5, null);

                cst.execute();
                cst.close();
                conn.commit();
                conn.close();
                GlobalCC.INFORMATIONREPORTING("Record Successfully Deleted");
                btnActionDeleteGroupClient.setDisabled(true);
                GlobalCC.refreshUI(btnActionDeleteGroupClient);
                ADFUtils.findIterator("findClientGroupsIterator").executeQuery();
                GlobalCC.refreshUI(tblClientGroup);
                ADFUtils.findIterator("findClientGroupMembersIterator").executeQuery();
                GlobalCC.refreshUI(tblClientGrpMembers);

                return null;

            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                return null;
            }

        } else {
            GlobalCC.INFORMATIONREPORTING("No record Selected::");
            return null;
        }
    }

    public String actionHideUserGroupLov() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:addClientGrpPopt" + "').hide(hints);");
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
        txtTelNo.setValue(null);
        txtVehicleRegNo.setValue(null); 
        
        session.setAttribute("sectorCode", null);
        clntDateCreatedFrom.setValue(null);
        clntDateCreatedTo.setValue(null);

        session.setAttribute("sectorName", null);
        session.setAttribute("searchClntStatus", null);
        session.setAttribute("searchClntType", null);
        session.setAttribute("sectorCode", null);
        List<UIComponent> children = resetSearchContainer.getChildren();
        UIComponent component = children.get(0);
        RichInputText sector = (RichInputText)component;
        sector.setValue(null);
        //activate components
        txtSearchName.setDisabled(true);
        txtTelNo.setDisabled(true);
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
        txtVehicleRegNo.setDisabled(true); 

        //refresh radio buttons
        rbtnPartOfAnyName.setSelected(false);
        rbtnExactName.setSelected(false);
        rbtnSector.setSelected(false);
        rbtnStatus.setSelected(false);
        rbtnClientType.setSelected(false);
        rbtnShortDesc.setSelected(false);
        rbtnShortDescLeg.setSelected(false);
        rbtnDateCreated.setSelected(false);
        rbtnPhySicalAddr.setSelected(false);
        rbtnPostalAddr.setSelected(false);
        rbtnOldNames.setSelected(false);
        rbtnCustomerId.setSelected(false);
        rbtnSearchAccountNo.setSelected(false);
        txtVehicleRegNoRadio.setSelected(false); 

        GlobalCC.refreshUI(rbtnSearchAccountNo);
        GlobalCC.refreshUI(rbtnPartOfAnyName);
        GlobalCC.refreshUI(rbtnExactName);
        GlobalCC.refreshUI(rbtnSector);
        GlobalCC.refreshUI(rbtnStatus);
        GlobalCC.refreshUI(rbtnClientType);
        GlobalCC.refreshUI(rbtnShortDesc);
        GlobalCC.refreshUI(rbtnShortDescLeg);
        GlobalCC.refreshUI(rbtnDateCreated);
        GlobalCC.refreshUI(rbtnPhySicalAddr);
        GlobalCC.refreshUI(rbtnPostalAddr);
        GlobalCC.refreshUI(rbtnOldNames);
        GlobalCC.refreshUI(rbtnCustomerId);
        //refesh components
        GlobalCC.refreshUI(txtAccountNo);
        GlobalCC.refreshUI(sector);
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
        GlobalCC.refreshUI(txtVehicleRegNo);
        GlobalCC.refreshUI(txtVehicleRegNoRadio); 
        
        
        session.setAttribute("searchCriteria", null);
        ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
        GlobalCC.refreshUI(tblClientPop);

        return null;
    }

    public String actionAddClientToGroup() {
        if (session.getAttribute("grpCode") != null) {
            actionResetSearch();
            btnSelectAll.setVisible(true);
            btnDeselectAll.setVisible(true);
            btnAddGrpMember.setVisible(true);
            session.setAttribute("_search", "addGroup");
            checkboxCol.setVisible(true);
            GlobalCC.refreshUI(tblClientPop);
            GlobalCC.refreshUI(btnSelectAll);
            GlobalCC.refreshUI(btnDeselectAll);
            GlobalCC.refreshUI(btnAddGrpMember);

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:searchClientPop" + "').show(hints);");
            return null;
        } else {

            GlobalCC.INFORMATIONREPORTING("First select  The group::");

            return null;
        }

    }

    public String actionShowConfirmDelete() {
        if (session.getAttribute("grpCode") != null) {

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:confirmDeleteClientGrp" +
                                 "').show(hints);");
            return null;
        } else {

            GlobalCC.INFORMATIONREPORTING("First select  The group::");

            return null;
        }

    }

    public String actionSaveGroupClientele() {

        if (btnsaveGrp.getText().equalsIgnoreCase("Save")) {

            String name = GlobalCC.checkNullValues(txtGrpName.getValue());
            String min = GlobalCC.checkNullValues(txtGrpMinimum.getValue());
            String max = GlobalCC.checkNullValues(txtGrpMax.getValue());

            if (name == null) {
                GlobalCC.errorValueNotEntered("Group name required::");
                return null;
            }
            if (min == null)

            {
                GlobalCC.errorValueNotEntered("Min number  required::");
                return null;
            }
            if (max == null)

            {
                GlobalCC.errorValueNotEntered("Max number required::");
                return null;

            }


            DBConnector dbCon = new DBConnector();
            OracleConnection conn = null;
            try {
                conn = dbCon.getDatabaseConnection();

                String query =
                    "begin TQC_SETUPS_PKG.clientGroup_prc(?,?,?,?,?); end;";
                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setString(3, name);
                cst.setBigDecimal(4, new BigDecimal(min));
                cst.setBigDecimal(5, new BigDecimal(max));

                cst.execute();
                cst.close();
                conn.commit();
                conn.close();


                GlobalCC.INFORMATIONREPORTING("Record Successfully saved");
                ADFUtils.findIterator("findClientGroupsIterator").executeQuery();
                GlobalCC.refreshUI(tblClientGroup);
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "pt1:addGrpClient" + "').hide(hints);");
                return null;

            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                return null;
            }


        } else {

            String name = GlobalCC.checkNullValues(txtGrpName.getValue());
            String min = GlobalCC.checkNullValues(txtGrpMinimum.getValue());
            String max = GlobalCC.checkNullValues(txtGrpMax.getValue());
            String code = GlobalCC.checkNullValues(txtGrpCode.getValue());
            if (name == null) {
                GlobalCC.errorValueNotEntered("Group name required::");
                return null;
            } else if (min == null)

            {
                GlobalCC.errorValueNotEntered("Min number  required::");
                return null;
            } else if (max == null)

            {
                GlobalCC.errorValueNotEntered("Max number required::");
                return null;

            } else if (code == null)

            {
                GlobalCC.errorValueNotEntered("Grp Code required::");
                return null;

            }


            DBConnector dbCon = new DBConnector();
            OracleConnection conn = null;
            try {
                conn = dbCon.getDatabaseConnection();

                String query =
                    "begin TQC_SETUPS_PKG.clientGroup_prc(?,?,?,?,?); end;";
                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.setString(1, "E");
                cst.setBigDecimal(2, new BigDecimal(code));
                cst.setString(3, name);
                cst.setBigDecimal(4, new BigDecimal(min));
                cst.setBigDecimal(5, new BigDecimal(max));

                cst.execute();
                cst.close();
                conn.commit();
                conn.close();
                GlobalCC.INFORMATIONREPORTING("Record Successfully Updated");
                btnActionEditGroupClient.setDisabled(false);
                GlobalCC.refreshUI(btnActionEditGroupClient);
                ADFUtils.findIterator("findClientGroupsIterator").executeQuery();
                GlobalCC.refreshUI(tblClientGroup);
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "pt1:addGrpClient" + "').hide(hints);");
                return null;

            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                return null;
            }


        }


    }

    public boolean checkIfAnyRowSelected() {

        RowKeySet rks = new RowKeySetImpl();

        int rowcount = tblClientPop.getRowCount();

        int count = 0;
        for (int i = 0; i < rowcount; i++) {
            tblClientPop.setRowIndex(i);
            Object key = tblClientPop.getRowKey();
            tblClientPop.setRowKey(key);
            JUCtrlHierNodeBinding nodeBinding =
                (JUCtrlHierNodeBinding)tblClientPop.getRowData();

            if (nodeBinding.getAttribute("selected").toString().equalsIgnoreCase("true")) {

                count = count + 1;
            }

        }
        if (count > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean checkIfAnyTableRowselected(RichTable table) {


        RowKeySet rks = new RowKeySetImpl();


        int rowcount = table.getRowCount();

        int count = 0;
        for (int i = 0; i < rowcount; i++) {
            table.setRowIndex(i);
            Object key = table.getRowKey();
            table.setRowKey(key);
            JUCtrlHierNodeBinding nodeBinding =
                (JUCtrlHierNodeBinding)table.getRowData();

            if (nodeBinding.getAttribute("selected").toString().equalsIgnoreCase("true")) {

                count = count + 1;
            }

        }

        if (count > 0) {
            return true;
        } else {
            return false;
        }

    }

    public String selectAll() {

        RowKeySet rks = new RowKeySetImpl();

        int rowcount = tblClientPop.getRowCount();


        for (int i = 0; i < rowcount; i++) {
            tblClientPop.setRowIndex(i);
            Object key = tblClientPop.getRowKey();
            tblClientPop.setRowKey(key);
            JUCtrlHierNodeBinding nodeBinding =
                (JUCtrlHierNodeBinding)tblClientPop.getRowData();
            columnSelect.setSelected(true);
            GlobalCC.refreshUI(columnSelect);
        }
        return null;

    }

    public String deselectAll() {

        RowKeySet rks = new RowKeySetImpl();
        if (checkIfAnyTableRowselected(tblClientPop)) {
            int rowcount = tblClientPop.getRowCount();


            for (int i = 0; i < rowcount; i++) {
                tblClientPop.setRowIndex(i);
                Object key = tblClientPop.getRowKey();
                tblClientPop.setRowKey(key);
                JUCtrlHierNodeBinding nodeBinding =
                    (JUCtrlHierNodeBinding)tblClientPop.getRowData();

                columnSelect.setSelected(false);
                GlobalCC.refreshUI(columnSelect);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record(s) selected::");
        }
        return null;

    }

    public String actionAcceptClientLov() {
        if (checkIfAnyRowSelected() == true) {
            Object xy = null;
            RowKeySet rks = new RowKeySetImpl();

            int rowcount = tblClientPop.getRowCount();

            for (int i = 0; i < rowcount; i++) {
                tblClientPop.setRowIndex(i);
                Object key = tblClientPop.getRowKey();
                tblClientPop.setRowKey(key);
                JUCtrlHierNodeBinding nodeBinding =
                    (JUCtrlHierNodeBinding)tblClientPop.getRowData();


                Object selected = nodeBinding.getAttribute("selected");

                if (selected.toString().equalsIgnoreCase("true")) {
                    if (session.getAttribute("grpCode") != null) {

                        DBConnector dbConnector = new DBConnector();
                        OracleConnection conn = null;
                        OracleCallableStatement statement = null;
                        try {
                            conn = dbConnector.getDatabaseConnection();
                            String query =
                                "begin TQC_SETUPS_PKG.clientGroupMembers_prc(?,?,?,?); end;";

                            statement =
                                    (OracleCallableStatement)conn.prepareCall(query);
                            statement.setString(1, "A");
                            statement.setBigDecimal(2, null);
                            statement.setBigDecimal(3,
                                                    new BigDecimal(nodeBinding.getAttribute("code").toString()));
                            statement.setBigDecimal(4,
                                                    new BigDecimal(session.getAttribute("grpCode").toString()));
                            statement.execute();

                            statement.close();
                            conn.commit();
                            conn.close();


                        } catch (Exception e) {
                            e.printStackTrace();
                            GlobalCC.EXCEPTIONREPORTING(conn, e);
                            return null;
                        }


                    } else {
                        GlobalCC.INFORMATIONREPORTING("First Select Client Group ::");
                        return null;
                    }


                }

            }

            ADFUtils.findIterator("findClientGroupMembersIterator").executeQuery();
            GlobalCC.refreshUI(tblClientGrpMembers);
            ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
            GlobalCC.refreshUI(tblClientPop);


            return null;
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;

        }
    }

    public void actionTblClientGroupListener(SelectionEvent selectionEvent) {
        Object key2 = tblClientGroup.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {
            session.setAttribute("grpCode",
                                 nodeBinding.getAttribute("grp_Code"));
            btnActionEditGroupClient.setDisabled(false);
            btnActionDeleteGroupClient.setDisabled(false);
            //ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
            GlobalCC.refreshUI(btnActionEditGroupClient);
            GlobalCC.refreshUI(btnActionDeleteGroupClient);


            ADFUtils.findIterator("findClientGroupMembersIterator").executeQuery();
            GlobalCC.refreshUI(tblClientGrpMembers);


        }
    }

    public String actionDeleteClntFromGroup() {


        if (checkIfAnyTableRowselected(tblClientGrpMembers) == true) {


            //session.setAttribute("grpdCode",  nodeBinding.getAttribute("grpd_Code"));

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:confirmDeleteClientGrpMember" +
                                 "').show(hints);");


            return null;


        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected::");
            return null;
        }
    }


    public void actiontblGrpMemberListener(SelectionEvent selectionEvent) {

        Object key2 = tblClientGrpMembers.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {
            session.setAttribute("grpdCode",
                                 nodeBinding.getAttribute("grpd_Code"));
            btnRemoveClientFromGroup.setDisabled(false);

            GlobalCC.refreshUI(btnRemoveClientFromGroup);

        }
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

    public String actionsearchClient() {
        // Add event code here...
        return null;
    }

    public void setSectorName(Object sectorName) {
        this.sectorName = session.getAttribute("sectorName");
    }

    public Object getSectorName() {
        return session.getAttribute("sectorName");
    }

    public void setRbtnShortDesc(RichSelectBooleanRadio rbtnShortDesc) {
        this.rbtnShortDesc = rbtnShortDesc;
    }

    public RichSelectBooleanRadio getRbtnShortDesc() {
        return rbtnShortDesc;
    }


    public String actionCreateNewGroup() {
        // Add event code here...
        return null;
    }

    public String actionEditGroupClients() {
        // Add event code here...
        return null;
    }

    public void setBtnActionEditGroupClient(RichCommandButton btnActionEditGroupClient) {
        this.btnActionEditGroupClient = btnActionEditGroupClient;
    }

    public RichCommandButton getBtnActionEditGroupClient() {
        return btnActionEditGroupClient;
    }

    public void setTblClientGroup(RichTable tblClientGroup) {
        this.tblClientGroup = tblClientGroup;
    }

    public RichTable getTblClientGroup() {
        return tblClientGroup;
    }

    public void setBtnActionDeleteGroupClient(RichCommandButton btnActionDeleteGroupClient) {
        this.btnActionDeleteGroupClient = btnActionDeleteGroupClient;
    }

    public RichCommandButton getBtnActionDeleteGroupClient() {
        return btnActionDeleteGroupClient;
    }

    public void setBtnRemoveClientFromGroup(RichCommandButton btnRemoveClientFromGroup) {
        this.btnRemoveClientFromGroup = btnRemoveClientFromGroup;
    }

    public RichCommandButton getBtnRemoveClientFromGroup() {
        return btnRemoveClientFromGroup;
    }

    public void setTblClientGrpMembers(RichTable tblClientGrpMembers) {
        this.tblClientGrpMembers = tblClientGrpMembers;
    }

    public RichTable getTblClientGrpMembers() {
        return tblClientGrpMembers;
    }

    public void setTxtGrpCode(RichInputText txtGrpCode) {
        this.txtGrpCode = txtGrpCode;
    }

    public RichInputText getTxtGrpCode() {
        return txtGrpCode;
    }

    public void setTxtGrpName(RichInputText txtGrpName) {
        this.txtGrpName = txtGrpName;
    }

    public RichInputText getTxtGrpName() {
        return txtGrpName;
    }

    public void setTxtGrpMinimum(RichInputNumberSpinbox txtGrpMinimum) {
        this.txtGrpMinimum = txtGrpMinimum;
    }

    public RichInputNumberSpinbox getTxtGrpMinimum() {
        return txtGrpMinimum;
    }

    public void setTxtGrpMax(RichInputNumberSpinbox txtGrpMax) {
        this.txtGrpMax = txtGrpMax;
    }

    public RichInputNumberSpinbox getTxtGrpMax() {
        return txtGrpMax;
    }

    public void setBtnsaveGrp(RichCommandButton btnsaveGrp) {
        this.btnsaveGrp = btnsaveGrp;
    }

    public RichCommandButton getBtnsaveGrp() {
        return btnsaveGrp;
    }

    public void setTblClientPop(RichTable tblClientPop) {
        this.tblClientPop = tblClientPop;
    }

    public RichTable getTblClientPop() {
        return tblClientPop;
    }

    public void setColumnSelect(RichSelectBooleanCheckbox columnSelect) {
        this.columnSelect = columnSelect;
    }

    public RichSelectBooleanCheckbox getColumnSelect() {
        return columnSelect;
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

    public void cboSearchClntTypelistener(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue() &&
            evt.getNewValue() != null) {


            String type =
                GlobalCC.checkNullValues(txtSearchClntType.getValue());
            session.setAttribute("searchClntType", type);

        }
    }

    public String actionSelectAllGrpmembers() {
        RowKeySet rks = new RowKeySetImpl();
        int count = 0;
        int rowcount = tblClientGrpMembers.getRowCount();
        if (rowcount > 0) {

            for (int i = 0; i < rowcount; i++) {
                tblClientGrpMembers.setRowIndex(i);
                Object key = tblClientGrpMembers.getRowKey();
                tblClientGrpMembers.setRowKey(key);
                JUCtrlHierNodeBinding nodeBinding =
                    (JUCtrlHierNodeBinding)tblClientGrpMembers.getRowData();
                nodeBinding.setAttribute("selected", true);
                grpMemberSelect.setSelected(true);
                GlobalCC.refreshUI(grpMemberSelect);
                count++;
            }

        } else {
            GlobalCC.INFORMATIONREPORTING("No Records::");
        }
        return null;

    }

    public String actionDeSelectAllGrpmembers() {
        RowKeySet rks = new RowKeySetImpl();
        int count = 0;
        if (checkIfAnyTableRowselected(tblClientGrpMembers)) {
            int rowcount = tblClientGrpMembers.getRowCount();
            if (rowcount > 0) {

                for (int i = 0; i < rowcount; i++) {
                    tblClientGrpMembers.setRowIndex(i);
                    Object key = tblClientGrpMembers.getRowKey();
                    tblClientGrpMembers.setRowKey(key);
                    JUCtrlHierNodeBinding nodeBinding =
                        (JUCtrlHierNodeBinding)tblClientGrpMembers.getRowData();

                    grpMemberSelect.setSelected(false);
                    GlobalCC.refreshUI(grpMemberSelect);
                    count++;
                }

            } else {
                GlobalCC.INFORMATIONREPORTING("No Records::");
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record(s) Selected::");
        }
        return null;

    }


    public void setGrpMemberSelect(RichSelectBooleanCheckbox grpMemberSelect) {
        this.grpMemberSelect = grpMemberSelect;
    }

    public RichSelectBooleanCheckbox getGrpMemberSelect() {
        return grpMemberSelect;
    }


    public void setBtnSelectAllGrpMembers(RichCommandButton btnSelectAllGrpMembers) {
        this.btnSelectAllGrpMembers = btnSelectAllGrpMembers;
    }

    public RichCommandButton getBtnSelectAllGrpMembers() {
        return btnSelectAllGrpMembers;
    }


    public void setResetContainer(RichCommandButton resetContainer) {
        this.resetContainer = resetContainer;
    }

    public RichCommandButton getResetContainer() {
        return resetContainer;
    }

    public void setResetSearchContainer(RichPanelLabelAndMessage resetSearchContainer) {
        this.resetSearchContainer = resetSearchContainer;
    }

    public RichPanelLabelAndMessage getResetSearchContainer() {
        return resetSearchContainer;
    }

    public void setTxtBranchMgrSeqNo(RichInputNumberSpinbox txtBranchMgrSeqNo) {
        this.txtBranchMgrSeqNo = txtBranchMgrSeqNo;
    }

    public RichInputNumberSpinbox getTxtBranchMgrSeqNo() {
        return txtBranchMgrSeqNo;
    }

    public void setBtnAddGrpMember(RichCommandButton btnAddGrpMember) {
        this.btnAddGrpMember = btnAddGrpMember;
    }

    public RichCommandButton getBtnAddGrpMember() {
        return btnAddGrpMember;
    }

    public void setBtnDeselectAll(RichCommandButton btnDeselectAll) {
        this.btnDeselectAll = btnDeselectAll;
    }

    public RichCommandButton getBtnDeselectAll() {
        return btnDeselectAll;
    }

    public void setBtnSelectAll(RichCommandButton btnSelectAll) {
        this.btnSelectAll = btnSelectAll;
    }

    public RichCommandButton getBtnSelectAll() {
        return btnSelectAll;
    }

    public void setTblClientHolder(RichPanelCollection tblClientHolder) {
        this.tblClientHolder = tblClientHolder;
    }

    public RichPanelCollection getTblClientHolder() {
        return tblClientHolder;
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

    public void actionConfirmDeleteAuditor(DialogEvent dialogEvent) {
        // Add event code here...
    }

    public void setCheckboxCol(RichColumn checkboxCol) {
        this.checkboxCol = checkboxCol;
    }

    public RichColumn getCheckboxCol() {
        return checkboxCol;
    }

    public void setEditCol(RichColumn editCol) {
        this.editCol = editCol;
    }

    public RichColumn getEditCol() {
        return editCol;
    }

    public String test() {

        return null;
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

    public String actionEditPrintServer() {
        // Add event code here...
        return null;
    }

    public String actionDeletePrintServer() {
        // Add event code here...
        return null;
    }

    public String actionSavePrinterServer() {
        // Add event code here...
        return null;
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

    public void criteriaValueChangeListener(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != null &&
            valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (rbtnSearchAccountNo.isSelected()) {

                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(false);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchStatus.setValue(null);
                clientPolicyNumber.setDisabled(true);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                clientPolicyNumber.setValue(null);
                txtSearchPostal.setValue(null);
                txtSearchPostal.setDisabled(true);
                txtIdNum.setDisabled(true);
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
                pinNumber.setDisabled(true);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchName.setLabel("SurName");
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);


            }

            else if (rbtnSearchOldAccountNo.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);

                txtOldAccountNo.setDisabled(false);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchStatus.setValue(null);
                clientPolicyNumber.setDisabled(true);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                clientPolicyNumber.setValue(null);
                txtSearchPostal.setValue(null);
                txtSearchPostal.setDisabled(true);
                txtIdNum.setDisabled(true);
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
                pinNumber.setDisabled(true);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchName.setLabel("SurName");
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);

            }


            else if (rbtnCustomerId.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
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
                txtSearchName.setLabel("SurName");
                pinNumber.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);

            } else if (rbtnShortDesc.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchName.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
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

                txtSearchName.setLabel("SurName");
                txtSearchStatus.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                pinNumber.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);

            } else if (rbtnShortDescLeg.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
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

                txtSearchName.setLabel("SurName");
                txtSearchStatus.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                pinNumber.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);

            } else if (rbtnClientType.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
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
                txtSearchName.setLabel("SurName");
                txtSearchClntType.setDisabled(false);

                pinNumber.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnStatus.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtSearchStatus.setDisabled(false);
                txtSearchStatus.setValue(null);
                txtAccountNo.setDisabled(true);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
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
                txtSearchName.setLabel("SurName");
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
                txtIdNum.setDisabled(true);
                pinNumber.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnPhySicalAddr.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
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
                txtSearchName.setLabel("SurName");
                txtSearchPhysical.setDisabled(false);

                pinNumber.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnPostalAddr.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchName.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
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
                txtSearchName.setLabel("SurName");
                txtSearchPostal.setDisabled(false);

                pinNumber.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);

            } else if (rbtnSector.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchName.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
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
                txtSearchName.setLabel("SurName");

                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                pinNumber.setDisabled(true);
                btnSectorLov.setDisabled(false);
                txtIdNum.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnPartOfAnyName.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchSector.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
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
                txtSearchName.setLabel("SurName");

                txtSearchOtherName.setDisabled(false);

                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                pinNumber.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnOldNames.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
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
                txtSearchName.setLabel("SurName");
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
                txtIdNum.setDisabled(true);
                pinNumber.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnExactName.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchSector.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
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
                txtSearchName.setLabel("Full Name");

                txtSearchOtherName.setDisabled(true);

                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                pinNumber.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnDateCreated.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                btnSectorLov.setDisabled(true);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
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
                txtSearchName.setLabel("SurName");

                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);

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
                txtIdNum.setDisabled(true);
                pinNumber.setDisabled(true);
                txtPassport.setDisabled(true);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rdbnDateOfBirth.isSelected()) {
                txtDateOfBirth.setDisabled(false);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                clientPolicyNumber.setDisabled(true);
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
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                txtSearchName.setLabel("SurName");
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                pinNumber.setDisabled(true);
                txtPassport.setDisabled(true);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnIdNumber.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtIdNum.setDisabled(false);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
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
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                txtSearchName.setLabel("SurName");
                btnSectorLov.setDisabled(true);
                pinNumber.setDisabled(true);
                txtPassport.setDisabled(true);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (txtClientPolNumber.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtIdNum.setDisabled(true);
                clientPolicyNumber.setDisabled(false);
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
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                txtSearchName.setLabel("SurName");
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                pinNumber.setDisabled(true);
                txtPassport.setDisabled(true);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (txtPinNumber.isSelected()) {
                pinNumber.setDisabled(false);
                txtDateOfBirth.setDisabled(true);
                txtIdNum.setDisabled(true);
                clientPolicyNumber.setDisabled(true);
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
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                txtSearchName.setLabel("SurName");
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtPassport.setDisabled(true);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (txtPassPortNumber.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtPassport.setDisabled(false);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
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
                txtSearchName.setLabel("SurName");
                txtSearchPhysical.setDisabled(true);
                txtPassport.setValue(null);
                pinNumber.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtTelNo.setDisabled(true);
                txtClaimNo.setDisabled(true);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (txtTelNoRadio.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtPassport.setDisabled(true);
                txtClaimNo.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
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
                txtSearchName.setLabel("SurName");
                txtSearchPhysical.setDisabled(true);
                txtPassport.setValue(null);
                pinNumber.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtTelNo.setDisabled(false);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (sbrClaimNo.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtPassport.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
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
                txtSearchName.setLabel("SurName");
                txtSearchPhysical.setDisabled(true);
                txtPassport.setValue(null);
                pinNumber.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(false);
                // txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null);
                GlobalCC.refreshUI(gridClientSearchDetails);
            }else if (txtVehicleRegNoRadio.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtPassport.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
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
                txtSearchName.setLabel("SurName");
                txtSearchPhysical.setDisabled(true);
                txtPassport.setValue(null);
                pinNumber.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                // txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                
                txtVehicleRegNo.setDisabled(false);
                //txtVehicleRegNo.setValue(null);
                GlobalCC.refreshUI(gridClientSearchDetails);
            }

        }
    }


    public void setBtnSectorLov(RichCommandButton btnSectorLov) {
        this.btnSectorLov = btnSectorLov;
    }

    public RichCommandButton getBtnSectorLov() {
        return btnSectorLov;
    }

    public void setTblAccountOfficers(RichTable tblAccountOfficers) {
        this.tblAccountOfficers = tblAccountOfficers;
    }

    public RichTable getTblAccountOfficers() {
        return tblAccountOfficers;
    }

    public void tblAccountOfficersListener(SelectionEvent selectionEvent) {

        Object key2 = tblAccountOfficers.getSelectedRowData();

        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {

            String userCode = nodeBinding.getAttribute("userCode").toString();
            session.setAttribute("sysUserCode", userCode);
            btnEditAccOfficer.setDisabled(false);

            GlobalCC.refreshUI(btnEditAccOfficer);
            ADFUtils.findIterator("fetchAllClientsByAccountOfficerIterator").executeQuery();
            lbCount.setValue(session.getAttribute("client_count"));
            GlobalCC.refreshUI(lbCount);
            GlobalCC.refreshUI(tblAccClients);


        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected:");
        }
    }

    public String btnEditAccOfficer() {
        // Add event code here...
        return null;
    }

    public void setBtnEditAccOfficer(RichCommandButton btnEditAccOfficer) {
        this.btnEditAccOfficer = btnEditAccOfficer;
    }

    public RichCommandButton getBtnEditAccOfficer() {
        return btnEditAccOfficer;
    }

    public String actionEditAccOffice() {
        if (session.getAttribute("sysUserCode") != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:UsersPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("NO RECORD SELECTED");
        }

        return null;
    }

    public void setLbCount(RichOutputLabel lbCount) {
        this.lbCount = lbCount;
    }

    public RichOutputLabel getLbCount() {
        return lbCount;
    }

    public void setTblAccClients(RichTable tblAccClients) {
        this.tblAccClients = tblAccClients;
    }

    public RichTable getTblAccClients() {
        return tblAccClients;
    }

    public String actionChangeAccOfficer() {
        Object key2 = tblUserList.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {
            String userCode = nodeBinding.getAttribute("userCode").toString();
            DBConnector dbCon = new DBConnector();
            OracleConnection conn = null;
            try {
                conn = dbCon.getDatabaseConnection();

                String query =
                    "begin TQC_CLIENTS_PKG.UpdateAccountOfficer(?,?,?); end;";
                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.registerOutParameter(1, OracleTypes.VARCHAR);
                cst.setBigDecimal(2,
                                  session.getAttribute("sysUserCode") == null ?
                                  null :
                                  new BigDecimal(session.getAttribute("sysUserCode").toString()));
                cst.setBigDecimal(3,
                                  new BigDecimal(userCode == null ? null : userCode));
                cst.execute();
                String err = cst.getString(1);
                cst.close();
                conn.commit();
                conn.close();

                if (err != null) {
                    GlobalCC.INFORMATIONREPORTING(err);
                    return null;
                } else {
                    GlobalCC.INFORMATIONREPORTING("Record Successfully updated");

                    session.setAttribute("sysUserCode", null);
                    btnEditAccOfficer.setDisabled(true);
                    lbCount.setValue(null);


                    ADFUtils.findIterator("fetchAllClientsByAccountOfficerIterator").executeQuery();
                    ADFUtils.findIterator("FindAccountOfficersIterator").executeQuery();
                    GlobalCC.refreshUI(btnEditAccOfficer);
                    GlobalCC.refreshUI(tblAccountOfficers);
                    GlobalCC.refreshUI(tblAccClients);
                }

            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                return null;
            }


        }
        GlobalCC.dismissPopUp("pt1", "accountOfficersPop");
        GlobalCC.dismissPopUp("pt1", "UsersPop");

        return null;
    }

    public void setBtnChangeAccOfficer(RichCommandButton btnChangeAccOfficer) {
        this.btnChangeAccOfficer = btnChangeAccOfficer;
    }

    public RichCommandButton getBtnChangeAccOfficer() {
        return btnChangeAccOfficer;
    }

    public void setTblUserList(RichTable tblUserList) {
        this.tblUserList = tblUserList;
    }

    public RichTable getTblUserList() {
        return tblUserList;
    }

    public void setBaseClientBackingBean1(RichInputText baseClientBackingBean1) {
        this.baseClientBackingBean1 = baseClientBackingBean1;
    }

    public RichInputText getBaseClientBackingBean1() {
        return baseClientBackingBean1;
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

    public void rbtnAccountNoChanged(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
    }

    public void setGridClientSearchDetails(HtmlPanelGrid gridClientSearchDetails) {
        this.gridClientSearchDetails = gridClientSearchDetails;
    }

    public HtmlPanelGrid getGridClientSearchDetails() {
        return gridClientSearchDetails;
    }


    public void enterKeyPressed(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            String action = actionAcceptSearchCriteria();
        }
    }

    public void setRbtnShortDescLeg(RichSelectBooleanRadio rbtnShortDescLeg) {
        this.rbtnShortDescLeg = rbtnShortDescLeg;
    }

    public RichSelectBooleanRadio getRbtnShortDescLeg() {
        return rbtnShortDescLeg;
    }

    public void setRdbnDateOfBirth(RichSelectBooleanRadio rdbnDateOfBirth) {
        this.rdbnDateOfBirth = rdbnDateOfBirth;
    }

    public RichSelectBooleanRadio getRdbnDateOfBirth() {
        return rdbnDateOfBirth;
    }

    public void setTxtDateOfBirth(RichInputDate txtDateOfBirth) {
        this.txtDateOfBirth = txtDateOfBirth;
    }

    public RichInputDate getTxtDateOfBirth() {
        return txtDateOfBirth;
    }

    public void setRbtnIdNumber(RichSelectBooleanRadio rbtnIdNumber) {
        this.rbtnIdNumber = rbtnIdNumber;
    }

    public RichSelectBooleanRadio getRbtnIdNumber() {
        return rbtnIdNumber;
    }

    public void setTxtIdNum(RichInputText txtIdNum) {
        this.txtIdNum = txtIdNum;
    }

    public RichInputText getTxtIdNum() {
        return txtIdNum;
    }

    public void setTxtClientPolNumber(RichSelectBooleanRadio txtClientPolNumber) {
        this.txtClientPolNumber = txtClientPolNumber;
    }

    public RichSelectBooleanRadio getTxtClientPolNumber() {
        return txtClientPolNumber;
    }

    public void setClientPolicyNumber(RichInputText clientPolicyNumber) {
        this.clientPolicyNumber = clientPolicyNumber;
    }

    public RichInputText getClientPolicyNumber() {
        return clientPolicyNumber;
    }

    public void setTxtPinNumber(RichSelectBooleanRadio txtPinNumber) {
        this.txtPinNumber = txtPinNumber;
    }

    public RichSelectBooleanRadio getTxtPinNumber() {
        return txtPinNumber;
    }

    public void setPinNumber(RichInputText pinNumber) {
        this.pinNumber = pinNumber;
    }

    public RichInputText getPinNumber() {
        return pinNumber;
    }

    public void setTxtPassPortNumber(RichSelectBooleanRadio txtPassPortNumber) {
        this.txtPassPortNumber = txtPassPortNumber;
    }

    public RichSelectBooleanRadio getTxtPassPortNumber() {
        return txtPassPortNumber;
    }

    public void setTxtPassport(RichInputText txtPassport) {
        this.txtPassport = txtPassport;
    }

    public RichInputText getTxtPassport() {
        return txtPassport;
    }

    public void setTxtTelNo(RichInputText txtTelNo) {
        this.txtTelNo = txtTelNo;
    }

    public RichInputText getTxtTelNo() {
        return txtTelNo;
    }

    public void setTxtTelNoRadio(RichSelectBooleanRadio txtTelNoRadio) {
        this.txtTelNoRadio = txtTelNoRadio;
    }

    public RichSelectBooleanRadio getTxtTelNoRadio() {
        return txtTelNoRadio;
    }

    public void setTxtClaimNo(RichInputText txtClaimNo) {
        this.txtClaimNo = txtClaimNo;
    }

    public RichInputText getTxtClaimNo() {
        return txtClaimNo;
    }

    public void setSbrClaimNo(RichSelectBooleanRadio sbrClaimNo) {
        this.sbrClaimNo = sbrClaimNo;
    }

    public RichSelectBooleanRadio getSbrClaimNo() {
        return sbrClaimNo;
    }

    public void setTxtOldAccountNo(RichInputText txtOldAccountNo) {
        this.txtOldAccountNo = txtOldAccountNo;
    }

    public RichInputText getTxtOldAccountNo() {
        return txtOldAccountNo;
    }

    public void setRbtnSearchOldAccountNo(RichSelectBooleanRadio rbtnSearchOldAccountNo) {
        this.rbtnSearchOldAccountNo = rbtnSearchOldAccountNo;
    }

    public RichSelectBooleanRadio getRbtnSearchOldAccountNo() {
        return rbtnSearchOldAccountNo;
    }

    public void setTxtVehicleRegNo(RichInputText txtVehicleRegNo) {
        this.txtVehicleRegNo = txtVehicleRegNo;
    }

    public RichInputText getTxtVehicleRegNo() {
        return txtVehicleRegNo;
    }

    public void setTxtVehicleRegNoRadio(RichSelectBooleanRadio txtVehicleRegNoRadio) {
        this.txtVehicleRegNoRadio = txtVehicleRegNoRadio;
    }

    public RichSelectBooleanRadio getTxtVehicleRegNoRadio() {
        return txtVehicleRegNoRadio;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public HttpSession getSession() {
        return session;
    }

    public Object getSectorName1() {
        return sectorName;
    }


    public void setRenderer(Rendering renderer) {
        this.renderer = renderer;
    }

    public Rendering getRenderer() {
        return renderer;
    }
}
