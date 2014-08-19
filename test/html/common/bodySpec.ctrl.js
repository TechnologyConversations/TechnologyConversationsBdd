describe('bodyModule', function() {

    beforeEach(module('bodyModule', 'storiesModule'));

    describe('bodyCtrl controller', function() {

        var scope, modal, location, service, httpBackend, tourResponseJson;
        var featuresResponseData, featuresResponseJson;
        var featuresUrl = '/api/v1/data/features';
        var feature1 = {
            display: true,
            enable: true,
            description: ''
        };
        var feature2 = {
            display: false,
            enable: false,
            description: 'This is feature 2 description'
        };

        beforeEach(
            inject(function($controller, $rootScope, $http, $httpBackend, $location, TcBddService) {
                modal = {
                    open: jasmine.createSpy('modal.open')
                };
                service = TcBddService;
                location = $location;
                featuresResponseData = {feature1: feature1, feature2: feature2};
                featuresResponseJson = {
                    meta: {},
                    data: featuresResponseData
                };
                httpBackend = $httpBackend;
                httpBackend.expectGET(featuresUrl).respond(featuresResponseJson);
                scope = $rootScope.$new();
                $controller('bodyCtrl', {
                    $scope: scope,
                    $http: $http,
                    $location: $location,
                    $modal: modal
                });
            })
        );

        describe('loadFeatures function', function() {
            it('should set response data to features variable', function() {
                httpBackend.expectGET(featuresUrl).respond(featuresResponseJson);
                scope.loadFeatures();
                httpBackend.flush();
                expect(scope.features).toEqual(featuresResponseData);
                expect(scope.features.feature1).toEqual(feature1);
            });
            it('should set response data to empty JSON array when error', function() {
                httpBackend.expectGET(featuresUrl).respond(400, featuresResponseJson);
                scope.loadFeatures();
                httpBackend.flush();
                expect(scope.features).toEqual([]);
            });
            it('should call openMenu function', function() {
                spyOn(scope, 'openMenu');
                httpBackend.expectGET(featuresUrl).respond(featuresResponseJson);
                scope.loadFeatures();
                httpBackend.flush();
                expect(scope.openMenu).toHaveBeenCalled();
            });
        });

        describe('startJoyRide function', function() {
            var id = 'ID';
            it('should call startJoyRide service', function() {
                spyOn(service, 'startJoyRide');
                scope.startJoyRide(id);
                expect(service.startJoyRide).toHaveBeenCalledWith(id, scope);
            });

        });

        describe('onFinishJoyRide function', function() {
            it('should call onFinishJoyRide service', function() {
                spyOn(service, 'onFinishJoyRide');
                scope.onFinishJoyRide();
                expect(service.onFinishJoyRide).toHaveBeenCalledWith(scope);
            });

        });

        describe('startJoyRideOnLoad function', function() {
            it('should call startJoyRide when search contains tour', function() {
                var id = 'navigation';
                location.search('tour', 'navigation');
                spyOn(scope, 'startJoyRide');
                scope.startJoyRideOnLoad();
                expect(scope.startJoyRide).toHaveBeenCalledWith('tour_' + id);
            });
            it('should not call startJoyRide when search does NOT contain tour', function() {
                spyOn(scope, 'startJoyRide');
                scope.startJoyRideOnLoad();
                expect(scope.startJoyRide).not.toHaveBeenCalled();
            });
        });

        describe('openStory function', function() {
            it('should call open on modal', function() {
                scope.openStory();
                expect(modal.open).toHaveBeenCalled();
            });
        });

        describe('openMenu function', function() {
            it('should call openStory function when search contains openMenu=openStory', function() {
                location.search('openMenu', 'openStory');
                spyOn(scope, 'openStory');
                scope.openMenu();
                expect(scope.openStory).toHaveBeenCalled();
            });
            it('should not call openStory when search does NOT contain openMenu=openStory', function() {
                spyOn(scope, 'openStory');
                scope.openMenu();
                expect(scope.openStory).not.toHaveBeenCalled();
            });
        });

    });

});
