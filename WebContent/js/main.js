angular.module('alvoApp', ['ngRoute', 'ngResource', 'alvoRotas', 'authInterceptor', 'headerDirective', 'categoriaService', 'loginService'])
	.config(function($httpProvider, $locationProvider) {
		//$locationProvider.html5Mode(true);
		//$httpProvider.interceptors.push('AuthInterceptor');
	});