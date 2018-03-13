//Controller for modal(dailog box) service call
priceToolcontrollers.controller('FindDealCtrl', function($scope, searchModel,dialog,customInterceptor,$window,endpointsurls,$state,DealService ) {
	//the dialog is injected in the specified controller
	  $scope.dealName = searchModel || '';
	  $scope.clientName = '';
	  $scope.all = false;
	 // var url=endpointsurls.HOME_FIND_DEAL_URL+$scope.clientName+'&dealName='+ $scope.dealName+'&allDeals='+$scope.all;
	  $scope.dealNames =[];

	  // method for ibit the dialog controller.
	  $scope.init=function()
	  {
		  if($scope.dealName!='')
			  {
		  $scope.searchDeal();
			  }
	  }

	  //search a deal from from back end.
	  $scope.searchDeal=function()
	  {
		  var url=endpointsurls.HOME_FIND_DEAL_URL+$scope.clientName+'&dealName='+ $scope.dealName+'&allDeals='+$scope.all;
		  customInterceptor.getrequest(url).then(function successCallback(response) {
			  $scope.dealNames = response.data;
		     }, function errorCallback(response) {
		        console.log(response.statusText);
		      });
	  }
	  //search a deal from ui.
	  $scope.search=function()
	  {
		  $scope.searchDeal();
	  }
	  $scope.init();
	  
	  $scope.linkToDeal=function(recentDeal){
			$state.go('home.assessment.generic.detail',({dealId:recentDeal.dealId}));
		/*$scope.dealInfo={};
		var dealTermInyears = '';
		if(recentDeal.dealTerm){
			$scope.dealInfo.dealTermInYears=recentDeal.dealTerm / 12;
		}
		$scope.dealInfo.dealId=recentDeal.dealId;
		$scope.dealInfo.currencySelected = recentDeal.currency;
		DealService.setDealData($scope.dealInfo);*/
	}
	  
	  
	  
	  //set the deal data and send to summission page.
	  $scope.linkToDeal=function(recentDeal){
		  $state.go('home.submission.genericSubmission.detail',({dealId:recentDeal.dealId}));
			/*$scope.dealInfo={};
			var dealTermInyears = '';
			if(recentDeal.dealTerm){
				$scope.dealInfo.dealTermInYears=recentDeal.dealTerm / 12;
			}
			$scope.dealInfo.dealId=recentDeal.dealId;
			$scope.dealInfo.currencySelected = recentDeal.currency;
			DealService.setDealData($scope.dealInfo);*/
			 dialog.close(undefined);
		}
	  $scope.save = function() {
	    dialog.close($scope.searchModel);
	  };

	  $scope.close = function(){
	    dialog.close(undefined);
	  };
});