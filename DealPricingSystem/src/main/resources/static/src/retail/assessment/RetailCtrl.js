priceToolcontrollers.controller('RetailCtrl',function($scope,$http,HostingService,EndUserService,StorageService,DealService,ServiceDeskService,AppsService,RetailService,customInterceptor,endpointsurls,$stateParams,$dialog,$confirm,$state) {
	$scope.tabs=[{class:'active',href:"#inputtab",tabname:"Input"},{class:'inActive',href:"#caltab",tabname:"Calculate"},{class:'inActive',href:"#resulttab",tabname:"Result"}];
	
	//initialize all the variable needed for the page.
	
	
	var ctrl = this;
	ctrl.active = 0;
	
	$scope.dealId = $stateParams.Id
	$scope.dealInfo={};
	$scope.hostingInfo = {};
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
	
	//gotoTotals
	$scope.gotoTotals=function()
	{
		$scope.isSaveRetail.retailA=true;
        DealService.setter($scope.isSaveRetail);
     	
     	$scope.assessmentIndicator[7]=1;
        var urlIndicator= endpointsurls.GENERic_INDICATOR+$stateParams.dealId+'?assessmentIndicator='+$scope.assessmentIndicator.join(',')+'&submissionIndicator='+$scope.submissionIndicator;
       customInterceptor.putrequestWithoutData(urlIndicator).then(function successCallback(response) {
    	   $state.go('home.assessment.generic.totals',({dealId:$stateParams.dealId}));
       });
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
	$scope.tabEvent=function($event,index){
		if($event.keyCode == 13 || $event.keyCode == 9){
			for(var i=index;i<$scope.retailInfo.shops.children[0].distributedVolume.length;i++){
				$scope.retailInfo.shops.children[0].distributedVolume[i].volume = $scope.retailInfo.shops.children[0].distributedVolume[index].volume;
			}
		}
	}
	
	// get the drop-down values
	$scope.getRetailDropdowns=function(){
		var url = endpointsurls.RETAIL_DROPDOWN+$stateParams.dealId;
        customInterceptor.getrequest(url).then(function successCallback(response) {
			$scope.genericDealInfoDto = response.data;
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
        	$scope.isSaveRetail= DealService.getter() || isSave;
    		DealService.setter($scope.isSaveRetail);
			$scope.dynamicTooltipTextUser ='Upon checking this box Q will add ' + $scope.genericDealInfoDto.retailSolutionsDtoList[0].shopPerc + ' lanes per shop ';
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
        $scope.dealYearlyDataInfoDtoList = $scope.dealInfoDto.dealYearlyDataInfoDtos;
        $scope.retailInfo = RetailService.initRetailDetails($scope.dealInfo);
        $scope.initCopy=angular.copy(RetailService.initRetailDetails($scope.dealInfo));
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
	        	$scope.id=undefined;
	        });
		}
	}

	// function of init app info
    $scope.init=function() {
    	
		$scope.getRetailDropdowns();
	}
	$scope.init();
	
	// map existing Retail Info
    function mapExistingRetailInfo() {
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
        	}
    		$scope.copyBackup=angular.copy($scope.retailInfo.shops.children[0].distributedVolume);
    	}
    }
    
    $scope.shopsChecked=function(val){
        if(val){
     	   for(var i=0;i<$scope.retailInfo.shops.children[0].distributedVolume.length;i++){
     		   $scope.copyBackupVolume=angular.copy($scope.copyBackup);
     		   $scope.retailInfo.shops.children[0].distributedVolume[i].volume=Math.round($scope.dealYearlyDataInfoDtoList[i].noOfSites * $scope.genericDealInfoDto.retailSolutionsDtoList[0].shopPerc);
               }
     	   }
        else{
             if($scope.id){
             		$scope.retailInfo.shops.children[0].distributedVolume=$scope.copyBackupVolume;
                 }
             	else{
                    for(var i=0;i<$scope.initCopy.shops.children[0].distributedVolume.length;i++)
                           {
             	   			 $scope.retailInfo.shops.children[0].distributedVolume[i].volume=undefined;
                           }
             		}
        	 }
	 }
 
	 // Save Retail input form data
	 $scope.saveRetailInputInfo = function(){
	 	setRetailYearlyInfo();
	 	$scope.retailInfoDto = {
	 			offshoreAllowed : $scope.dealDetails.offshoreSelected == "Yes" ? true : false,
				includeHardware :  $scope.dealDetails.hardwareIncludedSelected == "Yes" ? true : false,
				equipmentAge : $scope.retailInfo.ageList,
				equipmentSet : $scope.retailInfo.setList,
	 	    	retailYearlyDataInfoDtoList : $scope.retailInfo.retailYearlyDataInfoDtos,
	 	    	levelIndicator:($scope.retailInfo.shops.open)?1:0,
	 	    	dealId : $stateParams.dealId,
	 	}
	 	
	 	var url = endpointsurls.SAVE_RETAIL_INFO+$stateParams.dealId;
	     customInterceptor.postrequest(url,$scope.retailInfoDto).then(function successCallback(response) {
	     	$scope.goToCal();
	     	$scope.id=response.data.retailId;
			}, function errorCallback(response) {
				console.log(response.statusText);
			});
	 }
	 
	 // Extract data yearly wise
	 function setRetailYearlyInfo() {
	 	var yearlyInfoList = [];
	 	for (var y = 0; y < $scope.dealInfo/12; y++) {
	 		var yearlyData = {};
	 		yearlyData.year = y+1;
	 		yearlyData.noOfShops = $scope.retailInfo.shops.children[0].distributedVolume[y].volume;
	 		yearlyInfoList.push(yearlyData);
	 	}
	 	$scope.retailInfo.retailYearlyDataInfoDtos = yearlyInfoList;
	 }
	   
    //*********************************Calculate tab*****************************
    
	   $scope.populatedCalculateTabDetails = function() {
		   $scope.retailInfo.shops.children[0].selectedRadio='';
		   var url = endpointsurls.CALCULATE_RETAIL_INFO+$stateParams.dealId;
	        customInterceptor.getrequest(url).then(function successCallback(response) {
	        	$scope.calculateReatailInfo=response.data;
	        	 $scope.extractReatilAvg($scope.retailInfo.shops.children[0]);
	        	 $scope.unitPriceDto=[];
	 			$scope.createUnitPriceForLevels($scope.retailInfo.shops.children[0].distributedVolume.length);
			}, function errorCallback(response) {
				console.log(response.statusText);
				if(response.status=='401'){
					$state.go('login');
				}
			});}

	   // Re-Calculate functionality
	   $scope.reCalculate=function()
	   {
		 $scope.retailInfo.shops.children[0].selectedRadio='';
		 var putDeals=  {"offshoreAllowed" : $scope.dealDetails.offshoreSelected == "Yes" ? true : false,
					"includeHardware" :  $scope.dealDetails.hardwareIncludedSelected == "Yes" ? true : false,
					"equipmentAge" : $scope.retailInfo.ageList,
					"equipmentSet" : $scope.retailInfo.setList}
		 
		     var url = endpointsurls.RETAIL_RECALCULATE+$scope.id;
	        customInterceptor.putrequest(url,putDeals).then(function successCallback(response) {
	        	$scope.tabReset(1);
	        	$scope.calculateReatailInfo=response.data;
	        	 $scope.extractReatilAvg($scope.retailInfo.shops.children[0]);
	        	});
	        $scope.createUnitPriceForLevels($scope.retailInfo.shops.children[0].distributedVolume.length);
	   }

   		// function used for creating the unit price year wise
		  $scope.createUnitPriceForLevels=function(val)
		  {
			  $scope.unitPriceDto=[];
			  for(var i=1;i<=val;i++)
				  {
				  var unitPrice={};
				  unitPrice.year=i;
				  unitPrice.noOfShopsUnitPrice=0;
				  unitPrice.noOfShopsRevenue=0;
				 
				  $scope.unitPriceDto.push(unitPrice);
				  }

		  }

		  // function for extractAppAvg
		   $scope.extractReatilAvg=function(parent) {
	       if($scope.calculateReatailInfo.noOfShopsCalculateDto!=null)
			 {
	    	parent.benchLow = $scope.calculateReatailInfo.noOfShopsCalculateDto.benchDealLowAvgUnitPrice !=null ? $scope.calculateReatailInfo.noOfShopsCalculateDto.benchDealLowAvgUnitPrice :"NA";
	        parent.benchTarget = $scope.calculateReatailInfo.noOfShopsCalculateDto.benchDealTargetAvgUnitPrice !=null ? $scope.calculateReatailInfo.noOfShopsCalculateDto.benchDealTargetAvgUnitPrice:"NA";
	        parent.pastAvg = $scope.calculateReatailInfo.noOfShopsCalculateDto.pastDealAvgUnitPrice !=null  ? $scope.calculateReatailInfo.noOfShopsCalculateDto.pastDealAvgUnitPrice : "NA";
	        parent.compAvg = $scope.calculateReatailInfo.noOfShopsCalculateDto.compDealAvgUnitPrice !=null? $scope.calculateReatailInfo.noOfShopsCalculateDto.compDealAvgUnitPrice :"NA";
	        	}else
	        		{
	        		parent.benchLow='NA';
	        		parent.benchTarget='NA';
	        		parent.pastAvg='NA';
	        		 parent.compAvg='NA';
	        		}
	     }



		   // on selecting the radio button of calculate
		  $scope.createUnitPrice=function(child,key)
		  {
				switch (key) {
			    case 'pastAvg':// pastAvg
			    	switch (child.id) {
				    case '1.1'://total
				    	$scope.past=$scope.calculateReatailInfo.noOfShopsCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				        break;
				
				    	}
			        break;
			    case 'benchLow':// benchLow
			    	switch (child.id) {
				    case '1.1'://total
				    	$scope.benchLow=$scope.calculateReatailInfo.noOfShopsCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
				        break;
				    	}
			        break;
			    case 'benchTarget':// benchTarget
			    	switch (child.id) {
				    case '1.1'://total
				    	$scope.benchTarget=$scope.calculateReatailInfo.noOfShopsCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				        break;
				    	}
			    	break;
			    case 'compAvg':// compAvg
			    	switch (child.id) {
				    case '1.1':
				    	console.log('inside storage comp')
				        break;
				    	}
			    	break;}
		  }


		  // function used for set the unit price year wise
		  $scope.setUnitPriceForLevels=function(child,untiPrices)
		  {
			  switch (child.id) {
			    case '1.1'://total
			    	for(var i=0;i<untiPrices.length;i++)
			    		{
			    		$scope.unitPriceDto[i].noOfShopsUnitPrice=untiPrices[i].unitPrice;
			    		$scope.unitPriceDto[i].noOfShopsRevenue=untiPrices[i].revenue;
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
				  var infoModel={id:child.id,dealType:key,url:endpointsurls.RETAIL_NEAREST_DEAL};
			    $dialog.dialog(angular.extend(dialogOptions, {resolve: {infoModel: angular.copy(infoModel)}}))
			      .open()
			      .then(function(result) {
			        if(result) {
			          //angular.copy(result, infoModel);
			        }
			        infoModel = undefined;
			    });
			  };


		// function for back to input.
	    $scope.backToInput=function()
		{
			$scope.tabReset(0);
		}

	 // function for move to calculate tab.
		$scope.goToCal=function()
		{
			$scope.retailInfo.shops.children[0].selectedRadio='';
			$scope.retailInfo.shops.children[0].children[0].perNonPer='';
			$scope.retailInfo.shops.children[0].children[1].perNonPer='';
			$scope.retailInfo.shops.children[0].children[2].perNonPer='';
			$scope.retailInfo.shops.children[0].children[3].perNonPer='';
			$scope.populatedCalculateTabDetails();
			$scope.tabReset(1);
		}

		// function for go to result
		$scope.goToResult=function()
		{
			
			for(var i=0;i<$scope.unitPriceDto.length;i++)
    		{
    		$scope.unitPriceDto[i].noOfShopsRevenue=$scope.unitPriceDto[i].noOfShopsRevenue*12;
    		}
			var url= endpointsurls.RETAIL_REVENUE+$scope.id;
			customInterceptor.putrequest(url,$scope.unitPriceDto).then(function successCallback(response) {
				 var urlResult = endpointsurls.SAVE_RETAIL_INFO+ $stateParams.dealId;
			        customInterceptor.getrequest(urlResult).then(function successCallback(response) {
			        	$scope.existingRetailInfo = response.data;
			        	$scope.tabReset(2);
			        	$scope.viewBy.type = 'unit';
			       		if($scope.existingRetailInfo != null && $scope.id!=null){
			       			getExistingRetailResultInfo();
			       		}
			        }, function errorCallback(response) {
			        	//$scope.retailId=undefined;
			        });
	        	
			}, function errorCallback(response) {
				console.log(response.statusText);
			});
			
		}
		
		
		$scope.backToInput=function()
		{
			$scope.tabReset(0);
		}
		$scope.goToCal=function()
		{
			 $scope.populatedCalculateTabDetails();
			$scope.tabReset(1);
		}
	

		  
		//------------------------------Result Tab----------------------------------
		 
		// get existing yearly data when initialize
	    function getExistingRetailResultInfo() {
	    	if($scope.existingRetailInfo.retailYearlyDataInfoDtoList != null){
	    		for (var y = 0; y < $scope.existingRetailInfo.retailYearlyDataInfoDtoList.length; y++){
	        		if($scope.viewBy.type == 'unit'){
					    $scope.retailInfo.shops.children[0].distributedVolume[y].unit = $scope.existingRetailInfo.retailYearlyDataInfoDtoList[y].retailUnitPriceInfoDtoList[0].noOfShops;
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
		
		$scope.backToCal=function()
		{
			$scope.tabReset(1);
		}
		
		
});

