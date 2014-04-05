angular.module('compositeClassesModule', [])
    .controller('compositeClassesCtrl', ['$scope', '$http', '$modalInstance', 'compositeClasses',
        function($scope, $http, $modalInstance, compositeClasses) {
            $scope.compositeClasses = compositeClasses;
            // TODO Test
            $scope.close = function() {
                $modalInstance.close();
            };
            $scope.compositeClassUrl = function(packageName, className) {
                return '/page/composites/' + packageName + "." + className;
            };
            $scope.classNamePattern = classNamePattern;
            // TODO Test
            $scope.cssClass = cssClass;
            $scope.data = {class: ''};
        }
    ]);