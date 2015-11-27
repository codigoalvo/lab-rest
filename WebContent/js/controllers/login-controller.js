angular.module('alvoApp').controller('LoginController',	function($scope, $http, $window, $location) {
	$scope.usuario = {
		login : 'admin',
		senha : 'admin',
	};
	$scope.usuarioLogado = '';

	$scope.efetuarLogin = function() {
		$http({
			  method: 'POST',
			  data: $scope.usuario,
			  url:'ws/login',
			  headers: {'Content-Type':'application/json'}
		}).then(
			function(resp) {
				$scope.usuarioLogado = resp.data;
				console.log('usuarioLogado', resp.data);
				$window.sessionStorage.usuarioLogado = resp.data;
				$location.path("/home");
			}, function(err) {
				$scope.usuarioLogado = '';
				console.error('Error', err);
		})
	};
	
	$scope.efetuarLogout = function() {
		console.log('Removendo usuarioLogado da sessão!')
		$scope.usuarioLogado = '';
		delete $window.sessionStorage.usuarioLogado;
		console.log('Removendo token da sessão!')
		delete $window.sessionStorage.token;
	};
})