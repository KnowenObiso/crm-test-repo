package TurnQuest.view.models;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;  

/**
 * @author Dancan Kavagi
 * @date Jan 25, 2017
 * @version 1.0
 * @description : NoGenerator.java
 */
 

public class NoGenerator { 
     
    public Serializable generate(SessionImplementor sessionImpl, Object data,String sequence_id)
            throws HibernateException {
        Serializable result = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try { 
            if(sequence_id==null || sequence_id==""){
                throw new HibernateException("No sequence defined!");
            }
            System.out.println(data);
            String prefix = "";
            connection = sessionImpl.connection();
            statement = connection.createStatement();                   
             try {  
                 resultSet = statement.executeQuery("SELECT "+sequence_id+".NEXTVAL FROM DUAL"); 
             } catch(Exception ex) {
                 // if sequence is not found then creating the sequence
                 ex.printStackTrace();
                 statement = connection.createStatement();
                 statement.execute("CREATE SEQUENCE "+sequence_id);
                 System.out.println("Sequence Created successfully. ");
                 resultSet = statement.executeQuery("SELECT "+sequence_id+".NEXTVAL FROM DUAL");
             }
            
            if(resultSet.next()) {                               
                result = resultSet.getBigDecimal(1);
                System.out.println("Custom generated Sequence value : "+result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}