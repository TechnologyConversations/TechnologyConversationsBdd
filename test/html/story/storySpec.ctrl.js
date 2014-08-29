describe('storyModule', function() {

    var service, scope;

    beforeEach(module('ngCookies', 'storyModule', 'storiesModule'));

    beforeEach(inject(function($rootScope, TcBddService) {
        service = TcBddService;
        scope = $rootScope.$new();
    }));

    describe('storyCtrl controller', function() {

        var modal, form, story, httpBackend, location;
        var timeout;
        var steps = {status: 'OK'};
        var groovyComposites = [{path: 'this/is/path/to/composite.groovy'}];
        var pendingSteps = [
            "Given Web user is in the Browse Stories dialog",
            "Given something else"
        ];
        var reports = [
            {
                path:"/assets/jbehave/1396791774351/BeforeStories.html",
                steps:[]
            }, {
                path: "/assets/jbehave/1396789560784/public.stories.tcbdd.storiesList.html",
                steps: [
                    {text: "Then Web element modalHeader should have text Browse Stories", status: "successful"},
                    {text : pendingSteps[0], status: "pending"},
                    {text: "When Web user clicks the element tcbdd", status: "notPerformed"},
                    {text : pendingSteps[1], status: "pending"}
                ]
            }
        ];

        beforeEach(
            inject(function($controller, $httpBackend, $http, $location, $cookieStore, $timeout, $compile) {
                timeout = $timeout;
                location = $location;
                story = {
                    name: 'this is a story name',
                    path: 'this/is/path'
                };
                $controller('storyCtrl', {
                    $scope: scope,
                    $http: $http,
                    $modal: modal,
                    $location: $location,
                    $cookieStore: $cookieStore,
                    story: story,
                    steps: steps,
                    groovyComposites: groovyComposites,
                    $timeout: timeout
                });
                httpBackend = $httpBackend;
                form = $compile('<form>')(scope);
                form.$invalid = false;
                form.$valid = true;
                form.$setPristine = function() {};
                scope.storyForm = form;
            })
        );

        describe('getPendingSteps function', function() {
            it('should return a list of pending steps', function() {
                scope.setPendingSteps(reports);
                expect(scope.pendingSteps.length).toEqual(2);
            });
        });

        describe('hasPendingSteps function', function() {
            it('should return true if there are pending steps', function() {
                scope.setPendingSteps(reports);
                expect(scope.hasPendingSteps()).toBe(true);
            });
            it('should return false if there pending steps undefined', function() {
                expect(scope.hasPendingSteps()).toBe(false);
            });
            it('should return false if there are no pending steps', function() {
                scope.setPendingSteps([]);
                expect(scope.hasPendingSteps()).toBe(false);
            });
        });

        describe('getStoryRunnerStatusText function', function() {
            it('should use general getStoryRunnerStatusText function', function() {
                spyOn(service, 'getStoryRunnerStatusText');
                scope.pendingSteps = pendingSteps;
                scope.getStoryRunnerStatusText();
                expect(service.getStoryRunnerStatusText).toHaveBeenCalledWith(
                    scope.storyRunnerInProgress,
                    scope.storyRunnerSuccess,
                    scope.pendingSteps.length
                );
            });
        });

        describe('removeCollectionElement function', function() {
           it('should call the general removeCollectionElement function', function() {
               expect(scope.removeCollectionElement).toEqual(service.removeCollectionElement);
           });
        });

        describe('getRunnerProgressCss function', function() {
            it('should use general getRunnerProgressCss function', function() {
                spyOn(service, 'getRunnerProgressCss');
                scope.getRunnerProgressCss();
                expect(service.getRunnerProgressCss).toHaveBeenCalledWith(scope.storyRunnerInProgress);
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

        describe('openRunnerModal function', function() {
            it('should call service function openRunnerParametersModal', function() {
                spyOn(service, 'openRunnerParametersModal');
                scope.openRunnerModal();
                expect(service.openRunnerParametersModal).toHaveBeenCalledWith(false, scope);
            });
        });

        describe('stepEnterKey function', function() {
            it('should use global newCollectionItem function', function() {
                expect(scope.stepEnterKey).toBe(service.newCollectionItem);
            });
        });

        describe('stepTextPattern function', function() {
            it('should use service function stepTextPattern', function() {
                expect(scope.stepTextPattern()).toEqual(service.stepTextPattern());
            });
        });

        describe('canSaveStory function', function() {
            it('should return false when story is not valid', function() {
                form.$invalid = true;
                form.$valid = false;
                expect(scope.canSaveStory()).toEqual(false);
            });
            it('should return false when story has not been changed', function() {
                expect(scope.canSaveStory()).toEqual(false);
            });
            it('should return true when story has been changed and the form is valid', function() {
                scope.story = {status: 'this is new story'};
                expect(scope.canSaveStory()).toEqual(true);
            });
        });

        describe('buttonCssClass function', function() {
            it('should use global buttonCssClass function', function() {
                expect(scope.buttonCssClass).toEqual(service.buttonCssClass);
            });
        });

        describe('cssClass function', function() {
            it('should use the global cssClass function', function() {
                expect(scope.cssClass).toEqual(service.cssClass);
            });
        });

        describe('setAction function', function() {
            it('should set action to POST when new story is opened', function() {
                scope.story.name = '';
                scope.setAction();
                expect(scope.action).toEqual('POST');
            });
            it('should set action to PUT when existing story is opened', function() {
                scope.setAction();
                expect(scope.action).toEqual('PUT');
            });
        });

        describe('expandPanels function', function() {
            it('should expand all panels when run for the first time', function() {
                scope.panelsExpanded = false;
                scope.expandPanels();
                expect(scope.panels.story).toEqual(true);
                expect(scope.panels.description).toEqual(true);
                expect(scope.panels.meta).toEqual(true);
                expect(scope.panels.narrative).toEqual(true);
                expect(scope.panels.givenStories).toEqual(true);
                expect(scope.panels.lifecycle).toEqual(true);
                expect(scope.panels.scenarios).toEqual(true);
            });
            it('should contract all panels when run for the second time', function() {
                scope.panelsExpanded = true;
                scope.expandPanels();
                expect(scope.panels.story).toEqual(false);
                expect(scope.panels.description).toEqual(false);
                expect(scope.panels.meta).toEqual(false);
                expect(scope.panels.narrative).toEqual(false);
                expect(scope.panels.givenStories).toEqual(false);
                expect(scope.panels.lifecycle).toEqual(false);
            });
        });

        describe('canRunStory function', function() {
            it('should return false when form is invalid', function() {
                form.$valid = false;
                form.$invalid = true;
                expect(scope.canRunStory()).toEqual(false);
            });
            it('should return false when story runner is in progress', function() {
                scope.storyRunnerInProgress = true;
                expect(scope.canRunStory()).toEqual(false);
            });
            it('should return true when form is valid and story runner is NOT in progress', function() {
                scope.storyRunnerInProgress = false;
                expect(scope.canRunStory()).toEqual(true);
            });
        });

        describe('runStory function', function() {
            beforeEach(function() {
                spyOn(scope, 'saveStory');
                spyOn(scope, 'openRunnerParams');
            });
            it('should call saveStory', function() {
                spyOn(scope, 'canRunStory').and.returnValue(true);
                scope.runStory();
                expect(scope.saveStory).toHaveBeenCalled();
            });
            it('should call openRunnerParams', function() {
                spyOn(scope, 'canRunStory').and.returnValue(true);
                scope.runStory();
                expect(scope.openRunnerParams).toHaveBeenCalled();
            });
            it('should NOT call saveStory and openRunnerParams when canRunStory returns false', function() {
                spyOn(scope, 'canRunStory').and.returnValue(false);
                scope.runStory();
                expect(scope.saveStory).not.toHaveBeenCalled();
                expect(scope.openRunnerParams).not.toHaveBeenCalled();
            });
        });

        describe('canDeleteStory function', function() {
            it('should return false when action is NOT PUT', function() {
                scope.action = 'POST';
                scope.storyRunnerInProgress = false;
                expect(scope.canDeleteStory()).toEqual(false)
            });
            it('should return false when story runner is in progress', function() {
                scope.action = 'PUT';
                scope.storyRunnerInProgress = true;
                expect(scope.canDeleteStory()).toEqual(false)
            });
            it('should return true when action is PUT and story runner is NOT in progress', function() {
                scope.action = 'PUT';
                scope.storyRunnerInProgress = false;
                expect(scope.canDeleteStory()).toEqual(true)
            });
        });

        describe('addElement function', function() {
            it('should add an element to the collection', function() {
                var collection = [{key: 'item1'}, {key: 'item2'}];
                var expected = collection.length + 1;
                scope.addElement(collection);
                expect(collection.length).toEqual(expected);
            });
            it('should add an empty element to the collection', function() {
                var collection = [];
                scope.addElement(collection);
                expect(collection[0]).toEqual({});
            });
        });

        describe('addScenarioElement function', function() {
            it('should add empty scenario to the collection', function() {
                var scenarios = [];
                scope.addScenarioElement(scenarios);
                expect(scenarios[0]).toEqual({title: '', meta: [], steps: [], examplesTable: ''})
            });
        });

        describe('getReports function', function() {
            var responseJson;
            var reportsId = 123;
            var url = '/api/v1/reporters/list/' + reportsId;
            beforeEach(function() {
                spyOn(scope, 'setPendingSteps').and.returnValue(true);
                spyOn(service, 'openErrorModal');
                spyOn(scope, 'isStoryRunnerSuccess');
                responseJson = {
                    status: 'finished',
                    reports: [
                        {path: 'report1', steps: [{status: 'successful'}, {status: 'pending'}]},
                        {path: 'report2', steps: [{status: 'notPerformed'}]}
                    ]
                };
            });
            it('should call GET /reporters/list/[REPORTS_ID]', function() {
                httpBackend.expectGET(url).respond(responseJson);
                scope.getReports(reportsId);
                httpBackend.flush();
            });
            it('should assign GET response to reports', function() {
                httpBackend.expectGET(url).respond(responseJson);
                scope.getReports(reportsId);
                httpBackend.flush();
                expect(scope.reports).toEqual(responseJson.reports);
            });
            it('should call setPendingSteps function', function() {
                httpBackend.expectGET(url).respond(responseJson);
                scope.getReports(reportsId);
                httpBackend.flush();
                expect(scope.setPendingSteps).toHaveBeenCalled();
            });
            it('should add reportsId to reports', function() {
                httpBackend.expectGET(url).respond(responseJson);
                scope.getReports(reportsId);
                httpBackend.flush();
                expect(scope.reports.id).toBeDefined();
                expect(scope.reports.id).toEqual(reportsId);
            });
            it('should set storyRunnerInProgress to false when the response status is finished', function() {
                httpBackend.expectGET('/api/v1/reporters/list/' + reportsId).respond(responseJson);
                scope.getReports(reportsId);
                httpBackend.flush();
                expect(scope.storyRunnerInProgress).toEqual(false);
            });
            it('should set storyRunnerInProgress to true when the response status is NOT finished', function() {
                responseJson.status = 'inProgress';
                httpBackend.expectGET('/api/v1/reporters/list/' + reportsId).respond(responseJson);
                scope.getReports(reportsId);
                httpBackend.flush();
                expect(scope.storyRunnerInProgress).toEqual(true);
            });
            it('should call the openErrorModal function when the response is an error', function() {
                httpBackend.expectGET('/api/v1/reporters/list/' + reportsId).respond(400, {});
                scope.getReports(reportsId);
                httpBackend.flush();
                expect(service.openErrorModal).toHaveBeenCalled();
            });
            it('should set storyRunnerInProgress to false when the response is an error', function() {
                httpBackend.expectGET('/api/v1/reporters/list/' + reportsId).respond(400, {});
                scope.getReports(reportsId);
                httpBackend.flush();
                expect(scope.storyRunnerInProgress).toEqual(false);
            });
            it('should repeat the request until the response is an error and the message is NOT ID is NOT correct', function() {
                responseJson.status = 'inProgress';
                httpBackend.expectGET('/api/v1/reporters/list/' + reportsId).respond(responseJson);
                scope.getReports(reportsId);
                httpBackend.flush();
                httpBackend.expectGET('/api/v1/reporters/list/' + reportsId).respond(400, responseJson);
                timeout.flush();
                httpBackend.flush();
                timeout.flush();
            });
            it('should repeat the request when the response is an error and the message is ID is NOT correct', function() {
                responseJson.message = 'ID is NOT correct';
                httpBackend.expectGET('/api/v1/reporters/list/' + reportsId).respond(400, responseJson);
                scope.getReports(reportsId);
                httpBackend.flush();
                httpBackend.expectGET('/api/v1/reporters/list/' + reportsId).respond(400, responseJson);
                timeout.flush();
                httpBackend.flush();
            });
            it('should assign the isStoryRunnerSuccess function to storyRunnerSuccess when finished', function() {
                httpBackend.expectGET(url).respond(responseJson);
                scope.getReports(reportsId);
                httpBackend.flush();
                expect(scope.isStoryRunnerSuccess).toHaveBeenCalledWith(responseJson.reports);
                expect(scope.storyRunnerSuccess).toEqual(scope.isStoryRunnerSuccess());
            });
        });

        describe('onLoad function', function() {
            var reportsId = 'reportsId';
            it('should set pendingSteps to an empty array', function() {
                expect(scope.pendingSteps.length).toEqual(0);
            });
            it('should put steps to the scope', function() {
                expect(scope.steps).toEqual(steps);
            });
            it('should put story to the scope', function() {
                expect(scope.story).toEqual(story);
            });
            it('should put groovyComposites to the scope', function() {
                expect(scope.groovyComposites).toEqual(groovyComposites);
            });
            it('should put stepTypes to the scope', function() {
                expect(scope.stepTypes).toEqual(['GIVEN', 'WHEN', 'THEN']);
            });
            it('should put storyFormClass to the scope', function() {
                expect(scope.storyFormClass).toEqual('col-md-12');
            });
            it('should set storyRunnerVisible to false', function() {
                expect(scope.storyRunnerVisible).toEqual(false);
            });
            it('should set storyRunnerInProgress to false', function() {
                expect(scope.storyRunnerInProgress).toEqual(false);
            });
            it('should set storyRunnerSuccess to false', function() {
                expect(scope.storyRunnerSuccess).toEqual(true);
            });
            it('should call expandPanels function', function() {
                spyOn(scope, 'expandPanels');
                scope.onLoad();
                expect(scope.expandPanels).toHaveBeenCalled();
            });
            it('should set originalStory to story', function() {
                expect(scope.originalStory).toEqual(scope.story);
            });
            it('should call getReports when location.search contains reportsId', function() {
                location.search('reportsId', reportsId);
                spyOn(scope, 'getReports');
                scope.onLoad();
                expect(scope.getReports).toHaveBeenCalledWith(reportsId);
            });
            it('should NOT call getReports when location.search does NOT contain reportsId', function() {
                spyOn(scope, 'getReports');
                scope.onLoad();
                expect(scope.getReports).not.toHaveBeenCalled();
            });
            it('should set storyRunnerVisible to true when location.search contains reportsId', function() {
                location.search('reportsId', reportsId);
                scope.onLoad();
                expect(scope.storyRunnerVisible).toEqual(true);
            });
            it('should set storyFormClass to col-md-6 when location.search contains reportsId', function() {
                location.search('reportsId', reportsId);
                scope.onLoad();
                expect(scope.storyFormClass).toEqual('col-md-6');
            });
            it('should set storyRunnerClass to col-md-6 when location.search contains reportsId', function() {
                location.search('reportsId', reportsId);
                scope.onLoad();
                expect(scope.storyRunnerClass).toEqual('col-md-6');
            });
            it('should call openRunnerParams when location.search contains openModal=openRunStory', function() {
                spyOn(scope, 'openRunnerParams');
                location.search('openModal', 'openRunStory');
                scope.onLoad();
                expect(scope.openRunnerParams).toHaveBeenCalled();
            });
            it('should NOT call openRunnerParams when location.search does NOT contain openModal=openRunStory', function() {
                spyOn(scope, 'openRunnerParams');
                location.search('openModal', 'something_else');
                scope.onLoad();
                expect(scope.openRunnerParams).not.toHaveBeenCalled();
            });
            it('should call startJoyRideOnLoad service', function() {
                spyOn(service, 'startJoyRideOnLoad');
                scope.onLoad();
                expect(service.startJoyRideOnLoad).toHaveBeenCalledWith(location, scope);
            });
        });

        describe('isStoryRunnerSuccess function', function() {
            it('should return true when all statuses are successful, pending or notPerformed', function() {
                var reports = [
                    {steps: [{status: 'successful'}, {status: 'pending'}]},
                    {steps: [{status: 'notPerformed'}]}
                ];
                var result = scope.isStoryRunnerSuccess(reports);
                expect(result).toEqual(true);
            });
            it('should return false when at least one of statuses is NOT successful, pending or notPerformed', function() {
                var reports = [
                    {steps: [{status: 'failed'}, {status: 'pending'}]},
                    {steps: [{status: 'notPerformed'}]}
                ];
                var result = scope.isStoryRunnerSuccess(reports);
                expect(result).toEqual(false);
            });
            it('should set scenarioToggles to an empty array', function() {
                expect(scope.scenarioToggles.length).toEqual(0);
            });
        });

        describe('getReportUrl function', function() {
            it('should return the report URL', function() {
                var reportsId = 123;
                var report = 'myReport';
                var expected = '/api/v1/reporters/get/' + reportsId + '/' + report;
                expect(scope.getReportUrl(reportsId, report)).toEqual(expected);
            });
        });

        describe('revertStory function', function() {
            beforeEach(function() {
                spyOn(scope.storyForm, '$setPristine');
            });
            it('should set the value of story to be the copy of the originalStory', function() {
                scope.story = {value: 'something'};
                scope.originalStory = {value: 'something else'};
                scope.revertStory();
                expect(scope.story).toEqual(scope.originalStory);
            });
            it('should call storyForm.$setPristine function', function() {
                scope.revertStory();
                expect(scope.storyForm.$setPristine).toHaveBeenCalled();
            });
        });

        describe('canRevertStory function', function() {
           it('should return true when story and originalStory are not equal', function() {
               scope.story = {value: 'something'};
               scope.originalStory = {value: 'something else'};
               expect(scope.canRevertStory()).toBe(true);
           });
            it('should return false when story and originalStory are equal', function() {
                scope.story = {value: 'something'};
                scope.originalStory = {value: 'something'};
                expect(scope.canRevertStory()).toBe(false);
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

        describe('expandPanelsTour function', function() {
            it('should call expandPanels function', function() {
                spyOn(scope, 'expandPanels');
                scope.expandPanelsTour(true);
                expect(scope.expandPanels).toHaveBeenCalled();
            });
        });

        describe('changeScenarioToggleTour function', function() {
            it('should call changeScenarioToggle', function() {
                spyOn(scope, 'changeScenarioToggle');
                scope.changeScenarioToggleTour(true);
                expect(scope.changeScenarioToggle).toHaveBeenCalledWith(3);
            });
        });

        describe('toggleScenario function', function() {
            it('should add true to scenarioToggles when it does not exist', function() {
                scope.changeScenarioToggle(3);
                expect(scope.scenarioToggles).toEqual([{scenario: 3, expanded: true}]);
            });
            it('should change scenarioToggles to false when it is set to true', function() {
                scope.scenarioToggles = [
                    {scenario: 1, expanded: true},
                    {scenario: 2, expanded: true}
                ];
                scope.changeScenarioToggle(2);
                expect(scope.scenarioToggles.length).toEqual(2);
                expect(scope.scenarioToggles).toEqual([
                    {scenario: 1, expanded: true},
                    {scenario: 2, expanded: false}
                ]);
            });
            it('should change scenarioToggles to true when it is set to false', function() {
                scope.scenarioToggles = [{scenario: 3, expanded: false}];
                scope.changeScenarioToggle(3);
                expect(scope.scenarioToggles.length).toEqual(1);
                expect(scope.scenarioToggles).toEqual([{scenario: 3, expanded: true}]);
            });
            it('should set expanded to the value of the second argument if defined', function() {
                scope.scenarioToggles = [{scenario: 3, expanded: false}];
                scope.changeScenarioToggle(3, false);
                expect(scope.scenarioToggles).toEqual([{scenario: 3, expanded: false}]);
            });
        });

        describe('getScenarioToggle function', function() {
            it('should return false when expanded is false', function() {
                scope.scenarioToggles = [
                    {scenario: 1, expanded: true},
                    {scenario: 2, expanded: false}
                ];
                expect(scope.getScenarioToggle(2)).toEqual(false);
            });
            it('should return true when expanded is true', function() {
                scope.scenarioToggles = [
                    {scenario: 1, expanded: true},
                    {scenario: 2, expanded: false}
                ];
                expect(scope.getScenarioToggle(1)).toEqual(true);
            });
            it('should return false when scenario does NOT exist', function() {
                scope.scenarioToggles = [
                    {scenario: 1, expanded: true},
                    {scenario: 2, expanded: false}
                ];
                expect(scope.getScenarioToggle(3)).toEqual(false);
            });
        });

        describe('openErrorModal function', function() {
            it('should call openErrorModal service', function() {
                spyOn(service, 'openErrorModal');
                scope.openErrorModal();
                expect(service.openErrorModal).toHaveBeenCalled();
            });
        });

    });

    describe('storiesCtrl controller', function() {

        var httpBackend, location, scope, modal, modalInstance;
        var filesWithoutPath = {status: 'OK', files: 'filesWithoutPath'};
        var deleteStory = {
            "display": true,
            "enable": false,
            "description": ""
        };

        beforeEach(
            inject(function($controller, $httpBackend, $http, $location) {
                scope = {};
                location = $location;
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

        describe('on load', function() {
            it('deleteStory should be added to the scope', function() {
                expect(scope.features.deleteStory).toEqual(deleteStory);
            });
            it('should be called by the controller with the empty path', function() {
                spyOn(service, 'getStories');
                scope.onLoad();
                expect(service.getStories).toHaveBeenCalledWith(scope, '');
            });
            it('should call startJoyRide service', function() {
                spyOn(service, 'startJoyRideOnLoad');
                scope.onLoad();
                expect(service.startJoyRideOnLoad).toHaveBeenCalledWith(location, scope);
            });
        });

        describe('close function', function() {
            it('should call the close function of the modal', function() {
                scope.close();
                expect(modalInstance.close).toHaveBeenCalled();
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

        describe('openStory function', function() {
            var name = 'myStory';
            var rootPath = 'rootPath/';
            beforeEach(function() {
                scope.rootPath = rootPath;
                scope.openStory(name);
            });
            it('should call modal.close', function() {
                expect(modalInstance.close).toHaveBeenCalled();
            });
            it('should change path', function() {
                expect(location.path()).toEqual('/page/stories/view/' + rootPath + name)
            });
        });

        describe('onFinishJoyRide function', function() {
            it('should call onFinishJoyRide service function', function() {
                spyOn(service, 'onFinishJoyRide');
                scope.onFinishJoyRide();
                expect(service.onFinishJoyRide).toHaveBeenCalledWith(scope);
            });
        });

        describe('startJoyRide function', function() {
            it('should call startJoyRide service function', function() {
                var id = 'id';
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

        describe('startJoyRide function', function() {
            var id = 'ID';
            it('should call startJoyRide service', function() {
                spyOn(service, 'startJoyRide');
                scope.startJoyRide(id);
                expect(service.startJoyRide).toHaveBeenCalledWith(id, scope);
            });
        });

        describe('openDirsTour function', function() {
            it('should call openDir with tcbdd and login', function() {
                spyOn(scope, 'openDir');
                scope.openDirsTour(true);
                expect(scope.openDir).toHaveBeenCalledWith('tcbdd/login');
            });
        });

    });

});
