angular.module('configModule', [])
    .config(['$routeProvider', '$locationProvider',
        function($routeProvider, $locationProvider) {
            $locationProvider.html5Mode(true);
            $routeProvider
                // TODO Remove duplication in resolve
                .when(getNewStoryUrl(), {
                    templateUrl: '/assets/html/story/story.tmpl.html',
                    controller: 'storyCtrl',
                    // TODO Test
                    resolve: {
                        story: function($http, $modal) {
                            return getJson($http, $modal, '/stories/story.json', false);
                        },
                        steps: function($http, $modal) {
                            return getJson($http, $modal, '/steps/list.json', true);
                        },
                        composites: function($http, $modal) {
                            return getJson($http, $modal, '/composites', true);
                        }
                    }
                })
                .when(getNewStoryUrl() + ':path*', {
                    templateUrl: '/assets/html/story/story.tmpl.html',
                    controller: 'storyCtrl',
                    // TODO Test
                    resolve: {
                        story: function($route, $http, $modal) {
                            return getJson($http, $modal, '/stories/story.json?path=' + $route.current.params.path + '.story', false);
                        },
                        steps: function($http, $modal) {
                            return getJson($http, $modal, '/steps/list.json', true);
                        },
                        composites: function($http, $modal) {
                            return getJson($http, $modal, '/composites', true);
                        }
                    }
                })
                .when(getViewStoryUrl() + ':path*', {
                    templateUrl: '/assets/html/story/story.tmpl.html',
                    controller: 'storyCtrl',
                    // TODO Test
                    resolve: {
                        story: function($route, $http, $modal) {
                            return getJson($http, $modal, '/stories/story.json?path=' + $route.current.params.path + '.story', false);
                        },
                        steps: function($http, $modal) {
                            return getJson($http, $modal, '/steps/list.json', true);
                        },
                        composites: function($http, $modal) {
                            return getJson($http, $modal, '/composites', true);
                        }
                    },
                    reloadOnSearch: false
                })
                .when(getCompositesUrl() + ':className*', {
                    templateUrl: '/assets/html/composites/composites.tmpl.html',
                    controller: 'compositesCtrl',
                    // TODO Test
                    resolve: {
                        compositesClass: function($route, $http) {
                            return getCompositesJson($http, $route.current.params.className);
                        },
                        steps: function($http, $modal) {
                            return getJson($http, $modal, '/steps/list.json', false);
                        }
                    }
                })
                .when('/page/runner', {
                    templateUrl: '/assets/html/runner/runner.tmpl.html',
                    controller: 'runnerCtrl',
                    // TODO Test
                    resolve: {
                        data: function($route) {
                            return {reportsPath: $route.current.params.path};
                        }
                    }
                })
                // TODO Test
                .otherwise({
                    redirectTo: '/page/stories/new'
                });
        }
    ]);
