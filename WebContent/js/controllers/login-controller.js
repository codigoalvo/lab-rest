angular.module('alvoApp').controller('LoginController',	function($scope, $location, growl, servicosLogin) {
	$scope.usuario = {
		login : 'admin',
		senha : 'admin',
		senhaNova : '',
	};
	$scope.usuarioLogado = '';

	$scope.efetuarLogin = function() {
		servicosLogin.efetuarLogin($scope.usuario)
		.then( function(resp) {
			$scope.usuarioLogado = resp;
			$location.path("/home");
			growl.success('Login realizado com sucesso!');
		}).catch(function(erro) {
			console.log(erro);
			servicosLogin.efetuarLogout();
			growl.error(erro.msg, {title: 'Atenção!'});
		});
	};

	$scope.initLogin = function() {
		servicosLogin.efetuarLogout();
		$scope.usuario.login = 'admin';
		$scope.usuario.senha = 'admin';
		$scope.usuario.senhaNova = '';
	}

	$scope.initSenha = function() {
		var logado = servicosLogin.getUsuarioLogado();
		$scope.usuario.login = '';
		$scope.usuario.login = logado.login;
		$scope.usuario.senha = '';
		$scope.usuario.senhaNova = '';
	};

	$scope.alterarSenha = function() {
		servicosLogin.alterarSenha($scope.usuario)
		.then( function(resp) {
			$scope.usuarioLogado = resp;
			$location.path("/home");
			growl.success('Senha alterada com sucesso!');
		}).catch(function(erro) {
			console.log(erro);
			servicosLogin.efetuarLogout();
			growl.error(erro.msg, {title: 'Atenção!'});
		});
	};

})