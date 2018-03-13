package com.in.fujitsu.pricing.storage.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class StorageDefaultInfoDto implements Serializable {

	private static final long serialVersionUID = -1104270439308339062L;
	private Integer Id;
	private String storageName;
	private Float storageSize;

}
