angular.module('compositesModule', [])
    .controller('compositesCtrl', function($scope, $http, $modal, $location, $cookieStore, compositesClass, steps, TcBddService) {
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
        $scope.classNamePattern = TcBddService.classNamePattern;
        $scope.stepTextPattern = TcBddService.stepTextPattern;
        $scope.cssClass = TcBddService.cssClass;

        $scope.buttonCssClass = function(compositeClassForm, compositeForm) {
            if (!compositeClassForm.$valid) {
                return TcBddService.buttonCssClass(compositeClassForm);
            } else {
                return TcBddService.buttonCssClass(compositeForm);
            }
        };
        $scope.openComposite = function(composite) {
            $scope.composite = composite;
        };
        $scope.newCollectionItem = TcBddService.newCollectionItem;
        $scope.removeCollectionElement = TcBddService.removeCollectionElement;
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
            return (isNew || isUpdated) && isValid;
        };
        $scope.compositesAreValid = function(ngModelController) {
            var isValid = ngModelController.$valid;
            angular.forEach($scope.compositesClass.composites, function(element) {
                if (element.stepText === undefined || element.stepText === '') {
                    isValid = false;
                } else if (element.compositeSteps === undefined || element.compositeSteps.length === 0) {
                    isValid = false;
                }
            });
            return isValid;
        };
        $scope.saveCompositesClass = function() {
            $http.put('/groovyComposites', $scope.compositesClass).then(function() {
                $scope.deleteCompositesClassWithoutConfirmation();
                $location.path('/page/composites/' + $scope.compositesClass.class + '.groovy');
                $scope.compositesClass.isNew = false;
                $scope.originalCompositesClass = angular.copy($scope.compositesClass);
                $cookieStore.put('compositeClass', $scope.compositesClass.class);
            }, function(response) {
                TcBddService.openErrorModal(response.data);
            });
        };
        $scope.canDeleteCompositesClass = function() {
            return (!$scope.compositesClass.isNew);

        };
        // TODO Test
        $scope.deleteCompositesClassWithoutConfirmation = function() {
            var className = $scope.compositesClass.class;
            var originalClassName = $scope.originalCompositesClass.class;
            if (className !== originalClassName) {
                $http.delete('/groovyComposites/' + originalClassName).then(function () {
                }, function (response) {
                    TcBddService.openErrorModal(response.data);
                });
            }
        };
        // TODO Test
        $scope.deleteCompositesClass = function() {
            var message = {status: 'Delete Composites Class', message: 'Are you sure you want to delete this composites class?'};
            var okModal = TcBddService.openConfirmationModal(message);
            okModal.result.then(function() {
                $http.delete('/groovyComposites/' + $scope.originalCompositesClass.class).then(function() {
                    $location.path('/');
                }, function(response) {
                    TcBddService.openErrorModal(response.data);
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
    });