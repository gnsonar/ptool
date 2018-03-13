package com.in.fujitsu.pricing.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Maninder
 *
 */
@Entity
@Table(name = "DEAL_MASTER")
public class DealInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long dealId;

	private Long modifiedById;

	private String dealName;

	private String clientName;

	private Integer dealTerm;

	private String clientIndustry;

	private String dealPhase;

	private String currency;

	private String country;

	@Temporal(TemporalType.DATE)
	private Date startDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date submissionDate;

	private boolean offshoreAllowed;
	private String serviceWindowSla;
	private boolean includeHardware;
	private String salesRepresentative;
	private String bidManager;
	private String financialEngineer;
	private String leadSolutionArch;
	private Long transitionFees;
	private Long serviceGovernance;
	private boolean thirdPartyFinance;
	private boolean openBook;
	private boolean crossBorderTax;
	private boolean nonStandardVariablePricing;
	private boolean migrationCostApplicable;

	// status will contain either 'open' or any of the deal phase value
	private String dealStatus;

	// either of 'Past', 'Benchmark', 'Competitor'
	private String dealType;

	@OneToMany(mappedBy = "dealInfo", cascade = CascadeType.ALL)
	private Set<DealFXRatesInfo> dealFxRates;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "userId", nullable = false)
	private UserInfo userInfo;

	@JsonIgnore
	@OneToMany(mappedBy = "dealInfo", cascade = CascadeType.ALL)
	private List<DealYearlyDataInfo> dealYearlyDataInfo;

	@OneToMany(mappedBy = "dealInfo", cascade = CascadeType.ALL)
	private Set<DealCompetitorInfo> dealCompetitorInfo;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modificationDate;

	private String assessmentIndicator;
	private String submissionIndicator;

	public Long getDealId() {
		return dealId;
	}

	public void setDealId(Long dealId) {
		this.dealId = dealId;
	}

	public String getDealName() {
		return dealName;
	}

	public void setDealName(String dealName) {
		this.dealName = dealName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getDealPhase() {
		return dealPhase;
	}

	public void setDealPhase(String dealPhase) {
		this.dealPhase = dealPhase;
	}

	public Integer getDealTerm() {
		return dealTerm;
	}

	public void setDealTerm(Integer dealTerm) {
		this.dealTerm = dealTerm;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	public boolean isOffshoreAllowed() {
		return offshoreAllowed;
	}

	public void setOffshoreAllowed(boolean offshoreAllowed) {
		this.offshoreAllowed = offshoreAllowed;
	}

	public String getServiceWindowSla() {
		return serviceWindowSla;
	}

	public void seterviceWindowSla(String serviceWindowSla) {
		this.serviceWindowSla = serviceWindowSla;
	}

	public boolean isIncludeHardware() {
		return includeHardware;
	}

	public void setIncludeHardware(boolean includeHardware) {
		this.includeHardware = includeHardware;
	}

	public String getSalesRepresentative() {
		return salesRepresentative;
	}

	public void setSalesRepresentative(String salesRepresentative) {
		this.salesRepresentative = salesRepresentative;
	}

	public String getBidManager() {
		return bidManager;
	}

	public void setBidManager(String bidManager) {
		this.bidManager = bidManager;
	}

	public String getFinancialEngineer() {
		return financialEngineer;
	}

	public void setFinancialEngineer(String financialEngineer) {
		this.financialEngineer = financialEngineer;
	}

	public String getLeadSolutionArch() {
		return leadSolutionArch;
	}

	public void setLeadSolutionArch(String leadSolutionArch) {
		this.leadSolutionArch = leadSolutionArch;
	}

	public String getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}

	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public String getClientIndustry() {
		return clientIndustry;
	}

	public void setClientIndustry(String clientIndustry) {
		this.clientIndustry = clientIndustry;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setServiceWindowSla(String serviceWindowSla) {
		this.serviceWindowSla = serviceWindowSla;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public List<DealYearlyDataInfo> getDealYearlyDataInfo() {
		return dealYearlyDataInfo;
	}

	public void setDealYearlyDataInfo(List<DealYearlyDataInfo> dealYearlyDataInfo) {
		this.dealYearlyDataInfo = dealYearlyDataInfo;
	}

	public Long getTransitionFees() {
		return transitionFees;
	}

	public void setTransitionFees(Long transitionFees) {
		this.transitionFees = transitionFees;
	}

	public Long getServiceGovernance() {
		return serviceGovernance;
	}

	public void setServiceGovernance(Long serviceGovernance) {
		this.serviceGovernance = serviceGovernance;
	}

	public Set<DealFXRatesInfo> getDealFxRates() {
		return dealFxRates;
	}

	public void setDealFxRates(Set<DealFXRatesInfo> dealFxRates) {
		this.dealFxRates = dealFxRates;
	}

	public boolean isThirdPartyFinance() {
		return thirdPartyFinance;
	}

	public void setThirdPartyFinance(boolean thirdPartyFinance) {
		this.thirdPartyFinance = thirdPartyFinance;
	}

	public boolean isOpenBook() {
		return openBook;
	}

	public void setOpenBook(boolean openBook) {
		this.openBook = openBook;
	}

	public boolean isCrossBorderTax() {
		return crossBorderTax;
	}

	public void setCrossBorderTax(boolean crossBorderTax) {
		this.crossBorderTax = crossBorderTax;
	}

	public boolean isNonStandardVariablePricing() {
		return nonStandardVariablePricing;
	}

	public void setNonStandardVariablePricing(boolean nonStandardVariablePricing) {
		this.nonStandardVariablePricing = nonStandardVariablePricing;
	}

	public boolean isMigrationCostApplicable() {
		return migrationCostApplicable;
	}

	public void setMigrationCostApplicable(boolean migrationCostApplicable) {
		this.migrationCostApplicable = migrationCostApplicable;
	}

	public Set<DealCompetitorInfo> getDealCompetitorInfo() {
		return dealCompetitorInfo;
	}

	public void setDealCompetitorInfo(Set<DealCompetitorInfo> dealCompetitorInfo) {
		this.dealCompetitorInfo = dealCompetitorInfo;
	}

	public Long getModifiedById() {
		return modifiedById;
	}

	public void setModifiedById(Long modifiedById) {
		this.modifiedById = modifiedById;
	}

	public String getAssessmentIndicator() {
		return assessmentIndicator;
	}

	public void setAssessmentIndicator(String assessmentIndicator) {
		this.assessmentIndicator = assessmentIndicator;
	}

	public String getSubmissionIndicator() {
		return submissionIndicator;
	}

	public void setSubmissionIndicator(String submissionIndicator) {
		this.submissionIndicator = submissionIndicator;
	}

}
