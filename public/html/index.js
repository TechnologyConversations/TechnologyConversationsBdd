angular.module('storiesModule', ['ngRoute', 'ui.bootstrap'])
    .config(function($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true);
        $routeProvider
            .when('/page/stories/new', {
                templateUrl: '/assets/html/story.html',
                controller: 'storyCtrl',
                resolve: {
                    story: function($route, $http, $modal) {
                        return getJson($http, $modal, '/stories/story.json');
                    }
                }
            })
            .when('/page/stories/:path', {
                templateUrl: '/assets/html/story.html',
                controller: 'storyCtrl',
                resolve: {
                    story: function($route, $http, $modal) {
                        return getJson($http, $modal, '/stories/story.json?path=' + $route.current.params.path + '.story');
                    }
                }
            })
            .otherwise({redirectTo: '/page/stories/new'});
    })
    .controller('modalCtrl', function ($scope, $modalInstance, data) {
        $scope.data = data;
        $scope.ok = function () {
            $modalInstance.close();
        };
    })
    .controller('storiesCtrl', function($scope, $http, $modal) {
        $http.get('/stories/list.json').then(function(response) {
            $scope.files = response.data;
        }, function(response) {
            openModal($modal, response.data);
        });
    })
    .controller('storyCtrl', function($scope, $http, $modal, story) {

        var originalStory = angular.copy(story);
        $scope.story = story;
        $scope.action = $scope.story.name === '' ? 'POST' : 'PUT';

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
            return $scope.storyForm.$valid && !angular.equals($scope.story, originalStory);
        };

        $scope.saveStory = function() {
            if ($scope.canSaveStory()) {
                if ($scope.action == 'POST') {
                    $http.post('/stories/story.json', $scope.story).then(function() {
                        $scope.action = 'PUT';
                        originalStory = angular.copy($scope.story);
                    }, function(response) {
                        openModal($modal, response.data);
                    });
                } else {
                    if ($scope.story.name != originalStory.name) {
                        $scope.story.originalName = originalStory.name;
                    }
                    $http.put('/stories/story.json', $scope.story).then(function() {
                        originalStory = angular.copy($scope.story);
                    }, function(response) {
                        openModal($modal, response.data);
                    });
                }
            }
        };

        $scope.removeElement = function(collection, index) {
            collection.splice(index, 1);
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

    });

function getJson($http, $modal, url) {
    return $http.get(url).then(function(response) {
        return response.data;
    }, function(response) {
        openModal($modal, response.data);
    });
}

function openModal($modal, data) {
    $modal.open({
        templateUrl: '/assets/html/errorModal.tmpl.html',
        controller: 'modalCtrl',
        resolve: {
            data: function() {
                return data;
            }
        }
    });
}