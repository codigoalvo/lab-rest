angular.module('alvoTheme', []).config(function($mdThemingProvider) {

	var alvoBlue = $mdThemingProvider.extendPalette('blue', {
		'200': '3AC2CF',
		'500': '0099CC'
	});
	$mdThemingProvider.definePalette('codigoalvoBluePalette', alvoBlue);

	var alvoGreen = $mdThemingProvider.extendPalette('green', {
		'300': '40FF40',
		'500': '00CC00'
	});
	$mdThemingProvider.definePalette('codigoalvoGreenPalette', alvoGreen);

  $mdThemingProvider.theme('default')
		.primaryPalette('codigoalvoBluePalette', {
			'default': '500',
			'hue-1': '200'
		})
		.accentPalette('codigoalvoGreenPalette', {
			'default': '500',
			'hue-1': '300'
		});
});