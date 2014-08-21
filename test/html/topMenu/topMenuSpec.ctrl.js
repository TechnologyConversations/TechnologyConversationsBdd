describe('topMenuModule module', function() {

    beforeEach(module('topMenuModule', 'storiesModule'));

    describe('topMenuController controller', function() {

        var scope, location;

        beforeEach(
            inject(function($rootScope, $controller, $location) {
                scope = $rootScope.$new();
                location = $location;
                $controller("topMenuController", {
                    $scope: scope,
                    $location: $location
                });
            })
        );

        describe('getTitle function', function() {
            it('should return View Story when URL is /page/stories/view/', function() {
                location.path('/page/stories/view/something');
                expect(scope.getTitle()).toEqual("View Story");
            });
            it('should return Composites when URL is /page/composites/COMPOSITE', function() {
                location.path('/page/composites/COMPOSITE');
                expect(scope.getTitle()).toEqual("Composites");
            });
            it('should return Reports when URL is /page/reports/*', function() {
                location.path('/page/reports/PATH/TO/REPORTS');
                expect(scope.getTitle()).toEqual("Reports");
            });
            it('should return Login when URL is /page/login/', function() {
                location.path('/page/login/');
                expect(scope.getTitle()).toEqual("Login");
            });
            it('should return Welcome when URL is /page/loginWelcome/', function() {
                location.path('/page/loginWelcome/');
                expect(scope.getTitle()).toEqual("Welcome");
            });
            it('should return Home when URL is /page/tour/', function() {
                location.path('/page/tour/');
                expect(scope.getTitle()).toEqual("Home");
            });
        });

    });

});
