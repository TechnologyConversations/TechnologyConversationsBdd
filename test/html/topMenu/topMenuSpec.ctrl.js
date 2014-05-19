describe('topMenuModule module', function() {

    beforeEach(module('topMenuModule'));

    describe('topMenuController controller', function() {

        var modal, scope, location, httpBackend;

        beforeEach(
            inject(function($rootScope, $controller, $location, $http, $httpBackend) {
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
                httpBackend = $httpBackend;
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

        describe('openRunnerSelector function', function() {
            it('openRunnerSelector call open on modal', function() {
                scope.openRunnerSelector();
                expect(modal.open).toHaveBeenCalled();
            });
        });

        describe('run function', function() {
            it('should call POST on /runner/run.json', function() {
                httpBackend.expectPOST('/runner/run.json').respond({});
                scope.run({});
                httpBackend.flush();
            });
            it('should open error modal in case of an error', function() {
                httpBackend.expectPOST('/runner/run.json').respond(400, '');
                scope.run({});
                httpBackend.flush();
                expect(modal.open).toHaveBeenCalled();
            });
            it('should open error modal in case of a status different than OK', function() {
                httpBackend.expectPOST('/runner/run.json').respond({status: 'NOK'});
                scope.run({});
                httpBackend.flush();
                expect(modal.open).toHaveBeenCalled();
            });
            it('should redirect to report screen when OK', function() {
                var reportsPath = 'this/is/report/dir/index.html';
                httpBackend.expectPOST('/runner/run.json').respond({status: 'OK', reportsPath: reportsPath});
                scope.run({});
                httpBackend.flush();
                expect(location.path()).toEqual('/page/reports/' + reportsPath);
            });
        });

    });

});
