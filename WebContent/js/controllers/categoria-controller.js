angular.module('alvoApp').controller('CategoriaController',
		function($scope, $routeParams, $location, $window, growl, dialogs, recursoCategoria, cadastroCategoria) {

	$scope.categorias = [];

	$scope.listarCategorias = function(categorias) {
		recursoCategoria.query(function(resp) {
			$scope.categorias = resp;
		}, function(erro) {
			$scope.categorias = [];
			console.log(erro);
		});
	};

	$scope.removerCategoria = function(categoria) {
		var dlg = dialogs.confirm('Atenção!', 'Confirma a exclusão da categoria: <br>"'+categoria.nome+'" ?', {'size':'sm'});
		dlg.result.then(function(btn){
			recursoCategoria.remove({categoriaId: categoria.id}, function(resp) {
				console.log(resp);
				$scope.categorias = recursoCategoria.query();
				growl.success(resp.mensagem);
			}, function(erro) {
				$scope.categorias = [];
				console.log(erro);
				growl.error(erro.mensagem, {title: 'Atenção!'});
			});
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
			growl.success(resp.mensagem);
		})
		.catch(function(erro) {
			$scope.categorias = [];
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};
	
});