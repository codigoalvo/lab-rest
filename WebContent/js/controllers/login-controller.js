angular.module('alvoApp').controller('LoginController',	function($scope, $location, servicosLogin) {
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
		}).catch(function(erro) {
			console.log(erro);
			servicosLogin.efetuarLogout();
		});
	};
	
	$scope.alterarSenha = function() {
		servicosLogin.alterarSenha($scope.usuario)
		.then( function(resp) {
			$scope.usuarioLogado = resp;
			$location.path("/home");
		}).catch(function(erro) {
			console.log(erro);
			servicosLogin.efetuarLogout();
		});
	};

})