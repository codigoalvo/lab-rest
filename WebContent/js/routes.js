angular.module('alvoRotas', []).config(function($routeProvider){
	$routeProvider.when('/categorias', {
		templateUrl: 'partials/categorias.html',
		controller: 'CategoriaController'
	});

	$routeProvider.when('/categorias/nova', {
		templateUrl: 'partials/categoria.html',
		controller: 'CategoriaController'
	});

	$routeProvider.when('/categorias/editar/:categoriaId', {
		templateUrl: 'partials/categoria.html',
		controller: 'CategoriaController'
	});

	$routeProvider.when('/negado', {
		templateUrl: 'partials/negado.html'
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