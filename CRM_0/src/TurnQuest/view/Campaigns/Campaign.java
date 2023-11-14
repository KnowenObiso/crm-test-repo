package TurnQuest.view.Campaigns;

import java.math.BigDecimal;

import java.sql.Date;
import java.sql.Timestamp;


public class Campaign {
    private BigDecimal campCode;
    private BigDecimal campUserCode;
    private String campUserName;
    private BigDecimal campTeamUserCode;
    private String campTeamName;
    private Date campDate;
    private String campSponsor;
    private BigDecimal campOrgCode;
    private String campOrgName;
    private BigDecimal campProdCode;
    private String campProdName;
    private BigDecimal campSysCode;
    private String campSysName;
    private String campName;
    private String campType;
    private String campStatus;
    private Date campExpCloseDate;
    private String campTargetAudience;
    private BigDecimal campTargetSize;
    private String campNumSent;
    private BigDecimal campBudgetedCost;
    private BigDecimal campActualCost;
    private BigDecimal campExpCost;
    private BigDecimal campExpRevenue;
    private BigDecimal campExpSalesCount;
    private BigDecimal campActualSalesCount;
    private BigDecimal campActualResponseCount;
    private BigDecimal campExpResponseCount;
    private BigDecimal campExpROI;
    private BigDecimal campActualROI;
    private String campDescription;
    private BigDecimal currencyCode;
    private String currencyName;
    private String campObjective;
    private BigDecimal campImpressionCount;

    private String event;
    private String venue;
    private Timestamp eventTime;


    public void setCampSysName(String campSysName) {
        this.campSysName = campSysName;
    }

    public String getCampSysName() {
        return campSysName;
    }

    public void setCampCode(BigDecimal campCode) {
        this.campCode = campCode;
    }

    public BigDecimal getCampCode() {
        return campCode;
    }

    public void setCampUserCode(BigDecimal campUserCode) {
        this.campUserCode = campUserCode;
    }

    public BigDecimal getCampUserCode() {
        return campUserCode;
    }

    public void setCampUserName(String campUserName) {
        this.campUserName = campUserName;
    }

    public String getCampUserName() {
        return campUserName;
    }

    public void setCampTeamUserCode(BigDecimal campTeamUserCode) {
        this.campTeamUserCode = campTeamUserCode;
    }

    public BigDecimal getCampTeamUserCode() {
        return campTeamUserCode;
    }

    public void setCampTeamName(String campTeamName) {
        this.campTeamName = campTeamName;
    }

    public String getCampTeamName() {
        return campTeamName;
    }

    public void setCampDate(Date campDate) {
        this.campDate = campDate;
    }

    public Date getCampDate() {
        return campDate;
    }

    public void setCampSponsor(String campSponsor) {
        this.campSponsor = campSponsor;
    }

    public String getCampSponsor() {
        return campSponsor;
    }

    public void setCampOrgCode(BigDecimal campOrgCode) {
        this.campOrgCode = campOrgCode;
    }

    public BigDecimal getCampOrgCode() {
        return campOrgCode;
    }

    public void setCampOrgName(String campOrgName) {
        this.campOrgName = campOrgName;
    }

    public String getCampOrgName() {
        return campOrgName;
    }

    public void setCampProdCode(BigDecimal campProdCode) {
        this.campProdCode = campProdCode;
    }

    public BigDecimal getCampProdCode() {
        return campProdCode;
    }

    public void setCampProdName(String campProdName) {
        this.campProdName = campProdName;
    }

    public String getCampProdName() {
        return campProdName;
    }

    public void setCampSysCode(BigDecimal campSysCode) {
        this.campSysCode = campSysCode;
    }

    public BigDecimal getCampSysCode() {
        return campSysCode;
    }

    public void setCampName(String campName) {
        this.campName = campName;
    }

    public String getCampName() {
        return campName;
    }

    public void setCampType(String campType) {
        this.campType = campType;
    }

    public String getCampType() {
        return campType;
    }

    public void setCampStatus(String campStatus) {
        this.campStatus = campStatus;
    }

    public String getCampStatus() {
        return campStatus;
    }

    public void setCampExpCloseDate(Date campExpCloseDate) {
        this.campExpCloseDate = campExpCloseDate;
    }

    public Date getCampExpCloseDate() {
        return campExpCloseDate;
    }

    public void setCampTargetAudience(String campTargetAudience) {
        this.campTargetAudience = campTargetAudience;
    }

    public String getCampTargetAudience() {
        return campTargetAudience;
    }

    public void setCampTargetSize(BigDecimal campTargetSize) {
        this.campTargetSize = campTargetSize;
    }

    public BigDecimal getCampTargetSize() {
        return campTargetSize;
    }

    public void setCampNumSent(String campNumSent) {
        this.campNumSent = campNumSent;
    }

    public String getCampNumSent() {
        return campNumSent;
    }

    public void setCampBudgetedCost(BigDecimal campBudgetedCost) {
        this.campBudgetedCost = campBudgetedCost;
    }

    public BigDecimal getCampBudgetedCost() {
        return campBudgetedCost;
    }


    public void setCampExpRevenue(BigDecimal campExpRevenue) {
        this.campExpRevenue = campExpRevenue;
    }

    public BigDecimal getCampExpRevenue() {
        return campExpRevenue;
    }

    public void setCampExpSalesCount(BigDecimal campExpSalesCount) {
        this.campExpSalesCount = campExpSalesCount;
    }

    public BigDecimal getCampExpSalesCount() {
        return campExpSalesCount;
    }

    public void setCampActualSalesCount(BigDecimal campActualSalesCount) {
        this.campActualSalesCount = campActualSalesCount;
    }

    public BigDecimal getCampActualSalesCount() {
        return campActualSalesCount;
    }


    public void setCampExpResponseCount(BigDecimal campExpResponseCount) {
        this.campExpResponseCount = campExpResponseCount;
    }

    public BigDecimal getCampExpResponseCount() {
        return campExpResponseCount;
    }

    public void setCampExpROI(BigDecimal campExpROI) {
        this.campExpROI = campExpROI;
    }

    public BigDecimal getCampExpROI() {
        return campExpROI;
    }

    public void setCampActualROI(BigDecimal campActualROI) {
        this.campActualROI = campActualROI;
    }

    public BigDecimal getCampActualROI() {
        return campActualROI;
    }

    public void setCampDescription(String campDescription) {
        this.campDescription = campDescription;
    }

    public String getCampDescription() {
        return campDescription;
    }

    public void setCurrencyCode(BigDecimal currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCampActualCost(BigDecimal campActualCost) {
        this.campActualCost = campActualCost;
    }

    public BigDecimal getCampActualCost() {
        return campActualCost;
    }

    public void setCampObjective(String campObjective) {
        this.campObjective = campObjective;
    }

    public String getCampObjective() {
        return campObjective;
    }

    public void setCampActualResponseCount(BigDecimal campActualResponseCount) {
        this.campActualResponseCount = campActualResponseCount;
    }

    public BigDecimal getCampActualResponseCount() {
        return campActualResponseCount;
    }

    public void setCampImpressionCount(BigDecimal campImpressionCount) {
        this.campImpressionCount = campImpressionCount;
    }

    public BigDecimal getCampImpressionCount() {
        return campImpressionCount;
    }

    public void setCampExpCost(BigDecimal campExpCost) {
        this.campExpCost = campExpCost;
    }

    public BigDecimal getCampExpCost() {
        return campExpCost;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getVenue() {
        return venue;
    }

    public void setEventTime(Timestamp eventTime) {
        this.eventTime = eventTime;
    }

    public Timestamp getEventTime() {
        return eventTime;
    }
}
