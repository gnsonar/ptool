//Controller for modal(dailog box) service call
priceToolcontrollers.controller('ContactCtrl', function($scope,searchModel,scope,modelName,dialog,customInterceptor, endpointsurls) {
	$scope.init=function()
	{
		$scope.conatactNames=[];
		$scope.getContact();
	}

	$scope.getContact=function(search)
	{
		$scope.conatactName=search || searchModel;
		  var url=endpointsurls.GENERIC_SUBMISSION_CONTACT_INFORMATION_URL+$scope.conatactName;
		  customInterceptor.getrequest(url).then(function successCallback(response) {
	          $scope.conatactNames = response.data;
	      }, function errorCallback(response) {
	          console.log(response.statusText);
	      });
	}
	
	  $scope.checkDuplicatecompetitor = function(val,index) {
		  console.log($scope.dealDetails.dealCompetitorInfoDtos)
		  $scope.arrylenth=  _.where($scope.dealDetails.dealCompetitorInfoDtos, {name: val});
		  if($scope.arrylenth.length>1)
			  {
			  $scope.dealDetails.dealCompetitorInfoDtos[index].name='';
			  }
		  
		  
		  };
	

	  $scope.init();
	  $scope.select = function(val) {
		  console.log(scope.dealDetails,modelName)
		  scope.dealDetails[modelName]=val;
		    dialog.close($scope.searchModel);
		  };
	  $scope.save = function() {
	    dialog.close($scope.searchModel);
	  };

	  $scope.close = function(){
	    dialog.close(undefined);
	  };
	  
	 

});