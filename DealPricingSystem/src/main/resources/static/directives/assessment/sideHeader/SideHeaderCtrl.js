priceToolcontrollers.controller('SideHeaderCtrl', function($scope,$rootScope,$location,DealService,$window,$stateParams,$state,$confirm,$alert) {

	// used in side header
	$scope.open = false;
	$scope.role = $window.sessionStorage.getItem('role');
	$rootScope.activeFor = function (page) {
	    return $state.current.name==page? 'active' : '';
	};

	$scope.currentSate=$state.current.name;
	$scope.dealId=$stateParams.dealId;

	$scope.isSaveSuccessful = function(val){
		var isSave={'genericA':false,
		     	'storageA':false,
		     	'networkA':false,
		     	'serviceDeskA':false,
		     	'appsA':false,
		     	'retailA':false,
		     	'hostingA':false,
		     	'endUserA':false
		};
	$scope.save=DealService.getter() || isSave;

			switch (val) {
		    case 0:
		    	return ($scope.save.genericA)?{"background":"lightgreen"}:{"background":"lightgreen1"};
		    	break;
		    case 1:
		    	return ( $scope.save.hostingA)?{"background":"lightgreen"}:{"background":"lightgreen1"};
		    	break;
		    case 2:
		    	return ($scope.save.storageA)?{"background":"lightgreen"}:{"background":"lightgreen1"};
		    	break;
		    case 3:
		    	return ($scope.save.endUserA)?{"background":"lightgreen"}:{"background":"lightgreen1"};
		    	break;
		    case 4:
		    	return ($scope.save.networkA)?{"background":"lightgreen"}:{"background":"lightgreen1"};
		    	break;
		    case 5:
		    	return ($scope.save.serviceDeskA)?{"background":"lightgreen"}:{"background":"lightgreen1"};
		    	break;
		    case 6:
		    	return ($scope.save.appsA)?{"background":"lightgreen"}:{"background":"lightgreen1"};
		    	break;
		    case 7:
		    	return ($scope.save.retailA)?{"background":"lightgreen"}:{"background":"lightgreen1"};
		    	break;
		}}

	$scope.redirect=function(val,e,event)
	{
		switch (val) {
	    case 0:
	    	if($state.current.name=='home.assessment.generic.totals')
    		{
	    		$state.go('home.assessment.generic.detail',({dealId:$stateParams.dealId}));
	    		return;
    		}
	    		if($state.current.name!=e)
	    		{
	    			$confirm({text: 'Are you want to leave the page without saving or updating ?'})
	    			.then(function() {
	    				$state.go('home.assessment.generic.detail',({dealId:$stateParams.dealId}));
	    			})}
	    			break;

	    case 1:
	    	if($state.current.name=='home.assessment.generic.totals')
    		{
	    		$state.go('home.assessment.generic.hosting',({dealId:$stateParams.dealId}));
	    		return;
    		}
		    	if($state.current.name!=e)
	    		{
	    			if($stateParams.dealId>0)
	    		{
					$confirm({text: 'Are you sure you want to leave the page?'})
					.then(function() {
	 	        	$state.go('home.assessment.generic.hosting',({dealId:$stateParams.dealId}));
	 	        });
	    				$scope.currentSate=$state.current.name;
	    		}
				else
	    		{
	    				$alert({title:'Information',text: 'Please create a generic deal to proceed'})
	    				event.stopPropagation();
	    				event.preventDefault();
	    		}}
	    		break;

	    case 2:
	    	if($state.current.name=='home.assessment.generic.totals')
    		{
	    		$state.go('home.assessment.generic.storage',({dealId:$stateParams.dealId}));
	    		return;
    		}
	    		if($state.current.name!=e)
	    		{
	    			if($stateParams.dealId>0)
	    		{
    				$confirm({text: 'Are you want to leave the page without saving or updating ?'})
    				.then(function() {
	 	        	$state.go('home.assessment.generic.storage',({dealId:$stateParams.dealId}));
	 	        });
	    				$scope.currentSate=$state.current.name;
	    		}
    			else
	    		{
	    				$alert({title:'Information',text: 'Please create a generic deal to proceed'})
	    				event.stopPropagation();
	    				event.preventDefault();
	    		}}
	    		break;

	    case 3:
	    	if($state.current.name=='home.assessment.generic.totals')
    		{
	    		$state.go('home.assessment.generic.endUser',({dealId:$stateParams.dealId}));
	    		return;
    		}
	    	if($state.current.name!=e)
    		{
    			if($stateParams.dealId>0)
    		{
    				$confirm({text: 'Are you sure you want to leave the page?'})
    				.then(function() {
    					$state.go('home.assessment.generic.endUser',({dealId:$stateParams.dealId}));
 	        });

    				$scope.currentSate=$state.current.name;
    		}
			else
    		{
    				$alert({title:'Information',text: 'Please create a generic deal to proceed'})
    				event.stopPropagation();
    				event.preventDefault();
    		}
    	}
    	break;

	    case 4:
	    	if($state.current.name=='home.assessment.generic.totals')
    		{
	    		$state.go('home.assessment.generic.network',({dealId:$stateParams.dealId}));
	    		return;
    		}
	    	if($state.current.name!=e)
    		{
    			if($stateParams.dealId>0)
    		{
    				$confirm({text: 'Are you sure you want to leave the page?'})
    				.then(function() {
    					$state.go('home.assessment.generic.network',({dealId:$stateParams.dealId}));
 	        });

    				$scope.currentSate=$state.current.name;
    		}
			else
    		{
    				$alert({title:'Information',text: 'Please create a generic deal to proceed'})
    				event.stopPropagation();
    				event.preventDefault();
    		}
    	}
    	break;


	    case 5:
	    	if($state.current.name=='home.assessment.generic.totals')
    		{
	    		$state.go('home.assessment.generic.serviceDesk',({dealId:$stateParams.dealId}));
	    		return;
    		}
		    	if($state.current.name!=e)
	    		{
	    			if($stateParams.dealId>0)
	    		{
	    				$confirm({text: 'Are you want to leave the page without saving or updating ?'})
	    				.then(function() {
	    					$state.go('home.assessment.generic.serviceDesk',({dealId:$stateParams.dealId}));
	 	        });

	    				$scope.currentSate=$state.current.name;
	    		}
				else
	    		{
	    				$alert({title:'Information',text: 'Please create a generic deal to proceed'})
	    				event.stopPropagation();
	    				event.preventDefault();
	    		}
	    	}
	    	break;

	    case 6:
	    	if($state.current.name=='home.assessment.generic.totals')
    		{
	    		$state.go('home.assessment.generic.apps',({dealId:$stateParams.dealId}));
	    		return;
    		}
	    	if($state.current.name!=e)
    		{
    			if($stateParams.dealId>0)
    		{
    				$confirm({text: 'Are you want to leave the page without saving or updating ?'})
    				.then(function() {
    					$state.go('home.assessment.generic.apps',({dealId:$stateParams.dealId}));
 	        });

    				$scope.currentSate=$state.current.name;
    		}
			else
    		{
    				$alert({title:'Information',text: 'Please create a generic deal to proceed'})
    				event.stopPropagation();
    				event.preventDefault();
    		}
    	}
    	break;

	    case 7:
	    	if($state.current.name=='home.assessment.generic.totals')
    		{
	    		$state.go('home.assessment.generic.retail',({dealId:$stateParams.dealId}));
	    		return;
    		}
	    	if($state.current.name!=e)
    		{
    			if($stateParams.dealId>0)
    		{
    				$confirm({text: 'Are you sure you want to leave the page?'})
    				.then(function() {
    					$state.go('home.assessment.generic.retail',({dealId:$stateParams.dealId}));
 	        });

    				$scope.currentSate=$state.current.name;
    		}
			else
    		{
    				$alert({title:'Information',text: 'Please create a generic deal to proceed'})
    				event.stopPropagation();
    				event.preventDefault();
    		}
    	}
    	break;


	    case 8:

	    		if($state.current.name!=e)
	    		{
	    		if($stateParams.dealId>0)
	    		{
	    			$confirm({text: 'Are you want to leave the page without saving or updating ?'})
	    			.then(function() {
	 	        	$state.go('home.assessment.generic.totals',({dealId:$stateParams.dealId}));
	 	        });

	    			$scope.currentSate=$state.current.name;
	    		}
	    		else
	    		{
	    			$alert({title:'Information',text: 'Please create a generic deal to proceed'})
	    			e.stopPropagation();
	    			e.preventDefault();
	    		}
	    	}
	    		break;

	    case 9:
		    	if($state.current.name!=e)
	    		{
	    		if($state.current.name!='home.assessment.generic.totals')
	    		{
	    			$confirm({text: 'Are you want to leave the page without saving?'})
	    			.then(function() {
	 	        	$state.go('home.submission.genericSubmission.detail',({dealId:$stateParams.dealId}));
	 	        });

	    		}
	    		else
	    		{
	    			$state.go('home.submission.genericSubmission.detail',({dealId:$stateParams.dealId}));
	    		}
	    	}
		    	break;
		}
	}

	$scope.showconfirmbox = function () {
		if ($window.confirm("Do you want to continue?"))
		$scope.result = "Yes";
		else
		$scope.result = "No";
		}

});
