angular.module('alvoApp', [])
.controller('CategoriasController', function($scope, $http) {
	$http.get('ws/categoria/list').then(function(resp) {
		$scope.categorias = resp.data;
		console.log('Success', resp);
		// For JSON responses, resp.data contains the result
	}, function(err) {
		console.error('ERR', err);
		// err.status will contain the status code
	})
})