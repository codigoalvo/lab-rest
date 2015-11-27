angular.module('minhasDiretivas', [])
	.directive('header', function (servicosLogin) {
		return {
			restrict: 'A',
			replace: true,
			scope: {user: '='},
			templateUrl: "js/directives/header.html",
			controller: ['$scope', '$filter', function ($scope, $filter) {
				// behaviour goes here
				if (servicosLogin.getUsuarioLogado()) {
					console.log('servicosLogin.getUsuarioLogado()', servicosLogin.getUsuarioLogado());
					$scope.usuarioLogado = servicosLogin.getUsuarioLogado();
					$scope.login = servicosLogin.getUsuarioLogado().login;
					console.log('login', $scope.login);
				}
			}]
		}
	});