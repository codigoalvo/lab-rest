angular.module('dialogsService', [])
	.factory('cDialogs', function($q, $mdDialog) {
		var recurso = {};

		recurso.loading = function() {
			 $mdDialog.show({
				template: '<md-dialog class="loading-dialog" ng-cloak><md-progress-circular md-mode="indeterminate" md-diameter="100px"></md-progress-circular></md-dialog>',
				parent: angular.element(document.body),
				clickOutsideToClose:false,
				escapeToClose:false,
				ariaLabel:'Carregando...'
			});
		}

		recurso.inform = function(titulo, corpoHtml, textoBotao) {
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

		recurso.hide = function() {
			$mdDialog.hide();
		}

		return recurso;
	});