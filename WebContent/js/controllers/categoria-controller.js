angular.module('alvoApp')
.controller('CategoriaController', function($scope, recursoCategoria) {
	$scope.categorias = [];

	recursoCategoria.query(function(categorias) {
		$scope.categorias = categorias;
	}, function(erro) {
		console.log(erro);
	});
});