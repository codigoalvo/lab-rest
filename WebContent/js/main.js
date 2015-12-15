google.load('visualization', '1', {packages: ['corechart']});
angular.module('alvoApp',	['ngRoute', 'ngResource', 'ngAnimate', 'ngMaterial', 'ngSanitize',
 							'angular-growl', 'google-chart',
							'alvoRotas', 'authInterceptor', 'miscDirectives', 'headerDirective', 'dialogsService',
							'loginService', 'usuarioService', 'categoriaService', 'contaService', 'emailService'])
	.config(function($httpProvider, $locationProvider, growlProvider) {
		//$locationProvider.html5Mode(true);
		//$httpProvider.interceptors.push('AuthInterceptor');
		growlProvider.globalTimeToLive({success: 3000, error: 5000, warning: 4000, info: 3000});
		growlProvider.globalPosition('bottom-right');
	}
);