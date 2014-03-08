require.config({
    paths: {
        "jquery": "//ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min",
        "angular": "//ajax.googleapis.com/ajax/libs/angularjs/1.2.12/angular.min.js",
        "angular-resource-min": "libs/angular-resource.min.js",
        "angular-route-min": "libs/angular-route.min.js",
        "boostrap-min": "libs/bootstrap.min.js",
        "jquery-ui-min": "libs/jquery-ui.min.js",
        "sortable": "libs/sortable",
        "ui-bootstrap-tpls-min": "libs/ui-bootstrap-tpls-0.10.0.min.js",
        "index": "index.js"
    }
});

require(["angular", "jquery", "angular-resource-min",
        "angular-route-min", "bootstrap-min", "jquery-ui-min",
        "sortable", "ui-bootstrap-tpls-min",

        "index"],function() {
});