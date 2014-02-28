angular.module('storiesModule', ['ngRoute', 'ui.bootstrap', 'ui.sortable'])
    .config(function($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true);
        $routeProvider
            .when(getNewStoryUrl(), {
                templateUrl: '/assets/html/story.tmpl.html',
                controller: 'storyCtrl',
                resolve: {
                    story: function($route, $http, $modal) {
                        return getJson($http, $modal, '/stories/story.json');
                    }
                }
            })
            .when(getNewStoryUrl() + ':path*', {
                templateUrl: '/assets/html/story.tmpl.html',
                controller: 'storyCtrl',
                resolve: {
                    story: function($route, $http, $modal) {
                        return getJson($http, $modal, '/stories/story.json?path=' + $route.current.params.path + '.story');
                    }
                }
            })
            .when(getViewStoryUrl() + ':path*', {
                templateUrl: '/assets/html/story.tmpl.html',
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
            $modalInstance.close('ok');
        };
        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    })
    .controller('topMenuController', function($scope, $modal) {
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
    })
    .controller('storiesCtrl', function($scope, $http, $modal, $modalInstance, $location) {
        $scope.rootPath = "";
        updateData("");
        $scope.ok = function() {
            $modalInstance.close();
        };
        $scope.openDir = function(path) {
            if (path === '..') {
                var dirs = $scope.rootPath.split('/');
                $scope.rootPath = dirs.slice(0, dirs.length - 2).join('/');
                if ($scope.rootPath !== '') {
                    $scope.rootPath += '/';
                }
                updateData('');
            } else {
                updateData(path);
            }
        };
        $scope.viewStoryUrl = function(name) {
            return getViewStoryUrl() + $scope.rootPath + name;
        };
        $scope.allowToPrevDir = function() {
            return $scope.rootPath !== "";
        };
        $scope.deleteStory = function(name) {
            var path = $scope.rootPath + name + '.story';
            deleteStory($modal, $http, $location, path);
            $scope.ok();
        };
        $scope.getNewStoryUrl = function() {
            return getNewStoryUrl();
        }
        function updateData(path) {
            $http.get('/stories/list.json?path=' + $scope.rootPath + path).then(function(response) {
                $scope.files = response.data;
                if (path !== '') {
                    $scope.rootPath += path + '/';
                }
            }, function(response) {
                openErrorModal($modal, response.data);
            });
        }
    })
    .controller('storyCtrl', function($scope, $http, $modal, $location, story) {
        var originalStory = angular.copy(story);
        $scope.story = story;
        var storyExtension = ".story";
        var pathArray = $scope.story.path.split('/');
        if ($scope.story.path.indexOf(storyExtension) === $scope.story.path.length - storyExtension.length) {
            $scope.dirPath = pathArray.slice(1, pathArray.length - 1).join('/') + '/';
        } else {
            $scope.dirPath = pathArray.slice(1, pathArray.length - 1).join('/') + '/';
        }
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
                $scope.story.path = $scope.dirPath + $scope.story.name + ".story";
                if ($scope.action === 'POST') {
                    var strippedPathArray = $scope.dirPath.split('/');
                    var strippedPath = strippedPathArray.slice(0, strippedPathArray.length - 1).join('/');
                    if (strippedPath !== '') {
                        strippedPath += '/';
                    }
                    $http.post('/stories/story.json', $scope.story).then(function() {
                        $location.path(getViewStoryUrl() + strippedPath + $scope.story.name);
                    }, function(response) {
                        openErrorModal($modal, response.data);
                    });
                } else {
                    if ($scope.story.name !== originalStory.name) {
                        $scope.story.originalName = originalStory.name;
                    }
                    $http.put('/stories/story.json', $scope.story).then(function() {
                        originalStory = angular.copy($scope.story);
                    }, function(response) {
                        openErrorModal($modal, response.data);
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
        $scope.canDeleteStory = function() {
            return $scope.action === 'PUT';
        };
        $scope.deleteStory = function() {
            var path = $scope.dirPath + $scope.story.name + '.story';
            deleteStory($modal, $http, $location, path);
        };
    });

function getJson($http, $modal, url) {
    return $http.get(url).then(function(response) {
        return response.data;
    }, function(response) {
        openErrorModal($modal, response.data);
    });
}

function openErrorModal($modal, data) {
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

function openConfirmationModal($modal, data) {
    return $modal.open({
        templateUrl: '/assets/html/confirmationModal.tmpl.html',
        controller: 'modalCtrl',
        resolve: {
            data: function() {
                return data;
            }
        }
    });
}

function getViewStoryUrl() {
    return '/page/stories/view/';
}

function getNewStoryUrl() {
    return '/page/stories/new/';
}

function deleteStory($modal, $http, $location, path) {
    var message = {status: 'Delete Story', message: 'Are you sure you want to delete this story?'};
    var okModal = openConfirmationModal($modal, message);
    okModal.result.then(function() {
        $http.delete('/stories/story/' + path).then(function() {
            $location.path(getNewStoryUrl());
        }, function(response) {
            openErrorModal($modal, response.data);
        });
    }, function() {
        // Do nothing
    });
}