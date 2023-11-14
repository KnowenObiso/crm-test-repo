package TurnQuest.view.dao;

import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.HibernateUtil;

import TurnQuest.view.models.ClientType;

import java.math.BigDecimal;

import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ClientTypeDAO {
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    public ClientTypeDAO() {
        super();
    }
    //-----------start dao's fetchClientTypes --------------------//
        public List<ClientType> fetchClientTypes(){
                    List<ClientType> clienttypes = null; 
            Session dbSess = HibernateUtil.getSession();
            Transaction tx = null;

             try {
                String query = "FROM ClientType S";
                tx = dbSess.beginTransaction(); 
                clienttypes = dbSess.createQuery(query).list(); 
                for (Iterator iterator = clienttypes.iterator(); iterator.hasNext();){
                   ClientType item = (ClientType) iterator.next(); 
                   System.out.println(item);  
                }
                tx.commit();
             } catch (HibernateException e) {
                if (tx!=null) tx.rollback();
                GlobalCC.EXCEPTIONREPORTING(e);
             } finally {
                dbSess.close(); 
             }
            return clienttypes;
        }
        public String updateClientType(ClientType val){ 
               Session dbSess = HibernateUtil.getSession();
               Transaction tx = null; 
               try { 
                     tx = dbSess.beginTransaction(); 
                   
                     ClientType item = new ClientType();
                   
                     item = (ClientType)dbSess.get(ClientType.class, val.getCode()); 
                     
                    item.setCode(val.getCode());
                    item.setClntType(val.getClntType());
                    item.setCategory(val.getCategory());
                    item.setType(val.getType());
                    item.setPerson(val.getPerson());
                    item.setDesc(val.getDesc());
                   
                     dbSess.update(item);  
                   
                     tx.commit();
                   
                     ADFUtils.findIterator("fetchClientTypesIterator").executeQuery();  
                     String message = "ClientType Saved Successfully!";
                     GlobalCC.INFORMATIONREPORTING(message); 
               } catch (HibernateException e) {
                  if (tx!=null) tx.rollback();
                  GlobalCC.EXCEPTIONREPORTING(e); 
               } finally {
                  dbSess.close(); 
               }
              return null;
          }
        public String addClientType(ClientType val){ 
               Session dbSess = HibernateUtil.getSession();
               Transaction tx = null; 
               try { 
                     tx = dbSess.beginTransaction(); 
                   
                     ClientType item = new ClientType(); 
                     
                    item.setCode(val.getCode());
                    item.setClntType(val.getClntType());
                    item.setCategory(val.getCategory());
                    item.setType(val.getType());
                    item.setPerson(val.getPerson());
                    item.setDesc(val.getDesc());
                   
                     dbSess.persist(item);  
                   
                     tx.commit();
                   
                     ADFUtils.findIterator("fetchClientTypesIterator").executeQuery();  
                     String message = "ClientType Saved Successfully!";
                     GlobalCC.INFORMATIONREPORTING(message); 
               } catch (HibernateException e) {
                  if (tx!=null) tx.rollback();
                  GlobalCC.EXCEPTIONREPORTING(e); 
               } finally {
                  dbSess.close(); 
               }
              return null;
          } 
         //-----------start dao's fetchClientType --------------------//
         public TurnQuest.view.models.ClientType fetchClientTypeByClientTypeCode(BigDecimal clienttypeCode){
             TurnQuest.view.models.ClientType clienttype = null; 
             Session dbSess = HibernateUtil.getSession();
             Transaction tx = null; 
              try { 
                 tx = dbSess.beginTransaction(); 
                 clienttype = (TurnQuest.view.models.ClientType)dbSess.get(TurnQuest.view.models.ClientType.class, clienttypeCode);
                 tx.commit();
              } catch (HibernateException e) { 
                 GlobalCC.EXCEPTIONREPORTING(e);
              } finally {
                 dbSess.close(); 
              }
             return clienttype;
         }
                    //-----------end dao's fetchClientType --------------------//
}
