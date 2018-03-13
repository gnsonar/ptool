priceToolcontrollers.controller('HomeCtrl', function($scope,$http,DealService,$dialog,customInterceptor,$window,$rootScope,endpointsurls,$state) {

	// init of all variables.
	$scope.role = $window.sessionStorage.getItem('role');
	$scope.userId=$window.sessionStorage.getItem('userId');
	var isSave = {'genA':false,
		     'storeA':false,
		     'serviceA':false,
		     'appsA':false,
		     'genS':false,
		     'storeS':false,
		     'serviceS':false,
		     'appsS':false,
	};
	
	$scope.maxSize = 5;
	$scope.bigTotalItems = 175;
	$scope.bigCurrentPage = 1

	//default page setting for HOME_RECENT_DEAL_URL
	,$scope.currentPageRd = 1
	,$scope.numPerPage = 5
	,$scope.maxSize = 5;
	 $scope.numPagesRd = 1;
	 $scope.numPages=5;

	//default page setting for what is new and recent deal
	 $scope.filteredLatestUpdates = []
	  ,$scope.currentPage = 1
	  ,$scope.numPerPage = 5
	  ,$scope.maxSize = 5;

	 // call to get the current page data after page changes
	 $scope.$watch('currentPageRd + numPerPage', function() {
		 $scope.getRecentDeals($scope.currentPageRd-1);
	  });


	 //get the current page data after page changes
	 $scope.getRecentDeals=function(currentPage)
	 {
		 var  url = endpointsurls.HOME_RECENT_DEAL_URL+'?&userId='+$scope.userId+'&page='+currentPage+'&size=5&sort=modificationDate,desc';
		 customInterceptor.getrequest(url).then(function successCallback(response) {
	        	$rootScope.recentDeals = response.data.content;
	        	$scope.numPagesRd=response.data.totalElements*2;
				}, function errorCallback(response) {
					if(response.toString().indexOf('Invalid URL') > 1 || response.status=='401'){
						$state.go('login');
					}
				});
	 }

	 // message for what is new will be come from here.
	 $scope.getLatestUpdates=function()
	 {
         var url = endpointsurls.HOME_GET_LATEST_UPDATE_URL;
         customInterceptor.getrequest(url).then(function successCallback(response) {
    	 $scope.latestUpdates = angular.copy(response.data);
    	// $scope.numPages=Math.ceil(response.data.length / $scope.numPerPage);
    	 $scope.bigTotalItems=response.data.length*2;
    	 $scope.filteredLatestUpdates = $scope.latestUpdates.slice(0, 5);
     }, function errorCallback(response) {
        console.log(response.statusText);
      });
	}

	 //method used to init the controller
	 $scope.init = function() {
		 DealService.setter(isSave);
		 $scope.getRecentDeals(0);
		 $scope.getLatestUpdates();
     }

	 //initial method will called.
	    $scope.init();
	    
	    // link to go generic tab.
	    $scope.linkToGenericDeal=function()
	    {
	    	$scope.dealInfo={};
			$state.go('home.assessment.generic.detail',{dealId:0});
	    }

	 // link to go generic submission  tab.
	    $scope.linkToGenericSubmissionDeal=function()
	    {
	    	$scope.dealInfo={};
			//DealService.setDealData($scope.dealInfo);
	    	$state.go('home.submission.genericSubmission.detail',{dealId:0});
	    }
	    
	    //redirect to the correspondence deal
		$scope.linkToDeal=function(recentDeal){
			$state.go('home.assessment.generic.detail',({dealId:recentDeal.dealId}));
		
	}
		

		// page wise data for what is new and recent deal
		$scope.$watch('currentPage + numPerPage', function() {
		    var begin = (($scope.currentPage - 1) * $scope.numPerPage)
		    , end = begin + $scope.numPerPage;
		   console.log($scope.latestUpdates)
		    if($scope.latestUpdates)
		    	{
		   $scope.filteredLatestUpdates = $scope.latestUpdates.slice(begin, end);
		    	}
		  });


		// modal for find deal section
		var dialogOptions = {
		    controller: 'FindDealCtrl',
		    templateUrl: '/src/home/findDeal/findDeal.html'
		  };
		  $scope.edit = function(searchModel){
		    $dialog.dialog(angular.extend(dialogOptions, {resolve: {searchModel: angular.copy(searchModel)}}))
		      .open()
		      .then(function(result) {
		        if(result) {
		          angular.copy(result, searchModel);
		        }
		        searchModel = undefined;
		    });
		  };
	});


