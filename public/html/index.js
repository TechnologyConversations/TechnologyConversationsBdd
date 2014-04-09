angular.module('storiesModule', [
    'ngRoute',
    'ngCookies',
    'ui.bootstrap',
    'ui.sortable',
    'configModule',
    'storyModule',
    'compositeClassesModule',
    'compositesModule'
])
    .controller('modalCtrl', ['$scope', '$modalInstance', 'data',
        function($scope, $modalInstance, data) {
            $scope.data = data;
            // TODO Test
            $scope.ok = function () {
                $modalInstance.close('ok');
            };
            // TODO Test
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }
    ])
    // TODO Test
    .controller('topMenuController', ['$scope', '$modal',
        function($scope, $modal) {
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
                openCompositeClass($modal);
            };
        }
    ])
    .controller('storiesCtrl', ['$scope', '$http', '$modal', '$modalInstance', '$location',
        function($scope, $http, $modal, $modalInstance, $location) {
            // TODO Test
            $scope.rootPath = '';
            $scope.updateData = function(path) {
                $http.get('/stories/list.json?path=' + $scope.rootPath + path).then(function(response) {
                    $scope.files = response.data;
                    if (path !== '') {
                        $scope.rootPath += path + '/';
                    }
                }, function(response) {
                    openErrorModal($modal, response.data);
                });
            };
            $scope.updateData('');
            // TODO Test
            $scope.close = function() {
                $modalInstance.close();
            };
            // TODO Test
            $scope.openDir = function(path) {
                if (path === '..') {
                    var dirs = $scope.rootPath.split('/');
                    $scope.rootPath = dirs.slice(0, dirs.length - 2).join('/');
                    if ($scope.rootPath !== '') {
                        $scope.rootPath += '/';
                    }
                    $scope.updateData('');
                } else {
                    $scope.updateData(path);
                }
            };
            // TODO Test
            $scope.viewStoryUrl = function(name) {
                return getViewStoryUrl() + $scope.rootPath + name;
            };
            // TODO Test
            $scope.allowToPrevDir = function() {
                return $scope.rootPath !== "";
            };
            // TODO Test
            $scope.deleteStory = function(name) {
                var path = $scope.rootPath + name + '.story';
                deleteStory($modal, $http, $location, path);
                $scope.ok();
            };
            // TODO Test
            $scope.createDirectory = function(path) {
                var json = '{"path": "' + $scope.rootPath + path + '"}';
                $http.post('/stories/dir.json', json).then(function() {
                    $scope.files.dirs.push({name: path});
                }, function(response) {
                    openErrorModal($modal, response.data);
                });
            };
            // TODO Test
            $scope.getNewStoryUrl = function() {
                return getNewStoryUrl();
            };
        }
    ]);

// TODO Test
function getJson($http, $modal, url, cacheType) {
    return $http.get(url, {cache: cacheType}).then(function(response) {
        return response.data;
    }, function(response) {
        openErrorModal($modal, response.data);
    });
}

// TODO Test
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

// TODO Test
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

// TODO Test
function getViewStoryUrl() {
    return '/page/stories/view/';
}

// TODO Test
function getNewStoryUrl() {
    return '/page/stories/new/';
}

// TODO Test
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

// TODO Test
function cssClass(ngModelController) {
    return {
        'has-error': ngModelController.$invalid,
        'has-success': ngModelController.$valid && ngModelController.$dirty
    };
}

function classNamePattern() {
    return (/^[a-zA-Z_$][a-zA-Z\d_$]*$/);
}