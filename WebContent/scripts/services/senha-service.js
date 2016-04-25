angular.module('senhaService', [])
	.factory('recursoSenha', function($http, $window, $q, servicosLogin) {
		var service = {};

		service.alterarSenha = function(senhas) {
			var usuarioLogado = servicosLogin.pegarUsuarioDoToken();
			var senhaRequest = {
				email: usuarioLogado.email,
				senha: senhas.atual,
				senhaNova: senhas.nova,
			};
			return $q(function(resolve, reject) {
				$http({
					method: 'POST',
					data: senhaRequest,
					url: 'ws/auth/senha',
					headers: { 'Content-Type': 'application/json' }
				}).then(
					function(resp) {
						var usuarioLogado = servicosLogin.pegarUsuarioDoToken();
						//console.log('servicosLogin.senha.usuarioLogado', usuarioLogado);
						resolve(usuarioLogado);
					},
					function(erro) {
						//console.error('Error', erro.data.mensagem);
						reject({
							mensagem: erro.data.mensagem,
						});
					})
			});
		};

		return service;
	});
