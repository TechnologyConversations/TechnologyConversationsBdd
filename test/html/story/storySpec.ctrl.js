describe('storyModule', function() {

    beforeEach(module('ngCookies', 'storyModule'));

    describe('storyCtrl controller', function() {
        var scope, modal, form, story;
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
                scope.addHistoryItem = function(text) {
                    scope.currentTabText = text;
                };
                story = {
                    name: 'this is a story name',
                    path: 'this/is/path'
                };
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
                form.$invalid = false;
                form.$valid = true;
                scope.storyForm = form;
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
               expect(scope.removeCollectionElement).toEqual(removeCollectionElement);
           });
        });

        describe('getRunnerProgressCss function', function() {
            it('should return active when story is in progress', function() {
                scope.storyRunnerInProgress = true;
                expect(scope.getRunnerProgressCss()).toEqual({
                    'progress progress-striped active': true,
                    'progress': false
                });
            });
            it('should return inactive when story is in progress', function() {
                scope.storyRunnerInProgress = false;
                expect(scope.getRunnerProgressCss()).toEqual({
                    'progress progress-striped active': false,
                    'progress': true
                });
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

        describe('stepEnterKey function', function() {
            it('should use global newCollectionItem function', function() {
                expect(scope.stepEnterKey).toBe(newCollectionItem);
            });
        });

        describe('stepTextPattern function', function() {
            it('should use util function', function() {
                expect(scope.stepTextPattern()).toEqual(stepTextPattern());
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
                expect(scope.buttonCssClass).toEqual(buttonCssClass);
            });
        });

        describe('buttonCssClass function', function() {
            it('should use the global buttonCssClass function', function() {
                expect(scope.buttonCssClass).toEqual(buttonCssClass);
            });
        });

        describe('cssClass function', function() {
            it('should use the global cssClass function', function() {
                expect(scope.cssClass).toEqual(cssClass);
            });
        });

        describe('setAction function', function() {
            it('should set action to POST when new story is opened', function() {
                scope.story.name = '';
                scope.setAction();
                expect(scope.action).toEqual('POST');
            });
            it('should set add tab story tab when existing story is opened', function() {
                scope.setAction();
                expect(scope.currentTabText).toEqual(scope.story.name + ' story');
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
                modalInstance = {
                    dismiss: jasmine.createSpy('modalInstance.dismiss'),
                    close: jasmine.createSpy('modalInstance.close')
                };
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

        describe('ok function', function () {
            it('should close the modal and return data', function() {
                scope.ok();
                expect(modalInstance.close).toHaveBeenCalledWith(data);
            })
        });

    });

});
