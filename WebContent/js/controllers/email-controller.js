angular.module('alvoApp').controller('EmailController',	function($scope, $routeParams, $location, growl, cDialogs, recursoEmail) {
	$scope.email = '';
	$scope.erro = false;
	$scope.usuarioRegistro = {
			email : undefined,
			login : '',
			senha : '',
			nome : '',
		};

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

	$scope.verificarIdRegistro = function() {
		$scope.erro = false;
		cDialogs.loading();
		$scope.usuarioRegistro.email = undefined;
		if ($routeParams.registroId) {
			//console.log('routeParams.registroId: '+$routeParams.registroId);
			recursoEmail.verificarRegistroId($routeParams.registroId)
			.then(function(resp){
				cDialogs.hide();
				var entidade = angular.fromJson(resp.entidade);
				//console.log('EmailController.verificarIdRegistro.resp.entidade: '+entidade.email);
				$scope.usuarioRegistro.email = resp.entidade.email;
				//console.log('$scope.usuarioRegistro.email '+$scope.usuarioRegistro.email);
			}).catch(function(erro) {
				$scope.erro = true;
				cDialogs.hide();
				console.log(erro);
				growl.error(erro.mensagem, {title: 'Atenção!'});
			});
		} else {
			growl.error('Codigo de confirmação de registro inválido ou não informado!', {title: 'Atenção!'});
		}
	};

	$scope.submeter = function() {
		cDialogs.loading();
		recursoEmail.cadastrarUsuario($scope.usuarioRegistro)
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

})