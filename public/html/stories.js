angular.module('storiesModule', [])
    .controller('storiesCtrl', function($scope, $http) {
        $http.get('/stories/json').then(function(response) {
            $scope.files = response.data;
        }, function(response) {
            console.log("FAILURE!!!!");
        });
        $scope.detailsClick = function(storyName) {
            $http.get('/stories/json/' + storyName + ".story").then(function(response) {
                $scope.story = response.data;
            }, function(response) {
                console.log("FAILURE!!!!");
            });
        };
    })