package TurnQuest.view.knowledgeBase;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;


public class KBDAO {
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public List<KbTopic1> findKbTopics() {

        List<KbTopic1> kbTopicsList1 = new ArrayList<KbTopic1>();


        DBConnector dbHandler = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;
        conn = dbHandler.getDatabaseConnection();
        String query1 = " begin ? := TQC_SERVICE_REQUESTS.getRootTopics; end;";
        String query2 =
            "begin ? := TQC_SERVICE_REQUESTS.getChildTopics(?); end;";
        try {
            cst = (OracleCallableStatement)conn.prepareCall(query1);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.execute();
            OracleResultSet rs1 = (OracleResultSet)cst.getObject(1);
            while (rs1.next()) {
                KbTopic1 kbTopic1 = new KbTopic1();
                kbTopic1.setKbtId(rs1.getString(1));
                kbTopic1.setKbtOrder(rs1.getString(2));
                kbTopic1.setKbtShrtDesc(rs1.getString(3));
                kbTopic1.setKbtDesc(rs1.getString(4));
                kbTopic1.setKbtParentId(rs1.getString(5));
                kbTopic1.setNodeType("LEVEL1");
                kbTopic1.setNodeName(rs1.getString(3));

                cst = (OracleCallableStatement)conn.prepareCall(query2);

                cst.setString(2, kbTopic1.getKbtId());
                cst.registerOutParameter(1, OracleTypes.CURSOR);
                cst.execute();
                OracleResultSet rs2 = (OracleResultSet)cst.getObject(1);
                List<KbTopic2> kbTopicsList2 = new ArrayList<KbTopic2>();
                while (rs2.next()) {
                    KbTopic2 kbTopic2 = new KbTopic2();
                    kbTopic2.setKbtId(rs2.getString(1));
                    kbTopic2.setKbtOrder(rs2.getString(2));
                    kbTopic2.setKbtShrtDesc(rs2.getString(3));
                    kbTopic2.setKbtDesc(rs2.getString(4));
                    kbTopic2.setKbtParentId(rs2.getString(5));
                    kbTopic2.setNodeType("LEVEL2");
                    kbTopic2.setNodeName(rs2.getString(3));
                    cst = (OracleCallableStatement)conn.prepareCall(query2);
                    cst.setString(2, kbTopic2.getKbtId());
                    cst.registerOutParameter(1, OracleTypes.CURSOR);
                    cst.execute();
                    OracleResultSet rs3 = (OracleResultSet)cst.getObject(1);
                    List<KbTopic3> kbTopicsList3 = new ArrayList<KbTopic3>();
                    while (rs3.next()) {
                        KbTopic3 kbTopic3 = new KbTopic3();
                        kbTopic3.setKbtId(rs3.getString(1));
                        kbTopic3.setKbtOrder(rs3.getString(2));
                        kbTopic3.setKbtShrtDesc(rs3.getString(3));
                        kbTopic3.setKbtDesc(rs3.getString(4));
                        kbTopic3.setKbtParentId(rs3.getString(5));
                        kbTopic3.setNodeType("LEVEL3");
                        kbTopic3.setNodeName(rs3.getString(3));

                        cst =
(OracleCallableStatement)conn.prepareCall(query2);
                        cst.setString(2, kbTopic3.getKbtId());
                        cst.registerOutParameter(1, OracleTypes.CURSOR);
                        cst.execute();
                        OracleResultSet rs4 =
                            (OracleResultSet)cst.getObject(1);
                        List<KbTopic4> kbTopicsList4 =
                            new ArrayList<KbTopic4>();
                        while (rs4.next()) {
                            KbTopic4 kbTopic4 = new KbTopic4();
                            kbTopic4.setKbtId(rs4.getString(1));
                            kbTopic4.setKbtOrder(rs4.getString(2));
                            kbTopic4.setKbtShrtDesc(rs4.getString(3));
                            kbTopic4.setKbtDesc(rs4.getString(4));
                            kbTopic4.setKbtParentId(rs4.getString(5));
                            kbTopic4.setNodeType("LEVEL4");
                            kbTopic4.setNodeName(rs4.getString(3));
                            kbTopicsList4.add(kbTopic4);
                        }
                        rs4.close();
                        kbTopic3.setSubKbTopics(kbTopicsList4);
                        kbTopicsList3.add(kbTopic3);
                    }
                    rs3.close();
                    kbTopic2.setSubKbTopics(kbTopicsList3);
                    kbTopicsList2.add(kbTopic2);
                }

                rs2.close();
                kbTopic1.setSubKbTopics(kbTopicsList2);
                kbTopicsList1.add(kbTopic1);
            }


            rs1.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return kbTopicsList1;
    }

    public List<KbContent> findKbContentPerTopic() {
        String kbId = (String)session.getAttribute("KBID");
        List<KbContent> kbContentList = new ArrayList<KbContent>();
        DBConnector dbHandler = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;
        conn = dbHandler.getDatabaseConnection();
        String query = "begin ? := TQC_SERVICE_REQUESTS.getKbContent(?); end;";
        try {
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setString(2, kbId);
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                KbContent kbContent = new KbContent();
                kbContent.setKbcId(rs.getString(1));
                kbContent.setKbtId(rs.getString(2));
                kbContent.setKbcOrder(rs.getString(3));
                kbContent.setKbcContent(rs.getString(4));
                kbContentList.add(kbContent);
            }
            rs.close();
            cst.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return kbContentList;
    }
}
