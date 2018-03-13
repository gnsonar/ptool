priceToolcontrollers.controller('NetworkSubCtrl',function($scope,$http,HostingService,EndUserService,StorageService,DealService,NetworkService,customInterceptor,endpointsurls,$stateParams,$alert,$confirm,$dialog,$state) {

	//initialize all the variable needed for the page.
	

	var ctrl = this;
	ctrl.active = 0;
	$scope.hostingInfo = {};
	$scope.dealInfo={};
	$scope.selectedSolutionId = '';
	$scope.avgPriceList=[];
	$scope.hostingChecked = false;
	$scope.enduserChecked = false;
	$scope.lastModStorageVol = [];
	$scope.dummy = [0];
	$scope.user = false;
	$scope.selectedBackUp='';
	$scope.viewBy = {type:'unit'};
	$scope.networkingDto={};
	$scope.pastAvgUnitPriceList=[];
	$scope.compAvgUnitPriceList=[];
	$scope.benchmarkLowAvgUnitPriceList=[];
	$scope.benchmarkTargetAvgUnitPriceList=[];
	$scope.selectedSolutionWanId = '';
	$scope.selectedSolutionLanId = '';
	$scope.lastModNetworkWanVol = [];
	$scope.lastModNetworkLanVol = [];
	$scope.lastModNetworkSecVol = [];
	$scope.solution = {};
	$scope.dealDetails={};
	$scope.showmsg = false;
	$scope.showvalidationmsg = false;
	$scope.customWanSol = false;
	$scope.customLanSol = false;

	// function for reseting tab and select the index which is selected by user.
	$scope.tabReset=function(index)
	{
		for(var i=0;i< $scope.tabs.length;i++){
			$scope.tabs[i].class="Inactive";
			if(i==index){
    			$scope.tabs[i].class="active";
    		}
		}
	}

	// function for stop the tab propagation
	$scope.tabRest=function(e)
	{
		e.stopPropagation();
		e.preventDefault();
	}

	// function for catching tab event solution
	$scope.tabEvent=function($event,index,val,childindex){
		if($event.keyCode == 13 || $event.keyCode == 9){
			switch(val) {
			case "WanVolume":
				for(var i=index;i<$scope.networkInfo.wanDevice.children[0].distributedVolume.length;i++){
					$scope.networkInfo.wanDevice.children[0].distributedVolume[i].volume = $scope.networkInfo.wanDevice.children[0].distributedVolume[index].volume;
				}
				break;
			case "WanPricingRevenue":
				for(var i=index;i<$scope.networkInfo.wanDevice.children[0].distributedVolume.length;i++){
					$scope.networkInfo.wanDevice.children[0].distributedVolume[i].revenue = $scope.networkInfo.wanDevice.children[0].distributedVolume[index].revenue;
				}
				break;
			case "WanPricingUnit":
				for(var i=index;i<$scope.networkInfo.wanDevice.children[0].distributedVolume.length;i++){
					$scope.networkInfo.wanDevice.children[0].distributedVolume[i].unit = $scope.networkInfo.wanDevice.children[0].distributedVolume[index].unit;
				}
				break;
			case "WanChildPricingRevenue":
				for(var i=index;i<$scope.networkInfo.wanDevice.children[0].distributedVolume.length;i++){
					$scope.networkInfo.wanDevice.children[0].children[childindex].distributedVolume[i].revenue = $scope.networkInfo.wanDevice.children[0].children[childindex].distributedVolume[index].revenue;
				}
				break;
			case "WanChildPricingUnit":
				for(var i=index;i<$scope.networkInfo.wanDevice.children[0].distributedVolume.length;i++){
					$scope.networkInfo.wanDevice.children[0].children[childindex].distributedVolume[i].unit = $scope.networkInfo.wanDevice.children[0].children[childindex].distributedVolume[index].unit;
				}
				break;
			case "LanVolume":
				for(var i=index;i<$scope.networkInfo.lanDevice.children[0].distributedVolume.length;i++){
					$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume = $scope.networkInfo.lanDevice.children[0].distributedVolume[index].volume;
				}
				break;
			case "LanPricingRevenue":
				for(var i=index;i<$scope.networkInfo.lanDevice.children[0].distributedVolume.length;i++){
					$scope.networkInfo.lanDevice.children[0].distributedVolume[i].revenue = $scope.networkInfo.lanDevice.children[0].distributedVolume[index].revenue;
				}
				break;
			case "LanPricingUnit":
				for(var i=index;i<$scope.networkInfo.lanDevice.children[0].distributedVolume.length;i++){
					$scope.networkInfo.lanDevice.children[0].distributedVolume[i].unit = $scope.networkInfo.lanDevice.children[0].distributedVolume[index].unit;
				}
				break;
			case "LanChildPricingRevenue":
				for(var i=index;i<$scope.networkInfo.lanDevice.children[0].distributedVolume.length;i++){
					$scope.networkInfo.lanDevice.children[0].children[childindex].distributedVolume[i].revenue = $scope.networkInfo.lanDevice.children[0].children[childindex].distributedVolume[index].revenue;
				}
				break;
			case "LanChildPricingUnit":
				for(var i=index;i<$scope.networkInfo.lanDevice.children[0].distributedVolume.length;i++){
					$scope.networkInfo.lanDevice.children[0].children[childindex].distributedVolume[i].unit = $scope.networkInfo.lanDevice.children[0].children[childindex].distributedVolume[index].unit;
				}
				break;
			case "WlanVolume":
				for(var i=index;i<$scope.networkInfo.wlanCon.children[0].distributedVolume.length;i++){
					$scope.networkInfo.wlanCon.children[0].distributedVolume[i].volume = $scope.networkInfo.wlanCon.children[0].distributedVolume[index].volume;
				}
				break;
			case "WlanPricingRevenue":
				for(var i=index;i<$scope.networkInfo.wlanCon.children[0].distributedVolume.length;i++){
					$scope.networkInfo.wlanCon.children[0].distributedVolume[i].revenue = $scope.networkInfo.wlanCon.children[0].distributedVolume[index].revenue;
				}
				break;
			case "WlanPricingUnit":
				for(var i=index;i<$scope.networkInfo.wlanCon.children[0].distributedVolume.length;i++){
					$scope.networkInfo.wlanCon.children[0].distributedVolume[i].unit = $scope.networkInfo.wlanCon.children[0].distributedVolume[index].unit;
				}
				break;
			case "WlanAccessVolume":
				for(var i=index;i<$scope.networkInfo.wlanAccess.children[0].distributedVolume.length;i++){
					$scope.networkInfo.wlanAccess.children[0].distributedVolume[i].volume = $scope.networkInfo.wlanAccess.children[0].distributedVolume[index].volume;
				}
				break;
			case "WlanAccessPricingRevenue":
				for(var i=index;i<$scope.networkInfo.wlanAccess.children[0].distributedVolume.length;i++){
					$scope.networkInfo.wlanAccess.children[0].distributedVolume[i].revenue = $scope.networkInfo.wlanAccess.children[0].distributedVolume[index].revenue;
				}
				break;
			case "WlanAccessPricingUnit":
				for(var i=index;i<$scope.networkInfo.wlanAccess.children[0].distributedVolume.length;i++){
					$scope.networkInfo.wlanAccess.children[0].distributedVolume[i].unit = $scope.networkInfo.wlanAccess.children[0].distributedVolume[index].unit;
				}
				break;
			case "LoadBalanceVolume":
				for(var i=index;i<$scope.networkInfo.loadBalance.children[0].distributedVolume.length;i++){
					$scope.networkInfo.loadBalance.children[0].distributedVolume[i].volume = $scope.networkInfo.loadBalance.children[0].distributedVolume[index].volume;
				}
				break;
			case "LoadBalancePricingRevenue":
				for(var i=index;i<$scope.networkInfo.loadBalance.children[0].distributedVolume.length;i++){
					$scope.networkInfo.loadBalance.children[0].distributedVolume[i].revenue = $scope.networkInfo.loadBalance.children[0].distributedVolume[index].revenue;
				}
				break;
			case "LoadBalancePricingUnit":
				for(var i=index;i<$scope.networkInfo.loadBalance.children[0].distributedVolume.length;i++){
					$scope.networkInfo.loadBalance.children[0].distributedVolume[i].unit = $scope.networkInfo.loadBalance.children[0].distributedVolume[index].unit;
				}
				break;
			case "VPNVolume":
				for(var i=index;i<$scope.networkInfo.vpnIp.children[0].distributedVolume.length;i++){
					$scope.networkInfo.vpnIp.children[0].distributedVolume[i].volume = $scope.networkInfo.vpnIp.children[0].distributedVolume[index].volume;
				}
				break;
			case "VPNPricingRevenue":
				for(var i=index;i<$scope.networkInfo.vpnIp.children[0].distributedVolume.length;i++){
					$scope.networkInfo.vpnIp.children[0].distributedVolume[i].revenue = $scope.networkInfo.vpnIp.children[0].distributedVolume[index].revenue;
				}
				break;
			case "VPNPricingUnit":
				for(var i=index;i<$scope.networkInfo.vpnIp.children[0].distributedVolume.length;i++){
					$scope.networkInfo.vpnIp.children[0].distributedVolume[i].unit = $scope.networkInfo.vpnIp.children[0].distributedVolume[index].unit;
				}
				break;
			case "DNSVolume":
				for(var i=index;i<$scope.networkInfo.dnsService.children[0].distributedVolume.length;i++){
					$scope.networkInfo.dnsService.children[0].distributedVolume[i].volume = $scope.networkInfo.dnsService.children[0].distributedVolume[index].volume;
				}
				break;
			case "DNSPricingRevenue":
				for(var i=index;i<$scope.networkInfo.dnsService.children[0].distributedVolume.length;i++){
					$scope.networkInfo.dnsService.children[0].distributedVolume[i].revenue = $scope.networkInfo.dnsService.children[0].distributedVolume[index].revenue;
				}
				break;
			case "DNSPricingUnit":
				for(var i=index;i<$scope.networkInfo.dnsService.children[0].distributedVolume.length;i++){
					$scope.networkInfo.dnsService.children[0].distributedVolume[i].unit = $scope.networkInfo.dnsService.children[0].distributedVolume[index].unit;
				}
				break;
			case "FirewallVolume":
				for(var i=index;i<$scope.networkInfo.firewalls.children[0].distributedVolume.length;i++){
					$scope.networkInfo.firewalls.children[0].distributedVolume[i].volume = $scope.networkInfo.firewalls.children[0].distributedVolume[index].volume;
				}
				break;
			case "FirewallPricingRevenue":
				for(var i=index;i<$scope.networkInfo.firewalls.children[0].distributedVolume.length;i++){
					$scope.networkInfo.firewalls.children[0].distributedVolume[i].revenue = $scope.networkInfo.firewalls.children[0].distributedVolume[index].revenue;
				}
				break;
			case "FirewallPricingUnit":
				for(var i=index;i<$scope.networkInfo.firewalls.children[0].distributedVolume.length;i++){
					$scope.networkInfo.firewalls.children[0].distributedVolume[i].unit = $scope.networkInfo.firewalls.children[0].distributedVolume[index].unit;
				}
				break;
			case "ReserveProVolume":
				for(var i=index;i<$scope.networkInfo.reservePro.children[0].distributedVolume.length;i++){
					$scope.networkInfo.reservePro.children[0].distributedVolume[i].volume = $scope.networkInfo.reservePro.children[0].distributedVolume[index].volume;
				}
				break;
			case "ReserveProPricingRevenue":
				for(var i=index;i<$scope.networkInfo.reservePro.children[0].distributedVolume.length;i++){
					$scope.networkInfo.reservePro.children[0].distributedVolume[i].revenue = $scope.networkInfo.reservePro.children[0].distributedVolume[index].revenue;
				}
				break;
			case "ReserveProPricingUnit":
				for(var i=index;i<$scope.networkInfo.reservePro.children[0].distributedVolume.length;i++){
					$scope.networkInfo.reservePro.children[0].distributedVolume[i].unit = $scope.networkInfo.reservePro.children[0].distributedVolume[index].unit;
				}
				break;
			}
		}
	}

		$scope.togglePanel = function(level) {
		  if(level.open){
			  level.open=false;
		  }else
			  {
			  level.open=true;
			  //retainNetworkingOldVolumes(level);

			  }
	    };

	    // get the drop-down values
		$scope.getNetworkDropdowns=function(){
			var url = endpointsurls.NETWORK_DROPDOWN+$stateParams.dealId;
	        customInterceptor.getrequest(url).then(function successCallback(response) {
				$scope.wanSolList = response.data.networkWanSolutionsDtoList;
				$scope.lanSolList = response.data.networkLanSolutionsDtoList;
				$scope.genericDealInfoDto = response.data;
				$scope.dealInfoDto = response.data.dealInfoDto;
				$scope.dealYearlyDataInfoDtoList = $scope.dealInfoDto.dealYearlyDataInfoDtos;
				$scope.submissionIndicator=response.data.dealInfoDto.submissionIndicator || '1,0,0,0,0,0,0,0';
	        	$scope.assessmentIndicator=response.data.dealInfoDto.assessmentIndicator || '0,0,0,0,0,0,0,0';
	        	$scope.submissionIndicator=$scope.submissionIndicator.split(',');
	        	var isSave={'genericS':($scope.submissionIndicator[0]==1)?true:false,
	        			'hostingS':($scope.submissionIndicator[1]==1)?true:false,
	    		     	'storageS':($scope.submissionIndicator[2]==1)?true:false,
	    		     	'endUserS':($scope.submissionIndicator[3]==1)?true:false,
	    		     	'networkS':($scope.submissionIndicator[4]==1)?true:false,
	    		     	'serviceDeskS':($scope.submissionIndicator[5]==1)?true:false,
	    		     	'appsS':($scope.submissionIndicator[6]==1)?true:false,
	    		     	'retailS':($scope.submissionIndicator[7]==1)?true:false
	    		};
	        	DealService.setter(isSave);
				
				 var isSaveStorage= DealService.getter() || isSave;
		   	    DealService.setter(isSaveStorage);
	            $scope.setDealDeatils();
			}, function errorCallback(response) {
				console.log(response.statusText);
				if(response.status=='401'){
					$state.go('login');
				}
			});
		}

		//get default  data of generic screen
		$scope.setDealDeatils=function(){
			$scope.dealDetails.offshoreSelected= $scope.dealInfoDto.offshoreAllowed;
	        $scope.dealDetails.standardwindowInfoSelected= $scope.dealInfoDto.serviceWindowSla;
	        $scope.dealDetails.hardwareIncludedSelected= $scope.dealInfoDto.includeHardware;
	        $scope.dealInfo= $scope.dealInfoDto.dealTerm;
	        $scope.networkInfo = NetworkService.initNetworkDetails($scope.dealInfo);
	        if($stateParams.dealId>0){
				 var url = endpointsurls.SAVE_NETWORK_INFO+ $stateParams.dealId;
			        customInterceptor.getrequest(url).then(function successCallback(response) {
		       		$scope.existingNetworkInfo = response.data;
		       		$scope.level=$scope.existingNetworkInfo.levelIndicator.split(',');
		       		$scope.networkInfo.wanDevice.open=($scope.level[0]==1)?true:false;
		       		$scope.networkInfo.wanDevice.children[0].open=($scope.level[1]==1)?true:false,
		       		$scope.networkInfo.lanDevice.open=($scope.level[2]==1)?true:false,
		       		$scope.networkInfo.lanDevice.children[0].open=($scope.level[3]==1)?true:false,
		       		$scope.networkInfo.wlanCon.open=($scope.level[4]==1)?true:false,
		       		$scope.networkInfo.wlanAccess.open=($scope.level[5]==1)?true:false,
		       		$scope.networkInfo.loadBalance.open=($scope.level[6]==1)?true:false,
		       		$scope.networkInfo.vpnIp.open=($scope.level[7]==1)?true:false,
		       		$scope.networkInfo.dnsService.open=($scope.level[8]==1)?true:false,
		       		$scope.networkInfo.firewalls.open=($scope.level[9]==1)?true:false,
		       		$scope.networkInfo.reservePro.open=($scope.level[10]==1)?true:false
		       		if($scope.existingNetworkInfo != null && $scope.existingNetworkInfo.networkId != null){
		       			mapExistingNetworkInfo();
		       		}
		        }, function errorCallback(response) {
		            console.log(response.statusText);
		        });

			}
		}

		//function for init controller
		$scope.init=function(){
			$scope.getNetworkDropdowns();
		}
		$scope.init();

		// map existing Service Desk Info
	    function mapExistingNetworkInfo() {
	    	$scope.dealDetails.offshoreSelected= $scope.existingNetworkInfo.offshoreAllowed == true ? "Yes" : "No";
	    	$scope.dealDetails.hardwareIncludedSelected = $scope.existingNetworkInfo.includeHardware == true ? "Yes" : "No";
		    $scope.dealDetails.standardwindowInfoSelected= $scope.existingNetworkInfo.levelOfService;
	   		$scope.dealDetails.towerArchitect = $scope.existingNetworkInfo.towerArchitect;
	   		$scope.selectedSolutionWanId = $scope.existingNetworkInfo.selectedWanSolutionId;
	   		$scope.selectedSolutionLanId = $scope.existingNetworkInfo.selectedLanSolutionId;
	   		getExistingNetworkYearlyInfo();
	   		
	   		if($scope.selectedSolutionWanId != null){
	   			var CustomSolutionWanName = _.where($scope.wanSolList, {solutionId: $scope.selectedSolutionWanId})[0].solutionName;
	   			if(CustomSolutionWanName != 'Custom'){
	   				$scope.onChangeWanDistSetting($scope.selectedSolutionWanId);
	   			}
	   		}
	   		
	   		if($scope.selectedSolutionLanId != null){
	   			var CustomSolutionLanName = _.where($scope.lanSolList, {solutionId: $scope.selectedSolutionLanId})[0].solutionName;
	   			if(CustomSolutionLanName != 'Custom'){
	   				$scope.onChangeLanDistSetting($scope.selectedSolutionLanId);
	   			}
	   		}
	   		
	    }

	    // map Existing Network Yearly Info
	    function getExistingNetworkYearlyInfo() {
	    	if($scope.existingNetworkInfo.networkYearlyDataInfoDtoList != null){
	    		for (var y = 0; y < $scope.existingNetworkInfo.networkYearlyDataInfoDtoList.length; y++){
	        		var yearlyDto = $scope.existingNetworkInfo.networkYearlyDataInfoDtoList[y];
	        		$scope.networkInfo.wanDevice.children[0].distributedVolume[y].year = yearlyDto.year;
	        		$scope.networkInfo.wanDevice.children[0].distributedVolume[y].volume = yearlyDto.wanDevices;
	        		$scope.networkInfo.wanDevice.children[0].children[0].distributedVolume[y].volume = yearlyDto.smallWanDevices;
	        		$scope.networkInfo.wanDevice.children[0].children[1].distributedVolume[y].volume = yearlyDto.mediumWanDevices;
	        		$scope.networkInfo.wanDevice.children[0].children[2].distributedVolume[y].volume = yearlyDto.largeWanDevices;
		    		$scope.networkInfo.lanDevice.children[0].distributedVolume[y].volume = yearlyDto.lanDevices;
		    		$scope.networkInfo.lanDevice.children[0].children[0].distributedVolume[y].volume = yearlyDto.smallLanDevices;
		    		$scope.networkInfo.lanDevice.children[0].children[1].distributedVolume[y].volume = yearlyDto.mediumLanDevices;
		    		$scope.networkInfo.lanDevice.children[0].children[2].distributedVolume[y].volume = yearlyDto.largeLanDevices;
		    		$scope.networkInfo.wlanCon.children[0].distributedVolume[y].volume = yearlyDto.wlanControllers;
		    		$scope.networkInfo.wlanAccess.children[0].distributedVolume[y].volume = yearlyDto.wlanAccesspoint;
		    		$scope.networkInfo.loadBalance.children[0].distributedVolume[y].volume = yearlyDto.loadBalancers;
		    		$scope.networkInfo.vpnIp.children[0].distributedVolume[y].volume = yearlyDto.vpnIpSec;
		    		$scope.networkInfo.dnsService.children[0].distributedVolume[y].volume = yearlyDto.dnsDhcpService;
		    		$scope.networkInfo.firewalls.children[0].distributedVolume[y].volume = yearlyDto.firewalls;
		    		$scope.networkInfo.reservePro.children[0].distributedVolume[y].volume = yearlyDto.proxies;
	        		getExistingServerPricingLevelWiseInfo(yearlyDto,y);
	        	}
	    	}
	    }

	    // Get existing Service Desk server price level wise
		function getExistingServerPricingLevelWiseInfo(yearlyDto,year){
			if(yearlyDto.networkUnitPriceInfoDtoList != null){
				if(yearlyDto.networkUnitPriceInfoDtoList.length > 0){
					  if($scope.viewBy.type == 'unit'){
						  $scope.networkInfo.wanDevice.children[0].distributedVolume[year].unit = yearlyDto.networkUnitPriceInfoDtoList[0].wanDevices;
				    	  $scope.networkInfo.wanDevice.children[0].children[0].distributedVolume[year].unit = yearlyDto.networkUnitPriceInfoDtoList[0].smallWanDevices;
				    		$scope.networkInfo.wanDevice.children[0].children[1].distributedVolume[year].unit = yearlyDto.networkUnitPriceInfoDtoList[0].mediumWanDevices;
				    		$scope.networkInfo.wanDevice.children[0].children[2].distributedVolume[year].unit = yearlyDto.networkUnitPriceInfoDtoList[0].largeWanDevices;
				    		$scope.networkInfo.lanDevice.children[0].distributedVolume[year].unit = yearlyDto.networkUnitPriceInfoDtoList[0].lanDevices;
				    		$scope.networkInfo.lanDevice.children[0].children[0].distributedVolume[year].unit = yearlyDto.networkUnitPriceInfoDtoList[0].smallLanDevices;
				    		$scope.networkInfo.lanDevice.children[0].children[1].distributedVolume[year].unit = yearlyDto.networkUnitPriceInfoDtoList[0].mediumLanDevices;
				    		$scope.networkInfo.lanDevice.children[0].children[2].distributedVolume[year].unit = yearlyDto.networkUnitPriceInfoDtoList[0].largeLanDevices;
				    	    $scope.networkInfo.wlanCon.children[0].distributedVolume[year].unit = yearlyDto.networkUnitPriceInfoDtoList[0].wlanControllers;
				    		$scope.networkInfo.wlanAccess.children[0].distributedVolume[year].unit = yearlyDto.networkUnitPriceInfoDtoList[0].wlanAccesspoint;
				    		$scope.networkInfo.loadBalance.children[0].distributedVolume[year].unit = yearlyDto.networkUnitPriceInfoDtoList[0].loadBalancers;
				    		$scope.networkInfo.vpnIp.children[0].distributedVolume[year].unit = yearlyDto.networkUnitPriceInfoDtoList[0].vpnIpSec;
				    		$scope.networkInfo.dnsService.children[0].distributedVolume[year].unit = yearlyDto.networkUnitPriceInfoDtoList[0].dnsDhcpService;
				    		$scope.networkInfo.firewalls.children[0].distributedVolume[year].unit = yearlyDto.networkUnitPriceInfoDtoList[0].firewalls;
				    		$scope.networkInfo.reservePro.children[0].distributedVolume[year].unit = yearlyDto.networkUnitPriceInfoDtoList[0].proxies;
					}
				}
			}
		}

		$scope.calcVolume = function(node) {
			if(node.id == 1.1){
				if($scope.selectedSolutionWanId != null && $scope.selectedSolutionWanId != undefined &&  $scope.selectedSolutionWanId != ''){
					calculateNetworkVolume(node);
				}
			}
			if(node.id == 2.1){
				if($scope.selectedSolutionLanId != null && $scope.selectedSolutionLanId != undefined &&  $scope.selectedSolutionLanId != ''){
					calculateNetworkVolume(node);
				}
			}
		}

		// when user change the Wan percentage
		$scope.onChangeWanPercentage=function(child){
			$scope.customWanSol = false;
			var per=0;
			for(var i=0;i<child.length;i++){
				per=parseInt(per)+parseInt(child[i].percentage);
			}

			if(per!=100){
				$scope.showErr=true;
			}
			else{
				$scope.showErr=false;
				$scope.showVolumeErr=false;
				$scope.setWanCustom();
				calculateNetworkVolume($scope.networkInfo.wanDevice.children[0]);
			}
		}

		// when user change the Lan percentage
		$scope.onChangeLanPercentage=function(child){
			$scope.customLanSol = false;
			var per=0;
			for(var i=0;i<child.length;i++){
				per=parseInt(per)+parseInt(child[i].percentage);
			}

			if(per!=100){
				$scope.showErr=true;
			}
			else{
				$scope.showErr=false;
				$scope.showVolumeErr=false;
				$scope.setLanCustom();
				calculateNetworkVolume($scope.networkInfo.lanDevice.children[0]);
			}
		}

		// function for setting the custom value
		$scope.setWanCustom=function()
		{
			$scope.customWanSol=true;
			var CustomSolutionWanId=_.where($scope.wanSolList, {solutionName: 'Custom'})[0];
			$scope.selectedSolutionWanId=CustomSolutionWanId.solutionId;
		}

		// function for setting the custom value
		$scope.setLanCustom=function()
		{
			$scope.customLanSol=true;
			var CustomSolutionLanId=_.where($scope.lanSolList, {solutionName: 'Custom'})[0];
			$scope.selectedSolutionLanId=CustomSolutionLanId.solutionId;
		}

		$scope.onChangeWanDistSetting = function(solId) {
			$scope.showErr=false;
			$scope.showVolumeErr=false;
			$scope.customWanSol = false;
			$scope.selectedSolutionWanId = solId;
	    	$scope.wanSolution = getSelectedNetworkSolutionObject(solId,'Wan');
	    	setLevelWisePercentage($scope.networkInfo.wanDevice,'Wan');
		};

		$scope.onChangeLanDistSetting = function(solId) {
			$scope.showErr=false;
			$scope.showVolumeErr=false;
			$scope.customLanSol = false;
			$scope.selectedSolutionLanId = solId;
	    	$scope.lanSolution = getSelectedNetworkSolutionObject(solId,'Lan');
	    	setLevelWisePercentage($scope.networkInfo.lanDevice,'Lan');
		};

		 function getSelectedNetworkSolutionObject(solId,Key){
			 var sol = "";
			 switch(Key) {
			 	case 'Wan':
					 sol = _.where($scope.wanSolList, {solutionId: solId});
					 break;
			 	case 'Lan':
					 sol = _.where($scope.lanSolList, {solutionId: solId});
					 break;
			 }
			 return sol[0];
	    }

		 function setLevelWisePercentage(parent,Key){
			switch(Key){
				case 'Wan':
					if(parent.children != null){
						for(var k = 0;k < parent.children[0].children.length;k++){
							var child = parent.children[0].children[k];
							switch(child.id) {
							case "1.1.1":
								child.percentage =  $scope.wanSolution? $scope.wanSolution.smallperc : "";break;
							case "1.1.2":
								child.percentage =  $scope.wanSolution? $scope.wanSolution.mediumPerc :"";break;
							case "1.1.3":
								child.percentage =  $scope.wanSolution? $scope.wanSolution.largePerc :"";break;

							}
						}
						calculateNetworkVolume($scope.networkInfo.wanDevice.children[0]);
					}
				break;
				case 'Lan':
					if(parent.children != null){
						for(var k = 0;k < parent.children[0].children.length;k++){
							var child = parent.children[0].children[k];
							switch(child.id) {
							case "2.1.1":
								child.percentage =  $scope.lanSolution? $scope.lanSolution.smallperc : "";break;
							case "2.1.2":
								child.percentage =  $scope.lanSolution? $scope.lanSolution.mediumPerc :"";break;
							case "2.1.3":
								child.percentage =  $scope.lanSolution? $scope.lanSolution.largePerc :"";break;

							}
						}
						calculateNetworkVolume($scope.networkInfo.lanDevice.children[0]);
					}
				break;
			}

		}

		function calculateNetworkVolume(parent){
			if(parent.children != null){
				for(var k = 0;k < parent.children.length;k++){
					var child = parent.children[k];

					for (var i = 0; i < $scope.dealInfo/12; i++){
						child.distributedVolume[i].volume = Math.round((parent.distributedVolume[i].volume * child.percentage)/100);
					}

//					if(child.children != null){
//						calculateNetworkVolume(child);
//					}
				}
				$scope.calcServerPrice(parent);
			}
		}

		// User change volume
		$scope.onChangevolume = function(Key){
			if(Key == 'Wan'){
				$scope.customWanSol=false
				//$scope.changeChildToParent();
				for(var i=0;i< $scope.networkInfo.wanDevice.children[0].children.length;i++){
					$scope.networkInfo.wanDevice.children[0].children[i].percentage = "";
				}
				for (var y = 0; y < $scope.dealInfo/12; y++) {
		    		if(($scope.networkInfo.wanDevice.children[0].distributedVolume[y].volume)!=parseInt(($scope.networkInfo.wanDevice.children[0].children[0].distributedVolume[y].volume))
		    	    		+parseInt(($scope.networkInfo.wanDevice.children[0].children[1].distributedVolume[y].volume))
		    	    		+ parseInt(($scope.networkInfo.wanDevice.children[0].children[2].distributedVolume[y].volume)))
		    			{
		    			$scope.showVolumeErr=true;
		    			return;
		    			}
		    		else
			    		{
			    			$scope.showVolumeErr=false;
			    		}

		    	}
				$scope.setWanCustom();
				$scope.calcServerPrice($scope.networkInfo.wanDevice.children[0]);
			}
			else if(Key == 'Lan'){
				$scope.customWanSol=false
				//$scope.changeChildToParent();
				for(var i=0;i< $scope.networkInfo.lanDevice.children[0].children.length;i++){
					$scope.networkInfo.lanDevice.children[0].children[i].percentage = "";
				}
				for (var y = 0; y < $scope.dealInfo/12; y++) {
		    		if(($scope.networkInfo.lanDevice.children[0].distributedVolume[y].volume)!=parseInt(($scope.networkInfo.lanDevice.children[0].children[0].distributedVolume[y].volume))
		    	    		+parseInt(($scope.networkInfo.lanDevice.children[0].children[1].distributedVolume[y].volume))
		    	    		+ parseInt(($scope.networkInfo.lanDevice.children[0].children[2].distributedVolume[y].volume)))
		    			{
		    			$scope.showVolumeErr=true;
		    			return;
		    			}
		    		else
			    		{
			    			$scope.showVolumeErr=false;
			    		}

		    	}
				$scope.setLanCustom();
				$scope.calcServerPrice($scope.networkInfo.lanDevice.children[0]);
			}

		}

		// calculate parent server price when user insert child server pricing
		$scope.calcServerPrice = function (parent){
			if(parent.levelName == 'WAN Devices'){
				if($scope.networkInfo.wanDevice.children[0].open == true){
					if($scope.viewBy.type == 'unit'){
						for(var i=0;i< parent.distributedVolume.length;i++){
							var Child1volume = "";
							var Child2volume = "";
							var Child3volume = "";
							var Child1unit = "";
							var Child2unit = "";
							var Child3unit = "";
							for(var k = 0;k < parent.children.length;k++){
								var child = parent.children[k];
								switch(child.id) {
								case "1.1.1": //Small Server pricing
									Child1volume = parseFloat((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
									Child1unit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
									break;
								case "1.1.2": //Medium Server pricing
									Child2volume = parseFloat((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
									Child2unit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
									break;
								case "1.1.3": //Large Server pricing
									Child3volume = parseFloat((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
									Child3unit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
									break;
								}
							}
							parent.distributedVolume[i].unit = (((Child1volume * Child1unit) + (Child2volume * Child2unit) + (Child3volume * Child3unit)) / (Child1volume + Child2volume + Child3volume))
							parent.distributedVolume[i].unit = parent.distributedVolume[i].unit.toFixed(2)
							if(isNaN(parent.distributedVolume[i].unit)){
								parent.distributedVolume[i].unit = '';
							}
						}
					}
					if($scope.viewBy.type == 'revenue'){
						for(var i=0;i< parent.distributedVolume.length;i++){
							var Child1revenue = "";
							var Child2revenue = "";
							var Child3revenue = "";
							for(var k = 0;k < parent.children.length;k++){
								var child = parent.children[k];
								switch(child.id) {
								case "1.1.1": //Small Server pricing
									Child1revenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
									break;
								case "1.1.2": //Medium Server pricing
									Child2revenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
									break;
								case "1.1.3": //Large Server pricing
									Child3revenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
									break;
								}
							}
							parent.distributedVolume[i].revenue = Math.round(Child1revenue + Child2revenue + Child3revenue)
						}
					}
				}
			}
			if(parent.levelName == 'LAN Devices'){
				if($scope.networkInfo.lanDevice.children[0].open == true){
					if($scope.viewBy.type == 'unit'){
						for(var i=0;i< parent.distributedVolume.length;i++){
							var Child1volume = "";
							var Child2volume = "";
							var Child3volume = "";
							var Child1unit = "";
							var Child2unit = "";
							var Child3unit = "";
							for(var k = 0;k < parent.children.length;k++){
								var child = parent.children[k];
								switch(child.id) {
								case "2.1.1": //Small Server pricing
									Child1volume = parseFloat((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
									Child1unit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
									break;
								case "2.1.2": //Medium Server pricing
									Child2volume = parseFloat((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
									Child2unit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
									break;
								case "2.1.3": //Large Server pricing
									Child3volume = parseFloat((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
									Child3unit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
									break;
								}
							}
							parent.distributedVolume[i].unit = (((Child1volume * Child1unit) + (Child2volume * Child2unit) + (Child3volume * Child3unit)) / (Child1volume + Child2volume + Child3volume))
							parent.distributedVolume[i].unit = parent.distributedVolume[i].unit.toFixed(2)
							if(isNaN(parent.distributedVolume[i].unit)){
								parent.distributedVolume[i].unit = '';
							}
						}
					}
					if($scope.viewBy.type == 'revenue'){
						for(var i=0;i< parent.distributedVolume.length;i++){
							var Child1revenue = "";
							var Child2revenue = "";
							var Child3revenue = "";
							for(var k = 0;k < parent.children.length;k++){
								var child = parent.children[k];
								switch(child.id) {
								case "2.1.1": //Small Server pricing
									Child1revenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
									break;
								case "2.1.2": //Medium Server pricing
									Child2revenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
									break;
								case "2.1.3": //Large Server pricing
									Child3revenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
									break;
								}
							}
							parent.distributedVolume[i].revenue = Math.round(Child1revenue + Child2revenue + Child3revenue)
						}
					}
				}
			}
		}

		// On change Unit-revenue price radio button
		$scope.onchangeprice = function(value){
			if(value == 'revenue'){
				if($scope.networkInfo.wanDevice.open == true){
					if($scope.networkInfo.wanDevice.children[0].open == true){
						for(var i=0;i< $scope.networkInfo.wanDevice.children[0].distributedVolume.length;i++){
							var Child1revenue = "";
							var Child2revenue = "";
							var Child3revenue = "";
							for(var k = 0;k < $scope.networkInfo.wanDevice.children[0].children.length;k++){
								child = $scope.networkInfo.wanDevice.children[0].children[k];
				    			switch(child.id) {
				    			case "1.1": //Wan
									break;
								case "1.1.1": //Small Server pricing
									child.distributedVolume[i].revenue = Math.round(((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume)  * ((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit));
									if(isNaN(child.distributedVolume[i].revenue)){
										child.distributedVolume[i].revenue = 0;
									}
									Child1revenue = child.distributedVolume[i].revenue;
									break;
								case "1.1.2": //Medium Server pricing
									child.distributedVolume[i].revenue = Math.round(((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume)  * ((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit));
									if(isNaN(child.distributedVolume[i].revenue)){
										child.distributedVolume[i].revenue = 0;
									}
									Child2revenue = child.distributedVolume[i].revenue;
									break;
								case "1.1.3": //Large Server pricing
									child.distributedVolume[i].revenue = Math.round(((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume)  * ((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit));
									if(isNaN(child.distributedVolume[i].revenue)){
										child.distributedVolume[i].revenue = 0;
									}
									Child3revenue = child.distributedVolume[i].revenue;
									break;
				    			}
							}
							$scope.networkInfo.wanDevice.children[0].distributedVolume[i].revenue = Math.round(Child1revenue + Child2revenue + Child3revenue);
						}
					}
					else{
						for(var i=0;i< $scope.networkInfo.wanDevice.children[0].distributedVolume.length;i++){
							$scope.networkInfo.wanDevice.children[0].distributedVolume[i].revenue = Math.round($scope.networkInfo.wanDevice.children[0].distributedVolume[i].volume * $scope.networkInfo.wanDevice.children[0].distributedVolume[i].unit);
							if(isNaN($scope.networkInfo.wanDevice.children[0].distributedVolume[i].revenue)){
								$scope.networkInfo.wanDevice.children[0].distributedVolume[i].revenue = 0;
							}
						}
					}
				}
				if($scope.networkInfo.lanDevice.open == true){
					if($scope.networkInfo.lanDevice.children[0].open == true){
						for(var i=0;i< $scope.networkInfo.lanDevice.children[0].distributedVolume.length;i++){
							var Child1revenue = "";
							var Child2revenue = "";
							var Child3revenue = "";
							for(var k = 0;k < $scope.networkInfo.lanDevice.children[0].children.length;k++){
								child = $scope.networkInfo.lanDevice.children[0].children[k];
				    			switch(child.id) {
				    			case "2.1": //Lan
									break;
								case "2.1.1": //Small Server pricing
									child.distributedVolume[i].revenue = Math.round(((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume)  * ((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit));
									if(isNaN(child.distributedVolume[i].revenue)){
										child.distributedVolume[i].revenue = 0;
									}
									Child1revenue = child.distributedVolume[i].revenue;
									break;
								case "2.1.2": //Medium Server pricing
									child.distributedVolume[i].revenue = Math.round(((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume)  * ((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit));
									if(isNaN(child.distributedVolume[i].revenue)){
										child.distributedVolume[i].revenue = 0;
									}
									Child2revenue = child.distributedVolume[i].revenue;
									break;
								case "2.1.3": //Large Server pricing
									child.distributedVolume[i].revenue = Math.round(((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume)  * ((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit));
									if(isNaN(child.distributedVolume[i].revenue)){
										child.distributedVolume[i].revenue = 0;
									}
									Child3revenue = child.distributedVolume[i].revenue;
									break;
				    			}
							}
							$scope.networkInfo.lanDevice.children[0].distributedVolume[i].revenue = Math.round(Child1revenue + Child2revenue + Child3revenue);
						}
					}
					else{
						for(var i=0;i< $scope.networkInfo.lanDevice.children[0].distributedVolume.length;i++){
							$scope.networkInfo.lanDevice.children[0].distributedVolume[i].revenue = Math.round($scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume * $scope.networkInfo.lanDevice.children[0].distributedVolume[i].unit);
							if(isNaN($scope.networkInfo.lanDevice.children[0].distributedVolume[i].revenue)){
								$scope.networkInfo.lanDevice.children[0].distributedVolume[i].revenue = 0;
							}
						}
					}
				}
				if($scope.networkInfo.wlanCon.open == true){
					for(var i=0;i< $scope.networkInfo.wlanCon.children[0].distributedVolume.length;i++){
						$scope.networkInfo.wlanCon.children[0].distributedVolume[i].revenue = Math.round($scope.networkInfo.wlanCon.children[0].distributedVolume[i].volume * $scope.networkInfo.wlanCon.children[0].distributedVolume[i].unit);
						if(isNaN($scope.networkInfo.wlanCon.children[0].distributedVolume[i].revenue)){
							$scope.networkInfo.wlanCon.children[0].distributedVolume[i].revenue = 0;
						}
					}
				}
				if($scope.networkInfo.wlanAccess.open == true){
					for(var i=0;i< $scope.networkInfo.wlanAccess.children[0].distributedVolume.length;i++){
						$scope.networkInfo.wlanAccess.children[0].distributedVolume[i].revenue = Math.round($scope.networkInfo.wlanAccess.children[0].distributedVolume[i].volume * $scope.networkInfo.wlanAccess.children[0].distributedVolume[i].unit);
						if(isNaN($scope.networkInfo.wlanAccess.children[0].distributedVolume[i].revenue)){
							$scope.networkInfo.wlanAccess.children[0].distributedVolume[i].revenue = 0;
						}
					}
				}
				if($scope.networkInfo.loadBalance.open == true){
					for(var i=0;i< $scope.networkInfo.loadBalance.children[0].distributedVolume.length;i++){
						$scope.networkInfo.loadBalance.children[0].distributedVolume[i].revenue = Math.round($scope.networkInfo.loadBalance.children[0].distributedVolume[i].volume * $scope.networkInfo.loadBalance.children[0].distributedVolume[i].unit);
						if(isNaN($scope.networkInfo.loadBalance.children[0].distributedVolume[i].revenue)){
							$scope.networkInfo.loadBalance.children[0].distributedVolume[i].revenue = 0;
						}
					}
				}
				if($scope.networkInfo.vpnIp.open == true){
					for(var i=0;i< $scope.networkInfo.vpnIp.children[0].distributedVolume.length;i++){
						$scope.networkInfo.vpnIp.children[0].distributedVolume[i].revenue = Math.round($scope.networkInfo.vpnIp.children[0].distributedVolume[i].volume * $scope.networkInfo.vpnIp.children[0].distributedVolume[i].unit);
						if(isNaN($scope.networkInfo.vpnIp.children[0].distributedVolume[i].revenue)){
							$scope.networkInfo.vpnIp.children[0].distributedVolume[i].revenue = 0;
						}
					}
				}
				if($scope.networkInfo.dnsService.open == true){
					for(var i=0;i< $scope.networkInfo.dnsService.children[0].distributedVolume.length;i++){
						$scope.networkInfo.dnsService.children[0].distributedVolume[i].revenue = Math.round($scope.networkInfo.dnsService.children[0].distributedVolume[i].volume * $scope.networkInfo.dnsService.children[0].distributedVolume[i].unit);
						if(isNaN($scope.networkInfo.dnsService.children[0].distributedVolume[i].revenue)){
							$scope.networkInfo.dnsService.children[0].distributedVolume[i].revenue = 0;
						}
					}
				}
				if($scope.networkInfo.firewalls.open == true){
					for(var i=0;i< $scope.networkInfo.firewalls.children[0].distributedVolume.length;i++){
						$scope.networkInfo.firewalls.children[0].distributedVolume[i].revenue = Math.round($scope.networkInfo.firewalls.children[0].distributedVolume[i].volume * $scope.networkInfo.firewalls.children[0].distributedVolume[i].unit);
						if(isNaN($scope.networkInfo.firewalls.children[0].distributedVolume[i].revenue)){
							$scope.networkInfo.firewalls.children[0].distributedVolume[i].revenue = 0;
						}
					}
				}
				if($scope.networkInfo.reservePro.open == true){
					for(var i=0;i< $scope.networkInfo.reservePro.children[0].distributedVolume.length;i++){
						$scope.networkInfo.reservePro.children[0].distributedVolume[i].revenue = Math.round($scope.networkInfo.reservePro.children[0].distributedVolume[i].volume * $scope.networkInfo.reservePro.children[0].distributedVolume[i].unit);
						if(isNaN($scope.networkInfo.reservePro.children[0].distributedVolume[i].revenue)){
							$scope.networkInfo.reservePro.children[0].distributedVolume[i].revenue = 0;
						}
					}
				}
			}
			if(value == 'unit'){
				if($scope.networkInfo.wanDevice.open == true){
					if($scope.networkInfo.wanDevice.children[0].open == true){
						for(var i=0;i< $scope.networkInfo.wanDevice.children[0].distributedVolume.length;i++){
							for(var k = 0;k < $scope.networkInfo.wanDevice.children[0].children.length;k++){
								child = $scope.networkInfo.wanDevice.children[0].children[k];
				    			switch(child.id) {
				    			case "1.1": //Wan
									break;
								case "1.1.1": //Small Server pricing
									child.distributedVolume[i].unit = ((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue) / ((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
									if(isNaN(child.distributedVolume[i].unit)){
										child.distributedVolume[i].unit = 0;
									}
									child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
									break;
								case "1.1.2": //Medium Server pricing
									child.distributedVolume[i].unit = ((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue) / ((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
									if(isNaN(child.distributedVolume[i].unit)){
										child.distributedVolume[i].unit = 0;
									}
									child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
									break;
								case "1.1.3": //Large Server pricing
									child.distributedVolume[i].unit = ((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue) / ((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
									if(isNaN(child.distributedVolume[i].unit)){
										child.distributedVolume[i].unit = 0;
									}
									child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
									break;
				    			}
							}
							$scope.networkInfo.wanDevice.children[0].distributedVolume[i].unit = ($scope.networkInfo.wanDevice.children[0].distributedVolume[i].revenue / $scope.networkInfo.wanDevice.children[0].distributedVolume[i].volume).toFixed(2);
							if(isNaN($scope.networkInfo.wanDevice.children[0].distributedVolume[i].unit)){
								$scope.networkInfo.wanDevice.children[0].distributedVolume[i].unit = 0.00;
							}
						}
					}
					else{
						for(var i=0;i< $scope.networkInfo.wanDevice.children[0].distributedVolume.length;i++){
							$scope.networkInfo.wanDevice.children[0].distributedVolume[i].unit = ($scope.networkInfo.wanDevice.children[0].distributedVolume[i].revenue / $scope.networkInfo.wanDevice.children[0].distributedVolume[i].volume).toFixed(2);
							if(isNaN($scope.networkInfo.wanDevice.children[0].distributedVolume[i].unit)){
								$scope.networkInfo.wanDevice.children[0].distributedVolume[i].unit = 0.00;
							}
						}
					}
				}
				if($scope.networkInfo.lanDevice.open == true){
					if($scope.networkInfo.lanDevice.children[0].open == true){
						for(var i=0;i< $scope.networkInfo.lanDevice.children[0].distributedVolume.length;i++){
							for(var k = 0;k < $scope.networkInfo.lanDevice.children[0].children.length;k++){
								child = $scope.networkInfo.lanDevice.children[0].children[k];
				    			switch(child.id) {
				    			case "2.1": //Lan
									break;
								case "2.1.1": //Small Server pricing
									child.distributedVolume[i].unit = ((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue) / ((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
									if(isNaN(child.distributedVolume[i].unit)){
										child.distributedVolume[i].unit = 0;
									}
									child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
									break;
								case "2.1.2": //Medium Server pricing
									child.distributedVolume[i].unit = ((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue) / ((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
									if(isNaN(child.distributedVolume[i].unit)){
										child.distributedVolume[i].unit = 0;
									}
									child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
									break;
								case "2.1.3": //Large Server pricing
									child.distributedVolume[i].unit = ((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue) / ((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
									if(isNaN(child.distributedVolume[i].unit)){
										child.distributedVolume[i].unit = 0;
									}
									child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
									break;
				    			}
							}
							$scope.networkInfo.lanDevice.children[0].distributedVolume[i].unit = ($scope.networkInfo.lanDevice.children[0].distributedVolume[i].revenue / $scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume).toFixed(2);
							if(isNaN($scope.networkInfo.lanDevice.children[0].distributedVolume[i].unit)){
								$scope.networkInfo.lanDevice.children[0].distributedVolume[i].unit = 0.00;
							}
						}
					}
					else{
						for(var i=0;i< $scope.networkInfo.lanDevice.children[0].distributedVolume.length;i++){
							$scope.networkInfo.lanDevice.children[0].distributedVolume[i].unit = ($scope.networkInfo.lanDevice.children[0].distributedVolume[i].revenue / $scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume).toFixed(2);
							if(isNaN($scope.networkInfo.lanDevice.children[0].distributedVolume[i].unit)){
								$scope.networkInfo.lanDevice.children[0].distributedVolume[i].unit = 0.00;
							}
						}
					}
				}
				if($scope.networkInfo.wlanCon.open == true){
					for(var i=0;i< $scope.networkInfo.wlanCon.children[0].distributedVolume.length;i++){
						$scope.networkInfo.wlanCon.children[0].distributedVolume[i].unit = ($scope.networkInfo.wlanCon.children[0].distributedVolume[i].revenue / $scope.networkInfo.wlanCon.children[0].distributedVolume[i].volume).toFixed(2);
						if(isNaN($scope.networkInfo.wlanCon.children[0].distributedVolume[i].unit)){
							$scope.networkInfo.wlanCon.children[0].distributedVolume[i].unit = 0.00;
						}
					}
				}
				if($scope.networkInfo.wlanAccess.open == true){
					for(var i=0;i< $scope.networkInfo.wlanAccess.children[0].distributedVolume.length;i++){
						$scope.networkInfo.wlanAccess.children[0].distributedVolume[i].unit = ($scope.networkInfo.wlanAccess.children[0].distributedVolume[i].revenue / $scope.networkInfo.wlanAccess.children[0].distributedVolume[i].volume).toFixed(2);
						if(isNaN($scope.networkInfo.wlanAccess.children[0].distributedVolume[i].unit)){
							$scope.networkInfo.wlanAccess.children[0].distributedVolume[i].unit = 0.00;
						}
					}
				}
				if($scope.networkInfo.loadBalance.open == true){
					for(var i=0;i< $scope.networkInfo.loadBalance.children[0].distributedVolume.length;i++){
						$scope.networkInfo.loadBalance.children[0].distributedVolume[i].unit = ($scope.networkInfo.loadBalance.children[0].distributedVolume[i].revenue / $scope.networkInfo.loadBalance.children[0].distributedVolume[i].volume).toFixed(2);
						if(isNaN($scope.networkInfo.loadBalance.children[0].distributedVolume[i].unit)){
							$scope.networkInfo.loadBalance.children[0].distributedVolume[i].unit = 0.00;
						}
					}
				}
				if($scope.networkInfo.vpnIp.open == true){
					for(var i=0;i< $scope.networkInfo.vpnIp.children[0].distributedVolume.length;i++){
						$scope.networkInfo.vpnIp.children[0].distributedVolume[i].unit = ($scope.networkInfo.vpnIp.children[0].distributedVolume[i].revenue / $scope.networkInfo.vpnIp.children[0].distributedVolume[i].volume).toFixed(2);
						if(isNaN($scope.networkInfo.vpnIp.children[0].distributedVolume[i].unit)){
							$scope.networkInfo.vpnIp.children[0].distributedVolume[i].unit = 0.00;
						}
					}
				}
				if($scope.networkInfo.dnsService.open == true){
					for(var i=0;i< $scope.networkInfo.dnsService.children[0].distributedVolume.length;i++){
						$scope.networkInfo.dnsService.children[0].distributedVolume[i].unit = ($scope.networkInfo.dnsService.children[0].distributedVolume[i].revenue / $scope.networkInfo.dnsService.children[0].distributedVolume[i].volume).toFixed(2);
						if(isNaN($scope.networkInfo.dnsService.children[0].distributedVolume[i].unit)){
							$scope.networkInfo.dnsService.children[0].distributedVolume[i].unit = 0.00;
						}
					}
				}
				if($scope.networkInfo.firewalls.open == true){
					for(var i=0;i< $scope.networkInfo.firewalls.children[0].distributedVolume.length;i++){
						$scope.networkInfo.firewalls.children[0].distributedVolume[i].unit = ($scope.networkInfo.wlanCon.children[0].distributedVolume[i].revenue / $scope.networkInfo.wlanCon.children[0].distributedVolume[i].volume).toFixed(2);
						if(isNaN($scope.networkInfo.firewalls.children[0].distributedVolume[i].unit)){
							$scope.networkInfo.firewalls.children[0].distributedVolume[i].unit = 0.00;
						}
					}
				}
				if($scope.networkInfo.reservePro.open == true){
					for(var i=0;i< $scope.networkInfo.reservePro.children[0].distributedVolume.length;i++){
						$scope.networkInfo.reservePro.children[0].distributedVolume[i].unit = ($scope.networkInfo.reservePro.children[0].distributedVolume[i].revenue / $scope.networkInfo.reservePro.children[0].distributedVolume[i].volume).toFixed(2);
						if(isNaN($scope.networkInfo.reservePro.children[0].distributedVolume[i].unit)){
							$scope.networkInfo.reservePro.children[0].distributedVolume[i].unit = 0.00;
						}
					}
				}
			}
		}

		//Save Network Info Data
		$scope.saveNetworkInfo = function(){
			$scope.getIndicator();
			setNetworkYearlyInfo();
			$scope.networkInfoDto = {
	    			offshoreAllowed : $scope.dealDetails.offshoreSelected == "Yes" ? true : false,
	    			includeHardware : $scope.dealDetails.hardwareIncludedSelected == "Yes" ? true : false,
	    			levelOfService :  $scope.dealDetails.standardwindowInfoSelected,
	    			selectedWanSolutionId : $scope.selectedSolutionWanId,
	    			selectedLanSolutionId : $scope.selectedSolutionLanId,
	    			dealId : $stateParams.dealId,
	    	    	levelIndicator:$scope.getIndicator(),
	    	    	towerArchitect : $scope.dealDetails.towerArchitect,
	    	    	networkYearlyDataInfoDtoList : $scope.networkInfo.networkYearlyDataInfoDtos,
	    	}
			if(($scope.networkInfo.wanDevice.open && !$scope.networkInfo.wanDevice.children[0].open)
					|| ($scope.networkInfo.lanDevice.open && !$scope.networkInfo.lanDevice.children[0].open)){
				$confirm({text: 'Default distribution setting has been applied to child levels'})
	 	        .then(function() {
	 				var url = endpointsurls.SAVE_NETWORK_INFO+$stateParams.dealId;
	 		        customInterceptor.postrequest(url,$scope.networkInfoDto).then(function successCallback(response) {
	 		        	$scope.isSaveNetwork= DealService.getter() || isSave;
	 		        	$scope.isSaveNetwork.networkS=true;
	 		    		DealService.setter($scope.isSaveNetwork);
	 		    		
	 		    		$scope.putIndicator();
	 				}, function errorCallback(response) {
	 					console.log(response.statusText);
	 					$alert({title:'Error',text: 'Failed to save data.'})});
	    	})}
			else{
				var url = endpointsurls.SAVE_NETWORK_INFO+$stateParams.dealId;
		        customInterceptor.postrequest(url,$scope.networkInfoDto).then(function successCallback(response) {
		        	$scope.isSaveNetwork= DealService.getter() || isSave;
		        	$scope.isSaveNetwork.networkS=true;
		    		DealService.setter($scope.isSaveNetwork);
		    		
		    		$scope.putIndicator();
				}, function errorCallback(response) {
					console.log(response.statusText);
					$alert({title:'Error',text: 'Failed to save data.'})
				});
			}
		}

		
		//putIndicator
	    $scope.putIndicator=function()
	    {
	    	$scope.submissionIndicator[4]=1;
	        var urlIndicator= endpointsurls.GENERic_INDICATOR+$stateParams.dealId+'?assessmentIndicator='+$scope.assessmentIndicator+'&submissionIndicator='+$scope.submissionIndicator.join(',');
	       customInterceptor.putrequestWithoutData(urlIndicator).then(function successCallback(response) {
	    	   $state.go('home.submission.genericSubmission.totalsSubmission',({dealId:$stateParams.dealId}));
	       });
	    }
		// get the Indicator value
	    $scope.getIndicator=function()
	    {
	    	var levelIndicator='';
	    	levelIndicator+=$scope.networkInfo.wanDevice.open?1:0;
	    	levelIndicator+=',';
	    	if($scope.networkInfo.wanDevice.open){
	    		levelIndicator+=$scope.networkInfo.wanDevice.children[0].open?1:0;
	        	levelIndicator+=',';
	    	}
	    	else{
	    		levelIndicator+='0,';
	    	}
	    	levelIndicator+=$scope.networkInfo.lanDevice.open?1:0;
	    	levelIndicator+=',';
	    	if($scope.networkInfo.lanDevice.open){
	    		levelIndicator+=$scope.networkInfo.lanDevice.children[0].open?1:0;
	        	levelIndicator+=',';
	    	}
	    	else{
	    		levelIndicator+='0,';
	    	}
	    	levelIndicator+=$scope.networkInfo.wlanCon.open?1:0;
	    	levelIndicator+=',';
	    	levelIndicator+=$scope.networkInfo.wlanAccess.open?1:0;
	    	levelIndicator+=',';
	    	levelIndicator+=$scope.networkInfo.loadBalance.open?1:0;
	    	levelIndicator+=',';
	    	levelIndicator+=$scope.networkInfo.vpnIp.open?1:0;
	    	levelIndicator+=',';
	    	levelIndicator+=$scope.networkInfo.dnsService.open?1:0;
	    	levelIndicator+=',';
	    	levelIndicator+=$scope.networkInfo.firewalls.open?1:0;
	    	levelIndicator+=',';
	    	levelIndicator+=$scope.networkInfo.reservePro.open?1:0;
	        return levelIndicator;
	    }

		// Extract data yearly wise
	    function setNetworkYearlyInfo() {
	    	var yearlyInfoList = [];
	    	for (var y = 0; y < $scope.dealInfo/12; y++) {
	    		var yearlyData = {};
	    		yearlyData.year = y+1;
	    		if($scope.networkInfo.wanDevice.open){
	    			yearlyData.wanDevices = $scope.networkInfo.wanDevice.children[0].distributedVolume[y].volume;
    				yearlyData.smallWanDevices = $scope.networkInfo.wanDevice.children[0].children[0].distributedVolume[y].volume;
            		yearlyData.mediumWanDevices = $scope.networkInfo.wanDevice.children[0].children[1].distributedVolume[y].volume;
            		yearlyData.largeWanDevices = $scope.networkInfo.wanDevice.children[0].children[2].distributedVolume[y].volume;
	    		}
	    		if($scope.networkInfo.lanDevice.open){
	    			yearlyData.lanDevices = $scope.networkInfo.lanDevice.children[0].distributedVolume[y].volume;
    				yearlyData.smallLanDevices = $scope.networkInfo.lanDevice.children[0].children[0].distributedVolume[y].volume;
            		yearlyData.mediumLanDevices = $scope.networkInfo.lanDevice.children[0].children[1].distributedVolume[y].volume;
            		yearlyData.largeLanDevices = $scope.networkInfo.lanDevice.children[0].children[2].distributedVolume[y].volume;
	    		}
	    		if($scope.networkInfo.wlanCon.open){
	    			yearlyData.wlanControllers = $scope.networkInfo.wlanCon.children[0].distributedVolume[y].volume;
	    		}
	    		if($scope.networkInfo.wlanAccess.open){
	    			yearlyData.wlanAccesspoint = $scope.networkInfo.wlanAccess.children[0].distributedVolume[y].volume;
	    		}
	    		if($scope.networkInfo.loadBalance.open){
	    			yearlyData.loadBalancers = $scope.networkInfo.loadBalance.children[0].distributedVolume[y].volume;
	    		}
	    		if($scope.networkInfo.vpnIp.open){
	    			yearlyData.vpnIpSec = $scope.networkInfo.vpnIp.children[0].distributedVolume[y].volume;
	    		}
	    		if($scope.networkInfo.dnsService.open){
	    			yearlyData.dnsDhcpService = $scope.networkInfo.dnsService.children[0].distributedVolume[y].volume;
	    		}
	    		if($scope.networkInfo.firewalls.open){
	    			yearlyData.firewalls = $scope.networkInfo.firewalls.children[0].distributedVolume[y].volume;
	    		}
	    		if($scope.networkInfo.reservePro.open){
	    			yearlyData.proxies = $scope.networkInfo.reservePro.children[0].distributedVolume[y].volume;
	    		}
	    		yearlyData.networkUnitPriceInfoDtoList= extractServerUnitPrice(y);
	    		yearlyData.networkRevenueInfoDtoList = extractServerRevenuePrice(y);
	    		yearlyInfoList.push(yearlyData);
	    	}
	    	$scope.networkInfo.networkYearlyDataInfoDtos = yearlyInfoList;
	    }

	    // extract server price in units
	    function extractServerUnitPrice(year){
	    	var unitPrice = [];
	    	var unitInfo={};

	    	if($scope.viewBy.type == 'unit'){
	    		if($scope.networkInfo.wanDevice.open){
	    			unitInfo.wanDevices = $scope.networkInfo.wanDevice.children[0].distributedVolume[year].unit;
    				unitInfo.smallWanDevices = $scope.networkInfo.wanDevice.children[0].children[0].distributedVolume[year].unit;
    	    		unitInfo.mediumWanDevices = $scope.networkInfo.wanDevice.children[0].children[1].distributedVolume[year].unit;
    	    		unitInfo.largeWanDevices = $scope.networkInfo.wanDevice.children[0].children[2].distributedVolume[year].unit;
	    		}
	    		if($scope.networkInfo.lanDevice.open){
	    			unitInfo.lanDevices = $scope.networkInfo.lanDevice.children[0].distributedVolume[year].unit;
    				unitInfo.smallLanDevices = $scope.networkInfo.lanDevice.children[0].children[0].distributedVolume[year].unit;
    	    		unitInfo.mediumLanDevices = $scope.networkInfo.lanDevice.children[0].children[1].distributedVolume[year].unit;
    	    		unitInfo.largeLanDevices = $scope.networkInfo.lanDevice.children[0].children[2].distributedVolume[year].unit;
	    		}
	    		if($scope.networkInfo.wlanCon.open){
	    			unitInfo.wlanControllers = $scope.networkInfo.wlanCon.children[0].distributedVolume[year].unit;
	    		}
	    		if($scope.networkInfo.wlanAccess.open){
	    			unitInfo.wlanAccesspoint = $scope.networkInfo.wlanAccess.children[0].distributedVolume[year].unit;
	    		}
	    		if($scope.networkInfo.loadBalance.open){
	    			unitInfo.loadBalancers = $scope.networkInfo.loadBalance.children[0].distributedVolume[year].unit;
	    		}
	    		if($scope.networkInfo.vpnIp.open){
	    			unitInfo.vpnIpSec = $scope.networkInfo.vpnIp.children[0].distributedVolume[year].unit;
	    		}
	    		if($scope.networkInfo.dnsService.open){
	    			unitInfo.dnsDhcpService = $scope.networkInfo.dnsService.children[0].distributedVolume[year].unit;
	    		}
	    		if($scope.networkInfo.firewalls.open){
	    			unitInfo.firewalls = $scope.networkInfo.firewalls.children[0].distributedVolume[year].unit;
	    		}
	    		if($scope.networkInfo.reservePro.open){
	    			unitInfo.proxies = $scope.networkInfo.reservePro.children[0].distributedVolume[year].unit;
	    		}
	    	}
	    	
	    	if($scope.viewBy.type == 'revenue'){
	    		if($scope.networkInfo.wanDevice.open){
	    			unitInfo.wanDevices = $scope.networkInfo.wanDevice.children[0].distributedVolume[year].revenue / $scope.networkInfo.wanDevice.children[0].distributedVolume[year].volume;
    				for(var k = 0;k < $scope.networkInfo.wanDevice.children[0].children.length;k++){
    		    		child = $scope.networkInfo.wanDevice.children[0].children[k];
    					switch(child.id) {
    					case "1.1": //Wan
    						break;
    					case "1.1.1": //Small Server pricing
    						unitInfo.smallWanDevices = child.distributedVolume[year].revenue / child.distributedVolume[year].volume;
    						break;
    					case "1.1.2": //Medium Server pricing
    						unitInfo.mediumWanDevices = child.distributedVolume[year].revenue / child.distributedVolume[year].volume;
    						break;
    					case "1.1.3": //Large Server pricing
    						unitInfo.largeWanDevices = child.distributedVolume[year].revenue / child.distributedVolume[year].volume;
    						break;
    					}
    		    	}
	    		}
	    		if($scope.networkInfo.lanDevice.open){
	    			unitInfo.lanDevices = $scope.networkInfo.lanDevice.children[0].distributedVolume[year].revenue / $scope.networkInfo.lanDevice.children[0].distributedVolume[year].volume;
    				for(var k = 0;k < $scope.networkInfo.lanDevice.children[0].children.length;k++){
    		    		child = $scope.networkInfo.lanDevice.children[0].children[k];
    					switch(child.id) {
    					case "2.1": //Lan
    						break;
    					case "2.1.1": //Small Server pricing
    						unitInfo.smallLanDevices = child.distributedVolume[year].revenue / child.distributedVolume[year].volume;
    						break;
    					case "2.1.2": //Medium Server pricing
    						unitInfo.mediumLanDevices = child.distributedVolume[year].revenue / child.distributedVolume[year].volume;
    						break;
    					case "2.1.3": //Large Server pricing
    						unitInfo.largeLanDevices = child.distributedVolume[year].revenue / child.distributedVolume[year].volume;
    						break;

    					}
    		    	}
	    		}
	    		if($scope.networkInfo.wlanCon.open){
	    			unitInfo.wlanControllers = $scope.networkInfo.wlanCon.children[0].distributedVolume[year].revenue / $scope.networkInfo.wlanCon.children[0].distributedVolume[year].volume;
	    		}
	    		if($scope.networkInfo.wlanAccess.open){
	    			unitInfo.wlanAccesspoint = $scope.networkInfo.wlanAccess.children[0].distributedVolume[year].revenue / $scope.networkInfo.wlanAccess.children[0].distributedVolume[year].volume;
	    		}
	    		if($scope.networkInfo.loadBalance.open){
	    			unitInfo.loadBalancers = $scope.networkInfo.loadBalance.children[0].distributedVolume[year].revenue / $scope.networkInfo.loadBalance.children[0].distributedVolume[year].volume;
	    		}
	    		if($scope.networkInfo.vpnIp.open){
	    			unitInfo.vpnIpSec = $scope.networkInfo.vpnIp.children[0].distributedVolume[year].revenue / $scope.networkInfo.vpnIp.children[0].distributedVolume[year].volume;
	    		}
	    		if($scope.networkInfo.dnsService.open){
	    			unitInfo.dnsDhcpService = $scope.networkInfo.dnsService.children[0].distributedVolume[year].revenue / $scope.networkInfo.dnsService.children[0].distributedVolume[year].volume;
	    		}
	    		if($scope.networkInfo.firewalls.open){
	    			unitInfo.firewalls = $scope.networkInfo.firewalls.children[0].distributedVolume[year].revenue / $scope.networkInfo.firewalls.children[0].distributedVolume[year].volume;
	    		}
	    		if($scope.networkInfo.reservePro.open){
	    			unitInfo.proxies = $scope.networkInfo.reservePro.children[0].distributedVolume[year].revenue / $scope.networkInfo.reservePro.children[0].distributedVolume[year].volume;
	    		}
	    	}
	    	
	    	unitPrice.push(unitInfo)
	    	return unitPrice;
	    }

	    // extract server price in revenue
	    function extractServerRevenuePrice(year){
	    	var revenue = [];
	    	var revenueInfo={};

	    	if($scope.viewBy.type == 'unit'){
	    		if($scope.networkInfo.wanDevice.open){
	    			revenueInfo.totalWanRevenue = Math.round($scope.networkInfo.wanDevice.children[0].distributedVolume[year].unit * $scope.networkInfo.wanDevice.children[0].distributedVolume[year].volume) * 12;
	    		}
	    		if($scope.networkInfo.lanDevice.open){
	    			revenueInfo.totalLanRevenue = Math.round($scope.networkInfo.lanDevice.children[0].distributedVolume[year].unit * $scope.networkInfo.lanDevice.children[0].distributedVolume[year].volume) * 12;
	    		}
	    		if($scope.networkInfo.wlanCon.open){
	    			revenueInfo.totalWlanControllersRevenue = Math.round($scope.networkInfo.wlanCon.children[0].distributedVolume[year].unit * $scope.networkInfo.wlanCon.children[0].distributedVolume[year].volume) * 12;
	    		}
	    		if($scope.networkInfo.wlanAccess.open){
	    			revenueInfo.totalWlanAccesspointRevenue = Math.round($scope.networkInfo.wlanAccess.children[0].distributedVolume[year].unit * $scope.networkInfo.wlanAccess.children[0].distributedVolume[year].volume) * 12;
	    		}
	    		if($scope.networkInfo.loadBalance.open){
	    			revenueInfo.totalLoadBalancersRevenue = Math.round($scope.networkInfo.loadBalance.children[0].distributedVolume[year].unit * $scope.networkInfo.loadBalance.children[0].distributedVolume[year].volume) * 12;
	    		}
	    		if($scope.networkInfo.vpnIp.open){
	    			revenueInfo.totalVpnIpSecRevenue = Math.round($scope.networkInfo.vpnIp.children[0].distributedVolume[year].unit * $scope.networkInfo.vpnIp.children[0].distributedVolume[year].volume) * 12;
	    		}
	    		if($scope.networkInfo.dnsService.open){
	    			revenueInfo.totalDnsDhcpServiceRevenue = Math.round($scope.networkInfo.dnsService.children[0].distributedVolume[year].unit * $scope.networkInfo.dnsService.children[0].distributedVolume[year].volume) * 12;
	    		}
	    		if($scope.networkInfo.firewalls.open){
	    			revenueInfo.totalFirewallsRevenue = Math.round($scope.networkInfo.firewalls.children[0].distributedVolume[year].unit * $scope.networkInfo.firewalls.children[0].distributedVolume[year].volume) * 12;
	    		}
	    		if($scope.networkInfo.reservePro.open){
	    			revenueInfo.totalProxiesRevenue = Math.round($scope.networkInfo.reservePro.children[0].distributedVolume[year].unit * $scope.networkInfo.reservePro.children[0].distributedVolume[year].volume) * 12;
	    		}
	    	}
	    	
	    	if($scope.viewBy.type == 'revenue'){
	    		if($scope.networkInfo.wanDevice.open){
	    			revenueInfo.totalWanRevenue = $scope.networkInfo.wanDevice.children[0].distributedVolume[year].revenue * 12;
	    		}
	    		if($scope.networkInfo.lanDevice.open){
	    			revenueInfo.totalLanRevenue = $scope.networkInfo.lanDevice.children[0].distributedVolume[year].revenue * 12;
	    		}
	    		if($scope.networkInfo.wlanCon.open){
	    			revenueInfo.totalWlanControllersRevenue = $scope.networkInfo.wlanCon.children[0].distributedVolume[year].revenue * 12;
	    		}
	    		if($scope.networkInfo.wlanAccess.open){
	    			revenueInfo.totalWlanAccesspointRevenue = $scope.networkInfo.wlanAccess.children[0].distributedVolume[year].revenue * 12;
	    		}
	    		if($scope.networkInfo.loadBalance.open){
	    			revenueInfo.totalLoadBalancersRevenue = $scope.networkInfo.loadBalance.children[0].distributedVolume[year].revenue * 12;
	    		}
	    		if($scope.networkInfo.vpnIp.open){
	    			revenueInfo.totalVpnIpSecRevenue = $scope.networkInfo.vpnIp.children[0].distributedVolume[year].revenue * 12;
	    		}
	    		if($scope.networkInfo.dnsService.open){
	    			revenueInfo.totalDnsDhcpServiceRevenue = $scope.networkInfo.dnsService.children[0].distributedVolume[year].revenue * 12;
	    		}
	    		if($scope.networkInfo.firewalls.open){
	    			revenueInfo.totalFirewallsRevenue = $scope.networkInfo.firewalls.children[0].distributedVolume[year].revenue * 12;
	    		}
	    		if($scope.networkInfo.reservePro.open){
	    			revenueInfo.totalProxiesRevenue = $scope.networkInfo.reservePro.children[0].distributedVolume[year].revenue * 12;
	    		}
	    	}
	    	
	    	revenue.push(revenueInfo)
	    	return revenue;
	    }

		//Todo modal for find deal section
		var dialogOptions = {
		    controller: 'ContactCtrl',
		    templateUrl: '/modals/contactInformation/contactInformation.html'
		  };
		var scope=$scope;
		  $scope.edit = function(searchModel,modelName){
		    $dialog.dialog(angular.extend(dialogOptions, {resolve: {searchModel: searchModel,scope:scope,modelName:modelName}}))
		      .open()
		      .then(function(result) {
		        if(result) {
		          angular.copy(result, searchModel);
		        }
		        searchModel = undefined;
		    });
		  };


});

