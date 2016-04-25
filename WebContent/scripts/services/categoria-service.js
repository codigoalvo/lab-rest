angular.module('categoriaService', ['ngResource'])
	.factory('recursoCategoria', function($resource) {
		return $resource('ws/usuarios/:usuarioId/categorias/:categoriaId', {}, {
			'update': {
				method: 'PUT'
			},
			'remove': {
				method: 'DELETE'
			}
		})
	})
	.factory("cadastroCategoria", function(recursoCategoria, $q) {
		var service = {};
		service.gravar = function(usuarioId, categoria) {
			return $q(function(resolve, reject) {
				//console.log('cadastroCategoria.categoria.id: '+categoria.id);
				//console.log('cadastroCategoria.categoria.nome: '+categoria.nome);
				//console.log('cadastroCategoria.usuarioId: ' + usuarioId)
				if (categoria.id) {
					recursoCategoria.update({ usuarioId: usuarioId, categoriaId: categoria.id }, categoria, function(resp) {
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
					recursoCategoria.save({ usuarioId: usuarioId }, categoria, function(resp) {
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
			}, {
				stripTrailingSlashes: false
			});
		};
		return service;
	});
