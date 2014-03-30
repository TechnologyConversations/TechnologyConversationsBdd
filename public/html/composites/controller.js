angular.module('compositesModule', [])
    .controller('compositesCtrl', ['$scope', '$http', 'composites',
        function($scope, $http, composites) {
            $scope.composites = composites;
            $scope.classNamePattern = classNamePattern;
            // TODO Test
            $scope.cssClass = cssClass;
//            $scope.openCompositeClass = function(package, className) {
//                $http.get('/composites/' + package + "." + className).then(function() {
//                    console.log('111');
//                }, function(response) {
//                    openErrorModal($modal, response.data);
//                });
//            };
        }
    ]);