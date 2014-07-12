// TODO Test
function openConfirmationModal($modal, data) {
    return $modal.open({
        templateUrl: '/assets/html/confirmationModal.tmpl.html',
        controller: 'modalCtrl',
        resolve: {
            data: function() {
                return data;
            }
        }
    });
}

// TODO Test
function getCompositesJson(http, fileName) {
    var url = '/groovyComposites/' + fileName;
    return http.get(url, {cache: false}).then(function(response) {
        return response.data;
    }, function() {
        var className = fileName.substring(0, fileName.lastIndexOf('.'));
        return {
            class: className,
            composites:[{stepText: '', compositeSteps: [{}]}],
            isNew: true
        };
    });
}

function stepTextPattern() {
    return (/^(Given|When|Then) .+$/);
}

function getStories($scope, $http, $modal, path) {
    if ($scope.rootPath === undefined) {
        $scope.rootPath = '';
    }
    $http.get('/stories/list.json?path=' + $scope.rootPath + path).then(function(response) {
        $scope.files = response.data;
        if (path !== '') {
            $scope.rootPath += path + '/';
        }
    }, function(response) {
        openErrorModal($modal, response.data);
    });
}

function openDir($scope, $http, $modal, path) {
    if (path === '..') {
        var dirs = $scope.rootPath.split('/');
        $scope.rootPath = dirs.slice(0, dirs.length - 2).join('/');
        if ($scope.rootPath !== '') {
            $scope.rootPath += '/';
        }
        getStories($scope, $http, $modal, '');
    } else {
        getStories($scope, $http, $modal, path);
    }
}

// TODO Test
function openRunnerParametersModal($modal, showGetApi) {
    return $modal.open({
        templateUrl: '/assets/html/runner/runnerParams.tmpl.html',
        controller: 'runnerParamsCtrl',
        resolve: {
            data: function($route, $http, $modal) {
                return getJson($http, $modal, '/steps/classes.json', true);
            },
            showGetApi: function() {
                return showGetApi;
            }
        }
    });
}

function getRunnerStatusCss(inProgress, success, pendingSteps) {
    return {
        'progress-bar progress-bar-info': inProgress,
        'progress-bar progress-bar-warning': !inProgress && success && pendingSteps,
        'progress-bar progress-bar-success': !inProgress && success && !pendingSteps,
        'progress-bar progress-bar-danger': !inProgress && !success
    };
}

function getStoryRunnerStatusText(inProgress, success, pendingStepsLength) {
    if (inProgress) {
        return 'Story run is in progress';
    } else if (success) {
        if (pendingStepsLength > 0) {
            return 'Story run was successful with ' + pendingStepsLength + ' pending steps';
        } else {
            return 'Story run was successful';
        }
    } else {
        return 'Story run failed';
    }
}

function getRunnerProgressCss(inProgress) {
    return {
        'progress progress-striped active': inProgress,
        'progress': !inProgress
    };
}