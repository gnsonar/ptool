package com.in.fujitsu.pricing.servicedesk.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SERVICE_DESK_SOLUTION")
public class ServiceDeskSolutionsInfo implements Serializable {

	private static final long serialVersionUID = 2375532168110832965L;

	@Id
	@GeneratedValue
	private Integer solutionId;

	private String solutionName;

	private String solutionDesc;

	@Column(scale = 2)
	private BigDecimal voicePerc;

	@Column(scale = 2)
	private BigDecimal mailPerc;

	@Column(scale = 2)
	private BigDecimal chatPerc;

	@Column(scale = 2)
	private BigDecimal portalPerc;

	public Integer getSolutionId() {
		return solutionId;
	}
	public void setSolutionId(Integer solutionId) {
		this.solutionId = solutionId;
	}
	public String getSolutionName() {
		return solutionName;
	}
	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}
	public String getSolutionDesc() {
		return solutionDesc;
	}
	public void setSolutionDesc(String solutionDesc) {
		this.solutionDesc = solutionDesc;
	}
	public BigDecimal getVoicePerc() {
		return voicePerc;
	}
	public void setVoicePerc(BigDecimal voicePerc) {
		this.voicePerc = voicePerc;
	}
	public BigDecimal getMailPerc() {
		return mailPerc;
	}
	public void setMailPerc(BigDecimal mailPerc) {
		this.mailPerc = mailPerc;
	}
	public BigDecimal getChatPerc() {
		return chatPerc;
	}
	public void setChatPerc(BigDecimal chatPerc) {
		this.chatPerc = chatPerc;
	}
	public BigDecimal getPortalPerc() {
		return portalPerc;
	}
	public void setPortalPerc(BigDecimal portalPerc) {
		this.portalPerc = portalPerc;
	}



}
