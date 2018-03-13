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
public class ServiceWindowDto implements Serializable {

	private static final long serialVersionUID = 7688479708121708168L;
	private Long id;
	private String windowName;
	private String windowRange;
	private boolean isActive;

}
