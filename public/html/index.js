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
    'runnerModule',
    'loginModule',
    'tourModule'
])
    .service('TcBddService', function($modal, $http, $location, $q) {
        this.openCompositeClass = function(compositeStepText) {
            var self = this;
            $modal.open({
                templateUrl: '/assets/html/compositeClasses/compositeClasses.tmpl.html',
                controller: 'compositeClassesCtrl',
                resolve: {
                    compositeClasses: function($route, $http, $modal) {
                        return self.getJson('/groovyComposites', false);
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
        this.openRunnerParametersModal = function(showGetApi, $scope) {
            var self = this;
            return $modal.open({
                templateUrl: '/assets/html/runner/runnerParams.tmpl.html',
                controller: 'runnerParamsCtrl',
                resolve: {
                    data: function($route, $http, $modal) {
                        return self.getJson('/steps/classes.json', true);
                    },
                    showGetApi: function() {
                        return showGetApi;
                    },
                    features: function() {
                        return {runStory: $scope.features.runStory};
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
            var self = this;
            if ($scope.rootPath === undefined) {
                $scope.rootPath = '';
            }
            $http.get('/stories/list.json?path=' + $scope.rootPath + path).then(function(response) {
                $scope.files = response.data;
                if (path !== '') {
                    $scope.rootPath += path + '/';
                }
            }, function(response) {
                self.openErrorModal(response.data);
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
        this.cssClass = function(ngModelController) {
            return {
                'has-error': ngModelController.$invalid,
                'has-success': ngModelController.$valid && ngModelController.$dirty
            };
        };
        // TODO Test
        this.deleteStory = function(path) {
            var self = this;
            var deferred = $q.defer();
            var message = {status: 'Delete Story', message: 'Are you sure you want to delete this story?'};
            var okModal = this.openConfirmationModal(message);
            okModal.result.then(function() {
                $http.delete('/stories/story/' + path).then(function() {
                    $location.path('/page/stories/new/');
                    deferred.resolve('OK');
                }, function(response) {
                    self.openErrorModal(response.data);
                    deferred.reject('NOK');
                });
            }, function() {
                deferred.reject('NOK');
            });
            return deferred.promise;
        };
        this.getJson = function(url, cacheType) {
            var self = this;
            return $http.get(url, {cache: cacheType}).then(function(response) {
                return response.data;
            }, function(response) {
                self.openErrorModal(response.data);
            });
        };
        this.openErrorModal = function(data) {
            $modal.open({
                templateUrl: '/assets/html/errorModal.tmpl.html',
                controller: 'modalCtrl',
                resolve: {
                    data: function() {
                        return data;
                    }
                }
            });
        };
        this.startJoyRide = function(id, scope) {
            $http.get('/api/v1/data/' + id).then(function(response) {
                scope.configJoyRide = response.data.data;
                scope.tours = [{}];
                scope.startJoyRideFlag = true;
            }, function() {
                scope.startJoyRideFlag = false;
            });
        };
        this.onFinishJoyRide = function(scope) {
            scope.startJoyRideFlag = false;
            scope.tours = [];
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
    });
