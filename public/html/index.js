angular.module('storiesModule', [
    'ngRoute',
    'ngCookies',
    'ui.bootstrap',
    'ui.sortable',
    'configModule',
    'bodyModule',
    'topMenuModule',
    'storyModule',
    'compositeClassesModule',
    'compositesModule',
    'runnerModule'
])
    .service('TcBddService', function($modal, $http) {
        this.openCompositeClass = function(compositeStepText) {
            $modal.open({
                templateUrl: '/assets/html/compositeClasses/compositeClasses.tmpl.html',
                controller: 'compositeClassesCtrl',
                resolve: {
                    compositeClasses: function($route, $http, $modal) {
                        return getJson($http, $modal, '/groovyComposites', false);
                    },
                    compositeStepText: function() {
                        return compositeStepText;
                    }
                }
            });
        };
        this.newCollectionItem = function(event, collection) {
            if (event.which === 13) {
                collection.push({});
            }
        };
        this.removeCollectionElement = function(collection, index) {
            collection.splice(index, 1);
        };
        this.buttonCssClass = function(ngModelController) {
            return {
                'btn-success': ngModelController.$valid,
                'btn-danger': ngModelController.$invalid
            };
        };
        this.getRunnerProgressCss = function(inProgress) {
            return {
                'progress progress-striped active': inProgress,
                'progress': !inProgress
            };
        };
        this.getStoryRunnerStatusText = function(inProgress, success, pendingStepsLength) {
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
        };
        this.getRunnerStatusCss = function(inProgress, success, pendingSteps) {
            return {
                'progress-bar progress-bar-info': inProgress,
                'progress-bar progress-bar-warning': !inProgress && success && pendingSteps,
                'progress-bar progress-bar-success': !inProgress && success && !pendingSteps,
                'progress-bar progress-bar-danger': !inProgress && !success
            };
        };
        this.openRunnerParametersModal = function(showGetApi) {
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
        };
        this.openDir = function($scope, path) {
            if (path === '..') {
                var dirs = $scope.rootPath.split('/');
                $scope.rootPath = dirs.slice(0, dirs.length - 2).join('/');
                if ($scope.rootPath !== '') {
                    $scope.rootPath += '/';
                }
                this.getStories($scope, '');
            } else {
                this.getStories($scope, path);
            }
        };
        this.getStories = function ($scope, path) {
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
        };
        this.stepTextPattern = function() {
            return (/^(Given|When|Then) .+$/);
        };
        this.openConfirmationModal = function(data) {
            return $modal.open({
                templateUrl: '/assets/html/confirmationModal.tmpl.html',
                controller: 'modalCtrl',
                resolve: {
                    data: function() {
                        return data;
                    }
                }
            });
        };
        this.classNamePattern = function() {
            return (/^[a-zA-Z_$][a-zA-Z\d_$]*$/);
        };
    })
    .controller('modalCtrl', function($scope, $modalInstance, data) {
        $scope.data = data;
        $scope.ok = function () {
            $modalInstance.close('ok');
        };
        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    })
    .controller('storiesCtrl', function($scope, $http, $modal, $modalInstance, $location, $q, TcBddService) {
        TcBddService.getStories($scope, '');
        $scope.openDir = function(path) {
            TcBddService.openDir($scope, path);
        };
        $scope.close = function() {
            $modalInstance.close();
        };
        $scope.viewStoryUrl = function(name) {
            return getViewStoryUrl() + $scope.rootPath + name;
        };
        $scope.allowToPrevDir = function() {
            return $scope.rootPath !== '';
        };
        // TODO Test
        $scope.deleteStory = function(name, index) {
            var path = $scope.rootPath + name + '.story';
            deleteStory($modal, $http, $location, $q, path).then(function() {
                $scope.files.stories.splice(index, 1);
            });
        };
        // TODO Test
        $scope.createDirectory = function(path) {
            var json = '{"path": "' + $scope.rootPath + path + '"}';
            $http.post('/stories/dir.json', json).then(function() {
                $scope.files.dirs.push({name: path});
            }, function(response) {
                openErrorModal($modal, response.data);
            });
        };
        // TODO Test
        $scope.getNewStoryUrl = function() {
            return getNewStoryUrl();
        };
    });

// TODO Test
function getJson($http, $modal, url, cacheType) {
    return $http.get(url, {cache: cacheType}).then(function(response) {
        return response.data;
    }, function(response) {
        openErrorModal($modal, response.data);
    });
}

// TODO Test
function openErrorModal($modal, data) {
    $modal.open({
        templateUrl: '/assets/html/errorModal.tmpl.html',
        controller: 'modalCtrl',
        resolve: {
            data: function() {
                return data;
            }
        }
    });
}

function getViewStoryUrl() {
    return '/page/stories/view/';
}

function getNewStoryUrl() {
    return '/page/stories/new/';
}

function getCompositesUrl() {
    return '/page/composites/';
}

// TODO Test
function deleteStory($modal, $http, $location, $q, path) {
    var deferred = $q.defer();
    var message = {status: 'Delete Story', message: 'Are you sure you want to delete this story?'};
    var okModal = openConfirmationModal($modal, message);
    okModal.result.then(function() {
        $http.delete('/stories/story/' + path).then(function() {
            $location.path(getNewStoryUrl());
            deferred.resolve('OK');
        }, function(response) {
            openErrorModal($modal, response.data);
            deferred.reject('NOK');
        });
    }, function() {
        deferred.reject('NOK');
    });
    return deferred.promise;
}

// TODO: Remove after deleteStory is moved to services
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
function cssClass(ngModelController) {
    return {
        'has-error': ngModelController.$invalid,
        'has-success': ngModelController.$valid && ngModelController.$dirty
    };
}
