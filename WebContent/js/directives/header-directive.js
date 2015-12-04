angular.module('headerDirective', [])
	.directive('navheader', function (servicosLogin, $compile) {

		var templateDeslogado = "<li ng-show='!usuarioLogado'><a href='#login'><span class='glyphicon glyphicon-log-in'></span> login </a></li>";
		var templateLogado =	"<li ng-show='usuarioLogado' class='dropdown'>" +
									"<a href='' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'>" +
										"<span class='glyphicon glyphicon-user'></span> {{usuarioLogado.login}} <span class='caret'></span>" +
									"</a>" +
									"<ul class='dropdown-menu'>" +
										"<li><a href='#senha'><span class='glyphicon glyphicon-edit'></span> senha </a></li>" +
										"<li role='separator' class='divider'></li>" +
										"<li><a href='#logout' ng-click='efetuarLogout()'><span class='glyphicon glyphicon-log-out'></span> logout </a></li>" +
									"</ul>" +
								"</li>";

		var templateAbre =	"<nav class='navbar navbar-default navbar-fixed-top'>" +
								"<div class='container'>" +
									"<div class='navbar-header'>" +
										"<button type='button' class='navbar-toggle collapsed' data-toggle='collapse' data-target='#navbar' aria-expanded='false' aria-controls='navbar'>" +
											"<span class='sr-only'>Toggle navigation</span>" +
											"<span class='icon-bar'></span>" +
											"<span class='icon-bar'></span>" +
											"<span class='icon-bar'></span>" +
										"</button>"+
										"<a class='navbar-brand' href='#home'>" +
											"<img src='images/codigoalvo.svg' alt='codigoalvo' height='23px'/>" +
										"</a>" +
									"</div>";

		var templateFecha =	"</ul>" +
						"</div>" +
					"</div>" +
				"</nav>";

		var templateOpcoes =	"<div id='navbar' class='navbar-collapse collapse'>" +
									"<ul class='nav navbar-nav'>" +
										"<li><a href='#home'><span class='glyphicon glyphicon-home' /></a></li>" +
										"<li><a href='#resumo'>Resumo</a></li>" +
										"<li><a href='ws/categorias/1'>Categoria (1) - REST</a></li>" +
										"<li><a href='ws/categorias'>Categorias - REST</a></li>" +
										"<li><a href='#categorias'>Categorias - Angular</a></li>";

		var opcoesAdmin =	"<li ng-show='usuarioLogado' class='dropdown'>" +
								"<a href='' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'>" +
									"</span> Administração <span class='caret'></span>" +
								"</a>" +
								"<ul class='dropdown-menu'>" +
									"<li><a href='#usuarios'></span> Usuários </a></li>" +
								"</ul>" +
							"</li>";

		var opcoesFecha = "</ul>" +"<ul class='nav navbar-nav navbar-right'>";

		var templateHtml = "";

		var ddo = {};
			ddo.restrict = 'A';

			ddo.replace = true;
			ddo.template = "";
			ddo.scope = {
				templateMenuLogin: '=templateMenuLogin',
				usuarioLogado: '=usuarioLogado',
				usuarioAdmin: '=usuarioAdmin',
			}

			ddo.compile = function(element, attr) {
				return function($scope, element, attr) {
					templateHtml = "";
					templateHtml += templateAbre;
					templateHtml += templateOpcoes;
					if ($scope.usuarioAdmin) {
						templateHtml += opcoesAdmin;
					}
					templateHtml += opcoesFecha;
					if ($scope.usuarioLogado) {
						templateHtml +=	templateLogado;
					} else {
						templateHtml += templateDeslogado;
					}
					templateHtml +=	templateFecha;
					var	htmlElement = angular.element(templateHtml);
					element.html(htmlElement);
					$compile(htmlElement)($scope);
				};
			};

			ddo.controller = ['$scope', '$window', function ($scope, $window) {
				var usuarioLogado = servicosLogin.pegarUsuarioDoToken();
				if (usuarioLogado) {
					$scope.usuarioLogado = usuarioLogado;
					if ($scope.usuarioLogado.tipo === 'ADMIN') {
						$scope.usuarioAdmin = true;
					} else {
						$scope.usuarioAdmin = false;
					}
					//console.log('header.usuarioLogado', $scope.usuarioLogado);
				} else {
					$scope.usuarioLogado = undefined;
					$scope.usuarioAdmin = false;
				}
				$scope.efetuarLogout = function() {
					servicosLogin.efetuarLogout();
				}
			}];
		return ddo;
	})
;