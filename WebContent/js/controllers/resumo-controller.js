angular.module('alvoApp').controller('ResumoController',
	function($scope) {
		$scope.data1 = {};
		$scope.data1.dataTable = new google.visualization.DataTable();
		$scope.data1.dataTable.addColumn("string","Mês");
		$scope.data1.dataTable.addColumn("number","Alimentação");
		$scope.data1.dataTable.addColumn("number","Transporte");
		$scope.data1.dataTable.addColumn("number","Saúde");
		$scope.data1.dataTable.addRow(["Outubro",10, 13, 15]);
		$scope.data1.dataTable.addRow(["Novembro",11, 8, 14]);
		$scope.data1.dataTable.addRow(["Dezembro",7, 16, 12]);
		$scope.data1.title="Resumo";
		$scope.data1.isStacked=true;;

		$scope.data2 = {};
		$scope.data2.dataTable = new google.visualization.DataTable();
		$scope.data2.dataTable.addColumn("string","Mês");
		$scope.data2.dataTable.addColumn("number","Gasto");
		$scope.data2.dataTable.addRow(["Setembro",24]);
		$scope.data2.dataTable.addRow(["Outubro",35]);
		$scope.data2.dataTable.addRow(["Novembro",30]);
		$scope.data2.dataTable.addRow(["Dezembro",51]);
	}
);