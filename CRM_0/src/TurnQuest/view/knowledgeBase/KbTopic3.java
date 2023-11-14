package TurnQuest.view.knowledgeBase;


import java.util.List;

public class KbTopic3 {
    private String kbtId;
    private String kbtOrder;
    private String kbtShrtDesc;
    private String kbtDesc;
    private String kbtParentId;
    private String nodeType;
    private String nodeName;
    private List<KbTopic4> subKbTopics;


    public void setKbtId(String kbtId) {
        this.kbtId = kbtId;
    }

    public String getKbtId() {
        return kbtId;
    }

    public void setKbtOrder(String kbtOrder) {
        this.kbtOrder = kbtOrder;
    }

    public String getKbtOrder() {
        return kbtOrder;
    }

    public void setKbtShrtDesc(String kbtShrtDesc) {
        this.kbtShrtDesc = kbtShrtDesc;
    }

    public String getKbtShrtDesc() {
        return kbtShrtDesc;
    }

    public void setKbtDesc(String kbtDesc) {
        this.kbtDesc = kbtDesc;
    }

    public String getKbtDesc() {
        return kbtDesc;
    }

    public void setKbtParentId(String kbtParentId) {
        this.kbtParentId = kbtParentId;
    }

    public String getKbtParentId() {
        return kbtParentId;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setSubKbTopics(List<KbTopic4> subKbTopics) {
        this.subKbTopics = subKbTopics;
    }

    public List<KbTopic4> getSubKbTopics() {
        return subKbTopics;
    }
}
