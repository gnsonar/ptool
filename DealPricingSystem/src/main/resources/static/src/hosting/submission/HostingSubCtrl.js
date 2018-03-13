priceToolcontrollers.controller('HostingSubCtrl',function($scope,$http,HostingService,EndUserService,StorageService,DealService,NetworkService,customInterceptor,endpointsurls,$stateParams,$alert,$dialog,$state,$confirm) {
	
	var ctrl = this;
	ctrl.active = 0;
	$scope.pastAvgUnitPriceList=[];
	$scope.compAvgUnitPriceList=[];
	$scope.benchmarkLowAvgUnitPriceList=[];
	$scope.benchmarkTargetAvgUnitPriceList=[];
    $scope.hostingCalculateDto = [];
    $scope.dealInfo={};
    $scope.dealDetails = {};
	$scope.solList = [];
	$scope.selectedSolutionId = '';
	$scope.solution = {};
	$scope.hostingInfoDto = {};
	$scope.existingHostingInfo = {};
	$scope.viewBy = {type:'unit'};
	$scope.platformViewBy = {type:'unit'};
	$scope.showmsg = false;
	$scope.custom=false;
	$scope.showvalidationmsg = false;
	
	
	
	$scope.platformHosting = {
			open : false
	};

	$scope.serverHosting = {
			open : false
	};

	$scope.solSetting = {
			open : false
	};
	
	// function for togel panel
	$scope.togglePanel = function(level) {
		if(level.open){
			level.open=false;
		}else
		{
			level.open=true;
		}
	};
	
	// function for catching tab event solution
	$scope.tabEvent=function($event,index,val,childindex,grandchildindex,ggcindex,sgcIndex){
		if($event.keyCode == 13 || $event.keyCode == 9){
			switch(val) {
			case "ServerVolume":
				for(var i=index;i<$scope.hostingInfo.serverHosting.children[0].distributedVolume.length;i++){
					$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume = $scope.hostingInfo.serverHosting.children[0].distributedVolume[index].volume;
				}
				break;
			case "ServerPricingRevenue":
				for(var i=index;i<$scope.hostingInfo.serverHosting.children[0].distributedVolume.length;i++){
					$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].revenue = $scope.hostingInfo.serverHosting.children[0].distributedVolume[index].revenue;
				}
				break;
			case "ServerPricingUnit":
				for(var i=index;i<$scope.hostingInfo.serverHosting.children[0].distributedVolume.length;i++){
					$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].unit = $scope.hostingInfo.serverHosting.children[0].distributedVolume[index].unit;
				}
				break;
			case "ServerChildPricingRevenue":
				for(var i=index;i<$scope.hostingInfo.serverHosting.children[0].distributedVolume.length;i++){
					$scope.hostingInfo.serverHosting.children[0].children[childindex].distributedVolume[i].revenue = $scope.hostingInfo.serverHosting.children[0].children[childindex].distributedVolume[index].revenue;
				}
				break;
			case "ServerChildPricingUnit":
				for(var i=index;i<$scope.hostingInfo.serverHosting.children[0].distributedVolume.length;i++){
					$scope.hostingInfo.serverHosting.children[0].children[childindex].distributedVolume[i].unit = $scope.hostingInfo.serverHosting.children[0].children[childindex].distributedVolume[index].unit;
				}
				break;
			case "ServerGrandChildPricingRevenue":
				for(var i=index;i<$scope.hostingInfo.serverHosting.children[0].distributedVolume.length;i++){
					$scope.hostingInfo.serverHosting.children[0].children[childindex].children[grandchildindex].distributedVolume[i].revenue = $scope.hostingInfo.serverHosting.children[0].children[childindex].children[grandchildindex].distributedVolume[index].revenue;
				}
				break;
			case "ServerGrandChildPricingUnit":
				for(var i=index;i<$scope.hostingInfo.serverHosting.children[0].distributedVolume.length;i++){
					$scope.hostingInfo.serverHosting.children[0].children[childindex].children[grandchildindex].distributedVolume[i].unit = $scope.hostingInfo.serverHosting.children[0].children[childindex].children[grandchildindex].distributedVolume[index].unit;
				}
				break;
			case "ServerGGCPricingRevenue":
				for(var i=index;i<$scope.hostingInfo.serverHosting.children[0].distributedVolume.length;i++){
					$scope.hostingInfo.serverHosting.children[0].children[childindex].children[grandchildindex].children[ggcindex].distributedVolume[i].revenue = $scope.hostingInfo.serverHosting.children[0].children[childindex].children[grandchildindex].children[ggcindex].distributedVolume[index].revenue;
				}
				break;
			case "ServerGGCPricingUnit":
				for(var i=index;i<$scope.hostingInfo.serverHosting.children[0].distributedVolume.length;i++){
					$scope.hostingInfo.serverHosting.children[0].children[childindex].children[grandchildindex].children[ggcindex].distributedVolume[i].unit = $scope.hostingInfo.serverHosting.children[0].children[childindex].children[grandchildindex].children[ggcindex].distributedVolume[index].unit;
				}
				break;
			case "ServerSGCPricingRevenue":
				for(var i=index;i<$scope.hostingInfo.serverHosting.children[0].distributedVolume.length;i++){
					$scope.hostingInfo.serverHosting.children[0].children[childindex].children[grandchildindex].children[ggcindex].children[sgcIndex].distributedVolume[i].revenue = $scope.hostingInfo.serverHosting.children[0].children[childindex].children[grandchildindex].children[ggcindex].children[sgcIndex].distributedVolume[index].revenue;
				}
				break;
			case "ServerSGCPricingUnit":
				for(var i=index;i<$scope.hostingInfo.serverHosting.children[0].distributedVolume.length;i++){
					$scope.hostingInfo.serverHosting.children[0].children[childindex].children[grandchildindex].children[ggcindex].children[sgcIndex].distributedVolume[i].unit = $scope.hostingInfo.serverHosting.children[0].children[childindex].children[grandchildindex].children[ggcindex].children[sgcIndex].distributedVolume[index].unit;
				}
				break;
			case "PlatformChildVolume":
				for(var i=index;i<$scope.hostingInfo.platformHosting.distributedVolume.length;i++){
					$scope.hostingInfo.platformHosting.children[childindex].distributedVolume[i].volume = $scope.hostingInfo.platformHosting.children[childindex].distributedVolume[index].volume;
				}
				break;
			case "PlatformChildPricingRevenue":
				for(var i=index;i<$scope.hostingInfo.platformHosting.children[0].distributedVolume.length;i++){
					$scope.hostingInfo.platformHosting.children[childindex].distributedVolume[i].revenue = $scope.hostingInfo.platformHosting.children[childindex].distributedVolume[index].revenue;
				}
				break;
			case "PlatformChildPricingUnit":
				for(var i=index;i<$scope.hostingInfo.platformHosting.children[0].distributedVolume.length;i++){
					$scope.hostingInfo.platformHosting.children[childindex].distributedVolume[i].unit = $scope.hostingInfo.platformHosting.children[childindex].distributedVolume[index].unit;
				}
				break;
			}
		}
	}
	
	// get the dropdown values
	$scope.getHostingDropdowns=function(){
		var url = endpointsurls.HOSTING_DROPDOWN+$stateParams.dealId;
        customInterceptor.getrequest(url).then(function successCallback(response) {
			$scope.solList = response.data.hostingSolutionsDtoList;
			$scope.genericDealInfoDto = response.data;
			$scope.dealInfoDto=response.data.dealInfoDto;
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
            $scope.getDealDeatils();

		}, function errorCallback(response) {
			console.log(response.statusText);
			if(response.status=='401'){
				$state.go('login');
			}
		});
	}

	//get default  data of generic screen
	$scope.getDealDeatils=function(){
        $scope.dealDetails.offshoreSelected= $scope.dealInfoDto.offshoreAllowed;
        $scope.dealDetails.standardwindowInfoSelected= $scope.dealInfoDto.serviceWindowSla;
        $scope.dealDetails.hardwareIncludedSelected= $scope.dealInfoDto.includeHardware;
        $scope.dealInfo= $scope.dealInfoDto.dealTerm;
        $scope.hostingInfo = HostingService.initHostingDetails($scope.dealInfo);
        if($stateParams.dealId>0){
			 var url = endpointsurls.SAVE_HOSTING_INFO+ $stateParams.dealId;
		        customInterceptor.getrequest(url).then(function successCallback(response) {
	       		$scope.existingHostingInfo = response.data;
	       		$scope.id=response.data.hostingId;
	       		$scope.level=$scope.existingHostingInfo.levelIndicator.split(',');
	       		$scope.hostingInfo.serverHosting.open=($scope.level[0]==1)?true:false;
	        	$scope.hostingInfo.serverHosting.children[0].open=($scope.level[1]==1)?true:false;
	        	$scope.hostingInfo.serverHosting.children[0].children[0].open=($scope.level[2]==1)?true:false;
	        	$scope.hostingInfo.serverHosting.children[0].children[0].children[0].open=($scope.level[3]==1)?true:false;
	        	$scope.hostingInfo.serverHosting.children[0].children[0].children[1].open=($scope.level[4]==1)?true:false;
	        	$scope.hostingInfo.serverHosting.children[0].children[1].open=($scope.level[5]==1)?true:false;
	        	$scope.hostingInfo.serverHosting.children[0].children[1].children[0].open=($scope.level[6]==1)?true:false;
	        	$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].open=($scope.level[7]==1)?true:false;
	        	$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].open=($scope.level[8]==1)?true:false;
	        	$scope.hostingInfo.serverHosting.children[0].children[1].children[1].open=($scope.level[9]==1)?true:false;
	        	$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].open=($scope.level[10]==1)?true:false;
	        	$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].open=($scope.level[11]==1)?true:false;
	        	$scope.hostingInfo.platformHosting.open=($scope.level[12]==1)?true:false;
	       		if($scope.existingHostingInfo != null && $scope.id!=null){
	       			mapExistingHostingInfo();
	       		}
	        }, function errorCallback(response) {
	        	$scope.id=undefined;
	        });
		}
	}

	//function for init controller
	$scope.init=function(){
		$scope.custom=false;
		$scope.getHostingDropdowns();
	}
	$scope.init();
	
	// map existing Hosting Info
    function mapExistingHostingInfo() {
    	$scope.dealDetails.offshoreSelected= $scope.existingHostingInfo.offshoreAllowed == true ? "Yes" : "No";
    	$scope.dealDetails.hardwareIncludedSelected = $scope.existingHostingInfo.includeHardware == true ? "Yes" : "No";
    	$scope.dealDetails.tooling = $scope.existingHostingInfo.includeTooling == true ? "Yes" : "No";
   		$scope.dealDetails.standardwindowInfoSelected= $scope.existingHostingInfo.levelOfService;
		$scope.dealDetails.colocation = $scope.existingHostingInfo.coLocation;
   		$scope.dealDetails.towerArchitect = $scope.existingHostingInfo.towerArchitect;
   		$scope.selectedSolutionId = $scope.existingHostingInfo.selectedSolutionId;
   		getExistingHostingYearlyInfo();
   		if($scope.selectedSolutionId != null){
   			var CustomSolutionName=_.where($scope.solList, {solutionId: $scope.selectedSolutionId})[0].solutionName;
   	   		if(CustomSolutionName!='Custom'){
   	   		$scope.onChangeDistSetting($scope.selectedSolutionId,'map');
   	   		}
   		} 		
    }
    
    // map Existing Hosting Yearly Info
    function getExistingHostingYearlyInfo() {
    	if($scope.existingHostingInfo.hostingYearlyDataInfoDtoList != null){
    		for (var y = 0; y < $scope.existingHostingInfo.hostingYearlyDataInfoDtoList.length; y++){
        		var yearlyDto = $scope.existingHostingInfo.hostingYearlyDataInfoDtoList[y];
        		$scope.hostingInfo.serverHosting.children[0].distributedVolume[y].year = yearlyDto.year;
        		$scope.hostingInfo.serverHosting.children[0].distributedVolume[y].volume = yearlyDto.servers;
        		$scope.hostingInfo.serverHosting.children[0].children[0].distributedVolume[y].volume = yearlyDto.physical;
        		$scope.hostingInfo.serverHosting.children[0].children[0].children[0].distributedVolume[y].volume = yearlyDto.physicalWin;
        		$scope.hostingInfo.serverHosting.children[0].children[0].children[0].children[0].distributedVolume[y].volume = yearlyDto.physicalWinSmall;
        		$scope.hostingInfo.serverHosting.children[0].children[0].children[0].children[1].distributedVolume[y].volume = yearlyDto.physicalWinMedium;
        		$scope.hostingInfo.serverHosting.children[0].children[0].children[0].children[2].distributedVolume[y].volume = yearlyDto.physicalWinLarge;
        		$scope.hostingInfo.serverHosting.children[0].children[0].children[1].distributedVolume[y].volume = yearlyDto.physicalUnix;
        		$scope.hostingInfo.serverHosting.children[0].children[0].children[1].children[0].distributedVolume[y].volume = yearlyDto.physicalUnixSmall;
        		$scope.hostingInfo.serverHosting.children[0].children[0].children[1].children[1].distributedVolume[y].volume = yearlyDto.physicalUnixMedium;
        		$scope.hostingInfo.serverHosting.children[0].children[0].children[1].children[2].distributedVolume[y].volume = yearlyDto.physicalUnixLarge;
        		$scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[y].volume = yearlyDto.virtual;
        		$scope.hostingInfo.serverHosting.children[0].children[1].children[0].distributedVolume[y].volume = yearlyDto.virtualPublic;
        		$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].distributedVolume[y].volume = yearlyDto.virtualPublicWin;
        		$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].children[0].distributedVolume[y].volume = yearlyDto.virtualPublicWinSmall;
        		$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].children[1].distributedVolume[y].volume = yearlyDto.virtualPublicWinMedium;
        		$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].children[2].distributedVolume[y].volume = yearlyDto.virtualPublicWinLarge;
        		$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].distributedVolume[y].volume = yearlyDto.virtualPublicUnix;
        		$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].children[0].distributedVolume[y].volume = yearlyDto.virtualPublicUnixSmall;
        		$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].children[1].distributedVolume[y].volume = yearlyDto.virtualPublicUnixMedium;
        		$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].children[2].distributedVolume[y].volume = yearlyDto.virtualPublicUnixLarge;
        		$scope.hostingInfo.serverHosting.children[0].children[1].children[1].distributedVolume[y].volume = yearlyDto.virtualPrivate;
        		$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].distributedVolume[y].volume = yearlyDto.virtualPrivateWin;
        		$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].children[0].distributedVolume[y].volume = yearlyDto.virtualPrivateWinSmall;
        		$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].children[1].distributedVolume[y].volume = yearlyDto.virtualPrivateWinMedium;
        		$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].children[2].distributedVolume[y].volume = yearlyDto.virtualPrivateWinLarge;
        		$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].distributedVolume[y].volume = yearlyDto.virtualPrivateUnix;
        		$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].children[0].distributedVolume[y].volume = yearlyDto.virtualPrivateUnixSmall;
        		$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].children[1].distributedVolume[y].volume = yearlyDto.virtualPrivateUnixMedium;
        		$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].children[2].distributedVolume[y].volume = yearlyDto.virtualPrivateUnixLarge;
        		$scope.hostingInfo.platformHosting.children[0].distributedVolume[y].volume = yearlyDto.sqlInstances;
        		$scope.hostingInfo.platformHosting.children[1].distributedVolume[y].volume = yearlyDto.cotsInstallations;
        		getExistingServerPricingLevelWiseInfo(yearlyDto,y);
        	}
    	}
    }
    
    // Get existing Hosting server price level wise
	function getExistingServerPricingLevelWiseInfo(yearlyDto,year){
		if(yearlyDto.hostingUnitPriceInfoDtoList != null){
			if(yearlyDto.hostingUnitPriceInfoDtoList.length > 0){
				  if($scope.viewBy.type == 'unit'){
					  $scope.hostingInfo.serverHosting.children[0].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].servers;
		        		$scope.hostingInfo.serverHosting.children[0].children[0].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].physical;
		        		$scope.hostingInfo.serverHosting.children[0].children[0].children[0].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].physicalWin;
		        		$scope.hostingInfo.serverHosting.children[0].children[0].children[0].children[0].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].physicalWinSmall;
		        		$scope.hostingInfo.serverHosting.children[0].children[0].children[0].children[1].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].physicalWinMedium;
		        		$scope.hostingInfo.serverHosting.children[0].children[0].children[0].children[2].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].physicalWinLarge;
		        		$scope.hostingInfo.serverHosting.children[0].children[0].children[1].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].physicalUnix;
		        		$scope.hostingInfo.serverHosting.children[0].children[0].children[1].children[0].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].physicalUnixSmall;
		        		$scope.hostingInfo.serverHosting.children[0].children[0].children[1].children[1].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].physicalUnixMedium;
		        		$scope.hostingInfo.serverHosting.children[0].children[0].children[1].children[2].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].physicalUnixLarge;
		        		$scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].virtual;
		        		$scope.hostingInfo.serverHosting.children[0].children[1].children[0].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].virtualPublic;
		        		$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].virtualPublicWin;
		        		$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].children[0].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].virtualPublicWinSmall;
		        		$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].children[1].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].virtualPublicWinMedium;
		        		$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].children[2].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].virtualPublicWinLarge;
		        		$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].virtualPublicUnix;
		        		$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].children[0].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].virtualPublicUnixSmall;
		        		$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].children[1].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].virtualPublicUnixMedium;
		        		$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].children[2].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].virtualPublicUnixLarge;
		        		$scope.hostingInfo.serverHosting.children[0].children[1].children[1].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].virtualPrivate;
		        		$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].virtualPrivateWin;
		        		$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].children[0].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].virtualPrivateWinSmall;
		        		$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].children[1].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].virtualPrivateWinMedium;
		        		$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].children[2].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].virtualPrivateWinLarge;
		        		$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].virtualPrivateUnix;
		        		$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].children[0].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].virtualPrivateUnixSmall;
		        		$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].children[1].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].virtualPrivateUnixMedium;
		        		$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].children[2].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].virtualPrivateUnixLarge;
		        		$scope.hostingInfo.platformHosting.children[0].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].sqlInstances;
		        		$scope.hostingInfo.platformHosting.children[1].distributedVolume[year].unit = yearlyDto.hostingUnitPriceInfoDtoList[0].cotsInstallations;
				}
			}
		}
	}
	
	//Distribution Setting Drop-down Selected
	$scope.onChangeDistSetting = function(solId,map) {
		$scope.showErr=false;
		$scope.showVolumeErr=false;
		$scope.customSol = false;
		$scope.selectedSolutionId = solId;
    	$scope.Solution = _.where($scope.solList, {solutionId: solId});
    	setLevelWisePercentage($scope.hostingInfo.serverHosting,map);
	};
	
	function setLevelWisePercentage(parent,map){
		if(parent.children != null){
			for(var i = 0;i < parent.children[0].children.length;i++){
				var child = parent.children[0].children[i];
				switch(child.id) {
				case "1.1.1":
					child.percentage =  $scope.Solution? $scope.Solution[0].physicalPerc : "";
					for(var j = 0;j < child.children.length;j++){
						var grandchild = child.children[j];
						switch(grandchild.id) {
						case "1.1.1.1":
							grandchild.percentage =  $scope.Solution? $scope.Solution[0].winLinuxPerc : "";
							for(var k = 0;k < grandchild.children.length;k++){
								var greatgrandchild = grandchild.children[k];
								switch(greatgrandchild.id) {
								case "1.1.1.1.1":
									greatgrandchild.percentage = $scope.Solution? $scope.Solution[0].smallPerc : "";break;
								case "1.1.1.1.2":
									greatgrandchild.percentage = $scope.Solution? $scope.Solution[0].mediumPerc : "";break;
								case "1.1.1.1.3":
									greatgrandchild.percentage = $scope.Solution? $scope.Solution[0].largePerc : "";break;
								}								
							}
							break;
						case "1.1.1.2":
							grandchild.percentage =  $scope.Solution? $scope.Solution[0].unixPerc :"";
							for(var k = 0;k < grandchild.children.length;k++){
								var greatgrandchild = grandchild.children[k];
								switch(greatgrandchild.id) {
								case "1.1.1.2.1":
									greatgrandchild.percentage = $scope.Solution? $scope.Solution[0].smallPerc : "";break;
								case "1.1.1.2.2":
									greatgrandchild.percentage = $scope.Solution? $scope.Solution[0].mediumPerc : "";break;
								case "1.1.1.2.3":
									greatgrandchild.percentage = $scope.Solution? $scope.Solution[0].largePerc : "";break;
								}								
							}
							break;
						}
					}
					break;
				case "1.1.2":
					child.percentage =  $scope.Solution? $scope.Solution[0].virtualPerc :"";
					for(var j = 0;j < child.children.length;j++){
						var grandchild = child.children[j];
						switch(grandchild.id) {
						case "1.1.2.1":
							grandchild.percentage =  $scope.Solution? $scope.Solution[0].publicPerc : "";
							for(var k = 0;k < grandchild.children.length;k++){
								var greatgrandchild = grandchild.children[k];
								switch(greatgrandchild.id) {
								case "1.1.2.1.1":
									greatgrandchild.percentage = $scope.Solution? $scope.Solution[0].winLinuxPerc : "";
									for(var l = 0;l < greatgrandchild.children.length;l++){
										var greatgreatgrandchild = greatgrandchild.children[l];
										switch(greatgreatgrandchild.id) {
										case "1.1.2.1.1.1":
											greatgreatgrandchild.percentage = $scope.Solution? $scope.Solution[0].smallPerc : "";break;
										case "1.1.2.1.1.2":
											greatgreatgrandchild.percentage = $scope.Solution? $scope.Solution[0].mediumPerc : "";break;
										case "1.1.2.1.1.3":
											greatgreatgrandchild.percentage = $scope.Solution? $scope.Solution[0].largePerc : "";break;
										}
									}
									break;
								case "1.1.2.1.2":
									greatgrandchild.percentage = $scope.Solution? $scope.Solution[0].unixPerc : "";
									for(var l = 0;l < greatgrandchild.children.length;l++){
										var greatgreatgrandchild = greatgrandchild.children[l];
										switch(greatgreatgrandchild.id) {
										case "1.1.2.1.2.1":
											greatgreatgrandchild.percentage = $scope.Solution? $scope.Solution[0].smallPerc : "";break;
										case "1.1.2.1.2.2":
											greatgreatgrandchild.percentage = $scope.Solution? $scope.Solution[0].mediumPerc : "";break;
										case "1.1.2.1.2.3":
											greatgreatgrandchild.percentage = $scope.Solution? $scope.Solution[0].largePerc : "";break;
										}
									}
									break;
								}								
							}
							break;
						case "1.1.2.2":
							grandchild.percentage =  $scope.Solution? $scope.Solution[0].privatePerc :"";
							for(var k = 0;k < grandchild.children.length;k++){
								var greatgrandchild = grandchild.children[k];
								switch(greatgrandchild.id) {
								case "1.1.2.2.1":
									greatgrandchild.percentage = $scope.Solution? $scope.Solution[0].winLinuxPerc : "";
									for(var l = 0;l < greatgrandchild.children.length;l++){
										var greatgreatgrandchild = greatgrandchild.children[l];
										switch(greatgreatgrandchild.id) {
										case "1.1.2.2.1.1":
											greatgreatgrandchild.percentage = $scope.Solution? $scope.Solution[0].smallPerc : "";break;
										case "1.1.2.2.1.2":
											greatgreatgrandchild.percentage = $scope.Solution? $scope.Solution[0].mediumPerc : "";break;
										case "1.1.2.2.1.3":
											greatgreatgrandchild.percentage = $scope.Solution? $scope.Solution[0].largePerc : "";break;
										}
									}
									break;
								case "1.1.2.2.2":
									greatgrandchild.percentage = $scope.Solution? $scope.Solution[0].unixPerc : "";
									for(var l = 0;l < greatgrandchild.children.length;l++){
										var greatgreatgrandchild = greatgrandchild.children[l];
										switch(greatgreatgrandchild.id) {
										case "1.1.2.2.2.1":
											greatgreatgrandchild.percentage = $scope.Solution? $scope.Solution[0].smallPerc : "";break;
										case "1.1.2.2.2.2":
											greatgreatgrandchild.percentage = $scope.Solution? $scope.Solution[0].mediumPerc : "";break;
										case "1.1.2.2.2.3":
											greatgreatgrandchild.percentage = $scope.Solution? $scope.Solution[0].largePerc : "";break;
										}
									}
									break;
								}								
							}
							break;
						}
					}
					break;
				}
			}
			$scope.calculateHostingVolume($scope.hostingInfo.serverHosting.children[0],map);
		}
	}
	
	$scope.calculateHostingVolume = function (parent,map){
		if(parent.children != null){
			for(var i = 0; i < $scope.dealInfo/12; i++){
				for(var j = 0;j < parent.children.length;j++){
					var child = parent.children[j];
					if(child.percentage != undefined){
						child.distributedVolume[i].volume = Math.round((parent.distributedVolume[i].volume * child.percentage)/100);
					}					
					if(child.children.length > 0){
						for(var k = 0;k < child.children.length;k++){
							var grandchild = child.children[k];
							if(grandchild.percentage != undefined){
								grandchild.distributedVolume[i].volume = Math.round((child.distributedVolume[i].volume * grandchild.percentage)/100);
							}							
							if(grandchild.children.length > 0){
								for(var l = 0;l < grandchild.children.length;l++){
									var greatgrandchild = grandchild.children[l];
									if(greatgrandchild.percentage != undefined){
										greatgrandchild.distributedVolume[i].volume = Math.round((grandchild.distributedVolume[i].volume * greatgrandchild.percentage)/100);
									}							
									if(greatgrandchild.children.length > 0){
										for(var m = 0;m < greatgrandchild.children.length;m++){
											var greatgreatgrandchild = greatgrandchild.children[m];
											if(greatgreatgrandchild.percentage != undefined){
												greatgreatgrandchild.distributedVolume[i].volume = Math.round((greatgrandchild.distributedVolume[i].volume * greatgreatgrandchild.percentage)/100);
											}		
										}
									}					
								}
							}
						}
					}										
				}
			}
			if(map!='map'){
			$scope.calcServerPrice(parent);
			}
		}
	}
	
	// calculate parent server price when user insert child server pricing
	$scope.calcServerPrice = function (parent){
		if($scope.viewBy.type == 'unit'){
			for(var i=0;i< parent.distributedVolume.length;i++){
				var child1volume = "";
				var child2volume = "";
				var child1unit = "";
				var child2unit = "";
				for(var j = 0;j < parent.children.length;j++){
					var child = parent.children[j];
					switch(child.id) {
					case "1.1.1": //Physical Server Pricing
						if(child.open == true){
							var grandchild1volume = "";
							var grandchild2volume = "";
							var grandchild1unit = "";
							var grandchild2unit = "";
							for(var k=0;k< child.children.length;k++){
								var grandchild = child.children[k];
								switch(grandchild.id){
								case "1.1.1.1": //Windows/Linux Server Pricing
									if(grandchild.open == true){
										var greatgrandchild1volume = "";
										var greatgrandchild2volume = "";
										var greatgrandchild3volume = "";
										var greatgrandchild1unit = "";
										var greatgrandchild2unit = "";
										var greatgrandchild3unit = "";
										for(var l=0;l< grandchild.children.length;l++){
											var greatgrandchild = grandchild.children[l];
											switch(greatgrandchild.id) {
											case "1.1.1.1.1": //Small Server Pricing
												greatgrandchild1volume = parseInt((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume);
												greatgrandchild1unit = parseFloat((greatgrandchild.distributedVolume[i].unit=='' || greatgrandchild.distributedVolume[i].unit==undefined || greatgrandchild.distributedVolume[i].unit==null) ?0:greatgrandchild.distributedVolume[i].unit);
												break;
											case "1.1.1.1.2": //Medium Server Pricing
												greatgrandchild2volume = parseInt((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume);
												greatgrandchild2unit = parseFloat((greatgrandchild.distributedVolume[i].unit=='' || greatgrandchild.distributedVolume[i].unit==undefined || greatgrandchild.distributedVolume[i].unit==null) ?0:greatgrandchild.distributedVolume[i].unit);
												break;
											case "1.1.1.1.3": //Large Server Pricing
												greatgrandchild3volume = parseInt((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume);
												greatgrandchild3unit = parseFloat((greatgrandchild.distributedVolume[i].unit=='' || greatgrandchild.distributedVolume[i].unit==undefined || greatgrandchild.distributedVolume[i].unit==null) ?0:greatgrandchild.distributedVolume[i].unit);
												break;
											}
										}
										grandchild1volume = parseInt((grandchild.distributedVolume[i].volume=='' || grandchild.distributedVolume[i].volume==undefined || grandchild.distributedVolume[i].volume==null) ?0:grandchild.distributedVolume[i].volume);
										grandchild1unit = ((greatgrandchild1volume * greatgrandchild1unit) + (greatgrandchild2volume * greatgrandchild2unit) + (greatgrandchild3volume * greatgrandchild3unit))/(greatgrandchild1volume + greatgrandchild2volume + greatgrandchild3volume)
										if(isNaN(grandchild1unit)){
											grandchild1unit = 0;
										}
										grandchild1unit = grandchild1unit.toFixed(2)
										grandchild.distributedVolume[i].unit = grandchild1unit;
									}
									else{
										grandchild1volume = parseInt((grandchild.distributedVolume[i].volume=='' || grandchild.distributedVolume[i].volume==undefined || grandchild.distributedVolume[i].volume==null) ?0:grandchild.distributedVolume[i].volume);
										grandchild1unit = parseFloat((grandchild.distributedVolume[i].unit=='' || grandchild.distributedVolume[i].unit==undefined || grandchild.distributedVolume[i].unit==null) ?0:grandchild.distributedVolume[i].unit);
										grandchild1unit = grandchild1unit.toFixed(2)
									}
									break;
								case "1.1.1.2": //Unix Server Pricing
									if(grandchild.open == true){
										var greatgrandchild1volume = "";
										var greatgrandchild2volume = "";
										var greatgrandchild3volume = "";
										var greatgrandchild1unit = "";
										var greatgrandchild2unit = "";
										var greatgrandchild3unit = "";
										for(var l=0;l< grandchild.children.length;l++){
											var greatgrandchild = grandchild.children[l];
											switch(greatgrandchild.id) {
											case "1.1.1.2.1": //Small Server Pricing
												greatgrandchild1volume = parseInt((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume);
												greatgrandchild1unit = parseFloat((greatgrandchild.distributedVolume[i].unit=='' || greatgrandchild.distributedVolume[i].unit==undefined || greatgrandchild.distributedVolume[i].unit==null) ?0:greatgrandchild.distributedVolume[i].unit);
												break;
											case "1.1.1.2.2": //Medium Server Pricing
												greatgrandchild2volume = parseInt((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume);
												greatgrandchild2unit = parseFloat((greatgrandchild.distributedVolume[i].unit=='' || greatgrandchild.distributedVolume[i].unit==undefined || greatgrandchild.distributedVolume[i].unit==null) ?0:greatgrandchild.distributedVolume[i].unit);
												break;
											case "1.1.1.2.3": //Large Server Pricing
												greatgrandchild3volume = parseInt((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume);
												greatgrandchild3unit = parseFloat((greatgrandchild.distributedVolume[i].unit=='' || greatgrandchild.distributedVolume[i].unit==undefined || greatgrandchild.distributedVolume[i].unit==null) ?0:greatgrandchild.distributedVolume[i].unit);
												break;
											}
										}
										grandchild2volume = parseInt((grandchild.distributedVolume[i].volume=='' || grandchild.distributedVolume[i].volume==undefined || grandchild.distributedVolume[i].volume==null) ?0:grandchild.distributedVolume[i].volume);
										grandchild2unit = ((greatgrandchild1volume * greatgrandchild1unit) + (greatgrandchild2volume * greatgrandchild2unit) + (greatgrandchild3volume * greatgrandchild3unit))/(greatgrandchild1volume + greatgrandchild2volume + greatgrandchild3volume)
										if(isNaN(grandchild2unit)){
											grandchild2unit = 0;
										}
										grandchild2unit = grandchild2unit.toFixed(2)
										grandchild.distributedVolume[i].unit = grandchild2unit;
									}
									else{
										grandchild2volume = parseInt((grandchild.distributedVolume[i].volume=='' || grandchild.distributedVolume[i].volume==undefined || grandchild.distributedVolume[i].volume==null) ?0:grandchild.distributedVolume[i].volume);
										grandchild2unit = parseFloat((grandchild.distributedVolume[i].unit=='' || grandchild.distributedVolume[i].unit==undefined || grandchild.distributedVolume[i].unit==null) ?0:grandchild.distributedVolume[i].unit);
										grandchild2unit = grandchild2unit.toFixed(2)
									}
									break;
								}
							}
							child1volume = parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							child1unit = ((grandchild1volume * grandchild1unit) + (grandchild2volume * grandchild2unit))/(grandchild1volume + grandchild2volume)
							if(isNaN(child1unit)){
								child1unit = 0;
							}
							child1unit = child1unit.toFixed(2)
							child.distributedVolume[i].unit = child1unit;
						}
						else{
							child1volume = parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							child1unit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
							child1unit = child1unit.toFixed(2);
						}
						break;
					case "1.1.2": //Virtual Server Pricing
						if(child.open == true){
							var grandchild1volume = "";
							var grandchild2volume = "";
							var grandchild1unit = "";
							var grandchild2unit = "";
							for(var k=0;k< child.children.length;k++){
								var grandchild = child.children[k];
								switch(grandchild.id){
								case "1.1.2.1": //Public Server Pricing
									if(grandchild.open == true){
										var greatgrandchild1volume = "";
										var greatgrandchild2volume = "";
										var greatgrandchild1unit = "";
										var greatgrandchild2unit = "";
										for(var l=0;l< grandchild.children.length;l++){
											var greatgrandchild = grandchild.children[l];
											switch(greatgrandchild.id){
											case "1.1.2.1.1": //Windows/Linux Server Pricing
												if(greatgrandchild.open == true){
													var greatgreatgrandchild1volume = "";
													var greatgreatgrandchild2volume = "";
													var greatgreatgrandchild3volume = "";
													var greatgreatgrandchild1unit = "";
													var greatgreatgrandchild2unit = "";
													var greatgreatgrandchild3unit = "";
													for(var m=0;m< greatgrandchild.children.length;m++){
														var greatgreatgrandchild = greatgrandchild.children[m];
														switch(greatgreatgrandchild.id) {
														case "1.1.2.1.1.1": //Small Server Pricing
															greatgreatgrandchild1volume = parseInt((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume);
															greatgreatgrandchild1unit = parseFloat((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit);
															break;
														case "1.1.2.1.1.2": //Medium Server Pricing
															greatgreatgrandchild2volume = parseInt((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume);
															greatgreatgrandchild2unit = parseFloat((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit);
															break;
														case "1.1.2.1.1.3": //Large Server Pricing
															greatgreatgrandchild3volume = parseInt((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume);
															greatgreatgrandchild3unit = parseFloat((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit);
															break;
														}
													}
													greatgrandchild1volume = parseInt((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume);
													greatgrandchild1unit = ((greatgreatgrandchild1volume * greatgreatgrandchild1unit) + (greatgreatgrandchild2volume * greatgreatgrandchild2unit) + (greatgreatgrandchild3volume * greatgreatgrandchild3unit))/(greatgreatgrandchild1volume + greatgreatgrandchild2volume + greatgreatgrandchild3volume)
													if(isNaN(greatgrandchild1unit)){
														greatgrandchild1unit = 0;
													}
													greatgrandchild1unit = greatgrandchild1unit.toFixed(2)
													greatgrandchild.distributedVolume[i].unit = greatgrandchild1unit;
												}
												else{
													greatgrandchild1volume = parseInt((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume);
													greatgrandchild1unit = parseFloat((greatgrandchild.distributedVolume[i].unit=='' || greatgrandchild.distributedVolume[i].unit==undefined || greatgrandchild.distributedVolume[i].unit==null) ?0:greatgrandchild.distributedVolume[i].unit);
													greatgrandchild1unit = greatgrandchild1unit.toFixed(2)
												}
												break;
											case "1.1.2.1.2": //Unix Server Pricing
												if(greatgrandchild.open == true){
													var greatgreatgrandchild1volume = "";
													var greatgreatgrandchild2volume = "";
													var greatgreatgrandchild3volume = "";
													var greatgreatgrandchild1unit = "";
													var greatgreatgrandchild2unit = "";
													var greatgreatgrandchild3unit = "";
													for(var m=0;m< greatgrandchild.children.length;m++){
														var greatgreatgrandchild = greatgrandchild.children[m];
														switch(greatgreatgrandchild.id) {
														case "1.1.2.1.2.1": //Small Server Pricing
															greatgreatgrandchild1volume = parseInt((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume);
															greatgreatgrandchild1unit = parseFloat((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit);
															break;
														case "1.1.2.1.2.2": //Medium Server Pricing
															greatgreatgrandchild2volume = parseInt((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume);
															greatgreatgrandchild2unit = parseFloat((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit);
															break;
														case "1.1.2.1.2.3": //Large Server Pricing
															greatgreatgrandchild3volume = parseInt((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume);
															greatgreatgrandchild3unit = parseFloat((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit);
															break;
														}
													}
													greatgrandchild2volume = parseInt((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume);
													greatgrandchild2unit = ((greatgreatgrandchild1volume * greatgreatgrandchild1unit) + (greatgreatgrandchild2volume * greatgreatgrandchild2unit) + (greatgreatgrandchild3volume * greatgreatgrandchild3unit))/(greatgreatgrandchild1volume + greatgreatgrandchild2volume + greatgreatgrandchild3volume)
													if(isNaN(greatgrandchild2unit)){
														greatgrandchild2unit = 0;
													}
													greatgrandchild2unit = greatgrandchild2unit.toFixed(2)
													greatgrandchild.distributedVolume[i].unit = greatgrandchild2unit;
												}
												else{
													greatgrandchild2volume = parseInt((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume);
													greatgrandchild2unit = parseFloat((greatgrandchild.distributedVolume[i].unit=='' || greatgrandchild.distributedVolume[i].unit==undefined || greatgrandchild.distributedVolume[i].unit==null) ?0:greatgrandchild.distributedVolume[i].unit);
													greatgrandchild2unit = greatgrandchild2unit.toFixed(2)
												}
												break;
											}
										}
										grandchild1volume = parseInt((grandchild.distributedVolume[i].volume=='' || grandchild.distributedVolume[i].volume==undefined || grandchild.distributedVolume[i].volume==null) ?0:grandchild.distributedVolume[i].volume);
										grandchild1unit = ((greatgrandchild1volume * greatgrandchild1unit) + (greatgrandchild2volume * greatgrandchild2unit))/(greatgrandchild1volume + greatgrandchild2volume)
										if(isNaN(grandchild1unit)){
											grandchild1unit = '';
										}
										grandchild1unit = grandchild1unit.toFixed(2)
										grandchild.distributedVolume[i].unit = grandchild1unit;
									}
									else{
										grandchild1volume = parseInt((grandchild.distributedVolume[i].volume=='' || grandchild.distributedVolume[i].volume==undefined || grandchild.distributedVolume[i].volume==null) ?0:grandchild.distributedVolume[i].volume);
										grandchild1unit = parseFloat((grandchild.distributedVolume[i].unit=='' || grandchild.distributedVolume[i].unit==undefined || grandchild.distributedVolume[i].unit==null) ?0:grandchild.distributedVolume[i].unit);
										grandchild1unit = grandchild1unit.toFixed(2);
									}
									break;
								case "1.1.2.2": //Private Server Pricing
									if(grandchild.open == true){
										var greatgrandchild1volume = "";
										var greatgrandchild2volume = "";
										var greatgrandchild1unit = "";
										var greatgrandchild2unit = "";
										for(var l=0;l< grandchild.children.length;l++){
											var greatgrandchild = grandchild.children[l];
											switch(greatgrandchild.id){
											case "1.1.2.2.1": //Windows/Linux Server Pricing
												if(greatgrandchild.open == true){
													var greatgreatgrandchild1volume = "";
													var greatgreatgrandchild2volume = "";
													var greatgreatgrandchild3volume = "";
													var greatgreatgrandchild1unit = "";
													var greatgreatgrandchild2unit = "";
													var greatgreatgrandchild3unit = "";
													for(var m=0;m< greatgrandchild.children.length;m++){
														var greatgreatgrandchild = greatgrandchild.children[m];
														switch(greatgreatgrandchild.id) {
														case "1.1.2.2.1.1": //Small Server Pricing
															greatgreatgrandchild1volume = parseInt((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume);
															greatgreatgrandchild1unit = parseFloat((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit);
															break;
														case "1.1.2.2.1.2": //Medium Server Pricing
															greatgreatgrandchild2volume = parseInt((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume);
															greatgreatgrandchild2unit = parseFloat((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit);
															break;
														case "1.1.2.2.1.3": //Large Server Pricing
															greatgreatgrandchild3volume = parseInt((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume);
															greatgreatgrandchild3unit = parseFloat((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit);
															break;
														}
													}
													greatgrandchild1volume = parseInt((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume);
													greatgrandchild1unit = ((greatgreatgrandchild1volume * greatgreatgrandchild1unit) + (greatgreatgrandchild2volume * greatgreatgrandchild2unit) + (greatgreatgrandchild3volume * greatgreatgrandchild3unit))/(greatgreatgrandchild1volume + greatgreatgrandchild2volume + greatgreatgrandchild3volume)
													if(isNaN(greatgrandchild1unit)){
														greatgrandchild1unit = 0;
													}
													greatgrandchild1unit = greatgrandchild1unit.toFixed(2)
													greatgrandchild.distributedVolume[i].unit = greatgrandchild1unit;
												}
												else{
													greatgrandchild1volume = parseInt((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume);
													greatgrandchild1unit = parseFloat((greatgrandchild.distributedVolume[i].unit=='' || greatgrandchild.distributedVolume[i].unit==undefined || greatgrandchild.distributedVolume[i].unit==null) ?0:greatgrandchild.distributedVolume[i].unit);
													greatgrandchild1unit = greatgrandchild1unit.toFixed(2)
												}
												break;
											case "1.1.2.2.2": //Unix Server Pricing
												if(greatgrandchild.open == true){
													var greatgreatgrandchild1volume = "";
													var greatgreatgrandchild2volume = "";
													var greatgreatgrandchild3volume = "";
													var greatgreatgrandchild1unit = "";
													var greatgreatgrandchild2unit = "";
													var greatgreatgrandchild3unit = "";
													for(var m=0;m< greatgrandchild.children.length;m++){
														var greatgreatgrandchild = greatgrandchild.children[m];
														switch(greatgreatgrandchild.id) {
														case "1.1.2.2.2.1": //Small Server Pricing
															greatgreatgrandchild1volume = parseInt((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume);
															greatgreatgrandchild1unit = parseFloat((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit);
															break;
														case "1.1.2.2.2.2": //Medium Server Pricing
															greatgreatgrandchild2volume = parseInt((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume);
															greatgreatgrandchild2unit = parseFloat((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit);
															break;
														case "1.1.2.2.2.3": //Large Server Pricing
															greatgreatgrandchild3volume = parseInt((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume);
															greatgreatgrandchild3unit = parseFloat((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit);
															break;
														}
													}
													greatgrandchild2volume = parseInt((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume);
													greatgrandchild2unit = ((greatgreatgrandchild1volume * greatgreatgrandchild1unit) + (greatgreatgrandchild2volume * greatgreatgrandchild2unit) + (greatgreatgrandchild3volume * greatgreatgrandchild3unit))/(greatgreatgrandchild1volume + greatgreatgrandchild2volume + greatgreatgrandchild3volume)
													if(isNaN(greatgrandchild2unit)){
														greatgrandchild2unit = 0;
													}
													greatgrandchild2unit = greatgrandchild2unit.toFixed(2)
													greatgrandchild.distributedVolume[i].unit = greatgrandchild2unit;
												}
												else{
													greatgrandchild2volume = parseInt((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume);
													greatgrandchild2unit = parseFloat((greatgrandchild.distributedVolume[i].unit=='' || greatgrandchild.distributedVolume[i].unit==undefined || greatgrandchild.distributedVolume[i].unit==null) ?0:greatgrandchild.distributedVolume[i].unit);
													greatgrandchild2unit = greatgrandchild2unit.toFixed(2)
												}
												break;
											}
										}
										grandchild2volume = parseInt((grandchild.distributedVolume[i].volume=='' || grandchild.distributedVolume[i].volume==undefined || grandchild.distributedVolume[i].volume==null) ?0:grandchild.distributedVolume[i].volume);
										grandchild2unit = ((greatgrandchild1volume * greatgrandchild1unit) + (greatgrandchild2volume * greatgrandchild2unit))/(greatgrandchild1volume + greatgrandchild2volume)
										if(isNaN(grandchild2unit)){
											grandchild2unit = 0;
										}
										grandchild2unit = grandchild2unit.toFixed(2)
										grandchild.distributedVolume[i].unit = grandchild2unit;
									}
									else{
										grandchild2volume = parseInt((grandchild.distributedVolume[i].volume=='' || grandchild.distributedVolume[i].volume==undefined || grandchild.distributedVolume[i].volume==null) ?0:grandchild.distributedVolume[i].volume);
										grandchild2unit = parseFloat((grandchild.distributedVolume[i].unit=='' || grandchild.distributedVolume[i].unit==undefined || grandchild.distributedVolume[i].unit==null) ?0:grandchild.distributedVolume[i].unit);
										grandchild2unit = grandchild2unit.toFixed(2);
									}
									break;
								}
							}
							child2volume = parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							child2unit = ((grandchild1volume * grandchild1unit) + (grandchild2volume * grandchild2unit))/(grandchild1volume + grandchild2volume)
							if(isNaN(child2unit)){
								child2unit = 0;
							}
							child2unit = child2unit.toFixed(2)
							child.distributedVolume[i].unit = child2unit;
						}
						else{
							child2volume = parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							child2unit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
							child2unit = child2unit.toFixed(2);
						}
						break;
					}
				}
				parent.distributedVolume[i].unit = (((child1volume * child1unit) + (child2volume * child2unit)) / (child1volume + child2volume))
				if(isNaN(parent.distributedVolume[i].unit)){
					parent.distributedVolume[i].unit = 0;
				}
				parent.distributedVolume[i].unit = parent.distributedVolume[i].unit.toFixed(2)
			}
		}
		if($scope.viewBy.type == 'revenue'){
			for(var i=0;i< parent.distributedVolume.length;i++){
				var child1revenue= "";
				var child2revenue = "";
				for(var j = 0;j < parent.children.length;j++){
					var child = parent.children[j];
					switch(child.id) {
					case "1.1.1": //Physical Server Pricing
						if(child.open == true){
							var grandchild1revenue = "";
							var grandchild2revenue = "";
							for(var k=0;k< child.children.length;k++){
								var grandchild = child.children[k];
								switch(grandchild.id){
								case "1.1.1.1": //Windows/Linux Server Pricing
									if(grandchild.open == true){
										var greatgrandchild1revenue = "";
										var greatgrandchild2revenue = "";
										var greatgrandchild3revenue = "";
										for(var l=0;l< grandchild.children.length;l++){
											var greatgrandchild = grandchild.children[l];
											switch(greatgrandchild.id) {
											case "1.1.1.1.1": //Small Server Pricing
												greatgrandchild1revenue = parseInt((greatgrandchild.distributedVolume[i].revenue=='' || greatgrandchild.distributedVolume[i].revenue==undefined || greatgrandchild.distributedVolume[i].revenue==null) ?0:greatgrandchild.distributedVolume[i].revenue);
												break;
											case "1.1.1.1.2": //Medium Server Pricing												
												greatgrandchild2revenue = parseInt((greatgrandchild.distributedVolume[i].revenue=='' || greatgrandchild.distributedVolume[i].revenue==undefined || greatgrandchild.distributedVolume[i].revenue==null) ?0:greatgrandchild.distributedVolume[i].revenue);
												break;
											case "1.1.1.1.3": //Large Server Pricing												
												greatgrandchild3revenue = parseInt((greatgrandchild.distributedVolume[i].revenue=='' || greatgrandchild.distributedVolume[i].revenue==undefined || greatgrandchild.distributedVolume[i].revenue==null) ?0:greatgrandchild.distributedVolume[i].revenue);
												break;
											}
										}
										grandchild1revenue = (greatgrandchild1revenue + greatgrandchild2revenue + greatgrandchild3revenue)
										if(isNaN(grandchild1revenue)){
											grandchild1revenue = 0;
										}
										grandchild.distributedVolume[i].revenue = grandchild1revenue;
									}
									else{										
										grandchild1revenue = parseInt((grandchild.distributedVolume[i].revenue=='' || grandchild.distributedVolume[i].revenue==undefined || grandchild.distributedVolume[i].revenue==null) ?0:grandchild.distributedVolume[i].revenue);
									}
									break;
								case "1.1.1.2": //Unix Server Pricing
									if(grandchild.open == true){
										var greatgrandchild1revenue= "";
										var greatgrandchild2revenue = "";
										var greatgrandchild3revenue = "";
										for(var l=0;l< grandchild.children.length;l++){
											var greatgrandchild = grandchild.children[l];
											switch(greatgrandchild.id) {
											case "1.1.1.2.1": //Small Server Pricing												
												greatgrandchild1revenue = parseInt((greatgrandchild.distributedVolume[i].revenue=='' || greatgrandchild.distributedVolume[i].revenue==undefined || greatgrandchild.distributedVolume[i].revenue==null) ?0:greatgrandchild.distributedVolume[i].revenue);
												break;
											case "1.1.1.2.2": //Medium Server Pricing
												greatgrandchild2revenue = parseInt((greatgrandchild.distributedVolume[i].revenue=='' || greatgrandchild.distributedVolume[i].revenue==undefined || greatgrandchild.distributedVolume[i].revenue==null) ?0:greatgrandchild.distributedVolume[i].revenue);
												break;
											case "1.1.1.2.3": //Large Server Pricing
												greatgrandchild3revenue = parseInt((greatgrandchild.distributedVolume[i].revenue=='' || greatgrandchild.distributedVolume[i].revenue==undefined || greatgrandchild.distributedVolume[i].revenue==null) ?0:greatgrandchild.distributedVolume[i].revenue);
												break;
											}
										}
										grandchild2revenue = (greatgrandchild1revenue + greatgrandchild2revenue + greatgrandchild3revenue)
										if(isNaN(grandchild2revenue)){
											grandchild2revenue = 0;
										}
										grandchild.distributedVolume[i].revenue = grandchild2revenue;
									}
									else{
										grandchild2revenue = parseInt((grandchild.distributedVolume[i].revenue=='' || grandchild.distributedVolume[i].revenue==undefined || grandchild.distributedVolume[i].revenue==null) ?0:grandchild.distributedVolume[i].revenue);
									}
									break;
								}
							}
							child1revenue = (grandchild1revenue + grandchild2revenue)
							if(isNaN(child1revenue)){
								child1revenue = 0;
							}
							child.distributedVolume[i].revenue = child1revenue;
						}
						else{
							child1revenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
						}
						break;
					case "1.1.2": //Virtual Server Pricing
						if(child.open == true){
							var grandchild1revenue= "";
							var grandchild2revenue = "";
							for(var k=0;k< child.children.length;k++){
								var grandchild = child.children[k];
								switch(grandchild.id){
								case "1.1.2.1": //Public Server Pricing
									if(grandchild.open == true){
										var greatgrandchild1revenue = "";
										var greatgrandchild2revenue = "";
										for(var l=0;l< grandchild.children.length;l++){
											var greatgrandchild = grandchild.children[l];
											switch(greatgrandchild.id){
											case "1.1.2.1.1": //Windows/Linux Server Pricing
												if(greatgrandchild.open == true){
													var greatgreatgrandchild1revenue = "";
													var greatgreatgrandchild2revenue = "";
													var greatgreatgrandchild3revenue = "";
													for(var m=0;m< greatgrandchild.children.length;m++){
														var greatgreatgrandchild = greatgrandchild.children[m];
														switch(greatgreatgrandchild.id) {
														case "1.1.2.1.1.1": //Small Server Pricing
															greatgreatgrandchild1revenue = parseInt((greatgreatgrandchild.distributedVolume[i].revenue=='' || greatgreatgrandchild.distributedVolume[i].revenue==undefined || greatgreatgrandchild.distributedVolume[i].revenue==null) ?0:greatgreatgrandchild.distributedVolume[i].revenue);
															break;
														case "1.1.2.1.1.2": //Medium Server Pricing
															greatgreatgrandchild2revenue = parseInt((greatgreatgrandchild.distributedVolume[i].revenue=='' || greatgreatgrandchild.distributedVolume[i].revenue==undefined || greatgreatgrandchild.distributedVolume[i].revenue==null) ?0:greatgreatgrandchild.distributedVolume[i].revenue);
															break;
														case "1.1.2.1.1.3": //Large Server Pricing
															greatgreatgrandchild3revenue = parseInt((greatgreatgrandchild.distributedVolume[i].revenue=='' || greatgreatgrandchild.distributedVolume[i].revenue==undefined || greatgreatgrandchild.distributedVolume[i].revenue==null) ?0:greatgreatgrandchild.distributedVolume[i].revenue);
															break;
														}
													}
													greatgrandchild1revenue = (greatgreatgrandchild1revenue + greatgreatgrandchild2revenue + greatgreatgrandchild3revenue)
													if(isNaN(greatgrandchild1revenue)){
														greatgrandchild1revenue = 0;
													}
													greatgrandchild.distributedVolume[i].revenue = greatgrandchild1revenue;
												}
												else{
													greatgrandchild1revenue = parseInt((greatgrandchild.distributedVolume[i].revenue=='' || greatgrandchild.distributedVolume[i].revenue==undefined || greatgrandchild.distributedVolume[i].revenue==null) ?0:greatgrandchild.distributedVolume[i].revenue);
												}
												break;
											case "1.1.2.1.2": //Unix Server Pricing
												if(greatgrandchild.open == true){
													var greatgreatgrandchild1revenue = "";
													var greatgreatgrandchild2revenue = "";
													var greatgreatgrandchild3revenue = "";
													for(var m=0;m< greatgrandchild.children.length;m++){
														var greatgreatgrandchild = greatgrandchild.children[m];
														switch(greatgreatgrandchild.id) {
														case "1.1.2.1.2.1": //Small Server Pricing
															greatgreatgrandchild1revenue = parseInt((greatgreatgrandchild.distributedVolume[i].revenue=='' || greatgreatgrandchild.distributedVolume[i].revenue==undefined || greatgreatgrandchild.distributedVolume[i].revenue==null) ?0:greatgreatgrandchild.distributedVolume[i].revenue);
															break;
														case "1.1.2.1.2.2": //Medium Server Pricing
															greatgreatgrandchild2revenue = parseInt((greatgreatgrandchild.distributedVolume[i].revenue=='' || greatgreatgrandchild.distributedVolume[i].revenue==undefined || greatgreatgrandchild.distributedVolume[i].revenue==null) ?0:greatgreatgrandchild.distributedVolume[i].revenue);
															break;
														case "1.1.2.1.2.3": //Large Server Pricing
															greatgreatgrandchild3revenue = parseInt((greatgreatgrandchild.distributedVolume[i].revenue=='' || greatgreatgrandchild.distributedVolume[i].revenue==undefined || greatgreatgrandchild.distributedVolume[i].revenue==null) ?0:greatgreatgrandchild.distributedVolume[i].revenue);
															break;
														}
													}													
													greatgrandchild2revenue = (greatgreatgrandchild1revenue + greatgreatgrandchild2revenue + greatgreatgrandchild3revenue)
													if(isNaN(greatgrandchild2revenue)){
														greatgrandchild2revenue = 0;
													}
													greatgrandchild.distributedVolume[i].revenue = greatgrandchild2revenue;
												}
												else{
													greatgrandchild2revenue = parseInt((greatgrandchild.distributedVolume[i].revenue=='' || greatgrandchild.distributedVolume[i].revenue==undefined || greatgrandchild.distributedVolume[i].revenue==null) ?0:greatgrandchild.distributedVolume[i].revenue);
												}
												break;
											}
										}
										grandchild1revenue = (greatgrandchild1revenue + greatgrandchild2revenue)
										if(isNaN(grandchild1revenue)){
											grandchild1revenue = 0;
										}
										grandchild.distributedVolume[i].revenue = grandchild1revenue;
									}
									else{
										grandchild1revenue = parseInt((grandchild.distributedVolume[i].revenue=='' || grandchild.distributedVolume[i].revenue==undefined || grandchild.distributedVolume[i].revenue==null) ?0:grandchild.distributedVolume[i].revenue);
									}
									break;
								case "1.1.2.2": //Private Server Pricing
									if(grandchild.open == true){
										var greatgrandchild1revenue = "";
										var greatgrandchild2revenue = "";
										for(var l=0;l< grandchild.children.length;l++){
											var greatgrandchild = grandchild.children[l];
											switch(greatgrandchild.id){
											case "1.1.2.2.1": //Windows/Linux Server Pricing
												if(greatgrandchild.open == true){
													var greatgreatgrandchild1revenue = "";
													var greatgreatgrandchild2revenue = "";
													var greatgreatgrandchild3revenue = "";
													for(var m=0;m< greatgrandchild.children.length;m++){
														var greatgreatgrandchild = greatgrandchild.children[m];
														switch(greatgreatgrandchild.id) {
														case "1.1.2.2.1.1": //Small Server Pricing											
															greatgreatgrandchild1revenue = parseInt((greatgreatgrandchild.distributedVolume[i].revenue=='' || greatgreatgrandchild.distributedVolume[i].revenue==undefined || greatgreatgrandchild.distributedVolume[i].revenue==null) ?0:greatgreatgrandchild.distributedVolume[i].revenue);
															break;
														case "1.1.2.2.1.2": //Medium Server Pricing
															greatgreatgrandchild2revenue = parseInt((greatgreatgrandchild.distributedVolume[i].revenue=='' || greatgreatgrandchild.distributedVolume[i].revenue==undefined || greatgreatgrandchild.distributedVolume[i].revenue==null) ?0:greatgreatgrandchild.distributedVolume[i].revenue);
															break;
														case "1.1.2.2.1.3": //Large Server Pricing															greatgreatgrandchild3volume = parseInt((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume);
															greatgreatgrandchild3revenue = parseInt((greatgreatgrandchild.distributedVolume[i].revenue=='' || greatgreatgrandchild.distributedVolume[i].revenue==undefined || greatgreatgrandchild.distributedVolume[i].revenue==null) ?0:greatgreatgrandchild.distributedVolume[i].revenue);
															break;
														}
													}	
													greatgrandchild1revenue = (greatgreatgrandchild1revenue + greatgreatgrandchild2revenue + greatgreatgrandchild3revenue)
													if(isNaN(greatgrandchild1revenue)){
														greatgrandchild1revenue = 0;
													}
													greatgrandchild.distributedVolume[i].revenue = greatgrandchild1revenue;
												}
												else{
													greatgrandchild1revenue = parseInt((greatgrandchild.distributedVolume[i].revenue=='' || greatgrandchild.distributedVolume[i].revenue==undefined || greatgrandchild.distributedVolume[i].revenue==null) ?0:greatgrandchild.distributedVolume[i].revenue);
												}
												break;
											case "1.1.2.2.2": //Unix Server Pricing
												if(greatgrandchild.open == true){
													var greatgreatgrandchild1revenue = "";
													var greatgreatgrandchild2revenue = "";
													var greatgreatgrandchild3revenue = "";
													for(var m=0;m< greatgrandchild.children.length;m++){
														var greatgreatgrandchild = greatgrandchild.children[m];
														switch(greatgreatgrandchild.id) {
														case "1.1.2.2.2.1": //Small Server Pricing
															greatgreatgrandchild1revenue = parseInt((greatgreatgrandchild.distributedVolume[i].revenue=='' || greatgreatgrandchild.distributedVolume[i].revenue==undefined || greatgreatgrandchild.distributedVolume[i].revenue==null) ?0:greatgreatgrandchild.distributedVolume[i].revenue);
															break;
														case "1.1.2.2.2.2": //Medium Server Pricing
															greatgreatgrandchild2revenue = parseInt((greatgreatgrandchild.distributedVolume[i].revenue=='' || greatgreatgrandchild.distributedVolume[i].revenue==undefined || greatgreatgrandchild.distributedVolume[i].revenue==null) ?0:greatgreatgrandchild.distributedVolume[i].revenue);
															break;
														case "1.1.2.2.2.3": //Large Server Pricing
															greatgreatgrandchild3revenue = parseInt((greatgreatgrandchild.distributedVolume[i].revenue=='' || greatgreatgrandchild.distributedVolume[i].revenue==undefined || greatgreatgrandchild.distributedVolume[i].revenue==null) ?0:greatgreatgrandchild.distributedVolume[i].revenue);
															break;
														}
													}
													greatgrandchild2revenue = (greatgreatgrandchild1revenue + greatgreatgrandchild2revenue + greatgreatgrandchild3revenue)
													if(isNaN(greatgrandchild2revenue)){
														greatgrandchild2revenue = 0;
													}
													greatgrandchild.distributedVolume[i].revenue = greatgrandchild2revenue;
												}
												else{
													greatgrandchild2revenue = parseInt((greatgrandchild.distributedVolume[i].revenue=='' || greatgrandchild.distributedVolume[i].revenue==undefined || greatgrandchild.distributedVolume[i].revenue==null) ?0:greatgrandchild.distributedVolume[i].revenue);
												}
												break;
											}
										}										
										grandchild2revenue = (greatgrandchild1revenue + greatgrandchild2revenue)
										if(isNaN(grandchild2revenue)){
											grandchild2revenue = 0;
										}
										grandchild.distributedVolume[i].revenue = grandchild2revenue;
									}
									else{
										grandchild2revenue = parseInt((grandchild.distributedVolume[i].revenue=='' || grandchild.distributedVolume[i].revenue==undefined || grandchild.distributedVolume[i].revenue==null) ?0:grandchild.distributedVolume[i].revenue);
									}
									break;
								}
							}
							child2revenue = (grandchild1revenue + grandchild2revenue)
							if(isNaN(child2revenue)){
								child2revenue = 0;
							}
							child.distributedVolume[i].revenue = child2revenue;
						}
						else{
							child2revenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
						}
						break;
					}
				}
				parent.distributedVolume[i].revenue = (child1revenue + child2revenue)
				if(isNaN(parent.distributedVolume[i].revenue)){
					parent.distributedVolume[i].revenue = 0;
				}
			};
		}
	}
	
	$scope.onchangeparentvolume = function(){
		if($scope.selectedSolutionId != null && $scope.selectedSolutionId != undefined &&  $scope.selectedSolutionId != ''){
			$scope.onChangeDistSetting($scope.selectedSolutionId);
		}
		
	}
	
	// function for setting the custom value
	$scope.setCustom=function()
	{
		$scope.custom=true;
		var CustomSolutionId=_.where($scope.solList, {solutionName: 'Custom'})[0];
		$scope.selectedSolutionId=CustomSolutionId.solutionId;
	}
	
	// Percentage changes
	$scope.onchangepercentage = function(parent,index){
		var len = parent.children.length;
		if(len == 2){
			$scope.setCustom();
			//var changePercentage=$scope.endUserInfo.user.children[0].children[0].children[index].percentage;
			if(index == 0){
				parent.children[index+1].percentage=100-parent.children[index].percentage;
			}
			else{
				parent.children[index-1].percentage=100-parent.children[index].percentage;
			}
			if(parent.children[index].percentage == undefined || parent.children[index].percentage > '100'){
				for(var i=0; i< $scope.dealInfo/12; i++){
					parent.children[index].distributedVolume[i].volume = undefined;
				}
			}
			$scope.calculateHostingVolume($scope.hostingInfo.serverHosting.children[0]);
		}
		if(len == 3){
			var per=0;
			for(var i=0;i<parent.children.length;i++){
				per=parseInt(per)+parseInt(parent.children[i].percentage);
			}

			if(per!=100){
				$scope.showErr=true;
			}
			else{
				$scope.showErr=false;
				$scope.showVolumeErr=false;
				$scope.setCustom();
				$scope.calculateHostingVolume($scope.hostingInfo.serverHosting.children[0]);
			}
		}	
	}
	
	// Volume changes
	$scope.onChangeVolume = function(parent){
		var len = parent.children.length;
		if(len == 2){
			for (var i = 0; i < $scope.dealInfo/12; i++) {
	    		if(parseInt(parent.distributedVolume[i].volume) != parseInt(parent.children[0].distributedVolume[i].volume) + parseInt(parent.children[1].distributedVolume[i].volume)){
	    			$scope.showVolumeErr=true;
	    			return;
	    		}
	    		for(var j=0;j< parent.children.length;j++){
	    			parent.children[j].percentage = undefined;
				}
	    		$scope.showVolumeErr=false;
				$scope.setCustom();
				$scope.calculateHostingVolume($scope.hostingInfo.serverHosting.children[0]);
	    	}
		}
		if(len == 3){
			for (var i = 0; i < $scope.dealInfo/12; i++) {
	    		if(parseInt(parent.distributedVolume[i].volume) != parseInt(parent.children[0].distributedVolume[i].volume) + parseInt(parent.children[1].distributedVolume[i].volume) + parseInt(parent.children[2].distributedVolume[i].volume)){
	    			$scope.showVolumeErr=true;
	    			return;
	    		}
	    		for(var j=0;j< parent.children.length;j++){
	    			parent.children[j].percentage = undefined;
				}
	    		$scope.showVolumeErr=false;
				$scope.setCustom();
				$scope.calculateHostingVolume($scope.hostingInfo.serverHosting.children[0]);
	    	}
		}
	}
	
	// On change Unit-revenue price radio button
	$scope.onchangeprice = function(value){
		var parent = $scope.hostingInfo.serverHosting.children[0];
		if(value == 'revenue'){
			for(var i=0;i< parent.distributedVolume.length;i++){
				parent.distributedVolume[i].revenue = Math.round(((parent.distributedVolume[i].volume=='' || parent.distributedVolume[i].volume==undefined || parent.distributedVolume[i].volume==null) ?0:parent.distributedVolume[i].volume)  * ((parent.distributedVolume[i].unit=='' || parent.distributedVolume[i].unit==undefined || parent.distributedVolume[i].unit==null) ?0:parent.distributedVolume[i].unit));
				if(isNaN(parent.distributedVolume[i].revenue)){
					parent.distributedVolume[i].revenue = 0;
				}
				for(var j = 0;j < parent.children.length;j++){
					var child = parent.children[j];
					switch(child.id) {
					case "1.1.1": // Physical Server
						child.distributedVolume[i].revenue = Math.round(((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume)  * ((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit));
						if(isNaN(child.distributedVolume[i].revenue)){
							child.distributedVolume[i].revenue = 0;
						}
						for(var k = 0;k < child.children.length;k++){
							var grandchild = child.children[k];
							switch(grandchild.id) {
							case "1.1.1.1": // Window/Linux
								grandchild.distributedVolume[i].revenue = Math.round(((grandchild.distributedVolume[i].volume=='' || grandchild.distributedVolume[i].volume==undefined || grandchild.distributedVolume[i].volume==null) ?0:grandchild.distributedVolume[i].volume)  * ((grandchild.distributedVolume[i].unit=='' || grandchild.distributedVolume[i].unit==undefined || grandchild.distributedVolume[i].unit==null) ?0:grandchild.distributedVolume[i].unit));
								if(isNaN(grandchild.distributedVolume[i].revenue)){
									grandchild.distributedVolume[i].revenue = 0;
								}
								for(var l = 0;l < grandchild.children.length;l++){
									var greatgrandchild = grandchild.children[l];
									switch(greatgrandchild.id) {
									case "1.1.1.1.1": // Small
										greatgrandchild.distributedVolume[i].revenue = Math.round(((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume)  * ((greatgrandchild.distributedVolume[i].unit=='' || greatgrandchild.distributedVolume[i].unit==undefined || greatgrandchild.distributedVolume[i].unit==null) ?0:greatgrandchild.distributedVolume[i].unit));
										if(isNaN(greatgrandchild.distributedVolume[i].revenue)){
											greatgrandchild.distributedVolume[i].revenue = 0;
										}
										break;
									case "1.1.1.1.2": // Medium
										greatgrandchild.distributedVolume[i].revenue = Math.round(((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume)  * ((greatgrandchild.distributedVolume[i].unit=='' || greatgrandchild.distributedVolume[i].unit==undefined || greatgrandchild.distributedVolume[i].unit==null) ?0:greatgrandchild.distributedVolume[i].unit));
										if(isNaN(greatgrandchild.distributedVolume[i].revenue)){
											greatgrandchild.distributedVolume[i].revenue = 0;
										}
										break;
									case "1.1.1.1.3": // Large
										greatgrandchild.distributedVolume[i].revenue = Math.round(((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume)  * ((greatgrandchild.distributedVolume[i].unit=='' || greatgrandchild.distributedVolume[i].unit==undefined || greatgrandchild.distributedVolume[i].unit==null) ?0:greatgrandchild.distributedVolume[i].unit));
										if(isNaN(greatgrandchild.distributedVolume[i].revenue)){
											greatgrandchild.distributedVolume[i].revenue = 0;
										}
										break;
									}
								}
								break;
							case "1.1.1.2": // Unix
								grandchild.distributedVolume[i].revenue = Math.round(((grandchild.distributedVolume[i].volume=='' || grandchild.distributedVolume[i].volume==undefined || grandchild.distributedVolume[i].volume==null) ?0:grandchild.distributedVolume[i].volume)  * ((grandchild.distributedVolume[i].unit=='' || grandchild.distributedVolume[i].unit==undefined || grandchild.distributedVolume[i].unit==null) ?0:grandchild.distributedVolume[i].unit));
								if(isNaN(grandchild.distributedVolume[i].revenue)){
									grandchild.distributedVolume[i].revenue = 0;
								}
								for(var l = 0;l < grandchild.children.length;l++){
									var greatgrandchild = grandchild.children[l];
									switch(greatgrandchild.id) {
									case "1.1.1.1.1": // Small
										greatgrandchild.distributedVolume[i].revenue = Math.round(((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume)  * ((greatgrandchild.distributedVolume[i].unit=='' || greatgrandchild.distributedVolume[i].unit==undefined || greatgrandchild.distributedVolume[i].unit==null) ?0:greatgrandchild.distributedVolume[i].unit));
										if(isNaN(greatgrandchild.distributedVolume[i].revenue)){
											greatgrandchild.distributedVolume[i].revenue = 0;
										}
										break;
									case "1.1.1.1.2": // Medium
										greatgrandchild.distributedVolume[i].revenue = Math.round(((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume)  * ((greatgrandchild.distributedVolume[i].unit=='' || greatgrandchild.distributedVolume[i].unit==undefined || greatgrandchild.distributedVolume[i].unit==null) ?0:greatgrandchild.distributedVolume[i].unit));
										if(isNaN(greatgrandchild.distributedVolume[i].revenue)){
											greatgrandchild.distributedVolume[i].revenue = 0;
										}
										break;
									case "1.1.1.1.3": // Large
										greatgrandchild.distributedVolume[i].revenue = Math.round(((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume)  * ((greatgrandchild.distributedVolume[i].unit=='' || greatgrandchild.distributedVolume[i].unit==undefined || greatgrandchild.distributedVolume[i].unit==null) ?0:greatgrandchild.distributedVolume[i].unit));
										if(isNaN(greatgrandchild.distributedVolume[i].revenue)){
											greatgrandchild.distributedVolume[i].revenue = 0;
										}
										break;
									}
								}
								break;
							}
						}
						break;
					case "1.1.2": // Virtual Server
						child.distributedVolume[i].revenue = Math.round(((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume)  * ((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit));
						if(isNaN(child.distributedVolume[i].revenue)){
							child.distributedVolume[i].revenue = 0;
						}
						for(var k = 0;k < child.children.length;k++){
							var grandchild = child.children[k];
							switch(grandchild.id) {
							case "1.1.2.1": // Public
								grandchild.distributedVolume[i].revenue = Math.round(((grandchild.distributedVolume[i].volume=='' || grandchild.distributedVolume[i].volume==undefined || grandchild.distributedVolume[i].volume==null) ?0:grandchild.distributedVolume[i].volume)  * ((grandchild.distributedVolume[i].unit=='' || grandchild.distributedVolume[i].unit==undefined || grandchild.distributedVolume[i].unit==null) ?0:grandchild.distributedVolume[i].unit));
								if(isNaN(grandchild.distributedVolume[i].revenue)){
									grandchild.distributedVolume[i].revenue = 0;
								}
								for(var l = 0;l < grandchild.children.length;l++){
									var greatgrandchild = grandchild.children[l];
									switch(greatgrandchild.id) {
									case "1.1.2.1.1": // Windows/Linux
										greatgrandchild.distributedVolume[i].revenue = Math.round(((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume)  * ((greatgrandchild.distributedVolume[i].unit=='' || greatgrandchild.distributedVolume[i].unit==undefined || greatgrandchild.distributedVolume[i].unit==null) ?0:greatgrandchild.distributedVolume[i].unit));
										if(isNaN(greatgrandchild.distributedVolume[i].revenue)){
											greatgrandchild.distributedVolume[i].revenue = 0;
										}
										for(var m = 0;m < greatgrandchild.children.length;m++){
											var greatgreatgrandchild = greatgrandchild.children[m];
											switch(greatgreatgrandchild.id) {
											case "1.1.2.1.1.1": // Small
												greatgreatgrandchild.distributedVolume[i].revenue = Math.round(((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume)  * ((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit));
												if(isNaN(greatgreatgrandchild.distributedVolume[i].revenue)){
													greatgreatgrandchild.distributedVolume[i].revenue = 0;
												}
												break;
											case "1.1.2.1.1.2": // Medium
												greatgreatgrandchild.distributedVolume[i].revenue = Math.round(((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume)  * ((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit));
												if(isNaN(greatgreatgrandchild.distributedVolume[i].revenue)){
													greatgreatgrandchild.distributedVolume[i].revenue = 0;
												}
												break;
											case "1.1.2.1.1.3": // Large
												greatgreatgrandchild.distributedVolume[i].revenue = Math.round(((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume)  * ((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit));
												if(isNaN(greatgreatgrandchild.distributedVolume[i].revenue)){
													greatgreatgrandchild.distributedVolume[i].revenue = 0;
												}
												break;
											}
										}
										break;
									case "1.1.2.1.2": //Unix
										greatgrandchild.distributedVolume[i].revenue = Math.round(((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume)  * ((greatgrandchild.distributedVolume[i].unit=='' || greatgrandchild.distributedVolume[i].unit==undefined || greatgrandchild.distributedVolume[i].unit==null) ?0:greatgrandchild.distributedVolume[i].unit));
										if(isNaN(greatgrandchild.distributedVolume[i].revenue)){
											greatgrandchild.distributedVolume[i].revenue = 0;
										}
										for(var m = 0;m < greatgrandchild.children.length;m++){
											var greatgreatgrandchild = greatgrandchild.children[m];
											switch(greatgreatgrandchild.id) {
											case "1.1.2.1.2.1": // Small
												greatgreatgrandchild.distributedVolume[i].revenue = Math.round(((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume)  * ((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit));
												if(isNaN(greatgreatgrandchild.distributedVolume[i].revenue)){
													greatgreatgrandchild.distributedVolume[i].revenue = 0;
												}
												break;
											case "1.1.2.1.2.2": // Medium
												greatgreatgrandchild.distributedVolume[i].revenue = Math.round(((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume)  * ((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit));
												if(isNaN(greatgreatgrandchild.distributedVolume[i].revenue)){
													greatgreatgrandchild.distributedVolume[i].revenue = 0;
												}
												break;
											case "1.1.2.1.2.3": // Large
												greatgreatgrandchild.distributedVolume[i].revenue = Math.round(((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume)  * ((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit));
												if(isNaN(greatgreatgrandchild.distributedVolume[i].revenue)){
													greatgreatgrandchild.distributedVolume[i].revenue = 0;
												}
												break;
											}
										}
										break;
									}
								}
								break;
							case "1.1.2.2": // Private
								grandchild.distributedVolume[i].revenue = Math.round(((grandchild.distributedVolume[i].volume=='' || grandchild.distributedVolume[i].volume==undefined || grandchild.distributedVolume[i].volume==null) ?0:grandchild.distributedVolume[i].volume)  * ((grandchild.distributedVolume[i].unit=='' || grandchild.distributedVolume[i].unit==undefined || grandchild.distributedVolume[i].unit==null) ?0:grandchild.distributedVolume[i].unit));
								if(isNaN(grandchild.distributedVolume[i].revenue)){
									grandchild.distributedVolume[i].revenue = 0;
								}
								for(var l = 0;l < grandchild.children.length;l++){
									var greatgrandchild = grandchild.children[l];
									switch(greatgrandchild.id) {
									case "1.1.2.2.1": // Windows/Linux
										greatgrandchild.distributedVolume[i].revenue = Math.round(((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume)  * ((greatgrandchild.distributedVolume[i].unit=='' || greatgrandchild.distributedVolume[i].unit==undefined || greatgrandchild.distributedVolume[i].unit==null) ?0:greatgrandchild.distributedVolume[i].unit));
										if(isNaN(greatgrandchild.distributedVolume[i].revenue)){
											greatgrandchild.distributedVolume[i].revenue = 0;
										}
										for(var m = 0;m < greatgrandchild.children.length;m++){
											var greatgreatgrandchild = greatgrandchild.children[m];
											switch(greatgreatgrandchild.id) {
											case "1.1.2.2.1.1": // Small
												greatgreatgrandchild.distributedVolume[i].revenue = Math.round(((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume)  * ((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit));
												if(isNaN(greatgreatgrandchild.distributedVolume[i].revenue)){
													greatgreatgrandchild.distributedVolume[i].revenue = 0;
												}
												break;
											case "1.1.2.2.1.2": // Medium
												greatgreatgrandchild.distributedVolume[i].revenue = Math.round(((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume)  * ((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit));
												if(isNaN(greatgreatgrandchild.distributedVolume[i].revenue)){
													greatgreatgrandchild.distributedVolume[i].revenue = 0;
												}
												break;
											case "1.1.2.2.1.3": // Large
												greatgreatgrandchild.distributedVolume[i].revenue = Math.round(((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume)  * ((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit));
												if(isNaN(greatgreatgrandchild.distributedVolume[i].revenue)){
													greatgreatgrandchild.distributedVolume[i].revenue = 0;
												}
												break;
											}
										}
										break;
									case "1.1.2.2.2": //Unix
										greatgrandchild.distributedVolume[i].revenue = Math.round(((greatgrandchild.distributedVolume[i].volume=='' || greatgrandchild.distributedVolume[i].volume==undefined || greatgrandchild.distributedVolume[i].volume==null) ?0:greatgrandchild.distributedVolume[i].volume)  * ((greatgrandchild.distributedVolume[i].unit=='' || greatgrandchild.distributedVolume[i].unit==undefined || greatgrandchild.distributedVolume[i].unit==null) ?0:greatgrandchild.distributedVolume[i].unit));
										if(isNaN(greatgrandchild.distributedVolume[i].revenue)){
											greatgrandchild.distributedVolume[i].revenue = 0;
										}
										for(var m = 0;m < greatgrandchild.children.length;m++){
											var greatgreatgrandchild = greatgrandchild.children[m];
											switch(greatgreatgrandchild.id) {
											case "1.1.2.2.2.1": // Small
												greatgreatgrandchild.distributedVolume[i].revenue = Math.round(((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume)  * ((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit));
												if(isNaN(greatgreatgrandchild.distributedVolume[i].revenue)){
													greatgreatgrandchild.distributedVolume[i].revenue = 0;
												}
												break;
											case "1.1.2.2.2.2": // Medium
												greatgreatgrandchild.distributedVolume[i].revenue = Math.round(((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume)  * ((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit));
												if(isNaN(greatgreatgrandchild.distributedVolume[i].revenue)){
													greatgreatgrandchild.distributedVolume[i].revenue = 0;
												}
												break;
											case "1.1.2.2.2.3": // Large
												greatgreatgrandchild.distributedVolume[i].revenue = Math.round(((greatgreatgrandchild.distributedVolume[i].volume=='' || greatgreatgrandchild.distributedVolume[i].volume==undefined || greatgreatgrandchild.distributedVolume[i].volume==null) ?0:greatgreatgrandchild.distributedVolume[i].volume)  * ((greatgreatgrandchild.distributedVolume[i].unit=='' || greatgreatgrandchild.distributedVolume[i].unit==undefined || greatgreatgrandchild.distributedVolume[i].unit==null) ?0:greatgreatgrandchild.distributedVolume[i].unit));
												if(isNaN(greatgreatgrandchild.distributedVolume[i].revenue)){
													greatgreatgrandchild.distributedVolume[i].revenue = 0;
												}
												break;
											}
										}
										break;
									}
								}
								break;
							}
						}
					}
				}
				for(var n=0;n < $scope.hostingInfo.platformHosting.children.length;n++){
					$scope.hostingInfo.platformHosting.children[n].distributedVolume[i].revenue = Math.round((($scope.hostingInfo.platformHosting.children[n].distributedVolume[i].volume=='' || $scope.hostingInfo.platformHosting.children[n].distributedVolume[i].volume==undefined || $scope.hostingInfo.platformHosting.children[n].distributedVolume[i].volume==null) ?0:$scope.hostingInfo.platformHosting.children[n].distributedVolume[i].volume)  * (($scope.hostingInfo.platformHosting.children[n].distributedVolume[i].unit=='' || $scope.hostingInfo.platformHosting.children[n].distributedVolume[i].unit==undefined || $scope.hostingInfo.platformHosting.children[n].distributedVolume[i].unit==null) ?0:$scope.hostingInfo.platformHosting.children[n].distributedVolume[i].unit));
					if(isNaN($scope.hostingInfo.platformHosting.children[n].distributedVolume[i].revenue)){
						$scope.hostingInfo.platformHosting.children[n].distributedVolume[i].revenue = 0;
					}
				}
			}
		}
		if(value == 'unit'){
			for(var i=0;i< parent.distributedVolume.length;i++){
				parent.distributedVolume[i].unit = parseFloat(parent.distributedVolume[i].revenue / parent.distributedVolume[i].volume);
				if(isNaN(parent.distributedVolume[i].unit)){
					parent.distributedVolume[i].unit = 0;
				}
				parent.distributedVolume[i].unit = parent.distributedVolume[i].unit.toFixed(2);
				for(var j = 0;j < parent.children.length;j++){
					var child = parent.children[j];
					switch(child.id) {
					case "1.1.1": // Physical Server
						child.distributedVolume[i].unit = parseFloat(child.distributedVolume[i].revenue / child.distributedVolume[i].volume);
						if(isNaN(child.distributedVolume[i].unit)){
							child.distributedVolume[i].unit = 0;
						}
						child.distributedVolume[i].unit = child.distributedVolume[i].unit.toFixed(2);
						for(var k = 0;k < child.children.length;k++){
							var grandchild = child.children[k];
							switch(grandchild.id) {
							case "1.1.1.1": // Window/Linux
								grandchild.distributedVolume[i].unit = parseFloat(grandchild.distributedVolume[i].revenue / grandchild.distributedVolume[i].volume);
								if(isNaN(grandchild.distributedVolume[i].unit)){
									grandchild.distributedVolume[i].unit = 0;
								}
								grandchild.distributedVolume[i].unit = grandchild.distributedVolume[i].unit.toFixed(2);
								for(var l = 0;l < grandchild.children.length;l++){
									var greatgrandchild = grandchild.children[l];
									switch(greatgrandchild.id) {
									case "1.1.1.1.1": // Small
										greatgrandchild.distributedVolume[i].unit = parseFloat(greatgrandchild.distributedVolume[i].revenue / greatgrandchild.distributedVolume[i].volume);
										if(isNaN(greatgrandchild.distributedVolume[i].unit)){
											greatgrandchild.distributedVolume[i].unit = 0;
										}
										greatgrandchild.distributedVolume[i].unit = greatgrandchild.distributedVolume[i].unit.toFixed(2);
										break;
									case "1.1.1.1.2": // Medium
										greatgrandchild.distributedVolume[i].unit = parseFloat(greatgrandchild.distributedVolume[i].revenue / greatgrandchild.distributedVolume[i].volume);
										if(isNaN(greatgrandchild.distributedVolume[i].unit)){
											greatgrandchild.distributedVolume[i].unit = 0;
										}
										greatgrandchild.distributedVolume[i].unit = greatgrandchild.distributedVolume[i].unit.toFixed(2);
										break;
									case "1.1.1.1.3": // Large
										greatgrandchild.distributedVolume[i].unit = parseFloat(greatgrandchild.distributedVolume[i].revenue / greatgrandchild.distributedVolume[i].volume);
										if(isNaN(greatgrandchild.distributedVolume[i].unit)){
											greatgrandchild.distributedVolume[i].unit = 0;
										}
										greatgrandchild.distributedVolume[i].unit = greatgrandchild.distributedVolume[i].unit.toFixed(2);
										break;
									}
								}
								break;
							case "1.1.1.2": // Unix
								grandchild.distributedVolume[i].unit = parseFloat(grandchild.distributedVolume[i].revenue / grandchild.distributedVolume[i].volume);
								if(isNaN(grandchild.distributedVolume[i].unit)){
									grandchild.distributedVolume[i].unit = 0;
								}
								grandchild.distributedVolume[i].unit = grandchild.distributedVolume[i].unit.toFixed(2);
								for(var l = 0;l < grandchild.children.length;l++){
									var greatgrandchild = grandchild.children[l];
									switch(greatgrandchild.id) {
									case "1.1.1.1.1": // Small
										greatgrandchild.distributedVolume[i].unit = parseFloat(greatgrandchild.distributedVolume[i].revenue / greatgrandchild.distributedVolume[i].volume);
										if(isNaN(greatgrandchild.distributedVolume[i].unit)){
											greatgrandchild.distributedVolume[i].unit = 0;
										}
										greatgrandchild.distributedVolume[i].unit = greatgrandchild.distributedVolume[i].unit.toFixed(2);
										break;
									case "1.1.1.1.2": // Medium
										greatgrandchild.distributedVolume[i].unit = parseFloat(greatgrandchild.distributedVolume[i].revenue / greatgrandchild.distributedVolume[i].volume);
										if(isNaN(greatgrandchild.distributedVolume[i].unit)){
											greatgrandchild.distributedVolume[i].unit = 0;
										}
										greatgrandchild.distributedVolume[i].unit = greatgrandchild.distributedVolume[i].unit.toFixed(2);
										break;
									case "1.1.1.1.3": // Large
										greatgrandchild.distributedVolume[i].unit = parseFloat(greatgrandchild.distributedVolume[i].revenue / greatgrandchild.distributedVolume[i].volume);
										if(isNaN(greatgrandchild.distributedVolume[i].unit)){
											greatgrandchild.distributedVolume[i].unit = 0;
										}
										greatgrandchild.distributedVolume[i].unit = greatgrandchild.distributedVolume[i].unit.toFixed(2);
										break;
									}
								}
								break;
							}
						}
						break;
					case "1.1.2": // Virtual Server
						child.distributedVolume[i].unit = parseFloat(child.distributedVolume[i].revenue / child.distributedVolume[i].volume);
						if(isNaN(child.distributedVolume[i].unit)){
							child.distributedVolume[i].unit = 0;
						}
						child.distributedVolume[i].unit = child.distributedVolume[i].unit.toFixed(2);
						for(var k = 0;k < child.children.length;k++){
							var grandchild = child.children[k];
							switch(grandchild.id) {
							case "1.1.2.1": // Public
								grandchild.distributedVolume[i].unit = parseFloat(grandchild.distributedVolume[i].revenue / grandchild.distributedVolume[i].volume);
								if(isNaN(grandchild.distributedVolume[i].unit)){
									grandchild.distributedVolume[i].unit = 0;
								}
								grandchild.distributedVolume[i].unit = grandchild.distributedVolume[i].unit.toFixed(2);
								for(var l = 0;l < grandchild.children.length;l++){
									var greatgrandchild = grandchild.children[l];
									switch(greatgrandchild.id) {
									case "1.1.2.1.1": // Windows/Linux
										greatgrandchild.distributedVolume[i].unit = parseFloat(greatgrandchild.distributedVolume[i].revenue / greatgrandchild.distributedVolume[i].volume);
										if(isNaN(greatgrandchild.distributedVolume[i].unit)){
											greatgrandchild.distributedVolume[i].unit = 0;
										}
										greatgrandchild.distributedVolume[i].unit = greatgrandchild.distributedVolume[i].unit.toFixed(2);
										for(var m = 0;m < greatgrandchild.children.length;m++){
											var greatgreatgrandchild = greatgrandchild.children[m];
											switch(greatgreatgrandchild.id) {
											case "1.1.2.1.1.1": // Small
												greatgreatgrandchild.distributedVolume[i].unit = parseFloat(greatgreatgrandchild.distributedVolume[i].revenue / greatgreatgrandchild.distributedVolume[i].volume);
												if(isNaN(greatgreatgrandchild.distributedVolume[i].unit)){
													greatgreatgrandchild.distributedVolume[i].unit = 0;
												}
												greatgreatgrandchild.distributedVolume[i].unit = greatgreatgrandchild.distributedVolume[i].unit.toFixed(2);
												break;
											case "1.1.2.1.1.2": // Medium
												greatgreatgrandchild.distributedVolume[i].unit = parseFloat(greatgreatgrandchild.distributedVolume[i].revenue / greatgreatgrandchild.distributedVolume[i].volume);
												if(isNaN(greatgreatgrandchild.distributedVolume[i].unit)){
													greatgreatgrandchild.distributedVolume[i].unit = 0;
												}
												greatgreatgrandchild.distributedVolume[i].unit = greatgreatgrandchild.distributedVolume[i].unit.toFixed(2);
												break;
											case "1.1.2.1.1.3": // Large
												greatgreatgrandchild.distributedVolume[i].unit = parseFloat(greatgreatgrandchild.distributedVolume[i].revenue / greatgreatgrandchild.distributedVolume[i].volume);
												if(isNaN(greatgreatgrandchild.distributedVolume[i].unit)){
													greatgreatgrandchild.distributedVolume[i].unit = 0;
												}
												greatgreatgrandchild.distributedVolume[i].unit = greatgreatgrandchild.distributedVolume[i].unit.toFixed(2);
												break;
											}
										}
										break;
									case "1.1.2.1.2": //Unix
										greatgrandchild.distributedVolume[i].unit = parseFloat(greatgrandchild.distributedVolume[i].revenue / greatgrandchild.distributedVolume[i].volume);
										if(isNaN(greatgrandchild.distributedVolume[i].unit)){
											greatgrandchild.distributedVolume[i].unit = 0;
										}
										greatgrandchild.distributedVolume[i].unit = greatgrandchild.distributedVolume[i].unit.toFixed(2);
										for(var m = 0;m < greatgrandchild.children.length;m++){
											var greatgreatgrandchild = greatgrandchild.children[m];
											switch(greatgreatgrandchild.id) {
											case "1.1.2.1.2.1": // Small
												greatgreatgrandchild.distributedVolume[i].unit = parseFloat(greatgreatgrandchild.distributedVolume[i].revenue / greatgreatgrandchild.distributedVolume[i].volume);
												if(isNaN(greatgreatgrandchild.distributedVolume[i].unit)){
													greatgreatgrandchild.distributedVolume[i].unit = 0;
												}
												greatgreatgrandchild.distributedVolume[i].unit = greatgreatgrandchild.distributedVolume[i].unit.toFixed(2);
												break;
											case "1.1.2.1.2.2": // Medium
												greatgreatgrandchild.distributedVolume[i].unit = parseFloat(greatgreatgrandchild.distributedVolume[i].revenue / greatgreatgrandchild.distributedVolume[i].volume);
												if(isNaN(greatgreatgrandchild.distributedVolume[i].unit)){
													greatgreatgrandchild.distributedVolume[i].unit = 0;
												}
												greatgreatgrandchild.distributedVolume[i].unit = greatgreatgrandchild.distributedVolume[i].unit.toFixed(2);
												break;
											case "1.1.2.1.2.3": // Large
												greatgreatgrandchild.distributedVolume[i].unit = parseFloat(greatgreatgrandchild.distributedVolume[i].revenue / greatgreatgrandchild.distributedVolume[i].volume);
												if(isNaN(greatgreatgrandchild.distributedVolume[i].unit)){
													greatgreatgrandchild.distributedVolume[i].unit = 0;
												}
												greatgreatgrandchild.distributedVolume[i].unit = greatgreatgrandchild.distributedVolume[i].unit.toFixed(2);
												break;
											}
										}
										break;
									}
								}
								break;
							case "1.1.2.2": // Private
								grandchild.distributedVolume[i].unit = parseFloat(grandchild.distributedVolume[i].revenue / grandchild.distributedVolume[i].volume);
								if(isNaN(grandchild.distributedVolume[i].unit)){
									grandchild.distributedVolume[i].unit = 0;
								}
								grandchild.distributedVolume[i].unit = grandchild.distributedVolume[i].unit.toFixed(2);
								for(var l = 0;l < grandchild.children.length;l++){
									var greatgrandchild = grandchild.children[l];
									switch(greatgrandchild.id) {
									case "1.1.2.2.1": // Windows/Linux
										greatgrandchild.distributedVolume[i].unit = parseFloat(greatgrandchild.distributedVolume[i].revenue / greatgrandchild.distributedVolume[i].volume);
										if(isNaN(greatgrandchild.distributedVolume[i].unit)){
											greatgrandchild.distributedVolume[i].unit = 0;
										}
										greatgrandchild.distributedVolume[i].unit = greatgrandchild.distributedVolume[i].unit.toFixed(2);
										for(var m = 0;m < greatgrandchild.children.length;m++){
											var greatgreatgrandchild = greatgrandchild.children[m];
											switch(greatgreatgrandchild.id) {
											case "1.1.2.2.1.1": // Small
												greatgreatgrandchild.distributedVolume[i].unit = parseFloat(greatgreatgrandchild.distributedVolume[i].revenue / greatgreatgrandchild.distributedVolume[i].volume);
												if(isNaN(greatgreatgrandchild.distributedVolume[i].unit)){
													greatgreatgrandchild.distributedVolume[i].unit = 0;
												}
												greatgreatgrandchild.distributedVolume[i].unit = greatgreatgrandchild.distributedVolume[i].unit.toFixed(2);
												break;
											case "1.1.2.2.1.2": // Medium
												greatgreatgrandchild.distributedVolume[i].unit = parseFloat(greatgreatgrandchild.distributedVolume[i].revenue / greatgreatgrandchild.distributedVolume[i].volume);
												if(isNaN(greatgreatgrandchild.distributedVolume[i].unit)){
													greatgreatgrandchild.distributedVolume[i].unit = 0;
												}
												greatgreatgrandchild.distributedVolume[i].unit = greatgreatgrandchild.distributedVolume[i].unit.toFixed(2);
												break;
											case "1.1.2.2.1.3": // Large
												greatgreatgrandchild.distributedVolume[i].unit = parseFloat(greatgreatgrandchild.distributedVolume[i].revenue / greatgreatgrandchild.distributedVolume[i].volume);
												if(isNaN(greatgreatgrandchild.distributedVolume[i].unit)){
													greatgreatgrandchild.distributedVolume[i].unit = 0;
												}
												greatgreatgrandchild.distributedVolume[i].unit = greatgreatgrandchild.distributedVolume[i].unit.toFixed(2);												
												break;
											}
										}
										break;
									case "1.1.2.2.2": //Unix
										greatgrandchild.distributedVolume[i].unit = parseFloat(greatgrandchild.distributedVolume[i].revenue / greatgrandchild.distributedVolume[i].volume);
										if(isNaN(greatgrandchild.distributedVolume[i].unit)){
											greatgrandchild.distributedVolume[i].unit = 0;
										}
										greatgrandchild.distributedVolume[i].unit = greatgrandchild.distributedVolume[i].unit.toFixed(2);
										for(var m = 0;m < greatgrandchild.children.length;m++){
											var greatgreatgrandchild = greatgrandchild.children[m];
											switch(greatgreatgrandchild.id) {
											case "1.1.2.2.2.1": // Small
												greatgreatgrandchild.distributedVolume[i].unit = parseFloat(greatgreatgrandchild.distributedVolume[i].revenue / greatgreatgrandchild.distributedVolume[i].volume);
												if(isNaN(greatgreatgrandchild.distributedVolume[i].unit)){
													greatgreatgrandchild.distributedVolume[i].unit = 0;
												}
												greatgreatgrandchild.distributedVolume[i].unit = greatgreatgrandchild.distributedVolume[i].unit.toFixed(2);
												break;
											case "1.1.2.2.2.2": // Medium
												greatgreatgrandchild.distributedVolume[i].unit = parseFloat(greatgreatgrandchild.distributedVolume[i].revenue / greatgreatgrandchild.distributedVolume[i].volume);
												if(isNaN(greatgreatgrandchild.distributedVolume[i].unit)){
													greatgreatgrandchild.distributedVolume[i].unit = 0;
												}
												greatgreatgrandchild.distributedVolume[i].unit = greatgreatgrandchild.distributedVolume[i].unit.toFixed(2);
												break;
											case "1.1.2.2.2.3": // Large
												greatgreatgrandchild.distributedVolume[i].unit = parseFloat(greatgreatgrandchild.distributedVolume[i].revenue / greatgreatgrandchild.distributedVolume[i].volume);
												if(isNaN(greatgreatgrandchild.distributedVolume[i].unit)){
													greatgreatgrandchild.distributedVolume[i].unit = 0;
												}
												greatgreatgrandchild.distributedVolume[i].unit = greatgreatgrandchild.distributedVolume[i].unit.toFixed(2);
												break;
											}
										}
										break;
									}
								}
								break;
							}
						}
					}
				}
				for(var n=0;n < $scope.hostingInfo.platformHosting.children.length;n++){
					$scope.hostingInfo.platformHosting.children[n].distributedVolume[i].unit = parseFloat($scope.hostingInfo.platformHosting.children[n].distributedVolume[i].revenue / $scope.hostingInfo.platformHosting.children[n].distributedVolume[i].volume);
					if(isNaN($scope.hostingInfo.platformHosting.children[n].distributedVolume[i].unit)){
						$scope.hostingInfo.platformHosting.children[n].distributedVolume[i].unit = 0;
					}
					$scope.hostingInfo.platformHosting.children[n].distributedVolume[i].unit = $scope.hostingInfo.platformHosting.children[n].distributedVolume[i].unit.toFixed(2);
				}
			}
		}
	}
	
	// Save Hosting Sub form data
    $scope.savehostingSubInfo = function(){
    	$scope.getIndicator();
    	setHostingYearlyInfo();
    	$scope.hostingInfoDto = {
    			offshoreAllowed : $scope.dealDetails.offshoreSelected == "Yes" ? true : false,
    			levelOfService :  $scope.dealDetails.standardwindowInfoSelected,
    			includeHardware: $scope.dealDetails.hardwareIncludedSelected == "Yes" ? true : false,
    			includeTooling: $scope.dealDetails.tooling == "Yes" ? true : false,
    			coLocation: $scope.dealDetails.colocation,
    			selectedSolutionId: $scope.selectedSolutionId,
    			hostingYearlyDataInfoDtoList : $scope.hostingInfo.hostingYearlyDataInfoDtos,
    			levelIndicator: $scope.getIndicator(),
    	    	dealId : $stateParams.dealId,
    	    	towerArchitect : $scope.dealDetails.towerArchitect
    	}
    	if($scope.hostingInfo.serverHosting.open && !$scope.hostingInfo.serverHosting.children[0].open){$confirm({text: 'Default distribution setting has been applied to child levels'})
 	        .then(function() {
    	var url = endpointsurls.SAVE_HOSTING_INFO+$stateParams.dealId;
        customInterceptor.postrequest(url,$scope.hostingInfoDto).then(function successCallback(response) {
        	$scope.isSaveHosting= DealService.getter() || isSave;
        	$scope.isSaveHosting.hostingS=true;
    		DealService.setter($scope.isSaveHosting);
    		$scope.putIndicator();
    		
		}, function errorCallback(response) {
			console.log(response.statusText);
			$alert({title:'Error',text: 'Failed to save data.'})
		});})}
    	else{
			var url = endpointsurls.SAVE_HOSTING_INFO+$stateParams.dealId;
	        customInterceptor.postrequest(url,$scope.hostingInfoDto).then(function successCallback(response) {
	        	$scope.isSaveHosting= DealService.getter() || isSave;
	        	$scope.isSaveHosting.hostingS=true;
	    		DealService.setter($scope.isSaveHosting);
	    		$scope.putIndicator();
	    		
			}, function errorCallback(response) {
				console.log(response.statusText);
				$alert({title:'Error',text: 'Failed to save data.'})});
		};
    }
    //putIndicator
    $scope.putIndicator=function()
    {
    	$scope.submissionIndicator[1]=1;
        var urlIndicator= endpointsurls.GENERic_INDICATOR+$stateParams.dealId+'?assessmentIndicator='+$scope.assessmentIndicator+'&submissionIndicator='+$scope.submissionIndicator.join(',');
       customInterceptor.putrequestWithoutData(urlIndicator).then(function successCallback(response) {
    	   $state.go('home.submission.genericSubmission.totalsSubmission',({dealId:$stateParams.dealId}));
       });
    }
    // get the Indicator value
    $scope.getIndicator=function()
    {
    	var levelIndicator='';
    	levelIndicator+=$scope.hostingInfo.serverHosting.open?1:0; // server
    	levelIndicator+=',';
    	if($scope.hostingInfo.serverHosting.open == true){
    		levelIndicator+=$scope.hostingInfo.serverHosting.children[0].open?1:0; // server volume
        	levelIndicator+=',';
        	if($scope.hostingInfo.serverHosting.children[0].open == true){
        		levelIndicator+=$scope.hostingInfo.serverHosting.children[0].children[0].open?1:0; // physical 
            	levelIndicator+=',';
            	if($scope.hostingInfo.serverHosting.children[0].children[0].open == true){
            		levelIndicator+=$scope.hostingInfo.serverHosting.children[0].children[0].children[0].open?1:0; // windows
                	levelIndicator+=',';
                	levelIndicator+=$scope.hostingInfo.serverHosting.children[0].children[0].children[1].open?1:0; // unix
                	levelIndicator+=',';
            	}
            	else{
            		levelIndicator+='0,0,';
            	}
            	levelIndicator+=$scope.hostingInfo.serverHosting.children[0].children[1].open?1:0; // virtual
            	levelIndicator+=',';
            	if($scope.hostingInfo.serverHosting.children[0].children[1].open == true){
            		levelIndicator+=$scope.hostingInfo.serverHosting.children[0].children[1].children[0].open?1:0; // public
                	levelIndicator+=',';
                	if($scope.hostingInfo.serverHosting.children[0].children[1].children[0].open == true){
                		levelIndicator+=$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].open?1:0; // windows
                    	levelIndicator+=',';
                    	levelIndicator+=$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].open?1:0; // unix
                    	levelIndicator+=',';
                	}
                	else{
                		levelIndicator+='0,0,';
                	}
                	levelIndicator+=$scope.hostingInfo.serverHosting.children[0].children[1].children[1].open?1:0; // private
                	levelIndicator+=',';
                	if($scope.hostingInfo.serverHosting.children[0].children[1].children[1].open == true){
                		levelIndicator+=$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].open?1:0; // windows
                    	levelIndicator+=',';
                    	levelIndicator+=$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].open?1:0; // unix
                    	levelIndicator+=',';
                	}
                	else{
                		levelIndicator+='0,0,';
                	}
            	}
            	else{
            		levelIndicator+='0,0,0,0,0,0,';
            	}
        	}
        	else{
        		levelIndicator+='0,0,0,0,0,0,0,0,0,0,';
        	}
    	}
    	else{
    		levelIndicator+='0,0,0,0,0,0,0,0,0,0,0,';
    	}
    	levelIndicator+=$scope.hostingInfo.platformHosting.open?1:0; // platform
        return levelIndicator;
    }
    
    // Extract data yearly wise
    function setHostingYearlyInfo() {
    	var yearlyInfoList = [];
    	for (var y = 0; y < $scope.dealInfo/12; y++) {
    		var yearlyData = {};
    		yearlyData.year = y+1;
    		if($scope.hostingInfo.serverHosting.open){
    			yearlyData.servers = $scope.hostingInfo.serverHosting.children[0].distributedVolume[y].volume;
        		yearlyData.physical = $scope.hostingInfo.serverHosting.children[0].children[0].distributedVolume[y].volume;
        		yearlyData.physicalWin = $scope.hostingInfo.serverHosting.children[0].children[0].children[0].distributedVolume[y].volume;
        		yearlyData.physicalWinSmall = $scope.hostingInfo.serverHosting.children[0].children[0].children[0].children[0].distributedVolume[y].volume;
        		yearlyData.physicalWinMedium = $scope.hostingInfo.serverHosting.children[0].children[0].children[0].children[1].distributedVolume[y].volume;
        		yearlyData.physicalWinLarge = $scope.hostingInfo.serverHosting.children[0].children[0].children[0].children[2].distributedVolume[y].volume;
        		yearlyData.physicalUnix = $scope.hostingInfo.serverHosting.children[0].children[0].children[1].distributedVolume[y].volume;
        		yearlyData.physicalUnixSmall = $scope.hostingInfo.serverHosting.children[0].children[0].children[1].children[0].distributedVolume[y].volume;
        		yearlyData.physicalUnixMedium = $scope.hostingInfo.serverHosting.children[0].children[0].children[1].children[1].distributedVolume[y].volume;
        		yearlyData.physicalUnixLarge = $scope.hostingInfo.serverHosting.children[0].children[0].children[1].children[2].distributedVolume[y].volume;
        		yearlyData.virtual = $scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[y].volume;
        		yearlyData.virtualPublic = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].distributedVolume[y].volume;
        		yearlyData.virtualPublicWin = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].distributedVolume[y].volume;
        		yearlyData.virtualPublicWinSmall = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].children[0].distributedVolume[y].volume;
        		yearlyData.virtualPublicWinMedium = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].children[1].distributedVolume[y].volume;
        		yearlyData.virtualPublicWinLarge = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].children[2].distributedVolume[y].volume;
        		yearlyData.virtualPublicUnix = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].distributedVolume[y].volume;
        		yearlyData.virtualPublicUnixSmall = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].children[0].distributedVolume[y].volume;
        		yearlyData.virtualPublicUnixMedium = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].children[1].distributedVolume[y].volume;
        		yearlyData.virtualPublicUnixLarge = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].children[2].distributedVolume[y].volume;
        		yearlyData.virtualPrivate = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].distributedVolume[y].volume;
        		yearlyData.virtualPrivateWin = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].distributedVolume[y].volume;
        		yearlyData.virtualPrivateWinSmall = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].children[0].distributedVolume[y].volume;
        		yearlyData.virtualPrivateWinMedium = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].children[1].distributedVolume[y].volume;
        		yearlyData.virtualPrivateWinLarge = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].children[2].distributedVolume[y].volume;
        		yearlyData.virtualPrivateUnix = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].distributedVolume[y].volume;
        		yearlyData.virtualPrivateUnixSmall = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].children[0].distributedVolume[y].volume;
        		yearlyData.virtualPrivateUnixMedium = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].children[1].distributedVolume[y].volume;
        		yearlyData.virtualPrivateUnixLarge = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].children[2].distributedVolume[y].volume;
    		}
    		if($scope.hostingInfo.platformHosting.open){
    			yearlyData.sqlInstances = $scope.hostingInfo.platformHosting.children[0].distributedVolume[y].volume;
        		yearlyData.cotsInstallations = $scope.hostingInfo.platformHosting.children[1].distributedVolume[y].volume;
    		}
    		yearlyData.hostingUnitPriceInfoDtoList= extractServerUnitPrice(y);
    		yearlyData.hostingRevenueInfoDtoList = extractServerRevenuePrice(y);
    		yearlyInfoList.push(yearlyData);
    	}
    	$scope.hostingInfo.hostingYearlyDataInfoDtos = yearlyInfoList;
    }
    
    // extract server price in units
    function extractServerUnitPrice(year){
    	var unitPrice = [];
    	var unitInfo={};

    	if($scope.viewBy.type == 'unit'){
    		if($scope.hostingInfo.serverHosting.open){
    			unitInfo.servers = $scope.hostingInfo.serverHosting.children[0].distributedVolume[year].unit;
        		unitInfo.physical = $scope.hostingInfo.serverHosting.children[0].children[0].distributedVolume[year].unit;
        		unitInfo.physicalWin = $scope.hostingInfo.serverHosting.children[0].children[0].children[0].distributedVolume[year].unit;
        		unitInfo.physicalWinSmall = $scope.hostingInfo.serverHosting.children[0].children[0].children[0].children[0].distributedVolume[year].unit;
        		unitInfo.physicalWinMedium = $scope.hostingInfo.serverHosting.children[0].children[0].children[0].children[1].distributedVolume[year].unit;
        		unitInfo.physicalWinLarge = $scope.hostingInfo.serverHosting.children[0].children[0].children[0].children[2].distributedVolume[year].unit;
        		unitInfo.physicalUnix = $scope.hostingInfo.serverHosting.children[0].children[0].children[1].distributedVolume[year].unit;
        		unitInfo.physicalUnixSmall = $scope.hostingInfo.serverHosting.children[0].children[0].children[1].children[0].distributedVolume[year].unit;
        		unitInfo.physicalUnixMedium = $scope.hostingInfo.serverHosting.children[0].children[0].children[1].children[1].distributedVolume[year].unit;
        		unitInfo.physicalUnixLarge = $scope.hostingInfo.serverHosting.children[0].children[0].children[1].children[2].distributedVolume[year].unit;
        		unitInfo.virtual = $scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[year].unit;
        		unitInfo.virtualPublic = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].distributedVolume[year].unit;
        		unitInfo.virtualPublicWin = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].distributedVolume[year].unit;
        		unitInfo.virtualPublicWinSmall = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].children[0].distributedVolume[year].unit;
        		unitInfo.virtualPublicWinMedium = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].children[1].distributedVolume[year].unit;
        		unitInfo.virtualPublicWinLarge = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].children[2].distributedVolume[year].unit;
        		unitInfo.virtualPublicUnix = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].distributedVolume[year].unit;
        		unitInfo.virtualPublicUnixSmall = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].children[0].distributedVolume[year].unit;
        		unitInfo.virtualPublicUnixMedium = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].children[1].distributedVolume[year].unit;
        		unitInfo.virtualPublicUnixLarge = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].children[2].distributedVolume[year].unit;
        		unitInfo.virtualPrivate = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].distributedVolume[year].unit;
        		unitInfo.virtualPrivateWin = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].distributedVolume[year].unit;
        		unitInfo.virtualPrivateWinSmall = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].children[0].distributedVolume[year].unit;
        		unitInfo.virtualPrivateWinMedium = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].children[1].distributedVolume[year].unit;
        		unitInfo.virtualPrivateWinLarge = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].children[2].distributedVolume[year].unit;
        		unitInfo.virtualPrivateUnix = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].distributedVolume[year].unit;
        		unitInfo.virtualPrivateUnixSmall = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].children[0].distributedVolume[year].unit;
        		unitInfo.virtualPrivateUnixMedium = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].children[1].distributedVolume[year].unit;
        		unitInfo.virtualPrivateUnixLarge = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].children[2].distributedVolume[year].unit;
    		}
    		if($scope.hostingInfo.platformHosting.open){
    			unitInfo.sqlInstances = $scope.hostingInfo.platformHosting.children[0].distributedVolume[year].unit;
        		unitInfo.cotsInstallations = $scope.hostingInfo.platformHosting.children[1].distributedVolume[year].unit;
    		}
    	}
    	
    	if($scope.viewBy.type == 'revenue'){
    		if($scope.hostingInfo.serverHosting.open){
    			unitInfo.servers = $scope.hostingInfo.serverHosting.children[0].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].distributedVolume[year].volume;
        		unitInfo.physical = $scope.hostingInfo.serverHosting.children[0].children[0].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[0].distributedVolume[year].volume;
        		unitInfo.physicalWin = $scope.hostingInfo.serverHosting.children[0].children[0].children[0].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[0].children[0].distributedVolume[year].volume;
        		unitInfo.physicalWinSmall = $scope.hostingInfo.serverHosting.children[0].children[0].children[0].children[0].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[0].children[0].children[0].distributedVolume[year].volume;
        		unitInfo.physicalWinMedium = $scope.hostingInfo.serverHosting.children[0].children[0].children[0].children[1].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[0].children[0].children[1].distributedVolume[year].volume;
        		unitInfo.physicalWinLarge = $scope.hostingInfo.serverHosting.children[0].children[0].children[0].children[2].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[0].children[0].children[2].distributedVolume[year].volume;
        		unitInfo.physicalUnix = $scope.hostingInfo.serverHosting.children[0].children[0].children[1].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[0].children[1].distributedVolume[year].volume;
        		unitInfo.physicalUnixSmall = $scope.hostingInfo.serverHosting.children[0].children[0].children[1].children[0].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[0].children[1].children[0].distributedVolume[year].volume;
        		unitInfo.physicalUnixMedium = $scope.hostingInfo.serverHosting.children[0].children[0].children[1].children[1].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[0].children[1].children[1].distributedVolume[year].volume;
        		unitInfo.physicalUnixLarge = $scope.hostingInfo.serverHosting.children[0].children[0].children[1].children[2].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[0].children[1].children[2].distributedVolume[year].volume;
        		unitInfo.virtual = $scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[year].volume;
        		unitInfo.virtualPublic = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[1].children[0].distributedVolume[year].volume;
        		unitInfo.virtualPublicWin = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].distributedVolume[year].volume;
        		unitInfo.virtualPublicWinSmall = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].children[0].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].children[0].distributedVolume[year].volume;
        		unitInfo.virtualPublicWinMedium = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].children[1].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].children[1].distributedVolume[year].volume;
        		unitInfo.virtualPublicWinLarge = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].children[2].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].children[2].distributedVolume[year].volume;
        		unitInfo.virtualPublicUnix = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].distributedVolume[year].volume;
        		unitInfo.virtualPublicUnixSmall = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].children[0].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].children[0].distributedVolume[year].volume;
        		unitInfo.virtualPublicUnixMedium = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].children[1].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].children[1].distributedVolume[year].volume;
        		unitInfo.virtualPublicUnixLarge = $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].children[2].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].children[2].distributedVolume[year].volume;
        		unitInfo.virtualPrivate = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[1].children[1].distributedVolume[year].volume;
        		unitInfo.virtualPrivateWin = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].distributedVolume[year].volume;
        		unitInfo.virtualPrivateWinSmall = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].children[0].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].children[0].distributedVolume[year].volume;
        		unitInfo.virtualPrivateWinMedium = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].children[1].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].children[1].distributedVolume[year].volume;
        		unitInfo.virtualPrivateWinLarge = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].children[2].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].children[2].distributedVolume[year].volume;
        		unitInfo.virtualPrivateUnix = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].distributedVolume[year].volume;
        		unitInfo.virtualPrivateUnixSmall = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].children[0].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].children[0].distributedVolume[year].volume;
        		unitInfo.virtualPrivateUnixMedium = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].children[1].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].children[1].distributedVolume[year].volume;
        		unitInfo.virtualPrivateUnixLarge = $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].children[2].distributedVolume[year].revenue / $scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].children[2].distributedVolume[year].volume;
    		}
    		if($scope.hostingInfo.platformHosting.open){
    			unitInfo.sqlInstances = $scope.hostingInfo.platformHosting.children[0].distributedVolume[year].revenue / $scope.hostingInfo.platformHosting.children[0].distributedVolume[year].volume;
        		unitInfo.cotsInstallations = $scope.hostingInfo.platformHosting.children[1].distributedVolume[year].revenue / $scope.hostingInfo.platformHosting.children[1].distributedVolume[year].volume;
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
    		if($scope.hostingInfo.serverHosting.open){
    			revenueInfo.servers = Math.round($scope.hostingInfo.serverHosting.children[0].distributedVolume[year].unit * $scope.hostingInfo.serverHosting.children[0].distributedVolume[year].volume) * 12;
    		}
    		if($scope.hostingInfo.platformHosting.open){
    			revenueInfo.sqlInstances = Math.round($scope.hostingInfo.platformHosting.children[0].distributedVolume[year].unit * $scope.hostingInfo.platformHosting.children[0].distributedVolume[year].volume) * 12;
        		revenueInfo.cotsInstallations = Math.round($scope.hostingInfo.platformHosting.children[1].distributedVolume[year].unit * $scope.hostingInfo.platformHosting.children[1].distributedVolume[year].volume) * 12;
    		}
    	}
    	if($scope.viewBy.type == 'revenue'){
    		if($scope.hostingInfo.serverHosting.open){
    			revenueInfo.servers = $scope.hostingInfo.serverHosting.children[0].distributedVolume[year].revenue * 12;
    		}
    		if($scope.hostingInfo.platformHosting.open){
    			revenueInfo.sqlInstances = $scope.hostingInfo.platformHosting.children[0].distributedVolume[year].revenue * 12;
        		revenueInfo.cotsInstallations = $scope.hostingInfo.platformHosting.children[1].distributedVolume[year].revenue * 12;
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

