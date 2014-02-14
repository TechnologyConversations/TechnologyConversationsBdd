angular.module('storiesModule', ['ngRoute'])
    .config(function($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true)
        $routeProvider
            .when('/page/stories/new', {
                templateUrl: '/assets/html/story.html',
                controller: 'storyCtrl',
                resolve: {
                    story: function($route, $http) {
                        return getJson($http, '/stories/story.json');
                    }
                }
            })
            .when('/page/stories/:path', {
                templateUrl: '/assets/html/story.html',
                controller: 'storyCtrl',
                resolve: {
                    story: function($route, $http) {
                        return getJson($http, '/stories/story.json?path=' + $route.current.params.path + '.story');
                    }
                }
            })
            .otherwise({redirectTo: '/page/stories/new'});
    })
    .controller('storiesCtrl', function($scope, $http) {
        $http.get('/stories/list.json').then(function(response) {
            $scope.files = response.data;
        }, function(response) {
            // TODO Log
            console.log("FAILURE!!!!");
        });
    })
    .controller('storyCtrl', function($scope, story) {
        var originalStory = angular.copy(story);
        $scope.story = story
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

function getJson($http, url) {
    return $http.get(url).then(function(response) {
        return response.data;
    }, function(response) {
        // TODO Log
        console.log("FAILURE!!!!");
    });
}