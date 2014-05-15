describe('bodyModule', function() {

    beforeEach(module('bodyModule', 'ngCookies'));

    describe('bodyCtrl controller', function() {

        var scope, cookieStore, location;
        var cookieHistory;
        var historyItem = {text: 'My History Item', url: '/MyUrl'};

        beforeEach(
            inject(function($rootScope, $injector, $controller, $location) {
                cookieHistory = [{text: 'My Cookie History Item 1', url: '/MyCookieUrl1'}, {text: 'My Cookie History Item 2', url: '/MyCookieUrl2'}];
                scope = $rootScope.$new();
                cookieStore = $injector.get('$cookieStore');
                cookieStore.put('history', cookieHistory);
                location = $location;
                $controller("bodyCtrl", {
                    $scope: scope,
                    $cookieStore: cookieStore,
                    $location: location
                });
            })
        );

        describe('addHistoryItem function', function() {
            it('should load history from cookies', function() {
                expect(scope.history.length).toEqual(cookieHistory.length);
            });
            it('should add the history item to the array', function() {
                scope.addHistoryItem(historyItem.text);
                expect(scope.history.length).toEqual(cookieHistory.length + 1);
            });
            it('should add the history item to the cookieStore', function() {
                scope.addHistoryItem(historyItem.text);
                expect(cookieStore.get('history').length).toEqual(cookieHistory.length + 1);
            });
            it('should not allow duplicate history to the array', function() {
                scope.addHistoryItem(historyItem.text);
                scope.addHistoryItem(historyItem.text);
                expect(scope.history.length).toEqual(cookieHistory.length + 1);
            });
            it('should not allow the same text in multiple history items', function() {
                scope.addHistoryItem(historyItem.text);
                scope.addHistoryItem('My History Item');
                expect(scope.history.length).toEqual(cookieHistory.length + 1);
            });
            it('should allow multiple history with different text', function() {
                scope.addHistoryItem(historyItem.text);
                scope.addHistoryItem('My History Item 2');
                expect(scope.history.length).toEqual(cookieHistory.length + 2);
            });
            it('should store both text and url', function() {
                location.path(historyItem.url);
                scope.addHistoryItem(historyItem.text);
                cookieHistory.splice(0, 0, historyItem);
                expect(scope.history).toEqual(cookieHistory);
            });
            it('should insert new history item as the first item in the array', function() {
                scope.addHistoryItem(historyItem.text);
                expect(scope.history[0].text).toEqual(historyItem.text);
            });
            it('should not add more than 10 items', function() {
                for(var index = 1; index < 15; index++) {
                    scope.addHistoryItem('Item ' + index);
                }
                expect(scope.history.length).toEqual(10);
            });
        });

        describe('removeHistoryItem function', function() {
            it('should remove element on specified index', function() {
                scope.removeHistoryItem(0);
                expect(scope.history.length).toEqual(1);
                expect(scope.history[0]).toEqual({text: 'My Cookie History Item 2', url: '/MyCookieUrl2'});
            });
            it('should remove element from cookies', function() {
                scope.removeHistoryItem(0);
                expect(cookieStore.get('history')).toEqual(scope.history);
            });
        });

    });

});