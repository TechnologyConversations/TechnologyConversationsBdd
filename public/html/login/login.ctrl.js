angular.module('loginModule', [])
    .controller('loginCtrl', function($scope, $location, TcBddService) {
        $scope.init = function () {
            $scope.users = [];
        };
        $scope.cssClass = TcBddService.cssClass;
        $scope.buttonCssClass = TcBddService.buttonCssClass;
        $scope.canLogin = function () {
            return $scope.loginForm.$valid;
        };
        // TODO Remove when real solution is done
        $scope.register = function() {
            $scope.users.push($scope.user);
            $scope.user = {};
        };
        // TODO Remove when real solution is done
        $scope.delete = function() {
            angular.forEach($scope.users, function(user, index) {
                if (user.username === $scope.user.username) {
                    $scope.users.splice(index, 1);
                    $scope.user = {};
                }
            });
        };
        $scope.login = function() {
            $location.path('/page/loginWelcome');
        };
        $scope.init();
    })
    .controller('loginWelcomeCtrl', function($scope) {
    });
