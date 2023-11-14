package TurnQuest.view.Base;


/*
import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.RepositoryService;

*/
public class deployment {

    public deployment() {
        super();
    }
    /* RepositoryService repositoryService;

    public void jpdlFileChange(ValueChangeEvent valueChangeEvent) throws IOException {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            UploadedFile file = (UploadedFile)valueChangeEvent.getNewValue();

            if (file != null) {
                ProcessEngine processEngine =
                    new Configuration().setResource("jbpm.cfg.xml").buildProcessEngine();
                repositoryService = processEngine.getRepositoryService();
                repositoryService.createDeployment().addResourceFromInputStream(file.getFilename(),
                                                                                file.getInputStream()).deploy();
            }

        }
        // Add event code here...
    }
*/
}
