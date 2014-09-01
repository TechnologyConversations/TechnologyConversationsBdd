angular.module('tourModule', [])
    .controller('tourCtrl', function ($scope, $location, TcBddService) {
        $scope.onLoad = function() {
            TcBddService.startJoyRideOnLoad($location, $scope);
        };
        $scope.onFinishJoyRide = function() {
            TcBddService.onFinishJoyRide($scope);
        };
        $scope.startJoyRide = function(id) {
            TcBddService.startJoyRide(id, $scope);
        };
        $scope.onLoad();
    });
