//Controller for modal(dialog box) service call
priceToolcontrollers.controller('CopyCtrl', function($scope,searchModel,scope,modelName,dialog,customInterceptor, endpointsurls) {
	//code to be pasted in copycnrl.
    $scope.init=function()
    {
    	$scope.nickname='';
    	$scope.scenarioList=scope ||[];
    	$scope.nameErr = false;
    }
    
      
      $scope.duplicate= function () {
    	  if($scope.scenarioList.length > 0){
    		  for(var i=0;i<$scope.scenarioList.length;i++)
       	   {
       	   if($scope.nickname==$scope.scenarioList[i].scenarioName)
       		   {
       		   $scope.nickname='';
       		   $scope.nameErr = true;
       		   return false;
       		   break;
       		   }else
       			   {
       			$scope.nameErr = false;
       			   return true;
       			   }
       	   }
    	  }
    	  else{
    		  return true; 
    	  } 
      }
     


	  $scope.init();
	  $scope.select = function(val) {
		  console.log(scope.dealDetails,modelName)
		  scope.dealDetails[modelName]=val;
		    dialog.close($scope.searchModel);
		  };
	  $scope.save = function() {
		  if($scope.duplicate()){
		  $scope.nameDetail = {nickname: $scope.nickname, description: $scope.description};
	    dialog.close($scope.nameDetail);
		  }
	  };

	  $scope.close = function(){
	    dialog.close(undefined);
	  };
	  
	 

});