var gulp = require('gulp');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var ngAnnotate = require('gulp-ng-annotate');
var sourcemaps = require('gulp-sourcemaps');
var jasmine = require('gulp-jasmine');

gulp.task('js', function() {
   gulp.src(['html/index.js', 'html/**/*.js'])
       .pipe(sourcemaps.init())
       .pipe(concat('app.js'))
       .pipe(ngAnnotate())
       .pipe(uglify())
       .pipe(sourcemaps.write())
       .pipe(gulp.dest('.'))
});

gulp.task('watch', ['js'], function() {
   gulp.watch(['html/**/*.js', '../test/html/**/*Spec*.js'], ['js'])
});