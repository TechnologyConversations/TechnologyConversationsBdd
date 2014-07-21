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
