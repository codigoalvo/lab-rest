angular.module('contaService', ['ngResource'])
	.factory('recursoConta', function($resource) {
		return $resource('ws/usuarios/:usuarioId/contas/:contaId', {},
			{
				'update' : {
					method: 'PUT'
				},
				'remove' : {
					method: 'DELETE' 
				}
			})
	})
	.factory("cadastroConta", function(recursoConta, $http, $q) {
		var service = {};
		service.gravar = function(usuarioId, conta) {
			return $q(function(resolve, reject) {
				if(conta.id) {
					recursoConta.update({usuarioId: usuarioId, contaId: conta.id}, conta, function() {
						resolve({
							mensagem: 'Conta ' + conta.nome + ' atualizada com sucesso',
							inclusao: false
						});
					}, function(erro) {
						console.log(erro);
						reject({
							mensagem: erro.data.mensagem,
						});
					});

				} else {
					recursoConta.save({usuarioId: usuarioId}, conta, function() {
						resolve({
							mensagem: 'Conta ' + conta.nome + ' incluída com sucesso',
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
					  url:'ws/usuarios/:usuarioId/contas/tipos',
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