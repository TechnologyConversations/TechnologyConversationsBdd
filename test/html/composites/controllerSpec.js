describe('compositesModule', function() {

    beforeEach(module('compositesModule'));

    describe('compositesCtrl controller', function() {

        var scope, form, httpBackend, modal, location;
        var composite = {stepText: 'Given precondition', compositeSteps :[{step: 'When action'},{step: 'Then result'}]};
        var newComposite = {stepText: 'Given some other precondition', compositeSteps :[{step: 'When action'},{step: 'Then result'}]};
        var packageName = 'compositesClass.com.technologyconversations.bdd.steps';
        var className = 'WebStepsComposites';
        var newClassName = 'OtherStepsComposites';
        var compositesClass = {
            package: packageName,
            class: className,
            composites: [composite]
        };
        var newCompositesClass = {
            package: packageName,
            class: newClassName,
            composites: [composite]
        };
        var steps = {"steps":[{"type":"GIVEN","step":"Given Web address $url is opened"}]};

        beforeEach(
            inject(function($controller, $injector, $rootScope, $http, $httpBackend, $compile, $location) {
                scope = $rootScope.$new();
                location = $location;
                $controller('compositesCtrl', {
                    $scope: scope,
                    $http: $http,
                    $modal: modal,
                    $location: $location,
                    compositesClass: compositesClass,
                    steps: steps
                });
                httpBackend = $httpBackend;
                form = $compile('<form>')(scope);
            })
        );

        it('should default composite to {}', function() {
           expect(scope.composite).toBeUndefined();
        });

        it('should put compositesClass to the scope variable compositesClass', function () {
            expect(scope.compositesClass).toEqual(compositesClass);
        });

        it('should put compositesClass to the scope variable originalCompositesClass', function () {
            expect(scope.originalCompositesClass).toEqual(compositesClass);
        });

        it('should put steps to the scope variable steps', function () {
            expect(scope.steps).toEqual(steps);
        });

        it('should return common function from classNamePattern', function() {
            expect(scope.classNamePattern().toString()).toBe(classNamePattern().toString());
        });

        describe('buttonCssClass function', function() {
            it('should use util function buttonCssClass', function() {
                expect(scope.cssClass).toBe(cssClass);
            });
        });

        describe('buttonCssClass', function() {
            it('should use util function buttonCssClass', function() {
                expect(scope.buttonCssClass).toBe(buttonCssClass);
            });
        });

        describe('openComposite', function() {
            it('should set value to the scope variable composite', function() {
                scope.openComposite(composite);
                expect(scope.composite).toBe(composite);
            });
        });

        describe('stepTextPattern', function() {
            it('should start with Given, When or Then', function() {
                expect('Given something').toMatch(scope.stepTextPattern());
                expect('When something').toMatch(scope.stepTextPattern());
                expect('Then something').toMatch(scope.stepTextPattern());
                expect('Give me something').not.toMatch(scope.stepTextPattern());
            });
        });

        describe('newComposite', function() {
            it('to reset the composite to {}', function() {
                scope.composite = {status: 'dirty'};
                scope.newComposite();
                expect(scope.composite).toEqual({});
            });
        });

        describe('newCollectionItem function', function() {
            it('should use util newCollectionItem function', function() {
                expect(scope.newCollectionItem).toBe(newCollectionItem);
            });
        });

        describe('removeCollectionElement function', function() {
            it('should use util removeCollectionElement function', function() {
                expect(scope.removeCollectionElement).toBe(removeCollectionElement);
            });
        });

        describe('revertCompositesClass function', function() {
            it('should revert compositesClass to originalCompositesClass', function() {
                scope.compositesClass = newCompositesClass;
                scope.revertCompositesClass();
                expect(scope.compositesClass).toEqual(scope.originalCompositesClass);
            });
            it('should revert composite to {}', function() {
                scope.composite = composite;
                scope.revertCompositesClass();
                expect(scope.composite).toEqual({});
            });
        });

        describe('canRevertCompositesClass function', function() {
            it('should return false when both compositesClass and originalCompositesClass are the same', function() {
                expect(scope.canRevertCompositesClass()).toEqual(false);
            });
            it('should return true when compositesClass are different than originalCompositesClass', function() {
                scope.compositesClass = newCompositesClass;
                expect(scope.canRevertCompositesClass()).toEqual(true);
            });
        });

        describe('canSaveCompositesClass function', function() {
            it('should return false if form is invalid', function() {
                form.$invalid = true;
                form.$valid = false;
                expect(scope.canSaveCompositesClass(form)).toEqual(false);
            });
            it('should return false if data did NOT change', function() {
                form.$invalid = false;
                form.$valid = true;
                expect(scope.canSaveCompositesClass(form)).toEqual(false);
            });
            it('should return true if isNew is set to true (the rest of criteria is ignored)', function() {
                form.$invalid = true;
                form.$valid = false;
                scope.compositesClass.isNew = true;
                expect(scope.canSaveCompositesClass(form)).toEqual(true);
            });
            it('should return true if form is valid and data did change', function() {
                form.$invalid = false;
                form.$valid = true;
                scope.compositesClass = newCompositesClass;
                expect(scope.canSaveCompositesClass(form)).toEqual(true);
            });
        });

        describe('saveCompositesClass function', function() {
            it('should send PUT request to /composites', function() {
                httpBackend.expectPUT('/composites').respond();
                scope.saveCompositesClass();
                httpBackend.flush();
            });
            it('should put compositesClass to the scope variable originalCompositesClass when response is successful', function () {
                scope.compositesClass.composites.push(newComposite);
                httpBackend.expectPUT('/composites').respond();
                scope.saveCompositesClass();
                httpBackend.flush();
                expect(scope.originalCompositesClass).toEqual(scope.compositesClass);
            });
            it('should set compositesClass.isNew to false', function() {
                httpBackend.expectPUT('/composites').respond();
                scope.saveCompositesClass();
                httpBackend.flush();
                expect(scope.compositesClass.isNew).toEqual(false);
            });
            it('should call openErrorModal function when response is NOT successful', function() {
                // TODO
            });
            it('should change location if package or class changed', function() {
                httpBackend.expectPUT('/composites').respond();
                httpBackend.expectDELETE('/composites/' + packageName + '.' + className).respond();
                scope.compositesClass.class = newClassName;
                scope.saveCompositesClass();
                httpBackend.flush();
                expect(location.path()).toEqual('/page/composites/' + packageName + '.' + newClassName);
            });
            it('should make DELETE request to /composites/FULL_CLASS_NAME when class or package change', function() {
                httpBackend.expectPUT('/composites').respond();
                httpBackend.expectDELETE('/composites/' + packageName + '.' + className).respond();
                scope.compositesClass.class = newClassName;
                scope.originalCompositesClass.class = className;
                scope.saveCompositesClass();
                httpBackend.flush();
            });
        });
        describe('canDeleteCompositesClass function', function() {
            it('should return false when compositesClass.isNew equals true', function() {
                scope.compositesClass.isNew = true;
                expect(scope.canDeleteCompositesClass()).toEqual(false);
            });
        });
        describe('deleteCompositesClass function', function() {
            // TODO
        });

    });

});