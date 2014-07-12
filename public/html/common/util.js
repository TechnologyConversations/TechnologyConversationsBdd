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
function getCompositesJson(http, fileName) {
    var url = '/groovyComposites/' + fileName;
    return http.get(url, {cache: false}).then(function(response) {
        return response.data;
    }, function() {
        var className = fileName.substring(0, fileName.lastIndexOf('.'));
        return {
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

