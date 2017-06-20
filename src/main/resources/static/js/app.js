var app = angular.module('bezbednost', [ 'ngRoute', 'ngNotify']);

// routeProvider
app.config(function($routeProvider, $locationProvider) {

	$routeProvider.when('/index', {
		templateUrl : 'index.html',
		controller : 'indexController'
	}).when('/generate', {
		templateUrl : 'html/generate.html',
		controller : 'generateController'
	}).when('/search', {
		templateUrl: 'html/search.html',
		controller: 'generateController'
	}).when('/withdraw', {
		templateUrl: 'html/withdraw.html',
		controller: 'generateController'
	}).when('/status', {
		templateUrl: 'html/status.html',
		controller: 'generateController'
	}).when('/admin', {
		templateUrl: 'html/admin.html',
		controller: 'generateController'
	}).when('/generateCSR', {
		templateUrl : 'html/generateCRS.html',
		controller : 'generateController'
	})/*.when('/changePassword', {
		templateUrl : 'changePassword.html',
		controller : 'bidderController'
	})*/
});