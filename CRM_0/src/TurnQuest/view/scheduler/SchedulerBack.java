package TurnQuest.view.scheduler;


import TurnQuest.view.Alerts.SystemAlerts;
import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.sql.CallableStatement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanRadio;
import oracle.adf.view.rich.component.rich.input.RichSelectManyCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleConnection;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

import org.quartz.Scheduler;
import org.quartz.TriggerKey;


//import org.quartz.TriggerKey;


public class SchedulerBack {


    private RichSelectOneChoice repeatSelect;
    private RichPanelBox dailyPanel;
    private RichPanelBox weeklyPanel;
    private RichPanelBox monthlyPanel;
    private RichPanelBox yearlyPanel;
    private RichSelectBooleanRadio rdEveryDay;
    private RichSelectBooleanRadio rdEveryWeekday;
    private RichSelectBooleanRadio rdEveryDayOption;
    private RichInputNumberSpinbox txtEveryDays;
    private RichSelectBooleanRadio rdEveryWeek;
    private RichSelectOneChoice rdWeekDaily;
    private RichSelectBooleanRadio rdWeekNo;
    private RichInputNumberSpinbox txtWeekNo;
    private RichSelectManyCheckbox chkWeek;
    private RichSelectBooleanRadio rdMonth;
    private RichInputNumberSpinbox txtDay;
    private RichInputNumberSpinbox txtEveryMonth;
    private RichSelectBooleanRadio rdMonth2;
    private RichSelectOneChoice txtMonthLevel;
    private RichSelectOneChoice txtMonthDay;
    private RichInputNumberSpinbox txtmonthEvery;
    private RichSelectBooleanRadio rdYearly;
    private RichSelectOneChoice rdYearMonth;
    private RichInputNumberSpinbox txtYearlyNoMonths;
    private RichSelectBooleanRadio rdYearly2;
    private RichSelectOneChoice rdYearLevel;
    private RichSelectOneChoice rdYearDay;
    private RichSelectOneChoice rdYearlyMonth;
    private RichInputDate txtStartTime;
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private RichTable userSelected;
    private RichInputText txtAssignee;
    private RichTable notifiedLov;
    private RichInputText txtNotifyFail;
    private RichTable notifySucceed;
    private RichInputText txtNotSucc;
    private RichInputText txtJobName;
    private RichInputText txtJobDesc;
    private RichSelectOneChoice txtJobType;
    private RichInputText txtJobTemplate;
    private RichInputText txtFailTemplate;
    private RichInputText txtSuccessTemplate;
    private RichTable schedulerTable;
    private RichOutputLabel assigneeLbl;
    private HtmlPanelGrid assigneePnl;
    private RichOutputLabel notifyUsrLbl;
    private RichOutputLabel notUsrTmpLbl;
    private RichOutputLabel occurence;
    private RichInputText txtRecurrence;
    private RichTable messageTemp;
    private RichCommandButton assigneeBtn;
    private RichOutputLabel execObjLbl;
    private RichCommandButton execBtn;
    private RichSelectOneChoice status;
    private RichTable tblSystems;
    private RichInputNumberSpinbox qtCode;
    private RichTable execTab;
    private RichSelectOneChoice threshType;
    private RichInputNumberSpinbox threshValue;
    private RichInputDate txtExecutionTime;
    private RichTable schedulerTbl;

    public void changeRepeatType(ValueChangeEvent valueChangeEvent) {
        System.out.println("Ok");
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            System.out.println(valueChangeEvent.getNewValue());
            occurence.setVisible(true);
            if (valueChangeEvent.getNewValue() != null &&
                valueChangeEvent.getNewValue().toString().equalsIgnoreCase("N")) {
                occurence.setVisible(false);
                dailyPanel.setVisible(false);
                weeklyPanel.setVisible(false);
                monthlyPanel.setVisible(false);
                yearlyPanel.setVisible(false);
            } else if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("D")) {
                occurence.setVisible(true);
                dailyPanel.setVisible(true);
                weeklyPanel.setVisible(false);
                monthlyPanel.setVisible(false);
                yearlyPanel.setVisible(false);
            } else if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("W")) {
                occurence.setVisible(true);
                dailyPanel.setVisible(false);
                weeklyPanel.setVisible(true);
                monthlyPanel.setVisible(false);
                yearlyPanel.setVisible(false);
            } else if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("M")) {
                occurence.setVisible(true);
                dailyPanel.setVisible(false);
                weeklyPanel.setVisible(false);
                monthlyPanel.setVisible(true);
                yearlyPanel.setVisible(false);
            } else if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("Y")) {
                occurence.setVisible(true);
                dailyPanel.setVisible(false);
                weeklyPanel.setVisible(false);
                monthlyPanel.setVisible(false);
                yearlyPanel.setVisible(true);
            }
            GlobalCC.refreshUI(dailyPanel);
            GlobalCC.refreshUI(weeklyPanel);
            GlobalCC.refreshUI(monthlyPanel);
            GlobalCC.refreshUI(yearlyPanel);
            GlobalCC.refreshUI(occurence);
        }

    }

    public void setRepeatSelect(RichSelectOneChoice repeatSelect) {
        this.repeatSelect = repeatSelect;
    }

    public RichSelectOneChoice getRepeatSelect() {
        return repeatSelect;
    }

    public void setDailyPanel(RichPanelBox dailyPanel) {
        this.dailyPanel = dailyPanel;
    }

    public RichPanelBox getDailyPanel() {
        return dailyPanel;
    }

    public void setWeeklyPanel(RichPanelBox weeklyPanel) {
        this.weeklyPanel = weeklyPanel;
    }

    public RichPanelBox getWeeklyPanel() {
        return weeklyPanel;
    }

    public void setMonthlyPanel(RichPanelBox monthlyPanel) {
        this.monthlyPanel = monthlyPanel;
    }

    public RichPanelBox getMonthlyPanel() {
        return monthlyPanel;
    }

    public void setYearlyPanel(RichPanelBox yearlyPanel) {
        this.yearlyPanel = yearlyPanel;
    }

    public RichPanelBox getYearlyPanel() {
        return yearlyPanel;
    }

    private String getType(String type) {
        if (type.equals("1")) {
            return "First";
        } else if (type.equals("2")) {
            return "Second";
        } else if (type.equals("3")) {
            return "Third";
        } else if (type.equals("4")) {
            return "Fourth";
        } else if (type.equals("5")) {
            return "Fifth";
        } else
            return "none";
    }

    private String getWeek(String type) {
        if (type.equals("1")) {
            return "Monday";
        } else if (type.equals("2")) {
            return "Tuesday";
        } else if (type.equals("3")) {
            return "Wednesday";
        } else if (type.equals("4")) {
            return "Thursday";
        } else if (type.equals("5")) {
            return "Friday";
        } else if (type.equals("6")) {
            return "Saturday";
        } else if (type.equals("7")) {
            return "Sunday";
        } else
            return "none";
    }


    public String getMonth(String type) {
        if (type.equals("1")) {
            return "January";
        } else if (type.equals("2")) {
            return "February";
        } else if (type.equals("3")) {
            return "March";
        } else if (type.equals("4")) {
            return "April";
        } else if (type.equals("5")) {
            return "May";
        } else if (type.equals("6")) {
            return "June";
        } else if (type.equals("7")) {
            return "July";
        } else if (type.equals("8")) {
            return "August";
        } else if (type.equals("9")) {
            return "September";
        } else if (type.equals("10")) {
            return "October";
        } else if (type.equals("11")) {
            return "November";
        } else if (type.equals("12")) {
            return "December";
        } else
            return "None";
    }

    public void setRdEveryDay(RichSelectBooleanRadio rdEveryDay) {
        this.rdEveryDay = rdEveryDay;
    }

    public RichSelectBooleanRadio getRdEveryDay() {
        return rdEveryDay;
    }

    public void setRdEveryWeekday(RichSelectBooleanRadio rdEveryWeekday) {
        this.rdEveryWeekday = rdEveryWeekday;
    }

    public RichSelectBooleanRadio getRdEveryWeekday() {
        return rdEveryWeekday;
    }

    public void setRdEveryDayOption(RichSelectBooleanRadio rdEveryDayOption) {
        this.rdEveryDayOption = rdEveryDayOption;
    }

    public RichSelectBooleanRadio getRdEveryDayOption() {
        return rdEveryDayOption;
    }

    public void setTxtEveryDays(RichInputNumberSpinbox txtEveryDays) {
        this.txtEveryDays = txtEveryDays;
    }

    public RichInputNumberSpinbox getTxtEveryDays() {
        return txtEveryDays;
    }

    public void setRdEveryWeek(RichSelectBooleanRadio rdEveryWeek) {
        this.rdEveryWeek = rdEveryWeek;
    }

    public RichSelectBooleanRadio getRdEveryWeek() {
        return rdEveryWeek;
    }

    public void setRdWeekDaily(RichSelectOneChoice rdWeekDaily) {
        this.rdWeekDaily = rdWeekDaily;
    }

    public RichSelectOneChoice getRdWeekDaily() {
        return rdWeekDaily;
    }

    public void setRdWeekNo(RichSelectBooleanRadio rdWeekNo) {
        this.rdWeekNo = rdWeekNo;
    }

    public RichSelectBooleanRadio getRdWeekNo() {
        return rdWeekNo;
    }

    public void setTxtWeekNo(RichInputNumberSpinbox txtWeekNo) {
        this.txtWeekNo = txtWeekNo;
    }

    public RichInputNumberSpinbox getTxtWeekNo() {
        return txtWeekNo;
    }

    public void setChkWeek(RichSelectManyCheckbox chkWeek) {
        this.chkWeek = chkWeek;
    }

    public RichSelectManyCheckbox getChkWeek() {
        return chkWeek;
    }

    public void setRdMonth(RichSelectBooleanRadio rdMonth) {
        this.rdMonth = rdMonth;
    }

    public RichSelectBooleanRadio getRdMonth() {
        return rdMonth;
    }

    public void setTxtDay(RichInputNumberSpinbox txtDay) {
        this.txtDay = txtDay;
    }

    public RichInputNumberSpinbox getTxtDay() {
        return txtDay;
    }

    public void setTxtEveryMonth(RichInputNumberSpinbox txtEveryMonth) {
        this.txtEveryMonth = txtEveryMonth;
    }

    public RichInputNumberSpinbox getTxtEveryMonth() {
        return txtEveryMonth;
    }

    public void setRdMonth2(RichSelectBooleanRadio rdMonth2) {
        this.rdMonth2 = rdMonth2;
    }

    public RichSelectBooleanRadio getRdMonth2() {
        return rdMonth2;
    }

    public void setTxtMonthLevel(RichSelectOneChoice txtMonthLevel) {
        this.txtMonthLevel = txtMonthLevel;
    }

    public RichSelectOneChoice getTxtMonthLevel() {
        return txtMonthLevel;
    }

    public void setTxtMonthDay(RichSelectOneChoice txtMonthDay) {
        this.txtMonthDay = txtMonthDay;
    }

    public RichSelectOneChoice getTxtMonthDay() {
        return txtMonthDay;
    }

    public void setTxtmonthEvery(RichInputNumberSpinbox txtmonthEvery) {
        this.txtmonthEvery = txtmonthEvery;
    }

    public RichInputNumberSpinbox getTxtmonthEvery() {
        return txtmonthEvery;
    }

    public void setRdYearly(RichSelectBooleanRadio rdYearly) {
        this.rdYearly = rdYearly;
    }

    public RichSelectBooleanRadio getRdYearly() {
        return rdYearly;
    }

    public void setRdYearMonth(RichSelectOneChoice rdYearMonth) {
        this.rdYearMonth = rdYearMonth;
    }

    public RichSelectOneChoice getRdYearMonth() {
        return rdYearMonth;
    }

    public void setTxtYearlyNoMonths(RichInputNumberSpinbox txtYearlyNoMonths) {
        this.txtYearlyNoMonths = txtYearlyNoMonths;
    }

    public RichInputNumberSpinbox getTxtYearlyNoMonths() {
        return txtYearlyNoMonths;
    }

    public void setRdYearly2(RichSelectBooleanRadio rdYearly2) {
        this.rdYearly2 = rdYearly2;
    }

    public RichSelectBooleanRadio getRdYearly2() {
        return rdYearly2;
    }

    public void setRdYearLevel(RichSelectOneChoice rdYearLevel) {
        this.rdYearLevel = rdYearLevel;
    }

    public RichSelectOneChoice getRdYearLevel() {
        return rdYearLevel;
    }

    public void setRdYearDay(RichSelectOneChoice rdYearDay) {
        this.rdYearDay = rdYearDay;
    }

    public RichSelectOneChoice getRdYearDay() {
        return rdYearDay;
    }

    public void setRdYearlyMonth(RichSelectOneChoice rdYearlyMonth) {
        this.rdYearlyMonth = rdYearlyMonth;
    }

    public RichSelectOneChoice getRdYearlyMonth() {
        return rdYearlyMonth;
    }

    public void setTxtStartTime(RichInputDate txtStartTime) {
        this.txtStartTime = txtStartTime;
    }

    public RichInputDate getTxtStartTime() {
        return txtStartTime;
    }


    public String assigneeSelected() {
        Object key2 = userSelected.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        txtAssignee.setValue(nodeBinding.getAttribute("username"));
        txtAssignee.setLabel(nodeBinding.getAttribute("userCode").toString());
        GlobalCC.refreshUI(txtAssignee);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:p3" +
                             "').hide(hints);");
        return null;
    }

    public void setUserSelected(RichTable userSelected) {
        this.userSelected = userSelected;
    }

    public RichTable getUserSelected() {
        return userSelected;
    }

    public void setTxtAssignee(RichInputText txtAssignee) {
        this.txtAssignee = txtAssignee;
    }

    public RichInputText getTxtAssignee() {
        return txtAssignee;
    }

    public String notifiedSelected() {
        Object key2 = notifiedLov.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        txtNotifyFail.setLabel(nodeBinding.getAttribute("userCode").toString());
        txtNotifyFail.setValue(nodeBinding.getAttribute("username"));
        GlobalCC.refreshUI(txtNotifyFail);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:popup1" +
                             "').hide(hints);");
        return null;
    }

    public void setNotifiedLov(RichTable notifiedLov) {
        this.notifiedLov = notifiedLov;
    }

    public RichTable getNotifiedLov() {
        return notifiedLov;
    }

    public void setTxtNotifyFail(RichInputText txtNotifyFail) {
        this.txtNotifyFail = txtNotifyFail;
    }

    public RichInputText getTxtNotifyFail() {
        return txtNotifyFail;
    }

    public void setNotifySucceed(RichTable notifySucceed) {
        this.notifySucceed = notifySucceed;
    }

    public RichTable getNotifySucceed() {
        return notifySucceed;
    }

    public String notifiedSuccSelected() {
        Object key2 = notifySucceed.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        txtNotSucc.setLabel(nodeBinding.getAttribute("userCode").toString());
        txtNotSucc.setValue(nodeBinding.getAttribute("username"));
        GlobalCC.refreshUI(txtNotSucc);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:popup2" +
                             "').hide(hints);");
        return null;
    }

    public void setTxtNotSucc(RichInputText txtNotSucc) {
        this.txtNotSucc = txtNotSucc;
    }

    public RichInputText getTxtNotSucc() {
        return txtNotSucc;
    }

    public void setTxtJobName(RichInputText txtJobName) {
        this.txtJobName = txtJobName;
    }

    public RichInputText getTxtJobName() {
        return txtJobName;
    }

    public void setTxtJobDesc(RichInputText txtJobDesc) {
        this.txtJobDesc = txtJobDesc;
    }

    public RichInputText getTxtJobDesc() {
        return txtJobDesc;
    }

    public void setTxtJobType(RichSelectOneChoice txtJobType) {
        this.txtJobType = txtJobType;
    }

    public RichSelectOneChoice getTxtJobType() {
        return txtJobType;
    }

    public void setTxtJobTemplate(RichInputText txtJobTemplate) {
        this.txtJobTemplate = txtJobTemplate;
    }

    public RichInputText getTxtJobTemplate() {
        return txtJobTemplate;
    }


    public void setTxtFailTemplate(RichInputText txtFailTemplate) {
        this.txtFailTemplate = txtFailTemplate;
    }

    public RichInputText getTxtFailTemplate() {
        return txtFailTemplate;
    }

    public void setTxtSuccessTemplate(RichInputText txtSuccessTemplate) {
        this.txtSuccessTemplate = txtSuccessTemplate;
    }

    public RichInputText getTxtSuccessTemplate() {
        return txtSuccessTemplate;
    }

    public String saveAction() {
        OracleConnection conn = null;
        try {
            String success = null;
            String qtCodeVal = GlobalCC.checkNullValues(qtCode.getValue());
            String jobName = GlobalCC.checkNullValues(txtJobName.getValue());
            String assignee = GlobalCC.checkNullValues(txtAssignee.getValue());
            String execObj =
                GlobalCC.checkNullValues(txtJobTemplate.getValue());
            String jobType = GlobalCC.checkNullValues(txtJobType.getValue());
            String jobDesc = GlobalCC.checkNullValues(txtJobDesc.getValue());
            String notFailUser =
                GlobalCC.checkNullValues(txtNotifyFail.getValue());
            String notSuccUser =
                GlobalCC.checkNullValues(txtNotSucc.getValue());
            String notFailTemp =
                GlobalCC.checkNullValues(txtFailTemplate.getValue());
            String notSuccTemp =
                GlobalCC.checkNullValues(txtSuccessTemplate.getValue());
            String statusVal = GlobalCC.checkNullValues(status.getValue());
            String threshTypeVal =
                GlobalCC.checkNullValues(threshType.getValue());
            String threshValVal =
                GlobalCC.checkNullValues(threshValue.getValue());
            String repeat = null;
            if (jobName == null) {
                GlobalCC.errorValueNotEntered("Missing Job Name");
                return null;
            }
            if (jobDesc == null) {
                GlobalCC.errorValueNotEntered("Missing Job Desc");
                return null;
            }
            if (jobType == null) {
                GlobalCC.errorValueNotEntered("Missing Job Type");
                return null;
            }
            if (GlobalCC.checkNullValues(txtStartTime.getValue()) == null) {
                GlobalCC.errorValueNotEntered("Missing Execution Time");
                return null;
            }
            if (jobType.equalsIgnoreCase("A")) {
                if (assignee == null) {
                    GlobalCC.errorValueNotEntered("Missing Assignee");
                    return null;
                }
                if (execObj == null) {
                    GlobalCC.errorValueNotEntered("Missing Message Template");
                    return null;
                }
                execObj = txtJobTemplate.getLabel();
                assignee = txtAssignee.getLabel();
            } else if (jobType.equalsIgnoreCase("R")) {
                if (assignee == null) {
                    GlobalCC.errorValueNotEntered("Send To:");
                    return null;
                }
                if (execObj == null) {
                    GlobalCC.errorValueNotEntered("Missing Object");
                    return null;
                }
                execObj = txtJobTemplate.getLabel();
                assignee = txtAssignee.getLabel();
            } else if (jobType.equalsIgnoreCase("S")) {
                if (assignee == null) {
                    GlobalCC.errorValueNotEntered("Assignee:");
                    return null;
                }
                if (execObj == null) {
                    GlobalCC.errorValueNotEntered("Missing Object");
                    return null;
                }
                execObj = txtJobTemplate.getLabel();
                assignee = txtAssignee.getLabel();
            }
            if (statusVal == null) {
                GlobalCC.errorValueNotEntered("Error Missing Status Value");
                return null;
            }
            if (notFailUser != null) {
                notFailUser = txtNotifyFail.getLabel();
                System.out.println(notFailUser);
                if (notFailTemp == null) {
                    GlobalCC.errorValueNotEntered("Error Missing Failure Message Template");
                    return null;
                }
                notFailTemp = txtFailTemplate.getLabel();
            }

            if (notSuccUser != null) {
                notSuccUser = txtNotSucc.getLabel();
                if (notSuccTemp == null) {
                    GlobalCC.errorValueNotEntered("Error Missing Success Message Template");
                    return null;
                }
                notSuccTemp = txtSuccessTemplate.getLabel();
            }
            /*String notFailUser = GlobalCC.checkNullValues(txtNotifyFail.getValue());
        String notSuccUser = GlobalCC.checkNullValues(txtNotSucc.getValue());
        String notFailTemp = GlobalCC.checkNullValues(txtFailTemplate.getValue());
        String notSuccTemp = GlobalCC.checkNullValues(txtSuccessTemplate.getValue());*/
            if (repeatSelect.getValue() == null) {
                GlobalCC.errorValueNotEntered("Select Repeat Type");
                return null;
            } else {
                repeat = GlobalCC.checkNullValues(repeatSelect.getValue());
            }

            if (repeatSelect.getValue().equals("D")) {
                if (rdEveryDayOption.getValue() == null) {

                } else if (rdEveryDayOption.isSelected()) {
                    if (txtEveryDays.getValue() == null) {
                        GlobalCC.errorValueNotEntered("Enter No. of Days");
                        return null;
                    }
                }
            }
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(GlobalCC.extractDate(txtStartTime));
            int hour = cal.get(cal.HOUR_OF_DAY);
            int min = cal.get(cal.MINUTE);
            int year = cal.get(cal.YEAR);
            int month = cal.get(cal.MONTH) + 1;
            int day = cal.get(cal.DAY_OF_MONTH);
            int sec = cal.get(cal.SECOND);
            String cronjob = "";
            if (repeatSelect.getValue().equals("N")) {
                cronjob =
                        sec + " " + min + " " + hour + " " + day + " " + month +" ? " + " " + year;
                txtRecurrence.setValue("None");
            } else if (repeatSelect.getValue().equals("D")) {
                if (rdEveryDay.getValue() == null) {

                } else if (rdEveryDay.isSelected()) {
                    cronjob =sec + " " + min + " " + hour + " " + "*" + " " + "*" +" ? ";
                    txtRecurrence.setValue("Every Day");
                }
                if (rdEveryWeekday.getValue() == null) {

                } else if (rdEveryWeekday.isSelected()) {
                    cronjob =sec + " " + min + " " + hour + " " + "?" + " " + "*" +" MON-FRI ";
                    txtRecurrence.setValue("Every Week Day");
                }

                if (rdEveryDayOption.getValue() == null) {

                } else if (rdEveryDayOption.isSelected()) {
                    cronjob =
                            sec + " " + min + " " + hour + " " + "*/" + txtEveryDays.getValue() +" " + "*" + " ? ";
                    txtRecurrence.setValue("Every " + txtEveryDays.getValue() +
                                           " Days");
                }

            } else if (repeatSelect.getValue().equals("W")) {
                if (rdEveryWeek.getValue() == null) {

                } else if (rdEveryWeek.isSelected()) {
                    cronjob =sec + " " + min + " " + hour + " " + "?" + " " + "*" +" "+rdWeekDaily.getValue();
                    txtRecurrence.setValue("Every " + getWeek(rdWeekDaily.getValue().toString()));
                }

                if (rdWeekNo.getValue() == null) {

                } else if (rdWeekNo.isSelected()) {
                    if (txtWeekNo.getValue() == null) {
                        GlobalCC.errorValueNotEntered("Select Week");
                        return null;
                    }
                    ArrayList al = (ArrayList)chkWeek.getValue();
                    if (al.size() == 0) {
                        GlobalCC.errorValueNotEntered("Select Day(s)");
                        return null;
                    }
                    StringBuffer buf = new StringBuffer();
                    StringBuffer days = new StringBuffer();
                    for (Iterator it = al.iterator(); it.hasNext(); ) {
                        buf.append(" ");
                        String val = it.next().toString();
                        days.append(getWeek(val) + " ");
                        buf.append(val + "/" + txtWeekNo.getValue());
                        buf.append(",");
                    }
                    String val = buf.toString();
                    System.out.println(val);
                    val = val.substring(0, val.length() - 1);
                    cronjob =sec + " " + min + " " + hour + " " + "?" + " " + "*" +val;

                    txtRecurrence.setValue("Every " + txtWeekNo.getValue() +" Weeks on " + days.toString());
                }

            } else if (repeatSelect.getValue().equals("M")) {
                if (rdMonth.getValue() == null) {

                } else if (rdMonth.isSelected()) {

                    if (txtDay.getValue() == null) {
                        GlobalCC.errorValueNotEntered("Enter Day");
                        return null;
                    }
                    if (txtEveryMonth.getValue() == null) {
                        GlobalCC.errorValueNotEntered("Enter Month");
                        return null;
                    }
                    cronjob =
                            sec + " " + min + " " + hour + " " + txtDay.getValue() +
                            " " + "*/" + txtEveryMonth.getValue() + " ? ";
                    txtRecurrence.setValue("On Day " + txtDay.getValue() +
                                           " of every  " +
                                           txtEveryMonth.getValue() +
                                           " Months");

                }
                if (rdMonth2.getValue() == null) {

                } else if (rdMonth2.isSelected()) {
                    if (txtMonthLevel.getValue() == null) {
                        GlobalCC.errorValueNotEntered("Select Level");
                        return null;
                    }
                    if (txtMonthDay.getValue() == null) {
                        GlobalCC.errorValueNotEntered("Select Day");
                        return null;
                    }
                    if (txtmonthEvery.getValue() == null) {
                        GlobalCC.errorValueNotEntered("Enter Month");
                        return null;
                    }
                    txtRecurrence.setValue(getType(txtMonthLevel.getValue().toString()) +
                                           " " +
                                           getWeek(txtMonthDay.getValue().toString()) +
                                           " of every " +
                                           txtmonthEvery.getValue() +
                                           " Months");
                    if (txtMonthLevel.getValue().equals("1")) {
                        cronjob =
                                sec + " " + min + " " + hour + " " + "1-7" + " " +
                                "*/" + txtmonthEvery.getValue() + " " +
                                txtMonthDay.getValue();

                    } else if (txtMonthLevel.getValue().equals("2")) {
                        cronjob =
                                sec + " " + min + " " + hour + " " + "8-14" + " " +
                                "*/" + txtmonthEvery.getValue() + " " +
                                txtMonthDay.getValue();
                    } else if (txtMonthLevel.getValue().equals("3")) {
                        cronjob =
                                sec + " " + min + " " + hour + " " + "15-21" +
                                " " + "*/" + txtmonthEvery.getValue() + " " +
                                txtMonthDay.getValue();
                    } else if (txtMonthLevel.getValue().equals("4")) {
                        cronjob =
                                sec + " " + min + " " + hour + " " + "22-28" +
                                " " + "*/" + txtmonthEvery.getValue() + " " +
                                txtMonthDay.getValue();
                    } else if (txtMonthLevel.getValue().equals("5")) {
                        cronjob =
                                sec + " " + min + " " + hour + " " + "29-31" +
                                " " + "*/" + txtmonthEvery.getValue() + " " +
                                txtMonthDay.getValue();
                    }
                }
            } else if (repeatSelect.getValue().equals("Y")) {
                if (rdYearly.getValue() == null) {

                } else if (rdYearly.isSelected()) {
                    if (rdYearMonth.getValue() == null) {
                        GlobalCC.errorValueNotEntered("Select Month");
                        return null;
                    }
                    if (txtYearlyNoMonths.getValue() == null) {
                        GlobalCC.errorValueNotEntered("Enter Day");
                        return null;
                    }
                    cronjob =
                            sec + " " + min + " " + hour + " " + txtYearlyNoMonths.getValue() +
                            " " + rdYearMonth.getValue() + " ? ";
                    txtRecurrence.setValue("Every Year on " +
                                           getMonth(rdYearMonth.getValue().toString()) +
                                           ", " +
                                           txtYearlyNoMonths.getValue());
                }
                if (rdYearly2.getValue() == null) {

                } else if (rdYearly2.isSelected()) {

                    if (rdYearLevel.getValue() == null) {
                        GlobalCC.errorValueNotEntered("Select Level");
                        return null;
                    }
                    if (rdYearDay.getValue() == null) {
                        GlobalCC.errorValueNotEntered("Select Day");
                        return null;
                    }
                    if (rdYearlyMonth.getValue() == null) {
                        GlobalCC.errorValueNotEntered("Select Month");
                        return null;
                    }
                    txtRecurrence.setValue(getType(rdYearLevel.getValue().toString()) +
                                           " " +
                                           getWeek(rdYearDay.getValue().toString()) +
                                           " of every " +
                                           getMonth(rdYearlyMonth.getValue().toString()));
                    if (rdYearLevel.getValue().equals("1"))
                        cronjob =
                                sec + " " + min + " " + hour + " " + "1-7" + " " +
                                rdYearlyMonth.getValue() + " " +
                                rdYearDay.getValue();
                    else if (rdYearLevel.getValue().equals("2"))
                        cronjob =
                                sec + " " + min + " " + hour + " " + "8-14" + " " +
                                rdYearlyMonth.getValue() + " " +
                                rdYearDay.getValue();
                    else if (rdYearLevel.getValue().equals("3"))
                        cronjob =
                                sec + " " + min + " " + hour + " " + "15-21" +
                                " " + rdYearlyMonth.getValue() + " " +
                                rdYearDay.getValue();
                    else if (rdYearLevel.getValue().equals("4"))
                        cronjob =
                                sec + " " + min + " " + hour + " " + "22-28" +
                                " " + rdYearlyMonth.getValue() + " " +
                                rdYearDay.getValue();
                    else if (rdYearLevel.getValue().equals("5"))
                        cronjob =
                                sec + " " + min + " " + hour + " " + "29-31" +
                                " " + rdYearlyMonth.getValue() + " " +
                                rdYearDay.getValue();

                }
            }
            // System.out.println(cronjob);
            conn = new DBConnector().getDatabaseConnection();
            CallableStatement cst3 = null;
            String Complete =
                "BEGIN tqc_setups_pkg.create_alerts(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);END;";
            cst3 = conn.prepareCall(Complete);
            cst3.setString(1, jobName);
            cst3.setString(2, jobDesc);
            cst3.setTimestamp(3, GlobalCC.extractTime(txtStartTime));
            cst3.setTimestamp(4, null);
            System.out.println("This is the one ring" + qtCodeVal);
            cst3.setObject(5, qtCodeVal);
            cst3.setObject(6, session.getAttribute("sysCode"));
            cst3.setString(7, (String)txtRecurrence.getValue());
            System.out.println("This is the assignee" +
                               txtAssignee.getLabel());
            cst3.setObject(8, txtAssignee.getLabel());
            cst3.setObject(9, notFailUser);
            cst3.setObject(10, notSuccUser);
            if (jobType.equalsIgnoreCase("S")) {
                cst3.setObject(11, session.getAttribute("type"));
            } else {
                cst3.setObject(11, jobType);
            }
            cst3.setObject(12,
                           txtJobTemplate.getLabel()); //(String)txtJobTemplate.getValue());
            cst3.setString(13, notFailTemp);
            cst3.setString(14, notSuccTemp);
            cst3.setString(15, (String)session.getAttribute("addEdit"));
            cst3.setString(16, cronjob);
            cst3.setString(17, statusVal);
            cst3.setString(18, repeat);
            cst3.setString(19, threshTypeVal);
            cst3.setString(20, threshValVal);
            System.out.println(qtCodeVal);
            System.out.println(repeat);
            cst3.execute();
            cst3.close();
            conn.close();
            //        SystemAlerts alerts = new SystemAlerts();
            //
            //        alerts.makeTrigger(jobName, cronjob);
            SystemAlerts alerts = new SystemAlerts();
            alerts.stopJob(jobName);
            success = alerts.removeJob(jobName);
            if (success != null) {
                alerts.scheduleJob(jobName, cronjob);
                //alerts.makeTrigger(jobName, cronjob);
            } else {
                //alerts.makeTrigger(jobName, cronjob);
                return null;
            }


           /* ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:schedule" + "').hide(hints);");*/
            GlobalCC.hidePopup("crm:schedule");
            ADFUtils.findIterator("findSchedulesIterator").executeQuery();
            GlobalCC.refreshUI(schedulerTable);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }


    public String addAlert() {
        session.setAttribute("addEdit", "A");
        repeatSelect.setValue(null);
        txtAssignee.setValue(null);
        txtAssignee.setLabel(null);
        txtNotifyFail.setValue(null);
        txtNotSucc.setValue(null);
        txtJobName.setDisabled(false);
        txtJobName.setValue(null);
        txtJobDesc.setValue(null);
        threshType.setValue(null);
        threshValue.setValue(null);
        txtJobType.setValue(null);
        txtJobTemplate.setValue(null);
        txtFailTemplate.setValue(null);
        txtSuccessTemplate.setValue(null);
        txtStartTime.setValue(null);
        occurence.setVisible(false);
        dailyPanel.setVisible(false);
        weeklyPanel.setVisible(false);
        monthlyPanel.setVisible(false);
        yearlyPanel.setVisible(false);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:schedule" +
                             "').show(hints);");
        return null;
    }

    public void setSchedulerTable(RichTable schedulerTable) {
        this.schedulerTable = schedulerTable;
    }

    public RichTable getSchedulerTable() {
        return schedulerTable;
    }

    public String cancelAssignee() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:p3" +
                             "').hide(hints);");
        return null;
    }

    public String cancelNotify() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:popup1" +
                             "').hide(hints);");
        return null;
    }

    public String cancNotSucceed() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:popup2" +
                             "').hide(hints);");
        return null;
    }

    public void setAssigneeLbl(RichOutputLabel assigneeLbl) {
        this.assigneeLbl = assigneeLbl;
    }

    public RichOutputLabel getAssigneeLbl() {
        return assigneeLbl;
    }

    public void setAssigneePnl(HtmlPanelGrid assigneePnl) {
        this.assigneePnl = assigneePnl;
    }

    public HtmlPanelGrid getAssigneePnl() {
        return assigneePnl;
    }

    public void setNotifyUsrLbl(RichOutputLabel notifyUsrLbl) {
        this.notifyUsrLbl = notifyUsrLbl;
    }

    public RichOutputLabel getNotifyUsrLbl() {
        return notifyUsrLbl;
    }

    public void setNotUsrTmpLbl(RichOutputLabel notUsrTmpLbl) {
        this.notUsrTmpLbl = notUsrTmpLbl;
    }

    public RichOutputLabel getNotUsrTmpLbl() {
        return notUsrTmpLbl;
    }

    public void jobTypeSelected(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue() &&
            valueChangeEvent.getNewValue() != null) {
            if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("A")) {
                assigneeLbl.setValue("Alert To:");
                execObjLbl.setValue("Message Template");
                assigneeLbl.setVisible(true);
                txtAssignee.setVisible(true);
                assigneeBtn.setVisible(true);
                execObjLbl.setVisible(true);
                txtJobTemplate.setVisible(true);
                execBtn.setVisible(true);
            } else if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("R")) {
                assigneeLbl.setValue("Send To:");
                execObjLbl.setValue("Report Name");
                assigneeLbl.setVisible(true);
                txtAssignee.setVisible(true);
                assigneeBtn.setVisible(true);
                execObjLbl.setVisible(true);
                txtJobTemplate.setVisible(true);
                execBtn.setVisible(true);
            } else if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("S")) {
                assigneeLbl.setValue("Send To:");
                execObjLbl.setValue("Task Name");
                assigneeLbl.setVisible(true);
                txtAssignee.setVisible(true);
                assigneeBtn.setVisible(true);
                execObjLbl.setVisible(true);
                txtJobTemplate.setVisible(true);
                execBtn.setVisible(true);
            } else if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("J")) {
                assigneeLbl.setValue("Send To:");
                execObjLbl.setValue("Routine Name");
                assigneeLbl.setVisible(true);
                txtAssignee.setVisible(true);
                assigneeBtn.setVisible(true);
                execObjLbl.setVisible(true);
                txtJobTemplate.setVisible(true);
                execBtn.setVisible(true);
            } else {
                assigneeLbl.setVisible(false);
                txtAssignee.setVisible(false);
                assigneeBtn.setVisible(false);
                execObjLbl.setVisible(false);
                txtJobTemplate.setVisible(false);
                execBtn.setVisible(false);
            }
            GlobalCC.refreshUI(assigneeLbl);
            GlobalCC.refreshUI(txtAssignee);
            GlobalCC.refreshUI(assigneeBtn);
            GlobalCC.refreshUI(execObjLbl);
            GlobalCC.refreshUI(txtJobTemplate);
            GlobalCC.refreshUI(execBtn);
        }
    }

    public void setOccurence(RichOutputLabel occurence) {
        this.occurence = occurence;
    }

    public RichOutputLabel getOccurence() {
        return occurence;
    }

    public void setTxtRecurrence(RichInputText txtRecurrence) {
        this.txtRecurrence = txtRecurrence;
    }

    public RichInputText getTxtRecurrence() {
        return txtRecurrence;
    }

    public void setMessageTemp(RichTable messageTemp) {
        this.messageTemp = messageTemp;
    }

    public RichTable getMessageTemp() {
        return messageTemp;
    }

    public String templateSelected() {
        Object key2 = messageTemp.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        String type = (String)session.getAttribute("type");
        if (type.equalsIgnoreCase("F")) {
            txtFailTemplate.setLabel(nodeBinding.getAttribute("msgtCode").toString());
            txtFailTemplate.setValue(nodeBinding.getAttribute("msgtMsg"));
            GlobalCC.refreshUI(txtFailTemplate);
        } else if (type.equalsIgnoreCase("S")) {
            txtSuccessTemplate.setLabel(nodeBinding.getAttribute("msgtCode").toString());
            txtSuccessTemplate.setValue(nodeBinding.getAttribute("msgtMsg"));
            GlobalCC.refreshUI(txtSuccessTemplate);
        } else if (type.equalsIgnoreCase("A")) {
            txtJobTemplate.setLabel(nodeBinding.getAttribute("msgtCode").toString());
            txtJobTemplate.setValue(nodeBinding.getAttribute("msgtMsg"));
            GlobalCC.refreshUI(txtJobTemplate);
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:template" +
                             "').hide(hints);");
        return null;
    }

    public String templateCancel() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:template" +
                             "').hide(hints);");
        return null;
    }

    public String launchSuccTemp() {
        session.setAttribute("type", "S");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:template" +
                             "').show(hints);");
        return null;
    }

    public String launchFailTemp() {
        session.setAttribute("type", "F");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:template" +
                             "').show(hints);");
        return null;
    }

    public void setAssigneeBtn(RichCommandButton assigneeBtn) {
        this.assigneeBtn = assigneeBtn;
    }

    public RichCommandButton getAssigneeBtn() {
        return assigneeBtn;
    }

    public void setExecObjLbl(RichOutputLabel execObjLbl) {
        this.execObjLbl = execObjLbl;
    }

    public RichOutputLabel getExecObjLbl() {
        return execObjLbl;
    }

    public void setExecBtn(RichCommandButton execBtn) {
        this.execBtn = execBtn;
    }

    public RichCommandButton getExecBtn() {
        return execBtn;
    }

    public String launchExecObject() {
        String jobType = GlobalCC.checkNullValues(txtJobType.getValue());
        if (jobType == null) {
            GlobalCC.errorValueNotEntered("Select Job Type First");
            return null;
        }
        if (jobType.equalsIgnoreCase("A")) {
            session.setAttribute("type", "A");
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:template" + "').show(hints);");
        } else if (jobType.equalsIgnoreCase("R")) {
            session.setAttribute("type", "RPT");
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:execObj" + "').show(hints);");
            ADFUtils.findIterator("findExecutionObjectsIterator").executeQuery();
            GlobalCC.refreshUI(execTab);
        } else if (jobType.equalsIgnoreCase("J")) {
            session.setAttribute("type", "J");
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:execObj" + "').show(hints);");
            ADFUtils.findIterator("findExecutionObjectsIterator").executeQuery();
            GlobalCC.refreshUI(execTab);
        } else {
            session.setAttribute("type", null);
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:execObj" + "').show(hints);");
            ADFUtils.findIterator("findExecutionObjectsIterator").executeQuery();
            GlobalCC.refreshUI(execTab);

        }
        return null;
    }

    public void setStatus(RichSelectOneChoice status) {
        this.status = status;
    }

    public RichSelectOneChoice getStatus() {
        return status;
    }
    public String deleteJob(){
        try{
            Object key = schedulerTbl.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if(nodeBinding == null){
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        if (nodeBinding.getAttribute("qtJobName").toString().equalsIgnoreCase("INCOMING_EMAIL") ||
            nodeBinding.getAttribute("qtJobName").toString().equalsIgnoreCase("OUTGOING_SMS")) {
            GlobalCC.errorValueNotEntered("Cannot Delete Job: " +
                                          nodeBinding.getAttribute("qtJobName") +
                                          "; This is a standard System Routine");
            return null;
        }
        SystemAlerts alerts = new SystemAlerts();
        String success =
            alerts.removeJob((String)nodeBinding.getAttribute("qtJobName"));
        ADFUtils.findIterator("currentRunningJobsIterator").executeQuery();
        GlobalCC.refreshUI(schedulerTbl);
        }catch(Exception e){
            GlobalCC.EXCEPTIONREPORTING(e);
        }
         GlobalCC.INFORMATIONREPORTING("Job deleted successfully");
         return null;

    }
    public String deleteAlert() {
        Object key2 = schedulerTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        if (nodeBinding.getAttribute("qtJobName").toString().equalsIgnoreCase("INCOMING_EMAIL") ||
            nodeBinding.getAttribute("qtJobName").toString().equalsIgnoreCase("OUTGOING_SMS")) {
            GlobalCC.errorValueNotEntered("Cannot Delete Job: " +
                                          nodeBinding.getAttribute("qtJobName") +
                                          "; This is a standard System Routine");
            return null;
        }
        OracleConnection conn = null;
        try {
            conn = new DBConnector().getDatabaseConnection();
            CallableStatement cst3 = null;
            String Complete =
                "BEGIN tqc_setups_pkg.create_alerts(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);END;";
            cst3 = conn.prepareCall(Complete);
            cst3.setString(1, null);
            cst3.setString(2, null);
            cst3.setTimestamp(3, null);
            cst3.setTimestamp(4, null);
            cst3.setBigDecimal(5,
                               (BigDecimal)nodeBinding.getAttribute("qtCode"));
            cst3.setString(6, null);
            cst3.setString(7, null);
            cst3.setString(8, null);
            cst3.setString(9, null);
            cst3.setString(10, null);
            cst3.setString(11, null);
            cst3.setString(12, null); //(String)txtJobTemplate.getValue());
            cst3.setString(13, null);
            cst3.setString(14, null);
            cst3.setString(15, "D");
            cst3.setString(16, null);
            cst3.setString(17, null);
            cst3.setString(18, null);
            cst3.setString(19, null);
            cst3.setString(20, null);
            cst3.execute();
            cst3.close();
            conn.close();
            //            SystemAlerts alerts = new SystemAlerts();
            //           alerts.stopJob((String)nodeBinding.getAttribute("qtJobName"));

            SystemAlerts alerts = new SystemAlerts();
            String success =
                alerts.removeJob((String)nodeBinding.getAttribute("qtJobName"));

            ADFUtils.findIterator("findSchedulesIterator").executeQuery();
            GlobalCC.refreshUI(schedulerTable);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String editSchedule() {
        Object key2 = schedulerTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        session.setAttribute("addEdit", "E");
        repeatSelect.setValue(r.getAttribute("qtRecurrence"));
        if (r.getAttribute("qtJobType").toString().equalsIgnoreCase("A")) {
            txtAssignee.setValue(r.getAttribute("assignee"));
            if (r.getAttribute("qtJobAssignee") != null) {
                txtAssignee.setLabel(r.getAttribute("qtJobAssignee").toString());
            }
            txtAssignee.setVisible(true);
            assigneeLbl.setVisible(true);
            execObjLbl.setValue("Message Template");
            execObjLbl.setVisible(true);
            txtJobTemplate.setVisible(true);
            execBtn.setVisible(true);
            assigneeBtn.setVisible(true);
        } else if (r.getAttribute("qtJobType").toString().equalsIgnoreCase("R")) {
            txtAssignee.setValue(r.getAttribute("assignee"));
            txtAssignee.setLabel(r.getAttribute("qtJobAssignee").toString());
            txtAssignee.setVisible(true);
            assigneeLbl.setVisible(true);
            execObjLbl.setValue("Report Name");
            execBtn.setVisible(true);
            assigneeBtn.setVisible(true);
            execObjLbl.setVisible(true);
            txtJobTemplate.setVisible(true);
            execBtn.setVisible(true);
        } else if (r.getAttribute("qtJobType").toString().equalsIgnoreCase("W")) {
            txtAssignee.setValue(r.getAttribute("assignee"));
            txtAssignee.setLabel(r.getAttribute("qtJobAssignee").toString());
            txtAssignee.setVisible(true);
            assigneeLbl.setVisible(true);
            execObjLbl.setValue("Task Name");
            execBtn.setVisible(true);
            assigneeBtn.setVisible(true);
            execObjLbl.setVisible(true);
            txtJobTemplate.setVisible(true);
            execBtn.setVisible(true);
        } else if (r.getAttribute("qtJobType").toString().equalsIgnoreCase("J")) {
            txtAssignee.setValue(r.getAttribute("assignee"));
            if (r.getAttribute("assignee") != null) {
                txtAssignee.setLabel(r.getAttribute("qtJobAssignee").toString());
            } else {
                txtAssignee.setLabel(null);
            }
            txtAssignee.setVisible(true);
            assigneeLbl.setVisible(true);
            execObjLbl.setValue("Routine Name");
            execBtn.setVisible(true);
            assigneeBtn.setVisible(true);
            execObjLbl.setVisible(true);
            txtJobTemplate.setVisible(true);
            execBtn.setVisible(true);
        } else {
            execObjLbl.setVisible(false);
            txtJobTemplate.setVisible(false);
            execBtn.setVisible(false);
            assigneeBtn.setVisible(false);
            txtAssignee.setVisible(false);
            assigneeLbl.setVisible(false);
        }
        if (r.getAttribute("qtNotifiedFailUser") != null) {
            txtNotifyFail.setValue(r.getAttribute("failUser"));
            txtNotifyFail.setLabel(r.getAttribute("qtNotifiedFailUser").toString());
        } else {
            txtNotifyFail.setValue(null);
            txtNotifyFail.setLabel(null);
        }
        if (r.getAttribute("qtNotifiedSuccUser") != null) {
            txtNotSucc.setValue(r.getAttribute("succUser"));
            txtNotSucc.setLabel(r.getAttribute("qtNotifiedSuccUser").toString());
        } else {
            txtNotSucc.setValue(null);
            txtNotSucc.setLabel(null);
        }
        txtJobName.setDisabled(true);
        txtJobName.setValue(r.getAttribute("qtJobName"));
        txtJobDesc.setValue(r.getAttribute("qtDescription"));
        if (r.getAttribute("qtJobType").toString().equalsIgnoreCase("W")) {
            txtJobType.setValue("S");
        } else {
            txtJobType.setValue(r.getAttribute("qtJobType"));
        }
        if (r.getAttribute("qtJobTemplate") != null) {
            txtJobTemplate.setValue(r.getAttribute("jobTemplate"));
            txtJobTemplate.setLabel(r.getAttribute("qtJobTemplate").toString());
        }
        if (r.getAttribute("qtFailNotifyTemplate") != null) {
            txtFailTemplate.setValue(r.getAttribute("failTemplate"));
            txtFailTemplate.setLabel(r.getAttribute("qtFailNotifyTemplate").toString());
        }

        if (r.getAttribute("qtSuccNotifyTemplate") != null) {
            txtSuccessTemplate.setValue(r.getAttribute("succTemplate"));
            txtSuccessTemplate.setLabel(r.getAttribute("qtSuccNotifyTemplate").toString());
        }

        occurence.setVisible(true);
        String recurrence = "N";
        if (r.getAttribute("qtRecurrence") != null) {
            recurrence = r.getAttribute("qtRecurrence").toString();
        }
        String recurType = "N";
        if (r.getAttribute("qtRecurrenceType") != null) {
            recurType = r.getAttribute("qtRecurrenceType").toString();
        }
        if (recurrence.equalsIgnoreCase("N")) {
            occurence.setVisible(false);
            dailyPanel.setVisible(false);
            weeklyPanel.setVisible(false);
            monthlyPanel.setVisible(false);
            yearlyPanel.setVisible(false);
        } else if (recurrence.equalsIgnoreCase("D")) {
            occurence.setVisible(true);
            dailyPanel.setVisible(true);
            weeklyPanel.setVisible(false);
            monthlyPanel.setVisible(false);
            yearlyPanel.setVisible(false);
            if (recurType.equalsIgnoreCase("Every Day")) {
                rdEveryDay.setSelected(true);
            } else if (recurType.equalsIgnoreCase("Every Week Day")) {
                rdEveryWeekday.setSelected(true);
            } else {
                rdEveryDayOption.setSelected(true);
                recurType = recurType.replace("Every", "");
                recurType = recurType.replace("Days", "");
                recurType = recurType.trim();
                txtEveryDays.setValue(recurType);
            }
        } else if (recurrence.equalsIgnoreCase("W")) {
            occurence.setVisible(true);
            dailyPanel.setVisible(false);
            weeklyPanel.setVisible(true);
            monthlyPanel.setVisible(false);
            yearlyPanel.setVisible(false);
            if (recurType.contains("on")) {
                rdWeekNo.setSelected(true);
            } else {
                rdEveryWeek.setSelected(true);
            }
        } else if (recurrence.equalsIgnoreCase("M")) {
            occurence.setVisible(true);
            dailyPanel.setVisible(false);
            weeklyPanel.setVisible(false);
            monthlyPanel.setVisible(true);
            yearlyPanel.setVisible(false);
        } else if (recurrence.equalsIgnoreCase("Y")) {
            occurence.setVisible(true);
            dailyPanel.setVisible(false);
            weeklyPanel.setVisible(false);
            monthlyPanel.setVisible(false);
            yearlyPanel.setVisible(true);
        }


        txtStartTime.setValue(r.getAttribute("qtStartTime"));
        status.setValue(r.getAttribute("qtStatus"));
        qtCode.setValue(r.getAttribute("qtCode"));
        threshType.setValue(r.getAttribute("qtThreshType"));
        threshValue.setValue(r.getAttribute("qtThreshValue"));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:schedule" +
                             "').show(hints);");
        return null;
    }

    public void tblSystemsListener(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet rowKeySet = selectionEvent.getAddedSet();
            Object key2 = rowKeySet.iterator().next();
            tblSystems.setRowKey(key2);
            JUCtrlValueBinding nodeBinding =
                (JUCtrlValueBinding)tblSystems.getRowData();
            if (nodeBinding != null) {
                session.setAttribute("sysCode",
                                     nodeBinding.getAttribute("code"));
                ADFUtils.findIterator("findMessageTemplatesIterator").executeQuery();
                GlobalCC.refreshUI(messageTemp);
                ADFUtils.findIterator("findSchedulesIterator").executeQuery();
                GlobalCC.refreshUI(schedulerTable);
            }
        }
    }

    public void setTblSystems(RichTable tblSystems) {
        this.tblSystems = tblSystems;
    }

    public RichTable getTblSystems() {
        return tblSystems;
    }

    public void setQtCode(RichInputNumberSpinbox qtCode) {
        this.qtCode = qtCode;
    }

    public RichInputNumberSpinbox getQtCode() {
        return qtCode;
    }

    public void setExecTab(RichTable execTab) {
        this.execTab = execTab;
    }

    public RichTable getExecTab() {
        return execTab;
    }

    public String cancelExec() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:execObj" +
                             "').hide(hints);");
        return null;
    }

    public String execSelected() {
        Object key2 = execTab.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        session.setAttribute("type", nodeBinding.getAttribute("objType"));
        txtJobTemplate.setLabel(nodeBinding.getAttribute("objCode").toString());
        txtJobTemplate.setValue(nodeBinding.getAttribute("objDesc"));
        GlobalCC.refreshUI(txtJobTemplate);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:execObj" +
                             "').hide(hints);");
        return null;
    }

    public void setThreshType(RichSelectOneChoice threshType) {
        this.threshType = threshType;
    }

    public RichSelectOneChoice getThreshType() {
        return threshType;
    }

    public void setThreshValue(RichInputNumberSpinbox threshValue) {
        this.threshValue = threshValue;
    }

    public RichInputNumberSpinbox getThreshValue() {
        return threshValue;
    }

    public void setTxtExecutionTime(RichInputDate txtExecutionTime) {
        this.txtExecutionTime = txtExecutionTime;
    }

    public RichInputDate getTxtExecutionTime() {
        return txtExecutionTime;
    }

    public String reschedule() {
        Object key2 = schedulerTbl.getSelectedRowData();
        JUCtrlValueBinding n = (JUCtrlValueBinding)key2;
        if (n == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        try {
            SchedulerConfig config = new SchedulerConfig();
            ExternalContext ex =  FacesContext.getCurrentInstance().getExternalContext();
             ServletContext contextEvent = (ServletContext)ex.getContext();
            Scheduler sched = config.getScheduler(contextEvent);
            System.out.println("qtJobName: "+(String)n.getAttribute("qtJobName"));
            TriggerKey triggerKey = new TriggerKey((String)n.getAttribute("qtJobName") , "TurnQuestJobs");
            if (n.getAttribute("objLocation").equals("Resume")) {
                sched.resumeTrigger(triggerKey);
            } 
            if(n.getAttribute("objLocation").equals("Pause")){
                sched.pauseTrigger(triggerKey);
                System.out.println("Job Paused");
            }
            ADFUtils.findIterator("currentRunningJobsIterator").executeQuery();
            GlobalCC.refreshUI(schedulerTbl);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
            return null;
        }
        GlobalCC.refreshUI(schedulerTbl);
        GlobalCC.INFORMATIONREPORTING("Process Successfully......");

        return null;
    }

    public void setSchedulerTbl(RichTable schedulerTbl) {
        this.schedulerTbl = schedulerTbl;
    }

    public RichTable getSchedulerTbl() {
        return schedulerTbl;
    }
}
