package com.in.fujitsu.pricing.storage.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "STORAGE_DEFAULT")
public class StorageDefaultInfo implements Serializable {

	private static final long serialVersionUID = 1403592081997301947L;

	@Id
	@GeneratedValue
	private Integer Id;
	private String storageName;
	private Float storageSize;

	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getStorageName() {
		return storageName;
	}
	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}
	public Float getStorageSize() {
		return storageSize;
	}
	public void setStorageSize(Float storageSize) {
		this.storageSize = storageSize;
	}





}
