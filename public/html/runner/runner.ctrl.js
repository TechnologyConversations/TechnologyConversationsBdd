angular.module('runnerModule', [])
    .controller('runnerCtrl', ['$scope', '$modal', '$http',
        function($scope, $modal, $http) {
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
                        openRunnerParametersModal($modal).result.then(function (data) {
                            var classes = data;
                            $http.get('/composites').then(function (response) {
                                var composites = response.data;
                                $scope.run({
                                    storyPaths: storyPaths,
                                    classes: classes,
                                    composites: composites
                                });
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
                    if (data.status !== 'OK') {
                        openErrorModal($modal, data);
                    } else {
                        $scope.reportsUrl = data.reportsPath.replace('public/', '/assets/');
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
    ]);