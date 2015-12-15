angular.module('alvoApp').controller('CategoriaController',
		function($scope, $routeParams, $location, $window, growl, cDialogs, servicosLogin, recursoCategoria, cadastroCategoria) {

	$scope.categorias = [];
	$scope.categoria = {};
	$scope.usuarioLogado = servicosLogin.pegarUsuarioDoToken();

	$scope.listarCategorias = function(categorias) {
		cDialogs.loading();
		recursoCategoria.query({usuarioId: $scope.usuarioLogado.id},function(resp) {
			cDialogs.hide();
			$scope.categorias = resp;
		}, function(erro) {
			cDialogs.hide();
			$scope.categorias = [];
			console.log(erro);
		});
	};

	$scope.removerCategoria = function(categoria) {
		cDialogs.confirm('Atenção!', 'Confirma a exclusão da categoria: <br>"'+categoria.nome+'" ?', 'Sim', 'Não')
		.then(function(btn){
			cDialogs.loading();
			recursoCategoria.remove({usuarioId: $scope.usuarioLogado.id, categoriaId: categoria.id}, function(resp) {
				cDialogs.hide();
				console.log(resp);
				$scope.listarCategorias();
				growl.success(resp.mensagem);
			}, function(erro) {
				$scope.categorias = [];
				console.log(erro);
				growl.error(erro.mensagem, {title: 'Atenção!'});
			});
		});
	};

	if($routeParams.categoriaId) {
		cDialogs.loading();
		recursoCategoria.get({usuarioId: $scope.usuarioLogado.id, categoriaId: $routeParams.categoriaId}, function(categoria) {
			cDialogs.hide();
			$scope.categoria = categoria; 
		}, function(erro) {
			cDialogs.hide();
			$scope.categoria = {};
			console.log(erro);
			growl.error('Não foi possível obter a categoria', {title: 'Atenção!'});
		});
	}

	$scope.submeter = function() {
		cDialogs.loading();
		cadastroCategoria.gravar($scope.usuarioLogado.id, $scope.categoria)
		.then(function(resp) {
			cDialogs.hide();
			$scope.categorias = [];
			if (resp.inclusao) $scope.categoria = {};
			$location.path("/categorias");
			growl.success(resp.mensagem);
		})
		.catch(function(erro) {
			cDialogs.hide();
			$scope.categorias = [];
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};
	
});