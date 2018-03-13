priceToolcontrollers.controller('TotalsSubCtrl', function($scope,DealService,customInterceptor,endpointsurls,$stateParams,Excel,$timeout,$dialog,GraphService) {
	$scope.numberArr = [];
	$scope.subTotals = [];
	$scope.addSubTotals = [];
	$scope.serviceGovList = [];
	$scope.transitionFeeList = [];
	$scope.totalRevenueList = [];
	$scope.migrationCost=[];
	$scope.appYearlyRevenue=[];
	$scope.serviceDeskYearlyRevenue=[];
	$scope.reatailInfo=[];
	$scope.endUserYearlyRevenue=[];
	$scope.networkYearlyRevenue=[];
	$scope.hostingYearlyRevenue=[];
	$scope.storageInfo = [];
	$scope.retailInfo = [];
	
	
	//function for init
	$scope.init = function(){
		var url =  endpointsurls.TOTAL_REVENUE+$stateParams.dealId;
        customInterceptor.getrequest(url).then(function successCallback(response) {
        	$scope.yearlyRevenue=response.data;
        	$scope.dealInfoDto=response.data.dealInfoDto;
        	//$scope.yearlyRevenue['scenarioDesc']="Active";
        	$scope.migrationCostApplicable = $scope.dealInfoDto.migrationCost;
        	$scope.dealCurrency=$scope.dealInfoDto.currency;
        	$scope.serviceGovernance=$scope.dealInfoDto.serviceGovernance;
        	$scope.transitionFees=$scope.dealInfoDto.transitionFees;
        	$scope.dealTermInYears = $scope.dealInfoDto.dealTerm; 
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
        	for(var i=0; i< $scope.dealTermInYears/12; i++){
        		$scope.numberArr.push(i);
        		
        		// Hosting Yearly Revenue
        		if($scope.yearlyRevenue.hostingYearlyRevenue[i] != null){
        			$scope.hostingYearlyRevenue.push($scope.yearlyRevenue.hostingYearlyRevenue[i]);
        		}
        		else{
        			$scope.hostingYearlyRevenue.push({totalRevenue: 0});
        		}

        		// Storage Yearly Revenue
        		if($scope.yearlyRevenue.storageYearlyRevenue[i] != null){
        			$scope.storageInfo.push($scope.yearlyRevenue.storageYearlyRevenue[i]);
        		}
        		else{
        			$scope.storageInfo.push({totalRevenue: 0});
        		}

        		// End User Yearly Revenue
        		if($scope.yearlyRevenue.endUserYearlyRevenue[i] != null){
        			$scope.endUserYearlyRevenue.push($scope.yearlyRevenue.endUserYearlyRevenue[i]);
        		}
        		else{
        			$scope.endUserYearlyRevenue.push({totalRevenue: 0});
        		}

        		// Network Yearly Revenue
        		if($scope.yearlyRevenue.networkYearlyRevenue[i] != null){
        			$scope.networkYearlyRevenue.push($scope.yearlyRevenue.networkYearlyRevenue[i]);
        		}
        		else{
        			$scope.networkYearlyRevenue.push({totalRevenue: 0});
        		}

        		// Service Desk Yearly Revenue
        		if($scope.yearlyRevenue.serviceDeskYearlyRevenue[i] != null){
        			$scope.serviceDeskYearlyRevenue.push($scope.yearlyRevenue.serviceDeskYearlyRevenue[i]);
        		}
        		else{
        			$scope.serviceDeskYearlyRevenue.push({totalContactsRevenue: 0});
        		}

        		// Application Yearly Revenue
        		if($scope.yearlyRevenue.appYearlyRevenue[i] != null){
        			$scope.appYearlyRevenue.push($scope.yearlyRevenue.appYearlyRevenue[i]);
        		}
        		else{
        			$scope.appYearlyRevenue.push({totalAppsRevenue: 0});
        		}

        		// Retail Yearly Revenue
        		if($scope.yearlyRevenue.retailYearlyRevenue[i] != null){
        			$scope.retailInfo.push($scope.yearlyRevenue.retailYearlyRevenue[i]);
        		}
        		else{
        			$scope.retailInfo.push({noOfShops: 0});
        		}

        		// Retail Yearly Revenue
        		if($scope.yearlyRevenue.totalMigrationCost[i] != null){
        			$scope.migrationCost.push($scope.yearlyRevenue.totalMigrationCost[i]);
        		}
        		else{
        			$scope.migrationCost.push({cost: 0.00});
        		}
        	}

        	$scope.setRevenue();
        })

	}
        // set revenue yearwise
        $scope.setRevenue=function()
        {
			for(var i=0;i<$scope.dealTermInYears/12;i++)
		    {
				$scope.subTotals.push(parseFloat($scope.storageInfo[i].totalRevenue)+
				parseFloat($scope.appYearlyRevenue[i].totalAppsRevenue+
			    parseFloat($scope.retailInfo[i].noOfShops)+
			    parseFloat($scope.networkYearlyRevenue[i].totalRevenue)+
			    parseFloat($scope.serviceDeskYearlyRevenue[i].totalContactsRevenue)+
			    parseFloat($scope.hostingYearlyRevenue[i].totalRevenue)+
				parseFloat($scope.endUserYearlyRevenue[i].totalRevenue)));
				
				//serviceSub
				var serviceSub=($scope.subTotals[i]*$scope.serviceGovernance)/100;
				$scope.serviceGovList.push(serviceSub.toFixed(2));
				if(i==0)
					{
					var transitionFee=($scope.subTotals[i]*$scope.transitionFees)/100;
					$scope.transitionFeeList.push(transitionFee.toFixed(2));
					}else
						{
						$scope.transitionFeeList.push(0);
						}
				
				$scope.addSubTotals.push(parseFloat($scope.transitionFeeList[i])+parseFloat($scope.serviceGovList[i])+parseFloat($scope.migrationCost[i].cost));
				$scope.totalRevenueList.push($scope.addSubTotals[i]+$scope.subTotals[i]);
				}

        }

	$scope.init();

});


