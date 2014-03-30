describe('storiesModule controllers', function() {
    var scope, http, modal, modalInstance, location;

    beforeEach(module('storiesModule'));

    beforeEach(
        inject(function($injector) {
            http = $injector.get('$http');
            location = $injector.get('$location');
            scope = {};
        })
    );

    describe('config', function() {

        describe('/page/stories/new/ path', function() {

            var path = '/page/stories/new/';
            var route;
            beforeEach(
                inject(function($route) {
                    route = $route;
                })
            );

            it('should have the template /assets/html/story.tmpl.html', function() {
                expect(route.routes[path].templateUrl).toEqual('/assets/html/story.tmpl.html');
            });

            it('should have the controller storyCtrl', function() {
                expect(route.routes[path].controller).toBe('storyCtrl');
            });

        });

    });

    describe('modalCtrl controller', function() {

        var modalInstance;
        var data = {status: 'OK'};
        beforeEach(
            inject(function($controller) {
                $controller("modalCtrl", {$scope: scope , $modalInstance: modalInstance, data: data});
            })
        );

        it('should put data to the scope', function() {
            expect(scope.data).toBe(data);
        });

    });

    describe('runnerCtrl controller', function() {

        var modalInstance, data, cookieStore;
        var cookieValue = 'value1';

        beforeEach(
            inject(function($injector, $controller) {
                cookieStore = $injector.get('$cookieStore');
                cookieStore.put(data.classes[0].fullName + "." + data.classes[0].params[0].key, cookieValue);
                cookieStore.put(data.classes[0].fullName + "." + data.classes[0].params[1].key, 'value2');
                $controller("runnerCtrl", {$scope: scope , $modalInstance: modalInstance, $cookieStore: cookieStore, data: data});
            }),
            data = {
                classes: [{
                    fullName: 'full.name.of.the.class',
                    params: [{key: 'key1'}, {key: 'key2'}]
                }]
            }
        );

        it('should put data to the scope', function() {
            expect(scope.data).toBe(data);
        });

        it('should put values from cookies', function() {
            expect(scope.data.classes[0].params[0].value).toEqual(cookieValue);
        });
    });

    describe('storiesCtrl controller', function() {

        var httpBackend;
        var filesWithoutPath = {status: 'OK', files: 'filesWithoutPath'};
        var filesWithPath = {status: 'OK', files: 'filesWithPath'};

        beforeEach(
            inject(function($controller, $httpBackend) {
                $controller('storiesCtrl', {
                    $scope: scope,
                    $http: http,
                    $modal: modal,
                    $modalInstance: modalInstance,
                    $location: location
                });
                httpBackend = $httpBackend;
                httpBackend.expectGET('/stories/list.json?path=').respond(filesWithoutPath);
            })
        );

        describe('update data', function() {

            it('should update files with data returned from the server', function() {
                httpBackend.expectGET('/stories/list.json?path=my_path').respond(filesWithPath);
                scope.updateData('my_path');
                httpBackend.flush();
                expect(scope.files).toEqual(filesWithPath);
            });

            it('should be called by the controller with the empty path', function() {
                expect(scope.files).toBeUndefined();
                httpBackend.flush();
                expect(scope.files).toEqual(filesWithoutPath);
            });

        });

    });

});

describe("common functions", function() {

    describe('classNamePattern validates that values is a valid Java class name', function() {

        it('should NOT start with a number', function() {
            expect('1abc').not.toMatch(classNamePattern());
        });

        it('should use any combination of letters, digits, underscores and dollar signs', function() {
            expect('aBc').toMatch(classNamePattern());
            expect('a123').toMatch(classNamePattern());
            expect('_a').toMatch(classNamePattern());
            expect('$a').toMatch(classNamePattern());
            expect('aBc_D$23').toMatch(classNamePattern());
        });

        it('should NOT use any character other than letters, digits, underscores and dollar signs', function() {
            expect('abc%').not.toMatch(classNamePattern());
            expect('ab c').not.toMatch(classNamePattern());
        });

    });

});