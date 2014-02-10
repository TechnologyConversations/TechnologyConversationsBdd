angular.module('storiesModule', [])
    .controller('storiesCtrl', function($scope, $http) {
        $http.get('/stories/list.json').then(function(response) {
            $scope.files = response.data;
        }, function(response) {
            // TODO Log
            console.log("FAILURE!!!!");
        });
        var newStory;
        var originalStory;

        $http.get('/stories/story.json').then(function(response) {
            newStory = response.data;
            originalStory = angular.copy(newStory);
            $scope.story = angular.copy(newStory);
        }, function(response) {
            // TODO Log
            console.log("FAILURE!!!!");
        });

        $scope.detailsClick = function(storyName) {
            $http.get('/stories/story.json?path=' + storyName + '.story').then(function(response) {
                originalStory = response.data;
                $scope.story = angular.copy(originalStory);
            }, function(response) {
                // TODO Log
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
        $scope.canSaveStory = function() {
            return $scope.storyForm.$valid && !angular.equals($scope.story, originalStory)
        };
        $scope.removeElement = function(collection, index) {
            collection.splice(index, 1)
        };
        $scope.addElement = function(collection, key) {
            collection.push({key: ''});
        };
        $scope.addScenarioElement = function(collection) {
            collection.push({title: '', meta: [], steps: [], examplesTable: ''});
        };
        $scope.revertStory = function() {
            $scope.story = angular.copy(originalStory);
            $scope.storyForm.$setPristine();
        };
        $scope.canRevertStory = function() {
            return !angular.equals($scope.story, originalStory);
        };
    })