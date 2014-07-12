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

});