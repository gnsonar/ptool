priceToolcontrollers.controller('RetailSubCtrl',function($scope,$http,HostingService,EndUserService,StorageService,DealService,ServiceDeskService,AppsService,RetailService,customInterceptor,endpointsurls,$stateParams,$dialog,$confirm,$state) {
	
	//initialize all the variable needed for the page.
	var ctrl = this;
	ctrl.active = 0;
	$scope.dealInfo={};
	$scope.hostingInfo = {};
	$scope.dealInfo={};
	$scope.retailInfo={};
	$scope.hostingInfo = {};
	var ctrl = this;
	ctrl.active = 0;
    $scope.dealDetails = {};
    $scope.selectedRetailId = {};
    $scope.solution = {};
    $scope.viewBy = {type:'unit'};
	$scope.retailInfoDto = {};
	$scope.existingRetailInfo = {};
	$scope.retailCalculateDto = {};
	$scope.showErr=false;
	$scope.showVolumeErr=false;
	$scope.custom=false;

	$scope.shops = {
		open : false
	};
	
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
			case "RetailVolume":
				for(var i=index;i<$scope.retailInfo.shops.children[0].distributedVolume.length;i++){
					$scope.retailInfo.shops.children[0].distributedVolume[i].volume = $scope.retailInfo.shops.children[0].distributedVolume[index].volume;
				}
				break;
			case "RetailPricingRevenue":
				for(var i=index;i<$scope.retailInfo.shops.children[0].distributedVolume.length;i++){
					$scope.retailInfo.shops.children[0].distributedVolume[i].revenue = $scope.retailInfo.shops.children[0].distributedVolume[index].revenue;
				}
				break;
			case "RetailPricingUnit":
				for(var i=index;i<$scope.retailInfo.shops.children[0].distributedVolume.length;i++){
					$scope.retailInfo.shops.children[0].distributedVolume[i].unit = $scope.retailInfo.shops.children[0].distributedVolume[index].unit;
				}
				break;
			}
		}
	}
	

	// get the drop-down values
	$scope.getRetailDropdowns=function(){
		var url = endpointsurls.RETAIL_DROPDOWN+$stateParams.dealId;
        customInterceptor.getrequest(url).then(function successCallback(response) {
			$scope.genericDealInfoDto = response.data;
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
            $scope.getDealDetails();
		}, function errorCallback(response) {
			console.log(response.statusText);
			if(response.status=='401'){
				$state.go('login');
			}
		});
	}

	//get default  data of generic screen
	$scope.getDealDetails=function(){
		$scope.dealInfoDto = $scope.genericDealInfoDto.dealInfoDto;
		$scope.dealDetails.offshoreSelected= $scope.dealInfoDto.offshoreAllowed;
        $scope.dealDetails.hardwareIncludedSelected= $scope.dealInfoDto.includeHardware;
        $scope.dealInfo= $scope.dealInfoDto.dealTerm;
        $scope.retailInfo = RetailService.initRetailDetails($scope.dealInfo);
        console.log($scope.retailInfo);
        if($stateParams.dealId>0){
			 var url = endpointsurls.SAVE_RETAIL_INFO+ $stateParams.dealId;
	        customInterceptor.getrequest(url).then(function successCallback(response) {
	        	$scope.existingRetailInfo = response.data;
	       		$scope.id=response.data.retailId;
	       		$scope.retailInfo.shops.open = $scope.existingRetailInfo.levelIndicator == '1'? true:false;
	       		if($scope.existingRetailInfo != null && $scope.id!=null){
	       			mapExistingRetailInfo();
	       		}
	        }, function errorCallback(response) {
	        	$scope.retailId=undefined;
	        });
		}
	}


	// function of init Retail info
    $scope.init=function() {
		$scope.getRetailDropdowns();
	}
	$scope.init();

	
	// map existing Retail Info
    function mapExistingRetailInfo() {
    	$scope.dealDetails.towerArchitect = $scope.existingRetailInfo.towerArchitect
    	$scope.dealDetails.offshoreSelected = $scope.existingRetailInfo.offshoreAllowed == true ? "Yes" : "No";
    	$scope.dealDetails.hardwareIncludedSelected = $scope.existingRetailInfo.includeHardware == true ? "Yes" : "No";
    	$scope.retailInfo.ageList = $scope.existingRetailInfo.equipmentAge;
    	$scope.retailInfo.setList = $scope.existingRetailInfo.equipmentSet;	    
	    getExistingRetailYearlyInfo();
    }
    
    // get existing yearly data when initialize
    function getExistingRetailYearlyInfo() {
    	if($scope.existingRetailInfo.retailYearlyDataInfoDtoList != null){
    		for (var y = 0; y < $scope.existingRetailInfo.retailYearlyDataInfoDtoList.length; y++){
        		var yearlyDto = $scope.existingRetailInfo.retailYearlyDataInfoDtoList[y];
        		$scope.retailInfo.shops.children[0].distributedVolume[y].year = yearlyDto.year;
        		$scope.retailInfo.shops.children[0].distributedVolume[y].volume = yearlyDto.noOfShops;
        		getExistingServerPricingLevelWiseInfo($scope.retailInfo.shops.children[0],yearlyDto,y);

        	}
    	}
    }
    
    // Get existing Retail server price level wise
	function getExistingServerPricingLevelWiseInfo(parent,yearlyDto,year){
		if(yearlyDto.retailUnitPriceInfoDtoList != null){
			if(yearlyDto.retailUnitPriceInfoDtoList.length > 0){
				 if($scope.viewBy.type == 'unit'){
					  $scope.retailInfo.shops.children[0].distributedVolume[year].unit = yearlyDto.retailUnitPriceInfoDtoList[0].noOfShops;
				 }
			}
		}
	}
    
	// On change Unit-revenue price radio button
	$scope.onchangeprice = function(value){
		if(value == 'revenue'){
			if($scope.retailInfo.shops.open == true){
				for(var i=0;i< $scope.retailInfo.shops.children[0].distributedVolume.length;i++){
					$scope.retailInfo.shops.children[0].distributedVolume[i].revenue = Math.round($scope.retailInfo.shops.children[0].distributedVolume[i].volume * $scope.retailInfo.shops.children[0].distributedVolume[i].unit);
					if(isNaN($scope.retailInfo.shops.children[0].distributedVolume[i].revenue)){
						$scope.retailInfo.shops.children[0].distributedVolume[i].revenue = 0;
					}
				}
			}
		}
		if(value == 'unit'){
			if($scope.retailInfo.shops.open == true){
				for(var i=0;i< $scope.retailInfo.shops.children[0].distributedVolume.length;i++){
					$scope.retailInfo.shops.children[0].distributedVolume[i].unit = ($scope.retailInfo.shops.children[0].distributedVolume[i].revenue / $scope.retailInfo.shops.children[0].distributedVolume[i].volume).toFixed(2);
					if(isNaN($scope.retailInfo.shops.children[0].distributedVolume[i].unit)){
						$scope.retailInfo.shops.children[0].distributedVolume[i].unit = 0.00;
					}
				}
			}
		}
	}
	
    // Save RetailSub form data
    $scope.saveRetailSubInfo = function(){
    	setRetailYearlyInfo();
    	$scope.retailInfoDto = {
    			offshoreAllowed : $scope.dealDetails.offshoreSelected == "Yes" ? true : false,
				includeHardware :  $scope.dealDetails.hardwareIncludedSelected == "Yes" ? true : false,
				equipmentAge : $scope.retailInfo.ageList,
				equipmentSet : $scope.retailInfo.setList,
	 	    	retailYearlyDataInfoDtoList : $scope.retailInfo.retailYearlyDataInfoDtos,
	 	    	levelIndicator:($scope.retailInfo.shops.open)?1:0,
	 	    	dealId : $stateParams.dealId,
    	    	towerArchitect : $scope.dealDetails.towerArchitect 
    	}
    	
    	var url = endpointsurls.SAVE_RETAIL_INFO+$stateParams.dealId;
        customInterceptor.postrequest(url,$scope.retailInfoDto).then(function successCallback(response) {
        	$scope.isSaveRetail= DealService.getter() || isSave;
        	$scope.isSaveRetail.retailS=true;
    		DealService.setter($scope.isSaveRetail);
    		$scope.putIndicator();
    		
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
    }
	
  //putIndicator
    $scope.putIndicator=function()
    {
    	$scope.submissionIndicator[7]=1;
        var urlIndicator= endpointsurls.GENERic_INDICATOR+$stateParams.dealId+'?assessmentIndicator='+$scope.assessmentIndicator+'&submissionIndicator='+$scope.submissionIndicator.join(',');
       customInterceptor.putrequestWithoutData(urlIndicator).then(function successCallback(response) {
    	   $state.go('home.submission.genericSubmission.totalsSubmission',({dealId:$stateParams.dealId}));
       });
    }
    // Extract data yearly wise
    function setRetailYearlyInfo() {
    	var yearlyInfoList = [];
    	for (var y = 0; y < $scope.dealInfo/12; y++) {
    		var yearlyData = {};
    		yearlyData.year = y+1;
    		yearlyData.noOfShops = $scope.retailInfo.shops.children[0].distributedVolume[y].volume;
    		yearlyData.retailUnitPriceInfoDtoList= extractServerUnitPrice($scope.retailInfo.shops.children[0],y);
    		yearlyData.retailRevenueInfoDtoList = extractServerRevenuePrice($scope.retailInfo.shops.children[0],y);
    		yearlyInfoList.push(yearlyData);
    	}
    	$scope.retailInfo.retailYearlyDataInfoDtos = yearlyInfoList;
    }
    
    // extract server price in units
    function extractServerUnitPrice(parent,year){
    	var unitPrice = [];
    	var unitInfo={};

    	if($scope.viewBy.type == 'unit'){
    		unitInfo.noOfShops = parent.distributedVolume[year].unit;
    	}
    	if($scope.viewBy.type == 'revenue'){
    		unitInfo.noOfShops = parent.distributedVolume[year].revenue / parent.distributedVolume[year].volume;
    	}
    	unitPrice.push(unitInfo)
    	return unitPrice;
    }
    
    // extract server price in revenue
    function extractServerRevenuePrice(parent,year){
    	var revenue = [];
    	var revenueInfo={};

    	if($scope.viewBy.type == 'unit'){
    		revenueInfo.noOfShops = Math.round(parent.distributedVolume[year].unit * parent.distributedVolume[year].volume) * 12;
    	}
    	if($scope.viewBy.type == 'revenue'){
    		revenueInfo.noOfShops = parent.distributedVolume[year].revenue * 12;
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

