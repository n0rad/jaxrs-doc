'use strict';


var jaxrsDoc = angular.module('jaxrs-doc', ['ui.router', 'ui.bootstrap']);

jaxrsDoc.constant('docServiceUrl', docServiceUrl);

jaxrsDoc.config(function ($stateProvider, $urlRouterProvider, $locationProvider) {
    $locationProvider.html5Mode(true);

    $stateProvider.state('slash', {
            url: "/",
            templateUrl: 'views/Main.html'
        }).state('project', {
            url: "/:currentProject",
            templateUrl: 'views/Main.html'
        }).state('version', {
            url: "/:currentProject/:currentVersion",
            templateUrl: 'views/Project.html'
        }).state('api', {
            url: "/:currentProject/:currentVersion/apiGroup/*currentApi",
            templateUrl: 'views/Project.html'
        });


//  $routeProvider.when('/', {
//    controller : 'ApiController',
//    templateUrl : 'views/Main.html'
//  	  }).when('/:currentProject', {
//  	    controller : 'ApiController',
//  	    templateUrl : 'views/Main.html'
//  	  }).when('/:currentProject/:currentVersion', {
//          controller : 'ApiController',
//          templateUrl : 'views/Project.html'
//      }).when('/:currentProject/:currentVersion/api/:currentApi', {
//          controller : 'ApiController',
//          templateUrl : 'views/Project.html'
//      });
////	.otherwise({redirectTo:'/'});
});