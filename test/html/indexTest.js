describe('storiesModule controllers', function() {
//        var http, modal, modalInstance, location, rootScope, scope;
    var modalInstance, cookieStore, scope;
//        var createController;

    beforeEach(module('storiesModule'));

    beforeEach(
        inject(function($injector) {
            cookieStore = $injector.get('$cookieStore');
//            http = $injector.get('$http');
//            modal = $injector.get('$modal');
//            modalInstance = $injector.get('$modalInstance');
//            location = $injector.get('$location');
            var rootScope = $injector.get('$rootScope');
            scope = rootScope.$new();
//            var $controller = $injector.get('$controller');
//            createController = function() {
//                return $controller('storiesCtrl', {
//                    '$scope': $scope
//                });
//            };
        })
    );

    describe('modalCtrl controller', function() {

        it('Should put data to the scope', inject(function($rootScope, $controller, $injector) {
                var data = {status: 'OK'};
                $controller("modalCtrl", {$scope: scope , $modalInstance: modalInstance, data: data});
                expect(scope.data).toBe(data);
        }));

    });

    describe('runnerCtrl controller', function() {

        var data = {
            classes: [{
                fullName: 'full.name.of.the.class',
                params: [{key: 'key1'}, {key: 'key2'}]
            }]
        };

        it('should put data to the scope', inject(function($rootScope, $controller, $injector) {
            $controller("runnerCtrl", {$scope: scope , $modalInstance: modalInstance, $cookieStore: cookieStore, data: data});
            expect(scope.data).toBe(data);
        }));

        it('should put values from cookies', inject(function($rootScope, $controller, $injector) {
            cookieStore.put(data.classes[0].fullName + "." + data.classes[0].params[0].key, 'value1');
            cookieStore.put(data.classes[0].fullName + "." + data.classes[0].params[1].key, 'value2');
            $controller("runnerCtrl", {$scope: scope , $modalInstance: modalInstance, $cookieStore: cookieStore, data: data});
            expect(scope.data.classes[0].params[0].value).toEqual('value1');
        }));
    });

});
