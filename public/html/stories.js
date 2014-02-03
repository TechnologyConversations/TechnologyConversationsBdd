angular.module('storiesModule', [])
    .controller('storiesCtrl', function($scope, $http) {
        var storiesPromise = $http.get('/stories/json');
        storiesPromise.then(function(response) {
            $scope.dirs = response.data.dirs;
            $scope.stories = response.data.stories;
        }, function(response) {
            console.log("FAILURE!!!!")
        });
        $scope.detailsClick = function() {
            $scope.storyName = 'TODO Name'
            $scope.storyContent = 'TODO Content'
        };
    })