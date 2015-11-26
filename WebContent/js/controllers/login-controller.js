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
				console.log('usuarioLogado', resp.data);
				$window.sessionStorage.usuarioLogado = resp.data;
			}, function(err) {
				$scope.usuarioLogado = err;
				console.error('Error', err);
		})
	};
})