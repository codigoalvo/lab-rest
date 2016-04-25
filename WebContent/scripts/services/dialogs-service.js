angular.module('dialogsService', [])
	.factory('cDialogs', function($q, $mdDialog, $timeout) {
		var recurso = {};

		recurso.loadTimer = undefined;

		recurso.delayedLoading = function(milisegundos) {
			if (milisegundos === undefined || milisegundos === null || milisegundos === '') {
				milisegundos = 800;
			}
			recurso.loadTimer = $timeout(function() {
				//console.log('dialog.delayedLoading');
				recurso.stopTimer();
				recurso.loading();
			}, milisegundos);
		}

		recurso.loading = function() {
			$mdDialog.show({
				template: '<md-dialog class="loading-dialog" ng-cloak><md-progress-circular md-mode="indeterminate" md-diameter="100px"></md-progress-circular></md-dialog>',
				parent: angular.element(document.body),
				clickOutsideToClose: false,
				escapeToClose: false,
				ariaLabel: 'Carregando...'
			});
		}

		recurso.stopTimer = function() {
			if (recurso.loadTimer) {
				$timeout.cancel(recurso.loadTimer);
				recurso.loadTimer = undefined;
			}
		}

		recurso.inform = function(titulo, corpoHtml, textoBotao) {
			//console.log('dialog.inform');
			$mdDialog.show(
				$mdDialog.alert()
				.title(titulo)
				.ariaLabel(titulo)
				.htmlContent(corpoHtml)
				.parent(angular.element(document.querySelector('#popupContainer')))
				.clickOutsideToClose(true)
				.ok(textoBotao)
			);
		}

		recurso.confirm = function(titulo, corppoHtml, textoSim, textoNao) {
			//console.log('dialog.confirm');
			return $q(function(resolve, reject) {
				var confirm = $mdDialog.confirm()
					.title(titulo)
					.ariaLabel(titulo)
					.htmlContent(corppoHtml)
					.ok(textoSim)
					.cancel(textoNao);
				$mdDialog.show(confirm).then(function() {
					resolve(textoSim);
				}, function() {
					reject(textoNao);
				});
			});
		}

		recurso.custom = function(templateUrl, locals) {
			//console.log('dialog.custom.entidade: '+angular.toJson(locals));
			return $q(function(resolve, reject) {
				//console.log('q.locals: '+locals)
				$mdDialog.show({
					controller: DialogController,
					controllerAs: 'ctrl',
					templateUrl: templateUrl,
					parent: angular.element(document.body),
					clickOutsideToClose: true,
					locals: locals,
				}).then(function(resp) {
					resolve(resp);
				}, function(error) {
					if (error) {
						console.log(error);
					}
					reject(error);
				});
			});

			function DialogController($scope, $mdDialog, locals) {
				//console.log('DialogController.locals: '+angular.toJson(locals));
				$scope.locals = locals;
				$scope.hide = function() {
					$mdDialog.hide();
				};
				$scope.cancel = function() {
					$mdDialog.cancel();
				};
				$scope.answer = function(answer) {
					$mdDialog.hide(answer);
				};
			}
		}

		recurso.hide = function() {
			//console.log('dialog.hide')
			recurso.stopTimer();
			$mdDialog.hide();
		}

		return recurso;
	});
