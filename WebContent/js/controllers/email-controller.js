angular.module('alvoApp').controller('EmailController',	function($scope, $routeParams, $location, growl, cDialogs, recursoEmail) {
	$scope.email = '';
	$scope.erro = false;

	$scope.cadastrarUsuario = function(usuario) {
		cDialogs.loading();
		recursoEmail.cadastrarUsuario(usuario)
		.then(function(resp) {
			cDialogs.hide();
			$location.path("/login");
			growl.success('Registro de novo usuário confirmado com sucesso!');
		})
		.catch(function(erro) {
			cDialogs.hide();
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};

	$scope.dialogCadastrar = function(emailRegistro) {
		console.log('dialogCadastrar');
		var locals = {
			usuarioRegistro : {
				email : emailRegistro,
			},
		}
		console.log("locals.usuarioRegistro: "+locals.usuarioRegistro);
		cDialogs.custom('dialogs/confirmar.html', locals).then(function(resp){
			$scope.cadastrarUsuario(resp);
		}).catch(function(erro) {
			if (erro) {
				console.log(erro);
			}
			growl.warning('O registro NÃO foi concluído!', {title: 'Atenção!'});
			$location.path("/home");
		});
	}

	$scope.verificarEmailId = function(registroId) {
		$scope.erro = false;
		cDialogs.delayedLoading();
		//console.log('verificarIdRegistro.registroId: '+registroId);
		recursoEmail.verificarEmailId(registroId)
		.then(function(resp){
			cDialogs.hide();
			var entidade = angular.fromJson(resp.entidade);
			console.log('EmailController.verificarIdRegistro.resp.entidade: '+entidade);
			if (entidade.tipo === 'R') {
				$scope.dialogCadastrar(resp.entidade.email);
			} else if (entidade.tipo === 'S') {
				growl.error('Email de alteração de senha', {title: 'Atenção!'})
			} else {
				growl.error('Tipo de email inválido!', {title: 'Atenção!'})
			}
		}).catch(function(erro) {
			$scope.erro = true;
			cDialogs.hide();
			console.log(erro);
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};

	if ($routeParams.registroId) { 
		//console.log('routeParams.registroId: '+$routeParams.registroId);
		$scope.verificarEmailId($routeParams.registroId);
	}

	$scope.registrarEmail = function() {
		$scope.enviarEmail('R');
	}

	$scope.enviarEmail = function(tipo) {
		cDialogs.loading();
		recursoEmail.enviarEmail($scope.email, tipo)
		.then( function(resp) {
			cDialogs.hide();
			console.log(resp);
			$location.path("/home");
			cDialogs.inform('Aviso', 'Um email foi enviado para: <br/>' +
						  '<b>'+$scope.email + '</b> <br/>'+
						  'Para dar continuidade ao processo, <br/> ' +
						  'siga as instruções no email enviado. <br/>' +
						  '** VERIFIQUE A PASTA DE SPAM! **'
						  , 'OK');
		}).catch(function(erro) {
			cDialogs.hide();
			console.log(erro);
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};

})