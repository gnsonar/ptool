package com.in.fujitsu.pricing.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author mishrasub
 *
 */
@Entity
@Table(name = "Service_Window_Master")
public class ServiceWindow implements Serializable {

	private static final long serialVersionUID = -5480854082627443219L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	private String windowName;
	private String windowRange;
	private boolean isActive;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getWindowName() {
		return windowName;
	}
	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}
	public String getWindowRange() {
		return windowRange;
	}
	public void setWindowRange(String windowRange) {
		this.windowRange = windowRange;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}



}
