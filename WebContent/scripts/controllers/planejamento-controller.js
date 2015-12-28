angular.module('alvoApp').controller('PlanejamentoController',
		function($scope, $timeout, growl, cDialogs, servicosLogin, recursoPlanejamento, cadastroPlanejamento) {

	$scope.planejamentos = [];
	$scope.planejamento = {};
	$scope.hoje = new Date();
	$scope.mesSelecionado = $scope.hoje.getMonth()+1;
	$scope.anoSelecionado = $scope.hoje.getYear()+1900;
	$scope.usuarioLogado = servicosLogin.pegarUsuarioDoToken();
	$scope.isAdmin = ($scope.usuarioLogado.tipo === 'ADMIN');
	$scope.meses = [{key: 1, value:'Janeiro'}, {key: 2, value:'Fevereiro'}, {key: 3, value:'Março'}, {key: 4, value:'Abril'},
									{key: 5, value:'Maio'}, {key: 6, value:'Junho'}, {key: 7, value:'julho'}, {key: 8, value:'Agosto'},
									{key: 9, value:'Setembro'}, {key: 10, value:'Outubro'}, {key: 11, value:'Novembro'}, {key: 12, value:'Dezembro'}];
	$scope.anos = [2012, 2013, 2014, 2015, 2016, 2017];

	$scope.fixMes = function(mes) {
		var response = mes;
		if (mes > 12) {
			response = mes - 12;
		} else if (mes < 1) {
			response = 12 - mes;
		}
		return response;
	}

	$scope.labelMes = function(mesNum) {
		var mesFix = $scope.fixMes(eval(mesNum));
		var mesPos = eval(mesFix)-1;
		return ($scope.meses[mesPos]).value;
	}

	$scope.periodoChanged = function() {
		$scope.labelMesSelecionado = $scope.labelMes(eval($scope.mesSelecionado));
		$scope.labelMesAnterior = $scope.labelMes(eval($scope.mesSelecionado)-1);
		$scope.labelMesSeguinte = $scope.labelMes(eval($scope.mesSelecionado)+1);
	}

	$scope.listarPlanejamentosPeriodo = function(mes, ano) {
		cDialogs.delayedLoading();
		return recursoPlanejamento.query({usuarioId: $scope.usuarioLogado.id, mes : mes, ano : ano},function(resp) {
			cDialogs.hide();
			console.log('PlanejamentoController.listarPlanejamentosPeriodo.resp'+angular.toJson(resp));
			return resp;
		}, function(erro) {
			cDialogs.hide();
			console.log(erro);
			return [];
		});
	};

	$scope.listarPlanejamentos = function() {
		$scope.periodoChanged();
		$scope.planejamentos = $scope.listarPlanejamentosPeriodo($scope.mesSelecionado, $scope.anoSelecionado);
	}

	$scope.mesAnterior = function() {
		$scope.mesSelecionado--;
		if ($scope.mesSelecionado < 1) {
			$scope.mesSelecionado = eval(12) - eval($scope.mesSelecionado);
			$scope.anoSelecionado--;
		}
		$scope.listarPlanejamentos();
	}

	$scope.mesProximo = function() {
		$scope.mesSelecionado++;
		if ($scope.mesSelecionado > 12) {
			$scope.mesSelecionado = eval($scope.mesSelecionado) - eval(12);
			$scope.anoSelecionado++;
		}
		$scope.listarPlanejamentos();
	}

	$scope.removerPlanejamentoDireto = function(planejamento) {
		cDialogs.loading();
		recursoPlanejamento.remove({usuarioId: $scope.usuarioLogado.id, planejamentoId: planejamento.id}, function(resp) {
			cDialogs.hide();
			//console.log(resp);
			$scope.listarPlanejamentos();
			growl.success(resp.mensagem);
		}, function(erro) {
			$scope.planejamentos = [];
			console.log(erro);
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	}

	$scope.removerPlanejamentoConfirmar = function(planejamento) {
		cDialogs.confirm('Atenção!', 'Confirma a exclusão da planejamento: <br>"'+planejamento.nome+'" ?', 'Sim', 'Não')
		.then(function(btn){
			$scope.removerPlanejamentoDireto(planejamento);
		});
	};

	$scope.gravar = function(planejamento) {
		cDialogs.delayedLoading(150);
		cadastroPlanejamento.gravar($scope.usuarioLogado.id, planejamento)
		.then(function(resp) {
			cDialogs.hide();
			$scope.planejamento = {};
			$scope.listarPlanejamentos();
			growl.success(resp.mensagem);
		})
		.catch(function(erro) {
			cDialogs.hide();
			$scope.planejamento = {};
			$scope.listarPlanejamentos();
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};

	$scope.submeter = function() {
		$scope.gravar($scope.planejamento);
	}

	$scope.dialogIncluir = function() {
		var locals = {
				planejamento : {},
		};
		cDialogs.custom('dialogs/planejamento.html', locals).then(function(resp){
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