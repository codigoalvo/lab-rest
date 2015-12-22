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

})