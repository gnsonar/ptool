priceToolcontrollers.controller('ServiceDeskCtrl',function($scope,$http,HostingService,EndUserService,StorageService,DealService,ServiceDeskService,customInterceptor,endpointsurls,$stateParams,$dialog,$confirm,$state,$alert) {
	$scope.tabs=[{class:'active',href:"#inputtab",tabname:"Input"},{class:'inActive',href:"#caltab",tabname:"Calculate"},{class:'inActive',href:"#resulttab",tabname:"Result"}];

	//initialize all the variable needed for the page.
	

	var ctrl = this;
	ctrl.active = 0;

	$scope.dealId = $stateParams.Id
	$scope.dealInfo={};
	$scope.serviceDeskInfo = {};
	$scope.pastAvgUnitPriceList=[];
	$scope.compAvgUnitPriceList=[];
	$scope.benchmarkLowAvgUnitPriceList=[];
	$scope.benchmarkTargetAvgUnitPriceList=[];
	$scope.servicedeskInfoDto = {};
	$scope.dealDetails = {};
    $scope.contactDistList = {};
    $scope.selectedContactId = {};
    $scope.solution = {};
    $scope.viewBy = {type:'unit'};
	$scope.serviceDeskDto = {};
	$scope.existingServiceDeskInfo = {};
	$scope.serviceDeskCalculateDto = {};
	$scope.userChecked = false;
	$scope.lastModContactVol = [];
	$scope.showmsg = false;
	$scope.showvalidationmsg = false;
	$scope.selectedServiceDeskId = {};
	$scope.check = true;
	$scope.user = false;
	$scope.custom=false;

	$scope.contactDetail = {
			open : false
	};

	$scope.gotoTotals=function()
	{
		$scope.serviceSave= DealService.getter() || isSave;
		$scope.serviceSave.serviceDeskA=true;
		DealService.setter($scope.serviceSave);
		$scope.assessmentIndicator[5]=1;
        var urlIndicator= endpointsurls.GENERic_INDICATOR+$stateParams.dealId+'?assessmentIndicator='+$scope.assessmentIndicator.join(',')+'&submissionIndicator='+$scope.submissionIndicator;
       customInterceptor.putrequestWithoutData(urlIndicator).then(function successCallback(response) {
    	   $state.go('home.assessment.generic.totals',({dealId:$stateParams.dealId}));
       });
		
	}

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

	//function for stop the tab propagation
	$scope.tabRest=function(e)
	{
		e.stopPropagation();
		e.preventDefault();
	}

	//function for catching tab event solution
	$scope.tabEvent=function($event,index,val){
		if($event.keyCode == 13 ||
	           $event.keyCode == 9)
			{
			for(var i=index;i<$scope.serviceDeskInfo.contact.children[0].distributedVolume.length;i++)
				{
			$scope.serviceDeskInfo.contact.children[0].distributedVolume[i].volume=$scope.serviceDeskInfo.contact.children[0].distributedVolume[index].volume;
			}
	}}

	// function for user checked
	$scope.userChecked = function(val){
        $scope.user = val;
        if(val){
     for(var i=0;i<$scope.serviceDeskInfo.contact.children[0].distributedVolume.length;i++)
               {
           $scope.copyBackupVolume=angular.copy($scope.copyBackup);
           $scope.serviceDeskInfo.contact.children[0].distributedVolume[i].volume = Math.round($scope.dealDetails.dealYearlyDataInfoDtos[i].noOfUsers * $scope.contactRatio);
               }
        }
        else{
               if($scope.id){
                      $scope.serviceDeskInfo.contact.children[0].distributedVolume=$scope.copyBackupVolume;
               }
               else{
                     for(var i=0;i<$scope.initCopy.contact.children[0].distributedVolume.length;i++)
               {
                                   $scope.serviceDeskInfo.contact.children[0].distributedVolume[i].volume=undefined;
               }
               }
        }
        calculateServiceDeskVolume($scope.serviceDeskInfo.contact.children[0]);
 }


	// get the drop-down values
	$scope.getServiceDeskDropdowns=function(){
		var url = endpointsurls.SERVICE_DROPDOWN+$stateParams.dealId;
        customInterceptor.getrequest(url).then(function successCallback(response) {
        	$scope.solList = response.data.serviceDeskSolutionsDtoList;
        	$scope.contactRatioList = response.data.serviceDeskContactRatioDtoList;
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
        	$scope.serviceSave= DealService.getter() || isSave;
            DealService.setter($scope.serviceSave);
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
	        $scope.dealDetails.dealYearlyDataInfoDtos = $scope.dealInfoDto.dealYearlyDataInfoDtos;
	        $scope.dealInfo= $scope.dealInfoDto.dealTerm;
	        $scope.serviceDeskInfo = ServiceDeskService.initServiceDeskDetails($scope.dealInfo);
	        $scope.initCopy=angular.copy(ServiceDeskService.initServiceDeskDetails($scope.dealInfo));
	        if($stateParams.dealId>0){
	        	var url = endpointsurls.SAVE_SERVICE_INFO+ $stateParams.dealId;
	        	customInterceptor.getrequest(url).then(function successCallback(response) {
	        		$scope.existingServiceDeskInfo = response.data;
		       		$scope.id=response.data.serviceDeskId;
		       		$scope.contactRatioSelectedId = $scope.existingServiceDeskInfo.selectedContactRatio;
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
	       		getExistingVolumeLevelWiseInfo($scope.serviceDeskInfo.contact.children[0],yearlyDto,y)
	       		$scope.copyBackup=angular.copy($scope.serviceDeskInfo.contact.children[0].distributedVolume);
   			}
	   	}
    }

   // get Existing Service Desk LevelWise Info
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
	}


	// User change Contact distribution setting
	$scope.onChangeDistSetting = function(solId) {
    	$scope.selectedSolutionId = solId;
    	$scope.solution = getSelectedServiceDeskSolutionObject(solId);
    	setLevelWiseServiceDeskPercentage($scope.serviceDeskInfo.contact);
    	$scope.showErr=false;
    	$scope.showVolumeErr=false;
    };

    //On change Contact Ratio
    $scope.onChangeContactRatio = function(contactRatioId){
    	$scope.contactRatioSelectedId = contactRatioId;
    	if($scope.contactRatioSelectedId != null){
    		$scope.contactRatio = getSelectedContactRatio($scope.contactRatioSelectedId);
        	$scope.check = false;
    	}
    	else{
    		$scope.check = true;
    	}
    	if($scope.user){
    		$scope.userChecked($scope.user);
    	}
    }

    // set volume on change of percentage
    function setLevelWiseServiceDeskPercentage(parent) {
    	if(parent.children != null) {
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

    // function for setting the custom value
	$scope.setCustom=function()
	{
		$scope.custom=true;
		var CustomSolutionId=_.where($scope.solList, {solutionName: 'Custom'})[0];
		$scope.selectedSolutionId=CustomSolutionId.solutionId;
	}

    // on change of parent volume
    $scope.calcVolume = function(node) {
    	if($scope.selectedSolutionId != null && $scope.selectedSolutionId != undefined &&  $scope.selectedSolutionId != ''){
    		calculateServiceDeskVolume(node);
    	}
    }

    //calculate volume on change
    function calculateServiceDeskVolume(parent) {
    	if(parent.children != null){
    		for(var k = 0;k < parent.children.length;k++) {
    			var child = parent.children[k];
        		for (var i = 0; i < $scope.dealInfo/12; i++){
        			child.distributedVolume[i].volume = Math.round((parent.distributedVolume[i].volume * child.percentage)/100);
        		}
    		}
    	}
    }

	// get the selected solution
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

    // get the selected contact ratio
    function getSelectedContactRatio(Id) {
    	var ratio = {};
    	for(var i = 0; i < $scope.contactRatioList.length; i++ ) {
    		if($scope.contactRatioList[i].id == Id) {
    			ratio = $scope.contactRatioList[i].contactRatio;
    			break;
    		}
    	}
    	return ratio;
    }

	    // Save Service Desk input form data
	    $scope.saveServiceDeskInfo = function(){
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
	    	    	selectedContactRatio : $scope.contactRatioSelectedId
	    	}
	    	if(!$scope.serviceDeskInfo.contact.children[0].open){$confirm({text: 'Default distribution setting has been applied to child levels'})
	 	        .then(function() {
	    	var url = endpointsurls.SAVE_SERVICE_INFO+$stateParams.dealId;
	        customInterceptor.postrequest(url,$scope.servicedeskInfoDto).then(function successCallback(response) {
	    		$scope.goToCal();
	    		$scope.serviceId=response.data.serviceDeskId;
	    		//$alert({title:'Success',text: 'Service Desk submitted successfully.'})
			}, function errorCallback(response) {
				console.log(response.statusText);
				$alert({title:'Error',text: 'Failed to save data.'})
			});})}else
				{
				var url = endpointsurls.SAVE_SERVICE_INFO+$stateParams.dealId;
		        customInterceptor.postrequest(url,$scope.servicedeskInfoDto).then(function successCallback(response) {
		    		$scope.goToCal();
		    		$scope.serviceId=response.data.serviceDeskId;
		    		//$alert({title:'Success',text: 'Service Desk submitted successfully.'})
				}, function errorCallback(response) {
					console.log(response.statusText);
					$alert({title:'Error',text: 'Failed to save data.'})
				});
				};
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
	    		yearlyInfoList.push(yearlyData);
	    	}
	    	$scope.serviceDeskInfo.serviceDeskYearlyDataInfoDtos = yearlyInfoList;
	    }



    //*********************************Calculate tab*****************************//*

	   $scope.populatedCalculateTabDetails = function() {
		   var url = endpointsurls.SERVICE_CALCULATE_INFO+$stateParams.dealId;
	        customInterceptor.getrequest(url).then(function successCallback(response) {
	        	$scope.calculateServiceInfo=response.data;
	        	 $scope.extractServiceAvg($scope.serviceDeskInfo.contact.children[0]);
	        	 $scope.unitPriceDto=[];
	 			$scope.createUnitPriceForLevels($scope.serviceDeskInfo.contact.children[0].distributedVolume.length);
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
               "multiLingual" :  $scope.serviceDeskInfo.multiLingual == "Yes" ? true : false,
               "toolingIncluded" : $scope.serviceDeskInfo.tooling == "Yes" ? true : false};
		     var url = endpointsurls.SERVICE_RECALCULATE+$scope.serviceId;
	        customInterceptor.putrequest(url,putDeatls).then(function successCallback(response) {
	        	$scope.tabReset(1);
		   $scope.calculateServiceInfo=response.data;
      	   $scope.extractServiceAvg($scope.serviceDeskInfo.contact.children[0]);
      	   });
	        $scope.createUnitPriceForLevels($scope.serviceDeskInfo.contact.children[0].distributedVolume.length);
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
			$scope.serviceDeskInfo.contact.children[0].selectedRadio='';
			$scope.serviceDeskInfo.contact.children[0].children[0].perNonPer='';
			$scope.serviceDeskInfo.contact.children[0].children[1].perNonPer='';
			$scope.serviceDeskInfo.contact.children[0].children[2].perNonPer='';
			$scope.serviceDeskInfo.contact.children[0].children[3].perNonPer='';
		}

	// function used for creating the unit price year wise
		  $scope.createUnitPriceForLevels=function(val)
		  {
			  $scope.unitPriceDto=[];
			  for(var i=1;i<=val;i++)
				  {
				  var unitPrice={};
				  unitPrice.year=i;
				  unitPrice.totalContactsUnitPrice=0;
				  unitPrice.totalContactsRevenue=0;
				  unitPrice.voiceContactsUnitPrice=0;
				  unitPrice.voiceContactsRevenue=0;
				  unitPrice.mailContactsUnitPrice=0;
				  unitPrice.mailContactsRevenue=0;
				  unitPrice.chatContactsUnitPrice=0;
				  unitPrice.chatContactsRevenue=0;
				  unitPrice.portalContactsUnitPrice=0;
				  unitPrice.portalContactsRevenue=0;
				  $scope.unitPriceDto.push(unitPrice);
				  }

		  }

		  // function for extractAppAvg
	   $scope.extractServiceAvg=function(parent) {
	       if($scope.calculateServiceInfo.totalContactsCalculateDto!=null)
        	{
	    	parent.benchLow = $scope.calculateServiceInfo.totalContactsCalculateDto.benchDealLowAvgUnitPrice !=null ? $scope.calculateServiceInfo.totalContactsCalculateDto.benchDealLowAvgUnitPrice :"NA";
	        parent.benchTarget =  $scope.calculateServiceInfo.totalContactsCalculateDto.benchDealTargetAvgUnitPrice !=null ? $scope.calculateServiceInfo.totalContactsCalculateDto.benchDealTargetAvgUnitPrice:"NA";
	        parent.pastAvg = $scope.calculateServiceInfo.totalContactsCalculateDto.pastDealAvgUnitPrice !=null  ? $scope.calculateServiceInfo.totalContactsCalculateDto.pastDealAvgUnitPrice : "NA";
	        parent.compAvg = $scope.calculateServiceInfo.totalContactsCalculateDto.compDealAvgUnitPrice !=null? $scope.calculateServiceInfo.totalContactsCalculateDto.compDealAvgUnitPrice :"NA";
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
	            case "1.1.1": //voice
	            	if($scope.calculateServiceInfo.voiceContactsCalculateDto!=null){
	                child.benchLow =$scope.calculateServiceInfo.voiceContactsCalculateDto.benchDealLowAvgUnitPrice!=null ? $scope.calculateServiceInfo.voiceContactsCalculateDto.benchDealLowAvgUnitPrice : "NA";
	                child.benchTarget =$scope.calculateServiceInfo.voiceContactsCalculateDto.benchDealTargetAvgUnitPrice!=null ? $scope.calculateServiceInfo.voiceContactsCalculateDto.benchDealTargetAvgUnitPrice : "NA";
	                child.pastAvg = $scope.calculateServiceInfo.voiceContactsCalculateDto.pastDealAvgUnitPrice!=null? $scope.calculateServiceInfo.voiceContactsCalculateDto.pastDealAvgUnitPrice : "NA";
	                child.compAvg = $scope.calculateServiceInfo.voiceContactsCalculateDto.compDealAvgUnitPrice!=null ? $scope.calculateServiceInfo.voiceContactsCalculateDto.compDealAvgUnitPrice : "NA";
	            	}else
	            		{
	            		child.benchLow='NA';
	            		child.benchTarget='NA';
		        		child.pastAvg='NA';
		        		child.compAvg='NA';
	            		}
	            	console.log(child);
	                break;
	            case "1.1.2": //MAIL
	            	if($scope.calculateServiceInfo.mailContactsCalculateDto!=null){
		                child.benchLow =$scope.calculateServiceInfo.mailContactsCalculateDto.benchDealLowAvgUnitPrice!=null ? $scope.calculateServiceInfo.mailContactsCalculateDto.benchDealLowAvgUnitPrice : "NA";
		                child.benchTarget =$scope.calculateServiceInfo.mailContactsCalculateDto.benchDealTargetAvgUnitPrice!=null ? $scope.calculateServiceInfo.mailContactsCalculateDto.benchDealTargetAvgUnitPrice : "NA";
		                child.pastAvg = $scope.calculateServiceInfo.mailContactsCalculateDto.pastDealAvgUnitPrice!=null? $scope.calculateServiceInfo.mailContactsCalculateDto.pastDealAvgUnitPrice : "NA";
		                child.compAvg = $scope.calculateServiceInfo.mailContactsCalculateDto.compDealAvgUnitPrice!=null ? $scope.calculateServiceInfo.mailContactsCalculateDto.compDealAvgUnitPrice : "NA";
		            	}else
		            		{
		            		child.benchLow='NA';
		            		child.benchTarget='NA';
			        		child.pastAvg='NA';
			        		child.compAvg='NA';
		            		}
	                break;
	            case "1.1.3": //Chat
	            	if($scope.calculateServiceInfo.chatContactsCalculateDto!=null){
		                child.benchLow =$scope.calculateServiceInfo.chatContactsCalculateDto.benchDealLowAvgUnitPrice!=null ? $scope.calculateServiceInfo.chatContactsCalculateDto.benchDealLowAvgUnitPrice : "NA";
		                child.benchTarget =$scope.calculateServiceInfo.chatContactsCalculateDto.benchDealTargetAvgUnitPrice!=null ? $scope.calculateServiceInfo.chatContactsCalculateDto.benchDealTargetAvgUnitPrice : "NA";
		                child.pastAvg = $scope.calculateServiceInfo.chatContactsCalculateDto.pastDealAvgUnitPrice!=null? $scope.calculateServiceInfo.chatContactsCalculateDto.pastDealAvgUnitPrice : "NA";
		                child.compAvg = $scope.calculateServiceInfo.chatContactsCalculateDto.compDealAvgUnitPrice!=null ? $scope.calculateServiceInfo.chatContactsCalculateDto.compDealAvgUnitPrice : "NA";
		            	}else
		            		{
		            		child.benchLow='NA';
		            		child.benchTarget='NA';
			        		child.pastAvg='NA';
			        		child.compAvg='NA';
		            		}
	                break;
	            case "1.1.4": //portal
	            	if($scope.calculateServiceInfo.portalContactsCalculateDto!=null){
		                child.benchLow =$scope.calculateServiceInfo.portalContactsCalculateDto.benchDealLowAvgUnitPrice!=null ? $scope.calculateServiceInfo.portalContactsCalculateDto.benchDealLowAvgUnitPrice : "NA";
		                child.benchTarget =$scope.calculateServiceInfo.portalContactsCalculateDto.benchDealTargetAvgUnitPrice!=null ? $scope.calculateServiceInfo.portalContactsCalculateDto.benchDealTargetAvgUnitPrice : "NA";
		                child.pastAvg = $scope.calculateServiceInfo.portalContactsCalculateDto.pastDealAvgUnitPrice!=null? $scope.calculateServiceInfo.portalContactsCalculateDto.pastDealAvgUnitPrice : "NA";
		                child.compAvg = $scope.calculateServiceInfo.portalContactsCalculateDto.compDealAvgUnitPrice!=null ? $scope.calculateServiceInfo.portalContactsCalculateDto.compDealAvgUnitPrice : "NA";
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
				    case '1.1'://contact
				    	$scope.past=$scope.calculateServiceInfo.totalContactsCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				        break;
				    case '1.1.1'://voice
				    	$scope.past=$scope.calculateServiceInfo.voiceContactsCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				        break;
				    case '1.1.2'://MAIL
				    	$scope.past=$scope.calculateServiceInfo.mailContactsCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				    	break;
				    case '1.1.3'://Chat
				    	$scope.past=$scope.calculateServiceInfo.chatContactsCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				    	break;
				    case '1.1.4'://portal
				    	$scope.past=$scope.calculateServiceInfo.portalContactsCalculateDto.pastDealYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.past);
				    	break;
				    	}
			        break;
			    case 'benchLow':// benchLow
			    	switch (child.id) {
				    case '1.1'://contact
				    	$scope.benchLow=$scope.calculateServiceInfo.totalContactsCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
				        break;
				    case '1.1.1'://voice
				    	$scope.benchLow=$scope.calculateServiceInfo.voiceContactsCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
				        break;
				    case '1.1.2'://MAIL
				    	$scope.benchLow=$scope.calculateServiceInfo.mailContactsCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
				    	break;
				    case '1.1.3'://Chat
				    	$scope.benchLow=$scope.calculateServiceInfo.chatContactsCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
				    	break;
				    case '1.1.4'://portal
				    	$scope.benchLow=$scope.calculateServiceInfo.portalContactsCalculateDto.benchmarkLowYearlyCalcDtoList;
				    	$scope.setUnitPriceForLevels(child,$scope.benchLow);
				    	break;
				    	}
			        break;
			    case 'benchTarget':// benchTarget
			    	switch (child.id) {
				    case '1.1'://contact
				    	$scope.benchTarget=$scope.calculateServiceInfo.totalContactsCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				        break;
				    case '1.1.1'://voice
				    	$scope.benchTarget=$scope.calculateServiceInfo.voiceContactsCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				        break;
				    case '1.1.2'://MAIL
				    	$scope.benchTarget=$scope.calculateServiceInfo.mailContactsCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				    	break;
				    case '1.1.3'://Chat
				    	$scope.benchTarget=$scope.calculateServiceInfo.chatContactsCalculateDto.benchmarkTargetYearlyCalcDtoList;
				    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
				    	break;
				    case '1.1.4'://portal
				    	$scope.benchTarget=$scope.calculateServiceInfo.portalContactsCalculateDto.benchmarkTargetYearlyCalcDtoList;
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
			    case '1.1'://contact
			    	for(var i=0;i<untiPrices.length;i++)
			    		{
			    		$scope.unitPriceDto[i].totalContactsUnitPrice=untiPrices[i].unitPrice;
			    		$scope.unitPriceDto[i].totalContactsRevenue=untiPrices[i].revenue;
			    		}
			        break;
			    case '1.1.1'://voice
			    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].voiceContactsUnitPrice=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].voiceContactsRevenue=untiPrices[i].revenue;
		    		$scope.unitPriceDto[i].totalContactsRevenue=$scope.unitPriceDto[i].voiceContactsRevenue+$scope.unitPriceDto[i].mailContactsRevenue+$scope.unitPriceDto[i].chatContactsRevenue+$scope.unitPriceDto[i].portalContactsRevenue;
		    		$scope.unitPriceDto[i].totalContactsUnitPrice=parseFloat($scope.unitPriceDto[i].totalContactsRevenue/$scope.serviceDeskInfo.contact.children[0].distributedVolume[i].volume).toFixed(2);
		    		}
			        break;
			    case '1.1.2'://MAIL
			    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].mailContactsUnitPrice=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].mailContactsRevenue=untiPrices[i].revenue;
		    		$scope.unitPriceDto[i].totalContactsRevenue=$scope.unitPriceDto[i].voiceContactsRevenue+$scope.unitPriceDto[i].mailContactsRevenue+$scope.unitPriceDto[i].chatContactsRevenue+$scope.unitPriceDto[i].portalContactsRevenue;
		    		$scope.unitPriceDto[i].totalContactsUnitPrice=parseFloat($scope.unitPriceDto[i].totalContactsRevenue/$scope.serviceDeskInfo.contact.children[0].distributedVolume[i].volume).toFixed(2);
		    		}
			    	break;
			    case '1.1.3'://Chat
			    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].chatContactsUnitPrice=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].chatContactsRevenue=untiPrices[i].revenue;
		    		$scope.unitPriceDto[i].totalContactsRevenue=$scope.unitPriceDto[i].voiceContactsRevenue+$scope.unitPriceDto[i].mailContactsRevenue+$scope.unitPriceDto[i].chatContactsRevenue+$scope.unitPriceDto[i].portalContactsRevenue;
		    		$scope.unitPriceDto[i].totalContactsUnitPrice=parseFloat($scope.unitPriceDto[i].totalContactsRevenue/$scope.serviceDeskInfo.contact.children[0].distributedVolume[i].volume).toFixed(2);
		    		}
			    	break;
			    case '1.1.4':// portal
			    	for(var i=0;i<untiPrices.length;i++)
		    		{
		    		$scope.unitPriceDto[i].portalContactsUnitPrice=untiPrices[i].unitPrice;
		    		$scope.unitPriceDto[i].portalContactsRevenue=untiPrices[i].revenue;
		    		$scope.unitPriceDto[i].totalContactsRevenue=$scope.unitPriceDto[i].voiceContactsRevenue+$scope.unitPriceDto[i].mailContactsRevenue+$scope.unitPriceDto[i].chatContactsRevenue+$scope.unitPriceDto[i].portalContactsRevenue;
		    		$scope.unitPriceDto[i].totalContactsUnitPrice=parseFloat($scope.unitPriceDto[i].totalContactsRevenue/$scope.serviceDeskInfo.contact.children[0].distributedVolume[i].volume).toFixed(2);
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
			  $scope.unitPriceDto[i].totalContactsRevenue=$scope.unitPriceDto[i].totalContactsRevenue*12;
			  }
			var url= endpointsurls.SERVICE_REVENUE+$scope.serviceId;
			customInterceptor.putrequest(url,$scope.unitPriceDto).then(function successCallback(response) {
				 var urlresult = endpointsurls.SAVE_SERVICE_INFO+ $stateParams.dealId;
			        customInterceptor.getrequest(urlresult).then(function successCallback(response) {
			        	$scope.existingServiceDeskInfo = response.data;
			        	$scope.tabReset(2);
			       		if($scope.existingServiceDeskInfo != null && $scope.existingServiceDeskInfo.serviceDeskId !=null){
			       			getExistingServiceDeskResultInfo();
			       		}
			        }, function errorCallback(response) {
			        	//$scope.id=undefined;
			        });
	        	$scope.viewBy = {type:'unit'};
			}, function errorCallback(response) {
				console.log(response.statusText);
			});

		}

		// Modal for find deal section
		var dialogOptions = {
		    controller: 'DealInformationCtrl',
		    templateUrl: '/modals/dealInformation/dealInformation.html'
		  };
		  $scope.getNearestDeal = function(child,key){
			  var infoModel={id:child.id,dealType:key,url:endpointsurls.SERVICE_NEAREST_DEAL};
		    $dialog.dialog(angular.extend(dialogOptions, {resolve: {infoModel: angular.copy(infoModel)}}))
		      .open()
		      .then(function(result) {
		        if(result) {
		          //angular.copy(result, infoModel);
		        }
		        infoModel = undefined;
		    });
		  };

		  //---------------------- Result Tab -----------------------//

		// get existing yearly data when initialize
	    function getExistingServiceDeskResultInfo() {
	    	if($scope.existingServiceDeskInfo.serviceDeskYearlyDataInfoDtoList != null){
	    		for (var y = 0; y < $scope.existingServiceDeskInfo.serviceDeskYearlyDataInfoDtoList.length; y++){
	        		var yearlyDto = $scope.existingServiceDeskInfo.serviceDeskYearlyDataInfoDtoList[y];
	        		getExistingServerPricingLevelWiseInfo($scope.serviceDeskInfo.contact.children[0],yearlyDto,y);

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

		// Back button clicked in Result tab
		$scope.backToCal=function()
		{
			$scope.tabReset(1);
		}
});

