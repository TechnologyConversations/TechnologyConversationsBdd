angular.module('storyModule', [])
    .controller('storyCtrl', function($scope, $http, $modal, $location, $cookieStore, $q, $anchorScroll, $timeout, story, steps, groovyComposites, TcBddService) {
        $scope.onLoad = function() {
            $scope.pendingSteps = [];
            $scope.story = story;
            $scope.steps = steps;
            $scope.groovyComposites = groovyComposites;
            $scope.stepTypes = ['GIVEN', 'WHEN', 'THEN'];
            $scope.storyFormClass = 'col-md-12';
            $scope.storyRunnerVisible = false;
            $scope.storyRunnerInProgress = false;
            $scope.storyRunnerSuccess = true;
            $scope.expandPanels();
            $scope.originalStory = angular.copy(story);
            $scope.scenarioToggles = [];
            var pathArray = $scope.story.path.split('/');
            $scope.dirPath = pathArray.slice(0, pathArray.length - 1).join('/');
            if ($scope.dirPath !== '') {
                $scope.dirPath += '/';
            }
            $scope.setAction();
            if ($location.search().reportsId !== undefined) {
                $scope.storyFormClass = 'col-md-6';
                $scope.storyRunnerClass = 'col-md-6';
                $scope.storyRunnerVisible = true;
                $scope.getReports($location.search().reportsId);
            }
            if ($location.search().openModal !== undefined) {
                var modal = $location.search().openModal;
                if (modal === 'openRunStory') {
                    $scope.openRunnerParams();
                }
            }
            TcBddService.startJoyRideOnLoad($location, $scope);
        };
        $scope.setAction = function() {
            if ($scope.story.name !== '') {
                $scope.action = 'PUT';
            } else {
                $scope.action = 'POST';
            }
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
        $scope.cssClass = TcBddService.cssClass;
        $scope.buttonCssClass = TcBddService.buttonCssClass;
        $scope.canSaveStory = function() {
            var isValid = $scope.storyForm.$valid;
            var hasChanged = !angular.equals($scope.story, $scope.originalStory);
            return isValid && hasChanged;
        };
        $scope.stepTextPattern = TcBddService.stepTextPattern;
        // TODO Test
        $scope.saveStory = function() {
            if ($scope.canSaveStory()) {
                $scope.story.path = $scope.dirPath + $scope.story.name + ".story";
                if ('POST' === $scope.action) {
                    var strippedPathArray = $scope.dirPath.split('/');
                    var strippedPath = strippedPathArray.slice(0, strippedPathArray.length - 1).join('/');
                    if (strippedPath !== '') {
                        strippedPath += '/';
                    }
                    $http.post('/stories/story.json', $scope.story).then(function () {
                        $location.path('/page/stories/view/' + strippedPath + $scope.story.name);
                        $scope.originalStory = angular.copy($scope.story);
                    }, function (response) {
                        $scope.openErrorModal(response.data);
                    });
                } else {
                    if ($scope.story.name !== $scope.originalStory.name) {
                        $scope.story.originalPath = $scope.originalStory.path;
                    }
                    $http.put('/stories/story.json', $scope.story).then(function () {
                        $scope.originalStory = angular.copy($scope.story);
                    }, function (response) {
                        $scope.openErrorModal(response.data);
                    });
                }
            }
        };
        $scope.canRunStory = function () {
            return $scope.storyForm.$valid && !$scope.storyRunnerInProgress;
        };
        $scope.runStory = function () {
            if ($scope.canRunStory()) {
                $scope.saveStory();
                $scope.openRunnerParams();
            }
        };
        // TODO Test
        $scope.openRunnerParams = function() {
            $scope.openRunnerModal().result.then(function (data) {
                var classes = data.classes;
                $scope.storyFormClass = 'col-md-6';
                $scope.storyRunnerClass = 'col-md-6';
                $scope.storyRunnerVisible = true;
                $scope.storyRunnerInProgress = true;
                classes.forEach(function (classEntry) {
                    classEntry.params.forEach(function (paramEntry) {
                        $cookieStore.put(classEntry.fullName + "." + paramEntry.key, paramEntry.value);
                    });
                });
                var json = {
                    storyPaths: [{path: $scope.story.path}],
                    classes: classes,
                    groovyComposites: $scope.groovyComposites
                };
                $http.post('/runner/run.json', json).then(function (response) {
                    $scope.getReports(response.data.id);
                }, function (response) {
                    $scope.storyRunnerSuccess = false;
                    $scope.storyRunnerInProgress = false;
                    $scope.openErrorModal(response.data);
                });
            }, function(response) {
                $scope.openErrorModal(response.data);
            });
        };
        $scope.getReports = function(reportsId) {
            $http.get('/api/v1/reporters/list/' + reportsId).then(function (response) {
                console.log(reportsId);
                console.log(response.data);
                if (response.data.status !== 'finished') {
                    console.log('111');
                    $scope.storyRunnerInProgress = true;
                    $timeout(function() {
                        $scope.getReports(reportsId);
                    }, 5000);
                } else {
                    console.log('222');
                    var reports = response.data.reports;
                    $scope.reports = reports;
                    $scope.reports.id = reportsId;
                    $scope.setPendingSteps(reports);
                    $scope.storyRunnerInProgress = false;
                    $scope.storyRunnerSuccess = $scope.isStoryRunnerSuccess(reports);
                }
            }, function (response) {
                if (response.data.message === 'ID is NOT correct') {
                    $timeout(function() {
                        $scope.getReports(reportsId);
                    }, 5000);
                } else {
                    $scope.storyRunnerInProgress = false;
                    TcBddService.openErrorModal(response.data);
                }
            });
        };
        $scope.isStoryRunnerSuccess = function(reports) {
            var result = true;
            reports.forEach(function(report) {
                report.steps.forEach(function(step) {
                    if (step.status !== 'successful' && step.status !== 'pending' && step.status !== 'notPerformed') {
                        result = false;
                    }
                });
            });
            return result;
        };
        $scope.getReportUrl = function(reportsId, report) {
            return '/api/v1/reporters/get/' + reportsId + '/' + report;
        };
        $scope.openRunnerModal = function() {
            return TcBddService.openRunnerParametersModal(false, $scope);
        };
        $scope.getRunnerProgressCss = function () {
            return TcBddService.getRunnerProgressCss($scope.storyRunnerInProgress);
        };
        $scope.getRunnerStatusCss = function () {
            return TcBddService.getRunnerStatusCss(
                $scope.storyRunnerInProgress,
                $scope.storyRunnerSuccess,
                ($scope.pendingSteps > 0)
            );
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
            return TcBddService.getStoryRunnerStatusText(
                $scope.storyRunnerInProgress,
                $scope.storyRunnerSuccess,
                $scope.pendingSteps.length
            );
        };
        $scope.removeCollectionElement = TcBddService.removeCollectionElement;
        $scope.addElement = function (collection) {
            collection.push({});
        };
        $scope.addScenarioElement = function (collection) {
            collection.push({title: '', meta: [], steps: [], examplesTable: ''});
        };
        $scope.revertStory = function () {
            $scope.story = angular.copy($scope.originalStory);
            $scope.storyForm.$setPristine();
        };
        $scope.canRevertStory = function () {
            return !angular.equals($scope.story, $scope.originalStory);
        };
        $scope.canDeleteStory = function () {
            return $scope.action === 'PUT' && !$scope.storyRunnerInProgress;
        };
        // TODO Test
        $scope.deleteStory = function () {
            var path = $scope.dirPath + $scope.story.name + '.story';
            TcBddService.deleteStory(path);
        };
        $scope.stepEnterKey = TcBddService.newCollectionItem;
        // TODO Test
        $scope.clickPendingStep = function(stepText) {
            var compositeClass = $cookieStore.get("compositeClass");
            if (compositeClass === undefined || compositeClass === '') {
                return TcBddService.openCompositeClass(stepText);
            } else {
                return $location.search('stepText', stepText).path('/page/composites/' + compositeClass + '.groovy');
            }
        };
        $scope.openErrorModal = function() {
            TcBddService.openErrorModal();
        };
        $scope.changeScenarioToggle = function(scenario) {
            var found = false;
            $scope.scenarioToggles.forEach(function(toggle) {
                if (toggle.scenario === scenario) {
                    toggle.expanded = !toggle.expanded;
                    found = true;
                }
            });
            if (!found) {
                $scope.scenarioToggles.push({scenario: scenario, expanded: true});
            }
        };
        $scope.getScenarioToggle = function(scenario) {
            var expanded = false;
            $scope.scenarioToggles.forEach(function(toggle) {
                if (toggle.scenario === scenario) {
                    expanded = toggle.expanded;
                }
            });
            return expanded;
        };
        $scope.onFinishJoyRide = function() {
            TcBddService.onFinishJoyRide($scope);
        };
        $scope.startJoyRide = function(id) {
            TcBddService.startJoyRide(id, $scope);
        };
        $scope.expandPanelsTour = function(flag) {
            $scope.expandPanels();
        };
        $scope.changeScenarioToggleTour = function(flag) {
            $scope.changeScenarioToggle(3);
        };
        $scope.onLoad();
    })
    .controller('storiesCtrl', function($scope, $http, $modal, $modalInstance, $location, $q, TcBddService, features) {
        TcBddService.getStories($scope, '');
        $scope.features = features;
        $scope.openDir = function(path) {
            TcBddService.openDir($scope, path);
        };
        $scope.close = function() {
            $modalInstance.close();
        };
        $scope.openStory = function(name) {
            $scope.onFinishJoyRide();
            $modalInstance.close();
            $location.path('/page/stories/view/' + $scope.rootPath + name);
        };
        $scope.allowToPrevDir = function() {
            return $scope.rootPath !== '';
        };
        // TODO Test
        $scope.deleteStory = function(name, index) {
            var path = $scope.rootPath + name + '.story';
            TcBddService.deleteStory(path).then(function() {
                $scope.files.stories.splice(index, 1);
            });
        };
        // TODO Test
        $scope.createDirectory = function(path) {
            var json = '{"path": "' + $scope.rootPath + path + '"}';
            $http.post('/stories/dir.json', json).then(function() {
                $scope.files.dirs.push({name: path});
            }, function(response) {
                TcBddService.openErrorModal(response.data);
            });
        };
        $scope.onFinishJoyRide = function() {
            TcBddService.onFinishJoyRide($scope);
        };
        $scope.startJoyRide = function(id) {
            TcBddService.startJoyRide(id, $scope);
        };
    });