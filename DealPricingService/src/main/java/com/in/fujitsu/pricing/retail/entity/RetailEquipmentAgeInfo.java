package com.in.fujitsu.pricing.retail.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @author pawarbh
 *
 */
@Entity
@Table(name = "RETAIL_EQUIPMENT_AGE")
public class RetailEquipmentAgeInfo implements Serializable {

	private static final long serialVersionUID = -5480854082627443219L;

	@Id
	@GeneratedValue
	private Integer id;
	private String age;
	private String ageDesc;
	private boolean isActive;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getAgeDesc() {
		return ageDesc;
	}
	public void setAgeDesc(String ageDesc) {
		this.ageDesc = ageDesc;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
}
