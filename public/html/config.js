angular.module('configModule', [])
    .config(function($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true);
        $routeProvider
            // TODO Remove duplication in resolve
            .when('/page/stories/new/', {
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
                    groovyComposites: function($http, $modal) {
                        return getJson($http, $modal, '/groovyComposites', true);
                    }
                }
            })
            .when('/page/stories/new/:path*', {
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
                    groovyComposites: function($http, $modal) {
                        return getJson($http, $modal, '/groovyComposites', true);
                    }
                }
            })
            .when('/page/stories/view/:path*', {
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
                    groovyComposites: function($http, $modal) {
                        return getJson($http, $modal, '/groovyComposites', true);
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
                        var fileName = $route.current.params.className;
                        var url = '/groovyComposites/' + fileName;
                        return $http.get(url, {cache: false}).then(function(response) {
                            return response.data;
                        }, function() {
                            var className = fileName.substring(0, fileName.lastIndexOf('.'));
                            return {
                                class: className,
                                composites:[{stepText: '', compositeSteps: [{}]}],
                                isNew: true
                            };
                        });
                    },
                    steps: function($http, $modal) {
                        return getJson($http, $modal, '/steps/list.json', false);
                    }
                }
            })
            .when('/page/runner/', {
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
    });
