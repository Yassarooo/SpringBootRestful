angular.module('NAProject')
    .controller('registrationConfirmController', ['$scope', '$routeParams', function ($http, $scope, $routeParams, $rootScope) {
        $rootScope.$broadcast('hideload');

        $scope.message = "Registration successful ! ";
    }]);
