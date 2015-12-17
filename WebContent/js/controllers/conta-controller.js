angular.module('alvoApp').controller('ContaController',
		function($scope, $rootScope, $routeParams, $location, $window, $q, growl, cDialogs, servicosLogin, recursoConta, cadastroConta) {

	$scope.contas = [];
	$scope.conta = {};
	$scope.tiposConta = [];
	$scope.usuarioLogado = servicosLogin.pegarUsuarioDoToken();
	$scope.isAdmin = ($scope.usuarioLogado.tipo === 'ADMIN');

	$scope.carregarTipos = function() {
		return $q(function(resolve, reject) {
			if ($scope.tiposConta == undefined ||  $scope.tiposConta == null  ||  $scope.tiposConta.length == 0) {
				cadastroConta.tipos()
				.then(function(resp) {
					console.log('ContaController.tiposConta', resp);
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
				console.log('listarContas.tiposCarregados');
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

	$scope.removerContaDireto = function(conta) {
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
	}
	
	$scope.removerContaConfirmar = function(conta) {
		cDialogs.confirm('Atenção!', 'Confirma a exclusão da conta: <br>"'+conta.nome+'" ?', 'Sim', 'Não')
		.then(function(btn){
			$scope.removerContaDireto(conta);
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

	$scope.gravar = function(conta) {
		cDialogs.loading();
		cadastroConta.gravar($scope.usuarioLogado.id, conta)
		.then(function(resp) {
			cDialogs.hide();
			$scope.conta = {};
			$scope.listarContas();
			growl.success(resp.mensagem);
		})
		.catch(function(erro) {
			cDialogs.hide();
			$scope.conta = {};
			$scope.listarContas();
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};

	$scope.submeter = function() {
		$scope.gravar($scope.conta);
	}

	$scope.dialogIncluir = function() {
		var locals = {
				conta : {},
				tiposConta : $scope.tiposConta,
		}
		cDialogs.custom('dialogs/conta.html', locals).then(function(resp){
			$scope.gravar(resp);
		}).catch(function(erro) {
			if (erro) {
				console.log(erro);
			}
		});
	}

	$scope.dialogAlterar = function(conta) {
		var locals = {
				conta : conta,
				tiposConta : $scope.tiposConta,
		}
		cDialogs.custom('dialogs/conta.html', locals).then(function(resp){
			$scope.gravar(resp);
		}).catch(function(erro) {
			if (erro) {
				console.log(erro);
			}
		});
	}
	
});