angular.module('alvoApp', ['ngRoute'])
	.config(function($routeProvider, $locationProvider) {

		//$locationProvider.html5Mode(true);

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