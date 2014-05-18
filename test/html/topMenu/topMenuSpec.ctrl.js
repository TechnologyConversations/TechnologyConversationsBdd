describe('topMenuModule module', function() {

    beforeEach(module('topMenuModule'));

    describe('topMenuController controller', function() {

        var modal, scope, location;

        beforeEach(
            inject(function($rootScope, $controller, $location) {
                scope = $rootScope.$new();
                location = $location;
                $controller("topMenuController", {
                    $scope: scope,
                    $modal: modal,
                    $location: $location
                });
            })
        );

        describe('getTitle function', function() {
            it('should return View Story when URL is /page/stories/view/', function() {
                location.path(getViewStoryUrl() + 'something');
                expect(scope.getTitle()).toEqual("View Story");
            });
            it('should return New Story when URL is /page/stories/view/', function() {
                location.path(getNewStoryUrl() + 'something');
                expect(scope.getTitle()).toEqual("New Story");
            });
            it('should return Composites when URL is /page/composites/COMPOSITE', function() {
                location.path('/page/composites/COMPOSITE');
                expect(scope.getTitle()).toEqual("Composites");
            });
        });

    });

});
