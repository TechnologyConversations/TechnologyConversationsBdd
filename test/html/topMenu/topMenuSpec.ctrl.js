describe('topMenuModule module', function() {

    beforeEach(module('topMenuModule', 'storiesModule'));

    describe('topMenuController controller', function() {

        var modal, scope, location;
        var service;

        beforeEach(
            inject(function($rootScope, $controller, $location, $http, TcBddService) {
                service = TcBddService;
                scope = $rootScope.$new();
                location = $location;
                modal = {
                    open: jasmine.createSpy('modal.open')
                };
                $controller("topMenuController", {
                    $scope: scope,
                    $modal: modal,
                    $location: $location,
                    $http: $http
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
            it('should return Reports when URL is /page/reports/*', function() {
                location.path('/page/reports/PATH/TO/REPORTS');
                expect(scope.getTitle()).toEqual("Reports");
            });
        });

        describe('openStory function', function() {
            it('should call open on modal', function() {
                scope.openStory();
                expect(modal.open).toHaveBeenCalled();
            });
        });

        describe('openCompositeClass function', function() {
            it('should call openCompositeClass service', function() {
                spyOn(service, 'openCompositeClass');
                scope.openCompositeClass();
                expect(service.openCompositeClass).toHaveBeenCalled();
            });
        });

    });

});
