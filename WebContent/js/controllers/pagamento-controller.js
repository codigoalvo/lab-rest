angular.module('alvoApp').controller('PagamentoController',
		function($scope, $routeParams, $location, $window, growl, dialogs, recursoPagamento, cadastroPagamento) {

	$scope.pagamentos = [];

	$scope.listarPagamentos = function(pagamentos) {
		recursoPagamento.query(function(resp) {
			$scope.pagamentos = resp;
		}, function(erro) {
			$scope.pagamentos = [];
			console.log(erro);
		});
	};

	$scope.removerPagamento = function(pagamento) {
		var dlg = dialogs.confirm('Atenção!', 'Confirma a exclusão do pagamento: <br>"'+pagamento.nome+'" ?', {'size':'sm'});
		dlg.result.then(function(btn){
			recursoPagamento.remove({pagamentoId: pagamento.id}, function(resp) {
				console.log(resp);
				$scope.pagamentos = recursoPagamento.query();
				growl.success(resp.mensagem);
			}, function(erro) {
				$scope.pagamentos = [];
				console.log(erro);
				growl.error(erro.mensagem, {title: 'Atenção!'});
			});
		});
	};

	$scope.pagamento = {};

	if($routeParams.pagamentoId) {
		recursoPagamento.get({pagamentoId: $routeParams.pagamentoId}, function(pagamento) {
			$scope.pagamento = pagamento; 
		}, function(erro) {
			$scope.pagamento = {};
			console.log(erro);
			growl.error('Não foi possível obter o pagamento', {title: 'Atenção!'});
		});
	}

	$scope.submeter = function() {
		cadastroPagamento.gravar($scope.pagamento)
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