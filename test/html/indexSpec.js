describe('storiesModule controllers', function() {
    var scope, http, modalInstance;

    beforeEach(module('storiesModule'));

    beforeEach(
        inject(function($injector) {
            http = $injector.get('$http');
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

    describe('compositeClassesCtrl controller', function() {

        var packageName = 'composites.com.technologyconversations.bdd.steps';
        var className = 'WebStepsComposites';
        var compositeClasses = [
            {
                package: packageName,
                class: className
            }
        ];

        beforeEach(
            inject(function($controller) {
                $controller("compositeClassesCtrl", {$scope: scope, $http: http, $modalInstance: modalInstance, compositeClasses: compositeClasses});
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

//    describe('compositesCtrl controller', function() {
//
//        var composites = [
//            {
//                package: 'composites.com.technologyconversations.bdd.steps',
//                class: 'WebStepsComposites'
//            }
//        ];
////        var composite = {
////            package: 'composites.com.technologyconversations.bdd.steps',
////            class: 'WebStepsComposites',
////            composites:[{
////                composite: {
////                    stepText: 'Given this is my composite',
////                    compositeSteps:[
////                        {step: 'Given something'},
////                        {step: 'When else'},
////                        {step: 'Then OK'}
////                    ]
////                }
////            }]
////        };
//
//        it('should put composites data to the scope', inject(function($controller) {
//            $controller("compositesCtrl", {$scope: scope, $http: http, composites: composites});
//            expect(scope.composites).toBe(composites);
//        }));
////
////        it('should put data to the scope', inject(function($controller) {
////            $controller("compositesCtrl", {$scope: scope, composites: composites});
////            expect(scope.composites).toBe(composites);
////        }));
//
//    });

});

describe("common functions", function() {

    describe('classNamePattern validates that values is a valid Java class name', function() {

        it('cannot start with a number', function() {
            expect('1abc').not.toMatch(classNamePattern());
        });

        it('can use any combination of letters, digits, underscores and dollar signs', function() {
            expect('aBc').toMatch(classNamePattern());
            expect('a123').toMatch(classNamePattern());
            expect('_a').toMatch(classNamePattern());
            expect('$a').toMatch(classNamePattern());
            expect('aBc_D$23').toMatch(classNamePattern());
        });

        it('cannot use any character other than letters, digits, underscores and dollar signs', function() {
            expect('abc%').not.toMatch(classNamePattern());
            expect('ab c').not.toMatch(classNamePattern());
        });

    });

});