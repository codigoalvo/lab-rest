angular.module('alvoApp', ['ngRoute', 'ngResource'])
	.config(function($routeProvider, $httpProvider, $locationProvider) {

		//$locationProvider.html5Mode(true);

		//$httpProvider.interceptors.push('authInterceptor');

		$routeProvider.when('/categorias', {
			templateUrl: 'partials/categorias.html',
			controller: 'CategoriasController'
		});
		
		$routeProvider.when('/login', {
			templateUrl: 'partials/login.html',
			controller: 'LoginController'
		});
		
		$routeProvider.when('/home', {
			templateUrl: 'partials/home.html'
		});
		
		$routeProvider.otherwise({redirectTo: '/home'});

	});