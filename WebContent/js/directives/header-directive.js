angular.module('headerDirective', [])
	.directive('bsNavHeader', function (servicosLogin, $compile) {

		var templateDeslogado = "<li ng-show='!usuarioLogado'><a href='#login'><span class='glyphicon glyphicon-log-in'></span><span class='font-bh-light'> entrar </span></a></li>"+
								"<li ng-show='!usuarioLogado'><a href='#registro'><span class='glyphicon glyphicon-check'></span><span class='font-bh-light'> registrar </span></a></li>";

		var templateLogado =	"<li ng-show='usuarioLogado' class='dropdown'>" +
									"<a href='' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'>" +
										"<span class='glyphicon glyphicon-user'></span><span class='font-bh-light'> {{usuarioLogado.login}} </span><span class='caret'></span>" +
									"</a>" +
									"<ul class='dropdown-menu'>" +
										"<li><a href='#senha'><span class='glyphicon glyphicon-edit'></span><span class='font-bh-light'> senha </span></a></li>" +
										"<li role='separator' class='divider'></li>" +
										"<li><a href='#logout' ng-click='efetuarLogout()'><span class='glyphicon glyphicon-log-out'></span><span class='font-bh-light'> logout </span></a></li>" +
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

		var templateHome = "<li><a href='#home'><span class='glyphicon glyphicon-home' /></a></li>";
		
		var templateResumo = "<li><a href='#resumo' class='font-bh-light'>Resumo</a></li>";

		var menuCadastros =	"<li ng-show='usuarioLogado' class='dropdown'>" +
								"<a href='' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'>" +
									"<span class='font-bh-light'>Cadastros</span> <span class='caret'></span>" +
								"</a>" +
								"<ul class='dropdown-menu'>" +
									"<li><a href='#categorias' class='font-bh-light'>Categorias</a></li>" +
									"<li><a href='#contas' class='font-bh-light'>Contas</a></li>" +
								"</ul>" +
							"</li>";

		var menuTemp =	"<li ng-show='usuarioLogado' class='dropdown'>" +
								"<a href='' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'>" +
									"<span class='font-bh-light'>Temp</span> <span class='caret'></span>" +
								"</a>" +
								"<ul class='dropdown-menu'>" +
									"<li><a href='ws/usuarios/1/categorias/1' class='font-bh-light'>Categoria (1) - REST</a></li>" +
									"<li><a href='ws/usuarios/1/categorias' class='font-bh-light'>Categorias - REST</a></li>" +
								"</ul>" +
							"</li>";

		var menuAdmin =	"<li ng-show='usuarioLogado' class='dropdown'>" +
								"<a href='' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'>" +
									"<span class='font-bh-light'> Administração </span><span class='caret'></span>" +
								"</a>" +
								"<ul class='dropdown-menu'>" +
									"<li><a href='#usuarios'><span class='font-bh-light'> Usuários </span></a></li>" +
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
						templateHtml += menuCadastros;
					}
					templateHtml += menuTemp;
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