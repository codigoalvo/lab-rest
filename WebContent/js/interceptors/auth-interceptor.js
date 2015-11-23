angular.module('alvoApp').factory('AuthInterceptor',function($q, $location, $window, $injector) {
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
			return response || $q.when(response);
		},

		// Revoke client authentication if 401 is received

		responseError: function(rejection) {
			console.log(rejection);
			// Dynamically get the service since they can't be injected into config
			var AuthenticationService = $injector.get('AuthenticationService');

			if (rejection != null && rejection.status === 401 && ($window.sessionStorage.token || AuthenticationService.isLogged)) {
				delete $window.sessionStorage.token;
				AuthenticationService.isLogged = false;
				$location.path("/login");
			}

			return $q.reject(rejection);
		}
	};
});