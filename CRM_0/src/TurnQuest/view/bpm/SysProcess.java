package TurnQuest.view.bpm;

import java.math.BigDecimal;

public class SysProcess {
    public SysProcess() {
        super();
    }

    private String type;
    private String name;

    private BigDecimal sysCode;
    private String jpdlDesc;
    private BigDecimal deployment;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSysCode(BigDecimal sysCode) {
        this.sysCode = sysCode;
    }

    public BigDecimal getSysCode() {
        return sysCode;
    }

    public void setJpdlDesc(String jpdlDesc) {
        this.jpdlDesc = jpdlDesc;
    }

    public String getJpdlDesc() {
        return jpdlDesc;
    }

    public void setDeployment(BigDecimal deployment) {
        this.deployment = deployment;
    }

    public BigDecimal getDeployment() {
        return deployment;
    }
}
