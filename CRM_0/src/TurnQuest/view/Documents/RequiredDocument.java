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

package TurnQuest.view.Documents;


import java.util.Date;


/**
 * The RequiredDocument Bean. Includes properties and methods that map to a
 * given RequiredDocument.
 *
 * @author Frankline Ogongi
 */
public class RequiredDocument {

    public String code;
    public String shortDesc;
    public String description;
    public String mandatory;
    public Date dateAdded;
    public String exempted;
    public String Account_type;
  
    public RequiredDocument() {
        super();
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public String getMandatory() {
        return mandatory;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setExempted(String exempted) {
        this.exempted = exempted;
    }

    public String getExempted() {
        return exempted;
    }

    public void setAccount_type(String Account_type) {
        this.Account_type = Account_type;
    }

    public String getAccount_type() {
        return Account_type;
    }
  
}
