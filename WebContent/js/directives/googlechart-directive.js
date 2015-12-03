angular.module("google-chart",[])
	.directive("googleChart",function(){  
		return{
			restrict : "A",
			link: function($scope, $elem, $attr){
				var dt = $scope[$attr.ngModel].dataTable;

				var options = {};
				if($scope[$attr.ngModel].title)
					options.title = $scope[$attr.ngModel].title;

				var googleChart = new google.visualization[$attr.googleChart]($elem[0]);
				googleChart.draw(dt,options)
			}
		}
});