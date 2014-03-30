angular.module('compositesModule', [])
    .controller('compositesCtrl', ['$scope', '$http', 'composites',
        function($scope, $http, composites) {
            $scope.newComposite = function() {
                $scope.composite = {};
            };
//            $scope.newComposite();
            $scope.composites = composites;
            $scope.classNamePattern = classNamePattern;
            $scope.stepTextPattern = function() {
                return (/^[Given|When|Then].*$/);
            };
            // TODO Test
            $scope.cssClass = cssClass;
            $scope.openComposite = function(composite) {
                $scope.composite = composite;
            };
        }
    ]);