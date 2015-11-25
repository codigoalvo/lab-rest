angular.module('categoriaService', ['ngResource'])
	.factory('recursoCategoria', function($resource) {

		return $resource('ws/categorias/:categoriaId', null, {
			'update' : {
				method: 'PUT'
			}
		});
	})