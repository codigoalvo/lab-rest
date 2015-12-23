angular.module('emailService', [])
	.factory('recursoEmail', function($http, $window, $q) {
		var service = {};
		service.enviarEmail = function(email, tipo) {
			return $q(function(resolve, reject) {
				$http({
					  method: 'POST',
					  data: email,
					  url:'ws/email/'+tipo+'/enviar',
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

		service.verificarEmailId = function(registroId) {
			return $q(function(resolve, reject) {
				console.log('EmailService.verificarRegistroId : Removendo token da sessão!')
				delete $window.sessionStorage.token;
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
						console.log('EmailService.cadastrarUsuario : Removendo token da sessão!')
						delete $window.sessionStorage.token;
						resolve(resp.data);
					}, function(erro) {
						console.error('confirmarEmail.erro', erro);
						reject(erro.data);
					})
			});
		}

		service.alterarSenha = function(validadorEmail, senhas) {
			console.log('EmailService.alterarSenha');
			return $q(function(resolve, reject) {
				$http({
					  method: 'POST',
					  data: angular.toJson({ email : validadorEmail.email,
					  				hash : validadorEmail.id,
					  				senha: senhas.nova,
					  			}),
					  url:'ws/email/senha',
					  headers: {'Content-Type' : 'application/json'}
				}).then(
					function(resp) {
						console.log('confirmarEmail.resp: '+resp);
						console.log('EmailService.alterarSenha : Removendo token da sessão!')
						delete $window.sessionStorage.token;
						resolve(resp.data);
					}, function(erro) {
						console.error('confirmarEmail.erro', erro);
						reject(erro.data);
					})
			});
		}

		return service; 
	});