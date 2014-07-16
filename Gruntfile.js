module.exports = function(grunt) {

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        jasmine: {
            test: {
                src: [
                    'public/bower_components/jasmine/lib/jasmine-core/jasmine.js',
                    'public/bower_components/jasmine/lib/jasmine-core/jasmine-html.js',
                    'public/bower_components/jasmine/lib/jasmine-core/boot.js',
                    'public/bower_components/jquery/dist/jquery.min.js',
                    'public/bower_components/jquery-ui/ui/minified/jquery-ui.min.js',
                    'public/bower_components/angular/angular.min.js',
                    'public/bower_components/angular-resource/angular-resource.min.js',
                    'public/bower_components/angular-route/angular-route.min.js',
                    'public/bower_components/angular-cookies/angular-cookies.min.js',
                    'public/bower_components/bootstrap/docs/assets/js/bootstrap.min.js',
                    'public/bower_components/angular-bootstrap/ui-bootstrap-tpls.min.js',
                    'public/bower_components/angular-ui-sortable/sortable.min.js',
                    'public/bower_components/angular-mocks/angular-mocks.js',
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