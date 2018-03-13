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
@Table(name = "RETAIL_EQUIPMENT_SET")
public class RetailEquipmentSetInfo implements Serializable {

	private static final long serialVersionUID = -5480854082627443219L;

	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	private String setDesc;
	private boolean isActive;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSetDesc() {
		return setDesc;
	}
	public void setSetDesc(String setDesc) {
		this.setDesc = setDesc;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
}
