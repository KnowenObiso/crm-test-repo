package TurnQuest.view.models;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor; 
import org.hibernate.id.IdentifierGenerator;

/**
 * @author Dancan Kavagi
 * @date Jan 25, 2017
 * @version 1.0
 * @description : SignatoryNumberGenerator.java
 */
 
public class SignatoryNumberGenerator implements IdentifierGenerator {
    
    private String DEFAULT_SEQUENCE_NAME = "TQC_CLNSIG_CODE_SEQ";
    
    @Override
    public Serializable generate(SessionImplementor sessionImpl, Object data)
            throws HibernateException {  
        return new  NoGenerator().generate(sessionImpl, data, DEFAULT_SEQUENCE_NAME);
    }
}