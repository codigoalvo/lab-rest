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

	$routeProvider.when('/contas', {
		templateUrl: 'partials/contas.html',
		controller: 'ContaController'
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

	$routeProvider.when('/registro', {
		templateUrl: 'partials/registro.html',
		controller: 'EmailController'
	});

	$routeProvider.when('/registro/confirmar/:registroId', {
		templateUrl: 'partials/home.html',
		controller: 'EmailController'
	});

	$routeProvider.when('/resumo', {
		templateUrl: 'partials/resumo.html',
		controller: 'ResumoController'
	});

	$routeProvider.when('/home', {
		templateUrl: 'partials/home.html'
	});
	
	$routeProvider.when('/header', {
		templateUrl: 'partials/header.html'
	});


	$routeProvider.otherwise({redirectTo: '/home'});
});