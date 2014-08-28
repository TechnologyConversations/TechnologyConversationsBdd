module.exports = function(grunt) {

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        jasmine: {
            test: {
                src: [
                    'bower_components/jasmine/lib/jasmine-core/jasmine.js',
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
                    'bower_components/ng-joyride/ng-joyride.js',
                    'public/html/**/*.js'
                ],
                options: {
                    specs: 'test/html/**/*Spec*.js',
                    display: 'short',
                    summary: true
                }
            }
        },
        watch: {
            files: ['public/html/**/*.js', 'test/html/**/*Spec*.js'],
            tasks: ['jasmine']
        }
  });

    grunt.loadNpmTasks('grunt-contrib-jasmine');
    grunt.loadNpmTasks('grunt-contrib-watch');

    grunt.registerTask('default', ['jasmine']);

};