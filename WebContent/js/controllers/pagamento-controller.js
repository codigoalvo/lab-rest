angular.module('alvoApp').controller('PagamentoController',
		function($scope, $routeParams, $location, $window, $q, growl, dialogs, servicosLogin, recursoPagamento, cadastroPagamento) {

	$scope.pagamentos = [];
	$scope.pagamento = {};
	$scope.tiposPagamento = [];
	$scope.usuarioLogado = servicosLogin.pegarUsuarioDoToken();

	$scope.carregarTipos = function() {
		return $q(function(resolve, reject) {
			if ($scope.tiposPagamento == undefined ||  $scope.tiposPagamento == null  ||  $scope.tiposPagamento.length == 0) {
				cadastroPagamento.tipos()
				.then(function(resp) {
					console.log('PagamentoController.tiposPagamento', resp);
					$scope.tiposPagamento = resp;
					resolve(true);
				}).catch(function(erro) {
					$scope.tiposPagamento = [];
					console.log(erro);
					growl.error(erro.mensagem, {title: 'Atenção!'});
					reject(false);
				});
			} else {
				resolve(true);
			}
		});
	};

	$scope.getTipo = function(key) {
		//console.log('getTipo');
		var tipo = {};
		for (pos in $scope.tiposPagamento) {
			tipo = $scope.tiposPagamento[pos];
			//console.log('tipo='+tipo);
			//console.log('tipo: '+tipo.key+':'+tipo.value);
			if (tipo.key === key) {
				//console.log('found! '+key+'='+tipo.key);
				return tipo.value;
			}
		}
		//console.log('tipo not found!');
		return 'UNDEF';
	}

	$scope.listarPagamentos = function(pagamentos) {
		recursoPagamento.query({usuarioId: $scope.usuarioLogado.id}, function(resp) {
			$scope.pagamentos = resp;
			$scope.carregarTipos().then(function() {
				console.log('tipos carregados');
				$scope.pagamentos.forEach(function(pagamento) {
					//console.log('pagamento = '+pagamento.tipo);
					var tipoValue = $scope.getTipo(pagamento.tipo);
					//console.log('pagamento.tipoValue= '+tipoValue);
					pagamento.tipoValue = tipoValue;
					//console.log('pagamento = '+pagamento);
				});
			});
		}, function(erro) {
			$scope.pagamentos = [];
			console.log(erro);
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