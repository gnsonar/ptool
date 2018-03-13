priceToolcontrollers
		.controller(
				'CurrencyCtrl',
				function($scope, $http, $state, $location, customInterceptor,
						endpointsurls) {

					// Active For is used to show active tab
					$scope.activeFor = function(page) {
						var currentRoute = $location.path().substring(1);
						return page === currentRoute ? 'active' : '';
					};

					// Remove the error message
					$scope.removeClass = function() {
						$scope.success = false;
						$scope.error = false;
					}

					// function for init the controler
					$scope.init = function() {
						$scope.success = false;
						$scope.error = false;
						$scope.getCurrency();
					}

					$scope.resetCurrency = function() {
						$scope.getCurrency();
					}

					// method for get currency values for admin request.
					$scope.getCurrency = function() {
						var url = endpointsurls.ADMIN_GET_CURRENCY_URL;
						customInterceptor
								.getrequest(url)
								.then(
										function successCallback(response) {
											for (var i = 0; i < response.data.length; i++) {
												var position = (i < response.data[i].fxRatesDtos.length) ? i
														: response.data[i].fxRatesDtos.length;
												response.data[i].fxRatesDtos
														.splice(
																position,
																0,
																{
																	"currencyTo" : response.data[i].currencyFrom,
																	"rate" : 1
																})
											}
											$scope.currencyDetail = response.data;
											console.log($scope.currencyDetail);
										}, function errorCallback(response) {
											if(response.toString().indexOf('Invalid URL') > 1 || response.status=='401'){
												$state.go('login');
											}
										});
					}

					// method for update currency values .
					$scope.updateCurrency = function() {
						var url = endpointsurls.ADMIN_UPDATE_CURRENCY_URL;
						customInterceptor
								.putrequest(url, $scope.currencyDetail).then(
										function successCallback(response) {
											$scope.success = true;
											$scope.error = false;
											$scope.getCurrency();
										}, function errorCallback(response) {
											$scope.success = false;
											$scope.error = true;
										});
					}
					$scope.init();

				});
