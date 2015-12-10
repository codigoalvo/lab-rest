angular.module('alvoApp').controller('EmailController',	function($scope, $routeParams, $location, growl, dialogs, recursoEmail) {
	$scope.email = '';
	$scope.erro = false;
	$scope.usuarioRegistro = {
			email : undefined,
			login : '',
			senha : '',
			nome : '',
		};

	$scope.registrarEmail = function() {
		var dlg = dialogs.create('dialogs/aguarde.html','', '' ,{'size':'sm', 'keyboard':false , 'backdrop':'static'});
		recursoEmail.registrarEmail($scope.email)
		.then( function(resp) {
			dlg.dismiss('Dismiss');
			console.log(resp);
			$location.path("/home");
			dialogs.notify('Aviso', 'Um email foi enviado para: <br/>' +
						  '<b>'+$scope.email + '</b> <br/>'+
						  'Para dar continuidade ao registro, <br/> ' +
						  'siga as instruções no email enviado. <br/>' +
						  '** VERIFIQUE A PASTA DE SPAM! **'
						  , {'size':'sm'});
		}).catch(function(erro) {
			dlg.dismiss('Dismiss');
			console.log(erro);
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};

	$scope.verificarIdRegistro = function() {
		$scope.erro = false;
		var dlg = dialogs.create('dialogs/aguarde.html','', '' ,{'size':'sm', 'keyboard':false , 'backdrop':'static'});
		$scope.usuarioRegistro.email = undefined;
		if ($routeParams.registroId) {
			//console.log('routeParams.registroId: '+$routeParams.registroId);
			recursoEmail.verificarRegistroId($routeParams.registroId)
			.then(function(resp){
				dlg.dismiss('Dismiss');
				var entidade = angular.fromJson(resp.entidade);
				//console.log('EmailController.verificarIdRegistro.resp.entidade: '+entidade.email);
				$scope.usuarioRegistro.email = resp.entidade.email;
				//console.log('$scope.usuarioRegistro.email '+$scope.usuarioRegistro.email);
			}).catch(function(erro) {
				$scope.erro = true;
				dlg.dismiss('Dismiss');
				console.log(erro);
				growl.error(erro.mensagem, {title: 'Atenção!'});
			});
		} else {
			growl.error('Codigo de confirmação de registro inválido ou não informado!', {title: 'Atenção!'});
		}
	};

	$scope.submeter = function() {
		var dlg = dialogs.create('dialogs/aguarde.html','', '' ,{'size':'sm', 'keyboard':false , 'backdrop':'static'});
		recursoEmail.cadastrarUsuario($scope.usuarioRegistro)
		.then(function(resp) {
			dlg.dismiss('Dismiss');
			$location.path("/login");
			growl.success('Registro de novo usuário confirmado com sucesso!');
		})
		.catch(function(erro) {
			dlg.dismiss('Dismiss');
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};

})