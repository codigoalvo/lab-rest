angular.module('alvoApp').controller('TransacaoController',
		function($scope, $timeout, growl, cDialogs, servicosLogin, recursoTransacao, cadastroTransacao) {

	$scope.transacoes = [];
	$scope.transacao = {};
	$scope.hoje = new Date();
	$scope.mesSelecionado = $scope.hoje.getMonth()+1;
	$scope.anoSelecionado = $scope.hoje.getYear()+1900;
	$scope.usuarioLogado = servicosLogin.pegarUsuarioDoToken();
	$scope.isAdmin = ($scope.usuarioLogado.tipo === 'ADMIN');

	$scope.listarTransacoes = function() {
		$scope.transacoes = $scope.listarTransacoesPeriodo($scope.mesSelecionado, $scope.anoSelecionado);
	}

	$scope.listarTransacoesPeriodo = function(mes, ano) {
		cDialogs.delayedLoading();
		return recursoTransacao.query({usuarioId: $scope.usuarioLogado.id, mes : mes, ano : ano},function(resp) {
			cDialogs.hide();
			console.log('PlanejamentoController.listarPlanejamentosPeriodo.resp'+angular.toJson(resp));
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

	$scope.dialogIncluir = function() {
		var locals = {
				transacao : {},
		};
		cDialogs.custom('dialogs/transacao.html', locals).then(function(resp){
			$scope.gravar(resp);
		}).catch(function(erro) {
			if (erro) {
				console.log(erro);
			}
		});
	}

	$scope.formatDate = function(dataStr) {
		var data = new Date(dataStr);
		//var dateString = data.getDate()  + "-" + (data.getMonth()+1) + "-" + data.getFullYear();
		var dateString = ("0" + data.getDate()).slice(-2) + "/" + ("0"+(data.getMonth()+1)).slice(-2) + "/" + data.getFullYear();
		return dateString;
	}

	$scope.rowClass = function(entidade) {
		if (entidade.tipo == 'R') {
			return 'ca-transacao-receita';
		} else if (entidade.tipo == 'D') {
			return 'ca-transacao-despesa';
		}
	}

});