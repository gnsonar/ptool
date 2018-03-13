priceToolcontrollers.controller('HostingCtrl',function($scope,$http,DealService,HostingService,endpointsurls,customInterceptor,endpointsurls,$stateParams,$dialog,$confirm,$state,$alert) {
	$scope.tabs=[{class:'active',href:"#inputtab",tabname:"Input"},{class:'inActive',href:"#caltab",tabname:"Calculate"},{class:'inActive',href:"#resulttab",tabname:"Result"}];
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
	$scope.numberArr = [];
	$scope.selectedSolutionId = '';
	$scope.solution = {};
	$scope.hostingInfoDto = {};
	$scope.existingHostingInfo = {};
	$scope.viewBy = {type:'unit'};
	$scope.showmsg = false;
	$scope.customSol=false;
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
    $scope.tabEvent=function($event,index,val,childindex,grandchildindex,ggcindex,sgcIndex){
        if($event.keyCode == 13 || $event.keyCode == 9){
            switch(val) {
            case "ServerVolume":
                for(var i=index;i<$scope.hostingInfo.serverHosting.children[0].distributedVolume.length;i++){
                                $scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume = $scope.hostingInfo.serverHosting.children[0].distributedVolume[index].volume;
                }
                break;
            case "PlatformChildVolume":
                for(var i=index;i<$scope.hostingInfo.platformHosting.distributedVolume.length;i++){
                               $scope.hostingInfo.platformHosting.children[childindex].distributedVolume[i].volume = $scope.hostingInfo.platformHosting.children[childindex].distributedVolume[index].volume;
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
			$scope.isSaveHosting= DealService.getter() || isSave;
			DealService.setter($scope.isSaveHosting);
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
        for(var i=0; i< $scope.dealInfo/12; i++){
    		$scope.numberArr.push(i);
    	}
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
   	   			$scope.onChangeDistSetting($scope.selectedSolutionId);
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
        	}
    	}
    }

    //Distribution Setting Drop-down Selected
	$scope.onChangeDistSetting = function(solId) {
		$scope.showErr=false;
		$scope.showVolumeErr=false;
		$scope.customSol = false;
		$scope.selectedSolutionId = solId;
    	$scope.Solution = _.where($scope.solList, {solutionId: solId});
    	setLevelWisePercentage($scope.hostingInfo.serverHosting);
	};

	function setLevelWisePercentage(parent){
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
			$scope.calculateHostingVolume($scope.hostingInfo.serverHosting.children[0]);
		}
	}

	$scope.calculateHostingVolume = function (parent){
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
		}
	}

	// Server volume change
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

	// Save Hosting Input data
    $scope.savehostingInfo = function(){
    	$scope.getIndicator();
    	setHostingYearlyInfo();
    	$scope.hostingInfoDto = {
    			offshoreAllowed : $scope.dealDetails.offshoreSelected == "Yes" ? true : false,
    			levelOfService :  $scope.dealDetails.standardwindowInfoSelected,
    			includeHardware: $scope.dealDetails.hardwareIncludedSelected == "Yes" ? true : false,
    			includeTooling: $scope.dealDetails.tooling == "Yes" ? true : false,
    			coLocation: $scope.dealDetails.colocation,
    			selectedSolutionId: $scope.hostingInfo.serverHosting.open == true ? $scope.selectedSolutionId : undefined,
    			hostingYearlyDataInfoDtoList : $scope.hostingInfo.hostingYearlyDataInfoDtos,
    			levelIndicator: $scope.getIndicator(),
    	    	dealId : $stateParams.dealId,
    	    	towerArchitect : $scope.dealDetails.towerArchitect
    	}
    	if($scope.hostingInfo.serverHosting.open && !$scope.hostingInfo.serverHosting.children[0].open){$confirm({text: 'Default distribution setting has been applied to child levels'})
 	        .then(function() {
    	var url = endpointsurls.SAVE_HOSTING_INFO+$stateParams.dealId;
        customInterceptor.postrequest(url,$scope.hostingInfoDto).then(function successCallback(response) {
        	$scope.id=response.data.hostingId;
        	$scope.goToCal();

		}, function errorCallback(response) {
			console.log(response.statusText);
			$alert({title:'Error',text: 'Failed to save data.'})
		});})}
    	else{
			var url = endpointsurls.SAVE_HOSTING_INFO+$stateParams.dealId;
	        customInterceptor.postrequest(url,$scope.hostingInfoDto).then(function successCallback(response) {
	        	$scope.goToCal();
	        	$scope.id=response.data.hostingId;
			}, function errorCallback(response) {
				console.log(response.statusText);
				$alert({title:'Error',text: 'Failed to save data.'})});
		};
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

    		yearlyInfoList.push(yearlyData);
    	}
    	$scope.hostingInfo.hostingYearlyDataInfoDtos = yearlyInfoList;
    }

//*//*********************************Calculate tab*****************************//*
 // Recalculate
	   $scope.reCalculate=function()
	   {
		   $scope.resetCalculateDetails();
		 var putDeatls= { offshoreAllowed : $scope.dealDetails.offshoreSelected == "Yes" ? true : false,
	    			levelOfService :  $scope.dealDetails.standardwindowInfoSelected,
	    			includeHardware: $scope.dealDetails.hardwareIncludedSelected == "Yes" ? true : false,
	    			toolingIncluded: $scope.dealDetails.tooling == "Yes" ? true : false,
	    			coLocation: $scope.dealDetails.colocation}
		     var url = endpointsurls.HOSTING_RECALCULATE+$scope.id;
	        customInterceptor.putrequest(url,putDeatls).then(function successCallback(response) {
	        	$scope.tabReset(1);
	        	$scope.hostingCalculateDto  = response.data;
	               extractServerVolumeAvg($scope.hostingInfo.serverHosting.children[0]);
	                extractChildLevelWiseAverages($scope.hostingInfo.serverHosting.children[0].children);
	                extractPlatformHostingvalues($scope.hostingInfo.platformHosting.children);
	        });
	        $scope.createUnitPriceForLevels($scope.hostingInfo.platformHosting.children[0].distributedVolume.length);
	   }



         $scope.populatedCalculateTabDetails = function(){
        	 $scope.resetCalculateDetails();
				var url = endpointsurls.CALCULATE_HOSTING_INFO+$stateParams.dealId;
				customInterceptor.getrequest(url).then(function successCallback(response) {
               $scope.hostingCalculateDto  = response.data;
               extractServerVolumeAvg($scope.hostingInfo.serverHosting.children[0]);
                extractChildLevelWiseAverages($scope.hostingInfo.serverHosting.children[0].children);
                extractPlatformHostingvalues($scope.hostingInfo.platformHosting.children);
                $scope.unitPriceDto=[];
	 			$scope.createUnitPriceForLevels($scope.hostingInfo.platformHosting.children[0].distributedVolume.length);
         }, function errorCallback(response) {
				console.log(response.statusText);
			});
            }


//extractServerVolumeAvg
     function extractServerVolumeAvg(parent){
    	 if($scope.hostingCalculateDto.servers!=null){
    		 parent['dataValidation']=false;
       parent.benchLow = $scope.hostingCalculateDto.servers.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.servers.benchDealLowAvgUnitPrice:"NA";
       parent.benchTarget = $scope.hostingCalculateDto.servers.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.servers.benchDealTargetAvgUnitPrice:"NA";
       parent.pastAvg =  $scope.hostingCalculateDto.servers.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.servers.pastDealAvgUnitPrice:"NA";
       parent.compAvg = $scope.hostingCalculateDto.servers.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.servers.compDealAvgUnitPrice:"NA";
    	 }else
    		 {
    		 parent.benchLow="NA";
    		 parent.benchTarget="NA";
    		 parent.pastAvg="NA";
    		 parent.compAvg="NA";
    		 parent['dataValidation']=true;
    		 }

    	 }

//extractPlatformHostingvalues
      function extractPlatformHostingvalues(parent){

           for(var i=0;i<parent.length;i++){
        	   switch(parent[i].id) {
        	   case "2.1":
        		  if($scope.hostingCalculateDto.sqlInstances!=null){
                		  parent[i]['dataValidation']=false;
                		  parent[i].benchLow = $scope.hostingCalculateDto.sqlInstances.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.sqlInstances.benchDealLowAvgUnitPrice:"NA";
                	       parent[i].benchTarget = $scope.hostingCalculateDto.sqlInstances.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.sqlInstances.benchDealTargetAvgUnitPrice:"NA";
                	       parent[i].pastAvg =  $scope.hostingCalculateDto.sqlInstances.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.sqlInstances.pastDealAvgUnitPrice:"NA";
                	       parent[i].compAvg = $scope.hostingCalculateDto.sqlInstances.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.sqlInstances.compDealAvgUnitPrice:"NA";
	              }else
	     		   {
	            	  parent[i]['dataValidation']=true;
	            	  parent[i].benchLow="NA";
	            	  parent[i].benchTarget="NA";
	            	  parent[i].pastAvg="NA";
	            	  parent[i].compAvg="NA";
	         		 }
                break;

        	   case "2.2":
        		   if($scope.hostingCalculateDto.cotsInstallations!=null){
                	   parent[i]['dataValidation']=false;
                	   parent[i].benchLow = $scope.hostingCalculateDto.cotsInstallations.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.cotsInstallations.benchDealLowAvgUnitPrice:"NA";
            	       parent[i].benchTarget = $scope.hostingCalculateDto.cotsInstallations.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.cotsInstallations.benchDealTargetAvgUnitPrice:"NA";
            	       parent[i].pastAvg =  $scope.hostingCalculateDto.cotsInstallations.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.cotsInstallations.pastDealAvgUnitPrice:"NA";
            	       parent[i].compAvg = $scope.hostingCalculateDto.cotsInstallations.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.cotsInstallations.compDealAvgUnitPrice:"NA";
                   }
                  else
 	     		 {
                	  parent[i]['dataValidation']=true;
                	  parent[i].benchLow="NA";
                	  parent[i].benchTarget="NA";
                	  parent[i].pastAvg="NA";
                	  parent[i].compAvg="NA";
 	         		 }
        		   break;

           }

           }
   }

      function extractChildLevelWiseAverages(parent){
           for(var k = 0;k < parent.length;k++){
              child = parent[k];
              switch(child.id) {
                    case "1.1": //Server Volume

                    	/*if(false){
                            child.benchLow =$scope.hostingCalculateDto.benchDealPhysicalLowUnitPrice !=0 ? $scope.hostingCalculateDto.benchDealPhysicalLowUnitPrice : "NA";
                            child.benchTarget = $scope.hostingCalculateDto.benchDealPhysicalTargetUnitPrice !=0 ? $scope.hostingCalculateDto.benchDealPhysicalTargetUnitPrice : "NA";
                            child.pastAvg = $scope.hostingCalculateDto.pastDealPhysicalAvgUnitPrice !=0 ? $scope.hostingCalculateDto.pastDealPhysicalAvgUnitPrice : "NA";
                            child.compAvg = $scope.hostingCalculateDto.compDealPhysicalAvgUnitPrice !=0 ? $scope.hostingCalculateDto.compDealPhysicalAvgUnitPrice : "NA";
                     	}else{
                     		child.benchLow ='NA';
                     		child.benchTarget="NA";
                     		 child.pastAvg="NA";
                     		 child.compAvg="NA"
                     	}*/
                            break;
                    case "1.1.1": //Physical Server
                    	 if($scope.hostingCalculateDto.physical!=null){
                    		child['dataValidation']=false;
                    		child.benchLow = $scope.hostingCalculateDto.physical.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.physical.benchDealLowAvgUnitPrice:"NA";
                    		child.benchTarget = $scope.hostingCalculateDto.physical.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.physical.benchDealTargetAvgUnitPrice:"NA";
                    		child.pastAvg =  $scope.hostingCalculateDto.physical.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.physical.pastDealAvgUnitPrice:"NA";
                    		child.compAvg = $scope.hostingCalculateDto.physical.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.physical.compDealAvgUnitPrice:"NA";
                    	}else{
                    		child['dataValidation']=true;
                    		child.benchLow ='NA';
                    		child.benchTarget="NA";
                    		 child.pastAvg="NA";
                    		 child.compAvg="NA"
                    	}
                           break;


                     case "1.1.2": //Virtual Server
                    	 if($scope.hostingCalculateDto.virtual!=null){
                     		child['dataValidation']=false;
                     		child.benchLow = $scope.hostingCalculateDto.virtual.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.virtual.benchDealLowAvgUnitPrice:"NA";
                     		child.benchTarget = $scope.hostingCalculateDto.virtual.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.virtual.benchDealTargetAvgUnitPrice:"NA";
                     		child.pastAvg =  $scope.hostingCalculateDto.virtual.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtual.pastDealAvgUnitPrice:"NA";
                     		child.compAvg = $scope.hostingCalculateDto.virtual.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtual.compDealAvgUnitPrice:"NA";
                     	}else{
                     		child['dataValidation']=true;
			          		child.benchLow ='NA';
			          		child.benchTarget="NA";
			          		 child.pastAvg="NA";
			          		 child.compAvg="NA"
			          	}
                           break;
                     case "1.1.1.1": //Physical Windows/Linux

                    	if($scope.hostingCalculateDto.physicalWin!=null){
                      		child['dataValidation']=false;
                      		child.benchLow = $scope.hostingCalculateDto.physicalWin.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalWin.benchDealLowAvgUnitPrice:"NA";
                      		child.benchTarget = $scope.hostingCalculateDto.physicalWin.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalWin.benchDealTargetAvgUnitPrice:"NA";
                      		child.pastAvg =  $scope.hostingCalculateDto.physicalWin.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalWin.pastDealAvgUnitPrice:"NA";
                      		child.compAvg = $scope.hostingCalculateDto.physicalWin.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalWin.compDealAvgUnitPrice:"NA";
                      	}else{
                      		child['dataValidation']=true;
				          		child.benchLow ='NA';
				          		child.benchTarget="NA";
				          		 child.pastAvg="NA";
				          		 child.compAvg="NA"
				          	}
			                           break;
                     case "1.1.1.2": // Physical Unix

                    	if($scope.hostingCalculateDto.physicalUnix!=null){
                       		child['dataValidation']=false;
                       		child.benchLow = $scope.hostingCalculateDto.physicalUnix.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalUnix.benchDealLowAvgUnitPrice:"NA";
                       		child.benchTarget = $scope.hostingCalculateDto.physicalUnix.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalUnix.benchDealTargetAvgUnitPrice:"NA";
                       		child.pastAvg =  $scope.hostingCalculateDto.physicalUnix.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalUnix.pastDealAvgUnitPrice:"NA";
                       		child.compAvg = $scope.hostingCalculateDto.physicalUnix.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalUnix.compDealAvgUnitPrice:"NA";
                       	}else{
                       		child['dataValidation']=true;
				          		child.benchLow ='NA';
				          		child.benchTarget="NA";
				          		 child.pastAvg="NA";
				          		 child.compAvg="NA"
				          	}

                           break;


                     case "1.1.2.1": // virtual Public
                    	 if($scope.hostingCalculateDto.virtualPublic!=null){
                        		child['dataValidation']=false;
                        		child.benchLow = $scope.hostingCalculateDto.virtualPublic.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublic.benchDealLowAvgUnitPrice:"NA";
                        		child.benchTarget = $scope.hostingCalculateDto.virtualPublic.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublic.benchDealTargetAvgUnitPrice:"NA";
                        		child.pastAvg =  $scope.hostingCalculateDto.virtualPublic.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublic.pastDealAvgUnitPrice:"NA";
                        		child.compAvg = $scope.hostingCalculateDto.virtualPublic.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublic.compDealAvgUnitPrice:"NA";
                        	}else{
                        		child['dataValidation']=true;
				          		child.benchLow ='NA';
				          		child.benchTarget="NA";
				          		 child.pastAvg="NA";
				          		 child.compAvg="NA"
				          	}

                           break;
                     case "1.1.2.2": //virtual private
                    	 if($scope.hostingCalculateDto.virtualPrivate!=null){
                     		child['dataValidation']=false;
                     		child.benchLow = $scope.hostingCalculateDto.virtualPrivate.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivate.benchDealLowAvgUnitPrice:"NA";
                     		child.benchTarget = $scope.hostingCalculateDto.virtualPrivate.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivate.benchDealTargetAvgUnitPrice:"NA";
                     		child.pastAvg =  $scope.hostingCalculateDto.virtualPrivate.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivate.pastDealAvgUnitPrice:"NA";
                     		child.compAvg = $scope.hostingCalculateDto.virtualPrivate.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivate.compDealAvgUnitPrice:"NA";
                     	}else{
                     		child['dataValidation']=true;
				          		child.benchLow ='NA';
				          		child.benchTarget="NA";
				          		 child.pastAvg="NA";
				          		 child.compAvg="NA"
				          	}

                           break;
                       /*   grand*/

                     case "1.1.2.1.1": // virtual Public window/lin
                    	 if($scope.hostingCalculateDto.virtualPublicWin!=null){
                      		child['dataValidation']=false;
                      		child.benchLow = $scope.hostingCalculateDto.virtualPublicWin.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicWin.benchDealLowAvgUnitPrice:"NA";
                      		child.benchTarget = $scope.hostingCalculateDto.virtualPublicWin.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicWin.benchDealTargetAvgUnitPrice:"NA";
                      		child.pastAvg =  $scope.hostingCalculateDto.virtualPublicWin.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicWin.pastDealAvgUnitPrice:"NA";
                      		child.compAvg = $scope.hostingCalculateDto.virtualPublicWin.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicWin.compDealAvgUnitPrice:"NA";
                      	}else{
                      		child['dataValidation']=true;
				          		child.benchLow ='NA';
				          		child.benchTarget="NA";
				          		 child.pastAvg="NA";
				          		 child.compAvg="NA"
				          	}

                           break;
                     case "1.1.2.1.2": //virtual Public unix
                    	if($scope.hostingCalculateDto.virtualPublicUnix!=null){
                       		child['dataValidation']=false;
                       		child.benchLow = $scope.hostingCalculateDto.virtualPublicUnix.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicUnix.benchDealLowAvgUnitPrice:"NA";
                       		child.benchTarget = $scope.hostingCalculateDto.virtualPublicUnix.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicUnix.benchDealTargetAvgUnitPrice:"NA";
                       		child.pastAvg =  $scope.hostingCalculateDto.virtualPublicUnix.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicUnix.pastDealAvgUnitPrice:"NA";
                       		child.compAvg = $scope.hostingCalculateDto.virtualPublicUnix.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicUnix.compDealAvgUnitPrice:"NA";
                       	}else{
                       		child['dataValidation']=true;
				          		child.benchLow ='NA';
				          		child.benchTarget="NA";
				          		 child.pastAvg="NA";
				          		 child.compAvg="NA"
				          	}

                           break;

                     case "1.1.2.2.1": // virtual Private  window/lin virtualPrivateWin
                    	 if($scope.hostingCalculateDto.virtualPrivateWin!=null){
                        		child['dataValidation']=false;
                        		child.benchLow = $scope.hostingCalculateDto.virtualPrivateWin.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateWin.benchDealLowAvgUnitPrice:"NA";
                        		child.benchTarget = $scope.hostingCalculateDto.virtualPrivateWin.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateWin.benchDealTargetAvgUnitPrice:"NA";
                        		child.pastAvg =  $scope.hostingCalculateDto.virtualPrivateWin.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateWin.pastDealAvgUnitPrice:"NA";
                        		child.compAvg = $scope.hostingCalculateDto.virtualPrivateWin.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateWin.compDealAvgUnitPrice:"NA";
                        	}else{
                        		child['dataValidation']=true;
				          		child.benchLow ='NA';
				          		child.benchTarget="NA";
				          		 child.pastAvg="NA";
				          		 child.compAvg="NA"
				          	}

                           break;
                     case "1.1.2.2.2": //virtual private unix
                    	if($scope.hostingCalculateDto.virtualPrivateUnix!=null){
                     		child['dataValidation']=false;
                     		child.benchLow = $scope.hostingCalculateDto.virtualPrivateUnix.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateUnix.benchDealLowAvgUnitPrice:"NA";
                     		child.benchTarget = $scope.hostingCalculateDto.virtualPrivateUnix.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateUnix.benchDealTargetAvgUnitPrice:"NA";
                     		child.pastAvg =  $scope.hostingCalculateDto.virtualPrivateUnix.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateUnix.pastDealAvgUnitPrice:"NA";
                     		child.compAvg = $scope.hostingCalculateDto.virtualPrivateUnix.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateUnix.compDealAvgUnitPrice:"NA";
                     	}else{
                     		child['dataValidation']=true;
				          		child.benchLow ='NA';
				          		child.benchTarget="NA";
				          		 child.pastAvg="NA";
				          		 child.compAvg="NA"
				          	}

                           break;

                           /*   grand end */

                     case "1.1.1.1.1": //samll - WinLinux Physical Server
                    	  if($scope.hostingCalculateDto.physicalWinSmall!=null){
                      		child['dataValidation']=false;
                      		child.benchLow = $scope.hostingCalculateDto.physicalWinSmall.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalWinSmall.benchDealLowAvgUnitPrice:"NA";
                      		child.benchTarget = $scope.hostingCalculateDto.physicalWinSmall.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalWinSmall.benchDealTargetAvgUnitPrice:"NA";
                      		child.pastAvg =  $scope.hostingCalculateDto.physicalWinSmall.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalWinSmall.pastDealAvgUnitPrice:"NA";
                      		child.compAvg = $scope.hostingCalculateDto.physicalWinSmall.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalWinSmall.compDealAvgUnitPrice:"NA";
                      	}else{
                      		child['dataValidation']=true;
				          		child.benchLow ='NA';
				          		child.benchTarget="NA";
				          		 child.pastAvg="NA";
				          		 child.compAvg="NA"
				          	}
                         break;

                     case "1.1.1.1.2": //medium - WinLinux Physical Server
                    	 if($scope.hostingCalculateDto.physicalWinMedium!=null){
                       		child['dataValidation']=false;
                       		child.benchLow = $scope.hostingCalculateDto.physicalWinMedium.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalWinMedium.benchDealLowAvgUnitPrice:"NA";
                       		child.benchTarget = $scope.hostingCalculateDto.physicalWinMedium.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalWinMedium.benchDealTargetAvgUnitPrice:"NA";
                       		child.pastAvg =  $scope.hostingCalculateDto.physicalWinMedium.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalWinMedium.pastDealAvgUnitPrice:"NA";
                       		child.compAvg = $scope.hostingCalculateDto.physicalWinMedium.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalWinMedium.compDealAvgUnitPrice:"NA";
                       	}else{
                       		child['dataValidation']=true;
				          		child.benchLow ='NA';
				          		child.benchTarget="NA";
				          		 child.pastAvg="NA";
				          		 child.compAvg="NA"
				          	}
                         break;

                     case "1.1.1.1.3": //Large - WinLinux Physical Server
                    	 if($scope.hostingCalculateDto.physicalWinLarge!=null){
                        		child['dataValidation']=false;
                        		child.benchLow = $scope.hostingCalculateDto.physicalWinLarge.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalWinLarge.benchDealLowAvgUnitPrice:"NA";
                        		child.benchTarget = $scope.hostingCalculateDto.physicalWinLarge.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalWinLarge.benchDealTargetAvgUnitPrice:"NA";
                        		child.pastAvg =  $scope.hostingCalculateDto.physicalWinLarge.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalWinLarge.pastDealAvgUnitPrice:"NA";
                        		child.compAvg = $scope.hostingCalculateDto.physicalWinLarge.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalWinLarge.compDealAvgUnitPrice:"NA";
                        	}else{
                        		child['dataValidation']=true;
				          		child.benchLow ='NA';
				          		child.benchTarget="NA";
				          		 child.pastAvg="NA";
				          		 child.compAvg="NA"
				          	}
                         break;

                     case "1.1.1.2.1": //samll - Unix Physical Server
                    	 if($scope.hostingCalculateDto.physicalUnixSmall!=null){
                     		child['dataValidation']=false;
                     		child.benchLow = $scope.hostingCalculateDto.physicalUnixSmall.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalUnixSmall.benchDealLowAvgUnitPrice:"NA";
                     		child.benchTarget = $scope.hostingCalculateDto.physicalUnixSmall.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalUnixSmall.benchDealTargetAvgUnitPrice:"NA";
                     		child.pastAvg =  $scope.hostingCalculateDto.physicalUnixSmall.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalUnixSmall.pastDealAvgUnitPrice:"NA";
                     		child.compAvg = $scope.hostingCalculateDto.physicalUnixSmall.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalUnixSmall.compDealAvgUnitPrice:"NA";
                     	}else{
                     		child['dataValidation']=true;
				          		child.benchLow ='NA';
				          		child.benchTarget="NA";
				          		 child.pastAvg="NA";
				          		 child.compAvg="NA"
				          	}
                         break;

                     case "1.1.1.2.2": //medium - Unix Physical Server
                    	 if($scope.hostingCalculateDto.physicalUnixMedium!=null){
                      		child['dataValidation']=false;
                      		child.benchLow = $scope.hostingCalculateDto.physicalUnixMedium.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalUnixMedium.benchDealLowAvgUnitPrice:"NA";
                      		child.benchTarget = $scope.hostingCalculateDto.physicalUnixMedium.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalUnixMedium.benchDealTargetAvgUnitPrice:"NA";
                      		child.pastAvg =  $scope.hostingCalculateDto.physicalUnixMedium.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalUnixMedium.pastDealAvgUnitPrice:"NA";
                      		child.compAvg = $scope.hostingCalculateDto.physicalUnixMedium.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalUnixMedium.compDealAvgUnitPrice:"NA";
                      	}else{
                      		child['dataValidation']=true;
				          		child.benchLow ='NA';
				          		child.benchTarget="NA";
				          		 child.pastAvg="NA";
				          		 child.compAvg="NA"
				          	}
                         break;

                     case "1.1.1.2.3": //Large - Unix Physical Server
                    	  if($scope.hostingCalculateDto.physicalUnixLarge!=null){
                       		child['dataValidation']=false;
                       		child.benchLow = $scope.hostingCalculateDto.physicalUnixLarge.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalUnixLarge.benchDealLowAvgUnitPrice:"NA";
                       		child.benchTarget = $scope.hostingCalculateDto.physicalUnixLarge.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalUnixLarge.benchDealTargetAvgUnitPrice:"NA";
                       		child.pastAvg =  $scope.hostingCalculateDto.physicalUnixLarge.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalUnixLarge.pastDealAvgUnitPrice:"NA";
                       		child.compAvg = $scope.hostingCalculateDto.physicalUnixLarge.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.physicalUnixLarge.compDealAvgUnitPrice:"NA";
                       	}else{
                       		child['dataValidation']=true;
				          		child.benchLow ='NA';
				          		child.benchTarget="NA";
				          		 child.pastAvg="NA";
				          		 child.compAvg="NA"
				          	}
                         break;

                       //        physical end here ***********************Virtual start

                     case "1.1.2.2.1.1": //samll - WinLinux Private -Virtual Server

                    	 if($scope.hostingCalculateDto.virtualPrivateWinSmall!=null){
                    		 child['dataValidation']=false;
                        		child.benchLow = $scope.hostingCalculateDto.virtualPrivateWinSmall.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateWinSmall.benchDealLowAvgUnitPrice:"NA";
                        		child.benchTarget = $scope.hostingCalculateDto.virtualPrivateWinSmall.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateWinSmall.benchDealTargetAvgUnitPrice:"NA";
                        		child.pastAvg =  $scope.hostingCalculateDto.virtualPrivateWinSmall.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateWinSmall.pastDealAvgUnitPrice:"NA";
                        		child.compAvg = $scope.hostingCalculateDto.virtualPrivateWinSmall.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateWinSmall.compDealAvgUnitPrice:"NA";
                        	}else{
                        		child['dataValidation']=true;
          		          		child.benchLow ='NA';
          		          		child.benchTarget="NA";
          		          		 child.pastAvg="NA";
          		          		 child.compAvg="NA"
          		          	}
                         break;

                     case "1.1.2.2.1.2": //medium - WinLinux Private -Virtual Server
                    	if($scope.hostingCalculateDto.virtualPrivateWinMedium!=null){
                    		child['dataValidation']=false;
                     		child.benchLow = $scope.hostingCalculateDto.virtualPrivateWinMedium.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateWinMedium.benchDealLowAvgUnitPrice:"NA";
                     		child.benchTarget = $scope.hostingCalculateDto.virtualPrivateWinMedium.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateWinMedium.benchDealTargetAvgUnitPrice:"NA";
                     		child.pastAvg =  $scope.hostingCalculateDto.virtualPrivateWinMedium.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateWinMedium.pastDealAvgUnitPrice:"NA";
                     		child.compAvg = $scope.hostingCalculateDto.virtualPrivateWinMedium.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateWinMedium.compDealAvgUnitPrice:"NA";
                     	}else{
                     		child['dataValidation']=true;
          		          		child.benchLow ='NA';
          		          		child.benchTarget="NA";
          		          		 child.pastAvg="NA";
          		          		 child.compAvg="NA"
          		          	}
                         break;

                     case "1.1.2.2.1.3": //Large - WinLinux -Private -Virtual Server
                    	if($scope.hostingCalculateDto.virtualPrivateWinLarge!=null){
                    		child['dataValidation']=false;
                      		child.benchLow = $scope.hostingCalculateDto.virtualPrivateWinLarge.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateWinLarge.benchDealLowAvgUnitPrice:"NA";
                      		child.benchTarget = $scope.hostingCalculateDto.virtualPrivateWinLarge.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateWinLarge.benchDealTargetAvgUnitPrice:"NA";
                      		child.pastAvg =  $scope.hostingCalculateDto.virtualPrivateWinLarge.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateWinLarge.pastDealAvgUnitPrice:"NA";
                      		child.compAvg = $scope.hostingCalculateDto.virtualPrivateWinLarge.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateWinLarge.compDealAvgUnitPrice:"NA";
                      	}else{
                      		child['dataValidation']=true;
          		          		child.benchLow ='NA';
          		          		child.benchTarget="NA";
          		          		 child.pastAvg="NA";
          		          		 child.compAvg="NA"
          		          	}
                         break;

                     case "1.1.2.2.2.1": //Unix -  small  -Private -Virtual Server virtualPrivateUnixSmall
                    	  if($scope.hostingCalculateDto.virtualPrivateUnixSmall!=null){
                    		  child['dataValidation']=false;
                       		child.benchLow = $scope.hostingCalculateDto.virtualPrivateUnixSmall.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateUnixSmall.benchDealLowAvgUnitPrice:"NA";
                       		child.benchTarget = $scope.hostingCalculateDto.virtualPrivateUnixSmall.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateUnixSmall.benchDealTargetAvgUnitPrice:"NA";
                       		child.pastAvg =  $scope.hostingCalculateDto.virtualPrivateUnixSmall.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateUnixSmall.pastDealAvgUnitPrice:"NA";
                       		child.compAvg = $scope.hostingCalculateDto.virtualPrivateUnixSmall.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateUnixSmall.compDealAvgUnitPrice:"NA";
                       	}else{
                       		child['dataValidation']=true;
          		          		child.benchLow ='NA';
          		          		child.benchTarget="NA";
          		          		 child.pastAvg="NA";
          		          		 child.compAvg="NA"
          		          	}
                         break;

                     case "1.1.2.2.2.2": //Unix -  medium  -Private -Virtual Server
                    	if($scope.hostingCalculateDto.virtualPrivateUnixMedium!=null){
                    		child['dataValidation']=false;
                        		child.benchLow = $scope.hostingCalculateDto.virtualPrivateUnixMedium.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateUnixMedium.benchDealLowAvgUnitPrice:"NA";
                        		child.benchTarget = $scope.hostingCalculateDto.virtualPrivateUnixMedium.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateUnixMedium.benchDealTargetAvgUnitPrice:"NA";
                        		child.pastAvg =  $scope.hostingCalculateDto.virtualPrivateUnixMedium.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateUnixMedium.pastDealAvgUnitPrice:"NA";
                        		child.compAvg = $scope.hostingCalculateDto.virtualPrivateUnixMedium.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateUnixMedium.compDealAvgUnitPrice:"NA";
                        	}else{
                        		child['dataValidation']=true;
          		          		child.benchLow ='NA';
          		          		child.benchTarget="NA";
          		          		 child.pastAvg="NA";
          		          		 child.compAvg="NA"
          		          	}
                         break;

                     case "1.1.2.2.2.3": //Unix -  Large  -Private -Virtual Server
                    	 if($scope.hostingCalculateDto.virtualPrivateUnixLarge!=null){
                    		 child['dataValidation']=false;
                     		child.benchLow = $scope.hostingCalculateDto.virtualPrivateUnixLarge.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateUnixLarge.benchDealLowAvgUnitPrice:"NA";
                     		child.benchTarget = $scope.hostingCalculateDto.virtualPrivateUnixLarge.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateUnixLarge.benchDealTargetAvgUnitPrice:"NA";
                     		child.pastAvg =  $scope.hostingCalculateDto.virtualPrivateUnixLarge.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateUnixLarge.pastDealAvgUnitPrice:"NA";
                     		child.compAvg = $scope.hostingCalculateDto.virtualPrivateUnixLarge.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPrivateUnixLarge.compDealAvgUnitPrice:"NA";
                     	}else{
                     		child['dataValidation']=true;
          		          		child.benchLow ='NA';
          		          		child.benchTarget="NA";
          		          		 child.pastAvg="NA";
          		          		 child.compAvg="NA"
          		          	}
                         break;

                     case "1.1.2.1.1.1": //samll - WinLinux virtualPublicWinSmall
                    	  if($scope.hostingCalculateDto.virtualPublicWinSmall!=null){
                    		  child['dataValidation']=false;
                      		child.benchLow = $scope.hostingCalculateDto.virtualPublicWinSmall.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicWinSmall.benchDealLowAvgUnitPrice:"NA";
                      		child.benchTarget = $scope.hostingCalculateDto.virtualPublicWinSmall.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicWinSmall.benchDealTargetAvgUnitPrice:"NA";
                      		child.pastAvg =  $scope.hostingCalculateDto.virtualPublicWinSmall.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicWinSmall.pastDealAvgUnitPrice:"NA";
                      		child.compAvg = $scope.hostingCalculateDto.virtualPublicWinSmall.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicWinSmall.compDealAvgUnitPrice:"NA";
                      	}else{
                      		child['dataValidation']=true;
          		          		child.benchLow ='NA';
          		          		child.benchTarget="NA";
          		          		 child.pastAvg="NA";
          		          		 child.compAvg="NA"
          		          	}
                         break;

                     case "1.1.2.1.1.2": //medium - WinLinux virtualPublicWinMedium
                    	  if($scope.hostingCalculateDto.virtualPublicWinMedium!=null){
                    		  child['dataValidation']=false;
                       		child.benchLow = $scope.hostingCalculateDto.virtualPublicWinMedium.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicWinMedium.benchDealLowAvgUnitPrice:"NA";
                       		child.benchTarget = $scope.hostingCalculateDto.virtualPublicWinMedium.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicWinMedium.benchDealTargetAvgUnitPrice:"NA";
                       		child.pastAvg =  $scope.hostingCalculateDto.virtualPublicWinMedium.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicWinMedium.pastDealAvgUnitPrice:"NA";
                       		child.compAvg = $scope.hostingCalculateDto.virtualPublicWinMedium.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicWinMedium.compDealAvgUnitPrice:"NA";
                       	}else{
                       		child['dataValidation']=true;
          		          		child.benchLow ='NA';
          		          		child.benchTarget="NA";
          		          		 child.pastAvg="NA";
          		          		 child.compAvg="NA"
          		          	}
                         break;

                     case "1.1.2.1.1.3": //Large - WinLinux
                    	 if($scope.hostingCalculateDto.virtualPublicWinLarge!=null){
                    		 child['dataValidation']=false;
                        		child.benchLow = $scope.hostingCalculateDto.virtualPublicWinLarge.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicWinLarge.benchDealLowAvgUnitPrice:"NA";
                        		child.benchTarget = $scope.hostingCalculateDto.virtualPublicWinLarge.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicWinLarge.benchDealTargetAvgUnitPrice:"NA";
                        		child.pastAvg =  $scope.hostingCalculateDto.virtualPublicWinLarge.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicWinLarge.pastDealAvgUnitPrice:"NA";
                        		child.compAvg = $scope.hostingCalculateDto.virtualPublicWinLarge.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicWinLarge.compDealAvgUnitPrice:"NA";
                        	}else{
                        		child['dataValidation']=true;
          		          		child.benchLow ='NA';
          		          		child.benchTarget="NA";
          		          		 child.pastAvg="NA";
          		          		 child.compAvg="NA"
          		          	}
                         break;

                     case "1.1.2.1.2.1": //small -  Unix Public Virtual Server virtualPublicUnixSmall
                    	if($scope.hostingCalculateDto.virtualPublicUnixSmall!=null){
                    		child['dataValidation']=false;
                     		child.benchLow = $scope.hostingCalculateDto.virtualPublicUnixSmall.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicUnixSmall.benchDealLowAvgUnitPrice:"NA";
                     		child.benchTarget = $scope.hostingCalculateDto.virtualPublicUnixSmall.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicUnixSmall.benchDealTargetAvgUnitPrice:"NA";
                     		child.pastAvg =  $scope.hostingCalculateDto.virtualPublicUnixSmall.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicUnixSmall.pastDealAvgUnitPrice:"NA";
                     		child.compAvg = $scope.hostingCalculateDto.virtualPublicUnixSmall.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicUnixSmall.compDealAvgUnitPrice:"NA";
                     	}else{
                     		child['dataValidation']=true;
          		          		child.benchLow ='NA';
          		          		child.benchTarget="NA";
          		          		 child.pastAvg="NA";
          		          		 child.compAvg="NA"
          		          	}
                         break;

                     case "1.1.2.1.2.2": //Medium - Unix Public Virtual Server virtualPublicUnixMedium
                    	  if($scope.hostingCalculateDto.virtualPublicUnixMedium!=null){
                    		  child['dataValidation']=false;
                      		child.benchLow = $scope.hostingCalculateDto.virtualPublicUnixMedium.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicUnixMedium.benchDealLowAvgUnitPrice:"NA";
                      		child.benchTarget = $scope.hostingCalculateDto.virtualPublicUnixMedium.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicUnixMedium.benchDealTargetAvgUnitPrice:"NA";
                      		child.pastAvg =  $scope.hostingCalculateDto.virtualPublicUnixMedium.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicUnixMedium.pastDealAvgUnitPrice:"NA";
                      		child.compAvg = $scope.hostingCalculateDto.virtualPublicUnixMedium.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicUnixMedium.compDealAvgUnitPrice:"NA";
                      	}else{
                      		child['dataValidation']=true;
          		          		child.benchLow ='NA';
          		          		child.benchTarget="NA";
          		          		 child.pastAvg="NA";
          		          		 child.compAvg="NA"
          		          	}
                         break;

                     case "1.1.2.1.2.3": //Large - Unix Public Virtual Server
                    	if($scope.hostingCalculateDto.virtualPublicUnixLarge!=null){
                    		 child['dataValidation']=false;
                       		child.benchLow = $scope.hostingCalculateDto.virtualPublicUnixLarge.benchDealLowAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicUnixLarge.benchDealLowAvgUnitPrice:"NA";
                       		child.benchTarget = $scope.hostingCalculateDto.virtualPublicUnixLarge.benchDealTargetAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicUnixLarge.benchDealTargetAvgUnitPrice:"NA";
                       		child.pastAvg =  $scope.hostingCalculateDto.virtualPublicUnixLarge.pastDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicUnixLarge.pastDealAvgUnitPrice:"NA";
                       		child.compAvg = $scope.hostingCalculateDto.virtualPublicUnixLarge.compDealAvgUnitPrice!=null?$scope.hostingCalculateDto.virtualPublicUnixLarge.compDealAvgUnitPrice:"NA";
                       	}else{
                       		child['dataValidation']=true;
          		          		child.benchLow ='NA';
          		          		child.benchTarget="NA";
          		          		 child.pastAvg="NA";
          		          		 child.compAvg="NA"
          		          	}
                         break;


           }



              if(child.children != null){
            	  extractChildLevelWiseAverages(child.children);
              }
        }
     }



   // on selecting the radio button of calculate
	  $scope.createUnitPrice=function(child,key)
	  {
			switch (key) {
		    case 'pastAvg':// pastAvg
		    	switch (child.id) {
		    	  case "1.1": //Server Volume
		    		   $scope.past=$scope.hostingCalculateDto.servers.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                          break;

                  case "1.1.1": //Physical Server
                	   $scope.past=$scope.hostingCalculateDto.physical.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                         break;

                   case "1.1.2": //Virtual Server
                	   $scope.past=$scope.hostingCalculateDto.virtual.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                         break;

                   case "1.1.1.1": //Physical Windows/Linux
                	   $scope.past=$scope.hostingCalculateDto.physicalWin.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
			             break;

                   case "1.1.1.2": // Physical Unix
                	   $scope.past=$scope.hostingCalculateDto.physicalUnix.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                         break;

                   case "1.1.2.1": // virtual Public
                	   $scope.past=$scope.hostingCalculateDto.virtualPublic.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);

                         break;
                   case "1.1.2.2": //virtual private
                	   $scope.past=$scope.hostingCalculateDto.virtualPrivate.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                         break;

                   case "1.1.2.1.1": // virtual Public window/lin
                	   $scope.past=$scope.hostingCalculateDto.virtualPublicWin.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                         break;

                   case "1.1.2.1.2": //virtual Public unix
                	   $scope.past=$scope.hostingCalculateDto.virtualPublicUnix.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                         break;

                   case "1.1.2.2.1": // virtual Private  window/lin virtualPrivateWin
                	   $scope.past=$scope.hostingCalculateDto.virtualPrivateWin.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                         break;

                   case "1.1.2.2.2": //virtual private unix
                	   $scope.past=$scope.hostingCalculateDto.virtualPrivateUnix.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                         break;


                   case "1.1.1.1.1": //samll - WinLinux Physical Server
                	   $scope.past=$scope.hostingCalculateDto.physicalWinSmall.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                       break;

                   case "1.1.1.1.2": //medium - WinLinux Physical Server
                	   $scope.past=$scope.hostingCalculateDto.physicalWinMedium.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                       break;

                   case "1.1.1.1.3": //Large - WinLinux Physical Server
                	   $scope.past=$scope.hostingCalculateDto.physicalWinLarge.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                       break;

                   case "1.1.1.2.1": //samll - Unix Physical Server
                	   $scope.past=$scope.hostingCalculateDto.physicalUnixSmall.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                       break;

                   case "1.1.1.2.2": //medium - Unix Physical Server
                	   $scope.past=$scope.hostingCalculateDto.physicalUnixMedium.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                       break;

                   case "1.1.1.2.3": //Large - Unix Physical Server
                	   $scope.past=$scope.hostingCalculateDto.physicalUnixLarge.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                       break;

                   case "1.1.2.2.1.1": //samll - WinLinux Private -Virtual Server
                	   $scope.past=$scope.hostingCalculateDto.virtualPrivateWinSmall.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                       break;

                   case "1.1.2.2.1.2": //medium - WinLinux Private -Virtual Server
                	   $scope.past=$scope.hostingCalculateDto.virtualPrivateWinMedium.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
				    	 break;
                   case "1.1.2.2.1.3": //Large - WinLinux -Private -Virtual Server
                	   $scope.past=$scope.hostingCalculateDto.virtualPrivateWinLarge.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                       break;

                   case "1.1.2.2.2.1": //Unix -  small  -Private -Virtual Server virtualPrivateUnixSmall
                	   $scope.past=$scope.hostingCalculateDto.virtualPrivateUnixSmall.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                       break;

                   case "1.1.2.2.2.2": //Unix -  medium  -Private -Virtual Server
                	   $scope.past=$scope.hostingCalculateDto.virtualPrivateUnixMedium.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                       break;

                   case "1.1.2.2.2.3": //Unix -  Large  -Private -Virtual Server
                	   $scope.past=$scope.hostingCalculateDto.virtualPrivateUnixLarge.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                       break;

                   case "1.1.2.1.1.1": //samll - WinLinux virtualPublicWinSmall
                	   $scope.past=$scope.hostingCalculateDto.virtualPublicWinSmall.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                       break;

                   case "1.1.2.1.1.2": //medium - WinLinux virtualPublicWinMedium
                	   $scope.past=$scope.hostingCalculateDto.virtualPublicWinMedium.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                       break;

                   case "1.1.2.1.1.3": //Large - WinLinux
                	   $scope.past=$scope.hostingCalculateDto.virtualPublicWinLarge.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                       break;

                   case "1.1.2.1.2.1": //small -  Unix Public Virtual Server virtualPublicUnixSmall
                	   $scope.past=$scope.hostingCalculateDto.virtualPublicUnixSmall.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                       break;

                   case "1.1.2.1.2.2": //Medium - Unix Public Virtual Server virtualPublicUnixMedium
                	   $scope.past=$scope.hostingCalculateDto.virtualPublicUnixMedium.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                       break;

                   case "1.1.2.1.2.3": //Large - Unix Public Virtual Server
                	   $scope.past=$scope.hostingCalculateDto.virtualPublicUnixLarge.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
                       break;
                   case "2.1":
                	   $scope.past=$scope.hostingCalculateDto.sqlInstances.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
				    	break;
                     case "2.2":
                	   $scope.past=$scope.hostingCalculateDto.cotsInstallations.pastDealYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.past);
				    	break;
		    	}
		        break;

		    case 'benchLow':// benchLow
		    	switch (child.id) {
		   	  case "1.1": //Server Volume
	    		   $scope.benchLow=$scope.hostingCalculateDto.servers.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                     break;
             case "1.1.1": //Physical Server
            	 $scope.benchLow=$scope.hostingCalculateDto.physical.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                    break;
              case "1.1.2": //Virtual Server
            	  $scope.benchLow=$scope.hostingCalculateDto.virtual.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                    break;
              case "1.1.1.1": //Physical Windows/Linux
            	  $scope.benchLow=$scope.hostingCalculateDto.physicalWin.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
		            break;
              case "1.1.1.2": // Physical Unix
            	  $scope.benchLow=$scope.hostingCalculateDto.physicalUnix.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                    break;
              case "1.1.2.1": // virtual Public
            	  $scope.benchLow=$scope.hostingCalculateDto.virtualPublic.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                    break;
              case "1.1.2.2": //virtual private
            	  $scope.benchLow=$scope.hostingCalculateDto.virtualPrivate.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                    break;
                /*   grand*/
              case "1.1.2.1.1": // virtual Public window/lin
            	  $scope.benchLow=$scope.hostingCalculateDto.virtualPublicWin.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                    break;
              case "1.1.2.1.2": //virtual Public unix
            	  $scope.benchLow=$scope.hostingCalculateDto.virtualPublicUnix.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                    break;
              case "1.1.2.2.1": // virtual Private  window/lin virtualPrivateWin
            	  $scope.benchLow=$scope.hostingCalculateDto.virtualPrivateWin.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                    break;
              case "1.1.2.2.2": //virtual private unix
            	  $scope.benchLow=$scope.hostingCalculateDto.virtualPrivateUnix.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                    break;
                    /*   grand end */
              case "1.1.1.1.1": //samll - WinLinux Physical Server
            	  $scope.benchLow=$scope.hostingCalculateDto.physicalWinSmall.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                  break;
              case "1.1.1.1.2": //medium - WinLinux Physical Server
            	  $scope.benchLow=$scope.hostingCalculateDto.physicalWinMedium.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                  break;
              case "1.1.1.1.3": //Large - WinLinux Physical Server
            	  $scope.benchLow=$scope.hostingCalculateDto.physicalWinLarge.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                    break;
              case "1.1.1.2.1": //samll - Unix Physical Server
            	  $scope.benchLow=$scope.hostingCalculateDto.physicalUnixSmall.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                  break;
              case "1.1.1.2.2": //medium - Unix Physical Server
            	  $scope.benchLow=$scope.hostingCalculateDto.physicalUnixMedium.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                  break;
              case "1.1.1.2.3": //Large - Unix Physical Server
            	  $scope.benchLow=$scope.hostingCalculateDto.physicalUnixLarge.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                  break;
                //        physical end here ***********************Virtual start
              case "1.1.2.2.1.1": //samll - WinLinux Private -Virtual Server
            	  $scope.benchLow=$scope.hostingCalculateDto.virtualPrivateWinSmall.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                  break;
              case "1.1.2.2.1.2": //medium - WinLinux Private -Virtual Server
            	  $scope.benchLow=$scope.hostingCalculateDto.virtualPrivateWinMedium.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
			    	break;
              case "1.1.2.2.1.3": //Large - WinLinux -Private -Virtual Server
            	  $scope.benchLow=$scope.hostingCalculateDto.virtualPrivateWinLarge.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                  break;
              case "1.1.2.2.2.1": //Unix -  small  -Private -Virtual Server virtualPrivateUnixSmall
            	  $scope.benchLow=$scope.hostingCalculateDto.virtualPrivateUnixSmall.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                  break;
              case "1.1.2.2.2.2": //Unix -  medium  -Private -Virtual Server
            	  $scope.benchLow=$scope.hostingCalculateDto.virtualPrivateUnixMedium.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                  break;
              case "1.1.2.2.2.3": //Unix -  Large  -Private -Virtual Server
            	  $scope.benchLow=$scope.hostingCalculateDto.virtualPrivateUnixLarge.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                  break;
              case "1.1.2.1.1.1": //samll - WinLinux virtualPublicWinSmall
            	  $scope.benchLow=$scope.hostingCalculateDto.virtualPublicWinSmall.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                  break;
              case "1.1.2.1.1.2": //medium - WinLinux virtualPublicWinMedium
            	  $scope.benchLow=$scope.hostingCalculateDto.virtualPublicWinMedium.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
                  break;
              case "1.1.2.1.1.3": //Large - WinLinux
            	  $scope.benchLow=$scope.hostingCalculateDto.virtualPublicWinLarge.benchmarkLowYearlyCalcDtoList;
			     $scope.setUnitPriceForLevels(child,$scope.benchLow);
                  break;
              case "1.1.2.1.2.1": //small -  Unix Public Virtual Server virtualPublicUnixSmall
            	  $scope.benchLow=$scope.hostingCalculateDto.virtualPublicUnixSmall.benchmarkLowYearlyCalcDtoList;
 			     $scope.setUnitPriceForLevels(child,$scope.benchLow);
                  break;
              case "1.1.2.1.2.2": //Medium - Unix Public Virtual Server virtualPublicUnixMedium
            	  $scope.benchLow=$scope.hostingCalculateDto.virtualPublicUnixMedium.benchmarkLowYearlyCalcDtoList;
  			     $scope.setUnitPriceForLevels(child,$scope.benchLow);
                  break;
              case "1.1.2.1.2.3": //Large - Unix Public Virtual Server
            	  $scope.benchLow=$scope.hostingCalculateDto.virtualPublicUnixLarge.benchmarkLowYearlyCalcDtoList;
   			     $scope.setUnitPriceForLevels(child,$scope.benchLow);
                  break;
                case "2.1":
             	   $scope.benchLow=$scope.hostingCalculateDto.sqlInstances.benchmarkLowYearlyCalcDtoList;
   			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
   			    	break;
                  case "2.2":
             	   $scope.benchLow=$scope.hostingCalculateDto.cotsInstallations.benchmarkLowYearlyCalcDtoList;
   			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
   			    	break;
		    }
		    	  break;
		    case 'benchTarget':// compAvg
		    	switch (child.id) {
		  	  case "1.1": //Server Volume
	    		   $scope.benchTarget=$scope.hostingCalculateDto.servers.benchmarkTargetYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchTarget);
                    break;
            case "1.1.1": //Physical Server
           	 $scope.benchTarget=$scope.hostingCalculateDto.physical.benchmarkTargetYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchTarget);
                   break;
             case "1.1.2": //Virtual Server
           	  $scope.benchTarget=$scope.hostingCalculateDto.virtual.benchmarkTargetYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchTarget);
                   break;
             case "1.1.1.1": //Physical Windows/Linux
           	  $scope.benchTarget=$scope.hostingCalculateDto.physicalWin.benchmarkTargetYearlyCalcDtoList;
			   $scope.setUnitPriceForLevels(child,$scope.benchTarget);
		       break;
             case "1.1.1.2": // Physical Unix
           	  $scope.benchTarget=$scope.hostingCalculateDto.physicalUnix.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;
             case "1.1.2.1": // virtual Public
           	  $scope.benchTarget=$scope.hostingCalculateDto.virtualPublic.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;
             case "1.1.2.2": //virtual private
           	  $scope.benchTarget=$scope.hostingCalculateDto.virtualPrivate.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;
               /*   grand*/
             case "1.1.2.1.1": // virtual Public window/lin
           	  $scope.benchTarget=$scope.hostingCalculateDto.virtualPublicWin.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;
             case "1.1.2.1.2": //virtual Public unix
           	  $scope.benchTarget=$scope.hostingCalculateDto.virtualPublicUnix.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;

             case "1.1.2.2.1": // virtual Private  window/lin virtualPrivateWin
           	  $scope.benchTarget=$scope.hostingCalculateDto.virtualPrivateWin.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;
             case "1.1.2.2.2": //virtual private unix
           	  $scope.benchTarget=$scope.hostingCalculateDto.virtualPrivateUnix.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;
                   /*   grand end */

             case "1.1.1.1.1": //samll - WinLinux Physical Server
           	  $scope.benchTarget=$scope.hostingCalculateDto.physicalWinSmall.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;

             case "1.1.1.1.2": //medium - WinLinux Physical Server
           	  $scope.benchTarget=$scope.hostingCalculateDto.physicalWinMedium.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;

             case "1.1.1.1.3": //Large - WinLinux Physical Server
           	  $scope.benchTarget=$scope.hostingCalculateDto.physicalWinLarge.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;

             case "1.1.1.2.1": //samll - Unix Physical Server
           	  $scope.benchTarget=$scope.hostingCalculateDto.physicalUnixSmall.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;

             case "1.1.1.2.2": //medium - Unix Physical Server
           	  $scope.benchTarget=$scope.hostingCalculateDto.physicalUnixMedium.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;

             case "1.1.1.2.3": //Large - Unix Physical Server
           	  $scope.benchTarget=$scope.hostingCalculateDto.physicalUnixLarge.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;

               //        physical end here ***********************Virtual start

             case "1.1.2.2.1.1": //samll - WinLinux Private -Virtual Server
           	  $scope.benchTarget=$scope.hostingCalculateDto.virtualPrivateWinSmall.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;

             case "1.1.2.2.1.2": //medium - WinLinux Private -Virtual Server
           	  $scope.benchTarget=$scope.hostingCalculateDto.virtualPrivateWinMedium.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
			  break;
             case "1.1.2.2.1.3": //Large - WinLinux -Private -Virtual Server
           	  $scope.benchTarget=$scope.hostingCalculateDto.virtualPrivateWinLarge.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;

             case "1.1.2.2.2.1": //Unix -  small  -Private -Virtual Server virtualPrivateUnixSmall
           	  $scope.benchTarget=$scope.hostingCalculateDto.virtualPrivateUnixSmall.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;

             case "1.1.2.2.2.2": //Unix -  medium  -Private -Virtual Server
           	  $scope.benchTarget=$scope.hostingCalculateDto.virtualPrivateUnixMedium.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;

             case "1.1.2.2.2.3": //Unix -  Large  -Private -Virtual Server
           	  $scope.benchTarget=$scope.hostingCalculateDto.virtualPrivateUnixLarge.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;

             case "1.1.2.1.1.1": //samll - WinLinux virtualPublicWinSmall
           	  $scope.benchTarget=$scope.hostingCalculateDto.virtualPublicWinSmall.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;

             case "1.1.2.1.1.2": //medium - WinLinux virtualPublicWinMedium
           	  $scope.benchTarget=$scope.hostingCalculateDto.virtualPublicWinMedium.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;

             case "1.1.2.1.1.3": //Large - WinLinux
           	  $scope.benchTarget=$scope.hostingCalculateDto.virtualPublicWinLarge.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;

             case "1.1.2.1.2.1": //small -  Unix Public Virtual Server virtualPublicUnixSmall
          	   $scope.benchTarget=$scope.hostingCalculateDto.virtualPublicUnixSmall.benchmarkTargetYearlyCalcDtoList;
			   $scope.setUnitPriceForLevels(child,$scope.benchTarget);
               break;

             case "1.1.2.1.2.2": //Medium - Unix Public Virtual Server virtualPublicUnixMedium
           	  $scope.benchTarget=$scope.hostingCalculateDto.virtualPublicUnixMedium.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;

             case "1.1.2.1.2.3": //Large - Unix Public Virtual Server
           	  $scope.benchTarget=$scope.hostingCalculateDto.virtualPublicUnixLarge.benchmarkTargetYearlyCalcDtoList;
			  $scope.setUnitPriceForLevels(child,$scope.benchTarget);
              break;
             case "2.1":
          	 $scope.benchTarget=$scope.hostingCalculateDto.sqlInstances.benchmarkTargetYearlyCalcDtoList;
			 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
			 break;
               case "2.2":
          	   $scope.benchTarget=$scope.hostingCalculateDto.cotsInstallations.benchmarkTargetYearlyCalcDtoList;
			   $scope.setUnitPriceForLevels(child,$scope.benchTarget);
			   break;
		    	}
		    	break;

			}
	  }

	// function used for set the unit price year wise
	  $scope.setUnitPriceForLevels=function(child,untiPrices)
	  {
		  switch (child.id) {
		    case '1.1':// Server Volume
		    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].servers=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].serversRevenue=untiPrices[i].revenue;
		    		}
		        break;
		    case '2.1':// SQL/Open Source Instances
		    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].sqlInstances=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].sqlInstancesRevenue=untiPrices[i].revenue;
		    		}
		        break;

		    case '2.2':// COTS Installations/Licenses
		    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].cotsInstallations=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].cotsInstallationsRevenue=untiPrices[i].revenue;
		    		}
		        break;

		    case '1.1.1'://Physical Server
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
	    		$scope.unitPriceDto[i].physical=untiPrices[i].unitPrice;
	    		$scope.unitPriceDto[i].physicalRevenue=untiPrices[i].revenue;
	    		$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].physicalRevenue+$scope.unitPriceDto[i].virtualRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.2'://Virtual Server
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
	    		$scope.unitPriceDto[i].virtual=untiPrices[i].unitPrice;
	    		$scope.unitPriceDto[i].virtualRevenue=untiPrices[i].revenue;
	    		$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.1.1'://  p Windows/Linux
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
	    		$scope.unitPriceDto[i].physicalWin=untiPrices[i].unitPrice;
	    		$scope.unitPriceDto[i].physicalWinRevenue=untiPrices[i].revenue;
	    		$scope.unitPriceDto[i].physicalRevenue=$scope.unitPriceDto[i].physicalUnixRevenue+$scope.unitPriceDto[i].physicalWinRevenue;
	    		$scope.unitPriceDto[i].physical=parseFloat($scope.unitPriceDto[i].physicalRevenue/$scope.hostingInfo.serverHosting.children[0].children[0].distributedVolume[i].volume).toFixed(2);
	    		$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.1.2':// p Unix
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
	    		$scope.unitPriceDto[i].physicalUnix=untiPrices[i].unitPrice;
	    		$scope.unitPriceDto[i].physicalUnixRevenue=untiPrices[i].revenue;
	    		$scope.unitPriceDto[i].physicalRevenue=$scope.unitPriceDto[i].physicalUnixRevenue+$scope.unitPriceDto[i].physicalWinRevenue;
	    		$scope.unitPriceDto[i].physical=parseFloat($scope.unitPriceDto[i].physicalRevenue/$scope.hostingInfo.serverHosting.children[0].children[0].distributedVolume[i].volume).toFixed(2);
	    		$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.1.1.1'://P w s
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
	    		$scope.unitPriceDto[i].physicalWinSmall=untiPrices[i].unitPrice;
	    		$scope.unitPriceDto[i].physicalWinSmallRevenue=untiPrices[i].revenue;
	    		$scope.unitPriceDto[i].physicalWinRevenue=$scope.unitPriceDto[i].physicalWinRevenue+$scope.unitPriceDto[i].physicalWinMediumRevenue+$scope.unitPriceDto[i].physicalWinLargeRevenue;
	    		$scope.unitPriceDto[i].physicalWin=parseFloat($scope.unitPriceDto[i].physicalWinRevenue/$scope.hostingInfo.serverHosting.children[0].children[0].children[0].distributedVolume[i].volume).toFixed(2);
	    		$scope.unitPriceDto[i].physicalRevenue=$scope.unitPriceDto[i].physicalUnixRevenue+$scope.unitPriceDto[i].physicalWinRevenue;
	    		$scope.unitPriceDto[i].physical=parseFloat($scope.unitPriceDto[i].physicalRevenue/$scope.hostingInfo.serverHosting.children[0].children[0].distributedVolume[i].volume).toFixed(2);
	    		$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.1.1.2'://P w M
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
	    		$scope.unitPriceDto[i].physicalWinMedium=untiPrices[i].unitPrice;
	    		$scope.unitPriceDto[i].physicalWinMediumRevenue=untiPrices[i].revenue;
	    		$scope.unitPriceDto[i].physicalWinRevenue=$scope.unitPriceDto[i].physicalWinSmallRevenue+$scope.unitPriceDto[i].physicalWinMediumRevenue+$scope.unitPriceDto[i].physicalWinLargeRevenue;
	    		$scope.unitPriceDto[i].physicalWin=parseFloat($scope.unitPriceDto[i].physicalWinRevenue/$scope.hostingInfo.serverHosting.children[0].children[0].children[0].distributedVolume[i].volume).toFixed(2);
	    		$scope.unitPriceDto[i].physicalRevenue=$scope.unitPriceDto[i].physicalUnixRevenue+$scope.unitPriceDto[i].physicalWinRevenue;
	    		$scope.unitPriceDto[i].physical=parseFloat($scope.unitPriceDto[i].physicalRevenue/$scope.hostingInfo.serverHosting.children[0].children[0].distributedVolume[i].volume).toFixed(2);
	    		$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    	    $scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.1.1.3'://P w L
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
	    		$scope.unitPriceDto[i].physicalWinLarge=untiPrices[i].unitPrice;
	    		$scope.unitPriceDto[i].physicalWinLargeRevenue=untiPrices[i].revenue;
	    		$scope.unitPriceDto[i].physicalWinRevenue=$scope.unitPriceDto[i].physicalWinLargeRevenue+$scope.unitPriceDto[i].physicalWinMediumRevenue+$scope.unitPriceDto[i].physicalWinLargeRevenue;
	    		$scope.unitPriceDto[i].physicalWin=parseFloat($scope.unitPriceDto[i].physicalWinRevenue/$scope.hostingInfo.serverHosting.children[0].children[0].children[0].distributedVolume[i].volume).toFixed(2);
	    		$scope.unitPriceDto[i].physicalRevenue=$scope.unitPriceDto[i].physicalUnixRevenue+$scope.unitPriceDto[i].physicalWinRevenue;
	    		$scope.unitPriceDto[i].physical=parseFloat($scope.unitPriceDto[i].physicalRevenue/$scope.hostingInfo.serverHosting.children[0].children[0].distributedVolume[i].volume).toFixed(2);
	    		$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.1.2.1'://P U S
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
	    		$scope.unitPriceDto[i].physicalUnixSmall=untiPrices[i].unitPrice;
	    		$scope.unitPriceDto[i].physicalUnixSmallRevenue=untiPrices[i].revenue;
	    		$scope.unitPriceDto[i].physicalUnixRevenue=$scope.unitPriceDto[i].physicalUnixSmallRevenue+$scope.unitPriceDto[i].physicalUnixMediumRevenue+$scope.unitPriceDto[i].physicalUnixLargeRevenue;
	    		$scope.unitPriceDto[i].physicalUnix=parseFloat($scope.unitPriceDto[i].physicalUnixRevenue/$scope.hostingInfo.serverHosting.children[0].children[0].children[1].distributedVolume[i].volume).toFixed(2)
	    		$scope.unitPriceDto[i].physicalRevenue=$scope.unitPriceDto[i].physicalUnixRevenue+$scope.unitPriceDto[i].physicalWinRevenue;
	    		$scope.unitPriceDto[i].physical=parseFloat($scope.unitPriceDto[i].physicalRevenue/$scope.hostingInfo.serverHosting.children[0].children[0].distributedVolume[i].volume).toFixed(2);
	    		$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.1.2.2'://P U M
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
	    		$scope.unitPriceDto[i].physicalUnixMedium=untiPrices[i].unitPrice;
	    		$scope.unitPriceDto[i].physicalUnixMediumRevenue=untiPrices[i].revenue;
	    		$scope.unitPriceDto[i].physicalUnixRevenue=$scope.unitPriceDto[i].physicalUnixSmallRevenue+$scope.unitPriceDto[i].physicalUnixMediumRevenue+$scope.unitPriceDto[i].physicalUnixLargeRevenue;
	    		$scope.unitPriceDto[i].physicalUnix=parseFloat($scope.unitPriceDto[i].physicalUnixRevenue/$scope.hostingInfo.serverHosting.children[0].children[0].children[1].distributedVolume[i].volume).toFixed(2)
	    		$scope.unitPriceDto[i].physicalRevenue=$scope.unitPriceDto[i].physicalUnixRevenue+$scope.unitPriceDto[i].physicalWinRevenue;
	    		$scope.unitPriceDto[i].physical=parseFloat($scope.unitPriceDto[i].physicalRevenue/$scope.hostingInfo.serverHosting.children[0].children[0].distributedVolume[i].volume).toFixed(2);
	    		$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.1.2.3'://P U L
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
	    		$scope.unitPriceDto[i].physicalUnixLarge=untiPrices[i].unitPrice;
	    		$scope.unitPriceDto[i].physicalUnixLargeRevenue=untiPrices[i].revenue;
	    		$scope.unitPriceDto[i].physicalUnixRevenue=$scope.unitPriceDto[i].physicalUnixSmallRevenue+$scope.unitPriceDto[i].physicalUnixMediumRevenue+$scope.unitPriceDto[i].physicalUnixLargeRevenue;
	    		$scope.unitPriceDto[i].physicalUnix=parseFloat($scope.unitPriceDto[i].physicalUnixRevenue/$scope.hostingInfo.serverHosting.children[0].children[0].children[1].distributedVolume[i].volume).toFixed(2)
	    		$scope.unitPriceDto[i].physicalRevenue=$scope.unitPriceDto[i].physicalUnixRevenue+$scope.unitPriceDto[i].physicalWinRevenue;
	    		$scope.unitPriceDto[i].physical=parseFloat($scope.unitPriceDto[i].physicalRevenue/$scope.hostingInfo.serverHosting.children[0].children[0].distributedVolume[i].volume).toFixed(2);
	    		$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;


		    case '1.1.2.1'://Virtual Server  Public 1.1.2.1
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
		    	$scope.unitPriceDto[i].virtualPublic=untiPrices[i].unitPrice;
		    	$scope.unitPriceDto[i].virtualPublicRevenue=untiPrices[i].revenue;
		    	$scope.unitPriceDto[i].virtualRevenue=$scope.unitPriceDto[i].virtualPublicRevenue+$scope.unitPriceDto[i].virtualPrivateRevenue;
		    	$scope.unitPriceDto[i].virtual=parseFloat($scope.unitPriceDto[i].virtualRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.2.2'://Virtual Server  Private 1.1.2.1
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
		    	$scope.unitPriceDto[i].virtualPrivate=untiPrices[i].unitPrice;
		    	$scope.unitPriceDto[i].virtualPrivateRevenue=untiPrices[i].revenue;
		    	$scope.unitPriceDto[i].virtualRevenue=$scope.unitPriceDto[i].virtualPublicRevenue+$scope.unitPriceDto[i].virtualPrivateRevenue;
		    	$scope.unitPriceDto[i].virtual=parseFloat($scope.unitPriceDto[i].virtualRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;


		    case '1.1.2.1.1'://Virtual Server  Public Win
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
		    	$scope.unitPriceDto[i].virtualPublicWin=untiPrices[i].unitPrice;
		    	$scope.unitPriceDto[i].virtualPublicWinRevenue=untiPrices[i].revenue;
		    	$scope.unitPriceDto[i].virtualPublicRevenue=$scope.unitPriceDto[i].virtualPublicWin+$scope.unitPriceDto[i].virtualPublicUnixRevenue;
		        $scope.unitPriceDto[i].virtualPublic=parseFloat($scope.unitPriceDto[i].virtualPublicRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].distributedVolume[i].volume).toFixed(2);
		        $scope.unitPriceDto[i].virtualRevenue=$scope.unitPriceDto[i].virtualPublicRevenue+$scope.unitPriceDto[i].virtualPrivateRevenue;
		    	$scope.unitPriceDto[i].virtual=parseFloat($scope.unitPriceDto[i].virtualRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[i].volume).toFixed(2);
		        $scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.2.1.2'://Virtual Server  Public Unix
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
		    	$scope.unitPriceDto[i].virtualPublicUnix=untiPrices[i].unitPrice;
		    	$scope.unitPriceDto[i].virtualPublicUnixRevenue=untiPrices[i].revenue;
		    	$scope.unitPriceDto[i].virtualPublicRevenue=$scope.unitPriceDto[i].virtualPublicWin+$scope.unitPriceDto[i].virtualPublicUnixRevenue;
		    	$scope.unitPriceDto[i].virtualPublic=parseFloat($scope.unitPriceDto[i].virtualPublicRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].virtualRevenue=$scope.unitPriceDto[i].virtualPublicRevenue+$scope.unitPriceDto[i].virtualPrivateRevenue;
		    	$scope.unitPriceDto[i].virtual=parseFloat($scope.unitPriceDto[i].virtualRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;



		    case '1.1.2.2.1'://Virtual Server  Private  Win
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
		    	$scope.unitPriceDto[i].virtualPrivateWin=untiPrices[i].unitPrice;
		    	$scope.unitPriceDto[i].virtualPrivateWinRevenue=untiPrices[i].revenue;
		    	$scope.unitPriceDto[i].virtualPrivateRevenue=$scope.unitPriceDto[i].virtualPrivateWinRevenue+$scope.unitPriceDto[i].virtualPrivateUnixRevenue;
		    	$scope.unitPriceDto[i].virtualPrivate=parseFloat($scope.unitPriceDto[i].virtualPrivateRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].virtualRevenue=$scope.unitPriceDto[i].virtualPublicRevenue+$scope.unitPriceDto[i].virtualPrivateRevenue;
		    	$scope.unitPriceDto[i].virtual=parseFloat($scope.unitPriceDto[i].virtualRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.2.2.2'://Virtual Server  Private Unix
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
		    	$scope.unitPriceDto[i].virtualPrivateUnix=untiPrices[i].unitPrice;
		    	$scope.unitPriceDto[i].virtualPrivateUnixRevenue=untiPrices[i].revenue;
		    	$scope.unitPriceDto[i].virtualPrivateRevenue=$scope.unitPriceDto[i].virtualPrivateWinRevenue+$scope.unitPriceDto[i].virtualPrivateUnixRevenue;
		    	$scope.unitPriceDto[i].virtualPrivate=parseFloat($scope.unitPriceDto[i].virtualPrivateRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].virtualRevenue=$scope.unitPriceDto[i].virtualPublicRevenue+$scope.unitPriceDto[i].virtualPrivateRevenue;
		    	$scope.unitPriceDto[i].virtual=parseFloat($scope.unitPriceDto[i].virtualRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.2.1.1.1'://Virtual Server  Public Win S
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
		    	$scope.unitPriceDto[i].virtualPublicWinSmall=untiPrices[i].unitPrice;
		    	$scope.unitPriceDto[i].virtualPublicWinSmallRevenue=untiPrices[i].revenue;
		    	$scope.unitPriceDto[i].virtualPublicWinRevenue=$scope.unitPriceDto[i].virtualPublicWinSmallRevenue+$scope.unitPriceDto[i].virtualPublicWinMediumRevenue+$scope.unitPriceDto[i].virtualPublicWinLargeRevenue;
		    	$scope.unitPriceDto[i].virtualPublicWin=parseFloat($scope.unitPriceDto[i].virtualPublicWinRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].distributedVolume[i].volume).toFixed(2);;
		    	$scope.unitPriceDto[i].virtualPublicRevenue=$scope.unitPriceDto[i].virtualPublicWinRevenue+$scope.unitPriceDto[i].virtualPublicUnixRevenue;
		    	$scope.unitPriceDto[i].virtualPublic=parseFloat($scope.unitPriceDto[i].virtualPublicRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].virtualRevenue=$scope.unitPriceDto[i].virtualPublicRevenue+$scope.unitPriceDto[i].virtualPrivateRevenue;
		    	$scope.unitPriceDto[i].virtual=parseFloat($scope.unitPriceDto[i].virtualRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.2.1.1.2'://Virtual Server  Public Win M
		    	for(var i=0;i<untiPrices.length;i++)
	    		{

		    	$scope.unitPriceDto[i].virtualPublicWinMedium=untiPrices[i].unitPrice;
		    	$scope.unitPriceDto[i].virtualPublicWinMediumRevenue=untiPrices[i].revenue;
		    	$scope.unitPriceDto[i].virtualPublicWinRevenue=$scope.unitPriceDto[i].virtualPublicWinSmallRevenue+$scope.unitPriceDto[i].virtualPublicWinMediumRevenue+$scope.unitPriceDto[i].virtualPublicWinLargeRevenue;
		    	$scope.unitPriceDto[i].virtualPublicWin=parseFloat($scope.unitPriceDto[i].virtualPublicWinRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].distributedVolume[i].volume).toFixed(2);;
		    	$scope.unitPriceDto[i].virtualPublicRevenue=$scope.unitPriceDto[i].virtualPublicWinRevenue+$scope.unitPriceDto[i].virtualPublicUnixRevenue;
		    	$scope.unitPriceDto[i].virtualPublic=parseFloat($scope.unitPriceDto[i].virtualPublicRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].virtualRevenue=$scope.unitPriceDto[i].virtualPublicRevenue+$scope.unitPriceDto[i].virtualPrivateRevenue;
		    	$scope.unitPriceDto[i].virtual=parseFloat($scope.unitPriceDto[i].virtualRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.2.1.1.3'://Virtual Server  Public Win L
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
		    	$scope.unitPriceDto[i].virtualPublicWinLarge=untiPrices[i].unitPrice;
		    	$scope.unitPriceDto[i].virtualPublicWinLargeRevenue=untiPrices[i].revenue;
		    	$scope.unitPriceDto[i].virtualPublicWinRevenue=$scope.unitPriceDto[i].virtualPublicWinSmallRevenue+$scope.unitPriceDto[i].virtualPublicWinMediumRevenue+$scope.unitPriceDto[i].virtualPublicWinLargeRevenue;
		    	$scope.unitPriceDto[i].virtualPublicWin=parseFloat($scope.unitPriceDto[i].virtualPublicWinRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].distributedVolume[i].volume).toFixed(2);;
		    	$scope.unitPriceDto[i].virtualPublicRevenue=$scope.unitPriceDto[i].virtualPublicWinRevenue+$scope.unitPriceDto[i].virtualPublicUnixRevenue;
		    	$scope.unitPriceDto[i].virtualPublic=parseFloat($scope.unitPriceDto[i].virtualPublicRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].virtualRevenue=$scope.unitPriceDto[i].virtualPublicRevenue+$scope.unitPriceDto[i].virtualPrivateRevenue;
		    	$scope.unitPriceDto[i].virtual=parseFloat($scope.unitPriceDto[i].virtualRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;


		    case '1.1.2.1.2.1'://Virtual Server  Public UNix S
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
		    	$scope.unitPriceDto[i].virtualPublicUnixSmall=untiPrices[i].unitPrice;
		    	$scope.unitPriceDto[i].virtualPublicUnixSmallRevenue=untiPrices[i].revenue;
		    	$scope.unitPriceDto[i].virtualPublicUnixRevenue=$scope.unitPriceDto[i].virtualPublicUnixSmallRevenue+$scope.unitPriceDto[i].virtualPublicUnixMediumRevenue+$scope.unitPriceDto[i].virtualPublicUnixLargeRevenue;
		    	$scope.unitPriceDto[i].virtualPublicUnix=parseFloat($scope.unitPriceDto[i].virtualPublicUnixRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].virtualPublicRevenue=$scope.unitPriceDto[i].virtualPublicWinRevenue+$scope.unitPriceDto[i].virtualPublicUnixRevenue;
		    	$scope.unitPriceDto[i].virtualPublic=parseFloat($scope.unitPriceDto[i].virtualPublicRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].virtualRevenue=$scope.unitPriceDto[i].virtualPublicRevenue+$scope.unitPriceDto[i].virtualPrivateRevenue;
		    	$scope.unitPriceDto[i].virtual=parseFloat($scope.unitPriceDto[i].virtualRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.2.1.2.2'://Virtual Server  Public Unix M
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
		    	$scope.unitPriceDto[i].virtualPublicUnixMedium=untiPrices[i].unitPrice;
		    	$scope.unitPriceDto[i].virtualPublicUnixMediumRevenue=untiPrices[i].revenue;
		    	$scope.unitPriceDto[i].virtualPublicUnixRevenue=$scope.unitPriceDto[i].virtualPublicWinSmallRevenue+$scope.unitPriceDto[i].virtualPublicUnixMediumRevenue+$scope.unitPriceDto[i].virtualPublicUnixLargeRevenue;
		    	$scope.unitPriceDto[i].virtualPublicUnix=parseFloat($scope.unitPriceDto[i].virtualPublicUnixRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].virtualPublicRevenue=$scope.unitPriceDto[i].virtualPublicWinRevenue+$scope.unitPriceDto[i].virtualPublicUnixRevenue;
		    	$scope.unitPriceDto[i].virtualPublic=parseFloat($scope.unitPriceDto[i].virtualPublicRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].virtualRevenue=$scope.unitPriceDto[i].virtualPublicRevenue+$scope.unitPriceDto[i].virtualPrivateRevenue;
		    	$scope.unitPriceDto[i].virtual=parseFloat($scope.unitPriceDto[i].virtualRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.2.1.2.3'://Virtual Server  Public Unix L
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
		    	$scope.unitPriceDto[i].virtualPublicUnixLarge=untiPrices[i].unitPrice;
		    	$scope.unitPriceDto[i].virtualPublicUnixLargeRevenue=untiPrices[i].revenue;
		    	$scope.unitPriceDto[i].virtualPublicUnixRevenue=$scope.unitPriceDto[i].virtualPublicWinSmallRevenue+$scope.unitPriceDto[i].virtualPublicWinMediumRevenue+$scope.unitPriceDto[i].virtualPublicUnixLargeRevenue;
		    	$scope.unitPriceDto[i].virtualPublicUnix=parseFloat($scope.unitPriceDto[i].virtualPublicUnixRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].virtualPublicRevenue=$scope.unitPriceDto[i].virtualPublicWinRevenue+$scope.unitPriceDto[i].virtualPublicUnixRevenue;
		    	$scope.unitPriceDto[i].virtualPublic=parseFloat($scope.unitPriceDto[i].virtualPublicRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].virtualRevenue=$scope.unitPriceDto[i].virtualPublicRevenue+$scope.unitPriceDto[i].virtualPrivateRevenue;
		    	$scope.unitPriceDto[i].virtual=parseFloat($scope.unitPriceDto[i].virtualRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
		    	$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;


		    case '1.1.2.2.1.1'://Virtual Server  Private  Win S
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
		    	$scope.unitPriceDto[i].virtualPrivateWinSmall=untiPrices[i].unitPrice;
		    	$scope.unitPriceDto[i].virtualPrivateWinSmallRevenue=untiPrices[i].revenue;
		    	$scope.unitPriceDto[i].virtualPrivateWinRevenue=$scope.unitPriceDto[i].virtualPrivateWinSmallRevenue+$scope.unitPriceDto[i].virtualPrivateWinMediumRevenue+$scope.unitPriceDto[i].virtualPrivateWinLargeRevenue;
		    	$scope.unitPriceDto[i].virtualPrivateWin=parseFloat($scope.unitPriceDto[i].virtualPrivateWinRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].distributedVolume[i].volume).toFixed(2);;
		    	$scope.unitPriceDto[i].virtualPrivateRevenue=$scope.unitPriceDto[i].virtualPrivateWinRevenue+$scope.unitPriceDto[i].virtualPrivateUnixRevenue;
		    	$scope.unitPriceDto[i].virtualPrivate=parseFloat($scope.unitPriceDto[i].virtualPrivateRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].virtualRevenue=$scope.unitPriceDto[i].virtualPublicRevenue+$scope.unitPriceDto[i].virtualPrivateRevenue;
		    	$scope.unitPriceDto[i].virtual=parseFloat($scope.unitPriceDto[i].virtualRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.2.2.1.2'://Virtual Server  Private  Win M
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
		    	$scope.unitPriceDto[i].virtualPrivateWinMedium=untiPrices[i].unitPrice;
		    	$scope.unitPriceDto[i].virtualPrivateWinMediumRevenue=untiPrices[i].revenue;
		    	$scope.unitPriceDto[i].virtualPrivateWinRevenue=$scope.unitPriceDto[i].virtualPrivateWinSmallRevenue+$scope.unitPriceDto[i].virtualPrivateWinMediumRevenue+$scope.unitPriceDto[i].virtualPrivateWinLargeRevenue;
		    	$scope.unitPriceDto[i].virtualPrivateWin=parseFloat($scope.unitPriceDto[i].virtualPrivateWinRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].distributedVolume[i].volume).toFixed(2);;
		    	$scope.unitPriceDto[i].virtualPrivateRevenue=$scope.unitPriceDto[i].virtualPrivateWinRevenue+$scope.unitPriceDto[i].virtualPrivateUnixRevenue;
		    	$scope.unitPriceDto[i].virtualPrivate=parseFloat($scope.unitPriceDto[i].virtualPrivateRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].virtualRevenue=$scope.unitPriceDto[i].virtualPublicRevenue+$scope.unitPriceDto[i].virtualPrivateRevenue;
		    	$scope.unitPriceDto[i].virtual=parseFloat($scope.unitPriceDto[i].virtualRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
		    	$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.2.2.1.3'://Virtual Server  Private  Win l
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
		    	$scope.unitPriceDto[i].virtualPrivateWinLarge=untiPrices[i].unitPrice;
		    	$scope.unitPriceDto[i].virtualPrivateWinLargeRevenue=untiPrices[i].revenue;
		    	$scope.unitPriceDto[i].virtualPrivateWinRevenue=$scope.unitPriceDto[i].virtualPrivateWinSmallRevenue+$scope.unitPriceDto[i].virtualPrivateWinMediumRevenue+$scope.unitPriceDto[i].virtualPrivateWinLargeRevenue;
		    	$scope.unitPriceDto[i].virtualPrivateWin=parseFloat($scope.unitPriceDto[i].virtualPrivateWinRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].distributedVolume[i].volume).toFixed(2);;
		    	$scope.unitPriceDto[i].virtualPrivateRevenue=$scope.unitPriceDto[i].virtualPrivateWinRevenue+$scope.unitPriceDto[i].virtualPrivateUnixRevenue;
		    	$scope.unitPriceDto[i].virtualPrivate=parseFloat($scope.unitPriceDto[i].virtualPrivateRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].virtualRevenue=$scope.unitPriceDto[i].virtualPublicRevenue+$scope.unitPriceDto[i].virtualPrivateRevenue;
		    	$scope.unitPriceDto[i].virtual=parseFloat($scope.unitPriceDto[i].virtualRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.2.2.2.1'://Virtual Server  Private  U S
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
		    	$scope.unitPriceDto[i].virtualPrivateUnixSmall=untiPrices[i].unitPrice;
		    	$scope.unitPriceDto[i].virtualPrivateUnixSmallRevenue=untiPrices[i].revenue;
		    	$scope.unitPriceDto[i].virtualPrivateUnixRevenue=$scope.unitPriceDto[i].virtualPrivateUnixSmallRevenue+$scope.unitPriceDto[i].virtualPrivateUnixMediumRevenue+$scope.unitPriceDto[i].virtualPrivateUnixLargeRevenue;
		    	$scope.unitPriceDto[i].virtualPrivateUnix=parseFloat($scope.unitPriceDto[i].virtualPrivateUnixRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].virtualPrivateRevenue=$scope.unitPriceDto[i].virtualPrivateWinRevenue+$scope.unitPriceDto[i].virtualPrivateUnixRevenue;
		    	$scope.unitPriceDto[i].virtualPrivate=parseFloat($scope.unitPriceDto[i].virtualPrivateRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].virtualRevenue=$scope.unitPriceDto[i].virtualPublicRevenue+$scope.unitPriceDto[i].virtualPrivateRevenue;
		    	$scope.unitPriceDto[i].virtual=parseFloat($scope.unitPriceDto[i].virtualRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.2.2.2.2'://Virtual Server  Private  U M
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
		    	$scope.unitPriceDto[i].virtualPrivateUnixMedium=untiPrices[i].unitPrice;
		    	$scope.unitPriceDto[i].virtualPrivateUnixMediumRevenue=untiPrices[i].revenue;
		    	$scope.unitPriceDto[i].virtualPrivateUnixRevenue=$scope.unitPriceDto[i].virtualPrivateUnixSmallRevenue+$scope.unitPriceDto[i].virtualPrivateUnixMediumRevenue+$scope.unitPriceDto[i].virtualPrivateUnixLargeRevenue;
		    	$scope.unitPriceDto[i].virtualPrivateUnix=parseFloat($scope.unitPriceDto[i].virtualPrivateUnixRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].virtualPrivateRevenue=$scope.unitPriceDto[i].virtualPrivateWinRevenue+$scope.unitPriceDto[i].virtualPrivateUnixRevenue;
		    	$scope.unitPriceDto[i].virtualPrivate=parseFloat($scope.unitPriceDto[i].virtualPrivateRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].virtualRevenue=$scope.unitPriceDto[i].virtualPublicRevenue+$scope.unitPriceDto[i].virtualPrivateRevenue;
		    	$scope.unitPriceDto[i].virtual=parseFloat($scope.unitPriceDto[i].virtualRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		    case '1.1.2.2.2.3'://Virtual Server  Private  U L
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
		    	$scope.unitPriceDto[i].virtualPrivateUnixLarge=untiPrices[i].unitPrice;
		    	$scope.unitPriceDto[i].virtualPrivateUnixLargeRevenue=untiPrices[i].revenue;
		    	$scope.unitPriceDto[i].virtualPrivateUnixRevenue=$scope.unitPriceDto[i].virtualPrivateUnixSmallRevenue+$scope.unitPriceDto[i].virtualPrivateUnixMediumRevenue+$scope.unitPriceDto[i].virtualPrivateUnixLargeRevenue;
		    	$scope.unitPriceDto[i].virtualPrivateUnix=parseFloat($scope.unitPriceDto[i].virtualPrivateUnixRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].virtualPrivateRevenue=$scope.unitPriceDto[i].virtualPrivateWinRevenue+$scope.unitPriceDto[i].virtualPrivateUnixRevenue;
		    	$scope.unitPriceDto[i].virtualPrivate=parseFloat($scope.unitPriceDto[i].virtualPrivateRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].virtualRevenue=$scope.unitPriceDto[i].virtualPublicRevenue+$scope.unitPriceDto[i].virtualPrivateRevenue;
		    	$scope.unitPriceDto[i].virtual=parseFloat($scope.unitPriceDto[i].virtualRevenue/$scope.hostingInfo.serverHosting.children[0].children[1].distributedVolume[i].volume).toFixed(2);
		    	$scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].virtualRevenue+$scope.unitPriceDto[i].physicalRevenue;
	    		$scope.unitPriceDto[i].servers=parseFloat($scope.unitPriceDto[i].serversRevenue/$scope.hostingInfo.serverHosting.children[0].distributedVolume[i].volume).toFixed(2);
	    		}
		    	break;

		  }
		  console.log($scope.unitPriceDto);
	  }
	// function used for creating the unit price year wise
	  $scope.createUnitPriceForLevels=function(val)
	  {
		  $scope.unitPriceDto=[];
		  for(var i=1;i<=val;i++)
			  {
			  var unitPrice={};
			   unitPrice.year=i;

			   unitPrice.servers=0;
			   unitPrice.serversRevenue=0;

			   unitPrice.physical=0;
			   unitPrice.physicalRevenue=0;

			   unitPrice.physicalWin=0;
			   unitPrice.physicalWinRevenue=0;

			   unitPrice.physicalWinSmall=0;
			   unitPrice.physicalWinSmallRevenue=0;

			   unitPrice.physicalWinMedium=0;
			   unitPrice.physicalWinMediumRevenue=0;

			   unitPrice.physicalWinLarge=0;
			   unitPrice.physicalWinLargeRevenue=0;

			   unitPrice.physicalUnix=0;
			   unitPrice.physicalUnixRevenue=0;

			   unitPrice.physicalUnixSmall=0;
			   unitPrice.physicalUnixSmallRevenue=0;

			   unitPrice.physicalUnixMedium=0;
			   unitPrice.physicalUnixMediumRevenue=0;

			   unitPrice.physicalUnixLarge=0;
			   unitPrice.physicalUnixLargeRevenue=0;

			   unitPrice.virtual=0;
			   unitPrice.virtualRevenue=0;

			   unitPrice.virtualPublic=0;
			   unitPrice.virtualPublicRevenue=0;

			   unitPrice.virtualPublicWin=0;
			   unitPrice.virtualPublicWinRevenue=0;

			   unitPrice.virtualPublicWinSmall=0;
			   unitPrice.virtualPublicWinSmallRevenue=0;

			   unitPrice.virtualPublicWinMedium=0;
			   unitPrice.virtualPublicWinMediumRevenue=0;

			   unitPrice.virtualPublicWinLarge=0;
			   unitPrice.virtualPublicWinLargeRevenue=0;

			   unitPrice.virtualPublicUnix=0;
			   unitPrice.virtualPublicUnixRevenue=0;

			   unitPrice.virtualPublicUnixSmall=0;
			   unitPrice.virtualPublicUnixSmallRevenue=0;

			   unitPrice.virtualPublicUnixMedium=0;
			   unitPrice.virtualPublicUnixMediumRevenue=0;

			   unitPrice.virtualPublicUnixLarge=0;
			   unitPrice.virtualPublicUnixLargeRevenue=0;

			   unitPrice.virtualPrivate=0;
			   unitPrice.virtualPrivateRevenue=0;

			   unitPrice.virtualPrivateWin=0;
			   unitPrice.virtualPrivateWinRevenue=0;

			   unitPrice.virtualPrivateWinSmall=0;
			   unitPrice.virtualPrivateWinSmallRevenue=0;

			   unitPrice.virtualPrivateWinMedium=0;
			   unitPrice.virtualPrivateWinMediumRevenue=0;

			   unitPrice.virtualPrivateWinLarge=0;
			   unitPrice.virtualPrivateWinLargeRevenue=0;

			   unitPrice.virtualPrivateUnix=0;
			   unitPrice.virtualPrivateUnixRevenue=0;

			   unitPrice.virtualPrivateUnixSmall=0;
			   unitPrice.virtualPrivateUnixSmallRevenue=0;

			   unitPrice.virtualPrivateUnixMedium=0;
			   unitPrice.virtualPrivateUnixMediumRevenue=0;

			   unitPrice.virtualPrivateUnixLarge=0;
			   unitPrice.virtualPrivateUnixLargeRevenue=0;

			   unitPrice.sqlInstances=0;
			   unitPrice.sqlInstancesRevenue=0;

			   unitPrice.cotsInstallations=0;
			   unitPrice.cotsInstallationsRevenue=0;

			  $scope.unitPriceDto.push(unitPrice);
			  }
        console.log($scope.unitPriceDto);
	  }

   // Modal for find deal section
		var dialogOptions = {
		    controller: 'DealInformationCtrl',
		    templateUrl: '/modals/dealInformation/dealInformation.html'
		  };
		  $scope.getNearestDeal = function(child,key){
			  var infoModel={id:child.id,dealType:key,url:endpointsurls.HOSTING_NEAREST_DEAL};
		    $dialog.dialog(angular.extend(dialogOptions, {resolve: {infoModel: angular.copy(infoModel)}}))
		      .open()
		      .then(function(result) {
		        if(result) {
		          //angular.copy(result, infoModel);
		        }
		        infoModel = undefined;
		    });
		  };

		// reset radio buttons
			$scope.resetCalculateDetails=function(){

				$scope.hostingInfo.serverHosting.children[0].childVolumehosting=''; //Server Volume

				$scope.hostingInfo.serverHosting.children[0].children[0].perNonPer='';//Physical Server
				$scope.hostingInfo.serverHosting.children[0].children[1].perNonPer='';//Virtual Server *
				$scope.hostingInfo.serverHosting.children[0].children[0].children[0].perNonPer='';//Windows/Linux *

				$scope.hostingInfo.serverHosting.children[0].children[0].children[0].perNonPer='';//Windows/Linux *
				$scope.hostingInfo.serverHosting.children[0].children[0].children[0].children[0].perNonPer='';//s
				$scope.hostingInfo.serverHosting.children[0].children[0].children[0].children[1].perNonPer='';//m
				$scope.hostingInfo.serverHosting.children[0].children[0].children[0].children[2].perNonPer='';//l

				$scope.hostingInfo.serverHosting.children[0].children[0].children[1].perNonPer='';//Unix *
				$scope.hostingInfo.serverHosting.children[0].children[0].children[1].children[0].perNonPer='';//s
				$scope.hostingInfo.serverHosting.children[0].children[0].children[1].children[1].perNonPer='';//m
				$scope.hostingInfo.serverHosting.children[0].children[0].children[1].children[2].perNonPer='';//l


				$scope.hostingInfo.serverHosting.children[0].children[1].perNonPer='';//Virtual Server *
				$scope.hostingInfo.serverHosting.children[0].children[1].children[0].perNonPer='';//public
				$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].perNonPer='';////Windows/Linux *
				$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].children[0].perNonPer=''//s
				$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].children[1].perNonPer='' //m
				$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[0].children[2].perNonPer='' //L

				$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].perNonPer='';// Unix
				$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].children[0].perNonPer=''//s
				$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].children[1].perNonPer='' //m
				$scope.hostingInfo.serverHosting.children[0].children[1].children[0].children[1].children[2].perNonPer='' //L

				$scope.hostingInfo.serverHosting.children[0].children[1].children[1].perNonPer='';//private
				$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].perNonPer='';//unix
				$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].children[0].perNonPer=''//s
				$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].children[1].perNonPer='' //m
				$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[1].children[2].perNonPer='' //L



				$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].perNonPer='';//winlin
				$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].children[0].perNonPer=''//s
				$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].children[1].perNonPer='' //m
				$scope.hostingInfo.serverHosting.children[0].children[1].children[1].children[0].children[2].perNonPer='' //

				$scope.hostingInfo.platformHosting.children[0].platform='';

				$scope.hostingInfo.platformHosting.children[1].platform='';

			}

			//goToCal
	$scope.goToCal=function()
	{
		 $scope.populatedCalculateTabDetails();
		$scope.tabReset(1);
	}

	//backToInput
	$scope.backToInput=function()
	{
		$scope.tabReset(0);
	}

	//goToResult
	$scope.goToResult=function()
	{
		var url= endpointsurls.HOSTING_REVENUE+$scope.id;

		 for(var i=0;i<$scope.unitPriceDto.length;i++)
			 {
			 $scope.unitPriceDto[i].serversRevenue=$scope.unitPriceDto[i].serversRevenue*12;
			 $scope.unitPriceDto[i].sqlInstancesRevenue=$scope.unitPriceDto[i].sqlInstancesRevenue*12;
			 $scope.unitPriceDto[i].cotsInstallationsRevenue=$scope.unitPriceDto[i].cotsInstallationsRevenue*12;
			 }

		customInterceptor.putrequest(url,$scope.unitPriceDto).then(function successCallback(response) {
			 var urlresult = endpointsurls.SAVE_HOSTING_INFO+ $stateParams.dealId;
		        customInterceptor.getrequest(urlresult).then(function successCallback(response) {
		        	$scope.existingHostingResultInfo = response.data;
		        	$scope.tabReset(2);
		        	$scope.viewBy = {type:'unit'};
		       		if($scope.existingHostingResultInfo != null && $scope.id !=null){
		       			getExistingHostingResultYearlyInfo();
		       		}
		        }, function errorCallback(response) {
		        	$scope.id=undefined;
		        });
		        })
	}

	//---------------------------------- Result Tab ------------------------------

	// map Existing Hosting Yearly Info
    function getExistingHostingResultYearlyInfo() {
    	if($scope.existingHostingResultInfo.hostingYearlyDataInfoDtoList != null){
    		for (var y = 0; y < $scope.existingHostingResultInfo.hostingYearlyDataInfoDtoList.length; y++){
        		var yearlyDto = $scope.existingHostingResultInfo.hostingYearlyDataInfoDtoList[y];
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

	// Go To Total
	$scope.gotoTotals = function(){
		$scope.isSaveHosting.hostingA=true;
        DealService.setter($scope.isSaveHosting);
		
		$scope.assessmentIndicator[1]=1;
        var urlIndicator= endpointsurls.GENERic_INDICATOR+$stateParams.dealId+'?assessmentIndicator='+$scope.assessmentIndicator.join(',')+'&submissionIndicator='+$scope.submissionIndicator;
       customInterceptor.putrequestWithoutData(urlIndicator).then(function successCallback(response) {
    	   $state.go('home.assessment.generic.totals',({dealId:$stateParams.dealId}));
       });
	}

	//backToCal
	$scope.backToCal=function()
	{
		$scope.tabReset(1);
	}

});

