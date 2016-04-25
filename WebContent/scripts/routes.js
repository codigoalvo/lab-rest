angular.module('alvoRotas', []).config(function($routeProvider) {
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

	$routeProvider.when('/contas', {
		templateUrl: 'partials/contas.html',
		controller: 'ContaController'
	});

	$routeProvider.when('/categorias', {
		templateUrl: 'partials/categorias.html',
		controller: 'CategoriaController'
	});

	$routeProvider.when('/planejamento', {
		templateUrl: 'partials/planejamento.html',
		controller: 'PlanejamentoController'
	});

	$routeProvider.when('/negado', {
		templateUrl: 'partials/negado.html'
	});

	$routeProvider.when('/login', {
		templateUrl: 'partials/login.html',
		controller: 'LoginController'
	});

	$routeProvider.when('/senha/alterar', {
		templateUrl: 'partials/home.html',
		controller: 'SenhaController'
	});

	$routeProvider.when('/senha/esqueci', {
		templateUrl: 'partials/esqueci.html',
		controller: 'EmailController'
	});

	$routeProvider.when('/registro', {
		templateUrl: 'partials/registro.html',
		controller: 'EmailController'
	});

	$routeProvider.when('/email/verificar/:registroId', {
		templateUrl: 'partials/home.html',
		controller: 'EmailController'
	});

	$routeProvider.when('/extrato', {
		templateUrl: 'partials/transacoes.html',
		controller: 'TransacaoController'
	});

	$routeProvider.when('/resumo', {
		templateUrl: 'partials/resumo.html',
		controller: 'ResumoController'
	});

	$routeProvider.when('/home', {
		templateUrl: 'partials/home.html'
	});

	$routeProvider.otherwise({ redirectTo: '/home' });
});
