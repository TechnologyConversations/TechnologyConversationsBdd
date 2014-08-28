module.exports = function(config){
    config.set({

        basePath : '../',

        files : [
            'bower_components/jasmine/lib/jasmine-core/jasmine.js',
            'bower_components/jasmine/lib/jasmine-core/jasmine-html.js',
            'bower_components/jasmine/lib/jasmine-core/boot.js',
            'bower_components/jquery/dist/jquery.min.js',
            'bower_components/jquery-ui/ui/minified/jquery-ui.min.js',
            'bower_components/angular/angular.min.js',
            'bower_components/angular-resource/angular-resource.min.js',
            'bower_components/angular-route/angular-route.min.js',
            'bower_components/angular-cookies/angular-cookies.min.js',
            'bower_components/bootstrap/docs/assets/js/bootstrap.min.js',
            'bower_components/angular-bootstrap/ui-bootstrap-tpls.min.js',
            'bower_components/angular-ui-sortable/sortable.min.js',
            'bower_components/angular-mocks/angular-mocks.js',
            'public/html/**/*.js',
            'test/**/*.js'
        ],

        autoWatch : true,

        frameworks: ['jasmine'],

        browsers : ['Chrome'],

        plugins : [
            'karma-chrome-launcher',
            'karma-jasmine',
            'karma-junit-reporter'
        ],

        logLevel: 'LOG_DEBUG',

        junitReporter : {
            outputFile: 'test/output/unit.xml',
            suite: 'unit'
        }

    });
};