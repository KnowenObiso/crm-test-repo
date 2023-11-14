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

package TurnQuest.view.setups;

import java.io.Serializable;

/**
 * The CurrencyDenomination Bean. Includes properties and methods that map to a
 * given CurrencyDenomination.
 *
 * @author Frankline Ogongi
 */
public class Currency implements Serializable {

    /* Properties*/
    private String code;
    private String symbol;
    private String description;
    private String round;
    private String curNumWord;
    private String curDecimalWord;
    private String numWords;
    private String decimalWord;

    /**
     * Default Constructor
     */
    public Currency() {
        super();
    }

    /* Methods */

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getRound() {
        return round;
    }

    public void setCurNumWord(String curNumWord) {
        this.curNumWord = curNumWord;
    }

    public String getCurNumWord() {
        return curNumWord;
    }

    public void setCurDecimalWord(String curDecimalWord) {
        this.curDecimalWord = curDecimalWord;
    }

    public String getCurDecimalWord() {
        return curDecimalWord;
    }

    public void setNumWords(String numWords) {
        this.numWords = numWords;
    }

    public String getNumWords() {
        return numWords;
    }

    public void setDecimalWord(String decimalWord) {
        this.decimalWord = decimalWord;
    }

    public String getDecimalWord() {
        return decimalWord;
    }
}
