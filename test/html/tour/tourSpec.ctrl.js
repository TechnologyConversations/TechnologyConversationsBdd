describe('tourModule', function() {

    var scope;

    beforeEach(module('tourModule'));

    describe('tourCtrl controller', function() {

        beforeEach(
            inject(function($controller, $rootScope) {
                scope = $rootScope.$new();
                $controller('tourCtrl', {
                    $scope: scope,
                    $location: $location
                });
            })
        );

    });

});
