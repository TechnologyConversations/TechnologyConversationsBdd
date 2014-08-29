describe('tourModule', function() {

    var scope;

    beforeEach(module('tourModule', 'storiesModule'));

    describe('tourCtrl controller', function() {

        var location, service;

        beforeEach(
            inject(function($controller, $rootScope, $location, TcBddService) {
                service = TcBddService;
                location = $location;
                scope = $rootScope.$new();
                $controller('tourCtrl', {
                    $scope: scope,
                    $location: $location,
                    TcBddService: service
                });
            })
        );

        describe('onLoad function', function() {
            it('should call startJoyRideOnLoad service', function() {
                spyOn(service, 'startJoyRideOnLoad');
                scope.onLoad();
                expect(service.startJoyRideOnLoad).toHaveBeenCalledWith(location, scope);
            });
        });

        describe('onFinishJoyRide function', function() {
            it('should call onFinishJoyRide service', function() {
                spyOn(service, 'onFinishJoyRide');
                scope.onFinishJoyRide();
                expect(service.onFinishJoyRide).toHaveBeenCalledWith(scope);
            });
        });

        describe('startJoyRide function', function() {
            it('should call startJoyRide service', function() {
                var id = 'ID';
                spyOn(service, 'startJoyRide');
                scope.startJoyRide(id);
                expect(service.startJoyRide).toHaveBeenCalledWith(id, scope);
            });
        });

    });

});
