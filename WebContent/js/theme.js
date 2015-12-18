angular.module('alvoTheme', []).config(function($mdThemingProvider) {
  $mdThemingProvider.theme('default')
  .primaryPalette('blue', {
    'default': '500',
    'hue-1': '100',
    'hue-2': '600',
    'hue-3': '50'
  })
  .accentPalette('green', {
    'default': '500'
  });
});