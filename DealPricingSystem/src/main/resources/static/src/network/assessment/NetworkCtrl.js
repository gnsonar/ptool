priceToolcontrollers.controller('NetworkCtrl',function($scope,$http,HostingService,EndUserService,StorageService,DealService,NetworkService,customInterceptor,endpointsurls,$stateParams,$dialog,$confirm,$state,FactorService) {
	$scope.tabs=[{class:'active',href:"#inputtab",tabname:"Input"},{class:'inActive',href:"#caltab",tabname:"Calculate"},{class:'inActive',href:"#resulttab",tabname:"Result"}];

	//initialize all the variable needed for the page.
	

	var ctrl = this;
	ctrl.active = 0;
	$scope.id=undefined;
	$scope.dealId = $stateParams.Id
	$scope.hostingInfo = {};
	$scope.dealInfo={dealTermInYears:36};
	$scope.selectedSolutionId = '';
	$scope.avgPriceList=[];
	$scope.disablehosting = true;
	$scope.hostingChecked = false;
	$scope.enduserChecked = false;
	$scope.lastModStorageVol = [];
	$scope.dummy = [0,1,2,3,4,5,6,7,8,9];
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



	$scope.gotoTotals=function()
	{
     	
     	$scope.assessmentIndicator[4]=1;
        var urlIndicator= endpointsurls.GENERic_INDICATOR+$stateParams.dealId+'?assessmentIndicator='+$scope.assessmentIndicator.join(',')+'&submissionIndicator='+$scope.submissionIndicator;
       customInterceptor.putrequestWithoutData(urlIndicator).then(function successCallback(response) {
    	   $state.go('home.assessment.generic.totals',({dealId:$stateParams.dealId}));
       });
	}

	// Remove the error message
	$scope.removeClass=function()
	{
		$scope.showvalidationmsg=false;
		//$scope.error=false;
	}

	$scope.copyBakcupVolume=[];

	// end user calculation
	$scope.userChecked=function(val)
    {
		 $scope.user=val;
           if(val){
        	   $scope.factorWanAceesPoint();

        	   $scope.factorLan();

           }
           else{
        	   $scope.removeFactorWanAceesPoint();
        	   $scope.removeFactorLanUser();
           }

    }

	// no of  site  check
	$scope.siteChecked=function(val)
    {
		$scope.sites=val;
           if(val){
        	   $scope.factorWanController();
        	   $scope.factorLan();
        	   $scope.factorWan();
           }
           else{
        	   $scope.removeFactorWanController();
        	   $scope.removeFactorLanSites();
        	   $scope.removeFactorWan();
           }

    }

	// no dataCenter check
	$scope.dataChecked=function(val)
    {
		$scope.dataCenter=val;
           if(val){
        	   $scope.factorWan();
           }
           else{
        	   $scope.removeFactorWan();
           }

    }
	
	
	$scope.serverChecked=function(val)
    {
		$scope.server=val;
           if(val){
        	   $scope.factorLan();
           }
           else{
        	   $scope.removeFactorLanServer();
           }

    }
	
	// wlan asess point
	$scope.factorWanAceesPoint=function()
	{

		for(var i=0;i<$scope.networkInfo.wlanAccess.children[0].distributedVolume.length;i++)
        {
       // $scope.copyBakcupVolume=angular.copy($scope.copyBakcup);
			$scope.networkInfo.wlanAccess.children[0].distributedVolume[i].volume=Math.round($scope.dealYearlyDataInfoDtoList[i].noOfUsers/$scope.networkWlanAccessPointFactorDtoList[0].factorSize);
        }
	}


	// wlan asess point	remove
	$scope.removeFactorWanAceesPoint=function()
	{
		for(var i=0;i<$scope.dealYearlyDataInfoDtoList.length;i++)
        {
			if($scope.id>0){
				$scope.networkInfoWlanAccessBackup=FactorService.getter();
				$scope.networkInfo.wlanAccess.children[0].distributedVolume[i].volume = $scope.networkInfoWlanAccessBackup.networkYearlyDataInfoDtoList[i].wlanAccesspoint;
			}
			else{
				// $scope.copyBakcupVolume=angular.copy($scope.copyBakcup);
				$scope.networkInfo.wlanAccess.children[0].distributedVolume[i].volume=undefined;
			}
        }
	}

	//factorWanController
	$scope.factorWanController=function(){
		for(var i=0;i<$scope.networkInfo.wlanCon.children[0].distributedVolume.length;i++)
        {
       // $scope.copyBakcupVolume=angular.copy($scope.copyBakcup);
			$scope.networkInfo.wlanCon.children[0].distributedVolume[i].volume=Math.round($scope.dealYearlyDataInfoDtoList[i].noOfSites*$scope.networkWlanControllerFactorDtoList[0].factorSize);
        }

	}


	//removeFactorWanController
	$scope.removeFactorWanController=function(){
		for(var i=0;i<$scope.dealYearlyDataInfoDtoList.length;i++)
        {
			if($scope.id>0){
				$scope.networkInfoWlanConBackup=FactorService.getter();
				$scope.networkInfo.wlanCon.children[0].distributedVolume[i].volume = $scope.networkInfoWlanConBackup.networkYearlyDataInfoDtoList[i].wlanControllers;
			}
			else{
				// $scope.copyBakcupVolume=angular.copy($scope.copyBakcup);
				$scope.networkInfo.wlanCon.children[0].distributedVolume[i].volume=undefined;
			}
        }
	}

	//factorWan
	$scope.factorWan=function(){
		for(var i=0;i<$scope.networkInfo.wanDevice.children[0].distributedVolume.length;i++)
        {
       // $scope.copyBakcupVolume=angular.copy($scope.copyBakcup);
			if($scope.sites){
			$scope.networkInfo.wanDevice.children[0].distributedVolume[i].volume=
			Math.round($scope.dealYearlyDataInfoDtoList[i].noOfSites*$scope.networkWanFactorDtoList[1].factorSize);
			}
			if($scope.dataCenter){
				$scope.networkInfo.wanDevice.children[0].distributedVolume[i].volume=
				Math.round($scope.dealYearlyDataInfoDtoList[i].noOfDatacenters*$scope.networkWanFactorDtoList[0].factorSize);
				}

			if($scope.sites && $scope.dataCenter){
				$scope.networkInfo.wanDevice.children[0].distributedVolume[i].volume=
					Math.round($scope.dealYearlyDataInfoDtoList[i].noOfDatacenters*$scope.networkWanFactorDtoList[0].factorSize)+
					Math.round($scope.dealYearlyDataInfoDtoList[i].noOfSites*$scope.networkWanFactorDtoList[1].factorSize)
			}
		};
		calculateNetworkVolume($scope.networkInfo.wanDevice.children[0]);
	}

	//removeFactorWan
	$scope.removeFactorWan=function(){
		for(var i=0;i<$scope.dealYearlyDataInfoDtoList.length;i++)
        {
			if(!$scope.sites){
				$scope.networkInfo.wanDevice.children[0].distributedVolume[i].volume=
				Math.round($scope.networkInfo.wanDevice.children[0].distributedVolume[i].volume)-
				Math.round($scope.dealYearlyDataInfoDtoList[i].noOfSites*$scope.networkWanFactorDtoList[1].factorSize);
				}
				if(!$scope.dataCenter){
					$scope.networkInfo.wanDevice.children[0].distributedVolume[i].volume=
				    Math.round($scope.networkInfo.wanDevice.children[0].distributedVolume[i].volume)-
					Math.round($scope.dealYearlyDataInfoDtoList[i].noOfDatacenters*$scope.networkWanFactorDtoList[0].factorSize);
					}

				if(!$scope.sites && !$scope.dataCenter){
					$scope.networkInfo.wanDevice.children[0].distributedVolume[i].volume=undefined;
					if($scope.id>0){
						$scope.networkInfoWanBackup=FactorService.getter();
						$scope.networkInfo.wanDevice.children[0].distributedVolume[i].volume=$scope.networkInfoWanBackup.networkYearlyDataInfoDtoList[i].wanDevices;
						}

				}
        }
		calculateNetworkVolume($scope.networkInfo.wanDevice.children[0]);
	}


	$scope.factorLan=function(){

		for(var i=0;i<$scope.networkInfo.lanDevice.children[0].distributedVolume.length;i++)
        {
			if($scope.user)
				{
			$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume=
			Math.round($scope.dealYearlyDataInfoDtoList[i].noOfUsers/$scope.networkLanFactorDtoList[1].factorSize);
				}
			if($scope.sites)
			{
				$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume=
				Math.round($scope.dealYearlyDataInfoDtoList[i].noOfSites*$scope.networkLanFactorDtoList[0].factorSize);
			}
			if($scope.server)
			{
				$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume=
					Math.round($scope.hostingServerList[i].volume/$scope.networkLanFactorDtoList[2].factorSize);
			}
			
			
			if($scope.user && $scope.sites)
			{
			$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume=
			  Math.round($scope.dealYearlyDataInfoDtoList[i].noOfUsers/$scope.networkLanFactorDtoList[1].factorSize)+
			  Math.round($scope.dealYearlyDataInfoDtoList[i].noOfSites*$scope.networkLanFactorDtoList[0].factorSize)
		 
			}
			
			if($scope.sites && $scope.server)
			{
			$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume=
				Math.round($scope.dealYearlyDataInfoDtoList[i].noOfSites*$scope.networkLanFactorDtoList[0].factorSize)+
				Math.round($scope.hostingServerList[i].volume/$scope.networkLanFactorDtoList[2].factorSize);
		 
			}
			if($scope.user && $scope.server)
			{
			$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume=
		   Math.round($scope.dealYearlyDataInfoDtoList[i].noOfUsers/$scope.networkLanFactorDtoList[1].factorSize)+
		   Math.round($scope.hostingServerList[i].volume/$scope.networkLanFactorDtoList[2].factorSize);
			}
			
			if($scope.user && $scope.sites && $scope.server)
			{
			$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume=
		  Math.round($scope.dealYearlyDataInfoDtoList[i].noOfUsers/$scope.networkLanFactorDtoList[1].factorSize)+
		  Math.round($scope.dealYearlyDataInfoDtoList[i].noOfSites*$scope.networkLanFactorDtoList[0].factorSize)+
		  Math.round($scope.hostingServerList[i].volume/$scope.networkLanFactorDtoList[2].factorSize);

			}
        }
		calculateNetworkVolume($scope.networkInfo.lanDevice.children[0]);
	}

	//removeFactorLan
	$scope.removeFactorLanUser=function(){
		for(var i=0;i<$scope.dealYearlyDataInfoDtoList.length;i++)
        {
     
			if(!$scope.user)
			{
		$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume=
			Math.round($scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume)-
			Math.round($scope.dealYearlyDataInfoDtoList[i].noOfUsers/$scope.networkLanFactorDtoList[1].factorSize);
			}
		
		
		if(!$scope.user && !$scope.sites && !$scope.server)
		{
			$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume=undefined;
			if($scope.id>0){
				$scope.networkInfoLanBackup=FactorService.getter();
				$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume=$scope.networkInfoLanBackup.networkYearlyDataInfoDtoList[i].lanDevices;
				}
		}
		
        }
		calculateNetworkVolume($scope.networkInfo.lanDevice.children[0]);
	}

	//removeFactorLan
	$scope.removeFactorLanServer=function(){
		for(var i=0;i<$scope.dealYearlyDataInfoDtoList.length;i++)
        {
			if(!$scope.server)
			{
				$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume=
					$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume-
					Math.round($scope.hostingServerList[i].volume/$scope.networkLanFactorDtoList[2].factorSize);
			}
		if(!$scope.user && !$scope.sites && !$scope.server)
		{
			$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume=undefined;
			if($scope.id>0){
				$scope.networkInfoLanBackup=FactorService.getter();
				$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume=$scope.networkInfoLanBackup.networkYearlyDataInfoDtoList[i].lanDevices;
				}
		}
        }
     
		calculateNetworkVolume($scope.networkInfo.lanDevice.children[0]);
		
	}
	
	
	//removeFactorLan
	$scope.removeFactorLanSites=function(){
		for(var i=0;i<$scope.dealYearlyDataInfoDtoList.length;i++)
        {
			if(!$scope.sites)
			{
				$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume=
					Math.round($scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume)-
					Math.round($scope.dealYearlyDataInfoDtoList[i].noOfSites*$scope.networkLanFactorDtoList[0].factorSize);
			}
		if(!$scope.user && !$scope.sites && !$scope.server)
		{
			$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume=undefined;
			if($scope.id>0){
				$scope.networkInfoLanBackup=FactorService.getter();
				$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume=$scope.networkInfoLanBackup.networkYearlyDataInfoDtoList[i].lanDevices;
				}
		}
        }
     
		calculateNetworkVolume($scope.networkInfo.lanDevice.children[0]);
		
	}
	
	// function for toggle panel
	$scope.togglePanel = function(level) {
		if(level.open){
			level.open=false;
		}else
		{
			level.open=true;
		}
	};

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
	$scope.tabEvent=function($event,index,val){
		if($event.keyCode == 13 || $event.keyCode == 9){
			switch(val) {
			case "WanVolume":
				for(var i=index;i<$scope.networkInfo.wanDevice.children[0].distributedVolume.length;i++){
					$scope.networkInfo.wanDevice.children[0].distributedVolume[i].volume = $scope.networkInfo.wanDevice.children[0].distributedVolume[index].volume;
				}
				break;
			case "LanVolume":
				for(var i=index;i<$scope.networkInfo.lanDevice.children[0].distributedVolume.length;i++){
					$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume = $scope.networkInfo.lanDevice.children[0].distributedVolume[index].volume;
				}
				break;
			case "WlanVolume":
				for(var i=index;i<$scope.networkInfo.wlanCon.children[0].distributedVolume.length;i++){
					$scope.networkInfo.wlanCon.children[0].distributedVolume[i].volume = $scope.networkInfo.wlanCon.children[0].distributedVolume[index].volume;
				}
				break;
			case "WlanAccessVolume":
				for(var i=index;i<$scope.networkInfo.wlanAccess.children[0].distributedVolume.length;i++){
					$scope.networkInfo.wlanAccess.children[0].distributedVolume[i].volume = $scope.networkInfo.wlanAccess.children[0].distributedVolume[index].volume;
				}
				break;
			case "LoadBalanceVolume":
				for(var i=index;i<$scope.networkInfo.loadBalance.children[0].distributedVolume.length;i++){
					$scope.networkInfo.loadBalance.children[0].distributedVolume[i].volume = $scope.networkInfo.loadBalance.children[0].distributedVolume[index].volume;
				}
				break;
			case "VPNVolume":
				for(var i=index;i<$scope.networkInfo.vpnIp.children[0].distributedVolume.length;i++){
					$scope.networkInfo.vpnIp.children[0].distributedVolume[i].volume = $scope.networkInfo.vpnIp.children[0].distributedVolume[index].volume;
				}
				break;
			case "DNSVolume":
				for(var i=index;i<$scope.networkInfo.dnsService.children[0].distributedVolume.length;i++){
					$scope.networkInfo.dnsService.children[0].distributedVolume[i].volume = $scope.networkInfo.dnsService.children[0].distributedVolume[index].volume;
				}
				break;
			case "FirewallVolume":
				for(var i=index;i<$scope.networkInfo.firewalls.children[0].distributedVolume.length;i++){
					$scope.networkInfo.firewalls.children[0].distributedVolume[i].volume = $scope.networkInfo.firewalls.children[0].distributedVolume[index].volume;
				}
				break;
			case "ReserveProVolume":
				for(var i=index;i<$scope.networkInfo.reservePro.children[0].distributedVolume.length;i++){
					$scope.networkInfo.reservePro.children[0].distributedVolume[i].volume = $scope.networkInfo.reservePro.children[0].distributedVolume[index].volume;
				}
				break;
			}
		}
	}

	// get the drop-down values
	$scope.getNetworkDropdowns=function(){
		var url = endpointsurls.NETWORK_DROPDOWN+$stateParams.dealId;
        customInterceptor.getrequest(url).then(function successCallback(response) {
			$scope.wanSolList = response.data.networkWanSolutionsDtoList;
			$scope.lanSolList = response.data.networkLanSolutionsDtoList;
			$scope.hostingServerList=response.data.physicalServerList;
			if($scope.hostingServerList != null && $scope.hostingServerList.length > 0 && $scope.hostingServerList[0].volume != null){
				$scope.disablehosting = false;
			}
			$scope.genericDealInfoDto = response.data;
			$scope.dealInfoDto = response.data.dealInfoDto;
			$scope.dealYearlyDataInfoDtoList = $scope.dealInfoDto.dealYearlyDataInfoDtos;
			$scope.networkWanFactorDtoList=response.data.networkWanFactorDtoList;
			$scope.networkLanFactorDtoList=response.data.networkLanFactorDtoList;
			$scope.networkWlanControllerFactorDtoList=response.data.networkWlanControllerFactorDtoList;
			$scope.networkWlanAccessPointFactorDtoList=response.data.networkWlanAccessPointFactorDtoList;
			$scope.submissionIndicator=response.data.dealInfoDto.submissionIndicator;
        	$scope.assessmentIndicator=response.data.dealInfoDto.assessmentIndicator.split(',');
        	var isSave={'genericA':($scope.assessmentIndicator[0]==1)?true:false,
        			'hostingA':($scope.assessmentIndicator[1]==1)?true:false,
    		     	'storageA':($scope.assessmentIndicator[2]==1)?true:false,
    		     	'endUserA':($scope.assessmentIndicator[3]==1)?true:false,
    		     	'networkA':($scope.assessmentIndicator[4]==1)?true:false,
    		     	'serviceDeskA':($scope.assessmentIndicator[5]==1)?true:false,
    		     	'appsA':($scope.assessmentIndicator[6]==1)?true:false,
    		     	'retailA':($scope.assessmentIndicator[7]==1)?true:false
    		};
			$scope.isSaveNetwork= DealService.getter() || isSave;
			DealService.setter($scope.isSaveNetwork);
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
		        FactorService.setter(angular.copy(response.data));
	       		$scope.existingNetworkInfo = response.data;
	       		$scope.level=$scope.existingNetworkInfo.levelIndicator.split(',');
	       		$scope.networkInfo.wanDevice.open=($scope.level[0]==1)?true:false;
	       		$scope.networkInfo.wanDevice.children[0].open=($scope.level[1]==1)?true:false;
	       		$scope.networkInfo.lanDevice.open=($scope.level[2]==1)?true:false,
	       		$scope.networkInfo.lanDevice.children[0].open=($scope.level[3]==1)?true:false,
	       		$scope.networkInfo.wlanCon.open=($scope.level[4]==1)?true:false,
	       		$scope.networkInfo.wlanAccess.open=($scope.level[5]==1)?true:false,
	       		$scope.networkInfo.loadBalance.open=($scope.level[6]==1)?true:false,
	       		$scope.networkInfo.vpnIp.open=($scope.level[7]==1)?true:false,
	       		$scope.networkInfo.dnsService.open=($scope.level[8]==1)?true:false,
	       		$scope.networkInfo.firewalls.open=($scope.level[9]==1)?true:false,
	       		$scope.networkInfo.reservePro.open=($scope.level[10]==1)?true:false
	       		$scope.id=$scope.existingNetworkInfo.networkId;
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
   		$scope.selectedSolutionWanId = $scope.existingNetworkInfo.selectedWanSolutionId;
   		$scope.selectedSolutionLanId = $scope.existingNetworkInfo.selectedLanSolutionId;
   		getExistingNetworkYearlyInfo();
   		
   		if($scope.selectedSolutionWanId != null){
   			var CustomSolutionWanName = _.where($scope.wanSolList, {solutionId: $scope.selectedSolutionWanId})[0].solutionName;
   			if(CustomSolutionWanName != 'Custom'){
   				$scope.onChangeWanDistSetting($scope.selectedSolutionWanId);
   			}else{
   				
   			}
   		}
   		
   		if($scope.selectedSolutionLanId != null){
   			var CustomSolutionLanName = _.where($scope.lanSolList, {solutionId: $scope.selectedSolutionLanId})[0].solutionName;
   			if(CustomSolutionLanName != 'Custom'){
   				$scope.onChangeLanDistSetting($scope.selectedSolutionLanId);
   			}
   		}
   		
    }
    //
    
   
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
        	}
    	}
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

	// Calculate child volume when parent volume change
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
		}

	}

	//Save Network Info Data
	$scope.saveNetworkInputInfo = function(){
		$scope.getIndicator();
		setNetworkYearlyInfo();
		$scope.networkInfoDto = {
    			offshoreAllowed : $scope.dealDetails.offshoreSelected == "Yes" ? true : false,
    			includeHardware : $scope.dealDetails.hardwareIncludedSelected == "Yes" ? true : false,
    			levelOfService :  $scope.dealDetails.standardwindowInfoSelected,
    			selectedWanSolutionId : $scope.networkInfo.wanDevice.open == true ? $scope.selectedSolutionWanId : null,
    			selectedLanSolutionId : $scope.networkInfo.lanDevice.open == true ? $scope.selectedSolutionLanId : null,
    			dealId : $stateParams.dealId,
    	    	levelIndicator:$scope.getIndicator(),
    	    	towerArchitect : $scope.dealDetails.towerArchitect,
    	    	networkYearlyDataInfoDtoList : $scope.networkInfo.networkYearlyDataInfoDtos,
    	}
		if(($scope.networkInfo.wanDevice.open && !$scope.networkInfo.wanDevice.children[0].open)
				|| ($scope.networkInfo.lanDevice.open && !$scope.networkInfo.lanDevice.children[0].open)) {
			$confirm({text: 'Default distribution setting has been applied to child levels'})
 	        .then(function() {
 				var url = endpointsurls.SAVE_NETWORK_INFO+$stateParams.dealId;
 		        customInterceptor.postrequest(url,$scope.networkInfoDto).then(function successCallback(response) {
 		        	$scope.goToCal();
 		        	$scope.id=response.data.networkId;
 				}, function errorCallback(response) {
 					console.log(response.statusText);
 					$alert({title:'Error',text: 'Failed to save data.'})});
    	})}
		else{
			var url = endpointsurls.SAVE_NETWORK_INFO+$stateParams.dealId;
	        customInterceptor.postrequest(url,$scope.networkInfoDto).then(function successCallback(response) {
	        	$scope.goToCal();
		        $scope.id=response.data.networkId;
			}, function errorCallback(response) {
				console.log(response.statusText);
				$alert({title:'Error',text: 'Failed to save data.'})
			});
		}
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
    		yearlyInfoList.push(yearlyData);
    	}
    	$scope.networkInfo.networkYearlyDataInfoDtos = yearlyInfoList;
    }

    // --------------------------------------------------------------Calculate Tab----------------------------------------------------------------------------------

    // Recalculate
    $scope.reCalculate=function(){
 	 $scope.resetCalculateDetails();
 	 var putDeatls=  {"offshoreAllowed" : $scope.dealDetails.offshoreSelected == "Yes" ? true : false,
 			 		  "levelOfService" :$scope.dealDetails.standardwindowInfoSelected,
 			 		  "includeHardware" : $scope.dealDetails.hardwareIncludedSelected == "Yes" ? true : false,
 				      "multiLingual" :  null,
 				      "toolingIncluded" : null};
 	     var url = endpointsurls.NETWORK_RECALCULATE+$scope.id;
         customInterceptor.putrequest(url,putDeatls).then(function successCallback(response) {
         	$scope.tabReset(1);
         	$scope.networkingCalculateDto=response.data;
         	extractNetworkingWanDevicesAvg($scope.networkInfo.wanDevice.children[0]);
			extractWanSetailNetworkingChildLevelWiseAverages($scope.networkInfo.wanDevice.children[0].children);
			extractNetworkingLanDevicesAvg($scope.networkInfo.lanDevice.children[0]);
			extractLanSetailNetworkingChildLevelWiseAverages($scope.networkInfo.lanDevice.children[0].children);
			extractNetworkingWanControllersDevicesAvg($scope.networkInfo.wlanCon.children[0]);
			extractNetworkingwlanAccessDevicesAvg($scope.networkInfo.wlanAccess.children[0]);
			extractNetworkingloadBalanceDevicesAvg($scope.networkInfo.loadBalance.children[0]);
			extractNetworkingvpnIpDevicesAvg($scope.networkInfo.vpnIp.children[0]);
			extractNetworkingdnsServiceDevicesAvg($scope.networkInfo.dnsService.children[0]);
			extractNetworkingfirewallsDevicesAvg($scope.networkInfo.firewalls.children[0]);
			extractNetworkingreserveProDevicesAvg($scope.networkInfo.reservePro.children[0]);
         	;});
         $scope.createUnitPriceForLevels($scope.networkInfo.wanDevice.children[0].distributedVolume.length);
         
 	}

		  $scope.populatedNetworkingCalculateTabDetails = function(){
			  var url = endpointsurls.NETWORK_CALCULATE_INFO+$stateParams.dealId;
		        customInterceptor.getrequest(url).then(function successCallback(response) {
		        	$scope.networkingCalculateDto  = response.data;
					extractNetworkingWanDevicesAvg($scope.networkInfo.wanDevice.children[0]);
					extractWanSetailNetworkingChildLevelWiseAverages($scope.networkInfo.wanDevice.children[0].children);
					extractNetworkingLanDevicesAvg($scope.networkInfo.lanDevice.children[0]);
					extractLanSetailNetworkingChildLevelWiseAverages($scope.networkInfo.lanDevice.children[0].children);
					extractNetworkingWanControllersDevicesAvg($scope.networkInfo.wlanCon.children[0]);
					extractNetworkingwlanAccessDevicesAvg($scope.networkInfo.wlanAccess.children[0]);
					extractNetworkingloadBalanceDevicesAvg($scope.networkInfo.loadBalance.children[0]);
					extractNetworkingvpnIpDevicesAvg($scope.networkInfo.vpnIp.children[0]);
					extractNetworkingdnsServiceDevicesAvg($scope.networkInfo.dnsService.children[0]);
					extractNetworkingfirewallsDevicesAvg($scope.networkInfo.firewalls.children[0]);
					extractNetworkingreserveProDevicesAvg($scope.networkInfo.reservePro.children[0]);
		        	 $scope.unitPriceDto=[];
		 			$scope.createUnitPriceForLevels($scope.networkInfo.wanDevice.children[0].distributedVolume.length);
				}, function errorCallback(response) {
					console.log(response.statusText);
					if(response.status=='401'){
						$state.go('login');
					}
				})



			}

		// function used for creating the unit price year wise
		  $scope.createUnitPriceForLevels=function(val)
		  {
			  $scope.unitPriceDto=[];
			  for(var i=1;i<=val;i++)
				  {
				  var unitPrice={};
				   unitPrice.year=i;
				   unitPrice.totalWanUnitPrice=0;
				   unitPrice.totalSmallWanUnitPrice=0;
				   unitPrice.totalMediumWanUnitPrice=0;
				   unitPrice.totalLargeWanUnitPrice=0;

				   unitPrice.totalSmallWanRevenue=0;
				   unitPrice.totalMediumWanRevenue=0;
				   unitPrice.totalLargeWanRevenue=0;
				   unitPrice.totalWanRevenue=0;

				   unitPrice.totalLanUnitPrice=0;
				   unitPrice.totalSmallLanUnitPrice=0;
				   unitPrice.totalMediumLanUnitPrice=0;
				   unitPrice.totalLargeLanUnitPrice=0;

				   unitPrice.totalLanRevenue=0;
				   unitPrice.totalSmallLanRevenue=0;
				   unitPrice.totalMediumLanRevenue=0;
				   unitPrice.totalLargeLanRevenue=0;

				   unitPrice.totalWlanControllersUnitPrice=0;
				   unitPrice.totalWlanAccesspointUnitPrice=0;
				   unitPrice.totalLoadBalancersUnitPrice=0;
				   unitPrice.totalVpnIpSecUnitPrice=0;
				   unitPrice.totalDnsDhcpServiceUnitPrice=0;
				   unitPrice.totalFirewallsUnitPrice=0;
				   unitPrice.totalProxiesUnitPrice=0;

				   unitPrice.totalWlanControllersRevenue=0;
				   unitPrice.totalWlanAccesspointRevenue=0;
				   unitPrice.totalLoadBalancersRevenue=0;
				   unitPrice.totalVpnIpSecRevenue=0;
				   unitPrice.totalDnsDhcpServiceRevenue=0;
				   unitPrice.totalFirewallsRevenue=0;
				   unitPrice.totalProxiesRevenue=0;
				  $scope.unitPriceDto.push(unitPrice);
				  }

		  }
		  // extract wanDevice
		  function extractNetworkingWanDevicesAvg(parent){
			  if($scope.networkingCalculateDto.wanDevicesCalculateDto!=null){
				parent.benchLow = $scope.networkingCalculateDto.wanDevicesCalculateDto.benchDealLowAvgUnitPrice!=null?$scope.networkingCalculateDto.wanDevicesCalculateDto.benchDealLowAvgUnitPrice:"NA";
				parent.benchTarget =$scope.networkingCalculateDto.wanDevicesCalculateDto.benchDealTargetAvgUnitPrice!=null?$scope.networkingCalculateDto.wanDevicesCalculateDto.benchDealTargetAvgUnitPrice:"NA";
				parent.pastAvg =$scope.networkingCalculateDto.wanDevicesCalculateDto.pastDealAvgUnitPrice!=null?$scope.networkingCalculateDto.wanDevicesCalculateDto.pastDealAvgUnitPrice:"NA";
				parent.compAvg =$scope.networkingCalculateDto.wanDevicesCalculateDto.compDealAvgUnitPrice!=null?$scope.networkingCalculateDto.wanDevicesCalculateDto.compDealAvgUnitPrice:"NA";
			}
		  else
			  {
			  parent.benchLow='NA';
			  parent.benchTarget='NA';
			  parent.pastAvg='NA';
			  parent.compAvg='NA';

			  }
		  }

		  // extract lanDevice
		  function extractNetworkingLanDevicesAvg(parent){
			  if($scope.networkingCalculateDto.lanDevicesCalculateDto!=null){
					parent.benchLow = $scope.networkingCalculateDto.lanDevicesCalculateDto.benchDealLowAvgUnitPrice!=null?$scope.networkingCalculateDto.lanDevicesCalculateDto.benchDealLowAvgUnitPrice:"NA";
					parent.benchTarget =$scope.networkingCalculateDto.lanDevicesCalculateDto.benchDealTargetAvgUnitPrice!=null?$scope.networkingCalculateDto.lanDevicesCalculateDto.benchDealTargetAvgUnitPrice:"NA";
					parent.pastAvg =$scope.networkingCalculateDto.lanDevicesCalculateDto.pastDealAvgUnitPrice!=null?$scope.networkingCalculateDto.lanDevicesCalculateDto.pastDealAvgUnitPrice:"NA";
					parent.compAvg =$scope.networkingCalculateDto.lanDevicesCalculateDto.compDealAvgUnitPrice!=null?$scope.networkingCalculateDto.lanDevicesCalculateDto.compDealAvgUnitPrice:"NA";
				}
			  else
				  {
				  parent.benchLow='NA';
				  parent.benchTarget='NA';
				  parent.pastAvg='NA';
				  parent.compAvg='NA';

				  }
				  }


		  // extract WLAN Controllers
		  function extractNetworkingWanControllersDevicesAvg(parent){
			  if($scope.networkingCalculateDto.wlanControllersCalculateDto!=null){
					parent.benchLow = $scope.networkingCalculateDto.wlanControllersCalculateDto.benchDealLowAvgUnitPrice!=null?$scope.networkingCalculateDto.wlanControllersCalculateDto.benchDealLowAvgUnitPrice:"NA";
					parent.benchTarget =$scope.networkingCalculateDto.wlanControllersCalculateDto.benchDealTargetAvgUnitPrice!=null?$scope.networkingCalculateDto.wlanControllersCalculateDto.benchDealTargetAvgUnitPrice:"NA";
					parent.pastAvg =$scope.networkingCalculateDto.wlanControllersCalculateDto.pastDealAvgUnitPrice!=null?$scope.networkingCalculateDto.wlanControllersCalculateDto.pastDealAvgUnitPrice:"NA";
					parent.compAvg =$scope.networkingCalculateDto.wlanControllersCalculateDto.compDealAvgUnitPrice!=null?$scope.networkingCalculateDto.wlanControllersCalculateDto.compDealAvgUnitPrice:"NA";
				}
			  else
				  {
				  parent.benchLow='NA';
				  parent.benchTarget='NA';
				  parent.pastAvg='NA';
				  parent.compAvg='NA';

				  }
				  }


		  // extract wlanAccess
		  function extractNetworkingwlanAccessDevicesAvg(parent){
			  if($scope.networkingCalculateDto.wlanAccesspointCalculateDto!=null){
					parent.benchLow = $scope.networkingCalculateDto.wlanAccesspointCalculateDto.benchDealLowAvgUnitPrice!=null?$scope.networkingCalculateDto.wlanAccesspointCalculateDto.benchDealLowAvgUnitPrice:"NA";
					parent.benchTarget =$scope.networkingCalculateDto.wlanAccesspointCalculateDto.benchDealTargetAvgUnitPrice!=null?$scope.networkingCalculateDto.wlanAccesspointCalculateDto.benchDealTargetAvgUnitPrice:"NA";
					parent.pastAvg =$scope.networkingCalculateDto.wlanAccesspointCalculateDto.pastDealAvgUnitPrice!=null?$scope.networkingCalculateDto.wlanAccesspointCalculateDto.pastDealAvgUnitPrice:"NA";
					parent.compAvg =$scope.networkingCalculateDto.wlanAccesspointCalculateDto.compDealAvgUnitPrice!=null?$scope.networkingCalculateDto.wlanAccesspointCalculateDto.compDealAvgUnitPrice:"NA";
				}
			  else
				  {
				  parent.benchLow='NA';
				  parent.benchTarget='NA';
				  parent.pastAvg='NA';
				  parent.compAvg='NA';

				  }
				  }
		  // extract loadBalance
		  function extractNetworkingloadBalanceDevicesAvg(parent){
			  if($scope.networkingCalculateDto.loadBalancersCalculateDto!=null){
					parent.benchLow = $scope.networkingCalculateDto.loadBalancersCalculateDto.benchDealLowAvgUnitPrice!=null?$scope.networkingCalculateDto.loadBalancersCalculateDto.benchDealLowAvgUnitPrice:"NA";
					parent.benchTarget =$scope.networkingCalculateDto.loadBalancersCalculateDto.benchDealTargetAvgUnitPrice!=null?$scope.networkingCalculateDto.loadBalancersCalculateDto.benchDealTargetAvgUnitPrice:"NA";
					parent.pastAvg =$scope.networkingCalculateDto.loadBalancersCalculateDto.pastDealAvgUnitPrice!=null?$scope.networkingCalculateDto.loadBalancersCalculateDto.pastDealAvgUnitPrice:"NA";
					parent.compAvg =$scope.networkingCalculateDto.loadBalancersCalculateDto.compDealAvgUnitPrice!=null?$scope.networkingCalculateDto.loadBalancersCalculateDto.compDealAvgUnitPrice:"NA";
				}
			  else
				  {
				  parent.benchLow='NA';
				  parent.benchTarget='NA';
				  parent.pastAvg='NA';
				  parent.compAvg='NA';

				  }
				  }
		  // extract vpnIp
		  function extractNetworkingvpnIpDevicesAvg(parent){
			  if($scope.networkingCalculateDto.vpnIpSecCalculateDto!=null){
					parent.benchLow = $scope.networkingCalculateDto.vpnIpSecCalculateDto.benchDealLowAvgUnitPrice!=null?$scope.networkingCalculateDto.vpnIpSecCalculateDto.benchDealLowAvgUnitPrice:"NA";
					parent.benchTarget =$scope.networkingCalculateDto.vpnIpSecCalculateDto.benchDealTargetAvgUnitPrice!=null?$scope.networkingCalculateDto.vpnIpSecCalculateDto.benchDealTargetAvgUnitPrice:"NA";
					parent.pastAvg =$scope.networkingCalculateDto.vpnIpSecCalculateDto.pastDealAvgUnitPrice!=null?$scope.networkingCalculateDto.vpnIpSecCalculateDto.pastDealAvgUnitPrice:"NA";
					parent.compAvg =$scope.networkingCalculateDto.vpnIpSecCalculateDto.compDealAvgUnitPrice!=null?$scope.networkingCalculateDto.vpnIpSecCalculateDto.compDealAvgUnitPrice:"NA";
				}
			  else
				  {
				  parent.benchLow='NA';
				  parent.benchTarget='NA';
				  parent.pastAvg='NA';
				  parent.compAvg='NA';

				  }
				  }


			//extractNetworkingdnsServiceDevicesAvg($scope.networkInfo.dnsService.children[0]);


			 // extract dnsService
		   function extractNetworkingdnsServiceDevicesAvg(parent){
			   if($scope.networkingCalculateDto.dnsDhcpServiceCalculateDto!=null){
					parent.benchLow = $scope.networkingCalculateDto.dnsDhcpServiceCalculateDto.benchDealLowAvgUnitPrice!=null?$scope.networkingCalculateDto.dnsDhcpServiceCalculateDto.benchDealLowAvgUnitPrice:"NA";
					parent.benchTarget =$scope.networkingCalculateDto.dnsDhcpServiceCalculateDto.benchDealTargetAvgUnitPrice!=null?$scope.networkingCalculateDto.dnsDhcpServiceCalculateDto.benchDealTargetAvgUnitPrice:"NA";
					parent.pastAvg =$scope.networkingCalculateDto.dnsDhcpServiceCalculateDto.pastDealAvgUnitPrice!=null?$scope.networkingCalculateDto.dnsDhcpServiceCalculateDto.pastDealAvgUnitPrice:"NA";
					parent.compAvg =$scope.networkingCalculateDto.dnsDhcpServiceCalculateDto.compDealAvgUnitPrice!=null?$scope.networkingCalculateDto.dnsDhcpServiceCalculateDto.compDealAvgUnitPrice:"NA";
				}
			  else
				  {
				  parent.benchLow='NA';
				  parent.benchTarget='NA';
				  parent.pastAvg='NA';
				  parent.compAvg='NA';

				  }
				  }

			// firewalls
			   function extractNetworkingfirewallsDevicesAvg(parent){
				   if($scope.networkingCalculateDto.firewallsCalculateDto!=null){
						parent.benchLow = $scope.networkingCalculateDto.firewallsCalculateDto.benchDealLowAvgUnitPrice!=null?$scope.networkingCalculateDto.firewallsCalculateDto.benchDealLowAvgUnitPrice:"NA";
						parent.benchTarget =$scope.networkingCalculateDto.firewallsCalculateDto.benchDealTargetAvgUnitPrice!=null?$scope.networkingCalculateDto.firewallsCalculateDto.benchDealTargetAvgUnitPrice:"NA";
						parent.pastAvg =$scope.networkingCalculateDto.firewallsCalculateDto.pastDealAvgUnitPrice!=null?$scope.networkingCalculateDto.firewallsCalculateDto.pastDealAvgUnitPrice:"NA";
						parent.compAvg =$scope.networkingCalculateDto.firewallsCalculateDto.compDealAvgUnitPrice!=null?$scope.networkingCalculateDto.firewallsCalculateDto.compDealAvgUnitPrice:"NA";
					}
				  else
					  {
					  parent.benchLow='NA';
					  parent.benchTarget='NA';
					  parent.pastAvg='NA';
					  parent.compAvg='NA';

					  }
				  }

			// reserveProDevicesAvg
			   function extractNetworkingreserveProDevicesAvg(parent){
				   if($scope.networkingCalculateDto.proxiesCalculateDto!=null){
						parent.benchLow = $scope.networkingCalculateDto.proxiesCalculateDto.benchDealLowAvgUnitPrice!=null?$scope.networkingCalculateDto.proxiesCalculateDto.benchDealLowAvgUnitPrice:"NA";
						parent.benchTarget =$scope.networkingCalculateDto.proxiesCalculateDto.benchDealTargetAvgUnitPrice!=null?$scope.networkingCalculateDto.proxiesCalculateDto.benchDealTargetAvgUnitPrice:"NA";
						parent.pastAvg =$scope.networkingCalculateDto.proxiesCalculateDto.pastDealAvgUnitPrice!=null?$scope.networkingCalculateDto.proxiesCalculateDto.pastDealAvgUnitPrice:"NA";
						parent.compAvg =$scope.networkingCalculateDto.proxiesCalculateDto.compDealAvgUnitPrice!=null?$scope.networkingCalculateDto.proxiesCalculateDto.compDealAvgUnitPrice:"NA";
					}
				  else
					  {
					  parent.benchLow='NA';
					  parent.benchTarget='NA';
					  parent.pastAvg='NA';
					  parent.compAvg='NA';

					  }
				  }

		  //  extract Wan SetailNetworkingChildLevelWiseAverages
		  function extractWanSetailNetworkingChildLevelWiseAverages(parent){
				var child = {};
				if(parent != null){
					for(var k = 0;k < parent.length;k++){
						child = parent[k];
						switch(child.id) {

						case "1": // Storage Volume
							break;
						case "1.1.1": // Small*

							if($scope.networkingCalculateDto.smallWanDevicesCalculateDto!=null){
							child.benchLow = $scope.networkingCalculateDto.smallWanDevicesCalculateDto.benchDealLowAvgUnitPrice!=null? $scope.networkingCalculateDto.smallWanDevicesCalculateDto.benchDealLowAvgUnitPrice: "NA" ;                        ;// $scope.hostingCalculateDto.benchDealPhysicalLowUnitPrice!=0
							child.benchTarget = $scope.networkingCalculateDto.smallWanDevicesCalculateDto.benchDealTargetAvgUnitPrice!=null? $scope.networkingCalculateDto.smallWanDevicesCalculateDto.benchDealTargetAvgUnitPrice: "NA" ;
							child.pastAvg = $scope.networkingCalculateDto.smallWanDevicesCalculateDto.pastDealAvgUnitPrice!=null ? $scope.networkingCalculateDto.smallWanDevicesCalculateDto.pastDealAvgUnitPrice:"NA";
							child.compAvg = $scope.networkingCalculateDto.smallWanDevicesCalculateDto.compDealAvgUnitPrice!=null? $scope.networkingCalculateDto.smallWanDevicesCalculateDto.compDealAvgUnitPrice:"NA" ;
							}else
							  {
								child.benchLow='NA';
								child.benchTarget='NA';
								child.pastAvg='NA';
								child.compAvg='NA';
							  }
							break;
						case "1.1.2": //Medium*
							if($scope.networkingCalculateDto.mediumWanDevicesCalculateDto!=null){
								child.benchLow = $scope.networkingCalculateDto.mediumWanDevicesCalculateDto.benchDealLowAvgUnitPrice!=null? $scope.networkingCalculateDto.mediumWanDevicesCalculateDto.benchDealLowAvgUnitPrice: "NA" ;                        ;// $scope.hostingCalculateDto.benchDealPhysicalLowUnitPrice!=0
								child.benchTarget = $scope.networkingCalculateDto.mediumWanDevicesCalculateDto.benchDealTargetAvgUnitPrice!=null? $scope.networkingCalculateDto.mediumWanDevicesCalculateDto.benchDealTargetAvgUnitPrice: "NA" ;
								child.pastAvg = $scope.networkingCalculateDto.mediumWanDevicesCalculateDto.pastDealAvgUnitPrice!=null ? $scope.networkingCalculateDto.mediumWanDevicesCalculateDto.pastDealAvgUnitPrice:"NA";
								child.compAvg = $scope.networkingCalculateDto.mediumWanDevicesCalculateDto.compDealAvgUnitPrice!=null? $scope.networkingCalculateDto.mediumWanDevicesCalculateDto.compDealAvgUnitPrice:"NA" ;
								}else
								  {
									child.benchLow='NA';
									child.benchTarget='NA';
									child.pastAvg='NA';
									child.compAvg='NA';
								  }
							break;
						case "1.1.3": // large
							if($scope.networkingCalculateDto.largeWanDevicesCalculateDto!=null){
								child.benchLow = $scope.networkingCalculateDto.largeWanDevicesCalculateDto.benchDealLowAvgUnitPrice!=null? $scope.networkingCalculateDto.largeWanDevicesCalculateDto.benchDealLowAvgUnitPrice: "NA" ;                        ;// $scope.hostingCalculateDto.benchDealPhysicalLowUnitPrice!=0
								child.benchTarget = $scope.networkingCalculateDto.largeWanDevicesCalculateDto.benchDealTargetAvgUnitPrice!=null? $scope.networkingCalculateDto.largeWanDevicesCalculateDto.benchDealTargetAvgUnitPrice: "NA" ;
								child.pastAvg = $scope.networkingCalculateDto.largeWanDevicesCalculateDto.pastDealAvgUnitPrice!=null ? $scope.networkingCalculateDto.largeWanDevicesCalculateDto.pastDealAvgUnitPrice:"NA";
								child.compAvg = $scope.networkingCalculateDto.largeWanDevicesCalculateDto.compDealAvgUnitPrice!=null? $scope.networkingCalculateDto.largeWanDevicesCalculateDto.compDealAvgUnitPrice:"NA" ;
								}else
								  {
									child.benchLow='NA';
									child.benchTarget='NA';
									child.pastAvg='NA';
									child.compAvg='NA';
								  }
							break;

						}

					}
				}
			}

		  // extract Lan SetailNetworkingChildLevelWiseAverages
		  function extractLanSetailNetworkingChildLevelWiseAverages(parent){
			  var child = {};
				if(parent != null){
					for(var k = 0;k < parent.length;k++){
						child = parent[k];
						console.log(child);
						switch(child.id) {

						case "1": // Storage Volume
							break;
						case "2.1.1": // Small*

							if($scope.networkingCalculateDto.smallLanDevicesCalculateDto!=null){
								child.benchLow = $scope.networkingCalculateDto.smallLanDevicesCalculateDto.benchDealLowAvgUnitPrice!=null? $scope.networkingCalculateDto.smallLanDevicesCalculateDto.benchDealLowAvgUnitPrice: "NA" ;                        ;// $scope.hostingCalculateDto.benchDealPhysicalLowUnitPrice!=0
								child.benchTarget = $scope.networkingCalculateDto.smallLanDevicesCalculateDto.benchDealTargetAvgUnitPrice!=null? $scope.networkingCalculateDto.smallLanDevicesCalculateDto.benchDealTargetAvgUnitPrice: "NA" ;
								child.pastAvg = $scope.networkingCalculateDto.smallLanDevicesCalculateDto.pastDealAvgUnitPrice!=null ? $scope.networkingCalculateDto.smallLanDevicesCalculateDto.pastDealAvgUnitPrice:"NA";
								child.compAvg = $scope.networkingCalculateDto.smallLanDevicesCalculateDto.compDealAvgUnitPrice!=null? $scope.networkingCalculateDto.smallLanDevicesCalculateDto.compDealAvgUnitPrice:"NA" ;
								}else
								  {
									child.benchLow='NA';
									child.benchTarget='NA';
									child.pastAvg='NA';
									child.compAvg='NA';
								  }
							break;
						case "2.1.2": //Medium*
							if($scope.networkingCalculateDto.mediumLanDevicesCalculateDto!=null){
								child.benchLow = $scope.networkingCalculateDto.mediumLanDevicesCalculateDto.benchDealLowAvgUnitPrice!=null? $scope.networkingCalculateDto.mediumLanDevicesCalculateDto.benchDealLowAvgUnitPrice: "NA" ;                        ;// $scope.hostingCalculateDto.benchDealPhysicalLowUnitPrice!=0
								child.benchTarget = $scope.networkingCalculateDto.mediumLanDevicesCalculateDto.benchDealTargetAvgUnitPrice!=null? $scope.networkingCalculateDto.mediumLanDevicesCalculateDto.benchDealTargetAvgUnitPrice: "NA" ;
								child.pastAvg = $scope.networkingCalculateDto.mediumLanDevicesCalculateDto.pastDealAvgUnitPrice!=null ? $scope.networkingCalculateDto.mediumLanDevicesCalculateDto.pastDealAvgUnitPrice:"NA";
								child.compAvg = $scope.networkingCalculateDto.mediumLanDevicesCalculateDto.compDealAvgUnitPrice!=null? $scope.networkingCalculateDto.mediumLanDevicesCalculateDto.compDealAvgUnitPrice:"NA" ;
								}else
								  {
									child.benchLow='NA';
									child.benchTarget='NA';
									child.pastAvg='NA';
									child.compAvg='NA';
								  }
							break;
						case "2.1.3": // large
							if($scope.networkingCalculateDto.largeLanDevicesCalculateDto!=null){
								child.benchLow = $scope.networkingCalculateDto.largeLanDevicesCalculateDto.benchDealLowAvgUnitPrice!=null? $scope.networkingCalculateDto.largeLanDevicesCalculateDto.benchDealLowAvgUnitPrice: "NA" ;                        ;// $scope.hostingCalculateDto.benchDealPhysicalLowUnitPrice!=0
								child.benchTarget = $scope.networkingCalculateDto.largeLanDevicesCalculateDto.benchDealTargetAvgUnitPrice!=null? $scope.networkingCalculateDto.largeLanDevicesCalculateDto.benchDealTargetAvgUnitPrice: "NA" ;
								child.pastAvg = $scope.networkingCalculateDto.largeLanDevicesCalculateDto.pastDealAvgUnitPrice!=null ? $scope.networkingCalculateDto.largeLanDevicesCalculateDto.pastDealAvgUnitPrice:"NA";
								child.compAvg = $scope.networkingCalculateDto.largeLanDevicesCalculateDto.compDealAvgUnitPrice!=null? $scope.networkingCalculateDto.largeLanDevicesCalculateDto.compDealAvgUnitPrice:"NA" ;
								}else
								  {
									child.benchLow='NA';
									child.benchTarget='NA';
									child.pastAvg='NA';
									child.compAvg='NA';
								  }
							break;

						}

					}
				}
			}

		// on selecting the radio button of calculate
		  $scope.createUnitPrice=function(child,key)
		  {
				switch (key) {
			    case 'pastAvg':// pastAvg
			    	switch (child.id) {
				    case '1.1'://Wan
				    	$scope.past=$scope.networkingCalculateDto.wanDevicesCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				        break;
				    case '1.1.1'://small
				    	$scope.past=$scope.networkingCalculateDto.smallWanDevicesCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				        break;
				    case '1.1.2'://medium
				    	$scope.past=$scope.networkingCalculateDto.mediumWanDevicesCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				    	break;
				    case '1.1.3'://large
				    	$scope.past=$scope.networkingCalculateDto.largeWanDevicesCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				    	break;
				    case '2.1'://lan
				    	$scope.past=$scope.networkingCalculateDto.lanDevicesCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				    	break;
				    case '2.1.1'://small
				    	$scope.past=$scope.networkingCalculateDto.smallLanDevicesCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				        break;
				    case '2.1.2'://medium
				    	$scope.past=$scope.networkingCalculateDto.mediumLanDevicesCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				    	break;
				    case '2.1.3'://large
				    	$scope.past=$scope.networkingCalculateDto.largeLanDevicesCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				    	break;
				    case '3.1'://wlanc
				    	$scope.past=$scope.networkingCalculateDto.wlanControllersCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				    	break;
				    case '4.1'://WLAN Access Points
				    	$scope.past=$scope.networkingCalculateDto.wlanAccesspointCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				    	break;
				    case '5.1':// Load Balancers
				    	$scope.past=$scope.networkingCalculateDto.loadBalancersCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				    	break;
				    case '6.1'://VPN/IPSec
				    	$scope.past=$scope.networkingCalculateDto.vpnIpSecCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				    	 break;
				    case '7.1'://DNS/DHCP Service
				    	$scope.past=$scope.networkingCalculateDto.dnsDhcpServiceCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				    	 break;
				    case '8.1'://Firewalls
				    	$scope.past=$scope.networkingCalculateDto.firewallsCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				    	 break;
				    case '9.1'://Reverse Proxies
				    	$scope.past=$scope.networkingCalculateDto.proxiesCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				    	 break;
				    	}
			        break;
			    case 'benchLow':// benchLow
			    	switch (child.id) {
			    	case '1.1'://Wan
				    	$scope.benchLow=$scope.networkingCalculateDto.wanDevicesCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
				        break;
				    case '1.1.1'://small
				    	$scope.benchLow=$scope.networkingCalculateDto.smallWanDevicesCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
				        break;
				    case '1.1.2'://medium
				    	$scope.benchLow=$scope.networkingCalculateDto.mediumWanDevicesCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
				    	break;
				    case '1.1.3'://large
				    	$scope.benchLow=$scope.networkingCalculateDto.largeWanDevicesCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
				    	break;
				    case '2.1'://lan
				    	$scope.benchLow=$scope.networkingCalculateDto.lanDevicesCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
				    	break;
				    case '2.1.1'://small
				    	$scope.benchLow=$scope.networkingCalculateDto.smallLanDevicesCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
				        break;
				    case '2.1.2'://medium
				    	$scope.benchLow=$scope.networkingCalculateDto.mediumLanDevicesCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
				    	break;
				    case '2.1.3'://large
				    	$scope.benchLow=$scope.networkingCalculateDto.largeLanDevicesCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
				    	break;
				    case '3.1'://wlanc
				    	$scope.benchLow=$scope.networkingCalculateDto.wlanControllersCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
				    	break;
				    case '4.1'://WLAN Access Points
				    	$scope.benchLow=$scope.networkingCalculateDto.wlanAccesspointCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
				    	break;
				    case '5.1':// Load Balancers
				    	$scope.benchLow=$scope.networkingCalculateDto.loadBalancersCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
				    	break;
				    case '6.1'://VPN/IPSec
				    	$scope.benchLow=$scope.networkingCalculateDto.vpnIpSecCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
				    	 break;
				    case '7.1'://DNS/DHCP Service
				    	$scope.benchLow=$scope.networkingCalculateDto.dnsDhcpServiceCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
				    	 break;
				    case '8.1'://Firewalls
				    	$scope.benchLow=$scope.networkingCalculateDto.firewallsCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
				    	 break;
				    case '9.1'://Reverse Proxies
				    	$scope.benchLow=$scope.networkingCalculateDto.proxiesCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
			             break;
			        }
			    	 break;
			    case 'benchTarget':// benchTarget
			    	switch (child.id) {
			    	case '1.1'://Wan
				    	$scope.benchTarget=$scope.networkingCalculateDto.wanDevicesCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				        break;
				    case '1.1.1'://small
				    	$scope.benchTarget=$scope.networkingCalculateDto.smallWanDevicesCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				        break;
				    case '1.1.2'://medium
				    	$scope.benchTarget=$scope.networkingCalculateDto.mediumWanDevicesCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				    	break;
				    case '1.1.3'://large
				    	$scope.benchTarget=$scope.networkingCalculateDto.largeWanDevicesCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				    	break;
				    case '2.1'://lan
				    	$scope.benchTarget=$scope.networkingCalculateDto.lanDevicesCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				    	break;
				    case '2.1.1'://small
				    	$scope.benchTarget=$scope.networkingCalculateDto.smallLanDevicesCalculateDto.benchmarkTargetYearlyCalcDtoList;;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				        break;
				    case '2.1.2'://medium
				    	$scope.benchTarget=$scope.networkingCalculateDto.mediumLanDevicesCalculateDto.benchmarkTargetYearlyCalcDtoList
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				    	break;
				    case '2.1.3'://large
				    	$scope.benchTarget=$scope.networkingCalculateDto.largeLanDevicesCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				    	break;
				    case '3.1'://wanc
				    	$scope.benchTarget=$scope.networkingCalculateDto.wlanControllersCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				    	break;
				    case '4.1'://WLAN Access Points
				    	$scope.benchTarget=$scope.networkingCalculateDto.wlanAccesspointCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				    	break;
				    case '5.1':// Load Balancers
				    	$scope.benchTarget=$scope.networkingCalculateDto.loadBalancersCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				    	break;
				    case '6.1'://VPN/IPSec
				    	$scope.benchTarget=$scope.networkingCalculateDto.vpnIpSecCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				    	 break;
				    case '7.1'://DNS/DHCP Service
				    	$scope.benchTarget=$scope.networkingCalculateDto.dnsDhcpServiceCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				    	 break;
				    case '8.1'://Firewalls
				    	$scope.benchTarget=$scope.networkingCalculateDto.firewallsCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				    	 break;
				    case '9.1'://Reverse Proxies
				    	$scope.benchTarget=$scope.networkingCalculateDto.proxiesCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
			        break;
			        }
			    	break;
			    case 'compAvg':// compAvg
			    	switch (child.id) {
				    case '1.1':
				    	console.log('inside storage comp')
				        break;
				    case '1.1.1':
				    	console.log('inside per comp')
				        break;
				    case '1.1.2':
				    	console.log('inside nonper comp')
				    	break;
				    case '2.1':
				    	console.log('inside backup comp')
				    	break;
				    	}
			    	break;}
		  }


		  // function used for set the unit price year wise
		  $scope.setUnitPriceForLevels=function(child,untiPrices)
		  {
			  switch (child.id) {
			    case '1.1'://Wan
			    	for(var i=0;i<untiPrices.length;i++)
			    		{
			    		$scope.unitPriceDto[i].totalWanUnitPrice=untiPrices[i].unitPrice;
			    		$scope.unitPriceDto[i].totalWanRevenue=untiPrices[i].revenue;
			    		}
			        break;
			    case '1.1.1'://small
			    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].totalSmallWanUnitPrice=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].totalSmallWanRevenue=untiPrices[i].revenue;
		    		$scope.unitPriceDto[i].totalWanRevenue=$scope.unitPriceDto[i].totalSmallWanRevenue+$scope.unitPriceDto[i].totalMediumWanRevenue+$scope.unitPriceDto[i].totalLargeWanRevenue;
		    		$scope.unitPriceDto[i].totalWanUnitPrice=parseFloat($scope.unitPriceDto[i].totalWanRevenue/$scope.networkInfo.wanDevice.children[0].distributedVolume[i].volume).toFixed(2);
		    		}
			        break;
			    case '1.1.2'://medium
			    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].totalMediumWanUnitPrice=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].totalMediumWanRevenue=untiPrices[i].revenue;
		    		$scope.unitPriceDto[i].totalWanRevenue=$scope.unitPriceDto[i].totalSmallWanRevenue+$scope.unitPriceDto[i].totalMediumWanRevenue+$scope.unitPriceDto[i].totalLargeWanRevenue;
		    		$scope.unitPriceDto[i].totalWanUnitPrice=parseFloat($scope.unitPriceDto[i].totalWanRevenue/$scope.networkInfo.wanDevice.children[0].distributedVolume[i].volume).toFixed(2);
		    		}
			    	break;
			    case '1.1.3'://large
			    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].totalLargeWanUnitPrice=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].totalLargeWanRevenue=untiPrices[i].revenue;
		    		$scope.unitPriceDto[i].totalWanRevenue=$scope.unitPriceDto[i].totalSmallWanRevenue+$scope.unitPriceDto[i].totalMediumWanRevenue+$scope.unitPriceDto[i].totalLargeWanRevenue;
		    		$scope.unitPriceDto[i].totalWanUnitPrice=parseFloat($scope.unitPriceDto[i].totalWanRevenue/$scope.networkInfo.wanDevice.children[0].distributedVolume[i].volume).toFixed(2);
		    		}
			    	break;
			    case '2.1'://lan
			    	for(var i=0;i<untiPrices.length;i++)
			    		{
			    		$scope.unitPriceDto[i].totalLanUnitPrice=untiPrices[i].unitPrice;
			    		$scope.unitPriceDto[i].totalLanRevenue=untiPrices[i].revenue;
			    		}
			        break;

			    case '2.1.1'://samll
			    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].totalSmallLanUnitPrice=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].totalSmallLanRevenue=untiPrices[i].revenue;
		    		$scope.unitPriceDto[i].totalLanRevenue=$scope.unitPriceDto[i].totalSmallLanRevenue+$scope.unitPriceDto[i].totalMediumLanRevenue+$scope.unitPriceDto[i].totalLargeLanRevenue;
		    		$scope.unitPriceDto[i].totalLanUnitPrice=parseFloat($scope.unitPriceDto[i].totalLanRevenue/$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume).toFixed(2);
		    		}
			    	break;
			    case '2.1.2'://medium
			    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].totalMediumLanUnitPrice=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].totalMediumLanRevenue=untiPrices[i].revenue;
		    		$scope.unitPriceDto[i].totalLanRevenue=$scope.unitPriceDto[i].totalSmallLanRevenue+$scope.unitPriceDto[i].totalMediumLanRevenue+$scope.unitPriceDto[i].totalLargeLanRevenue;
		    		$scope.unitPriceDto[i].totalLanUnitPrice=parseFloat($scope.unitPriceDto[i].totalLanRevenue/$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume).toFixed(2);
		    		}
			    	break;
			    case '2.1.3'://large
			    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].totalLargeLanUnitPrice=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].totalLargeLanRevenue=untiPrices[i].revenue;
		    		$scope.unitPriceDto[i].totalLanRevenue=$scope.unitPriceDto[i].totalSmallLanRevenue+$scope.unitPriceDto[i].totalMediumLanRevenue+$scope.unitPriceDto[i].totalLargeLanRevenue;
		    		$scope.unitPriceDto[i].totalLanUnitPrice=parseFloat($scope.unitPriceDto[i].totalLanRevenue/$scope.networkInfo.lanDevice.children[0].distributedVolume[i].volume).toFixed(2);
		    		}
			        break;
			    case '3.1'://WLAN Controllers
			    	for(var i=0;i<untiPrices.length;i++)
			    		{
			    		$scope.unitPriceDto[i].totalWlanControllersUnitPrice=untiPrices[i].unitPrice;
			    		$scope.unitPriceDto[i].totalWlanControllersRevenue=untiPrices[i].revenue;
			    		}
			        break;
			    case '4.1'://WLAN Access Points
			    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].totalWlanAccesspointUnitPrice=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].totalWlanAccesspointRevenue=untiPrices[i].revenue;

		    		}
			        break;
			    case '5.1'://Load Balancers
			    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].totalLoadBalancersUnitPrice=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].totalLoadBalancersRevenue=untiPrices[i].revenue;

		    		}
			    	break;
			    case '6.1'://VPN/IPSec
			    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].totalVpnIpSecUnitPrice=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].totalVpnIpSecRevenue=untiPrices[i].revenue;

		    		}
			    	break;
			    case '7.1'://DHCP Service
			    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].totalDnsDhcpServiceUnitPrice=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].totalDnsDhcpServiceRevenue=untiPrices[i].revenue;

		    		}
			    	break;
			    case '8.1'://Firewalls
			    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].totalFirewallsUnitPrice=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].totalFirewallsRevenue=untiPrices[i].revenue;
		    		}
			    	break;
			    case '9.1'://Reverse Proxies
			    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].totalProxiesUnitPrice=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].totalProxiesRevenue=untiPrices[i].revenue;
		    		}
			    	break;
			    	}

			  console.log($scope.unitPriceDto);
		  }
		// Modal for find deal section
			var dialogOptions = {
			    controller: 'DealInformationCtrl',
			    templateUrl: '/modals/dealInformation/dealInformation.html'
			  };
			  $scope.getNearestDeal = function(child,key){
				  var infoModel={id:child.id,dealType:key,url:endpointsurls.NETWORK_NEAREST_DEAL};
			    $dialog.dialog(angular.extend(dialogOptions, {resolve: {infoModel: angular.copy(infoModel)}}))
			      .open()
			      .then(function(result) {
			        if(result) {
			          //angular.copy(result, infoModel);
			        }
			        infoModel = undefined;
			    });
			  };



			// go to calculate tab
			$scope.goToCal=function()
			{
				$scope.resetCalculateDetails();
				$scope.tabReset(1);
				$scope.populatedNetworkingCalculateTabDetails();
			}
			// reset radio buttons
			$scope.resetCalculateDetails=function(){

				$scope.networkInfo.wanDevice.children[0].childVolumeNetwork='';
				$scope.networkInfo.wanDevice.children[0].children[0].perNonPer='';
				$scope.networkInfo.wanDevice.children[0].children[1].perNonPer='';
				$scope.networkInfo.wanDevice.children[0].children[2].perNonPer='';
				$scope.networkInfo.lanDevice.children[0].lan='';
				$scope.networkInfo.lanDevice.children[0].children[0].perNonPer='';
				$scope.networkInfo.lanDevice.children[0].children[1].perNonPer='';
				$scope.networkInfo.lanDevice.children[0].children[1].perNonPer='';
				$scope.networkInfo.wlanCon.children[0].wlanCon='';
				$scope.networkInfo.wlanAccess.children[0].acessPoint='';
				$scope.networkInfo.loadBalance.children[0].loadBalancer='';
				$scope.networkInfo.vpnIp.children[0].Vpnlan='';
				$scope.networkInfo.dnsService.children[0].dns='';
				$scope.networkInfo.firewalls.children[0].fireWal='';
				$scope.networkInfo.reservePro.children[0].proxy='';

			}
			// back to input
			$scope.backToInput=function()
			{
				$scope.tabReset(0);
			}

			// go to result
			$scope.goToResult=function()
			{
				for(var i=0;i<$scope.unitPriceDto.length;i++)
				  {
				  $scope.unitPriceDto[i].totalProxiesRevenue=$scope.unitPriceDto[i].totalProxiesRevenue*12;
				  $scope.unitPriceDto[i].totalFirewallsRevenue=$scope.unitPriceDto[i].totalFirewallsRevenue*12;
				  $scope.unitPriceDto[i].totalDnsDhcpServiceRevenue=$scope.unitPriceDto[i].totalDnsDhcpServiceRevenue*12;
				  $scope.unitPriceDto[i].totalVpnIpSecRevenue=$scope.unitPriceDto[i].totalVpnIpSecRevenue*12;
				  $scope.unitPriceDto[i].totalLoadBalancersRevenue=$scope.unitPriceDto[i].totalLoadBalancersRevenue*12;
				  $scope.unitPriceDto[i].totalWlanAccesspointRevenue=$scope.unitPriceDto[i].totalWlanAccesspointRevenue*12;
				  $scope.unitPriceDto[i].totalLanRevenue=$scope.unitPriceDto[i].totalLanRevenue*12;
				  $scope.unitPriceDto[i].totalWanRevenue=$scope.unitPriceDto[i].totalWanRevenue*12;
				  $scope.unitPriceDto[i].totalWlanControllersRevenue=$scope.unitPriceDto[i].totalWlanControllersRevenue*12;
				  }
				
				var url= endpointsurls.NETOWK_REVENUE+$scope.id;
				customInterceptor.putrequest(url,$scope.unitPriceDto).then(function successCallback(response) {
					 var urlresult = endpointsurls.SAVE_NETWORK_INFO+ $stateParams.dealId;
				        customInterceptor.getrequest(urlresult).then(function successCallback(response) {
				        	$scope.existingNetworkInfo = response.data;
				        	$scope.tabReset(2);
				        	$scope.viewBy.type = 'unit';
				       		if($scope.existingNetworkInfo != null && $scope.id !=null){
				       			getExistingNetworkResultInfo();
				       		}
				        }, function errorCallback(response) {
				        	$scope.id=undefined;
				        });
		        	$scope.viewBy = {type:'unit'};
				}, function errorCallback(response) {
					console.log(response.statusText);
				});
			}

	//------------------------- Result Tab ----------------------------------------------

		function getExistingNetworkResultInfo(){

			if($scope.existingNetworkInfo.networkYearlyDataInfoDtoList != null){
	    		for (var y = 0; y < $scope.existingNetworkInfo.networkYearlyDataInfoDtoList.length; y++){
	        		var yearlyDto = $scope.existingNetworkInfo.networkYearlyDataInfoDtoList[y];
	        		getExistingServerPricingLevelWiseInfo(yearlyDto,y);

	        	}
	    	}
		}

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

		$scope.backToCal=function()
		{
			$scope.tabReset(1);
		}

		$scope.gotoTotals=function()
		{
			$scope.isSaveNetwork=DealService.getter();
			$scope.isSaveNetwork.networkA=true;
	        DealService.setter($scope.isSaveNetwork);
	        $scope.assessmentIndicator[4]=1;
            var urlIndicator= endpointsurls.GENERic_INDICATOR+$stateParams.dealId+'?assessmentIndicator='+$scope.assessmentIndicator.join(',')+'&submissionIndicator='+$scope.submissionIndicator;
           customInterceptor.putrequestWithoutData(urlIndicator).then(function successCallback(response) {
           });
			$state.go('home.assessment.generic.totals',({dealId:$stateParams.dealId}));
		}
});
