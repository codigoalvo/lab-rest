angular.module('alvoApp', ['ngRoute', 'ngResource', 'ngAnimate', 'angular-growl', 'alvoRotas', 'authInterceptor', 'miscDirectives', 'headerDirective', 'loginService', 'usuarioService', 'categoriaService'])
	.config(function($httpProvider, $locationProvider, growlProvider) {
		//$locationProvider.html5Mode(true);
		//$httpProvider.interceptors.push('AuthInterceptor');
		growlProvider.globalTimeToLive({success: 1000, error: 2000, warning: 3000, info: 4000});
		growlProvider.globalPosition('bottom-right');
	}
);