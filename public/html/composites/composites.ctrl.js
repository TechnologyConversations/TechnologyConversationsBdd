angular.module('compositesModule', [])
    .controller('compositesCtrl', ['$scope', '$http', '$modal', '$location', '$cookieStore', 'compositesClass', 'steps',
        function($scope, $http, $modal, $location, $cookieStore, compositesClass, steps) {
            $scope.addNewComposite = function() {
                $scope.composite = {stepText: '', compositeSteps: [{}]};
                $scope.compositesClass.composites.push($scope.composite);
            };
            $scope.addStepTextParam = function() {
                if ($location.search().stepText !== undefined && $location.search().stepText !== '') {
                    $scope.compositesClass.composites.push({stepText: $location.search().stepText, compositeSteps: []});
                }
            };
            $scope.addNewCompositeStep = function() {
                $scope.composite.compositeSteps.push({});
            };
            $scope.setLastComposite = function() {
                if ($scope.compositesClass !== undefined) {
                    var length = $scope.compositesClass.composites.length;
                    $scope.composite = $scope.compositesClass.composites[length - 1];
                } else {
                    $scope.composite = {};
                }
            };
            $scope.compositesClass = compositesClass;
            $scope.addStepTextParam();
            $scope.originalCompositesClass = angular.copy(compositesClass);
            $scope.setLastComposite();
            $scope.steps = steps;
            $scope.classNamePattern = classNamePattern;
            $scope.stepTextPattern = stepTextPattern;
            $scope.cssClass = cssClass;
            $scope.buttonCssClass = buttonCssClass;
            $scope.openComposite = function(composite) {
                $scope.composite = composite;
            };
            $scope.newCollectionItem = newCollectionItem;
            $scope.removeCollectionElement = removeCollectionElement;
            $scope.revertCompositesClass = function() {
                $scope.compositesClass = angular.copy($scope.originalCompositesClass);
                $scope.composite = $scope.compositesClass.composites[0];
            };
            $scope.canRevertCompositesClass = function() {
                return !angular.equals($scope.compositesClass, $scope.originalCompositesClass);
            };
            $scope.canSaveCompositesClass = function(ngModelController) {
                var isValid = ngModelController.$valid;
                var isUpdated = !angular.equals($scope.compositesClass, $scope.originalCompositesClass);
                var isNew = $scope.compositesClass.isNew;
                return isNew || (isValid && isUpdated);
            };
            $scope.compositesAreValid = function() {
                var isValid = true;
                angular.forEach($scope.compositesClass.composites, function(element) {
                    if (element.compositeSteps === undefined || element.compositeSteps.length === 0) {
                        isValid = false;
                    }
                });
                return isValid;
            };
            $scope.saveCompositesClass = function() {
                $http.put('/composites', $scope.compositesClass).then(function() {
                    $scope.deleteCompositesClassWithoutConfirmation();
                    $location.path('/page/composites/' + $scope.compositesClass.package + '.' + $scope.compositesClass.class);
                    $scope.compositesClass.isNew = false;
                    $scope.originalCompositesClass = angular.copy($scope.compositesClass);
                    $cookieStore.put('compositeClass', $scope.compositesClass.class);
                }, function(response) {
                    openErrorModal($modal, response.data);
                });
            };
            $scope.canDeleteCompositesClass = function() {
                return (!$scope.compositesClass.isNew);

            };
            $scope.deleteCompositesClassWithoutConfirmation = function() {
                var packageName = $scope.compositesClass.package;
                var className = $scope.compositesClass.class;
                var originalPackageName = $scope.originalCompositesClass.package;
                var originalClassName = $scope.originalCompositesClass.class;
                if (packageName !== originalPackageName || className !== originalClassName) {
                    $http.delete('/composites/' + originalPackageName + '.' + originalClassName).then(function () {
                    }, function (response) {
                        openErrorModal($modal, response.data);
                    });
                }
            };
            $scope.deleteCompositesClass = function() {
                var message = {status: 'Delete Composites Class', message: 'Are you sure you want to delete this composites class?'};
                var okModal = openConfirmationModal($modal, message);
                okModal.result.then(function() {
                    var fullClassName = $scope.originalCompositesClass.package + '.' + $scope.originalCompositesClass.class;
                    $http.delete('/composites/' + fullClassName).then(function() {
                        $location.path('/');
                    }, function(response) {
                        openErrorModal($modal, response.data);
                    });
                }, function() {
                    // Do nothing
                });
            };
            $scope.saveCompositesText = function() {
                if ($scope.compositesClass.isNew) {
                    return 'Create New Composites';
                } else {
                    return 'Update Composites';
                }
            };
        }
    ]);