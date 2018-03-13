package com.in.fujitsu.pricing.storage.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ChhabrMa
 *
 */
@Getter
@Setter
@ToString
public class StorageRevenueInfoDto implements Serializable {

	private static final long serialVersionUID = 9129977584436239044L;

	private BigDecimal storageRevenue;
	private BigDecimal backupRevenue;
	private BigDecimal totalRevenue;
	private String benchMarkType;
	private int year;

}
