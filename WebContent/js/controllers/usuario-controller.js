angular.module('alvoApp').controller('UsuarioController',
		function($scope, $routeParams, $q, growl, cDialogs, recursoUsuario, cadastroUsuario) {

	$scope.usuarios = [];
	$scope.tiposUsuario = [];
	$scope.hoje = new Date();

	$scope.carregarTipos = function() {
		return $q(function(resolve, reject) {
			if ($scope.tiposUsuario == undefined ||  $scope.tiposUsuario == null  ||  $scope.tiposUsuario.length == 0) {
				cadastroUsuario.tipos()
				.then(function(resp) {
					console.log('UsuarioController.tiposUsuario', resp);
					$scope.tiposUsuario = resp;
					resolve(true);
				}).catch(function(erro) {
					$scope.tiposUsuario = [];
					console.log(erro);
					growl.error(erro.mensagem, {title: 'Atenção!'});
					reject(false);
				});
			} else {
				resolve(true);
			}
		});
	};

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

	$scope.removerUsuarioDireto = function(usuario) {
		cDialogs.loading();
		recursoUsuario.remove({usuarioId: usuario.id}, function(resp) {
			cDialogs.hide();
			//console.log(resp);
			$scope.usuarios = recursoUsuario.query();
			growl.success(resp.mensagem);
		}, function(erro) {
			cDialogs.hide();
			$scope.usuarios = [];
			console.log(erro);
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	}

	$scope.removerUsuarioConfirmar = function(usuario) {
		cDialogs.confirm('Atenção!', 'Confirma a exclusão do usuário: <br>"'+usuario.login+'" ?', 'Sim', 'Não')
		.then(function(btn){
			$scope.removerUsuarioDireto(usuario);
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

	$scope.gravar = function(usuario) {
		cDialogs.loading();
		cadastroUsuario.gravar(usuario)
		.then(function(resp) {
			cDialogs.hide();
			$scope.usuario = {};
			$scope.listarUsuarios();
			growl.success(resp.mensagem);
		})
		.catch(function(erro) {
			cDialogs.hide();
			$scope.usuario = {};
			$scope.listarUsuarios();
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};

	$scope.submeter = function() {
		$scope.gravar($scope.usuario);
	};

	$scope.dialogIncluir = function() {
		$scope.carregarTipos().then(function() {
			var locals = {
					usuario : {},
					tiposUsuario : $scope.tiposUsuario,
					hoje : $scope.hoje,
			}
			console.log("locals.tiposUsuario: "+locals.tiposUsuario);
			cDialogs.custom('dialogs/usuario.html', locals).then(function(resp){
				$scope.gravar(resp);
			}).catch(function(erro) {
				if (erro) {
					console.log(erro);
				}
			});
		});
	}

	$scope.dialogAlterar = function(usuario) {
			$scope.carregarTipos().then(function() {
			var locals = {
					usuario : usuario,
					tiposUsuario : $scope.tiposUsuario,
					hoje : $scope.hoje,
			}
			cDialogs.custom('dialogs/usuario.html', locals).then(function(resp){
				$scope.gravar(resp);
			}).catch(function(erro) {
				if (erro) {
					console.log(erro);
				}
			});
		});
	}

});