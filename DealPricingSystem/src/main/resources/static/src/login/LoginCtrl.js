// this controller is responsible for login  and authentication.
priceToolcontrollers.controller('LoginCtrl', function($scope, $state, $window, endpointsurls, $http, customInterceptor) {

	// remember me
	$scope.rememberMe=false;

	// method used to init the controller
	$scope.init = function() {

		if($window.sessionStorage.getItem('rememberMe'))
		{
			$scope.username=$window.sessionStorage.getItem('userName');
			$scope.password=window.atob($window.sessionStorage.getItem('password'));
		}
		$scope.error=false;
		var url = endpointsurls.HOST_DETAILS_URL;
		customInterceptor.getrequestwithoutbaseurl(url).then(
				function successCallback(response) {
					$scope.hostname = response.data.hostname;
					$scope.port = response.data.portNumber;
					$window.sessionStorage.setItem('hostname', $scope.hostname);
					$window.sessionStorage.setItem('port', $scope.port);
				}, function errorCallback(response) {
				});
	}
	 // initial method will called.
	 $scope.init();

	//function for login and authenticate.
	$scope.login = function() {
		$scope.userInfoDto= {
			userName: $scope.username,
	        password: $scope.password
		};
		var url = endpointsurls.LOGIN_URL;
		customInterceptor.getrequestwithouttoken(url, $scope.userInfoDto).then(function successCallback(response) {
	            $scope.userInfoDto = response.data;
	            var userName=response.data.userName;
	            var authToken=response.data.token;
	            var role=response.data.role;
	            var userId=response.data.userId;
	            $window.sessionStorage.setItem('userName',userName);
	            $window.sessionStorage.setItem('authToken',authToken);
	            $window.sessionStorage.setItem('role',role);
	            $window.sessionStorage.setItem('userId',userId);
	            if($scope.rememberMe)
	    		{
	            	$window.sessionStorage.setItem('rememberMe',$scope.rememberMe);
	            	$window.sessionStorage.setItem('password',window.btoa($scope.password));
	    		} else {
	    			$window.sessionStorage.removeItem('rememberMe');
	    			$window.sessionStorage.removeItem('password');
	    		}
	            $state.go("home.details");
	        },function errorCallback(response) {

	        	$scope.error=true;
	            $state.go("login");
	        });
		 }
});
