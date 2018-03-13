priceToolcontrollers.controller('GenericSubmissionCtrl', function($scope,$http,DealService,customInterceptor, endpointsurls,$window,$dialog,$stateParams,$confirm,$alert,$state) {

	$scope.role = $window.sessionStorage.getItem('role');
	$scope.userId=$window.sessionStorage.getItem('userId');
	if($scope.role!='Admin' && $scope.role!='Finance' )
	{
		$state.go("login")
	}

	$scope.success = false;
	$scope.error = false;
	$scope.numberArr = [];
	$scope.interpretation = {};
    $scope.dealInfo={};
    $scope.dealDetails = {};
    $scope.genericDealInfoDto = [];
    $scope.dealDetails.transitionFees=20;
    $scope.dealDetails.serviceGovernance=8;
    $scope.dealInfoDto1=[];
    $scope.dealInfoDto={
            "userId": $scope.dealInfo.userId,
            "dealName": "",
			"dealPhase":"",
			"dealTerm":"",
			"startDate": "",
			"offshoreAllowed": "",
			"includeHardware": "",
			"dealStatus": "",
			"dealType":"Past Deal",
			"clientName":"",
			"clientIndustry":"",
			"currency":"",
			"country":"",
	};

    $scope.$watch('dealDetails.startdate', function (value) {
    	if($stateParams.dealId==0){
    		if(new Date($scope.dealDetails.startdate)<new Date(moment().format("YYYY-MM-DD")))
    		$scope.dealDetails.startdate=undefined;
		}else
			{
    			if(new Date($scope.dealDetails.startdate)<new Date($scope.startdate))
	    		$scope.dealDetails.startdate=undefined;
			}
      })

    $scope.getNumber = function(dealterm) {
    	$scope.numberArr = [];
    	var dealTermInYears = $scope.dealDetails.dealterm/12;
    	$scope.dealInfo.dealTermInYears = dealTermInYears;
    	//DealService.setDealData($scope.dealInfo);
	    for(var i=0;i<dealTermInYears;i++)
	    {
	    	$scope.numberArr.push(i)
	    }
    }


    $scope.onChangeCountryName=function(countryName){
    	for(var i=0;i<$scope.genericDealInfoDto.countryCurrencyInfoDtoList.length;i++){
    		if($scope.genericDealInfoDto.countryCurrencyInfoDtoList[i].countryName==countryName){
    			 $scope.dealDetails.currencySelected=$scope.genericDealInfoDto.countryCurrencyInfoDtoList[i].currencyCode;
    		}
    		if(countryName==undefined)
    			 $scope.dealDetails.currencySelected=undefined;
    	}
    }


    $scope.$watch('dealDetails.dealterm', function(value) {
    	if(value > 120){
    		$scope.dealDetails.dealterm = "";
    	}
    	else{
    		$scope.getNumber($scope.dealDetails.dealterm);
    	}
    });

    $scope.saveDealDetails=function(){
    	setYearlyInfo();
	    $scope.dealInfoDto={
	     "dealId":($stateParams.dealId==0)?null:$stateParams.dealId,
	     "userId":$window.sessionStorage.getItem('userId'),
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
    	"thirdPartyFinance":$scope.dealDetails.thirdPartyFinance,
    	"openBook":$scope.dealDetails.openBook,
    	"crossBorderTax":$scope.dealDetails.crossBorderTax,
    	"nonStandardVariablePricing":$scope.dealDetails.nonStandardVariablePricing,
    	"salesRepresentative":$scope.dealDetails.salesRepresentative,
    	"bidManager":$scope.dealDetails.bidManager,
    	"financialEngineer":$scope.dealDetails.financialEngineer,
    	"leadSolutionArch":$scope.dealDetails.leadSolutionArch,
    	"assessmentIndicator": ($stateParams.dealId==0)?"0,0,0,0,0,0,0,0":$scope.assessmentIndicator,
		"submissionIndicator": ($stateParams.dealId==0)?"1,0,0,0,0,0,0,0":$scope.submissionIndicator.join(',')

    	}

	    console.log($scope.dealInfo.dealYearlyDataInfoDtos);

	    var url= endpointsurls.GENERIC_SUBMISSION_SAVE_URL;
    	customInterceptor.postrequest(url, $scope.dealInfoDto).then(function successCallback(response) {
	            $scope.dealInfoDto = response.data;
	            $state.go('home.submission.genericSubmission.detail',{dealId:response.data.dealId});
	            $scope.dealInfo.dealId = $scope.dealInfoDto.dealId;
	            var extnIsave= DealService.getter() || isSave;
	            extnIsave.genericS=true;
	       	    DealService.setter(extnIsave);
	            $alert({title:'Success',text: 'Deal information saved successfully.'})
	        }, function errorCallback(response) {
	            console.log(response.statusText);
	            $alert({title:'Error',text: 'Failed to save the deal information.'})
	        });
    };

    $scope.init=function(){
    	var url= endpointsurls.GENERIC_SUBMISSION_DEAL_INFO_URL;
    	customInterceptor.getrequest(url).then(function successCallback(response) {
            $scope.genericDealInfoDto = response.data;
            $scope.dealDetails.serviceGovernance=$scope.genericDealInfoDto.dealCostParametersDtoList[1].amount;
            $scope.dealDetails.transitionFees=$scope.genericDealInfoDto.dealCostParametersDtoList[0].amount;
            $scope.dealDetails();
        }, function errorCallback(response) {
        	if(response.status=='401'){
				$state.go('login');
			}
        });

   }
    // get dealDetails
    $scope.dealDetails=function()
    {
    	if($stateParams.dealId>0){

 		   var url= endpointsurls.GENERIC_SUBMISSION_DEAL_DETAILS_URL+$stateParams.dealId;
 		   customInterceptor.getrequest(url).then(function successCallback(response) {
		            $scope.dealInfoDto = response.data;
		            $scope.dealDetails.dealname= $scope.dealInfoDto.dealName;
		            $scope.dealDetails.clientname= $scope.dealInfoDto.clientName;
		            $scope.dealDetails.dealterm= $scope.dealInfoDto.dealTerm;
		            $scope.dealDetails.clientIndustryDtoSelected= $scope.dealInfoDto.clientIndustry;
		            $scope.dealDetails.dealCompetitorInfoDtos=($scope.dealInfoDto.dealCompetitorInfoDtos.length==0)?[{name: ''}]:$scope.dealInfoDto.dealCompetitorInfoDtos;
		            $scope.dealDetails.dealPhaseSelected= $scope.dealInfoDto.dealPhase;
		            $scope.dealDetails.currencySelected= $scope.dealInfoDto.currency;
		            $scope.dealDetails.countrySelected= $scope.dealInfoDto.country;
		            $scope.dealDetails.startdate=$scope.dealInfoDto.startDate;
		            $scope.startdate=angular.copy($scope.dealInfoDto.startDate);
		            $scope.dealDetails.standardwindowInfoSelected= $scope.dealInfoDto.serviceWindowSla;
		            $scope.dealDetails.dealTypeSelected= $scope.dealInfoDto.dealType;
		            $scope.dealDetails.offshoreSelected= $scope.dealInfoDto.offshoreAllowed;
		            $scope.dealDetails.hardwareIncludedSelected= $scope.dealInfoDto.includeHardware;
		            $scope.dealDetails.migrationCost=$scope.dealInfoDto.migrationCost;
		            $scope.dealDetails.financialEngineer=$scope.dealInfoDto.financialEngineer;
		            $scope.dealDetails.leadSolutionArch=$scope.dealInfoDto.leadSolutionArch;
		            $scope.dealDetails.bidManager=$scope.dealInfoDto.bidManager;
		            $scope.dealDetails.salesRepresentative=$scope.dealInfoDto.salesRepresentative;
		            $scope.dealDetails.serviceGovernance=$scope.dealInfoDto.serviceGovernance;
		            $scope.dealDetails.transitionFees=$scope.dealInfoDto.transitionFees;
		            $scope.dealDetails.thirdPartyFinance = $scope.dealInfoDto.thirdPartyFinance;
		        	$scope.dealDetails.openBook=$scope.dealInfoDto.openBook;
		        	$scope.dealDetails.crossBorderTax =$scope.dealInfoDto.crossBorderTax;
		        	$scope.dealDetails.nonStandardVariablePricing=$scope.dealInfoDto.nonStandardVariablePricing;
		        	$scope.submissionIndicator=response.data.submissionIndicator || '1,0,0,0,0,0,0,0';
		        	$scope.assessmentIndicator=response.data.assessmentIndicator || '0,0,0,0,0,0,0,0';
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
		            getExistingDealYearlyInfo();

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

	  //Value shown when Tab key press in Volumetric defaults
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
							for(var i=index;i<$scope.numberArr.length;i++)
							{
								$scope.dealDetails.noOfDatacenters.distributedVolume[i]={volume:$scope.dealDetails.noOfDatacenters.distributedVolume[index].volume};
							}
						break;
					}
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

});


