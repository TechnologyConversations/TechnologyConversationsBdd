angular.module('bodyModule', [])
    .controller('bodyCtrl', function ($scope, $cookieStore, $location, TcBddService) {
        $scope.history = $cookieStore.get('history');
        if ($scope.history === undefined) {
            $scope.history = [];
        }
        $scope.addHistoryItem = function(text) {
            angular.forEach($scope.history, function(value, key) {
                if (value.text === text) {
                    $scope.removeHistoryItem(key);
                }
            });
            while($scope.history.length >= 10) {
                $scope.removeHistoryItem($scope.history.length - 1);
            }
            $scope.history.splice(0, 0, {text: text, url: $location.path()});
            $cookieStore.put('history', $scope.history);
        };
        $scope.removeHistoryItem = function(index) {
            TcBddService.removeCollectionElement($scope.history, index);
            $cookieStore.put('history', $scope.history);
        };
    });
