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
				if(usuario.id) {
					recursoUsuario.update({usuarioId: usuario.id}, usuario, function() {
						resolve({
							msg: 'Usuario ' + usuario.nome + ' atualizado com sucesso',
							inclusao: false
						});
					}, function(erro) {
						console.log(erro);
						reject({
							msg: erro.data.msg,
						});
					});

				} else {
					recursoUsuario.save(usuario, function() {
						resolve({
							msg: 'Usuario ' + usuario.nome + ' incluído com sucesso',
							inclusao: true
						});
					}, function(erro) {
						console.log(erro);
						reject({
							msg: erro.data.msg,
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
						console.log('servicosUsuario.tipos', resp.data);
						resolve(tipos);
					}, function(erro) {
						console.error('Error', erro);
						reject({
							msg: erro.data.msg,
						});
					})
				});
			};
		return service;
	});