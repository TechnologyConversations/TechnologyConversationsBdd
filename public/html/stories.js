angular.module('storiesModule', [])
    .controller('storiesCtrl', function($scope, $http) {
        var storiesPromise = $http.get('/stories/json');
        storiesPromise.then(function(response) {
            $scope.dirs = response.data.dirs;
            $scope.stories = response.data.stories;
        }, function(response) {
            console.log("FAILURE!!!!")
        });
        $scope.selectStory = function(story) {
            $scope.selectedStory = story;
        };
        $scope.isSelected = function(story) {
            return $scope.selectedStory === story;
        };
        $scope.detailsClick = function() {
            console.log('TODO Display story')
        }
    })
    .controller('storyCtrl', function($scope) {
        $scope.name = 'TODO Name'
        $scope.content = 'TODO Content'
    });