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


public class DirectDebitReport implements Serializable {

    private BigDecimal ddrCode;
    private String ddrReportDesc;


    private BigDecimal dfrCode;
    private String dfrFailedRemarks;
    private String dfrApplicationLevel;

    public DirectDebitReport() {
        super();
    }

    public void setDdrCode(BigDecimal ddrCode) {
        this.ddrCode = ddrCode;
    }

    public BigDecimal getDdrCode() {
        return ddrCode;
    }

    public void setDdrReportDesc(String ddrReportDesc) {
        this.ddrReportDesc = ddrReportDesc;
    }

    public String getDdrReportDesc() {
        return ddrReportDesc;
    }

    public void setDfrCode(BigDecimal dfrCode) {
        this.dfrCode = dfrCode;
    }

    public BigDecimal getDfrCode() {
        return dfrCode;
    }

    public void setDfrFailedRemarks(String dfrFailedRemarks) {
        this.dfrFailedRemarks = dfrFailedRemarks;
    }

    public String getDfrFailedRemarks() {
        return dfrFailedRemarks;
    }

    public void setDfrApplicationLevel(String dfrApplicationLevel) {
        this.dfrApplicationLevel = dfrApplicationLevel;
    }

    public String getDfrApplicationLevel() {
        return dfrApplicationLevel;
    }
}
