angular.module('alvoApp', ['ngRoute', 'ngResource'])
	.config(function($routeProvider, $httpProvider, $locationProvider) {

		//$locationProvider.html5Mode(true);
		//$httpProvider.interceptors.push('AuthInterceptor');

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
					}
					return $q.reject(rejection);
				}
			};
		}];
		$httpProvider.interceptors.push(interceptor);


		$routeProvider.when('/categorias', {
			templateUrl: 'partials/categorias.html',
			controller: 'CategoriasController'
		});
		
		$routeProvider.when('/login', {
			templateUrl: 'partials/login.html',
			controller: 'LoginController'
		});
		
		$routeProvider.when('/home', {
			templateUrl: 'partials/home.html'
		});
		
		$routeProvider.otherwise({redirectTo: '/home'});

	});