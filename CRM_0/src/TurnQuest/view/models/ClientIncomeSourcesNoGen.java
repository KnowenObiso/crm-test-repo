package TurnQuest.view.models;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class ClientIncomeSourcesNoGen  implements IdentifierGenerator {
    private String DEFAULT_SEQUENCE_NAME = "TQC_CLNTIS_CODE_SEQ";
    
    @Override
    public Serializable generate(SessionImplementor sessionImpl, Object data)
            throws HibernateException {  
        return new  NoGenerator().generate(sessionImpl, data, DEFAULT_SEQUENCE_NAME);
    }
}
