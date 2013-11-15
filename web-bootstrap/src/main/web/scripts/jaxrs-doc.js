'use strict';


var jaxrsDoc = angular.module('jaxrs-doc', ['ngRoute', 'ui.bootstrap']);

jaxrsDoc.constant('docServiceUrl', docServiceUrl);

jaxrsDoc.config(function ($routeProvider, $locationProvider) {  
  $locationProvider.html5Mode(true);
  $routeProvider.when('/', {
    controller : 'ApiController',
    templateUrl : 'views/Main.html'
  	  }).when('/:currentProject', {
  	    controller : 'ApiController',
  	    templateUrl : 'views/Main.html'
  	  }).when('/:currentProject/:currentVersion', {
          controller : 'ApiController',
          templateUrl : 'views/Project.html'
      }).when('/:currentProject/:currentVersion/api/:currentApi', {
          controller : 'ApiController',
          templateUrl : 'views/Project.html'
      });
//	.otherwise({redirectTo:'/'});
});