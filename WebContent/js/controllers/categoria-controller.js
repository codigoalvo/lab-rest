angular.module('alvoApp').controller('CategoriaController',	function($scope, $routeParams, $location, growl, recursoCategoria, cadastroCategoria) {
	$scope.categorias = [];

	$scope.listarCategorias = function(categorias) {
		recursoCategoria.query(function(categorias) {
			$scope.categorias = categorias;
		}, function(erro) {
			$scope.categorias = [];
			console.log(erro);
		});
	};

	$scope.removerCategoria = function(categoria) {
		recursoCategoria.remove({categoriaId: categoria.id}, function(resp) {
			console.log(resp);
			$scope.categorias = recursoCategoria.query();
			growl.success(resp.msg);
		}, function(erro) {
			$scope.categorias = [];
			console.log(erro);
			growl.error(erro.msg, {title: 'Atenção!'});
		});
	};
	
	$scope.categoria = {};

	if($routeParams.categoriaId) {
		recursoCategoria.get({categoriaId: $routeParams.categoriaId}, function(categoria) {
			$scope.categoria = categoria; 
		}, function(erro) {
			$scope.categoria = {};
			console.log(erro);
			growl.error('Não foi possível obter a categoria', {title: 'Atenção!'});
		});
	}

	$scope.submeter = function() {
		cadastroCategoria.gravar($scope.categoria)
		.then(function(resp) {
			$scope.categorias = [];
			if (resp.inclusao) $scope.categoria = {};
			$location.path("/categorias");
			growl.success(resp.msg);
		})
		.catch(function(erro) {
			$scope.categorias = [];
			growl.error(erro.msg, {title: 'Atenção!'});
		});
	};
	
});