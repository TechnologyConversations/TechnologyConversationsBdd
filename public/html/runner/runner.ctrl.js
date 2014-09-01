angular.module('runnerModule', [])
    .controller('runnerCtrl', function($scope, $modal, $http, $location, $timeout, TcBddService) {
        // TODO Test more than checking whether $modal.open was called
        $scope.openRunnerSelector = function() {
            return $modal.open({
                templateUrl: '/assets/html/runner/runnerSelector.tmpl.html',
                controller: 'runnerSelectorCtrl',
                resolve: {
                    data: function() {
                        return {};
                    }
                }
            });
        };
        // TODO Test
        $scope.openRunner = function() {
            var runnerSelector = $scope.openRunnerSelector();
            if (runnerSelector !== undefined) {
                runnerSelector.result.then(function (data) {
                    var storyPaths = [];
                    data.dirs.forEach(function (dir) {
                        storyPaths.push({path: dir.path + "/**/*.story"});
                    });
                    data.stories.forEach(function (story) {
                        storyPaths.push({path: story.path});
                    });
                    TcBddService.openRunnerParametersModal(true, $scope).result.then(function (data) {
                        var classes = data.classes;
                        var action = data.action;
                        $http.get('/groovyComposites').then(function (response) {
                            var groovyComposites = response.data;
                            $scope.apiJson = {
                                storyPaths: storyPaths,
                                classes: classes,
                                groovyComposites: groovyComposites
                            };
                            if (action === 'run') {
                                $scope.run($scope.apiJson);
                                $scope.showApi = false;
                            } else {
                                $scope.showApi = true;
                            }
                        });
                    });
                });
            }
        };
        $scope.run = function(json) {
            var reportsPrefix = '/api/v1/reporters/get/';
            $scope.reportsUrl = '';
            $scope.storyRunnerSuccess = false;
            $scope.showRunnerProgress = true;
            $http.post('/runner/run.json', json).then(function (response) {
                var data = response.data;
                if (data.status === 'OK') {
                    $scope.reportsUrl = reportsPrefix + data.reportsPath;
                    $scope.storyRunnerInProgress = true;
                    $scope.getReports(data.id);
                } else if (data.status === 'FAILED') {
                    $scope.reportsUrl = reportsPrefix + data.reportsPath;
                    $scope.storyRunnerInProgress = false;
                } else {
                    TcBddService.openErrorModal(data);
                }
            }, function (response) {
                $scope.storyRunnerInProgress = false;
                $scope.storyRunnerSuccess = false;
                TcBddService.openErrorModal(response.data);
            });
        };
        $scope.getReports = function(reportsId) {
            $http.get('/api/v1/reporters/list/' + reportsId).then(function (response) {
                $scope.reports = response.data;
                if ($scope.reports.status !== 'finished') {
                    $scope.showRunnerProgress = true;
                    $timeout(function() {
                        $scope.getReports(reportsId);
                    }, 15000);
                } else {
                    $scope.showRunnerProgress = false;
                }
            }, function (response) {
                if (response.data.message === 'ID is NOT correct') {
                    $timeout(function() {
                        $scope.getReports(reportsId);
                    }, 15000);
                } else {
                    $scope.showRunnerProgress = false;
                    TcBddService.openErrorModal(response.data);
                }
            });
        };
        $scope.getRunnerStatusCss = function () {
            return TcBddService.getRunnerStatusCss(
                $scope.storyRunnerInProgress,
                $scope.storyRunnerSuccess,
                ($scope.pendingSteps > 0)
            );
        };
        $scope.getStoryRunnerStatusText = function () {
            if ($scope.storyRunnerInProgress) {
                return 'Stories run is in progress';
            } else {
                return 'Stories run is finished';
            }
        };
        $scope.getRunnerProgressCss = function () {
            return TcBddService.getRunnerProgressCss($scope.storyRunnerInProgress);
        };
        $scope.onLoad = function() {
            $scope.storyRunnerInProgress = false;
            $scope.storyRunnerSuccess = false;
            $scope.showRunnerProgress = false;
            $scope.pendingSteps = [];
            $scope.reportsUrl = '';
            if ($location.search().reportsId === undefined) {
                $scope.openRunner();
            } else {
                var reportsId = $location.search().reportsId;
                $scope.reportsUrl = '/api/v1/reporters/get/' + reportsId + '/view/reports.html';
                $scope.getReports(reportsId);
            }
            $scope.showApi = false;
        };
        $scope.apiUrl = function() {
            return $location.protocol() + '://' + $location.host() + ':' + $location.port() + '/runner/run.json';
        };
        $scope.onLoad();
    })
    .controller('runnerSelectorCtrl', function($scope, $http, $modal, $modalInstance, $location, TcBddService) {
        $scope.onLoad = function() {
            $scope.files = {dirs: [], stories: []};
            TcBddService.startJoyRideOnLoad($location, $scope);
        };
        TcBddService.getStories($scope, '');
        $scope.openDir = function(path) {
            TcBddService.openDir($scope, path);
        };
        $scope.cancelRunnerSelector = function () {
            $modalInstance.dismiss('cancel');
        };
        $scope.okRunnerSelector = function() {
            var selectedFiles = {dirs: [], stories: []};
            angular.forEach($scope.files.dirs, function(value) {
                if (value.selected) {
                    selectedFiles.dirs.push({path: $scope.rootPath + value.name});
                }
            });
            angular.forEach($scope.files.stories, function(value) {
                if (value.selected) {
                    selectedFiles.stories.push({path: $scope.rootPath + value.name + ".story"});
                }
            });
            $modalInstance.close(selectedFiles);
        };
        $scope.allowToPrevDir = function() {
            return $scope.rootPath !== '';
        };
        $scope.canContinue = function() {
            var hasSelected = false;
            angular.forEach($scope.files.dirs, function(value) {
                if (value.selected) {
                    hasSelected = true;
                }
            });
            if (!hasSelected) {
                angular.forEach($scope.files.stories, function (value) {
                    if (value.selected) {
                        hasSelected = true;
                    }
                });
            }
            return hasSelected;
        };
        $scope.onFinishJoyRide = function() {
            TcBddService.onFinishJoyRide($scope);
        };
        $scope.startJoyRide = function(id) {
            TcBddService.startJoyRide(id, $scope);
        };
        $scope.onLoad();
    })
    .controller('runnerParamsCtrl', function ($scope, $modalInstance, $cookieStore, $location, data, showGetApi, TcBddService, features) {
        $scope.onLoad = function() {
            $scope.paramArray = [];
            $scope.features = features;
            TcBddService.startJoyRideOnLoad($location, $scope);
        };
        $scope.classes = data.classes;
        $scope.setParams = function() {
            $scope.classes.forEach(function(classEntry) {
                classEntry.params.forEach(function(paramEntry) {
                    if (paramEntry.value !== undefined && paramEntry.value !== '') {
                        paramEntry.disabled = true;
                    } else {
                        paramEntry.disabled = false;
                        try {
                            paramEntry.value = $cookieStore.get(classEntry.fullName + "." + paramEntry.key);
                        } catch(err) {
                            console.log('Could not retrieve cookie ' + classEntry.fullName + "." + paramEntry.key);
                            console.log(err.message);
                        }
                    }
                });
            });
        };
		$scope.hasOptions = function(options) {
			if (options) {
			  return options.length > 0;
			} else {
			  return false;
			}
		};
        $scope.hasParams = function(classEntry) {
            return classEntry.params !== undefined && classEntry.params.length > 0;
        };
        $scope.ok = function() {
            $modalInstance.close({action: 'run', classes: $scope.classes});
        };
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.showGetApi = function() {
            return showGetApi;
        };
        $scope.getApi = function() {
            $modalInstance.close({action: 'api', classes: $scope.classes});
        };
        $scope.paramElementId = function(className, paramKey) {
            var formattedClassName = className.charAt(0).toLowerCase() + className.slice(1);
            var formattedParamKey = paramKey.charAt(0).toUpperCase() + paramKey.slice(1);
            return formattedClassName + formattedParamKey;
        };
        $scope.setParams();
        $scope.onFinishJoyRide = function() {
            TcBddService.onFinishJoyRide($scope);
        };
        $scope.startJoyRide = function(id) {
            TcBddService.startJoyRide(id, $scope);
        };
        $scope.onLoad();
    });
