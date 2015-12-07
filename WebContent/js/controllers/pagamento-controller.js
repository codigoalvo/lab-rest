angular.module('alvoApp').controller('PagamentoController',
		function($scope, $routeParams, $location, $window, growl, dialogs, servicosLogin, recursoPagamento, cadastroPagamento) {

	$scope.pagamentos = [];
	$scope.pagamento = {};
	$scope.tiposPagamento = [];
	$scope.usuarioLogado = servicosLogin.pegarUsuarioDoToken();

	$scope.listarPagamentos = function(pagamentos) {
		recursoPagamento.query({usuarioId: $scope.usuarioLogado.id}, function(resp) {
			$scope.pagamentos = resp;
		}, function(erro) {
			$scope.pagamentos = [];
			console.log(erro);
		});
	};

	$scope.carregarTipos = function() {
		cadastroPagamento.tipos()
		.then(function(resp) {
			//console.log('PagamentoController.tiposPagamento', resp);
			$scope.tiposPagamento = resp;
		}).catch(function(erro) {
			$scope.tiposPagamento = [];
			console.log(erro);
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};

	$scope.removerPagamento = function(pagamento) {
		var dlg = dialogs.confirm('Atenção!', 'Confirma a exclusão do pagamento: <br>"'+pagamento.nome+'" ?', {'size':'sm'});
		dlg.result.then(function(btn){
			recursoPagamento.remove({usuarioId: $scope.usuarioLogado.id, pagamentoId: pagamento.id}, function(resp) {
				console.log(resp);
				$scope.listarPagamentos();
				growl.success(resp.mensagem);
			}, function(erro) {
				$scope.pagamentos = [];
				console.log(erro);
				growl.error(erro.mensagem, {title: 'Atenção!'});
			});
		});
	};

	if($routeParams.pagamentoId) {
		recursoPagamento.get({usuarioId: $scope.usuarioLogado.id, pagamentoId: $routeParams.pagamentoId}, function(pagamento) {
			$scope.pagamento = pagamento; 
		}, function(erro) {
			$scope.pagamento = {};
			console.log(erro);
			growl.error('Não foi possível obter o pagamento', {title: 'Atenção!'});
		});
	}

	$scope.submeter = function() {
		cadastroPagamento.gravar($scope.usuarioLogado.id, $scope.pagamento)
		.then(function(resp) {
			$scope.pagamentos = [];
			if (resp.inclusao) $scope.pagamento = {};
			$location.path("/pagamentos");
			growl.success(resp.mensagem);
		})
		.catch(function(erro) {
			$scope.pagamentos = [];
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};
	
});