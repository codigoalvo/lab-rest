angular.module('alvoApp',	['ngRoute', 'ngResource', 'ngAnimate', 'angular-growl', 'dialogs.main',
							'ui.bootstrap', 'pascalprecht.translate','dialogs.default-translations',
							'alvoRotas', 'authInterceptor', 'miscDirectives', 'headerDirective', 
							'loginService', 'usuarioService', 'categoriaService'])
	.config(function($httpProvider, $locationProvider, growlProvider) {
		//$locationProvider.html5Mode(true);
		//$httpProvider.interceptors.push('AuthInterceptor');
		growlProvider.globalTimeToLive({success: 3000, error: 5000, warning: 4000, info: 3000});
		growlProvider.globalPosition('bottom-right');
	}
);