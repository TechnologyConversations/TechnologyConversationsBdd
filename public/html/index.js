angular.module('storiesModule', ['ngRoute', 'ngCookies', 'ui.bootstrap', 'ui.sortable'])
    .config(function($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true);
        $routeProvider
            // TODO Remove duplication in resolve
            .when(getNewStoryUrl(), {
                templateUrl: '/assets/html/story.tmpl.html',
                controller: 'storyCtrl',
                resolve: {
                    story: function($route, $http, $modal) {
                        return getJson($http, $modal, '/stories/story.json', false);
                    },
                    steps: function($route, $http, $modal) {
                        return getJson($http, $modal, '/steps/list.json', true);
                    },
                    classes: function($route, $http, $modal) {
                        return getJson($http, $modal, '/steps/classes.json', true);
                    }
                }
            })
            .when(getNewStoryUrl() + ':path*', {
                templateUrl: '/assets/html/story.tmpl.html',
                controller: 'storyCtrl',
                resolve: {
                    story: function($route, $http, $modal) {
                        return getJson($http, $modal, '/stories/story.json?path=' + $route.current.params.path + '.story', false);
                    },
                    steps: function($route, $http, $modal) {
                        return getJson($http, $modal, '/steps/list.json', true);
                    },
                    classes: function($route, $http, $modal) {
                        return getJson($http, $modal, '/steps/classes.json', true);
                    }
                }
            })
            .when(getViewStoryUrl() + ':path*', {
                templateUrl: '/assets/html/story.tmpl.html',
                controller: 'storyCtrl',
                resolve: {
                    story: function($route, $http, $modal) {
                        return getJson($http, $modal, '/stories/story.json?path=' + $route.current.params.path + '.story', false);
                    },
                    steps: function($route, $http, $modal) {
                        return getJson($http, $modal, '/steps/list.json', true);
                    },
                    classes: function($route, $http, $modal) {
                        return getJson($http, $modal, '/steps/classes.json', true);
                    }
                },
                reloadOnSearch: false
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
    .controller('runnerCtrl', function ($scope, $modalInstance, $cookieStore, data) {
        $scope.data = data;
        $scope.data.classes.forEach(function(classEntry) {
            classEntry.params.forEach(function(paramEntry) {
                paramEntry.value = $cookieStore.get(classEntry.fullName + "." + paramEntry.key);
            });
        });
        $scope.ok = function () {
            $modalInstance.close($scope.data);
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
        $scope.createDirectory = function(path) {
            var json = '{"path": "' + $scope.rootPath + path + '"}';
            $http.post('/stories/dir.json', json).then(function() {
                $scope.files.dirs.push({name: path});
            }, function(response) {
                openErrorModal($modal, response.data);
            });
        };
        $scope.getNewStoryUrl = function() {
            return getNewStoryUrl();
        };
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
    .controller('storyCtrl', function($scope, $http, $modal, $location, $cookieStore, story, steps, classes) {
        $scope.story = story;
        $scope.steps = steps;
        $scope.classes = classes;
        $scope.stepTypes = ['GIVEN', 'WHEN', 'THEN'];
        $scope.storyFormClass = 'col-md-12';
        $scope.storyRunnerClass = 'col-md-6';
        $scope.storyRunnerVisible = false;
        $scope.storyRunnerInProgress = false;
        $scope.storyRunnerSuccess = true;

        var originalStory = angular.copy(story);
        var pathArray = $scope.story.path.split('/');
        $scope.dirPath = pathArray.slice(0, pathArray.length - 1).join('/');
        if ($scope.dirPath !== '') {
            $scope.dirPath += '/';
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
                        $scope.story.originalPath = originalStory.path;
                    }
                    $http.put('/stories/story.json', $scope.story).then(function() {
                        originalStory = angular.copy($scope.story);
                    }, function(response) {
                        openErrorModal($modal, response.data);
                    });
                }
            }
        };
        $scope.canRunStory = function() {
            return $scope.storyForm.$valid && !$scope.storyRunnerInProgress;
        };
        $scope.runStory = function() {
            $scope.storyFormClass = 'col-md-6';
            $scope.storyRunnerClass = 'col-md-6';
            $scope.storyRunnerVisible = true;
            if ($scope.canRunStory()) {
                $scope.storyRunnerInProgress = true;
                $scope.saveStory();
                var runnerModal = openRunnerModal($modal, $scope.classes);
                runnerModal.result.then(function(data) {
                    data.classes.forEach(function(classEntry) {
                        classEntry.params.forEach(function(paramEntry) {
                            $cookieStore.put(classEntry.fullName + "." + paramEntry.key, paramEntry.value);
                        });
                    });
//                    var json = {
//                        storyPath: $scope.story.path,
//                        classes: data.classes
//                    };
//                    $http.post('/runner/run.json', json).then(function(response) {
//                        $scope.storyRunnerSuccess = (response.data.status === 'OK');
//                        $scope.storyRunnerInProgress = false;
//                        $http.get('/reporters/list/' + response.data.id + '.json').then(function(response) {
//                            $scope.reports = response.data.reports;
//                        }, function(response) {
//                            openErrorModal($modal, response.data);
//                        });
//                    }, function(response) {
//                        $scope.storyRunnerSuccess = false;
//                        $scope.storyRunnerInProgress = false;
//                        openErrorModal($modal, response.data);
//                    });
                }, function() {
                    // Do nothing
                });
            }
        };
        $scope.getRunnerProgressCss = function() {
            return {
                'progress progress-striped active': $scope.storyRunnerInProgress,
                'progress': !$scope.storyRunnerInProgress
            };
        };
        $scope.getRunnerStatusCss = function() {
            return {
                'progress-bar progress-bar-info': $scope.storyRunnerInProgress,
                'progress-bar progress-bar-success': $scope.storyRunnerSuccess,
                'progress-bar progress-bar-danger': !$scope.storyRunnerSuccess
            };
        };
        $scope.getStoryRunnerStatusText = function() {
            if ($scope.storyRunnerInProgress) {
                return 'Story run is in progress';
            } else if ($scope.storyRunnerSuccess) {
                return 'Story run was successful';
            } else {
                return 'Story run failed';
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
            return $scope.action === 'PUT' && !$scope.storyRunnerInProgress;
        };
        $scope.deleteStory = function() {
            var path = $scope.dirPath + $scope.story.name + '.story';
            deleteStory($modal, $http, $location, path);
        };
        $scope.stepEnterKey = function(event, collection) {
            if (event.which === 13) {
                $scope.addElement(collection, 'step');
            }
        };
    });

function getJson($http, $modal, url, cacheType) {
    return $http.get(url, {cache: cacheType}).then(function(response) {
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

function openRunnerModal($modal, data) {
    return $modal.open({
        templateUrl: '/assets/html/runner.tmpl.html',
        controller: 'runnerCtrl',
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
