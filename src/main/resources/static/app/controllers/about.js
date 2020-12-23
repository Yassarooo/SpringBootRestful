angular.module('NAProject')
// Creating the Angular Controller
    .controller('AboutController', function ($http, $scope, $rootScope) {
        $rootScope.$broadcast('hideload');
    });
