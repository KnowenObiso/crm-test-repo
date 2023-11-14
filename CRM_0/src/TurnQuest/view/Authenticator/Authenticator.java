package TurnQuest.view.Authenticator;


import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class Authenticator {

    public static boolean addQuotation = false;
    public static boolean viewQuotation = false;
    public static boolean editQuotation = false;
    public static boolean addPolicy = false;
    public static boolean viewPolicy = false;
    public static boolean editPolicy = false;
    public static boolean addClaim = false;
    public static boolean viewClaim = false;
    public static boolean modifyClaim = false;
    public static boolean quotations = false;
    public static boolean policies = false;
    public static boolean claims = false;
    public static boolean account = false;

    private static String Language;
    private static String OrgType;

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public Authenticator() {
    }

    public String Authenticate() {
        if (session.getAttribute("UserCode") == null) {
            String message = "Not logged In";
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(message));

            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.jspx");
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null,
                                                             new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                              e.getMessage(),
                                                                              e.getMessage()));
            }
        } else {

            HttpServletResponse response =
                (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.addHeader("Pragma", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            response.addHeader("Cache-Control", "must-revalidate");
            response.addHeader("Expires", "Mon, 8 Aug 2006 10:00:00 GMT");
        }
        return null;
    }

    public String ResetLanguage() {
        // Language =
        //     "TQGIS.view.Resources." + (String)session.getAttribute("languageBundle");
        OrgType = (String)session.getAttribute("brokerInsurer");

        return null;
    }


    public String Logout() {

        HttpServletRequest request =
            (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();


        ExternalContext ectx =
            FacesContext.getCurrentInstance().getExternalContext();
        HttpServletResponse response = (HttpServletResponse)ectx.getResponse();
        HttpSession session = (HttpSession)ectx.getSession(false);
        session.invalidate();
        session = request.getSession(true);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        //CurrentUser.UserCode = null;

        session.setAttribute("UserCode", null);

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.jspx");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }
        return null;
    }

    public void setLanguage(String Language) {
        Authenticator.Language = Language;
    }

    public String getLanguage() {
        return Language;
    }

    public void setOrgType(String OrgType) {
        Authenticator.OrgType = OrgType;
    }

    public String getOrgType() {
        return OrgType;
    }
}
