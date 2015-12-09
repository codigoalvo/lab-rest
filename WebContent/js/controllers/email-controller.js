angular.module('alvoApp').controller('EmailController',	function($scope, $location, growl, recursoEmail) {
	$scope.email = '';

	$scope.registrarEmail = function() {
		recursoEmail.registrarEmail($scope.email)
		.then( function(resp) {
			console.log(resp);
			$location.path("/home");
			growl.success('Um email foi enviado para confirmação do email de registro');
		}).catch(function(erro) {
			console.log(erro);
			growl.error(erro.mensagem, {title: 'Atenção!'});
		});
	};

})