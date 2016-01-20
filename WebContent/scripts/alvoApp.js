google.load('visualization', '1', {packages: ['corechart']});

angular.module('alvoApp',	['ngRoute', 'ngResource', 'ngAnimate', 'ngMaterial', 'ngSanitize',
 							'angular-growl', 'google-chart', 'xeditable', 'alvoTheme',
							'alvoRotas', 'authInterceptor', 'miscDirectives', 'headerDirective', 'dialogsService',
							'loginService', 'usuarioService', 'contaService', 'categoriaService', 'planejamentoService',
							'transacaoService', 'emailService', 'senhaService'])
	.config(function($httpProvider, $locationProvider, growlProvider) {
		//$locationProvider.html5Mode(true);
		//$httpProvider.interceptors.push('AuthInterceptor');
		growlProvider.globalTimeToLive({success: 3000, error: 5000, warning: 4000, info: 3000});
		growlProvider.globalPosition('bottom-right');
	}).run(function(editableOptions, editableThemes, $rootScope, $templateCache) {
		
		$rootScope.$on('$viewContentLoaded', function() {
			$templateCache.removeAll();
		});

		//editableOptions.theme = 'bs3'; // xeditable - bootstrap3 theme. Can be also 'bs2', 'default'
		editableOptions.theme = 'default'; // set `default` theme
		editableThemes['default'].formTpl = '<form class="editable-wrap" name="rowform"></form>';
		editableThemes['default'].submitTpl = '<button md-button type="submit" class="md-raised md-button-icon" ng-disabled="rowform.$invalid"><md-icon>save</md-icon></button>';
		editableThemes['default'].cancelTpl = '<button md-button type="button" class="md-raised md-button-icon" ng-click="$form.$cancel()"><md-icon>reply</md-icon></button>';

	});