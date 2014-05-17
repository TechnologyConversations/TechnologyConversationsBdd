describe('util', function() {

    // TODO
//    describe('newCollectionItem function', function() {
//
//    });

    describe('removeCollectionElement function', function() {
        it('should remove element on specified index', function() {
            var collection = [{item: 0}, {item: 1}, {item: 2}];
            removeCollectionElement(collection, 1);
            expect(collection.length).toEqual(2);
            expect(collection[1]).toEqual({item: 2});
        });
    });

    describe('buttonCssClass function', function() {
        var scope, form;
        beforeEach(
            inject(function ($rootScope, $compile) {
                scope = $rootScope.$new();
                form = $compile('<form>')(scope);
            })
        );
        it('should return success if form is valid', function() {
            form.$invalid = false;
            form.$valid = true;
            expect(buttonCssClass(form)).toEqual({'btn-success': true, 'btn-danger': false});
        });
        it('should return danger if form is valid', function() {
            form.$invalid = true;
            form.$valid = false;
            expect(buttonCssClass(form)).toEqual({'btn-success': false, 'btn-danger': true});
        });
    });

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

});