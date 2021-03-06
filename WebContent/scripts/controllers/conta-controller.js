angular.module('alvoApp').controller('ContaController',
	function($scope, $timeout, $q, growl, cDialogs, servicosLogin, recursoConta, cadastroConta) {

		$scope.contas = [];
		$scope.conta = {};
		$scope.exibirInativos = false;
		$scope.tiposConta = [];
		$scope.usuarioLogado = servicosLogin.pegarUsuarioDoToken();
		$scope.isAdmin = ($scope.usuarioLogado.tipo === 'ADMIN');

		$scope.carregarTipos = function() {
			return $q(function(resolve, reject) {
				if ($scope.tiposConta == undefined || $scope.tiposConta == null || $scope.tiposConta.length == 0) {
					cDialogs.delayedLoading(1000);
					cadastroConta.tipos()
						.then(function(resp) {
							//console.log('ContaController.tiposConta', resp);
							$scope.tiposConta = resp;
							cDialogs.hide();
							resolve(true);
						}).catch(function(erro) {
							$scope.tiposConta = [];
							console.log(erro);
							cDialogs.hide();
							growl.error(erro.mensagem, { title: 'Atenção!' });
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

		$scope.listarDelay = function() {
			$timeout(function() {
				$scope.listarContas();
			}, 50)
		};

		$scope.listarContas = function() {
			cDialogs.delayedLoading();
			recursoConta.query({ usuarioId: $scope.usuarioLogado.id, exibirInativos: $scope.exibirInativos }, function(resp) {
				cDialogs.hide();
				$scope.contas = resp;
				$scope.carregarTipos().then(function() {
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
			recursoConta.remove({ usuarioId: $scope.usuarioLogado.id, contaId: conta.id }, function(resp) {
				cDialogs.hide();
				//console.log(resp);
				$scope.listarContas();
				growl.success(resp.mensagem);
			}, function(erro) {
				cDialogs.hide();
				$scope.contas = [];
				console.log(erro);
				growl.error(erro.mensagem, { title: 'Atenção!' });
			});
		}

		$scope.removerContaConfirmar = function(conta) {
			cDialogs.confirm('Atenção!', 'Confirma a exclusão da conta: <br>"' + conta.nome + '" ?', 'Sim', 'Não')
				.then(function(btn) {
					$scope.removerContaDireto(conta);
				});
		};

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
					growl.error(erro.mensagem, { title: 'Atenção!' });
				});
		};

		$scope.submeter = function() {
			$scope.gravar($scope.conta);
		}

		$scope.dialogIncluir = function() {
			var locals = {
				conta: {},
				tiposConta: $scope.tiposConta,
			}
			cDialogs.custom('dialogs/conta.html', locals).then(function(resp) {
				$scope.gravar(resp);
			}).catch(function(erro) {
				if (erro) {
					console.log(erro);
				}
			});
		}

		$scope.dialogAlterar = function(conta) {
			var locals = {
				conta: angular.copy(conta),
				tiposConta: $scope.tiposConta,
			}
			cDialogs.custom('dialogs/conta.html', locals).then(function(resp) {
				$scope.gravar(resp);
			}).catch(function(erro) {
				if (erro) {
					console.log(erro);
				}
			});
		}

		$scope.rowClass = function(entidade) {
			if (!entidade.ativo) {
				return 'ca-linha-inativo';
			}
		}

	});
