angular.module('loginService', [])
	.factory('servicosLogin', function($http, $window, $q) {
		var service = {};
		service.efetuarLogin = function(usuario) {
			return $q(function(resolve, reject) {
			$http({
				  method: 'POST',
				  data: usuario,
				  url:'ws/auth/login',
				  headers: {'Content-Type':'application/json'}
			}).then(
				function(resp) {
					var usuarioLogado = service.pegarUsuarioDoToken();
					//console.log('servicosLogin.login.data', resp.data);
					//console.log('servicosLogin.login.usuarioLogado', usuarioLogado);
					//console.log('servicosLogin.login.usuarioLogado.email', usuarioLogado.email);
					resolve(usuarioLogado);
				}, function(erro) {
					//console.error('Error', erro.data.mensagem);
					service.efetuarLogout();
					reject({
						mensagem: erro.data.mensagem,
					});
				})
			});
		};

		service.efetuarLogout = function() {
			console.log('Removendo token da sessão!')
			delete $window.sessionStorage.token;
		};

		service.decode = function urlBase64Decode(str) {
			var output = str.replace('-', '+').replace('_', '/');
			switch (output.length % 4) {
			case 0:
				break;
			case 2:
				output += '==';
				break;
			case 3:
				output += '=';
				break;
			default:
				throw 'Cadeia de caracteres base64url inválida!';
			}
			return window.atob(output);
		}

		service.pegarUsuarioDoToken = function getUserFromDecodedToken() {
			var decodedToken = service.pegarDecodedToken();
			var user = angular.fromJson(decodedToken.usuario);
			return user;
		}

		service.pegarDecodedToken = function getDecodedToken() {
			var token = $window.sessionStorage.token;
			var decodedToken = {};
			if (typeof token !== 'undefined') {
				//console.log('pegarUsuarioDoToken.token is undefined');
				var encoded = token.split('.')[1];
				decodedToken = JSON.parse(service.decode(encoded));
			}
			//console.log('LoginService.decodedToken: '+angular.toJson(decodedToken));
			return decodedToken;
		}

		return service; 
	});