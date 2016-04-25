angular.module('alvoApp').controller('LoginController', function($scope, $location, growl, servicosLogin, cDialogs) {
	$scope.usuario = {};

	$scope.loginTeste = function() {
		$scope.usuario.email = 'admin@email.com';
		$scope.usuario.senha = 'admin';
	}

	$scope.loginTeste();

	$scope.efetuarLogin = function() {
		cDialogs.delayedLoading(500);
		servicosLogin.efetuarLogin($scope.usuario)
			.then(function(resp) {
				cDialogs.hide();
				$location.path("/home");
				growl.success('Login realizado com sucesso!');
			}).catch(function(erro) {
				cDialogs.hide();
				console.log(erro);
				servicosLogin.efetuarLogout();
				growl.error(erro.mensagem, { title: 'Atenção!' });
			});
	};

	$scope.initLogin = function() {
		servicosLogin.efetuarLogout();
		$scope.loginTeste();
	}

})
