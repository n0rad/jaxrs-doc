'use strict';

jaxrsDoc.controller('FilterController', ['$scope', 'ProjectConf', '$location', function($scope, ProjectConf, $location) {

  $scope.projects = ProjectConf.getProjects();
//  $scope.currentProject = Projects.currentProject;
  
  $scope.$on('currentProject', function() {
    $scope.currentProject = ProjectConf.currentProject;
  });
  
  

//  $scope.currentVersion = $routeParams.currentVersion;
//  $scope.currentProject = project;
  


}]);
