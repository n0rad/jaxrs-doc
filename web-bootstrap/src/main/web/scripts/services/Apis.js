'use strict';

jaxrsDoc.factory('Apis', ['$http', function($http) {
  
  
  
  
//  $http.get(docServiceUrl + '/jaxrs-doc-2.23.0-SNAPSHOT.json').success(function(project) {
//    $scope.apiGroups = buildApiGroups(project.apis);
//    $scope.models = project.models;
//    $scope.$watch(function() { return $location.path() + $location.hash(); }, function(newLoc, oldLoc){
//      $scope.currentGroup = $scope.apiGroups[$location.path()][$location.hash()];
//      $scope.currentGroupName = $location.hash();
//   });
//  });
}]);

function buildApiGroups(apis) {
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
