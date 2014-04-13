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

});