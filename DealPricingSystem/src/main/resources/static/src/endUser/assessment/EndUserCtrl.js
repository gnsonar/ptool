priceToolcontrollers
		.controller(
				'EndUserCtrl',
				function($scope, $http, HostingService, EndUserService,
						StorageService, DealService, NetworkService,
						EndUserService, customInterceptor, endpointsurls,
						$stateParams, $dialog, $confirm, $alert, $state) {
					$scope.tabs = [ {
						class : 'active',
						href : "#inputtab",
						tabname : "Input"
					}, {
						class : 'inActive',
						href : "#caltab",
						tabname : "Calculate"
					}, {
						class : 'inActive',
						href : "#resulttab",
						tabname : "Result"
					} ];
					$scope.hostingInfo = {};
					var ctrl = this;
					ctrl.active = 0;
					$scope.dealInfo = {};
					$scope.pastAvgUnitPriceList = [];
					$scope.compAvgUnitPriceList = [];
					$scope.benchmarkLowAvgUnitPriceList = [];
					$scope.benchmarkTargetAvgUnitPriceList = [];
					$scope.storageCalculateDto = [];
					$scope.checkedval = '';
					$scope.selectedSolutionId = '';
					$scope.selectedImacId = '';
					$scope.avgPriceList = [];
					$scope.hostingChecked = false;
					$scope.enduserChecked = false;;
					$scope.endUserInfoDto = {};
					$scope.dummy = [ 0 ];
					$scope.user = false;
					$scope.customSol = false;
					$scope.customImac = false;
					$scope.viewBy = {type : 'unit'};
					$scope.solList = [];
					$scope.contactRatioList = [];
					$scope.imacList = [];
					$scope.imacIntensityList = {};
					$scope.solution = {};
					$scope.dealDetails = {};
					$scope.copyEndUserVolume = [];
					$scope.copyImac = [];
					$scope.showmsg = false;
					$scope.showvalidationmsg = false;
					$scope.showErrpercentage=false;


					// function for reseting tab and select the index which is
					// selected by user.
					$scope.tabReset = function(index) {
						for (var i = 0; i < $scope.tabs.length; i++) {
							$scope.tabs[i].class = "Inactive";
							if (i == index) {
								$scope.tabs[i].class = "active";
							}
						}
					}

					// function for stop the tab propagation
					$scope.tabRest = function(e) {
						e.stopPropagation();
						e.preventDefault();
					}

					// function for toggle panel
					$scope.togglePanel = function(level) {
						if (level.open) {
							level.open = false;
						} else {
							level.open = true;
							// retainOldVolumes(level);
						}
					};

					// function for catching tab event solution
					$scope.tabEvent=function($event,index,val){
						if($event.keyCode == 13 || $event.keyCode == 9){
							switch(val) {
							case "EndUser":
								for(var i=index;i<$scope.endUserInfo.user.children[0].distributedVolume.length;i++){
									$scope.endUserInfo.user.children[0].distributedVolume[i].volume = $scope.endUserInfo.user.children[0].distributedVolume[index].volume;
								}
								break;
							case "ImacVolume":
								for(var i=index;i<$scope.endUserInfo.userImac.children[0].distributedVolume.length;i++){
									$scope.endUserInfo.userImac.children[0].distributedVolume[i].volume = $scope.endUserInfo.userImac.children[0].distributedVolume[index].volume;
								}
								break;
							}
						}
					}

					// get the drop-down values
					$scope.getEndUserDropdowns = function() {
						var url = endpointsurls.ENDUSER_DROPDOWN + $stateParams.dealId;
						customInterceptor.getrequest(url).then(
								function successCallback(response)
								{
									$scope.contactRatioList = response.data.contactRatioDtoList;
									$scope.solList = response.data.endUserSolutionsDtoList;
									$scope.imacList = response.data.imacFactorDtoList;
									$scope.genericDealInfoDto = response.data;
									$scope.dealInfoDto = response.data.dealInfoDto;
									$scope.dealYearlyDataInfoDtoList = $scope.dealInfoDto.dealYearlyDataInfoDtos;
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
						        	$scope.isSaveEndUser= DealService.getter() || isSave;
						            DealService.setter($scope.isSaveEndUser);
						            $scope.getDealDeatils();

								}, function errorCallback(response) {
									console.log(response.statusText);
									if (response.status == '401') {
										$state.go('login');
									}
								});
					}

					// get default data of generic screen
					$scope.getDealDeatils = function()
					{
						$scope.dealDetails.offshoreSelected = $scope.dealInfoDto.offshoreAllowed;
						$scope.dealDetails.hardwareIncludedSelected= $scope.dealInfoDto.includeHardware;
						$scope.dealInfo = $scope.dealInfoDto.dealTerm;
						$scope.endUserInfo = EndUserService.initEndUserDetails($scope.dealInfo);
						$scope.initCopy = EndUserService.initEndUserDetails($scope.dealInfo);
						console.log($scope.endUserInfo);
						if ($stateParams.dealId > 0) {
							var url = endpointsurls.SAVE_ENDUSER_INFO+$stateParams.dealId;
							customInterceptor.getrequest(url).then(
									function successCallback(response)
									{
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
									},
									function errorCallback(response) {
										$scope.endUserId = undefined;
									});

						}
					}, function errorCallback(response) {
						console.log(response.statusText);
					};

					// function of init End User info

					$scope.init = function() {

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
				   				$scope.onChangeDistSetting($scope.selectedSolutionId);
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
				        	}
				    		$scope.copyEndUser=angular.copy($scope.endUserInfo.user.children[0].distributedVolume);
				    		$scope.copyImac=angular.copy($scope.endUserInfo.userImac.children[0].distributedVolume);
				    	}
				    }

				    //Distribution Setting Drop-down Selected
					$scope.onChangeDistSetting = function(solId) {
						$scope.showErr=false;
						$scope.showVolumeErr=false;
						$scope.customSol = false;
						$scope.selectedSolutionId = solId;
				    	$scope.Solution = _.where($scope.solList, {solutionId: $scope.selectedSolutionId});
				    	setLevelWisePercentage($scope.endUserInfo.user);
					};

					// On change Imac Intensity
					$scope.onChangeImacintensity = function(intensity){
						$scope.customImac = false;
						$scope.selectedImacId = intensity;
						$scope.imacIntensityList = _.where($scope.imacList, {id: $scope.selectedImacId});
						if($scope.checkedval != false || $scope.checkedval != ''){
							$scope.userChecked($scope.checkedval);
						}
					}

					function setLevelWisePercentage(parent){
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
							calculateEndUserVolume($scope.endUserInfo.user.children[0]);
						}
					}

					function calculateEndUserVolume(parent){
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
						}
					}

					$scope.userChecked=function(val){
						$scope.checkedval = val
						if($scope.checkedval == true){
							if($scope.endUserInfo.user.open == true){

					        		for(var i=0;i<$scope.endUserInfo.user.children[0].distributedVolume.length;i++)
					     	   	   	{
						     		   $scope.copyEndUserVolume=angular.copy($scope.copyEndUser);
						     		   $scope.endUserInfo.user.children[0].distributedVolume[i].volume=Math.round($scope.dealYearlyDataInfoDtoList[i].noOfUsers);
					     	   	   	}
					        		if($scope.selectedSolutionId != null && $scope.selectedSolutionId != "" && $scope.selectedSolutionId != undefined){
					        		$scope.onChangeDistSetting($scope.selectedSolutionId);
					        	}
							}
							if($scope.endUserInfo.userImac.open == true){
								if($scope.selectedImacId != null && $scope.selectedImacId != "" && $scope.selectedImacId != undefined){
					        		for(var i=0;i<$scope.endUserInfo.user.children[0].distributedVolume.length;i++)
					     	   	   	{
						     		   $scope.copyImacVolume=angular.copy($scope.copyImac);
						     		   $scope.endUserInfo.userImac.children[0].distributedVolume[i].volume=Math.round($scope.dealYearlyDataInfoDtoList[i].noOfUsers * $scope.imacIntensityList[0].factorSize);
					     	   	   	}
					        	 }
							  }
				     	   }
				        else{
				             if($scope.id){
					            	 if($scope.endUserInfo.user.open == true){
					            		 $scope.endUserInfo.user.children[0].distributedVolume=$scope.copyEndUserVolume;
					            		 $scope.onChangeDistSetting($scope.selectedSolutionId);
					            	 }
					            	 if($scope.endUserInfo.userImac.open == true){
					            		 $scope.endUserInfo.userImac.children[0].distributedVolume=$scope.copyImacVolume;
					            	 }
				                 }
				             	else{
				                    for(var i=0;i<$scope.initCopy.userImac.children[0].distributedVolume.length;i++)
				                           {
				                    		 if($scope.endUserInfo.user.open == true){
				                    			 $scope.endUserInfo.user.children[0].distributedVolume[i].volume=undefined;
				                    		 }
				                    		 if($scope.endUserInfo.userImac.open == true){
				                    			 $scope.endUserInfo.userImac.children[0].distributedVolume[i].volume=undefined;
				                    		 }
				                         }
				             		}
				        	 }
					 }

					$scope.calcVolume = function(){
						if($scope.selectedSolutionId != null && $scope.selectedSolutionId != undefined &&  $scope.selectedSolutionId != ''){
							$scope.onChangeDistSetting($scope.selectedSolutionId);
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
					}

					// Save End User form data
				    $scope.saveEndUserInfo = function(){
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
				    	    	dealId : $stateParams.dealId
				    	}
				    	if($scope.endUserInfo.user.open && !$scope.endUserInfo.user.children[0].open){$confirm({text: 'Default distribution setting has been applied to child levels'})
				 	        .then(function() {
				    	var url = endpointsurls.SAVE_ENDUSER_INFO+$stateParams.dealId;
				        customInterceptor.postrequest(url,$scope.endUserInfoDto).then(function successCallback(response) {
				        	$scope.id=response.data.endUserId;
				        	$scope.goToCal();
						}, function errorCallback(response) {
							console.log(response.statusText);
							$alert({title:'Error',text: 'Failed to save data.'})
						});})}
				    	else{
							var url = endpointsurls.SAVE_ENDUSER_INFO+$stateParams.dealId;
					        customInterceptor.postrequest(url,$scope.endUserInfoDto).then(function successCallback(response) {
					        	$scope.id=response.data.endUserId;
					        	$scope.goToCal();
							}, function errorCallback(response) {
								console.log(response.statusText);
								$alert({title:'Error',text: 'Failed to save data.'})});
						};
				    }

				    // get the Indicator value
				    $scope.getIndicator=function()
				    {
				    	var levelIndicator='';
				    	levelIndicator+=$scope.endUserInfo.user.open?1:0;
				    	levelIndicator+=',';
				    	if($scope.endUserInfo.user.open){
				    	levelIndicator+=$scope.endUserInfo.user.children[0].open?1:0;
				    	}else
				    		{
				    		levelIndicator+=0;
				    		}

				    	levelIndicator+=',';
				    	if($scope.endUserInfo.user.children[0].open){
				    	levelIndicator+=$scope.endUserInfo.user.children[0].children[0].open?1:0;
				    	}else
				    		{
				    		levelIndicator+=0;
				    		}
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
				    		yearlyInfoList.push(yearlyData);
				    	}
				    	$scope.endUserInfo.endUserYearlyDataInfoDtos = yearlyInfoList;
				    }

					// --------------------------------------------------------------Calculate Tab------------------------------------------------------------


					// Recalculate
				    $scope.reCalculate=function(){
				 	// $scope.resetCalculateDetails();
				 	 var putDeatls=  {"offshoreAllowed" : $scope.dealDetails.offshoreSelected == "Yes" ? true : false,
				 			 		  "includeBreakFix" :$scope.dealDetails.breakfixSelected == "Yes" ? true : false,
				 			 		  "includeHardware" : $scope.dealDetails.hardwareIncludedSelected == "Yes" ? true : false,
				 				      "resolutionTime" :  $scope.dealDetails.resolutionTimeSelected,
				 				      };
				 	     var url = endpointsurls.ENDUSER_RECALCULATE+$scope.id;
				         customInterceptor.putrequest(url,putDeatls).then(function successCallback(response) {
				         	$scope.tabReset(1);
				         	$scope.endUserCalculateDto=response.data;
				         	extractEnduserVolumeAvg($scope.endUserInfo.user.children[0]);
							extractEnduserImacVolumeAvg($scope.endUserInfo.userImac.children[0]);
							extractEndUserChildLevelWiseAverages($scope.endUserInfo.user.children[0].children);
						    $scope.resetCalculateDetails();
				         	;});
				         $scope.createUnitPriceForLevels($scope.endUserInfo.user.children[0].distributedVolume.length);
				 	}

				 // function for population calulate details.
					$scope.populatedEndUserCalculateTabDetails = function() {
						$scope.resetCalculateDetails();
						var url = endpointsurls.ENDUSER_CALCULATE_INFO+$stateParams.dealId;
						customInterceptor.getrequest(url).then(function successCallback(response) {
						$scope.endUserCalculateDto=response.data;
						$scope.unitPriceDto=[];
						extractEnduserVolumeAvg($scope.endUserInfo.user.children[0]);
						extractEnduserImacVolumeAvg($scope.endUserInfo.userImac.children[0]);
						extractEndUserChildLevelWiseAverages($scope.endUserInfo.user.children[0].children);
						$scope.createUnitPriceForLevels($scope.endUserInfo.user.children[0].distributedVolume.length);
						}, function errorCallback(response) {
							console.log(response.statusText);
						});
					}



					//createUnitPriceForLevels
					$scope.createUnitPriceForLevels=function(val)
					  {
						$scope.unitPriceDto=[];
							  for(var i=1;i<=val;i++)
							  {
							  var unitPrice={};
							   unitPrice.year=i
							   unitPrice.totalEndUserUnitPrice=0;
							   unitPrice.totalEndUserRevenue=0;

							   unitPrice.totalLaptopUnitPrice=0;
							   unitPrice.totalLaptopRevenue=0;

							   unitPrice.totalHighEndLaptopUnitPrice=0;
							   unitPrice.totalHighEndLaptopRevenue=0;

							   unitPrice.totalStandardLaptopUnitPrice=0;
							   unitPrice.totalStandardLaptopRevenue=0;

							   unitPrice.totalDesktopUnitPrice=0;
							   unitPrice.totalDesktopRevenue=0;

							   unitPrice.totalThinClientUnitPrice=0;
							   unitPrice.totalThinClientLanRevenue=0;

							   unitPrice.totalMobileUnitPrice=0;
							   unitPrice.totalMobileRevenue=0;

							   unitPrice.totalImacUnitPrice=0;
							   unitPrice.totalImacRevenue=0;
							   $scope.unitPriceDto.push(unitPrice);
							  }

					  }


					// extractEnduserVolumeAvg
					function extractEnduserVolumeAvg(parent) {
						if ($scope.endUserCalculateDto.endUserDevicesCalculateDto!=null) {

							parent.benchLow = $scope.endUserCalculateDto.endUserDevicesCalculateDto.benchDealLowAvgUnitPrice !=null ? $scope.endUserCalculateDto.endUserDevicesCalculateDto.benchDealLowAvgUnitPrice
									: "NA";
							parent.benchTarget = $scope.endUserCalculateDto.endUserDevicesCalculateDto.benchDealTargetAvgUnitPrice != null ? $scope.endUserCalculateDto.endUserDevicesCalculateDto.benchDealTargetAvgUnitPrice
									: "NA";
							parent.pastAvg = $scope.endUserCalculateDto.endUserDevicesCalculateDto.pastDealAvgUnitPrice != null ? $scope.endUserCalculateDto.endUserDevicesCalculateDto.pastDealAvgUnitPrice
									: "NA";
							parent.compAvg = $scope.endUserCalculateDto.endUserDevicesCalculateDto.compDealAvgUnitPrice != null? $scope.endUserCalculateDto.endUserDevicesCalculateDto.compDealAvgUnitPrice
									: "NA";
						} else {
							parent.benchLow = 'NA';
							parent.benchTarget = 'NA';
							parent.pastAvg = 'NA';
							parent.compAvg = 'NA';

						}
					}

					// extractEnduserImacVolumeAvg
					function extractEnduserImacVolumeAvg(parent) {
						if ($scope.endUserCalculateDto.imacDevicesCalculateDto!=null) {

							parent.benchLow = $scope.endUserCalculateDto.imacDevicesCalculateDto.benchDealLowAvgUnitPrice !=null ? $scope.endUserCalculateDto.imacDevicesCalculateDto.benchDealLowAvgUnitPrice
									: "NA";
							parent.benchTarget = $scope.endUserCalculateDto.imacDevicesCalculateDto.benchDealTargetAvgUnitPrice != null ? $scope.endUserCalculateDto.imacDevicesCalculateDto.benchDealTargetAvgUnitPrice
									: "NA";
							parent.pastAvg = $scope.endUserCalculateDto.imacDevicesCalculateDto.pastDealAvgUnitPrice != null ? $scope.endUserCalculateDto.imacDevicesCalculateDto.pastDealAvgUnitPrice
									: "NA";
							parent.compAvg = $scope.endUserCalculateDto.imacDevicesCalculateDto.compDealAvgUnitPrice != null? $scope.endUserCalculateDto.imacDevicesCalculateDto.compDealAvgUnitPrice
									: "NA";
						} else {
							parent.benchLow = 'NA';
							parent.benchTarget = 'NA';
							parent.pastAvg = 'NA';
							parent.compAvg = 'NA';

						}
					}

                  //extractEndUserChildLevelWiseAverages
					function extractEndUserChildLevelWiseAverages(parent) {
						for (var k = 0; k < parent.length; k++) {
							child = parent[k];
							console.log(child);
							switch (child.id) {
							case "1.1": // Storage Volume
								break;
							case "1.1.1": // Laptops
								if ($scope.endUserCalculateDto.laptopsCalculateDto!=null) {

									child.benchLow = $scope.endUserCalculateDto.laptopsCalculateDto.benchDealLowAvgUnitPrice !=null ? $scope.endUserCalculateDto.laptopsCalculateDto.benchDealLowAvgUnitPrice
											: "NA";
									child.benchTarget = $scope.endUserCalculateDto.laptopsCalculateDto.benchDealTargetAvgUnitPrice != null ? $scope.endUserCalculateDto.laptopsCalculateDto.benchDealTargetAvgUnitPrice
											: "NA";
									child.pastAvg = $scope.endUserCalculateDto.laptopsCalculateDto.pastDealAvgUnitPrice != null ? $scope.endUserCalculateDto.laptopsCalculateDto.pastDealAvgUnitPrice
											: "NA";
									child.compAvg = $scope.endUserCalculateDto.laptopsCalculateDto.compDealAvgUnitPrice != null? $scope.endUserCalculateDto.laptopsCalculateDto.compDealAvgUnitPrice
											: "NA";
								} else {
									child.benchLow = 'NA';
									child.benchTarget = 'NA';
									child.pastAvg = 'NA';
									child.compAvg = 'NA';
								}
								break;
							case "1.1.2": // Desktops
								if ($scope.endUserCalculateDto.desktopsCalculateDto!=null) {

									child.benchLow = $scope.endUserCalculateDto.desktopsCalculateDto.benchDealLowAvgUnitPrice !=null ? $scope.endUserCalculateDto.desktopsCalculateDto.benchDealLowAvgUnitPrice
											: "NA";
									child.benchTarget = $scope.endUserCalculateDto.desktopsCalculateDto.benchDealTargetAvgUnitPrice != null ? $scope.endUserCalculateDto.desktopsCalculateDto.benchDealTargetAvgUnitPrice
											: "NA";
									child.pastAvg = $scope.endUserCalculateDto.desktopsCalculateDto.pastDealAvgUnitPrice != null ? $scope.endUserCalculateDto.desktopsCalculateDto.pastDealAvgUnitPrice
											: "NA";
									child.compAvg = $scope.endUserCalculateDto.desktopsCalculateDto.compDealAvgUnitPrice != null? $scope.endUserCalculateDto.desktopsCalculateDto.compDealAvgUnitPrice
											: "NA";
								} else {
									child.benchLow = 'NA';
									child.benchTarget = 'NA';
									child.pastAvg = 'NA';
									child.compAvg = 'NA';
								}
								break;
							case "1.1.3": // Thin Clients(VDI)
								if ($scope.endUserCalculateDto.thinClientsCalculateDto!=null) {

									child.benchLow = $scope.endUserCalculateDto.thinClientsCalculateDto.benchDealLowAvgUnitPrice !=null ? $scope.endUserCalculateDto.thinClientsCalculateDto.benchDealLowAvgUnitPrice
											: "NA";
									child.benchTarget = $scope.endUserCalculateDto.thinClientsCalculateDto.benchDealTargetAvgUnitPrice != null ? $scope.endUserCalculateDto.thinClientsCalculateDto.benchDealTargetAvgUnitPrice
											: "NA";
									child.pastAvg = $scope.endUserCalculateDto.thinClientsCalculateDto.pastDealAvgUnitPrice != null ? $scope.endUserCalculateDto.thinClientsCalculateDto.pastDealAvgUnitPrice
											: "NA";
									child.compAvg = $scope.endUserCalculateDto.thinClientsCalculateDto.compDealAvgUnitPrice != null? $scope.endUserCalculateDto.thinClientsCalculateDto.compDealAvgUnitPrice
											: "NA";
								} else {
									child.benchLow = 'NA';
									child.benchTarget = 'NA';
									child.pastAvg = 'NA';
									child.compAvg = 'NA';
								}
								break;
							case "1.1.4": // Mobile devices
								if ($scope.endUserCalculateDto.mobileDevicesCalculateDto!=null) {

									child.benchLow = $scope.endUserCalculateDto.mobileDevicesCalculateDto.benchDealLowAvgUnitPrice !=null ? $scope.endUserCalculateDto.mobileDevicesCalculateDto.benchDealLowAvgUnitPrice
											: "NA";
									child.benchTarget = $scope.endUserCalculateDto.mobileDevicesCalculateDto.benchDealTargetAvgUnitPrice != null ? $scope.endUserCalculateDto.mobileDevicesCalculateDto.benchDealTargetAvgUnitPrice
											: "NA";
									child.pastAvg = $scope.endUserCalculateDto.mobileDevicesCalculateDto.pastDealAvgUnitPrice != null ? $scope.endUserCalculateDto.mobileDevicesCalculateDto.pastDealAvgUnitPrice
											: "NA";
									child.compAvg = $scope.endUserCalculateDto.mobileDevicesCalculateDto.compDealAvgUnitPrice != null? $scope.endUserCalculateDto.mobileDevicesCalculateDto.compDealAvgUnitPrice
											: "NA";
								} else {
									child.benchLow = 'NA';
									child.benchTarget = 'NA';
									child.pastAvg = 'NA';
									child.compAvg = 'NA';
								}
								break;
							case "1.1.1.1": // High-end laptops

								if ($scope.endUserCalculateDto.highEndLaptopsCalculateDto!=null) {

									child.benchLow = $scope.endUserCalculateDto.highEndLaptopsCalculateDto.benchDealLowAvgUnitPrice !=null ? $scope.endUserCalculateDto.highEndLaptopsCalculateDto.benchDealLowAvgUnitPrice
											: "NA";
									child.benchTarget = $scope.endUserCalculateDto.highEndLaptopsCalculateDto.benchDealTargetAvgUnitPrice != null ? $scope.endUserCalculateDto.highEndLaptopsCalculateDto.benchDealTargetAvgUnitPrice
											: "NA";
									child.pastAvg = $scope.endUserCalculateDto.highEndLaptopsCalculateDto.pastDealAvgUnitPrice != null ? $scope.endUserCalculateDto.highEndLaptopsCalculateDto.pastDealAvgUnitPrice
											: "NA";
									child.compAvg = $scope.endUserCalculateDto.highEndLaptopsCalculateDto.compDealAvgUnitPrice != null? $scope.endUserCalculateDto.highEndLaptopsCalculateDto.compDealAvgUnitPrice
											: "NA";
								} else {
									child.benchLow = 'NA';
									child.benchTarget = 'NA';
									child.pastAvg = 'NA';
									child.compAvg = 'NA';
								}
								break;
							case "1.1.1.2": // Standard laptops
								if ($scope.endUserCalculateDto.standardLaptopsCalculateDto!=null) {

									child.benchLow = $scope.endUserCalculateDto.standardLaptopsCalculateDto.benchDealLowAvgUnitPrice !=null ? $scope.endUserCalculateDto.standardLaptopsCalculateDto.benchDealLowAvgUnitPrice
											: "NA";
									child.benchTarget = $scope.endUserCalculateDto.standardLaptopsCalculateDto.benchDealTargetAvgUnitPrice != null ? $scope.endUserCalculateDto.standardLaptopsCalculateDto.benchDealTargetAvgUnitPrice
											: "NA";
									child.pastAvg = $scope.endUserCalculateDto.standardLaptopsCalculateDto.pastDealAvgUnitPrice != null ? $scope.endUserCalculateDto.standardLaptopsCalculateDto.pastDealAvgUnitPrice
											: "NA";
									child.compAvg = $scope.endUserCalculateDto.standardLaptopsCalculateDto.compDealAvgUnitPrice != null? $scope.endUserCalculateDto.standardLaptopsCalculateDto.compDealAvgUnitPrice
											: "NA";
								} else {
									child.benchLow = 'NA';
									child.benchTarget = 'NA';
									child.pastAvg = 'NA';
									child.compAvg = 'NA';
								}
								break;
							}
							 if(child.children != null){
		                            extractEndUserChildLevelWiseAverages(child.children);
		                        }
						}


					}

					// on selecting the radio button of calculate
					  $scope.createUnitPrice=function(child,key)
					  {
							switch (key) {
						    case 'pastAvg':// pastAvg
						    	switch (child.id) {
							    case '1.1'://End user Devices*
							    	$scope.past=$scope.endUserCalculateDto.endUserDevicesCalculateDto.pastDealYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.past);
							        break;
							    case '1.1.1'://Laptops
							    	console.log($scope.endUserCalculateDto.laptopsCalculateDto.pastDealYearlyCalcDtoList);
							    	$scope.past=$scope.endUserCalculateDto.laptopsCalculateDto.pastDealYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.past);
							        break;
							    case '1.1.2'://Desktops
							    	$scope.past=$scope.endUserCalculateDto.desktopsCalculateDto.pastDealYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.past);
							    	break;
							    case '1.1.3'://Thin Clients(VDI)
							    	$scope.past=$scope.endUserCalculateDto.thinClientsCalculateDto.pastDealYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.past);
							    	break;
							    case '1.1.4'://Mobile devices
							    	$scope.past=$scope.endUserCalculateDto.mobileDevicesCalculateDto.pastDealYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.past);
							    	break;
							    case '2.1'://IMAC
							    	$scope.past=$scope.endUserCalculateDto.imacDevicesCalculateDto.pastDealYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.past);
							        break;
							    case '1.1.1.1'://High-end laptops *
							    	$scope.past=$scope.endUserCalculateDto.highEndLaptopsCalculateDto.pastDealYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.past);
							    	break;
							    case '1.1.1.2'://Standard laptops
							    	$scope.past=$scope.endUserCalculateDto.standardLaptopsCalculateDto.pastDealYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.past);
							    	break;

							    	}
						        break;
						    case 'benchLow':// benchLow
						    	switch (child.id) {
						    	case '1.1':////End user Devices*
							    	$scope.benchLow=$scope.endUserCalculateDto.endUserDevicesCalculateDto.benchmarkLowYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
							        break;
							    case '1.1.1'://Laptops
							    	$scope.benchLow=$scope.endUserCalculateDto.laptopsCalculateDto.benchmarkLowYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
							        break;
							    case '1.1.2'://Desktops
							    	$scope.benchLow=$scope.endUserCalculateDto.desktopsCalculateDto.benchmarkLowYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
							    	break;
							    case '1.1.3'://Thin Clients(VDI
							    	$scope.benchLow=$scope.endUserCalculateDto.thinClientsCalculateDto.benchmarkLowYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
							    	break;
							    case '1.1.4':///Mobile devices
							    	$scope.benchLow=$scope.endUserCalculateDto.mobileDevicesCalculateDto.benchmarkLowYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
							    	break;
							    case '2.1'://IMAC
							    	$scope.benchLow=$scope.endUserCalculateDto.imacDevicesCalculateDto.benchmarkLowYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
							        break;
							    case '1.1.1.1':////High-end laptops *
							    	$scope.benchLow=$scope.endUserCalculateDto.highEndLaptopsCalculateDto.benchmarkLowYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
							    	break;
							    case '1.1.1.2'://Standard laptops
							    	$scope.benchLow=$scope.endUserCalculateDto.standardLaptopsCalculateDto.benchmarkLowYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.benchLow);
							    	break;

							    }
						    	break;
						    case 'benchTarget':// benchTarget
						    	switch (child.id) {
						    	case '1.1':////End user Devices*
							    	$scope.benchTarget=$scope.endUserCalculateDto.endUserDevicesCalculateDto.benchmarkTargetYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
							        break;
							    case '1.1.1'://Laptops
							    	$scope.benchTarget=$scope.endUserCalculateDto.laptopsCalculateDto.benchmarkTargetYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
							        break;
							    case '1.1.2'://Desktops
							    	$scope.benchTarget=$scope.endUserCalculateDto.desktopsCalculateDto.benchmarkTargetYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
							    	break;
							    case '1.1.3'://Thin Clients(VDI
							    	$scope.benchTarget=$scope.endUserCalculateDto.thinClientsCalculateDto.benchmarkTargetYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
							    	break;
							    case '1.1.4':///Mobile devices
							    	$scope.benchTarget=$scope.endUserCalculateDto.mobileDevicesCalculateDto.benchmarkTargetYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
							    	break;
							    case '2.1'://IMAC
							    	$scope.benchTarget=$scope.endUserCalculateDto.imacDevicesCalculateDto.benchmarkTargetYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
							        break;
							    case '1.1.1.1':////High-end laptops *
							    	$scope.benchTarget=$scope.endUserCalculateDto.highEndLaptopsCalculateDto.benchmarkTargetYearlyCalcDtoList;
							    	 $scope.setUnitPriceForLevels(child,$scope.benchTarget);
							    	break;
							    case '1.1.1.2'://Standard laptops
							    	$scope.benchTarget=$scope.endUserCalculateDto.standardLaptopsCalculateDto.benchmarkTargetYearlyCalcDtoList;
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


					  // function used for set the unit price year wise endUserInfo.user.children[0].children[childIndex]
					  $scope.setUnitPriceForLevels=function(child,untiPrices)
					  {
						  switch (child.id) {
						    case '1.1':////End user Devices*
						    	for(var i=0;i<untiPrices.length;i++)
						    		{
						    		$scope.unitPriceDto[i].totalEndUserUnitPrice=untiPrices[i].unitPrice;
						    		$scope.unitPriceDto[i].totalEndUserRevenue=untiPrices[i].revenue*12;
						    		}
						        break;
						    case '1.1.1'://Laptops
						    	for(var i=0;i<untiPrices.length;i++)
					    		{
					    		$scope.unitPriceDto[i].totalLaptopUnitPrice=untiPrices[i].unitPrice;
					    		$scope.unitPriceDto[i].totalLaptopRevenue=untiPrices[i].revenue;
					    		$scope.unitPriceDto[i].totalEndUserRevenue=$scope.unitPriceDto[i].totalDesktopRevenue+$scope.unitPriceDto[i].totalThinClientLanRevenue+$scope.unitPriceDto[i].totalLaptopRevenue;
					    		$scope.unitPriceDto[i].totalEndUserUnitPrice=parseFloat($scope.unitPriceDto[i].totalEndUserRevenue/$scope.endUserInfo.user.children[0].distributedVolume[i].volume).toFixed(2);
					    		var Mobile=parseFloat($scope.unitPriceDto[i].totalMobileRevenue/$scope.endUserInfo.user.children[0].children[3].distributedVolume[i].volume);
					    		$scope.unitPriceDto[i].totalEndUserUnitPrice=parseFloat($scope.unitPriceDto[i].totalEndUserUnitPrice+Mobile).toFixed(2);
					    		$scope.unitPriceDto[i].totalEndUserRevenue=($scope.unitPriceDto[i].totalEndUserRevenue+$scope.unitPriceDto[i].totalMobileRevenue)*12;
					    		}
						        break;
						    case '1.1.2'://Desktops
						    	for(var i=0;i<untiPrices.length;i++)
					    		{
					    		$scope.unitPriceDto[i].totalDesktopUnitPrice=untiPrices[i].unitPrice;
					    		$scope.unitPriceDto[i].totalDesktopRevenue=untiPrices[i].revenue;
					    		$scope.unitPriceDto[i].totalEndUserRevenue=$scope.unitPriceDto[i].totalDesktopRevenue+$scope.unitPriceDto[i].totalThinClientLanRevenue+$scope.unitPriceDto[i].totalLaptopRevenue;
					    		$scope.unitPriceDto[i].totalEndUserUnitPrice=parseFloat($scope.unitPriceDto[i].totalEndUserRevenue/$scope.endUserInfo.user.children[0].distributedVolume[i].volume).toFixed(2);
					    		var Mobile=parseFloat($scope.unitPriceDto[i].totalMobileRevenue/$scope.endUserInfo.user.children[0].children[3].distributedVolume[i].volume);
					    		$scope.unitPriceDto[i].totalEndUserUnitPrice=parseFloat($scope.unitPriceDto[i].totalEndUserUnitPrice+Mobile).toFixed(2);
					    		$scope.unitPriceDto[i].totalEndUserRevenue=($scope.unitPriceDto[i].totalEndUserRevenue+$scope.unitPriceDto[i].totalMobileRevenue)*12;
					    		}
						    	break;
						    case '1.1.3'://Thin Clients(VDI
						    	for(var i=0;i<untiPrices.length;i++)
					    		{
					    		$scope.unitPriceDto[i].totalThinClientUnitPrice=untiPrices[i].unitPrice;
					    		$scope.unitPriceDto[i].totalThinClientLanRevenue=untiPrices[i].revenue;
					    		$scope.unitPriceDto[i].totalEndUserRevenue=$scope.unitPriceDto[i].totalDesktopRevenue+$scope.unitPriceDto[i].totalThinClientLanRevenue+$scope.unitPriceDto[i].totalLaptopRevenue;
					    		$scope.unitPriceDto[i].totalEndUserUnitPrice=parseFloat($scope.unitPriceDto[i].totalEndUserRevenue/$scope.endUserInfo.user.children[0].distributedVolume[i].volume).toFixed(2);
					    		var Mobile=parseFloat($scope.unitPriceDto[i].totalMobileRevenue/$scope.endUserInfo.user.children[0].children[3].distributedVolume[i].volume);
					    		$scope.unitPriceDto[i].totalEndUserUnitPrice=parseFloat($scope.unitPriceDto[i].totalEndUserUnitPrice+Mobile).toFixed(2);
					    		$scope.unitPriceDto[i].totalEndUserRevenue=($scope.unitPriceDto[i].totalEndUserRevenue+$scope.unitPriceDto[i].totalMobileRevenue)*12;
					    		}
						    	break;
						    case '1.1.4':///Mobile devices
						    	for(var i=0;i<untiPrices.length;i++)
						    		{
						    		$scope.unitPriceDto[i].totalMobileUnitPrice=untiPrices[i].unitPrice;
						    		$scope.unitPriceDto[i].totalMobileRevenue=untiPrices[i].revenue;
						    		$scope.unitPriceDto[i].totalEndUserRevenue=$scope.unitPriceDto[i].totalDesktopRevenue+$scope.unitPriceDto[i].totalThinClientLanRevenue+$scope.unitPriceDto[i].totalLaptopRevenue;
						    		$scope.unitPriceDto[i].totalEndUserUnitPrice=parseFloat($scope.unitPriceDto[i].totalEndUserRevenue/$scope.endUserInfo.user.children[0].distributedVolume[i].volume).toFixed(2);
						    		var Mobile=parseFloat($scope.unitPriceDto[i].totalMobileRevenue/$scope.endUserInfo.user.children[0].children[3].distributedVolume[i].volume);
						    		$scope.unitPriceDto[i].totalEndUserUnitPrice=parseFloat($scope.unitPriceDto[i].totalEndUserUnitPrice+Mobile).toFixed(2);
						    		$scope.unitPriceDto[i].totalEndUserRevenue=($scope.unitPriceDto[i].totalEndUserRevenue+$scope.unitPriceDto[i].totalMobileRevenue)*12;
						    		}
						        break;

						    case '1.1.1.1'://///High-end laptops *
						    	for(var i=0;i<untiPrices.length;i++)
					    		{
					    		$scope.unitPriceDto[i].totalHighEndLaptopUnitPrice=untiPrices[i].unitPrice;
					    		$scope.unitPriceDto[i].totalHighEndLaptopRevenue=untiPrices[i].revenue;
					    		$scope.unitPriceDto[i].totalLaptopRevenue=$scope.unitPriceDto[i].totalHighEndLaptopRevenue+$scope.unitPriceDto[i].totalStandardLaptopRevenue;
					    		$scope.unitPriceDto[i].totalLaptopUnitPrice=parseFloat($scope.unitPriceDto[i].totalLaptopRevenue/$scope.endUserInfo.user.children[0].children [0].distributedVolume[i].volume).toFixed(2);
					    		$scope.unitPriceDto[i].totalEndUserRevenue=$scope.unitPriceDto[i].totalDesktopRevenue+$scope.unitPriceDto[i].totalThinClientLanRevenue+$scope.unitPriceDto[i].totalLaptopRevenue;
					    		$scope.unitPriceDto[i].totalEndUserUnitPrice=parseFloat($scope.unitPriceDto[i].totalEndUserRevenue/$scope.endUserInfo.user.children[0].distributedVolume[i].volume).toFixed(2);
					    		var Mobile=parseFloat($scope.unitPriceDto[i].totalMobileRevenue/$scope.endUserInfo.user.children[0].children[3].distributedVolume[i].volume);
					    		$scope.unitPriceDto[i].totalEndUserUnitPrice=parseFloat($scope.unitPriceDto[i].totalEndUserUnitPrice+Mobile).toFixed(2);
					    		$scope.unitPriceDto[i].totalEndUserRevenue=($scope.unitPriceDto[i].totalEndUserRevenue+$scope.unitPriceDto[i].totalMobileRevenue)*12;
					    		}
						    	break;
						    case '1.1.1.2'://Standard laptops
						    	for(var i=0;i<untiPrices.length;i++)
					    		{
					    		$scope.unitPriceDto[i].totalStandardLaptopUnitPrice=untiPrices[i].unitPrice;
					    		$scope.unitPriceDto[i].totalStandardLaptopRevenue=untiPrices[i].revenue;
					    		$scope.unitPriceDto[i].totalLaptopRevenue=$scope.unitPriceDto[i].totalHighEndLaptopRevenue+$scope.unitPriceDto[i].totalStandardLaptopRevenue;
					    		$scope.unitPriceDto[i].totalLaptopUnitPrice=parseFloat($scope.unitPriceDto[i].totalLaptopRevenue/$scope.endUserInfo.user.children[0].children [0].distributedVolume[i].volume).toFixed(2);
					    		$scope.unitPriceDto[i].totalEndUserRevenue=$scope.unitPriceDto[i].totalDesktopRevenue+$scope.unitPriceDto[i].totalThinClientLanRevenue+$scope.unitPriceDto[i].totalLaptopRevenue;
					    		$scope.unitPriceDto[i].totalEndUserUnitPrice=parseFloat($scope.unitPriceDto[i].totalEndUserRevenue/$scope.endUserInfo.user.children[0].distributedVolume[i].volume).toFixed(2);
					    		var Mobile=parseFloat($scope.unitPriceDto[i].totalMobileRevenue/$scope.endUserInfo.user.children[0].children[3].distributedVolume[i].volume);
					    		$scope.unitPriceDto[i].totalEndUserUnitPrice=parseFloat($scope.unitPriceDto[i].totalEndUserUnitPrice+Mobile).toFixed(2);
					    		$scope.unitPriceDto[i].totalEndUserRevenue=($scope.unitPriceDto[i].totalEndUserRevenue+$scope.unitPriceDto[i].totalMobileRevenue)*12;
					    		}
						    	break;
						    case '2.1':////IMAC
						    	for(var i=0;i<untiPrices.length;i++)
					    		{
					    		$scope.unitPriceDto[i].totalImacUnitPrice=untiPrices[i].unitPrice;
					    		$scope.unitPriceDto[i].totalImacRevenue=untiPrices[i].revenue*12;
					    		}
						        break;


						  }
						  console.log($scope.unitPriceDto);
					  }



					// reset radio buttons
						$scope.resetCalculateDetails=function(){
							$scope.endUserInfo.user.children[0].children[0].children[1].perNonPer='';
							$scope.endUserInfo.user.children[0].children[0].children[0].perNonPer='';
							$scope.endUserInfo.user.children[0].children[0].perNonPer='';
							$scope.endUserInfo.user.children[0].children[1].perNonPer='';
							$scope.endUserInfo.user.children[0].children[2].perNonPer='';
							$scope.endUserInfo.user.children[0].children[3].perNonPer='';
							$scope.endUserInfo.userImac.children[0].imac='';
							$scope.endUserInfo.user.children[0].parent='';
						}

						// Modal for find deal section
						var dialogOptions = {
						    controller: 'DealInformationCtrl',
						    templateUrl: '/modals/dealInformation/dealInformation.html'
						  };
						  $scope.getNearestDeal = function(child,key){
							  var infoModel={id:child.id,dealType:key,url:endpointsurls.ENDUSER_NEAREST_DEAL};
						    $dialog.dialog(angular.extend(dialogOptions, {resolve: {infoModel: angular.copy(infoModel)}}))
						      .open()
						      .then(function(result) {
						        if(result) {
						          //angular.copy(result, infoModel);
						        }
						        infoModel = undefined;
						    });
						  };


						// go to result
							$scope.goToResult=function()
							{
								var url= endpointsurls.ENDUSER_REVENUE+$scope.id;
								customInterceptor.putrequest(url,$scope.unitPriceDto).then(function successCallback(response) {
									 var urlresult = endpointsurls.SAVE_ENDUSER_INFO+ $stateParams.dealId;
								        customInterceptor.getrequest(urlresult).then(function successCallback(response) {
								        	$scope.existingEndUserResultInfo = response.data;
								        	$scope.tabReset(2);
								        	$scope.viewBy.type = 'unit';
								       		if($scope.existingEndUserResultInfo != null && $scope.id !=null){
								       			getExistingEndUserResultYearlyInfo();
								       		}
								        }, function errorCallback(response) {
								        	$scope.id=undefined;
								        });
								}, function errorCallback(response) {
									console.log(response.statusText);
								});
							}

					// backToInput
					$scope.backToInput = function() {
						$scope.tabReset(0);
					}

					// goToCal
					$scope.goToCal = function() {
						$scope.tabReset(1);
						$scope.populatedEndUserCalculateTabDetails();
					}



					//--------------------------------- Result Tab ---------------------------------------

					// map Existing End User Yearly Info
				    function getExistingEndUserResultYearlyInfo() {
				    	if($scope.existingEndUserResultInfo.endUserYearlyDataInfoDtoList != null){
				    		for (var y = 0; y < $scope.existingEndUserResultInfo.endUserYearlyDataInfoDtoList.length; y++){
				        		var yearlyDto = $scope.existingEndUserResultInfo.endUserYearlyDataInfoDtoList[y];
				        		getExistingResultServerPricingLevelWiseInfo(yearlyDto,y);
				        	}
				    	}
				    }

					// Get existing End User Result server price level wise
					function getExistingResultServerPricingLevelWiseInfo(yearlyDto,year){
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
										child = $scope.serviceDeskInfo.contact.children[0].children[k];
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
												child.distributedVolume[i].unit = parseFloat((child.distributedVolume[i].revenue=='' || child.distributedVolume[i].revenue==undefined || child.distributedVolume[i].revenue==null) ?0:children.distributedVolume[i].revenue) / ((child.distributedVolume[i].volume=='' || child.distributedVolume[i].volume==undefined || child.distributedVolume[i].volume==null) ?0:child.distributedVolume[i].volume);
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
									$scope.endUserInfo.user.children[0].distributedVolume[i].unit = ($scope.endUserInfo.user.children[0].distributedVolume[i].revenue / $scope.endUserInfo.user.children[0].distributedVolume[i].volume).toFixed(2);
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

					$scope.backToCal=function()
					{
						$scope.tabReset(1);
					}

					$scope.gotoTotals=function()
					{
						$scope.isSaveEndUser.endUserA=true;
				        DealService.setter($scope.isSaveEndUser);
				        $scope.assessmentIndicator[3]=1;
			             var urlIndicator= endpointsurls.GENERic_INDICATOR+$stateParams.dealId+'?assessmentIndicator='+$scope.assessmentIndicator.join(',')+'&submissionIndicator='+$scope.submissionIndicator;
			            customInterceptor.putrequestWithoutData(urlIndicator).then(function successCallback(response) {
			            	$state.go('home.assessment.generic.totals',({dealId:$stateParams.dealId}));
			            });

					}

				});
