describe('util', function() {

    describe('stepTextPattern function', function() {
        it('should start with Given, When or Then', function() {
            expect('Given something').toMatch(stepTextPattern());
            expect('When something').toMatch(stepTextPattern());
            expect('Then something').toMatch(stepTextPattern());
            expect('Give me something').not.toMatch(stepTextPattern());
        });
    });

    describe('getStories function', function() {
        var scope, http, modal, httpBackend;
        var filesWithPath = {status: 'OK', files: 'filesWithPath'};
        beforeEach(
            inject(function ($rootScope, $http, $httpBackend) {
                scope = $rootScope.$new();
                http = $http;
                httpBackend = $httpBackend;
            })
        );
        it('should update files with data returned from the server', function () {
            httpBackend.expectGET('/stories/list.json?path=my_path').respond(filesWithPath);
            getStories(scope, http, modal, 'my_path');
            httpBackend.flush();
            expect(scope.files).toEqual(filesWithPath);
        });
    });

    describe('openDir function', function() {
        var scope, http, modal, httpBackend;
        var filesWithPath = {status: 'OK', files: 'filesWithPath'};
        beforeEach(
            inject(function ($rootScope, $http, $httpBackend) {
                scope = $rootScope.$new();
                http = $http;
                httpBackend = $httpBackend;
            })
        );
        it('should call getStories for the parent dir when path is ".."', function() {
            httpBackend.expectGET('/stories/list.json?path=this/is/').respond(filesWithPath);
            scope.rootPath = "this/is/dir/";
            openDir(scope, http, modal, '..');
            httpBackend.flush();
            expect(scope.files).toEqual(filesWithPath);
        });
        it('should call getStories for the path', function() {
            httpBackend.expectGET('/stories/list.json?path=this/is/dir').respond(filesWithPath);
            scope.rootPath = "this/is/";
            openDir(scope, http, modal, 'dir');
            httpBackend.flush();
            expect(scope.files).toEqual(filesWithPath);
        });
    });

    describe('getRunnerStatusCss function', function() {
        var inProgress;
        var success;
        var pendingSteps;
        beforeEach(function() {
            inProgress = false;
            success = false;
            pendingSteps = false;
        });
        it('should return info if story runner is in progress', function() {
            inProgress = true;
            expect(getRunnerStatusCss(inProgress, success, pendingSteps)).toEqual({
                'progress-bar progress-bar-info': true,
                'progress-bar progress-bar-warning': false,
                'progress-bar progress-bar-success': false,
                'progress-bar progress-bar-danger': false
            });
        });
        it('should return warning if story runner finished and has pending steps', function() {
            success = true;
            pendingSteps = true;
            expect(getRunnerStatusCss(inProgress, success, pendingSteps)).toEqual({
                'progress-bar progress-bar-info': false,
                'progress-bar progress-bar-warning': true,
                'progress-bar progress-bar-success': false,
                'progress-bar progress-bar-danger': false
            });
        });
        it('should return success if story runner finished', function() {
            success = true;
            expect(getRunnerStatusCss(inProgress, success, pendingSteps)).toEqual({
                'progress-bar progress-bar-info': false,
                'progress-bar progress-bar-warning': false,
                'progress-bar progress-bar-success': true,
                'progress-bar progress-bar-danger': false
            });
        });
        it('should return danger if story runner finished and is not success', function() {
            expect(getRunnerStatusCss(inProgress, success, pendingSteps)).toEqual({
                'progress-bar progress-bar-info': false,
                'progress-bar progress-bar-warning': false,
                'progress-bar progress-bar-success': false,
                'progress-bar progress-bar-danger': true
            });
        });
    });

    describe('getStoryRunnerStatusText function', function() {
        var inProgress;
        var success;
        var pendingStepsLength;
        beforeEach(function() {
            inProgress = false;
            success = false;
            pendingStepsLength = 0;
        });
        it('should return "Story run is in progress" when story runner is in progress', function() {
            inProgress = true;
            expect(getStoryRunnerStatusText(inProgress, success, pendingStepsLength)).toEqual('Story run is in progress');
        });
        it('should return "Story run was successful with pending steps" when story runner is NOT in progress, status is success and there are pending steps', function() {
            success = true;
            pendingStepsLength = 2;
            expect(getStoryRunnerStatusText(inProgress, success, pendingStepsLength)).toEqual('Story run was successful with 2 pending steps');
        });
        it('should return "Story run was successful" when story runner is NOT in progress, status is success and there are NO pending steps', function() {
            success = true;
            expect(getStoryRunnerStatusText(inProgress, success, pendingStepsLength)).toEqual('Story run was successful');
        });
        it('should return "Story run failed" when story runner is NOT in progress and status is NOT success', function() {
            expect(getStoryRunnerStatusText(inProgress, success, pendingStepsLength)).toEqual('Story run failed');
        });
    });

});