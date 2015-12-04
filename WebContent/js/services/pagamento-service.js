angular.module('pagamentoService', ['ngResource'])
	.factory('recursoPagamento', function($resource) {
		return $resource('ws/pagamentos/:pagamentoId', {},
			{
				'update' : {
					method: 'PUT'
				},
				'remove' : {
					method: 'DELETE' 
				}
			})
	})
	.factory("cadastroPagamento", function(recursoPagamento, $http, $q) {
		var service = {};
		service.gravar = function(pagamento) {
			return $q(function(resolve, reject) {
				if(pagamento.id) {
					recursoPagamento.update({pagamentoId: pagamento.id}, pagamento, function() {
						resolve({
							mensagem: 'Pagamento ' + pagamento.nome + ' atualizado com sucesso',
							inclusao: false
						});
					}, function(erro) {
						console.log(erro);
						reject({
							mensagem: erro.data.mensagem,
						});
					});

				} else {
					recursoPagamento.save(pagamento, function() {
						resolve({
							mensagem: 'Pagamento ' + pagamento.nome + ' incluído com sucesso',
							inclusao: true
						});
					}, function(erro) {
						console.log(erro);
						reject({
							mensagem: erro.data.mensagem,
						});
					});
				}
			},
			{
			    stripTrailingSlashes: false
			});
		};
		service.tipos = function() {
			return $q(function(resolve, reject) {
				$http({
					  method: 'GET',
					  url:'ws/pagamentos/tipos',
					  headers: {'Content-Type':'application/json'}
				}).then(
					function(resp) {
						var tipos = resp.data;
						resolve(tipos);
					}, function(erro) {
						console.error('Error', erro);
						reject({
							mensagem: erro.data.mensagem,
						});
					})
				});
			};
		return service;
	});