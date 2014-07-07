describe('storyModule', function() {

    beforeEach(module('ngCookies', 'storyModule'));

    describe('storyCtrl controller', function() {

        var scope, modal, form, story, httpBackend;
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
            inject(function($rootScope, $controller, $httpBackend, $http, $location, $cookieStore, $compile) {
                scope = $rootScope.$new();
                scope.addHistoryItem = function(text) {
                    scope.currentTabText = text;
                };
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
                    groovyComposites: groovyComposites
                });
                httpBackend = $httpBackend;
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
            it('should use general getStoryRunnerStatusText function', function() {
                scope.pendingSteps = pendingSteps;
                var expected = getStoryRunnerStatusText(
                    scope.storyRunnerInProgress,
                    scope.storyRunnerSuccess,
                    scope.pendingSteps.length);
                expect(scope.getStoryRunnerStatusText()).toEqual(expected);
            });
        });

        describe('removeCollectionElement function', function() {
           it('should call the general removeCollectionElement function', function() {
               expect(scope.removeCollectionElement).toEqual(removeCollectionElement);
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

        describe('getRunnerStatusCss function', function() {
            it('should use general getRunnerStatusCss function', function() {
                var expected = getRunnerStatusCss(
                    scope.storyRunnerInProgress,
                    scope.storyRunnerSuccess,
                    (scope.pendingSteps > 0));
                expect(scope.getRunnerStatusCss()).toEqual(expected);
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

        describe('addScenarioElement function', function() {
            it('should add empty scenario to the collection', function() {
                var scenarios = [];
                scope.addScenarioElement(scenarios);
                expect(scenarios[0]).toEqual({title: '', meta: [], steps: [], examplesTable: ''})
            });
        });

        describe('getReports function', function() {
            var responseData = {data: 'something'};
            var reportsId = 123;
            var url = '/api/v1/reporters/list/' + reportsId;
            beforeEach(function() {
                spyOn(scope, 'setPendingSteps');
                spyOn(scope, 'openErrorModal');
            });
            it('should call GET /reporters/list/[REPORTS_ID]', function() {
                httpBackend.expectGET(url).respond(responseData);
                scope.getReports(reportsId);
                httpBackend.flush();
            });
            it('should assign GET response to reports', function() {
                httpBackend.expectGET(url).respond(responseData);
                scope.getReports(reportsId);
                httpBackend.flush();
                expect(scope.reports).toEqual(responseData);
            });
            it('should call setPendingSteps function', function() {
                httpBackend.expectGET(url).respond(responseData);
                scope.getReports(reportsId);
                httpBackend.flush();
                expect(scope.setPendingSteps).toHaveBeenCalled();
            });
            it('should call openErrorModal in case of bad request', function() {
                httpBackend.expectGET(url).respond(400, responseData);
                scope.getReports(reportsId);
                httpBackend.flush();
                expect(scope.openErrorModal).toHaveBeenCalled();
            });
        });

    });

});
