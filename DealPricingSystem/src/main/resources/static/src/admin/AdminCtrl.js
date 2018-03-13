priceToolcontrollers.controller('AdminCtrl', function($scope,$http,$state,$location,customInterceptor,endpointsurls, $window) {

	//Active For is used to show active tab
	$scope.spinner=false;
	$scope.role = $window.sessionStorage.getItem('role');
	if($scope.role!='Admin')
		{
		$state.go("login")
		}
	$scope.activeFor = function (page) {
	    var currentRoute = $location.path().substring(1);
	    return page === currentRoute ? 'active' : '';
	};

	// Remove the error message
	$scope.removeClass=function()
	{
		$scope.success=false;
		$scope.error=false;
	}

	//default values for models
	$scope.messageDto={};
	$scope.newMessage = {msg:""};
	$scope.roles=[];
	$scope.userInfoDtos=[];
	$scope.userInfoDto={};

	// function for init the controler
	$scope.init = function(){
		$scope.success=false;
    	$scope.error=false;
		if($state.current.url=='/admin'){
			$state.go('admin.userRequest');
		}
		$scope.getPendingRequest();
	}

	// method for get init values for admin request.
	$scope.getPendingRequest = function()
	{
	 var url = endpointsurls.ADMIN_ACCESS_REQUEST_URL;
	   customInterceptor.getrequest(url).then(function successCallback(response) {
  	   $scope.userInfoDtos = response.data
  	   var url = endpointsurls.ADMIN_ROLES_URL;
	       customInterceptor.getrequest(url).then(function successCallback(response) {
	    	   $scope.roles = response.data
	        }, function errorCallback(response) {
	            console.log(response.statusText);
	        });
	   }, function errorCallback(response) {
		   if(response.toString().indexOf('Invalid URL') > 1 || response.status=='401'){
				$state.go('login');
			}
  });
	}

	$scope.init();

	// this method will update the status
	$scope.updateUserRequest= function(userId,status,role){
		$scope.spinner=true;
		 $scope.userInfoDto= {
			 "userId": userId,
			 "role":role,
			 "status":status,
			 "approvedBy": $window.sessionStorage.getItem('userName')
		 }
		 var url = endpointsurls.ADMIN_UPDATE_ACCESS_REQUEST_URL;
		 customInterceptor.postrequest(url,$scope.userInfoDto).then(function successCallback(response) {
			    $scope.getPendingRequest();
	            $scope.userInfoDto = response.data;
	            $scope.spinner=false;
	        }, function errorCallback(response) {
	        	$scope.spinner=false;
	            console.log(response.statusText);
	        });
		}

	// this method will save  the new admin message
		$scope.saveAdminMessage=function(message){
		$scope.messageDto={"messageName":message};
		var url = endpointsurls.ADMIN_SAVE_MESSAGE_URL;
		customInterceptor.postrequest(url,$scope.messageDto).then(function successCallback(response) {
			$scope.newMessage.msg='';
			$scope.success=true;
			$scope.error=false;
		      $scope.userInfoDto = response.data;
		        }, function errorCallback(response) {
		        	$scope.success=false;
		        	$scope.error=true;
		        });
			}
});
