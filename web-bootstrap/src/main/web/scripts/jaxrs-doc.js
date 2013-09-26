'use strict';

var jaxrsDoc = angular.module('jaxrs-doc', ['ngRoute', 'ui.bootstrap']);

jaxrsDoc.constant('docServiceUrl', docServiceUrl);


jaxrsDoc.config(function ($routeProvider, $locationProvider) {
  
  $locationProvider.html5Mode(true);

  $routeProvider.when('/', {
      templateUrl: 'views/main.html',
      controller: 'MainCtrl'
    }).otherwise({redirectTo: '/'});
});