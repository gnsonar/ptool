priceToolDirectives.directive("sideheader", function() {
	  return {
	    restrict: 'A',
	    templateUrl: '/directives/assessment/sideHeader/sideheader.html',
	    scope: true,
	    transclude : false,
	    controller: 'SideHeaderCtrl'
	  };
});