angular.module('alvoApp').factory('authInterceptor', function($q, $location) {

	var meuInterceptor = {
		responseError : function(resposta) {
			if (resposta.status == 401) {
				$location.path('/login'); // aqui o nome de uma rota para parcial de login
			}
			return $q.reject(resposta);
		}
	}

	return meuInterceptor;
});