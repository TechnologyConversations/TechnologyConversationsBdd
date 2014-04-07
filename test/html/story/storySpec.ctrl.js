describe('storyModule', function() {

    beforeEach(module('ngCookies', 'storyModule'));

    describe('storyCtrl controller', function() {
        var scope, modal, form;
        var story = {
            path: 'this/is/path'
        };
        var steps = {status: 'OK'};
        var classes = {status: 'OK'};
        var composites = {status: 'OK'};
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
            inject(function($rootScope, $controller, $http, $location, $cookieStore, $compile) {
                scope = $rootScope.$new();
                $controller("storyCtrl", {
                    $scope: scope,
                    $http: $http,
                    $modal: modal,
                    $location: $location,
                    $cookieStore: $cookieStore,
                    story: story,
                    steps: steps,
                    classes: classes,
                    composites: composites
                });
                form = $compile('<form>')(scope);
            })
        );

        describe('by default', function() {
            it('should put steps to the scope', function() {
                expect(scope.steps).toEqual(steps);
            });
            it('should put story to the scope', function() {
                expect(scope.story).toEqual(story);
            });
            it('should put classes to the scope', function() {
                expect(scope.classes).toEqual(classes);
            });
            it('should put composites to the scope', function() {
                expect(scope.composites).toEqual(composites);
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
        });

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
            it('should return "Story run is in progress" when story runner is in progress', function() {
                scope.storyRunnerInProgress = true;
                expect(scope.getStoryRunnerStatusText()).toEqual('Story run is in progress');
            });
            it('should return "Story run was successful with pending steps" when story runner is NOT in progress, status is success and there are pending steps', function() {
                scope.storyRunnerInProgress = false;
                scope.storyRunnerSuccess = true;
                scope.pendingSteps = pendingSteps;
                expect(scope.getStoryRunnerStatusText()).toEqual('Story run was successful with 2 pending steps');
            });
            it('should return "Story run was successful" when story runner is NOT in progress, status is success and there are NO pending steps', function() {
                scope.storyRunnerInProgress = false;
                scope.storyRunnerSuccess = true;
                expect(scope.getStoryRunnerStatusText()).toEqual('Story run was successful');
            });
            it('should return "Story run failed" when story runner is NOT in progress and status is NOT success', function() {
                scope.storyRunnerInProgress = false;
                scope.storyRunnerSuccess = false;
                expect(scope.getStoryRunnerStatusText()).toEqual('Story run failed');
            });
        });

        describe('removeCollectionElement function', function() {
           it('should call the general removeCollectionElement function', function() {
               scope.removeCollectionElement = removeCollectionElement;
           });
        });

        describe('getRunnerStatusCss function', function() {
            it('should return info if story runner is in progress', function() {
                scope.storyRunnerInProgress = true;
                expect(scope.getRunnerStatusCss()).toEqual({
                    'progress-bar progress-bar-info': true,
                    'progress-bar progress-bar-warning': false,
                    'progress-bar progress-bar-success': false,
                    'progress-bar progress-bar-danger': false
                });
            });
            it('should return warning if story runner finished and has pending steps', function() {
                scope.storyRunnerInProgress = false;
                scope.storyRunnerSuccess = true;
                scope.pendingSteps = pendingSteps;
                expect(scope.getRunnerStatusCss()).toEqual({
                    'progress-bar progress-bar-info': false,
                    'progress-bar progress-bar-warning': true,
                    'progress-bar progress-bar-success': false,
                    'progress-bar progress-bar-danger': false
                });
            });
            it('should return success if story runner finished', function() {
                scope.storyRunnerInProgress = false;
                scope.storyRunnerSuccess = true;
                expect(scope.getRunnerStatusCss()).toEqual({
                    'progress-bar progress-bar-info': false,
                    'progress-bar progress-bar-warning': false,
                    'progress-bar progress-bar-success': true,
                    'progress-bar progress-bar-danger': false
                });
            });
            it('should return danger if story runner finished and is not success', function() {
                scope.storyRunnerInProgress = false;
                scope.storyRunnerSuccess = false;
                expect(scope.getRunnerStatusCss()).toEqual({
                    'progress-bar progress-bar-info': false,
                    'progress-bar progress-bar-warning': false,
                    'progress-bar progress-bar-success': false,
                    'progress-bar progress-bar-danger': true
                });
            });
        });

    });

    describe('runnerCtrl controller', function() {

        var modalInstance, data, cookieStore, scope;
        var cookieValue = 'value1';

        beforeEach(
            inject(function($rootScope, $injector, $controller) {
                scope = $rootScope.$new();
                cookieStore = $injector.get('$cookieStore');
                cookieStore.put(data.classes[0].fullName + "." + data.classes[0].params[0].key, cookieValue);
                cookieStore.put(data.classes[0].fullName + "." + data.classes[0].params[1].key, 'value2');
                $controller("runnerCtrl", {
                    $scope: scope ,
                    $modalInstance: modalInstance,
                    $cookieStore: cookieStore,
                    data: data});
            }),
            data = {
                classes: [{
                    fullName: 'full.name.of.the.class',
                    params: [{key: 'key1'}, {key: 'key2'}]
                }]
            }
        );

        describe('by default', function() {
            it('should put data to the scope', function() {
                expect(scope.data).toBe(data);
            });
            it('should put values from cookies', function() {
                expect(scope.data.classes[0].params[0].value).toEqual(cookieValue);
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

        describe('runStory function', function() {
            it('', function() {
                form.$invalid = true;
                form.$valid = false;
            });
        });

    });

});
