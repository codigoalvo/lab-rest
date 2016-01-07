angular.module('transacaoService', ['ngResource'])
	.factory('recursoTransacao', function($resource) {
		return $resource('ws/usuarios/:usuarioId/transacoes/:transacaoId', {},
			{
				'update' : {
					method: 'PUT'
				},
				'remove' : {
					method: 'DELETE' 
				}
			})
	})
	.factory("cadastroTransacao", function(recursoTransacao, $q) {
		var service = {};
		service.gravar = function(usuarioId, transacao) {
			return $q(function(resolve, reject) {
				//console.log('cadastroTransacao.transacao.id: '+transacao.id);
				//console.log('cadastroTransacao.transacao.nome: '+transacao.nome);
				//console.log('cadastroTransacao.usuarioId: ' + usuarioId)
				if(transacao.id) {
					recursoTransacao.update({usuarioId: usuarioId, transacaoId: transacao.id}, transacao, function(resp) {
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
					recursoTransacao.save({usuarioId: usuarioId}, transacao, function(resp) {
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
			},
			{
			    stripTrailingSlashes: false
			});
		};
		return service;
	});