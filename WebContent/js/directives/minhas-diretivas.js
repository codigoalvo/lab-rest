angular.module('minhasDiretivas', [])
	.directive('header', function (servicosLogin) {
		
		var templateDeslogado = "<li ng-show='!usuarioLogado'><a href='#login'><span class='glyphicon glyphicon-log-in'></span> login </a></li>";
		var templateLogado =	"<li ng-show='usuarioLogado' class='dropdown'>" +
									"<a href='' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'>" +
										"<span class='glyphicon glyphicon-user'></span> {{usuarioLogado.login}} <span class='caret'></span>" +
									"</a>" +
									"<ul class='dropdown-menu'>" +
										"<li><a href='#perfil'><span class='glyphicon glyphicon-edit'></span> perfil </a></li>" +
										"<li role='separator' class='divider'></li>" +
										"<li><a href='#logout' ng-click='efetuarLogout()'><span class='glyphicon glyphicon-log-out'></span> logout </a></li>" +
									"</ul>" +
								"</li>";

		var templateHtml = "<nav class='navbar navbar-default navbar-fixed-top'>" +
								"<div class='container'>" +
									"<div class='navbar-header'>" +
										"<a class='navbar-brand' href='#home'>" +
											"<img src='images/codigoalvo.svg' alt='codigoalvo' height='23px'/>" +
										"</a>" +
									"</div>" +
									"<div id='navbar' class='navbar-collapse collapse'>" +
										"<ul class='nav navbar-nav'>" +
											"<li><a href='#home'><span class='glyphicon glyphicon-home' /></a></li>" +
											"<li><a href='ws/categorias/1'>Categoria (1) - REST</a></li>" +
											"<li><a href='ws/categorias'>Categorias - REST</a></li>" +
											"<li><a href='#categorias'>Categorias - Angular</a></li>" +
										"</ul>" +
										"<ul class='nav navbar-nav navbar-right'>"+ /* ng-bind-html='templateMenuLogin'>" + */
											templateDeslogado +
											templateLogado +
										"</ul>" +
									"</div>" +
								"</div>" +
							"</nav>";

		var ddo = {};
			ddo.restrict = 'A';

			ddo.replace = true;
			ddo.template = templateHtml;
			ddo.scope = {
				templateMenuLogin: '=templateMenuLogin',
			}

			/*
			ddo.compile = function(element, attr) {
				// manipulating template?

				var	e = angular.element(templateMenuLogin);
				element.append(e);

				// the following is your linking function
				return function($scope, element, attr) {
					// template contains binding expressions? Yes
					$compile(e)($scope);
				};
			};
			*/
			

			ddo.controller = ['$scope', '$window', function ($scope, $window) {
				var token = $window.sessionStorage.token;
				console.log('header.token', token);
				var decodedToken = servicosLogin.pegarUsuarioDoToken();
				console.log('header.decodedToken', decodedToken);
				$scope.templateMenuLogin = "";
				if (decodedToken  &&  decodedToken.usuario) {
					$scope.usuarioLogado = angular.fromJson(decodedToken.usuario);
					$scope.myUser = $scope.usuarioLogado.login;
					console.log('header.usuarioLogado', $scope.usuarioLogado);
					/*$scope.templateMenuLogin = templateLogado;*/
				} else {
					$scope.usuarioLogado = undefined;
					$scope.myUser = '';
					/*$scope.templateMenuLogin = templateDeslogado;*/
				}
				$scope.efetuarLogout = function() {
					servicosLogin.efetuarLogout();
				}
			}];
		return ddo;
	}
);