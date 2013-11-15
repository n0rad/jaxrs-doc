'use strict';

jaxrsDoc.factory('ProjectConf', ['$http', '$rootScope', function($http, $rootScope) {
  var projects = {};
    
  var Projects = {

      currentProject : undefined,

      getProjects : function() {
      var promise = $http.get('/projects.csv').then(function (response) {
        console.log(response);
        var projLine = response.data.split('\n');
        for (var i = 0; i < projLine.length; i++) {
          var proj =  projLine[i].split(',');
          if (!proj || !proj[2]) {
            continue;
          }
          if (!projects[proj[0]]) {
            projects[proj[0]] = {};
          }
          projects[proj[0]][proj[1]] = proj[2];
        }
        return projects;
      });
      return promise;
    },
    
    loadProject : function(project, version) {
      if (!project || !version || !projects[project] || !projects[project][version]) {
        return;
      }
      var promise = $http.get(projects[project][version]).then(function (projectDefinition) {
        console.log(projectDefinition);
        return projectDefinition;
      });
      return promise;      
    },
  };
  return Projects;
}]);
