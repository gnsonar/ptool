package com.in.fujitsu.pricing.storage.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "STORAGE_BACKUP")
public class StorageBackupInfo implements Serializable {

	private static final long serialVersionUID = 8238165977065461220L;

	@Id
	@GeneratedValue
	private Integer backupId;
	private String backupFrequencyName;
	private Float backupSize;

	public Integer getBackupId() {
		return backupId;
	}
	public void setBackupId(Integer backupId) {
		this.backupId = backupId;
	}
	public String getBackupFrequencyName() {
		return backupFrequencyName;
	}
	public void setBackupFrequencyName(String backupFrequencyName) {
		this.backupFrequencyName = backupFrequencyName;
	}
	public Float getBackupSize() {
		return backupSize;
	}
	public void setBackupSize(Float backupSize) {
		this.backupSize = backupSize;
	}



}
