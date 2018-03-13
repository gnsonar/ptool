var priceToolDirectives=	angular.module('priceToolDirectives', []);
var priceToolServices=	angular.module('priceToolServices', []);
var priceToolFilters =angular.module('priceToolFilters', []);
var	priceToolConstants=angular.module('priceToolConstants', []);
var priceToolcontrollers =angular.module('priceToolcontrollers', ['priceToolFilters', 'priceToolServices', 'priceToolDirectives', 'priceToolConstants']);