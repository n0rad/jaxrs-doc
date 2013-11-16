'use strict';

jaxrsDoc.controller('GlobalController', ['$scope', '$location', 'Projects',
function($scope, $location, Projects) {
  
  $scope.$watch(function() { return $location.path() + $location.hash(); }, function(newLoc, oldLoc){
    var urlElement = newLoc.split('/', 3);
    Projects.currentProject = $scope.currentProject = urlElement[1];
    Projects.currentVersion = $scope.currentVersion = urlElement[2];
    Projects.currentApiUrl = $scope.currentApiUrl = newLoc.substr(urlElement[1].length + urlElement[2].length + 2 + "/apiGroup".length);
  });
  
}]);
