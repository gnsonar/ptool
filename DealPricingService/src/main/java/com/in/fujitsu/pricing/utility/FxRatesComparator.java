package com.in.fujitsu.pricing.utility;

import java.util.Comparator;

import com.in.fujitsu.pricing.entity.FXRatesInfo;

/**
 * @author mishrasub
 *
 */
public class FxRatesComparator implements Comparator<FXRatesInfo> {
	public int compare(FXRatesInfo o1, FXRatesInfo o2) {
		return o1.getCurrencyTo().compareTo(o2.getCurrencyTo());
	}
}