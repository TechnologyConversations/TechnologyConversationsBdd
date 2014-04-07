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
                        story: function($route, $http, $modal) {
                            return getJson($http, $modal, '/stories/story.json', false);
                        },
                        steps: function($route, $http, $modal) {
                            return getJson($http, $modal, '/steps/list.json', true);
                        },
                        classes: function($route, $http, $modal) {
                            return getJson($http, $modal, '/steps/classes.json', true);
                        },
                        composites: function($route, $http, $modal) {
                            return getJson($http, $modal, '/composites', true);
                        }
                    }
                })
                // TODO Test
                .when(getNewStoryUrl() + ':path*', {
                    templateUrl: '/assets/html/story/story.tmpl.html',
                    controller: 'storyCtrl',
                    resolve: {
                        story: function($route, $http, $modal) {
                            return getJson($http, $modal, '/stories/story.json?path=' + $route.current.params.path + '.story', false);
                        },
                        steps: function($route, $http, $modal) {
                            return getJson($http, $modal, '/steps/list.json', true);
                        },
                        classes: function($route, $http, $modal) {
                            return getJson($http, $modal, '/steps/classes.json', true);
                        },
                        composites: function($route, $http, $modal) {
                            return getJson($http, $modal, '/composites', true);
                        }
                    }
                })
                // TODO Test
                .when(getViewStoryUrl() + ':path*', {
                    templateUrl: '/assets/html/story/story.tmpl.html',
                    controller: 'storyCtrl',
                    resolve: {
                        story: function($route, $http, $modal) {
                            return getJson($http, $modal, '/stories/story.json?path=' + $route.current.params.path + '.story', false);
                        },
                        steps: function($route, $http, $modal) {
                            return getJson($http, $modal, '/steps/list.json', true);
                        },
                        classes: function($route, $http, $modal) {
                            return getJson($http, $modal, '/steps/classes.json', true);
                        },
                        composites: function($route, $http, $modal) {
                            return getJson($http, $modal, '/composites', true);
                        }
                    },
                    reloadOnSearch: false
                })
                .when('/page/composites/:className*', {
                    templateUrl: '/assets/html/composites/composites.tmpl.html',
                    controller: 'compositesCtrl',
                    // TODO Test
                    resolve: {
                        compositesClass: function($route, $http) {
                            return getCompositesJson($http, $route.current.params.className);
                        },
                        steps: function($route, $http, $modal) {
                            return getJson($http, $modal, '/steps/list.json', false);
                        }
                    }
                })
                // TODO Test
                .otherwise({
                    redirectTo: '/page/stories/new'
                });
        }
    ]);
