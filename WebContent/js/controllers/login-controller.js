angular.module('alvoApp').controller('LoginController',	function($scope, $location, growl, servicosLogin, cDialogs) {
	$scope.usuario = {
		login : 'admin',
		senha : 'admin',
		senhaNova : '',
		senhaConfirma : '',
	};
	$scope.usuarioLogado = '';

	$scope.efetuarLogin = function() {
		cDialogs.delayedLoading(500);
		servicosLogin.efetuarLogin($scope.usuario)
		.then( function(resp) {
			cDialogs.hide();
			$scope.usuarioLogado = resp;
			$location.path("/home");
			growl.success('Login realizado com sucesso!');
		}).catch(function(erro) {
			cDialogs.hide();
			console.log(erro);
			servicosLogin.efetuarLogout();
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};

	$scope.initLogin = function() {
		servicosLogin.efetuarLogout();
		$scope.usuario.login = 'admin';
		$scope.usuario.senha = 'admin';
		$scope.usuario.senhaNova = '';
	}

	$scope.initSenha = function() {
		var logado = servicosLogin.pegarUsuarioDoToken();
		console.log('LoginController.initSenha.logado', logado);
		$scope.usuario.login = '';
		$scope.usuario.login = logado.login;
		console.log('LoginController.initSenha.logado.login', logado.login);
		$scope.usuario.senha = '';
		$scope.usuario.senhaNova = '';
		$scope.usuario.senhaConfirma = '';
	};

	$scope.alterarSenha = function() {
		if ($scope.usuario.senhaNova !== undefined  &&
			$scope.usuario.senhaNova !== null  &&
			$scope.usuario.senhaNova !== '' &&	
			$scope.usuario.senhaNova === $scope.usuario.senhaConfirma) {
			cDialogs.loading();
			servicosLogin.alterarSenha($scope.usuario)
			.then( function(resp) {
				cDialogs.hide();
				$scope.usuarioLogado = resp;
				$location.path("/home");
				growl.success('Senha alterada com sucesso!');
			}).catch(function(erro) {
				cDialogs.hide();
				console.log(erro);
				growl.error(erro.mensagem, {title: 'Atenção!'});
			});
		} else {
			growl.error('Nova senha e a confirmação da senha devem ser informadas e devem ser iguais');
		}
	};

})