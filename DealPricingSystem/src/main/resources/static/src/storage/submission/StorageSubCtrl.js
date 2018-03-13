priceToolcontrollers.controller('StorageSubCtrl',function($scope,$http,HostingService,EndUserService,StorageService,DealService,customInterceptor,endpointsurls,$stateParams,$alert,$dialog,$state) {
	
	//initialize all the variable needed for the page.
	
	
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
	$scope.lastModStorageVol = [];
	$scope.dummy = [];
	$scope.selectedBackUp='';
	$scope.viewBy = {type:'unit'};
	$scope.solList = [];
	$scope.solution = {};
	$scope.storageInfoDto = {};
	$scope.dealDetails={};
	$scope.showmsg = false;
	$scope.showvalidationmsg = false;
	$scope.catcherror = true;
	$scope.customSol=false;

	// function for reseting tab and select the index which is selected by user.
	$scope.tabRest=function(index)
	{
		for(var i=0;i< $scope.tabs.length;i++){
			$scope.tabs[i].class="Inactive";
			if(i==index){
    			$scope.tabs[i].class="active";
    		}
		}
	}

	// function for catching tab event solution
	$scope.tabEvent=function($event,index,parent,child){
		if(parent == 'storage'){
			if($event.keyCode == 13 || $event.keyCode == 9){
					if(child == 'volume'){
						for(var i=index;i<$scope.storageInfo.storage.children[0].distributedVolume.length;i++)
						{
							$scope.storageInfo.storage.children[0].distributedVolume[i].volume = $scope.storageInfo.storage.children[0].distributedVolume[index].volume;
						}
					}
					if(child == 'pricingrevenue'){
						for(var i=index;i<$scope.storageInfo.storage.children[0].distributedVolume.length;i++)
						{
							$scope.storageInfo.storage.children[0].distributedVolume[i].revenue = $scope.storageInfo.storage.children[0].distributedVolume[index].revenue;
						}
					}
					if(child == 'pricingunit'){
						for(var i=index;i<$scope.storageInfo.storage.children[0].distributedVolume.length;i++)
						{
							$scope.storageInfo.storage.children[0].distributedVolume[i].unit = $scope.storageInfo.storage.children[0].distributedVolume[index].unit;
						}
					}
				}
			}
		if(parent == 'backup'){
			if($event.keyCode == 13 || $event.keyCode == 9){
				if(child == 'volume'){
					for(var i=index;i<$scope.storageInfo.backup.children[0].distributedVolume.length;i++)
					{
						$scope.storageInfo.backup.children[0].distributedVolume[i].volume = $scope.storageInfo.backup.children[0].distributedVolume[index].volume;
					}
				}
				if(child == 'pricingrevenue'){
					for(var i=index;i<$scope.storageInfo.backup.children[0].distributedVolume.length;i++)
					{
						$scope.storageInfo.backup.children[0].distributedVolume[i].revenue = $scope.storageInfo.backup.children[0].distributedVolume[index].revenue;
					}
				}
				if(child == 'pricingunit'){
					for(var i=index;i<$scope.storageInfo.backup.children[0].distributedVolume.length;i++)
					{
						$scope.storageInfo.backup.children[0].distributedVolume[i].unit = $scope.storageInfo.backup.children[0].distributedVolume[index].unit;
					}
				}

			}
		}
	}

	//get drop-down data
	$scope.getStorageDropdowns=function(){
		var url = endpointsurls.STORAGE_DROPDOWNS+$stateParams.dealId;
        customInterceptor.getrequest(url).then(function successCallback(response) {
			$scope.solList = response.data.storageSolutionsInfoDtoList;
			$scope.backUpList=response.data.storageBackupInfoDtoList;
			$scope.genericDealInfoDto = response.data;
			$scope.dealYearlyDataInfoDtoList=response.data.dealInfoDto.dealYearlyDataInfoDtos;
			$scope.storageDefaultInfoDtoList=response.data.storageDefaultInfoDtoList;
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
        	DealService.setter(isSave)
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
        $scope.storageInfo = StorageService.initStorageDetails($scope.dealInfo);
        if($stateParams.dealId>0){
			 var url = endpointsurls.STORAGE_DETAILS_URL+ $stateParams.dealId;
		        customInterceptor.getrequest(url).then(function successCallback(response) {
	       		$scope.existingStorageInfo = response.data;
	       		$scope.level=$scope.existingStorageInfo.levelIndicator.split(',');
	       		$scope.storageInfo.storage.open=($scope.level[0]==1)?true:false;
	       		$scope.storageInfo.storage.children[0].open=($scope.level[1]==1)?true:false;
	       		$scope.storageInfo.backup.open=($scope.level[2]==1)?true:false
	       		if($scope.existingStorageInfo != null && $scope.existingStorageInfo.id!=null){
	       			mapExistingSolutionInfo();
	       		}
	        }, function errorCallback(response) {
	            console.log(response.statusText);
	        });

		}
	}

	//function for init controller
		$scope.init=function(){
			$scope.selectedBackUp='';
		    $scope.custom=false;
	   	    $scope.getStorageDropdowns();
	}
	$scope.init();

	$scope.togglePanel = function(level) {
		if(level.open){
			level.open=false;
		}else
		{
				level.open=true;
				//retainOldVolumes(level);
		}
	};
	
	//Keep track of previously saved Storage Volume
//	function retainOldVolumes(level){
//		$scope.lastModStorageVol = [];
//		if(level.levelName == $scope.storageInfo.storage.children[0].levelName){
//			for (var y = 0; y < $scope.dealInfo/12; y++){
//				$scope.lastModStorageVol.push($scope.storageInfo.storage.children[0].distributedVolume[y].volume);
//			}
//		}
//	}

	// calculate parent server price when user insert child server pricing
	$scope.calcServerPrice = function (){
		var parent = $scope.storageInfo.storage.children[0];
		if($scope.viewBy.type == 'unit'){
			for(var i=0;i< parent.distributedVolume.length;i++){
				//parent.distributedVolume[i].unit = child
				var performancevolume = "";
				var standardvolume = "";
				var performanceunit = "";
				var nonperformanceunit = "";
				if($scope.storageInfo.storage.children[0].open){
					for(var k = 0;k < parent.children.length;k++){
						var child = parent.children[k];
						switch(child.id) {
						case "1.1.1":
							performancevolume = parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							performanceunit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
							//child.percentage =  $scope.solution? $scope.solution.performanceValue : "";
							break;
						case "1.1.2":
							standardvolume = parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
							nonperformanceunit = parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit);
							//child.percentage =  $scope.solution? $scope.solution.nonPerformanceValue :"";
							break;
						}
					}
					parent.distributedVolume[i].unit = (((performanceunit * performancevolume) + (nonperformanceunit * standardvolume)) / (performancevolume + standardvolume))
					if(isNaN(parent.distributedVolume[i].unit)){
						parent.distributedVolume[i].unit = 0;
					}
					parent.distributedVolume[i].unit = parent.distributedVolume[i].unit.toFixed(2);
				}
			}
		}
		if($scope.viewBy.type == 'revenue'){
			for(var i=0;i< parent.distributedVolume.length;i++){
				//parent.distributedVolume[i].unit = child
				var performancerevenue = "";
				var nonperformancerevenue = "";
				for(var k = 0;k < parent.children.length;k++){
					var child = parent.children[k];
					switch(child.id) {
					case "1.1.1":
						performancerevenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
						break;
					case "1.1.2":
						nonperformancerevenue = parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue);
						break;
					}
				}
				parent.distributedVolume[i].revenue = Math.round(performancerevenue + nonperformancerevenue);
				if(isNaN(parent.distributedVolume[i].revenue)){
					parent.distributedVolume[i].revenue = 0.00;
				}
			}
		}
	}

	// when user change the percentage
	$scope.onChangeStoragePercentage = function(node,index) {
		$scope.customSol=true;
		var CustomSolutionId=_.where($scope.solList, {solutionName: 'Custom'})[0];
		$scope.selectedSolutionId=CustomSolutionId.solutionId;
		var parent = $scope.storageInfo.storage.children[0];
		var changePercentage=$scope.storageInfo.storage.children[0].children[index].percentage;

		if($scope.storageInfo.storage.children[0].children[index+1])
			{
			$scope.storageInfo.storage.children[0].children[index+1].percentage=100-changePercentage;
			}else
				{
				$scope.storageInfo.storage.children[0].children[index-1].percentage=100-changePercentage;
				}
		calculateStorageVolume(parent);
	};

	// User change volume
	$scope.onChangevolume = function(child,childIndex,index){
		$scope.customSol=true;
		var CustomSolutionId=_.where($scope.solList, {solutionName: 'Custom'})[0];
		$scope.selectedSolutionId=CustomSolutionId.solutionId;
		$scope.storageInfo.storage.children[0].children[0].percentage = '';
		$scope.storageInfo.storage.children[0].children[1].percentage = '';
		for (var y = 0; y < $scope.dealInfo/12; y++) {
			if(parseInt($scope.storageInfo.storage.children[0].distributedVolume[y].volume)!=parseInt($scope.storageInfo.storage.children[0].children[0].distributedVolume[y].volume)
			+parseInt($scope.storageInfo.storage.children[0].children[1].distributedVolume[y].volume)){
				$scope.showVolumeErr=true;
				return;
			}else
			{
				$scope.showVolumeErr=false;
			}
		}
		$scope.calcServerPrice();
	}

	// getting the node
	function getStorageParentNode(child){
		var node = {};
		for(var i=0;i< $scope.storageInfo.nodeList.length;i++){
			if($scope.storageInfo.nodeList[i].id == child.parentId){
				node = $scope.storageInfo.nodeList[i];
				break;
			}
		}
		return node;
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
	    			}
				}
			}
		}
	}

	// On change Unit-revenue price radio button
	$scope.onchangeprice = function(value){
		if(value == 'revenue')
			{
			if($scope.storageInfo.storage.children[0].open == true){
				for(var i=0;i< $scope.storageInfo.storage.children[0].distributedVolume.length;i++){
					var performancerevenue = "";
					var nonperformancerevenue = "";
					var performancevolume = "";
					var standardvolume = "";
						for(var k = 0;k < $scope.storageInfo.storage.children[0].children.length;k++){
							child = $scope.storageInfo.storage.children[0].children[k];
			    			switch(child.id) {
			  				case "1.1": //Server Volume
			  					break;
			  				case "1.1.1": //Performance Server
			  						child.distributedVolume[i].revenue = Math.round(((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume)  * (parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit)));
			  						if(isNaN(child.distributedVolume[i].revenue)){
			  							child.distributedVolume[i].revenue = 0;
			  						}
			  						performancerevenue = Math.round(child.distributedVolume[i].revenue);
			  						performancevolume = (parseInt(child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
			    				break;
			    			case "1.1.2": //Virtual Server
			    					child.distributedVolume[i].revenue = Math.round((parseFloat((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume))  * (parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit)));
			    					if(isNaN(child.distributedVolume[i].revenue)){
			  							child.distributedVolume[i].revenue = 0;
			  						}
			    					nonperformancerevenue = Math.round(child.distributedVolume[i].revenue);
			    					standardvolume = (parseInt(child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
			    				break;
			    			}
						}
						$scope.storageInfo.storage.children[0].distributedVolume[i].revenue = Math.round(performancerevenue + nonperformancerevenue)
						$scope.storageInfo.backup.children[0].distributedVolume[i].revenue = Math.round($scope.storageInfo.backup.children[0].distributedVolume[i].volume * $scope.storageInfo.backup.children[0].distributedVolume[i].unit);
						if(isNaN($scope.storageInfo.backup.children[0].distributedVolume[i].revenue)){
							$scope.storageInfo.backup.children[0].distributedVolume[i].revenue = 0;
  						}
					}
				}
				else{
					for(var i=0;i< $scope.storageInfo.storage.children[0].distributedVolume.length;i++){
						$scope.storageInfo.storage.children[0].distributedVolume[i].revenue = Math.round($scope.storageInfo.storage.children[0].distributedVolume[i].volume * $scope.storageInfo.storage.children[0].distributedVolume[i].unit)
						if(isNaN($scope.storageInfo.storage.children[0].distributedVolume[i].revenue)){
							$scope.storageInfo.storage.children[0].distributedVolume[i].revenue = 0;
  						}
						$scope.storageInfo.backup.children[0].distributedVolume[i].revenue = Math.round($scope.storageInfo.backup.children[0].distributedVolume[i].volume * $scope.storageInfo.backup.children[0].distributedVolume[i].unit);
						if(isNaN($scope.storageInfo.backup.children[0].distributedVolume[i].revenue)){
							$scope.storageInfo.backup.children[0].distributedVolume[i].revenue = 0;
  						}
					}
				}
			}
		if(value == 'unit'){
			if($scope.storageInfo.storage.children[0].open == true){
				for(var i=0;i< $scope.storageInfo.storage.children[0].distributedVolume.length;i++){
					var performanceunit = "";
					var nonperformanceunit = "";
					var performancevolume = "";
					var standardvolume = "";
						for(var k = 0;k < $scope.storageInfo.storage.children[0].children.length;k++){
							child = $scope.storageInfo.storage.children[0].children[k];
			    			switch(child.id) {
			  				case "1.1": //Server Volume
			  					break;
			  				case "1.1.1": //Performance Server
			  						child.distributedVolume[i].unit = (parseFloat((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue)) / (parseFloat((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume));
			  						if(isNaN(child.distributedVolume[i].unit)){
			  							child.distributedVolume[i].unit = 0;
			  						}
			  						child.distributedVolume[i].unit= child.distributedVolume[i].unit.toFixed(2)
		  						break;
			    			case "1.1.2": //Virtual Server
			    					child.distributedVolume[i].unit = (parseFloat((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue)) / (parseFloat((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume));
			    					if(isNaN(child.distributedVolume[i].unit)){
			  							child.distributedVolume[i].unit = 0;
			  						}
			    					child.distributedVolume[i].unit = child.distributedVolume[i].unit.toFixed(2)
		    					break;
			    			}
						}
						$scope.storageInfo.storage.children[0].distributedVolume[i].unit = ($scope.storageInfo.storage.children[0].distributedVolume[i].revenue / $scope.storageInfo.storage.children[0].distributedVolume[i].volume).toFixed(2);
						if(isNaN($scope.storageInfo.storage.children[0].distributedVolume[i].unit)){
							$scope.storageInfo.storage.children[0].distributedVolume[i].unit = 0.00;
  						}
						$scope.storageInfo.backup.children[0].distributedVolume[i].unit = ($scope.storageInfo.backup.children[0].distributedVolume[i].revenue / $scope.storageInfo.backup.children[0].distributedVolume[i].volume).toFixed(2);
						if(isNaN($scope.storageInfo.backup.children[0].distributedVolume[i].unit)){
							$scope.storageInfo.backup.children[0].distributedVolume[i].unit = 0.00;
  						}
					}
				}
				else{
					for(var i=0;i< $scope.storageInfo.storage.children[0].distributedVolume.length;i++){
						$scope.storageInfo.storage.children[0].distributedVolume[i].unit = ($scope.storageInfo.storage.children[0].distributedVolume[i].revenue / $scope.storageInfo.storage.children[0].distributedVolume[i].volume).toFixed(2);
						if(isNaN($scope.storageInfo.storage.children[0].distributedVolume[i].unit)){
							$scope.storageInfo.storage.children[0].distributedVolume[i].unit = 0.00;
  						}
						$scope.storageInfo.backup.children[0].distributedVolume[i].unit = ($scope.storageInfo.backup.children[0].distributedVolume[i].revenue / $scope.storageInfo.backup.children[0].distributedVolume[i].volume).toFixed(2);
						if(isNaN($scope.storageInfo.backup.children[0].distributedVolume[i].unit)){
							$scope.storageInfo.backup.children[0].distributedVolume[i].unit = 0.00;
  						}
					}
				}
			}
		}


	// calculation of on change of solution setting
	$scope.onChangeDistSetting = function(solId) {
		$scope.customSol=false;
		$scope.selectedSolutionId = solId;
    	$scope.solution = getSelectedStorageSolutionObject(solId);
		setStorageLevelWisePercentage($scope.storageInfo.storage);
		calculateStorageVolume($scope.storageInfo.storage.children[0]);
	};

	// get the storage object
	  function getSelectedStorageSolutionObject(solId){
	    	var sol = {};
	    	for(var i = 0; i < $scope.solList.length; i++ ){
	    		if($scope.solList[i].solutionId == solId){
	    			sol = $scope.solList[i];
	    			break;
	    		}
	    	}
	    	return sol;
	    }


	  // set the storage percentage
	function setStorageLevelWisePercentage(parent){
		if(parent.children != null){
			for(var k = 0;k < parent.children.length;k++){
				var child = parent.children[k];
				switch(child.id) {
				case "1.1.1":
					child.percentage =  $scope.solution? $scope.solution.performanceValue : "";break;
				case "1.1.2":
					child.percentage =  $scope.solution? $scope.solution.nonPerformanceValue :"";break;
				}
				if(child.children != null){
					setStorageLevelWisePercentage(child);
				}
			}
		}
	}

	//Rule for change in back up
	$scope.onChangebackUp=function(selectedBackUp){
        $scope.custom=false;
        $scope.selectedBackUp=selectedBackUp;
        $scope.copyBakcup=angular.copy($scope.storageInfo.storage.children[0].distributedVolume);
        var backUp=_.where($scope.backUpList, {backupId: parseInt(selectedBackUp)})[0];
        if(backUp.backupFrequencyName!='Custom')
               {
        for(var i=0;i<$scope.storageInfo.backup.children[0].distributedVolume.length;i++)
               {
               $scope.storageInfo.backup.children[0].distributedVolume[i].volume=Math.round($scope.copyBakcup[i].volume*backUp.backupSize);
               }}
 }
//set Backup data
 $scope.setBackUp=function()
 {
        $scope.custom=true;
        var CustomBackUp=_.where($scope.backUpList, {backupFrequencyName: 'Custom'})[0];
        $scope.selectedBackUp=CustomBackUp.backupId;
 }

	// calculate the yearly volume.
	$scope.calcVolume = function(node) {
		if($scope.selectedSolutionId != null && $scope.selectedSolutionId != undefined &&  $scope.selectedSolutionId != ''){
			calculateStorageVolume(node);
			if($scope.storageInfo.backup.open && ($scope.selectedBackUp != '')){
				$scope.onChangebackUp($scope.selectedBackUp);
			}
		}
	}


	// mapping the existing volumes.
	  function mapExistingSolutionInfo(){
		  $scope.storageTowerId = $scope.existingStorageInfo.id;
	    	$scope.dealDetails.offshoreSelected = $scope.existingStorageInfo.offshoreAllowed == true ? "Yes" : "No";
	    	$scope.dealDetails.hardwareIncludedSelected = $scope.existingStorageInfo.includeHardware == true ? "Yes" : "No";
	    	$scope.selectedSolutionId = $scope.existingStorageInfo.selectedSolution;
	    	$scope.dealDetails.standardwindowInfoSelected = $scope.existingStorageInfo.serviceWindowSla;
	    	if($scope.existingStorageInfo.backupFrequency != null){
	    		$scope.selectedBackUp = parseInt($scope.existingStorageInfo.backupFrequency);
	    		var CustomBackupfreqName = _.where($scope.backUpList, {backupId: $scope.selectedBackUp})[0].backupFrequencyName;
	   			if(CustomBackupfreqName != 'Custom'){
	   				$scope.onChangebackUp($scope.selectedBackUp);
	   			}
	    	}
	    	if($scope.selectedSolutionId != null){
	   			var CustomSolutionName = _.where($scope.solList, {solutionId: $scope.selectedSolutionId})[0].solutionName;
	   			if(CustomSolutionName != 'Custom'){
	   				$scope.onChangeDistSetting($scope.selectedSolutionId);
	   			}
	   		}
	    	
	    	$scope.dealDetails.towerArchitect = $scope.existingStorageInfo.towerArchitect;
	    	getExistingStorageSolutionYearlyInfo();

	    }
	  // mapping the exsting year data.
	  function getExistingStorageSolutionYearlyInfo(){
	    	if($scope.existingStorageInfo.storageYearlyDataInfoDtos != null){
	    		for (var y = 0; y < $scope.existingStorageInfo.storageYearlyDataInfoDtos.length; y++){
	        		var yearlyDto = $scope.existingStorageInfo.storageYearlyDataInfoDtos[y];
	        		$scope.storageInfo.storage.children[0].distributedVolume[y].year = yearlyDto.year;
	        		$scope.storageInfo.storage.children[0].distributedVolume[y].volume = yearlyDto.storageVolume;

	        		getExistingStorageLevelWiseInfo($scope.storageInfo.storage.children[0],yearlyDto,y);
	        		getExistingStorageServerPricingLevelWiseInfo($scope.storageInfo.storage.children[0],yearlyDto,y);
	        		$scope.storageInfo.backup.children[0].distributedVolume[y].volume = yearlyDto.backupVolume ? yearlyDto.backupVolume : null;

	        	}
	    		$scope.copyBakcup=$scope.storageInfo.storage.children[0].distributedVolume;
	    		//retainOldVolumes($scope.storageInfo.storage.children[0]);
	    	}
	    }


	  // getExistingStorageLevelWiseInfo
	  function getExistingStorageLevelWiseInfo(parent,yearlyDto,year){
		  	//var editSolution = getSelectedStorageSolutionObject($scope.selectedSolutionId);
	    	var child = {};
	    	if(parent.children != null){
	    		for(var k = 0;k < parent.children.length;k++){
	    			child = parent.children[k];
	    			switch(child.id) {
  				case "1.1": //Server Volume
  					break;
  				case "1.1.1": //Performance Server
  						//child.percentage = editSolution.performanceValue;
  						child.distributedVolume[year].volume = yearlyDto.performanceStorage;
    				break;
    			case "1.1.2": //Virtual Server
    					//child.percentage = editSolution.nonPerformanceValue;
    					child.distributedVolume[year].volume = yearlyDto.nonPerformanceStorage;
    				break;

	    			}
	    		}
	    	}
	    }

	  // Get existing Storage server price level wise
	  function getExistingStorageServerPricingLevelWiseInfo(parent,yearlyDto,year){
		  if(yearlyDto.unitPrice.length > 0){
			  if($scope.viewBy.type == 'unit'){
				  $scope.storageInfo.storage.children[0].distributedVolume[year].unit = yearlyDto.unitPrice[0].storageVolumeUnitPrice;
				  $scope.storageInfo.backup.children[0].distributedVolume[year].unit = yearlyDto.unitPrice[0].backupVolumeUnitPrice;
				  for(var k = 0;k < parent.children.length;k++){
		  			child = parent.children[k];
		  			switch(child.id) {
					case "1.1": //Server Volume
						break;
					case "1.1.1": //Performance Server
						child.distributedVolume[year].unit = yearlyDto.unitPrice[0].performanceUnitPrice;
						break;
					case "1.1.2": //Virtual Server
						child.distributedVolume[year].unit = yearlyDto.unitPrice[0].nonPerformanceUnitPrice;
						break;

		  			}
		  		}
			  }
			  if($scope.viewBy.type == 'revenue'){
				  $scope.storageInfo.storage.children[0].distributedVolume[year].revenue = $scope.storageInfo.storage.children[0].distributedVolume[year].volume * yearlyDto.unitPrice[0].storageVolumeUnitPrice;
				  $scope.storageInfo.backup.children[0].distributedVolume[year].revenue = $scope.storageInfo.backup.children[0].distributedVolume[year].volume * yearlyDto.unitPrice[0].backupVolumeUnitPrice;
				  for(var k = 0;k < parent.children.length;k++){
		  			child = parent.children[k];
		  			switch(child.id) {
					case "1.1": //Server Volume
						break;
					case "1.1.1": //Performance Server
						child.distributedVolume[year].revenue = yearlyDto.unitPrice[0].performanceUnitPrice * child.distributedVolume[year].volume;
						break;
					case "1.1.2": //Virtual Server
						child.distributedVolume[year].revenue = yearlyDto.unitPrice[0].nonPerformanceUnitPrice * child.distributedVolume[year].volume;
						break;

		  			}
		  		}
			  }

		  }
	  }


	// get the Indicator value
	    $scope.getIndicator=function()
	    {
	    	var levelIndicator='';
	    	levelIndicator+=$scope.storageInfo.storage.open?1:0;
	    	levelIndicator+=',';
	    	if($scope.storageInfo.storage.open){
	    		levelIndicator+=$scope.storageInfo.storage.children[0].open?1:0;
		    	levelIndicator+=',';
	    	}
	    	else{
	    		levelIndicator+='0,';
	    	}
	    	
	    	levelIndicator+=$scope.storageInfo.backup.open?1:0;
	        return levelIndicator;
	    }

	 // save the storage details
	    $scope.saveStorageInputDetails = function(){
	    	$scope.getIndicator();
			getYearlyInfo();
			$scope.storageInfoDto = {
					offshoreAllowed: ($scope.dealDetails.offshoreSelected=='Yes')?true:false,
					serviceWindowSla: $scope.dealDetails.standardwindowInfoSelected,
					includeHardware: ($scope.dealDetails.hardwareIncludedSelected=='Yes')?true:false,
					selectedSolution: $scope.storageInfo.storage.open == true ? $scope.selectedSolutionId : null,
					dealId:$stateParams.dealId,
					backupFrequency: $scope.storageInfo.backup.open == true ? $scope.selectedBackUp : null,
					towerArchitect: $scope.dealDetails.towerArchitect,
					levelIndicator: $scope.getIndicator(),
					storageYearlyDataInfoDtos: $scope.storageInfo.storageYearlyDataInfoDtos,
					currency:$scope.dealInfoDto.currency
			}

			var url = endpointsurls.STORAGE_SAVE_DETAILS_URL+$stateParams.dealId;
	        customInterceptor.postrequest(url,$scope.storageInfoDto).then(function successCallback(response) {

	    		 var isSaveStorage= DealService.getter() || isSave;
	    		 isSaveStorage.storageS=true;
		       	 DealService.setter(isSaveStorage);
		       	 $scope.putIndicator();
	        	//$alert({title:'Success',text: 'Storage and Backup submitted successfully.'})
		       	 
			}, function errorCallback(response) {
				console.log(response.statusText);
				$alert({title:'Error',text: 'Failed to submit deal.'})
			});
		}

	    
	  //putIndicator
	    $scope.putIndicator=function()
	    {
	    	$scope.submissionIndicator[2]=1;
	        var urlIndicator= endpointsurls.GENERic_INDICATOR+$stateParams.dealId+'?assessmentIndicator='+$scope.assessmentIndicator+'&submissionIndicator='+$scope.submissionIndicator.join(',');
	       customInterceptor.putrequestWithoutData(urlIndicator).then(function successCallback(response) {
	    	   $state.go('home.submission.genericSubmission.totalsSubmission',({dealId:$stateParams.dealId}));
	       });
	    }
	    // get year info
	    function getYearlyInfo(){
			var yearlyInfoList = [];
			for (var y = 0; y < $scope.dealInfo/12; y++){
				var yearlyData = {};
				yearlyData.year = y+1;
				if($scope.storageInfo.storage.open){
					yearlyData.storageVolume = $scope.storageInfo.storage.children[0].distributedVolume[y].volume;
					extractLevelWiseInfo($scope.storageInfo.storage.children[0],yearlyData,y);
				}
				if($scope.storageInfo.backup.open){
					yearlyData.backupVolume = $scope.storageInfo.backup.children[0].distributedVolume[y].volume;
				}

				yearlyData.unitPrice= extractServerUnitPrice($scope.storageInfo.storage.children[0],y);

				yearlyData.revenue = extractServerRevenuePrice($scope.storageInfo.storage.children[0],y);
				yearlyInfoList.push(yearlyData);
			}
			$scope.storageInfo.storageYearlyDataInfoDtos = yearlyInfoList;
		}

	    // extract server price in units
	    function extractServerUnitPrice(parent,year){
	    	var unitPrice = [];
	    	var unitInfo={};

	    	if($scope.viewBy.type == 'unit'){
	    		if($scope.storageInfo.storage.open){
	    			unitInfo.storageVolumeUnitPrice = $scope.storageInfo.storage.children[0].distributedVolume[year].unit;
    				for(var k = 0;k < parent.children.length;k++){
    		    		child = parent.children[k];
    					switch(child.id) {
    					case "1.1": //Storage Volume
    						break;
    					case "1.1.1": //Performance Server pricing
    						unitInfo.performanceUnitPrice = child.distributedVolume[year].unit;
    						break;
    					case "1.1.2": //Standard Server pricing
    						unitInfo.nonPerformanceUnitPrice = child.distributedVolume[year].unit;
    						break;

    					}
	    			}
	    		}
	    		if($scope.storageInfo.backup.open){
	    			unitInfo.backupVolumeUnitPrice = $scope.storageInfo.backup.children[0].distributedVolume[year].unit;
	    		}
	    	}
	    	
	    	if($scope.viewBy.type == 'revenue'){
	    		if($scope.storageInfo.storage.open){
	    			unitInfo.storageVolumeUnitPrice = $scope.storageInfo.storage.children[0].distributedVolume[year].revenue / $scope.storageInfo.storage.children[0].distributedVolume[year].volume;
    				for(var k = 0;k < parent.children.length;k++){
    		    		child = parent.children[k];
    					switch(child.id) {
    					case "1.1": //Storage Volume
    						break;
    					case "1.1.1": //Performance Server pricing
    						unitInfo.performanceUnitPrice = child.distributedVolume[year].revenue / child.distributedVolume[year].volume;
    						break;
    					case "1.1.2": //Standard Server pricing
    						unitInfo.nonPerformanceUnitPrice = child.distributedVolume[year].revenue / child.distributedVolume[year].volume;
    						break;

    					}
    		    	}
	    		}
	    		if($scope.storageInfo.backup.open){
	    			unitInfo.backupVolumeUnitPrice = $scope.storageInfo.backup.children[0].distributedVolume[year].revenue / $scope.storageInfo.backup.children[0].distributedVolume[year].volume;
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
	    		if($scope.storageInfo.storage.open){
	    			revenueInfo.storageRevenue = Math.round(parent.distributedVolume[year].unit * parent.distributedVolume[year].volume) * 12;
	    		}
	    		if($scope.storageInfo.backup.open){
	    			revenueInfo.backupRevenue = Math.round($scope.storageInfo.backup.children[0].distributedVolume[year].unit * $scope.storageInfo.backup.children[0].distributedVolume[year].volume) * 12;
	    		}
	    	}
	    	
	    	if($scope.viewBy.type == 'revenue'){
	    		if($scope.storageInfo.storage.open){
	    			revenueInfo.storageRevenue = parent.distributedVolume[year].revenue * 12;
	    		}
	    		if($scope.storageInfo.backup.open){
	    			revenueInfo.backupRevenue = $scope.storageInfo.backup.children[0].distributedVolume[year].revenue * 12;
	    		}
	    		
	    	}
	    	revenue.push(revenueInfo)
	    	return revenue;
	    }

	    // get the extractLevelWiseInfo
		function extractLevelWiseInfo(parent,yearlyData,year){
			var child = {};
			if(parent.children != null){
				for(var k = 0;k < parent.children.length;k++){
					child = parent.children[k];
					switch(child.id) {
					case "1.1": //Storage Volume
						break;
					case "1.1.1": //Performance
						//$scope.storageInfo.fastStoragePerc = child.percentage;
						yearlyData.performanceStorage = child.distributedVolume[year].volume;

						break;
					case "1.1.2": //Standard
						//$scope.storageInfo.normalStoragePerc = child.percentage;
						yearlyData.nonPerformanceStorage = child.distributedVolume[year].volume;

						break;

					}
				}
//				if($scope.storageInfo.storage.children[0].distributedVolume[year].volume != ((parseInt(yearlyData.performanceStorage)) + (parseInt(yearlyData.nonPerformanceStorage)))){
//					$scope.showvalidationmsg = true;;
//					exit;
//				}
			}
		}


// calculate storage info
	function calculateStorageVolume(parent){
		console.log(parent)
		if(parent.children != null){
			for(var k = 0;k < parent.children.length;k++){
				var child = parent.children[k];

				for (var i = 0; i < $scope.dealInfo/12; i++){
					child.distributedVolume[i].volume = ((parent.distributedVolume[i].volume * child.percentage)/100);
				}

				if(child.children != null){
					calculateStorageVolume(child);
				}
			}
		}
		$scope.calcServerPrice();
	}

	//count countTotaStoragelServers
	function countTotaStoragelServers() {
		var totalVol = 0;
		for(var i =0; i < $scope.storageInfo.storageDetails.distributedVolume.length; i++){
			totalVol = totalVol + parseInt($scope.storageInfo.storageDetails.distributedVolume[i].volume);
		}
		return totalVol;
	}

	//get getStorageServerDistribution
	function getStorageServerDistribution(){
		if($scope.solution == null){
			return 0;
		}
		else{
			return 1;
		}
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

	//Value shown when Tab key press in Volumetric defaults
	  $scope.tabEvent1=function($event,index,childindex,val){
			if($event.keyCode == 13 || $event.keyCode == 9) {
				if(val == 'revenue'){
					for(var i=index;i<$scope.storageInfo.storage.children[0].children[childindex].distributedVolume.length;i++)
					{
						$scope.storageInfo.storage.children[0].children[childindex].distributedVolume[i].revenue = $scope.storageInfo.storage.children[0].children[childindex].distributedVolume[index].revenue;
					}
				}
				if(val == 'unit'){
					for(var i=index;i<$scope.storageInfo.storage.children[0].children[childindex].distributedVolume.length;i++)
					{
						$scope.storageInfo.storage.children[0].children[childindex].distributedVolume[i].unit = $scope.storageInfo.storage.children[0].children[childindex].distributedVolume[index].unit;

					}
				}
			}
		 }


});

