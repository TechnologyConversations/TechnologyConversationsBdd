describe('compositesModule', function() {

    beforeEach(module('compositesModule'));

    describe('compositesCtrl controller', function() {

        var scope;
        var composite = {
            package: 'composites.com.technologyconversations.bdd.steps',
            class: 'WebStepsComposites'
        };
        var composites = [composite];

        beforeEach(
            inject(function($controller, $injector) {
                scope = {};
                $controller('compositesCtrl', {
                    $scope: scope,
                    $http: $injector.get('$http'),
                    composites: composites});
            })
        );

        it('should default composite to {}', function() {
           expect(scope.composite).toBeUndefined();
        });

        it('should put composites to the scope', function () {
            expect(scope.composites).toBe(composites);
        });

        it('should return common function from classNamePattern', function() {
            expect(scope.classNamePattern().toString()).toBe(classNamePattern().toString());
        });

        describe('openComposite', function() {
            it('should set value to the composite', function() {
                scope.openComposite(composite);
                expect(scope.composite).toBe(composite);
            });
        });

        describe('stepTextPattern', function() {
            it('should start with Given, When or Then', function() {
                expect('Given something').toMatch(scope.stepTextPattern());
                expect('When something').toMatch(scope.stepTextPattern());
                expect('Then something').toMatch(scope.stepTextPattern());
                expect('Something').not.toMatch(scope.stepTextPattern());
            });
        });

        describe('newComposite', function() {
            it('to reset the composite to {}', function() {
                scope.composite = {status: 'dirty'};
                scope.newComposite();
                expect(scope.composite).toEqual({});
            });
        });

    });

});