priceToolcontrollers.controller('ServiceDeskSubCtrl',function($scope,$http,HostingService,EndUserService,StorageService,DealService,ServiceDeskService,customInterceptor,endpointsurls,$stateParams,$dialog,$confirm,$state) {
	
	//initialize all the variable needed for the page.
	
	
	var ctrl = this;
	ctrl.active = 0;
	$scope.dealInfo={};
	$scope.hostingInfo = {};
	$scope.dealDetails = {};
    $scope.contactDistList = {};
    $scope.selectedContactId = {};
    $scope.solution = {};
    $scope.viewBy = {type:'unit'};
    $scope.servicedeskInfoDto = {};
	$scope.existingServiceDeskInfo = {};
	$scope.serviceDeskCalculateDto = {};
	$scope.userChecked = false;
	$scope.lastModContactVol = [];
	$scope.showErr=false;
	$scope.showVolumeErr=false;
	$scope.custom=false;

	$scope.contactDetail = {
			open : false
	};

	// function for toggle panel
	$scope.togglePanel = function(level) {
		if(level.open){
			level.open=false;
		}else{
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

	// Get list of Service Desk distribution
    function getSelectedServiceDeskSolutionObject(solId) {
    	var sol = {};
    	for(var i = 0; i < $scope.solList.length; i++ ) {
    		if($scope.solList[i].solutionId == solId) {
    			sol = $scope.solList[i];
    			break;
    		}
    	}
    	return sol;
    }

	// function for catching tab event solution
	$scope.tabEvent=function($event,index,val,childindex){
		if($event.keyCode == 13 || $event.keyCode == 9){
			switch(val) {
			case "contact":
				for(var i=index;i<$scope.serviceDeskInfo.contact.children[0].distributedVolume.length;i++){
					$scope.serviceDeskInfo.contact.children[0].distributedVolume[i].volume = $scope.serviceDeskInfo.contact.children[0].distributedVolume[index].volume;
				}
				break;
			case "parentpricingrevenue":
				for(var i=index;i<$scope.serviceDeskInfo.contact.children[0].distributedVolume.length;i++){
					$scope.serviceDeskInfo.contact.children[0].distributedVolume[i].revenue = $scope.serviceDeskInfo.contact.children[0].distributedVolume[index].revenue;
				}
				break;
			case "parentpricingunit":
				for(var i=index;i<$scope.serviceDeskInfo.contact.children[0].distributedVolume.length;i++){
					$scope.serviceDeskInfo.contact.children[0].distributedVolume[i].unit = $scope.serviceDeskInfo.contact.children[0].distributedVolume[index].unit;
				}
				break;
			case "childpricingrevenue":
				for(var i=index;i<$scope.serviceDeskInfo.contact.children[0].distributedVolume.length;i++){
					$scope.serviceDeskInfo.contact.children[0].children[childindex].distributedVolume[i].revenue = $scope.serviceDeskInfo.contact.children[0].children[childindex].distributedVolume[index].revenue
				}
				break;
			case "childpricingunit":
				for(var i=index;i<$scope.serviceDeskInfo.contact.children[0].distributedVolume.length;i++){
					$scope.serviceDeskInfo.contact.children[0].children[childindex].distributedVolume[i].unit = $scope.serviceDeskInfo.contact.children[0].children[childindex].distributedVolume[index].unit
				}
				break;
			}
		}
	}



	// get the drop-down values
	$scope.getServiceDeskDropdowns=function(){
		var url = endpointsurls.SERVICE_DROPDOWN+$stateParams.dealId;
        customInterceptor.getrequest(url).then(function successCallback(response) {
			$scope.solList = response.data.serviceDeskSolutionsDtoList;
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
	        $scope.serviceDeskInfo = ServiceDeskService.initServiceDeskDetails($scope.dealInfo);
	        console.log($scope.serviceDeskInfo);
	        if($stateParams.dealId>0){
	        	var url = endpointsurls.SAVE_SERVICE_INFO+ $stateParams.dealId;
	        	customInterceptor.getrequest(url).then(function successCallback(response) {
		        	$scope.existingServiceDeskInfo = response.data;
		       		$scope.id=response.data.id
		       		$scope.level=$scope.existingServiceDeskInfo.levelIndicator;
		       		$scope.serviceDeskInfo.contact.open=true;
		       		$scope.serviceDeskInfo.contact.children[0].open=($scope.level==1)?true:false;
		       		if($scope.existingServiceDeskInfo != null){
		       			mapExistingServiceDeskInfo();
		       		}
	        	}, function errorCallback(response) {
	        	$scope.serviceDeskId=undefined;
	        	});
	        }
	}

    $scope.init=function(){
		$scope.getServiceDeskDropdowns();
	}
	$scope.init();

	// map existing Service Desk Info
    function mapExistingServiceDeskInfo() {
    	$scope.dealDetails.offshoreSelected= $scope.existingServiceDeskInfo.offshoreAllowed == true ? "Yes" : "No";
	    $scope.dealDetails.standardwindowInfoSelected= $scope.existingServiceDeskInfo.levelOfService;
   		$scope.dealDetails.towerArchitect = $scope.existingServiceDeskInfo.towerArchitect;
	    $scope.serviceDeskInfo.multiLingual = $scope.existingServiceDeskInfo.multiLingual == true ? "Yes" : "No";
	    $scope.serviceDeskInfo.tooling = $scope.existingServiceDeskInfo.toolingIncluded == true ? "Yes" : "No";
    	$scope.selectedSolutionId = $scope.existingServiceDeskInfo.selectedContactSolution;
    	if($scope.selectedSolutionId != null){
   			var CustomSolutionName = _.where($scope.solList, {solutionId: $scope.selectedSolutionId})[0].solutionName;
   	   		if(CustomSolutionName != 'Custom'){
   	   			$scope.onChangeDistSetting($scope.selectedSolutionId);
   	   		}
   		}
    	getExistingServiceDeskYearlyInfo();
    }

    // map Existing Service Desk Yearly Info
    function getExistingServiceDeskYearlyInfo() {
    	if($scope.existingServiceDeskInfo.serviceDeskYearlyDataInfoDtoList != null){
    		for (var y = 0; y < $scope.existingServiceDeskInfo.serviceDeskYearlyDataInfoDtoList.length; y++){
        		var yearlyDto = $scope.existingServiceDeskInfo.serviceDeskYearlyDataInfoDtoList[y];
        		$scope.serviceDeskInfo.contact.children[0].distributedVolume[y].year = yearlyDto.year;
        		$scope.serviceDeskInfo.contact.children[0].distributedVolume[y].volume = yearlyDto.totalContacts;
        		getExistingVolumeLevelWiseInfo($scope.serviceDeskInfo.contact.children[0],yearlyDto,y);
        		getExistingServerPricingLevelWiseInfo($scope.serviceDeskInfo.contact.children[0],yearlyDto,y);
        	}
    	}
    }

    // getExisting Service Desk Volume LevelWise Info
    function getExistingVolumeLevelWiseInfo(parent,yearlyDto,year){
    	var child = {};
    	if(parent.children != null){
    		for(var k = 0;k < parent.children.length;k++){
    			child = parent.children[k];
    			switch(child.id) {
					case "1.1": //contact
						break;
					case "1.1.1": //Voice Volume
						child.distributedVolume[year].volume = yearlyDto.voiceContacts;
						break;
					case "1.1.2": //Mail Volume
						child.distributedVolume[year].volume = yearlyDto.mailContacts;
						break;
					case "1.1.3": //Chat Volume
						child.distributedVolume[year].volume = yearlyDto.chatContacts;
						break;
					case "1.1.4": //Portal Volume
						child.distributedVolume[year].volume = yearlyDto.portalContacts;
						break;
    			}
    		}
    	}
    }

	// Get existing Service Desk server price level wise
	function getExistingServerPricingLevelWiseInfo(parent,yearlyDto,year){
		if(yearlyDto.serviceDeskUnitPriceInfoDtoList != null){
			if(yearlyDto.serviceDeskUnitPriceInfoDtoList.length > 0){
				  if($scope.viewBy.type == 'unit'){
					  $scope.serviceDeskInfo.contact.children[0].distributedVolume[year].unit = yearlyDto.serviceDeskUnitPriceInfoDtoList[0].totalContactsUnitPrice;
					  for(var k = 0;k < parent.children.length;k++){
						child = parent.children[k];
			  			switch(child.id) {
			  			case "1.1": //contact
							break;
						case "1.1.1": //Voice Server pricing
							child.distributedVolume[year].unit = yearlyDto.serviceDeskUnitPriceInfoDtoList[0].voiceContactsUnitPrice;
							break;
						case "1.1.2": //Mail Server pricing
							child.distributedVolume[year].unit = yearlyDto.serviceDeskUnitPriceInfoDtoList[0].mailContactsUnitPrice;
							break;
						case "1.1.3": //Chat Server pricing
							child.distributedVolume[year].unit = yearlyDto.serviceDeskUnitPriceInfoDtoList[0].chatContactsUnitPrice;
							break;
						case "1.1.4": //Portal Server pricing
							child.distributedVolume[year].unit = yearlyDto.serviceDeskUnitPriceInfoDtoList[0].portalContactsUnitPrice;
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
			if($scope.serviceDeskInfo.contact.children[0].open == true){
				for(var i=0;i< $scope.serviceDeskInfo.contact.children[0].distributedVolume.length;i++){
					var Child1revenue = "";
					var Child2revenue = "";
					var Child3revenue = "";
					var Child4revenue = "";
					for(var k = 0;k < $scope.serviceDeskInfo.contact.children[0].children.length;k++){
						child = $scope.serviceDeskInfo.contact.children[0].children[k];
		    			switch(child.id) {
		    			case "1.1": //contact
							break;
						case "1.1.1": //Voice Server pricing
							child.distributedVolume[i].revenue = Math.round(((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume)  * ((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit));
							if(isNaN(child.distributedVolume[i].revenue)){
								child.distributedVolume[i].revenue = 0;
							}
							Child1revenue = child.distributedVolume[i].revenue;
							break;
						case "1.1.2": //Mail Server pricing
							child.distributedVolume[i].revenue = Math.round(((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume)  * ((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit));
							if(isNaN(child.distributedVolume[i].revenue)){
								child.distributedVolume[i].revenue = 0;
							}
							Child2revenue = child.distributedVolume[i].revenue;
							break;
						case "1.1.3": //Chat Server pricing
							child.distributedVolume[i].revenue = Math.round(((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume)  * ((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit));
							if(isNaN(child.distributedVolume[i].revenue)){
								child.distributedVolume[i].revenue = 0;
							}
							Child3revenue = child.distributedVolume[i].revenue;
							break;
						case "1.1.4": //Portal Server pricing
							child.distributedVolume[i].revenue = Math.round(((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume)  * ((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit));
							if(isNaN(child.distributedVolume[i].revenue)){
								child.distributedVolume[i].revenue = 0;
							}
							Child4revenue = child.distributedVolume[i].revenue;
							break;
		    			}
					}
					$scope.serviceDeskInfo.contact.children[0].distributedVolume[i].revenue = Math.round(Child1revenue + Child2revenue + Child3revenue + Child4revenue);
				}
			}
			else{
				for(var i=0;i< $scope.serviceDeskInfo.contact.children[0].distributedVolume.length;i++){
					$scope.serviceDeskInfo.contact.children[0].distributedVolume[i].revenue = Math.round($scope.serviceDeskInfo.contact.children[0].distributedVolume[i].volume * $scope.serviceDeskInfo.contact.children[0].distributedVolume[i].unit);
					if(isNaN($scope.serviceDeskInfo.contact.children[0].distributedVolume[i].revenue)){
						$scope.serviceDeskInfo.contact.children[0].distributedVolume[i].revenue = 0;
					}
				}
			}
		}
		if(value == 'unit'){
			if($scope.serviceDeskInfo.contact.children[0].open == true){
				for(var i=0;i< $scope.serviceDeskInfo.contact.children[0].distributedVolume.length;i++){
					for(var k = 0;k < $scope.serviceDeskInfo.contact.children[0].children.length;k++){
						child = $scope.serviceDeskInfo.contact.children[0].children[k];
		    			switch(child.id) {
		    			case "1.1": //contact
							break;
						case "1.1.1": //voice Server pricing
							child.distributedVolume[i].unit = ((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue) / ((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							if(isNaN(child.distributedVolume[i].unit)){
								child.distributedVolume[i].unit = 0;
							}
							child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
							break;
						case "1.1.2": //Mail Server pricing
							child.distributedVolume[i].unit = ((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue) / ((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							if(isNaN(child.distributedVolume[i].unit)){
								child.distributedVolume[i].unit = 0;
							}
							child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
							break;
						case "1.1.3": //Chat Server pricing
							child.distributedVolume[i].unit = ((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue) / ((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							if(isNaN(child.distributedVolume[i].unit)){
								child.distributedVolume[i].unit = 0;
							}
							child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
							break;
						case "1.1.4": //Portal Server pricing
							child.distributedVolume[i].unit = ((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue) / ((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							if(isNaN(child.distributedVolume[i].unit)){
								child.distributedVolume[i].unit = 0;
							}
							child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
							break;
		    			}
					}
					$scope.serviceDeskInfo.contact.children[0].distributedVolume[i].unit = ($scope.serviceDeskInfo.contact.children[0].distributedVolume[i].revenue / $scope.serviceDeskInfo.contact.children[0].distributedVolume[i].volume).toFixed(2);
					if(isNaN($scope.serviceDeskInfo.contact.children[0].distributedVolume[i].unit)){
						$scope.serviceDeskInfo.contact.children[0].distributedVolume[i].unit = 0.00;
					}
				}
			}
			else{
				for(var i=0;i< $scope.serviceDeskInfo.contact.children[0].distributedVolume.length;i++){
					$scope.serviceDeskInfo.contact.children[0].distributedVolume[i].unit = ($scope.serviceDeskInfo.contact.children[0].distributedVolume[i].revenue / $scope.serviceDeskInfo.contact.children[0].distributedVolume[i].volume).toFixed(2);
					if(isNaN($scope.serviceDeskInfo.contact.children[0].distributedVolume[i].unit)){
						$scope.serviceDeskInfo.contact.children[0].distributedVolume[i].unit = 0.00;
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
	  				case "1.1": //Contacts
	  					break;
	  				case "1.1.1": //voice Server pricing
	  						child.distributedVolume[i].revenue = '';
	    				break;
	    			case "1.1.2": //Mail Server pricing
	    					child.distributedVolume[i].revenue = '';
	    				break;
	    			case "1.1.3": //Chat Server pricing
    					child.distributedVolume[i].revenue = '';
    					break;
	    			case "1.1.4": //Portal Server pricing
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
	  				case "1.1": //Contacts
	  					break;
	  				case "1.1.1": //voice Server pricing
	  						child.distributedVolume[i].unit = '';
	    				break;
	    			case "1.1.2": //Mail Server pricing
	    					child.distributedVolume[i].unit = '';
	    				break;
	    			case "1.1.3": //Chat Server pricing
    					child.distributedVolume[i].revenue = '';
    					break;
	    			case "1.1.4": //Portal Server pricing
    					child.distributedVolume[i].revenue = '';
    					break;
	    			}
				}
			}
		}
	}

	//on change parent Volume
	$scope.calcVolume = function(node) {
		if($scope.selectedSolutionId != null && $scope.selectedSolutionId != undefined &&  $scope.selectedSolutionId != ''){
    		calculateServiceDeskVolume(node);
    	}
    }

	// calculate parent server price when user insert child server pricing
	$scope.calcServerPrice = function (){
		var parent = $scope.serviceDeskInfo.contact.children[0];
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
					case "1.1.1": //voice Server pricing
						Child1volume = parseFloat((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
						Child1unit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
						break;
					case "1.1.2": //Mail Server pricing
						Child2volume = parseFloat((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
						Child2unit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
						break;
					case "1.1.3": //Chat Server pricing
						Child3volume = parseFloat((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
						Child3unit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
						break;
					case "1.1.4": //Portal Server pricing
						Child4volume = parseFloat((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
						Child4unit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
						break;
					}
				}
				parent.distributedVolume[i].unit = (((Child1volume * Child1unit) + (Child2volume * Child2unit) + (Child3volume * Child3unit) + (Child4volume * Child4unit)) / (Child1volume + Child2volume + Child3volume + Child4volume))
				if(isNaN(parent.distributedVolume[i].unit)){
					parent.distributedVolume[i].unit = 0;
				}
				parent.distributedVolume[i].unit = parent.distributedVolume[i].unit.toFixed(2)
			}
		}
		if($scope.viewBy.type == 'revenue'){
			for(var i=0;i< parent.distributedVolume.length;i++){
				//parent.distributedVolume[i].unit = child
				var Child1revenue = "";
				var Child2revenue = "";
				var Child3revenue = "";
				var Child4revenue = "";
				for(var k = 0;k < parent.children.length;k++){
					var child = parent.children[k];
					switch(child.id) {
					case "1.1.1": //voice Server pricing
						Child1revenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
						break;
					case "1.1.2": //Mail Server pricing
						Child2revenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
						break;
					case "1.1.3": //Chat Server pricing
						Child3revenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
						break;
					case "1.1.4": //Portal Server pricing
						Child4revenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
						break;
					}
				}
				parent.distributedVolume[i].revenue = Math.round(Child1revenue + Child2revenue + Child3revenue + Child4revenue)
			}
		}

	}

	// when user change the percentage
	$scope.onChangeServiceDeskPercentage=function(child){
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
				$scope.setCustom();
				calculateServiceDeskVolume($scope.serviceDeskInfo.contact.children[0]);
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
		for (var y = 0; y < $scope.dealInfo/12; y++) {
    		if(parseInt($scope.serviceDeskInfo.contact.children[0].distributedVolume[y].volume)!=parseInt($scope.serviceDeskInfo.contact.children[0].children[0].distributedVolume[y].volume)
    		+parseInt($scope.serviceDeskInfo.contact.children[0].children[1].distributedVolume[y].volume)
    		+ parseInt($scope.serviceDeskInfo.contact.children[0].children[2].distributedVolume[y].volume)
    		+parseInt($scope.serviceDeskInfo.contact.children[0].children[3].distributedVolume[y].volume)){
    			$scope.showVolumeErr=true;
    			return;
    		}
    	}
		for(var i=0;i< $scope.serviceDeskInfo.contact.children[0].children.length;i++){
			$scope.serviceDeskInfo.contact.children[0].children[i].percentage = "";
		}
		$scope.showVolumeErr=false;
		$scope.setCustom();
		$scope.calcServerPrice();
	}

	// User change Contact distribution setting
	$scope.onChangeDistSetting = function(solId) {
    	$scope.selectedSolutionId = solId;
    	$scope.solution = getSelectedServiceDeskSolutionObject(solId);
    	setLevelWiseServiceDeskPercentage($scope.serviceDeskInfo.contact);
    	$scope.showErr=false;
    	$scope.showVolumeErr=false;
    };

    //percentage set when Contact distribution change
    function setLevelWiseServiceDeskPercentage(parent) {
    	if(parent != null) {
    		for(var k = 0;k < parent.children[0].children.length;k++) {
    			var child = parent.children[0].children[k];
    			switch(child.id) {
	    			case "1.1.1":
	    				child.percentage = $scope.solution ? $scope.solution.voicePerc : "";
	    				break;
	    			case "1.1.2":
	    				child.percentage = $scope.solution ? $scope.solution.mailPerc : "";
	    				break;
	    			case "1.1.3":
	    				child.percentage = $scope.solution ? $scope.solution.chatPerc : "";
	    				break;
	    			case "1.1.4":
	    				child.percentage = $scope.solution ? $scope.solution.portalPerc : "";
	    				break;
	        	}
    		}
    	}
    	calculateServiceDeskVolume($scope.serviceDeskInfo.contact.children[0]);
    }

    //Volume calculated with this function
    function calculateServiceDeskVolume(parent) {
    	if(parent.children != null){
    		for(var k = 0;k < parent.children.length;k++) {
    			var child = parent.children[k];
        		for (var i = 0; i < $scope.dealInfo/12; i++){
        			child.distributedVolume[i].volume = Math.round((parent.distributedVolume[i].volume * child.percentage)/100);
        		}
    		}
    		$scope.calcServerPrice();
    	}
    }

    // Save Service Desk Sub form data
    $scope.saveServiceSubInfo = function(){
    	setServiceDeskYearlyInfo();
    	$scope.servicedeskInfoDto = {
    			selectedContactSolution : $scope.selectedSolutionId,
    			offshoreAllowed : $scope.dealDetails.offshoreSelected == "Yes" ? true : false,
    			levelOfService :  $scope.dealDetails.standardwindowInfoSelected,
    			multiLingual: $scope.serviceDeskInfo.multiLingual == "Yes" ? true : false,
    			toolingIncluded: $scope.serviceDeskInfo.tooling == "Yes" ? true : false,
    			serviceDeskYearlyDataInfoDtoList : $scope.serviceDeskInfo.serviceDeskYearlyDataInfoDtos,
    	    	levelIndicator:($scope.serviceDeskInfo.contact.children[0].open)?1:0,
    	    	dealId : $stateParams.dealId,
    	    	towerArchitect : $scope.dealDetails.towerArchitect
    	}
    	if(!$scope.serviceDeskInfo.contact.children[0].open){$confirm({text: 'Default distribution setting has been applied to child levels'})
 	        .then(function() {
    	var url = endpointsurls.SAVE_SERVICE_INFO+$stateParams.dealId;
        customInterceptor.postrequest(url,$scope.servicedeskInfoDto).then(function successCallback(response) {
        	$scope.isSaveServiceDesk= DealService.getter() || isSave;
        	$scope.isSaveServiceDesk.serviceDeskS=true;
    		DealService.setter($scope.isSaveServiceDesk);
    		$scope.putIndicator();
    		
		}, function errorCallback(response) {
			console.log(response.statusText);
			$alert({title:'Error',text: 'Failed to save data.'})
		});})}{
			var url = endpointsurls.SAVE_SERVICE_INFO+$stateParams.dealId;
	        customInterceptor.postrequest(url,$scope.servicedeskInfoDto).then(function successCallback(response) {
	        	$scope.isSaveServiceDesk= DealService.getter() || isSave;
	        	$scope.isSaveServiceDesk.serviceDeskS=true;
	    		DealService.setter($scope.isSaveServiceDesk);
	    		$scope.putIndicator();
	    		
			}, function errorCallback(response) {
				console.log(response.statusText);
				$alert({title:'Error',text: 'Failed to save data.'})});
		};
    }

    
  //putIndicator
    $scope.putIndicator=function()
    {
    	$scope.submissionIndicator[5]=1;
        var urlIndicator= endpointsurls.GENERic_INDICATOR+$stateParams.dealId+'?assessmentIndicator='+$scope.assessmentIndicator+'&submissionIndicator='+$scope.submissionIndicator.join(',');
       customInterceptor.putrequestWithoutData(urlIndicator).then(function successCallback(response) {
    	   $state.go('home.submission.genericSubmission.totalsSubmission',({dealId:$stateParams.dealId}));
       });
    }
    // Extract data yearly wise
    function setServiceDeskYearlyInfo() {
    	var yearlyInfoList = [];
    	for (var y = 0; y < $scope.dealInfo/12; y++) {
    		var yearlyData = {};
    		yearlyData.year = y+1;
    		yearlyData.totalContacts = $scope.serviceDeskInfo.contact.children[0].distributedVolume[y].volume;
    		yearlyData.voiceContacts = $scope.serviceDeskInfo.contact.children[0].children[0].distributedVolume[y].volume;
    		yearlyData.mailContacts = $scope.serviceDeskInfo.contact.children[0].children[1].distributedVolume[y].volume;
    		yearlyData.chatContacts = $scope.serviceDeskInfo.contact.children[0].children[2].distributedVolume[y].volume;
    		yearlyData.portalContacts = $scope.serviceDeskInfo.contact.children[0].children[3].distributedVolume[y].volume;
    		yearlyData.serviceDeskUnitPriceInfoDtoList= extractServerUnitPrice($scope.serviceDeskInfo.contact.children[0],y);
    		yearlyData.serviceDeskRevenueInfoDtoList = extractServerRevenuePrice($scope.serviceDeskInfo.contact.children[0],y);
    		yearlyInfoList.push(yearlyData);
    	}
    	$scope.serviceDeskInfo.serviceDeskYearlyDataInfoDtos = yearlyInfoList;
    }

    // extract server price in units
    function extractServerUnitPrice(parent,year){
    	var unitPrice = [];
    	var unitInfo={};

    	if($scope.viewBy.type == 'unit'){
    		unitInfo.totalContactsUnitPrice = $scope.serviceDeskInfo.contact.children[0].distributedVolume[year].unit;

	    	for(var k = 0;k < parent.children.length;k++){
	    		child = parent.children[k];
				switch(child.id) {
				case "1.1": //Contact
					break;
				case "1.1.1": //Voice Server pricing
					unitInfo.voiceContactsUnitPrice = child.distributedVolume[year].unit;
					break;
				case "1.1.2": //Mail Server pricing
					unitInfo.mailContactsUnitPrice = child.distributedVolume[year].unit;
					break;
				case "1.1.3": //Chat Server pricing
					unitInfo.chatContactsUnitPrice = child.distributedVolume[year].unit;
					break;
				case "1.1.4": //Portal Apps Server pricing
					unitInfo.portalContactsUnitPrice = child.distributedVolume[year].unit;
					break;

				}
	    	}
    	}
    	if($scope.viewBy.type == 'revenue'){
    		unitInfo.totalContactsUnitPrice = $scope.serviceDeskInfo.contact.children[0].distributedVolume[year].revenue / $scope.serviceDeskInfo.contact.children[0].distributedVolume[year].volume;

	    	for(var k = 0;k < parent.children.length;k++){
	    		child = parent.children[k];
				switch(child.id) {
				case "1.1": //Contact
					break;
				case "1.1.1": //Voice Server pricing
					unitInfo.voiceContactsUnitPrice = child.distributedVolume[year].revenue / child.distributedVolume[year].volume;
					break;
				case "1.1.2": //Mail Server pricing
					unitInfo.mailContactsUnitPrice = child.distributedVolume[year].revenue / child.distributedVolume[year].volume;
					break;
				case "1.1.3": //Chat Server pricing
					unitInfo.chatContactsUnitPrice = child.distributedVolume[year].revenue / child.distributedVolume[year].volume;
					break;
				case "1.1.4": //Portal Server pricing
					unitInfo.portalContactsUnitPrice = child.distributedVolume[year].revenue / child.distributedVolume[year].volume;
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
    		revenueInfo.totalContactsRevenue = Math.round(parent.distributedVolume[year].unit * parent.distributedVolume[year].volume) * 12;
    	}
    	if($scope.viewBy.type == 'revenue'){
    		revenueInfo.totalContactsRevenue = parent.distributedVolume[year].revenue * 12;
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

