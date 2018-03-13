priceToolcontrollers.controller('EndUserSubCtrl',function($scope,$http,HostingService,EndUserService,StorageService,DealService,NetworkService,customInterceptor,endpointsurls,$stateParams,$alert,$dialog,$state, $confirm) {

	$scope.hostingInfo = {};
	var ctrl = this;
	ctrl.active = 0;
    $scope.dealInfo={};
	$scope.pastAvgUnitPriceList=[];
	$scope.compAvgUnitPriceList=[];
	$scope.benchmarkLowAvgUnitPriceList=[];
	$scope.benchmarkTargetAvgUnitPriceList=[];
	$scope.storageCalculateDto = [];
	$scope.selectedSolutionId = '';
	$scope.avgPriceList=[];
	$scope.hostingChecked = false;
	$scope.enduserChecked = false;
	$scope.endUserInfoDto={};
	$scope.dummy = [0];
	$scope.user = false;
	$scope.customSol = false;
	$scope.customImac = false;
	$scope.viewBy = {type:'unit'};
	$scope.solList = [];
	$scope.solution = {};
	$scope.dealDetails={};
	$scope.showmsg = false;
	$scope.showvalidationmsg = false;
	$scope.showErrpercentage=false;
	$scope.showchildVolumeErr=false;


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

	// function for catching tab event solution
	$scope.tabEvent=function($event,index,val,childindex,grandchildIndex){
		if($event.keyCode == 13 || $event.keyCode == 9){
			switch(val) {
			case "EndUser":
				for(var i=index;i<$scope.endUserInfo.user.children[0].distributedVolume.length;i++){
					$scope.endUserInfo.user.children[0].distributedVolume[i].volume = $scope.endUserInfo.user.children[0].distributedVolume[index].volume;
				}
				break;
			case "EndUserPricingRevenue":
				for(var i=index;i<$scope.endUserInfo.user.children[0].distributedVolume.length;i++){
					$scope.endUserInfo.user.children[0].distributedVolume[i].revenue = $scope.endUserInfo.user.children[0].distributedVolume[index].revenue;
				}
				break;
			case "EndUserPricingUnit":
				for(var i=index;i<$scope.endUserInfo.user.children[0].distributedVolume.length;i++){
					$scope.endUserInfo.user.children[0].distributedVolume[i].unit = $scope.endUserInfo.user.children[0].distributedVolume[index].unit;
				}
				break;
			case "EndUserChildPricingRevenue":
				for(var i=index;i<$scope.endUserInfo.user.children[0].distributedVolume.length;i++){
					$scope.endUserInfo.user.children[0].children[childindex].distributedVolume[i].revenue = $scope.endUserInfo.user.children[0].children[childindex].distributedVolume[index].revenue;
				}
				break;
			case "EndUserChildPricingUnit":
				for(var i=index;i<$scope.endUserInfo.user.children[0].distributedVolume.length;i++){
					$scope.endUserInfo.user.children[0].children[childindex].distributedVolume[i].unit = $scope.endUserInfo.user.children[0].children[childindex].distributedVolume[index].unit;
				}
				break;
			case "LaptopPricingRevenue":
				for(var i=index;i<$scope.endUserInfo.user.children[0].distributedVolume.length;i++){
					$scope.endUserInfo.user.children[0].children[childindex].children[grandchildIndex].distributedVolume[i].revenue = $scope.endUserInfo.user.children[0].children[childindex].children[grandchildIndex].distributedVolume[index].revenue;
				}
				break;
			case "LaptopPricingUnit":
				for(var i=index;i<$scope.endUserInfo.user.children[0].distributedVolume.length;i++){
					$scope.endUserInfo.user.children[0].children[childindex].children[grandchildIndex].distributedVolume[i].unit = $scope.endUserInfo.user.children[0].children[childindex].children[grandchildIndex].distributedVolume[index].unit;
				}
				break;
			case "ImacVolume":
				for(var i=index;i<$scope.endUserInfo.userImac.children[0].distributedVolume.length;i++){
					$scope.endUserInfo.userImac.children[0].distributedVolume[i].volume = $scope.endUserInfo.userImac.children[0].distributedVolume[index].volume;
				}
				break;
			case "ImacPricingRevenue":
				for(var i=index;i<$scope.endUserInfo.userImac.children[0].distributedVolume.length;i++){
					$scope.endUserInfo.userImac.children[0].distributedVolume[i].revenue = $scope.endUserInfo.userImac.children[0].distributedVolume[index].revenue;
				}
				break;
			case "ImacPricingUnit":
				for(var i=index;i<$scope.endUserInfo.userImac.children[0].distributedVolume.length;i++){
					$scope.endUserInfo.userImac.children[0].distributedVolume[i].unit = $scope.endUserInfo.userImac.children[0].distributedVolume[index].unit;
				}
				break;
			}
		}
	}

	// get the drop-down values
	$scope.getEndUserDropdowns=function(){
		var url = endpointsurls.ENDUSER_DROPDOWN+$stateParams.dealId;
        customInterceptor.getrequest(url).then(function successCallback(response) {
			$scope.solList = response.data.endUserSolutionsDtoList;
			$scope.imacList = response.data.imacFactorDtoList;
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
        $scope.dealDetails.hardwareIncludedSelected= $scope.dealInfoDto.includeHardware;
        $scope.dealInfo= $scope.dealInfoDto.dealTerm;
        $scope.endUserInfo = EndUserService.initEndUserDetails($scope.dealInfo);
        console.log($scope.endUserInfo);
        if($stateParams.dealId>0){
			 var url = endpointsurls.SAVE_ENDUSER_INFO+ $stateParams.dealId;
	        customInterceptor.getrequest(url).then(function successCallback(response) {
	        	$scope.existingEndUserInfo = response.data;
	       		$scope.id=response.data.endUserId;
	       		$scope.level=$scope.existingEndUserInfo.levelIndicator.split(',');
	       		$scope.endUserInfo.user.open=($scope.level[0]==1)?true:false;
	       		$scope.endUserInfo.user.children[0].open=($scope.level[1]==1)?true:false,
	       		$scope.endUserInfo.user.children[0].children[0].open=($scope.level[2]==1)?true:false,
	       		$scope.endUserInfo.userImac.open=($scope.level[3]==1)?true:false
	       		if($scope.existingEndUserInfo != null && $scope.id!=null){
	       			mapExistingEndUserInfo();
	       		}
	        }, function errorCallback(response) {
	        	$scope.endUserId=undefined;
	        });

		}
	}


	// function of init End User info
    $scope.init=function() {
		$scope.getEndUserDropdowns();
	}
	$scope.init();

	// map existing End User Info
    function mapExistingEndUserInfo() {
    	$scope.dealDetails.offshoreSelected= $scope.existingEndUserInfo.offshoreAllowed == true ? "Yes" : "No";
    	$scope.dealDetails.hardwareIncludedSelected = $scope.existingEndUserInfo.includeHardware == true ? "Yes" : "No";
    	$scope.dealDetails.breakfixSelected = $scope.existingEndUserInfo.includeBreakFix == true ? "Yes" : "No";
    	$scope.dealDetails.resolutionTimeSelected = $scope.existingEndUserInfo.resolutionTime;
   		$scope.dealDetails.towerArchitect = $scope.existingEndUserInfo.towerArchitect;
   		$scope.selectedSolutionId = $scope.existingEndUserInfo.selectedSolution;
   		if($scope.existingEndUserInfo.imacType != null){
   			$scope.selectedImacId = parseInt($scope.existingEndUserInfo.imacType);
   			var CustomImacName = _.where($scope.imacList, {id: $scope.selectedImacId})[0].factorName;
   			if(CustomImacName != 'Custom'){
   				$scope.onChangeImacintensity($scope.selectedImacId);
   			}
    	}
   		if($scope.selectedSolutionId != null){
   			var CustomSolutionName = _.where($scope.solList, {solutionId: $scope.selectedSolutionId})[0].solutionName;
   			if(CustomSolutionName != 'Custom'){
   				$scope.onChangeDistSetting($scope.selectedSolutionId,'map');
   			}
   		}
   		getExistingEndUserYearlyInfo();
    }

    // map Existing End User Yearly Info
    function getExistingEndUserYearlyInfo() {
    	if($scope.existingEndUserInfo.endUserYearlyDataInfoDtoList != null){
    		for (var y = 0; y < $scope.existingEndUserInfo.endUserYearlyDataInfoDtoList.length; y++){
        		var yearlyDto = $scope.existingEndUserInfo.endUserYearlyDataInfoDtoList[y];
        		$scope.endUserInfo.user.children[0].distributedVolume[y].year = yearlyDto.year;
        		$scope.endUserInfo.user.children[0].distributedVolume[y].volume = yearlyDto.endUserDevices;
        		$scope.endUserInfo.user.children[0].children[0].distributedVolume[y].volume = yearlyDto.laptops;
        		$scope.endUserInfo.user.children[0].children[0].children[0].distributedVolume[y].volume = yearlyDto.highEndLaptops;
        		$scope.endUserInfo.user.children[0].children[0].children[1].distributedVolume[y].volume = yearlyDto.standardLaptops;
        		$scope.endUserInfo.user.children[0].children[1].distributedVolume[y].volume = yearlyDto.desktops;
        		$scope.endUserInfo.user.children[0].children[2].distributedVolume[y].volume = yearlyDto.thinClients;
        		$scope.endUserInfo.user.children[0].children[3].distributedVolume[y].volume = yearlyDto.mobileDevices;
        		$scope.endUserInfo.userImac.children[0].distributedVolume[y].volume = yearlyDto.imacDevices;
        		getExistingServerPricingLevelWiseInfo(yearlyDto,y);
        	}
    	}
    }

    // Get existing Service Desk server price level wise
	function getExistingServerPricingLevelWiseInfo(yearlyDto,year){
		if(yearlyDto.endUserUnitPriceInfoDtoList != null){
			if(yearlyDto.endUserUnitPriceInfoDtoList.length > 0){
				  if($scope.viewBy.type == 'unit'){
					  $scope.endUserInfo.user.children[0].distributedVolume[year].unit = yearlyDto.endUserUnitPriceInfoDtoList[0].endUserDevices;
			    	  $scope.endUserInfo.user.children[0].children[0].distributedVolume[year].unit = yearlyDto.endUserUnitPriceInfoDtoList[0].laptops;
			    		$scope.endUserInfo.user.children[0].children[0].children[0].distributedVolume[year].unit = yearlyDto.endUserUnitPriceInfoDtoList[0].highEndLaptops;
			    		$scope.endUserInfo.user.children[0].children[0].children[1].distributedVolume[year].unit = yearlyDto.endUserUnitPriceInfoDtoList[0].standardLaptops;
			    		$scope.endUserInfo.user.children[0].children[1].distributedVolume[year].unit = yearlyDto.endUserUnitPriceInfoDtoList[0].desktops;
			    		$scope.endUserInfo.user.children[0].children[2].distributedVolume[year].unit = yearlyDto.endUserUnitPriceInfoDtoList[0].thinClients;
			    		$scope.endUserInfo.user.children[0].children[3].distributedVolume[year].unit = yearlyDto.endUserUnitPriceInfoDtoList[0].mobileDevices;
			    		$scope.endUserInfo.userImac.children[0].distributedVolume[year].unit = yearlyDto.endUserUnitPriceInfoDtoList[0].imacDevices;
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
    	setLevelWisePercentage($scope.endUserInfo.user,map);
	};

	$scope.onChangeImacintensity = function(intensity){
		$scope.selectedImacId = intensity;
	}

	// on change End User Volume
	$scope.calcVolume = function(){
		if($scope.selectedSolutionId != null && $scope.selectedSolutionId != undefined &&  $scope.selectedSolutionId != ''){
			$scope.onChangeDistSetting($scope.selectedSolutionId);
		}
	}

	function setLevelWisePercentage(parent,map){
		if(parent.children != null){
			for(var k = 0;k < parent.children[0].children.length;k++){
				var child = parent.children[0].children[k];
				switch(child.id) {
				case "1.1.1":
					child.percentage =  $scope.Solution? $scope.Solution[0].laptopPerc : "";
					for(var i = 0;i < parent.children[0].children[0].children.length;i++){
						var children = parent.children[0].children[0].children[i];
						switch(children.id) {
						case "1.1.1.1":
							children.percentage =  $scope.Solution? $scope.Solution[0].highEndLaptopPerc : "";break;
						case "1.1.1.2":
							children.percentage =  $scope.Solution? $scope.Solution[0].standardLaptopPerc :"";break;
						}
					}
					break;
				case "1.1.2":
					child.percentage =  $scope.Solution? $scope.Solution[0].desktopPerc :"";break;
				case "1.1.3":
					child.percentage =  $scope.Solution? $scope.Solution[0].thinClientPerc :"";break;
				case "1.1.4":
					child.percentage =  $scope.Solution? $scope.Solution[0].mobilePerc :"";break;
				}
			}
			calculateEndUserVolume($scope.endUserInfo.user.children[0],map);
		}
	}

	function calculateEndUserVolume(parent,map){
		if(parent.children != null){
			for(var k = 0;k < parent.children.length;k++){
				var child = parent.children[k];

				for (var i = 0; i < $scope.dealInfo/12; i++){
					child.distributedVolume[i].volume = Math.round((parent.distributedVolume[i].volume * child.percentage)/100);
				}

				if((k == 0) ||(k == 1)){
					var children = parent.children[0].children[k];
					for (var i = 0; i < $scope.dealInfo/12; i++){
						children.distributedVolume[i].volume = Math.round((parent.children[0].distributedVolume[i].volume * children.percentage)/100);
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
						case "1.1.1": //Laptop End-User pricing
							if($scope.endUserInfo.user.children[0].children[0].open == true){
								var grandchild1volume = "";
								var grandchild2volume = "";
								var grandchild1unit = "";
								var grandchild2unit = "";
								for(var j=0;j< parent.children[0].children.length;j++){
									var children = parent.children[0].children[j];
									switch(children.id){
									case "1.1.1.1":
										grandchild1volume = parseInt((children.distributedVolume[i].volume=='' || children.distributedVolume[i].volume==undefined || children.distributedVolume[i].volume==null) ?0:children.distributedVolume[i].volume);
										grandchild1unit = parseFloat((children.distributedVolume[i].unit=='' || children.distributedVolume[i].unit==undefined || children.distributedVolume[i].unit==null) ?0:children.distributedVolume[i].unit);
										break;
									case "1.1.1.2":
										grandchild2volume = parseInt((children.distributedVolume[i].volume=='' || children.distributedVolume[i].volume==undefined || children.distributedVolume[i].volume==null) ?0:children.distributedVolume[i].volume);
										grandchild2unit = parseFloat((children.distributedVolume[i].unit=='' || children.distributedVolume[i].unit==undefined || children.distributedVolume[i].unit==null) ?0:children.distributedVolume[i].unit);
									}
								}
								Child1volume = parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
								Child1unit = ((grandchild1volume * grandchild1unit) + (grandchild2volume * grandchild2unit))/(grandchild1volume + grandchild2volume)
								if(isNaN(Child1unit)){
									Child1unit = 0;
								}
								Child1unit = Child1unit.toFixed(2)
								parent.children[0].distributedVolume[i].unit = Child1unit
							}
							else{
								Child1volume = parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
								Child1unit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
							}
							break;
						case "1.1.2": //Desktop End-User pricing
							Child2volume = parseFloat((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							Child2unit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
							break;
						case "1.1.3": //VDI End-User pricing
							Child3volume = parseFloat((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							Child3unit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
							break;
						case "1.1.4": //Mobile End-User pricing
							Child4volume = parseFloat((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							Child4unit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
							break;
						}
					}
					var endUserUnit = ((Child1volume * Child1unit) + (Child2volume * Child2unit) + (Child3volume * Child3unit)) / (Child1volume + Child2volume + Child3volume);
					endUserUnit = endUserUnit.toFixed(2);
					var endUserVolume = (Child1volume + Child2volume + Child3volume);
					parent.distributedVolume[i].unit = (((endUserVolume * endUserUnit) + (Child4volume * Child4unit)) / (endUserVolume + Child4volume))
					if(isNaN(parent.distributedVolume[i].unit)){
						parent.distributedVolume[i].unit = 0;
					}
					parent.distributedVolume[i].unit = parent.distributedVolume[i].unit.toFixed(2)
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
						case "1.1.1": //Laptop End-User pricing
							if($scope.endUserInfo.user.children[0].children[0].open == true){
								var grandchild1revenue = "";
								var grandchild2revenue = "";
								for(var j=0;j< parent.children[0].children.length;j++){
									var children = parent.children[0].children[j];
									switch(children.id){
									case "1.1.1.1":
										grandchild1revenue = parseInt((children.distributedVolume[i].revenue=='' || children.distributedVolume[i].revenue==undefined || children.distributedVolume[i].revenue==null) ?0:children.distributedVolume[i].revenue);
										break;
									case "1.1.1.2":
										grandchild2revenue = parseInt((children.distributedVolume[i].revenue=='' || children.distributedVolume[i].revenue==undefined || children.distributedVolume[i].revenue==null) ?0:children.distributedVolume[i].revenue);
									}
								}
								Child1revenue = Math.round(grandchild1revenue + grandchild2revenue)
								if(isNaN(Child1revenue)){
									Child1revenue = 0;
								}
								parent.children[0].distributedVolume[i].revenue = Child1unit
							}
							else{
								Child1revenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
							}
							break;
						case "1.1.2": //Desktop End-User pricing
							Child2revenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
							break;
						case "1.1.3": //VDI End-User pricing
							Child3revenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
							break;
						case "1.1.4": //Mobile End-User pricing
							Child4revenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
							break;
						}
					}
					parent.distributedVolume[i].revenue = Math.round(Child1revenue + Child2revenue + Child3revenue + Child4revenue)
					if(isNaN(parent.distributedVolume[i].revenue)){
						parent.distributedVolume[i].revenue = 0;
					}
				}
			}
	}

	// when user change the percentage
	$scope.onChangePercentage=function(child,index){
		$scope.customSol = false;
		if(index == 3){
			if($scope.endUserInfo.user.children[0].children[3].percentage > 100){
				$scope.showErrpercentage=true;
			}
			else{
				$scope.showErrpercentage=false;
				$scope.showVolumeErr=false;
				$scope.setCustom();
				calculateEndUserVolume($scope.endUserInfo.user.children[0]);
			}
		}
		else{
			var per=0;
			for(var i=0;i<child.length-1;i++){
				per=parseInt(per)+parseInt(child[i].percentage);
			}

			if(per!=100){
				$scope.showErr=true;
			}
			else{
				$scope.showErr=false;
				$scope.showVolumeErr=false;
				$scope.setCustom();
				calculateEndUserVolume($scope.endUserInfo.user.children[0]);
			}
		}
	}

	// function for setting the custom value
	$scope.setCustom=function()
	{
		$scope.customSol=true;
		var CustomSolutionId=_.where($scope.solList, {solutionName: 'Custom'})[0];
		$scope.selectedSolutionId=CustomSolutionId.solutionId;
	}

	// function for Imac type change to custom
	$scope.setImacCustom=function()
	{
		$scope.customImac=true;
		var CustomImacId = _.where($scope.imacList, {factorName: 'Custom'})[0];
		$scope.selectedImacId = CustomImacId.id;
	}

	// when user change the percentage
	$scope.onChangeLaptopChildPercentage = function(index) {
		$scope.setCustom();
		var changePercentage=$scope.endUserInfo.user.children[0].children[0].children[index].percentage;
		if(index == 0)
			{
			$scope.endUserInfo.user.children[0].children[0].children[index+1].percentage=100-changePercentage;
			}else
				{
				$scope.endUserInfo.user.children[0].children[0].children[index-1].percentage=100-changePercentage;
				}
		calculateEndUserVolume($scope.endUserInfo.user.children[0]);
	};

	// User change volume
	$scope.onChangevolume = function(index){
		$scope.customsol=false
		if(index == 3){
			$scope.endUserInfo.user.children[0].children[3].percentage = "";
			$scope.showVolumeErr=false;
			$scope.setCustom();
			$scope.calcServerPrice($scope.endUserInfo.user.children[0]);
		}
		else{
			for (var y = 0; y < $scope.dealInfo/12; y++) {
	    		if(parseInt($scope.endUserInfo.user.children[0].distributedVolume[y].volume)!=parseInt($scope.endUserInfo.user.children[0].children[0].distributedVolume[y].volume)
	    		+parseInt($scope.endUserInfo.user.children[0].children[1].distributedVolume[y].volume)
	    		+ parseInt($scope.endUserInfo.user.children[0].children[2].distributedVolume[y].volume)){
	    			$scope.showVolumeErr=true;
	    			return;
	    		}
	    	}
			for(var i=0;i< $scope.endUserInfo.user.children[0].children.length-1;i++){
				$scope.endUserInfo.user.children[0].children[i].percentage = "";
			}
			for(var i = 0; i < $scope.endUserInfo.user.children[0].children[0].children.length; i++){
				for (var j = 0; j < $scope.dealInfo/12; j++){
					$scope.endUserInfo.user.children[0].children[0].children[i].distributedVolume[j].volume = Math.round(($scope.endUserInfo.user.children[0].children[0].distributedVolume[j].volume * $scope.endUserInfo.user.children[0].children[0].children[i].percentage)/100);
				}
			}
			$scope.showVolumeErr=false;
			$scope.setCustom();
			$scope.calcServerPrice($scope.endUserInfo.user.children[0]);
		}
	}

	// User change volume
	$scope.onChangechildvolume = function(index){
		$scope.customsol=false
		for (var y = 0; y < $scope.dealInfo/12; y++) {
    		if(parseInt($scope.endUserInfo.user.children[0].children[0].distributedVolume[y].volume)!=parseInt($scope.endUserInfo.user.children[0].children[0].children[0].distributedVolume[y].volume)
    		+parseInt($scope.endUserInfo.user.children[0].children[0].children[1].distributedVolume[y].volume)){
    			$scope.showchildVolumeErr=true;
    			return;
    		}
    	}
		for(var i=0;i< $scope.endUserInfo.user.children[0].children[0].children.length;i++){
			$scope.endUserInfo.user.children[0].children[0].children[i].percentage = "";
		}
		$scope.showchildVolumeErr=false;
		$scope.setCustom();
		$scope.calcServerPrice($scope.endUserInfo.user.children[0]);
	}

	// On change Unit-revenue price radio button
	$scope.onchangeprice = function(value){
		if(value == 'revenue'){
			if($scope.endUserInfo.user.children[0].open == true){
				for(var i=0;i< $scope.endUserInfo.user.children[0].distributedVolume.length;i++){
					var Child1revenue = "";
					var Child2revenue = "";
					var Child3revenue = "";
					var Child4revenue = "";
					for(var k = 0;k < $scope.endUserInfo.user.children[0].children.length;k++){
						child = $scope.endUserInfo.user.children[0].children[k];
		    			switch(child.id) {
		    			case "1.1": //End-User
							break;
						case "1.1.1": //Laptop End-User pricing
							if($scope.endUserInfo.user.children[0].children[0].open == true){
								var grandchild1revenue = "";
								var grandchild2revenue = "";
								for(var j=0;j< $scope.endUserInfo.user.children[0].children[0].children.length;j++){
									var children = $scope.endUserInfo.user.children[0].children[0].children[j];
									switch(children.id){
									case "1.1.1.1":
										children.distributedVolume[i].revenue = Math.round(((children.distributedVolume[i].volume=='' || children.distributedVolume[i].volume==undefined || children.distributedVolume[i].volume==null) ?0:children.distributedVolume[i].volume)  * ((children.distributedVolume[i].unit=='' || children.distributedVolume[i].unit==undefined || children.distributedVolume[i].unit==null) ?0:children.distributedVolume[i].unit));
										if(isNaN(children.distributedVolume[i].revenue)){
											children.distributedVolume[i].revenue = 0;
										}
										grandchild1revenue = children.distributedVolume[i].revenue;
										break;
									case "1.1.1.2":
										children.distributedVolume[i].revenue = Math.round(((children.distributedVolume[i].volume=='' || children.distributedVolume[i].volume==undefined || children.distributedVolume[i].volume==null) ?0:children.distributedVolume[i].volume)  * ((children.distributedVolume[i].unit=='' || children.distributedVolume[i].unit==undefined || children.distributedVolume[i].unit==null) ?0:children.distributedVolume[i].unit));
										if(isNaN(children.distributedVolume[i].revenue)){
											children.distributedVolume[i].revenue = 0;
										}
										grandchild2revenue = children.distributedVolume[i].revenue;
									}
								}
								child.distributedVolume[i].revenue = Math.round(grandchild1revenue + grandchild2revenue)
								Child1revenue = child.distributedVolume[i].revenue;
							}
							else{
								child.distributedVolume[i].revenue = Math.round(((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume)  * ((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit));
								if(isNaN(child.distributedVolume[i].revenue)){
									child.distributedVolume[i].revenue = 0;
								}
								Child1revenue = child.distributedVolume[i].revenue;
							}
							break;
						case "1.1.2": //Desktop End-User pricing
							child.distributedVolume[i].revenue = Math.round(((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume)  * ((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit));
							if(isNaN(child.distributedVolume[i].revenue)){
								child.distributedVolume[i].revenue = 0;
							}
							Child2revenue = child.distributedVolume[i].revenue;
							break;
						case "1.1.3": //VDI End-User pricing
							child.distributedVolume[i].revenue = Math.round(((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume)  * ((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit));
							if(isNaN(child.distributedVolume[i].revenue)){
								child.distributedVolume[i].revenue = 0;
							}
							Child3revenue = child.distributedVolume[i].revenue;
							break;
						case "1.1.4": //Mobile End-User pricing
							child.distributedVolume[i].revenue = Math.round(((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume)  * ((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit));
							if(isNaN(child.distributedVolume[i].revenue)){
								child.distributedVolume[i].revenue = 0;
							}
							Child4revenue = child.distributedVolume[i].revenue;
							break;
		    			}
					}
					$scope.endUserInfo.user.children[0].distributedVolume[i].revenue = Math.round(Child1revenue + Child2revenue + Child3revenue + Child4revenue);
					$scope.endUserInfo.userImac.children[0].distributedVolume[i].revenue = Math.round((($scope.endUserInfo.userImac.children[0].distributedVolume[i].volume=='' || $scope.endUserInfo.userImac.children[0].distributedVolume[i].volume==undefined || $scope.endUserInfo.userImac.children[0].distributedVolume[i].volume==null) ?0:$scope.endUserInfo.userImac.children[0].distributedVolume[i].volume)  * (($scope.endUserInfo.userImac.children[0].distributedVolume[i].unit=='' || $scope.endUserInfo.userImac.children[0].distributedVolume[i].unit==undefined || $scope.endUserInfo.userImac.children[0].distributedVolume[i].unit==null) ?0:$scope.endUserInfo.userImac.children[0].distributedVolume[i].unit));
					if(isNaN($scope.endUserInfo.userImac.children[0].distributedVolume[i].revenue)){
						$scope.endUserInfo.userImac.children[0].distributedVolume[i].revenue = 0;
					}
				}
			}
			else{
				for(var i=0;i< $scope.endUserInfo.user.children[0].distributedVolume.length;i++){
					$scope.endUserInfo.user.children[0].distributedVolume[i].revenue = Math.round($scope.endUserInfo.user.children[0].distributedVolume[i].volume * $scope.endUserInfo.user.children[0].distributedVolume[i].unit);
					if(isNaN($scope.endUserInfo.user.children[0].distributedVolume[i].revenue)){
						$scope.endUserInfo.user.children[0].distributedVolume[i].revenue = 0;
					}
					$scope.endUserInfo.userImac.children[0].distributedVolume[i].revenue = Math.round((($scope.endUserInfo.userImac.children[0].distributedVolume[i].volume=='' || $scope.endUserInfo.userImac.children[0].distributedVolume[i].volume==undefined || $scope.endUserInfo.userImac.children[0].distributedVolume[i].volume==null) ?0:$scope.endUserInfo.userImac.children[0].distributedVolume[i].volume)  * (($scope.endUserInfo.userImac.children[0].distributedVolume[i].unit=='' || $scope.endUserInfo.userImac.children[0].distributedVolume[i].unit==undefined || $scope.endUserInfo.userImac.children[0].distributedVolume[i].unit==null) ?0:$scope.endUserInfo.userImac.children[0].distributedVolume[i].unit));
					if(isNaN($scope.endUserInfo.userImac.children[0].distributedVolume[i].revenue)){
						$scope.endUserInfo.userImac.children[0].distributedVolume[i].revenue = 0;
					}
				}
			}
		}
		if(value == 'unit'){
			if($scope.endUserInfo.user.children[0].open == true){
				for(var i=0;i< $scope.endUserInfo.user.children[0].distributedVolume.length;i++){
					for(var k = 0;k < $scope.endUserInfo.user.children[0].children.length;k++){
						child = $scope.endUserInfo.user.children[0].children[k];
		    			switch(child.id) {
		    			case "1.1": //contact
							break;
						case "1.1.1": //Laptop End-User pricing
							if($scope.endUserInfo.user.children[0].children[0].open == true){
								for(var j=0;j< $scope.endUserInfo.user.children[0].children[0].children.length;j++){
									var children = $scope.endUserInfo.user.children[0].children[0].children[j];
									switch(children.id){
									case "1.1.1.1":
										children.distributedVolume[i].unit = parseFloat(((children.distributedVolume[i].revenue=='' || children.distributedVolume[i].revenue==undefined || children.distributedVolume[i].revenue==null) ?0:children.distributedVolume[i].revenue)  / ((children.distributedVolume[i].volume=='' || children.distributedVolume[i].volume==undefined || children.distributedVolume[i].volume==null) ?0:children.distributedVolume[i].volume));
										if(isNaN(children.distributedVolume[i].unit)){
											children.distributedVolume[i].unit = 0;
										}
										children.distributedVolume[i].unit = children.distributedVolume[i].unit.toFixed(2);
										break;
									case "1.1.1.2":
										children.distributedVolume[i].unit = parseFloat(((children.distributedVolume[i].revenue=='' || children.distributedVolume[i].revenue==undefined || children.distributedVolume[i].revenue==null) ?0:children.distributedVolume[i].revenue)  / ((children.distributedVolume[i].volume=='' || children.distributedVolume[i].volume==undefined || children.distributedVolume[i].volume==null) ?0:children.distributedVolume[i].volume));
										if(isNaN(children.distributedVolume[i].unit)){
											children.distributedVolume[i].unit = 0;
										}
										children.distributedVolume[i].unit = children.distributedVolume[i].unit.toFixed(2);
										break;
									}
								}
								child.distributedVolume[i].unit = parseFloat((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue) / ((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
								if(isNaN(child.distributedVolume[i].unit)){
									child.distributedVolume[i].unit = 0;
								}
								child.distributedVolume[i].unit = child.distributedVolume[i].unit.toFixed(2)
							}
							else{
								child.distributedVolume[i].unit = parseFloat((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue) / ((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
								if(isNaN(child.distributedVolume[i].unit)){
									child.distributedVolume[i].unit = 0;
								}
								child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
							}
							break;
						case "1.1.2": //Desktop End-User pricing
							child.distributedVolume[i].unit = parseFloat((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue) / ((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							if(isNaN(child.distributedVolume[i].unit)){
								child.distributedVolume[i].unit = 0;
							}
							child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
							break;
						case "1.1.3": //VDI End-User pricing
							child.distributedVolume[i].unit = parseFloat((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue) / ((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							if(isNaN(child.distributedVolume[i].unit)){
								child.distributedVolume[i].unit = 0;
							}
							child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
							break;
						case "1.1.4": //Mobile End-User pricing
							child.distributedVolume[i].unit = parseFloat((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue) / ((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							if(isNaN(child.distributedVolume[i].unit)){
								child.distributedVolume[i].unit = 0;
							}
							child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
							break;
		    			}
					}
					$scope.endUserInfo.user.children[0].distributedVolume[i].unit = ($scope.endUserInfo.user.children[0].distributedVolume[i].revenue / (parseInt($scope.endUserInfo.user.children[0].distributedVolume[i].volume) + parseInt($scope.endUserInfo.user.children[0].children[3].distributedVolume[i].volume))).toFixed(2);
					if(isNaN($scope.endUserInfo.user.children[0].distributedVolume[i].unit)){
						$scope.endUserInfo.user.children[0].distributedVolume[i].unit = 0.00;
					}
					$scope.endUserInfo.userImac.children[0].distributedVolume[i].unit = ((($scope.endUserInfo.userImac.children[0].distributedVolume[i].revenue=='' || $scope.endUserInfo.userImac.children[0].distributedVolume[i].revenue==undefined || $scope.endUserInfo.userImac.children[0].distributedVolume[i].revenue==null) ?0:$scope.endUserInfo.userImac.children[0].distributedVolume[i].revenue)  / (($scope.endUserInfo.userImac.children[0].distributedVolume[i].volume=='' || $scope.endUserInfo.userImac.children[0].distributedVolume[i].volume==undefined || $scope.endUserInfo.userImac.children[0].distributedVolume[i].volume==null) ?0:$scope.endUserInfo.userImac.children[0].distributedVolume[i].volume)).toFixed(2);
					if(isNaN($scope.endUserInfo.userImac.children[0].distributedVolume[i].unit)){
						$scope.endUserInfo.userImac.children[0].distributedVolume[i].unit = 0.00;
					}
				}
			}
			else{
				for(var i=0;i< $scope.endUserInfo.user.children[0].distributedVolume.length;i++){
					$scope.endUserInfo.user.children[0].distributedVolume[i].unit = ($scope.endUserInfo.user.children[0].distributedVolume[i].revenue / $scope.endUserInfo.userchildren[0].distributedVolume[i].volume).toFixed(2);
					if(isNaN($scope.endUserInfo.user.children[0].distributedVolume[i].unit)){
						$scope.endUserInfo.user.children[0].distributedVolume[i].unit = 0.00;
					}
					$scope.endUserInfo.userImac.children[0].distributedVolume[i].unit = ((($scope.endUserInfo.userImac.children[0].distributedVolume[i].revenue=='' || $scope.endUserInfo.userImac.children[0].distributedVolume[i].revenue==undefined || $scope.endUserInfo.userImac.children[0].distributedVolume[i].revenue==null) ?0:$scope.endUserInfo.userImac.children[0].distributedVolume[i].revenue)  / (($scope.endUserInfo.userImac.children[0].distributedVolume[i].volume=='' || $scope.endUserInfo.userImac.children[0].distributedVolume[i].volume==undefined || $scope.endUserInfo.userImac.children[0].distributedVolume[i].volume==null) ?0:$scope.endUserInfo.userImac.children[0].distributedVolume[i].volume)).toFixed(2);
					if(isNaN($scope.endUserInfo.userImac.children[0].distributedVolume[i].unit)){
						$scope.endUserInfo.userImac.children[0].distributedVolume[i].unit = 0.00;
					}
				}
			}
		}

	}


	// Save End User Sub form data
    $scope.saveEndUserSubInfo = function(){
    	$scope.getIndicator();
    	setEndUserYearlyInfo();
    	$scope.endUserInfoDto = {
    			offshoreAllowed : $scope.dealDetails.offshoreSelected == "Yes" ? true : false,
    			includeBreakFix :  $scope.dealDetails.breakfixSelected == "Yes" ? true : false,
    			includeHardware: $scope.dealDetails.hardwareIncludedSelected == "Yes" ? true : false,
    			resolutionTime: $scope.dealDetails.resolutionTimeSelected,
    			imacType: $scope.endUserInfo.userImac.open == true ? $scope.selectedImacId : undefined,
    			selectedSolution: $scope.endUserInfo.user.open == true ? $scope.selectedSolutionId : undefined,
    			endUserYearlyDataInfoDtoList : $scope.endUserInfo.endUserYearlyDataInfoDtos,
    			levelIndicator: $scope.getIndicator(),
    	    	dealId : $stateParams.dealId,
    	    	towerArchitect : $scope.dealDetails.towerArchitect
    	}
    	if($scope.endUserInfo.user.open && !$scope.endUserInfo.user.children[0].open){$confirm({text: 'Default distribution setting has been applied to child levels'})
 	        .then(function() {
    	var url = endpointsurls.SAVE_ENDUSER_INFO+$stateParams.dealId;
        customInterceptor.postrequest(url,$scope.endUserInfoDto).then(function successCallback(response) {
        	$scope.isSaveEndUser= DealService.getter() || isSave;
        	$scope.isSaveEndUser.endUserS=true;
    		DealService.setter($scope.isSaveEndUser);
    		$scope.putIndicator();

		}, function errorCallback(response) {
			console.log(response.statusText);
			$alert({title:'Error',text: 'Failed to save data.'})
		});})}
    	else{
			var url = endpointsurls.SAVE_ENDUSER_INFO+$stateParams.dealId;
	        customInterceptor.postrequest(url,$scope.endUserInfoDto).then(function successCallback(response) {
	        	$scope.isSaveEndUser= DealService.getter() || isSave;
	        	$scope.isSaveEndUser.endUserS=true;
	    		DealService.setter($scope.isSaveEndUser);
	    		$scope.putIndicator();

			}, function errorCallback(response) {
				console.log(response.statusText);
				$alert({title:'Error',text: 'Failed to save data.'})});
		};
    }



  //putIndicator
      $scope.putIndicator=function()
      {
      	$scope.submissionIndicator[3]=1;
          var urlIndicator= endpointsurls.GENERic_INDICATOR+$stateParams.dealId+'?assessmentIndicator='+$scope.assessmentIndicator+'&submissionIndicator='+$scope.submissionIndicator.join(',');
         customInterceptor.putrequestWithoutData(urlIndicator).then(function successCallback(response) {
        	 $state.go('home.submission.genericSubmission.totalsSubmission',({dealId:$stateParams.dealId}));
         });
      }
    // get the Indicator value
    $scope.getIndicator=function()
    {
    	var levelIndicator='';
    	levelIndicator+=$scope.endUserInfo.user.open?1:0;
    	levelIndicator+=',';
    	levelIndicator+=$scope.endUserInfo.user.children[0].open?1:0;
    	levelIndicator+=',';
    	levelIndicator+=$scope.endUserInfo.user.children[0].children[0].open?1:0;
    	levelIndicator+=',';
    	levelIndicator+=$scope.endUserInfo.userImac.open?1:0;
        return levelIndicator;
    }

    // Extract data yearly wise
    function setEndUserYearlyInfo() {
    	var yearlyInfoList = [];
    	for (var y = 0; y < $scope.dealInfo/12; y++) {
    		var yearlyData = {};
    		yearlyData.year = y+1;
    		yearlyData.yearId = y+1;
    		if($scope.endUserInfo.user.open){
    			yearlyData.endUserDevices = $scope.endUserInfo.user.children[0].distributedVolume[y].volume;
        		yearlyData.laptops = $scope.endUserInfo.user.children[0].children[0].distributedVolume[y].volume;
        		yearlyData.highEndLaptops = $scope.endUserInfo.user.children[0].children[0].children[0].distributedVolume[y].volume;
        		yearlyData.standardLaptops = $scope.endUserInfo.user.children[0].children[0].children[1].distributedVolume[y].volume;
        		yearlyData.desktops = $scope.endUserInfo.user.children[0].children[1].distributedVolume[y].volume;
        		yearlyData.thinClients = $scope.endUserInfo.user.children[0].children[2].distributedVolume[y].volume;
        		yearlyData.mobileDevices = $scope.endUserInfo.user.children[0].children[3].distributedVolume[y].volume;
    		}
    		if($scope.endUserInfo.userImac.open){
    			yearlyData.imacDevices = $scope.endUserInfo.userImac.children[0].distributedVolume[y].volume;
    		}
    		yearlyData.endUserUnitPriceInfoDtoList= extractServerUnitPrice(y);
    		yearlyData.endUserRevenueInfoDtoList = extractServerRevenuePrice(y);
    		yearlyInfoList.push(yearlyData);
    	}
    	$scope.endUserInfo.endUserYearlyDataInfoDtos = yearlyInfoList;
    }

    // extract server price in units
    function extractServerUnitPrice(year){
    	var unitPrice = [];
    	var unitInfo={};

    	if($scope.viewBy.type == 'unit'){
    		if($scope.endUserInfo.user.open){
    			if($scope.endUserInfo.user.children[0].open){
    				unitInfo.endUserDevices = $scope.endUserInfo.user.children[0].distributedVolume[year].unit;
            		unitInfo.laptops = $scope.endUserInfo.user.children[0].children[0].distributedVolume[year].unit;
            		unitInfo.highEndLaptops = $scope.endUserInfo.user.children[0].children[0].children[0].distributedVolume[year].unit;
            		unitInfo.standardLaptops = $scope.endUserInfo.user.children[0].children[0].children[1].distributedVolume[year].unit;
            		unitInfo.desktops = $scope.endUserInfo.user.children[0].children[1].distributedVolume[year].unit;
            		unitInfo.thinClients = $scope.endUserInfo.user.children[0].children[2].distributedVolume[year].unit;
            		unitInfo.mobileDevices = $scope.endUserInfo.user.children[0].children[3].distributedVolume[year].unit;
    			}
    			else{
    				unitInfo.endUserDevices = $scope.endUserInfo.user.children[0].distributedVolume[year].unit
    			}
    		}
    		if($scope.endUserInfo.userImac.open){
    			unitInfo.imacDevices = $scope.endUserInfo.userImac.children[0].distributedVolume[year].unit;
    		}
    	}
    	if($scope.viewBy.type == 'revenue'){
    		if($scope.endUserInfo.user.open){
    			if($scope.endUserInfo.user.children[0].open){
    				unitInfo.endUserDevices = $scope.endUserInfo.user.children[0].distributedVolume[year].revenue / (parseInt($scope.endUserInfo.user.children[0].distributedVolume[year].volume) + parseInt($scope.endUserInfo.user.children[0].children[3].distributedVolume[year].volume));
            		unitInfo.laptops = $scope.endUserInfo.user.children[0].children[0].distributedVolume[year].revenue / $scope.endUserInfo.user.children[0].children[0].distributedVolume[year].volume;
            		unitInfo.highEndLaptops = $scope.endUserInfo.user.children[0].children[0].children[0].distributedVolume[year].revenue / $scope.endUserInfo.user.children[0].children[0].children[0].distributedVolume[year].volume;
            		unitInfo.standardLaptops = $scope.endUserInfo.user.children[0].children[0].children[1].distributedVolume[year].revenue / $scope.endUserInfo.user.children[0].children[0].children[1].distributedVolume[year].volume;
            		unitInfo.desktops = $scope.endUserInfo.user.children[0].children[1].distributedVolume[year].revenue / $scope.endUserInfo.user.children[0].children[1].distributedVolume[year].volume;
            		unitInfo.thinClients = $scope.endUserInfo.user.children[0].children[2].distributedVolume[year].revenue / $scope.endUserInfo.user.children[0].children[2].distributedVolume[year].volume;
            		unitInfo.mobileDevices = $scope.endUserInfo.user.children[0].children[3].distributedVolume[year].revenue / $scope.endUserInfo.user.children[0].children[3].distributedVolume[year].volume;
    			}
    			else{
    				unitInfo.endUserDevices = $scope.endUserInfo.user.children[0].distributedVolume[year].revenue / (parseInt($scope.endUserInfo.user.children[0].distributedVolume[year].volume));
    			}
    			
    		}
    		if($scope.endUserInfo.userImac.open){
    			unitInfo.imacDevices = $scope.endUserInfo.userImac.children[0].distributedVolume[year].revenue / $scope.endUserInfo.userImac.children[0].distributedVolume[year].volume;
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
    		if($scope.endUserInfo.user.open){
    			if($scope.endUserInfo.user.children[0].open){
    				revenueInfo.totalEndUserDevices = Math.round($scope.endUserInfo.user.children[0].distributedVolume[year].unit * (parseInt($scope.endUserInfo.user.children[0].distributedVolume[year].volume) + parseInt($scope.endUserInfo.user.children[0].children[3].distributedVolume[year].volume))) * 12;
    			}
    			else{
    				revenueInfo.totalEndUserDevices = Math.round($scope.endUserInfo.user.children[0].distributedVolume[year].unit * (parseInt($scope.endUserInfo.user.children[0].distributedVolume[year].volume))) * 12;
    			}
    		}
    		if($scope.endUserInfo.userImac.open){
    			revenueInfo.totalImacDevices = Math.round($scope.endUserInfo.userImac.children[0].distributedVolume[year].unit * $scope.endUserInfo.userImac.children[0].distributedVolume[year].volume) * 12;
    		}

    	}
    	if($scope.viewBy.type == 'revenue'){
    		if($scope.endUserInfo.user.open){
    			revenueInfo.totalEndUserDevices = $scope.endUserInfo.user.children[0].distributedVolume[year].revenue * 12;
    		}
    		if($scope.endUserInfo.userImac.open){
    			revenueInfo.totalImacDevices = $scope.endUserInfo.userImac.children[0].distributedVolume[year].revenue * 12;
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

