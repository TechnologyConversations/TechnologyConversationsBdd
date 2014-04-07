angular.module('compositeClassesModule', [])
    .controller('compositeClassesCtrl', ['$scope', '$http', '$modalInstance', 'compositeClasses', 'compositeStepText',
        function($scope, $http, $modalInstance, compositeClasses, compositeStepText) {
            $scope.compositeClasses = compositeClasses;
            $scope.compositeStepText = compositeStepText;
            // TODO Test
            $scope.close = function() {
                $modalInstance.close();
            };
            $scope.compositeClassUrl = function(packageName, className) {
                var url = '/page/composites/' + packageName + "." + className;
                if ($scope.compositeStepText !== undefined && $scope.compositeStepText !== '') {
                    url += '?stepText=' + $scope.compositeStepText;
                }
                return url;
            };
            $scope.classNamePattern = classNamePattern;
            // TODO Test
            $scope.cssClass = cssClass;
            $scope.data = {class: ''};
        }
    ]);