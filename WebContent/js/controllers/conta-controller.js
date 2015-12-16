angular.module('alvoApp').controller('ContaController',
		function($scope, $routeParams, $location, $window, $q, growl, cDialogs, servicosLogin, recursoConta, cadastroConta) {

	$scope.contas = [];
	$scope.conta = {};
	$scope.tiposConta = [];
	$scope.usuarioLogado = servicosLogin.pegarUsuarioDoToken();

	$scope.carregarTipos = function() {
		return $q(function(resolve, reject) {
			if ($scope.tiposConta == undefined ||  $scope.tiposConta == null  ||  $scope.tiposConta.length == 0) {
				cadastroConta.tipos()
				.then(function(resp) {
					//console.log('ContaController.tiposConta', resp);
					$scope.tiposConta = resp;
					resolve(true);
				}).catch(function(erro) {
					$scope.tiposConta = [];
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
		for (pos in $scope.tiposConta) {
			tipo = $scope.tiposConta[pos];
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

	$scope.listarContas = function(contas) {
		cDialogs.loading();
		recursoConta.query({usuarioId: $scope.usuarioLogado.id}, function(resp) {
			cDialogs.hide();
			$scope.contas = resp;
			$scope.carregarTipos().then(function() {
				//console.log('tipos carregados');
				$scope.contas.forEach(function(conta) {
					//console.log('conta = '+conta.tipo);
					var tipoValue = $scope.getTipo(conta.tipo);
					//console.log('conta.tipoValue= '+tipoValue);
					conta.tipoValue = tipoValue;
					//console.log('conta = '+conta);
				});
			});
		}, function(erro) {
			cDialogs.hide();
			$scope.contas = [];
			console.log(erro);
		});
	};

	$scope.removerConta = function(conta) {
		cDialogs.confirm('Atenção!', 'Confirma a exclusão da conta: <br>"'+conta.nome+'" ?', 'Sim', 'Não')
		.then(function(btn){
			cDialogs.loading();
			recursoConta.remove({usuarioId: $scope.usuarioLogado.id, contaId: conta.id}, function(resp) {
				cDialogs.hide();
				//console.log(resp);
				$scope.listarContas();
				growl.success(resp.mensagem);
			}, function(erro) {
				cDialogs.hide();
				$scope.contas = [];
				console.log(erro);
				growl.error(erro.mensagem, {title: 'Atenção!'});
			});
		});
	};

	if($routeParams.contaId) {
		cDialogs.loading();
		recursoConta.get({usuarioId: $scope.usuarioLogado.id, contaId: $routeParams.contaId}, function(conta) {
			cDialogs.hide();
			$scope.conta = conta; 
		}, function(erro) {
			cDialogs.hide();
			$scope.conta = {};
			console.log(erro);
			growl.error('Não foi possível obter a conta', {title: 'Atenção!'});
		});
	}

	$scope.submeter = function() {
		cDialogs.loading();
		cadastroConta.gravar($scope.usuarioLogado.id, $scope.conta)
		.then(function(resp) {
			cDialogs.hide();
			$scope.contas = [];
			if (resp.inclusao) $scope.conta = {};
			$location.path("/contas");
			growl.success(resp.mensagem);
		})
		.catch(function(erro) {
			cDialogs.hide();
			$scope.contas = [];
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};
	
});