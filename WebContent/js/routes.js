angular.module('alvoRotas', []).config(function($routeProvider){
	$routeProvider.when('/usuarios', {
		templateUrl: 'partials/usuarios.html',
		controller: 'UsuarioController'
	});

	$routeProvider.when('/usuarios/incluir', {
		templateUrl: 'partials/usuario.html',
		controller: 'UsuarioController'
	});

	$routeProvider.when('/usuarios/editar/:usuarioId', {
		templateUrl: 'partials/usuario.html',
		controller: 'UsuarioController'
	});
	
	$routeProvider.when('/categorias', {
		templateUrl: 'partials/categorias.html',
		controller: 'CategoriaController'
	});

	$routeProvider.when('/categorias/incluir', {
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

	$routeProvider.when('/senha', {
		templateUrl: 'partials/senha.html',
		controller: 'LoginController'
	});

	$routeProvider.when('/home', {
		templateUrl: 'partials/home.html'
	});

	$routeProvider.otherwise({redirectTo: '/home'});
});