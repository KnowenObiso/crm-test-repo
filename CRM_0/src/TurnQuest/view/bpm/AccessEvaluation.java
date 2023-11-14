package TurnQuest.view.bpm;

import org.jbpm.api.jpdl.DecisionHandler;
import org.jbpm.api.model.OpenExecution;

public class AccessEvaluation implements DecisionHandler {
    private static final long serialVersionUID = 1L;

    public AccessEvaluation() {
        super();
    }
    public String decide(OpenExecution execution) {
        /* try {
            String Rights = null;
            BPMProcessing wf = new BPMProcessing();
            Rights = wf.CheckUserRights();
            //System.out.println("Rights..."+Rights);
            if (Rights == null) {
                return "No";
            }
            if (Rights.equalsIgnoreCase("N")) {
                return "No";
            }
            if (Rights.equalsIgnoreCase("Y")) {
                return "Yes";
            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }*/
        return "Yes";
    }
}