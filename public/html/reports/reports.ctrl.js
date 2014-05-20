angular.module('reportsModule', [])
    .controller('reportsCtrl', ['$scope',
        function($scope) {
            $scope.storyRunnerInProgress = true;
            $scope.storyRunnerSuccess = false;
            $scope.pendingSteps = [];
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
        }
    ]);
