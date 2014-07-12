angular.module('topMenuModule', [])
    .controller('topMenuController', function($scope, $modal, $location, TcBddService) {
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
        $scope.openCompositeClass = function() {
            TcBddService.openCompositeClass($modal);
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
    });
