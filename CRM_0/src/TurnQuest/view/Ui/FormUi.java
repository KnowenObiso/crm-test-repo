package TurnQuest.view.Ui;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;

import org.apache.commons.dbutils.DbUtils;


public class FormUi {
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public FormUi() {
        super();
    }
    /* // find a jsf component inside the JSF page

    public static UIComponent UIComponent(String id) {
        String uri =
            ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
        System.out.println("PAGE OPENED: " + uri);

        return fetchUIComponent(FacesContext.getCurrentInstance().getViewRoot(),
                                id);
    }

    private static UIComponent fetchUIComponent(UIComponent root, String id) {
        if (root != null) {
            if (root.getId().equals(id)) {
                return root;
            }
            if (root.getChildCount() > 0) {
                for (UIComponent subUiComponent : root.getChildren()) {
                    UIComponent returnComponent =
                        fetchUIComponent(subUiComponent, id);
                    if (returnComponent != null) {
                        return returnComponent;
                    }
                }
            }
        }
        return null;
    } */

    // CRM-1452 implementing search for a UIComponent
    // find a jsf component inside the JSF page

    private static UIComponent getUIComponent(String name) {
        FacesContext facesCtx = FacesContext.getCurrentInstance();
        return getUIComponent(facesCtx.getViewRoot(), name);
    }

    // find a UIComponent inside a UIComponent

    private static UIComponent getUIComponent(UIComponent component,
                                              String name) {
        if (component != null)
            System.out.println(component.getId());

        List<UIComponent> items = component.getChildren();
        Iterator<UIComponent> facets = component.getFacetsAndChildren();

        if (items.size() > 0) {
            System.out.println("got childern");
            for (UIComponent item : items) {
                UIComponent found = getUIComponent(item, name);
                if (found != null) {
                    return found;
                }
                if (item.getId().equalsIgnoreCase(name)) {
                    return item;
                }
            }
        } else if (facets.hasNext()) {
            System.out.println("got facets");
            while (facets.hasNext()) {
                UIComponent facet = facets.next();
                UIComponent found = getUIComponent(facet, name);
                if (found != null) {
                    return found;
                }
                if (facet.getId().equalsIgnoreCase(name)) {
                    return facet;
                }
            }
        }
        return null;
    }
    // END.

    public static String UiValue(UIComponent mine) {
        String val = null;

        if (mine != null) {
            if (mine.getClass() == RichSelectOneChoice.class) {
                RichSelectOneChoice ui = (RichSelectOneChoice)mine;
                val = GlobalCC.checkNullValues(ui.getValue());
            } else if (mine.getClass() == RichInputDate.class) {
                RichInputDate ui = (RichInputDate)mine;
                val = GlobalCC.checkNullValues(ui.getValue());
            } else if (mine.getClass() == RichInputText.class) {
                RichInputText ui = (RichInputText)mine;
                val = GlobalCC.checkNullValues(ui.getValue());
            } else if (mine.getClass() == RichInputNumberSpinbox.class) {
                RichInputNumberSpinbox ui = (RichInputNumberSpinbox)mine;
                val = GlobalCC.checkNullValues(ui.getValue());
            }
            //System.out.println("Ui Component Id --> " + mine.getId()+" Value --> "+val);
        }
        return val;
    }

    public static boolean UiVisible(UIComponent mine) {
        if (mine != null) {
            if (mine.getClass() == RichSelectOneChoice.class) {
                return ((RichSelectOneChoice)mine).isVisible();
            } else if (mine.getClass() == RichInputDate.class) {
                return ((RichInputDate)mine).isVisible();
            } else if (mine.getClass() == RichInputText.class) {
                return ((RichInputText)mine).isVisible();
            } else if (mine.getClass() == RichInputNumberSpinbox.class) {
                return ((RichInputNumberSpinbox)mine).isVisible();
            }
        }
        return false;
    }

    public static String UiLabel(UIComponent mine) {
        if (mine != null) {
            if (mine.getClass() == RichSelectOneChoice.class) {
                return ((RichSelectOneChoice)mine).getLabel();
            } else if (mine.getClass() == RichInputDate.class) {
                return ((RichInputDate)mine).getLabel();
            } else if (mine.getClass() == RichInputText.class) {
                return ((RichInputText)mine).getLabel();
            } else if (mine.getClass() == RichInputNumberSpinbox.class) {
                return ((RichInputNumberSpinbox)mine).getLabel();
            }
        }
        return null;
    }

    public static boolean UiDisabled(UIComponent mine) {
        if (mine != null) {
            if (mine.getClass() == RichSelectOneChoice.class) {
                return ((RichSelectOneChoice)mine).isDisabled();
            } else if (mine.getClass() == RichInputDate.class) {
                return ((RichInputDate)mine).isDisabled();
            } else if (mine.getClass() == RichInputText.class) {
                return ((RichInputText)mine).isDisabled();
            } else if (mine.getClass() == RichInputNumberSpinbox.class) {
                return ((RichInputNumberSpinbox)mine).isDisabled();
            }
        }
        return false;
    }

    public List<FormField> getFieldsGroup(String GroupId) {
        List<FormField> fields = new ArrayList<FormField>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin ?:= Tqc_Forms_Pkg.get_group_fields(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleResultSet rs = null;
        try {
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setString(2, GroupId);
            statement.setBigDecimal(3, null);
            statement.execute();
            rs = (OracleResultSet)statement.getObject(1);

            while (rs.next()) {
                // For every row, create a ClientTitle object with the
                // values and add it to the list
                FormField field = new FormField();
                field.setUi_Id(GlobalCC.checkNullValues(rs.getString("fm_ui_id")));
                field.setSession_Id(GlobalCC.checkNullValues(rs.getString("fm_session_id")));
                field.setLabel(GlobalCC.checkNullValues(rs.getString("fm_field_label")));
                field.setName(GlobalCC.checkNullValues(rs.getString("fm_field_name")));
                field.setRequired(GlobalCC.checkNullValues(rs.getString("fm_mandatory")));
                field.setVisible(GlobalCC.checkNullValues(rs.getString("fm_visible")));
                field.setValidate(GlobalCC.checkNullValues(rs.getString("fm_validate")));
                fields.add(field);
            }
            System.out.println("Group Field: " + fields.size());
            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        } finally {
            DbUtils.closeQuietly(connection, statement, rs);
        }

        return fields;
    }

    public boolean validate(String GroupId) {
        List<FormField> fieldList = getFieldsGroup(GroupId);

        for (FormField f : fieldList) {
            if (f.getRequired() && f.isValidable()) {
                String val = null;
                UIComponent ui = null;
                if (f.getSession_Id() != null) {
                    val =
GlobalCC.checkNullValues(session.getAttribute(f.getSession_Id()));
                }
                if (f.getUi_Id() != null) {
                    ui = FormUi.getUIComponent(f.getUi_Id());
                    if (ui == null) {
                        GlobalCC.errorValueNotEntered("Input With id '" +
                                                      f.getUi_Id() +
                                                      "' not found! ");
                        return false;
                    }
                    val = FormUi.UiValue(ui);
                    if ((!FormUi.UiVisible(ui)) && val == null) {
                        continue;
                    }
                }

                if (val == null &&
                    (f.getUi_Id() != null || f.getSession_Id() != null)) {
                    String label = f.getLabel();
                    System.out.println("label--->" + label + "val--->" + val +
                                       "f.getUi_Id()--->" + f.getUi_Id() +
                                       "f.getName()--->" + f.getName() +
                                       "FormUi.UiLabel(ui)-->" +
                                       FormUi.UiLabel(ui));
                    label = label.replaceAll("\\:", "");
                    label = label.trim();
                    String errMsg =
                        "Error Value Missing: Enter the " + label + "!";

                    if (ui != null && !FormUi.UiDisabled(ui)) {
                        String vLabel =
                            GlobalCC.checkNullValues(FormUi.UiLabel(ui));
                        if (vLabel != null) {
                            vLabel = vLabel.replaceAll("\\:", "");
                            label = vLabel;
                        }

                        errMsg =
                                "Error Value Missing: Enter the " + label + "!";
                    }
                    GlobalCC.errorValueNotEntered(errMsg);
                    return false;
                }
            }
        }
        return true;
    }
}
