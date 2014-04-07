describe('configModule controllers', function() {

    beforeEach(module('ngRoute', 'configModule'));

    describe('config controller', function() {

        describe('/page/stories/new/ path', function() {

            var path = '/page/stories/new/';
            var route, httpBackend;
            beforeEach(
                inject(function($route, $httpBackend) {
                    route = $route;
                    httpBackend = $httpBackend;
                })
            );

            it('should have the template /assets/html/story/story.tmpl.html', function() {
                expect(route.routes[path].templateUrl).toEqual('/assets/html/story/story.tmpl.html');
            });

            it('should have the controller storyCtrl', function() {
                expect(route.routes[path].controller).toBe('storyCtrl');
            });

        });

    });

});