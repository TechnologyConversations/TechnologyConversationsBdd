angular.module('topMenuModule', [])
    .controller('topMenuController', ['$scope', '$modal', '$location', '$http',
        function($scope, $modal, $location, $http) {
            // TODO Test more than checking whether $modal.open was called
            $scope.openStory = function() {
                $modal.open({
                    templateUrl: '/assets/html/stories.tmpl.html',
                    controller: 'storiesCtrl',
                    resolve: {
                        data: function() {
                            return {};
                        }
                    }
                });
            };
            // TODO Test
            $scope.openCompositeClass = function() {
                openCompositeClass($modal);
            };
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
                $scope.openRunnerSelector().result.then(function(data) {
                    var storyPaths = [];
                    data.dirs.forEach(function(dir) {
                        storyPaths.push({path: dir.path + "/**/*.story"});
                    });
                    data.stories.forEach(function(story) {
                        storyPaths.push({path: story.path});
                    });
                    openRunnerParametersModal($modal).result.then(function (data) {
                        var classes = data;
                        $http.get('/composites').then(function(response) {
                            var composites = response.data;
                            $scope.run({
                                storyPaths: storyPaths,
                                classes: classes,
                                composites: composites
                            });
                        });
                    });
                });
            };
            // TODO Test
            $scope.run = function(json) {
                $http.post('/runner/run.json', json).then(function (response) {
                    var data = response.data;
                    if (data.status !== 'OK') {
                        openErrorModal($modal, data);
                    } else {
                        $location.path('/page/reports/' + data.reportsPath);
                    }
                }, function (response) {
                    openErrorModal($modal, response.data);
                });
            };
            $scope.getTitle = function() {
                var path = $location.path();
                if (path.indexOf(getViewStoryUrl()) === 0) {
                    return 'View Story';
                } else if (path.indexOf(getNewStoryUrl()) === 0) {
                    return 'New Story';
                } else if (path.indexOf(getCompositesUrl()) === 0) {
                    return 'Composites';
                } else if (path.indexOf('/page/reports/') === 0) {
                    return 'Reports';
                } else {
                    return '';
                }
            };
        }
    ]);
