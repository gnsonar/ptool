priceToolcontrollers.controller('AppsCtrl',function($scope,$http,HostingService,EndUserService,StorageService,DealService,ServiceDeskService,AppsService,customInterceptor,endpointsurls,$stateParams,$dialog,$confirm,$state) {
	$scope.tabs=[{class:'active',href:"#inputtab",tabname:"Input"},{class:'inActive',href:"#caltab",tabname:"Calculate"},{class:'inActive',href:"#resulttab",tabname:"Result"}];

	//initialize all the variable needed for the page.
	

	var ctrl = this;
	ctrl.active = 0;
	$scope.dealId = $stateParams.Id
	$scope.dealInfo={};
	$scope.hostingInfo = {};
    $scope.dealDetails = {};
    $scope.appDistList = {};
    $scope.selectedAppId = {};
    $scope.solution = {};
    $scope.viewBy = {type:'unit'};
	$scope.appInfoDto = {};
	$scope.existingAppInfo = {};
	$scope.appCalculateDto = {};
	$scope.showErr=false;
	$scope.showVolumeErr=false;
	$scope.custom=false;

	$scope.totalApps = {
			open : false
		};


	$scope.gotoTotals=function()
	{
		$scope.appSave= DealService.getter() || isSave;
		$scope.appSave.appsA=true;
		DealService.setter($scope.appSave);
		$scope.assessmentIndicator[6]=1;
        var urlIndicator= endpointsurls.GENERic_INDICATOR+$stateParams.dealId+'?assessmentIndicator='+$scope.assessmentIndicator.join(',')+'&submissionIndicator='+$scope.submissionIndicator;
       customInterceptor.putrequestWithoutData(urlIndicator).then(function successCallback(response) {
    	   $state.go('home.assessment.generic.totals',({dealId:$stateParams.dealId}));
       });
		
	}

	// function for setting the custom value
	$scope.setCustom=function()
	{
		$scope.custom=true;
		var CustomSolutionId=_.where($scope.solList, {solutionName: 'Custom'})[0];
		$scope.selectedSolutionId = CustomSolutionId.solutionId;
	}

	// function for toggle panel
	$scope.togglePanel = function(level) {
		if(level.open){
			level.open=false;
		}else
		{
			level.open=true;
			//retainOldVolumes(level);
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
		if($event.keyCode == 13 ||
	           $event.keyCode == 9)
			{
			for(var i=index;i<$scope.appInfo.total.children[0].distributedVolume.length;i++)
				{
			$scope.appInfo.total.children[0].distributedVolume[i].volume=$scope.appInfo.total.children[0].distributedVolume[index].volume;
			}
	}}

	// on change percentage
	$scope.onChangeAppPercentage=function(child){
		$scope.custom=false
		var per=0;
		for(var i=0;i<child.length;i++)
			{
			per=parseInt(per)+parseInt(child[i].percentage);
			}

		if(per!=100)
			{
			$scope.showErr=true;
			}else
				{
				$scope.showErr=false;
				$scope.showVolumeErr=false;
				$scope.setCustom();
				calculateAppVolume($scope.appInfo.total.children[0]);
				}
	}

	// on change volume
	$scope.onChangechildVolume=function()
	{

		for(var i=0;i< $scope.appInfo.total.children[0].children.length;i++){
			$scope.appInfo.total.children[0].children[i].percentage = "";
		}
		for (var y = 0; y < $scope.dealInfo/12; y++) {
    		if(($scope.appInfo.total.children[0].distributedVolume[y].volume)!=parseInt(($scope.appInfo.total.children[0].children[0].distributedVolume[y].volume))
    		+parseInt(($scope.appInfo.total.children[0].children[1].distributedVolume[y].volume))
    		+ parseInt(($scope.appInfo.total.children[0].children[2].distributedVolume[y].volume))
    		+parseInt(($scope.appInfo.total.children[0].children[3].distributedVolume[y].volume))){
    			$scope.showVolumeErr=true;
    			break;
    		}else
    		{
    			$scope.showVolumeErr=false;
    			$scope.setCustom();
    		}
    	}
	}

		// get the drop-down values
		$scope.getAppsDropdowns=function(){
			var url = endpointsurls.APP_DROPDOWN+$stateParams.dealId;
	        customInterceptor.getrequest(url).then(function successCallback(response) {
				$scope.solList = response.data.applicationSolutionsInfoDtoList;
				$scope.genericDealInfoDto = response.data;
				$scope.dealInfoDto = response.data.dealInfoDto;
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
	        	$scope.appSave= DealService.getter() || isSave;
	            DealService.setter($scope.appSave);
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
	        $scope.dealInfo= $scope.dealInfoDto.dealTerm;
	        $scope.appInfo = AppsService.initAppDetails($scope.dealInfo);
	        console.log($scope.appInfo);
	        if($stateParams.dealId>0){
				 var url = endpointsurls.SAVE_APP_INFO+ $stateParams.dealId;
		        customInterceptor.getrequest(url).then(function successCallback(response) {
		        	$scope.existingAppInfo = response.data;
		       		$scope.id=response.data.id
		       		$scope.level=$scope.existingAppInfo.levelIndicator;
		       		$scope.appInfo.total.open=true;
		       		$scope.appInfo.total.children[0].open=($scope.level==1)?true:false;
		       		if($scope.existingAppInfo != null && $scope.existingAppInfo.id!=null){
		       			mapExistingAppInfo();
		       		}
		        }, function errorCallback(response) {
		        	//$scope.appId=undefined;
		        });

			}
		}


		// function of init app info
	    $scope.init=function() {
			$scope.getAppsDropdowns();

		}
		$scope.init();
		

	    // map existingAppInfo
	    function mapExistingAppInfo() {
	    	 $scope.dealDetails.offshoreSelected= $scope.existingAppInfo.offshoreAllowed == true ? "Yes" : "No";;
		    $scope.dealDetails.standardwindowInfoSelected= $scope.existingAppInfo.levelOfService;
	    	$scope.selectedSolutionId = $scope.existingAppInfo.selectedAppSolution;
	    	if($scope.selectedSolutionId != null){
	   			var CustomSolutionName = _.where($scope.solList, {solutionId: $scope.selectedSolutionId})[0].solutionName;
	   	   		if(CustomSolutionName != 'Custom'){
	   	   			$scope.onChangeDistSetting($scope.selectedSolutionId);
	   	   		}
	   		}
	    	getExistingAppYearlyInfo();
	    }

	    // map ExistingAppYearlyInfo
	    function getExistingAppYearlyInfo() {
	    	if($scope.existingAppInfo.appYearlyDataInfoDtoList != null){
	    		for (var y = 0; y < $scope.existingAppInfo.appYearlyDataInfoDtoList.length; y++){
	        		var yearlyDto = $scope.existingAppInfo.appYearlyDataInfoDtoList[y];
	        		$scope.appInfo.total.children[0].distributedVolume[y].year = yearlyDto.year;
	        		$scope.appInfo.total.children[0].distributedVolume[y].volume = yearlyDto.totalAppsVolume;
	        		$scope.appInfo.total.children[0].children[0].distributedVolume[y].volume = yearlyDto.simpleAppsVolume;
	        		$scope.appInfo.total.children[0].children[1].distributedVolume[y].volume = yearlyDto.mediumAppsVolume;
	        		$scope.appInfo.total.children[0].children[2].distributedVolume[y].volume = yearlyDto.complexAppsVolume;
	        		$scope.appInfo.total.children[0].children[3].distributedVolume[y].volume = yearlyDto.veryComplexAppsVolume;
	        	}
	    	}
	    }


		// onChange of solution setting
		$scope.onChangeDistSetting = function(solId) {
			$scope.showErr=false;
			$scope.showVolumeErr=false;
			$scope.selectedSolutionId = solId;
	    	$scope.solution = getSelectedAppSolutionObject(solId);
	    	setLevelWiseAppPercentage($scope.appInfo.total);

	    };

	    // on change of parent volume
	    $scope.calcVolume = function(node) {
	    	if($scope.selectedSolutionId != null && $scope.selectedSolutionId != undefined &&  $scope.selectedSolutionId != ''){
	    		calculateAppVolume(node);
	    	}
	    }

	    // on change of percentage
//	    $scope.onChangePercentage = function(node) {
//	    	$scope.selectedContactId = 0;
//	    	calculateAppVolume($scope.appInfo.total.children[0]);
//	    };

	    //calculate volume on change
	    function calculateAppVolume(parent) {
	    	if(parent.children != null){
	    		for(var k = 0;k < parent.children.length;k++) {
	    			var child = parent.children[k];
	        		for (var i = 0; i < $scope.dealInfo/12; i++){
	        			child.distributedVolume[i].volume = Math.round((parent.distributedVolume[i].volume * child.percentage)/100);
	        		}
	    		}
	    	}
	    }

	    // set volume on change of percentage
	    function setLevelWiseAppPercentage(parent) {
	    	if(parent.children != null) {
	    		for(var k = 0;k < parent.children[0].children.length;k++) {
	    			var child = parent.children[0].children[k];
	    			switch(child.id) {
		    			case "1.1.1":
		    				child.percentage = $scope.solution ? $scope.solution.simplePerc : "";
		    				break;
		    			case "1.1.2":
		    				child.percentage = $scope.solution ? $scope.solution.mediumPerc : "";
		    				break;
		    			case "1.1.3":
		    				child.percentage = $scope.solution ? $scope.solution.complexPerc : "";
		    				break;
		    			case "1.1.4":
		    				child.percentage = $scope.solution ? $scope.solution.veryComplexPerc : "";
		    				break;
		        	}
	    		}
	    	}
	    	calculateAppVolume($scope.appInfo.total.children[0]);
	    }

	    // get the selected solution
	    function getSelectedAppSolutionObject(solId) {
	    	var sol = {};
	    	for(var i = 0; i < $scope.solList.length; i++ ) {
	    		if($scope.solList[i].solutionId == solId) {
	    			sol = $scope.solList[i];
	    			break;
	    		}
	    	}
	    	return sol;
	    }

	    // save app info
	    $scope.saveAppInfo=function() {
	    	setAppYearlyInfo();
	    	$scope.appInfoDto = {
	    			selectedAppSolution : $scope.selectedSolutionId,
	    			offshoreAllowed : $scope.dealDetails.offshoreSelected == "Yes" ? true : false,
	    	    	levelOfService :  $scope.dealDetails.standardwindowInfoSelected,
	    	    	appYearlyDataInfoDtoList : $scope.appInfo.appYearlyDataInfoDtos,
	    	    	levelIndicator:($scope.appInfo.total.children[0].open)?1:0,
	    	    	dealId : $stateParams.dealId,
	    	}
	    	console.log($scope.appInfoDto);
	    	var url = endpointsurls.SAVE_APP_INFO+$stateParams.dealId;
	    	if(!$scope.appInfo.total.children[0].open){$confirm({text: 'Default distribution setting has been applied to child levels'})
	 	        .then(function() {
	        customInterceptor.postrequest(url,$scope.appInfoDto).then(function successCallback(response) {
	        	$scope.appId=response.data.id;
		        	console.log($scope.storageId);
		        	$scope.goToCal();
		        //   $scope.populatedCalculateTabDetails();
			}, function errorCallback(response) {
				console.log(response.statusText);
			});
	 	        })}else
	 	        	{
	 	        	customInterceptor.postrequest(url,$scope.appInfoDto).then(function successCallback(response) {
	 		        	$scope.appId=response.data.id;
	 			        	console.log($scope.storageId);
	 			        	$scope.goToCal();
	 			        //   $scope.populatedCalculateTabDetails();
	 				}, function errorCallback(response) {
	 					console.log(response.statusText);
	 				});
	 	        	};
	    	//
	    }

        // set yearly data
	    function setAppYearlyInfo() {
	    	var yearlyInfoList = [];
	    	for (var y = 0; y < $scope.dealInfo/12; y++) {
	    		var yearlyData = {};
	    		yearlyData.year = y+1;
	    		yearlyData.totalAppsVolume = $scope.appInfo.total.children[0].distributedVolume[y].volume;
	    		yearlyData.simpleAppsVolume = $scope.appInfo.total.children[0].children[0].distributedVolume[y].volume;
	    		yearlyData.mediumAppsVolume = $scope.appInfo.total.children[0].children[1].distributedVolume[y].volume;
	    		yearlyData.complexAppsVolume = $scope.appInfo.total.children[0].children[2].distributedVolume[y].volume;
	    		yearlyData.veryComplexAppsVolume = $scope.appInfo.total.children[0].children[3].distributedVolume[y].volume;
	    		yearlyInfoList.push(yearlyData);
	    	}
	    	$scope.appInfo.appYearlyDataInfoDtos = yearlyInfoList;
	    }

	    /*********************************Calculate tab*****************************/

	   $scope.populatedCalculateTabDetails = function() {
		   var url = endpointsurls.CALCULATE_APP_INFO+$stateParams.dealId;
	        customInterceptor.getrequest(url).then(function successCallback(response) {
	        	$scope.calculateAppInfo=response.data;
	        	 $scope.extractAppAvg($scope.appInfo.total.children[0]);
	        	 $scope.unitPriceDto=[];
	 			$scope.createUnitPriceForLevels($scope.appInfo.total.children[0].distributedVolume.length);
			}, function errorCallback(response) {
				console.log(response.statusText);
				if(response.status=='401'){
					$state.go('login');
				}
			});}

	   // Recalculate
	   $scope.reCalculate=function()
	   {
		   $scope.reset();
		 var putDeatls=  {"offshoreAllowed" : $scope.dealDetails.offshoreSelected == "Yes" ? true : false,
               "levelOfService" :$scope.dealDetails.standardwindowInfoSelected,
               'includeHardware' : null,
               "multiLingual" :  null,
               "toolingIncluded" : null};
		     var url = endpointsurls.APP_RECALCULATE+$scope.appId;
	        customInterceptor.putrequest(url,putDeatls).then(function successCallback(response) {
	        	$scope.tabReset(1);
	        	$scope.calculateAppInfo=response.data;
	        	 $scope.extractAppAvg($scope.appInfo.total.children[0]);
	        	 });
	        $scope.createUnitPriceForLevels($scope.appInfo.total.children[0].distributedVolume.length);
	   }

	// function for move to calculate tab.
		$scope.goToCal=function()
		{
			$scope.reset();
			$scope.populatedCalculateTabDetails();
			$scope.tabReset(1);
		}

		// reset radio
		$scope.reset=function()
		{
			$scope.appInfo.total.children[0].selectedRadio='';
			$scope.appInfo.total.children[0].children[0].perNonPer='';
			$scope.appInfo.total.children[0].children[1].perNonPer='';
			$scope.appInfo.total.children[0].children[2].perNonPer='';
			$scope.appInfo.total.children[0].children[3].perNonPer='';
		}

	// function used for creating the unit price year wise
		  $scope.createUnitPriceForLevels=function(val)
		  {
			  $scope.unitPriceDto=[];
			  for(var i=1;i<=val;i++)
				  {
				  var unitPrice={};
				  unitPrice.year=i;
				  unitPrice.totalAppsUnitPrice=0;
				  unitPrice.totalAppsRevenue=0;
				  unitPrice.simpleAppsUnitPrice=0;
				  unitPrice.simpleAppsRevenue=0;
				  unitPrice.mediumAppsUnitPrice=0;
				  unitPrice.mediumAppsRevenue=0;
				  unitPrice.complexAppsUnitPrice=0;
				  unitPrice.complexAppsRevenue=0;
				  unitPrice.veryComplexAppsUnitPrice=0;
				  unitPrice.veryComplexAppsRevenue=0;
				  $scope.unitPriceDto.push(unitPrice);
				  }

		  }

		  // function for extractAppAvg
	   $scope.extractAppAvg=function(parent) {
	       if($scope.calculateAppInfo.totalApplicationCalculateDto!=null)
	        	{
	    	parent.benchLow = $scope.calculateAppInfo.totalApplicationCalculateDto.benchDealLowTotalAppsAvgUnitPrice !=null ? $scope.calculateAppInfo.totalApplicationCalculateDto.benchDealLowTotalAppsAvgUnitPrice :"NA";
	        parent.benchTarget =  $scope.calculateAppInfo.totalApplicationCalculateDto.benchDealTargetTotalAppsAvgUnitPrice !=null ? $scope.calculateAppInfo.totalApplicationCalculateDto.benchDealTargetTotalAppsAvgUnitPrice:"NA";
	        parent.pastAvg = $scope.calculateAppInfo.totalApplicationCalculateDto.pastDealTotalAppsAvgUnitPrice !=null  ? $scope.calculateAppInfo.totalApplicationCalculateDto.pastDealTotalAppsAvgUnitPrice : "NA";
	        parent.compAvg = $scope.calculateAppInfo.totalApplicationCalculateDto.compDealTotalAppsAvgUnitPrice !=null? $scope.calculateAppInfo.totalApplicationCalculateDto.compDealTotalAppsAvgUnitPrice :"NA";
	        	}else
	        		{
	        		parent.benchLow='NA';
	        		parent.benchTarget='NA';
	        		parent.pastAvg='NA';
	        		 parent.compAvg='NA';
	        		}

	        for(var k = 0;k < parent.children.length;k++){
	        	child = parent.children[k];
	            switch(child.id) {
	            case "1.1.1": //Simple Apps
	            	if($scope.calculateAppInfo.simpleApplicationCalculateDto!=null){
	                child.benchLow =$scope.calculateAppInfo.simpleApplicationCalculateDto.benchDealLowSimpleAppsAvgUnitPrice!=null ? $scope.calculateAppInfo.simpleApplicationCalculateDto.benchDealLowSimpleAppsAvgUnitPrice : "NA";
	                child.benchTarget =$scope.calculateAppInfo.simpleApplicationCalculateDto.benchDealTargetSimpleAppsAvgUnitPrice!=null ? $scope.calculateAppInfo.simpleApplicationCalculateDto.benchDealTargetSimpleAppsAvgUnitPrice : "NA";
	                child.pastAvg = $scope.calculateAppInfo.simpleApplicationCalculateDto.pastDealSimpleAppsAvgUnitPrice!=null? $scope.calculateAppInfo.simpleApplicationCalculateDto.pastDealSimpleAppsAvgUnitPrice : "NA";
	                child.compAvg = $scope.calculateAppInfo.simpleApplicationCalculateDto.compDealSimpleAppsAvgUnitPrice!=null ? $scope.calculateAppInfo.simpleApplicationCalculateDto.compDealSimpleAppsAvgUnitPrice : "NA";
	            	}else
	            		{
	            		child.benchLow='NA';
	            		child.benchTarget='NA';
		        		child.pastAvg='NA';
		        		child.compAvg='NA';
	            		}
	                break;
	            case "1.1.2": //Medium Apps
	            	if($scope.calculateAppInfo.mediumApplicationCalculateDto!=null){
		                child.benchLow =$scope.calculateAppInfo.mediumApplicationCalculateDto.benchDealLowMediumAppsAvgUnitPrice!=null ? $scope.calculateAppInfo.mediumApplicationCalculateDto.benchDealLowMediumAppsAvgUnitPrice : "NA";
		                child.benchTarget =$scope.calculateAppInfo.mediumApplicationCalculateDto.benchDealTargetMediumAppsAvgUnitPrice!=null ? $scope.calculateAppInfo.mediumApplicationCalculateDto.benchDealTargetMediumAppsAvgUnitPrice : "NA";
		                child.pastAvg = $scope.calculateAppInfo.mediumApplicationCalculateDto.pastDealMediumAppsAvgUnitPrice!=null? $scope.calculateAppInfo.mediumApplicationCalculateDto.pastDealMediumAppsAvgUnitPrice : "NA";
		                child.compAvg = $scope.calculateAppInfo.mediumApplicationCalculateDto.compDealMediumAppsAvgUnitPrice!=null ? $scope.calculateAppInfo.mediumApplicationCalculateDto.compDealMediumAppsAvgUnitPrice : "NA";
		            	}else
		            		{
		            		child.benchLow='NA';
		            		child.benchTarget='NA';
			        		child.pastAvg='NA';
			        		child.compAvg='NA';
		            		}
	                break;
	            case "1.1.3": //Complex Apps
	            	if($scope.calculateAppInfo.complexApplicationCalculateDto!=null){
		                child.benchLow =$scope.calculateAppInfo.complexApplicationCalculateDto.benchDealLowComplexAppsAvgUnitPrice!=null ? $scope.calculateAppInfo.complexApplicationCalculateDto.benchDealLowComplexAppsAvgUnitPrice : "NA";
		                child.benchTarget =$scope.calculateAppInfo.complexApplicationCalculateDto.benchDealTargetComplexAppsAvgUnitPrice!=null ? $scope.calculateAppInfo.complexApplicationCalculateDto.benchDealTargetComplexAppsAvgUnitPrice : "NA";
		                child.pastAvg = $scope.calculateAppInfo.complexApplicationCalculateDto.pastDealComplexAppsAvgUnitPrice!=null? $scope.calculateAppInfo.complexApplicationCalculateDto.pastDealComplexAppsAvgUnitPrice : "NA";
		                child.compAvg = $scope.calculateAppInfo.complexApplicationCalculateDto.compDealComplexAppsAvgUnitPrice!=null ? $scope.calculateAppInfo.complexApplicationCalculateDto.compDealComplexAppsAvgUnitPrice : "NA";
		            	}else
		            		{
		            		child.benchLow='NA';
		            		child.benchTarget='NA';
			        		child.pastAvg='NA';
			        		child.compAvg='NA';
		            		}
	                break;
	            case "1.1.4": //Very Complex Apps
	            	if($scope.calculateAppInfo.veryComplexApplicationCalculateDto!=null){
		                child.benchLow =$scope.calculateAppInfo.veryComplexApplicationCalculateDto.benchDealLowVeryComplexAppsAvgUnitPrice!=null ? $scope.calculateAppInfo.veryComplexApplicationCalculateDto.benchDealLowVeryComplexAppsAvgUnitPrice : "NA";
		                child.benchTarget =$scope.calculateAppInfo.veryComplexApplicationCalculateDto.benchDealTargetVeryComplexAppsAvgUnitPrice!=null ? $scope.calculateAppInfo.veryComplexApplicationCalculateDto.benchDealTargetVeryComplexAppsAvgUnitPrice : "NA";
		                child.pastAvg = $scope.calculateAppInfo.veryComplexApplicationCalculateDto.pastDealVeryComplexAppsAvgUnitPrice!=null? $scope.calculateAppInfo.veryComplexApplicationCalculateDto.pastDealVeryComplexAppsAvgUnitPrice : "NA";
		                child.compAvg = $scope.calculateAppInfo.veryComplexApplicationCalculateDto.compDealVeryComplexAppsAvgUnitPrice!=null ? $scope.calculateAppInfo.veryComplexApplicationCalculateDto.compDealVeryComplexAppsAvgUnitPrice : "NA";
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



	 // on selecting the radio button of calculate
		  $scope.createUnitPrice=function(child,key)
		  {
				switch (key) {
			    case 'pastAvg':// pastAvg
			    	switch (child.id) {
				    case '1.1'://total
				    	$scope.past=$scope.calculateAppInfo.totalApplicationCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				        break;
				    case '1.1.1'://Simple Apps
				    	$scope.past=$scope.calculateAppInfo.simpleApplicationCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				        break;
				    case '1.1.2'://Medium Apps
				    	$scope.past=$scope.calculateAppInfo.mediumApplicationCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				    	break;
				    case '1.1.3'://Complex Apps
				    	$scope.past=$scope.calculateAppInfo.complexApplicationCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				    	break;
				    case '1.1.4'://very Complex Apps
				    	$scope.past=$scope.calculateAppInfo.veryComplexApplicationCalculateDto.pastDealYearlyCalcDtoList;
				    	console.log($scope.pastVeryComplexApps);
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				    	break;
				    	}
			        break;
			    case 'benchLow':// benchLow
			    	switch (child.id) {
				    case '1.1'://total
				    	$scope.benchLow=$scope.calculateAppInfo.totalApplicationCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
				        break;
				    case '1.1.1'://Simple Apps
				    	$scope.benchLow=$scope.calculateAppInfo.simpleApplicationCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
				        break;
				    case '1.1.2'://Medium Apps
				    	$scope.benchLow=$scope.calculateAppInfo.mediumApplicationCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
				    	break;
				    case '1.1.3'://Complex Apps
				    	$scope.benchLow=$scope.calculateAppInfo.complexApplicationCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
				    	break;
				    case '1.1.4'://very Complex Apps
				    	$scope.benchLow=$scope.calculateAppInfo.veryComplexApplicationCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
				    	break;
				    	}
			        break;
			    case 'benchTarget':// benchTarget
			    	switch (child.id) {
				    case '1.1'://total
				    	$scope.benchTarget=$scope.calculateAppInfo.totalApplicationCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				        break;
				    case '1.1.1'://simple
				    	$scope.benchTarget=$scope.calculateAppInfo.simpleApplicationCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				        break;
				    case '1.1.2'://medium
				    	$scope.benchTarget=$scope.calculateAppInfo.mediumApplicationCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				    	break;
				    case '1.1.3'://Complex Apps
				    	$scope.benchTarget=$scope.calculateAppInfo.complexApplicationCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				    	break;
				    case '1.1.4'://very Complex Apps
				    	$scope.benchTarget=$scope.calculateAppInfo.veryComplexApplicationCalculateDto.benchmarkTargetYearlyCalcDtoList;
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
			    case '1.1'://total
			    	for(var i=0;i<untiPrices.length;i++)
			    		{
			    		$scope.unitPriceDto[i].totalAppsUnitPrice=untiPrices[i].unitPrice;
			    		$scope.unitPriceDto[i].totalAppsRevenue=untiPrices[i].revenue;
			    		}
			        break;
			    case '1.1.1'://simple
			    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].simpleAppsUnitPrice=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].simpleAppsRevenue=untiPrices[i].revenue;
		    		$scope.unitPriceDto[i].totalAppsRevenue=$scope.unitPriceDto[i].simpleAppsRevenue+$scope.unitPriceDto[i].mediumAppsRevenue+$scope.unitPriceDto[i].complexAppsRevenue+$scope.unitPriceDto[i].veryComplexAppsRevenue;
		    		$scope.unitPriceDto[i].totalAppsUnitPrice=$scope.unitPriceDto[i].totalAppsRevenue/$scope.appInfo.total.children[0].distributedVolume[i].volume;
		    		}
			        break;
			    case '1.1.2'://medium
			    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].mediumAppsUnitPrice=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].mediumAppsRevenue=untiPrices[i].revenue;
		    		$scope.unitPriceDto[i].totalAppsRevenue=$scope.unitPriceDto[i].simpleAppsRevenue+$scope.unitPriceDto[i].mediumAppsRevenue+$scope.unitPriceDto[i].complexAppsRevenue+$scope.unitPriceDto[i].veryComplexAppsRevenue;
		    		$scope.unitPriceDto[i].totalAppsUnitPrice=$scope.unitPriceDto[i].totalAppsRevenue/$scope.appInfo.total.children[0].distributedVolume[i].volume;
		    		}
			    	break;
			    case '1.1.3'://complex
			    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].complexAppsUnitPrice=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].complexAppsRevenue=untiPrices[i].revenue;
		    		$scope.unitPriceDto[i].totalAppsRevenue=$scope.unitPriceDto[i].simpleAppsRevenue+$scope.unitPriceDto[i].mediumAppsRevenue+$scope.unitPriceDto[i].complexAppsRevenue+$scope.unitPriceDto[i].veryComplexAppsRevenue;
		    		$scope.unitPriceDto[i].totalAppsUnitPrice=$scope.unitPriceDto[i].totalAppsRevenue/$scope.appInfo.total.children[0].distributedVolume[i].volume;
		    		}
			    	break;
			    case '1.1.4':// very complex
			    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].veryComplexAppsUnitPrice=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].veryComplexAppsRevenue=untiPrices[i].revenue;
		    		$scope.unitPriceDto[i].totalAppsRevenue=$scope.unitPriceDto[i].simpleAppsRevenue+$scope.unitPriceDto[i].mediumAppsRevenue+$scope.unitPriceDto[i].complexAppsRevenue+$scope.unitPriceDto[i].veryComplexAppsRevenue;
		    		$scope.unitPriceDto[i].totalAppsUnitPrice=$scope.unitPriceDto[i].totalAppsRevenue/$scope.appInfo.total.children[0].distributedVolume[i].volume;
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
				  var infoModel={id:child.id,dealType:key,url:endpointsurls.APP_NEAREST_DEAL};
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


		// function for go to result
		$scope.goToResult=function()
		{
			for(var i=0;i<$scope.unitPriceDto.length;i++)
			  {
			  $scope.unitPriceDto[i].totalAppsRevenue=$scope.unitPriceDto[i].totalAppsRevenue*12;
			  }
			var url= endpointsurls.APP_REVENUE+$scope.appId;
			customInterceptor.putrequest(url,$scope.unitPriceDto).then(function successCallback(response) {
				 var urlresult = endpointsurls.SAVE_APP_INFO+ $stateParams.dealId;
			        customInterceptor.getrequest(urlresult).then(function successCallback(response) {
			        	$scope.existingAppresultInfo = response.data;
			        	$scope.tabReset(2);
			       		if($scope.existingAppresultInfo != null && $scope.existingAppresultInfo.id!=null){
			       			getexistingAppresultInfo();
			       		}
			        }, function errorCallback(response) {
			        	//$scope.appId=undefined;
			        });
	        	$scope.viewBy = {type:'unit'};
			}, function errorCallback(response) {
				console.log(response.statusText);
			});

		}

		//Todo modal for find deal section
		var dialogOptions = {
		    controller: 'DealInformationCtrl',
		    templateUrl: '/modals/dealInformation/dealInformation.html'
		  };
		  $scope.edit = function(searchModel){
		    $dialog.dialog(angular.extend(dialogOptions, {resolve: {searchModel: angular.copy(searchModel)}}))
		      .open()
		      .then(function(result) {
		        if(result) {
		          angular.copy(result, searchModel);
		        }
		        searchModel = undefined;
		    });
		  };

		//------------------------------Result Tab----------------------------------

		// back to calculate tab
		$scope.backToCalculate=function()
		{
			$scope.tabReset(1);
		}

		// get existing yearly data when initialize
	    function getexistingAppresultInfo() {
	    	if($scope.existingAppresultInfo.appYearlyDataInfoDtoList != null){
	    		for (var y = 0; y < $scope.existingAppresultInfo.appYearlyDataInfoDtoList.length; y++){
	        		var yearlyDto = $scope.existingAppresultInfo.appYearlyDataInfoDtoList[y];
	        		getExistingServerPricingLevelWiseInfo($scope.appInfo.total.children[0],yearlyDto,y);

	        	}
	    	}
	    }

	    // Get existing App server price level wise
		function getExistingServerPricingLevelWiseInfo(parent,yearlyDto,year){
			if(yearlyDto.appUnitPriceInfoDtoList != null){
				if(yearlyDto.appUnitPriceInfoDtoList.length > 0){
					  if($scope.viewBy.type == 'unit'){
						  $scope.appInfo.total.children[0].distributedVolume[year].unit = yearlyDto.appUnitPriceInfoDtoList[0].totalAppsUnitPrice;
						  for(var k = 0;k < parent.children.length;k++){
				  			child = parent.children[k];
				  			switch(child.id) {
				  			case "1.1": //total
								break;
							case "1.1.1": //Simple Apps Server pricing
								child.distributedVolume[year].unit = yearlyDto.appUnitPriceInfoDtoList[0].simpleAppsUnitPrice;
								break;
							case "1.1.2": //Medium Apps Server pricing
								child.distributedVolume[year].unit = yearlyDto.appUnitPriceInfoDtoList[0].mediumAppsUnitPrice;
								break;
							case "1.1.3": //Complex Apps Server pricing
								child.distributedVolume[year].unit = yearlyDto.appUnitPriceInfoDtoList[0].complexAppsUnitPrice;
								break;
							case "1.1.4": //Very Complex Apps Server pricing
								child.distributedVolume[year].unit = yearlyDto.appUnitPriceInfoDtoList[0].veryComplexAppsUnitPrice;
								break;

				  			}
				  		}
					 }
				  }
			  }
		  }

		// On change Unit-revenue price radio button
		$scope.onchangeprice = function(value){
			if(value == 'revenue'){
				if($scope.appInfo.total.children[0].open == true){
					for(var i=0;i< $scope.appInfo.total.children[0].distributedVolume.length;i++){
						var simplerevenue = "";
						var mediumrevenue = "";
						var complexrevenue = "";
						var verycomplexrevenue = "";
						for(var k = 0;k < $scope.appInfo.total.children[0].children.length;k++){
							child = $scope.appInfo.total.children[0].children[k];
			    			switch(child.id) {
			    			case "1.1": //total
								break;
							case "1.1.1": //Simple Apps Server pricing
								child.distributedVolume[i].revenue = Math.round((parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume))  * (parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit)));
								if(isNaN(child.distributedVolume[i].revenue)){
		  							child.distributedVolume[i].revenue= 0;
		  						}
								simplerevenue = child.distributedVolume[i].revenue;
								break;
							case "1.1.2": //Medium Apps Server pricing
								child.distributedVolume[i].revenue = Math.round((parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume))  * (parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit)));
								if(isNaN(child.distributedVolume[i].revenue)){
		  							child.distributedVolume[i].revenue= 0;
		  						}
								mediumrevenue = child.distributedVolume[i].revenue;
								break;
							case "1.1.3": //Complex Apps Server pricing
								child.distributedVolume[i].revenue = Math.round((parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume))  * (parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit)));
								if(isNaN(child.distributedVolume[i].revenue)){
		  							child.distributedVolume[i].revenue= 0;
		  						}
								complexrevenue = child.distributedVolume[i].revenue;
								break;
							case "1.1.4": //Very Complex Apps Server pricing
								child.distributedVolume[i].revenue = Math.round((parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume))  * (parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit)));
								if(isNaN(child.distributedVolume[i].revenue)){
		  							child.distributedVolume[i].revenue= 0;
		  						}
								verycomplexrevenue = child.distributedVolume[i].revenue;
								break;
			    			}
						}
						$scope.appInfo.total.children[0].distributedVolume[i].revenue = Math.round(simplerevenue + mediumrevenue + complexrevenue + verycomplexrevenue);
					}
				}
				else{
					for(var i=0;i< $scope.appInfo.total.children[0].distributedVolume.length;i++){
						$scope.appInfo.total.children[0].distributedVolume[i].revenue = Math.round($scope.appInfo.total.children[0].distributedVolume[i].volume * $scope.appInfo.total.children[0].distributedVolume[i].unit);
						if(isNaN($scope.appInfo.total.children[0].distributedVolume[i].revenue)){
							$scope.appInfo.total.children[0].distributedVolume[i].revenue = 0;
  						}
					}
				}
			}
			if(value == 'unit'){
				if($scope.appInfo.total.children[0].open == true){
					for(var i=0;i< $scope.appInfo.total.children[0].distributedVolume.length;i++){
						for(var k = 0;k < $scope.appInfo.total.children[0].children.length;k++){
							child = $scope.appInfo.total.children[0].children[k];
			    			switch(child.id) {
			    			case "1.1": //total
								break;
							case "1.1.1": //Simple Apps Server pricing
								child.distributedVolume[i].unit = (parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue)) / (parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume));
								if(isNaN(child.distributedVolume[i].unit)){
		  							child.distributedVolume[i].unit= 0;
		  						}
								child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
								break;
							case "1.1.2": //Medium Apps Server pricing
								child.distributedVolume[i].unit = (parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue)) / (parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume));
								if(isNaN(child.distributedVolume[i].unit)){
		  							child.distributedVolume[i].unit= 0;
		  						}
								child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
								break;
							case "1.1.3": //Complex Apps Server pricing
								child.distributedVolume[i].unit = (parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue)) / (parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume));
								if(isNaN(child.distributedVolume[i].unit)){
		  							child.distributedVolume[i].unit= 0;
		  						}
								child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
								break;
							case "1.1.4": //Very Complex Apps Server pricing
								child.distributedVolume[i].unit = (parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue)) / (parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume));
								if(isNaN(child.distributedVolume[i].unit)){
		  							child.distributedVolume[i].unit= 0;
		  						}
								child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
								break;
			    			}
						}
						$scope.appInfo.total.children[0].distributedVolume[i].unit = ($scope.appInfo.total.children[0].distributedVolume[i].revenue / $scope.appInfo.total.children[0].distributedVolume[i].volume).toFixed(2);
						if(isNaN($scope.appInfo.total.children[0].distributedVolume[i].unit)){
							$scope.appInfo.total.children[0].distributedVolume[i].unit = 0.00;
  						}
					}
				}
				else{
					for(var i=0;i< $scope.appInfo.total.children[0].distributedVolume.length;i++){
						$scope.appInfo.total.children[0].distributedVolume[i].unit = ($scope.appInfo.total.children[0].distributedVolume[i].revenue / $scope.appInfo.total.children[0].distributedVolume[i].volume).toFixed(2);
						if(isNaN($scope.appInfo.total.children[0].distributedVolume[i].unit)){
							$scope.appInfo.total.children[0].distributedVolume[i].unit = 0.00;
  						}
					}
				}
			}
		}




});

