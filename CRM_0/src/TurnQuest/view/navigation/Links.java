package TurnQuest.view.navigation;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.LOVDAO;
import TurnQuest.view.Base.Rendering;
import TurnQuest.view.Connect.Authorization;
import TurnQuest.view.Login.Index;

import java.util.Map;
import java.util.Set;

import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;


public class Links {

    private RichPanelGroupLayout navCont;

    public Links() {
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);


    private String applicationLocation;
    private RichPanelBox personnelSysPan;
    private RichPanelBox payrollPan;
    private RichPanelBox coreSetupPan;
    private RichPanelBox incidencesPanel;

    /**
     * Logout by invalidating the session and redirecting to the home page
     * The container will ask user to re-authenticate automatically.
     * @return null
     * @throws IOException
     */
    public String logOut() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext ectx = ctx.getExternalContext();
        HttpSession session = (HttpSession)ectx.getSession(false);

        if (session != null) {
            session.invalidate();
        }


        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/index.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        // Tell faces that we're handling the navigation from here and that there
        // is no need for a new context to be created
        ctx.responseComplete();
        return null;
    }

    public String goHome() {
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            lov.resetPageButtons();
            session.setAttribute("MM", "home");
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/home.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return "failed";
    }

    public String goToSetups() {

        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            session.setAttribute("MM", "CoreSetups");
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/coreSetups.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return "failed";
    }

    public String goToServiceRequests() {

        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/serviceRequests.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return "failed";
    }

    public String goToLeadSources() {


        Authorization auth = new Authorization();
        String process = "LP";
        String processArea = "LPLS";
        String processSubArea = "LPLSA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/leadSources.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;

    }

    public String goToLeadStatuses() {


        Authorization auth = new Authorization();
        String process = "LP";
        String processArea = "LPS";
        String processSubArea = "LPSA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/leadStatuses.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }


        return null;


    }

    public String showUsers() {


        Authorization auth = new Authorization();
        String process = "SA";
        String processArea = "SAU";
        String processSubArea = "SAUA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/users.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }


        return null;


    }


    public String payrollSele() {
        personnelSysPan.setRendered(false);
        payrollPan.setRendered(true);
        coreSetupPan.setRendered(false);
        incidencesPanel.setRendered(false);
        if (navCont != null) {
            GlobalCC.refreshUI(navCont);
        }
        return null;
    }

    public String showAdministration() {

        try {
            //LOVDAO lov = new LOVDAO();
            //lov.reInitializeVariables();
            session.setAttribute("KK", "administration");
            if (navCont != null) {
                GlobalCC.refreshUI(navCont);
            }
            //FacesContext fc = FacesContext.getCurrentInstance();
            //UIViewRoot viewRoot = fc.getApplication().getViewHandler().createView(fc, "/home.jspx");
            //fc.setViewRoot(viewRoot);
            // fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;


    }

    public String showPortalSetUps() {

        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            lov.resetPageButtons();
            session.setAttribute("MM", "portalSetUps");
            if (navCont != null) {
                GlobalCC.refreshUI(navCont);
            }
            //FacesContext fc = FacesContext.getCurrentInstance();
            //UIViewRoot viewRoot = fc.getApplication().getViewHandler().createView(fc, "/home.jspx");
            //fc.setViewRoot(viewRoot);
            // fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;


    }

    public String showClients() {

        Authorization auth = new Authorization();
        String process = "AMA";
        String processArea = "AMAC";
        String processSubArea = "AMCA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();

            session.removeAttribute("myTrigger");
            session.removeAttribute("ClientCode");

            if (session.getAttribute("DEFAULT_BRANCH_CODE") == null) {
                Index ind = new Index();
                ind.getUserDefaults();
                if (session.getAttribute("DEFAULT_BRANCH_CODE") == null) {
                    GlobalCC.INFORMATIONREPORTING("Please set your 'Default Branch' to Continue");
                    return null;
                }
            }
            FacesContext xfc = FacesContext.getCurrentInstance();
            xfc.getExternalContext().redirect("clients.jspx");


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;

    }

    public String showSaccos() {

        Authorization auth = new Authorization();
        String process = "AMA";
        String processArea = "AMAS";
        String processSubArea = "AMASA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            session.removeAttribute("myTrigger");
            if (session.getAttribute("DEFAULT_BRANCH_CODE") == null) {
                Index ind = new Index();
                ind.getUserDefaults();
                if (session.getAttribute("DEFAULT_BRANCH_CODE") == null) {
                    GlobalCC.INFORMATIONREPORTING("Please set your 'Default Branch' to Continue");
                    return null;
                }
            }

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().redirect("saccos.jspx");


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }


        return null;

    }

    public String UserRoles() {
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/userRoles.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }


    public void setApplicationLocation(String applicationLocation) {
        this.applicationLocation = applicationLocation;
    }

    public String getApplicationLocation() {
        return applicationLocation;
    }

    public void setPersonnelSysPan(RichPanelBox personnelSysPan) {
        this.personnelSysPan = personnelSysPan;
    }

    public RichPanelBox getPersonnelSysPan() {
        return personnelSysPan;
    }

    public void setPayrollPan(RichPanelBox payrollPan) {
        this.payrollPan = payrollPan;
    }

    public RichPanelBox getPayrollPan() {
        return payrollPan;
    }

    public void setCoreSetupPan(RichPanelBox coreSetupPan) {
        this.coreSetupPan = coreSetupPan;
    }

    public RichPanelBox getCoreSetupPan() {
        return coreSetupPan;
    }

    public void setIncidencesPanel(RichPanelBox incidencesPanel) {
        this.incidencesPanel = incidencesPanel;
    }

    public RichPanelBox getIncidencesPanel() {
        return incidencesPanel;
    }

    public String showHoldingCompanies() {


        Authorization auth = new Authorization();
        String process = "AMA";
        String processArea = "AMHC";
        String processSubArea = "AMHCA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/holdingCompanies.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;

    }

    public String showBanks() {


        Authorization auth = new Authorization();
        String process = "BS";
        String processArea = "BSB";
        String processSubArea = "BSBA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/banksDefinition.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;

    }

    public String showAgencyClassification() {

        Authorization auth = new Authorization();
        String process = "AMA";
        String processArea = "AMC";
        String processSubArea = "AMACA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/agencyClasses.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;

    }

    public String showAgencyLoading() {

        Authorization auth = new Authorization();
        String process = "AMA";
        String processArea = "AMAAL";
        String processSubArea = "AMACLA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/agentLoading.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String showBankAccountsLoading() {

        Authorization auth = new Authorization();
        String process = "AMA";
        String processArea = "AMAAL";
        String processSubArea = "AMACLA";
        //       String AccessGranted =
        //            auth.checkUserRights(process, processArea, processSubArea, null,
        //                                 null);
        //        if(AccessGranted.equalsIgnoreCase("Y")!=true) {
        //            GlobalCC.accessDenied();
        //            return null;
        //        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/bankAccountsLoading.jspx.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String showCurrencyRates() {

        Authorization auth = new Authorization();
        String process = "OP";
        String processArea = "OPCR";
        String processSubArea = "OPCRA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/currencyRatesDefinition.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showCurrencies() {
        Authorization auth = new Authorization();
        String process = "OP";
        String processArea = "OPC";
        String processSubArea = "OPCA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/currencyDefinitions.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String showSectors() {


        Authorization auth = new Authorization();
        String process = "AMA";
        String processArea = "AMS";
        String processSubArea = "AMSA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/sectors.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;

    }

    public String showServiceProviderTypes() {
        Authorization auth = new Authorization();
        String process = "AMA";
        String processArea = "AMSPT";
        String processSubArea = "AMSPTA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/serviceProviderTypes.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String showCountries() {

        Authorization auth = new Authorization();
        String process = "OS";
        String processArea = "OSC";
        String processSubArea = "OSCA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/countries.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showUserParameters() {

        Authorization auth = new Authorization();
        String process = "OP";
        String processArea = "OPUP";
        String processSubArea = "OPUPA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/userParameters.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;

    }

    public String showUserLabels() {

        Authorization auth = new Authorization();
        String process = "OP";
        String processArea = "OPSL";
        String processSubArea = "OPSLA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/sysLabel.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }


    public String showAccountDefinitions() {


        Authorization auth = new Authorization();
        String process = "AMA";
        String processArea = "AMA";
        String processSubArea = "AMAA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }

        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            session.removeAttribute("myTrigger");
            if (session.getAttribute("DEFAULT_BRANCH_CODE") == null) {
                Index ind = new Index();
                ind.getUserDefaults();
                if (session.getAttribute("DEFAULT_BRANCH_CODE") == null) {
                    GlobalCC.INFORMATIONREPORTING("Please set your 'Default Branch' to Continue");
                    return null;
                }
            }
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/accounts.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }


        return null;
    }


    public String showAccountTypes() {


        Authorization auth = new Authorization();
        String process = "AMA";
        String processArea = "AMT";
        String processSubArea = "AMTA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/accounttypes.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }


        return null;

    }

    public String showEntities() {


        Authorization auth = new Authorization();
        String process = "AMA";
        String processArea = "AMAAE";
        String processSubArea = "AMAAEA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/showEntities.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }


        return null;

    }

    public String showConsolidateAccounts() {


        Authorization auth = new Authorization();
        String process = "AMA";
        String processArea = "AMACA";
        String processSubArea = "AMACAA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/consolidateAccounts.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;

    }

    public String showClientTypes() {


        Authorization auth = new Authorization();
        String process = "AMA";
        String processArea = "AMT";
        String processSubArea = "AMTA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/ClientTypes.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;

    }

    public String showAgentTypes() {

        Authorization auth = new Authorization();
        String process = "AMA";
        String processArea = "AMT";
        String processSubArea = "AMTA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/AgentTypes.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String showOrganizations() {

        Authorization auth = new Authorization();
        String process = "OS";
        String processArea = "OSO";
        String processSubArea = "OSOA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/organization.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;
    }

    public String showDivisions() {
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/divisions.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String showRequiredDocuments() {

        Authorization auth = new Authorization();
        String process = "OP";
        String processArea = "OPRD";
        String processSubArea = "OPRDA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/requiredDocuments.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String showServiceProviders() {


        Authorization auth = new Authorization();
        String process = "AMA";
        String processArea = "AMSP";
        String processSubArea = "AMSPA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            session.setAttribute("docAccountType", "SP");
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/serviceProviders.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showSystemRoles() {

        Authorization auth = new Authorization();
        String process = "SA";
        String processArea = "SAR";
        String processSubArea = "SARA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        Rendering renderer = new Rendering();
        boolean newScreen = renderer.isNewRolesScreenSet();
        String rolesScreenLink;
        if (newScreen) {
            rolesScreenLink = "/sysProcessRolesNew.jspx";
        } else {
            rolesScreenLink = "/sysProcessRoles.jspx";
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                rolesScreenLink);
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;
    }

    public String showProposalForms() {

        Authorization auth = new Authorization();

        String process = "POTSU";
        String processArea = "POTSUF";
        String processSubArea = "POTSUFA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/modSubUnits.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;


    }

    public String showClientContent() {

        Authorization auth = new Authorization();
        String process = "SA";
        String processArea = "SAR";
        String processSubArea = "SARA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/webProducts.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String showWebScldesc() {

        Authorization auth = new Authorization();
        String process = "SA";
        String processArea = "SAR";
        String processSubArea = "SARA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/SubClassDescSetUp.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String showUserRoles() {
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/userRoles.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String showPaymentModes() {

        Authorization auth = new Authorization();
        String process = "OP";
        String processArea = "OP";
        String processSubArea = "OPA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/paymentModes.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showProspects() {

        Authorization auth = new Authorization();
        String process = "AM";
        String processArea = "AMP";
        String processSubArea = "AMPA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/prospectsDefinitions.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;

    }

    public String showClientTitles() {
        Authorization auth = new Authorization();
        String process = "AMA";
        String processArea = "AMACT";
        String processSubArea = "AMACTA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/clientTitles.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;

    }


    public String showClaimMemorandums() {

        Authorization auth = new Authorization();
        String process = "M";
        String processArea = "MMS";
        String processSubArea = "MMSA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/memoSetup.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showDirectDebits() {


        Authorization auth = new Authorization();
        String process = "BS";
        String processArea = "BSDD";
        String processSubArea = "BSDDA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/updateDirectDebits.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showBankBranches() {
        Authorization auth = new Authorization();
        String process = "BS";
        String processArea = "BSBB";
        String processSubArea = "BSBBA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }


        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/bankBranches.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showDDFailedRmks() {
        Authorization auth = new Authorization();
        String process = "BS";
        String processArea = "BSDFR";
        String processSubArea = "BSDFRA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/DDfailRemarksSetups.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showMessageTemplate() {


        Authorization auth = new Authorization();
        String process = "M";
        String processArea = "MMT";
        String processSubArea = "MMTA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/messageTemplate.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }


        return null;
    }

    public String showSingleMessage() {

        Authorization auth = new Authorization();
        String process = "M";
        String processArea = "MSM";
        String processSubArea = "MSMA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/singleMessage.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showIncomingMessages() {

        Authorization auth = new Authorization();
        String process = "M";
        String processArea = "MIM";
        String processSubArea = "MIMA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/incomingMessage.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showIncidenceSetups() {


        Authorization auth = new Authorization();
        String process = "M";
        String processArea = "MIS";
        String processSubArea = "MISA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/incidenceSetups.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;

    }

    public String showHierarchies() {


        Authorization auth = new Authorization();
        String process = "OS";
        String processArea = "OSH";
        String processSubArea = "OSHA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/hierarchies.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }


    public String showCountryHolidays() {
        Authorization auth = new Authorization();
        String process = "OS";
        String processArea = "OSCH";
        String processSubArea = "OSCHA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/countryHolidays.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showActivityTypes() {


        Authorization auth = new Authorization();
        String process = "AM";
        String processArea = "AMAT";
        String processSubArea = "AMATA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/activityTypes.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showSysPostLevels() {


        Authorization auth = new Authorization();
        String process = "OS";
        String processArea = "OSP";
        String processSubArea = "OSPA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/postLevels.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showPayModes() {


        Authorization auth = new Authorization();
        String process = "OP";
        String processArea = "OPPM";
        String processSubArea = "OPPMA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/ClaimsPayMode.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showMobileServiceProviders() {


        Authorization auth = new Authorization();
        String process = "M";
        String processArea = "MMSP";
        String processSubArea = "MMSPA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/mobileServiceProviders.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showPrinterSetups() {


        Authorization auth = new Authorization();
        String process = "OP";
        String processArea = "OPPS";
        String processSubArea = "OPPSA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/printerSetups.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showSystemApplicableArea() {

        Authorization auth = new Authorization();
        String process = "OP";
        String processArea = "OPSP";
        String processSubArea = "OPSPA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/systemApplicableAreas.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }


    public String showSequences() {


        Authorization auth = new Authorization();
        String process = "OP";
        String processArea = "OPS";
        String processSubArea = "OPSA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/sequences.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showAlerts() {


        Authorization auth = new Authorization();
        String process = "M";
        String processArea = "ALT";
        String processSubArea = "ALTA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/alerts.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showProductAttributes() {


        Authorization auth = new Authorization();
        String process = "CM";
        String processArea = "CMPA";
        String processSubArea = "CMPAA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/products.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showClientAttributes() {


        Authorization auth = new Authorization();
        String process = "CM";
        String processArea = "CMCA";
        String processSubArea = "CMCAA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/clientAttributesDefinations.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showLeads() {


        Authorization auth = new Authorization();
        String process = "LP";
        String processArea = "LPL";
        String processSubArea = "LPLA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/leads.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showActivities() {


        Authorization auth = new Authorization();
        String process = "AM";
        String processArea = "AMA";
        String processSubArea = "AMAA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            session.setAttribute("messageType", "EMAIL");
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/Activities.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showCampaignManager() {


        Authorization auth = new Authorization();
        String process = "CM";
        String processArea = "CMC";
        String processSubArea = "CMCA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/CampaignManagement.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showStatuses() {


        Authorization auth = new Authorization();
        String process = "AM";
        String processArea = "AMAS";
        String processSubArea = "AMASA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/activityStatuses.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showPriorityLevels() {


        Authorization auth = new Authorization();
        String process = "AM";
        String processArea = "AMPL";
        String processSubArea = "AMPLA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/PriorityLevels.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showReportGroups() {


        Authorization auth = new Authorization();
        String process = "SR";
        String processArea = "SRRG";
        String processSubArea = "SRRGA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/reportGroups.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showReports() {

        Authorization auth = new Authorization();
        String process = "SR";
        String processArea = "SRR";
        String processSubArea = "SRRA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/sysReports.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String showSystemReports() {

        try {
            // LOVDAO lov = new LOVDAO();
            // lov.reInitializeVariables();
            session.setAttribute("KK", "SystemReports");
            if (navCont != null) {
                GlobalCC.refreshUI(navCont);
            }
            //FacesContext fc = FacesContext.getCurrentInstance();
            // UIViewRoot viewRoot = fc.getApplication().getViewHandler().createView(fc, "/home.jspx");
            // fc.setViewRoot(viewRoot);
            // fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;


    }

    public String showOrgSetups() {
        try {
            //LOVDAO lov = new LOVDAO();
            // lov.reInitializeVariables();
            session.setAttribute("KK", "OrganizationalSetups");
            if (navCont != null) {
                GlobalCC.refreshUI(navCont);
            }
            // FacesContext fc = FacesContext.getCurrentInstance();
            // UIViewRoot viewRoot = fc.getApplication().getViewHandler().createView(fc, "/home.jspx");
            //  fc.setViewRoot(viewRoot);
            //  fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;
    }

    public String showOrgSetupsGl() {
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            lov.resetPageButtons();
            session.setAttribute("MM", "OrganizationalSetupsGL");
            if (navCont != null) {
                GlobalCC.refreshUI(navCont);
            }
            // FacesContext fc = FacesContext.getCurrentInstance();
            // UIViewRoot viewRoot = fc.getApplication().getViewHandler().createView(fc, "/home.jspx");
            //  fc.setViewRoot(viewRoot);
            //  fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;
    }

    public String showOrgParameters() {
        try {
            // LOVDAO lov = new LOVDAO();
            // lov.reInitializeVariables();
            session.setAttribute("KK", "OrganizParameters");
            if (navCont != null) {
                GlobalCC.refreshUI(navCont);
            }
            // FacesContext fc = FacesContext.getCurrentInstance();
            //UIViewRoot viewRoot = fc.getApplication().getViewHandler().createView(fc, "/home.jspx");
            // fc.setViewRoot(viewRoot);
            // fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String showAccountManagement() {
        try {
            //            LOVDAO lov = new LOVDAO();
            //            lov.reInitializeVariables();
            session.setAttribute("KK", "AccountsManagement");
            if (navCont != null) {
                GlobalCC.refreshUI(navCont);
            }
            //FacesContext fc = FacesContext.getCurrentInstance();
            // UIViewRoot viewRoot = fc.getApplication().getViewHandler().createView(fc, "/home.jspx");
            // fc.setViewRoot(viewRoot);
            // fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String showServiceRequests() {
        try {
            //            LOVDAO lov = new LOVDAO();
            //            lov.reInitializeVariables();
            session.setAttribute("KK", "serviceReq");
            if (navCont != null) {
                GlobalCC.refreshUI(navCont);
            }
            // FacesContext fc = FacesContext.getCurrentInstance();
            // UIViewRoot viewRoot = fc.getApplication().getViewHandler().createView(fc, "/home.jspx");
            // fc.setViewRoot(viewRoot);
            // fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String showServiceDesk() {
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            lov.resetPageButtons();
            session.setAttribute("MM", "serviceDesk");
            if (navCont != null) {
                GlobalCC.refreshUI(navCont);
            }
            // FacesContext fc = FacesContext.getCurrentInstance();
            // UIViewRoot viewRoot = fc.getApplication().getViewHandler().createView(fc, "/home.jspx");
            // fc.setViewRoot(viewRoot);
            // fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String showBankSetups() {
        try {
            //            LOVDAO lov = new LOVDAO();
            //            lov.reInitializeVariables();
            session.setAttribute("KK", "BankSetups");
            if (navCont != null) {
                GlobalCC.refreshUI(navCont);
            }
            // FacesContext fc = FacesContext.getCurrentInstance();
            //  UIViewRoot viewRoot = fc.getApplication().getViewHandler().createView(fc, "/home.jspx");
            //  fc.setViewRoot(viewRoot);
            //  fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }


    public String showMessaging() {
        try {
            //            LOVDAO lov = new LOVDAO();
            //            lov.reInitializeVariables();
            session.setAttribute("KK", "Messaging");
            if (navCont != null) {
                GlobalCC.refreshUI(navCont);
            }
            // FacesContext fc = FacesContext.getCurrentInstance();
            // UIViewRoot viewRoot = fc.getApplication().getViewHandler().createView(fc, "/home.jspx");
            //  fc.setViewRoot(viewRoot);
            //  fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String showCampaignManagement() {
        try {
            //  LOVDAO lov = new LOVDAO();
            // lov.reInitializeVariables();
            session.setAttribute("KK", "Campaignmng");
            if (navCont != null) {
                GlobalCC.refreshUI(navCont);
            }
            // FacesContext fc = FacesContext.getCurrentInstance();
            // UIViewRoot viewRoot = fc.getApplication().getViewHandler().createView(fc, "/home.jspx");
            // fc.setViewRoot(viewRoot);
            // fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String showCampaigns() {
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            lov.resetPageButtons();
            session.setAttribute("MM", "CampaignManagement");
            if (navCont != null) {
                GlobalCC.refreshUI(navCont);
            }
            // FacesContext fc = FacesContext.getCurrentInstance();
            // UIViewRoot viewRoot = fc.getApplication().getViewHandler().createView(fc, "/home.jspx");
            // fc.setViewRoot(viewRoot);
            // fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String showActivityManagement() {
        try {
            // LOVDAO lov = new LOVDAO();
            //   lov.reInitializeVariables();
            session.setAttribute("KK", "ActivityManagement");
            if (navCont != null) {
                GlobalCC.refreshUI(navCont);
            }
            //  FacesContext fc = FacesContext.getCurrentInstance();
            //  UIViewRoot viewRoot = fc.getApplication().getViewHandler().createView(fc, "/home.jspx");
            //  fc.setViewRoot(viewRoot);
            //  fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String showLeadsManagement() {
        try {
            // LOVDAO lov = new LOVDAO();
            // lov.reInitializeVariables();
            session.setAttribute("KK", "LeadManagement");
            if (navCont != null) {
                GlobalCC.refreshUI(navCont);
            }
            //  FacesContext fc = FacesContext.getCurrentInstance();
            //  UIViewRoot viewRoot = fc.getApplication().getViewHandler().createView(fc, "/home.jspx");
            //  fc.setViewRoot(viewRoot);
            //  fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String showServCats() {
        Authorization auth = new Authorization();
        String process = "SRT";
        String processArea = "RCT";
        String processSubArea = "RCTA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/servReqCat.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String serviceDesk() {
        Authorization auth = new Authorization();
        String process = "SRT";
        String processArea = "SRD";
        String processSubArea = "SRDA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            session.setAttribute("filter", null);
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/serviceRequests.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String requestTrack() {
        Authorization auth = new Authorization();
        String process = "SRT";
        String processArea = "RQT";
        String processSubArea = "RQTA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            session.setAttribute("filter", null);
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/requestTrack.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;
    }

    public String showSheduler() {

        Authorization auth = new Authorization();
        String process = "M";
        String processArea = "SCH";
        String processSubArea = "SCHA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }

        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            session.setAttribute("filter", null);
            session.setAttribute("messageType", "EMAIL");
            session.setAttribute("sysCode", "0");
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/scheduler.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;
    }

    public String mailSettings() {
        Authorization auth = new Authorization();
        String process = "M";
        String processArea = "MST";
        String processSubArea = "MSTA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            session.setAttribute("filter", null);
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/mailSettings.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;
    }

    public String smsSettings() {
        Authorization auth = new Authorization();
        String process = "M";
        String processArea = "SST";
        String processSubArea = "SSTA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/smsSettings.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String authorizeMessages() {
        Authorization auth = new Authorization();
        String process = "M";
        String processArea = "ATM";
        String processSubArea = "ATMA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/MobileSmsMessages.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;


    }

    public String knowledgeBase() {
        Authorization auth = new Authorization();
        String process = "SRT";
        String processArea = "KNB";
        String processSubArea = "KNBA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            session.setAttribute("filter", null);
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/knowledgeBase.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;
    }

    public String eacalations() {
        Authorization auth = new Authorization();
        String process = "SRT";
        String processArea = "ESC";
        String processSubArea = "ESCA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            session.setAttribute("filter", null);
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/escalations.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;
    }


    public String newEscalations() {
        Authorization auth = new Authorization();
        String process = "SRT";
        String processArea = "ESC";
        String processSubArea = "ESCA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            session.setAttribute("filter", null);
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/newEscalations.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;
    }

    public String keyWords() {
        Authorization auth = new Authorization();
        String process = "SRT";
        String processArea = "KWD";
        String processSubArea = "KWDA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            session.setAttribute("filter", null);
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/msgParameters.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;
    }

    public String reqHist() {
        Authorization auth = new Authorization();
        String process = "SRT";
        String processArea = "RQH";
        String processSubArea = "RQHA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            session.setAttribute("filter", null);
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/reqHist.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String loadClients() {
        Authorization auth = new Authorization();
        String process = "AMA";
        String processArea = "AMACL";
        String processSubArea = "AMACLA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            session.removeAttribute("myTrigger");
            if (session.getAttribute("DEFAULT_BRANCH_CODE") == null) {
                Index ind = new Index();
                ind.getUserDefaults();
                if (session.getAttribute("DEFAULT_BRANCH_CODE") == null) {
                    GlobalCC.INFORMATIONREPORTING("Please set your 'Default Branch' to Continue");
                    return null;
                }
            }
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/ClientLoading.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;

    }

    public String showEcmReports() {
        Authorization auth = new Authorization();
        String process = "OP";
        String processArea = "OPER";
        String processSubArea = "OPERA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            session.setAttribute("filter", null);
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/ecmRpts.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String showEcmSetups() {
        Authorization auth = new Authorization();
        String process = "OP";
        String processArea = "OPEP";
        String processSubArea = "OPEPA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            session.setAttribute("filter", null);
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/ecmSetup.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String goProducts() {
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();

            FacesContext fc = FacesContext.getCurrentInstance();
            //fc.getExternalContext().redirect("product.jspx");
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/product.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return "failed";
    }

    public String goStatement() {
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().redirect("statement.jspx");
            /*UIViewRoot viewRoot = fc.getApplication().getViewHandler().createView(fc, "/statement.jspx");
         fc.setViewRoot(viewRoot);
         fc.renderResponse();*/


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return "failed";
    }

    public String goClaims() {
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().redirect("myClaims.jspx");
            /*UIViewRoot viewRoot = fc.getApplication().getViewHandler().createView(fc, "/myClaims.jspx");
          fc.setViewRoot(viewRoot);
          fc.renderResponse();*/


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return "failed";
    }

    public String defineReports() {
        Authorization auth = new Authorization();
        String process = "SR";
        String processArea = "SRDF";
        String processSubArea = "SRDFA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();

            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/sysRptDefinition.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return "failed";
    }

    public String showRunningJobs() {
        Authorization auth = new Authorization();
        String process = "M";
        String processArea = "RNJ";
        String processSubArea = "RNJA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return "failed";
        }

        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            session.setAttribute("filter", null);
            session.setAttribute("messageType", "EMAIL");
            session.setAttribute("sysCode", "0");
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/runningJobs.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return "failed";
    }

    public String smsHistory() {
        Authorization auth = new Authorization();
        String process = "M";
        String processArea = "SHS";
        String processSubArea = "SHSA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return "failed";
        }
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();

            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/smsHistory.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return "failed";
    }

    public String emailHistory() {
        Authorization auth = new Authorization();
        String process = "AMA";
        String processArea = "AMAC";
        String processSubArea = "AMCA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return "failed";
        }
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();

            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/emailHistory.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return "failed";
    }

    public String selectDepartments() {
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().redirect("departments.jspx");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return "failed";
    }

    public String selectRatings() {
        Authorization auth = new Authorization();
        String process = "AMA";
        String processArea = "AMART";
        String processSubArea = "AMARTA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return "failed";
        }
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().redirect("ratingOrganizations.jspx");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return "failed";
    }

    public String selectTemplateSetUps() {
        Authorization auth = new Authorization();
        String process = "BS";
        String processArea = "BSTS";
        String processSubArea = "BSTSA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return "failed";
        }
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();


            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/templateSetUps.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return "failed";
    }

    public String securityQuestions() {
        Authorization auth = new Authorization();
        String process = "SA";
        String processArea = "SASQ";
        String processSubArea = "SASQA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();

            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/securityQuestions.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String showUserGroups() {
        
        // Reset all the sessions backing the User Groups
        session.setAttribute("groupTypeUserCode", null);
        session.setAttribute("accountTypeID", null);
        session.setAttribute("accountName", null);    
        
        Authorization auth = new Authorization();
        String process = "SA";
        String processArea = "SAUG";
        String processSubArea = "SAUGA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        System.out.println("UserGroups: " + AccessGranted);
        
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        
        try {
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
          
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/userGroups.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        
        return null;
    }
    
    public String showUserGroupCategories(){        
        
        Authorization auth = new Authorization();
        String process = "SA";
        String processArea = "SAUGC";
        String processSubArea = "SAUGCA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        
        System.out.println("UserGroupCategories: " + AccessGranted);
        
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        
        try{
            LOVDAO lov = new LOVDAO();
            lov.reInitializeVariables();
            
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot = 
                fc.getApplication().getViewHandler().createView(fc, "/userGroupCategories.jspx");
            
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        }catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        
        return null;
    }

    public void setNavCont(RichPanelGroupLayout navCont) {
        this.navCont = navCont;
    }

    public RichPanelGroupLayout getNavCont() {
        return navCont;
    }


    public static void navigate(String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

    }

    public static void logout(String url) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Set set2 = fc.getExternalContext().getRequestMap().keySet();
        Map map2 = fc.getExternalContext().getRequestMap();
        int v = 0;
        Object[] obj2 = set2.toArray();
        String values2 = null;
        while (v < map2.size()) {
            values2 = obj2[v].toString();
            if ((!values2.contains(".")) && (!values2.contains("_")) &&
                (!values2.contains("ADF")) || values2.contains("VIEW_CACHE")) {
                fc.getExternalContext().getRequestMap().remove(values2);
            }
            v++;
        }
        UIViewRoot viewRoot =
            fc.getApplication().getViewHandler().createView(fc, "/" + url);
        fc.setViewRoot(viewRoot);
        fc.renderResponse();

    }


    public String showSysRolesMakerChecker() {
        
        
        Authorization auth = new Authorization();
        String process = "SA";
        String processArea = "SAR";
        String processSubArea = "SARA";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
            GlobalCC.accessDenied();
            return null;
        }
        Rendering renderer = new Rendering();
        boolean newScreen = renderer.isNewRolesScreenSet();
        String rolesScreenLink;
        if (newScreen) {
            rolesScreenLink = "/sysRolesMakerChecker.jspx";
        } else {
            rolesScreenLink = "/sysRolesMakerChecker.jspx";
        }
        try {
            LOVDAO lov = null;
            lov = new LOVDAO();
            lov.reInitializeVariables();
            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                rolesScreenLink);
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;
    }

}
