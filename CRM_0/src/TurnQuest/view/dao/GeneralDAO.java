package TurnQuest.view.dao;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.PeriodBean;

import java.math.BigDecimal;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.enums.IncludeRelationships;


public class GeneralDAO {
    public GeneralDAO() {
        super();
    }

    /** Get a String (yyyy/mm/dd) and returns a java.sql.Date.
     * It is useful to get user input and insert it in
     * the database like MySQL.
     *
     * @param strDate a String in the format dd/mm/yyyy
     * @return java.sql.Date
     */
    public static java.sql.Date getSqlDate(String strDate) {
        String[] splitedDate = strDate.split("-");
        int year = Integer.parseInt(splitedDate[0]) - 1900;
        int month = Integer.parseInt(splitedDate[1]) - 1;
        int day = Integer.parseInt(splitedDate[2]);

        return new java.sql.Date(year, month, day);
    }

    public static PeriodBean getPeriod(BigDecimal branchCode, String period,
                                       String year) {

        List<PeriodBean> periodList = null;
        PeriodBean periodBean = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? :=  FMS_SL_CURSORS.get_specific_period(?,?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            periodList = new ArrayList<PeriodBean>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            // if (branchCode != null && !period.equals("") && !year.equals("")) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setString(2, period);
            statement.setBigDecimal(3, branchCode);
            statement.setString(4, year);
            statement.execute();

            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);


            while (resultSet.next()) {
                PeriodBean periodObj = new PeriodBean();
                periodObj.setPeriod(resultSet.getString(1));
                periodObj.setBranchCode(resultSet.getBigDecimal(2));
                periodObj.setYear(resultSet.getString(3));
                periodObj.setWefDate(resultSet.getDate(4));
                periodObj.setWetDate(resultSet.getDate(5));
                periodObj.setCounter(resultSet.getBigDecimal(6));
                periodObj.setState(resultSet.getString(7));
                periodObj.setStart(resultSet.getString(8));
                periodObj.setOrgCode(resultSet.getBigDecimal(9));
                periodObj.setCurrPeriod(resultSet.getBigDecimal(10));
                periodObj.setCurPeriod(resultSet.getBigDecimal(11));
                periodList.add(periodObj);
            }

            if (periodList.size() == 1) {
                periodBean = periodList.get(0);

            } else {
                periodBean = null;

            }

            statement.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return periodBean;
    }

    public static String getOrgName(BigDecimal orgCode) {
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_INTERFACES_PKG.ORGANIZATIONNAME(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        String orgName = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (orgCode != null) {
                statement.registerOutParameter(1, OracleTypes.VARCHAR);
                statement.setBigDecimal(2, orgCode);
                statement.setString(3, "N");
                statement.execute();

                orgName = statement.getString(1);

                statement.close();
                connection.close();
            } else {
                return null;
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return orgName;
    }


    public List<EcmBean> getEnquireDocs() {
        List<EcmBean> ecmBeans = new ArrayList<EcmBean>();
        HttpSession sessions =
            (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Session session = null;
        try {
            session = new EcmHelper().getCmisSession();

            session.getDefaultContext().setCacheEnabled(false);
            OperationContext context = session.createOperationContext();
            //  context.setRenditionFilterString("alf:icon16");
            context.setIncludeAllowableActions(true);
            context.setCacheEnabled(false);
            context.setIncludeRelationships(IncludeRelationships.BOTH);


            String polNo = sessions.getAttribute("ClientCode").toString();
            String query =
                "SELECT doc.*, iptc.* from cmis:document AS doc JOIN tqgib:clientsDocs   AS iptc ON doc.cmis:objectId = iptc.cmis:objectId where iptc.tqgib:ClientID ='" +
                polNo + "'";

            ItemIterable<QueryResult> q = session.query(query, false, context);
            for (QueryResult qr : q) {
                CmisObject item =
                    session.getObject(session.createObjectId((qr.getPropertyByQueryName("iptc.cmis:objectId").getFirstValue().toString())),
                                      context);


                System.out.println("Doc Name..." + item.getName());
                if (item instanceof Document) {

                    EcmBean ecm = new EcmBean();
                    ecm.setName(removeExtension(item.getName()));
                    ecm.setActualName(item.getName());
                    ecm.setModifiedBy(item.getLastModifiedBy());
                    ecm.setDateCreated(new SimpleDateFormat("dd/MM/yyyy").format(item.getCreationDate().getTime()));
                    ecm.setVersionLabel(((Document)item).getVersionLabel());
                    ecm.setId(item.getId());
                    ecm.setMimeType(((Document)item).getContentStreamMimeType());
                    ecmBeans.add(ecm);

                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //session=null;
        }
        return ecmBeans;
    }
    
    public List<EcmBean> getAgentDocs() {
        List<EcmBean> ecmBeans = new ArrayList<EcmBean>();
        HttpSession sessions =
            (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Session session = null;
        try {
            session = new EcmHelper().getCmisSession();

            session.getDefaultContext().setCacheEnabled(false);
            OperationContext context = session.createOperationContext();
            //  context.setRenditionFilterString("alf:icon16");
            context.setIncludeAllowableActions(true);
            context.setCacheEnabled(false);
            context.setIncludeRelationships(IncludeRelationships.BOTH);


            String polNo = sessions.getAttribute("agencyCode").toString();
            String query =
                "SELECT doc.*, iptc.* from cmis:document AS doc JOIN tqgib:agentsDocs  AS iptc ON doc.cmis:objectId = iptc.cmis:objectId where iptc.tqgib:AgentCode ='" +
                polNo + "'";

            ItemIterable<QueryResult> q = session.query(query, false, context);
            for (QueryResult qr : q) {
                CmisObject item =
                    session.getObject(session.createObjectId((qr.getPropertyByQueryName("iptc.cmis:objectId").getFirstValue().toString())),
                                      context);


                System.out.println("Doc Name..." + item.getName());
                if (item instanceof Document) {

                    EcmBean ecm = new EcmBean();
                    ecm.setName(removeExtension(item.getName()));
                    ecm.setActualName(item.getName());
                    ecm.setModifiedBy(item.getLastModifiedBy());
                    ecm.setDateCreated(new SimpleDateFormat("dd/MM/yyyy").format(item.getCreationDate().getTime()));
                    ecm.setVersionLabel(((Document)item).getVersionLabel());
                    ecm.setId(item.getId());
                    ecm.setMimeType(((Document)item).getContentStreamMimeType());
                    ecmBeans.add(ecm);

                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //session=null;
        }
        return ecmBeans;
    }
    public List<EcmBean> getSPDocs() {
        List<EcmBean> ecmBeans = new ArrayList<EcmBean>();
        HttpSession sessions =
            (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Session session = null;
    //        String serviceDeskDms = GlobalCC.checkNullValues(sessions.getAttribute("SERVICE_DESK_DMS_ENABLED"));
    //        if (serviceDeskDms == null){
    //                serviceDeskDms = "N";
    //            }
    //        System.out.println("serviceDeskDms "+serviceDeskDms);
    //        if (serviceDeskDms.equalsIgnoreCase("Y")){
         try {
            session = new EcmHelper().getCmisSession();

            session.getDefaultContext().setCacheEnabled(false);
            OperationContext context = session.createOperationContext();
            //  context.setRenditionFilterString("alf:icon16");
            context.setIncludeAllowableActions(true);
            context.setCacheEnabled(false);
            context.setIncludeRelationships(IncludeRelationships.BOTH);

            
            
            String servRef = sessions.getAttribute("serviceProviderCode").toString();
            String query =
                "SELECT doc.*, iptc.* from cmis:document AS doc JOIN tqgib:SPsDocs   AS iptc ON doc.cmis:objectId = iptc.cmis:objectId where iptc.tqgib:SPCode ='" +
                servRef + "'";

            ItemIterable<QueryResult> q = session.query(query, false, context);
            for (QueryResult qr : q) {
                CmisObject item =
                    session.getObject(session.createObjectId((qr.getPropertyByQueryName("iptc.cmis:objectId").getFirstValue().toString())),
                                      context);


                System.out.println("Doc Name..." + item.getName());
                if (item instanceof Document) {

                    EcmBean ecm = new EcmBean();
                    ecm.setName(removeExtension(item.getName()));
                    ecm.setActualName(item.getName());
                    ecm.setModifiedBy(item.getLastModifiedBy());
                    ecm.setDateCreated(new SimpleDateFormat("dd/MM/yyyy").format(item.getCreationDate().getTime()));
                    ecm.setVersionLabel(((Document)item).getVersionLabel());
                    ecm.setId(item.getId());
                    ecm.setMimeType(((Document)item).getContentStreamMimeType());
                    ecmBeans.add(ecm);

                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //session=null;
        }
    //        }else{
    //            return null;
    //        }
        return ecmBeans;
    }

    public List<EcmBean> getServiceRequestDocs() {
        List<EcmBean> ecmBeans = new ArrayList<EcmBean>();
        HttpSession sessions =
            (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Session session = null;
//        String serviceDeskDms = GlobalCC.checkNullValues(sessions.getAttribute("SERVICE_DESK_DMS_ENABLED"));
//        if (serviceDeskDms == null){
//                serviceDeskDms = "N";
//            }
//        System.out.println("serviceDeskDms "+serviceDeskDms);
//        if (serviceDeskDms.equalsIgnoreCase("Y")){
         try {
            session = new EcmHelper().getCmisSession();

            //session.getDefaultContext().setCacheEnabled(false);
            OperationContext context = session.createOperationContext();
            //  context.setRenditionFilterString("alf:icon16");
            context.setIncludeAllowableActions(true);
            context.setCacheEnabled(false);
            context.setIncludeRelationships(IncludeRelationships.BOTH);
            
            
            
            String servRef = sessions.getAttribute("reqRefNo").toString();
            String query =
                "SELECT doc.*, iptc.* from cmis:document AS doc JOIN tqgib:serviceRequestDocs   AS iptc ON doc.cmis:objectId = iptc.cmis:objectId where iptc.tqgib:serviceRefNo ='" +
                servRef + "'";

            ItemIterable<QueryResult> q = session.query(query, false, context);
            for (QueryResult qr : q) {
                CmisObject item =
                    session.getObject(session.createObjectId((qr.getPropertyByQueryName("iptc.cmis:objectId").getFirstValue().toString())),
                                      context);


                System.out.println("Doc Name..." + item.getName());
                if (item instanceof Document) {

                    EcmBean ecm = new EcmBean();
                    ecm.setName(removeExtension(item.getName()));
                    ecm.setActualName(item.getName());
                    ecm.setModifiedBy(item.getLastModifiedBy());
                    ecm.setDateCreated(new SimpleDateFormat("dd/MM/yyyy").format(item.getCreationDate().getTime()));
                    ecm.setVersionLabel(((Document)item).getVersionLabel());
                    ecm.setId(item.getId());
                    ecm.setMimeType(((Document)item).getContentStreamMimeType());
                    ecmBeans.add(ecm);

                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //session=null;
        }
//        }else{
//            return null;
//        }
        return ecmBeans;
    }
    private String removeExtension(String s) {

        String separator = System.getProperty("file.separator");
        String filename;

        // Remove the path upto the filename.
        int lastSeparatorIndex = s.lastIndexOf(separator);
        if (lastSeparatorIndex == -1) {
            filename = s;
        } else {
            filename = new String(s.substring(lastSeparatorIndex + 1));
        }

        // Remove the extension.
        int extensionIndex = filename.lastIndexOf(".");
        if (extensionIndex == -1)
            return filename;

        return new String(filename.substring(0, extensionIndex));
    }

}
