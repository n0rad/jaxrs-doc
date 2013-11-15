'use strict';

jaxrsDoc.controller('FilterController', ['$scope', 'Projects', '$location', function($scope, Projects, $location) {

  $scope.projects = Projects.loadProjectList();
//  $scope.currentProject = Projects.currentProject;
  
  $scope.$on('currentProject', function() {
    $scope.currentProject = Projects.currentProject;
  });
  
  

//  $scope.currentVersion = $routeParams.currentVersion;
//  $scope.currentProject = project;
  


}]);
