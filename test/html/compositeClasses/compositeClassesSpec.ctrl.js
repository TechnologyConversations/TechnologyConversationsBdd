describe('compositeClassesModule', function() {

    beforeEach(module('compositeClassesModule'));

    describe('compositeClassesCtrl controller', function() {

        var scope, modalInstance, form;
        var packageName = 'compositesClass.com.technologyconversations.bdd.steps';
        var className = 'WebStepsComposites';
        var compositeClasses = [
            {
                package: packageName,
                class: className
            }
        ];
        var compositeStepText = 'Given this is my composite';

        beforeEach(
            inject(function($rootScope, $compile, $injector, $controller, $http) {
                scope = $rootScope.$new();
                $controller("compositeClassesCtrl", {
                    $scope: scope,
                    $http: $http,
                    $modalInstance: modalInstance,
                    compositeClasses: compositeClasses,
                    compositeStepText: compositeStepText});
                form = $compile('<form>')(scope);
            })
        );

        describe('by default', function() {
            it('should put compositeClasses data to the scope', function() {
                expect(scope.compositeClasses).toBe(compositeClasses);
            });
            it('should put compositeStepText data to the scope', function() {
                expect(scope.compositeStepText).toBe(compositeStepText);
            });
        });

        describe('compositeClassUrl function', function() {
            it('should return composites URL', function() {
                scope.compositeStepText = undefined;
                var url = scope.compositeClassUrl(packageName, className);
                expect(url).toEqual('/page/composites/' + packageName + '.' + className);
            });
            it('should add compositeStepText to the URL', function() {
                var url = scope.compositeClassUrl(packageName, className);
                expect(url).toEqual('/page/composites/' + packageName + '.' + className + '?stepText=' + compositeStepText);
            });
            it('should ignore package if empty', function() {
                var url = scope.compositeClassUrl('', className);
                expect(url).toEqual('/page/composites/' + className + '?stepText=' + compositeStepText);
            });
            it('should remove directory', function() {
                var url = scope.compositeClassUrl('', 'composites/something.groovy');
                expect(url).toEqual('/page/composites/something.groovy?stepText=' + compositeStepText);
            });
            it('should remove windows directory', function() {
                var url = scope.compositeClassUrl('', 'composites\\something.groovy');
                expect(url).toEqual('/page/composites/something.groovy?stepText=' + compositeStepText);
            });
        });

        describe('compositeClassText function', function() {
            it('should remove extension', function() {
                var text = scope.compositeClassText('something.groovy');
                expect(text).toEqual('something');
            });
            it('should remove directory', function() {
                var text = scope.compositeClassText('composites/something');
                expect(text).toEqual('something');
            });
            it('should remove windows directory', function() {
                var text = scope.compositeClassText('composites\\something');
                expect(text).toEqual('something');
            });
        });

        describe('classNamePattern function', function() {
            it('should return common function', function() {
                expect(scope.classNamePattern().toString()).toBe(classNamePattern().toString());
            });
        });

    });

});