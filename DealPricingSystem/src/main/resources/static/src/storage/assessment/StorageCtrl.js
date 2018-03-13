priceToolcontrollers.controller('StorageCtrl',function($scope,$http,HostingService,EndUserService,StorageService,DealService,customInterceptor,endpointsurls,$stateParams,$dialog,$confirm,$state) {
	$scope.tabs=[{class:'active',href:"#inputtab",tabname:"Input"},{class:'inActive',href:"#caltab",tabname:"Calculate"},{class:'inActive',href:"#resulttab",tabname:"Result"}];

	$scope.dealId = $stateParams.Id

	//initialize all the variable needed for the page.
	

	var ctrl = this;
	ctrl.active = 0;
	$scope.hostingInfo = {};
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
	$scope.disablehosting = true;
	$scope.lastModStorageVol = [];
	$scope.dummy = [0];
	$scope.user = false;
	$scope.hosting = false;
	$scope.selectedBackUp='';
	$scope.viewBy = {type:'unit'};
	$scope.solList = [];
	$scope.solution = {};
	$scope.storageInfoDto = {};
	$scope.dealDetails={};
	$scope.showmsg = false;
	$scope.customSol=false;
	$scope.showvalidationmsg = false;
	$scope.numberArr = [];

	$scope.goToCal=function()
	{
		$scope.tabReset(1);
	}

	$scope.gotoTotals=function()
	{
		$scope.isSaveStorage.storageA=true;
        DealService.setter($scope.isSaveStorage);
        $scope.assessmentIndicator[2]=1;
        var urlIndicator= endpointsurls.GENERic_INDICATOR+$stateParams.dealId+'?assessmentIndicator='+$scope.assessmentIndicator.join(',')+'&submissionIndicator='+$scope.submissionIndicator;
       customInterceptor.putrequestWithoutData(urlIndicator).then(function successCallback(response) {
    	   $state.go('home.assessment.generic.totals',({dealId:$stateParams.dealId}));
       });
		
	}
	
	$scope.userChecked=function(val){
		$scope.user = val;
		updateparentvolume();
	}
	
	$scope.hostingChecked=function(val){
		$scope.hosting = val;
		updateparentvolume();
	}
	
	function updateparentvolume(){
		if($scope.hosting && !$scope.user){
			for(var i=0;i<$scope.storageInfo.storage.children[0].distributedVolume.length;i++){
				$scope.storageInfo.storage.children[0].distributedVolume[i].volume=Math.round(($scope.hostingServerList[i].volume * $scope.storageDefaultInfoDtoList[0].storageSize)/1024);
			}
			$scope.onChangebackUp($scope.selectedBackUp);
		}
		else if(!$scope.hosting && $scope.user){
			for(var i=0;i<$scope.storageInfo.storage.children[0].distributedVolume.length;i++){
				$scope.storageInfo.storage.children[0].distributedVolume[i].volume=Math.round(($scope.dealYearlyDataInfoDtoList[i].noOfUsers*$scope.storageDefaultInfoDtoList[1].storageSize)/1024);
			}
			$scope.onChangebackUp($scope.selectedBackUp);
		}
		else if($scope.hosting && $scope.user){
			for(var i=0;i<$scope.storageInfo.storage.children[0].distributedVolume.length;i++){
				$scope.storageInfo.storage.children[0].distributedVolume[i].volume = (Math.round(($scope.hostingServerList[i].volume * $scope.storageDefaultInfoDtoList[0].storageSize)/1024)) + (Math.round(($scope.dealYearlyDataInfoDtoList[i].noOfUsers * $scope.storageDefaultInfoDtoList[1].storageSize)/1024));
			}
			$scope.onChangebackUp($scope.selectedBackUp);
		}
		else{
			if($scope.id){
				for(var i=0;i<$scope.storageInfo.storage.children[0].distributedVolume.length;i++){
					$scope.storageInfo.storage.children[0].distributedVolume[i].volume = $scope.existingStorageInfo.storageYearlyDataInfoDtos[i].storageVolume;
				}
				$scope.onChangebackUp($scope.selectedBackUp);
			}	
			else{
				for(var i=0;i<$scope.initCopy.storage.children[0].distributedVolume.length;i++){
					$scope.storageInfo.storage.children[0].distributedVolume[i].volume=undefined;
				}
				$scope.onChangebackUp($scope.selectedBackUp);
			}
		}
		calculateStorageVolume($scope.storageInfo.storage.children[0]);
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
	$scope.tabEvent=function($event,index,val){
		if($event.keyCode == 13 ||
	           $event.keyCode == 9)
			{
			for(var i=index;i<$scope.storageInfo.storage.children[0].distributedVolume.length;i++)
				{
			$scope.storageInfo.storage.children[0].distributedVolume[i].volume=$scope.storageInfo.storage.children[0].distributedVolume[index].volume;
			}
	}}


	// function for catching tab event backup
	$scope.tabEventBackup=function($event,index){
		if($event.keyCode == 13 ||
	           $event.keyCode == 9)
			{
			for(var i=index;i<$scope.storageInfo.backup.children[0].distributedVolume.length;i++)
				{
				$scope.storageInfo.backup.children[0].distributedVolume[i].volume=$scope.storageInfo.backup.children[0].distributedVolume[index].volume;
			}
	}}




	//Rule for change in back up
	$scope.onChangebackUp=function(selectedBackUp){
        $scope.custom=false;
        $scope.selectedBackUp=selectedBackUp;
        $scope.copyBakcup=angular.copy($scope.storageInfo.storage.children[0].distributedVolume);
        var backUp=_.where($scope.backUpList, {backupId: parseInt(selectedBackUp)})[0];
        if(backUp.backupFrequencyName!='Custom'){
			for(var i=0;i<$scope.storageInfo.backup.children[0].distributedVolume.length;i++){
				$scope.storageInfo.backup.children[0].distributedVolume[i].volume= Math.round($scope.copyBakcup[i].volume*backUp.backupSize);
			}
		}
	}

	//set Backup data
     $scope.setBackUp=function()
     {
        $scope.custom=true;
        var CustomBackUp=_.where($scope.backUpList, {backupFrequencyName: 'Custom'})[0];
        $scope.selectedBackUp=CustomBackUp.backupId;
     }


	//get default  data of generic screen
	$scope.setDealDeatils=function(){
		$scope.dealDetails.offshoreSelected= $scope.dealInfoDto.offshoreAllowed;
        $scope.dealDetails.standardwindowInfoSelected= $scope.dealInfoDto.serviceWindowSla;
        $scope.dealDetails.hardwareIncludedSelected= $scope.dealInfoDto.includeHardware;
        $scope.dealInfo= $scope.dealInfoDto.dealTerm;
        $scope.storageInfo = StorageService.initStorageDetails($scope.dealInfo);
        $scope.initCopy=angular.copy(StorageService.initStorageDetails($scope.dealInfo));
        if($stateParams.dealId>0){
			 var url = endpointsurls.STORAGE_DETAILS_URL+ $stateParams.dealId;
		        customInterceptor.getrequest(url).then(function successCallback(response) {
	       		$scope.existingStorageInfo = response.data;
	       		$scope.id=response.data.id;
	       		$scope.level=$scope.existingStorageInfo.levelIndicator.split(',');
	       		$scope.storageInfo.storage.open=($scope.level[0]==1)?true:false;
	       		$scope.storageInfo.storage.children[0].open=($scope.level[1]==1)?true:false;
	       		$scope.storageInfo.backup.open=($scope.level[2]==1)?true:false;
	       		if($scope.existingStorageInfo != null && $scope.existingStorageInfo.id!=null){
	       			mapExistingSolutionInfo();
	       		}
	        }, function errorCallback(response) {
	        	$scope.id=undefined;
	        });
		}
	}

	// get the drop-down values
	$scope.getStorageDropdowns=function(){
		var url = endpointsurls.STORAGE_DROPDOWNS+$stateParams.dealId;
        customInterceptor.getrequest(url).then(function successCallback(response) {
			$scope.solList = response.data.storageSolutionsInfoDtoList;
			$scope.backUpList=response.data.storageBackupInfoDtoList;
			$scope.genericDealInfoDto = response.data;
			$scope.dealYearlyDataInfoDtoList=response.data.dealInfoDto.dealYearlyDataInfoDtos;
			$scope.hostingServerList = response.data.hostingServerList;
			if($scope.hostingServerList != null && $scope.hostingServerList.length > 0 && $scope.hostingServerList[0].volume != null){
				$scope.disablehosting = false;
			}
			$scope.storageDefaultInfoDtoList=response.data.storageDefaultInfoDtoList;
			$scope.dynamicTooltipTextHosting= 'By checking this box Q will add 500 GB per server (both physical and virtual) from the ‘Infrastructure and Hosting’ tower on top of the storage volume.';
			$scope.dynamicTooltipTextUser= 'By checking this box Q will add 5 GB per user on top of the storage volume.';
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
        	$scope.isSaveStorage= DealService.getter() || isSave;
            DealService.setter($scope.isSaveStorage);
            $scope.setDealDeatils();

		}, function errorCallback(response) {
			console.log(response.statusText);
			if(response.status=='401'){
				$state.go('login');
			}
		});
	}


	//function for init controller
	$scope.init=function(){
       $scope.selectedBackUp='';
       $scope.custom=false;
       
       $scope.getStorageDropdowns();
       $scope.hostingInfo = HostingService.getHostingDetails();
       $scope.endUserInfo = EndUserService.getEndUserDetails();

	}
	$scope.init();


	// function for tracking the state
//	function retainOldVolumes(level){
//		$scope.lastModStorageVol = [];
//		if(level.levelName == $scope.storageInfo.storage.children[0].levelName){
//			for (var y = 0; y < $scope.dealInfo/12; y++){
//				$scope.lastModStorageVol.push($scope.storageInfo.storage.children[0].distributedVolume[y].volume);
//			}
//		}
//	}

	// for end user and hosting
	$scope.onClickOfCheckBox=function(){
		if($scope.hostingChecked && !$scope.enduserChecked){
			for (var y = 0; y < $scope.dealInfo.dealTermInYears; y++){
				$scope.storageInfo.storage.distributedVolume[y].volume = $scope.hostingInfo.serverhostingDetails.distributedVolume[y].volume;
			}
		}else if( !$scope.hostingChecked && $scope.enduserChecked){
			for (var y = 0; y < $scope.dealInfo.dealTermInYears; y++){
				$scope.storageInfo.storage.distributedVolume[y].volume = $scope.endUserInfo.userDetails.distributedVolume[y].volume;
			}
		}else if( $scope.hostingChecked && $scope.enduserChecked){
			for (var y = 0; y < $scope.dealInfo.dealTermInYears; y++){
				$scope.storageInfo.storage.distributedVolume[y].volume = $scope.hostingInfo.serverhostingDetails.distributedVolume[y].volume + $scope.endUserInfo.userDetails.distributedVolume[y].volume;
			}
		}else{
			for (var y = 0; y < $scope.dealInfo.dealTermInYears; y++){
				$scope.storageInfo.storage.distributedVolume[y].volume = $scope.lastModStorageVol[y];
			}
		}
	}


// when user change the percentage
	$scope.onChangeStoragePercentage = function(node,index) {
		$scope.customSol=true;
		var CustomSolutionId=_.where($scope.solList, {solutionName: 'Custom'})[0];
		$scope.selectedSolutionId=CustomSolutionId.solutionId;
    	/*$scope.selectedSolutionId = 3;*/
		var parent =$scope.storageInfo.storage.children[0];
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
    	var sol =_.where($scope.solList, {solutionId: solId});
    	return sol[0];
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


	// calculate the yearly volume.
	$scope.calcVolume = function(node) {
		if($scope.selectedSolutionId != null && $scope.selectedSolutionId != undefined &&  $scope.selectedSolutionId != ''){
			calculateStorageVolume(node);
			if($scope.storageInfo.backup.open && ($scope.selectedBackUp != '')){
				$scope.onChangebackUp($scope.selectedBackUp);
			}
		}
	}

	//changeChildVolume.
	$scope.changeChildVolume=function()
	{
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
			}
			else{
				$scope.showVolumeErr=false;
			}
		}
	}

	// mapping the existing volumes.
	function mapExistingSolutionInfo(){
    	$scope.storageTowerId = $scope.existingStorageInfo.id;
    	$scope.dealDetails.offshoreSelected = $scope.existingStorageInfo.offshoreAllowed == true ? "Yes" : "No";
    	$scope.dealDetails.hardwareIncludedSelected = $scope.existingStorageInfo.includeHardware == true ? "Yes" : "No";
    	$scope.dealDetails.standardwindowInfoSelected = $scope.existingStorageInfo.serviceWindowSla;
    	$scope.selectedSolutionId = $scope.existingStorageInfo.selectedSolution;
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
    	getExistingStorageSolutionYearlyInfo();
    }

	// mapping the existing year data.
	function getExistingStorageSolutionYearlyInfo(){
    	if($scope.existingStorageInfo.storageYearlyDataInfoDtos != null){
    		for (var y = 0; y < $scope.existingStorageInfo.storageYearlyDataInfoDtos.length; y++){
        		var yearlyDto = $scope.existingStorageInfo.storageYearlyDataInfoDtos[y];
        		$scope.storageInfo.storage.children[0].distributedVolume[y].year = yearlyDto.year;
        		$scope.storageInfo.storage.children[0].distributedVolume[y].volume = yearlyDto.storageVolume;
        		getExistingStorageLevelWiseInfo($scope.storageInfo.storage.children[0],yearlyDto,y);
        		$scope.storageInfo.backup.children[0].distributedVolume[y].volume = yearlyDto.backupVolume ? yearlyDto.backupVolume : null;

        	}
    		//retainOldVolumes($scope.storageInfo.storage.children[0]);
    	 }
	 }


	   //getExistingStorageLevelWiseInfo
	    function getExistingStorageLevelWiseInfo(parent,yearlyDto,year){
	    	//var editSolution = getSelectedStorageSolutionObject($scope.selectedSolutionId);
	    	//console.log(editSolution[0].nonPerformanceValue)
	    	var child = {};
	    	if(parent.children != null){
	    		for(var k = 0;k < parent.children.length;k++){
	    			child = parent.children[k];
	    			switch(child.id) {
    				case "1.1": //Server Volume
    					break;
    				case "1.1.1": //Performance Server
    					//parent.children[k].percentage = editSolution.performanceValue;
    					child.distributedVolume[year].volume = yearlyDto.performanceStorage;
	    				break;
	    			case "1.1.2": //Virtual Server
	    				//parent.children[k].percentage =editSolution.nonPerformanceValue;
	    				child.distributedVolume[year].volume =yearlyDto.nonPerformanceStorage;
	    				break;

	    			}
	    			/*if(child.children != null){
	    				getExistingStorageLevelWiseInfo(child,yearlyDto,year);
		    		}*/
	    		}
	    	}
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
					storageYearlyDataInfoDtos: $scope.storageInfo.storageYearlyDataInfoDtos,
					currency:$scope.dealInfoDto.currency,
					levelIndicator:$scope.getIndicator()
			}
			if($scope.storageInfo.storage.open && !$scope.storageInfo.storage.children[0].open){$confirm({text: 'Default distribution setting has been applied to child levels'})
 	        .then(function() {
 	        	var url = endpointsurls.STORAGE_SAVE_DETAILS_URL+$stateParams.dealId;
 		        customInterceptor.postrequest(url,$scope.storageInfoDto).then(function successCallback(response) {
 		        	$scope.storageId=response.data.id;
 		        	console.log($scope.storageId);
 		        	$scope.tabReset(1)
 		           $scope.populatedCalculateTabDetails();
 				}, function errorCallback(response) {
 					console.log(response.statusText);
 				});

 	        })

		}else
			{
			var url = endpointsurls.STORAGE_SAVE_DETAILS_URL+$stateParams.dealId;
		        customInterceptor.postrequest(url,$scope.storageInfoDto).then(function successCallback(response) {
		        	$scope.storageId=response.data.id;
 		        	console.log($scope.storageId);
		        	$scope.tabReset(1);
 		           $scope.populatedCalculateTabDetails();
				}, function errorCallback(response) {
					console.log(response.statusText);
				});

			}}


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
				yearlyInfoList.push(yearlyData);
			}
			$scope.storageInfo.storageYearlyDataInfoDtos = yearlyInfoList;
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
					if($scope.selectedSolutionId)
						{
					child.distributedVolume[i].volume =Math.round((parent.distributedVolume[i].volume * child.percentage)/100);
						}
					else
						{
						child.distributedVolume[i].volume=undefined;
						}
				}

				if(child.children != null){
					calculateStorageVolume(child);
				}
			}
		}
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


	//   Calculate Tab--------------------------------------------------------------------                //

	// function for populating the calculate tab data.

	$scope.populatedCalculateTabDetails = function(){
		$scope.storageInfo.storage.children[0].children[0].perNonPer='';
		$scope.storageInfo.storage.children[0].children[1].perNonPer='';
		$scope.storageInfo.backup.children[0].backUp='';
		$scope.storageInfo.storage.children[0].childVolumeStorage='';
		var url = endpointsurls.STORAGE_CALCULATOR+$stateParams.dealId;
		customInterceptor.getrequest(url).then(function successCallback(response) {
			$scope.CalculateDto  = response.data;
			extractStorageVolumeAvg($scope.storageInfo.storage.children[0]);
			extractStorageChildLevelWiseAverages($scope.storageInfo.storage.children[0]);
			extractbackupVolumeAvg($scope.storageInfo.backup.children[0]);
			$scope.unitPriceDto=[];
			$scope.createUnitPriceForLevels($scope.storageInfo.backup.children[0].distributedVolume.length);
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}


	// Recalculate
	   $scope.reCalculate=function()
	   {
		   $scope.storageInfo.storage.children[0].children[0].perNonPer='';
			$scope.storageInfo.storage.children[0].children[1].perNonPer='';
			$scope.storageInfo.backup.children[0].backUp='';
			$scope.storageInfo.storage.children[0].childVolumeStorage='';
		 var putDeatls=  {offshoreAllowed: ($scope.dealDetails.offshoreSelected=='Yes')?true:false,
				 	levelOfService: $scope.dealDetails.standardwindowInfoSelected,
					includeHardware: ($scope.dealDetails.hardwareIncludedSelected=='Yes')?true:false
					};
		     var url = endpointsurls.STORAGE_RECALCULATE+$scope.storageId;
	        customInterceptor.putrequest(url,putDeatls).then(function successCallback(response) {
	        	$scope.tabReset(1);
	        	$scope.CalculateDto  = response.data;
				extractStorageVolumeAvg($scope.storageInfo.storage.children[0]);
				extractStorageChildLevelWiseAverages($scope.storageInfo.storage.children[0]);
				extractbackupVolumeAvg($scope.storageInfo.backup.children[0]);});
	        
	        $scope.createUnitPriceForLevels($scope.storageInfo.backup.children[0].distributedVolume.length);
	   }
	// function for populating the storage level calculate tab data.

	function extractStorageVolumeAvg(parent){
		if($scope.CalculateDto.storageCalculateDto!=null){
		parent.benchLow = $scope.CalculateDto.storageCalculateDto.benchDealLowAvgUnitPrice!=null?$scope.CalculateDto.storageCalculateDto.benchDealLowAvgUnitPrice:"NA";
		parent.benchTarget =$scope.CalculateDto.storageCalculateDto.benchDealTargetAvgUnitPrice!=null?$scope.CalculateDto.storageCalculateDto.benchDealTargetAvgUnitPrice:"NA";
		parent.pastAvg = $scope.CalculateDto.storageCalculateDto.pastDealAvgUnitPrice!=null?$scope.CalculateDto.storageCalculateDto.pastDealAvgUnitPrice:"NA";
		parent.compAvg =$scope.CalculateDto.storageCalculateDto.compDealAvgUnitPrice!=null?$scope.CalculateDto.storageCalculateDto.compDealAvgUnitPrice:"NA";
	}else{
		parent.benchLow='NA';
		parent.benchTarget ='NA';
		parent.pastAvg='NA';
		parent.compAvg='NA'
	}
		}

	// function for populating the backup level calculate tab data.
	function extractbackupVolumeAvg(parent){
		if($scope.CalculateDto.backupCalculateDto!=null){
			parent.benchLow = $scope.CalculateDto.backupCalculateDto.benchDealLowAvgUnitPrice!=null?$scope.CalculateDto.backupCalculateDto.benchDealLowAvgUnitPrice:"NA";
			parent.benchTarget =$scope.CalculateDto.backupCalculateDto.benchDealTargetAvgUnitPrice!=null?$scope.CalculateDto.backupCalculateDto.benchDealTargetAvgUnitPrice:"NA";
			parent.pastAvg = $scope.CalculateDto.backupCalculateDto.pastDealAvgUnitPrice!=null?$scope.CalculateDto.backupCalculateDto.pastDealAvgUnitPrice:"NA";
			parent.compAvg =$scope.CalculateDto.backupCalculateDto.compDealAvgUnitPrice!=null?$scope.CalculateDto.backupCalculateDto.compDealAvgUnitPrice:"NA";
	}else{
		parent.benchLow='NA';
		parent.benchTarget ='NA';
		parent.pastAvg='NA';
		parent.compAvg='NA'
	}}


	// function for populating the Performance and Standard level calculate tab data.
	function extractStorageChildLevelWiseAverages(parent){
		var child = {};
		if(parent.children != null){
			for(var k = 0;k < parent.children.length;k++){
				child = parent.children[k];
				switch(child.id) {
				case "1.1": //Storage Volume
					break;
				case "1.1.1": //Performance
					if($scope.CalculateDto.performanceCalculateDto!=null){
						child.benchLow = $scope.CalculateDto.performanceCalculateDto.benchDealLowAvgUnitPrice!=null?$scope.CalculateDto.performanceCalculateDto.benchDealLowAvgUnitPrice:"NA";
						child.benchTarget =$scope.CalculateDto.performanceCalculateDto.benchDealTargetAvgUnitPrice!=null?$scope.CalculateDto.performanceCalculateDto.benchDealTargetAvgUnitPrice:"NA";
						child.pastAvg = $scope.CalculateDto.performanceCalculateDto.pastDealAvgUnitPrice!=null?$scope.CalculateDto.performanceCalculateDto.pastDealAvgUnitPrice:"NA";
						child.compAvg =$scope.CalculateDto.performanceCalculateDto.compDealAvgUnitPrice!=null?$scope.CalculateDto.performanceCalculateDto.compDealAvgUnitPrice:"NA";
						child['valid']=false;
					}
					else
						{
						child.benchLow='NA';
						child.benchTarget ='NA';
						child.pastAvg='NA';
						child.compAvg='NA'
						child['valid']=true;
						}
					break;
				case "1.1.2": //Standard
					if($scope.CalculateDto.nonPerformanceCalculateDto!=null){
						child.benchLow = $scope.CalculateDto.nonPerformanceCalculateDto.benchDealLowAvgUnitPrice!=null?$scope.CalculateDto.nonPerformanceCalculateDto.benchDealLowAvgUnitPrice:"NA";
						child.benchTarget =$scope.CalculateDto.nonPerformanceCalculateDto.benchDealTargetAvgUnitPrice!=null?$scope.CalculateDto.nonPerformanceCalculateDto.benchDealTargetAvgUnitPrice:"NA";
						child.pastAvg = $scope.CalculateDto.nonPerformanceCalculateDto.pastDealAvgUnitPrice!=null?$scope.CalculateDto.nonPerformanceCalculateDto.pastDealAvgUnitPrice:"NA";
						child.compAvg =$scope.CalculateDto.nonPerformanceCalculateDto.compDealAvgUnitPrice!=null?$scope.CalculateDto.nonPerformanceCalculateDto.compDealAvgUnitPrice:"NA";
						child['valid']=false;
					}
				else
					{
					child.benchLow='NA';
					child.benchTarget ='NA';
					child.pastAvg='NA';
					child.compAvg='NA'
					child['valid']=true;
					}
					break;

				}
				if(child.children != null){
					extractStorageChildLevelWiseAverages(child);
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
			    case '1.1'://storage
			    	$scope.past=$scope.CalculateDto.storageCalculateDto.pastDealYearlyCalcDtoList;
			    	 $scope.setUnitPriceForLevels(child,$scope.past);
			        break;
			    case '1.1.1'://performance
			    	$scope.past=$scope.CalculateDto.performanceCalculateDto.pastDealYearlyCalcDtoList;
			    	 $scope.setUnitPriceForLevels(child,$scope.past);
			        break;
			    case '1.1.2'://standard
			    	$scope.past=$scope.CalculateDto.nonPerformanceCalculateDto.pastDealYearlyCalcDtoList;
			    	 $scope.setUnitPriceForLevels(child,$scope.past);
			    	break;
			    case '2.1'://backup
			    	$scope.past=$scope.CalculateDto.backupCalculateDto.pastDealYearlyCalcDtoList;
			    	 $scope.setUnitPriceForLevels(child,$scope.past);
			    	break;
			    	}
		        break;
		    case 'benchLow':// benchLow
		    	switch (child.id) {
			    case '1.1'://storage
			    	$scope.benchLow=$scope.CalculateDto.storageCalculateDto.benchmarkLowYearlyCalcDtoList;
			    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
			        break;
			    case '1.1.1'://performance
			    	$scope.benchLow=$scope.CalculateDto.performanceCalculateDto.benchmarkLowYearlyCalcDtoList;
			    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
			        break;
			    case '1.1.2'://standard
			    	$scope.benchLow=$scope.CalculateDto.nonPerformanceCalculateDto.benchmarkLowYearlyCalcDtoList;
			    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
			    	break;
			    case '2.1'://backup
			    	$scope.benchLow=$scope.CalculateDto.backupCalculateDto.benchmarkLowYearlyCalcDtoList;
			    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
			    	break;
			    	}
		        break;
		    case 'benchTarget':// benchTarget
		    	switch (child.id) {
			    case '1.1'://storage
			    	$scope.benchTarget=$scope.CalculateDto.storageCalculateDto.benchmarkTargetYearlyCalcDtoList;
			    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
			        break;
			    case '1.1.1'://performance
			    	$scope.benchTarget=$scope.CalculateDto.performanceCalculateDto.benchmarkTargetYearlyCalcDtoList;
			    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
			        break;
			    case '1.1.2'://standard
			    	$scope.benchTarget=$scope.CalculateDto.nonPerformanceCalculateDto.benchmarkTargetYearlyCalcDtoList;
			    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
			    	break;
			    case '2.1'://backup
			    	$scope.benchTarget=$scope.CalculateDto.backupCalculateDto.benchmarkTargetYearlyCalcDtoList;
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
	  $scope.setUnitPriceForLevels=function(child,untiPrices,nonuUnitPrice)
	  {
		  switch (child.id) {
		    case '1.1'://storage
		    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].storageUnitPrice=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].storageRevenue=untiPrices[i].revenue;
		    		}
		        break;
		    case '1.1.1'://performance
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
	    		$scope.unitPriceDto[i].performanceUnitPrice=untiPrices[i].unitPrice;
	    		$scope.unitPriceDto[i].performanceRevenue=untiPrices[i].revenue;
	    		$scope.unitPriceDto[i].storageRevenue=$scope.unitPriceDto[i].performanceRevenue+$scope.unitPriceDto[i].nonPerformanceRevenue;
	    		$scope.unitPriceDto[i].storageUnitPrice=$scope.unitPriceDto[i].storageRevenue/$scope.storageInfo.storage.children[0].distributedVolume[i].volume;
	    		}
		        break;
		    case '1.1.2'://standard
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
	    		$scope.unitPriceDto[i].nonPerformanceUnitPrice=untiPrices[i].unitPrice;
	    		$scope.unitPriceDto[i].nonPerformanceRevenue=untiPrices[i].revenue;
	    		$scope.unitPriceDto[i].storageRevenue=$scope.unitPriceDto[i].performanceRevenue+$scope.unitPriceDto[i].nonPerformanceRevenue;
	    		$scope.unitPriceDto[i].storageUnitPrice=$scope.unitPriceDto[i].storageRevenue/$scope.storageInfo.storage.children[0].distributedVolume[i].volume;
	    		}
		    	break;
		    case '2.1'://backup
		    	for(var i=0;i<untiPrices.length;i++)
	    		{
	    		$scope.unitPriceDto[i].backupUnitPrice=untiPrices[i].unitPrice;
	    		$scope.unitPriceDto[i].backupRevenue=untiPrices[i].revenue;
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
			  $scope.unitPriceDto.push({year:i,performanceUnitPrice:0,nonPerformanceUnitPrice:0,storageUnitPrice:0,backupUnitPrice:0})
			  }

	  }

	// Modal for find deal section
		var dialogOptions = {
		    controller: 'DealInformationCtrl',
		    templateUrl: '/modals/dealInformation/dealInformation.html'
		  };
		  $scope.getNearestDeal = function(child,key){
			  var infoModel={id:child.id,dealType:key,url:endpointsurls.STORAGE_NEAREST_DEAL};
		    $dialog.dialog(angular.extend(dialogOptions, {resolve: {infoModel: angular.copy(infoModel)}}))
		      .open()
		      .then(function(result) {
		        if(result) {
		          //angular.copy(result, infoModel);
		        }
		        infoModel = undefined;
		    });
		  };



	// back to calculate tab

	$scope.backToInput=function()
	{
		$scope.tabReset(0);
	}

	// back to calculate tab
	$scope.backToCalculate=function()
	{
		$scope.tabReset(1);
	}


	// go to result tab ****************************************************************************************

	$scope.goToResult=function()
	{
		for(var i=0;i<$scope.unitPriceDto.length;i++)
		  {
		  $scope.unitPriceDto[i].storageRevenue=$scope.unitPriceDto[i].storageRevenue*12;
		  $scope.unitPriceDto[i].backupRevenue=$scope.unitPriceDto[i].backupRevenue*12;
		  }
		var url = endpointsurls.STORAGE_RESULT+$scope.storageId;
	     customInterceptor.putrequest(url,$scope.unitPriceDto).then(function successCallback(response) {
	    	 var result = endpointsurls.STORAGE_DETAILS_URL+ $stateParams.dealId;
		        customInterceptor.getrequest(result).then(function successCallback(response) {
		        	$scope.existingStorageInfo = response.data;
		        	if($scope.existingStorageInfo != null && $scope.existingStorageInfo.id!=null){
		        		mapExistingStorageSolutionYearlyInfo();
		       		}
		        	$scope.tabReset(2);
		        	$scope.viewBy = {type:'unit'};
		        });
			}, function errorCallback(response) {
				console.log(response.statusText);
			});

	}

	function mapExistingStorageSolutionYearlyInfo(){
    	if($scope.existingStorageInfo.storageYearlyDataInfoDtos != null){
    		for (var y = 0; y < $scope.existingStorageInfo.storageYearlyDataInfoDtos.length; y++){
        		var yearlyDto = $scope.existingStorageInfo.storageYearlyDataInfoDtos[y];
        		$scope.storageInfo.storage.children[0].distributedVolume[y].year = yearlyDto.year;
        		$scope.storageInfo.storage.children[0].distributedVolume[y].volume = yearlyDto.storageVolume;
        		$scope.storageInfo.backup.children[0].distributedVolume[y].volume = yearlyDto.backupVolume ? yearlyDto.backupVolume : null;
        		getExistingStorageLevelWiseInfo($scope.storageInfo.storage.children[0],yearlyDto,y);
        		getExistingStorageServerPricingLevelWiseInfo($scope.storageInfo.storage.children[0],yearlyDto,y);
        	}
    	}
    }

	  //getExistingStorageLevelWiseInfo
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

	//getExistingStorageServerPricingLevelWiseInfo
	  function getExistingStorageServerPricingLevelWiseInfo(parent,yearlyDto,year){
		  if(yearlyDto.unitPrice.length > 0){
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
	  }

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
				  						child.distributedVolume[i].revenue = Math.round((parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume))  * (parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit)));
				  						if(isNaN(child.distributedVolume[i].revenue)){
				  							child.distributedVolume[i].revenue= 0;
				  						}
				  						performancerevenue = child.distributedVolume[i].revenue;
				  						performancevolume = parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
				    				break;
				    			case "1.1.2": //Virtual Server
				    					child.distributedVolume[i].revenue = Math.round((parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume))  * (parseFloat((child.distributedVolume[i].unit=='' || child.distributedVolume[i].unit==undefined || child.distributedVolume[i].unit==null) ?0:child.distributedVolume[i].unit)));
				    					if(isNaN(child.distributedVolume[i].revenue)){
				  							child.distributedVolume[i].revenue= 0;
				  						}
				    					nonperformancerevenue = child.distributedVolume[i].revenue;
				    					standardvolume = parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
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
							$scope.storageInfo.storage.children[0].distributedVolume[i].revenue = Math.round($scope.storageInfo.storage.children[0].distributedVolume[i].volume * $scope.storageInfo.storage.children[0].distributedVolume[i].unit);
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
				  						child.distributedVolume[i].unit = (parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue)) / (parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume));
				  						if(isNaN(child.distributedVolume[i].unit)){
				  							child.distributedVolume[i].unit = 0;
				  						}
				  						child.distributedVolume[i].unit = (child.distributedVolume[i].unit).toFixed(2);
				  						performanceunit = (child.distributedVolume[i].unit).toFixed(2);
				  						performancevolume = parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
				    				break;
				    			case "1.1.2": //Virtual Server
				    					child.distributedVolume[i].unit = (parseInt((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:child.distributedVolume[i].revenue)) / (parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume));
				    					if(isNaN(child.distributedVolume[i].unit)){
				  							child.distributedVolume[i].unit = 0;
				  						}
				    					child.distributedVolume[i].unit = (child.distributedVolume[i].unit).toFixed(2);
				    					nonperformanceunit = (child.distributedVolume[i].unit).toFixed(2);
				    					standardvolume = parseInt((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);;
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
});

