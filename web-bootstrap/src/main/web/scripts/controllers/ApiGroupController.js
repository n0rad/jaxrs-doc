'use strict';

jaxrsDoc.controller('ApiGroupController', ['$scope', 'Projects', 'Apis', function($scope, Projects, Apis) {
//  $scope.currentProject = ProjectConf.currentProject;
    $scope.projectDefinition = Projects.getProject(Projects.currentProject, Projects.currentVersion);
}]);
