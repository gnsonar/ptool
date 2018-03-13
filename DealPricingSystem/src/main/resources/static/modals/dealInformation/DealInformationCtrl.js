//Controller for modal(dialog box) service call
priceToolcontrollers.controller('DealInformationCtrl', function($scope, infoModel,dialog,customInterceptor,$window,endpointsurls,$stateParams) {

	$scope.role = $window.sessionStorage.getItem('role');
	//the dialog is injected in the specified controller
	//{dealId}?levelName=Storage&dealType=Past
	  $scope.dealName = infoModel.dealType;
	  $scope.dealNames =[];

	  // method for init the dialog controller.
	  $scope.init=function()
	  {
		  var url=infoModel.url+$stateParams.dealId+'?';
		  url+='levelName='+infoModel.id;
		    	switch (infoModel.dealType) {
			    case 'benchTarget'://Benchmark
			    	url+='&dealType='+'Benchmark';
			        break;
			    case 'benchLow'://Benchmark
			    	url+='&dealType='+'Benchmark';
			        break;
			    case 'pastAvg'://Benchmark
			    	url+='&dealType='+'Past';
			        break;
		    	}
		  $scope.nearestInVolumeDeals=[];
		  customInterceptor.getrequest(url).then(function successCallback(response) {
			  $scope.pastDeal=response.data;
			  $scope.noOfUsedDeals=response.data.noOfUsedDeals;
			  $scope.nearestInVolumeDeals=response.data.nearestInVolumeDeals;
			  $scope.cheapestDeal=response.data.cheapestDeal;
			  $scope.expensiveDeal=response.data.expensiveDeal;
			  $scope.selectedBenchMarkDeal=response.data.selectedBenchMarkDeal;
			  $scope.highBenchMarkDeal=response.data.highBenchMarkDeal;
			  $scope.lowBenchMarkDeal=response.data.lowBenchMarkDeal;
	        }, function errorCallback(response) {
	            console.log(response.statusText);
	        });

	  }

	  $scope.init();
	  $scope.benchmark=["Used Benchmark","Nearby Lower Benchmark"," Nearby Higher Benchmark"];

	  $scope.isCollapsed2 = true;
	  $scope.isCollapsed3 = true;
	  $scope.isCollapsed21 = true;
	  /*$scope.isCollapsed22= true;
	  $scope.isCollapsed23 = true;*/
	  $scope.isCollapsed31 = true;
	  $scope.isCollapsed32 = true;
	  $scope.isCollapsed41 = true;
	$scope.isCollapsed42= true;
	  $scope.isCollapsed43 = true;



	  $scope.save = function() {
	    dialog.close($scope.searchModel);
	  };

	  $scope.close = function(){
	    dialog.close(undefined);
	  };
});