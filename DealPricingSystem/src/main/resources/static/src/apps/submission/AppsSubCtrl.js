priceToolcontrollers.controller('AppsSubCtrl',function($scope,$http,HostingService,EndUserService,StorageService,DealService,ServiceDeskService,AppsService,customInterceptor,endpointsurls,$stateParams,$dialog,$confirm,$state) {
	
	//initialize all the variable needed for the page.
	
	var ctrl = this;
	ctrl.active = 0;
	$scope.dealInfo={};
	$scope.hostingInfo = {};
    $scope.dealDetails = {};
    $scope.appDistList = {};
    //$scope.selectedSolutionId = {};
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
	$scope.tabEvent=function($event,index,val,childindex){
		if($event.keyCode == 13 || $event.keyCode == 9){
			switch(val) {
			case "total":
				for(var i=index;i<$scope.appInfo.total.children[0].distributedVolume.length;i++){
					$scope.appInfo.total.children[0].distributedVolume[i].volume = $scope.appInfo.total.children[0].distributedVolume[index].volume;
				}
				break;
			case "parentpricingrevenue":
				for(var i=index;i<$scope.appInfo.total.children[0].distributedVolume.length;i++){
					$scope.appInfo.total.children[0].distributedVolume[i].revenue = $scope.appInfo.total.children[0].distributedVolume[index].revenue;
				}
				break;
			case "parentpricingunit":
				for(var i=index;i<$scope.appInfo.total.children[0].distributedVolume.length;i++){
					$scope.appInfo.total.children[0].distributedVolume[i].unit = $scope.appInfo.total.children[0].distributedVolume[index].unit;
				}
				break;
			case "childpricingrevenue":
				for(var i=index;i<$scope.appInfo.total.children[0].distributedVolume.length;i++){
					$scope.appInfo.total.children[0].children[childindex].distributedVolume[i].revenue = $scope.appInfo.total.children[0].children[childindex].distributedVolume[index].revenue
				}
				break;
			case "childpricingunit":
				for(var i=index;i<$scope.appInfo.total.children[0].distributedVolume.length;i++){
					$scope.appInfo.total.children[0].children[childindex].distributedVolume[i].unit = $scope.appInfo.total.children[0].children[childindex].distributedVolume[index].unit
				}
				break;
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
        $scope.dealInfo= $scope.dealInfoDto.dealTerm;
        $scope.appInfo = AppsService.initAppDetails($scope.dealInfo);
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
	        	$scope.appId=undefined;
	        });

		}
	}

	//function for init controller
	$scope.init=function(){
		$scope.getAppsDropdowns();
	}
	$scope.init();

	// map existingAppInfo
    function mapExistingAppInfo() {
    	$scope.dealDetails.towerArchitect = $scope.existingAppInfo.towerArchitect
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

    // get existing yearly data when initialize
    function getExistingAppYearlyInfo() {
    	if($scope.existingAppInfo.appYearlyDataInfoDtoList != null){
    		for (var y = 0; y < $scope.existingAppInfo.appYearlyDataInfoDtoList.length; y++){
        		var yearlyDto = $scope.existingAppInfo.appYearlyDataInfoDtoList[y];
        		$scope.appInfo.total.children[0].distributedVolume[y].year = yearlyDto.year;
        		$scope.appInfo.total.children[0].distributedVolume[y].volume = yearlyDto.totalAppsVolume;

        		getExistingVolumeLevelWiseInfo($scope.appInfo.total.children[0],yearlyDto,y);
        		getExistingServerPricingLevelWiseInfo($scope.appInfo.total.children[0],yearlyDto,y);

        	}
    	}
    }

    // getExistingStorageLevelWiseInfo
    function getExistingVolumeLevelWiseInfo(parent,yearlyDto,year){
    	var child = {};
    	if(parent.children != null){
    		for(var k = 0;k < parent.children.length;k++){
    			child = parent.children[k];
    			switch(child.id) {
					case "1.1": //total
						break;
					case "1.1.1": //Simple Apps Volume
						child.distributedVolume[year].volume = yearlyDto.simpleAppsVolume;
						break;
					case "1.1.2": //Medium Apps Volume
						child.distributedVolume[year].volume = yearlyDto.mediumAppsVolume;
						break;
					case "1.1.3": //Complex Apps Volume
						child.distributedVolume[year].volume = yearlyDto.complexAppsVolume;
						break;
					case "1.1.4": //Very Complex Apps Volume
						child.distributedVolume[year].volume = yearlyDto.veryComplexAppsVolume;
						break;
    			}
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
				  if($scope.viewBy.type == 'revenue'){
					  $scope.appInfo.total.children[0].distributedVolume[year].revenue = yearlyDto.appUnitPriceInfoDtoList[0].totalAppsUnitPrice * yearlyDto.totalAppsVolume;
					  for(var k = 0;k < parent.children.length;k++){
			  			child = parent.children[k];
			  			switch(child.id) {
			  			case "1.1": //total
							break;
						case "1.1.1": //Simple Apps Server pricing
							child.distributedVolume[year].revenue = yearlyDto.appUnitPriceInfoDtoList[0].simpleAppsUnitPrice * yearlyDto.simpleAppsVolume;
							break;
						case "1.1.2": //Medium Apps Server pricing
							child.distributedVolume[year].revenue = yearlyDto.appUnitPriceInfoDtoList[0].mediumAppsUnitPrice * yearlyDto.mediumAppsUnitPrice;
							break;
						case "1.1.3": //Complex Apps Server pricing
							child.distributedVolume[year].revenue = yearlyDto.appUnitPriceInfoDtoList[0].complexAppsUnitPrice * yearlyDto.complexAppsUnitPrice;
							break;
						case "1.1.4": //Very Complex Apps Server pricing
							child.distributedVolume[year].revenue = yearlyDto.appUnitPriceInfoDtoList[0].veryComplexAppsUnitPrice * yearlyDto.veryComplexAppsUnitPrice;
							break;

			  			}
			  		}
				  }

			  }
		}
	}

	// Parent server pricing is changed
	$scope.onchangeparentpricing = function(parent,value){
		if(value == 'revenue'){
			for(var i=0;i< parent.distributedVolume.length;i++){
				for(var k = 0;k < parent.children.length;k++){
					child = parent.children[k];
	    			switch(child.id) {
	  				case "1.1": //Server Volume
	  					break;
	  				case "1.1.1": //Performance Server
	  						child.distributedVolume[i].revenue = '';
	    				break;
	    			case "1.1.2": //Virtual Server
	    					child.distributedVolume[i].revenue = '';
	    				break;
	    			case "1.1.3": //Virtual Server
    					child.distributedVolume[i].revenue = '';
    					break;
	    			case "1.1.4": //Virtual Server
    					child.distributedVolume[i].revenue = '';
    					break;
	    			}
				}
			}
		}
		if(value == 'unit'){
			for(var i=0;i< parent.distributedVolume.length;i++){
				for(var k = 0;k < parent.children.length;k++){
					child = parent.children[k];
	    			switch(child.id) {
	  				case "1.1": //Server Volume
	  					break;
	  				case "1.1.1": //Performance Server
  						child.distributedVolume[i].unit = '';
	    				break;
	    			case "1.1.2": //Virtual Server
    					child.distributedVolume[i].unit = '';
	    				break;
	    			case "1.1.3": //Virtual Server
    					child.distributedVolume[i].unit = '';
    					break;
	    			case "1.1.4": //Virtual Server
    					child.distributedVolume[i].unit = '';
    					break;
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
					var Child1revenue = "";
					var Child2revenue = "";
					var Child3revenue = "";
					var Child4revenue = "";
					for(var k = 0;k < $scope.appInfo.total.children[0].children.length;k++){
						child = $scope.appInfo.total.children[0].children[k];
		    			switch(child.id) {
		    			case "1.1": //total
							break;
						case "1.1.1": //Simple Apps Server pricing
							child.distributedVolume[i].revenue = Math.round(((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume)  * ((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit));
							if(isNaN(child.distributedVolume[i].revenue)){
	  							child.distributedVolume[i].revenue= 0;
	  						}
							Child1revenue = child.distributedVolume[i].revenue;
							break;
						case "1.1.2": //Medium Apps Server pricing
							child.distributedVolume[i].revenue = Math.round(((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume)  * ((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit));
							if(isNaN(child.distributedVolume[i].revenue)){
	  							child.distributedVolume[i].revenue= 0;
	  						}
							Child2revenue = child.distributedVolume[i].revenue;
							break;
						case "1.1.3": //Complex Apps Server pricing
							child.distributedVolume[i].revenue = Math.round(((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume)  * ((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit));
							if(isNaN(child.distributedVolume[i].revenue)){
	  							child.distributedVolume[i].revenue= 0;
	  						}
							Child3revenue = child.distributedVolume[i].revenue;
							break;
						case "1.1.4": //Very Complex Apps Server pricing
							child.distributedVolume[i].revenue = Math.round(((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume)  * ((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit));
							if(isNaN(child.distributedVolume[i].revenue)){
	  							child.distributedVolume[i].revenue= 0;
	  						}
							Child4revenue = child.distributedVolume[i].revenue;
							break;
		    			}
					}
					$scope.appInfo.total.children[0].distributedVolume[i].revenue = Math.round(Child1revenue + Child2revenue + Child3revenue + Child4revenue);
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
							child.distributedVolume[i].unit = ((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue) / ((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							if(isNaN(child.distributedVolume[i].unit)){
	  							child.distributedVolume[i].unit= 0;
	  						}
							child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
							break;
						case "1.1.2": //Medium Apps Server pricing
							child.distributedVolume[i].unit = ((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue) / ((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							if(isNaN(child.distributedVolume[i].unit)){
	  							child.distributedVolume[i].unit= 0;
	  						}
							child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
							break;
						case "1.1.3": //Complex Apps Server pricing
							child.distributedVolume[i].unit = ((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue) / ((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							if(isNaN(child.distributedVolume[i].unit)){
	  							child.distributedVolume[i].unit= 0;
	  						}
							child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
							break;
						case "1.1.4": //Very Complex Apps Server pricing
							child.distributedVolume[i].unit = ((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue) / ((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
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

	// calculate parent server price when user insert child server pricing
	$scope.calcServerPrice = function (){
		var parent = $scope.appInfo.total.children[0];
		if(parent.children[0].distributedVolume[0].unit != ''){
			if($scope.viewBy.type == 'unit'){
				for(var i=0;i< parent.distributedVolume.length;i++){
					var Child1volume = "";
					var Child2volume = "";
					var Child3volume = "";
					var Child4volume = "";
					var Child1unit = "";
					var Child2unit = "";
					var Child3unit = "";
					var Child4unit = "";
					for(var k = 0;k < parent.children.length;k++){
						var child = parent.children[k];
						switch(child.id) {
						case "1.1.1": //Simple Apps Server pricing
							Child1volume = parseFloat((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							Child1unit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
							break;
						case "1.1.2": //Medium Apps Server pricing
							Child2volume = parseFloat((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							Child2unit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
							break;
						case "1.1.3": //Complex Apps Server pricing
							Child3volume = parseFloat((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							Child3unit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
							break;
						case "1.1.4": //Very Complex Apps Server pricing
							Child4volume = parseFloat((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							Child4unit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
							break;
						}
					}
					parent.distributedVolume[i].unit = (((Child1volume * Child1unit) + (Child2volume * Child2unit) + (Child3volume * Child3unit) + (Child4volume * Child4unit)) / (Child1volume + Child2volume + Child3volume + Child4volume))
					parent.distributedVolume[i].unit = parent.distributedVolume[i].unit.toFixed(2)
					if(isNaN(parent.distributedVolume[i].unit)){
						parent.distributedVolume[i].unit = 0.00;
					}
				}
			}
			if($scope.viewBy.type == 'revenue'){
				for(var i=0;i< parent.distributedVolume.length;i++){
					var Child1revenue = "";
					var Child2revenue = "";
					var Child3revenue = "";
					var Child4revenue = "";
					for(var k = 0;k < parent.children.length;k++){
						var child = parent.children[k];
						switch(child.id) {
						case "1.1.1": //Simple Apps Server pricing
							Child1revenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
							break;
						case "1.1.2": //Medium Apps Server pricing
							Child2revenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
							break;
						case "1.1.3": //Complex Apps Server pricing
							Child3revenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
							break;
						case "1.1.4": //Very Complex Apps Server pricing
							Child4revenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
							break;
						}
					}
					parent.distributedVolume[i].revenue = Math.round(Child1revenue + Child2revenue + Child3revenue + Child4revenue);
					if(isNaN(parent.distributedVolume[i].revenue)){
						parent.distributedVolume[i].revenue = 0;
					}
				}
			}
		}
	}

	// on change of parent volume
    $scope.calcVolume = function(node) {
    	if($scope.selectedSolutionId != null && $scope.selectedSolutionId != undefined &&  $scope.selectedSolutionId != ''){
    		calculateAppVolume(node);
    	}
    }

	// when user change the percentage
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

	// function for setting the custom value
	$scope.setCustom=function()
	{
		$scope.custom=true;
		var CustomSolutionId=_.where($scope.solList, {solutionName: 'Custom'})[0];
		$scope.selectedSolutionId=CustomSolutionId.solutionId;
	}

	// User change volume
	$scope.onChangevolume = function(){
		$scope.custom=false
		//$scope.changeChildToParent();
		for(var i=0;i< $scope.appInfo.total.children[0].children.length;i++){
			$scope.appInfo.total.children[0].children[i].percentage = "";
		}
		for (var y = 0; y < $scope.dealInfo/12; y++) {
    		if(($scope.appInfo.total.children[0].distributedVolume[y].volume)!=parseInt(($scope.appInfo.total.children[0].children[0].distributedVolume[y].volume))
    	    		+parseInt(($scope.appInfo.total.children[0].children[1].distributedVolume[y].volume))
    	    		+ parseInt(($scope.appInfo.total.children[0].children[2].distributedVolume[y].volume))
    	    		+parseInt(($scope.appInfo.total.children[0].children[3].distributedVolume[y].volume))){
    			$scope.showVolumeErr=true;
    			return;
    		}else
    		{
    			$scope.showVolumeErr=false;
    		}

    	}
		$scope.setCustom();
		$scope.calcServerPrice();
	}

	// User change App distribution setting
	$scope.onChangeDistSetting = function(solId) {
		$scope.showErr=false;
		$scope.showVolumeErr=false;
    	$scope.selectedSolutionId = solId;
    	$scope.solution = getSelectedAppSolutionObject(solId);
    	setLevelWiseAppPercentage($scope.appInfo.total);
    };

    //percentage set when App distribution change
    function setLevelWiseAppPercentage(parent) {
    	if(parent != null) {
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

    //Volume calculated with this function
    function calculateAppVolume(parent) {
    	if(parent.children != null){
    		for(var k = 0;k < parent.children.length;k++) {
    			var child = parent.children[k];
        		for (var i = 0; i < $scope.dealInfo/12; i++){
        			child.distributedVolume[i].volume = Math.round((parent.distributedVolume[i].volume * child.percentage)/100);
        		}
    		}
    		//$scope.changeChildToParent();
    		$scope.calcServerPrice();
    	}
    }

    // Get list of App distribution
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

    // Save AppSub form data
    $scope.saveAppSubInfo = function(){
    	setAppYearlyInfo();
    	$scope.appInfoDto = {
    			selectedAppSolution : $scope.selectedSolutionId,
    			offshoreAllowed : $scope.dealDetails.offshoreSelected == "Yes" ? true : false,
    	    	levelOfService :  $scope.dealDetails.standardwindowInfoSelected,
    	    	appYearlyDataInfoDtoList : $scope.appInfo.appYearlyDataInfoDtos,
    	    	levelIndicator:($scope.appInfo.total.children[0].open)?1:0,
    	    	dealId : $stateParams.dealId,
    	    	towerArchitect : $scope.dealDetails.towerArchitect
    	}

    	var url = endpointsurls.SAVE_APP_INFO+$stateParams.dealId;
    	if(!$scope.appInfo.total.children[0].open){$confirm({text: 'Default distribution setting has been applied to child levels'})
 	        .then(function() {
        customInterceptor.postrequest(url,$scope.appInfoDto).then(function successCallback(response) {
        	$scope.isSaveApps= DealService.getter() || isSave;
        	$scope.isSaveApps.appsS=true;
    		DealService.setter($scope.isSaveApps);
    		$scope.putIndicator();
    		
		}, function errorCallback(response) {
			console.log(response.statusText);
			$alert({title:'Error',text: 'Failed to save data.'})
		});})}else
			{
			customInterceptor.postrequest(url,$scope.appInfoDto).then(function successCallback(response) {
	        	$scope.isSaveApps= DealService.getter() || isSave;
	        	$scope.isSaveApps.appsS=true;
	    		DealService.setter($scope.isSaveApps);
	    		$scope.putIndicator();
	    		
			}, function errorCallback(response) {
				console.log(response.statusText);
				$alert({title:'Error',text: 'Failed to save data.'})
			});
			};
    }

  //putIndicator
    $scope.putIndicator=function()
    {
    	$scope.submissionIndicator[6]=1;
        var urlIndicator= endpointsurls.GENERic_INDICATOR+$stateParams.dealId+'?assessmentIndicator='+$scope.assessmentIndicator+'&submissionIndicator='+$scope.submissionIndicator.join(',');
       customInterceptor.putrequestWithoutData(urlIndicator).then(function successCallback(response) {
    	   $state.go('home.submission.genericSubmission.totalsSubmission',({dealId:$stateParams.dealId}));
       });
    }
    // Extract data yearly wise
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
    		yearlyData.appUnitPriceInfoDtoList= extractServerUnitPrice($scope.appInfo.total.children[0],y);
    		yearlyData.applicationRevenueInfoDtoList = extractServerRevenuePrice($scope.appInfo.total.children[0],y);
    		yearlyInfoList.push(yearlyData);
    	}
    	$scope.appInfo.appYearlyDataInfoDtos = yearlyInfoList;
    }

    // extract server price in units
    function extractServerUnitPrice(parent,year){
    	var unitPrice = [];
    	var unitInfo={};

    	if($scope.viewBy.type == 'unit'){
    		unitInfo.totalAppsUnitPrice = $scope.appInfo.total.children[0].distributedVolume[year].unit;

	    	for(var k = 0;k < parent.children.length;k++){
	    		child = parent.children[k];
				switch(child.id) {
				case "1.1": //total
					break;
				case "1.1.1": //Simple Apps Server pricing
					unitInfo.simpleAppsUnitPrice = child.distributedVolume[year].unit;
					break;
				case "1.1.2": //Medium Apps Server pricing
					unitInfo.mediumAppsUnitPrice = child.distributedVolume[year].unit;
					break;
				case "1.1.3": //Complex Apps Server pricing
					unitInfo.complexAppsUnitPrice = child.distributedVolume[year].unit;
					break;
				case "1.1.4": //Very Complex Apps Server pricing
					unitInfo.veryComplexAppsUnitPrice = child.distributedVolume[year].unit;
					break;

				}
	    	}
    	}
    	if($scope.viewBy.type == 'revenue'){
    		unitInfo.totalAppsUnitPrice = $scope.appInfo.total.children[0].distributedVolume[year].revenue / $scope.appInfo.total.children[0].distributedVolume[year].volume;

	    	for(var k = 0;k < parent.children.length;k++){
	    		child = parent.children[k];
				switch(child.id) {
				case "1.1": //total
					break;
				case "1.1.1": //Simple Apps Server pricing
					unitInfo.simpleAppsUnitPrice = child.distributedVolume[year].revenue / child.distributedVolume[year].volume;
					break;
				case "1.1.2": //Medium Apps Server pricing
					unitInfo.mediumAppsUnitPrice = child.distributedVolume[year].revenue / child.distributedVolume[year].volume;
					break;
				case "1.1.3": //Complex Apps Server pricing
					unitInfo.complexAppsUnitPrice = child.distributedVolume[year].revenue / child.distributedVolume[year].volume;
					break;
				case "1.1.4": //Very Complex Apps Server pricing
					unitInfo.veryComplexAppsUnitPrice = child.distributedVolume[year].revenue / child.distributedVolume[year].volume;
					break;

				}
	    	}
    	}
    	unitPrice.push(unitInfo)
    	return unitPrice;
    }

    // extract server price in revenue
    function extractServerRevenuePrice(parent,year){
    	var revenue = [];
    	var revenueInfo={};

    	if($scope.viewBy.type == 'unit'){
    		revenueInfo.totalAppsRevenue = Math.round(parent.distributedVolume[year].unit * parent.distributedVolume[year].volume) * 12;
    	}
    	if($scope.viewBy.type == 'revenue'){
    		revenueInfo.totalAppsRevenue = parent.distributedVolume[year].revenue * 12;
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

