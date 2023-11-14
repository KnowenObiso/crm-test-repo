package TurnQuest.view.bpm;


import TurnQuest.view.Base.GlobalCC;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;
import org.jbpm.api.task.AssignmentHandler;


public class AssignProcessTask implements AssignmentHandler {

    public void assign(Assignable assignable, OpenExecution execution) {
        HttpSession session = null;
        try {
            session =
                    (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        } catch (Exception e) {
            session = null;
        }
        if (session != null) {
            String assignee = GlobalCC.checkNullValues(session.getAttribute("TaskAssignee"));
            if (assignee == null)
                assignable.setAssignee(GlobalCC.checkNullValues(session.getAttribute("Username")));
            else{
                assignable.setAssignee(assignee);
            }
        }  
    }
}