priceToolcontrollers.controller('SideHeaderSubCtrl', function($scope,$rootScope,$location,DealService,$window,$stateParams,$state,$alert,$confirm,endpointsurls,customInterceptor) {
	// used in side header
	$scope.open = false;
	$scope.role = $window.sessionStorage.getItem('role');
	$rootScope.activeFor = function (page) {
	    return $state.current.name==page? 'active' : '';
	};

	if($scope.role!='Admin' && $scope.role!='Finance' )
	{
		$state.go("login");
	}
	$scope.currentSate=$state.current.name;
	$scope.dealId=$stateParams.dealId;

	$scope.isSaveSuccessful = function(val){
		var isSave={'genericS':false,
		     	'storageS':false,
		     	'networkS':false,
		     	'serviceDeskS':false,
		     	'appsS':false,
		     	'retailS':false,
		     	'hostingS':false,
		     	'endUserS':false
		};
		$scope.save=DealService.getter() || isSave;
		switch (val) {
	    case 0:
	    	return ($scope.save.genericS)?{"background":"lightgreen"}:{"background":"lightgreen1"};
	    	break;
	    case 1:
	    	return ($scope.save.hostingS)?{"background":"lightgreen"}:{"background":"lightgreen1"};
	    	break;
	    case 2:
	    	return ($scope.save.storageS)?{"background":"lightgreen"}:{"background":"lightgreen1"};
	    	break;
	    case 3:
	    	return ($scope.save.endUserS)?{"background":"lightgreen"}:{"background":"lightgreen1"};
	    	break;
	    case 4:
    	return ($scope.save.networkS)?{"background":"lightgreen"}:{"background":"lightgreen1"};
	    	break;
	    case 5:
	    	return ($scope.save.serviceDeskS)?{"background":"lightgreen"}:{"background":"lightgreen1"};
	    	break;
	    case 6:
	    	return ($scope.save.appsS)?{"background":"lightgreen"}:{"background":"lightgreen1"};
	    	break;
	    case 7:
	    	return ($scope.save.retailS)?{"background":"lightgreen"}:{"background":"lightgreen1"};
	    	break;
	}}



	$scope.redirect=function(val,e,event)
	{
		switch (val) {
		case 0:
			if($state.current.name=='home.submission.genericSubmission.totalsSubmission')
    		{
				$state.go('home.submission.genericSubmission.detail',({dealId:$stateParams.dealId}));
	    		return;
    		}
				if($state.current.name!=e)
	    		{
		    		$confirm({text: 'Are you want to leave the page without saving or updating ?'})
		    		.then(function() {
	    			$state.go('home.submission.genericSubmission.detail',({dealId:$stateParams.dealId}));
		    		})
	 	        }
	    		break;

		case 1:
			if($state.current.name=='home.submission.genericSubmission.totalsSubmission')
    		{
				$state.go('home.submission.genericSubmission.hostingSubmission',({dealId:$stateParams.dealId}));
	    		return;
    		}
	    	if($state.current.name!=e)
    		{
		    	if($stateParams.dealId>0)
	    		{
		    		 $confirm({text: 'Are you want to leave the page without saving or updating ?'})
		 	        .then(function() {
		 	        	$state.go('home.submission.genericSubmission.hostingSubmission',({dealId:$stateParams.dealId}));
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

    	case 2:
    		if($state.current.name=='home.submission.genericSubmission.totalsSubmission')
    		{
    			$state.go('home.submission.genericSubmission.storageSubmission',({dealId:$stateParams.dealId}));
	    		return;
    		}
		    	if($state.current.name!=e)
	    		{
			    	if($stateParams.dealId>0)
		    		{
			    		 $confirm({text: 'Are you want to leave the page without saving or updating ?'})
			 	        .then(function() {
			 	        	$state.go('home.submission.genericSubmission.storageSubmission',({dealId:$stateParams.dealId}));
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

    	case 3:
    		if($state.current.name=='home.submission.genericSubmission.totalsSubmission')
    		{
    			$state.go('home.submission.genericSubmission.endUserSubmission',({dealId:$stateParams.dealId}));
	    		return;
    		}
    		if($state.current.name!=e)
    		{
		    	if($stateParams.dealId>0)
	    		{
		    		 $confirm({text: 'Are you sure you want to leave the page?'})
		 	        .then(function() {
		 	        	$state.go('home.submission.genericSubmission.endUserSubmission',({dealId:$stateParams.dealId}));
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
	    	if($state.current.name=='home.submission.genericSubmission.totalsSubmission')
    		{
	    		$state.go('home.submission.genericSubmission.networkSubmission',({dealId:$stateParams.dealId}));
	    		return;
    		}
    		if($state.current.name!=e)
    		{
		    	if($stateParams.dealId>0)
	    		{
		    		 $confirm({text: 'Are you sure you want to leave the page?'})
		 	        .then(function() {
		 	        	$state.go('home.submission.genericSubmission.networkSubmission',({dealId:$stateParams.dealId}));
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
	    	if($state.current.name=='home.submission.genericSubmission.totalsSubmission')
    		{
	    		$state.go('home.submission.genericSubmission.serviceDeskSubmission',({dealId:$stateParams.dealId}));
	    		return;
    		}
	    		if($state.current.name!=e)
	    		{
			    	if($stateParams.dealId>0)
		    		{
			    		 $confirm({text:'Are you want to leave the page without saving or updating ?'})
			 	        .then(function() {
			 	        	$state.go('home.submission.genericSubmission.serviceDeskSubmission',({dealId:$stateParams.dealId}));
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
	    	if($state.current.name=='home.submission.genericSubmission.totalsSubmission')
    		{
	    		$state.go('home.submission.genericSubmission.appsSubmission',({dealId:$stateParams.dealId}));
	    		return;
    		}
		    	if($state.current.name!=e)
	    		{
			    	if($stateParams.dealId>0)
		    		{
			    		 $confirm({text: 'Are you want to leave the page without saving or updating ?'})
			 	        .then(function() {
			 	        	$state.go('home.submission.genericSubmission.appsSubmission',({dealId:$stateParams.dealId}));
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
	    	if($state.current.name=='home.submission.genericSubmission.totalsSubmission')
    		{
	    		$state.go('home.submission.genericSubmission.retailSubmission',({dealId:$stateParams.dealId}));
	    		return;
    		}
	    	if($state.current.name!=e)
    		{
		    	if($stateParams.dealId>0)
	    		{
		    		 $confirm({text: 'Are you sure you want to leave the page?'})
		 	        .then(function() {
		 	        	$state.go('home.submission.genericSubmission.retailSubmission',({dealId:$stateParams.dealId}));
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
		 	        	$state.go('home.submission.genericSubmission.totalsSubmission',({dealId:$stateParams.dealId}));
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
		}
	}

	$scope.submitDealFlow = function (status) {
		$confirm({text: 'Do you want to change the deal workflow?'})
	        .then(function() {
		var url = endpointsurls.SUBMISSION_DEAL_WORKFLOW_URL+$stateParams.dealId+'?status='+status;
		customInterceptor.putrequest(url).then(
				function successCallback(response)
				{
					$alert({title:'Information',text: 'Deal status change successfully'})
					$state.go('home.details');
				});});
	}


	$scope.showconfirmbox = function () {
		if ($window.confirm("Do you want to continue?"))
		$scope.result = "Yes";
		else
		$scope.result = "No";
		}
});
