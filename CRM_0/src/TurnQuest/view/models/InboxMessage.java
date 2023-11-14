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

package TurnQuest.view.models;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;


public class InboxMessage implements Serializable {

    private BigDecimal ibxCode;
    private BigDecimal ibxSysCode;
    private String ibxSysModule;
    private String ibxTelNo;
    private String ibxMessage;
    private String ibxStatus;
    private Date ibxDate;
    private Date ibxAssignedDate;
    private String ibxMessageReplied;
    private String ibxUserInformed;
    private String ibxAssignedTo;

    public InboxMessage() {
        super();
    }

    public void setIbxCode(BigDecimal ibxCode) {
        this.ibxCode = ibxCode;
    }

    public BigDecimal getIbxCode() {
        return ibxCode;
    }

    public void setIbxSysCode(BigDecimal ibxSysCode) {
        this.ibxSysCode = ibxSysCode;
    }

    public BigDecimal getIbxSysCode() {
        return ibxSysCode;
    }

    public void setIbxSysModule(String ibxSysModule) {
        this.ibxSysModule = ibxSysModule;
    }

    public String getIbxSysModule() {
        return ibxSysModule;
    }

    public void setIbxTelNo(String ibxTelNo) {
        this.ibxTelNo = ibxTelNo;
    }

    public String getIbxTelNo() {
        return ibxTelNo;
    }

    public void setIbxMessage(String ibxMessage) {
        this.ibxMessage = ibxMessage;
    }

    public String getIbxMessage() {
        return ibxMessage;
    }

    public void setIbxStatus(String ibxStatus) {
        this.ibxStatus = ibxStatus;
    }

    public String getIbxStatus() {
        return ibxStatus;
    }

    public void setIbxDate(Date ibxDate) {
        this.ibxDate = ibxDate;
    }

    public Date getIbxDate() {
        return ibxDate;
    }

    public void setIbxAssignedDate(Date ibxAssignedDate) {
        this.ibxAssignedDate = ibxAssignedDate;
    }

    public Date getIbxAssignedDate() {
        return ibxAssignedDate;
    }

    public void setIbxMessageReplied(String ibxMessageReplied) {
        this.ibxMessageReplied = ibxMessageReplied;
    }

    public String getIbxMessageReplied() {
        return ibxMessageReplied;
    }

    public void setIbxUserInformed(String ibxUserInformed) {
        this.ibxUserInformed = ibxUserInformed;
    }

    public String getIbxUserInformed() {
        return ibxUserInformed;
    }

    public void setIbxAssignedTo(String ibxAssignedTo) {
        this.ibxAssignedTo = ibxAssignedTo;
    }

    public String getIbxAssignedTo() {
        return ibxAssignedTo;
    }
}
