'use strict';

jaxrsDoc.controller('GlobalController', ['$scope', '$location', 'ProjectConf',
function($scope, $location, ProjectConf) {
  
  $scope.$watch(function() { return $location.path() + $location.hash(); }, function(newLoc, oldLoc){
    var urlElement = newLoc.split('/', 3);
    ProjectConf.currentProject = $scope.currentProject = urlElement[1];
    ProjectConf.currentVersion = $scope.currentVersion = urlElement[2];
  });
  
}]);
