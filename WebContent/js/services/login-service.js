angular.module('loginService', [])
	.factory('servicosLogin', function($http, $window, $q) {
		var service = {};
		var usuarioLogado = null;
		service.efetuarLogin = function(usuario) {
			return $q(function(resolve, reject) {
			$http({
				  method: 'POST',
				  data: usuario,
				  url:'ws/login',
				  headers: {'Content-Type':'application/json'}
			}).then(
				function(resp) {
					usuarioLogado = resp.data;
					console.log('servicosLogin.usuarioLogado', resp.data);
					$window.sessionStorage.usuarioLogado = resp.data;
					resolve(usuarioLogado);
				}, function(erro) {
					usuarioLogado = null;
					console.error('Error', erro);
					reject({
						mensagem: 'Não foi possivel realizar o login!'
					});
				})
			});
		};

		service.efetuarLogout = function() {
			console.log('Removendo usuarioLogado da sessão!')
			usuarioLogado = null;
			delete $window.sessionStorage.usuarioLogado;
			console.log('Removendo token da sessão!')
			delete $window.sessionStorage.token;
		};

		service.getUsuarioLogado = function() {
			return angular.copy(usuarioLogado);
		};

		return service; 
	});