package com.in.fujitsu.pricing.hosting.helper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.hosting.calculator.HostingFxRateConvertor;
import com.in.fujitsu.pricing.hosting.entity.HostingInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingYearlyDataInfo;


/**
 * @author pawarbh
 *
 */
@Component
public class HostingCommonHelper {

	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getServersAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getServers() != 0) {
					totalVolume += yearlyDataInfo.getServers();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getPhysicalAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getPhysical() != 0) {
					totalVolume += yearlyDataInfo.getPhysical();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getPhysicalWinAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getPhysicalWin() != 0) {
					totalVolume += yearlyDataInfo.getPhysicalWin();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getPhysicalWinSmallAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getPhysicalWinSmall() != 0) {
					totalVolume += yearlyDataInfo.getPhysicalWinSmall();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getPhysicalWinMediumAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getPhysicalWinMedium() != 0) {
					totalVolume += yearlyDataInfo.getPhysicalWinMedium();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getPhysicalWinLargeAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getPhysicalWinLarge() != 0) {
					totalVolume += yearlyDataInfo.getPhysicalWinLarge();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getPhysicalUnixAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getPhysicalUnix() != 0) {
					totalVolume += yearlyDataInfo.getPhysicalUnix();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getPhysicalUnixSmallAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getPhysicalUnixSmall() != 0) {
					totalVolume += yearlyDataInfo.getPhysicalUnixSmall();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getPhysicalUnixMediumAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getPhysicalUnixMedium() != 0) {
					totalVolume += yearlyDataInfo.getPhysicalUnixMedium();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getPhysicalUnixLargeAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getPhysicalUnixLarge() != 0) {
					totalVolume += yearlyDataInfo.getPhysicalUnixLarge();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getVirtualAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getVirtual() != 0) {
					totalVolume += yearlyDataInfo.getVirtual();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getVirtualPublicAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getVirtualPublic() != 0) {
					totalVolume += yearlyDataInfo.getVirtualPublic();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getVirtualPublicWinAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getVirtualPublicWin() != 0) {
					totalVolume += yearlyDataInfo.getVirtualPublicWin();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getVirtualPublicWinSmallAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getVirtualPublicWinSmall() != 0) {
					totalVolume += yearlyDataInfo.getVirtualPublicWinSmall();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getVirtualPublicWinMediumAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getVirtualPublicWinMedium() != 0) {
					totalVolume += yearlyDataInfo.getVirtualPublicWinMedium();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getVirtualPublicWinLargeAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getVirtualPublicWinLarge() != 0) {
					totalVolume += yearlyDataInfo.getVirtualPublicWinLarge();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getVirtualPublicUnixAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getVirtualPublicUnix() != 0) {
					totalVolume += yearlyDataInfo.getVirtualPublicUnix();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getVirtualPublicUnixSmallAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getVirtualPublicUnixSmall() != 0) {
					totalVolume += yearlyDataInfo.getVirtualPublicUnixSmall();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getVirtualPublicUnixMediumAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getVirtualPublicUnixMedium() != 0) {
					totalVolume += yearlyDataInfo.getVirtualPublicUnixMedium();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getVirtualPublicUnixLargeAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getVirtualPublicUnixLarge() != 0) {
					totalVolume += yearlyDataInfo.getVirtualPublicUnixLarge();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getVirtualPrivateAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getVirtualPrivate() != 0) {
					totalVolume += yearlyDataInfo.getVirtualPrivate();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getVirtualPrivateWinAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getVirtualPrivateWin() != 0) {
					totalVolume += yearlyDataInfo.getVirtualPrivateWin();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getVirtualPrivateWinSmallAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getVirtualPrivateWinSmall() != 0) {
					totalVolume += yearlyDataInfo.getVirtualPrivateWinSmall();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getVirtualPrivateWinMediumAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getVirtualPrivateWinMedium() != 0) {
					totalVolume += yearlyDataInfo.getVirtualPrivateWinMedium();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getVirtualPrivateWinLargeAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getVirtualPrivateWinLarge() != 0) {
					totalVolume += yearlyDataInfo.getVirtualPrivateWinLarge();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getVirtualPrivateUnixAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getVirtualPrivateUnix() != 0) {
					totalVolume += yearlyDataInfo.getVirtualPrivateUnix();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getVirtualPrivateUnixSmallAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getVirtualPrivateUnixSmall() != 0) {
					totalVolume += yearlyDataInfo.getVirtualPrivateUnixSmall();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getVirtualPrivateUnixMediumAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getVirtualPrivateUnixMedium() != 0) {
					totalVolume += yearlyDataInfo.getVirtualPrivateUnixMedium();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getVirtualPrivateUnixLargeAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getVirtualPrivateUnixLarge() != 0) {
					totalVolume += yearlyDataInfo.getVirtualPrivateUnixLarge();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getSqlInstancesAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getSqlInstances() != 0) {
					totalVolume += yearlyDataInfo.getSqlInstances();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return avgVolume
	 */
	public BigDecimal getCotsInstallationsAverageVolume(List<HostingYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalVolume = 0;
			int size = 0;
			for (HostingYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getCotsInstallations() != 0) {
					totalVolume += yearlyDataInfo.getCotsInstallations();
					size++;
				}
			}

			if (size != 0) {
				avgVolume = new BigDecimal(totalVolume / size);
			}
		}
		return avgVolume;
	}
	
	/**
	 * @param assessmentDealTerm
	 * @param dealResults
	 */
	public void adjustYearlyDataBasedOnDealTerm(Integer assessmentDealTerm, List<HostingInfo> dealResults) {
		for (HostingInfo hostingInfo : dealResults) {
			Integer dealTerm = hostingInfo.getDealInfo().getDealTerm() / 12;
			Integer currentDealTerm = hostingInfo.getDealInfo().getDealTerm() % 12 == 0 ? dealTerm : dealTerm + 1;

			if (currentDealTerm < assessmentDealTerm) {
				int count = assessmentDealTerm - currentDealTerm;
				List<HostingYearlyDataInfo> yearlyDataInfoList = hostingInfo
						.getHostingYearlyDataInfoList();
				int size = yearlyDataInfoList.size();
				HostingYearlyDataInfo yearlyDataInfo = yearlyDataInfoList.get(size - 1);
				for (int i = 1; i <= count; i++) {
					HostingYearlyDataInfo cloneYearlyDataInfo = (HostingYearlyDataInfo) yearlyDataInfo
							.clone();
					cloneYearlyDataInfo.setYear(size + i);
					yearlyDataInfoList.add(cloneYearlyDataInfo);
				}

			}
			if (currentDealTerm > assessmentDealTerm) {
				List<HostingYearlyDataInfo> yearlyDataInfoList = hostingInfo
						.getHostingYearlyDataInfoList();
				int size = yearlyDataInfoList.size();
				for (int i = size - 1; i >= assessmentDealTerm; i--) {
					yearlyDataInfoList.remove(i);
				}
			}
		}
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
	
	/**
	 * @param dealResults
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 */
	public void applyFxRatesAndCountryFactor(List<HostingInfo> dealResults, List<CountryFactorInfo> countryFactors,
			String referenceCurrency, String referenceCountry, BigDecimal referenceCountryFactor, String level) {
		// currency conversion based on the FX Rates and country factor
		HostingFxRateConvertor fxRateConvertor = new HostingFxRateConvertor();
		fxRateConvertor.calculateUnitPriceFromFXRates(dealResults, countryFactors, referenceCurrency,
				referenceCountry, referenceCountryFactor, level);

	}
	
}
