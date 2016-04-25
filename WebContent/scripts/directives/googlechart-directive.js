angular.module("google-chart", [])
	.directive("googleChart", function($window) {
		return {
			restrict: "A",
			link: function postlink(scope, elem, attr) {
				elem.desenha = function() {
					var dt = scope[attr.ngModel].dataTable;

					var options = {};
					if (scope[attr.ngModel].title)
						options.title = scope[attr.ngModel].title;
					if (scope[attr.ngModel].isStacked)
						options.isStacked = scope[attr.ngModel].isStacked;

					//elem.width($window.innerWidth*0.7);
					//elem.height($window.innerHeight*0.6);

					var googleChart = new google.visualization[attr.googleChart](elem[0]);
					googleChart.draw(dt, options);
					console.log('resizing: ' + attr.ngModel);
				}
				elem.desenha();
				angular.element($window).bind('resize', function() {
					elem.desenha();
				});
			}
		}
	});
