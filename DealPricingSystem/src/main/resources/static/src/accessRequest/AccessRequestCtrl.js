priceToolcontrollers.controller('AccessRequestCtrl', function($scope,$location, endpointsurls, customInterceptor) {
	$scope.reasonForAccess;
	$scope.email;
	$scope.region;
	$scope.fullName;
	$scope.hostDetails = {};
	$scope.success=false;
	$scope.error=false;
	$scope.spinner=false;
	
	// Remove the error message
	$scope.removeClass=function()
	{
		$scope.success=false;
		$scope.error=false;
	}
	
	// this method is used to register a new user.
	$scope.register = function() {
		$scope.spinner=true;
		$scope.userInfoDto = {
			"reasonForAccess" : $scope.reasonForAccess,
			"region" : $scope.region,
			"emailId" : $scope.email,
			"fullName" : $scope.fullName
		};
		var url = endpointsurls.REGISTRATION_URL;
		customInterceptor.postrequestwithouttoken(url, $scope.userInfoDto)
				.then(function sccessCallback(response) {
					$scope.spinner=false;
					$scope.success=true;
					$scope.error=false;
				}, function errorCallback(response) {
					$scope.spinner=false;
					$scope.success=false;
					$scope.error=true;
				});
	};
});
