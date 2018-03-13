// all request  will go through this interceptor.

priceToolServices.service('customInterceptor', ['$http', '$rootScope','$window','$state', function($http, $rootScope,$window,$state) {

    this.getUrl = function(urlParams) {
     var baseUrl = 'http://' + $window.sessionStorage.getItem('hostname') +':'+ $window.sessionStorage.getItem('port') + '/DealPricingService';
       var endPoint = baseUrl + urlParams;
       var cookietoken = $window.sessionStorage.getItem('authToken');
       $http.defaults.headers.common['Authorization'] = 'Bearer ' + cookietoken;
        return endPoint;
    };

    /*
    * Function to make GET request
    * Parameter :- EndPoint URL combined with baseURL
    */
    this.getrequest = function(endpoint) {
        console.info("endpoint is " + endpoint);
        $rootScope.spinner=true;
        var endPoint = this.getUrl(endpoint);
        var promise = $http.get(endPoint);
        promise.then(function successCallback(response) {
        	$rootScope.spinner=false;

		}, function errorCallback(response) {
			$rootScope.spinner=false;

			if(endpoint.indexOf('getGenericDetailsByDealId')>0 || endpoint.indexOf('dropdowns') >0)
				{
				$state.go('login');
				}

			if(response.status=='401'){
				$state.go('login');
			}
		});
        return promise;
    };

    // Used For login & accessRequest
    this.postrequestwithouttoken = function(urlParams, data) {
    	var baseUrl = 'http://' + $window.sessionStorage.getItem('hostname') +':'+ $window.sessionStorage.getItem('port') + '/DealPricingService';
	  	var endPoint = baseUrl + urlParams;
	  	var promise = $http.post(endPoint, data);
	     return promise;
	};

	// Used For login & accessRequest
    this.getrequestwithouttoken = function(urlParams, data) {
    	var baseUrl = 'http://' + $window.sessionStorage.getItem('hostname') +':'+ $window.sessionStorage.getItem('port') + '/DealPricingService';
	  	var endPoint = baseUrl + urlParams;
	  	$http.defaults.headers.common['Authorization'] = 'Basic ' + window.btoa(data.userName+':'+data.password);
	  	var promise = $http.get(endPoint);
	     return promise;
	};

    // Used to get host details
	this.getrequestwithoutbaseurl = function(endpoint) {
      var promise = $http.get(endpoint);
      return promise;
    };

    /*
     * Function to make Post request
     * Parameter :- EndPoint URL combined with baseURL
     */

    this.postrequest = function(endpoint, data) {
        var endPoint = this.getUrl(endpoint);
        var promise = $http.post(endPoint, data);
        return promise;
    };

    this.putrequest = function(endpoint, data) {
    	$rootScope.spinner=true;
        var endPoint = this.getUrl(endpoint);
        var promise = $http.put(endPoint, data);
        promise.then(function successCallback(response) {
        	$rootScope.spinner=false;
		}, function errorCallback(response) {
			$rootScope.spinner=false;
		});
        return promise;
    };
    
    this.putrequestWithoutData = function(endpoint) {
    	$rootScope.spinner=true;
        var endPoint = this.getUrl(endpoint);
        var promise = $http.put(endPoint);
        promise.then(function successCallback(response) {
        	$rootScope.spinner=false;
		}, function errorCallback(response) {
			$rootScope.spinner=false;
		});
        return promise;
    };
    this.deleterequest = function(endpoint, data) {
        var endPoint = this.getUrl(endpoint);
        var promise = $http({
            method: 'DELETE',
            url: endPoint
        })
        return promise;
    };
}]);
