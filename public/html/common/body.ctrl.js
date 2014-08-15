angular.module('bodyModule', ['ngJoyRide'])
    .controller('bodyCtrl', function ($scope) {
        $scope.startJoyRideFlag = false;
        $scope.configJoyRide = [
            {
                type: 'element',
                selector: '#menu',
                heading: 'Navigation',
                text: 'The main purpose of the navigation menu is to provide quick links to major sections of the application as well as the information about the current location.',
                placement: 'bottom',
                scroll: true
            }, {
                type: 'element',
                selector: '#browseStories',
                heading: 'Browse Stories',
                text: 'Displays the dialog that can be used to open an existing or create a new story.',
                placement: 'bottom',
                scroll: true
            }, {
                type: 'element',
                selector: '#browseComposites',
                heading: 'Browse Composites',
                text: 'Displays the dialog that can be used to open an existing or create a new composites class.',
                placement: 'bottom',
                scroll: true
            }, {
                type: 'element',
                selector: '#runner',
                heading: 'Run Stories',
                text: 'Displays the dialog that can be used to select one or more stories and directories with stories that will be run.',
                placement: 'bottom',
                scroll: true
//            }, {
//                type: 'element',
//                selector: '#menu',
//                heading: 'Navigation',
//                text: 'Please use one of the links from this menu. When dialog is opened, you will find the XXX button that can be used to continue the tour.',
//                placement: 'bottom',
//                scroll: true
            }, {
                type: 'element',
                selector: '#menu',
                heading: 'Navigation',
                text: 'This tour is still under construction. More will be added soon.',
                placement: 'bottom',
                scroll: true
            }
        ];
        $scope.onFinishJoyRide = function() {
            console.log('TOUR FINISHED');
        };
        $scope.startJoyRide = function() {
            $scope.startJoyRideFlag = true;
        };
    });
