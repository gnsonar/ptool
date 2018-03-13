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
public class DealTypeDto implements Serializable {

	private static final long serialVersionUID = -3990719335086408159L;
	private Long id;
	private String dealType;
	private boolean isActive;

}
