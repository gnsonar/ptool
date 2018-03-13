package com.in.fujitsu.pricing.enduser.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @author pawarbh
 *
 */
@Entity
@Table(name = "END_USER_RESOLUTION_TIME")
public class EndUserResolutionTimeInfo implements Serializable {

	private static final long serialVersionUID = -5480854082627443219L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	private String resolutionName;
	private String resolutionRange;
	private boolean isActive;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getResolutionName() {
		return resolutionName;
	}
	public void setResolutionName(String resolutionName) {
		this.resolutionName = resolutionName;
	}
	public String getResolutionRange() {
		return resolutionRange;
	}
	public void setResolutionRange(String resolutionRange) {
		this.resolutionRange = resolutionRange;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}


}
