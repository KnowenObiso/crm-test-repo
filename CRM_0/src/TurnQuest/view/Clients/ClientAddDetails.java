package TurnQuest.view.Clients;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.LOVDAO;
import TurnQuest.view.Clients1.AppClients;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.sql.ResultSet;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.servlet.http.HttpSession;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanRadio;
import oracle.adf.view.rich.component.rich.input.RichSelectManyShuttle;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelTabbed;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;

import oracle.jbo.Key;
import oracle.jbo.Row;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;

import org.apache.myfaces.trinidad.event.AttributeChangeEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class ClientAddDetails {
    private RichInputText clientDesc;
    private RichTable clientLOV;
    private RichSelectBooleanRadio rdCreateClient;
    private RichCommandButton clientButton;
    private RichSelectBooleanRadio rdSearchClient;
    private RichTable countriesLOV;
    private RichInputText countryDesc;
    private RichTable townLOV;
    private RichInputText townDesc;
    private RichPanelTabbed clientsTab;

    private List<SelectItem> selectValues = new ArrayList<SelectItem>();
    private List<String> displayValue = new ArrayList<String>();

    private static int ProblemArose = 0;
    //private static int ScreenPosition = 0;
    private RichShowDetailItem selectClientAction;
    private RichShowDetailItem clientDetails;
    private RichShowDetailItem clientSystems;
    private RichCommandButton prevButton;
    private RichCommandButton nextButton;
    private RichTable sectorLOV;
    private RichInputText sectorDesc;
    private RichInputText domicileCountry;
    private RichTable domicilecountryLOV;
    private RichTable banksLOV;
    private RichInputText bankDesc;
    private RichTable searchClients;

    //private static BigDecimal ClientCode;
    private static String ClientshtCde;
    private RichInputText fullName;
    private RichInputText idRegNo;
    private RichInputDate doB;
    private RichInputText piNNumber;
    private RichInputText physicalAddress;
    private RichInputText postalAddress;
    private RichInputText emailAddress;
    private RichInputText telephone;
    private RichSelectOneChoice status;
    private RichInputText fax;
    private RichInputText remarks;
    private RichSelectOneChoice specialTerms;
    private RichSelectOneChoice declinedProposal;
    private RichSelectOneChoice increasedPremium;
    private RichSelectOneChoice cancelledPolicy;
    private RichInputText accNo;
    private RichInputDate wef;
    private RichInputDate wet;
    private RichInputText withdrawalReason;
    private RichInputText surname;
    private RichSelectBooleanRadio rdIndividual;
    private RichSelectBooleanRadio rdCorporate;
    private RichSelectOneChoice title;
    private RichInputText bankAccount;
    private RichSelectOneChoice direct;
    private RichInputText createdBy;
    private RichInputText smsNo;
    private RichInputDate dateCreated;
    private RichSelectOneChoice runOff;
    private RichShowDetailItem confirmClient;
    private RichInputText firstName;
    private RichInputText middleName;
    private RichSelectOneChoice searchCriteria;

    //private static String MiddleName;
    //private static String LastName;
    //private static String PINNumber;
    //private static String PostalAddress;
    //private static String SearchCriteria;
    private RichTable availableSystems;
    private RichTable assignedsystems;
    private RichInputText id;
    private RichSelectOneChoice holdingCompany;

    // private static String AddEdit = null;
    //private static String Direct;
    //private static String id1;
    //private static String RegNo;
    //private static String PhyAddress;
    // private static String TitleListener;
    //private static String email;
    //private static String telephone1;
    //private static String fax1;
    //private static String sms;
    //private static String bankAcc;
    //private static String remarks1;

    private static String WEFDate;
    private static String WETDate;
    private static String DOBDate;
    private RichInputText userName;
    private RichInputText fullNames;
    private RichInputText password;
    private RichInputText personelRank;
    private RichSelectOneChoice userstatus;
    private RichSelectOneChoice allowLogin;
    private RichInputText webemailAddress;
    private RichTable webClientAccounts;
    private RichShowDetailItem webAccTab;

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private RichSelectManyShuttle clientSystemRoles;
    private UISelectItems ClientSystemRoleSelect;
    private RichInputText accMangrDesc;
    private RichTable accMangrLOV;
    private RichOutputLabel lblClientName;
    private RichInputText clientBranchDivision;
    private RichInputText clientBranchDivisionCode;
    private RichTable tblBranchDivisons;
    private RichInputText txtContactPhone1;
    private RichInputText txtContactEmail1;
    private RichInputText txtContactPhone2;
    private RichInputText txtContactEmail2;
    private RichInputText contactPhone1;
    private RichInputText contactEmail1;
    private RichInputText contactPhone2;
    private RichInputText contactEmail2;
    private RichCommandButton btnShowContactPersons;
    private RichOutputLabel lblDOB;


    public String findClientDetails() {
        String ClientType;

        DCIteratorBinding dciter =
            ADFUtils.findIterator("findAllClients1Iterator");
        RowKeySet set = searchClients.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();
            //ClientCode = (BigDecimal)r.getAttribute("clientCode");
            session.setAttribute("ClientCode", r.getAttribute("clientCode"));
        }

        String query = "begin ? := tqc_clients_pkg.get_client_dtls(?); end;";
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        try {
            DBConnector datahandler = new DBConnector();

            conn = (OracleConnection)datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code

            cst.setObject(2, session.getAttribute("ClientCode"));

            cst.execute();

            ResultSet rs = (ResultSet)cst.getObject(1);

            while (rs.next()) {
                //add feild bindings
                ClientshtCde = rs.getString(2);
                id.setValue(rs.getString(2));
                firstName.setValue(rs.getString(4));
                session.setAttribute("FirstName", rs.getString(4));
                //FirstName = rs.getString(4);
                // fullName.setValue(rs.getString(3));
                surname.setValue(rs.getString(3));
                //LastName = rs.getString(3);
                session.setAttribute("LastName", rs.getString(3));
                idRegNo.setValue(rs.getString(5));
                //RegNo = rs.getString(5);
                session.setAttribute("RegNo", rs.getString(5));
                doB.setValue(rs.getDate(6));
                piNNumber.setValue(rs.getString(7));
                //PINNumber = rs.getString(7);
                session.setAttribute("PINNumber", rs.getString(7));
                physicalAddress.setValue(rs.getString(8));
                //PhyAddress = rs.getString(8);
                session.setAttribute("PhyAddress", rs.getString(8));
                postalAddress.setValue(rs.getString(9));
                //PostalAddress = rs.getString(9);
                session.setAttribute("PostalAddress", rs.getString(9));
                LOVDAO.TownCode = rs.getBigDecimal(10);
                townDesc.setValue(rs.getString(11));
                LOVDAO.CountryCode = rs.getBigDecimal(12);
                countryDesc.setValue(rs.getString(13));
                emailAddress.setValue(rs.getString(14));
                //email = rs.getString(14);
                session.setAttribute("email", rs.getString(14));
                telephone.setValue(rs.getString(15));
                //telephone1 = rs.getString(15);
                session.setAttribute("telephone1", rs.getString(15));
                status.setValue(rs.getString(17));
                fax.setValue(rs.getString(18));
                //fax1 = rs.getString(18);
                session.setAttribute("fax1", rs.getString(18));
                remarks.setValue(rs.getString(19));
                specialTerms.setValue(rs.getString(20));
                declinedProposal.setValue(rs.getString(21));
                increasedPremium.setValue(rs.getString(22));
                cancelledPolicy.setValue(rs.getString(23));
                accNo.setValue(rs.getString(25));
                //id1 = rs.getString(2);
                session.setAttribute("id1", rs.getString(2));
                wef.setValue(rs.getDate(27));
                wet.setValue(rs.getDate(28));
                withdrawalReason.setValue(rs.getString(29));
                LOVDAO.SectorCode = rs.getBigDecimal(30);
                ClientType = rs.getString(32);
                if (ClientType == "I") {
                    rdIndividual.setSelected(true);
                    rdCorporate.setSelected(false);
                } else {
                    rdCorporate.setSelected(true);
                    rdIndividual.setSelected(false);
                }
                title.setValue(rs.getString(33));
                //TitleListener = rs.getString(33);
                session.setAttribute("TitleListener", rs.getString(33));
                LOVDAO.branchCode = rs.getBigDecimal(36);
                bankAccount.setValue(rs.getString(37));
                session.setAttribute("bankAcc", rs.getString(37));
                //bankAcc = rs.getString(37);
                direct.setValue(rs.getString(46));
                //Direct = rs.getString(46);
                session.setAttribute("Direct", rs.getString(46));
                createdBy.setValue(rs.getString(40));
                smsNo.setValue(rs.getString(41));
                //sms = rs.getString(41);
                session.setAttribute("sms", rs.getString(41));
                dateCreated.setValue(rs.getDate(43));
                runOff.setValue(rs.getString(44));
                session.setAttribute("accManCode", rs.getBigDecimal(48));
                accMangrDesc.setValue(rs.getString(49));

                txtContactPhone1.setValue(rs.getString(50));
                txtContactEmail1.setValue(rs.getString(51));
                txtContactPhone2.setValue(rs.getString(52));
                txtContactEmail2.setValue(rs.getString(53));

            }
            ClientSystemValues();
            GlobalCC.refreshUI(clientSystemRoles);

            ADFUtils.findIterator("findUnallocatedClientSystemsIterator").executeQuery();
            GlobalCC.refreshUI(availableSystems);
            ADFUtils.findIterator("findallocatedClientSystemsIterator").executeQuery();
            GlobalCC.refreshUI(assignedsystems);

            ADFUtils.findIterator("findclientwebaccountsIterator").executeQuery();
            GlobalCC.refreshUI(webClientAccounts);

            //ScreenPosition = 1;
            session.setAttribute("ScreenPosition", 1);
            ScreenRendering();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }

        return null;
    }

    public String saveClientWebAccount() {
        String PersonelRank = null;
        String Email = null;

        String Query =
            "begin tqc_clients_pkg.create_client_web_account(?,?,?,?,?,?,?,?,?,?,?,?); end;";

        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        try {

            DBConnector datahandler = new DBConnector();

            conn = (OracleConnection)datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            if (personelRank.getValue() == null) {
                PersonelRank = personelRank.getValue().toString();
            } else {
                PersonelRank = null;
            }
            if (webemailAddress.getValue() == null) {
                Email = webemailAddress.getValue().toString();
            } else {
                Email = null;
            }


            cst.setString(1, userName.getValue().toString());
            cst.setString(2, password.getValue().toString());
            cst.setString(3, allowLogin.getValue().toString());
            cst.setString(4, null);
            cst.setString(5, null);
            cst.setString(6, PersonelRank);
            cst.setString(7, status.getValue().toString());
            cst.setObject(8, session.getAttribute("ClientCode"));
            cst.setString(9, (String)session.getAttribute("Username"));
            cst.setString(10, Email);
            cst.setString(11, "A");
            cst.setBigDecimal(12, null);
            cst.execute();
            conn.close();

            String message = "Client Account Captured.";
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(message));

            ADFUtils.findIterator("findclientwebaccountsIterator").executeQuery();
            GlobalCC.refreshUI(webClientAccounts);
            //ButtonSequence(0);

        } catch (Exception e) {

            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return null;
    }

    public String RemoveClientWebAccount() {
        BigDecimal ClientWebAccount = null;

        DCIteratorBinding dciter =
            ADFUtils.findIterator("findclientwebaccountsIterator");
        RowKeySet set = webClientAccounts.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();

            ClientWebAccount = (BigDecimal)r.getAttribute("clientAccCode");

        }


        //TODO
        String Query =
            "begin tqc_clients_pkg.create_client_web_account(?,?,?,?,?,?,?,?,?,?,?,?); end;";

        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        try {

            DBConnector datahandler = new DBConnector();

            conn = (OracleConnection)datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            cst.setString(1, null);
            cst.setString(2, null);
            cst.setString(3, null);
            cst.setString(4, null);
            cst.setString(5, null);
            cst.setString(6, null);
            cst.setString(7, null);
            cst.setBigDecimal(8, null);
            cst.setString(9, null);
            cst.setString(10, null);
            cst.setString(11, "D");
            cst.setBigDecimal(12, ClientWebAccount);

            cst.execute();
            conn.close();

            ADFUtils.findIterator("findclientwebaccountsIterator").executeQuery();
            GlobalCC.refreshUI(webClientAccounts);

        } catch (Exception e) {

            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String SaveClientDetails() {
        ProblemArose = 1;
        java.util.Date Test = new java.util.Date();
        ;

        // Date coverFrom = new Date();
        SimpleDateFormat sdf1 =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy");

        String wefString = null;
        String wetString = null;
        String DOBString = null;
        java.util.Date lossDate = new java.util.Date();
        java.util.Date notificationDate = new java.util.Date();
        java.util.Date DOB = new java.util.Date();
        ;


        lossDate = null;
        notificationDate = null;


        String query =
        "begin ? := tqc_clients_pkg.client_extended_proc(" +
        "?,?,?,?,?,?,?,?,?,?," + 
        "?,?,?,?,?,?,?,?,?,?," +
        "?,?,?,?,?,?,?,?,?,?," + 
        "?,?,?,?,?,?,?,?,?,?," +
        "?,?,?,?,?,?,?,?,?,?," + 
        "?,?,?,?,?,?,?,?,?,?," +
        "?,?,?,?,?,?,?,?,?,?," + 
        "?,?,?,?,?,?,?,?,?,?," +
        "?,?,?,?,?,?,?,?,?,?," + 
        "?,?,?,?,?,?" +
        "); end;";
        OracleCallableStatement cst = null;
        List<AppClients> users = new ArrayList<AppClients>();
        OracleConnection conn = null;
        try {
            if (rdCreateClient.isSelected()) {
                if (wef.getValue() == null) {

                } else {
                    lossDate = sdf1.parse(wef.getValue().toString());
                    wefString = sdf2.format(lossDate);

                }
                if (wet.getValue() == null) {

                } else {
                    notificationDate = sdf1.parse(wet.getValue().toString());
                    wetString = sdf2.format(notificationDate);

                }
                if (doB.getValue() == null) {

                } else {
                    DOB = sdf1.parse(doB.getValue().toString());
                    DOBString = sdf2.format(DOB);

                }
            } else {

                wefString = WEFDate;
                wetString = WETDate;
                DOBString = DOBDate;
            }


            DBConnector datahandler = new DBConnector();
            conn = (OracleConnection)datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.NUMBER); //authorization code
            cst.setString(2, (String)session.getAttribute("Direct"));
            cst.setString(3, (String)session.getAttribute("id1"));
            cst.setString(4, (String)session.getAttribute("FirstName"));
            cst.setString(5, (String)session.getAttribute("MiddleName"));
            cst.setString(6, (String)session.getAttribute("LastName"));
            cst.setString(7, (String)session.getAttribute("PINNumber"));
            cst.setString(8, (String)session.getAttribute("PostalAddress"));
            cst.setString(9, (String)session.getAttribute("PhyAddress"));
            cst.setString(10, (String)session.getAttribute("RegNo"));
            cst.setString(11, (String)session.getAttribute("Username"));
            cst.setString(12, wefString);
            cst.setString(13, wetString);
            cst.setString(14, (String)session.getAttribute("TitleListener"));
            cst.setString(15, DOBString);
            cst.setBigDecimal(16, LOVDAO.CountryCode);
            cst.setBigDecimal(17, LOVDAO.TownCode);
            cst.setString(18, LOVDAO.PostalZIPCode);
            cst.setString(19, (String)session.getAttribute("email"));
            cst.setString(20, (String)session.getAttribute("telephone1"));
            cst.setString(21, (String)session.getAttribute("sms"));
            cst.setString(22, (String)session.getAttribute("fax1"));
            cst.setBigDecimal(23, LOVDAO.SectorCode);
            cst.setString(24, LOVDAO.SectorName);
            cst.setBigDecimal(25, LOVDAO.DomicileCountry);
            cst.setString(26, holdingCompany.getValue().toString());
            cst.setString(27, status.getValue().toString());
            cst.setString(28, runOff.getValue().toString());
            cst.setString(29, null); //withdrawalReason.getValue().toString());
            cst.setString(30, (String)session.getAttribute("remarks1"));
            cst.setString(31, (String)session.getAttribute("bankAcc"));
            cst.setBigDecimal(32, LOVDAO.branchCode);
            cst.setString(33, specialTerms.getValue().toString());
            cst.setString(34, cancelledPolicy.getValue().toString());
            cst.setString(35, increasedPremium.getValue().toString());
            cst.setString(36, declinedProposal.getValue().toString());
            if (rdIndividual.isSelected()) {
                cst.setString(37, "I");
            } else {
                cst.setString(37, "C");
            }
            cst.setString(38, (String)session.getAttribute("AddEdit"));
            cst.setObject(39, session.getAttribute("ClientCode"));
            cst.setBigDecimal(40,
                              (BigDecimal)session.getAttribute("accManCode"));

            cst.setString(41, (String)txtContactPhone1.getValue());
            cst.setString(42, (String)txtContactEmail1.getValue());
            cst.setString(43, (String)txtContactPhone2.getValue());
            cst.setString(44, (String)txtContactEmail2.getValue());

            cst.execute();

            //ClientCode = cst.getBigDecimal(1);
            session.setAttribute("ClientCode", cst.getBigDecimal(1));
            String retVal = PopulateClientDocs();
            if (rdCreateClient.isSelected()) {
                //ADFUtils.findIterator("findAllClients1Iterator").executeQuery();
                //GlobalCC.refreshUI(searchClients);
            }

            ADFUtils.findIterator("findAllClients1Iterator").executeQuery();
            GlobalCC.refreshUI(searchClients);

            ProblemArose = 0;
            String message = "Client Captured";
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(message));
            conn.close();
        } catch (Exception e) {

            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return null;
    }

    public String PopulateClientDocs() {
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = (OracleConnection)datahandler.getDatabaseConnection();
        try {
            String query =
                "begin tqc_web_pkg.PopulateRequiredDocuments(?,?); end;";
            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);

            cst.setObject(1, session.getAttribute("ClientCode"));
            cst.setString(2, (String)session.getAttribute("Username"));


            cst.execute();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String RefreshComponents() {

        direct.setValue("N");
        postalAddress.setValue("");
        status.setValue("");
        accNo.setValue("");
        emailAddress.setValue("");
        runOff.setValue("N");
        id.setValue("");
        telephone.setValue("");
        holdingCompany.setValue("N");
        title.setValue("");
        smsNo.setValue("");
        specialTerms.setValue("N");
        firstName.setValue("");
        fax.setValue("");
        cancelledPolicy.setValue("N");
        middleName.setValue("");
        idRegNo.setValue("");
        increasedPremium.setValue("N");
        surname.setValue("");
        piNNumber.setValue("");
        declinedProposal.setValue("N");
        physicalAddress.setValue("");
        wef.setValue("");
        wet.setValue(null);
        doB.setValue(null);
        status.setValue("A");
        remarks.setValue("");
        withdrawalReason.setValue("");

        txtContactPhone1.setValue("");
        txtContactEmail1.setValue("");
        txtContactPhone2.setValue("");
        txtContactEmail2.setValue("");

        GlobalCC.refreshUI(status);
        GlobalCC.refreshUI(direct);
        GlobalCC.refreshUI(postalAddress);
        GlobalCC.refreshUI(status);
        GlobalCC.refreshUI(accNo);
        GlobalCC.refreshUI(emailAddress);
        GlobalCC.refreshUI(runOff);
        GlobalCC.refreshUI(id);
        GlobalCC.refreshUI(telephone);
        GlobalCC.refreshUI(holdingCompany);
        GlobalCC.refreshUI(title);
        GlobalCC.refreshUI(smsNo);
        GlobalCC.refreshUI(specialTerms);
        GlobalCC.refreshUI(firstName);
        GlobalCC.refreshUI(fax);
        GlobalCC.refreshUI(cancelledPolicy);
        GlobalCC.refreshUI(middleName);
        GlobalCC.refreshUI(idRegNo);
        GlobalCC.refreshUI(increasedPremium);
        GlobalCC.refreshUI(surname);
        GlobalCC.refreshUI(piNNumber);
        GlobalCC.refreshUI(declinedProposal);
        GlobalCC.refreshUI(physicalAddress);
        GlobalCC.refreshUI(wef);
        GlobalCC.refreshUI(wet);
        GlobalCC.refreshUI(doB);
        GlobalCC.refreshUI(remarks);
        GlobalCC.refreshUI(withdrawalReason);

        return null;
    }

    public String clientSelected() {

        DCIteratorBinding dciter =
            ADFUtils.findIterator("findUserByAgencyCodeIterator");
        RowKeySet set = clientLOV.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();
            LOVDAO.clientCode = (BigDecimal)r.getAttribute("clientCode");
            LOVDAO.clientDesc = (String)r.getAttribute("shortDescription");
            LOVDAO.ClientFullname = (String)r.getAttribute("fullname");

            clientDesc.setValue(LOVDAO.ClientFullname);
        }

        return null;
    }

    public String AccountManagerSelected() {

        DCIteratorBinding dciter =
            ADFUtils.findIterator("findAccountManagersIterator");
        RowKeySet set = accMangrLOV.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();
            session.setAttribute("accManCode", r.getAttribute("USR_CODE"));
            accMangrDesc.setValue(r.getAttribute("USR_NAME"));
        }

        return null;
    }

    public String CountrySelected() {

        DCIteratorBinding dciter =
            ADFUtils.findIterator("findCountries1Iterator");
        RowKeySet set = countriesLOV.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();
            LOVDAO.CountryCode = (BigDecimal)r.getAttribute("countryCode");
            LOVDAO.CountryShtDesc = (String)r.getAttribute("countryShtDesc");

            countryDesc.setValue((String)r.getAttribute("countryName"));
        }

        return null;
    }

    public String DomicileCountrySelected() {

        DCIteratorBinding dciter =
            ADFUtils.findIterator("findCountries1Iterator");
        RowKeySet set = domicilecountryLOV.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();
            LOVDAO.DomicileCountry = (BigDecimal)r.getAttribute("countryCode");

            domicileCountry.setValue((String)r.getAttribute("countryName"));
        }

        return null;
    }

    public String SectorSelected() {

        DCIteratorBinding dciter =
            ADFUtils.findIterator("findSectors1Iterator");
        RowKeySet set = sectorLOV.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();
            LOVDAO.SectorCode = (BigDecimal)r.getAttribute("sectorCode");
            LOVDAO.SectorName = (String)r.getAttribute("sectorName");

            sectorDesc.setValue(LOVDAO.SectorName);
        }

        return null;
    }

    public String TownSelected() {

        DCIteratorBinding dciter = ADFUtils.findIterator("findTowns1Iterator");
        RowKeySet set = townLOV.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();
            LOVDAO.TownCode = (BigDecimal)r.getAttribute("townCode");
            LOVDAO.TownName = (String)r.getAttribute("townShtDesc");

            townDesc.setValue(LOVDAO.TownName);
        }

        return null;
    }

    public String BankBranchSelected() {

        DCIteratorBinding dciter = ADFUtils.findIterator("findBanks1Iterator");
        RowKeySet set = banksLOV.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();
            LOVDAO.branchCode = (BigDecimal)r.getAttribute("bankBranchCode");
            LOVDAO.branchName = (String)r.getAttribute("branchName");

            session.setAttribute("branchCode",
                                 r.getAttribute("bankBranchCode"));

            bankDesc.setValue(LOVDAO.branchName);
        }

        return null;
    }

    public String cmdNext() {
        Integer ScreenPosition = new Integer(0);
        if ((Integer)session.getAttribute("ScreenPosition") != null) {
            ScreenPosition = (Integer)session.getAttribute("ScreenPosition");
        }
        String AddEdit = (String)session.getAttribute("AddEdit");
        if (this.selectClientAction.isRendered()) {
            ScreenPosition = ScreenPosition + 1;
        } else if (this.clientDetails.isRendered()) {
            if (AddEdit == "E") {
                SaveClientDetails();
                if (ProblemArose == 1) {
                    return null;
                }
                ScreenPosition = 0;
            } else {
                ScreenPosition = ScreenPosition + 1;
            }
        } else if (this.confirmClient.isRendered()) {
            SaveClientDetails();
            if (ProblemArose == 1) {
                return null;
            }
            ScreenPosition = 0;

        } else {
            //do nothing
        }
        session.setAttribute("ScreenPosition", ScreenPosition);
        ScreenRendering();

        return null;
    }

    public String cmdPrevious() {
        Integer ScreenPosition =
            (Integer)session.getAttribute("ScreenPosition");
        if (this.clientDetails.isRendered()) {
            ScreenPosition = ScreenPosition - 1;
        } else if (this.confirmClient.isRendered()) {
            ScreenPosition = ScreenPosition - 1;
        } else {
            //do nothing
        }
        session.setAttribute("ScreenPosition", ScreenPosition);
        ScreenRendering();
        return null;
    }

    public String ScreenRendering() {
        Integer ScreenPosition =
            (Integer)session.getAttribute("ScreenPosition");
        switch (ScreenPosition) {
        case 0:
            this.webAccTab.setRendered(false);
            this.clientDetails.setRendered(false);
            this.clientSystems.setRendered(false);
            this.selectClientAction.setRendered(true);
            this.confirmClient.setRendered(false);
            prevButton.setRendered(false);
            nextButton.setRendered(true);
            nextButton.setText("Next");
            break;
        case 1:
            this.clientDetails.setRendered(true);
            if (rdCreateClient.isSelected()) {
                this.clientSystems.setRendered(false);
                this.webAccTab.setRendered(false);
                //RefreshComponents();
                nextButton.setText("Next");
            } else {
                this.clientSystems.setRendered(true);
                this.webAccTab.setRendered(true);
                nextButton.setText("Save");
            }
            this.selectClientAction.setRendered(false);
            this.confirmClient.setRendered(false);
            prevButton.setRendered(true);

            break;
        case 2:
            this.clientDetails.setRendered(false);
            if (rdCreateClient.isSelected()) {
                this.clientSystems.setRendered(false);
                this.webAccTab.setRendered(false);
            } else {
                this.clientSystems.setRendered(true);
                this.webAccTab.setRendered(true);
            }
            this.selectClientAction.setRendered(false);
            this.confirmClient.setRendered(true);
            prevButton.setRendered(true);
            nextButton.setText("Save");
            break;
        }

        return null;
    }

    public void setClientDesc(RichInputText clientDesc) {
        this.clientDesc = clientDesc;
    }

    public RichInputText getClientDesc() {
        return clientDesc;
    }

    public void setClientLOV(RichTable clientLOV) {
        this.clientLOV = clientLOV;
    }

    public RichTable getClientLOV() {
        return clientLOV;
    }

    public void CreateClientListener(AttributeChangeEvent attributeChangeEvent) {

    }

    public void setRdCreateClient(RichSelectBooleanRadio rdCreateClient) {
        this.rdCreateClient = rdCreateClient;
    }

    public RichSelectBooleanRadio getRdCreateClient() {
        return rdCreateClient;
    }

    public void setClientButton(RichCommandButton clientButton) {
        this.clientButton = clientButton;
    }

    public RichCommandButton getClientButton() {
        return clientButton;
    }


    public void setRdSearchClient(RichSelectBooleanRadio rdSearchClient) {
        this.rdSearchClient = rdSearchClient;
    }

    public RichSelectBooleanRadio getRdSearchClient() {
        return rdSearchClient;
    }

    public void SearchClientListener(AttributeChangeEvent attributeChangeEvent) {

    }

    public void setCountriesLOV(RichTable countriesLOV) {
        this.countriesLOV = countriesLOV;
    }

    public RichTable getCountriesLOV() {
        return countriesLOV;
    }

    public void setCountryDesc(RichInputText countryDesc) {
        this.countryDesc = countryDesc;
    }

    public RichInputText getCountryDesc() {
        return countryDesc;
    }

    public void setTownLOV(RichTable townLOV) {
        this.townLOV = townLOV;
    }

    public RichTable getTownLOV() {
        return townLOV;
    }

    public void setTownDesc(RichInputText townDesc) {
        this.townDesc = townDesc;
    }

    public RichInputText getTownDesc() {
        return townDesc;
    }

    public void setClientsTab(RichPanelTabbed clientsTab) {
        this.clientsTab = clientsTab;
    }

    public RichPanelTabbed getClientsTab() {
        return clientsTab;
    }

    public void setSelectClientAction(RichShowDetailItem selectClientAction) {
        this.selectClientAction = selectClientAction;
    }

    public RichShowDetailItem getSelectClientAction() {
        return selectClientAction;
    }

    public void setClientDetails(RichShowDetailItem clientDetails) {
        this.clientDetails = clientDetails;
    }

    public RichShowDetailItem getClientDetails() {
        return clientDetails;
    }

    public void setClientSystems(RichShowDetailItem clientSystems) {
        this.clientSystems = clientSystems;
    }

    public RichShowDetailItem getClientSystems() {
        return clientSystems;
    }

    public void setPrevButton(RichCommandButton prevButton) {
        this.prevButton = prevButton;
    }

    public RichCommandButton getPrevButton() {
        return prevButton;
    }

    public void setNextButton(RichCommandButton nextButton) {
        this.nextButton = nextButton;
    }

    public RichCommandButton getNextButton() {
        return nextButton;
    }

    public void setSectorLOV(RichTable sectorLOV) {
        this.sectorLOV = sectorLOV;
    }

    public RichTable getSectorLOV() {
        return sectorLOV;
    }

    public void setSectorDesc(RichInputText sectorDesc) {
        this.sectorDesc = sectorDesc;
    }

    public RichInputText getSectorDesc() {
        return sectorDesc;
    }

    public void setDomicileCountry(RichInputText domicileCountry) {
        this.domicileCountry = domicileCountry;
    }

    public RichInputText getDomicileCountry() {
        return domicileCountry;
    }

    public void setDomicilecountryLOV(RichTable domicilecountryLOV) {
        this.domicilecountryLOV = domicilecountryLOV;
    }

    public RichTable getDomicilecountryLOV() {
        return domicilecountryLOV;
    }

    public void setBanksLOV(RichTable banksLOV) {
        this.banksLOV = banksLOV;
    }

    public RichTable getBanksLOV() {
        return banksLOV;
    }

    public void setBankDesc(RichInputText bankDesc) {
        this.bankDesc = bankDesc;
    }

    public RichInputText getBankDesc() {
        return bankDesc;
    }

    public void setSearchClients(RichTable searchClients) {
        this.searchClients = searchClients;
    }

    public RichTable getSearchClients() {
        return searchClients;
    }

    public void CreateClientlsnr(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (rdCreateClient.isSelected()) {
            //ClientCode = null;
            session.setAttribute("ClientCode", null);
            searchClients.setVisible(false);

            session.setAttribute("AddEdit", "A");
            GlobalCC.refreshUI(searchClients);
        }
    }

    public void searchClientlsnr(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (rdSearchClient.isSelected()) {
            searchClients.setVisible(true);

            session.setAttribute("AddEdit", "E");
            GlobalCC.refreshUI(searchClients);
        }

    }

    public void setFullName(RichInputText fullName) {
        this.fullName = fullName;
    }

    public RichInputText getFullName() {
        return fullName;
    }

    public void setIdRegNo(RichInputText idRegNo) {
        this.idRegNo = idRegNo;
    }

    public RichInputText getIdRegNo() {
        return idRegNo;
    }

    public void setDoB(RichInputDate doB) {
        this.doB = doB;
    }

    public RichInputDate getDoB() {
        return doB;
    }

    public void setPiNNumber(RichInputText piNNumber) {
        this.piNNumber = piNNumber;
    }

    public RichInputText getPiNNumber() {
        return piNNumber;
    }

    public void setPhysicalAddress(RichInputText physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public RichInputText getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPostalAddress(RichInputText postalAddress) {
        this.postalAddress = postalAddress;
    }

    public RichInputText getPostalAddress() {
        return postalAddress;
    }

    public void setEmailAddress(RichInputText emailAddress) {
        this.emailAddress = emailAddress;
    }

    public RichInputText getEmailAddress() {
        return emailAddress;
    }

    public void setTelephone(RichInputText telephone) {
        this.telephone = telephone;
    }

    public RichInputText getTelephone() {
        return telephone;
    }

    public void setStatus(RichSelectOneChoice status) {
        this.status = status;
    }

    public RichSelectOneChoice getStatus() {
        return status;
    }

    public void setFax(RichInputText fax) {
        this.fax = fax;
    }

    public RichInputText getFax() {
        return fax;
    }

    public void setRemarks(RichInputText remarks) {
        this.remarks = remarks;
    }

    public RichInputText getRemarks() {
        return remarks;
    }

    public void setSpecialTerms(RichSelectOneChoice specialTerms) {
        this.specialTerms = specialTerms;
    }

    public RichSelectOneChoice getSpecialTerms() {
        return specialTerms;
    }

    public void setDeclinedProposal(RichSelectOneChoice declinedProposal) {
        this.declinedProposal = declinedProposal;
    }

    public RichSelectOneChoice getDeclinedProposal() {
        return declinedProposal;
    }

    public void setIncreasedPremium(RichSelectOneChoice increasedPremium) {
        this.increasedPremium = increasedPremium;
    }

    public RichSelectOneChoice getIncreasedPremium() {
        return increasedPremium;
    }

    public void setCancelledPolicy(RichSelectOneChoice cancelledPolicy) {
        this.cancelledPolicy = cancelledPolicy;
    }

    public RichSelectOneChoice getCancelledPolicy() {
        return cancelledPolicy;
    }

    public void setAccNo(RichInputText accNo) {
        this.accNo = accNo;
    }

    public RichInputText getAccNo() {
        return accNo;
    }

    public void setWef(RichInputDate wef) {
        this.wef = wef;
    }

    public RichInputDate getWef() {
        return wef;
    }

    public void setWet(RichInputDate wet) {
        this.wet = wet;
    }

    public RichInputDate getWet() {
        return wet;
    }

    public void setWithdrawalReason(RichInputText withdrawalReason) {
        this.withdrawalReason = withdrawalReason;
    }

    public RichInputText getWithdrawalReason() {
        return withdrawalReason;
    }

    public void setSurname(RichInputText surname) {
        this.surname = surname;
    }

    public RichInputText getSurname() {
        return surname;
    }

    public void setRdIndividual(RichSelectBooleanRadio rdIndividual) {
        this.rdIndividual = rdIndividual;
    }

    public RichSelectBooleanRadio getRdIndividual() {
        return rdIndividual;
    }

    public void setRdCorporate(RichSelectBooleanRadio rdCorporate) {
        this.rdCorporate = rdCorporate;
    }

    public RichSelectBooleanRadio getRdCorporate() {
        return rdCorporate;
    }

    public void setTitle(RichSelectOneChoice title) {
        this.title = title;
    }

    public RichSelectOneChoice getTitle() {
        return title;
    }

    public void setBankAccount(RichInputText bankAccount) {
        this.bankAccount = bankAccount;
    }

    public RichInputText getBankAccount() {
        return bankAccount;
    }

    public void setDirect(RichSelectOneChoice direct) {
        this.direct = direct;
    }

    public RichSelectOneChoice getDirect() {
        return direct;
    }

    public void setCreatedBy(RichInputText createdBy) {
        this.createdBy = createdBy;
    }

    public RichInputText getCreatedBy() {
        return createdBy;
    }

    public void setSmsNo(RichInputText smsNo) {
        this.smsNo = smsNo;
    }

    public RichInputText getSmsNo() {
        return smsNo;
    }

    public void setDateCreated(RichInputDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public RichInputDate getDateCreated() {
        return dateCreated;
    }

    public void setRunOff(RichSelectOneChoice runOff) {
        this.runOff = runOff;
    }

    public RichSelectOneChoice getRunOff() {
        return runOff;
    }

    public void setConfirmClient(RichShowDetailItem confirmClient) {
        this.confirmClient = confirmClient;
    }

    public RichShowDetailItem getConfirmClient() {
        return confirmClient;
    }

    public void setFirstName(RichInputText firstName) {
        this.firstName = firstName;
    }

    public RichInputText getFirstName() {
        return firstName;
    }

    public void setMiddleName(RichInputText middleName) {
        this.middleName = middleName;
    }

    public RichInputText getMiddleName() {
        return middleName;
    }

    public void setSearchCriteria(RichSelectOneChoice searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public RichSelectOneChoice getSearchCriteria() {
        return searchCriteria;
    }

    public void setAvailableSystems(RichTable availableSystems) {
        this.availableSystems = availableSystems;
    }

    public RichTable getAvailableSystems() {
        return availableSystems;
    }

    public void setAssignedsystems(RichTable assignedsystems) {
        this.assignedsystems = assignedsystems;
    }

    public RichTable getAssignedsystems() {
        return assignedsystems;
    }

    public void firstNameListener(AttributeChangeEvent attributeChangeEvent) {
        // Add event code here...


    }

    public void middlenamelistener(AttributeChangeEvent attributeChangeEvent) {
        // Add event code here...


    }

    public void surnamelistener(AttributeChangeEvent attributeChangeEvent) {
        // Add event code here...


    }

    public void postalAddress(AttributeChangeEvent attributeChangeEvent) {
        // Add event code here...

    }

    public void pinnumberlistener(AttributeChangeEvent attributeChangeEvent) {
        // Add event code here...

    }

    public void searchlistener(AttributeChangeEvent attributeChangeEvent) {
        // Add event code here...

    }

    public void firstnameListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (firstName.getValue() == null) {

        } else {
            session.setAttribute("FirstName", firstName.getValue().toString());
        }
    }

    public void middlenamelistener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (middleName.getValue() == null) {

        } else {
            session.setAttribute("MiddleName",
                                 middleName.getValue().toString());
        }
    }

    public void lastnamelistener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (surname.getValue() == null) {

        } else {
            session.setAttribute("LastName", surname.getValue().toString());
        }
    }

    public void postallistener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (postalAddress.getValue() == null) {

        } else {
            session.setAttribute("PostalAddress",
                                 postalAddress.getValue().toString());
        }
    }

    public void pinlistener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (piNNumber.getValue() == null) {

        } else {
            session.setAttribute("PINNumber", piNNumber.getValue().toString());
        }
    }

    public void searchlistener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (searchCriteria.getValue() == null) {

        } else {
            session.setAttribute("SearchCriteria",
                                 searchCriteria.getValue().toString());
        }
    }

    public void setId(RichInputText id) {
        this.id = id;
    }

    public RichInputText getId() {
        return id;
    }

    public void setHoldingCompany(RichSelectOneChoice holdingCompany) {
        this.holdingCompany = holdingCompany;
    }

    public RichSelectOneChoice getHoldingCompany() {
        return holdingCompany;
    }

    public void directListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (direct.getValue() == null) {

        } else {
            session.setAttribute("Direct", direct.getValue().toString());
        }


    }

    public void idListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (id.getValue() == null) {

        } else {
            session.setAttribute("id1", id.getValue().toString());
        }
    }

    public void physicalAddressListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (physicalAddress.getValue() == null) {

        } else {
            session.setAttribute("PhyAddress",
                                 physicalAddress.getValue().toString());
        }

    }

    public void regListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (idRegNo.getValue() == null) {

        } else {
            session.setAttribute("RegNo", idRegNo.getValue().toString());
        }
    }

    public void titleListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (title.getValue() == null) {

        } else {
            session.setAttribute("TitleListener", title.getValue().toString());
        }
    }

    public void telephonelistener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (telephone.getValue() == null) {

        } else {
            session.setAttribute("telephone1",
                                 telephone.getValue().toString());
        }

    }

    public void smslistener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (smsNo.getValue() == null) {

        } else {
            session.setAttribute("sms", smsNo.getValue().toString());
        }
    }

    public void faxListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (fax.getValue() == null) {

        } else {
            session.setAttribute("fax1", fax.getValue().toString());
        }
    }

    public void emailListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (emailAddress.getValue() == null) {

        } else {
            session.setAttribute("email", emailAddress.getValue().toString());
        }
    }

    public void bankListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (bankAccount.getValue() == null) {

        } else {
            session.setAttribute("bankAcc", bankAccount.getValue().toString());
        }
    }

    public void remarksListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (remarks.getValue() == null) {

        } else {
            session.setAttribute("remarks1", remarks.getValue().toString());
        }
    }

    public void WefListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        // Date coverFrom = new Date();
        SimpleDateFormat sdf1 =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy");

        String wefString;


        java.util.Date lossDate = new java.util.Date();


        lossDate = null;


        try {
            lossDate = sdf1.parse(wef.getValue().toString());
            WEFDate = sdf2.format(lossDate);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }


    }

    public void wetlistener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        SimpleDateFormat sdf1 =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy");

        java.util.Date notificationDate = new java.util.Date();
        String wetString;

        notificationDate = null;

        try {
            notificationDate = sdf1.parse(wet.getValue().toString());
            WETDate = sdf2.format(notificationDate);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }


    }

    public String ClientSystemValues() {

        String query =
            "begin ? := tqc_clients_pkg.get_clnt_unallcted_sys(?); end;";
        OracleCallableStatement cst = null;
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = (OracleConnection)datahandler.getDatabaseConnection();
        try {


            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setObject(2, session.getAttribute("ClientCode"));
            cst.execute();
            ResultSet rs = (ResultSet)cst.getObject(1);
            int t = 0;
            while (t < selectValues.size()) {
                selectValues.remove(t);
                t++;
            }
            while (rs.next()) {
                SelectItem selectItem = new SelectItem();

                selectItem.setValue(rs.getBigDecimal(1).toString());
                selectItem.setDescription(rs.getString(3));
                selectItem.setLabel(rs.getString(3));
                selectValues.add(selectItem);
            }

            rs.close();
            //conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        String userQuery =
            "begin ? := tqc_clients_pkg.get_clnt_allcted_sys(?); end;";
        OracleCallableStatement cstUsr = null;
        try {

            cstUsr = (OracleCallableStatement)conn.prepareCall(userQuery);
            cstUsr.registerOutParameter(1,
                                        OracleTypes.CURSOR); //authorization code
            cstUsr.setObject(2, session.getAttribute("ClientCode"));

            cstUsr.execute();
            ResultSet rsUser = (ResultSet)cstUsr.getObject(1);
            int t = 0;
            while (t < displayValue.size()) {
                displayValue.remove(t);
                t++;
            }

            while (rsUser.next()) {
                displayValue.add(rsUser.getBigDecimal(1).toString());
            }

            rsUser.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        if (clientSystemRoles == null) {
        } else {
            clientSystemRoles.setValue(displayValue);
            ClientSystemRoleSelect.setValue(selectValues);
        }
        return null;
    }

    public void ClientSytemsGrant(ValueChangeEvent valueChangeEvent) {
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            try {
                conn = (OracleConnection)datahandler.getDatabaseConnection();
                ArrayList<String> myVals =
                    (ArrayList<String>)clientSystemRoles.getValue();
                ArrayList<SelectItem> myVals2 =
                    (ArrayList<SelectItem>)ClientSystemRoleSelect.getValue();
                int v = 0;
                String revokeQuery =
                    "begin tqc_clients_pkg.unalloc_clnt_system(?,?); end;";

                while (v < myVals2.size()) {
                    SelectItem select = myVals2.get(v);
                    OracleCallableStatement cst = null;


                    cst =
(OracleCallableStatement)conn.prepareCall(revokeQuery);
                    cst.setObject(1, session.getAttribute("ClientCode"));
                    cst.setString(2, (String)select.getValue());
                    cst.execute();

                    v++;
                }
                String query =
                    "begin tqc_clients_pkg.alloc_clnt_system(?,?); end;";
                int k = 0;
                while (k < myVals.size()) {
                    OracleCallableStatement cst = null;


                    cst = (OracleCallableStatement)conn.prepareCall(query);
                    cst.setObject(1, session.getAttribute("ClientCode"));
                    cst.setString(2, (String)myVals.get(k));
                    cst.execute();


                    k++;
                }

                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
    }

    public String AddClientSystem() {
        BigDecimal SysCode = null;

        DCIteratorBinding dciter =
            ADFUtils.findIterator("findUnallocatedClientSystemsIterator");
        RowKeySet set = availableSystems.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();

            SysCode = (BigDecimal)r.getAttribute("unallocatedSystemCode");

        }


        //TODO
        String Query = "begin tqc_clients_pkg.alloc_clnt_system(?,?); end;";

        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        try {

            DBConnector datahandler = new DBConnector();

            conn = (OracleConnection)datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            cst.setObject(1, session.getAttribute("ClientCode"));
            cst.setBigDecimal(2, SysCode);

            cst.execute();
            conn.close();

            ADFUtils.findIterator("findUnallocatedClientSystemsIterator").executeQuery();
            GlobalCC.refreshUI(availableSystems);
            ADFUtils.findIterator("findallocatedClientSystemsIterator").executeQuery();
            GlobalCC.refreshUI(assignedsystems);

        } catch (Exception e) {

            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String RemoveClientSystem() {
        BigDecimal SysCode = null;

        DCIteratorBinding dciter =
            ADFUtils.findIterator("findallocatedClientSystemsIterator");
        RowKeySet set = assignedsystems.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();

            SysCode = (BigDecimal)r.getAttribute("allocatefSystemCode");

        }


        //TODO
        String Query = "begin tqc_clients_pkg.unalloc_clnt_system(?,?); end;";

        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        try {

            DBConnector datahandler = new DBConnector();

            conn = (OracleConnection)datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            cst.setObject(1, session.getAttribute("ClientCode"));
            cst.setBigDecimal(2, SysCode);

            cst.execute();
            conn.close();

            ADFUtils.findIterator("findUnallocatedClientSystemsIterator").executeQuery();
            GlobalCC.refreshUI(availableSystems);
            ADFUtils.findIterator("findallocatedClientSystemsIterator").executeQuery();
            GlobalCC.refreshUI(assignedsystems);

        } catch (Exception e) {

            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void dobListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        SimpleDateFormat sdf1 =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy");

        java.util.Date DOB = new java.util.Date();
        String DOBString;

        try {
            DOB = sdf1.parse(doB.getValue().toString());
            DOBDate = sdf2.format(DOB);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }


    }

    public void setUserName(RichInputText userName) {
        this.userName = userName;
    }

    public RichInputText getUserName() {
        return userName;
    }

    public void setFullNames(RichInputText fullNames) {
        this.fullNames = fullNames;
    }

    public RichInputText getFullNames() {
        return fullNames;
    }

    public void setPassword(RichInputText password) {
        this.password = password;
    }

    public RichInputText getPassword() {
        return password;
    }

    public void setPersonelRank(RichInputText personelRank) {
        this.personelRank = personelRank;
    }

    public RichInputText getPersonelRank() {
        return personelRank;
    }

    public void setUserstatus(RichSelectOneChoice userstatus) {
        this.userstatus = userstatus;
    }

    public RichSelectOneChoice getUserstatus() {
        return userstatus;
    }

    public void setAllowLogin(RichSelectOneChoice allowLogin) {
        this.allowLogin = allowLogin;
    }

    public RichSelectOneChoice getAllowLogin() {
        return allowLogin;
    }

    public void setWebemailAddress(RichInputText webemailAddress) {
        this.webemailAddress = webemailAddress;
    }

    public RichInputText getWebemailAddress() {
        return webemailAddress;
    }

    public void setWebClientAccounts(RichTable webClientAccounts) {
        this.webClientAccounts = webClientAccounts;
    }

    public RichTable getWebClientAccounts() {
        return webClientAccounts;
    }

    public void setWebAccTab(RichShowDetailItem webAccTab) {
        this.webAccTab = webAccTab;
    }

    public RichShowDetailItem getWebAccTab() {
        return webAccTab;
    }

    public void setSelectValues(List<SelectItem> selectValues) {
        this.selectValues = selectValues;
    }

    public List<SelectItem> getSelectValues() {
        return selectValues;
    }

    public void setDisplayValue(List<String> displayValue) {
        this.displayValue = displayValue;
    }

    public List<String> getDisplayValue() {
        return displayValue;
    }

    public void setClientSystemRoles(RichSelectManyShuttle clientSystemRoles) {
        this.clientSystemRoles = clientSystemRoles;
    }

    public RichSelectManyShuttle getClientSystemRoles() {
        return clientSystemRoles;
    }

    public void setClientSystemRoleSelect(UISelectItems ClientSystemRoleSelect) {
        this.ClientSystemRoleSelect = ClientSystemRoleSelect;
    }

    public UISelectItems getClientSystemRoleSelect() {
        return ClientSystemRoleSelect;
    }

    public void setAccMangrDesc(RichInputText accMangrDesc) {
        this.accMangrDesc = accMangrDesc;
    }

    public RichInputText getAccMangrDesc() {
        return accMangrDesc;
    }

    public void setAccMangrLOV(RichTable accMangrLOV) {
        this.accMangrLOV = accMangrLOV;
    }

    public RichTable getAccMangrLOV() {
        return accMangrLOV;
    }

    public void selectTypeOfClient(ValueChangeEvent valueChangeEvent) {

        if (rdIndividual.isSelected()) {

            title.setDisabled(false);
            lblClientName.setValue("First Name");
            lblDOB.setValue("Date of Birth");
            middleName.setDisabled(false);
            middleName.setDisabled(false);
            surname.setDisabled(false);
            fullName.setDisabled(true);
            btnShowContactPersons.setVisible(false);
            btnShowContactPersons.setDisabled(true);
        } else {

            title.setDisabled(true);
            lblClientName.setValue("Corporate Name");
            lblDOB.setValue("Date of Incorporation");
            middleName.setValue("N/A");
            middleName.setDisabled(true);
            surname.setValue("N/A");
            middleName.setDisabled(true);
            surname.setDisabled(true);
            fullName.setValue("N/A");
            fullName.setDisabled(true);
            btnShowContactPersons.setVisible(true);
            btnShowContactPersons.setDisabled(false);
        }

        GlobalCC.refreshUI(clientDetails);
    }

    public void setLblClientName(RichOutputLabel lblClientName) {
        this.lblClientName = lblClientName;
    }

    public RichOutputLabel getLblClientName() {
        return lblClientName;
    }

    public String actionAcceptBranchDivision() {
        DCIteratorBinding binder =
            ADFUtils.findIterator("fetchDivisionByBranchIterator");
        RowKeySet set = tblBranchDivisons.getSelectedRowKeys();
        Iterator row = set.iterator();

        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();

            clientBranchDivisionCode.setValue(rows.getAttribute("DIV_CODE"));
            clientBranchDivision.setValue(rows.getAttribute("DIV_NAME"));
        }
        return null;
    }

    public void setClientBranchDivision(RichInputText clientBranchDivision) {
        this.clientBranchDivision = clientBranchDivision;
    }

    public RichInputText getClientBranchDivision() {
        return clientBranchDivision;
    }

    public void setClientBranchDivisionCode(RichInputText clientBranchDivisionCode) {
        this.clientBranchDivisionCode = clientBranchDivisionCode;
    }

    public RichInputText getClientBranchDivisionCode() {
        return clientBranchDivisionCode;
    }

    public void setTblBranchDivisons(RichTable tblBranchDivisons) {
        this.tblBranchDivisons = tblBranchDivisons;
    }

    public RichTable getTblBranchDivisons() {
        return tblBranchDivisons;
    }

    public String selectABranchdivision() {
        // Check if a branch code exists
        if ((BigDecimal)session.getAttribute("branchCode") == null ||
            ((BigDecimal)session.getAttribute("branchCode")).equals("")) {
            GlobalCC.INFORMATIONREPORTING("You need to select a Branch!");

        } else {

            // Open the popup dialog to display towns
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crmTemplate:branchDivisionPop" +
                                 "').show(hints);");
        }
        return null;
    }

    public String actionAcceptContactPersons() {

        txtContactPhone1.setValue(contactPhone1.getValue());
        txtContactEmail1.setValue(contactEmail1.getValue());
        txtContactPhone2.setValue(contactPhone2.getValue());
        txtContactEmail2.setValue(contactEmail2.getValue());

        return null;
    }

    public void setTxtContactPhone1(RichInputText txtContactPhone1) {
        this.txtContactPhone1 = txtContactPhone1;
    }

    public RichInputText getTxtContactPhone1() {
        return txtContactPhone1;
    }

    public void setTxtContactEmail1(RichInputText txtContactEmail1) {
        this.txtContactEmail1 = txtContactEmail1;
    }

    public RichInputText getTxtContactEmail1() {
        return txtContactEmail1;
    }

    public void setTxtContactPhone2(RichInputText txtContactPhone2) {
        this.txtContactPhone2 = txtContactPhone2;
    }

    public RichInputText getTxtContactPhone2() {
        return txtContactPhone2;
    }

    public void setTxtContactEmail2(RichInputText txtContactEmail2) {
        this.txtContactEmail2 = txtContactEmail2;
    }

    public RichInputText getTxtContactEmail2() {
        return txtContactEmail2;
    }

    public void setContactPhone1(RichInputText contactPhone1) {
        this.contactPhone1 = contactPhone1;
    }

    public RichInputText getContactPhone1() {
        return contactPhone1;
    }

    public void setContactEmail1(RichInputText contactEmail1) {
        this.contactEmail1 = contactEmail1;
    }

    public RichInputText getContactEmail1() {
        return contactEmail1;
    }

    public void setContactPhone2(RichInputText contactPhone2) {
        this.contactPhone2 = contactPhone2;
    }

    public RichInputText getContactPhone2() {
        return contactPhone2;
    }

    public void setContactEmail2(RichInputText contactEmail2) {
        this.contactEmail2 = contactEmail2;
    }

    public RichInputText getContactEmail2() {
        return contactEmail2;
    }

    public void setBtnShowContactPersons(RichCommandButton btnShowContactPersons) {
        this.btnShowContactPersons = btnShowContactPersons;
    }

    public RichCommandButton getBtnShowContactPersons() {
        return btnShowContactPersons;
    }

    public String actionShowContactPersons() {

        contactPhone1.setValue("");
        contactEmail1.setValue("");
        contactPhone2.setValue("");
        contactEmail2.setValue("");

        // Open the popup dialog to add new contact Persons
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crmTemplate:contactPersonsPopup" +
                             "').show(hints);");
        return null;
    }

    public void setLblDOB(RichOutputLabel lblDOB) {
        this.lblDOB = lblDOB;
    }

    public RichOutputLabel getLblDOB() {
        return lblDOB;
    }
}
