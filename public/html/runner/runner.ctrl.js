angular.module('runnerModule', [])
    .controller('runnerCtrl', function($scope, $modal, $http, $location, TcBddService) {
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
                    TcBddService.openRunnerParametersModal(true).result.then(function (data) {
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
            var reportsPrefix = '/assets/jbehave/';
            $scope.storyRunnerInProgress = true;
            $scope.showRunnerProgress = true;
            $scope.reportsUrl = '';
            $http.post('/runner/run.json', json).then(function (response) {
                var data = response.data;
                if (data.status === 'OK') {
                    $scope.reportsUrl = reportsPrefix + data.reportsPath;
                    $scope.storyRunnerInProgress = false;
                    $scope.storyRunnerSuccess = true;
                } else if (data.status === 'FAILED') {
                    $scope.reportsUrl = reportsPrefix + data.reportsPath;
                    $scope.storyRunnerInProgress = false;
                    $scope.storyRunnerSuccess = false;
                }
                else {
                    TcBddService.openErrorModal(data);
                }
                $scope.storyRunnerInProgress = false;
                $scope.storyRunnerSuccess = true;
            }, function (response) {
                $scope.storyRunnerInProgress = false;
                $scope.storyRunnerSuccess = false;
                TcBddService.openErrorModal(response.data);
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
        $scope.init = function() {
            $scope.storyRunnerInProgress = false;
            $scope.storyRunnerSuccess = false;
            $scope.showRunnerProgress = false;
            $scope.pendingSteps = [];
            $scope.reportsUrl = '';
            $scope.openRunner();
            $scope.showApi = false;
        };
        $scope.apiUrl = function() {
            return $location.protocol() + '://' + $location.host() + ':' + $location.port() + '/runner/run.json';
        };
        $scope.init();
    })
    .controller('runnerSelectorCtrl', function($scope, $http, $modal, $modalInstance, TcBddService) {
        $scope.files = {dirs: [], stories: []};
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
    })
    .controller('runnerParamsCtrl', function ($scope, $modalInstance, $cookieStore, data, showGetApi) {
        $scope.classes = data.classes;
        $scope.paramArray = [];
        $scope.classes.forEach(function(classEntry) {
            classEntry.params.forEach(function(paramEntry) {
                try {
                    paramEntry.value = $cookieStore.get(classEntry.fullName + "." + paramEntry.key);
                } catch(err) {
                    console.log('Could not retrieve cookie ' + classEntry.fullName + "." + paramEntry.key);
                    console.log(err.message);
                }
            });
        });

		$scope.hasOptions = function(options) {
			if (options) {
			  return options.length > 0;
			} else {
			  return false;
			}
		};

//        $scope.paramFound = function (paramName) {
//            return ($scope.paramArray.indexOf(paramName)>=0);
//        };
//
//        $scope.selectedOption = function (option, param) {
//            if ($scope.paramFound(param.key)) {
//                return false;
//            } else if (param.value == option.value) {
//                $scope.paramArray.push(param.key);
//                return true;
//            }
//            return (option.isSelected);
//        };

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
    });
