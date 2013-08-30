'use strict';

var jaxrsDoc = angular.module('jaxrs-doc', ['ngRoute', 'ui.bootstrap']);

jaxrsDoc.constant('docServiceUrl', docServiceUrl);


  jaxrsDoc.config(function ($routeProvider, $locationProvider) {
    
    $locationProvider.html5Mode(true);

    $routeProvider.when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      }).otherwise({redirectTo: '/'});
  }).filter('classShortName', function() {
    return function(input) {
      return input.substring(input.lastIndexOf('.') + 1);
    };
  })
  
  .directive('myCurrentTime', function($timeout, dateFilter) {
    // return the directive link function. (compile function not needed)
    return function(scope, element, attrs) {
      var format,  // date format
          timeoutId; // timeoutId, so that we can cancel the time updates

      // used to update the UI
      function updateTime() {
        element.text(dateFilter(new Date(), format));
      }

      // watch the expression, and update the UI on change.
      scope.$watch(attrs.myCurrentTime, function(value) {
        format = value;
        updateTime();
      });

      // schedule update in one second
      function updateLater() {
        // save the timeoutId for canceling
        timeoutId = $timeout(function() {
          updateTime(); // update DOM
          updateLater(); // schedule another update
        }, 1000);
      }

      // listen on DOM destroy (removal) event, and cancel the next UI update
      // to prevent updating time after the DOM element was removed.
      element.on('$destroy', function() {
        $timeout.cancel(timeoutId);
      });

      updateLater(); // kick off the UI update process.
    };
  });




jaxrsDoc.controller('Ctrl2', ['$scope', function($scope) {
  $scope.format = 'M/d/yy h:mm:ss a';  
}]);


jaxrsDoc.controller('navigation', ['$scope', '$location', '$http', 'docServiceUrl', function($scope, $location, $http, docServiceUrl) {
  $http.get(docServiceUrl + '/definition.json').success(function(project) {
    $scope.apiGroups = buildApiGroups(project.apis);
    $scope.models = project.models;
    $scope.$watch(function() { return $location.path() + $location.hash(); }, function(newLoc, oldLoc){
      $scope.currentGroup = $scope.apiGroups[$location.path()][$location.hash()];
      $scope.currentGroupName = $location.hash();
   });
  });  
}]);
    
jaxrsDoc.controller('AccordionDemoCtrl', ['$scope', function($scope) {
  
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




function DropdownCtrl($scope) {
    $scope.items = [
      "The first choice!",
      "And another choice for you.",
      "but wait! A third!"
    ];
  }