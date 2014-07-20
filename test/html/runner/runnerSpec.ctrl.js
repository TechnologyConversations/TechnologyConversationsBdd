describe('runnerModule', function() {

    var scope, modal, service;

    beforeEach(module('ngCookies', 'runnerModule', 'storiesModule'));

    beforeEach(
        inject(function($rootScope, TcBddService) {
            service = TcBddService;
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
            inject(function($controller, $httpBackend, $http, $location) {
                httpBackend = $httpBackend;
                $controller('runnerCtrl', {
                    $scope: scope,
                    $modal: modal,
                    $modalInstance: modalInstance,
                    $http: $http,
                    $location: $location
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
            it('should set showApi to false', function() {
                expect(scope.showApi).toEqual(false);
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
                reportsPath: '1234/view/reports.html'
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
                spyOn(service, 'openErrorModal');
                httpBackend.expectPOST('/runner/run.json').respond(400, '');
                scope.run({});
                httpBackend.flush();
                expect(service.openErrorModal).toHaveBeenCalled();
            });
            it('should open error modal in case of a status different than OK', function() {
                spyOn(service, 'openErrorModal');
                httpBackend.expectPOST('/runner/run.json').respond({status: 'NOK'});
                scope.run({});
                httpBackend.flush();
                expect(service.openErrorModal).toHaveBeenCalled();
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
                spyOn(service, 'openErrorModal');
                httpBackend.expectPOST('/runner/run.json').respond(400, '');
                scope.run({});
                httpBackend.flush();
                expect(service.openErrorModal).toHaveBeenCalled();
                expect(scope.storyRunnerInProgress).toEqual(false);
            });
            it('should set storyRunnerSuccess to false after failure', function() {
                spyOn(service, 'openErrorModal');
                httpBackend.expectPOST('/runner/run.json').respond(400, '');
                scope.run({});
                httpBackend.flush();
                expect(service.openErrorModal).toHaveBeenCalled();
                expect(scope.storyRunnerSuccess).toEqual(false);
            });
            it('should set reportsUrl', function() {
                httpBackend.expectPOST('/runner/run.json').respond(runResponse);
                scope.run({});
                httpBackend.flush();
                expect(scope.reportsUrl).toEqual('/api/v1/reporters/get/' + runResponse.reportsPath);
            });
        });

        describe('getRunnerStatusCss function', function() {
            it('should use general getRunnerStatusCss function', function() {
                spyOn(service, 'getRunnerStatusCss');
                scope.getRunnerStatusCss();
                expect(service.getRunnerStatusCss).toHaveBeenCalledWith(
                    scope.storyRunnerInProgress,
                    scope.storyRunnerSuccess,
                    (scope.pendingSteps > 0)
                );
            });
        });

        describe('getStoryRunnerStatusText function', function() {
            it('should return "Stories run is in progress" when storyRunnerInProgress is true', function() {
                scope.storyRunnerInProgress = true;
                expect(scope.getStoryRunnerStatusText()).toEqual('Stories run is in progress');
            });
            it('should return "Stories run is finished" when storyRunnerInProgress is false', function() {
                scope.storyRunnerInProgress = false;
                expect(scope.getStoryRunnerStatusText()).toEqual('Stories run is finished');
            });
        });

        describe('getRunnerProgressCss function', function() {
            it('should use general getRunnerProgressCss function', function() {
                spyOn(service, 'getRunnerProgressCss');
                scope.getRunnerProgressCss();
                expect(service.getRunnerProgressCss).toHaveBeenCalledWith(scope.storyRunnerInProgress);
            });
        });

        describe('apiUrl function', function() {
            it('should return the API url', function() {
                expect(scope.apiUrl()).toMatch('/runner/run.json');
            });
        });

    });

    describe('runnerSelectorCtrl controller', function() {

        var httpBackend, modal, modalInstance, service;
        var filesWithoutPath = {status: 'OK', files: 'filesWithoutPath'};

        beforeEach(
            inject(function($controller, $httpBackend, $http, TcBddService) {
                service = TcBddService;
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

        describe('by default', function() {
            it('dirs should be set to an empty array', function() {
                expect(scope.files.dirs.length).toEqual(0);
            });
            it('stories should be set to an empty array', function() {
                expect(scope.files.stories.length).toEqual(0);
            });
        });

        describe('openDir function', function() {
            it('should call the service function openDir', function() {
                var path = 'my/path';
                spyOn(service, 'openDir');
                scope.openDir(path);
                expect(service.openDir).toHaveBeenCalledWith(scope, path);
            });
        });

        describe('getStories function', function() {
            it('should be called by the controller with the empty path', function() {
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

        describe('canContinue function', function() {
            it('should return false when there is NO directory or story selected', function() {
                scope.files = {
                    dirs: [{name: 'dir1', selected: false}],
                    stories: [{name: 'story1', selected: false}]
                };
                expect(scope.canContinue()).toEqual(false);
            });
            it('should return true when there is at least one directory selected', function() {
                scope.files = {
                    dirs: [{name: 'dir1', selected: true}],
                    stories: [{name: 'story1', selected: false}]
                };
                expect(scope.canContinue()).toEqual(true);
            });
            it('should return true when there is at least one story selected', function() {
                scope.files = {
                    dirs: [{name: 'dir1', selected: false}],
                    stories: [{name: 'story1', selected: true}]
                };
                expect(scope.canContinue()).toEqual(true);
            });
        });

    });

    describe('runnerParamsCtrl controller', function() {

        var modalInstance, data, cookieStore, scope, clazz, classes, value;
        var paramWithValue, paramWithoutValue;

        beforeEach(
            inject(function($rootScope, $injector, $controller) {
                scope = $rootScope.$new();
                value = 'myValue';
                paramWithValue = {key: 'key1', value: value};
                paramWithoutValue = {key: 'key2', value: ''};
                clazz = {
                    fullName: 'full.name.of.the.class',
                    params: [paramWithValue, paramWithoutValue, {key: 'key3', value: ''}]
                };
                classes = [clazz];
                data = {classes: classes};
                cookieStore = $injector.get('$cookieStore');
                modalInstance = {
                    dismiss: jasmine.createSpy('modalInstance.dismiss'),
                    close: jasmine.createSpy('modalInstance.close')
                };
                $controller('runnerParamsCtrl', {
                    $scope: scope ,
                    $modalInstance: modalInstance,
                    $cookieStore: cookieStore,
                    data: data,
                    showGetApi: true
                });
            })
        );

        describe('by default', function() {
           it('should return 0 if the paramArray is empty', function() {
                expect(scope.paramArray.length).toEqual(0);
           });
        });

        describe('setParams function', function() {
            var expected;
            var cookieValue = 'myCookieValue';
            beforeEach(function() {
                expected = angular.copy(classes);
            });
            it('should put classes with values from cookies to the scope', function() {
                cookieStore.put(clazz.fullName + "." + paramWithoutValue.key, cookieValue);
                scope.setParams();
                expect(paramWithoutValue.value).toEqual(cookieValue);
            });
            it('should use cookies only when value is empty', function() {
                scope.setParams();
                expect(paramWithValue.value).toEqual(value);
            });
            it('should add disabled to params that have the value', function() {
                scope.setParams();
                expect(paramWithValue.disabled).toEqual(true);
            });
            it('should add disabled false to params that do NOT have the value', function() {
                scope.setParams();
                expect(paramWithoutValue.disabled).toEqual(false);
            });
        });

        describe('hasOptions function', function() {
           it('should return true if the options array in the parameter has at least one element', function() {
				var optionsEntry = ["option1","option2","option3"];
				expect(scope.hasOptions(optionsEntry)).toEqual(true);
           });
            it('should return false if the parameter DOES NOT have any element in the options array', function() {
                var optionsEntry = [];
                expect(scope.hasOptions(optionsEntry)).toEqual(false);
            });
           it('should return false if the options array in the parameter is not defined', function() {
                var optionsEntry = undefined;
                expect(scope.hasOptions(optionsEntry)).toEqual(false);
           });
        });

		
        describe('hasParams function', function() {
            it('should return true if it contains at least one parameter', function() {
                var classEntry = {params: [{param: "param1"}, {param: "param2"}]};
                expect(scope.hasParams(classEntry)).toEqual(true);
            });
            it('should return false if it does NOT contain parameters', function() {
                var classEntry = {params: []};
                expect(scope.hasParams(classEntry)).toEqual(false);
            });
        });

        describe('paramElementId function', function() {
            it('should return first letter of the className as lower case', function() {
                var actual = scope.paramElementId("ThisIsClassName", "thisIsParamKey");
                expect(actual).toMatch(/thisIsClassName/);
            });
            it('should return first letter of the paramKey as upper case', function() {
                var actual = scope.paramElementId("ThisIsClassName", "thisIsParamKey");
                expect(actual).toMatch(/ThisIsParamKey/);
            });
        });

        describe('cancel function', function () {
            it('should dismiss the modal', function() {
                scope.cancel();
                expect(modalInstance.dismiss).toHaveBeenCalledWith('cancel');
            })
        });

        describe('ok function', function() {
            it('should close the modal and return data', function() {
                scope.ok();
                expect(modalInstance.close).toHaveBeenCalledWith({action: 'run', classes: classes});
            });
        });

        describe('showGetApi function', function() {
            it('should return showGetApi value', function() {
                expect(scope.showGetApi()).toEqual(true);
            });
        });

        describe('getApi function', function() {
            it('should close the modal and return data with action set to api', function() {
                scope.getApi();
                expect(modalInstance.close).toHaveBeenCalledWith({action: 'api', classes: classes});
            });
        });

    });

});
