angular.module('miscDirectives', [])
	.directive('focus', function($timeout) {
		return {
			scope: {
				trigger: '@focus'
			},
			link: function(scope, element) {
				scope.$watch('trigger', function(value) {
					if (value === "true") {
						$timeout(function() {
							element[0].focus();
						});
					}
				});
			}
		};
	})
	.directive('equals', function() {
		return {
			restrict: 'A',
			require: '?ngModel',
			link: function(scope, elem, attrs, ngModel) {
				if (!ngModel) return;
				scope.$watch(attrs.ngModel, function() {
					validate();
				});
				attrs.$observe('equals', function(val) {
					validate();
				});
				var validate = function() {
					var val1 = ngModel.$viewValue;
					var val2 = attrs.equals;
					ngModel.$setValidity('equals', !val1 || !val2 || val1 === val2);
				};
			}
		}
	});
