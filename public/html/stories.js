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
        $scope.getCssClass = function(ngModelController) {
            return {
                'has-error': ngModelController.$invalid,
                'has-success': ngModelController.$valid && ngModelController.$dirty
            };
        };
        $scope.getButtonCssClass = function() {
            return {
                'btn-success': $scope.storyForm.$valid,
                'btn-danger': $scope.storyForm.$invalid
            };
        };
        $scope.getButtonEnabled = function() {
            return $scope.storyForm.$dirty && $scope.storyForm.$valid
        };
        $scope.removeElement = function(collection, index) {
            collection.splice(index, 1)
        }
        $scope.addElement = function(collection, key) {
            collection.push({key: ''});
        }
    })