angular.module('alvoApp').controller('LoginController',	function($scope, $http, $window) {
	$scope.usuario = {
		login : 'admin',
		senha : 'admin',
	};
	$scope.usuarioLogado = '';
	$scope.submit = function() {
		$http({
			  method: 'POST',
			  data: $scope.usuario,
			  url:'ws/login',
			  headers: {'Content-Type':'application/json'}
		}).then(
			function(resp) {
				$scope.usuarioLogado = resp.data;
				console.log('idUsuario', resp.data.id);
				console.log('Success', resp);
				
				$window.sessionStorage.token = resp.data.token;
				console.log('Token in Session: ', $window.sessionStorage.token);
			}, function(err) {
				$scope.usuarioLogado = err;
				console.error('Error', err);
		})
	};
})