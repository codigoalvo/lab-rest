angular.module('alvoApp').controller('CategoriaController',	function($scope, $routeParams, $location, recursoCategoria, cadastroCategoria) {
	$scope.categorias = [];

	$scope.listarCategorias = function(categorias) {
		recursoCategoria.query(function(categorias) {
			$scope.categorias = categorias;
		}, function(erro) {
			console.log(erro);
		});
	};
	
	$scope.removerCategoria = function(categoria) {
		recursoCategoria.remove({categoriaId: categoria.id}, function(resp) {
			console.log(resp);
			$scope.categorias = recursoCategoria.query();
		}, function(erro) {
			console.log(erro);
		});
	};
	
	$scope.categoria = {};
	$scope.mensagem = '';

	if($routeParams.categoriaId) {
		recursoCategoria.get({categoriaId: $routeParams.categoriaId}, function(categoria) {
			$scope.categoria = categoria; 
		}, function(erro) {
			console.log(erro);
			$scope.mensagem = 'Não foi possível obter a categoria'
		});
	}

	$scope.submeter = function() {
		cadastroCategoria.gravar($scope.categoria)
		.then(function(dados) {
			$scope.mensagem = dados.mensagem;
			if (dados.inclusao) $scope.categoria = {};
			$location.path("/categorias");
		})
		.catch(function(erro) {
			$scope.mensagem = erro.mensagem;
		});
	};
	
});