package org.jbpm;


import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;
import org.jbpm.api.task.AssignmentHandler;


public class AssignTask implements AssignmentHandler {
    public AssignTask() {
        super();
    }
    // HttpSession session =
    // (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    String assignee;

    public void assign(Assignable assignable, OpenExecution execution) {
        //  if(session.getAttribute("assignee")==null){
        //      assignee = ((BigDecimal)session.getAttribute("UserCode")).toString();

        //  }else{
        //      assignee = (String)session.getAttribute("assignee");
        //  }
        //System.out.println("Jose");
        assignable.setAssignee(assignee);

    }

}
