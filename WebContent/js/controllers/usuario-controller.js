angular.module('alvoApp').controller('UsuarioController',	function($scope, $routeParams, $location, recursoUsuario, cadastroUsuario) {
	$scope.usuarios = [];
	$scope.tiposUsuario = [];

	$scope.listarUsuarios = function(usuarios) {
		recursoUsuario.query(function(usuarios) {
			$scope.usuarios = usuarios;
		}, function(erro) {
			$scope.usuarios = [];
			console.log(erro);
		});
	};
	
	$scope.tiposUsuario = function() {
		cadastroUsuario.tipos()
		.then(function(dados) {
			console.log('UsuarioController.tiposUsuario', dados);
			$scope.tiposUsuario = dados;
		}).catch(function(erro) {
			$scope.tiposUsuario = [];
			console.log(erro);
		});
	};

	$scope.removerUsuario = function(usuario) {
		recursoUsuario.remove({usuarioId: usuario.id}, function(resp) {
			console.log(resp);
			$scope.usuarios = recursoUsuario.query();
		}, function(erro) {
			$scope.usuarios = [];
			console.log(erro);
		});
	};

	$scope.usuario = {};
	$scope.mensagem = '';

	if($routeParams.usuarioId) {
		recursoUsuario.get({usuarioId: $routeParams.usuarioId}, function(usuario) {
			$scope.usuario = usuario; 
		}, function(erro) {
			console.log(erro);
			$scope.usuario = {};
			$scope.mensagem = 'Não foi possível obter o usuario'
		});
	}

	$scope.submeter = function() {
		cadastroUsuario.gravar($scope.usuario)
		.then(function(dados) {
			$scope.mensagem = dados.mensagem;
			$scope.usuarios = [];
			$scope.usuario = {};
			if (dados.inclusao) $scope.usuario = {};
			$location.path("/usuarios");
		})
		.catch(function(erro) {
			$scope.usuarios = [];
			$scope.usuario = {};
			$scope.mensagem = erro.mensagem;
		});
	};
	
});