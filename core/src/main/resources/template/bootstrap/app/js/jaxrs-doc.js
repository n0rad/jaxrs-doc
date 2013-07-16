angular.module("jaxrs-doc", ['ui.bootstrap']);

function apiController($scope, $http) {

	function buildApiGroup(apis) {
		var result = {};

		for (var i = 0; i < apis.length; i++) {
			var api = apis[i];
			for (var j = 0; j < api.operations.length; j++) {
				var operation = api.operations[j];
				var root = rootPath(operation.path);
				result[root] = result[root] ? result[root] : {};
				result[root][operation.path] = result[root][operation.path] ? result[root][operation.path] : {}; 
				var groupedOperation = result[root][operation.path];
				groupedOperation.methods = groupedOperation.methods ? groupedOperation.methods : []; 
				groupedOperation.operations = groupedOperation.operations ? groupedOperation.operations : []; 
				
				groupedOperation.methods.push(operation.httpMethod);
				groupedOperation.operations.push(operation);
			}
		}
		
		
		return result;
	}
	
	function rootPath(path) {
		if (!path) {
			return '';
		}
		var slashPos = path.substring(1).indexOf('/') + 1;
		if (slashPos <= 0) {
			slashPos = path.length;
		}
		var root = path.substring(0, slashPos);
		return root;
	}
	
	$http.get('definition.json').success(function(project) {
		$scope.apiGroup = buildApiGroup(project.apis);
	});

}


function DropdownCtrl($scope) {
	  $scope.items = [
	    "The first choice!",
	    "And another choice for you.",
	    "but wait! A third!"
	  ];
	}