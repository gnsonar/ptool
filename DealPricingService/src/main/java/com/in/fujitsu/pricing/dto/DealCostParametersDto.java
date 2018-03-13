package com.in.fujitsu.pricing.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author mishrasub
 *
 */
@Getter
@Setter
@ToString
public class DealCostParametersDto implements Serializable {

	private static final long serialVersionUID = -470412455352345109L;

	private Long id;
	private String name;
	private long amount;

}
