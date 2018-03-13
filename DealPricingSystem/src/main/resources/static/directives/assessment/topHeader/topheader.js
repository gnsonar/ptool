priceToolDirectives.directive("topheader", function() {
	  return {
	    restrict: 'A',
	    templateUrl: '/directives/assessment/topHeader/topheader.html',
	    scope: true,
	    transclude : false,
	    controller: 'TopHeaderCtrl'
	  };
});