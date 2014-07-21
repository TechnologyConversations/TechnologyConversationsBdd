describe('loginModule', function() {

    var scope, service;

    beforeEach(module('loginModule', 'storiesModule'));

    describe('loginCtrl controller', function() {

        var form, location;
        var user1 = {username: 'username1', password: 'password1'};
        var user2 = {username: 'username2', password: 'password2'};

        beforeEach(
            inject(function($controller, $compile, $rootScope, $location, TcBddService) {
                service = TcBddService;
                scope = $rootScope.$new();
                location = $location;
                $controller('loginCtrl', {
                    $scope: scope,
                    $location: location
                });
                form = $compile('<form>')(scope);
                form.$invalid = false;
                form.$valid = true;
                scope.loginForm = form;
            })
        );

        describe('init function', function() {
            it('should set users to an empty array', function() {
                scope.init();
                expect(scope.users).toEqual([]);
            });
        });

        describe('cssClass', function() {
            it('should use service function cssClass', function() {
                expect(scope.cssClass).toEqual(service.cssClass);
            });
        });

        describe('buttonCssClass function', function() {
            it('should use the global buttonCssClass function', function() {
                expect(scope.buttonCssClass).toEqual(service.buttonCssClass);
            });
        });

        describe('canLogin function', function() {
            it('should return false when form is invalid', function() {
                form.$valid = false;
                form.$invalid = true;
                expect(scope.canLogin()).toEqual(false);
            });
            it('should return true when form is valid', function() {
                expect(scope.canLogin()).toEqual(true);
            });
        });

        describe('register function', function() {
            it('should add item to the list of users', function() {
                scope.user = user1;
                scope.register();
                expect(scope.users).toEqual([user1]);
                scope.user = user2;
                scope.register();
                expect(scope.users).toEqual([user1, user2]);
            });
            it('should remove values from the user variable', function() {
                scope.user = user1;
                scope.register();
                expect(scope.user).toEqual({});
            });
        });

        describe('delete function', function() {
            beforeEach(function() {
                scope.users = [user1, user2];
                scope.user = user1;
                scope.delete();
            });
            it('should remove item from the list of users', function() {
                expect(scope.users).toEqual([user2]);
            });
            it('should remove values from the user variable', function() {
                expect(scope.user).toEqual({});
            });
        });

        describe('login function', function() {
            it('should redirect the the loginWelcome screen', function() {
                scope.login();
                expect(location.path()).toEqual('/page/loginWelcome')
            });
        });

    });

});
