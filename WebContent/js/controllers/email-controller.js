angular.module('alvoApp').controller('EmailController',	function($scope, $routeParams, $location, growl, cDialogs, recursoEmail) {
	$scope.email = '';
	$scope.erro = false;
	$scope.usuarioRegistro = {
			email : undefined,
			login : '',
			senha : '',
			nome : '',
		};

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

	$scope.dialogCadastrar = function() {
		console.log('dialogCadastrar');
		var locals = {
			usuarioRegistro : angular.copy($scope.usuarioRegistro),
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

	$scope.verificarIdRegistro = function(registroId) {
		$scope.erro = false;
		cDialogs.delayedLoading();
		$scope.usuarioRegistro.email = undefined;
		//console.log('verificarIdRegistro.registroId: '+registroId);
		recursoEmail.verificarRegistroId(registroId)
		.then(function(resp){
			cDialogs.hide();
			var entidade = angular.fromJson(resp.entidade);
			console.log('EmailController.verificarIdRegistro.resp.entidade: '+entidade.email);
			$scope.usuarioRegistro.email = resp.entidade.email;
			console.log('$scope.usuarioRegistro.email '+$scope.usuarioRegistro.email);
			$scope.dialogCadastrar();
		}).catch(function(erro) {
			$scope.erro = true;
			cDialogs.hide();
			console.log(erro);
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};

	if ($routeParams.registroId) { 
		//console.log('routeParams.registroId: '+$routeParams.registroId);
		$scope.verificarIdRegistro($routeParams.registroId);
	}

	$scope.registrarEmail = function() {
		cDialogs.loading();
		recursoEmail.registrarEmail($scope.email)
		.then( function(resp) {
			cDialogs.hide();
			console.log(resp);
			$location.path("/home");
			cDialogs.inform('Aviso', 'Um email foi enviado para: <br/>' +
						  '<b>'+$scope.email + '</b> <br/>'+
						  'Para dar continuidade ao registro, <br/> ' +
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