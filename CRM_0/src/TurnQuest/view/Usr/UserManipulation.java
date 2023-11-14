package TurnQuest.view.Usr;


import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;

import org.apache.myfaces.trinidad.event.SelectionEvent;


public class UserManipulation {
    private RichTable users;
    private RichTable tbluserGroup;

    public UserManipulation() {
        super();
    }
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public void setUsers(RichTable users) {
        this.users = users;
    }

    public RichTable getUsers() {
        return users;
    }

    public void userSelected(SelectionEvent selectionEvent) {

    }

    public void setTbluserGroup(RichTable tbluserGroup) {
        this.tbluserGroup = tbluserGroup;
    }

    public RichTable getTbluserGroup() {
        return tbluserGroup;
    }

    public void actiontbluserGroupListener(SelectionEvent selectionEvent) {
        // Add event code here...
    }
}
