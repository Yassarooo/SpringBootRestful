angular.module('NAProject')
// Creating the Angular Controller
    .controller('HomeController', function ($http, $scope, AuthService,$rootScope) {
        $rootScope.$broadcast('hideload');
        $scope.user = AuthService.user;
    });
