'use strict';

jaxrsDoc.controller('ApiController', ['$scope', 'ProjectConf', 'Apis', function($scope, ProjectConf, Apis) {
//  $scope.currentProject = ProjectConf.currentProject;
  $scope.projectDefinition = ProjectConf.loadProject(ProjectConf.currentProject, ProjectConf.currentVersion);
}]);
