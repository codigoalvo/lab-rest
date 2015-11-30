'use strict';
angular.module('tokenService')
	.factory('servicosToken', ['$http', '$window', function($http, $window){
        var baseUrl = "a_url_do_seu_servico";
        function changeUser(user) {
            angular.extend(currentUser, user);
        }
 
		function urlBase64Decode(str) {
			var output = str.replace('-', '+').replace('_', '/');
			switch (output.length % 4) {
			case 0:
				break;
			case 2:
				output += '==';
				break;
			case 3:
				output += '=';
				break;
			default:
				throw 'Cadeia de caracteres base64url inválida!';
			}
			return window.atob(output);
		}
		
		function getUserFromToken() {
			var token = $window.sessionStorage.token;
			var user = {};
			if (typeof token !== 'undefined') {
				var encoded = token.split('.')[1];
				user = JSON.parse(urlBase64Decode(encoded));
			}
			return user;
		}
 
        var currentUser = getUserFromToken();
 
        return {
            save: function(data, success, error) {
                $http.post(baseUrl + '/signin', data).success(success).error(error)
            },
            signin: function(data, success, error) {
                $http.post(baseUrl + '/authenticate', data).success(success).error(error)
            },
            me: function(success, error) {
                $http.get(baseUrl + '/me').success(success).error(error)
            },
            logout: function(success) {
                changeUser({});
                delete $window.sessionStorage.token;
                success();
            }
        };
    }
]);