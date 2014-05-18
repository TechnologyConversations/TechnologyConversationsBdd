describe('runnerModule', function() {

    var scope;

    beforeEach(module('runnerModule'));

    beforeEach(
        inject(function($rootScope) {
            scope = $rootScope.$new();
        })
    );

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
                    stories: [{name: 'story1.story', selected: true}]
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
                    stories: [{name: 'story1.story', selected: true}]
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
                    stories: [{name: 'story1.story', selected: true}, {name: 'story2.story', selected: true}, {name: 'story2.story'}]
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
