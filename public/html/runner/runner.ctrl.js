angular.module('runnerModule', [])

    .controller('runnerSelectorCtrl', ['$scope', '$http', '$modal', '$modalInstance',
        function($scope, $http, $modal, $modalInstance) {
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
                        selectedFiles.stories.push({path: $scope.rootPath + value.name});
                    }
                });
                $modalInstance.close(selectedFiles);
            };
        }
    ]);