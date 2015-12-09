angular.module('emailService', [])
	.factory('recursoEmail', function($http, $window, $q) {
		var service = {};
		service.registrarEmail = function(email) {
			return $q(function(resolve, reject) {
				$http({
					  method: 'POST',
					  data: email,
					  url:'ws/email/registrar',
					  headers: {'Content-Type':'text/plain'}
				}).then(
					function(resp) {
						//console.log('registrarEmail.resp: '+resp);
						resolve(resp.data);
					}, function(erro) {
						//console.error('registrarEmail.erro', erro);
						reject(erro.data);
					})
			});
		};

		service.verificarRegistroId = function(registroId) {
			return $q(function(resolve, reject) {
				$http({
					  method: 'GET',
					  url:'ws/email/verificar/'+registroId,
					  headers: {'Content-Type':'text/plain'}
				}).then(
					function(resp) {
						//console.log('verificarRegistroId.resp: '+resp);
						//console.log('verificarRegistroId.resp.data: '+resp.data);
						resolve({
							entidade : resp.data,
						});
					}, function(erro) {
						//console.error('verificarRegistroId.erro', erro);
						reject(erro.data);
					})
			});
		};

		service.cadastrarUsuario = function(usuario) {
			return $q(function(resolve, reject) {
				$http({
					  method: 'POST',
					  data: usuario,
					  url:'ws/email/confirmar',
					  headers: {'Content-Type' : 'application/json'}
				}).then(
					function(resp) {
						console.log('confirmarEmail.resp: '+resp);
						resolve(resp.data);
					}, function(erro) {
						console.error('confirmarEmail.erro', erro);
						reject(erro.data);
					})
			});
		}

		return service; 
	});