describe('storiesModule controllers', function() {
    var scope;

    beforeEach(module('storiesModule'));

    beforeEach(
        inject(function($injector) {
            var rootScope = $injector.get('$rootScope');
            scope = rootScope.$new();
        })
    );

    describe('config', function() {

        describe('/page/stories/new/ path', function() {

            var path = '/page/stories/new/';

            it('should have the template /assets/html/story.tmpl.html', inject(function($route) {
                expect($route.routes[path].templateUrl).toEqual('/assets/html/story.tmpl.html');
            }));

            it('should have the controller storyCtrl', inject(function($route) {
                expect($route.routes[path].controller).toBe('storyCtrl');
            }));

        });

        describe('/page/composites path', function() {

            var path = '/page/composites';

            it('should have the template /assets/html/composites.tmpl.html', inject(function($route) {
                expect($route.routes[path].templateUrl).toEqual('/assets/html/composites.tmpl.html');
            }));

            it('should have the controller compositesCtrl', inject(function($route) {
                expect($route.routes[path].controller).toBe('compositesCtrl');
            }));

        });

    });

    describe('modalCtrl controller', function() {

        var modalInstance;

        it('should put data to the scope', inject(function($controller) {
            var data = {status: 'OK'};
            $controller("modalCtrl", {$scope: scope , $modalInstance: modalInstance, data: data});
            expect(scope.data).toBe(data);
        }));

    });

    describe('runnerCtrl controller', function() {

        var modalInstance, data, cookieStore;

        beforeEach(
            inject(function($injector) {
                cookieStore = $injector.get('$cookieStore');
            }),
            data = {
                classes: [{
                    fullName: 'full.name.of.the.class',
                    params: [{key: 'key1'}, {key: 'key2'}]
                }]
            }
        );

        it('should put data to the scope', inject(function($rootScope, $controller) {
            $controller("runnerCtrl", {$scope: scope , $modalInstance: modalInstance, $cookieStore: cookieStore, data: data});
            expect(scope.data).toBe(data);
        }));

        it('should put values from cookies', inject(function($rootScope, $controller) {
            cookieStore.put(data.classes[0].fullName + "." + data.classes[0].params[0].key, 'value1');
            cookieStore.put(data.classes[0].fullName + "." + data.classes[0].params[1].key, 'value2');
            $controller("runnerCtrl", {$scope: scope , $modalInstance: modalInstance, $cookieStore: cookieStore, data: data});
            expect(scope.data.classes[0].params[0].value).toEqual('value1');
        }));
    });

});
