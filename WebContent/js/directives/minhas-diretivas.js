angular.module('minhasDiretivas', [])
	.directive('header', function (servicosLogin) {
		return {
			restrict: 'A',
			replace: true,
			scope: {user: '='},
			templateUrl: "js/directives/header.html",
			controller: ['$scope', '$window' , function ($scope, $window) {
				var token = $window.sessionStorage.token;
				console.log('header.token', token);
				var decodedToken = servicosLogin.pegarUsuarioDoToken();
				console.log('header.decodedToken', decodedToken);
				if (decodedToken  &&  decodedToken.usuario) {
					$scope.usuarioLogado = angular.fromJson(decodedToken.usuario);
					console.log('header.usuarioLogado', $scope.usuarioLogado);
				} else {
					$scope.usuarioLogado = undefined;
				}
				$scope.efetuarLogout = function() {
					servicosLogin.efetuarLogout();
				}
			}]
		}
	}
);