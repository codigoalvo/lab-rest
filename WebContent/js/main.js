google.load('visualization', '1', {packages: ['corechart']});
angular.module('alvoApp',	['ngRoute', 'ngResource', 'ngAnimate', 'ngMaterial', 'ngSanitize',
 							'angular-growl', 'google-chart', 'xeditable',
							'alvoRotas', 'authInterceptor', 'miscDirectives', 'headerDirective', 'dialogsService',
							'loginService', 'usuarioService', 'categoriaService', 'contaService', 'emailService'])
	.config(function($httpProvider, $locationProvider, growlProvider) {
		//$locationProvider.html5Mode(true);
		//$httpProvider.interceptors.push('AuthInterceptor');
		growlProvider.globalTimeToLive({success: 3000, error: 5000, warning: 4000, info: 3000});
		growlProvider.globalPosition('bottom-right');
	}).run(function(editableOptions) {
		editableOptions.theme = 'bs3'; // xeditable - bootstrap3 theme. Can be also 'bs2', 'default'
	});
