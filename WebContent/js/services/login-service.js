angular.module('loginService', [])
	.factory('servicosLogin', function($http, $window) {
		var service = {};
		var usuarioLogado = null;
		service.efetuarLogin = function(usuario) {
			$http({
				  method: 'POST',
				  data: usuario,
				  url:'ws/login',
				  headers: {'Content-Type':'application/json'}
			}).then(
				function(resp) {
					usuarioLogado = resp.data;
					console.log('usuarioLogado', resp.data);
					$window.sessionStorage.usuarioLogado = resp.data;
				}, function(err) {
					usuarioLogado = null;
					console.error('Error', err);
				})
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