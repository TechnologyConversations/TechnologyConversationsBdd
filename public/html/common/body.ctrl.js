angular.module('bodyModule', ['ngJoyRide'])
    .controller('bodyCtrl', function ($scope, $http, $modal, $location, TcBddService, $rootScope) {
        $scope.onFinishJoyRide = function() {
            TcBddService.onFinishJoyRide($scope);
        };
        $scope.startJoyRide = function(id) {
            TcBddService.startJoyRide(id, $scope);
        };
        $scope.startJoyRideOnLoad = function() {
            if ($location.search().tour !== undefined) {
                $scope.startJoyRide('tour_' + $location.search().tour);
            }
        };
        // TODO Test more than checking whether $modal.open was called
        $scope.openStory = function() {
            $modal.open({
                templateUrl: '/assets/html/stories.tmpl.html',
                controller: 'storiesCtrl',
                resolve: {
                    data: function() {
                        return {};
                    },
                    features: function() {
                        return {deleteStory: $scope.features.deleteStory};
                    }
                }
            });
        };
        $scope.openCompositeClass = function() {
            TcBddService.openCompositeClass();
        };
        $scope.openModal = function() {
            if ($location.search().openModal !== undefined) {
                var menu = $location.search().openModal;
                if (menu === 'openStory') {
                    $scope.openStory();
                } else if (menu === 'openCompositeClass') {
                    $scope.openCompositeClass();
                }
            }
        };
        $scope.loadFeatures = function() {
            $http.get('/api/v1/data/features').then(function(response) {
                $scope.features = response.data.data;
                $scope.openModal();
            }, function() {
                $scope.features = [];
            });
        };
        $scope.loadFeatures();
        $scope.startJoyRideOnLoad();
    });
