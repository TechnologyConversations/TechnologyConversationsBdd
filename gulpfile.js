var gulp = require('gulp');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var ngAnnotate = require('gulp-ng-annotate');
var sourcemaps = require('gulp-sourcemaps');
var jasmine = require('gulp-jasmine');
var jshint = require('gulp-jshint');
require('jshint-stylish');
require('gulp-grunt')(gulp);

gulp.task('default', ['test', 'js']);

gulp.task('js', function() {
   gulp.src(['public/html/index.js', 'public/html/**/*.js'])
       .pipe(jshint())
       .pipe(jshint.reporter('jshint-stylish'))
       .pipe(sourcemaps.init())
       .pipe(concat('app.js'))
       .pipe(ngAnnotate())
       .pipe(uglify())
       .pipe(sourcemaps.write())
       .pipe(gulp.dest('.'));
});

gulp.task('test', function() {
    gulp.run('grunt-jasmine');
});

gulp.task('watch', ['test', 'js'], function() {
   gulp.watch(['public/html/**/*.js', 'test/html/**/*Spec*.js'], ['test', 'js']);
});