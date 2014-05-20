angular.module('storyModule', [])
    .controller('storyCtrl', ['$scope', '$http', '$modal', '$location', '$cookieStore', '$q', '$anchorScroll', 'story', 'steps', 'classes', 'composites',
        function($scope, $http, $modal, $location, $cookieStore, $q, $anchorScroll, story, steps, classes, composites) {
            $scope.setAction = function() {
                $scope.action = $scope.story.name === '' ? 'POST' : 'PUT';
            };
            $scope.expandPanels = function() {
                var scenariosExpanded = true;
                if ($scope.panelsExpanded === undefined) {
                    $scope.panelsExpanded = false;
                } else {
                    $scope.panelsExpanded = !$scope.panelsExpanded;
                    scenariosExpanded = $scope.panelsExpanded;
                }
                $scope.panels = {
                    story: $scope.panelsExpanded,
                    description: $scope.panelsExpanded,
                    meta: $scope.panelsExpanded,
                    narrative: $scope.panelsExpanded,
                    givenStories: $scope.panelsExpanded,
                    lifecycle: $scope.panelsExpanded,
                    scenarios: scenariosExpanded
                };
            };
            $scope.story = story;
            $scope.steps = steps;
            $scope.classes = classes;
            $scope.composites = composites;
            $scope.stepTypes = ['GIVEN', 'WHEN', 'THEN'];
            $scope.storyFormClass = 'col-md-12';
            $scope.storyRunnerVisible = false;
            $scope.storyRunnerInProgress = false;
            $scope.storyRunnerSuccess = true;
            $scope.expandPanels();
            var originalStory = angular.copy(story);
            var pathArray = $scope.story.path.split('/');
            $scope.dirPath = pathArray.slice(0, pathArray.length - 1).join('/');
            if ($scope.dirPath !== '') {
                $scope.dirPath += '/';
            }
            $scope.setAction();
            $scope.cssClass = cssClass;
            $scope.buttonCssClass = buttonCssClass;
            $scope.canSaveStory = function() {
                var isValid = $scope.storyForm.$valid;
                var hasChanged = !angular.equals($scope.story, originalStory);
                return isValid && hasChanged;
            };
            $scope.stepTextPattern = stepTextPattern;
            // TODO Test
            $scope.saveStory = function() {
                if ($scope.canSaveStory()) {
                    $scope.story.path = $scope.dirPath + $scope.story.name + ".story";
                    if ($scope.action === 'POST') {
                        var strippedPathArray = $scope.dirPath.split('/');
                        var strippedPath = strippedPathArray.slice(0, strippedPathArray.length - 1).join('/');
                        if (strippedPath !== '') {
                            strippedPath += '/';
                        }
                        $http.post('/stories/story.json', $scope.story).then(function () {
                            $location.path(getViewStoryUrl() + strippedPath + $scope.story.name);
                            originalStory = angular.copy($scope.story);
                        }, function (response) {
                            openErrorModal($modal, response.data);
                        });
                    } else {
                        if ($scope.story.name !== originalStory.name) {
                            $scope.story.originalPath = originalStory.path;
                        }
                        $http.put('/stories/story.json', $scope.story).then(function () {
                            originalStory = angular.copy($scope.story);
                        }, function (response) {
                            openErrorModal($modal, response.data);
                        });
                    }
                }
            };
            $scope.canRunStory = function () {
                return $scope.storyForm.$valid && !$scope.storyRunnerInProgress;
            };
            // TODO Test
            $scope.runStory = function () {
                if ($scope.canRunStory()) {
                    $scope.saveStory();
                    $scope.openRunnerModal().result.then(function (data) {
                        $scope.storyFormClass = 'col-md-6';
                        $scope.storyRunnerClass = 'col-md-6';
                        $scope.storyRunnerVisible = true;
                        $scope.storyRunnerInProgress = true;
                        data.classes.forEach(function (classEntry) {
                            classEntry.params.forEach(function (paramEntry) {
                                $cookieStore.put(classEntry.fullName + "." + paramEntry.key, paramEntry.value);
                            });
                        });
                        var json = {
                            storyPath: $scope.story.path,
                            classes: data.classes,
                            composites: $scope.composites
                        };
                        $http.post('/runner/run.json', json).then(function (response) {
                            $scope.storyRunnerSuccess = (response.data.status === 'OK');
                            $scope.storyRunnerInProgress = false;
                            $http.get('/reporters/list/' + response.data.id + '.json').then(function (response) {
                                $scope.reports = response.data;
                                $scope.setPendingSteps($scope.reports);
                            }, function (response) {
                                openErrorModal($modal, response.data);
                            });
                        }, function (response) {
                            $scope.storyRunnerSuccess = false;
                            $scope.storyRunnerInProgress = false;
                            openErrorModal($modal, response.data);
                        });
                    }, function () {
                        // Do nothing
                    });
                }
            };
            // TODO Test
            $scope.openRunnerModal = function() {
                return $modal.open({
                    templateUrl: '/assets/html/runner.tmpl.html',
                    controller: 'runnerCtrl',
                    resolve: {
                        data: function() {
                            return $scope.classes;
                        }
                    }
                });
            };
            // TODO Test
            $scope.getRunnerProgressCss = function () {
                return {
                    'progress progress-striped active': $scope.storyRunnerInProgress,
                    'progress': !$scope.storyRunnerInProgress
                };
            };
            // TODO Test
            $scope.getRunnerStatusCss = function () {
                return {
                    'progress-bar progress-bar-info': $scope.storyRunnerInProgress,
                    'progress-bar progress-bar-warning': !$scope.storyRunnerInProgress && $scope.storyRunnerSuccess && $scope.hasPendingSteps(),
                    'progress-bar progress-bar-success': !$scope.storyRunnerInProgress && $scope.storyRunnerSuccess && !$scope.hasPendingSteps(),
                    'progress-bar progress-bar-danger': !$scope.storyRunnerInProgress && !$scope.storyRunnerSuccess
                };
            };
            $scope.setPendingSteps = function(reports) {
                $scope.pendingSteps = [];
                reports.forEach(function(report) {
                    report.steps.forEach(function(step) {
                        if (step.status === 'pending') {
                            $scope.pendingSteps.push({text: step.text});
                        }
                    });
                });
            };
            $scope.hasPendingSteps = function() {
                return $scope.pendingSteps !== undefined && $scope.pendingSteps.length > 0;
            };
            $scope.getStoryRunnerStatusText = function () {
                if ($scope.storyRunnerInProgress) {
                    return 'Story run is in progress';
                } else if ($scope.storyRunnerSuccess) {
                    if ($scope.pendingSteps !== undefined && $scope.pendingSteps.length > 0) {
                        return 'Story run was successful with ' + $scope.pendingSteps.length + " pending steps";
                    } else {
                        return 'Story run was successful';
                    }
                } else {
                    return 'Story run failed';
                }
            };
            $scope.removeCollectionElement = removeCollectionElement;
            // TODO Test
            $scope.addElement = function (collection) {
                collection.push({});
            };
            // TODO Test
            $scope.addScenarioElement = function (collection) {
                collection.push({title: '', meta: [], steps: [], examplesTable: ''});
            };
            // TODO Test
            $scope.revertStory = function () {
                $scope.story = angular.copy(originalStory);
                $scope.storyForm.$setPristine();
            };
            // TODO Test
            $scope.canRevertStory = function () {
                return !angular.equals($scope.story, originalStory);
            };
            // TODO Test
            $scope.canDeleteStory = function () {
                return $scope.action === 'PUT' && !$scope.storyRunnerInProgress;
            };
            // TODO Test
            $scope.deleteStory = function () {
                var path = $scope.dirPath + $scope.story.name + '.story';
                deleteStory($modal, $http, $location, $q, path);
            };
            $scope.stepEnterKey = newCollectionItem;
            // TODO Test
            $scope.clickPendingStep = function(stepText) {
                var compositeClass = $cookieStore.get("compositeClass");
                if (compositeClass === undefined || compositeClass === '') {
                    return openCompositeClass($modal, stepText);
                } else {
                    return $location.search('stepText', stepText).path('/page/composites/composites.com.technologyconversations.bdd.steps.' + compositeClass);
                }
            };
        }
    ])
    .controller('runnerCtrl', ['$scope', '$modalInstance', '$cookieStore', 'data',
        function ($scope, $modalInstance, $cookieStore, data) {
            $scope.data = data;
            $scope.data.classes.forEach(function(classEntry) {
                classEntry.params.forEach(function(paramEntry) {
                    paramEntry.value = $cookieStore.get(classEntry.fullName + "." + paramEntry.key);
                });
            });
            $scope.hasOptions = function(options) {
                if (options)
                  return options.length > 0;
                else
                  return false;
            };
            $scope.hasParams = function(classEntry) {
                return classEntry.params !== undefined && classEntry.params.length > 0;
            };
            // TODO Test
            $scope.ok = function () {
                $modalInstance.close($scope.data);
            };
            // TODO Test
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
            $scope.paramElementId = function(className, paramKey) {
                var formattedClassName = className.charAt(0).toLowerCase() + className.slice(1);
                var formattedParamKey = paramKey.charAt(0).toUpperCase() + paramKey.slice(1);
                return formattedClassName + formattedParamKey;
            };
        }
    ]);
