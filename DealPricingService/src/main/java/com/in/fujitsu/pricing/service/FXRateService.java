package com.in.fujitsu.pricing.service;

import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.in.fujitsu.pricing.dto.CurrencyDto;
import com.in.fujitsu.pricing.dto.FxRatesDto;
import com.in.fujitsu.pricing.entity.FXRatesInfo;
import com.in.fujitsu.pricing.repository.FxRateRepository;
import com.in.fujitsu.pricing.utility.FxRatesComparator;

/**
 * @author mishrasub
 *
 */
@Service
public class FXRateService {
	@Autowired
	private FxRateRepository fxRateRepository;

	public List<CurrencyDto> getCurrencyRates() throws ServerException {
		List<CurrencyDto> currencyDtoList = new ArrayList<>();
		try {
			List<FXRatesInfo> groupedFxRatesInfoList = fxRateRepository.getGroupedFxRates();

			for (FXRatesInfo fxRatesInfo : groupedFxRatesInfoList) {
				CurrencyDto currencyDto = new CurrencyDto();
				currencyDto.setCurrencyFrom(fxRatesInfo.getCurrencyFrom());
				currencyDtoList.add(currencyDto);
			}

			List<FXRatesInfo> fxRatesInfoList = fxRateRepository.findAll();
			Collections.sort(fxRatesInfoList, new FxRatesComparator());
			for (FXRatesInfo fxRatesInfo : fxRatesInfoList) {
				for (CurrencyDto currencyDto : currencyDtoList) {
					if (currencyDto.getCurrencyFrom().equalsIgnoreCase(fxRatesInfo.getCurrencyFrom())) {
						FxRatesDto fxRatesDto = new FxRatesDto();
						fxRatesDto.setCurrencyTo(fxRatesInfo.getCurrencyTo());
						fxRatesDto.setRate(fxRatesInfo.getRate());
						currencyDto.getFxRatesDtos().add(fxRatesDto);
					}
				}
			}

		} catch (Exception exception) {
			throw new ServerException(exception.getMessage());
		}
		return currencyDtoList;
	}

	public List<CurrencyDto> updateCurrencyRates(List<CurrencyDto> currencyDtoList) throws ServerException {
		try {
			List<FXRatesInfo> fxRatesInfoList = fxRateRepository.findAll();
			for (CurrencyDto currencyDto : currencyDtoList) {
				for (FXRatesInfo fxRatesInfo : fxRatesInfoList) {
					if (currencyDto.getCurrencyFrom().equalsIgnoreCase(fxRatesInfo.getCurrencyFrom())) {
						for (FxRatesDto fxRatesDto : currencyDto.getFxRatesDtos()) {
							if (fxRatesDto.getCurrencyTo().equalsIgnoreCase(fxRatesInfo.getCurrencyTo())) {
								fxRatesInfo.setRate(fxRatesDto.getRate());
								Calendar calendar = new GregorianCalendar();
								fxRatesInfo.setModifiedDate(new Date(calendar.getTimeInMillis()));
							}
						}
					}

				}
			}
			fxRateRepository.save(fxRatesInfoList);

		} catch (Exception exception) {
			throw new ServerException(exception.getMessage());
		}
		return currencyDtoList;
	}

}
