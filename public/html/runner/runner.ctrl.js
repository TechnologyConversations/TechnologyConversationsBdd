angular.module('runnerModule', [])
    .controller('runnerCtrl', ['$scope', '$modal', '$http', '$location',
        function($scope, $modal, $http, $location) {
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
                        openRunnerParametersModal($modal, true).result.then(function (data) {
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
                $scope.storyRunnerInProgress = true;
                $scope.showRunnerProgress = true;
                $scope.reportsUrl = '';
                $http.post('/runner/run.json', json).then(function (response) {
                    var data = response.data;
                    if (data.status === 'OK') {
                        $scope.reportsUrl = data.reportsPath.replace('public/', '/assets/');
                        $scope.storyRunnerInProgress = false;
                        $scope.storyRunnerSuccess = true;
                    } else if (data.status === 'FAILED') {
                        $scope.reportsUrl = data.reportsPath.replace('public/', '/assets/');
                        $scope.storyRunnerInProgress = false;
                        $scope.storyRunnerSuccess = false;
                    }
                    else {
                        openErrorModal($modal, data);
                    }
                    $scope.storyRunnerInProgress = false;
                    $scope.storyRunnerSuccess = true;
                }, function (response) {
                    $scope.storyRunnerInProgress = false;
                    $scope.storyRunnerSuccess = false;
                    openErrorModal($modal, response.data);
                });
            };
            $scope.getRunnerStatusCss = function () {
                return getRunnerStatusCss(
                    $scope.storyRunnerInProgress,
                    $scope.storyRunnerSuccess,
                    ($scope.pendingSteps > 0)
                );
            };
            $scope.getStoryRunnerStatusText = function () {
                return getStoryRunnerStatusText(
                    $scope.storyRunnerInProgress,
                    $scope.storyRunnerSuccess,
                    $scope.pendingSteps.length
                );
            };
            $scope.getRunnerProgressCss = function () {
                return getRunnerProgressCss($scope.storyRunnerInProgress);
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
        }
    ])
    .controller('runnerSelectorCtrl', ['$scope', '$http', '$modal', '$modalInstance',
        function($scope, $http, $modal, $modalInstance) {
            $scope.files = {dirs: [], stories: []};
            getStories($scope, $http, $modal, '');
            $scope.openDir = function(path) {
                openDir($scope, $http, $modal, path);
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
        }
    ])
    .controller('runnerParamsCtrl', ['$scope', '$modalInstance', '$cookieStore', 'data', 'showGetApi',
        function ($scope, $modalInstance, $cookieStore, data, showGetApi) {
            $scope.classes = data.classes;
            $scope.classes.forEach(function(classEntry) {
                classEntry.params.forEach(function(paramEntry) {
                    paramEntry.value = $cookieStore.get(classEntry.fullName + "." + paramEntry.key);
                });
            });
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
        }
    ]);