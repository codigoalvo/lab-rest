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
	.factory("cadastroUsuario", function(recursoUsuario, $q) {
		var service = {};
		service.gravar = function(usuario) {
			return $q(function(resolve, reject) {
				if(usuario.id) {
					recursoUsuario.update({usuarioId: usuario.id}, usuario, function() {
						resolve({
							mensagem: 'Usuario ' + usuario.nome + ' atualizado com sucesso',
							inclusao: false
						});
					}, function(erro) {
						console.log(erro);
						reject({
							mensagem: 'Não foi possível atualizar o usuario ' + usuario.nome
						});
					});

				} else {
					recursoUsuario.save(usuario, function() {
						resolve({
							mensagem: 'Usuario ' + usuario.nome + ' incluído com sucesso',
							inclusao: true
						});
					}, function(erro) {
						console.log(erro);
						reject({
							mensagem: 'Não foi possível incluir o usuario ' + usuario.nome
						});
					});
				}
			},
			{
			    stripTrailingSlashes: false
			});
		};
		return service;
	});