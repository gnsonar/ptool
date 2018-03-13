priceToolDirectives.directive("sideheadersubmission", function() {
	  return {
	    restrict: 'A',
	    templateUrl: '/directives/submission/sideHeader/sideheadersubmission.html',
	    scope: true,
	    transclude : false,
	    controller: 'SideHeaderSubCtrl'
	  };
});