angular.module('planejamentoService', ['ngResource'])
	.factory('recursoPlanejamento', function($resource) {
		return $resource('ws/usuarios/:usuarioId/planejamentos/:planejamentoId', {}, {
			'update': {
				method: 'PUT'
			},
			'remove': {
				method: 'DELETE'
			}
		})
	})
	.factory("cadastroPlanejamento", function(recursoPlanejamento, $q) {
		var service = {};
		service.gravar = function(usuarioId, planejamento) {
			return $q(function(resolve, reject) {
				//console.log('cadastroPlanejamento.planejamento.id: '+planejamento.id);
				//console.log('cadastroPlanejamento.planejamento.nome: '+planejamento.nome);
				//console.log('cadastroPlanejamento.usuarioId: ' + usuarioId)
				if (planejamento.id) {
					recursoPlanejamento.update({ usuarioId: usuarioId, planejamentoId: planejamento.id }, planejamento, function(resp) {
						resolve({
							mensagem: resp.mensagem,
							inclusao: false
						});
					}, function(erro) {
						console.log(erro);
						reject({
							mensagem: erro.data.mensagem,
						});
					});

				} else {
					recursoPlanejamento.save({ usuarioId: usuarioId }, planejamento, function(resp) {
						resolve({
							mensagem: resp.mensagem,
							inclusao: true
						});
					}, function(erro) {
						console.log(erro);
						reject({
							mensagem: erro.data.mensagem,
						});
					});
				}
			}, {
				stripTrailingSlashes: false
			});
		};
		return service;
	});
