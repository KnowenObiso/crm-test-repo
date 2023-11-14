package TurnQuest.view.ecm;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.reports.XMLPublisher;

import java.io.File;

import java.math.BigDecimal;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletResponse;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.alfresco.cmis.client.AlfrescoDocument;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Session;

import org.turnkey.cmis.CmisUtil;


/*
 * Author James Mbugua
 */
public class EcmUtil {
    public EcmUtil() {
        super();
    }

    CmisUtil cmisUtil;

    /*
 * Authentification
 * get the userName
 *          password
 *          url
 * as per a system
 * then creates a session using
 * those credentials
 */

    public Session Authentification() {
        DBConnector ecmDatahandler = null;
        ecmDatahandler = new DBConnector();
        OracleConnection ecmConn = null;
        ecmConn = ecmDatahandler.getDatabaseConnection();
        OracleCallableStatement ecmCst = null;
        double systemCode = 27;
        BigDecimal sysCode = new BigDecimal(systemCode);
        String url = null;
        String username = null;
        String passWord = null;
        Session ecmSession = null;

        try {
            String query =
                "begin ? :=  TQC_SETUPS_CURSOR.get_system_ecm_setups(?); end;";
            ecmCst = (OracleCallableStatement)ecmConn.prepareCall(query);
            ecmCst.registerOutParameter(1, OracleTypes.CURSOR);
            ecmCst.setBigDecimal(2, sysCode);
            ecmCst.execute();
            OracleResultSet rs = (OracleResultSet)ecmCst.getObject(1);
            cmisUtil = new CmisUtil();

            while (rs.next()) {
                url = rs.getString(2);
                username = rs.getString(3);
                passWord = rs.getString(4);

            }
            rs.close();
            ecmCst.close();
            ecmConn.commit();
            ecmConn.close();
            url = url + "/cmisatom";
            ecmSession = cmisUtil.createSession(username, passWord, url);

            String ecmsession = ecmSession.toString();
            if (ecmsession == null) {
                GlobalCC.errorValueNotEntered("Please Check alfresco connection : Can't Connect to alfresco!!");
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }

        return ecmSession;
    }
    /*
 * @checkDir
 * @param ecSession
 *            current ecm session
 * @param filePath
 *            Actual file
 * Check if a directory exists in DMS
 * return true if it exists
 * else false
 */

    public boolean checkDir(Session ecmSession, String filePath) {
        //        Folder root = ecmSession.getRootFolder();
        boolean check = false;
        try {
            cmisUtil = new CmisUtil();
            check = cmisUtil.checkIfDirectoryExists(ecmSession, filePath);
            //new CmisUtil().checkIfDirectoryExists(ecmSession, filePath);
        } catch (Exception ex) {
            System.out.println("Either the ecm connection failed or invalid file path: " +
                               filePath);
        }
        return check;
    }
    /*
 * reportFile
 * @param reportCode
 * return the report File
 */

    public File reportFile(BigDecimal rptCode) {

        XMLPublisher xmlPublisher = new XMLPublisher();

        String templateFile = null;
        String styleFile = null;
        String reportName = null;
        String dataFile = null;
        File ecmFile = null;
        String reportFormat = "pdf";

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;
        if (rptCode.toString().equalsIgnoreCase("0")) {
            GlobalCC.errorValueNotEntered("Report Not Attached");
            return null;
        }
        System.out.println(" JAYMO " + rptCode);
        String jobquery = "begin ? := tqc_web_cursor.getRptDetails(?); end;";

        try {
            conn = dbConnector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(jobquery);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2, rptCode);
            cst.executeQuery();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            while (rs.next()) {
                templateFile = rs.getString(5);
                styleFile = rs.getString(7);
                reportName = rs.getString(2);
                dataFile = rs.getString(3);

                System.out.println(reportName);
            }
            xmlPublisher.dataEngine(reportName, dataFile, conn);
            ecmFile =
                    xmlPublisher.FileProcessorEngine(reportFormat, templateFile,
                                                     styleFile, reportName);

            if (ecmFile == null) {
                return null;
            }

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext =
                facesContext.getExternalContext();
            HttpServletResponse response =
                (HttpServletResponse)externalContext.getResponse();
            String filename = null;
            filename = reportName;
            filename = filename.replace(" ", "_");
            String output = null;
            if (reportFormat == null || reportFormat.equals("pdf")) {
                output = filename + ".pdf";
                response.setContentType("application/pdf");
            } else {
                output = filename + "." + reportFormat;
                response.setContentType("application/octet-stream");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //GlobalCC.EXCEPTIONREPORTING(null, e);
        }

        return ecmFile;
    }

    /*
 * createDirInRootFolder
  * @param ecmSession
  *            current ecm session
  * @param newDir
  *          new folder to create
 * create a directory in the root directory(Company Home)
 */

    public void createDirInRootFolder(Session ecmSession, String newDir) {
        try {
            cmisUtil = new CmisUtil();
            cmisUtil.createFolderInRootFolder(ecmSession, newDir);
        } catch (Exception e) {
            System.out.println("Enable to Create Parent/Root Directory");
        }
    }

    /*
 * createDir
 * @param ecmSession
 *            current ecm session
 * @param parentDir
 *            is the parent folder
 * @param newDir
 *          new folder to create
 */

    public void createDir(Session ecmSession, Folder parentDir,
                          String newDir) {
        try {
            cmisUtil = new CmisUtil();
            cmisUtil.createFolder(ecmSession, parentDir, newDir);
        } catch (Exception ex) {
            System.out.println("Enable to Create Directory " + newDir);
        }
    }

    /*
 * upLoadFile
 * @param ecmSession
 *            current ecm session
 * @param folder
 *            is the parent folder
 * @param fileName
 *          the name to give to uploaded file
 * @param mimetype
 *          uploaded file mimetype
 * @param file
 *        Actual file to upload
 */

    public void upLoadFile(Session ecmSession, Folder folder, String fileName,
                           String mimetype, File file) {
        try {
            cmisUtil = new CmisUtil();
            cmisUtil.uploadFile(ecmSession, folder, fileName, mimetype, file);
        } catch (Exception e) {
            System.out.println("Enable to Upload File: " + file);
            //e.printStackTrace();
        }
    }

    /*
 * addAnAspectToDocument
 * @param aspect
 *          this is the doc aspect to add
 * @param fileName
 *          File to add aspect to
 * @param ecmSession
 *          Current ecm session
 */

    public AlfrescoDocument addAnAspectToDocument(String aspect,
                                                  String fileName,
                                                  Session ecmSession) {
        Document doc = null;
        AlfrescoDocument alfDoc = null;
        try {
            doc = (Document)ecmSession.getObjectByPath(fileName);
        } catch (Exception ex) {
            System.out.println("Could not get Document, Either connection failed or document doesn't exist :" +
                               fileName);
        }

        try {
            cmisUtil = new CmisUtil();
            alfDoc = cmisUtil.addAnAspectToDocument(doc, aspect);
        } catch (Exception ex) {
            System.out.println("Could not add Document aspect, Either connection failed or document doesn't exist :" +
                               alfDoc);
        }
        return alfDoc;
    }

    /*
   * updateMetadata
   * @param parentAspect
   *          aspect of parent dir
   * @param property
   *          metaData property
   * @param value
   *          metadata value to assign
   */

    public void updateMetadata(Document doc, String parentAspect,
                               String property, String value) {
        try {
            cmisUtil = new CmisUtil();
            cmisUtil.updateMetadata(doc, parentAspect, property, value);
        } catch (Exception ex) {
            System.out.println("Could not update Document metadata, Either connection failed or document doesn't exist :" +
                               doc);
        }
    }

    /*
     * Checks if DMS is Enabled
     * Return Y if enabled
     */

    public String checkIfDMSEnabled() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        String paramValue = null;
        try {
            String query =
                "SELECT param_value FROM TQC_PARAMETERS WHERE param_name = 'DMS_ENABLED'";

            conn = dbConnector.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            OracleResultSet rs = (OracleResultSet)stmt.executeQuery();
            while (rs.next()) {
                paramValue = rs.getString(1);
            }
            if (paramValue == null) {
                paramValue = "N";
            }
            rs.close();
            stmt.close();
            conn.commit();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return paramValue;
    }

    public static String removeExtension(String s) {

        String separator = System.getProperty("file.separator");
        String filename;

        // Remove the path upto the filename.
        int lastSeparatorIndex = s.lastIndexOf(separator);
        if (lastSeparatorIndex == -1) {
            filename = s;
        } else {
            filename = s.substring(lastSeparatorIndex + 1);
        }

        // Remove the extension.
        int extensionIndex = filename.lastIndexOf(".");
        if (extensionIndex == -1)
            return filename;

        return filename.substring(0, extensionIndex);
    }

    public static void printAspects(AlfrescoDocument alfDoc)
    {
        System.out.println("------------------------------------------");
        System.out.println("Aspects:");
        System.out.println("------------------------------------------");
        for (ObjectType aspect : alfDoc.getAspects())
        {
            System.out.println(aspect.getId());
        }
    }

    public static void printProperties(Document doc)
    {
        System.out.println("------------------------------------------");
        System.out.println("Properties:");
        System.out.println("------------------------------------------");
        for (Property<?> prop : doc.getProperties())
        {
            System.out.println(prop.getId() + ": " + prop.getValuesAsString().replace("[", "").replaceAll("]", ""));
        }
    }

    public static String getDocProperty(Document doc, String propertyName)
    {
        for (Property<?> prop : doc.getProperties())
        {
            if (prop.getId().equalsIgnoreCase(propertyName)) {
              return prop.getValuesAsString().replace("[", "").replaceAll("]", "");
            }
        }
        
        return null;
    }
    
    /*Demo procedure
    public static Session createSession()
    {
        SessionFactory f = SessionFactoryImpl.newInstance();
        Map<String, String> parameter = new HashMap<String, String>();
  
        // user credentials
        parameter.put(SessionParameter.USER, "admin");
        parameter.put(SessionParameter.PASSWORD, "admin");
  
        // connection settings
        // parameter.put(SessionParameter.ATOMPUB_URL,
        // "http://localhost:8080/alfresco/cmisatom");
        // parameter.put(SessionParameter.BINDING_TYPE,
        // BindingType.ATOMPUB.value());
  
        parameter.put(SessionParameter.WEBSERVICES_REPOSITORY_SERVICE,
                "http://localhost:8080/alfresco/cmisws/RepositoryService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_NAVIGATION_SERVICE,
                "http://localhost:8080/alfresco/cmisws/NavigationService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_OBJECT_SERVICE,
                "http://localhost:8080/alfresco/cmisws/ObjectService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_VERSIONING_SERVICE,
                "http://localhost:8080/alfresco/cmisws/VersioningService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_DISCOVERY_SERVICE,
                "http://localhost:8080/alfresco/cmis/DiscoveryService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_MULTIFILING_SERVICE,
                "http://localhost:8080/alfresco/cmisws/MultiFilingService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_RELATIONSHIP_SERVICE,
                "http://localhost:8080/alfresco/cmisws/RelationshipService?wsdl");
        parameter
                .put(SessionParameter.WEBSERVICES_ACL_SERVICE, "http://localhost:8080/alfresco/cmisws/ACLService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_POLICY_SERVICE,
                "http://localhost:8080/alfresco/cmisws/PolicyService?wsdl");
  
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.WEBSERVICES.value());
        parameter.put(SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");
  
        // create session
        return f.getRepositories(parameter).get(0).createSession();
    }
    */
    
    /*Demo procedure
    public static void alfrescoTest()
    {
        Session session = createSession();

        // create document
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(PropertyIds.NAME, "test1");
        properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document,P:cm:titled");
        properties.put("cm:description", "Beschreibung");

        Document doc = session.getRootFolder().createDocument(properties, null, null);

        printProperties(doc);

        // update properties
        properties.clear();
        properties.put(PropertyIds.NAME, "test2");
        properties.put("cm:description", "My Description");

        doc.updateProperties(properties);

        printProperties(doc);

        // get aspects
        AlfrescoDocument alfDoc = (AlfrescoDocument) doc;

        printAspects(alfDoc);

        // add aspect
        alfDoc.addAspect("P:cm:taggable");

        printAspects(alfDoc);

        // remove aspect
        alfDoc.removeAspect("P:cm:titled");

        printAspects(alfDoc);

        printProperties(doc);

        alfDoc.delete(true);
    }
    */

}
