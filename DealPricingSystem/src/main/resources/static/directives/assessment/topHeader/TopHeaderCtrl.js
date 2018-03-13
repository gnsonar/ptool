priceToolcontrollers.controller('TopHeaderCtrl', function($scope, $location, DealService, $window, customInterceptor, $state, endpointsurls,$state,$confirm,$dialog) {
	
	//user details will come from sessionStorage
	$scope.open = false;
	$scope.userName = $window.sessionStorage.getItem('userName');
	$scope.role = $window.sessionStorage.getItem('role');
	$scope.userId = $window.sessionStorage.getItem('userId');

	// will active the selected tab
	$scope.activeFor = function (page) {
	    var currentRoute = $location.path().substring(1);
	    return page === currentRoute ? 'active' : '';
	};
	
	// redirect to home or admin
	$scope.redirect=function(val)
	{
		if($state.current.name!='home.details' && $state.current.name!="admin" && $state.current.name!='admin.userRequest'
			&& $state.current.name!='assumption' && $state.current.name!='home.assessment.generic.totals' 
				&& $state.current.name!='home.submission.genericSubmission.totalsSubmission')
		{
			$confirm({text: 'Are you want to leave the page without saving?'})
			.then(function() {
				$state.go(val);
			})}else{
				$state.go(val);
			}
	}
	
	$scope.gotoAss=function(val){
		$state.go(val);
	}
	
	/*// Modal for find deal section
	var dialogOptions = {
	    controller: 'AssumptionCtrl',
	    templateUrl: '/modals/assumption/assumption.html'
	  };
		var scope=$scope;
	  $scope.getAssumption = function(){
	    $dialog.dialog(angular.extend(dialogOptions, {resolve: {}}))
	      .open()
	      .then(function(result) {
	        if(result) {
	          angular.copy(result, infoModel);
	        }
	        infoModel = undefined;
	    });
	  };
	*/
	
	
	
// logout and redirect to the home page
	$scope.logout = function() {
		$state.go("login");
		var url = endpointsurls.LOGOUT_URL +'/'+$scope.userId;
		customInterceptor.getrequest(url);
		DealService.dealInfo = {};

	}

});
