angular.module('storiesModule', [
    'ngRoute',
    'ngCookies',
    'ui.bootstrap',
    'ui.sortable',
    'configModule',
    'bodyModule',
    'topMenuModule',
    'storyModule',
    'compositeClassesModule',
    'compositesModule',
    'runnerModule'
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
    .controller('storiesCtrl', ['$scope', '$http', '$modal', '$modalInstance', '$location', '$q',
        function($scope, $http, $modal, $modalInstance, $location, $q) {
            getStories($scope, $http, $modal, '');
            $scope.openDir = function(path) {
                openDir($scope, $http, $modal, path);
            };
            // TODO Test
            $scope.close = function() {
                $modalInstance.close();
            };
            // TODO Test
            $scope.viewStoryUrl = function(name) {
                return getViewStoryUrl() + $scope.rootPath + name;
            };
            $scope.allowToPrevDir = function() {
                return $scope.rootPath !== '';
            };
            // TODO Test
            $scope.deleteStory = function(name, index) {
                var path = $scope.rootPath + name + '.story';
                deleteStory($modal, $http, $location, $q, path).then(function() {
                    $scope.files.stories.splice(index, 1);
                });
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

function getViewStoryUrl() {
    return '/page/stories/view/';
}

function getNewStoryUrl() {
    return '/page/stories/new/';
}

function getCompositesUrl() {
    return '/page/composites/';
}

// TODO Test
function deleteStory($modal, $http, $location, $q, path) {
    var deferred = $q.defer();
    var message = {status: 'Delete Story', message: 'Are you sure you want to delete this story?'};
    var okModal = openConfirmationModal($modal, message);
    okModal.result.then(function() {
        $http.delete('/stories/story/' + path).then(function() {
            $location.path(getNewStoryUrl());
            deferred.resolve('OK');
        }, function(response) {
            openErrorModal($modal, response.data);
            deferred.reject('NOK');
        });
    }, function() {
        deferred.reject('NOK');
    });
    return deferred.promise;
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