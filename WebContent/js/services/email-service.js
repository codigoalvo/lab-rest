angular.module('emailService', [])
	.factory('recursoEmail', function($http, $window, $q) {
		var service = {};
		service.registrarEmail = function(email) {
			return $q(function(resolve, reject) {
			$http({
				  method: 'POST',
				  data: email,
				  url:'ws/email/registrar',
				  headers: {'Content-Type':'text/plain'}
			}).then(
				function(resp) {
					//console.log('resp: '+resp);
					resolve(resp.data);
				}, function(erro) {
					//console.error('Error', erro);
					reject(erro.data);
				})
			});
		};

		return service; 
	});