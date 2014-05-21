describe('runnerModule', function() {

    var scope, modal;

    beforeEach(module('runnerModule'));

    beforeEach(
        inject(function($rootScope) {
            scope = $rootScope.$new();
            modal = {
                open: jasmine.createSpy('modal.open')
            };
        })
    );

    describe('runnerCtrl controller', function() {

        var modalInstance, httpBackend;
        var pendingSteps = [
            "Given Web user is in the Browse Stories dialog",
            "Given something else"
        ];

        beforeEach(
            inject(function($controller, $httpBackend, $http) {
                httpBackend = $httpBackend;
                $controller('runnerCtrl', {
                    $scope: scope,
                    $modal: modal,
                    $modalInstance: modalInstance,
                    $http: $http
                });
            })
        );

        describe('init function', function() {
            beforeEach(function() {
                spyOn(scope, 'openRunner');
                scope.init();
            });
            it('should call the openRunner function', function() {
                expect(scope.openRunner).toHaveBeenCalled();
            });
            it('should set storyRunnerInProgress to false', function() {
                expect(scope.storyRunnerInProgress).toEqual(false);
            });
            it('should set storyRunnerSuccess to false', function() {
                expect(scope.storyRunnerSuccess).toEqual(false);
            });
            it('should set pendingSteps to an empty array', function() {
                expect(scope.pendingSteps.length).toEqual(0);
            });
            it('should set showRunnerProgress to false', function() {
                expect(scope.showRunnerProgress).toEqual(false);
            });
            it('should set reportsUrl to an empty string', function() {
                expect(scope.reportsUrl).toEqual('');
            });
        });

        describe('openRunnerSelector function', function() {
            it('openRunnerSelector call open on modal', function() {
                scope.openRunnerSelector();
                expect(modal.open).toHaveBeenCalled();
            });
        });

        describe('run function', function() {
            var runResponse = {
                status: 'OK',
                reportsPath: 'public/this/is/reports/path/index.html'
            };
            it('should set storyRunnerInProgress to true', function() {
                scope.run({});
                expect(scope.storyRunnerInProgress).toEqual(true);
            });
            it('should set showRunnerProgress to true', function() {
                scope.run({});
                expect(scope.showRunnerProgress).toEqual(true);
            });
            it('should set reportsUrl to an empty string', function() {
                scope.run({});
                expect(scope.reportsUrl).toEqual('');
            });
            it('should call POST on /runner/run.json', function() {
                httpBackend.expectPOST('/runner/run.json').respond(runResponse);
                scope.run({});
                httpBackend.flush();
            });
            it('should open error modal in case of an error', function() {
                httpBackend.expectPOST('/runner/run.json').respond(400, '');
                scope.run({});
                httpBackend.flush();
                expect(modal.open).toHaveBeenCalled();
            });
            it('should open error modal in case of a status different than OK', function() {
                httpBackend.expectPOST('/runner/run.json').respond({status: 'NOK'});
                scope.run({});
                httpBackend.flush();
                expect(modal.open).toHaveBeenCalled();
            });
            it('should set storyRunnerInProgress to false after success', function() {
                httpBackend.expectPOST('/runner/run.json').respond(runResponse);
                scope.run({});
                httpBackend.flush();
                expect(scope.storyRunnerInProgress).toEqual(false);
            });
            it('should set storyRunnerSuccess to true after success', function() {
                httpBackend.expectPOST('/runner/run.json').respond(runResponse);
                scope.run({});
                httpBackend.flush();
                expect(scope.storyRunnerSuccess).toEqual(true);
            });
            it('should set storyRunnerInProgress to false after failure', function() {
                httpBackend.expectPOST('/runner/run.json').respond(400, '');
                scope.run({});
                httpBackend.flush();
                expect(scope.storyRunnerInProgress).toEqual(false);
            });
            it('should set storyRunnerSuccess to false after failure', function() {
                httpBackend.expectPOST('/runner/run.json').respond(400, '');
                scope.run({});
                httpBackend.flush();
                expect(scope.storyRunnerSuccess).toEqual(false);
            });
            it('should set reportsUrl', function() {
                httpBackend.expectPOST('/runner/run.json').respond(runResponse);
                scope.run({});
                httpBackend.flush();
                expect(scope.reportsUrl).toEqual('/assets/this/is/reports/path/index.html');
            });
        });

        describe('getRunnerStatusCss function', function() {
            it('should use general getRunnerStatusCss function', function() {
                var expected = getRunnerStatusCss(
                    scope.storyRunnerInProgress,
                    scope.storyRunnerSuccess,
                    (scope.pendingSteps.length > 0));
                expect(scope.getRunnerStatusCss()).toEqual(expected);
            });
        });

        describe('getStoryRunnerStatusText function', function() {
            it('should use general getStoryRunnerStatusText function', function() {
                scope.pendingSteps = pendingSteps;
                var expected = getStoryRunnerStatusText(
                    scope.storyRunnerInProgress,
                    scope.storyRunnerSuccess,
                    scope.pendingSteps.length);
                expect(scope.getStoryRunnerStatusText()).toEqual(expected);
            });
        });

        describe('getRunnerProgressCss function', function() {
            it('should use general getRunnerProgressCss function', function() {
                var expected = getRunnerProgressCss(
                    scope.storyRunnerInProgress
                );
                expect(scope.getRunnerProgressCss()).toEqual(expected);
            });
        });

    });

    describe('runnerSelectorCtrl controller', function() {

        var httpBackend, modal, modalInstance;
        var filesWithoutPath = {status: 'OK', files: 'filesWithoutPath'};

        beforeEach(
            inject(function($controller, $httpBackend, $http) {
                modalInstance = {
                    dismiss: jasmine.createSpy('modalInstance.dismiss'),
                    close: jasmine.createSpy('modalInstance.close')
                };
                $controller('runnerSelectorCtrl', {
                    $scope: scope,
                    $http: $http,
                    $modal: modal,
                    $modalInstance: modalInstance
                });
                httpBackend = $httpBackend;
                httpBackend.expectGET('/stories/list.json?path=').respond(filesWithoutPath);
            })
        );

        describe('getStories function', function() {
            it('should be called by the controller with the empty path', function() {
                expect(scope.files).toBeUndefined();
                httpBackend.flush();
                expect(scope.files).toEqual(filesWithoutPath);
            });
        });

        describe('cancel function', function () {
            it('should dismiss the modal', function() {
                scope.cancelRunnerSelector();
                expect(modalInstance.dismiss).toHaveBeenCalledWith('cancel');
            });
        });

        describe('ok function', function () {
            it('should include elements with selected set to true', function() {
                scope.files = {
                    dirs: [{name: 'dir1', selected: true}],
                    stories: [{name: 'story1', selected: true}]
                };
                var expected = {
                    dirs: [{path: 'dir1'}],
                    stories: [{path: 'story1.story'}]
                };
                scope.okRunnerSelector();
                expect(modalInstance.close).toHaveBeenCalledWith(expected);
            });
            it('should ignore elements with selected set to false', function() {
                scope.files = {
                    dirs: [{name: 'dir1', selected: false}],
                    stories: [{name: 'story1.story', selected: false}]
                };
                var expected = {
                    dirs: [],
                    stories: []
                };
                scope.okRunnerSelector();
                expect(modalInstance.close).toHaveBeenCalledWith(expected);
            });
            it('should ignore elements that selected undefined', function() {
                scope.files = {
                    dirs: [{name: 'dir1'}],
                    stories: [{name: 'story1.story'}]
                };
                var expected = {
                    dirs: [],
                    stories: []
                };
                scope.okRunnerSelector();
                expect(modalInstance.close).toHaveBeenCalledWith(expected);
            });
            it('should use full path', function() {
                scope.rootPath = 'this/is/dir/';
                scope.files = {
                    dirs: [],
                    stories: [{name: 'story1', selected: true}]
                };
                var expected = {
                    dirs: [],
                    stories: [{path: 'this/is/dir/story1.story'}]
                };
                scope.okRunnerSelector();
                expect(modalInstance.close).toHaveBeenCalledWith(expected);
            });
            it('should close the modal with selected files', function() {
                scope.files = {
                    dirs: [{name: 'dir1', selected: true}, {name: 'dir2', selected: false}, {name: 'dir3'}],
                    stories: [{name: 'story1', selected: true}, {name: 'story2', selected: true}, {name: 'story2'}]
                };
                var expected = {
                    dirs: [{path: 'dir1'}],
                    stories: [{path: 'story1.story'}, {path: 'story2.story'}]
                };
                scope.okRunnerSelector();
                expect(modalInstance.close).toHaveBeenCalledWith(expected);
            });
        });


    });

});
