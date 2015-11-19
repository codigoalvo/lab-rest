angular.module('alvoApp').controller('LoginController',	function($scope, $http) {
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
			}, function(err) {
				$scope.usuarioLogado = err;
				console.error('Error', err);
		})
	};
})