angular.module('authInterceptor', []).config(function($httpProvider){
	var interceptor = ['$q', '$window', '$location', '$injector', function($q, $window, $location, $injector) {
		return {
			request: function (config) {
				config.headers = config.headers || {};
				if ($window.sessionStorage.token) {
					config.headers.Authorization = $window.sessionStorage.token;
				}
				return config;
			},

			requestError: function(rejection) {
				return $q.reject(rejection);
			},

			response: function (response) {
				var token = response.headers('Authorization');
				if (token != null) {
					console.log('Old token in Session: ', $window.sessionStorage.token);
					console.log('New token from Header', token);
					$window.sessionStorage.token = token;
					console.log('*** Token in Session ***  ', $window.sessionStorage.token);
				} 
				return response || $q.when(response);
			},

			// Revoke client authentication if 401 is received
			responseError: function(rejection) {
				console.log('Rejection response: '+rejection);
				if (rejection != null && rejection.status === 401) {
					if ($window.sessionStorage.token) {
						console.log('Removendo token da sessão!')
						delete $window.sessionStorage.token;
					}
					console.log('Redirecionando para login!')
					$location.path("/login");
				} else if (rejection != null && rejection.status === 403) {
					console.log('Acesso negado! Redirecionando...')
					$location.path("/negado");
				}
				return $q.reject(rejection);
			}
		};
	}];
	$httpProvider.interceptors.push(interceptor);
});