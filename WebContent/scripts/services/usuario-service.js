angular.module('usuarioService', ['ngResource'])
	.factory('recursoUsuario', function($resource) {
		return $resource('ws/usuarios/:usuarioId', {},
			{
				'update' : {
					method: 'PUT'
				},
				'remove' : {
					method: 'DELETE' 
				}
			})
	})
	.factory("cadastroUsuario", function(recursoUsuario, $http, $q) {
		var service = {};
		service.gravar = function(usuario) {
			return $q(function(resolve, reject) {
				console.log('UsuarioService.gravar.usuario: '+angular.toJson(usuario));
				if(usuario.id) {
					recursoUsuario.update({usuarioId: usuario.id}, usuario, function(resp) {
						resolve({
							mensagem: resp.mensagem,
							inclusao: false
						});
					}, function(erro) {
						console.log(erro);
						reject({
							mensagem: erro.data.mensagem,
						});
					});

				} else {
					recursoUsuario.save(usuario, function(resp) {
						resolve({
							mensagem: resp.mensagem,
							inclusao: true
						});
					}, function(erro) {
						console.log(erro);
						reject({
							mensagem: erro.data.mensagem,
						});
					});
				}
			},
			{
			    stripTrailingSlashes: false
			});
		};
		service.tipos = function() {
			return $q(function(resolve, reject) {
				$http({
					  method: 'GET',
					  url:'ws/usuarios/tipos',
					  headers: {'Content-Type':'application/json'}
				}).then(
					function(resp) {
						var tipos = resp.data;
						resolve(tipos);
					}, function(erro) {
						console.error('Error', erro);
						reject({
							mensagem: erro.data.mensagem,
						});
					})
				});
			};
		return service;
	});