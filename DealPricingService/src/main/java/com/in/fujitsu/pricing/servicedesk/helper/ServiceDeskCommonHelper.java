package com.in.fujitsu.pricing.servicedesk.helper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.servicedesk.calculator.ServiceDeskFxRateConvertor;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskInfo;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskYearlyDataInfo;

/**
 * @author ChhabrMa
 *
 */
@Component
public class ServiceDeskCommonHelper {

	/**
	 * @param serviceDeskYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getTotalContactsAverageVolume(List<ServiceDeskYearlyDataInfo> serviceDeskYearlyDataInfoList) {
		BigDecimal avgTotalContacts = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(serviceDeskYearlyDataInfoList)) {
			int totalContacts = 0;
			int size = 0;
			for (ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo : serviceDeskYearlyDataInfoList) {
				if (serviceDeskYearlyDataInfo.getTotalContacts() != 0) {
					totalContacts += serviceDeskYearlyDataInfo.getTotalContacts();
					size++;
				}
			}
			if (size != 0) {
				avgTotalContacts = new BigDecimal(totalContacts / size);
			}

		}
		return avgTotalContacts;
	}

	/**
	 * @param serviceDeskYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getVoiceContactsAverageVolume(List<ServiceDeskYearlyDataInfo> serviceDeskYearlyDataInfoList) {
		BigDecimal avgVoiceContacts = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(serviceDeskYearlyDataInfoList)) {
			int voiceContacts = 0;
			int size = 0;
			for (ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo : serviceDeskYearlyDataInfoList) {
				if (serviceDeskYearlyDataInfo.getVoiceContacts() != 0) {
					voiceContacts += serviceDeskYearlyDataInfo.getVoiceContacts();
					size++;
				}
			}
			if (size != 0) {
				avgVoiceContacts = new BigDecimal(voiceContacts / size);
			}

		}
		return avgVoiceContacts;
	}

	/**
	 * @param serviceDeskYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getMailContactsAverageVolume(List<ServiceDeskYearlyDataInfo> serviceDeskYearlyDataInfoList) {
		BigDecimal avgMailContacts = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(serviceDeskYearlyDataInfoList)) {
			int mailContacts = 0;
			int size = 0;
			for (ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo : serviceDeskYearlyDataInfoList) {
				if (serviceDeskYearlyDataInfo.getMailContacts() != 0) {
					mailContacts += serviceDeskYearlyDataInfo.getMailContacts();
					size++;
				}
			}
			if (size != 0) {
				avgMailContacts = new BigDecimal(mailContacts / size);
			}

		}
		return avgMailContacts;
	}

	/**
	 * @param serviceDeskYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getChatContactsAverageVolume(List<ServiceDeskYearlyDataInfo> serviceDeskYearlyDataInfoList) {
		BigDecimal avgChatContacts = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(serviceDeskYearlyDataInfoList)) {
			int chatContacts = 0;
			int size = 0;
			for (ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo : serviceDeskYearlyDataInfoList) {
				if (serviceDeskYearlyDataInfo.getChatContacts() != 0) {
					chatContacts += serviceDeskYearlyDataInfo.getChatContacts();
					size++;
				}
			}
			if (size != 0) {
				avgChatContacts = new BigDecimal(chatContacts / size);
			}

		}
		return avgChatContacts;
	}

	/**
	 * @param serviceDeskYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getPortalContactsAverageVolume(List<ServiceDeskYearlyDataInfo> serviceDeskYearlyDataInfoList) {
		BigDecimal avgPortalContacts = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(serviceDeskYearlyDataInfoList)) {
			int portalContacts = 0;
			int size = 0;
			for (ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo : serviceDeskYearlyDataInfoList) {
				if (serviceDeskYearlyDataInfo.getPortalContacts() != 0) {
					portalContacts += serviceDeskYearlyDataInfo.getPortalContacts();
					size++;
				}
			}
			if (size != 0) {
				avgPortalContacts = new BigDecimal(portalContacts / size);
			}

		}
		return avgPortalContacts;
	}

	/**
	 * @param assessmentDealTerm
	 * @param dealResults
	 */
	public void adjustYearlyDataBasedOnDealTerm(Integer assessmentDealTerm, List<ServiceDeskInfo> dealResults) {
		for (ServiceDeskInfo serviceDeskInfo : dealResults) {
			Integer dealTerm = serviceDeskInfo.getDealInfo().getDealTerm() / 12;
			Integer currentDealTerm = serviceDeskInfo.getDealInfo().getDealTerm() % 12 == 0 ? dealTerm : dealTerm + 1;

			if (currentDealTerm < assessmentDealTerm) {
				int count = assessmentDealTerm - currentDealTerm;
				List<ServiceDeskYearlyDataInfo> serviceDeskYearlyDataInfoList = serviceDeskInfo
						.getServiceDeskYearlyDataInfoList();
				int size = serviceDeskYearlyDataInfoList.size();
				ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo = serviceDeskYearlyDataInfoList.get(size - 1);
				for (int i = 1; i <= count; i++) {
					ServiceDeskYearlyDataInfo cloneServiceDeskYearlyDataInfo = (ServiceDeskYearlyDataInfo) serviceDeskYearlyDataInfo
							.clone();
					cloneServiceDeskYearlyDataInfo.setYear(size + i);
					serviceDeskYearlyDataInfoList.add(cloneServiceDeskYearlyDataInfo);
				}

			}
			if (currentDealTerm > assessmentDealTerm) {
				List<ServiceDeskYearlyDataInfo> serviceDeskYearlyDataInfoList = serviceDeskInfo
						.getServiceDeskYearlyDataInfoList();
				int size = serviceDeskYearlyDataInfoList.size();
				for (int i = size - 1; i >= assessmentDealTerm; i--) {
					serviceDeskYearlyDataInfoList.remove(i);
				}
			}
		}
	}

	/**
	 * @param dealResults
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 */
	public void applyFxRatesAndCountryFactor(List<ServiceDeskInfo> dealResults, List<CountryFactorInfo> countryFactors,
			String referenceCurrency, String referenceCountry, BigDecimal referenceCountryFactor) {
		// currency conversion based on the FX Rates and country factor
		ServiceDeskFxRateConvertor serviceDeskFxRateConvertor = new ServiceDeskFxRateConvertor();

		serviceDeskFxRateConvertor.calculateUnitPriceFromFXRates(dealResults, countryFactors, referenceCurrency,
				referenceCountry, referenceCountryFactor);

	}

	/**
	 * @param assessmentAvgVolume
	 * @param yearlyAvgUnitPriceMap
	 * @return
	 */
	public Map<Long, BigDecimal> prepareDealAbsVolumeDiff(BigDecimal assessmentAvgVolume,
			Map<Long, BigDecimal> yearlyAvgUnitPriceMap) {
		Map<Long, BigDecimal> differencePercentageMap = new HashMap<>();
		for (Map.Entry<Long, BigDecimal> entry : yearlyAvgUnitPriceMap.entrySet()) {
			BigDecimal difference = assessmentAvgVolume.subtract(entry.getValue());
			BigDecimal differencePercentage = difference.divide(assessmentAvgVolume, 2, BigDecimal.ROUND_CEILING)
					.multiply(new BigDecimal(100));
			BigDecimal absoluteDifference = differencePercentage.abs();
			differencePercentageMap.put(entry.getKey(), absoluteDifference);
		}
		return differencePercentageMap;
	}

}
