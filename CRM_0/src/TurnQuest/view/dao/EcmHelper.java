package TurnQuest.view.dao;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.math.BigInteger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Policy;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.commons.dbutils.DbUtils;


public class EcmHelper {
    private String folder;


    private Session createSession(String user, String password, String url) {


        Session session = null;
        Map<String, String> params = new HashMap<String, String>();
        params.put(SessionParameter.USER, user);
        params.put(SessionParameter.PASSWORD, password);
        params.put(SessionParameter.ATOMPUB_URL, url);
        params.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        params.put(SessionParameter.OBJECT_FACTORY_CLASS,
                   "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");
        params.put(SessionParameter.CONNECT_TIMEOUT, "1000");
        params.put(SessionParameter.READ_TIMEOUT, "60000000");
        SessionFactory factory = SessionFactoryImpl.newInstance();
        session = factory.getRepositories(params).get(0).createSession();
        ;
        session.getDefaultContext().setCacheEnabled(false);
        return session;
    }

    public Session getCmisSession() {
        Session session = null;
        Connection conn = null;
        String url = "";
        String user = "";
        String pass = "";
        DBConnector datahandler = new DBConnector();
        conn = datahandler.getDatabaseConnection();
        CallableStatement cstSections = null;
        ResultSet sectionsRS = null;

        try {
            cstSections =
                    conn.prepareCall("begin ? := TQC_SETUPS_CURSOR.get_system_ecm_setups(?); end;");
            cstSections.registerOutParameter(1,
                                             oracle.jdbc.OracleTypes.CURSOR);
            cstSections.setInt(2, 27);
            cstSections.execute();
            sectionsRS = (ResultSet)cstSections.getObject(1);
            if (sectionsRS.next()) {
                url = sectionsRS.getString(2);
                user = sectionsRS.getString(3);
                pass = sectionsRS.getString(4);
                setFolder(sectionsRS.getString(6));
                if (url.endsWith("/")) {
                    url = url + "cmisatom";
                } else
                    url = url + "/cmisatom";

            }
            System.out.println("Url is " + url);

            DbUtils.closeQuietly(conn, cstSections, sectionsRS);
            session = createSession(user, pass, url);


        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof SQLException)
                GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cstSections, sectionsRS);
        }
        return session;
    }

    public List<String> getMetadata(String code) {
        List<String> metadata = new ArrayList<String>();
        DBConnector datahandler = new DBConnector();
        Connection conn = null;
        conn = datahandler.getDatabaseConnection();

        CallableStatement cstSections = null;
        ResultSet sectionsRS = null;
        try {
            cstSections =
                    conn.prepareCall("begin ? := TQC_SETUPS_CURSOR.get_content_metadata(?); end;");
            cstSections.registerOutParameter(1,
                                             oracle.jdbc.OracleTypes.CURSOR);
            cstSections.setString(2, code);
            cstSections.execute();
            sectionsRS = (ResultSet)cstSections.getObject(1);
            while (sectionsRS.next()) {
                metadata.add(sectionsRS.getString(3));
            }
            DbUtils.closeQuietly(conn, cstSections, sectionsRS);


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cstSections, sectionsRS);
        }
        return metadata;
    }


    public Map<String, String> getContentTypes(String sprcode) {
        Map<String, String> contentTypes = new HashMap<String, String>();
        DBConnector datahandler = new DBConnector();
        Connection conn = null;
        conn = datahandler.getDatabaseConnection();

        CallableStatement cstSections = null;
        ResultSet sectionsRS = null;
        try {
            cstSections =
                    conn.prepareCall("begin ? := TQC_SETUPS_CURSOR.get_ecm_doc_types(?); end;");
            cstSections.registerOutParameter(1,
                                             oracle.jdbc.OracleTypes.CURSOR);
            cstSections.setString(2, sprcode);
            cstSections.execute();
            sectionsRS = (ResultSet)cstSections.getObject(1);
            while (sectionsRS.next()) {
                contentTypes.put(sectionsRS.getString(1),
                                 sectionsRS.getString(3));
            }
            DbUtils.closeQuietly(conn, cstSections, sectionsRS);


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cstSections, sectionsRS);
        }
        return contentTypes;
    }

    public List<EcmHelperBean> getReports(String area) {
        List<EcmHelperBean> reports = new ArrayList<EcmHelperBean>();
        DBConnector datahandler = new DBConnector();
        Connection conn = null;
        conn = datahandler.getDatabaseConnection();

        CallableStatement cstSections = null;
        ResultSet sectionsRS = null;
        try {
            cstSections =
                    conn.prepareCall("begin ? := TQC_SETUPS_CURSOR.get_process_report(?,?); end;");
            cstSections.registerOutParameter(1,
                                             oracle.jdbc.OracleTypes.CURSOR);
            cstSections.setString(2, area);
            cstSections.setInt(3, 37);
            cstSections.execute();
            sectionsRS = (ResultSet)cstSections.getObject(1);
            while (sectionsRS.next()) {
                reports.add(new EcmHelperBean(sectionsRS.getString(2),
                                              sectionsRS.getString(1),
                                              sectionsRS.getString(5),
                                              "P:" + sectionsRS.getString(8),
                                              "D:" + sectionsRS.getString(10),
                                              sectionsRS.getString(9)));
            }
            DbUtils.closeQuietly(conn, cstSections, sectionsRS);


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cstSections, sectionsRS);
        }
        return reports;

    }


    private Document uploadFile(Session session, Folder folder,
                                String fileName, String mimetype,
                                File file) throws Exception {
        Map docProps = new HashMap();
        docProps.put("cmis:name", fileName);
        docProps.put("cmis:objectTypeId", "cmis:document");
        ContentStream contentStream =
            new ContentStreamImpl(fileName, BigInteger.valueOf(file.length()),
                                  mimetype, new FileInputStream(file));
        Document newDocument =
            folder.createDocument(docProps, contentStream, VersioningState.MAJOR,
                                  null, null, null,
                                  session.getDefaultContext());
        return newDocument;
    }


    private Folder createFolder(Session session, Folder ParentFolder,
                                String name) {
        Map<String, String> newFolderProps = new HashMap<String, String>();
        newFolderProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
        newFolderProps.put(PropertyIds.NAME, name);
        Folder child = ParentFolder.createFolder(newFolderProps);
        return child;
    }


    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }


    private File copyFile(String originalFile, String destinationfile) {
        InputStream inStream = null;
        OutputStream outStream = null;
        File bfile = null;
        try {

            File afile = new File(originalFile);
            bfile = new File(destinationfile);

            inStream = new FileInputStream(afile);
            outStream = new FileOutputStream(bfile);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0) { 
                outStream.write(buffer, 0, length); 
            }

            inStream.close();
            outStream.close();

            System.out.println("File is copied successful!");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bfile;

    }


    public String UploadFile(Session session, Folder ParentFolder,
                             String FileNameInDms, File file,
                             String FileMimeType, String aspectTypes,
                             List<TurnQuest.view.dao.EcmProps> props) throws FileNotFoundException {


        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(PropertyIds.OBJECT_TYPE_ID, aspectTypes);
        properties.put(PropertyIds.NAME, FileNameInDms);
        for (TurnQuest.view.dao.EcmProps property : props)
            properties.put(property.getName(), property.getValue());
        List<Ace> addAces = new LinkedList<Ace>();
        List<Ace> removeAces = new LinkedList<Ace>();
        List<Policy> policies = new LinkedList<Policy>();
        ContentStream contentStream;
        contentStream =
                new ContentStreamImpl(FileNameInDms, BigInteger.valueOf(file.length()),
                                      FileMimeType, new FileInputStream(file));
        Document newDocument =
            ParentFolder.createDocument(properties, contentStream,
                                        VersioningState.MAJOR, policies,
                                        addAces, removeAces,
                                        session.getDefaultContext());
        return (newDocument.getId());
    }
}

