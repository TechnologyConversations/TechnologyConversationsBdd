angular.module('tourModule', [])
    .controller('tourCtrl', function ($scope) {
        $scope.startJoyRide = function() {
            $scope.$parent.startJoyRideFlag = true;
        };
    });
