describe('bodyModule', function() {

    var scope;

    beforeEach(module('bodyModule'));

    describe('bodyCtrl controller', function() {

        beforeEach(
            inject(function($controller, $rootScope) {
                scope = $rootScope.$new();
                $controller('bodyCtrl', {
                    $scope: scope
                });
            })
        );

        describe('when loaded', function() {
            it('should have startJoyRideFlag set to false', function() {
                expect(scope.startJoyRideFlag).toEqual(false);
            });
        });

        describe('onFinishJoyRide function', function() {
            it('should set startJoyRideFlag to false', function() {
                scope.startJoyRideFlag = true;
                scope.onFinishJoyRide();
                expect(scope.startJoyRideFlag).toEqual(false);
            });
        });

        describe('startJoyRide function', function() {
            it('should set startJoyRide to true', function() {
                scope.startJoyRideFlag = false;
                scope.startJoyRide();
                expect(scope.startJoyRideFlag).toEqual(true);
            });
        });

    });

});
