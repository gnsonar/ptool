
package com.in.fujitsu.pricing.application.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Table(name="APPLICATION_YEARLY_VOLUME")
@DynamicInsert
@DynamicUpdate
public class ApplicationYearlyDataInfo implements Serializable, Cloneable {

	private static final long serialVersionUID = -2183711413234866870L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "appId")
	private ApplicationInfo appInfo;

	@Column(columnDefinition="int default 0")
	private Integer totalAppsVolume;

	@Column(columnDefinition="int default 0")
	private Integer simpleAppsVolume;

	@Column(columnDefinition="int default 0")
	private Integer mediumAppsVolume;

	@Column(columnDefinition="int default 0")
	private Integer complexAppsVolume;

	@Column(columnDefinition="int default 0")
	private Integer veryComplexAppsVolume;

	private Integer year;

	@JsonIgnore
	@OneToMany(mappedBy = "appYearlyDataInfo", cascade=CascadeType.ALL)
	private List<ApplicationUnitPriceInfo> appUnitPriceInfoList;

	@JsonIgnore
	@OneToMany(mappedBy = "appYearlyDataInfo", cascade = CascadeType.ALL)
	private List<ApplicationRevenueInfo> applicationRevenueInfoList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ApplicationInfo getAppInfo() {
		return appInfo;
	}

	public void setAppInfo(ApplicationInfo appInfo) {
		this.appInfo = appInfo;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getTotalAppsVolume() {
		return totalAppsVolume;
	}

	public void setTotalAppsVolume(Integer totalAppsVolume) {
		this.totalAppsVolume = totalAppsVolume;
	}

	public Integer getSimpleAppsVolume() {
		return simpleAppsVolume;
	}

	public void setSimpleAppsVolume(Integer simpleAppsVolume) {
		this.simpleAppsVolume = simpleAppsVolume;
	}

	public Integer getMediumAppsVolume() {
		return mediumAppsVolume;
	}

	public void setMediumAppsVolume(Integer mediumAppsVolume) {
		this.mediumAppsVolume = mediumAppsVolume;
	}

	public Integer getComplexAppsVolume() {
		return complexAppsVolume;
	}

	public void setComplexAppsVolume(Integer complexAppsVolume) {
		this.complexAppsVolume = complexAppsVolume;
	}

	public Integer getVeryComplexAppsVolume() {
		return veryComplexAppsVolume;
	}

	public void setVeryComplexAppsVolume(Integer veryComplexAppsVolume) {
		this.veryComplexAppsVolume = veryComplexAppsVolume;
	}

	public List<ApplicationUnitPriceInfo> getAppUnitPriceInfoList() {
		return appUnitPriceInfoList;
	}

	public void setAppUnitPriceInfoList(List<ApplicationUnitPriceInfo> appUnitPriceInfoList) {
		this.appUnitPriceInfoList = appUnitPriceInfoList;
	}

	public List<ApplicationRevenueInfo> getApplicationRevenueInfoList() {
		return applicationRevenueInfoList;
	}

	public void setApplicationRevenueInfoList(List<ApplicationRevenueInfo> applicationRevenueInfoList) {
		this.applicationRevenueInfoList = applicationRevenueInfoList;
	}

	@Override
	public ApplicationYearlyDataInfo clone() {
		ApplicationYearlyDataInfo cloneApplicationYearlyDataInfo = null;
		try {
			cloneApplicationYearlyDataInfo = (ApplicationYearlyDataInfo) super.clone();
			List<ApplicationUnitPriceInfo> applicationUnitPriceInfoList = new ArrayList<>();
			for (ApplicationUnitPriceInfo applicationUnitPriceInfo : cloneApplicationYearlyDataInfo
					.getAppUnitPriceInfoList()) {
				ApplicationUnitPriceInfo cloneUnitPrice = applicationUnitPriceInfo.clone();
				cloneUnitPrice.setAppYearlyDataInfo(cloneApplicationYearlyDataInfo);
				applicationUnitPriceInfoList.add(cloneUnitPrice);
			}
			cloneApplicationYearlyDataInfo.setAppUnitPriceInfoList(applicationUnitPriceInfoList);

		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage());
		}
		return cloneApplicationYearlyDataInfo;
	}

}
