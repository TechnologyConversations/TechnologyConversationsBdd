describe('compositesModule', function() {

    beforeEach(module('compositesModule'));

    describe('compositesCtrl controller', function() {

        var scope;
        var composites = [
            {
                package: 'composites.com.technologyconversations.bdd.steps',
                class: 'WebStepsComposites'
            }
        ];

        beforeEach(
            inject(function($controller, $injector) {
                scope = {};
                $controller('compositesCtrl', {
                    $scope: scope,
                    $http: $injector.get('$http'),
                    composites: composites});
            })
        );

        it('should put composites to the scope', function () {
            expect(scope.composites).toBe(composites);
        });

        it('should return common function from classNamePattern', function() {
            expect(scope.classNamePattern().toString()).toBe(classNamePattern().toString());
        });

    });

});