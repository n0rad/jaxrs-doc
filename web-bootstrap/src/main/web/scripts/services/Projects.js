'use strict';


jaxrsDoc.factory('Projects', ['$http', '$rootScope', function($http, $rootScope) {
    var projects = {};

    var Projects = {

        currentProject : undefined,

        loadProjectList : function() {
            var promise = $http.get('projects.csv').then(function (response) {
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
                    projects[proj[0]][proj[1]] = {};
                    projects[proj[0]][proj[1]].url = proj[2];
                }
                return projects;
            });
            return promise;
        },

        findApiGroup : function(apiUrl) {
            var groups = projects[this.currentProject][this.currentVersion].apiGroups;
            for (var k in groups){
                if (groups.hasOwnProperty(k)) {
                    for (var j in groups[k]){
                        if (groups[k].hasOwnProperty(j)) {
                            if (j == apiUrl) {
                                return groups[k][j];
                            }
                        }
                    }
                }
            }
        },

        getProject : function(project, version) {
            if (!project || !version || !projects[project] || !projects[project][version]) {
                return;
            }
            if (projects[project][version].data) {
                return projects[project][version];
            } else {
                var promise = $http.get(projects[project][version].url).then(function (response) {
                    console.log(response);
                    projects[project][version].data = response.data;
                    projects[project][version].apiGroups = buildApiGroups(response.data.apis);
                    return projects[project][version];
                });
                return promise;
            }
        }
    };
    return Projects;
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
