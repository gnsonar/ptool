// main App to kick start the application.
var pcApp = angular.module('pcApp', [ 'ui.router','ui.bootstrap','ui.bootstrap-2.5.0', 'ngSanitize','priceToolcontrollers','720kb.datepicker','angular-confirm','angular-alert','chart.js']);
	pcApp.run(['$rootScope', '$state', '$stateParams',
			function($rootScope, $state, $stateParams) {

				//Add references to $state and $stateParams to the $rootScope
				$rootScope.$state = $state;
				$rootScope.$stateParams = $stateParams;
			}
		])// all routing and state will be maintained here.
		.config(['$stateProvider', '$urlRouterProvider','$locationProvider', '$httpProvider',
			function($stateProvider, $urlRouterProvider,$locationProvider, $httpProvider) {
			 $locationProvider.html5Mode(true); //activate HTML5 Mode
			//common Content type for put and post
	     	$httpProvider.defaults.headers.put['Content-Type'] = 'application/json';
	     	$httpProvider.defaults.headers.post['Content-Type'] = 'application/json';
			$urlRouterProvider.otherwise('/');
			$stateProvider.state("login", {
					url: "/",
					controller: 'LoginCtrl',
					templateUrl: '/src/login/login.html'
				}).state("accessRequest", {
					url: "/accessRequest",
					controller: 'AccessRequestCtrl',
					templateUrl: 'src/accessRequest/accessRequest.html'
				})
				.state("admin", {
					url: "/admin",
					controller: 'AdminCtrl',
					templateUrl: '/src/admin/admin.html'
				}).state('admin.userRequest', {
			        url: '/userRequest',
			        templateUrl: 'src/admin/userRequest.html'
			    }).state('admin.homeMessage', {
			        url: '/homeMessage',
			        templateUrl: 'src/admin/homeMessage.html'
			    }).state('admin.currencyExchange', {
			        url: '/currencyExchange',
			        controller: 'CurrencyCtrl',
			        templateUrl: 'src/admin/currencyExchange.html'
			    }).state("assumption", {
					url: "/assumption",
					controller: 'AssumptionCtrl',
					templateUrl: '/modals/assumption/assumption.html'
				})
			    .state("home", {
					url: "/home",
					templateUrl: '/src/home/home.html'
				}).state("home.details", {
					url: "/details",
					controller: 'HomeCtrl',
					templateUrl: '/src/home/homeDetails/homeDetails.html'
				}).state("home.assessment", {
					url: "/assessment",
					templateUrl:'src/home/assessment.html'
				})
				.state("home.assessment.generic", {
					url: "/generic",
					template: '<ui-view/>'
				}).state("home.assessment.generic.detail", {
					url: "/detail/:dealId",
					controller: 'GenericCtrl',
					templateUrl: '/src/generic/assessment/generic.html'
				}).state("home.assessment.hosting", {
					url: "/hosting",
					controller: 'HostingCtrl',
					templateUrl: '/src/hosting/assessment/hosting.html'
				}).state("home.assessment.generic.storage", {
					url: "/:dealId/storage",
					controller: 'StorageCtrl',
					templateUrl: '/src/storage/assessment/storage.html'
				}).state("home.assessment.generic.totals", {
					url: "/:dealId/totals",
					controller: 'TotalsCtrl',
					templateUrl: '/src/totals/assessment/totals.html'
				}).state("home.assessment.generic.serviceDesk", {
					url: "/:dealId/serviceDesk",
					controller: 'ServiceDeskCtrl',
					templateUrl: '/src/serviceDesk/assessment/serviceDesk.html'
				}).state("home.assessment.generic.apps", {
					url: "/:dealId/apps",
					controller: 'AppsCtrl',
					templateUrl: '/src/apps/assessment/apps.html'
				}).state("home.assessment.generic.network", {
					url: "/:dealId/network",
					controller: 'NetworkCtrl',
					templateUrl: '/src/network/assessment/network.html'
				}).state("home.assessment.generic.endUser", {
					url: "/:dealId/endUser",
					controller: 'EndUserCtrl',
					templateUrl: '/src/endUser/assessment/endUser.html'
				}).state("home.assessment.generic.retail", {
					url: "/:dealId/retail",
					controller: 'RetailCtrl',
					templateUrl: '/src/retail/assessment/retail.html'
				}).state("home.assessment.generic.hosting", {
					url: "/:dealId/hosting",
					controller: 'HostingCtrl',
					templateUrl: '/src/hosting/assessment/hosting.html'
				})
				.state("home.submission", {
					url: "/submission",
					templateUrl:'src/home/submission.html'
				}).state("home.submission.genericSubmission", {
					url: "/genericSubmission",
					template:  '<ui-view/>'
				}).state("home.submission.genericSubmission.detail", {
					url: "/detail/:dealId",
					controller: 'GenericSubmissionCtrl',
					templateUrl: '/src/generic/submission/genericSubmission.html'
				}).state("home.submission.genericSubmission.storageSubmission", {
					url: "/:dealId/storageSubmission",
					controller: 'StorageSubCtrl',
					templateUrl: '/src/storage/submission/storageSubmission.html'
				}).state("home.submission.genericSubmission.totalsSubmission", {
					url: "/:dealId/totalsSubmission",
					controller: 'TotalsSubCtrl',
					templateUrl: '/src/totals/submission/totalsSubmission.html'
				}).state("home.submission.genericSubmission.serviceDeskSubmission", {
					url: "/:dealId/serviceDeskSubmission",
					controller: 'ServiceDeskSubCtrl',
					templateUrl: '/src/serviceDesk/submission/serviceDeskSubmission.html'
				}).state("home.submission.genericSubmission.appsSubmission", {
					url: "/:dealId/appsSubmission",
					controller: 'AppsSubCtrl',
					templateUrl: '/src/apps/submission/appsSubmission.html'
				}).state("home.submission.genericSubmission.networkSubmission", {
					url: "/:dealId/networkSubmission",
					controller: 'NetworkSubCtrl',
					templateUrl: '/src/network/submission/networkSubmission.html'
				}).state("home.submission.genericSubmission.endUserSubmission", {
					url: "/:dealId/endUserSubmission",
					controller: 'EndUserSubCtrl',
					templateUrl: '/src/endUser/submission/endUserSubmission.html'
				}).state("home.submission.genericSubmission.retailSubmission", {
					url: "/:dealId/retailSubmission",
					controller: 'RetailSubCtrl',
					templateUrl: '/src/retail/submission/retailSubmission.html'
				}).state("home.submission.genericSubmission.hostingSubmission", {
					url: "/:dealId/hostingSubmission",
					controller: 'HostingSubCtrl',
					templateUrl: '/src/hosting/submission/hostingSubmission.html'
				});
			}
		]);



	pcApp.run(function($rootScope, $timeout, $document,$state,$alert) {

	    // Timeout timer value
	    var TimeOutTimerValue = 45*60*1000;

	    // Start a timeout
	    var TimeOut_Thread = $timeout(function(){ LogoutByTimer() } , TimeOutTimerValue);
	    var bodyElement = angular.element($document);

	    angular.forEach(['keydown', 'keyup', 'click', 'mousemove', 'DOMMouseScroll', 'mousewheel', 'mousedown', 'touchstart', 'touchmove', 'scroll', 'focus'],
	    function(EventName) {
	         bodyElement.bind(EventName, function (e) { TimeOut_Resetter(e) });
	    });

	    function LogoutByTimer(){
	    	if($state.current.name!='login' || $state.current.name!='accessRequest')
    		{
	    	$alert({title:'Session Timeout',text: 'Your session has been expired. Please login to continue.'})
	    	$state.go('login')
    		}
	    }

	    function TimeOut_Resetter(e){
	     /*   console.log(' ' + e);*/

	        /// Stop the pending timeout
	        $timeout.cancel(TimeOut_Thread);

	        /// Reset the timeout
	        TimeOut_Thread = $timeout(function(){ LogoutByTimer() } , TimeOutTimerValue);
	    }

	})