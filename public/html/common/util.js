function newCollectionItem(event, collection) {
    if (event.which === 13) {
        collection.push({});
    }
}

function removeCollectionElement(collection, index) {
    collection.splice(index, 1);
}

function buttonCssClass(ngModelController) {
    return {
        'btn-success': ngModelController.$valid,
        'btn-danger': ngModelController.$invalid
    };
}

// TODO Test
function openConfirmationModal($modal, data) {
    return $modal.open({
        templateUrl: '/assets/html/confirmationModal.tmpl.html',
        controller: 'modalCtrl',
        resolve: {
            data: function() {
                return data;
            }
        }
    });
}

// TODO Test
function openCompositeClass($modal, compositeStepText) {
    $modal.open({
        templateUrl: '/assets/html/compositeClasses/compositeClasses.tmpl.html',
        controller: 'compositeClassesCtrl',
        resolve: {
            compositeClasses: function($route, $http, $modal) {
                return getJson($http, $modal, '/composites', false);
            },
            compositeStepText: function() {
                return compositeStepText;
            }
        }
    });
}

// TODO Test
function getCompositesJson(http, fullClassName) {
    var url = '/composites/' + fullClassName;
    return http.get(url, {cache: false}).then(function(response) {
        return response.data;
    }, function() {
        var lastDotIndex = fullClassName.lastIndexOf('.');
        var className = fullClassName.substring(lastDotIndex + 1);
        var packageName = fullClassName.substring(0, lastDotIndex);
        return {
            package: packageName,
            class: className,
            composites:[{stepText: '', compositeSteps: [{}]}],
            isNew: true
        };
    });
}

function stepTextPattern() {
    return (/^(Given|When|Then) .+$/);
}

function getStories($scope, $http, $modal, path) {
    if ($scope.rootPath === undefined) {
        $scope.rootPath = '';
    }
    $http.get('/stories/list.json?path=' + $scope.rootPath + path).then(function(response) {
        $scope.files = response.data;
        if (path !== '') {
            $scope.rootPath += path + '/';
        }
    }, function(response) {
        openErrorModal($modal, response.data);
    });
}

function openDir($scope, $http, $modal, path) {
    if (path === '..') {
        var dirs = $scope.rootPath.split('/');
        $scope.rootPath = dirs.slice(0, dirs.length - 2).join('/');
        if ($scope.rootPath !== '') {
            $scope.rootPath += '/';
        }
        getStories($scope, $http, $modal, '');
    } else {
        getStories($scope, $http, $modal, path);
    }
}