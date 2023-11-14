package TurnQuest.view.Accounts;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.Util;

import java.math.BigDecimal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class validationManip {
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    HttpServletRequest request =
        (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

    public validationManip() {
    }


    public void validatePinNumber(FacesContext facesContext,
                                  UIComponent uIComponent, Object object) {
        String pin = GlobalCC.checkNullValues(object);
        String type =
            GlobalCC.checkNullValues(session.getAttribute("typeVAl"));
        String checkType =
            GlobalCC.getSysParamValue("CLIENT_VALIDATE_PIN_TYPE");
        String url = request.getRequestURI();

        System.out.println("checkType=" + checkType + ";type=" + type +
                           ";pin=" + pin + ";url=" + url);
        if (pin != null) {
            if (!pin.toUpperCase().matches("^[A-Z]{1}[0-9]{9}[A-Z]{1}$")) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                              "Invalid Pin No. Should start with a letter, then a 9 digits number , then end with a letter!",
                                                              null));
            }
            if (("Y".equals(checkType) && (type != null) &&
                 (url.endsWith("clients.jspx")))) {
                if (GlobalCC.contains(new String[] { "C", "P", "G" }, type) &&
                    !pin.toUpperCase().startsWith("P")) {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                  "Corporate Pin should start with letter 'P'!",
                                                                  null));
                }
                if (GlobalCC.contains(new String[] { "S", "I" ,"F"}, type) &&
                    !pin.toUpperCase().startsWith("A")) {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                  "Personal Pin should start with letter 'A'!",
                                                                  null));
                }
            }
        }
    }

    public void validateBankAccNo(FacesContext facesContext,
                                  UIComponent uIComponent, Object object) {
        String validate;
        String name = GlobalCC.checkNullValues(object);
        if (name != null) {
            if (!name.toUpperCase().matches("^[0-9]{6,20}$")) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                              "Invalid Bank Account No. Should be a 6-20 digits number!",
                                                              null));
            }
        }
    }

    public void validatePassPort(FacesContext facesContext,
                                 UIComponent uIComponent, Object object) {
        String validate;
        String name = GlobalCC.checkNullValues(object);
        if (name != null) {
            if (!name.toUpperCase().matches("^[A-Z]{1}[0-9]{4,9}[A-Z]{0,1}$")) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                              "Invalid Passport No. Should start with a letter, then a number with digits between 4 and 8, then optionaly end with a letter!",
                                                              null));
            }
        }
    }

    public void validateSmsPrefix(FacesContext facesContext,
                                  UIComponent uIComponent, Object object) {
        String validate;
        String name = GlobalCC.checkNullValues(object);
        if (name != null) {

            if (!name.toUpperCase().matches("^[0-9]{4}$")) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                              "Invalid Sms Prefix No. Should be a 4 digit number!",
                                                              null));
            }
        }
    }

    public void validateSmsSuffix(FacesContext facesContext,
                                  UIComponent uIComponent, Object object) {
        String validate;
        String name = GlobalCC.checkNullValues(object);
        if (name != null) {
            if (!name.matches("^[0-9]{6}$")) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                              "Invalid Sms Prefix No. Should be a 6 digit number!",
                                                              null));
            }
        }
    }

    public void validateId(FacesContext facesContext, UIComponent uIComponent,
                           Object object) {
        String site =
            GlobalCC.checkNullValues(session.getAttribute("DEFAULT_SITE"));

        if (site != null) {
            if (site.equalsIgnoreCase("ZAMBIA")) {

            } else {
                String validate;
                String idNumberFormat;
                String clientType = null;
                Util util = new Util();
                validate = util.getPinNumberValidation();
                idNumberFormat = util.getIdNumberFormat();
                if (session.getAttribute("clientTypeVAl") != null) {
                    clientType = (String)session.getAttribute("clientTypeVAl");
                } else {
                    clientType = "C";
                }
                if (validate.equals("Y") && clientType.equals("I")) {
                    String name = object.toString();
                    if (object != null) {
                        int pinSz;
                        pinSz = object.toString().length();
                        System.out.print(object.toString().length());
                        boolean matchChar = name.contains(" ");
                        Pattern pattern = null;
                        if (idNumberFormat == null) {
                            pattern =
                                    Pattern.compile("[/,:<>!~@#$%^&()+=?()\"|!\\[#$-]");
                        } else {
                            pattern = Pattern.compile(idNumberFormat);
                        }
                        Matcher patternMatcher =
                            pattern.matcher(name.subSequence(0,
                                                             name.length()));

                        boolean specChar = patternMatcher.find();
                        try {
                            new BigDecimal(name);
                        } catch (Exception e) {
                            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          "Please check format.All characters must be numbers",
                                                                          null));
                        }
                        if (pinSz != 8) {
                            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          "Please check format.Id Must be 8 characters",
                                                                          null));
                        }

                        //                boolean val = name.contains("[A-Z]");
                        //                if (val == true) {
                        //                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        //                                                                  "Please check format.All characters must be numbers",
                        //                                                                  null));
                        //
                        //                }
                        if (matchChar == true) {
                            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          "Please check format.Spaces not Allowed",
                                                                          null));
                        }
                        if (specChar == true) {
                            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          "Please check format.Special charactes not Allowed",
                                                                          null));
                        } else {
                            return;
                        }

                    } else {

                    }
                }
            }
        }
        String validate;
        String idNumberFormat;
        String clientType = null;
        Util util = new Util();
        validate = util.getPinNumberValidation();
        idNumberFormat = util.getIdNumberFormat();
        if (session.getAttribute("clientTypeVAl") != null) {
            clientType = (String)session.getAttribute("clientTypeVAl");
        } else {
            clientType = "C";
        }
        if (validate.equals("Y") && clientType.equals("I")) {
            String name = object.toString();
            if (object != null) {
                int pinSz;
                pinSz = object.toString().length();
                System.out.print(object.toString().length());
                boolean matchChar = name.contains(" ");
                Pattern pattern = null;
                if (idNumberFormat == null) {
                    pattern =
                            Pattern.compile("[/,:<>!~@#$%^&()+=?()\"|!\\[#$-]");
                } else {
                    pattern = Pattern.compile(idNumberFormat);
                }
                Matcher patternMatcher =
                    pattern.matcher(name.subSequence(0, name.length()));

                boolean specChar = patternMatcher.find();
                try {
                    new BigDecimal(name);
                } catch (Exception e) {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                  "Please check format.All characters must be numbers",
                                                                  null));
                }
                if (pinSz != 8) {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                  "Please check format.Id Must be 8 characters",
                                                                  null));
                }

                //                boolean val = name.contains("[A-Z]");
                //                if (val == true) {
                //                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                //                                                                  "Please check format.All characters must be numbers",
                //                                                                  null));
                //
                //                }
                if (matchChar == true) {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                  "Please check format.Spaces not Allowed",
                                                                  null));
                }
                if (specChar == true) {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                  "Please check format.Special charactes not Allowed",
                                                                  null));
                } else {
                    return;
                }

            } else {

            }
        }
    }


    public String getPassNoFormat() {
        String val = (String)GlobalCC.getSysParamValue("PASSPORT_NO_FORMAT");
        if (val == null) {
            val = "^[A-Z]{1}[0-9]{4,8}[A-Z]{0,1}$";
        }
        return val;
    }


    public void validateClientPassPort(FacesContext facesContext,
                                       UIComponent uIComponent,
                                       Object object) {
        /*    String type = GlobalCC.checkNullValues(session.getAttribute("typeVAl"));
        String val = GlobalCC.checkNullValues(object);
        String validate = GlobalCC.getSysParamValue("IGNORE_FOREIGN_CLIENT_PASSPORT");
         String validatePassPort    =     GlobalCC.getSysParamValue("PASSPORT_VALIDATION");


  if("Y".equalsIgnoreCase(validatePassPort)){
        if( "F".equals(type) && "Y".equals(validate) ) {//don't validate on foreign clients
            return;
        }
        if (val != null ) {

                String passFormat = getPassNoFormat();
                String msg = (String) GlobalCC.getSysParamValue("PASSPORT_NO_ERROR_MSG");
                    if (!val.toUpperCase().matches(passFormat)) {
                        throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,msg, null));

                                      }

                        }

              }
    return null;
    */
    }
}
