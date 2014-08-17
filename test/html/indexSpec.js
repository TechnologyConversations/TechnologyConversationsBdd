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

    describe('TcBddService service', function() {

        var service, modal, httpBackend;

        beforeEach(
            inject(function(TcBddService, $modal, $httpBackend) {
                httpBackend = $httpBackend;
                service = TcBddService;
                modal = $modal;
            })
        );

        describe('openCompositeClass function', function() {
            var text = 'this is some random text';
            it('should call $modal.open', function() {
                spyOn(modal, 'open');
                service.openCompositeClass(text);
                expect(modal.open).toHaveBeenCalled();
            });

        });

        describe('removeCollectionElement function', function() {
            it('should remove element on specified index', function() {
                var collection = [{item: 0}, {item: 1}, {item: 2}];
                service.removeCollectionElement(collection, 1);
                expect(collection.length).toEqual(2);
                expect(collection[1]).toEqual({item: 2});
            });
        });

        describe('buttonCssClass function', function() {
            var scope, form;
            beforeEach(
                inject(function ($rootScope, $compile) {
                    scope = $rootScope.$new();
                    form = $compile('<form>')(scope);
                })
            );
            it('should return success if form is valid', function() {
                form.$invalid = false;
                form.$valid = true;
                expect(service.buttonCssClass(form)).toEqual({'btn-success': true, 'btn-danger': false});
            });
            it('should return danger if form is valid', function() {
                form.$invalid = true;
                form.$valid = false;
                expect(service.buttonCssClass(form)).toEqual({'btn-success': false, 'btn-danger': true});
            });
        });

        describe('getRunnerProgressCss function', function() {
            var inProgress;
            beforeEach(function() {
                inProgress = false;
            });
            it('should return active when story is in progress', function() {
                inProgress = true;
                expect(service.getRunnerProgressCss(inProgress)).toEqual({
                    'progress progress-striped active': true,
                    'progress': false
                });
            });
            it('should return inactive when story is in progress', function() {
                inProgress = false;
                expect(service.getRunnerProgressCss(inProgress)).toEqual({
                    'progress progress-striped active': false,
                    'progress': true
                });
            });
        });

        describe('newCollectionItem function', function() {
            it('should add new empty collection item when event is the ENTER key', function() {
                var collection = [];
                var event = {which: 13};
                service.newCollectionItem(event, collection);
                expect(collection).toEqual([{}]);
            });
            it('should NOT add new empty collection item when event is NOT the ENTER key', function() {
                var collection = [];
                var event = {which: 10};
                service.newCollectionItem(event, collection);
                expect(collection).toEqual([]);
            });
        });

        describe('getStoryRunnerStatusText function', function() {
            var inProgress, success, pendingStepsLength;
            beforeEach(function() {
                inProgress = false;
                success = false;
                pendingStepsLength = 0;
            });
            it('should return "Story run is in progress" when story runner is in progress', function() {
                inProgress = true;
                expect(service.getStoryRunnerStatusText(inProgress, success, pendingStepsLength)).toEqual('Story run is in progress');
            });
            it('should return "Story run was successful with pending steps" when story runner is NOT in progress, status is success and there are pending steps', function() {
                success = true;
                pendingStepsLength = 2;
                expect(service.getStoryRunnerStatusText(inProgress, success, pendingStepsLength)).toEqual('Story run was successful with 2 pending steps');
            });
            it('should return "Story run was successful" when story runner is NOT in progress, status is success and there are NO pending steps', function() {
                success = true;
                expect(service.getStoryRunnerStatusText(inProgress, success, pendingStepsLength)).toEqual('Story run was successful');
            });
            it('should return "Story run failed" when story runner is NOT in progress and status is NOT success', function() {
                expect(service.getStoryRunnerStatusText(inProgress, success, pendingStepsLength)).toEqual('Story run failed');
            });
        });

        describe('getRunnerStatusCss function', function() {
            var inProgress;
            var success;
            var pendingSteps;
            beforeEach(function() {
                inProgress = false;
                success = false;
                pendingSteps = false;
            });
            it('should return info if story runner is in progress', function() {
                inProgress = true;
                expect(service.getRunnerStatusCss(inProgress, success, pendingSteps)).toEqual({
                    'progress-bar progress-bar-info': true,
                    'progress-bar progress-bar-warning': false,
                    'progress-bar progress-bar-success': false,
                    'progress-bar progress-bar-danger': false
                });
            });
            it('should return warning if story runner finished and has pending steps', function() {
                success = true;
                pendingSteps = true;
                expect(service.getRunnerStatusCss(inProgress, success, pendingSteps)).toEqual({
                    'progress-bar progress-bar-info': false,
                    'progress-bar progress-bar-warning': true,
                    'progress-bar progress-bar-success': false,
                    'progress-bar progress-bar-danger': false
                });
            });
            it('should return success if story runner finished', function() {
                success = true;
                expect(service.getRunnerStatusCss(inProgress, success, pendingSteps)).toEqual({
                    'progress-bar progress-bar-info': false,
                    'progress-bar progress-bar-warning': false,
                    'progress-bar progress-bar-success': true,
                    'progress-bar progress-bar-danger': false
                });
            });
            it('should return danger if story runner finished and is not success', function() {
                expect(service.getRunnerStatusCss(inProgress, success, pendingSteps)).toEqual({
                    'progress-bar progress-bar-info': false,
                    'progress-bar progress-bar-warning': false,
                    'progress-bar progress-bar-success': false,
                    'progress-bar progress-bar-danger': true
                });
            });
        });

        describe('openRunnerParametersModal', function() {
            it('should call modal.open function', function() {
                var showGetApi = true;
                spyOn(modal, 'open');
                service.openRunnerParametersModal(showGetApi);
                expect(modal.open).toHaveBeenCalled();
            });
        });

        describe('openDir function', function() {
            var scope, http, modal, httpBackend;
            var filesWithPath = {status: 'OK', files: 'filesWithPath'};
            beforeEach(
                inject(function ($rootScope, $http, $httpBackend) {
                    scope = $rootScope.$new();
                    http = $http;
                    httpBackend = $httpBackend;
                })
            );
            it('should call getStories for the parent dir when path is ".."', function() {
                httpBackend.expectGET('/stories/list.json?path=this/is/').respond(filesWithPath);
                scope.rootPath = "this/is/dir/";
                service.openDir(scope, '..');
                httpBackend.flush();
                expect(scope.files).toEqual(filesWithPath);
            });
            it('should call getStories for the path', function() {
                httpBackend.expectGET('/stories/list.json?path=this/is/dir').respond(filesWithPath);
                scope.rootPath = "this/is/";
                service.openDir(scope, 'dir');
                httpBackend.flush();
                expect(scope.files).toEqual(filesWithPath);
            });
        });

        describe('getStories function', function() {
            var scope, http, modal, httpBackend;
            var filesWithPath = {status: 'OK', files: 'filesWithPath'};
            beforeEach(
                inject(function ($rootScope, $http, $httpBackend) {
                    scope = $rootScope.$new();
                    http = $http;
                    httpBackend = $httpBackend;
                })
            );
            it('should update files with data returned from the server', function () {
                var path = 'my_path';
                httpBackend.expectGET('/stories/list.json?path=' + path).respond(filesWithPath);
                service.getStories(scope, path);
                httpBackend.flush();
                expect(scope.files).toEqual(filesWithPath);
            });
        });

        describe('stepTextPattern function', function() {
            it('should start with Given, When or Then', function() {
                expect('Given something').toMatch(service.stepTextPattern());
                expect('When something').toMatch(service.stepTextPattern());
                expect('Then something').toMatch(service.stepTextPattern());
                expect('Give me something').not.toMatch(service.stepTextPattern());
            });
        });

        describe('openConfirmationModal function', function() {
            it('should call modal.open function', function() {
                var data = {};
                spyOn(modal, 'open');
                service.openConfirmationModal(data);
                expect(modal.open).toHaveBeenCalled();
            });
        });

        describe('cssClass function', function() {
            it('should return error when model controller is invalid', function() {
                var modelController = {
                    $invalid: true,
                    $valid: false,
                    $dirty: false
                };
                expect(service.cssClass(modelController)).toEqual({
                    'has-error': true,
                    'has-success': false
                });
            });
            it('should NOT return success when model controller is NOT dirty', function() {
                var modelController = {
                    $invalid: false,
                    $valid: true,
                    $dirty: false
                };
                expect(service.cssClass(modelController)).toEqual({
                    'has-error': false,
                    'has-success': false
                });
            });
            it('should NOT return success when model controller is dirty and invalid', function() {
                var modelController = {
                    $invalid: true,
                    $valid: false,
                    $dirty: true
                };
                expect(service.cssClass(modelController)).toEqual({
                    'has-error': true,
                    'has-success': false
                });
            });
            it('should return success when model controller is dirty and valid', function() {
                var modelController = {
                    $invalid: false,
                    $valid: true,
                    $dirty: true
                };
                expect(service.cssClass(modelController)).toEqual({
                    'has-error': false,
                    'has-success': true
                });
            });
        });

        describe('getJson function', function() {
            var expected;
            var url = '/any/url';
            var json = {any: 'json'};
            it('should return response', function() {
                httpBackend.expectGET(url).respond(json);
                service.getJson(url, true).then(function(response) {
                    expected = response;
                });
                httpBackend.flush();
                expect(expected).toEqual(json);
            });
            it('should call service function openErrorModal', function() {
                spyOn(service, 'openErrorModal');
                httpBackend.expectGET(url).respond(400, json);
                service.getJson(url, true).then(function(response) {
                    expected = response;
                });
                httpBackend.flush();
                expect(service.openErrorModal).toHaveBeenCalledWith(json);
            });
        });

        describe('openErrorModal function', function() {
            it('should call $modal.open', function() {
                var data = {};
                spyOn(modal, 'open');
                service.openErrorModal(data);
                expect(modal.open).toHaveBeenCalled();
            });
        });

    });

    describe('modalCtrl controller', function() {

        var modalInstance;
        var data = {status: 'OK'};
        beforeEach(
            inject(function($controller) {
                modalInstance = {
                    dismiss: jasmine.createSpy('modalInstance.dismiss'),
                    close: jasmine.createSpy('modalInstance.close')
                };
                $controller("modalCtrl", {
                    $scope: scope ,
                    $modalInstance: modalInstance,
                    data: data
                });
            })
        );

        it('should put data to the scope', function() {
            expect(scope.data).toBe(data);
        });

        describe('ok function', function() {
            it('should call the close function of the modal', function() {
                scope.ok();
                expect(modalInstance.close).toHaveBeenCalledWith('ok');
            });
        });

        describe('cancel function', function() {
            it('should call the dismiss function of the modal', function() {
                scope.cancel();
                expect(modalInstance.dismiss).toHaveBeenCalledWith('cancel');
            });
        });

    });

    describe('storiesCtrl controller', function() {

        var httpBackend, service, http;
        var filesWithoutPath = {status: 'OK', files: 'filesWithoutPath'};
        var deleteStory = {
            "display": true,
            "enable": false,
            "description": ""
        };

        beforeEach(
            inject(function($controller, $httpBackend, TcBddService, $http) {
                service = TcBddService;
                modalInstance = {
                    close: jasmine.createSpy('modalInstance.close')
                };
                $controller('storiesCtrl', {
                    $scope: scope,
                    $http: $http,
                    $modal: modal,
                    $modalInstance: modalInstance,
                    $location: location,
                    features: {deleteStory: deleteStory}
                });
                httpBackend = $httpBackend;
                httpBackend.expectGET('/stories/list.json?path=').respond(filesWithoutPath);
            })
        );

        describe('close function', function() {
            it('should call the close function of the modal', function() {
                scope.close();
                expect(modalInstance.close).toHaveBeenCalled();
            });
        });

        describe('getStories function', function() {
            it('should be called by the controller with the empty path', function() {
                expect(scope.files).toBeUndefined();
                httpBackend.flush();
                expect(scope.files).toEqual(filesWithoutPath);
            });
        });

        describe('allowToPrevDir function', function() {
            it('should return true when rootPath is NOT an empty string', function() {
                scope.rootPath = 'this/is/path';
                expect(scope.allowToPrevDir()).toEqual(true);
            });
            it('should return false when rootPath is an empty string', function() {
                scope.rootPath = '';
                expect(scope.allowToPrevDir()).toEqual(false);
            });
        });

        describe('openDir function', function() {
            it('should call service function openDir', function() {
                var path = 'my/path';
                spyOn(service, 'openDir');
                scope.openDir(path);
                expect(service.openDir).toHaveBeenCalledWith(scope, path);
            });
        });

        describe('classNamePattern validates that values is a valid Java class name', function() {
            it('should NOT start with a number', function() {
                expect('1abc').not.toMatch(service.classNamePattern());
            });
            it('should use any combination of letters, digits, underscores and dollar signs', function() {
                expect('aBc').toMatch(service.classNamePattern());
                expect('a123').toMatch(service.classNamePattern());
                expect('_a').toMatch(service.classNamePattern());
                expect('$a').toMatch(service.classNamePattern());
                expect('aBc_D$23').toMatch(service.classNamePattern());
            });
            it('should NOT use any character other than letters, digits, underscores and dollar signs', function() {
                expect('abc%').not.toMatch(service.classNamePattern());
                expect('ab c').not.toMatch(service.classNamePattern());
            });
        });

        describe('on load', function() {
            it('deleteStory should be added to the scope', function() {
                expect(scope.features.deleteStory).toEqual(deleteStory);
            });
        });

    });

});
