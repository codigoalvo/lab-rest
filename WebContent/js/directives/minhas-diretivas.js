angular.module('minhasDiretivas', [])
	.directive('header', function () {
		return {
			restrict: 'A',
			replace: true,
			scope: {user: '='},
			templateUrl: "js/directives/header.html",
			controller: ['$scope', '$filter', function ($scope, $filter) {
				// behaviour goes here
			}]
		}
	});