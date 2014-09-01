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
            it('should call openModal function', function() {
                spyOn(scope, 'openModal');
                httpBackend.expectGET(featuresUrl).respond(featuresResponseJson);
                scope.loadFeatures();
                httpBackend.flush();
                expect(scope.openModal).toHaveBeenCalled();
            });
        });

        describe('openStory function', function() {
            it('should call open on modal', function() {
                scope.openStory();
                expect(modal.open).toHaveBeenCalled();
            });
        });

        describe('openModal function', function() {
            it('should call openStory function when search contains openModal=openStory', function() {
                location.search('openModal', 'openStory');
                spyOn(scope, 'openStory');
                scope.openModal();
                expect(scope.openStory).toHaveBeenCalled();
            });
            it('should not call openStory when search does NOT contain openModal=openStory', function() {
                spyOn(scope, 'openStory');
                scope.openModal();
                expect(scope.openStory).not.toHaveBeenCalled();
            });
            it('should call openStory function when search contains openModal=openCompositeClass', function() {
                location.search('openModal', 'openCompositeClass');
                spyOn(scope, 'openCompositeClass');
                scope.openModal();
                expect(scope.openCompositeClass).toHaveBeenCalled();
            });
        });

        describe('openCompositeClass function', function() {
            it('should call openCompositeClass service', function() {
                spyOn(service, 'openCompositeClass');
                scope.openCompositeClass();
                expect(service.openCompositeClass).toHaveBeenCalled();
            });
        });

    });

});
