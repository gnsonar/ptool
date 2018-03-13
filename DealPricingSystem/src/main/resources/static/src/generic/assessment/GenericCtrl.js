priceToolcontrollers.controller('GenericCtrl', function($scope,$http,DealService,customInterceptor,$rootScope, endpointsurls,$window,$state,$stateParams,$confirm,$alert) {
	$rootScope.confrom=false;

	//initialize all the variable needed for the page.

	$scope.userId=$window.sessionStorage.getItem('userId');
	$scope.success = false;
	$scope.error = false;
	$scope.numberArr = [];
    $scope.interpretation = {};
    $scope.dealInfo={};
    $scope.dealDetails = {};
    $scope.dealDetails.transitionFees='';
    $scope.dealDetails.serviceGovernance='';
    $scope.genericDealInfoDto = [];
    $scope.dealInfoDto=[];
    $scope.dealInfoDto = {
		"userId" : $scope.userId,
		"dealName" : "",
		"dealPhase" : "",
		"dealTerm" : "",
		"startDate" : "",
		"offshoreAllowed" : "",
		"includeHardware" : "",
		"dealStatus" : "",
		"dealType" : "Past Deal",
		"clientName" : "",
		"clientIndustry" : "",
		"currency" : "",
		"country" : ""
    };

    $scope.dealInfoDto.modifiedById=$window.sessionStorage.getItem('userId');
    $scope.$watch('dealDetails.startdate', function (value) {
    	if($stateParams.dealId==0)
    		{
    	if(new Date($scope.dealDetails.startdate)<new Date(moment().format("YYYY-MM-DD")))
    		$scope.dealDetails.startdate=undefined;
    		}else
    			{
    			if(new Date($scope.dealDetails.startdate)<new Date($scope.startdate))
    	    		$scope.dealDetails.startdate=undefined;
    			}
      })

// get number of year input box
    $scope.getNumber = function(dealterm) {
    	$scope.numberArr = [];
    	var dealTermInYears = $scope.dealDetails.dealterm/12;
    	$scope.dealInfo.dealTermInYears = dealTermInYears;
	    for(var i=0;i<dealTermInYears;i++)
    {
    	$scope.numberArr.push(i);
	    }
    }

// will rerender the input box after change in deal term
    $scope.$watch('dealDetails.dealterm', function(value) {
    	if(value > 120){
    		$scope.dealDetails.dealterm = "";
    	}
    	else{
    		$scope.getNumber($scope.dealDetails.dealterm);
    	}
    });

// method for saving deal details.
    $scope.saveDealDetails=function(){
    	setYearlyInfo();
	    $scope.dealInfoDto={
	            "dealId":($stateParams.dealId==0)?null:$stateParams.dealId,
	            "userId":$scope.userId,
	            "dealName": $scope.dealDetails.dealname,
		    	"dealPhase":$scope.dealDetails.dealPhaseSelected,
		    	"dealTerm":$scope.dealDetails.dealterm,
		    	"dealStatus":"Open",
		    	"startDate": $scope.dealDetails.startdate,
		    	"offshoreAllowed": $scope.dealDetails.offshoreSelected,
		    	"includeHardware": $scope.dealDetails.hardwareIncludedSelected,
		    	"dealType":$scope.dealDetails.dealTypeSelected,
		    	"clientName":$scope.dealDetails.clientname,
		    	"clientIndustry":$scope.dealDetails.clientIndustryDtoSelected,
		    	"currency":$scope.dealDetails.currencySelected,
		    	"country":$scope.dealDetails.countrySelected,
		    	"serviceWindowSla":$scope.dealDetails.standardwindowInfoSelected,
		    	"dealYearlyDataInfoDtos":$scope.dealDetails.dealYearlyDataInfoDtos,
		    	"migrationCost":$scope.dealDetails.migrationCost,
		    	'dealCompetitorInfoDtos':$scope.dealDetails.dealCompetitorInfoDtos,
		    	'modifiedById':$window.sessionStorage.getItem('userId'),
		    	'transitionFees':$scope.dealDetails.transitionFees,
		    	'serviceGovernance':$scope.dealDetails.serviceGovernance,
		    	"assessmentIndicator": ($stateParams.dealId==0)?"1,0,0,0,0,0,0,0":$scope.assessmentIndicator.join(','),
		        "submissionIndicator": ($stateParams.dealId==0)?"0,0,0,0,0,0,0,0":$scope.submissionIndicator



    	}

	    var url= endpointsurls.GENERIC_SAVE_URL;
    	customInterceptor.postrequest(url, $scope.dealInfoDto).then(function successCallback(response) {
            $scope.dealInfoDto = response.data;
            $state.go('home.assessment.generic.detail',{dealId:response.data.dealId});
            $scope.dealInfo.dealId = $scope.dealInfoDto.dealId;
            $scope.dealInfo.currencySelected = $scope.dealInfoDto.currency;
            var extnIsave= DealService.getter() || isSave;
            extnIsave.genericA=true;
       	    DealService.setter(extnIsave);
       	    $alert({title:'Success',text: 'Deal information saved successfully.'})
       }, function errorCallback(response) {
           console.log(response.statusText);
           $alert({title:'Error',text: 'Failed to save deal information.'})
       });
    };

    $scope.onChangeCountryName=function(countryName){
    	for(var i=0;i<$scope.genericDealInfoDto.countryCurrencyInfoDtoList.length;i++){
    		if($scope.genericDealInfoDto.countryCurrencyInfoDtoList[i].countryName==countryName){
    			 $scope.dealDetails.currencySelected=$scope.genericDealInfoDto.countryCurrencyInfoDtoList[i].currencyCode;
    		}
    		if(countryName==undefined)
    			 $scope.dealDetails.currencySelected=undefined;
    	}
    }

    // todo if client need the confrom box on change of data
    setTimeout(function(){
    	$scope.count=0;
    $scope.$watch('genericForm.$dirty', function(newVal) {
    	  $scope.genericForm.$dirty=false;
    	  $scope.count=$scope.count+1;
    	  if($scope.count>2)
    		  {$rootScope.confrom=true}

    });
    }, 10);
// method for init the controller
    $scope.init=function(){
    	var url= endpointsurls.GENERIC_DEAL_INFO_URL;
    	// api call to get the dropdown values
    	customInterceptor.getrequest(url).then(function successCallback(response) {
            $scope.genericDealInfoDto = response.data;
            $scope.dealDetails.serviceGovernance=$scope.genericDealInfoDto.dealCostParametersDtoList[1].amount;
            $scope.dealDetails.transitionFees=$scope.genericDealInfoDto.dealCostParametersDtoList[0].amount;
        	// if deal id is available.
     	   if($stateParams.dealId>0){
     		   $scope.getInitData();
 	        }


        }, function errorCallback(response) {
        	if(response.status=='401'){
				$state.go('login');
			}
        });


   }
    //get the data when id is exist
    $scope.getInitData=function()
    {
    	if($stateParams.dealId>0){
    	var url =  endpointsurls.GENERIC_DEAL_DETAILS_URL + $stateParams.dealId;
        customInterceptor.getrequest(url).then(function successCallback(response) {
        	$scope.submissionIndicator=response.data.submissionIndicator || '0,0,0,0,0,0,0,0';
        	$scope.assessmentIndicator=response.data.assessmentIndicator || '1,0,0,0,0,0,0,0';
        	$scope.assessmentIndicator=$scope.assessmentIndicator.split(',');
        	var isSave={'genericA':($scope.assessmentIndicator[0]==1)?true:false,
        			'hostingA':($scope.assessmentIndicator[1]==1)?true:false,
    		     	'storageA':($scope.assessmentIndicator[2]==1)?true:false,
    		     	'endUserA':($scope.assessmentIndicator[3]==1)?true:false,
    		     	'networkA':($scope.assessmentIndicator[4]==1)?true:false,
    		     	'serviceDeskA':($scope.assessmentIndicator[5]==1)?true:false,
    		     	'appsA':($scope.assessmentIndicator[6]==1)?true:false,
    		     	'retailA':($scope.assessmentIndicator[7]==1)?true:false
    		};
        	DealService.setter(isSave);
        $scope.dealInfoDto = response.data;
        $scope.dealDetails.dealname= $scope.dealInfoDto.dealName;
        $scope.dealDetails.clientname= $scope.dealInfoDto.clientName;
        $scope.dealDetails.dealterm= $scope.dealInfoDto.dealTerm;
        $scope.dealDetails.clientIndustryDtoSelected= $scope.dealInfoDto.clientIndustry;
        $scope.dealDetails.dealPhaseSelected= $scope.dealInfoDto.dealPhase;
        $scope.dealDetails.currencySelected= $scope.dealInfoDto.currency;
        $scope.dealDetails.countrySelected= $scope.dealInfoDto.country;
        $scope.dealDetails.startdate=$scope.dealInfoDto.startDate;
        $scope.startdate=angular.copy($scope.dealInfoDto.startDate);
        $scope.dealDetails.standardwindowInfoSelected= $scope.dealInfoDto.serviceWindowSla;
        $scope.dealDetails.dealTypeSelected= $scope.dealInfoDto.dealType;
        $scope.dealDetails.offshoreSelected= $scope.dealInfoDto.offshoreAllowed;
        $scope.dealDetails.hardwareIncludedSelected= $scope.dealInfoDto.includeHardware;
        $scope.dealDetails.dealCompetitorInfoDtos=($scope.dealInfoDto.dealCompetitorInfoDtos.length==0)?[{name: ''}]:$scope.dealInfoDto.dealCompetitorInfoDtos;
        $scope.dealDetails.migrationCost=$scope.dealInfoDto.migrationCost;
        $scope.dealDetails.serviceGovernance=$scope.dealInfoDto.serviceGovernance;
        $scope.dealDetails.transitionFees=$scope.dealInfoDto.transitionFees;
        getExistingDealYearlyInfo($scope.dealInfoDto.dealYearlyDataInfoDtos);

    }, function errorCallback(response) {
        console.log(response.statusText);
    });
    	}
    }

	$scope.init();

	function setYearlyInfo(){
    	var yearlyInfoList = [];
    	for (var y = 0; y < $scope.dealInfo.dealTermInYears; y++){
    		var yearlyData = {};
    		yearlyData.year = y+1;
    		yearlyData.noOfUsers = $scope.dealDetails.noOfUsers.distributedVolume[y].volume;
    		yearlyData.noOfSites = $scope.dealDetails.noOfSites.distributedVolume[y].volume;
    		yearlyData.noOfDatacenters = $scope.dealDetails.noOfDatacenters.distributedVolume[y].volume;

    		yearlyInfoList.push(yearlyData);
    	}
    	$scope.dealDetails.dealYearlyDataInfoDtos = yearlyInfoList;
    }

	function getExistingDealYearlyInfo(){
		$scope.dealDetails.noOfUsers={};
		$scope.dealDetails.noOfSites={};
		$scope.dealDetails.noOfDatacenters={};
		$scope.dealDetails.noOfUsers.distributedVolume=[];
		$scope.dealDetails.noOfSites.distributedVolume=[];
		$scope.dealDetails.noOfDatacenters.distributedVolume=[];
    	for (var y = 0; y < $scope.dealInfoDto.dealYearlyDataInfoDtos.length; y++){
    		$scope.dealDetails.noOfUsers.distributedVolume[$scope.dealDetails.noOfUsers.distributedVolume.length]={volume: $scope.dealInfoDto.dealYearlyDataInfoDtos[y].noOfUsers};
    		$scope.dealDetails.noOfSites.distributedVolume[$scope.dealDetails.noOfSites.distributedVolume.length]={volume: $scope.dealInfoDto.dealYearlyDataInfoDtos[y].noOfSites};
    		$scope.dealDetails.noOfDatacenters.distributedVolume[$scope.dealDetails.noOfDatacenters.distributedVolume.length]={volume: $scope.dealInfoDto.dealYearlyDataInfoDtos[y].noOfDatacenters};
    	}

    }


	 $scope.dealDetails.dealCompetitorInfoDtos = [{name: ''}];

	  $scope.addNewCompetitor = function() {
	   // var newItemNo = $scope.competitors.length+1;
		  $scope.dealDetails.dealCompetitorInfoDtos.push({'name':''});
	  };

	  $scope.removeCompetitor = function() {
	    var lastItem = $scope.dealDetails.dealCompetitorInfoDtos.length-1;
	    $scope.dealDetails.dealCompetitorInfoDtos.splice(lastItem);
	  };

	  $scope.checkDuplicatecompetitor = function(val,index) {
		  console.log($scope.dealDetails.dealCompetitorInfoDtos)
		  $scope.arrylenth=  _.where($scope.dealDetails.dealCompetitorInfoDtos, {name: val});
		  if($scope.arrylenth.length>1)
			  {
			  $scope.dealDetails.dealCompetitorInfoDtos[index].name='';
			  }


		  };

		  $scope.tabEvent=function($event,index,val){
			  if($event.keyCode == 13 || $event.keyCode == 9) {
					switch(val){
					case "noOfUsers":
							for(var i=index;i<$scope.numberArr.length;i++)
							{
								$scope.dealDetails.noOfUsers.distributedVolume[i]={volume:$scope.dealDetails.noOfUsers.distributedVolume[index].volume};
							}
						break;
					case "noOfSites":
							for(var i=index;i<$scope.numberArr.length;i++)
							{
								$scope.dealDetails.noOfSites.distributedVolume[i]={volume:$scope.dealDetails.noOfSites.distributedVolume[index].volume};
							}
						break;
					case "noOfDatacenters":
//						if($stateParams.dealId>0){
//							for(var i=index;i<$scope.dealDetails.noOfDatacenters.distributedVolume.length;i++)
//							{
//								$scope.dealDetails.noOfDatacenters.distributedVolume[i].volume=$scope.dealDetails.noOfDatacenters.distributedVolume[index].volume;
//							}
//						}
//						else{
							for(var i=index;i<$scope.numberArr.length;i++)
							{
								$scope.dealDetails.noOfDatacenters.distributedVolume[i]={volume:$scope.dealDetails.noOfDatacenters.distributedVolume[index].volume};
							}
//						}
						break;
					}
				}
			 }

});

