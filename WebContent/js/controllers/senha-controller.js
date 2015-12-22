angular.module('alvoApp').controller('SenhaController',
	function($scope, $location, growl, recursoSenha, cDialogs) {

		$scope.alterarSenha = function(senhas) {
			if (senhas.atual === undefined  ||
					senhas.atual === null  ||
					senhas.atual === '') {
				growl.error('Senha atual deve ser informada!');
			}	else if (senhas.nova === undefined  ||
					senhas.nova === null  ||
					senhas.nova === '' ||	
					senhas.nova !== senhas.confirma) {
				growl.error('Nova senha e a confirmação da senha devem ser informadas e devem ser iguais!');
			} else {
				cDialogs.loading();
				recursoSenha.alterarSenha(senhas)
				.then( function(resp) {
					cDialogs.hide();
					$scope.usuarioLogado = resp;
					$location.path("/home");
					growl.success('Senha alterada com sucesso!');
				}).catch(function(erro) {
					cDialogs.hide();
					console.log(erro);
					growl.error(erro.mensagem, {title: 'Atenção!'});
				});
			}
		};

		$scope.dialogSenha = function() {
			console.log('dialogSenha');
			var locals = {
				informarAtual : true,
			}
			cDialogs.custom('dialogs/senha.html', locals).then(function(resp){
				console.log('dialogSenha.resp.senhas: '+angular.toJson(resp.senhas));
				$scope.alterarSenha(resp.senhas);
			}).catch(function(erro) {
				if (erro) {
					console.log(erro);
				}
				growl.warning('A operação NÃO foi concluída!', {title: 'Atenção!'});
				$location.path("/home");
			});
		}

		//Executa dialog senha na inicialização do controller
		$scope.dialogSenha();

});