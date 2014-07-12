angular.module('compositeClassesModule', [])
    .controller('compositeClassesCtrl', function($scope, $http, $modalInstance, compositeClasses, compositeStepText, TcBddService) {
        $scope.compositeClasses = compositeClasses;
        $scope.compositeStepText = compositeStepText;
        // TODO Test
        $scope.close = function() {
            $modalInstance.close();
        };
        $scope.compositeClassUrl = function(packageName, className) {
            var url = '/page/composites/';
            if (packageName !== undefined && packageName !== '') {
                url += packageName + '.';
            }
            var classNameParts;
            if (className.indexOf('/') >= 0) {
                classNameParts = className.split('/');
            } else {
                classNameParts = className.split('\\');
            }
            url += classNameParts[classNameParts.length - 1];
            if ($scope.compositeStepText !== undefined && $scope.compositeStepText !== '') {
                url += '?stepText=' + $scope.compositeStepText;
            }
            return url;
        };
        $scope.compositeClassText = function(className) {
            var classNameParts;
            if (className.indexOf('/') >= 0) {
                classNameParts = className.split('/');
            } else {
                classNameParts = className.split('\\');
            }
            var text = classNameParts[classNameParts.length - 1];
            if (text.indexOf('.') >= 0) {
                text = text.substr(0, text.lastIndexOf('.'));
            }
            return text;
        };
        $scope.classNamePattern = TcBddService.classNamePattern;
        // TODO Test
        $scope.cssClass = cssClass;
        $scope.data = {class: ''};
    });