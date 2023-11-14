/*
* Copyright (c) 2010 TurnKey Africa Ltd. All Rights Reserved.
*
* This software is the confidential and proprietary information of TurnKey
* Africa Ltd. ("Confidential Information"). You shall not disclose such
* Confidential Information and shall use it only in accordance with the terms
* of the license agreement you entered into with TurnKey Africa Ltd.
*
* TURNKEY AFRICA MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY
* OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
* TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
* PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TURNKEY AFRICA SHALL NOT BE LIABLE
* FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
* DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
*/

package TurnQuest.view.dao;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.HibernateUtil;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.ExchangeRate;
import TurnQuest.view.setups.Currency;
import TurnQuest.view.setups.CurrencyDenomination;
import TurnQuest.view.setups.CurrencyRate;

import java.math.BigDecimal;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class CurrencyDAO {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public CurrencyDAO() {
        super();
    }


    public List<Currency> fetchBaseCurrencies() {

        List<Currency> currenciesList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_base_currency(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            currenciesList = new ArrayList<Currency>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                Currency currency = new Currency();
                currency.setCode(resultSet.getString(1));
                currency.setSymbol(resultSet.getString(2));
                currency.setDescription(resultSet.getString(3));
                currency.setRound(resultSet.getString(4));
                currency.setCurNumWord(resultSet.getString(5));
                currency.setCurDecimalWord(resultSet.getString(6));
                currenciesList.add(currency);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return currenciesList;
    }

    /**
     * Fetches all <code>Currency</code> objects/records from the database
     * and returns them in a list.
     *
     * @return A list of <code>Currency</code> objects/records fetched from
     * the database.
     */
    public List<Currency> fetchAllCurrencies() {

        List<Currency> currenciesList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.currencies(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            currenciesList = new ArrayList<Currency>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                Currency currency = new Currency();
                currency.setCode(resultSet.getString(1));
                currency.setSymbol(resultSet.getString(2));
                currency.setDescription(resultSet.getString(3));
                currency.setRound(resultSet.getString(4));
                currency.setCurNumWord(resultSet.getString(5));
                currency.setCurDecimalWord(resultSet.getString(6));
                currenciesList.add(currency);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return currenciesList;
    }

    /**
     * Fetches all <code>CurrencyDenomination</code> objects/records that
     * belong to a given currency from the database and returns them in a list.
     *
     * @return A list of <code>CurrencyDenomination</code> objects/records
     * fetched from the database.
     */
    public List<CurrencyDenomination> fetchCurrencyDenominationsByCurrency() {

        List<CurrencyDenomination> denominationsList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_curr_denominations_by_code(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            denominationsList = new ArrayList<CurrencyDenomination>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("currencyCode") != null) {
                statement.setString(1,
                                    session.getAttribute("currencyCode").toString());

                statement.registerOutParameter(2, OracleTypes.CURSOR);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(2);

                while (resultSet.next()) {
                    CurrencyDenomination currDenomination =
                        new CurrencyDenomination();
                    currDenomination.setCode(resultSet.getString(1));
                    currDenomination.setCurrencyCode(resultSet.getString(2));
                    currDenomination.setValue(resultSet.getString(3));
                    currDenomination.setName(resultSet.getString(4));
                    currDenomination.setWef(resultSet.getDate(5));
                    denominationsList.add(currDenomination);
                }
                resultSet.close();
                statement.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return denominationsList;
    }

    /**
     * Fetches all <code>CurrencyRate</code> objects/records that
     * belong to a given currency from the database and returns them in a list.
     *
     * @return A list of <code>CurrencyRate</code> objects/records
     * fetched from the database.
     *
     public List<CurrencyRate> fetchCurrencyRatesByBaseCurrency() {

         List<CurrencyRate> ratesList = null;
         DBConnector dbConnector = new DBConnector();
         String query =
             "SELECT crt_code, \n" + 
             "        crt_cur_code, \n" + 
             "        crt_rate, \n" + 
             "        to_char (crt_date,'DD-MON-YYYY HH24:MI:SS') crt_date,\n" + 
             "        crt_base_cur_code, \n" + 
             "        cur_desc,\n" + 
             "        to_char (crt_wef,'DD-MON-YYYY HH24:MI:SS') crt_wef,\n" + 
             "        to_char (crt_wet,'DD-MON-YYYY HH24:MI:SS') crt_wet\n" + 
             "     FROM tqc_currency_rates, tqc_currencies\n" + 
             "     WHERE crt_cur_code = cur_code\n" + 
             "     AND crt_base_cur_code = :v_cur_code\n" + 
              "     order by  crt_date desc";
         OracleCallableStatement statement = null;
         OracleConnection connection = null;

         try {
             ratesList = new ArrayList<CurrencyRate>(); 
             
             BigDecimal curCode=GlobalCC.checkBDNullValues(session.getAttribute("currencyCode"));

             if (curCode != null) {  
                 query=query.replaceAll(":v_cur_code", curCode.toString()); 
                 System.out.println("query: "+query);
                 connection = dbConnector.getDatabaseConnection();
                 statement = (OracleCallableStatement)connection.prepareCall(query);
                 OracleResultSet resultSet =
                     (OracleResultSet)statement.executeQuery(); 
                 while (resultSet.next()) {
                     CurrencyRate currRate = new CurrencyRate();
                     currRate.setCode(resultSet.getString(1));
                     currRate.setCurrencyCode(resultSet.getString(2));
                     currRate.setCurrRate(resultSet.getString(3));
                     currRate.setCurrDate(resultSet.getString(4));
                     currRate.setBaseCurrencyCode(resultSet.getString(5));
                     currRate.setCurrencyDesc(resultSet.getString(6));
                     currRate.setCrtWefDate(resultSet.getString(7));                   
                     currRate.setCrtWetDate(resultSet.getString(8));
                     ratesList.add(currRate);
                 }

                 resultSet.close();
                 statement.close();
                 connection.close();
             }

         } catch (Exception e) {
             GlobalCC.EXCEPTIONREPORTING(connection, e);
         }
         return ratesList;
     }
     */
    /* public List<CurrencyRate> fetchCurrencyRatesByBaseCurrency(){
                    List<ExchangeRate> exchangerates = null; 
            List<CurrencyRate> ratesList = null;
            Session dbSess = HibernateUtil.getSession();
            Transaction tx = null;
            BigDecimal curCode=GlobalCC.checkBDNullValues(session.getAttribute("currencyCode"));

             try {
                 if(curCode==null){
                     return null;
                 }
                 ratesList = new ArrayList<CurrencyRate>(); 
                 String query = "SELECT {s.*},{c.*} FROM tqc_currency_rates s LEFT JOIN tqc_currencies c ON (s.crt_base_cur_code = c.cur_code) WHERE s.crt_base_cur_code = "+curCode;
                 tx = dbSess.beginTransaction(); 
                 List<Object[]> rows  = dbSess.
                 createSQLQuery(query).
                 addEntity("s",TurnQuest.view.models.ExchangeRate.class).
                 addEntity("c",TurnQuest.view.models.Currency.class).
                 list();  
                    for(Object[] row : rows){ 
                        ExchangeRate val = (ExchangeRate) row[0]; 
                        Currency cur = (Currency) row[1]; 
                        System.out.println(val);  
                        CurrencyRate currRate = new CurrencyRate();
                        currRate.setCode(GlobalCC.checkNullValues(val.getCode()));
                        currRate.setCurrencyCode(GlobalCC.checkNullValues(val.getCurCode()));
                        currRate.setCurrRate(GlobalCC.checkNullValues(val.getRate()));
                        currRate.setCurrDate(GlobalCC.checkNullValues(val.getDate()));
                        currRate.setBaseCurrencyCode(GlobalCC.checkNullValues(val.getBaseCurCode()));
                        currRate.setCurrencyDesc(GlobalCC.checkNullValues(cur.getDescription()));
                        currRate.setCrtWefDate(GlobalCC.checkNullValues(val.getWef()));                   
                        currRate.setCrtWetDate(GlobalCC.checkNullValues(val.getWet()));
                    ratesList.add(currRate);
                }
                tx.commit();
             } catch (HibernateException e) {
                if (tx!=null) tx.rollback();
                GlobalCC.EXCEPTIONREPORTING(e);
             } finally {
                dbSess.close(); 
             }
            return ratesList;
        }*/
    public List<CurrencyRate> fetchCurrencyRatesByBaseCurrencyOld(){
                       List<ExchangeRate> exchangerates = null; 
               List<CurrencyRate> ratesList = null;
               Session dbSess = HibernateUtil.getSession();
               Transaction tx = null;
               BigDecimal curCode=GlobalCC.checkBDNullValues(session.getAttribute("currencyCode"));

                try {
                    if(curCode==null){
                        return null;
                    }
                    ratesList = new ArrayList<CurrencyRate>(); 
                    String query = "FROM ExchangeRate s WHERE s.baseCurCode = "+curCode;
                    tx = dbSess.beginTransaction(); 
                    exchangerates  = dbSess. createQuery(query).list();
                    
                    for (Iterator iterator = exchangerates.iterator(); iterator.hasNext();){
                           ExchangeRate val = (ExchangeRate) iterator.next() ;
                           TurnQuest.view.models.Currency cur = val.getCurrency(); 
                           System.out.println(val);  
                           CurrencyRate currRate = new CurrencyRate();
                           currRate.setCode(GlobalCC.checkNullValues(val.getCode()));
                           currRate.setCurrencyCode(GlobalCC.checkNullValues(val.getCurCode()));
                           currRate.setCurrRate(GlobalCC.checkNullValues(val.getRate()));
                           currRate.setCurrDate(GlobalCC.timeStampStr(val.getDate()));
                           currRate.setBaseCurrencyCode(GlobalCC.checkNullValues(val.getBaseCurCode()));
                           currRate.setCurrencyDesc(GlobalCC.checkNullValues(cur.getDesc()));
                           currRate.setCrtWefDate(GlobalCC.timeStampStr(val.getWef()) );                   
                           currRate.setCrtWetDate(GlobalCC.timeStampStr(val.getWet()));
                       ratesList.add(currRate);
                   }
                   tx.commit();
                } catch (HibernateException e) {
                   if (tx!=null) tx.rollback();
                   GlobalCC.EXCEPTIONREPORTING(e);
                } finally {
                   dbSess.close(); 
                }
               return ratesList;
           }

    /**
     * Fetches all <code>Currency</code> objects/records from the database
     * and returns them in a list.
     *
     * @return A list of <code>Currency</code> objects/records fetched from
     * the database.
     */
    public List<Currency> fetchCurrenciesExcludeCurrency() {


        List<Currency> currenciesList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_currencies_exclude_curr(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        if (session.getAttribute("currencyCode") != null) {
            try {
                currenciesList = new ArrayList<Currency>();
                connection = dbConnector.getDatabaseConnection();
                statement =
                        (OracleCallableStatement)connection.prepareCall(query);

                statement.setBigDecimal(1,
                                        new BigDecimal(session.getAttribute("currencyCode").toString()));
                statement.registerOutParameter(2, OracleTypes.CURSOR);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(2);

                while (resultSet.next()) {
                    Currency currency = new Currency();
                    currency.setCode(resultSet.getString(1));
                    currency.setSymbol(resultSet.getString(2));
                    currency.setDescription(resultSet.getString(3));
                    currency.setRound(resultSet.getString(4));
                    //currency.setDescription(resultSet.getString(5));
                    //currency.setRound(resultSet.getString(6));
                    currenciesList.add(currency);
                }

                resultSet.close();
                statement.close();
                connection.close();

            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
        }
        return currenciesList;
    }
    public String updateExchangeRate(ExchangeRate val){ 
           Session dbSess = HibernateUtil.getSession();
          Transaction tx = null; String success="fail";
           try { 
                 tx = dbSess.beginTransaction(); 
               
                 ExchangeRate item = new ExchangeRate();
               
                 item = (ExchangeRate)dbSess.get(ExchangeRate.class, val.getCode()); 
                 
                 item.setCode(val.getCode());
                 item.setCurCode(val.getCurCode());
                 item.setRate(val.getRate());
                 item.setDate(val.getDate());
                 item.setBaseCurCode(val.getBaseCurCode());
                 item.setWef(val.getWef());
                 item.setWet(val.getWet()); 
                 item.setUpdatedBy(val.getUpdatedBy()); 
                 item.setUpdatedDate(val.getUpdatedDate());
               
                 dbSess.update(item);  
               
                 tx.commit();
               
                 ADFUtils.findIterator("fetchCurrencyRatesByBaseCurrencyIterator").executeQuery();  
                 String message = "ExchangeRate Saved Successfully!";
                 GlobalCC.INFORMATIONREPORTING(message); 
                success="success";
           } catch (HibernateException e) {
              if (tx!=null) tx.rollback();
              GlobalCC.EXCEPTIONREPORTING(e); 
           } finally {
              dbSess.close(); 
           }
          return success;
      }
    public String addExchangeRate(ExchangeRate val){ 
           Session dbSess = HibernateUtil.getSession();
           Transaction tx = null; String success="fail";
           try { 
                 tx = dbSess.beginTransaction(); 
               
                 ExchangeRate item = new ExchangeRate(); 
                 
                 item.setCode(val.getCode());
                 item.setCurCode(val.getCurCode());
                 item.setRate(val.getRate());
                 item.setDate(val.getDate());
                 item.setBaseCurCode(val.getBaseCurCode());
                 item.setWef(val.getWef());
                 item.setWet(val.getWet());
                 item.setCreatedBy(val.getCreatedBy());
                 item.setUpdatedBy(val.getUpdatedBy());
                 item.setCreatedDate(val.getCreatedDate());
                 item.setUpdatedDate(val.getUpdatedDate());
               
                 dbSess.persist(val);  
               
                 tx.commit();
               
                 ADFUtils.findIterator("fetchCurrencyRatesByBaseCurrencyIterator").executeQuery();  
                 String message = "ExchangeRate Saved Successfully!";
                 GlobalCC.INFORMATIONREPORTING(message); 
                success="success";
           } catch (HibernateException e) {
              if (tx!=null) tx.rollback();
              GlobalCC.EXCEPTIONREPORTING(e); 
           } finally {
              dbSess.close(); 
           }
          return success;
      } 
    public List<CurrencyRate> fetchCurrencyRatesByBaseCurrency() {

        List<CurrencyRate> ratesList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
        "SELECT STGCR_CODE crt_code, \n" + 
        "        STGCR_CUR_CODE crt_cur_code, \n" + 
        "        STGCR_RATE crt_rate, \n" + 
        "        to_char (STGCR_CREATED_DATE,'DD-MON-YYYY HH24:MI:SS') crt_date,\n" + 
        "        STGCR_BASE_CUR_CODE crt_base_cur_code, \n" + 
        "        cur_desc,\n" + 
        "        to_char (STGCR_WEF,'DD-MON-YYYY HH24:MI:SS') crt_wef,\n" + 
        "        to_char (STGCR_WET,'DD-MON-YYYY HH24:MI:SS') crt_wet,\n" +         
        "        to_char (STGCR_CREATED_DATE,'DD-MON-YYYY HH24:MI:SS') CRT_CREATED_DATE,\n" + 
        "        (SELECT USR_USERNAME FROM TQC_USERS WHERE USR_USERNAME=STGCR_CREATED_BY OR TO_CHAR(USR_CODE)=STGCR_CREATED_BY) CRT_CREATED_BY,\n" +        
        "        to_char (STGCR_AUTHORIZED_DATE,'DD-MON-YYYY HH24:MI:SS') crt_updated_date,\n" + 
        "        STGCR_AUTHORIZED_BY crt_updated_by,\n" +
        "        STGCR_AUTHORIZED crt_authorized\n" +
        "     FROM TQC_STAGED_CURRENCY_RATES, tqc_currencies\n" + 
        "     WHERE STGCR_CUR_CODE = cur_code\n" + 
        "     AND STGCR_BASE_CUR_CODE = :v_cur_code\n"+
        "     order by  STGCR_CREATED_DATE desc";
        
        
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            ratesList = new ArrayList<CurrencyRate>(); 
            
            BigDecimal curCode=GlobalCC.checkBDNullValues(session.getAttribute("currencyCode"));

            if (curCode != null) {  
                query=query.replaceAll(":v_cur_code", curCode.toString()); 
                System.out.println("query: "+query);
                connection = dbConnector.getDatabaseConnection();
                statement = (OracleCallableStatement)connection.prepareCall(query);
                OracleResultSet resultSet =
                    (OracleResultSet)statement.executeQuery(); 
                while (resultSet.next()) {
                    CurrencyRate currRate = new CurrencyRate();
                    currRate.setCode(resultSet.getString(1));
                    currRate.setCurrencyCode(resultSet.getString(2));
                    currRate.setCurrRate(resultSet.getString(3));
                    currRate.setCurrDate(resultSet.getString(4));
                    currRate.setBaseCurrencyCode(resultSet.getString(5));
                    currRate.setCurrencyDesc(resultSet.getString(6));
                    currRate.setCrtWefDate(resultSet.getString(7));                   
                    currRate.setCrtWetDate(resultSet.getString(8));
                    currRate.setCrtCreatedDate(resultSet.getString(9));
                    currRate.setCrtCreatedBy(resultSet.getString(10));
                    currRate.setCrtUpdatedDate(resultSet.getString(11));            
                    currRate.setCrtUpdatedBy(resultSet.getString(12));   
                    currRate.setCrtAuthorized(resultSet.getString(13));
                                
                    ratesList.add(currRate);
                }

                resultSet.close();
                statement.close();
                connection.close();
            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return ratesList;
    }
}
