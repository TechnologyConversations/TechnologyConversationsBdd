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