angular.module('alvoApp').controller('TransacaoController',
		function($scope, $timeout, $q, growl, cDialogs, servicosLogin, recursoTransacao, cadastroTransacao, recursoConta, recursoCategoria) {

	$scope.contas = [];
	$scope.categorias = [];
	$scope.transacoes = [];
	$scope.transacao = {};
	$scope.hoje = new Date();
	$scope.mesSelecionado = $scope.hoje.getMonth()+1;
	$scope.anoSelecionado = $scope.hoje.getYear()+1900;
	$scope.usuarioLogado = servicosLogin.pegarUsuarioDoToken();
	$scope.isAdmin = ($scope.usuarioLogado.tipo === 'ADMIN');

	$scope.listarContas = function() {
		return $q(function(resolve, reject) {
			if ($scope.contas == undefined ||  $scope.contas == null  ||  $scope.contas.length == 0) {
				cDialogs.delayedLoading(1000);
				recursoConta.query({usuarioId: $scope.usuarioLogado.id, exibirInativos : false}, function(resp) {
					cDialogs.hide();
					$scope.contas = resp;
					resolve(true);
				}, function(erro) {
					cDialogs.hide();
					$scope.contas = [];
					console.log(erro);
					reject(false);
				});
			} else {
				resolve(true);
			}
		});
	};

	$scope.listarCategorias = function() {
		return $q(function(resolve, reject) {
			if ($scope.categorias == undefined ||  $scope.categorias == null  ||  $scope.categorias.length == 0) {
				cDialogs.delayedLoading();
				recursoCategoria.query({usuarioId: $scope.usuarioLogado.id, exibirInativos : false},function(resp) {
					cDialogs.hide();
					$scope.categorias = resp;
					resolve(true);
				}, function(erro) {
					cDialogs.hide();
					$scope.categorias = [];
					console.log(erro);
					reject(false);
				});
			} else {
				resolve(true);
			}
		});
	};

	$scope.listarTransacoes = function() {
		$scope.transacoes = $scope.listarTransacoesPeriodo($scope.mesSelecionado, $scope.anoSelecionado);
	}

	$scope.listarTransacoesPeriodo = function(mes, ano) {
		cDialogs.delayedLoading();
		return recursoTransacao.query({usuarioId: $scope.usuarioLogado.id, mes : mes, ano : ano},function(resp) {
			cDialogs.hide();
			//console.log('PlanejamentoController.listarPlanejamentosPeriodo.resp'+angular.toJson(resp));
			return resp;
		}, function(erro) {
			cDialogs.hide();
			console.log(erro);
			return [];
		});
	};

	$scope.removerTransacaoDireto = function(transacao) {
		cDialogs.loading();
		recursoTransacao.remove({usuarioId: $scope.usuarioLogado.id, transacaoId: transacao.id}, function(resp) {
			cDialogs.hide();
			//console.log(resp);
			$scope.listarTransacoes();
			growl.success(resp.mensagem);
		}, function(erro) {
			$scope.transacoes = [];
			console.log(erro);
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	}

	$scope.removerTransacaoConfirmar = function(transacao) {
		cDialogs.confirm('Atenção!', 'Confirma a exclusão da transacao: <br>"'+transacao.nome+'" ?', 'Sim', 'Não')
		.then(function(btn){
			$scope.removerTransacaoDireto(transacao);
		});
	};

	$scope.gravar = function(transacao) {
		//console.log('Transacao.gravar 1: '+angular.toJson(transacao));
		transacao.conta = angular.fromJson(transacao.conta);
		transacao.categoria = angular.fromJson(transacao.categoria);
		//console.log('Transacao.gravar 2: '+angular.toJson(transacao));
		cDialogs.delayedLoading(150);
		cadastroTransacao.gravar($scope.usuarioLogado.id, transacao)
		.then(function(resp) {
			cDialogs.hide();
			$scope.transacao = {};
			$scope.listarTransacoes();
			growl.success(resp.mensagem);
		})
		.catch(function(erro) {
			cDialogs.hide();
			$scope.transacao = {};
			$scope.listarTransacoes();
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};

	$scope.submeter = function() {
		$scope.gravar($scope.transacao);
	}

	$scope.dialogEditar = function(transacao) {
		var transacaoAlterar = angular.copy(transacao);
		if (transacaoAlterar.dataTransacao) {
			transacaoAlterar.dataTransacao = new Date(transacaoAlterar.dataTransacao);
		} else {
			transacaoAlterar.dataTransacao = new Date();
		}
		if (transacaoAlterar.dataPagamento) {
			transacaoAlterar.dataPagamento = new Date(transacaoAlterar.dataPagamento);
		} else {
			transacaoAlterar.dataPagamento = new Date();
		}
		if (transacao.conta) {
			transacaoAlterar.conta = {id : transacao.conta.id};
		}
		if (transacao.categoria) {
			transacaoAlterar.categoria = {id : transacao.categoria.id};
		}
		console.log('Transacao.dialogEditar.transacaoAlterar: '+angular.toJson(transacaoAlterar));
		$scope.listarContas().then(function(resp) {
			$scope.listarCategorias().then(function(resp) {

				var locals = {
					transacao : transacaoAlterar,
					tiposTransacao : [{key:'D', value:'Despesa'},{key:'R', value:'Receita'}],
					contas : $scope.contas,
					categorias : $scope.categorias,
				};
				cDialogs.custom('dialogs/transacao.html', locals).then(function(resp){
					$scope.gravar(resp);
				}).catch(function(erro) {
					if (erro) {
						console.log(erro);
					}
				});

			});
		});
	}

	$scope.formatarData = function(dataStr) {
		var data = new Date(dataStr);
		return formatDate(data);
	}

	$scope.valorTransacao = function(transacao) {
		var formatado = '';
		if (transacao.tipo === 'D') {
			formatado += '- ';
		}
		formatado += $scope.formatNumberMoney(transacao.valor);
		return formatado;
	}

	$scope.formatNumberMoney = function(number) {
		return formatMoney(number, 2, ',', '.');
	}

	$scope.rowClass = function(entidade) {
		if (entidade.tipo == 'R') {
			return 'ca-transacao-receita';
		} else if (entidade.tipo == 'D') {
			return 'ca-transacao-despesa';
		}
	}

	$scope.valorClass = function(entidade) {
		if (entidade.tipo == 'R') {
			return 'ca-transacao-receita-valor';
		} else if (entidade.tipo == 'D') {
			return 'ca-transacao-despesa-valor';
		}
	}

});