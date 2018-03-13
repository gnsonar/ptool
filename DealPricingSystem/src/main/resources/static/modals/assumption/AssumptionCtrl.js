//Controller for modal(dialog box) service call
priceToolcontrollers.controller('AssumptionCtrl', function($scope,customInterceptor,$window,endpointsurls,$stateParams,$rootScope,assumptionContent) {

	$scope.role = $window.sessionStorage.getItem('role');

	$scope.activeRowIndex=0;
		  $scope.init=function()
			{$scope.assumptionsTowers=assumptionContent.generic;
			  $scope.path="/modals/assumption/partial/generic.html";

			  $scope.towers=["Generic","Infrastructure & Hosting", "Storage & Backup", "End User", "Network Services", "Service Desk", "Application Service", "Retail Services"]
			}

		  /*$rootScope.activeFor = function (index) {

		  };*/
		  $scope.getData=function(index)
		  {
			  if ($scope.activeRowIndex != index) {
			      $scope.activeRowIndex = index;
			     // $scope.colorclass='';
				  }
				  else
				  {
				  $scope.activeRowIndex=undefined

				 // $scope.colorclass='colorclass';
			  }


			  switch (index) {
			    case 0://Generic
			    	$scope.path="/modals/assumption/partial/generic.html";
			    	$scope.assumptionsTowers=assumptionContent.generic;

			        break;
			    case 1://Hosting
			    	$scope.path="/modals/assumption/partial/hosting.html";
			    	$scope.assumptionsTowers=assumptionContent.hosting;
			        break;
			    case 2://Storage
			    	$scope.path="/modals/assumption/partial/storage.html";
			    	$scope.assumptionsTowers=assumptionContent.storage;
			        break;
			    case 3://EndUser
			    	$scope.path="/modals/assumption/partial/endUser.html";
			    	$scope.assumptionsTowers=assumptionContent.endUser;
			        break;
			    case 4://Network
			    	$scope.path="/modals/assumption/partial/network.html";
			    	$scope.assumptionsTowers=assumptionContent.network;
			        break;
			    case 5://ServiceDesk
			    	$scope.path="/modals/assumption/partial/serviceDesk.html";
			    	$scope.assumptionsTowers=assumptionContent.serviceDesk;
			        break;
			    case 6://Application
			    	$scope.path="/modals/assumption/partial/apps.html";
			    	$scope.assumptionsTowers=assumptionContent.application;
			        break;
			    case 7://Retail
			    	$scope.path="/modals/assumption/partial/retail.html";
			    	$scope.assumptionsTowers=assumptionContent.retail;
			        break;
		    	}
		  }
	  $scope.init();

});