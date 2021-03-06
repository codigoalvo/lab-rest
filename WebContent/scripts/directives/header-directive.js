angular.module('headerDirective', [])
	.directive('bsNavHeader', function (servicosLogin, $compile) {

		var templateDeslogado = "<li ng-show='!usuarioLogado'><a href='#login'><span class='glyphicon glyphicon-log-in'></span> entrar </a></li>"+
								"<li ng-show='!usuarioLogado'><a href='#registro'><span class='glyphicon glyphicon-check'></span> registrar </a></li>";

		var templateLogado =	"<li ng-show='usuarioLogado' class='dropdown'>" +
									"<a href='' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'>" +
										"<span class='glyphicon glyphicon-user'></span> {{usuarioLogado.apelido}} <span class='caret'></span>" +
									"</a>" +
									"<ul class='dropdown-menu'>" +
										"<li><a href='#senha/alterar'><span class='glyphicon glyphicon-edit'></span> senha </a></li>" +
										"<li role='separator' class='divider'></li>" +
										"<li><a href='#logout' ng-click='efetuarLogout()'><span class='glyphicon glyphicon-log-out'></span> logout </a></li>" +
									"</ul>" +
								"</li>";

		var templateAbre =	"<nav class='bs-nav-header navbar navbar-default navbar-fixed-top'>" +
								"<div class='container'>" +
									"<div class='navbar-header'>" +
										"<button type='button' class='navbar-toggle collapsed' data-toggle='collapse' data-target='#navbar' aria-expanded='false' aria-controls='navbar'>" +
											"<span class='sr-only'>Toggle navigation</span>" +
											"<span class='icon-bar'></span>" +
											"<span class='icon-bar'></span>" +
											"<span class='icon-bar'></span>" +
										"</button>"+
										"<a class='navbar-brand' href='http://www.codigoalvo.com.br/' target='blank'>" +
											"<img src='images/codigoalvo.svg' alt='codigoalvo' height='23px'/>" +
										"</a>" +
									"</div>" +
									"<div id='navbar' class='navbar-collapse collapse'>" +
										"<ul class='nav navbar-nav'>";

		var templateFecha =	"</ul>" +
						"</div>" +
					"</div>" +
				"</nav>";

		var templateHome = "<li><a href='#home'><span class='glyphicon glyphicon-home'></span></a></li>";

		var templateResumo = "<li><a href='#resumo'>Resumo</a></li>";
		var templateExtrato = "<li><a href='#extrato'>Extrato</a></li>";
		var templatePlanejamento = "<li><a href='#planejamento'>Planejamento</a></li>";

		var menuCadastros =	"<li ng-show='usuarioLogado' class='dropdown'>" +
								"<a href='' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'>" +
									"Cadastros <span class='caret'></span>" +
								"</a>" +
								"<ul class='dropdown-menu'>" +
									"<li><a href='#categorias'>Categorias</a></li>" +
									"<li><a href='#contas'>Contas</a></li>" +
								"</ul>" +
							"</li>";

		var menuAdmin =	"<li ng-show='usuarioLogado' class='dropdown'>" +
								"<a href='' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'>" +
									"Administração <span class='caret'></span>" +
								"</a>" +
								"<ul class='dropdown-menu'>" +
									"<li><a href='#usuarios'>Usuários </span></a></li>" +
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
					templateHtml += templateHome;
					if ($scope.usuarioLogado) {
						templateHtml += templateResumo;
						templateHtml += templateExtrato;
						templateHtml += templatePlanejamento;
						templateHtml += menuCadastros;
					}
					if ($scope.usuarioAdmin) {
						templateHtml += menuAdmin;
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