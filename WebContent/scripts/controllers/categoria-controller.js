angular.module('alvoApp').controller('CategoriaController',
		function($scope, $timeout, growl, cDialogs, servicosLogin, recursoCategoria, cadastroCategoria) {

	$scope.categorias = [];
	$scope.categoria = {};
	$scope.exibirInativos = false;
	$scope.usuarioLogado = servicosLogin.pegarUsuarioDoToken();
	$scope.isAdmin = ($scope.usuarioLogado.tipo === 'ADMIN');
	$scope.hoje = new Date();

	$scope.listarCategorias = function() {
		cDialogs.delayedLoading();
		recursoCategoria.query({usuarioId: $scope.usuarioLogado.id, exibirInativos : $scope.exibirInativos},function(resp) {
			cDialogs.hide();
			$scope.categorias = resp;
		}, function(erro) {
			cDialogs.hide();
			$scope.categorias = [];
			console.log(erro);
		});
	};

	$scope.listarDelay = function(){
		$timeout(function() {
			$scope.listarCategorias();
		}, 50)
	};

	$scope.ativaDesativa = function(categoria) {
		//categoria.dataInativo = categoria.ativo ? null : $scope.hoje;
		$scope.gravar(categoria);
		//growl.success('Teste: '+categoria.ativo);
	}

	$scope.removerCategoriaDireto = function(categoria) {
		cDialogs.loading();
		recursoCategoria.remove({usuarioId: $scope.usuarioLogado.id, categoriaId: categoria.id}, function(resp) {
			cDialogs.hide();
			//console.log(resp);
			$scope.listarCategorias();
			growl.success(resp.mensagem);
		}, function(erro) {
			$scope.categorias = [];
			console.log(erro);
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	}

	$scope.removerCategoriaConfirmar = function(categoria) {
		cDialogs.confirm('Atenção!', 'Confirma a exclusão da categoria: <br>"'+categoria.nome+'" ?', 'Sim', 'Não')
		.then(function(btn){
			$scope.removerCategoriaDireto(categoria);
		});
	};

	$scope.gravar = function(categoria) {
		cDialogs.delayedLoading(150);
		cadastroCategoria.gravar($scope.usuarioLogado.id, categoria)
		.then(function(resp) {
			cDialogs.hide();
			$scope.categoria = {};
			$scope.listarCategorias();
			growl.success(resp.mensagem);
		})
		.catch(function(erro) {
			cDialogs.hide();
			$scope.categoria = {};
			$scope.listarCategorias();
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};

	$scope.submeter = function() {
		$scope.gravar($scope.categoria);
	}

	$scope.dialogIncluir = function() {
		var locals = {
				categoria : {},
		};
		cDialogs.custom('dialogs/categoria.html', locals).then(function(resp){
			$scope.gravar(resp);
		}).catch(function(erro) {
			if (erro) {
				console.log(erro);
			}
		});
	}

});