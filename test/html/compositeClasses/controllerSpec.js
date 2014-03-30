describe('compositeClassesModule', function() {

    beforeEach(module('compositeClassesModule'));

    describe('compositeClassesCtrl controller', function() {

        var scope, modalInstance;
        var packageName = 'composites.com.technologyconversations.bdd.steps';
        var className = 'WebStepsComposites';
        var compositeClasses = [
            {
                package: packageName,
                class: className
            }
        ];

        beforeEach(
            inject(function($injector, $controller) {
                scope = {};
                $controller("compositeClassesCtrl", {
                    $scope: scope,
                    $http: $injector.get('$http'),
                    $modalInstance: modalInstance,
                    compositeClasses: compositeClasses});
            })
        );

        it('should put compositeClasses data to the scope', function() {
            expect(scope.compositeClasses).toBe(compositeClasses);
        });

        it('should have compositeClassUrl method', function() {
            var url = scope.compositeClassUrl(packageName, className);
            expect(url).toEqual('/page/composites/' + packageName + "." + className);
        });

        it('classNamePattern should return common function', function() {
            expect(scope.classNamePattern().toString()).toBe(classNamePattern().toString());
        });

    });

});