angular.module('alvoApp').controller('UsuarioController',
		function($scope, $routeParams, $location, $window, growl, cDialogs, recursoUsuario, cadastroUsuario) {

	$scope.usuarios = [];
	$scope.tiposUsuario = [];
	$scope.hoje = new Date();

	$scope.listarUsuarios = function(usuarios) {
		cDialogs.loading();
		recursoUsuario.query(function(usuarios) {
			cDialogs.hide();
			$scope.usuarios = usuarios;
		}, function(erro) {
			cDialogs.hide();
			$scope.usuarios = [];
			console.log(erro);
		});
	};

	$scope.carregarTipos = function() {
		cadastroUsuario.tipos()
		.then(function(resp) {
			//console.log('UsuarioController.tiposUsuario', resp);
			$scope.tiposUsuario = resp;
		}).catch(function(erro) {
			$scope.tiposUsuario = [];
			console.log(erro);
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};

	$scope.removerUsuario = function(usuario) {
		cDialogs.confirm('Atenção!', 'Confirma a exclusão do usuário: <br>"'+usuario.login+'" ?', 'Sim', 'Não')
		.then(function(btn){
			cDialogs.loading();
			recursoUsuario.remove({usuarioId: usuario.id}, function(resp) {
				cDialogs.hide();
				console.log(resp);
				$scope.usuarios = recursoUsuario.query();
				growl.success(resp.mensagem);
			}, function(erro) {
				cDialogs.hide();
				$scope.usuarios = [];
				console.log(erro);
				growl.error(erro.mensagem, {title: 'Atenção!'});
			});
		});
	};

	$scope.usuario = {};

	if($routeParams.usuarioId) {
		cDialogs.loading();
		recursoUsuario.get({usuarioId: $routeParams.usuarioId}, function(usuario) {
			cDialogs.hide();
			//console.log('UsuarioController.buscar'+ angular.toJson(usuario))
			$scope.usuario = usuario; 
		}, function(erro) {
			cDialogs.hide();
			console.log(erro);
			$scope.usuario = {};
			growl.error('Não foi possível obter o usuario', {title: 'Atenção!'});
		});
	}

	$scope.submeter = function() {
		cDialogs.loading();
		cadastroUsuario.gravar($scope.usuario)
		.then(function(resp) {
			cDialogs.hide();
			$scope.usuarios = [];
			$scope.usuario = {};
			if (resp.inclusao) $scope.usuario = {};
			$location.path("/usuarios");
			growl.success(resp.mensagem);
		})
		.catch(function(erro) {
			cDialogs.hide();
			$scope.usuarios = [];
			$scope.usuario = {};
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};
	
});