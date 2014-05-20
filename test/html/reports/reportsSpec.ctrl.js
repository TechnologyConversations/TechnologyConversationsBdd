describe('reportsModule', function() {

    beforeEach(module('reportsModule'));

    describe('reportsCtrl controller', function() {
        var scope;
        var pendingSteps = [
            "Given Web user is in the Browse Stories dialog",
            "Given something else"
        ];

        beforeEach(
            inject(function($rootScope, $controller) {
                scope = $rootScope.$new();
                $controller("reportsCtrl", {
                    $scope: scope
                });
            })
        );

        describe('by default', function() {
            it('should have storyRunnerInProgress set to true', function() {
                expect(scope.storyRunnerInProgress).toEqual(true);
            });
            it('should have storyRunnerSuccess set to true', function() {
                expect(scope.storyRunnerSuccess).toEqual(false);
            });
            it('should have pendingSteps to be an empty array', function() {
                expect(scope.pendingSteps.length).toEqual(0);
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

});
