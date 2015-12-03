angular.module('alvoApp').controller('UsuarioController',
		function($scope, $routeParams, $location, $window, growl, dialogs, recursoUsuario, cadastroUsuario) {

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
		.then(function(resp) {
			console.log('UsuarioController.tiposUsuario', resp);
			$scope.tiposUsuario = resp;
		}).catch(function(erro) {
			$scope.tiposUsuario = [];
			console.log(erro);
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};

	$scope.removerUsuario = function(usuario) {
		var dlg = dialogs.confirm('Atenção!', 'Confirma a exclusão do usuário?', {});
		dlg.result.then(function(btn){
			recursoUsuario.remove({usuarioId: usuario.id}, function(resp) {
				console.log(resp);
				$scope.usuarios = recursoUsuario.query();
				growl.success(resp.mensagem);
			}, function(erro) {
				$scope.usuarios = [];
				console.log(erro);
				growl.error(erro.mensagem, {title: 'Atenção!'});
			});
		});
	};

	$scope.usuario = {};

	if($routeParams.usuarioId) {
		recursoUsuario.get({usuarioId: $routeParams.usuarioId}, function(usuario) {
			$scope.usuario = usuario; 
		}, function(erro) {
			console.log(erro);
			$scope.usuario = {};
			growl.error('Não foi possível obter o usuario', {title: 'Atenção!'});
		});
	}

	$scope.submeter = function() {
		cadastroUsuario.gravar($scope.usuario)
		.then(function(resp) {
			$scope.usuarios = [];
			$scope.usuario = {};
			if (resp.inclusao) $scope.usuario = {};
			$location.path("/usuarios");
			growl.success(resp.mensagem);
		})
		.catch(function(erro) {
			$scope.usuarios = [];
			$scope.usuario = {};
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};
	
});