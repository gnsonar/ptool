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
public class DealPhaseDto implements Serializable {

	private static final long serialVersionUID = 1127773693695116025L;

	private Long id;
	private String dealPhase;
	private boolean isActive;

}
