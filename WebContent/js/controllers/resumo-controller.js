angular.module('alvoApp').controller('ResumoController',
	function($scope) {
		$scope.data1 = {};
		$scope.data1.dataTable = new google.visualization.DataTable();
		$scope.data1.dataTable.addColumn("string","Name")
		$scope.data1.dataTable.addColumn("number","Qty")
		$scope.data1.dataTable.addRow(["Test",1]);
		$scope.data1.dataTable.addRow(["Test2",2]);
		$scope.data1.dataTable.addRow(["Test3",3]);
		$scope.data1.title="My Pie"
		
		$scope.data2 = {};
		$scope.data2.dataTable = new google.visualization.DataTable();
		$scope.data2.dataTable.addColumn("string","Name")
		$scope.data2.dataTable.addColumn("number","Qty")
		$scope.data2.dataTable.addRow(["Test",1]);
		$scope.data2.dataTable.addRow(["Test2",2]);
		$scope.data2.dataTable.addRow(["Test3",3]);
		
		
		$scope.data3 = {};
		$scope.data3.dataTable = new google.visualization.DataTable();
		$scope.data3.dataTable.addColumn("string","Name")
		$scope.data3.dataTable.addColumn("number","Qty")
		$scope.data3.dataTable.addRow(["Test",1]);
		$scope.data3.dataTable.addRow(["Test2",2]);
		$scope.data3.dataTable.addRow(["Test3",3]);
	}
);