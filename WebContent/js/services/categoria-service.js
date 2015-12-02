angular.module('categoriaService', ['ngResource'])
	.factory('recursoCategoria', function($resource) {
		return $resource('ws/categorias/:categoriaId', {},
			{
				'update' : {
					method: 'PUT'
				},
				'remove' : {
					method: 'DELETE' 
				}
			})
	})
	.factory("cadastroCategoria", function(recursoCategoria, $q) {
		var service = {};
		service.gravar = function(categoria) {
			return $q(function(resolve, reject) {
				if(categoria.id) {
					recursoCategoria.update({categoriaId: categoria.id}, categoria, function() {
						resolve({
							msg: 'Categoria ' + categoria.nome + ' atualizada com sucesso',
							inclusao: false
						});
					}, function(erro) {
						console.log(erro);
						reject({
							msg: erro.data.msg,
						});
					});

				} else {
					recursoCategoria.save(categoria, function() {
						resolve({
							msg: 'Categoria ' + categoria.nome + ' incluída com sucesso',
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
		return service;
	});