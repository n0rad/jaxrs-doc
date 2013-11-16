'use strict';

jaxrsDoc.controller('ApiController', ['$scope', 'Projects', 'Apis', function($scope, Projects, Apis) {

//    $scope.currentGroup = Projects.
    $scope.currentApiGroup = Projects.findApiGroup($scope.currentApiUrl);

}]);
