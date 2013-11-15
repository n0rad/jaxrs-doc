'use strict';

jaxrsDoc.controller('GlobalController', ['$scope', '$location', 'Projects',
function($scope, $location, Projects) {
  
  $scope.$watch(function() { return $location.path() + $location.hash(); }, function(newLoc, oldLoc){
    var urlElement = newLoc.split('/', 4);
    Projects.currentProject = $scope.currentProject = urlElement[1];
    Projects.currentVersion = $scope.currentVersion = urlElement[2];
    Projects.currentApi = $scope.currentApi = urlElement[3];
  });
  
}]);
