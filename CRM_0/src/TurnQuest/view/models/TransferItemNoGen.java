

package TurnQuest.view.models; 


import java.io.Serializable; 
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor; 
import org.hibernate.id.IdentifierGenerator;

/**
 * @author Dancan Kavagi
 * @date Jan 25, 2017
 * @version 1.0
 * @description : TransferItemNoGen.java
 */

public class TransferItemNoGen implements IdentifierGenerator {
    
    private String DEFAULT_SEQUENCE_NAME = "TQC_TTI_CODE_SEQ";
    
    @Override
    public Serializable generate(SessionImplementor sessionImpl, Object data)
            throws HibernateException {  
        return new  NoGenerator().generate(sessionImpl, data, DEFAULT_SEQUENCE_NAME);
    }
}