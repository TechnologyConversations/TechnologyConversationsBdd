describe('bodyModule', function() {

    beforeEach(module('bodyModule', 'ngCookies', 'storiesModule'));

    describe('bodyCtrl controller', function() {

        var scope;

        beforeEach(
            inject(function($rootScope, $injector, $controller) {
                scope = $rootScope.$new();
                $controller("bodyCtrl", {
                    $scope: scope
                });
            })
        );

    });

});