describe('compositesModule', function() {

    beforeEach(module('ngCookies', 'compositesModule'));

    describe('compositesCtrl controller', function() {

        var scope, form, httpBackend, modal, location, cookieStore;
        var composite = {stepText: 'Given precondition', compositeSteps :[{step: 'When action'},{step: 'Then result'}]};
        var anotherComposite = {stepText: 'When action', compositeSteps :[{step: 'Then result'}]};
        var newComposite = {stepText: 'Given some other precondition', compositeSteps :[{step: 'When action'},{step: 'Then result'}]};
        var packageName = 'compositesClass.com.technologyconversations.bdd.steps';
        var className = 'WebStepsComposites';
        var newClassName = 'OtherStepsComposites';
        var compositesClass;
        var newCompositesClass = {
            package: packageName,
            class: newClassName,
            composites: [composite]
        };
        var steps = {"steps":[{"type":"GIVEN","step":"Given Web address $url is opened"}]};
        var emptyComposite = {stepText: '', compositeSteps: [{}]};
        var compositeStepText = 'Given this is my composites step';
        var stepTextParam = 'Given URL params have stepText';

        beforeEach(
            inject(function($controller, $injector, $rootScope, $http, $httpBackend, $compile, $location, $cookieStore) {
                scope = $rootScope.$new();
                location = $location;
                cookieStore = $cookieStore;
                compositesClass = {
                    package: packageName,
                    class: className,
                    composites: [composite, anotherComposite]
                };
                $controller('compositesCtrl', {
                    $scope: scope,
                    $http: $http,
                    $modal: modal,
                    $location: location,
                    compositesClass: compositesClass,
                    compositeStepText: compositeStepText,
                    steps: steps
                });
                httpBackend = $httpBackend;
                form = $compile('<form>')(scope);
            })
        );

        describe('by default', function() {
            it('should default composite to the last element of compositesClass.composites', function() {
                var expected = scope.compositesClass.composites[scope.compositesClass.composites.length - 1];
                expect(scope.composite).toEqual(expected);
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
        });

        describe('setLastComposite function', function() {
            it('should default composite to the last element of compositesClass.composites', function() {
                var stepText = 'Given this is something different';
                scope.compositesClass.composites.push({stepText: stepText});
                scope.setLastComposite();
                expect(scope.composite.stepText).toEqual(stepText);
            });
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

        describe('stepTextPattern function', function() {
            it('should start with Given, When or Then', function() {
                expect('Given something').toMatch(scope.stepTextPattern());
                expect('When something').toMatch(scope.stepTextPattern());
                expect('Then something').toMatch(scope.stepTextPattern());
                expect('Give me something').not.toMatch(scope.stepTextPattern());
            });
        });

        describe('addNewComposite function ', function() {
            it('should reset the composite to {}', function() {
                scope.composite = {status: 'dirty'};
                scope.addNewComposite();
                expect(scope.composite).toEqual(emptyComposite);
            });
            it('should add new composite to compositesClass collection', function() {
                var expected = scope.compositesClass.composites.length + 1;
                scope.addNewComposite();
                var length = scope.compositesClass.composites.length;
                expect(length).toEqual(expected);
                var newComposite = scope.compositesClass.composites[length - 1];
                expect(newComposite).toEqual(emptyComposite);
            });
        });

        describe('addStepTextParam function', function() {
            it('should add query param stepText to compositeClass.composites', function(){
                location.search('stepText', stepTextParam);
                scope.addStepTextParam();
                var actual = scope.compositesClass.composites[scope.compositesClass.composites.length - 1].stepText;
                expect(actual).toEqual(stepTextParam);
            });
            it('should add query param stepText to composite', function(){
                location.search('stepText', stepTextParam);
                scope.addStepTextParam();
                scope.setLastComposite();
                var actual = scope.composite.stepText;
                expect(actual).toEqual(stepTextParam);
            });
        });

        describe('addNewCompositeStep function', function() {
            it('should add empty element to composite.compositeSteps', function() {
                var expected = scope.composite.compositeSteps.length + 1;
                scope.addNewCompositeStep();
                var length = scope.composite.compositeSteps.length;
                expect(length).toEqual(expected);
                var newStep = scope.composite.compositeSteps[length - 1];
                expect(newStep).toEqual({});
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
                scope.composite = newComposite;
                scope.revertCompositesClass();
                expect(scope.composite).toEqual(scope.compositesClass.composites[0]);
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
//            it('should return false if composites has an empty element ({})', function() {
//                form.$invalid = false;
//                form.$valid = true;
//                scope.compositesClass = newCompositesClass;
//                scope.compositesClass.composites.push({});
//                expect(scope.canSaveCompositesClass(form)).toEqual(false);
//            });
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
        describe('compositesAreValid function', function() {
            it('should return false when composite steps are not defined', function() {
                scope.compositesClass.composites.push({stepText: 'Given something'});
                expect(scope.compositesAreValid()).toEqual(false);
            });
            it('should return false when there are no composite steps', function() {
                scope.compositesClass.composites.push({stepText: 'Given something', compositeSteps: []});
                expect(scope.compositesAreValid()).toEqual(false);
            });
            it('should return true if all other conditions are fulfilled', function() {
                scope.compositesClass.composites.push({stepText: 'Given something', compositeSteps: [{steps: 'When something else'}]});
                expect(scope.compositesAreValid()).toEqual(true);
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
            it('should save composite class name as a cookie', function() {
                httpBackend.expectPUT('/composites').respond();
                scope.saveCompositesClass();
                httpBackend.flush();
                expect(cookieStore.get("compositeClass")).toEqual(className);
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