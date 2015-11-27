angular.module('alvoApp').controller('LoginController',	function($scope, $location, servicosLogin) {
	$scope.usuario = {
		login : 'admin',
		senha : 'admin',
	};
	$scope.usuarioLogado = '';

	$scope.efetuarLogin = function() {
		servicosLogin.efetuarLogin($scope.usuario)
		.then( function(resp) {
			$scope.usuarioLogado = resp;
			$location.path("/home");
		}).catch(function(erro) {
			console.log(erro);
		});
	};

	$scope.efetuarLogout = function() {
		servicosLogin.efetuarLogout();
	};
	
})