describe('bodyModule', function() {

    var scope, httpBackend, featuresResponseData, featuresResponseJson, featuresUrl, feature1, feature2;

    beforeEach(module('bodyModule'));

    describe('bodyCtrl controller', function() {

        beforeEach(
            inject(function($controller, $rootScope, $http, $httpBackend) {
                featuresUrl = '/api/v1/data/features';
                feature1 = {
                    display: true,
                    enable: true,
                    description: ''
                };
                feature2 = {
                    display: false,
                    enable: false,
                    description: 'This is feature 2 description'
                };
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
                    $http: $http
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
        });

    });

});
